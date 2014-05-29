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
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;
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
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import com.ftdi.D2xx;
import com.ftdi.D2xx.D2xxException;

public class ConsumirWSActivity extends Activity implements SensorEventListener {

	// variables límites de imagen
	public float[] values = new float[9];
	private float matrixX = 0;
	private float matrixY = 0;
	private float width = 0;
	private float height = 0;
	private float dx;
	private float dy;
	public float scale =1f;
	private static final float MIN_ZOOM = 1f;
	private static final float MAX_ZOOM = 3f;

	// trasparencia de alarmas
	private int pa1 = 0;
	private int pa2 = 0;
	private int pa3 = 0;
	private int pa4 = 0;
	private int pa5 = 0;
	private int pa6 = 0;
	private int r_a1 = 150;
	private int r_a2 = 150;
	private int r_g = 150;
	private int r_a4 = 150;
	private int r_a6 = 150;
	private int m_evac = 0;
	private int m_av1 = 0;
	private int m_av2 = 0;
	private int m_av3 = 0;
	private int m_av4 = 0;
	private int m_av5 = 0;
	private int m_av6 = 0;
	private int m_av7 = 0;
	private int m_av8 = 0;
	private int alert = 0;

	// Módulo Rfid
	public int devCount = 0;
	public int timeOutRead = 2000; // 2000
	public int repeatIntento = 500; // 500
	public byte[] InData = new byte[13];
	public Handler handler = new Handler();
	public int rxq = 0;

	public String etiqueta = "";

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
	private int grados;
	int pos_default=190;
	private int icon_nav_x = 265;
	private int icon_nav_y = 900;


	// Dibujar imagenes
	Panel Fondo;
	//public int piso=R.drawable.plano_1024x900_final;
	public int piso=0;
	Bitmap imagen, imagen1, alerta1, alerta2;
	Canvas canvas = null;
	float[] points = new float[9];
	
	//variable mesajes de alerta
	public String alerta_lugar="";
	public String alerta_lugar_line2="";
	public String alerta_salida="";

	// Timers
	private Timer timer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Fondo = new Panel(this);
		setContentView(Fondo);

		// ModuloRFID
		try {
			D2xx.setVIDPID(0x0403, 0xada1);
		} catch (D2xxException e) {

		}

		//matrix.postScale(1.5f, 1.5f);
		values[Matrix.MSCALE_X] = 1f;

		// **variables_acelerómetro***//
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		accelerometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		magnetometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

		//piso=R.drawable.plano_1024x900_final;
		
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
			// devCount = 0;
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
		// cada 10 s.
		try {
			this.timer = new Timer();
			this.timer.scheduleAtFixedRate(new TimerTask() {
				public void run() {
					new LongRunningGetIO().execute();
					// System.out.println(S1);

				}
			}, 500, 10000);
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
		devCount = 0;
		mSensorManager.unregisterListener(this);
		handler.removeCallbacks(runnable1); // stop thread Hilo y Runnable
		pausarLectura();

		finish();

	}

	private class Intento1 extends AsyncTask<String, Void, String> {

		byte[] InData = new byte[13];

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
			//1 -  0400C9673298  26418
			//2 -  0400C967379D  26423
			//3 -  0400C9673892  26424
			//4 -  0400C967359F  26421
			//5 -  0400C967369C  26422
			//6 -  0400C9673399  26419
			//7 -  0400C967349E  26420
			//8 -  0400C96747ED  26439
			//9 -  0400C967319B  26417
			//10 - 0400C9673993  26425
			
			//0600A9D0D5AA  53461
			savedMatrix.set(matrix);
			
			
			// 1 3D00428928DE
			// 2 3D0029EEA15B
			// 3 3D0074688DAC
			// 4 3D00762EFB9E
			// 5 3D00746882A3
			// 6 3D007440A3AA
			// 7 3D00226DE193
			// 8 3D001C0E7D52

			etiqueta = (string2);
			
			/*if(grados>0){
				compass=0;
			}else{
				compass=180;
			}*/
			
