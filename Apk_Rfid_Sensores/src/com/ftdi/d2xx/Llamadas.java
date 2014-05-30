package com.ftdi.d2xx;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class Llamadas extends Activity {

	private String num = "911";
	private static final int REQUEST_CHOOSE_PHONE = 10;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.numeros);

		Button boton1 = (Button) findViewById(R.id.button1);
		Button boton2 = (Button) findViewById(R.id.button2);
		Button boton3 = (Button) findViewById(R.id.button3);
		Button boton4 = (Button) findViewById(R.id.button4);
		Button boton5 = (Button) findViewById(R.id.button5);
		Button boton6 = (Button) findViewById(R.id.button6);
		Button boton7 = (Button) findViewById(R.id.button7);

		boton1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				num = "123456789";
				call();
			}
		});
		boton2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				num = "234567890";
				call();
			}
		});
		boton3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				num = "345678901";
				call();
			}
		});
		boton4.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				num = "456789012";
				call();
			}
		});
		boton5.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				num = "567890123";
				call();
			}
		});
		boton6.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				num = "678901234";
				call();
			}
		});
		
		
		
		
		boton7.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent("com.ftdi.d2xx.CONTACTOS");
				startActivityForResult(intent, REQUEST_CHOOSE_PHONE);
				//call();
			}
			
			
		});
		
		
		
		
	}

	
	
	
	private void call() {
		try {
			Intent callIntent = new Intent(Intent.ACTION_CALL);
			callIntent.setData(Uri.parse("tel:" + num.toString().trim()));
			startActivity(callIntent);
		} catch (ActivityNotFoundException activityException) {
			Log.e("helloandroid dialing example", "Call failed",
					activityException);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if ((requestCode == REQUEST_CHOOSE_PHONE)
				&& (resultCode == Activity.RESULT_OK)) {
			try {
				String phone = data.getStringExtra("phone");
				//num = phone;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
}
