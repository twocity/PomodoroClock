package com.twocities.pomodoro;

import net.simonvt.menudrawer.MenuDrawer;
import android.app.Activity;
import android.os.Bundle;

public class HomeActivity extends Activity {
	private MenuDrawer mMenuDrawer;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		initViews();
	}
	
	private void initViews() {
		mMenuDrawer = (MenuDrawer) findViewById(R.id.drawer);
		mMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN);
	}
}