			/*Area1*/
			if (etiqueta.contains("0400C9673298")) {// || etiqueta.contains("0400C967379D")) {
				//matrix.reset();
				piso=3;
				imagen = BitmapFactory.decodeResource(getResources(), R.drawable.plano_1024x900_final);
				
				icon_nav_x=275;
				icon_nav_y=950;
				//values[Matrix.MTRANS_Y]=-90;
				//values[Matrix.MTRANS_X]=0;
				//values[Matrix.MSCALE_X]=1f;
				
		
				//compass=0;
			//	XX=240;
		//		YY=1200;
				
				//matrix.postTranslate(0, -90);
				savedMatrix.set(matrix);
				//matrix.set(savedMatrix);
				//matrix.postScale(1.5f, 1.5f);
				
				
				/*Area2*/	
			} else if (etiqueta.contains("0400C967379D")) {// || etiqueta.contains("0400C967359F")) {
				//matrix.reset();
				piso=3;
				imagen = BitmapFactory.decodeResource(getResources(), R.drawable.plano_1024x900_final);
				icon_nav_x=275;
				icon_nav_y=850;
				
				//values[Matrix.MTRANS_X]=0;
				//values[Matrix.MTRANS_Y]=0;
				//matrix.set(savedMatrix);
				//compass=0;
				//XX=275;
				//YY=600;
				//matrix.postTranslate(0, 0);
				savedMatrix.set(matrix);
				//matrix.postScale(1.8f, 1.8f, 240, 755);
				//matrix.postScale(1.5f, 1.5f);
				
				/*Area3*/
			} else if (etiqueta.contains("0400C9673892")) {// || etiqueta.contains("0400C9673993")) {
				//matrix.reset();
				piso=3;
				imagen = BitmapFactory.decodeResource(getResources(), R.drawable.plano_1024x900_final);
				//piso=R.drawable.plano_1024x900_final;
				icon_nav_x=275;
				icon_nav_y=750;
				
				//values[Matrix.MTRANS_X]=0;
				//values[Matrix.MTRANS_Y]=0;
				//matrix.postTranslate(0, 0);
				savedMatrix.set(matrix);
				//XX=350;
				//YY=550;
				//compass=compass-90;
				//matrix.postScale(1.8f, 1.8f, 350, 550);
				//matrix.postScale(1.8f, 1.8f, XX, YY);
				//matrix.postScale(1.5f, 1.5f);
				
				/*Area4*/
			} else if (etiqueta.contains("0400C967359F")) {
				//matrix.reset();
				piso=3;
				imagen = BitmapFactory.decodeResource(getResources(), R.drawable.plano_1024x900_final);
				icon_nav_x=275;
				icon_nav_y=600;
				
				savedMatrix.set(matrix);
				//XX=580;
				//YY=340;
				//compass=0;
				//matrix.postScale(1.8f, 1.8f, XX, YY);
				//matrix.postScale(1.5f, 1.5f);
				
				/*Area5*/
			} else if (etiqueta.contains("0400C967369C")) {
				//matrix.reset();
				piso=3;
				imagen = BitmapFactory.decodeResource(getResources(), R.drawable.plano_1024x900_final);
				icon_nav_x=390;
				icon_nav_y=545;
				
				savedMatrix.set(matrix);
				//compass=0;
				//XX=780;
				//YY=120;
				//matrix.postScale(1.8f, 1.8f, 780,120 );
				//matrix.postScale(1.8f, 1.8f, XX, YY);
				//matrix.postScale(1.5f, 1.5f);
				
				/*Area6*/
			} else if (etiqueta.contains("0400C9673399")) {
				//matrix.reset();
				piso=3;
				imagen = BitmapFactory.decodeResource(getResources(), R.drawable.plano_1024x900_final);
				icon_nav_x=480;
				icon_nav_y=385;
				
				savedMatrix.set(matrix);
				//compass=compass-90;
				//XX=990;
				//YY=780;
				//matrix.postTranslate(-250, 0);
				//matrix.postScale(1.8f, 1.8f, XX, YY);
				
				/*Area7*/
			} else if (etiqueta.contains("0400C967349E")) {
				//matrix.reset();
				piso=3;
				imagen = BitmapFactory.decodeResource(getResources(), R.drawable.plano_1024x900_final);
				icon_nav_x=655;
				icon_nav_y=300;
				
				savedMatrix.set(matrix);
				//matrix.postTranslate(0,0);
				
			} /*Area8*/
			else if (etiqueta.contains("0400C96747ED")) {
				//matrix.reset();
				piso=3;
				imagen = BitmapFactory.decodeResource(getResources(), R.drawable.plano_1024x900_final);
				icon_nav_x=880;
				icon_nav_y=300;
				
				savedMatrix.set(matrix);
				//matrix.postTranslate(0,0);
			} 
			/*Area9*/
			else if (etiqueta.contains("0400C967319B")) {
				//matrix.reset();
				piso=3;
				imagen = BitmapFactory.decodeResource(getResources(), R.drawable.plano_1024x900_final);
				icon_nav_x=1020;
				icon_nav_y=300;
				savedMatrix.set(matrix);
				//matrix.postTranslate(0,0);
			} 
		/*Area10*/
			else if (etiqueta.contains("0400C9673993")) {
				//matrix.reset();
				piso=3;
				imagen = BitmapFactory.decodeResource(getResources(), R.drawable.plano_1024x900_final);
				icon_nav_x=655;
				icon_nav_y=165;
				//values[Matrix.MTRANS_Y]=-655;
				//values[Matrix.MTRANS_X]=0;
				//values[Matrix.MSCALE_X]=1f;
				
		
				//compass=0;
			//	XX=240;
		//		YY=1200;
				
				//matrix.postTranslate(0, -655);
				savedMatrix.set(matrix);
				//matrix.postTranslate(0,0);
				
				/*Planta Baja ARea11*/
			}else if (etiqueta.contains("0600A9D0D5AA")) {
				//matrix.reset();
				piso=2;
				imagen = BitmapFactory.decodeResource(getResources(), R.drawable.plano_3piso);
				icon_nav_x=410;
				icon_nav_y=367;
				//values[Matrix.MTRANS_Y]=-655;
				//values[Matrix.MTRANS_X]=0;
				//values[Matrix.MSCALE_X]=1f;
				
		
				//compass=0;
			//	XX=240;
		//		YY=1200;
				
				//matrix.postTranslate(0, -655);
				savedMatrix.set(matrix);
				//matrix.postTranslate(0,0);
			}
			matrix.set(savedMatrix);
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
					"http://172.17.23.179:8080/Prime1/resources/loginbeanpack.dbalertas");
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

						
						alerta_lugar="Alerta en:";
						alerta_lugar_line2="";
						//alerta_salida="Evacúe por";
						//alerta_salida="";
						
						
						if (S1.contains("FUEGO") || S2.contains("FUEGO")
								|| S3.contains("FUEGO") || S4.contains("FUEGO")
								|| S5.contains("FUEGO") || S6.contains("FUEGO")
								|| S7.contains("FUEGO") || S8.contains("FUEGO")) {
							m_evac = 200;//fondo gris mensaje parte inferior
							alert = 250;//mensaje de alerta superior
							m_av1 = 250; //alerta linea 1
							m_av2 = 250; //alerta linea 2
							m_av3 = 250;// mensaje salidas posibles
							
							alerta_salida="Evacúe por las escaleras";
							//alerta_lugar.concat("ALerta en:");

							if (S1.contains("FUEGO")) {

								/*
								 * Toast.makeText(getApplicationContext(),
								 * "ALARMA en Sector 1", Toast.LENGTH_SHORT)
								 * .show();
								 */
								alerta_lugar=(alerta_lugar+"DCCE, ");
								
								pa1 = 150;
								r_a1 = 0;

							} else {
							//	m_av1 = 0;
								pa1 = 0;
								r_a1 = 150;

							}

							if (S2.contains("FUEGO")) {

								alerta_lugar=(alerta_lugar+"Soporte Técnico, ");
								//m_av2 = 250;
								pa2 = 150;
								r_a2 = 0;

							} else {
							//	m_av2 = 0;
								pa2 = 0;
								r_a2 = 150;
							}

							if (S3.contains("FUEGO")) {
								
								alerta_lugar=(alerta_lugar+"Sala de Servidores, ");
								
								//m_av3 = 250;
								pa3 = 150;
							} else {
							//	m_av3 = 0;
								pa3 = 0;
							}

							if (S4.contains("FUEGO")) {

								alerta_lugar_line2=(alerta_lugar_line2+"Cursos Especializados, ");
								//m_av4 = 250;
								pa4 = 150;
								r_a4 = 0;
							} else {
							//	m_av4 = 0;
								pa4 = 0;
								r_a4 = 150;
							}
							if (S5.contains("FUEGO")) {
								
								alerta_lugar_line2=(alerta_lugar_line2+"Escaleras ");
								//m_av5 = 250;
								pa5 = 150;
								r_g = 0;
								r_a2 = 0;

							} else {
								//m_av5 = 0;
								pa5 = 0;
								r_g = 150;
								r_a2 = 150;

							}
							if (S6.contains("FUEGO")) {
								
								alerta_lugar_line2=(alerta_lugar_line2+"Salida de emergencia");
								
								//m_av6 = 250;
								pa6 = 150;
								r_a6 = 0;
							} else {
								alerta_salida=alerta_salida+" o salida emergencia";
								//m_av6 = 0;
								pa6 = 0;
								r_a6 = 150;
							}
							if (S7.contains("FUEGO")) {
								alerta_lugar_line2=(alerta_lugar_line2+"piso inferior");
							} else {

							}

							if (S8.contains("FUEGO")) {
								alerta_lugar_line2=(alerta_lugar_line2+"piso inferior 2");
							} else {

							}

						}

