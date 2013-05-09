package com.twocities.pomodoro;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.twocities.pomodoro.data.PomodoroClock;
import com.twocities.pomodoro.settings.SettingActivity;

public class PomodoroActivity extends Activity{
	private EditText mQuickStart;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pomodoro);
		
		mQuickStart = (EditText) findViewById(R.id.quick_add);
		mQuickStart.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_DONE) {
					startClock(v.getText().toString());
					return true;
				}
				return false;
			}
		});
	}
	
	public void doClick(View v) {
		switch(v.getId()){
			default:
				break;
		}
	}
	
	private void startClock(String title) {
		SharedPreferences perfs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		PomodoroClock clock = new PomodoroClock();
		clock.readFromSharedPerefs(perfs);
		if(clock.isRunning()) {
			Toast.makeText(getApplicationContext(), "There is a Clock running.", Toast.LENGTH_LONG).show();
			clock.clearSharedPerefs(perfs);
		}else {
			Intent i = new Intent(this, ScreenAlarmActivity.class);
			PomodoroClock newClock = new PomodoroClock(PomodoroClock.DEFAULT_LENGTH);
			newClock.updateTitle(title);
			i.putExtra(PomodoroClock.EXTRA_POMODORO, newClock);
			newClock.writeInSharedPerefs(perfs);
			this.startActivity(i);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.setting, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case R.id.action_settings:
				Intent i = new Intent(this, SettingActivity.class);
				this.startActivity(i);
				break;
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}
}