package com.twocities.pomodoro.data;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;

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
	public static final long DEFAULT_LENGTH = 25 * 60 * 1000;

	public static final String KEY_TIME_LEFT = "key_time_left";

	private static final String PREF_ID = "clock_id_";
	private static final String PREF_START_TIME = "start_time_";
	private static final String PREF_END_TIME = "end_time_";
	private static final String PREF_EXPECTED_END_TIME = "expected_end_time_";
	private static final String PREF_LENGTH = "clock_length_";
	private static final String PREF_LEFT = "clock_time_left_";
	private static final String PREF_STATE = "clock_state";

	public class Status {
		/**
		 * ` Indicates that the clock has not been executed yet.
		 */
		public static final int PENDING = 1;

		/**
		 * Indicates that the clock is running
		 */
		public static final int RUNNING = 2;

		/**
		 * Indicates that the clock is stop by user, and this clock is not
		 * finish
		 */
		public static final int STOP_NOT_DONE = 3;

		/**
		 * Indicates that the clock is finished.
		 */
		public static final int FINISH = 4;
	}

	public static final class Columns implements BaseColumns {
		public static final String TABLE_NAME = "clocks";
		public static final Uri CONTENT_URI = Uri
				.parse("content://com.twocities.pomodoro/clocks");
		/**
		 * The task id which this clock belongs to
		 * <P>
		 * Type: INTEGER (long)
		 * </P>
		 */
		public static final String CLOCK_TASK_ID = "task_id";

		/**
		 * The start time of this clock
		 * <P>
		 * Type: INTEGER (long)
		 * </P>
		 */
		public static final String CLOCK_START_TIME = "start_time";

		/**
		 * The end time of this clock
		 * <P>
		 * Type: INTEGER (long)
		 * </P>
		 */
		public static final String CLOCK_END_TIME = "end_time";

		/**
		 * The expected end time of this clock
		 * <P>
		 * Type: INTEGER (long)
		 * </P>
		 */
		public static final String CLOCK_EXPECTED_TIME = "expected_time";

		/**
		 * The length this clock
		 * <P>
		 * Type: INTEGER (long)
		 * </P>
		 */
		public static final String CLOCK_LENGTH = "clock_length";

		/**
		 * The time left of this clock
		 * <P>
		 * Type: INTEGER (long)
		 * </P>
		 */
		public static final String CLOCK_TIME_LEFT = "time_left";

		/**
		 * Current status of this clock
		 */
		public static final String CLOCK_STATUS = "clock_state";

		/**
		 * These save calls to cursor.getColumnIndexOrThrow() THEY MUST BE KEPT
		 * IN SYNC WITH ABOVE QUERY COLUMNS
		 */
		public static final int CLOCK_ID_INDEX = 0;
		public static final int CLOCK_TASK_ID_INDEX = 1;
		public static final int CLOCK_START_TIME_INDEX = 2;
		public static final int CLOCK_END_TIME_INDEX = 3;
		public static final int CLOCK_EXPECTED_TIME_INDEX = 4;
		public static final int CLOCK_LENGTH_INDEX = 5;
		public static final int CLOCK_TIME_LEFT_INDEX = 6;
		public static final int CLOCK_STATUS_INDEX = 7;
	}

	public int mId;
	public int mTaskId;
	public long mStartTime;
	public long mEndTime;
	public long mExpectedEndTime;
	public long mClockLength;
	public long mTimeLeft;
	public int mStatus;

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
		dest.writeInt(mTaskId);
		dest.writeLong(mStartTime);
		dest.writeLong(mEndTime);
		dest.writeLong(mExpectedEndTime);
		dest.writeLong(mClockLength);
		dest.writeLong(mTimeLeft);
		dest.writeInt(mStatus);
	}

	public PomodoroClock(Parcel parcel) {
		this.mId = parcel.readInt();
		this.mTaskId = parcel.readInt();
		this.mStartTime = parcel.readLong();
		this.mEndTime = parcel.readLong();
		this.mExpectedEndTime = parcel.readLong();
		this.mClockLength = parcel.readLong();
		this.mTimeLeft = parcel.readLong();
		this.mStatus = parcel.readInt();
	}

	public PomodoroClock(Cursor c) {
		this.mId = c.getInt(Columns.CLOCK_ID_INDEX);
		this.mTaskId = c.getInt(Columns.CLOCK_TASK_ID_INDEX);
		this.mStartTime = c.getLong(Columns.CLOCK_START_TIME_INDEX);
		this.mEndTime = c.getLong(Columns.CLOCK_END_TIME_INDEX);
		this.mExpectedEndTime = c.getLong(Columns.CLOCK_EXPECTED_TIME_INDEX);
		this.mClockLength = c.getLong(Columns.CLOCK_LENGTH_INDEX);
		this.mTimeLeft = c.getLong(Columns.CLOCK_TIME_LEFT_INDEX);
		this.mStatus = c.getInt(Columns.CLOCK_STATUS_INDEX);
	}

	public PomodoroClock(long length) {
		init(length);
	}

	public PomodoroClock() {
		init(0);
	}

	public void writeInSharedPerefs(SharedPreferences perfs) {
		SharedPreferences.Editor editor = perfs.edit();
		editor.putInt(PREF_ID, this.mId);
		editor.putLong(PREF_START_TIME, mStartTime);
		editor.putLong(PREF_END_TIME, mEndTime);
		editor.putLong(PREF_EXPECTED_END_TIME, mExpectedEndTime);
		editor.putLong(PREF_LENGTH, mClockLength);
		editor.putLong(PREF_LEFT, mTimeLeft);
		editor.putInt(PREF_STATE, mStatus);
		editor.apply();
	}

	public void readFromSharedPerefs(SharedPreferences perfs) {
		mId = perfs.getInt(PREF_ID, 0);
		mStartTime = perfs.getLong(PREF_START_TIME, 0);
		mEndTime = perfs.getLong(PREF_END_TIME, 0);
		mExpectedEndTime = perfs.getLong(PREF_EXPECTED_END_TIME, 0);
		mClockLength = perfs.getLong(PREF_LENGTH, 0);
		mTimeLeft = perfs.getLong(PREF_LEFT, 0);
		mStatus = perfs.getInt(PREF_STATE, Status.PENDING);
	}

	public void clearSharedPerefs(SharedPreferences perfs) {
		SharedPreferences.Editor editor = perfs.edit();
		editor.putInt(PREF_ID, 0);
		editor.putLong(PREF_START_TIME, 0);
		editor.putLong(PREF_END_TIME, 0);
		editor.putLong(PREF_EXPECTED_END_TIME, 0);
		editor.putLong(PREF_LENGTH, 0);
		editor.putLong(PREF_LEFT, 0);
		editor.putInt(PREF_STATE, Status.PENDING);
		editor.apply();
	}

	private void init(long length) {
		this.mId = (int) TimeUtils.getTimeNow();
		this.mTaskId = -1;
		this.mClockLength = length;
		this.mTimeLeft = length;
		this.mStartTime = TimeUtils.getTimeNow();
		this.mExpectedEndTime = mStartTime + mClockLength;
		this.mStatus = Status.PENDING;
	}

	public void updateDescription(String description) {
	}

	public void updateTitle(String title) {
	}

	public boolean isRunning() {
		return this.mStatus == Status.RUNNING;
	}

}