						else {
							m_evac = 0;
							alert = 0;

							m_av1 = 0;
							m_av2 = 0;
							r_a1 = 150;
							r_a2 = 150;
							r_a4 = 150;
							r_g = 150;
							r_a2 = 150;
							r_a6 = 150;
							pa1 = 0;
							pa2 = 0;
							pa3 = 0;
							pa4 = 0;
							pa5 = 0;
							pa6 = 0;

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

		class Pt {

			float x, y;

			Pt(float _x, float _y) {

				x = _x;

				y = _y;

			}

		}

		// *********RUTA por defecto*********//
		Pt[] myPath = {

		new Pt(265, 734), new Pt(265, 544), new Pt(421, 544), new Pt(493, 452),
				new Pt(493, 306), new Pt(655, 306),

		// new Pt(655, 306)

		};

		// *********RUTA Alarma 1*********//
		Pt[] Ruta_sensor1 = {

		new Pt(655, 306), new Pt(1050, 306)

		};

		// *********RUTA Alarma 2*********//
		Pt[] Ruta_sensor2 = { new Pt(493, 306), new Pt(265, 306) };

		// *********RUTA Gradas 2*********//
		Pt[] Ruta_gradas = { new Pt(493, 362), new Pt(402, 362) };

		// *********RUTA Alarma 4*********//
		Pt[] Ruta_sensor4 = { new Pt(265, 1100), new Pt(265, 734) };

		// *********RUTA Alarma 6*********//
		Pt[] Ruta_sensor6 = { new Pt(655, 73), new Pt(655, 306) };

		Typeface font = Typeface.createFromAsset(getContext().getAssets(),
				"fonts/SEGUISB.TTF");

		public Panel(Context context) {
			super(context);
			
			
			imagen = BitmapFactory.decodeResource(getResources(), R.drawable.plano_1024x900_final);
			
			imagen1 = BitmapFactory.decodeResource(getResources(),
					R.drawable.icon_nav2);
			//savedMatrix.set(matrix);
			getHolder().addCallback(this);
			mThread = new ViewThread(this);
		}

		public void doDraw(Canvas canvas) {
			
			
			
			//savedMatrix.set(matrix);
			canvas.drawColor(Color.BLACK);
			// paint fondo
			Paint p = new Paint();
			p.setAntiAlias(true);
			p.setDither(true);
			p.setColor(Color.BLUE);
			p.setTextSize(40);

			// Paint RUTA por defecto
			Paint p_ruta1 = new Paint();
			p_ruta1.setARGB(200, 100, 250, 100);
			p_ruta1.setStyle(Paint.Style.STROKE);
			p_ruta1.setStrokeWidth(8);

			// Paint Rutas alarma1
			Paint R_alarma1 = new Paint();
			R_alarma1.setARGB(r_a1, 100, 250, 100);
			R_alarma1.setStyle(Paint.Style.STROKE);
			R_alarma1.setStrokeWidth(8);

			// Paint Rutas alarma2
			Paint R_alarma2 = new Paint();
			R_alarma2.setARGB(r_a2, 100, 250, 100);
			R_alarma2.setStyle(Paint.Style.STROKE);
			R_alarma2.setStrokeWidth(8);

			// Paint Gradas
			Paint R_gradas = new Paint();
			R_gradas.setARGB(r_g, 50, 250, 50);
			R_gradas.setStyle(Paint.Style.STROKE);
			R_gradas.setStrokeWidth(8);

			// Paint Rutas alarma4
			Paint R_alarma4 = new Paint();
			R_alarma4.setARGB(r_a4, 100, 250, 100);
			R_alarma4.setStyle(Paint.Style.STROKE);
			R_alarma4.setStrokeWidth(8);

			// Paint Rutas alarma6
			Paint R_alarma6 = new Paint();
			R_alarma6.setARGB(r_a6, 100, 250, 100);
			R_alarma6.setStyle(Paint.Style.STROKE);
			R_alarma6.setStrokeWidth(8);

			// Paint alerta sensor texto
			Paint msj = new Paint();
			msj.setARGB(alert, 21, 64, 94);
			msj.setTypeface(font);
			msj.setTextSize(30);

			// Mejs contorno
			Paint msj_contorno = new Paint();
			msj_contorno.setStyle(Paint.Style.STROKE);
			msj_contorno.setStrokeWidth(5);
			msj_contorno.setARGB(alert, 21, 64, 94);
			msj_contorno.setAntiAlias(true);

			// Paint alerta sensor fondo
			Paint msj_fondo = new Paint();
			msj_fondo.setARGB(alert, 153, 187, 0);
			msj_fondo.setShadowLayer(15, 10, 5, Color.GRAY);

			// mesaje evacuacion
			Paint msj_evac = new Paint();
			msj_evac.setARGB(m_evac, 51, 51, 51);

			// mensaje de aviso1
			Paint msj_aviso1 = new Paint();
			msj_aviso1.setARGB(m_av1, 250, 250, 250);
			// msj.setTypeface(font);
			msj_aviso1.setTextSize(25);

			// mesaje aviso 2
			Paint msj_aviso2 = new Paint();
			msj_aviso2.setARGB(m_av2, 250, 250, 250);
			// msj.setTypeface(font);
			msj_aviso2.setTextSize(25);

			// mesaje aviso 3
			Paint msj_aviso3 = new Paint();
			msj_aviso3.setARGB(m_av3, 250, 250, 250);
			// msj.setTypeface(font);
			msj_aviso3.setTextSize(25);

			// mesaje aviso 4
			Paint msj_aviso4 = new Paint();
			msj_aviso4.setARGB(m_av4, 250, 250, 250);
			// msj.setTypeface(font);
			msj_aviso4.setTextSize(25);

			// mesaje aviso 5
			Paint msj_aviso5 = new Paint();
			msj_aviso5.setARGB(m_av5, 250, 250, 250);
			// msj.setTypeface(font);
			msj_aviso5.setTextSize(25);

			// mesaje aviso 6
			Paint msj_aviso6 = new Paint();
			msj_aviso6.setARGB(m_av6, 250, 250, 250);
			// msj.setTypeface(font);
			msj_aviso6.setTextSize(25);

			// mesaje aviso 7
			Paint msj_aviso7 = new Paint();
			msj_aviso7.setARGB(m_av7, 250, 250, 250);
			// msj.setTypeface(font);
			msj_aviso7.setTextSize(25);

			// mensaje aviso 8
			Paint msj_aviso8 = new Paint();
			msj_aviso8.setARGB(m_av8, 250, 250, 250);
			// msj.setTypeface(font);
			msj_aviso8.setTextSize(25);

			/***/
		
			
			// **********función para dibujar ruta**********

			Path path = new Path();

			path.moveTo(values[Matrix.MTRANS_X] + dx + (myPath[0].x)
					* values[Matrix.MSCALE_X], values[Matrix.MTRANS_Y] + dy
					+ (myPath[0].y) * values[Matrix.MSCALE_X]);

			for (int i = 1; i < myPath.length; i++) {

				path.lineTo(values[Matrix.MTRANS_X] + dx + (myPath[i].x)
						* values[Matrix.MSCALE_X], values[Matrix.MTRANS_Y] + dy
						+ (myPath[i].y) * values[Matrix.MSCALE_X]);

			}

			// *********ruta alarma 1
			Path R_a1 = new Path();

			R_a1.moveTo(values[Matrix.MTRANS_X] + dx + (Ruta_sensor1[0].x)
					* values[Matrix.MSCALE_X], values[Matrix.MTRANS_Y] + dy
					+ (Ruta_sensor1[0].y) * values[Matrix.MSCALE_X]);

			for (int i = 1; i < Ruta_sensor1.length; i++) {

				R_a1.lineTo(values[Matrix.MTRANS_X] + dx + (Ruta_sensor1[i].x)
						* values[Matrix.MSCALE_X], values[Matrix.MTRANS_Y] + dy
						+ (Ruta_sensor1[i].y) * values[Matrix.MSCALE_X]);

			}

			// ******ruta alarma2***************
			Path R_a2 = new Path();

			R_a2.moveTo(values[Matrix.MTRANS_X] + dx + (Ruta_sensor2[0].x)
					* values[Matrix.MSCALE_X], values[Matrix.MTRANS_Y] + dy
					+ (Ruta_sensor2[0].y) * values[Matrix.MSCALE_X]);

			for (int i = 1; i < Ruta_sensor2.length; i++) {

				R_a2.lineTo(values[Matrix.MTRANS_X] + dx + (Ruta_sensor2[i].x)
						* values[Matrix.MSCALE_X], values[Matrix.MTRANS_Y] + dy
						+ (Ruta_sensor2[i].y) * values[Matrix.MSCALE_X]);

			}

			// **********Ruta gradas**********
			Path R_g = new Path();

			R_g.moveTo(values[Matrix.MTRANS_X] + dx + (Ruta_gradas[0].x)
					* values[Matrix.MSCALE_X], values[Matrix.MTRANS_Y] + dy
					+ (Ruta_gradas[0].y) * values[Matrix.MSCALE_X]);

			for (int i = 1; i < Ruta_sensor2.length; i++) {

				R_g.lineTo(values[Matrix.MTRANS_X] + dx + (Ruta_gradas[i].x)
						* values[Matrix.MSCALE_X], values[Matrix.MTRANS_Y] + dy
						+ (Ruta_gradas[i].y) * values[Matrix.MSCALE_X]);

			}

			// ******ruta alarma4***************
			Path R_a4 = new Path();

			R_a4.moveTo(values[Matrix.MTRANS_X] + dx + (Ruta_sensor4[0].x)
					* values[Matrix.MSCALE_X], values[Matrix.MTRANS_Y] + dy
					+ (Ruta_sensor4[0].y) * values[Matrix.MSCALE_X]);

			for (int i = 1; i < Ruta_sensor2.length; i++) {

				R_a4.lineTo(values[Matrix.MTRANS_X] + dx + (Ruta_sensor4[i].x)
						* values[Matrix.MSCALE_X], values[Matrix.MTRANS_Y] + dy
						+ (Ruta_sensor4[i].y) * values[Matrix.MSCALE_X]);

			}

			// *********ruta alarma 6
			Path R_a6 = new Path();

			R_a6.moveTo(values[Matrix.MTRANS_X] + dx + (Ruta_sensor6[0].x)
					* values[Matrix.MSCALE_X], values[Matrix.MTRANS_Y] + dy
					+ (Ruta_sensor6[0].y) * values[Matrix.MSCALE_X]);

			for (int i = 1; i < Ruta_sensor1.length; i++) {

				R_a6.lineTo(values[Matrix.MTRANS_X] + dx + (Ruta_sensor6[i].x)
						* values[Matrix.MSCALE_X], values[Matrix.MTRANS_Y] + dy
						+ (Ruta_sensor6[i].y) * values[Matrix.MSCALE_X]);

			}

			// **********PAint ZONA ALARMAS******************
			Paint p_alerta1 = new Paint();
			p_alerta1.setARGB(pa1, 250, 100, 100);

			Paint p_alerta2 = new Paint();
			p_alerta2.setARGB(pa2, 250, 100, 100);

			Paint p_alerta3 = new Paint();
			p_alerta3.setARGB(pa3, 250, 100, 100);

			Paint p_alerta4 = new Paint();
			p_alerta4.setARGB(pa4, 250, 100, 100);

			Paint p_alerta5 = new Paint();
			p_alerta5.setARGB(pa5, 250, 100, 100);

			Paint p_alerta6 = new Paint();
			p_alerta6.setARGB(pa6, 250, 100, 100);

			// ********************
			// ******DIBUJAR en Matrix*****************//
			
			canvas.save();
			
			
			//matrix.set(savedMatrix);
			canvas.setMatrix(matrix);
			//matrix.getValues(values);


			float icon_x = values[Matrix.MTRANS_X] + dx
					+ (icon_nav_x - (imagen1.getWidth() / 2))
					* values[Matrix.MSCALE_X];
			float icon_y = values[Matrix.MTRANS_Y] + dy
					+ (icon_nav_y - (imagen1.getHeight() / 2))
					* values[Matrix.MSCALE_X];

			
			// canvas.rotate(compass, icon_x + imagen1.getWidth() / 2, icon_y +
			 //imagen1.getHeight() / 2);

			// coordenadas alarma 1 P3: gradas y ascensor
			float alarma1_x = values[Matrix.MTRANS_X] + dx + (930)
					* values[Matrix.MSCALE_X];
			float alarma1_y = values[Matrix.MTRANS_Y] + dy + (76)
					* values[Matrix.MSCALE_X];

			// coordenadas alarma 2 P3 Salas M,I,J...
			float alarma2_x = values[Matrix.MTRANS_X] + dx + (60)
					* values[Matrix.MSCALE_X];
			float alarma2_y = values[Matrix.MTRANS_Y] + dy + (80)
					* values[Matrix.MSCALE_X];

			// coordenadas alarma 3 P3
			float alarma3_x = values[Matrix.MTRANS_X] + dx + (564)
					* values[Matrix.MSCALE_X];
			float alarma3_y = values[Matrix.MTRANS_Y] + dy + (333)
					* values[Matrix.MSCALE_X];

			// coordenadas alarma 4 P3
			float alarma4_x = values[Matrix.MTRANS_X] + dx + (56)
					* values[Matrix.MSCALE_X];
			float alarma4_y = values[Matrix.MTRANS_Y] + dy + (840)
					* values[Matrix.MSCALE_X];

			// coordenadas alarma 5 P3
			float alarma5_x = values[Matrix.MTRANS_X] + dx + (312)
					* values[Matrix.MSCALE_X];
			float alarma5_y = values[Matrix.MTRANS_Y] + dy + (330)
					* values[Matrix.MSCALE_X];

			// coordenadas alarma 6 P3
			float alarma6_x = values[Matrix.MTRANS_X] + dx + (520)
					* values[Matrix.MSCALE_X];
			float alarma6_y = values[Matrix.MTRANS_Y] + dy + (10)
					* values[Matrix.MSCALE_X];

			canvas.drawBitmap(imagen, matrix, p);

			// ALARMAR 1
			canvas.drawRect(alarma1_x, alarma1_y, alarma1_x + 380
					* values[Matrix.MSCALE_X], alarma1_y + 509
					* values[Matrix.MSCALE_X], p_alerta1);
			// ALARMAR 2
			canvas.drawRect(alarma2_x, alarma2_y, alarma2_x + 368
					* values[Matrix.MSCALE_X], alarma2_y + 198
					* values[Matrix.MSCALE_X], p_alerta2);
			// ALARMAR 3

			canvas.drawRect(alarma3_x, alarma3_y, alarma3_x + 243
					* values[Matrix.MSCALE_X], alarma3_y + 255
					* values[Matrix.MSCALE_X], p_alerta3);

			// ALARMAR 4
			canvas.drawRect(alarma4_x, alarma4_y, alarma4_x + 510
					* values[Matrix.MSCALE_X], alarma4_y + 495
					* values[Matrix.MSCALE_X], p_alerta4);
			// ALARMAR 5
			canvas.drawRect(alarma5_x, alarma5_y, alarma5_x + 125
					* values[Matrix.MSCALE_X], alarma5_y + 122
					* values[Matrix.MSCALE_X], p_alerta5);
			// ALARMAR 6
			canvas.drawRect(alarma6_x, alarma6_y, alarma6_x + 162
					* values[Matrix.MSCALE_X], alarma6_y + 152
					* values[Matrix.MSCALE_X], p_alerta6);

			// RUTA por defecto
			canvas.drawPath(path, p_ruta1);

			// RUTA_ALARMAS
			canvas.drawPath(R_a1, R_alarma1);
			canvas.drawPath(R_a2, R_alarma2);
			canvas.drawPath(R_g, R_gradas);
			canvas.drawPath(R_a4, R_alarma4);
			canvas.drawPath(R_a6, R_alarma6);


			if (azimut != null) {
				canvas.rotate(grados, icon_x + imagen1.getWidth() / 2, icon_y
						+ imagen1.getHeight() / 2);

				canvas.drawBitmap(imagen1, icon_x, icon_y, p);
			}
			
			
			canvas.restore();
			
			
			// escribir alarmas visibles en pantalla
			RectF rec = new RectF(260, 10, 460, 60);
			// canvas.drawRect(rec, msj_fondo);
			canvas.drawRoundRect(rec, 10, 10, msj_fondo);
			canvas.drawRoundRect(rec, 10, 10, msj_contorno);
			canvas.drawText("ALERTA", 290, 45, msj);
			// canvas.drawText("  una alerta", 75, 120, msj);

			canvas.drawRect(0, 990, 720, 1140, msj_evac);
			canvas.drawText(alerta_lugar, 10, 1040,
					msj_aviso1);

			canvas.drawText(alerta_lugar_line2,	10, 1070, msj_aviso2);
			
			canvas.drawText(
					alerta_salida,
					10, 1100, msj_aviso3);
			/*canvas.drawText(
					"Evacúe por la salida de emergencia o las escaleras",
					10, 1070, msj_aviso3);
			canvas.drawText("Se activado alerta en las salas I,M,L", 10, 1040, msj_aviso4);
			canvas.drawText("Escaleras y salida de emergencia habilitadas", 10, 1070, msj_aviso4);
			
			canvas.drawText("alarma 5", 10, 1040, msj_aviso5);
			canvas.drawText("alarma 6", 10, 1040, msj_aviso6);
			canvas.drawText("alarma 7", 10, 1040, msj_aviso7);
			
			canvas.drawText(alerta_lugar, 10, 1040, msj_aviso8);*/
			//canvas.drawText(etiqueta, 20, 800, p);
			//canvas.drawText(String.valueOf(grados), 20, 800, p);

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
				mode=NONE;
				Log.d(TAG, "mode=NONE");
				break;
			case MotionEvent.ACTION_MOVE:

				if (mode == DRAG) {
					// ...
					int pantalla_Height = 1250;
					int pantalla_Width = 1050;

					matrix.set(savedMatrix);
					matrix.getValues(values);
					matrixX = values[Matrix.MTRANS_X];
					matrixY = values[Matrix.MTRANS_Y];
					width = values[Matrix.MSCALE_X] * imagen.getWidth();
					height = values[Matrix.MSCALE_Y] * imagen.getHeight();

					dx = event.getX() - start.x;
					dy = event.getY() - start.y;

					if (matrixY + dy > 0)
						while (matrixY + dy > 0)
							dy--;
					if (matrixX + dx + width < pantalla_Width)
						while (matrixX + dx + width < pantalla_Width)
							dx++;
					if (matrixY + dy + height < pantalla_Height)
						while (matrixY + dy + height < pantalla_Height)
							dy++;
					if (matrixX + dx > 0)
						while (matrixX + dx > 0)
							dx--;
					matrix.postTranslate(dx, dy);

				} else if (mode == ZOOM) {
					/*
					 * float newDist = spacing(event); Log.d(TAG, "newDist=" +
					 * newDist); if (newDist > 10f) { matrix.set(savedMatrix);
					 * float scale = newDist / oldDist; matrix.postScale(scale,
					 * scale, mid.x, mid.y); }
					 */

					float newDist = spacing(event);
					if (newDist > 10f) {
						matrix.set(savedMatrix);
						scale = newDist / oldDist;
						matrix.getValues(values);
						float currentScale = values[Matrix.MSCALE_X];
						if (scale * currentScale > MAX_ZOOM)
							scale = MAX_ZOOM / currentScale;
						else if (scale * currentScale < MIN_ZOOM)
							scale = MIN_ZOOM / currentScale;
						
						matrix.postScale(scale, scale, mid.x, mid.y);
						//canvas.scale(values[Matrix.MSCALE_X],values[Matrix.MSCALE_X]);
						//matrix.postTranslate(dx, dy);

						

					}
				}
				break;
			}
			//canvas.setMatrix(matrix);
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
				giro = (-azimut * 360 / (2 * 3.14f)) / 10;
				grados = (giro.intValue() * -10);
				grados=grados+pos_default;
			}
		}

	}

}
