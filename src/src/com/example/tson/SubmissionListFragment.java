package com.example.tson;


import java.util.ArrayList;



import java.util.Calendar;
import java.util.List;

import tson_utilities.Project;
import tson_utilities.TimeBlock;
import android.R.layout;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class SubmissionListFragment extends Fragment {
	List<SubmissionDayListItem> subList = new ArrayList<SubmissionDayListItem>();
	ListView submissionListView;
	List<Project> projectList = HomeActivity.user.getProjects();
	Calendar today = Calendar.getInstance();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View subListView = inflater.inflate(R.layout.submission_list, container, false);
		
		submissionListView = (ListView) subListView.findViewById(R.id.submittedDaysListView);
		
		for(int i=0;i<30;i++)
		{
			subList.add(new SubmissionDayListItem(today,HomeActivity.user.getTimeByDate(today), HomeActivity.user.isDateConfirmed(today)));
			today.add(Calendar.DAY_OF_YEAR, -1);
		}
		
		ArrayAdapter<SubmissionDayListItem> subAdapter = new SubmissionListAdapter();
		
		submissionListView.setAdapter(subAdapter);
		
		return subListView;
	}


	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getActivity().getMenuInflater().inflate(R.menu.sub_list, menu);
		return true;
	}
	
	
	public class SubmissionDayListItem
	{
		Calendar today;
		int timeWorked;
		int isConfirmed;
		
		public SubmissionDayListItem(Calendar c, int t, int i){
			today = (Calendar) c.clone();
			timeWorked=t;
			isConfirmed = i;
		}
	}
	
	private class SubmissionListAdapter extends ArrayAdapter<SubmissionDayListItem>
    {
		
		public SubmissionListAdapter()
    	{
    		super(getActivity(), R.layout.submissionlist_day_item, subList);
    		
    	}
    	
    	@Override
    	public View getView(int position, View view, ViewGroup parent)
    	{
    		
    		if(view == null)
    			view = getActivity().getLayoutInflater().inflate(R.layout.submissionlist_day_item, parent, false);
    		final SubmissionDayListItem currentItem = subList.get(position);
    		Log.d("Adapter call", "here"+currentItem.today.get(Calendar.DAY_OF_WEEK));
    		TextView submissionDate = (TextView) view.findViewById(R.id.submissionDate);
    		TextView projectTime = (TextView) view.findViewById(R.id.workTime);
    		Button editButton = (Button) view.findViewById(R.id.editDayButton);	
    		TextView weekText = (TextView) view.findViewById(R.id.weekText);
    		
    		if(position == 0 || currentItem.today.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY)
    		{
    			weekText.setText("Week: "+currentItem.today.get(Calendar.WEEK_OF_YEAR));
    			
    			RelativeLayout.LayoutParams params =  (RelativeLayout.LayoutParams)submissionDate.getLayoutParams();
    			params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
    			params.addRule(RelativeLayout.BELOW, R.id.weekText);
    			
    			RelativeLayout.LayoutParams paramsButton =  (RelativeLayout.LayoutParams)editButton.getLayoutParams();
    			paramsButton.addRule(RelativeLayout.BELOW, R.id.weekText);
    		}
    		else
    		{
    			weekText.setText("");
    			RelativeLayout.LayoutParams params =  (RelativeLayout.LayoutParams)submissionDate.getLayoutParams();
    			params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
    			params.addRule(RelativeLayout.BELOW, 0);
    			
    			RelativeLayout.LayoutParams paramsButton =  (RelativeLayout.LayoutParams)editButton.getLayoutParams();
    			paramsButton.addRule(RelativeLayout.BELOW, 0);
    		}
    		
    		submissionDate.setText(currentItem.today.get(Calendar.DAY_OF_MONTH)+"/"+(currentItem.today.get(Calendar.MONTH)+1));
    				
    		projectTime.setText(currentItem.timeWorked/60 + ":" +currentItem.timeWorked%60);
    		
    		//TODO MAKE THIS WORK WITH BOOLEAN VARIABLE

			if(currentItem.isConfirmed==1)
				view.setBackgroundColor(Color.rgb(126, 218, 126));//green
			else if(currentItem.isConfirmed==0)
				view.setBackgroundColor(Color.rgb(246, 237, 134)); //yellow
   			else
				view.setBackgroundColor(Color.rgb(245, 116, 103)); //red (getTimeByDate(currentDate) == 0

    		editButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
								
					int dateDifference = -(Calendar.getInstance().get(Calendar.DAY_OF_YEAR) - currentItem.today.get(Calendar.DAY_OF_YEAR));
					
					Fragment switchToFragment = new HomeFragment();
					Bundle bundle = new Bundle();
					bundle.putInt("dateDifference", dateDifference);
					switchToFragment.setArguments(bundle);
					
					ActionBar actionBar = getActivity().getActionBar();
					actionBar.removeAllTabs();
					actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
					getActivity().setTitle("Home");					
					
					FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
					fragmentManager.beginTransaction()
					.replace(R.id.frame_container, switchToFragment).commit();
				}
			});
    		
    		return view;
    	}
    }

}
