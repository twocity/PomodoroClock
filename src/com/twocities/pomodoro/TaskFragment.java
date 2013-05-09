package com.twocities.pomodoro;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.twocities.pomodoro.data.Task;

public class TaskFragment extends Fragment {

	private TextView mTitle;
	private TextView mDescription;
	private Task mTask;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_task, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mTitle = (TextView) view.findViewById(R.id.task_title);
		mDescription = (TextView) view.findViewById(R.id.task_description);
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (getArguments() != null) {
			mTask  = getArguments().getParcelable(Task.EXTRA_TASK_DATA);
			if (mTask == null) {
				throw new IllegalArgumentException("Task from Bundle is null!");
			}
			mTitle.setText(mTask.getTitle());
			mDescription.setText(mTask.getDescription());
		}

	}
	
	private void editTask(Task task) {
		Intent intent = new Intent(getActivity(), TaskEditActivity.class);
		intent.putExtra(Task.EXTRA_TASK_DATA, task);
		this.startActivity(intent);
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
				break;
			case R.id.m_share:
				break;
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}
}