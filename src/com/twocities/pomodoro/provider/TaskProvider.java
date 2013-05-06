package com.twocities.pomodoro.provider;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.twocities.pomodoro.Utils.Log;
import com.twocities.pomodoro.data.DataBaseHelper;

public class TaskProvider extends ContentProvider {
	private static final String TAG = "TaskProvider";

	/**
	 * Standard projection for a normal task
	 */
	public static final String[] READ_TASK_PROJECTION = new String[] {
			TaskConstract.Columns._ID, TaskConstract.Columns.TITLE,
			TaskConstract.Columns.DESCRIPTION, 
			TaskConstract.Columns.REMINDER_TIME, TaskConstract.Columns.DUE_TIME };

	/**
	 * A projection map used to select columns from the database
	 */
	private static HashMap<String, String> sTasksProjectionMap;

	/*
	 * Constants used by the Uri matcher to choose an action based on the
	 * pattern of the incoming URI
	 */
	// The incoming URI matches the Notes URI pattern
	private static final int TASKS = 1;

	// The incoming URI matches the Note ID URI pattern
	private static final int TASK_ID = 2;

	/**
	 * A UriMatcher instance
	 */
	private static final UriMatcher sUriMatcher;

	// Handle to a new DatabaseHelper.
	private DataBaseHelper mOpenHelper;

	/**
	 * A block that instantiates and sets static objects
	 */
	static {

		/*
		 * Creates and initializes the URI matcher
		 */
		// Create a new instance
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

		// Add a pattern that routes URIs terminated with "tasks" to a TASKS
		// operation
		sUriMatcher.addURI(TaskConstract.AUTHORITY, "tasks", TASKS);

		// Add a pattern that routes URIs terminated with "tasks" plus an
		// integer
		// to a task ID operation
		sUriMatcher.addURI(TaskConstract.AUTHORITY, "tasks/#", TASK_ID);

		/*
		 * Creates and initializes a projection map that returns all columns
		 */

		// Creates a new projection map instance. The map returns a column name
		// given a string. The two are usually equal.
		sTasksProjectionMap = new HashMap<String, String>();

		// Maps the string "_ID" to the column name "_ID"
		sTasksProjectionMap.put(TaskConstract.Columns._ID,
				TaskConstract.Columns._ID);

		// Maps "title" to "title"
		sTasksProjectionMap.put(TaskConstract.Columns.TITLE,
				TaskConstract.Columns.TITLE);

		// Maps "description" to "description"
		sTasksProjectionMap.put(TaskConstract.Columns.DESCRIPTION,
				TaskConstract.Columns.DESCRIPTION);

	}

	@Override
	public boolean onCreate() {
		Log.v(TAG, "@onCreate()");
		mOpenHelper = new DataBaseHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		// Constructs a new query builder and sets its table name
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(TaskConstract.Columns.TASKS_TABLE);

		switch (sUriMatcher.match(uri)) {
		case TASKS:
//			qb.setProjectionMap(sTasksProjectionMap);
			break;
		case TASK_ID:
			qb.appendWhere(TaskConstract.Columns._ID + "="
					+ uri.getLastPathSegment());
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		SQLiteDatabase db = mOpenHelper.getReadable();

		/*
		 * Performs the query. If no problems occur trying to read the database,
		 * then a Cursor object is returned; otherwise, the cursor variable
		 * contains null. If no records were selected, then the Cursor object is
		 * empty, and Cursor.getCount() returns 0.
		 */
		Cursor c = qb.query(db, // The database to query
				projection, // The columns to return from the query
				selection, // The columns for the where clause
				selectionArgs, // The values for the where clause
				null, // don't group the rows
				null, // don't filter by row groups
				sortOrder // The sort order
				);

		// Tells the Cursor what URI to watch, so it knows when its source data
		// changes
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	/**
	 * This is called when a client calls
	 * {@link android.content.ContentResolver#getType(Uri)}
	 */
	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
		case TASKS:
			return TaskConstract.CONTENT_TYPE;
		case TASK_ID:
			return TaskConstract.CONTENT_ITEM_TYPE;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}