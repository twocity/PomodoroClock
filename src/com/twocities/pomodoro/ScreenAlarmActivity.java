package com.twocities.pomodoro;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorInflater;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.WindowManager;

import com.twocities.pomodoro.Utils.Log;
import com.twocities.pomodoro.Utils.TimeUtils;
import com.twocities.pomodoro.data.PomodoroClock;
import com.twocities.pomodoro.widget.DigitalClock;

public class ScreenAlarmActivity extends Activity {
	private DigitalClock mContentView;
	private final int mFlags = (WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
			| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
			| WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	
	private boolean startService = true;
	private PomodoroClock mClock;
	private SharedPreferences mPrefs;
	

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_screenalarm);
		mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		mClock = getIntent().getParcelableExtra(PomodoroClock.EXTRA_POMODORO);
		mClock.writeInSharedPerefs(mPrefs);
		Log.v("ScreenAlarmActivity: " + mClock.toString());
		AlarmController.startAlarm(getApplicationContext(), mClock.mClockLength);
		initViews();
	}

	private void initViews() {
		mContentView = (DigitalClock) findViewById(R.id.digital_clock);

		mContentView.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				startService = false;
				Intent intent = new Intent(ScreenAlarmActivity.this,
						AlarmActivity.class);
				mClock.mTimeLeft = mClock.mExpectedEndTime - TimeUtils.getTimeNow(); 
				intent.putExtra(PomodoroClock.EXTRA_POMODORO, mClock);
				ScreenAlarmActivity.this.startActivity(intent);
				ScreenAlarmActivity.this.finish();
				return true;
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		Intent service = new Intent(this, TimerService.class);
		service.setAction(PomodoroClock.KILL_NOTIF);
		this.startService(service);
		
		mClock.readFromSharedPerefs(mPrefs);
		// update clock left length.
		mClock.mTimeLeft = mClock.mExpectedEndTime - TimeUtils.getTimeNow(); 
		layoutClockSaver();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		mClock.writeInSharedPerefs(mPrefs);
	}

	private void layoutClockSaver() {
		mContentView.forceLayout();
		mContentView.setAlpha(0);
		setContentVisible();
		Animator fadeIn = AnimatorInflater.loadAnimator(getApplicationContext(),
                R.anim.fade_in);
		fadeIn.addListener(new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animator animation) {
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				mContentView.start(mClock.mTimeLeft);
			}
			
			@Override
			public void onAnimationCancel(Animator animation) {
				
			}
		});
		fadeIn.setTarget(mContentView);
		fadeIn.start();
		setContentVisible();
	}

	private void setContentVisible() {
		int newVis = View.SYSTEM_UI_FLAG_LOW_PROFILE
				| View.SYSTEM_UI_FLAG_FULLSCREEN
				| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
		setWakeLock(true);
		mContentView.setSystemUiVisibility(newVis);
	}

	private void setWakeLock(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		winParams.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
		if (on)
			winParams.flags |= mFlags;
		else
			winParams.flags &= (~mFlags);
		win.setAttributes(winParams);
	}
	
	
	@Override
	public void onStop() {
		super.onStop();
		mContentView.stop();
		if(startService) {
			Intent service = new Intent(this, TimerService.class);
			service.setAction(PomodoroClock.SHOW_NOTIF);
			service.putExtra(PomodoroClock.EXTRA_POMODORO, mClock);
			this.startService(service);
		}
		startService = true;
	}
}