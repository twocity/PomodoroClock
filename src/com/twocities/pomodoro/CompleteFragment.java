package com.twocities.pomodoro;

import android.app.ActionBar.OnNavigationListener;
import android.os.Bundle;

import com.twocities.pomodoro.provider.TaskConstract;
import com.twocities.pomodoro.widget.ActionableToastBar;

public class CompleteFragment extends TodoListFragment implements
		OnNavigationListener {

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getListView().enableSwipe(false);

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