package com.twocities.pomodoro;

import android.app.ActionBar;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.twocities.pomodoro.Utils.TimeUtils;
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
					addTask(v.getText().toString());
//					startClock(v.getText().toString());
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
		clearQuickAddBar();
	}

	private void setupActionBar() {
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle("Today");
	}
	
	private void addTask(String title) {
		if (TextUtils.isEmpty(title)) {
			return;
		}
		ContentValues values = new ContentValues();
		long createTime = TimeUtils.getTimeNow();
		values.put(TaskConstract.Columns.TITLE, title);
		values.put(TaskConstract.Columns.REMINDER_TIME, createTime);
		values.put(TaskConstract.Columns.CREATE_TIME, createTime);
		values.put(TaskConstract.Columns.FLAG_DONE, 0);
		values.put(TaskConstract.Columns.FLAG_DEL, 0);
		values.put(TaskConstract.Columns.FLAG_EMERGENCY, 0);
		getActivity().getContentResolver().insert(TaskConstract.CONTENT_URI, values);
		clearQuickAddBar();
	}

	/**
	 * Start a pomodoro clock
	 * 
	 * @param title
	 */
	@SuppressWarnings("unused")
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
	
	private void clearQuickAddBar() {
		mQuickStart.clearFocus();
		mQuickStart.setText("");
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

	@Override
	protected ActionableToastBar getUndoBar() {
		return this.mUndoBar;
	}
}