package com.twocities.pomodoro.widget;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.twocities.pomodoro.R;
import com.twocities.pomodoro.Utils.Log;
import com.twocities.pomodoro.Utils.TimeUtils;

public class DigitalClock extends LinearLayout {
	private TextView mTimeMinutes;
	private TextView mTimeSeconds;
	
	private Ticker mClockTicker;
	private long mLeft;
	
	public DigitalClock(Context context) {
		super(context);
	}
	
	public DigitalClock(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		mTimeMinutes = (TextView) findViewById(R.id.digital_clock_minute);
		mTimeSeconds = (TextView) findViewById(R.id.digital_clock_second);
	}
	
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		stop();
	}
	
	public void start(long duration) {
		Log.v("start clock ticker");
		mClockTicker = null;
		mClockTicker = new Ticker(duration, 500);
		mClockTicker.start();
	}
	
	public void stop() {
		if(mClockTicker != null) {
			Log.v("cancel clock ticker");
			mClockTicker.cancel();
			mClockTicker = null;
		}
	}
	
	/**
	 * Get left time in million seconds
	 * @return
	 */
	public long getLeftMillis() {
		return this.mLeft;
	}
	
	public void updateTime(long left) {
		String minute = TimeUtils.getMinutesLeft(left);
		String second = TimeUtils.getSeconds(left);
		mTimeMinutes.setText(minute + ":");
		mTimeSeconds.setText(second);
	}
	
	private class Ticker extends CountDownTimer {
		
		public Ticker(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			updateTime(millisUntilFinished);
			mLeft = millisUntilFinished;
		}

		@Override
		public void onFinish() {
			Log.v("pomodoro finish");
		}
	}
}