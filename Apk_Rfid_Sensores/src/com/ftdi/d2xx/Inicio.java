package com.ftdi.d2xx;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Inicio extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pantinicial);
		
		Thread timer = new Thread(){
			public void run(){
				try{
					sleep(1500);					
				} catch (InterruptedException e){
					e.printStackTrace();
				}finally{
					Intent openStartingPoint = new Intent("com.ftdi.d2xx.PANTALLAOPCIONES");
					
					startActivity(openStartingPoint);
				}
			}
		};
		timer.start();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
		
	}		

}
