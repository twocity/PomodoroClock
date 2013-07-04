package com.twocities.pomodoro.widget;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.twocities.pomodoro.R;
import com.twocities.pomodoro.Utils.Log;
import com.twocities.pomodoro.Utils.TimeUtils;
import com.twocities.pomodoro.data.PomodoroClock;
import com.twocities.pomodoro.data.PomodoroClock.Status;

public class DigitalClock extends LinearLayout {
	private TextView mTimeMinutes;
	private TextView mTimeSeconds;

	private onClockTickerListener listener;
	private PomodoroClock mClock = null;
	private Ticker mClockTicker;
	private long mLeft;
	private long mClockLength;

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

	public void setOnClockTickerListener(onClockTickerListener l) {
		this.listener = l;
	}

	public void start(long duration) {
		mClock = new PomodoroClock(duration);
		mClock.mStatus = Status.RUNNING;
		startTicker(duration);
	}

	public void start(PomodoroClock clock) {
		mClock = clock;
		mClock.updateStatus(Status.RUNNING);
		startTicker(mClock.getClockLength());
	}

	/**
	 * Start a {@code CountDownTimer} with the given duration
	 * 
	 * @param duration
	 *            in in million seconds
	 */
	private void startTicker(long duration) {
		mClockLength = duration;
		if (mClockTicker != null && !mClockTicker.isFinished()) {
			stop();
		}
		mClockTicker = new Ticker(duration, 500);
		mClockTicker.start();
		if (listener != null) {
			listener.onClockStart();
		}
	}

	public void stop() {
		if (mClockTicker == null)
			return;
		Log.v("cancel clock ticker");
		mClockTicker.cancel();

		// update clock status
		boolean finished = mClockTicker.isFinished();
		int status = finished ? Status.FINISH : Status.STOP_NOT_DONE;
		mClock.updateStatus(status);
		mClock.mEndTime = TimeUtils.getTimeNow();
		mClock.mTimeLeft = mLeft;
		mClockTicker = null;
		if (listener != null && !finished) {
			listener.onClockCancel();
		}
		reset(mClockLength);
	}

	public PomodoroClock getClock() {
		return this.mClock;
	}

	/**
	 * Get left time in million seconds
	 */
	public long getLeftMillis() {
		return this.mLeft;
	}

	/**
	 * whether the clock is running
	 * 
	 * @return true if the clock is running.
	 */
	public boolean isRuning() {
		if (mClock != null) {
			return mClock.isRunning();
		}
		return false;
	}

	private void updateTime(long left) {
		String minute = TimeUtils.getMinutesLeft(left);
		String second = TimeUtils.getSeconds(left);
		mTimeMinutes.setText(minute + ":");
		mTimeSeconds.setText(second);
	}

	public void reset(long duration) {
		updateTime(duration);
	}

	private class Ticker extends CountDownTimer {

		boolean isFinish = false;

		public Ticker(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			updateTime(millisUntilFinished);
			mLeft = millisUntilFinished;
			if (listener != null) {
				listener.onClockTick(millisUntilFinished);
			}
		}

		@Override
		public void onFinish() {
			isFinish = true;
			mLeft = 0;
			if (listener != null) {
				listener.onClockFinish();
			}
		}

		public boolean isFinished() {
			return isFinish;
		}
	}

	/**
	 * Interface for clock behavior
	 */
	public interface onClockTickerListener {

		/**
		 * Called when clock start
		 */
		public void onClockStart();

		/**
		 * Called when clock canceled(but not finished)
		 */
		public void onClockCancel();

		/**
		 * Called when clock is finished
		 */
		public void onClockFinish();

		public void onClockTick(long millisUntilFinished);
	}
}