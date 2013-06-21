package com.twocities.pomodoro.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.twocities.pomodoro.provider.TaskConstract;

public class Task implements Parcelable {
	public static final String EXTRA_TASK_DATA = "extra_task_data";
	private int _id;
	private String title;
	private String description;
	private long createTime;
	private long reminderTime;
	private long dueTime;
	private long completeTime;
	/**
	 * 1 means done
	 */
	private int flagDone;

	/**
	 * 1 means deleted
	 */
	private int flagDel;

	/**
	 * 1 means is emergency
	 */
	private int flagEmergency;

	/**
	 * 1 means task showing in today list
	 */
	private int flagInToday;

	public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {
		@Override
		public Task createFromParcel(Parcel p) {
			return new Task(p);
		}

		@Override
		public Task[] newArray(int size) {
			return new Task[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	public Task() {
		this._id = -1;
		this.title = null;
		this.description = null;
		this.createTime = 0;
		this.reminderTime = 0;
		this.dueTime = 0;
		this.completeTime = 0;
		this.flagDone = 0;
		this.flagDel = 0;
		this.flagEmergency = 0;
		this.flagInToday = 0;
	}

	public Task(Parcel p) {
		this._id = p.readInt();
		this.title = p.readString();
		this.description = p.readString();
		this.createTime = p.readLong();
		this.reminderTime = p.readLong();
		this.dueTime = p.readLong();
		this.completeTime = p.readLong();
		this.flagDone = p.readInt();
		this.flagDel = p.readInt();
		this.flagEmergency = p.readInt();
		this.flagInToday = p.readInt();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(_id);
		dest.writeString(title);
		dest.writeString(description);
		dest.writeLong(createTime);
		dest.writeLong(reminderTime);
		dest.writeLong(dueTime);
		dest.writeLong(completeTime);
		dest.writeInt(flagDone);
		dest.writeInt(flagDel);
		dest.writeInt(flagEmergency);
		dest.writeInt(flagInToday);
	}

	/**
	 * Read task info from cursor
	 * 
	 * @param cursor
	 * @return
	 */
	public static Task CreateFromCursor(Cursor cursor) {
		Task task = new Task();
		if (cursor == null || cursor.getCount() == 0) {
			return null;
		}
		task.setId(cursor.getInt(TaskConstract.Columns.TASK_ID_INDEX));
		task.setTitle(cursor.getString(TaskConstract.Columns.TASK_TITLE_INDEX));
		task.setDescription(cursor
				.getString(TaskConstract.Columns.TASK_DESCRIPTION_INDEX));
		task.setCreateTime(cursor
				.getLong(TaskConstract.Columns.TASK_CREATE_TIME_INDEX));
		task.setReminderTime(cursor
				.getLong(TaskConstract.Columns.TASK_REMINDER_TIME_INDEX));
		task.setDueTime(cursor
				.getLong(TaskConstract.Columns.TASK_DUE_TIME_INDEX));
		task.setCompleteTime(cursor
				.getLong(TaskConstract.Columns.TASK_COMPLETE_TIME_INDEX));
		task.setFlagDone(cursor
				.getInt(TaskConstract.Columns.TASK_FLAG_DONE_INDEX));
		task.setFlagDel(cursor
				.getInt(TaskConstract.Columns.TASK_FLAG_DONE_INDEX));
		task.setFlagEmergency(cursor
				.getInt(TaskConstract.Columns.TASK_FLAG_EMER_INDEX));
		task.setFlagToday(cursor.getInt(TaskConstract.Columns.TASK_FLAG_TODAY));
		return task;
	}

	public ContentValues createContentValues() {
		ContentValues values = new ContentValues();
		return values;
	}

	public int getId() {
		return _id;
	}

	public void setId(int _id) {
		this._id = _id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getReminderTime() {
		return reminderTime;
	}

	public void setReminderTime(long reminderTime) {
		this.reminderTime = reminderTime;
	}

	public long getDueTime() {
		return dueTime;
	}

	public void setDueTime(long dueTime) {
		this.dueTime = dueTime;
	}

	public long getCompleteTime() {
		return completeTime;
	}

	public void setCompleteTime(long completeTime) {
		this.completeTime = completeTime;
	}

	public int getFlagDone() {
		return flagDone;
	}

	public void setFlagDone(int flagDone) {
		this.flagDone = flagDone;
	}

	public int getFlagDel() {
		return flagDel;
	}

	public void setFlagDel(int flagDel) {
		this.flagDel = flagDel;
	}

	public int getFlagEmergency() {
		return flagEmergency;
	}

	public void setFlagEmergency(int flagEmergency) {
		this.flagEmergency = flagEmergency;
	}

	public int getFlagToday() {
		return this.flagInToday;
	}

	public void setFlagToday(int flagToday) {
		this.flagInToday = flagToday;
	}

	public static Parcelable.Creator<Task> getCreator() {
		return CREATOR;
	}
}