package com.twocities.pomodoro;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.SimpleCursorAdapter;

import com.twocities.pomodoro.provider.TaskConstract;
import com.twocities.pomodoro.provider.TaskProvider;

public class FutureTodoList extends TodoListFragment implements
		OnNavigationListener {
	

	private SimpleCursorAdapter adapter;
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setupActionBar();
		
		getLoaderManager().initLoader(0, null, this);
		
		adapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_2, null,
                new String[] { TaskConstract.Columns.TITLE, TaskConstract.Columns.DESCRIPTION },
                new int[] { android.R.id.text1, android.R.id.text2 }, 0);
		setAdapter(adapter);
	}

	private void setupActionBar() {
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		ArrayAdapter<CharSequence> list = ArrayAdapter.createFromResource(getActivity(), R.array.future_list_filter, android.R.layout.simple_spinner_item);
		list.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		actionBar.setListNavigationCallbacks(list, this);
		actionBar.setDisplayShowTitleEnabled(false);
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		CursorLoader cursorLoader = new CursorLoader(getActivity(),
		        TaskConstract.CONTENT_URI, null, null, null, null);
		    return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		adapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		adapter.swapCursor(null);
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		return false;
	}
}