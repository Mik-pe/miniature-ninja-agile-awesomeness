package com.example.tson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import tson_utilities.Project;
import tson_utilities.User;


import android.app.Dialog;

import android.support.v4.app.Fragment;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;


public class HomeFragment extends Fragment implements View.OnTouchListener
{
	
	int hour,min, newHour, newMin;
	int holder = 0;
	static final int TIME_DIALOG_ID=0;

	int[] hourmin = {0,0};
	View currentPage;
	ListView projectListView;
	private View rootView;
	Button createProjectButton;
	TextView dateText;
	ImageButton prevDate;
	ImageButton nextDate;
	ArrayAdapter<Project> projectAdapter;
	Calendar homeFragmentCalendar;
	private static final int MIN_DISTANCE = 100;
    private float downX, downY, upX, upY;
    
	public static User user = HomeActivity.user;
	List<Project> projectList = user.getProjects();
	
	public HomeFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        
        Bundle bundle = this.getArguments();
        homeFragmentCalendar = Calendar.getInstance();
        
        int dateDifference = 0;
        try{
	         dateDifference = bundle.getInt("dateDifference");
	         homeFragmentCalendar.add(Calendar.DAY_OF_YEAR, dateDifference);
        }catch(Exception e){Log.d("HerregudNull", "Nu blev det null!!!!");} 
        
        
        prevDate = (ImageButton) rootView.findViewById(R.id.imageButton2);
        prevDate.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		homeFragmentCalendar.add(Calendar.DAY_OF_YEAR, -1);
        		newDate(homeFragmentCalendar);
        	}
        });
        
        nextDate = (ImageButton) rootView.findViewById(R.id.imageButton3);
        nextDate.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		if(homeFragmentCalendar.get(Calendar.DATE) != HomeActivity.getCal().get(Calendar.DATE))
            	{
	        		homeFragmentCalendar.add(Calendar.DAY_OF_YEAR, 1);
	        		newDate(homeFragmentCalendar);
            	}
        	}
        });
        
        createProjectButton = (Button) rootView.findViewById(R.id.create_project_button);
        createProjectButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), CreateProjectActivity.class);
				startActivity(intent);
			}
		});
        //Sets touch listener to be the homeFragment, which implements the touchlistener for swiping
        rootView.setOnTouchListener(this);
        newDate(homeFragmentCalendar); 
        return rootView;
    }
	/**
	 * newDate updates the view. Should be called when a change has been made to the date in homeFragment.
	 * @param c - calendar for the new date.
	 */
	public void newDate(Calendar c)
	{
		homeFragmentCalendar = (Calendar) c.clone();
		
        dateText = (TextView) rootView.findViewById(R.id.projectNameTextView);
        if(homeFragmentCalendar.get(Calendar.DATE) == HomeActivity.getCal().get(Calendar.DATE))
        {
        	dateText.setText("Today");
        }
        else
        {
        	dateText.setText(homeFragmentCalendar.get(Calendar.DAY_OF_MONTH)+"/"+(homeFragmentCalendar.get(Calendar.MONTH)+1));
        }
        
        projectListView = (ListView) rootView.findViewById(R.id.projectListView);
        
        projectAdapter = new ProjectListAdapter();
        
        projectListView.setAdapter(projectAdapter);
	}
	
    /**
     * ShowTimeDialog shows the time dialog when a textfield has been clicked.
     * @param v - the view for the timedialog.
     */
	public void showTimeDialog(View v)
    {
		
		//calculates what page and position we are at
		holder = projectListView.getPositionForView(v);
    	
    	//Calculate what hour and minute that we are at when we click
		try 
		{
			hourmin = user.getProjects().get(holder).getTimeByDate(homeFragmentCalendar).getTimeAsArray();	    	
	    	newHour = hourmin[0];
	    	newMin = hourmin[1];
		}catch (Exception name) 
		{
			Log.d("ERROR", name + "");
		}
    	
    	//Visar dialogrutan med timepicker
    	new TimePickerDialog(getActivity(), timeSetListener,  newHour, newMin, true).show();
    }
	
	//Creates the Dialog with the right time from which click
    
    //When u click Done in the dialog it will save it in the user and print the time out
    private TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			hour=hourOfDay;
			min=minute;

			user.getProjects().get(holder).addTime(homeFragmentCalendar.get(Calendar.YEAR), homeFragmentCalendar.get(Calendar.MONTH), homeFragmentCalendar.get(Calendar.DAY_OF_MONTH),hour, min);
   			user.getProjects().get(holder).getTimeByDate(homeFragmentCalendar).setTimeBlock(homeFragmentCalendar.get(Calendar.YEAR), homeFragmentCalendar.get(Calendar.MONTH), homeFragmentCalendar.get(Calendar.DAY_OF_MONTH), hour, min);

			projectAdapter.notifyDataSetChanged();
		}
	};
    
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }
    /**
     * Opens the Create Project View when the "Create Project" button is clicked
     * @param view - View for Create Project Screen
     */
    public void openCreateProjectActivity(View view)
    {    	
    	Intent intent = new Intent(getActivity(), CreateProjectActivity.class);
    	getActivity().startActivity(intent);
    	
    }
    
    private class ProjectListAdapter extends ArrayAdapter<Project>
    {
    	public ProjectListAdapter()
    	{
    		super(getActivity(), R.layout.project_listview_item, projectList);
    	}
    	
    	@Override
    	public View getView(int position, View view, ViewGroup parent)
    	{
    		if(view == null)
    			view = getActivity().getLayoutInflater().inflate(R.layout.project_listview_item, parent, false);
    		
    		Project currentProject = projectList.get(position);
    		
    		TextView projectName = (TextView) view.findViewById(R.id.projectNameTextView);
    		projectName.setText(currentProject.getName());
    		
    		TextView projectTime = (TextView) view.findViewById(R.id.projectTimeTextView);

    		try{
    			int[] time = currentProject.getTimeByDate(homeFragmentCalendar).getTimeAsArray();
    			projectTime.setText(time[0] + " h : "+ time[1] + " m");
    		}catch (Exception name) {
    			Log.d("ERROR", name + "");
    			projectTime.setText("0 h : 0 m");
    		}
    		
    		projectTime.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {

					showTimeDialog(v);					
				}
			});		
    		
    		return view;
    	}
    }
    
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		float deltaX;
		float deltaY;	

		switch (event.getAction()) {
            
        case MotionEvent.ACTION_DOWN:
            downX = event.getX();
            downY = event.getY();
        case MotionEvent.ACTION_UP:
            upX = event.getX();
            upY = event.getY();
            
            deltaX = downX - upX;
            deltaY = downY - upY;
            // horizontal swipe detection
            if (Math.abs(deltaX) > MIN_DISTANCE) {
                // left or right
                if (deltaX < 0  && (Math.abs(deltaY) < 100) ) {
                    homeFragmentCalendar.add(Calendar.DAY_OF_YEAR, -1);
                    newDate(homeFragmentCalendar);
                    dateText.scrollTo(0, (int) dateText.getY());
                    return false;
                }
                if (deltaX > 0 && (Math.abs(deltaY) < 100) ) {
                	if(homeFragmentCalendar.get(Calendar.DATE) != HomeActivity.getCal().get(Calendar.DATE))
                	{
                		homeFragmentCalendar.add(Calendar.DAY_OF_YEAR, 1);
                		newDate(homeFragmentCalendar);
                		dateText.scrollTo(0, (int) dateText.getY());
                	}
                    return false;
                }    
            return false; // allow other events like Click to be processed
            }
		}
		return false;
	}    
}
