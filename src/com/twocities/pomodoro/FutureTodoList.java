package com.twocities.pomodoro;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;

import com.twocities.pomodoro.adapters.TodoCursorAdapter;

public class FutureTodoList extends TodoListFragment implements
		OnNavigationListener {
	

	private TodoCursorAdapter adapter;
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setupActionBar();
		
//		getLoaderManager().initLoader(0, null, this);
		
		adapter = new TodoCursorAdapter(getActivity(), R.layout.layout_swipe_todo_item, null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		setListAdapter(adapter);
	}

	private void setupActionBar() {
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		ArrayAdapter<CharSequence> list = ArrayAdapter.createFromResource(getActivity(), R.array.future_list_filter, android.R.layout.simple_spinner_item);
		list.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		actionBar.setListNavigationCallbacks(list, this);
		actionBar.setDisplayShowTitleEnabled(false);
	}
	
//	@Override
//	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//	}
//
//	@Override
//	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//		adapter.swapCursor(data);
//	}
//
//	@Override
//	public void onLoaderReset(Loader<Cursor> loader) {
//		adapter.swapCursor(null);
//	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		return false;
	}
}