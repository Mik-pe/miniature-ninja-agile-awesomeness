package com.example.tson;

import java.util.Calendar;
import java.util.List;

import tson_utilities.Project;
import tyczj.extendedcalendarview.Day;
import tyczj.extendedcalendarview.ExtendedCalendarView;
import tyczj.extendedcalendarview.ExtendedCalendarView.OnDayClickListener;
import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


public class SubmissionCalFragment extends Fragment {
	
	
	/************************
	  *  	VARIABLES		*/	
	 /***********************/
	List<Project> projectList = HomeActivity.user.getProjects();
	
	ExtendedCalendarView cal;
		
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View subCalView = inflater.inflate(R.layout.submission_cal_fragment, container, false);
        
        cal = (ExtendedCalendarView) subCalView.findViewById(R.id.calendar);
		
        
        //Override the listener in the ExtendedCalenderView.
		cal.setOnDayClickListener(new OnDayClickListener(){

			@Override
			public void onDayClicked(AdapterView<?> adapter, View view,
					int position, long id, Day day) {
				
				Calendar tempCal = Calendar.getInstance();
				Calendar today = Calendar.getInstance();
				tempCal.set(day.getYear(), day.getMonth(), day.getDay());
				long dateDifference = -(today.getTimeInMillis() - tempCal.getTimeInMillis())/(1000*60*60*24);
				
				Log.e("TimeDifference", Long.toString(dateDifference));
				
				//Only past dates are clickable and will change the fragment
				if(dateDifference <= 0)
				{
					//Create a new instance of HomeFragment
					Fragment switchToFragment = new HomeFragment();
					
					//Create a bundle to send the date to HomeFragment
					Bundle bundle = new Bundle();
					bundle.putLong("dateDifference", dateDifference);
					bundle.putString("previousFragment", "Submission");
					switchToFragment.setArguments(bundle);
					
					//Reset the actionBar
					ActionBar actionBar = getActivity().getActionBar();
					actionBar.removeAllTabs();
					actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
					getActivity().setTitle("Home");					
					
					//And switch
					FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
					fragmentManager.beginTransaction()
					.replace(R.id.frame_container, switchToFragment).commit();
				}
				
			}
			
		});
        return subCalView;
	}


	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getActivity().getMenuInflater().inflate(R.menu.sub_list, menu);
		return true;
	}

}
