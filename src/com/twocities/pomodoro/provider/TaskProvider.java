package com.twocities.pomodoro.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.twocities.pomodoro.Utils.Log;
import com.twocities.pomodoro.data.DataBaseHelper;

public class TaskProvider extends ContentProvider {
	private static final String TAG = "TaskProvider";

	/**
	 * Standard projection for a normal task
	 */
	private static final String[] READ_TASK_PROJECTION = new String[] {
			TaskConstract.Columns._ID, TaskConstract.Columns.TITLE,
			TaskConstract.Columns.DESCRIPTION, TaskConstract.Columns.TAGS,
			TaskConstract.Columns.START_DATE, TaskConstract.Columns.DUE_DATE };

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
		switch (sUriMatcher.match(uri)) {
		case TASKS:
			return mOpenHelper.queryEvent();
		case TASK_ID:
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		return null;
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