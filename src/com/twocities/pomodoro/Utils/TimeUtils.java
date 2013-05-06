package com.twocities.pomodoro.Utils;

import java.util.Calendar;

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
        return System.currentTimeMillis();
    }
    
    public static String[] rangeOfToday() {
    	Calendar now = Calendar.getInstance();
    	now.set(Calendar.HOUR_OF_DAY, 0);
    	now.set(Calendar.MINUTE, 0);
    	now.set(Calendar.SECOND, 0);
    	String[] result = new String[2];
    	result[0] = String.valueOf(now.getTimeInMillis());
    	now.set(Calendar.HOUR_OF_DAY, 24);
    	result[1] = String.valueOf(now.getTimeInMillis());
    	return result;
    }
    
    public static String[] rangeOfTomorrow() {
    	return null;
    }
    
    public static boolean isToday(long time) {
    	Calendar toCompare = Calendar.getInstance();
    	toCompare.setTimeInMillis(time);
    	
    	Calendar now = Calendar.getInstance();
    	if (toCompare.get(Calendar.YEAR) == now.get(Calendar.YEAR) && 
    			toCompare.get(Calendar.MONTH) == now.get(Calendar.MONTH) &&
    			toCompare.get(Calendar.DATE) == now.get(Calendar.DATE)) {
    		return true;
    	}
    	return false;
    }
    
    public static boolean isTomorrow(long time) {
    	return false;
    }
    
    public static boolean isThisWeek(long time) {
    	return false;
    }

}