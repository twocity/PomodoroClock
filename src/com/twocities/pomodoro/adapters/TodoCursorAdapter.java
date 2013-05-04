package com.twocities.pomodoro.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

import com.twocities.pomodoro.R;
import com.twocities.pomodoro.provider.TaskConstract;

public class TodoCursorAdapter extends SwipeAdapter {

	public TodoCursorAdapter(Context context, int layout, Cursor c, int flags) {
		super(context, layout, c, flags);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		final String title = cursor.getString(TaskConstract.Columns.TASK_TITLE_INDEX);
		final String description = cursor.getString(TaskConstract.Columns.TASK_DESCRIPTION_INDEX);
		
		TextView titleView = (TextView) view.findViewById(R.id.text_todo_title);
		TextView descriptionView = (TextView) view.findViewById(R.id.text_todo_description);
		
		titleView.setText(title);
		descriptionView.setText(description);
	}
}