package com.ces.mislugares;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pantallauno);
		
		
		final Button btnMislugares = (Button)findViewById(R.id.button1);
		final Button btnSalir = (Button)findViewById(R.id.salir);
		
		//Implementamos el evento “click” del botón
        btnMislugares.setOnClickListener(new OnClickListener() {
            
        	public void onClick(View v) {
                  //Creamos el Intent
                  Intent intent =
                          new Intent(MainActivity.this, Btnmislugares.class);
                  startActivity(intent);
             }
        });
        
      //Implementamos el evento “click” del botón
        btnSalir.setOnClickListener(new OnClickListener() {
            
        	public void onClick(View v) {
                 
        		finish();
             }
        });
        
  

		}
}
