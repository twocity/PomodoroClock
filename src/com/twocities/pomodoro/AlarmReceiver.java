package com.twocities.pomodoro;

import com.twocities.pomodoro.data.PomodoroClock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent service = new Intent(context, TimerService.class);
		service.setAction(PomodoroClock.KILL_NOTIF);
		context.startService(service);
		
		Intent alertIntent = new Intent();
		alertIntent.setClass(context, AlertActivity.class);
		alertIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(alertIntent);
	}
}