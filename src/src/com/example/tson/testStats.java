package com.example.tson;

import java.util.Calendar;
import java.util.List;


import tson_utilities.Project;
import tson_utilities.User;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Path.FillType;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class testStats extends Fragment {
	ViewPager Tab;
    TabAdapter TabAdapter;
	ActionBar actionBar;
	ArrayAdapter<Project> statsAdapter;
	ListView projectListView;
	private View test;
	public static User user = HomeActivity.user;
	
	List<Project> projectListStats = user.getProjects();
	

	public testStats(){}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
        test = inflater.inflate(R.layout.fragment_test, container, false);
        projectListView = (ListView) test.findViewById(R.id.statistics_view);
        
        statsAdapter = new statsAdapter();
        
        projectListView.setAdapter(statsAdapter);
		
        
			return test;
    }
    
    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
    
    private class statsAdapter extends ArrayAdapter<Project>{
    	
    	
		public statsAdapter() {
			super(getActivity(), R.layout.statistics_listview_item, projectListStats);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public View getView(int position, View view, ViewGroup parent){
			
			if(view == null)
				view = getActivity().getLayoutInflater().inflate(R.layout.statistics_listview_item, parent, false);
			
	        TextView projectItem = (TextView) view.findViewById(R.id.statistics_project_item);
	        TextView progBar = (TextView) view.findViewById(R.id.statistics_progress_bar);
	        //double x = 0.2;
	        
	        double holder = 300;
	        double x = 0.2;
	        
//	        Log.d("Holder",""+holder);
	        
	        int y = (int) (x * holder);
	        
	        Log.d("YYYYYYY",""+holder);
	        
	        RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(dpToPx(y), dpToPx(40));
	        rl.setMargins(dpToPx(5),dpToPx(5),dpToPx(5),dpToPx(5));
	        
	        progBar.setLayoutParams(rl);
	        
	        
	        return view;
			
		}
    }

    
}
