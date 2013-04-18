package com.twocities.pomodoro.data;

import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

import com.twocities.pomodoro.Utils.TimeUtils;

public class PomodoroClock implements Parcelable {
	public static final String ACTION_UPDATE = "com.twocities.pomodoro.ACTION_UPDATE_CLOCK";
	public static final String ACTION_RUN_BACKGROUND = "com.twocities.pomodoro.ACTION_RUN_BACKGROUND";
	public static final String EXTRA_POMODORO = "extra_pomodoro";
	public static final String EXTRA_MINUTE = "extra_minute";
	public static final String EXTRA_SECOND = "extra_second";
	public static final String START_POMODORO = "start_pomodoro";
	public static final String CLOCK_LENGTH = "pomodoro_clock_length";
	public static final String SHOW_NOTIF = "action_show_notification";
	public static final String KILL_NOTIF = "action_kill_notification";
	public static final long DEFAULT_LENGTH = 25* 60 * 1000;

	public static final String KEY_TIME_LEFT = "key_time_left";

	private static final String PREF_ID = "clock_id_";
	private static final String PREF_START_TIME = "start_time_";
	private static final String PREF_END_TIME = "end_time_";
	private static final String PREF_EXPECTED_END_TIME = "expected_end_time_";
	private static final String PREF_LENGTH = "clock_length_";
	private static final String PREF_LEFT = "clock_time_left_";
	private static final String PREF_TITLE = "clock_title_";
	private static final String PREF_DESC = "clock_desc_";
	private static final String PREF_STATE = "clock_state";

	public int mId;
	public long mStartTime;
	public long mEndTime;
	public long mExpectedEndTime;
	public long mClockLength;
	public long mTimeLeft;
	public String mTitle;
	public String mDescription;
	public int mState;

	public static final int STATE_NOT_START = 0;
	public static final int STATE_RUNNING = 1;
	public static final int STATE_STOP_NOT_DONE = 2;
	public static final int STATE_DONE = 3;

	public static final Parcelable.Creator<PomodoroClock> CREATOR = new Parcelable.Creator<PomodoroClock>() {
		@Override
		public PomodoroClock createFromParcel(Parcel p) {
			return new PomodoroClock(p);
		}

		@Override
		public PomodoroClock[] newArray(int size) {
			return new PomodoroClock[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mId);
		dest.writeLong(mStartTime);
		dest.writeLong(mEndTime);
		dest.writeLong(mExpectedEndTime);
		dest.writeLong(mClockLength);
		dest.writeLong(mTimeLeft);
		dest.writeString(mTitle);
		dest.writeString(mDescription);
		dest.writeInt(mState);
	}

	public PomodoroClock(Parcel parcel) {
		this.mId = parcel.readInt();
		this.mStartTime = parcel.readLong();
		this.mEndTime = parcel.readLong();
		this.mExpectedEndTime = parcel.readLong();
		this.mClockLength = parcel.readLong();
		this.mTimeLeft = parcel.readLong();
		this.mTitle = parcel.readString();
		this.mDescription = parcel.readString();
		this.mState = parcel.readInt();
	}

	public void writeInSharedPerefs(SharedPreferences perfs) {
		SharedPreferences.Editor editor = perfs.edit();
		editor.putInt(PREF_ID, this.mId);
		editor.putLong(PREF_START_TIME, mStartTime);
		editor.putLong(PREF_END_TIME, mEndTime);
		editor.putLong(PREF_EXPECTED_END_TIME, mExpectedEndTime);
		editor.putLong(PREF_LENGTH, mClockLength);
		editor.putLong(PREF_LEFT, mTimeLeft);
		editor.putString(PREF_TITLE, mTitle);
		editor.putString(PREF_DESC, mDescription);
		editor.putInt(PREF_STATE, mState);
		editor.apply();
	}

	public void readFromSharedPerefs(SharedPreferences perfs) {
		mId = perfs.getInt(PREF_ID, 0);
		mStartTime = perfs.getLong(PREF_START_TIME, 0);
		mEndTime = perfs.getLong(PREF_END_TIME, 0);
		mExpectedEndTime = perfs.getLong(PREF_EXPECTED_END_TIME, 0);
		mClockLength = perfs.getLong(PREF_LENGTH, 0);
		mTimeLeft = perfs.getLong(PREF_LEFT, 0);
		mDescription = perfs.getString(PREF_DESC, "");
		mTitle = perfs.getString(PREF_TITLE, "");
		mState = perfs.getInt(PREF_STATE, STATE_NOT_START);
	}
	
	public void clearSharedPerefs(SharedPreferences perfs) {
		SharedPreferences.Editor editor = perfs.edit();
		editor.putInt(PREF_ID, 0);
		editor.putLong(PREF_START_TIME, 0);
		editor.putLong(PREF_END_TIME, 0);
		editor.putLong(PREF_EXPECTED_END_TIME, 0);
		editor.putLong(PREF_LENGTH, 0);
		editor.putLong(PREF_LEFT, 0);
		editor.putString(PREF_TITLE, "");
		editor.putString(PREF_DESC, "");
		editor.putInt(PREF_STATE, STATE_NOT_START);
		editor.apply();
	}

	public PomodoroClock(long length) {
		init(length);
	}

	public PomodoroClock() {
		init(0);
	}

	private void init(long length) {
		this.mId = (int) TimeUtils.getTimeNow();
		this.mClockLength = length;
		this.mTimeLeft = length;
		this.mStartTime = TimeUtils.getTimeNow();
		this.mExpectedEndTime = mStartTime + mClockLength;
		this.mState = STATE_RUNNING;
		this.mTitle = "";
		this.mDescription = "";
	}

	public void updateDescription(String description) {
		this.mDescription = description;
	}

	public void updateTitle(String title) {
		this.mTitle = title;
	}
	
	public boolean isRunning() {
		return this.mState == STATE_RUNNING;
	}

	@Override
	public String toString() {
		return "PomodoroClock [mId=" + mId + ", mStartTime=" + mStartTime
				+ ", mEndTime=" + mEndTime + ", mClockLength=" + mClockLength
				+ ", mTimeLeft=" + mTimeLeft + ", mTitle=" + mTitle
				+ ", mDescription=" + mDescription + ", mState=" + mState + "]";
	}
}