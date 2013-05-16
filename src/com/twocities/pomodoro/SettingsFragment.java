package com.twocities.pomodoro;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Load the preferences from xml
		addPreferencesFromResource(R.xml.settings_preferences);
		getActivity().getActionBar().setTitle("Settings");
	}
}