package com.twocities.pomodoro.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;

import com.twocity.swipeablelistview.SwipeLayout;

public class CheckedSwipeLayout extends SwipeLayout implements Checkable {
	private boolean checked = false;
	private static final int[] CHECKED_STATE_SET = { android.R.attr.state_checked };

	public CheckedSwipeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CheckedSwipeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CheckedSwipeLayout(Context context) {
		super(context);
	}

	@Override
	public void setChecked(boolean checked) {
		this.checked = checked;

		refreshDrawableState();

		// Propagate to childs
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);
			if (child instanceof Checkable) {
				((Checkable) child).setChecked(checked);
			}
		}
	}

	@Override
	protected int[] onCreateDrawableState(int extraSpace) {
		final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
		if (isChecked()) {
			mergeDrawableStates(drawableState, CHECKED_STATE_SET);
		}
		return drawableState;
	}

	@Override
	public boolean isChecked() {
		return this.checked;
	}

	@Override
	public void toggle() {
		this.checked = !this.checked;
	}

}