package com.twocities.pomodoro.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.twocities.pomodoro.Utils.Log;
import com.twocities.pomodoro.provider.TaskConstract;

public class DataBaseHelper extends SQLiteOpenHelper {
	private static final String NAME = "pomodoro.db";
	private static final int VERSION = 1;
	public static final String TASKS_TABLE = TaskConstract.Columns.TASKS_TABLE;
	private static final String CREATE_TASKS_TABLE = "CREATE TABLE "
			+ TASKS_TABLE + " ( " + TaskConstract.Columns._ID
			+ " LONG PRIMARY KEY," + TaskConstract.Columns.TITLE
			+ " TEXT, " + TaskConstract.Columns.DESCRIPTION + " TEXT, "
			+ TaskConstract.Columns.TAGS + " TEXT, "
			+ TaskConstract.Columns.START_DATE + " LONG, "
			+ TaskConstract.Columns.DUE_DATE + " LONG);";

	public DataBaseHelper(final Context context) {
		super(context, NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TASKS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TASKS_TABLE);
		onCreate(db);
	}

	public long insertEvent(ContentValues values) {
		SQLiteDatabase db = getWritable();
		if (db == null) {
			return -1;
		}
		return db.insert(TASKS_TABLE, null, values);
	}
	
	public Cursor queryEvent(String[] columns, String selection,
            String[] selectionArgs, String groupBy, String having,
            String orderBy) {
		Log.v("query event table");
		SQLiteDatabase db = getReadable();
		if (db == null) {
			return null;
		}
		return db.query(TASKS_TABLE, columns, selection, selectionArgs, groupBy, having, orderBy);
	}
	
	public Cursor queryEvent() {
		return queryEvent(null, null, null, null, null, null);
	}

	/**
	 * Get readable database
	 * 
	 * @return readable database or null if it failed to create/open
	 */
	public SQLiteDatabase getReadable() {
		try {
			return getReadableDatabase();
		} catch (SQLiteException e1) {
			// Make second attempt
			try {
				return getReadableDatabase();
			} catch (SQLiteException e2) {
				return null;
			}
		}
	}

	/**
	 * Get writable database
	 * 
	 * @return writable database or null if it failed to create/open
	 */
	public SQLiteDatabase getWritable() {
		try {
			return getWritableDatabase();
		} catch (SQLiteException e1) {
			// Make second attempt
			try {
				return getWritableDatabase();
			} catch (SQLiteException e2) {
				return null;
			}
		}
	}

}