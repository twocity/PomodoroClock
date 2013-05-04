package com.twocities.pomodoro.adapters;

import android.content.Context;
import android.database.Cursor;
import android.widget.ResourceCursorAdapter;

public abstract class SwipeAdapter extends ResourceCursorAdapter {

	public SwipeAdapter(Context context, int layout, Cursor c, int flags) {
		super(context, layout, c, flags);
	}
}