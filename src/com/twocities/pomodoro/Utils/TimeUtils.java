package com.twocities.pomodoro.Utils;

import android.os.SystemClock;

public class TimeUtils {
	
	public static String getMinutesLeft(long millseconds) {
		long minutes = (millseconds % (1000 * 60 * 60)) / (1000 * 60);
		return String.format("%02d", minutes);
	}
	
	public static String getSeconds(long millseconds) {
		long seconds = (millseconds % (1000 * 60)) / 1000;
		return String.format("%02d", seconds);
	} 
	
    public static long getTimeNow() {
        return SystemClock.elapsedRealtime();
    }

}