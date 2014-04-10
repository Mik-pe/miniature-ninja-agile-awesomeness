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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;


public class HomeFragment extends Fragment 
{
	
	int hour,min, newHour, newMin;
	int holder = 0;
	static final int TIME_DIALOG_ID=0;

	int[] hourmin = {0,0};
	View currentPage;
	ListView projectListView;
	private View rootView;
	private View listView;
	Button createProjectButton;
	TextView projectTimeTextViewVar;
	ArrayAdapter<Project> projectAdapter;
	

	public static User user = HomeActivity.user;
	List<Project> projectList = user.getProjects();
	
	public HomeFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
        Calendar c = Calendar.getInstance();
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        listView = inflater.inflate(R.layout.project_listview_item, container, false);
        
        createProjectButton = (Button) rootView.findViewById(R.id.create_project_button);
        createProjectButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), CreateProjectActivity.class);
				startActivity(intent);
				
			}
		});
        
       
        projectListView = (ListView) rootView.findViewById(R.id.projectListView);
        
        projectAdapter = new ProjectListAdapter();
        
        projectListView.setAdapter(projectAdapter);
         
        return rootView;
    }
	
    //Creating the dialog for the specific time
	public void showTimeDialog(View v)
    {
		
		//calculates what page and position we are at
		holder = projectListView.getPositionForView(v);
    	
    	//Calculate what hour and minute that we are at when we click
		try 
		{
			hourmin = user.getProjects().get(holder).getTimeByDate(Calendar.getInstance()).getTimeAsArray();	    	
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
			Calendar c = Calendar.getInstance();
			
			user.getProjects().get(holder).addTime(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),hour, min);
   			user.getProjects().get(holder).getTimeByDate(c).setTimeBlock(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), hour, min);

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
    			int[] time = currentProject.getTimeByDate(Calendar.getInstance()).getTimeAsArray();
    			projectTime.setText(time[0] + " h : "+ time[1] + " m");
    		}catch (Exception name) {
    			Log.d("ERROR", name + "");
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
}
