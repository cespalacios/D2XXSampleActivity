package com.ftdi.d2xx;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class Menuprincipal extends Activity implements View.OnClickListener{

	Button Menu1, Menu2, Menu3, Menu4, Menu5, Menu6;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menuprinc);
		initialize();
	}
	
	private void initialize(){
		Menu1 = (Button)findViewById(R.id.iB1);
		Menu2 = (Button)findViewById(R.id.iB2);
		Menu3 = (Button)findViewById(R.id.iB3);
		Menu4 = (Button)findViewById(R.id.iB4);
		//Menu5 = (Button)findViewById(R.id.iB5);
		Menu6 = (Button)findViewById(R.id.iB6);
		Menu1.setOnClickListener(this);
		Menu2.setOnClickListener(this);
		Menu3.setOnClickListener(this);
		Menu4.setOnClickListener(this);
		//Menu5.setOnClickListener(this);
		Menu6.setOnClickListener(this);
		
	}
	
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()){
		
		case R.id.iB1:
			Intent menu1Intent = new Intent(this, D2XXSampleActivity.class);
			startActivity(menu1Intent);
			break;
		case R.id.iB2:
			Intent menu2Intent = new Intent(this, Llamadas.class);
			startActivity(menu2Intent);
			break;
		case R.id.iB3:
			Intent menu3Intent = new Intent(this, HorizontalPagerDemo.class);
			startActivity(menu3Intent);
			break;
		case R.id.iB4:
			Intent menu4Intent = new Intent(this, Plan.class);
			startActivity(menu4Intent);
			break;
		case R.id.iB6:
			finish();
			
			break;
	}
	}
	
	

}
