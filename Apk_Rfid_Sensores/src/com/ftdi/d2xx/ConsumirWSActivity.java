package com.ftdi.d2xx;

//se produce un error al volver a ingresar debido al tamaño de la imagen
//URL
//"http://www.cheesejedi.com/rest_services/get_big_cheese.php?puzzle=1");
//"http://172.17.19.179:8080/WebApp1/resources/controlar.tablauno");

import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;
import com.ftdi.D2xx;
import com.ftdi.D2xx.D2xxException;


public class ConsumirWSActivity extends Activity implements SensorEventListener {

	// Módulo Rfid
	public int devCount = 0;
	public int timeOutRead = 2000; // 2000
	public int repeatIntento = 500; // 500
	public byte[] InData = new byte[13];
	public Handler handler = new Handler();
	public int rxq = 0;
	public String etiqueta="";

	// Ampliar y mover mapa
	private static final String TAG = "Touch";

	float oldDist = 1f;
	PointF start = new PointF();
	PointF mid = new PointF();
	Matrix matrix = new Matrix();
	Matrix savedMatrix = new Matrix();

	// Variables para Touch Event
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	int mode = NONE;

	// Lectura de notacion Json
	JSONArray jArray = null;
	String StringJson;
	String Item = "";
	String S1 = "";
	String S2 = "";
	String S3 = "";
	String S4 = "";
	String S5 = "";
	String S6 = "";
	String S7 = "";
	String S8 = "";
	TextView Sensor1, Sensor2, Sensor3, Sensor4, Sensor5, Sensor6, Sensor7,
			Sensor8, Item2;

	// Variables sensores
	private SensorManager mSensorManager;
	Sensor accelerometer;
	Sensor magnetometer;
	Float azimut;
	Float giro;
	int grados;

	// Dibujar imagenes
	Panel Fondo;
	Bitmap imagen, imagen1, alerta1, alerta2;
	Canvas canvas = null;
	float escala = 0;
	private int icon_nav_X = 350;
	private int icon_nav_Y = 741;


	// Timers
	private Timer timer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Fondo = new Panel(this);
		setContentView(Fondo);

		// ModuloRFID
		try {
			D2xx.setVIDPID(0x0403, 0xada1);
		} catch (D2xxException e) {

		}

		// **variables_acelerómetro***//
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		accelerometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		magnetometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

