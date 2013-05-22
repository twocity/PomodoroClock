package com.twocities.pomodoro.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

public abstract class SwipeAdapter extends CursorAdapter {
	private int mLayoutId;
	private Context mContext;
	private LayoutInflater mInflater;

	public SwipeAdapter(Context context, int resLayoutId, Cursor c, int flags) {
		super(context, c, flags);
		this.mLayoutId = resLayoutId;
		this.mContext = context;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (!getCursor().moveToPosition(position)) {
			throw new IllegalStateException("couldn't move cursor to position "
					+ position);
		}
		View v;
		if (convertView == null) {
			v = newView(mContext, getCursor(), parent);
		} else {
			// Do a translation check to test for animation. Change this to
			// something more
			// reliable and robust in the future.
			if (convertView.getTranslationX() != 0
					|| convertView.getTranslationY() != 0) {
				// view was animated, reset
				v = newView(mContext, getCursor(), parent);
			} else {
				v = convertView;
			}
		}
		v.setTag(getItemId(position));
		bindView(v, mContext, getCursor());
		return v;
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return mInflater.inflate(mLayoutId, parent, false);
	}
}