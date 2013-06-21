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
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.CursorAdapter;
import android.widget.ListView;

import com.twocities.pomodoro.adapters.TodoCursorAdapter;
import com.twocities.pomodoro.data.Task;
import com.twocities.pomodoro.provider.TaskConstract;
import com.twocities.pomodoro.widget.ActionableToastBar;
import com.twocity.swipeablelistview.SwipeableListView;
import com.twocity.swipeablelistview.SwipeableListView.OnItemSwipeListener;
import com.twocity.swipeablelistview.SwipeableListView.OnSwipeItemClickListener;

public abstract class TodoListFragment extends SwipeListFragment implements
		OnItemSwipeListener, OnSwipeItemClickListener, LoaderCallbacks<Cursor>,
		Callback, View.OnLongClickListener {
	private static final String TAG = TodoListFragment.class.getSimpleName();
	protected ActionMode mActionMode;
	private SwipeableListView mListView;
	private TodoCursorAdapter mAdapter;

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
		mAdapter = new TodoCursorAdapter(getActivity(),
				R.layout.layout_swipe_todo_item, null,
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

		mAdapter.setListView(getListView());
		mAdapter.setLongClickListener(this);
		setListAdapter(mAdapter);

		mListView = getListView();
		mListView.setFastScrollEnabled(false);
		mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		mListView.setOnSwipeItemClickListener(this);
		mListView.enableSwipe(true);
		mListView.setOnItemSwipeListener(this);
		mListView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				hideUndoBar(true, event);
				return false;
			}
		});
	}

	@Override
	public void onClick(int position, View view) {
		if (mActionMode != null) {
			onLongClick(view);
			return;
		}
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
	public boolean onLongClick(View v) {
		mAdapter.toggleSelectState(v);
		mAdapter.notifyDataSetChanged();
		updateActionMode();
		return true;
	}

	protected void updateActionMode() {
		int selectedNum = mAdapter.getSelectedItemsNum();
		if (mActionMode == null && selectedNum > 0) {
			// Start the action mode
			mActionMode = getActivity().startActionMode(this);
			setActionModeTitle(selectedNum);
		} else if (mActionMode != null) {
			if (selectedNum > 0) {
				// Update the number of selected items in the title
				// mActionMode.invalidate();
				setActionModeTitle(selectedNum);
			} else {
				// No selected items. close the action mode
				mActionMode.finish();
				mActionMode = null;
			}
		}
	}

	protected void setActionModeTitle(int number) {
		mActionMode.setTitle(String.valueOf(number));
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

	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.m_done:
			for (long id : mAdapter.getSelectedItemsId()) {
				completeTask(id, true);
			}
			break;
		case R.id.m_undone:
			for (long id : mAdapter.getSelectedItemsId()) {
				completeTask(id, false);
			}
			break;
		case R.id.m_delete:
			for (long id : mAdapter.getSelectedItemsId()) {
				deleteTask(id, true);
			}
			break;
		case R.id.m_add_to_today:
			break;
		default:
			break;
		}
		mAdapter.clearSelectedItems();
		mActionMode.finish();
		mActionMode = null;
		return true;
	}

	@Override
	public void onDestroyActionMode(ActionMode mode) {
		if (mAdapter != null) {
			mAdapter.clearSelectedItems();
		}
		mActionMode = null;
	}

	/**
	 * Task operations
	 */

	/**
	 * Mark the task's {@code TaskConstract.Columns.FLAG_DONE}
	 * 
	 * @param id
	 *            id of task
	 * @param complete
	 */
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
	
	protected void completeTask(long[] taskIds, boolean complete) {
		for (long id : taskIds) {
			completeTask(id, complete);
		}
	}

	/**
	 * Mark Task's FLAG_DEL
	 * @param id
	 * @param delete
	 */
	protected void deleteTask(long id, boolean delete) {
		int flag = 0;
		if (delete) {
			flag = 1;
		}
		ContentValues values = new ContentValues();
		values.put(TaskConstract.Columns.FLAG_DEL, flag);
		Uri uri = ContentUris.withAppendedId(TaskConstract.CONTENT_ID_URI_BASE,
				id);
		getActivity().getContentResolver().update(uri, values, null, null);
		getListAdapter().notifyDataSetChanged();
	}
	
	protected void deleteTask(long[] taskIds, boolean delete) {
		for (long id: taskIds) {
			deleteTask(id, delete);
		}
	}
	
	protected void moveToToday(long id, boolean toToday) {
		int flag = toToday ? 1 : 0;
		ContentValues values = new ContentValues();
		values.put(TaskConstract.Columns.FLAG_TODAY, flag);
		Uri uri = ContentUris.withAppendedId(TaskConstract.CONTENT_ID_URI_BASE,
				id);
		getActivity().getContentResolver().update(uri, values, null, null);
		getListAdapter().notifyDataSetChanged();
	}
	
	protected void moveToToday(long[] taskIds, boolean toToday) {
		for (long id : taskIds) {
			moveToToday(id, toToday);
		}
	}
	
	private void updateTask(long taskId, String key, String value) {
		ContentValues values = new ContentValues();
		values.put(key, value);
		Uri uri = ContentUris.withAppendedId(TaskConstract.CONTENT_ID_URI_BASE,
				taskId);
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