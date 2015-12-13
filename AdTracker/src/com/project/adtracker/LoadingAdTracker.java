package com.project.adtracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class LoadingAdTracker  extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading_activity);
		
		Handler handler = new Handler();
		handler.postDelayed(new Runnable(){
			public void run(){
				Intent intent = new Intent(LoadingAdTracker.this,MapActivity.class);
				startActivity(intent);
				finish();
			}
		},2000);
	}
}
