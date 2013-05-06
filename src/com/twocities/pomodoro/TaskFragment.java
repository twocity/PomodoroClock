package com.twocities.pomodoro;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class TaskFragment extends Fragment {

	private EditText mTitle;
	private EditText mDescription;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_task, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mTitle = (EditText) view.findViewById(R.id.task_title);
		mDescription = (EditText) view.findViewById(R.id.task_description);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Bundle task = getArguments();
		if(task != null) {
			mTitle.setText(task.getString("title"));
			mDescription.setText(task.getString("description"));
		}

	}
}