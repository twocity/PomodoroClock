package com.twocities.pomodoro.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.twocities.pomodoro.data.DataBaseHelper;
import com.twocities.pomodoro.data.PomodoroClock;

public class ClockProvider extends ContentProvider {
	private static final String TAG = ClockProvider.class.getSimpleName();
	private DataBaseHelper mOpenHelper;

	public static final String AUTHORITY = "com.twocities.pomodoro.provider.clocks";

	/** URI definitions **/

	/**
	 * The scheme part for this provider's URI
	 */
	private static final String SCHEME = "content://";

	/**
	 * Path parts for the URIs
	 */

	/**
	 * Path part for the Notes URI
	 */
	private static final String PATH_CLOCKS = "/clocks";

	/**
	 * Path part for the Note ID URI
	 */
	private static final String PATH_CLOCKS_ID = "/clocks/";

	/**
	 * The content:// style URL for this table
	 */
	public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY
			+ PATH_CLOCKS);

	/**
	 * The Content URI base for a single note. Callers must append a numberic
	 * task id to this Uri to retrieve a task
	 */
	public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + AUTHORITY
			+ PATH_CLOCKS_ID);

	/**
	 * The MIME type of {@link #CONTENT_URI} providing a directory of tasks.
	 */
	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.com.twocities.pomodoro.provider.clock";

	/**
	 * The MIME type of a {@link #CONTENT_URI} sub-directory of a single task.
	 */
	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.com.twocities.pomodoro.provider.clock";

	private static final int CLOCKS = 1;
	private static final int CLOCKS_ID = 2;
	private static final UriMatcher sURLMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);

	static {
		sURLMatcher.addURI(AUTHORITY, "clocks", CLOCKS);
		sURLMatcher.addURI(AUTHORITY, "clocks/#", CLOCKS_ID);
	}

	public ClockProvider() {
	}

	@Override
	public boolean onCreate() {
		mOpenHelper = new DataBaseHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri url, String[] projectionIn, String selection,
			String[] selectionArgs, String sort) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(PomodoroClock.Columns.TABLE_NAME);

		// Generate the body of the query
		int match = sURLMatcher.match(url);
		switch (match) {
		case CLOCKS:
			break;
		case CLOCKS_ID:
			qb.appendWhere("_id=");
			qb.appendWhere(url.getPathSegments().get(1));
			break;
		default:
			throw new IllegalArgumentException("Unknown URL " + url);
		}

		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		Cursor ret = qb.query(db, projectionIn, selection, selectionArgs, null,
				null, sort);

		if (ret == null) {
			Log.v(TAG, "Alarms.query: failed");
		} else {
			ret.setNotificationUri(getContext().getContentResolver(), url);
		}

		return ret;
	}

	@Override
	public String getType(Uri url) {
		int match = sURLMatcher.match(url);
		switch (match) {
		case CLOCKS:
			return CONTENT_TYPE;
		case CLOCKS_ID:
			return CONTENT_ITEM_TYPE;
		default:
			throw new IllegalArgumentException("Unknown URL");
		}
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		if (sURLMatcher.match(uri) != CLOCKS_ID) {
			throw new IllegalArgumentException("Unknow Uri " + uri);
		}
		SQLiteDatabase db = mOpenHelper.getWritable();
		String id = uri.getLastPathSegment();
		String finalSelection = PomodoroClock.Columns._ID + " = " + id;
		if (selection != null) {
			finalSelection = finalSelection + " AND " + selection;
		}

		int count = db.update(PomodoroClock.Columns.TABLE_NAME, values,
				finalSelection, selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		if (sURLMatcher.match(uri) != CLOCKS) {
			throw new IllegalArgumentException("Cannot insert into URL: " + uri);
		}
		// values.con
		SQLiteDatabase db = mOpenHelper.getWritable();
		long rowId = db.insert(PomodoroClock.Columns.TABLE_NAME, null,
				initialValues);

		if (rowId > 0) {
			// Creates a URI with the note ID pattern and the new row ID
			// appended to it.
			Uri noteUri = ContentUris.withAppendedId(
					TaskConstract.CONTENT_ID_URI_BASE, rowId);

			// Notifies observers registered against this provider that the data
			// changed.
			getContext().getContentResolver().notifyChange(noteUri, null);
			return noteUri;
		}

		// If the insert didn't succeed, then the rowID is <= 0. Throws an
		// exception.
		throw new SQLException("Failed to insert row into " + uri);
	}

	public int delete(Uri url, String where, String[] whereArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count;
		// long rowId = 0;
		switch (sURLMatcher.match(url)) {
		case CLOCKS:
			count = db.delete("alarms", where, whereArgs);
			break;
		case CLOCKS_ID:
			String segment = url.getPathSegments().get(1);
			// rowId = Long.parseLong(segment);
			if (TextUtils.isEmpty(where)) {
				where = "_id=" + segment;
			} else {
				where = "_id=" + segment + " AND (" + where + ")";
			}
			count = db.delete(PomodoroClock.Columns.TABLE_NAME, where,
					whereArgs);
			break;
		default:
			throw new IllegalArgumentException("Cannot delete from URL: " + url);
		}

		getContext().getContentResolver().notifyChange(url, null);
		return count;
	}
}
