package com.twocities.pomodoro;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.preference.PreferenceManager;

import com.twocities.pomodoro.Utils.Log;
import com.twocities.pomodoro.Utils.TimeUtils;
import com.twocities.pomodoro.data.PomodoroClock;

public class TimerService extends Service {
	private static final int CLOCK_NOTIFICATION_ID = Integer.MAX_VALUE - 1;
	private NotificationManager mNotificationManager;
	private Ticker mTicker = null;
	private boolean updateNotification = true;
	private PomodoroClock clock;
	
	@Override
	public void onCreate() {
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent == null) {
			return START_NOT_STICKY;
		}
		String actionType = intent.getAction();
		if(actionType.equals(PomodoroClock.KILL_NOTIF)) {
			if(mTicker != null) mTicker.cancel();
			updateNotification = false;
			mNotificationManager.cancel(CLOCK_NOTIFICATION_ID);
		}else if(actionType.equals(PomodoroClock.ACTION_RUN_BACKGROUND)) {
		}else if(actionType.equals(PomodoroClock.SHOW_NOTIF)) {
			updateNotification = true;
			clock = intent.getParcelableExtra(PomodoroClock.EXTRA_POMODORO);
			clock.mTimeLeft = clock.mExpectedEndTime - TimeUtils.getTimeNow(); 
			Log.v("TimerService:" + clock.toString());
			if(mTicker != null) {
				mTicker.cancel();
			}
			mTicker = new Ticker(clock.mTimeLeft, 500);
			mTicker.start();
		}
		return START_STICKY;
	}
	
	private void setNotification(long left) {
		clock.writeInSharedPerefs(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
		String minutesLeft = TimeUtils.getMinutesLeft(left);
		String secondsLeft = TimeUtils.getSeconds(left);
		StringBuffer timeLeft = new StringBuffer(minutesLeft);
		timeLeft.append(":").append(secondsLeft);
		Context context = getApplicationContext();
		Intent intent = new Intent(context, ScreenAlarmActivity.class);
		intent.putExtra(PomodoroClock.EXTRA_POMODORO, clock);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, PendingIntent.FLAG_ONE_SHOT
						| PendingIntent.FLAG_UPDATE_CURRENT);
		Notification notification = new Notification.Builder(context)
				.setAutoCancel(false)
				.setContentIntent(pendingIntent)
				.setContentTitle("Pomodoro")
				.setContentText(timeLeft.toString())
				.setOngoing(true)
				.setSmallIcon(R.drawable.stat_notify_alarm)
				.setPriority(Notification.PRIORITY_MAX).build();
		mNotificationManager.notify(CLOCK_NOTIFICATION_ID, notification);
	}
	

	@Override
	public void onDestroy() {
		mNotificationManager.cancel(CLOCK_NOTIFICATION_ID);
		if(mTicker != null) {
			mTicker.cancel();
			mTicker = null;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	private class Ticker extends CountDownTimer {
		
		public Ticker(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			clock.mTimeLeft = millisUntilFinished;
			if(updateNotification) {
				setNotification(millisUntilFinished);
			}
		}

		@Override
		public void onFinish() {
			mNotificationManager.cancel(CLOCK_NOTIFICATION_ID);
		}
	}
}