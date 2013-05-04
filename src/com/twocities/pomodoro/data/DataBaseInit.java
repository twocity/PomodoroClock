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
			ContentValues first = new ContentValues();
			first.put(TaskConstract.Columns.TITLE, "Hello World");
			first.put(TaskConstract.Columns.DESCRIPTION, "Welcome to use this wanderfull tool");
			first.put(TaskConstract.Columns.TAGS, "tag0000 ");
			first.put(TaskConstract.Columns.START_DATE, now);
			first.put(TaskConstract.Columns.DUE_DATE, now + 10000);
			helper.insertEvent(first);
			for (int i=0; i<20; ++i) {
				ContentValues values = new ContentValues();
				values.put(TaskConstract.Columns.TITLE, "new title " + i);
				values.put(TaskConstract.Columns.DESCRIPTION, "new description " + i);
				values.put(TaskConstract.Columns.TAGS, "tag " + i);
				values.put(TaskConstract.Columns.START_DATE, now);
				values.put(TaskConstract.Columns.DUE_DATE, now + 10000);
				helper.insertEvent(values);
			}
			return null;
		}
	}
}