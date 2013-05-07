package com.twocities.pomodoro;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

public class TaskEditActivity extends Activity {
	private Button startDateButton;
	private Button dueDateButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_task_edit);
		
		getActionBar().setDisplayShowCustomEnabled(true);
		getActionBar().setDisplayUseLogoEnabled(false);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setCustomView(R.layout.layout_edit_task_actionbar);
	
		startDateButton = (Button) findViewById(R.id.task_start_date);
		dueDateButton = (Button) findViewById(R.id.task_due_date);
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
	
	private void setDateMessage(Button button, String message) {
		button.setText(message);
	}
	
	private OnDateSetListener startListener = new OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			StringBuffer date = new StringBuffer();
			date.append(year)
				.append("/")
				.append(++monthOfYear)
				.append("/")
				.append(dayOfMonth);
			setDateMessage(startDateButton, date.toString());
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
			setDateMessage(dueDateButton, date.toString());
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