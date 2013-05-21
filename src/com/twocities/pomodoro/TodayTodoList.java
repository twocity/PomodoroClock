package com.twocities.pomodoro;

import android.app.ActionBar;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.twocities.pomodoro.Utils.TimeUtils;
import com.twocities.pomodoro.adapters.TodoCursorAdapter;
import com.twocities.pomodoro.data.PomodoroClock;
import com.twocities.pomodoro.provider.TaskConstract;
import com.twocities.pomodoro.widget.ActionableToastBar;

public class TodayTodoList extends TodoListFragment {
	private EditText mQuickStart;
	private ActionableToastBar mUndoBar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup containder,
			Bundle savedInstaceState) {
		return inflater.inflate(R.layout.fragment_today, containder, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mUndoBar = (ActionableToastBar) view.findViewById(R.id.undo_bar);
		mQuickStart = (EditText) view.findViewById(R.id.quick_add_pomodoro);

		mQuickStart.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					startClock(v.getText().toString());
					return true;
				}
				return false;
			}
		});
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setupActionBar();
	}

	@Override
	public void onResume() {
		super.onResume();
		mQuickStart.clearFocus();
		mQuickStart.setText("");
	}

	private void setupActionBar() {
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle("Today");
	}
	
	@Override
	public void onDismiss(ListView listView, final int[] reverseSortedPositions) {
		if (!(getListAdapter() instanceof TodoCursorAdapter)) {
			return;
		}
		completeTask(reverseSortedPositions, true);
		
        mUndoBar.show(new ActionableToastBar.ActionClickedListener() {
            @Override
            public void onActionClicked() {
            	completeTask(reverseSortedPositions, false);
            }
        }, 0, getString(R.string.complete_task), true, R.string.undo_title, true);
        
	}
	
	private void completeTask(int[] reverseSortedPositions, boolean complete) {
		TodoCursorAdapter adapter = (TodoCursorAdapter) getListAdapter();
		int flag = 0;
		if (complete) {
			flag = 1;
		}
		for (int position : reverseSortedPositions) {
			long id = adapter.getItemId(position);
			ContentValues values = new ContentValues();
			values.put(TaskConstract.Columns.FLAG_DONE, flag);
			Uri uri = ContentUris.withAppendedId(TaskConstract.CONTENT_ID_URI_BASE,
					id);
			getActivity().getContentResolver()
					.update(uri, values, null, null);
		}
		adapter.notifyDataSetChanged();
	}

	
	/**
	 * Start a pomodoro clock
	 * @param title
	 */
	private void startClock(String title) {
		SharedPreferences perfs = PreferenceManager
				.getDefaultSharedPreferences(getActivity()
						.getApplicationContext());
		PomodoroClock clock = new PomodoroClock();
		clock.readFromSharedPerefs(perfs);
		if (clock.isRunning()) {
			Toast.makeText(getActivity().getApplicationContext(),
					"There is a Clock running.", Toast.LENGTH_LONG).show();
			clock.clearSharedPerefs(perfs);
		} else {
			Intent i = new Intent(getActivity(), ScreenAlarmActivity.class);
			PomodoroClock newClock = new PomodoroClock(
					PomodoroClock.DEFAULT_LENGTH);
			newClock.updateTitle(title);
			i.putExtra(PomodoroClock.EXTRA_POMODORO, newClock);
			newClock.writeInSharedPerefs(perfs);
			this.startActivity(i);
		}
	}

	@Override
	protected String getSelection() {
		return " ( " + TaskConstract.Columns.REMINDER_TIME + " >= " + "?"
				+ " AND " + TaskConstract.Columns.REMINDER_TIME + " <= " + "?"
				+ " AND " + TaskConstract.Columns.FLAG_DEL + " != " + "?"
				+ " AND " + TaskConstract.Columns.FLAG_DONE + " != " + "?"
				+ " ) ";
	}

	@Override
	protected String[] getSelectionArgs() {
		String[] selectionArgs = new String[4];
		String[] timeSelection = TimeUtils.rangeOfToday();
		selectionArgs[0] = timeSelection[0];
		selectionArgs[1] = timeSelection[1];
		selectionArgs[2] = String.valueOf(1);
		selectionArgs[3] = String.valueOf(1);
		return selectionArgs;
	}

}