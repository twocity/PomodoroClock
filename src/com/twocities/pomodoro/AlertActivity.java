package com.twocities.pomodoro;

import com.twocities.pomodoro.data.PomodoroClock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

public class AlertActivity extends Activity {
	
	private TextView mComplete;
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_alert);
		mComplete = (TextView) findViewById(R.id.complete);
		mComplete.getRootView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
		clearClock();
	}
	
	public void haveBreak(View v) {
		Intent intent  = new Intent(this,PomodoroActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.startActivity(intent);
		this.finish();
	}
	
	private void clearClock() {
		PomodoroClock clock = new PomodoroClock();
		clock.readFromSharedPerefs(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
		clock.clearSharedPerefs(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
	}
}