package com.twocities.pomodoro.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines a contract between the content provider and its clients. A contract
 * defines the information that a client needs to access the provider as one or
 * more data tables. A contract is a public, non-extendable (final) class that
 * contains constants defining column names and URIs. A well-written client
 * depends only on the constants in the contract.
 */

public final class TaskConstract {
	public static final String AUTHORITY = "com.twocities.pomodoro.provider.Tasks";

	private TaskConstract() {
	}

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
	private static final String PATH_TASKS = "/tasks";

	/**
	 * Path part for the Note ID URI
	 */
	private static final String PATH_TASKS_ID = "/tasks/";

	/**
	 * The content:// style URL for this table
	 */
	public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY
			+ PATH_TASKS);

	/**
	 * The Content URI base for a single note. Callers must
	 * append a numberic task id to this Uri to retrieve a task
	 */
	public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + AUTHORITY
			+ PATH_TASKS_ID);

    /**
     * The content URI match pattern for a single task, specified by its ID. Use this to match
     * incoming URIs or to construct an Intent.
     */
	public static final Uri CONTENT_ID_URI_PATTERN = Uri.parse(SCHEME
			+ AUTHORITY + PATH_TASKS_ID + "/#");

	public static final class Columns implements BaseColumns {

		// This class cannot be instantiated
		private Columns() {
		}

		/*
		 * The tasks table name
		 */
		public static final String TASKS_TABLE = "tasks";

		/*
		 * Column definitions
		 */

		/**
		 * Column name for the title of the task
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String TITLE = "title";

		/**
		 * Column name for the description of the task
		 * <p>
		 * <Type: TEXT
		 * <p>
		 */
		public static final String DESCRIPTION = "description";

		/**
		 * Column name for the tags of the task
		 * <p>
		 * Type: TEXT
		 * <p>
		 */
		public static final String TAGS = "tags";

		/**
		 * Column name for the start time of the task
		 * <p>
		 * Type: INTEGER (long)
		 */
		public static final String START_DATE = "start_date";

		/**
		 * Column name for the due time of the task(in milliseconds)
		 * <p>
		 * Type: INTEGER (long)
		 */
		public static final String DUE_DATE = "due_data";

		/**
		 * These save calls to cursor.getColumnIndexOrThrow() THEY MUST BE KEPT
		 * IN SYNC WITH ABOVE QUERY COLUMNS
		 */
		public static final int TASK_ID_INDEX = 0;
		public static final int TASK_TITLE_INDEX = 1;
		public static final int TASK_DESCRIPTION_INDEX = 2;
		public static final int TASK_TAGS_INDEX = 3;
		public static final int TASK_STIME_INDEX = 4;
		public static final int TASK_DTIME_INDEX = 5;
	}
}