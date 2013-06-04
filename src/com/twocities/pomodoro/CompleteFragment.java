package com.twocities.pomodoro;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.twocities.pomodoro.provider.TaskConstract;
import com.twocities.pomodoro.widget.ActionableToastBar;

public class CompleteFragment extends TodoListFragment implements
		OnNavigationListener {

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getListView().enableSwipe(false);

		setupActionBar();
	}

	private void setupActionBar() {
		ActionBar actionBar = getActivity().getActionBar();
//		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
//		ArrayAdapter<CharSequence> list = ArrayAdapter.createFromResource(
//				getActivity(), R.array.future_list_filter,
//				android.R.layout.simple_spinner_item);
//		list.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		actionBar.setListNavigationCallbacks(list, this);
		actionBar.setDisplayShowTitleEnabled(true);
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		return false;
	}

	@Override
	protected String getSelection() {
		return " ( " + TaskConstract.Columns.FLAG_DONE + " = " + "?" + " ) ";
	}

	@Override
	protected String[] getSelectionArgs() {
		String[] selectionArgs = new String[1];
		selectionArgs[0] = String.valueOf(1);
		return selectionArgs;
	}

	@Override
	protected ActionableToastBar getUndoBar() {
		return null;
	}
}