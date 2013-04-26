package com.twocities.pomodoro.data;

import com.twocities.pomodoro.Utils.TimeUtils;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

public class DataBaseInit {

	private static boolean isInit = false;

	private DataBaseInit() {
	}

	public static void Init(Context context) {
		if (isInit) {
			return;
		}
		isInit = true;
		DataBaseHelper helper = new DataBaseHelper(context);
		new InitTask().execute(helper);
	}

	private static class InitTask extends AsyncTask<DataBaseHelper, Void, Void> {

		@Override
		protected Void doInBackground(DataBaseHelper... params) {
			long now = TimeUtils.getTimeNow();
			DataBaseHelper helper = params[0];
			for (int i=0; i<20; ++i) {
				ContentValues values = new ContentValues();
//				values.put(DataConstract.TaskColumns.TASK_ID, now);
				values.put(DataConstract.TaskColumns.TITLE, "new title " + i);
				values.put(DataConstract.TaskColumns.DESCRIPTION, "new description " + i);
				values.put(DataConstract.TaskColumns.TAGS, "tag " + 1);
				values.put(DataConstract.TaskColumns.START_DATE, now);
				values.put(DataConstract.TaskColumns.DUE_DATE, now + 10000);
				helper.insertEvent(values);
			}
			return null;
		}
	}
}