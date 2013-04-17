package com.twocities.pomodoro;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.twocities.pomodoro.data.PomodoroClock;

public abstract class ActivityWithReceiver extends Activity {

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		IntentFilter filter = new IntentFilter();
		filter.addAction(PomodoroClock.ACTION_UPDATE);
		registerReceiver(mUpdateReceiver, filter);
		Intent service = new Intent(this, TimerService.class);
		service.setAction(PomodoroClock.KILL_NOTIF);
		this.startService(service);
	}

	@Override
	public void onStop() {
		super.onStop();
		unregisterReceiver(mUpdateReceiver);
		Intent service = new Intent(this, TimerService.class);
		service.setAction(PomodoroClock.SHOW_NOTIF);
		this.startService(service);
	}

	private final BroadcastReceiver mUpdateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			updatePomodoroClock(
					intent.getStringExtra(PomodoroClock.EXTRA_MINUTE),
					intent.getStringExtra(PomodoroClock.EXTRA_SECOND));
		}
	};

	protected abstract void updatePomodoroClock(String minute, String second);

}