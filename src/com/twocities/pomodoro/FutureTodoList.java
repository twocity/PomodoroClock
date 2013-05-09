package com.twocities.pomodoro;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class FutureTodoList extends TodoListFragment implements
		OnNavigationListener {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup containder,
			Bundle savedInstaceState) {
		return inflater.inflate(R.layout.fragment_future, containder, false);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setupActionBar();

		getActivity().findViewById(R.id.add_task).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						addTask();
					}
				});
	}

	private void setupActionBar() {
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		ArrayAdapter<CharSequence> list = ArrayAdapter.createFromResource(
				getActivity(), R.array.future_list_filter,
				android.R.layout.simple_spinner_item);
		list.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		actionBar.setListNavigationCallbacks(list, this);
		actionBar.setDisplayShowTitleEnabled(false);
	}

	private void addTask() {
		Intent intent = new Intent(getActivity(), TaskEditActivity.class);
		this.startActivity(intent);
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		return false;
	}
}