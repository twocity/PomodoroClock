package com.twocities.pomodoro.data;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import com.twocities.pomodoro.Utils.TimeUtils;
import com.twocities.pomodoro.provider.TaskConstract;

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
				values.put(TaskConstract.Columns.TITLE, "new title " + i);
				values.put(TaskConstract.Columns.DESCRIPTION, "new description " + i);
				values.put(TaskConstract.Columns.TAGS, "tag " + 1);
				values.put(TaskConstract.Columns.START_DATE, now);
				values.put(TaskConstract.Columns.DUE_DATE, now + 10000);
				helper.insertEvent(values);
			}
			return null;
		}
	}
}