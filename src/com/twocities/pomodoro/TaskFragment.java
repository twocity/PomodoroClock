package com.twocities.pomodoro;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.twocities.pomodoro.Utils.Log;
import com.twocities.pomodoro.data.PomodoroClock;
import com.twocities.pomodoro.data.Task;
import com.twocities.pomodoro.provider.TaskConstract;
import com.twocities.pomodoro.widget.DigitalClock;

public class TaskFragment extends Fragment {
	private static final int REQUEST_EDIT_TASK = 1;
	private DigitalClock mContentView;
	private TextView mTitle;
	private TextView mDescription;

//	private PomodoroClock mClock;
//	private SharedPreferences mPrefs;
	private Task mTask;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_task, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mTitle = (TextView) view.findViewById(R.id.task_title);
		mDescription = (TextView) view.findViewById(R.id.task_description);
		
//		mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//		mClock = getIntent().getParcelableExtra(PomodoroClock.EXTRA_POMODORO);
//		mClock.writeInSharedPerefs(mPrefs);
//		Log.v("AlarmActivity: " + mClock.toString());
		mContentView = (DigitalClock) view.findViewById(R.id.digital_clock);
		mContentView.updateTime(PomodoroClock.DEFAULT_LENGTH);
		// Button startButton = (Button) view.findViewById(R.id.button_start);
		// startButton.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// com.twocities.pomodoro.Utils.Utils.startClock(getActivity(),
		// mTask.getTitle());
		//
		// }
		// });
		setHasOptionsMenu(true);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (getArguments() != null) {
			mTask = getArguments().getParcelable(Task.EXTRA_TASK_DATA);
			if (mTask == null) {
				throw new IllegalArgumentException("Task from Bundle is null!");
			}
			updateViewWithData(mTask);
		}
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

	/**
	 * Update views with the given data
	 * 
	 * @param task
	 */
	private void updateViewWithData(Task task) {
		if (task == null) {
			return;
		}
		/** update current task object **/
		this.mTask = task;
		mTitle.setText(task.getTitle());
		mDescription.setText(task.getDescription());
	}

	/**
	 * Start {@link TaskEditActivity} to edit the task
	 * 
	 * @param task
	 */
	private void editTask(Task task) {
		Intent intent = new Intent(getActivity(), TaskEditActivity.class);
		intent.putExtra(Task.EXTRA_TASK_DATA, task);
		this.startActivityForResult(intent, REQUEST_EDIT_TASK);
	}

	private void shareTask() {
		Intent sharedIntent = new Intent(Intent.ACTION_SEND);
		sharedIntent.putExtra(Intent.EXTRA_TITLE, mTask.getTitle());
		sharedIntent.putExtra(Intent.EXTRA_TEXT, mTask.getDescription());
		sharedIntent.setType("text/plain");
		String title = getString(R.string.chooser_title);
		// Create and start the chooser
		Intent chooser = Intent.createChooser(sharedIntent, title);
		startActivity(chooser);
	}

	/**
	 * This is not <strong> really delete </strong> this task,just add a
	 * 'deleted' flag.
	 */
	private void deleteTask() {
		mTask.setFlagDel(1);
		ContentValues updateValues = new ContentValues();
		updateValues.put(TaskConstract.Columns.FLAG_DEL, 1);
		int id = mTask.getId();
		Uri uri = ContentUris.withAppendedId(TaskConstract.CONTENT_ID_URI_BASE,
				id);
		getActivity().getContentResolver()
				.update(uri, updateValues, null, null);
		Toast.makeText(getActivity(), "deleted", Toast.LENGTH_SHORT).show();
		getFragmentManager().popBackStackImmediate();
	}

	/**
	 * Update data when user change the task.
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_EDIT_TASK) {
			if (resultCode == Activity.RESULT_OK) {
				Task task = data.getParcelableExtra(Task.EXTRA_TASK_DATA);
				updateViewWithData(task);
			}
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu optionsMenu, MenuInflater inflater) {
		inflater.inflate(R.menu.task_view, optionsMenu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.m_edit:
			editTask(mTask);
			break;
		case R.id.m_delete:
			deleteTask();
			break;
		case R.id.m_share:
			shareTask();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}