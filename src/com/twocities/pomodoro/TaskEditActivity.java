package com.twocities.pomodoro;

import java.util.Calendar;
import java.util.TimeZone;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.twocities.pomodoro.Utils.TimeUtils;
import com.twocities.pomodoro.data.Task;
import com.twocities.pomodoro.provider.TaskConstract;

public class TaskEditActivity extends Activity {
	private EditText titleEdit;
	private EditText descriptionEdit;
	private Button startDateButton;
	private Button dueDateButton;

	private Task mTask = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_task_edit);
		setupActionBar();
		initViews();

		handleIntentAction(getIntent());

		mTask = getIntent().getParcelableExtra(Task.EXTRA_TASK_DATA);
		if (mTask != null) {
			initData(mTask);
		}
	}

	/**
	 * Create or edit task
	 */
	private void newTask() {

		ContentValues values = new ContentValues();

		String title = titleEdit.getText().toString();
		String descriString = descriptionEdit.getText().toString();

		if (TextUtils.isEmpty(title)) {
			Toast.makeText(getApplicationContext(), "Can't create empty task",
					Toast.LENGTH_SHORT).show();
			return;
		}

		// set title and description
		values.put(TaskConstract.Columns.TITLE, title);
		values.put(TaskConstract.Columns.DESCRIPTION, descriString);

		// set reminder time
		Object startObj = startDateButton.getTag();
		if (startObj != null) {
			long start = (Long) startDateButton.getTag();
			values.put(TaskConstract.Columns.REMINDER_TIME, start);
			if (mTask != null) {
				mTask.setReminderTime(start);
			}
		}
		
		// set due time
		Object endObj = dueDateButton.getTag();
		if (endObj != null) {
			long due = (Long) dueDateButton.getTag();
			values.put(TaskConstract.Columns.DUE_TIME, due);
			if (mTask != null) {
				mTask.setDueTime(due);
			}
		}

		if (mTask == null) {
			// create a new task
			long createTime = TimeUtils.getTimeNow();
			values.put(TaskConstract.Columns.CREATE_TIME, createTime);
			values.put(TaskConstract.Columns.FLAG_DONE, 0);
			values.put(TaskConstract.Columns.FLAG_DEL, 0);
			values.put(TaskConstract.Columns.FLAG_EMERGENCY, 0);
		} else {
			// update current task
			mTask.setTitle(title);
			mTask.setDescription(descriString);
			// TODO update flag of emergency
		}
		newTask(values);
	}

	/**
	 * Insert or update a task
	 * 
	 * @param values
	 */
	private void newTask(ContentValues values) {
		if (mTask != null) {
			// update current task
			int id = mTask.getId();
			Uri uri = ContentUris.withAppendedId(
					TaskConstract.CONTENT_ID_URI_BASE, id);
			getContentResolver().update(uri, values, null, null);
			setResultData(mTask);
		} else {
			getContentResolver().insert(TaskConstract.CONTENT_URI, values);
		}
	}

	private void setResultData(Task task) {
		Intent data = new Intent();
		data.putExtra(Task.EXTRA_TASK_DATA, task);
		setResult(RESULT_OK, data);
	}

	/**
	 * init views
	 */
	private void initViews() {
		titleEdit = (EditText) findViewById(R.id.task_title);
		descriptionEdit = (EditText) findViewById(R.id.task_description);

		startDateButton = (Button) findViewById(R.id.task_start_date);
		dueDateButton = (Button) findViewById(R.id.task_due_date);
		View cancel = findViewById(R.id.action_cancel);
		View done = findViewById(R.id.action_done);

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TaskEditActivity.this.setResult(RESULT_CANCELED);
				finish();
			}
		});

		done.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				newTask();
				finish();
			}
		});
	}

	private void handleIntentAction(Intent intent) {
		String action = intent.getAction();
		String type = intent.getType();

		if (Intent.ACTION_SEND.equals(action) && type != null) {
			if ("text/plain".equals(type)) {
				String title = intent.getStringExtra(Intent.EXTRA_TITLE);
				String description = intent.getStringExtra(Intent.EXTRA_TEXT);
				if (title != null) {
					titleEdit.setText(title);
					title = intent.getStringExtra(Intent.EXTRA_SUBJECT);
					if (title != null) {
						titleEdit.setText(title);
					}
				}
				if (description != null) {
					descriptionEdit.setText(description);
				}
			}
		}
	}

	private void initData(Task task) {
		titleEdit.setText(task.getTitle());
		descriptionEdit.setText(task.getDescription());
	}

	/**
	 * Set custom view to actionbar
	 */
	private void setupActionBar() {
		getActionBar().setDisplayShowCustomEnabled(true);
		getActionBar().setDisplayUseLogoEnabled(false);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setCustomView(R.layout.layout_edit_task_actionbar);
	}

	/**
	 * Pick a date
	 * 
	 * @param v
	 */
	public void setDate(View v) {
		switch (v.getId()) {
		case R.id.task_start_date:
			showDatePicker(startListener, null);
			break;
		case R.id.task_due_date:
			showDatePicker(dueListender, null);
			break;
		default:
			break;
		}
	}

	private void showDatePicker(OnDateSetListener listener, String tag) {
		DatePickerDialogFragment fragment = new DatePickerDialogFragment();
		fragment.setListener(listener);
		fragment.show(getFragmentManager(), tag);
	}

	private void setDateMessage(Button button, int year, int monthOfYear,
			int dayOfMonth) {
		StringBuffer date = new StringBuffer();
		date.append(year).append("/").append(monthOfYear + 1).append("/")
				.append(dayOfMonth);
		button.setText(date.toString());
		Calendar now = Calendar.getInstance();
		now.setTimeZone(TimeZone.getDefault());
		now.set(Calendar.YEAR, year);
		now.set(Calendar.MONTH, monthOfYear);
		now.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		button.setTag(now.getTimeInMillis());
	}

	private OnDateSetListener startListener = new OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			setDateMessage(startDateButton, year, monthOfYear, dayOfMonth);
		}
	};

	private OnDateSetListener dueListender = new OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			setDateMessage(dueDateButton, year, monthOfYear, dayOfMonth);
		}
	};

	private class DatePickerDialogFragment extends DialogFragment {
		private OnDateSetListener listener;

		public DatePickerDialogFragment() {
		}

		public void setListener(OnDateSetListener listener) {
			this.listener = listener;
		}

		public Dialog onCreateDialog(Bundle savedInstanceState) {
			Calendar now = Calendar.getInstance();
			int year = now.get(Calendar.YEAR);
			int month = now.get(Calendar.MONTH);
			int dayofmonth = now.get(Calendar.DAY_OF_MONTH);
			return new DatePickerDialog(getActivity(), listener, year, month,
					dayofmonth);
		}
	}

}