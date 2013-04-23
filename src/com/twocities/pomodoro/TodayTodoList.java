package com.twocities.pomodoro;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.twocities.pomodoro.adapters.TodoListAdapter;
import com.twocities.pomodoro.data.PomodoroClock;
import com.twocities.pomodoro.data.Tasks;

public class TodayTodoList extends TodoListFragment {
	private EditText mQuickStart;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mQuickStart = (EditText) getActivity().getLayoutInflater().inflate(
				R.layout.layout_add_pomodoro_edit, null);

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
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle("TODAY");
	}

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
}