package com.twocities.pomodoro;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

import com.twocities.pomodoro.Utils.Log;
import com.twocities.pomodoro.Utils.TimeUtils;
import com.twocities.pomodoro.data.PomodoroClock;
import com.twocities.pomodoro.widget.DigitalClock;

public class AlarmActivity extends Activity {
	private DigitalClock mContentView;
	private TextView mClockTitle;
	private PomodoroClock mClock;
	private SharedPreferences mPrefs;
	
//	@Override
//	public void onNewIntent(Intent newIntent) {
//		Log.v("AlarmActivity @OnNewIntent");
//		setIntent(newIntent);
//	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm);
		mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		mClock = getIntent().getParcelableExtra(PomodoroClock.EXTRA_POMODORO);
		mClock.writeInSharedPerefs(mPrefs);
		Log.v("AlarmActivity: " + mClock.toString());
		mContentView = (DigitalClock) findViewById(R.id.digital_clock);
		mClockTitle = (TextView) findViewById(R.id.title);
		mClockTitle.setText("Write Pomodoro Clock App");
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		Intent service = new Intent(this, TimerService.class);
		service.setAction(PomodoroClock.KILL_NOTIF);
		this.startService(service);
		
		mClock.readFromSharedPerefs(mPrefs);
		mClock.mTimeLeft = mClock.mExpectedEndTime - TimeUtils.getTimeNow(); 
		mContentView.start(mClock.mTimeLeft);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		mClock.writeInSharedPerefs(mPrefs);
	}

	
	@Override
	public void onStop() {
		Log.v("AlarmActivity @onStop");
		super.onStop();
		mContentView.stop();
		Intent service = new Intent(this, TimerService.class);
		service.setAction(PomodoroClock.SHOW_NOTIF);
		service.putExtra(PomodoroClock.EXTRA_POMODORO, mClock);
		this.startService(service);
	}
}