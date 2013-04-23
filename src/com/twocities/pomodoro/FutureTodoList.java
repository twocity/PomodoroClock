package com.twocities.pomodoro;

import java.util.ArrayList;
import java.util.List;

import com.twocities.pomodoro.adapters.TodoListAdapter;
import com.twocities.pomodoro.data.Tasks;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class FutureTodoList extends TodoListFragment implements
		OnNavigationListener {

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setupActionBar();

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
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		return false;
	}
}