		// ***********************funciones****************************//
		iniciarLectura(); // iniciar timer para el lector json
		BuscarDisp();

	}

	protected void Hilo() {
		Thread tt = new Thread() {
			public void run() {
				handler.post(runnable1);
			}
		};
		tt.start();
	}

	private Runnable runnable1 = new Runnable() {
		public void run() {
			new Intento1().execute();
			handler.postDelayed(runnable1, repeatIntento);
		}
	};

	public void BuscarDisp() {

		// get device information - Acción del infoButton
		try {
			devCount = 0;
			devCount = D2xx.createDeviceInfoList();
			if (devCount > 0) {
				Toast.makeText(getApplicationContext(), "RFID conectado",
						Toast.LENGTH_SHORT).show();
				Hilo();
			} else {
				// showDialog(DIALOG_ALERT);
			}

		} // fin del try
		catch (D2xxException e) {
			String s = e.getMessage();
			if (s != null) {

			}
		} // fin de catch

	}

	public void iniciarLectura() {
		// consultar las notificaciones Json desde el serivor
		// cada 15 s.
		try {
			this.timer = new Timer();
			this.timer.scheduleAtFixedRate(new TimerTask() {
				public void run() {
					new LongRunningGetIO().execute();
					// System.out.println(S1);

				}
			}, 500, 15000);
		} catch (Exception e) {
		}
	} // fin de iniciarAlarma*/

	public void pausarLectura() {
		this.timer.cancel();

	}

	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, accelerometer,
				SensorManager.SENSOR_DELAY_UI);
		mSensorManager.registerListener(this, magnetometer,
				SensorManager.SENSOR_DELAY_UI);
	}

	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
		handler.removeCallbacks(runnable1); // stop thread Hilo y Runnable
		pausarLectura();
		escala = 0;
		finish();

	}

	private class Intento1 extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... string1) {
			D2xx ftD2xx = new D2xx();
			try {
				// open the port, send/receive data and close port
				ftD2xx.openByIndex(0);
				ftD2xx.setBitMode((byte) 0, D2xx.FT_BITMODE_RESET);
				ftD2xx.setBaudRate(9600);
				ftD2xx.setDataCharacteristics(D2xx.FT_DATA_BITS_8,
						D2xx.FT_STOP_BITS_1, D2xx.FT_PARITY_NONE);
				ftD2xx.setFlowControl(D2xx.FT_FLOW_NONE, (byte) 0x11,
						(byte) 0x13);
				ftD2xx.setLatencyTimer((byte) 16);
				ftD2xx.setTimeouts(timeOutRead, 0); // Lee bien con poquisimos
													// errores
				ftD2xx.purge((byte) (D2xx.FT_PURGE_TX | D2xx.FT_PURGE_RX));

				int rxq = 13;
				// read the data back!
				ftD2xx.read(InData, rxq);
				ftD2xx.resetDevice(); // Reset Device
				ftD2xx.close();
			} // fin try
			catch (D2xxException e) {
				String s = e.getMessage();
				if (s != null) {
					// myData.setText(s);
				}
			} // Fin catch
			return new String(InData);
		}

		// @Override
		protected void onPostExecute(String string2) {
			// tv1.append(string2);
			etiqueta=(string2); // on UI thread
		}

	}

	private class LongRunningGetIO extends AsyncTask<Void, Void, String> {
		protected String getASCIIContentFromEntity(HttpEntity entity)
				throws IllegalStateException, IOException {
			InputStream in = entity.getContent();
			StringBuffer out = new StringBuffer();
			int n = 1;
			while (n > 0) {
				byte[] b = new byte[4096];
				n = in.read(b);
				if (n > 0)
					out.append(new String(b, 0, n));
			}
			return out.toString();
		}

		@Override
		protected String doInBackground(Void... params) {
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			HttpGet httpGet = new HttpGet(
					"http://192.168.10.15:8080/Prime1/resources/loginbeanpack.dbalertas");
			String text = null;
			try {
				HttpResponse response = httpClient.execute(httpGet,
						localContext);
				HttpEntity entity = response.getEntity();
				text = getASCIIContentFromEntity(entity);
			} catch (Exception e) {
				return e.getLocalizedMessage();
			}
			return text;
		}

		protected void onPostExecute(String results) {
			if (results != null) {

				StringJson = results;

				try {
					jArray = new JSONArray(StringJson);

					for (int i = 0; i < jArray.length(); i++) {

						JSONObject e = jArray.getJSONObject(i);

						// Item.setText("ITEM: " + e.get("item"));

						Item = e.getString("item");
						S1 = e.getString("s1");
						S2 = e.getString("s2");
						S3 = e.getString("s3");
						S4 = e.getString("s4");
						S5 = e.getString("s5");
						S6 = e.getString("s6");
						S7 = e.getString("s7");
						S8 = e.getString("s8");

						if (S1.contains("FUEGO")) {

							Toast.makeText(getApplicationContext(),
									"ALARMA en Sector 1", Toast.LENGTH_SHORT)
									.show();

						} else {

						}

						if (S2.contains("FUEGO")) {

							Toast.makeText(getApplicationContext(),
									"ALARMA en Sector 2", Toast.LENGTH_SHORT)
									.show();

						} else {

						}

						if (S3.contains("FUEGO")) {

							Toast.makeText(getApplicationContext(),
									"ALARMA en Sector 3", Toast.LENGTH_SHORT)
									.show();

						} else {

						}

						if (S4.contains("FUEGO")) {

							Toast.makeText(getApplicationContext(),
									"ALARMA en Sector 3", Toast.LENGTH_SHORT)
									.show();

						} else {

						}
						if (S5.contains("FUEGO")) {

							Toast.makeText(getApplicationContext(),
									"ALARMA en Sector 3", Toast.LENGTH_SHORT)
									.show();

						} else {

						}
						if (S6.contains("FUEGO")) {

							Toast.makeText(getApplicationContext(),
									"ALARMA en Sector 3", Toast.LENGTH_SHORT)
									.show();

						} else {

						}
						if (S7.contains("FUEGO")) {

							Toast.makeText(getApplicationContext(),
									"ALARMA en Sector 3", Toast.LENGTH_SHORT)
									.show();

						} else {

						}

						if (S8.contains("FUEGO")) {

							Toast.makeText(getApplicationContext(),
									"ALARMA en Sector 3", Toast.LENGTH_SHORT)
									.show();

						} else {

						}

					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}
	}

	class Panel extends SurfaceView implements SurfaceHolder.Callback {
		// private GraficarThread thread1;
		private ViewThread mThread;

		public Panel(Context context) {
			super(context);
			imagen = BitmapFactory.decodeResource(getResources(),
					R.drawable.plano_1024x900); // *produce un error al volver
												// a
			// cargar la imagen debid0 al tamaño
			// de la misma

			/*
			 * alerta1 = BitmapFactory.decodeResource(getResources(),
			 * R.drawable.alertas1);
			 * 
			 * alerta2 = BitmapFactory.decodeResource(getResources(),
			 * R.drawable.alertas2);
			 */

			imagen1 = BitmapFactory.decodeResource(getResources(),
					R.drawable.icon_nav);

			// imagen1 = imagen;
			getHolder().addCallback(this);
			mThread = new ViewThread(this);
		}

		public void doDraw(Canvas canvas) {

			escala = 2.7f;
			canvas.drawColor(Color.BLACK);

			Paint p = new Paint();
			p.setColor(Color.GREEN);
			p.setAntiAlias(true);
			p.setTextSize(20);

			Paint p_alertas = new Paint();
			p_alertas.setARGB(254, 254, 100, 254);
			p_alertas.setAntiAlias(true);
			p_alertas.setTextSize(50);

			if (azimut != null)
				canvas.save();
			// canvas.rotate(grados, icon_nav_X, icon_nav_Y);

			// canvas.scale(escala, escala);
			canvas.drawBitmap(imagen, matrix, p);
			canvas.rotate(grados - 180);
			canvas.drawBitmap(imagen1, matrix, p);
			// canvas.drawRect(100, 100, 600, 800, p_alertas);

			// float[] values = null;

			// System.out.println(matrix.toShortString());
			canvas.restore();

			//

			/*
			 * if (S2.contentEquals("Fuego")) { // canvas.drawBitmap(alerta1,
			 * icon_nav_X - 30, icon_nav_Y - 30, // p);
			 * canvas.drawText("Alerta Sector1", icon_nav_X - 10, icon_nav_Y -
			 * 30, p); }
			 * 
			 * else { } if (S2.contentEquals("FUEGO"))
			 * canvas.drawText("Alerta Sector2", icon_nav_X - 10, icon_nav_Y -
			 * 90, p); else { }
			 */

			canvas.save();
			canvas.rotate(grados - 180, icon_nav_X, icon_nav_Y);
			canvas.drawBitmap(imagen1, icon_nav_X - (imagen1.getWidth() / 2),
					icon_nav_Y - (imagen1.getHeight() / 2), p);

			canvas.restore();
			
			canvas.drawText(etiqueta, icon_nav_X, icon_nav_Y - 0, p_alertas);

			// canvas.drawText("ALTO : "+String.valueOf(imagen.getHeight()),
			// icon_nav_X, icon_nav_Y-90, p);
			// canvas.drawText("ANCHO: "+String.valueOf(imagen.getWidth()),
			// icon_nav_X, icon_nav_Y-110, p);
			// canvas.drawText(String.valueOf(ancho), 390, 350, p);

			// canvas.drawRect(200, 500, 400, 700, p);
			// invalidate();
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			// Handle touch events here...
			// dumpEvent(event);

			// Handle touch events here...
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
				savedMatrix.set(matrix);
				start.set(event.getX(), event.getY());
				Log.d(TAG, "mode=DRAG");
				mode = DRAG;
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				oldDist = spacing(event);
				Log.d(TAG, "oldDist=" + oldDist);
				if (oldDist > 10f) {
					savedMatrix.set(matrix);
					midPoint(mid, event);
					mode = ZOOM;
					Log.d(TAG, "mode=ZOOM");
				}
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				mode = NONE;
				Log.d(TAG, "mode=NONE");
				break;
			case MotionEvent.ACTION_MOVE:

				if (mode == DRAG) {
					// ...

					{
						matrix.set(savedMatrix);

						matrix.postTranslate(event.getX() - start.x,
								event.getY() - start.y);
					}
					// System.out.println("X :"+String.valueOf(event.getX())+"Y :"+String.valueOf(event.getY()));
					// System.out.println(String.valueOf(event.getY()-start.y));

				} else if (mode == ZOOM) {
					float newDist = spacing(event);
					Log.d(TAG, "newDist=" + newDist);
					if (newDist > 10f) {
						matrix.set(savedMatrix);
						float scale = newDist / oldDist;
						matrix.postScale(scale, scale, mid.x, mid.y);
					}
				}
				break;
			}

			canvas.setMatrix(matrix);
			return true; // indicate event was handled
		}

		private float spacing(MotionEvent event) {
			float x = event.getX(0) - event.getX(1);
			float y = event.getY(0) - event.getY(1);
			return FloatMath.sqrt(x * x + y * y);
		}

		/** Calculate the mid point of the first two fingers */
		private void midPoint(PointF point, MotionEvent event) {
			float x = event.getX(0) + event.getX(1);
			float y = event.getY(0) + event.getY(1);
			point.set(x / 2, y / 2);
		}

		private void dumpEvent(MotionEvent event) {
			String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE",
					"POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
			StringBuilder sb = new StringBuilder();
			int action = event.getAction();
			int actionCode = action & MotionEvent.ACTION_MASK;
			sb.append("event ACTION_").append(names[actionCode]);
			if (actionCode == MotionEvent.ACTION_POINTER_DOWN
					|| actionCode == MotionEvent.ACTION_POINTER_UP) {
				sb.append("(pid ").append(
						action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
				sb.append(")");
			}
			sb.append("[");
			for (int i = 0; i < event.getPointerCount(); i++) {
				sb.append("#").append(i);
				sb.append("(pid ").append(event.getPointerId(i));
				sb.append(")=").append((int) event.getX(i));
				sb.append(",").append((int) event.getY(i));
				if (i + 1 < event.getPointerCount())
					sb.append(";");
			}
			sb.append("]");
			Log.d(TAG, sb.toString());

		}

		public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2,
				int arg3) {
		}

		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub

			if (!mThread.isAlive()) {
				mThread = new ViewThread(this);
				mThread.setRunning(true);
				mThread.start();
			}
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			if (mThread.isAlive()) {
				mThread.setRunning(false);
			}
		}

	}

	public class ViewThread extends Thread {
		private Panel mPanel;
		private SurfaceHolder mHolder;
		private boolean mRun = false;

		public ViewThread(Panel panel) {
			mPanel = panel;
			mHolder = mPanel.getHolder();
		}

		public void setRunning(boolean run) {
			mRun = run;
		}

		@Override
		public void run() {
			// Canvas canvas = null;
			while (mRun) {
				canvas = mHolder.lockCanvas();
				if (canvas != null) {
					mPanel.doDraw(canvas);
					mHolder.unlockCanvasAndPost(canvas);
				}
			}
		}
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	float[] mGravity;
	float[] mGeomagnetic;

	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub

		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
			mGravity = event.values;
		if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
			mGeomagnetic = event.values;
		if (mGravity != null && mGeomagnetic != null) {
			float R[] = new float[9];
			float I[] = new float[9];
			boolean success = SensorManager.getRotationMatrix(R, I, mGravity,
					mGeomagnetic);
			if (success) {
				float orientation[] = new float[3];
				SensorManager.getOrientation(R, orientation);
				azimut = orientation[0]; // orientation contains: azimut, pitch
											// and roll
				// giro = (-azimut*360/(2*3.14159f));
				giro = (-azimut * 360 / (2 * 3.14f)) / 3;
				grados = (giro.intValue() * 3);
			}
		}

	}

}