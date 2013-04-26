package com.twocities.pomodoro;

import com.twocities.pomodoro.data.DataBaseInit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class StartActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		DataBaseInit.Init(getApplicationContext());
		// TODO fade away
		Intent intent = new Intent(this, HomeActivity.class);
		this.startActivity(intent);
		this.finish();
	}
}
