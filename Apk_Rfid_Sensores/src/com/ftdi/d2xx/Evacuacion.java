package com.ftdi.d2xx;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Evacuacion extends Activity {
    /** Called when the activity is first created. */
    
	public boolean onCreateOptionsMenu(Menu menu){
		//Alternativa 1
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_principal, menu);
		return true;
	
	}
		public boolean onOptionsItemSelected(MenuItem item) {
			
			switch (item.getItemId()){
				case R.id.MOp1:
					Intent ourIntent = new Intent(this, Menuprincipal.class);
					startActivity(ourIntent);
					
					return true;
				case R.id.MOp2:
					Intent ourIntent4 = new Intent(this, Evacuacion.class);
					startActivity(ourIntent4);
					return true;
				case R.id.MOp3:
					finish();
					return true;
				default:
						return super.onOptionsItemSelected(item);
			}
		
		
	}
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ubicacion);
        
        Button OK = (Button)findViewById(R.id.b1);
        final ImageView lugar = (ImageView)findViewById(R.id.imageView1);
        
        
       OK.setOnClickListener(new View.OnClickListener() {
    	int contador = 0;
		public void onClick(View v) {
			// TODO Auto-generated method stub
			contador++;
			if (contador==1 ){
				
				
			}else if (contador==2 ){
				
				
			}else if (contador==3){
				
			}
			if (contador==4 ){
				
				
			}else if (contador==5 ){
				
				
			}else if (contador==6){
				
				contador = 0;
			}else if (contador==7){
				
				
			}else if (contador==8){
			
				contador = 0;
			}
		}
	});}
       
       public void createNotification(View view){
    	   NotificationManager notificationManager = (NotificationManager) 
					getSystemService(NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.ic_launcher,
				"Nueva Notificación", System.currentTimeMillis());
		// Hide the notification after its selected
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		Intent intent = new Intent(this, ReceptordeNotificacion.class);
		PendingIntent activity = PendingIntent.getActivity(this, 0, intent, 0);
		notification.setLatestEventInfo(this, "Salida de emergencia más cercana",
				"Siga las indicaciones", activity);
		notification.number += 1;
		notificationManager.notify(0, notification);
       }
		protected void onPause() {
				// TODO Auto-generated method stub
				super.onPause();
				finish();
				/**Intent ourIntent2 = new Intent(this, Menuprincipal.class);
				startActivity(ourIntent2);*/
		}
		
}
    
