package com.ftdi.d2xx;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Plan extends Activity implements View.OnClickListener{

	Button llamar, evacuar;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reco_evacuacion);
		
		 initialize();
	}
		 
	private void initialize(){
		llamar = (Button)findViewById(R.id.Bemergencia);
		evacuar = (Button)findViewById(R.id.Bevacuation);
		llamar.setOnClickListener(this);
		evacuar.setOnClickListener(this);
				
	}
	
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()){
		
		case R.id.Bevacuation:
			Intent menu1Intent = new Intent(this, Evacuacion.class);
			startActivity(menu1Intent);
			break;
		case R.id.Bemergencia:
			Intent menu2Intent = new Intent(this, Llamadas.class);
			startActivity(menu2Intent);
			break;
	
	}
			
				
			}

		}