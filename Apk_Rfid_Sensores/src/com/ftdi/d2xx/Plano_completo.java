package com.ftdi.d2xx;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class Plano_completo extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		WebView wV1=(WebView)findViewById(R.id.wV1);
		
		String imageUrl =  "file:///android_asset/plano_completo_2copia.png";
		wV1.getSettings().setBuiltInZoomControls(true);
		wV1.loadUrl(imageUrl);
	}

}
