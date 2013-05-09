package com.twocities.pomodoro.data;

import com.twocities.pomodoro.provider.TaskConstract;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

public class Task implements Parcelable {
	public static final String EXTRA_TASK_DATA = "extra_task_data";
	private int _id;
	private String title;
	private String description;
	private long createTime;
	private long reminderTime;
	private long dueTime;
	private long completeTime;
	private int flagDone;
	private int flagDel;
	private int flagEmergency;

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
	}

	/**
	 * Read task info from cursor
	 * @param cursor
	 * @return
	 */
	public static Task CreateFromCursor(Cursor cursor) {
		Task task = new Task();
		if (cursor == null || cursor.getCount() == 0) {
			return null;
		}
		task.set_id(cursor.getInt(TaskConstract.Columns.TASK_ID_INDEX));
		task.setTitle(cursor.getString(TaskConstract.Columns.TASK_TITLE_INDEX));
		task.setDescription(cursor.getString(TaskConstract.Columns.TASK_DESCRIPTION_INDEX));
		task.setCreateTime(cursor.getLong(TaskConstract.Columns.TASK_CREATE_TIME_INDEX));
		task.setReminderTime(cursor.getLong(TaskConstract.Columns.TASK_REMINDER_TIME_INDEX));
		task.setDueTime(cursor.getLong(TaskConstract.Columns.TASK_DUE_TIME_INDEX));
		task.setCompleteTime(cursor.getLong(TaskConstract.Columns.TASK_COMPLETE_TIME_INDEX));
		task.setFlagDone(cursor.getInt(TaskConstract.Columns.TASK_FLAG_DONE_INDEX));
		task.setFlagDel(cursor.getInt(TaskConstract.Columns.TASK_FLAG_DONE_INDEX));
		task.setFlagEmergency(cursor.getInt(TaskConstract.Columns.TASK_FLAG_EMER_INDEX));
		return task;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
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

	public static Parcelable.Creator<Task> getCreator() {
		return CREATOR;
	}
}