package com.twocities.pomodoro;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.CursorAdapter;

import com.twocities.pomodoro.adapters.TodoCursorAdapter;
import com.twocities.pomodoro.provider.TaskConstract;

public abstract class TodoListFragment extends SwipeListFragment implements
		LoaderCallbacks<Cursor> {


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		TodoCursorAdapter adapter = new TodoCursorAdapter(getActivity(), R.layout.layout_swipe_todo_item, null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		setListAdapter(adapter);
		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public void onDismiss(int[] reverseSortedPositions) {
//        for (int position : reverseSortedPositions) {
//            mAdapter.remove(position);
//        }
//        mAdapter.notifyDataSetChanged();
	}


	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		CursorLoader cursorLoader = new CursorLoader(getActivity(),
		        getUri(), 
		        getProjection(),
		        getSelection(),
		        getSelectionArgs(),
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