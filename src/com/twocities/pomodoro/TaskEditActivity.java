package com.twocities.pomodoro;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.twocities.pomodoro.provider.TaskConstract;

public class TaskEditActivity extends Activity {
	private EditText titleEdit;
	private EditText descriptionEdit;
	private Button startDateButton;
	private Button dueDateButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_task_edit);
		
		setupActionBar();
		
		titleEdit = (EditText) findViewById(R.id.task_title);
		descriptionEdit = (EditText) findViewById(R.id.task_description);
		
		startDateButton = (Button) findViewById(R.id.task_start_date);
		dueDateButton = (Button) findViewById(R.id.task_due_date);
	}
	
	private void newTask() {
		String title = titleEdit.getText().toString();
		String descriString = descriptionEdit.getText().toString();
//		long start = (Long) startDateButton.getTag();
//		long due = (Long) dueDateButton.getTag();
		
		ContentValues values = new ContentValues();
		values.put(TaskConstract.Columns.TITLE, title);
		values.put(TaskConstract.Columns.DESCRIPTION, descriString);
		newTask(values);
	}
	
	private void newTask(ContentValues values) {
		getContentResolver().insert(TaskConstract.CONTENT_URI, values);
	}

	public void taskAction(View v) {
		if (v.getId() == R.id.action_cancel) {
			this.finish();
			
		} else if (v.getId() == R.id.action_done) {
			newTask();
			this.finish();
		}
	}
	
	private void setupActionBar() {
//		getActionBar().setDisplayShowCustomEnabled(true);
//		getActionBar().setDisplayUseLogoEnabled(false);
//		getActionBar().setDisplayShowHomeEnabled(false);
//		getActionBar().setCustomView(R.layout.layout_edit_task_actionbar);
	}
	
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
	
	private void setDateMessage(Button button, int year, int monthOfYear, int dayOfMonth) {
		StringBuffer date = new StringBuffer();
		date.append(year)
			.append("/")
			.append(++monthOfYear)
			.append("/")
			.append(dayOfMonth);
		button.setText(date.toString());
		Calendar now = Calendar.getInstance();
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
			StringBuffer date = new StringBuffer();
			date.append(year)
				.append("/")
				.append(++monthOfYear)
				.append("/")
				.append(dayOfMonth);
			setDateMessage(dueDateButton, year, monthOfYear, dayOfMonth);
		}
	};
	
	
	
	private class DatePickerDialogFragment extends DialogFragment {
		private OnDateSetListener listener;
		
		public DatePickerDialogFragment() {}
		
		public void setListener(OnDateSetListener listener) {
			this.listener = listener;
		}
		
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	    	Calendar now = Calendar.getInstance();
	    	int year = now.get(Calendar.YEAR);
	    	int month = now.get(Calendar.MONTH);
	    	int dayofmonth = now.get(Calendar.DAY_OF_MONTH);
	    	return new DatePickerDialog(getActivity(), listener, year, month, dayofmonth);
	    }
	}

}