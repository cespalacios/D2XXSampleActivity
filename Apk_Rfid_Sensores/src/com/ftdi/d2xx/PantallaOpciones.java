package com.ftdi.d2xx;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PantallaOpciones extends Activity implements View.OnClickListener{

	Button entrenamiento, evacuacion;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pantallauno);
		initialize();
	}
	
	private void initialize(){
		entrenamiento = (Button)findViewById(R.id.b1pantallauno);
		evacuacion = (Button)findViewById(R.id.b2pantallauno);
		entrenamiento.setOnClickListener(this);
		evacuacion.setOnClickListener(this);
	}
		public void onClick(View view) {
			// TODO Auto-generated method stub
			switch (view.getId()){
			
			case R.id.b2pantallauno:
				Intent menu1Intent = new Intent(this, Menuprincipal.class);
				startActivity(menu1Intent);
				break;
			case R.id.b1pantallauno:
				Intent menu2Intent = new Intent(this, ConsumirWSActivity.class);
				startActivity(menu2Intent);
				break;
				}
		}

	}
	