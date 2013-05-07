package com.twocities.pomodoro;

import android.app.Activity;
import android.os.Bundle;

public class TaskEditActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_task_edit);
		
		getActionBar().setDisplayShowCustomEnabled(true);
		getActionBar().setDisplayUseLogoEnabled(false);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setCustomView(R.layout.layout_edit_task_actionbar);
	}
}