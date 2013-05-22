package com.twocities.pomodoro;

import com.twocities.pomodoro.provider.TaskConstract;
import com.twocities.pomodoro.widget.ActionableToastBar;

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
	
	private ActionableToastBar mUndoBar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup containder,
			Bundle savedInstaceState) {
		return inflater.inflate(R.layout.fragment_future, containder, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mUndoBar = (ActionableToastBar) view.findViewById(R.id.undo_bar);
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
	
	@Override
	protected String getSelection() {
		return " ( " + TaskConstract.Columns.FLAG_DEL + " != " + "?"
				+ " AND " + TaskConstract.Columns.FLAG_DONE + " != " + "?"
				+ " ) ";
	}

	@Override
	protected String[] getSelectionArgs() {
		String[] selectionArgs = new String[2];
		selectionArgs[0] = String.valueOf(1);
		selectionArgs[1] = String.valueOf(1);
		return selectionArgs;
	}

	@Override
	protected ActionableToastBar getUndoBar() {
		return this.mUndoBar;
	}
}