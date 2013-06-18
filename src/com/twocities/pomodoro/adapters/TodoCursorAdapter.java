package com.twocities.pomodoro.adapters;

import java.util.Calendar;
import java.util.HashSet;

import android.content.Context;
import android.database.Cursor;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.TextView;

import com.twocities.pomodoro.R;
import com.twocities.pomodoro.Utils.Log;
import com.twocities.pomodoro.provider.TaskConstract;

public class TodoCursorAdapter extends SwipeAdapter {
	private int mSelectedBackgroundColor;

	public TodoCursorAdapter(Context context, int layout, Cursor c, int flags) {
		super(context, layout, c, flags);
		mSelectedBackgroundColor = context.getResources().getColor(
				android.R.color.holo_blue_dark);
	}

	private HashSet<Long> mSelectedItems = new HashSet<Long>();
	private OnLongClickListener mLongClickListener;

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		view.setOnLongClickListener(mLongClickListener);
		long id = (Long) view.getTag();
		if (mSelectedItems.contains(id)) {
			view.setBackgroundColor(mSelectedBackgroundColor);
		} else {
			view.setBackgroundResource(R.drawable.todo_item_background);
		}
		// view.setPadding(10, 10, 10, 10);
		// view.setSelected(mSelectedItems.contains(id));
		final String title = cursor
				.getString(TaskConstract.Columns.TASK_TITLE_INDEX);
		final String description = cursor
				.getString(TaskConstract.Columns.TASK_DESCRIPTION_INDEX);

		TextView titleView = (TextView) view.findViewById(R.id.text_todo_title);
		TextView descriptionView = (TextView) view
				.findViewById(R.id.text_todo_description);

		titleView.setText(title);
		descriptionView.setText(description);

		long start = cursor
				.getLong(TaskConstract.Columns.TASK_REMINDER_TIME_INDEX);
		// long end = cursor.getLong(TaskConstract.Columns.TASK_DUE_TIME_INDEX);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(start);
		String startFormat = String.valueOf(DateFormat.format(
				"yyyy-MM-dd hhhh:mmmm", calendar.getTime()));
		Log.v(startFormat);
	}

	public void setLongClickListener(OnLongClickListener l) {
		mLongClickListener = l;
	}

	public void toggleSelectState(View v) {
		if (v != null) {
			long id = (Long) v.getTag();
			if (mSelectedItems.contains(id)) {
				mSelectedItems.remove(id);
			} else {
				mSelectedItems.add(id);
			}

		}
	}

	public int getSelectedItemsNum() {
		return mSelectedItems.size();
	}

	public void clearSelectedItems() {
		mSelectedItems.clear();
		notifyDataSetChanged();
	}

	public void deletedSelectedItems() {

	}

	public void removeSelectedId(int id) {
		mSelectedItems.remove(id);
	}

}