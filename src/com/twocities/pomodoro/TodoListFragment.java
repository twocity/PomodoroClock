package com.twocities.pomodoro;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public abstract class TodoListFragment extends ListFragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.todo_list, null);
		return v;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
//		setListAdapter(new ArrayAdapter<String>(getActivity(),
//                android.R.layout.simple_list_item_1, TODOLIST));
	}
	
	
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
    }
    
    public static final String[] TODOLIST = 
    {
            "Add Setting Screen to DidaClient",   
            "fix bug #34512",
            "Change ListView background",       
            "try to add ActionBarSherlock",
            "write blog",
            "drink coffee",
            "Add Setting Screen to DidaClient",   
            "fix bug #34512",
            "Change ListView background",       
            "try to add ActionBarSherlock",
            "write blog",
            "drink coffee",
            "Add Setting Screen to DidaClient",   
            "fix bug #34512",
            "Change ListView background",       
            "try to add ActionBarSherlock",
            "write blog",
            "drink coffee",
            "Add Setting Screen to DidaClient",   
            "fix bug #34512",
            "Change ListView background",       
            "try to add ActionBarSherlock",
            "write blog",
            "drink coffee",
            "Add Setting Screen to DidaClient",   
            "fix bug #34512",
            "Change ListView background",       
            "try to add ActionBarSherlock",
            "write blog",
            "drink coffee"
    };
}