package com.twocities.pomodoro;

import android.app.ActionBar.OnNavigationListener;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.twocities.pomodoro.provider.TaskConstract;
import com.twocities.pomodoro.widget.ActionableToastBar;

public class CompleteFragment extends TodoListFragment implements
		OnNavigationListener {

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getActivity().getActionBar().setTitle(R.string.complete_title);
		getListView().enableSwipe(false);

	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		return false;
	}

	@Override
	protected String getSelection() {
		return " ( " + TaskConstract.Columns.FLAG_DONE + " = " + "?"
				+ " AND " + TaskConstract.Columns.FLAG_DEL + " != " + "?"
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
		return null;
	}

	@Override
	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		MenuInflater inflater = mode.getMenuInflater();
		inflater.inflate(R.menu.bulk_edit_done, menu);
		return true;
	}

	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		// TODO Auto-generated method stub
		return false;
	}
}