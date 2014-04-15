package com.example.tson;

import java.util.ArrayList;
import com.example.tson.HomeActivity;
import java.util.Calendar;
import java.util.List;

import tson.sqlite.helper.DatabaseHelper;
import tson_utilities.Project;
import tson_utilities.TimeBlock;
import tson_utilities.User;
import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
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
	//Extend Home Activity to connect to DB
	public HomeActivity homeActivity = new HomeActivity();
	DatabaseHelper db = homeActivity.db;
	
	//Update reported time
	int hour, min, newHour, newMin;
	int holder = 0;
	static final int TIME_DIALOG_ID = 0;

	int[] hourmin = {0,0};
	View currentPage;
	ListView projectListView;
	private View rootView;
	private View listView;
	Button createProjectButton;
	Button reportTimeButton; 
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
        
        //Declaration of buttons on home screen
        createProjectButton = (Button) rootView.findViewById(R.id.create_project_button);
        reportTimeButton = (Button) rootView.findViewById(R.id.report_time);
        
        /**
         * PopUp for "Create Project" button
         */
        createProjectButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), CreateProjectActivity.class);
				startActivity(intent);
				
			}
		});
        
        /**
         * PopUp for "Confirm Time" button
         */
        reportTimeButton.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {				
				showReportDialog(v);
			}
		});
            
        projectListView = (ListView) rootView.findViewById(R.id.projectListView);       
        projectAdapter = new ProjectListAdapter();      
        projectListView.setAdapter(projectAdapter);
         
        return rootView;
        
    }//End OnCreate-function
	
    /**
     * Creating the dialog for changing the specific time
     * */
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
	    	
		}
		catch (Exception name) 		
		{
			Log.d("ERROR", name + "");
		}
    	
    	//Show dialog for time reporting
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
    
    /**
     * Creating the dialog for confirming reported time
     * @param v
     */
   	public void showReportDialog(View v)
    {
   		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	   	//Add title
   		builder.setTitle(R.string.title_confirm_time);
   		
   		//Add message
   		builder.setMessage(R.string.confirm_dialog_message);
 		
   		//Add the buttons 		
	   	builder.setPositiveButton(R.string.confirm_button, new DialogInterface.OnClickListener() 
	   	{	   	
		    // User clicked OK button - Go to submission page       
		   	public void onClick(DialogInterface dialog, int id) 
		   	{   	        	   
	       		//Change status on all reported timeblocks to Confirmed = true
	       		//List<Project> projectList = (ArrayList<Project>) user.getProjects();
	       		
	       		for(int i=0; i<projectList.size(); i++)
	       		{
	       			Project p = projectList.get(i);
	       			List<TimeBlock> s = p.getSubmissionList();
	       			
	       				for(int j=0; j<s.size() ; j++)
	       				{    					
	       					s.get(j).setConfirmed(1);
	       					db.setConfirmed(s.get(j));
	       				}
	       		}
	       		//Create Submission fragment
	    	   	Fragment fragment = new SubmissionFragment();
	        	if (fragment != null) 
	        	{
		 			FragmentManager fragmentManager = getFragmentManager();
		 			fragmentManager.beginTransaction()
		 			.replace(R.id.frame_container, fragment).commit();
		 			getActivity().setTitle("Submissions");
		 			Log.d("time block confirmed", "test");				   	
		   	 	} 
		   	}
       });
	   //Cancel button close the dialog and go back to home screen
	   builder.setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() 
	   {
           public void onClick(DialogInterface dialog, int id) 
           {
               
           }
	   });
	   	
	   	// Create the AlertDialog
	   	AlertDialog dialog = builder.create();
	   	dialog.show();
	   	
    }//End Dialog confirm reported time
    
    /**
     * The ProjectListAdapter class takes the projectList Array and converts the items into 
     * View objects to be loaded into the ListView container.
     */
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
    			Log.d("Test", "Hej");
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
}
