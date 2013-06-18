package com.twocities.pomodoro.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.twocities.pomodoro.ScreenAlarmActivity;
import com.twocities.pomodoro.data.PomodoroClock;

public class Utils {

	public static void startClock(Context context, String title) {
		SharedPreferences perfs = PreferenceManager
				.getDefaultSharedPreferences(context);
		PomodoroClock clock = new PomodoroClock();
		clock.readFromSharedPerefs(perfs);
		if (clock.isRunning()) {
			Toast.makeText(context, "There is a Clock running.",
					Toast.LENGTH_LONG).show();
			clock.clearSharedPerefs(perfs);
		} else {
			Intent i = new Intent(context, ScreenAlarmActivity.class);
			PomodoroClock newClock = new PomodoroClock(
					PomodoroClock.DEFAULT_LENGTH);
			newClock.updateTitle(title);
			i.putExtra(PomodoroClock.EXTRA_POMODORO, newClock);
			newClock.writeInSharedPerefs(perfs);
			context.startActivity(i);
		}
	}
}