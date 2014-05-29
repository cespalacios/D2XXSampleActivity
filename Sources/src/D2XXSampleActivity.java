package com.ftdi.d2xx;

/*Graficar con Canvas*/

import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class D2XXSampleActivity extends Activity implements SensorEventListener {

	ConsumirWSActivity Objson;
	Float azimut;
	Float giro;
	int grados;
	int devCount = 0;
	private SensorManager mSensorManager;
	Sensor accelerometer;
	Sensor magnetometer;
	Panel Fondo;
	final Handler mhandler = new Handler();
	String etiqueta = "3D001C0E7D52";
	private static final int DIALOG_ALERT = 10;
	public Timer timer = null;
	Bitmap imagen, imagen1;
	Canvas canvas = null;
	private int mov_NS = 20;
	private int mov_EO = 0;
	private int mX = 350;
	private int mY = 741;
	private int fX = -306;
	private int fY = -1766;

	
	int centro = 35;

	
/*	protected void miHilo() {
		Thread t = new Thread() {
			public void run() {
				mhandler.post(hilodeprueba);
			}
		};
		t.start();
	}

	final Runnable hilodeprueba = new Runnable() {
		public void run() {

			try {
				pausarLectura();
				devCount = D2xx.createDeviceInfoList();
				if (devCount > 0) {
					
					
					graficar();
					//mX=mX+10;
					//mCustomDrawableView.invalidate();

					// create a D2xx object
					D2xx ftD2xx = new D2xx();
					try {
						
						// open our first device
						ftD2xx.openByIndex(0);
						// configure our port
						// reset to UART mode for 232 devices
						ftD2xx.setBitMode((byte) 0, D2xx.FT_BITMODE_RESET);
						// set 9600 baud
						ftD2xx.setBaudRate(9600);
						// set 8 data bits, 1 stop bit, no parity
						ftD2xx.setDataCharacteristics(D2xx.FT_DATA_BITS_8,
								D2xx.FT_STOP_BITS_1, D2xx.FT_PARITY_NONE);
						// set no flow control
						ftD2xx.setFlowControl(D2xx.FT_FLOW_NONE, (byte) 0x11,
								(byte) 0x13);
						// set latency timer to 16ms
						ftD2xx.setLatencyTimer((byte) 16);
						// set a read timeout of 5s
						// ftD2xx.setTimeouts(5000, 0);
						// purge buffers
						ftD2xx.purge((byte) (D2xx.FT_PURGE_TX | D2xx.FT_PURGE_RX));

						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						int rxq;

						int[] devStatus = null;
						devStatus = ftD2xx.getStatus();
						rxq = devStatus[0];
						if (rxq > 0) {

							// read the data back!
							byte[] InData = new byte[rxq];
							ftD2xx.read(InData, rxq);

							etiqueta = (new String(InData));
							
							
							// 1 3D00428928DE
							// 2 3D0029EEA15B
							// 3 3D0074688DAC
							// 4 3D00762EFB9E
							// 5 3D00746882A3
							// 6 3D007440A3AA
							// 7 3D00226DE193
							// 8 3D001C0E7D52


						}
						// close the port
						ftD2xx.close();
						continuarLectura();

					} catch (D2xxException e) {
						String s = e.getMessage();
						if (s != null) {
						}
					} // fin del catch
				} else {

				}// fin del If devcount > 0
			} // fin del try

			catch (D2xxException e) {
				String s = e.getMessage();
				if (s != null) {
					// device_information.setText(s);
				}
			} // fin del catch

		}
	};*/

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		Fondo = new Panel(this);
		setContentView(Fondo);
		
		Objson = new ConsumirWSActivity();
	
		
		//*******************variables********************************//
		
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		accelerometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		magnetometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		
		
		//***********************funciones****************************//

		//BuscarDisp();
		iniciarLectura();
		
	}
	
	/*public void BuscarDisp() {

		// get device information - Acción del infoButton
		try {
			devCount = 0;
			devCount = D2xx.createDeviceInfoList();
			if (devCount > 0) {
				Toast.makeText(getApplicationContext(), "RFID conectado",
						Toast.LENGTH_SHORT).show();
			} else {
				showDialog(DIALOG_ALERT);
			}

		} // fin del try
		catch (D2xxException e) {
			String s = e.getMessage();
			if (s != null) {

			}
		} // fin de catch

	}*/

    public void graficar() {
		
		
			//if (etiqueta.contains("3D00428928DE")) {
			if (etiqueta.contains("928DE")) {	
				mov_EO= 0;
				fX = -306;
				fY = -1535;
				//fX = fX + 5;
				//mY = 20 - imagen.getHeight()/2;
				//imagen = BitmapFactory.decodeResource(getResources(),
					//	R.drawable.plano1);
			//lugar.setImageResource(R.drawable.plano1);
		//} else if (etiqueta.contains("3D0029EEA15B")){
		} else if (etiqueta.contains("EA15B")){
			mov_EO= 0;
			fX = -306;
			fY=-1060;
			//lugar.setImageResource(R.drawable.plano2);
				//fX = fX - 5;
				//mY = mY + 5;
				//imagen = BitmapFactory.decodeResource(getResources(),
					//	R.drawable.plano2);
		//} else if (etiqueta.contains("3D0074688DAC")) {
		} else if (etiqueta.contains("88DAC")) {	
			mov_EO= 0;
			fY= -650;//imagen 3
			fX= - 356;//imagen
			//lugar.setImageResource(R.drawable.plano3);
		} else if (etiqueta.contains("EFB9E")) {
		//} else if (etiqueta.contains("3D00762EFB9E")) {
			fX= -643;
			fY= -586;
			mov_EO=0;
			//lugar.setImageResource(R.drawable.plano4);
		} else if (etiqueta.contains("3D00746882A3")) {
			//lugar.setImageResource(R.drawable.plano5);
		} else if (etiqueta.contains("3D007440A3AA")) {
			//lugar.setImageResource(R.drawable.plano6);
		} else if (etiqueta.contains("3D00226DE193")) {
			//lugar.setImageResource(R.drawable.pa3_u4);
		} else if (etiqueta.contains("3D001C0E7D52")) {
			//lugar.setImageResource(R.drawable.pa3_u5);
		} else {
			//lugar.setImageResource(R.drawable.ic_launcher);
		}
			
    	
		
	}

	public void iniciarLectura() {
		try {
			this.timer = new Timer();
			this.timer.scheduleAtFixedRate(new TimerTask() {
				public void run() {
					

				}
			}, 500, 500);
		} catch (Exception e) {
		}
	} // fin de iniciarAlarma

	public void pausarLectura() {
		this.timer.cancel();
		devCount = 0;

	}

	/*public void continuarLectura() {
		this.timer = new Timer();
		this.timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				miHilo();

			}
		}, 0, 500);
	}*/
	    	
    
    class Panel extends SurfaceView implements SurfaceHolder.Callback{
		//private GraficarThread thread1;
		private ViewThread mThread;
		
		public Panel(Context context) {
			super(context);
			imagen = BitmapFactory.decodeResource(getResources(),
						R.drawable.plano_completo_2copia);
			
			imagen1 = BitmapFactory.decodeResource(getResources(),
					R.drawable.icon_nav);
			
			//imagen1 = imagen;
			getHolder().addCallback(this);
			mThread = new ViewThread(this); 
		}
		
		
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			//Intent intent = new Intent("com.ftdi.d2xx.PLANO_COMPLETO");
			//startActivity(intent);
			//System.out.println(Objson.S1);
		    return super.onTouchEvent(event);
		}
		
		

		public void doDraw(Canvas canvas) {
			
			//canvas.drawBitmap(imagen1, mX, mY, null);
			int ancho = imagen.getWidth();
			int alto =imagen.getHeight();
			Paint p = new Paint();
			p.setColor(Color.BLACK);
			p.setAntiAlias(true);
			p.setTextSize(15);
			
			
			if (azimut != null)
				canvas.save();
				canvas.rotate(grados+mov_NS+mov_EO, mX, mY);
				canvas.scale( 2.7f, 2.7f );
				canvas.drawBitmap(imagen, fX, fY, p);
				canvas.restore();
			
				
				
				//Objson.S1.contains("Fuego");
				
				canvas.drawBitmap(imagen1, mX, mY, p);
				canvas.drawText(Objson.S1, mX-10, mY-30, p);
				//canvas.drawText(String.valueOf(grados), mX, mY-90, p);
				//canvas.drawText(String.valueOf(ancho), 390, 350, p);
			
	
			//canvas.drawRect(200, 500, 400, 700, p);
			//invalidate();

		}

		public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2,
				int arg3) { }

		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			
			if(!mThread.isAlive()){
				mThread = new ViewThread(this);
				mThread.setRunning(true);
				mThread.start();
			}
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			if(mThread.isAlive()){
				mThread.setRunning(false);
			}
		}

	}
	
	public class ViewThread extends Thread{
		private Panel mPanel;
		private SurfaceHolder mHolder;
		private boolean mRun= false;
		
		public ViewThread(Panel panel){
			mPanel = panel;
			mHolder = mPanel.getHolder();
		}
		
		public void setRunning (boolean run){
			mRun=run;
		}
		
		@Override
		public void run(){
			//Canvas canvas = null;
			while(mRun){
				canvas = mHolder.lockCanvas();
				if(canvas != null){
					mPanel.doDraw(canvas);
					mHolder.unlockCanvasAndPost(canvas);
				}
			}
		}
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
		devCount = 0;
		//pausarLectura();
		finish();
		
	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_ALERT:
			// Create out AlterDialog
			Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Usb RFID Reader desconectado");
			builder.setCancelable(true);
			builder.setPositiveButton("Volver al menú principal",
					new OkOnClickListener());

			AlertDialog dialog = builder.create();
			dialog.show();
		}
		return super.onCreateDialog(id);
	}

	private final class OkOnClickListener implements
			DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {
			D2XXSampleActivity.this.finish();
		//	pausarLectura();
			finish();
			devCount = 0;
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
				//giro = (-azimut*360/(2*3.14159f));
				giro = (-azimut*360/(2*3.14f))/5;
				grados=(giro.intValue()*5);
			}
		}
		
	}

}
