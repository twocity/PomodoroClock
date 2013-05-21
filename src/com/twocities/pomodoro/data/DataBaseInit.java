package com.twocities.pomodoro.data;

import java.util.Calendar;
import java.util.TimeZone;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.twocities.pomodoro.provider.TaskConstract;

public class DataBaseInit {

	private static boolean isInit = false;

	private DataBaseInit() {
	}

	public static void Init(Context context) {
		isInit = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext()).getBoolean("is_init", false);
		if (isInit) {
			return;
		}
		isInit = true;
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext()).edit();
		editor.putBoolean("is_init", true);
		editor.commit();
		DataBaseHelper helper = new DataBaseHelper(context);
		new InitTask().execute(helper);
	}

	private static class InitTask extends AsyncTask<DataBaseHelper, Void, Void> {

		@Override
		protected Void doInBackground(DataBaseHelper... params) {
			long now = Calendar.getInstance(TimeZone.getDefault()).getTimeInMillis();
			long dayLength = 24 * 60 * 60 * 1000;
			DataBaseHelper helper = params[0];
			// yesterday
			ContentValues yesterday = new ContentValues();
			yesterday.put(TaskConstract.Columns.TITLE, "yesterday");
			yesterday.put(TaskConstract.Columns.DESCRIPTION, "This ia Task of yestoday");
			yesterday.put(TaskConstract.Columns.CREATE_TIME, now - dayLength - 60*1000);
			yesterday.put(TaskConstract.Columns.REMINDER_TIME, now - dayLength);
			yesterday.put(TaskConstract.Columns.DUE_TIME, now + 3000);
			yesterday.put(TaskConstract.Columns.FLAG_DONE, 0);
			yesterday.put(TaskConstract.Columns.FLAG_DEL, 0);
			yesterday.put(TaskConstract.Columns.FLAG_EMERGENCY, 0);
			helper.insertEvent(yesterday);
			// today
			for (int i=0;i<30;++i) {
				ContentValues today = new ContentValues();
				today.put(TaskConstract.Columns.TITLE, "today" + i);
				today.put(TaskConstract.Columns.DESCRIPTION, "This ia Task of today");
				today.put(TaskConstract.Columns.CREATE_TIME, now - dayLength - 60*1000);
				today.put(TaskConstract.Columns.REMINDER_TIME, now );
				today.put(TaskConstract.Columns.DUE_TIME, now + 3000);
				today.put(TaskConstract.Columns.FLAG_DONE, 0);
				today.put(TaskConstract.Columns.FLAG_DEL, 0);
				today.put(TaskConstract.Columns.FLAG_EMERGENCY, 1);
				helper.insertEvent(today);
			}

			// tomorrow
			ContentValues tomorrow = new ContentValues();
			tomorrow.put(TaskConstract.Columns.TITLE, "tomorrow");
			tomorrow.put(TaskConstract.Columns.DESCRIPTION, "This ia Task of tomorrow");
			tomorrow.put(TaskConstract.Columns.CREATE_TIME, now - dayLength - 60*1000);
			tomorrow.put(TaskConstract.Columns.REMINDER_TIME, now + dayLength );
			tomorrow.put(TaskConstract.Columns.DUE_TIME, now + dayLength + 3000);
			tomorrow.put(TaskConstract.Columns.FLAG_DONE, 0);
			tomorrow.put(TaskConstract.Columns.FLAG_DEL, 0);
			tomorrow.put(TaskConstract.Columns.FLAG_EMERGENCY, 1);
			helper.insertEvent(tomorrow);
			return null;
		}
	}
}