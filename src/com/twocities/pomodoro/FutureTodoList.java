package com.twocities.pomodoro;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.twocities.pomodoro.Utils.AsyncLoader;
import com.twocities.pomodoro.Utils.Log;
import com.twocities.pomodoro.adapters.TodoListAdapter;
import com.twocities.pomodoro.data.DataBaseHelper;
import com.twocities.pomodoro.data.Tasks;

public class FutureTodoList extends TodoListFragment implements
		OnNavigationListener {
	
	private DataBaseHelper mHelper;

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setupActionBar();
		
		mHelper = new DataBaseHelper(getActivity().getApplicationContext());
		getLoaderManager().initLoader(0, null, this);
		
		List<Tasks> list = new ArrayList<Tasks>();
		for(int i = 0; i<20; i++) {
			Tasks item = new Tasks();
			list.add(item);
		}
		TodoListAdapter adapter = new TodoListAdapter(getActivity(), getActivity().getLayoutInflater(), list);
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
		mHelper.queryEvent(null, null, null, null, null, null);
		return new AsyncLoader<Cursor>(getActivity()) {

			@Override
			public Cursor loadData() throws Exception {
				return mHelper.queryEvent();
			}
		};
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		while (data.moveToNext()) {
			String title = data.getString(1);
			String description = data.getString(2);
			Log.v("\ntitle: " + title);
			Log.v("\ndescription: " + description);
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		return false;
	}
}