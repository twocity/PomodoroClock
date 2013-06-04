package com.twocities.pomodoro;

import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.CursorAdapter;
import com.twocities.pomodoro.adapters.TodoCursorAdapter;
import com.twocities.pomodoro.data.Task;
import com.twocities.pomodoro.provider.TaskConstract;
import com.twocities.pomodoro.widget.ActionableToastBar;
import com.twocity.swipeablelistview.SwipeableListView.OnItemSwipeListener;
import com.twocity.swipeablelistview.SwipeableListView.OnSwipeItemClickListener;

public abstract class TodoListFragment extends SwipeListFragment implements
		OnItemSwipeListener, OnSwipeItemClickListener, LoaderCallbacks<Cursor> {
	protected Object mActionMode;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setupListView();
		getLoaderManager().initLoader(0, null, this);
	}
	
    @Override
    public void onResume() {
        super.onPause();
        if (getUndoBar() != null) {
            hideUndoBar(false, null);
        }
    }

	/**
	 * setup the SwipeableListView
	 */
	private void setupListView() {
		TodoCursorAdapter adapter = new TodoCursorAdapter(getActivity(),
				R.layout.layout_swipe_todo_item, null,
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

		adapter.setListView(getListView());
		setListAdapter(adapter);
		getListView().setFastScrollEnabled(false);
		getListView().setOnSwipeItemClickListener(this);
		getListView().enableSwipe(true);
		getListView().setOnItemSwipeListener(this);
		getListView().setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				hideUndoBar(true, event);
				return false;
			}
		});
	}

	@Override
	public void onClick(int position, View view) {
		Activity home = getActivity();
		if (home instanceof HomeActivity) {
			TaskFragment fragment = new TaskFragment();
			Cursor cursor = (Cursor) getListAdapter().getItem(position);
			Task task = Task.CreateFromCursor(cursor);
			if (task == null) {
				return;
			}
			Bundle bundle = new Bundle();
			bundle.putParcelable(Task.EXTRA_TASK_DATA, task);
			fragment.setArguments(bundle);
			((HomeActivity) home).switchContent(fragment, true);
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		CursorLoader cursorLoader = new CursorLoader(getActivity(), getUri(),
				getProjection(), getSelection(), getSelectionArgs(),
				getSortOrder());
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		getListAdapter().swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		getListAdapter().swapCursor(null);
	}

	protected abstract ActionableToastBar getUndoBar();

	/**
	 * Dismiss ActionableToastBar with animator
	 */
    private void hideUndoBar(boolean animate, MotionEvent event) {
    	ActionableToastBar mUndoBar = getUndoBar();
        if (mUndoBar != null) {
            if (event != null && mUndoBar.isEventInToastBar(event)) {
                // Avoid touches inside the undo bar.
                return;
            }
            mUndoBar.hide(animate);
        }
    }

	@Override
	public void onSwipe(View view) {
		final long id = (Long) view.getTag();
		completeTask(id, true);
		ActionableToastBar mUndoBar = getUndoBar();
		if (mUndoBar == null) {
			return;
		}
		mUndoBar.show(new ActionableToastBar.ActionClickedListener() {
			@Override
			public void onActionClicked() {
				completeTask(id, false);
			}
		}, 0, getString(R.string.complete_task), true, R.string.undo_title,
				true);
	}

	protected void completeTask(long id, boolean complete) {
		int flag = 0;
		if (complete) {
			flag = 1;
		}
		ContentValues values = new ContentValues();
		values.put(TaskConstract.Columns.FLAG_DONE, flag);
		Uri uri = ContentUris.withAppendedId(TaskConstract.CONTENT_ID_URI_BASE,
				id);
		getActivity().getContentResolver().update(uri, values, null, null);
		getListAdapter().notifyDataSetChanged();
	}

	protected Uri getUri() {
		return TaskConstract.CONTENT_URI;
	}

	protected String[] getProjection() {
		return null;
	}

	protected String getSelection() {
		return null;
	}

	protected String[] getSelectionArgs() {
		return null;
	}

	protected String getSortOrder() {
		return null;
	}

}