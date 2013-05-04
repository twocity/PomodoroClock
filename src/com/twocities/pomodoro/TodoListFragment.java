package com.twocities.pomodoro;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.CursorAdapter;

import com.twocities.pomodoro.adapters.TodoCursorAdapter;
import com.twocities.pomodoro.provider.TaskConstract;

public class TodoListFragment extends SwipeListFragment implements
		LoaderCallbacks<Cursor> {

//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		View v = inflater.inflate(R.layout.swipe_todo_list, null);
//		return v;
//	}

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
		        TaskConstract.CONTENT_URI, null, null, null, null);
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
}