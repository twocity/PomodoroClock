package com.twocities.pomodoro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class StartActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		
		// TODO fade away
		Intent intent = new Intent(this, PomodoroActivity.class);
		this.startActivity(intent);
		this.finish();
	}
}
