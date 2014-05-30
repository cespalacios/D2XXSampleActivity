package com.ftdi.d2xx;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
//import android.widget.EditText;
import android.widget.TextView;
//import android.widget.Toast;

import com.ftdi.D2xx;
import com.ftdi.D2xx.D2xxException;

public class D2XXSampleActivity extends Activity {
	
	Button infoBtn; //Buscar dispositivo
	
	public TextView number_devs;
	public TextView device_information;
	public TextView myData;
	public TextView tv1;
	
	public int devCount = 0;
	public int timeOutRead = 2000; //2000
	public int repeatIntento = 500; //500
	public byte[] InData = new byte[13];
	public Handler handler = new Handler();
	public int rxq = 0;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.main);
    	
    	// Specify a non-default VID and PID combination to match if required
    	try {
    		D2xx.setVIDPID(0x0403, 0xada1);
    	}
    	catch (D2xxException e)
    	{
    		
    	}
    	
        // find controls and specify event handlers
        number_devs = (TextView)findViewById(R.id.numDevstv);
        device_information = (TextView)findViewById(R.id.devInfotv);
        myData = (TextView)findViewById(R.id.datatv);
        tv1 = (TextView)findViewById(R.id.tv1);
        
     // get device information
		try {
			devCount = D2xx.createDeviceInfoList();
			if (devCount > 0)
			{
				D2xx.FtDeviceInfoListNode[] deviceList = new D2xx.FtDeviceInfoListNode[devCount];
				D2xx.getDeviceInfoList(devCount, deviceList);
				number_devs.setText("Number of devices: ");
				number_devs.append(Integer.toString(devCount));
				if (devCount > 0) // display the chip type for the first device
					switch (deviceList[0].type) {	
					case D2xx.FT_DEVICE_232R:
						device_information.setText("FT232R device");
						break;	
					}
				Hilo();
        	  } //fin del if devcount > 0
    		  else
    		  {
    			  number_devs.setText("Number of devices: ");
    			  number_devs.append(Integer.toString(devCount));
    			  device_information.setText("No device");
    		  }
		} //FIN try
		catch(D2xxException e)
		{
			String s = e.getMessage();
			if (s != null) {
				device_information.setText(s);
			}
		} //FIN catch
    } //FIN public void onCreate
    
    //Botón Regresar
    protected void onPause() {
    	super.onPause();
    	handler.removeCallbacks(runnable1); //stop thread Hilo y Runnable
    	handler.removeCallbacks(runnable2);
    	finish();
    }
    
	protected void Hilo() {
		Thread tt = new Thread() {
			public void run(){
				handler.post(runnable1);
			}
		};
		tt.start();
		
		Thread tt2 = new Thread() {
			public void run(){
				handler.post(runnable2);
			}
		};
		tt2.start();
	}
    
    private Runnable runnable1 = new Runnable() {   
        public void run() {
        	new Intento1().execute();
            handler.postDelayed(runnable1, repeatIntento);
        }
    };
    
    private Runnable runnable2 = new Runnable() {   
        public void run() {
        	//Toast.makeText(getApplicationContext(), "Hola", Toast.LENGTH_SHORT).show();
        	Intento2();
            handler.postDelayed(runnable2, 100);
        }
    };
    
    public void Intento2() {
    	tv1.append(" h ");
    }
    
    //<doInBackground, OnProgress, onPostExecute>
    private class Intento1 extends AsyncTask <String, Void, String> {
        
    	@Override
        protected String doInBackground(String... string1) {
    		D2xx ftD2xx = new D2xx();
	    	try {
	    		// open the port, send/receive data and close port
				ftD2xx.openByIndex(0);
				ftD2xx.setBitMode((byte)0, D2xx.FT_BITMODE_RESET);
				ftD2xx.setBaudRate(9600);
				ftD2xx.setDataCharacteristics(D2xx.FT_DATA_BITS_8, D2xx.FT_STOP_BITS_1, D2xx.FT_PARITY_NONE);
				ftD2xx.setFlowControl(D2xx.FT_FLOW_NONE, (byte)0x11, (byte)0x13);	
	        	ftD2xx.setLatencyTimer((byte)16);
	        	ftD2xx.setTimeouts(timeOutRead, 0); //Lee bien con poquisimos errores 
	        	ftD2xx.purge((byte) (D2xx.FT_PURGE_TX | D2xx.FT_PURGE_RX));
	        	
				int rxq = 13;
				// read the data back!
        		ftD2xx.read(InData,rxq);
            	ftD2xx.resetDevice(); //Reset Device
	        	ftD2xx.close();
	    	} //fin try
	    	catch (D2xxException e) {
	    		String s = e.getMessage();
				if (s != null) {
					//myData.setText(s);
				}
	    	} //Fin catch
        	return new String(InData);
    	}
    	
    	//@Override
        protected void onPostExecute(String string2) {               
        	//tv1.append(string2);	
        	myData.setText(string2); //on UI thread
        }
        
    }
    
} //FIN actividad D2XXSampleActivity
