package com.twocities.pomodoro.data;

public class DataConstract {
	
	private DataConstract() {
	}
	
	public interface TaskColumns {
		public static final String TASK_ID = "task_id";
		public static final String TITLE = "title";
		public static final String DESCRIPTION = "description";
		public static final String TAGS = "tags";
		public static final String START_DATE = "start_date";
		public static final String DUE_DATE = "due_data";
	}
}