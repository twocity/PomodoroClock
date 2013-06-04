package com.twocities.pomodoro;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;

import com.twocities.pomodoro.Utils.TimeUtils;
import com.twocities.pomodoro.provider.TaskConstract;
import com.twocities.pomodoro.widget.ActionableToastBar;

public class FutureTodoList extends TodoListFragment implements
		OnNavigationListener {

	public static final String EXTRA_SELECTED_MODE = "extra_selected_mode";
	private ActionableToastBar mUndoBar;
	private boolean mMutilSeletedMode = false;
	private ActionMode mActionMode;

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
		if (getArguments() != null) {
			mMutilSeletedMode = getArguments().getBoolean(EXTRA_SELECTED_MODE,
					false);
		}
		setupListView();
		handleMultiMode();
	}

	private void setupActionBar() {
		ActionBar actionBar = getActivity().getActionBar();
		// actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		// ArrayAdapter<CharSequence> list = ArrayAdapter.createFromResource(
		// getActivity(), R.array.future_list_filter,
		// android.R.layout.simple_spinner_item);
		// list.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// actionBar.setListNavigationCallbacks(list, this);
		actionBar.setDisplayShowTitleEnabled(false);
	}

	private void setupListView() {
		ListView listView = getListView();
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

	}

	private void handleMultiMode() {
		if (mMutilSeletedMode) {
			if (mActionMode == null) {
				mActionMode = getActivity()
						.startActionMode(mActionModeCallback);
			}
		}
	}

	@Override
	public void onClick(int position, View view) {
		if (mMutilSeletedMode && mActionMode != null) {
			view.setSelected(!view.isSelected());
			mActionMode.setTitle(String.valueOf(getListView()
					.getCheckedItemCount()));
		} else {
			super.onClick(position, view);
		}
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
		return " ( " + TaskConstract.Columns.FLAG_DEL + " != " + "?" + " AND "
				+ TaskConstract.Columns.FLAG_DONE + " != " + "?" + " AND "
				+ " ( " + TaskConstract.Columns.REMINDER_TIME + " < " + "?"
				+ " OR " + TaskConstract.Columns.REMINDER_TIME + " > " + "?"
				+ " ) " + " ) ";
	}

	@Override
	protected String[] getSelectionArgs() {
		String[] selectionArgs = new String[4];
		String[] timeSelection = TimeUtils.rangeOfToday();
		selectionArgs[0] = String.valueOf(1);
		selectionArgs[1] = String.valueOf(1);
		selectionArgs[2] = timeSelection[0];
		selectionArgs[3] = timeSelection[1];
		return selectionArgs;
	}

	@Override
	protected ActionableToastBar getUndoBar() {
		return this.mUndoBar;
	}

	private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

		// Called when the action mode is created; startActionMode() was called
		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			// Inflate a menu resource providing context menu items
			MenuInflater inflater = mode.getMenuInflater();
			inflater.inflate(R.menu.task_view, menu);
			return true;
		}

		// Called each time the action mode is shown. Always called after
		// onCreateActionMode, but
		// may be called multiple times if the mode is invalidated.
		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false; // Return false if nothing is done
		}

		// Called when the user selects a contextual menu item
		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			switch (item.getItemId()) {
			// case R.id.menu_share:
			// shareCurrentItem();
			// mode.finish(); // Action picked, so close the CAB
			// return true;
			default:
				return false;
			}
		}

		// Called when the user exits the action mode
		@Override
		public void onDestroyActionMode(ActionMode mode) {
			mActionMode = null;
		}
	};
}