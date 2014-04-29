
package com.example.tson;

import java.util.ArrayList;

import com.example.tson.HomeActivity;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import qustomstyle.QustomDialogBuilder;

import tson.sqlite.helper.DatabaseHelper;
import tson_utilities.Project;
import tson_utilities.TimeBlock;
import tson_utilities.User;
import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;


public class HomeFragment extends Fragment implements View.OnTouchListener
{
	/***********************
	  *  	VARIABLES		*/	
	 /***********************/
	
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
	Button createProjectButton;
	Button reportTimeButton; 
	TextView projectTimeTextViewVar;
	

	TextView dateText;
	ImageButton prevDate;
	ImageButton nextDate;
	ArrayAdapter<Project> projectAdapter;
	Calendar homeFragmentCalendar;
	private static final int MIN_DISTANCE = 100;
    private float downX, downY, upX, upY;
	public static User user = HomeActivity.user;
	List<Project> projectList = user.getProjects();
	
	/***********************
	  *  	CONSTRUCTOR		*/	
	 /***********************/
	
	public HomeFragment(){}
	
	/**
	 * OnCreateView for home fragment will catch eventual datechanges for when HomeFragment is
	 * Called from Submissions.
	 * It creates onClicks for the previous and next Dates.
	 *
	 */
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        
        /**
         * Bundle contains the date difference from submission.
         * The homeFragmentCalendar has its' date changed
         */
        Bundle bundle = this.getArguments();
        homeFragmentCalendar = (Calendar) HomeActivity.getCal().clone();
        int dateDifference = 0;
        try{
	         dateDifference =(int) bundle.getLong("dateDifference");
	         homeFragmentCalendar.add(Calendar.DAY_OF_YEAR, dateDifference);
        }catch(Exception e){Log.d("HerregudNull", "Nu blev det null!!!!");} 
        
        /**
         * Creates the onClick-function for the PREVIOUSDATE-image
         */
        prevDate = (ImageButton) rootView.findViewById(R.id.imageButton2);
        prevDate.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		homeFragmentCalendar.add(Calendar.DAY_OF_YEAR, -1);
        		dateText.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.left_to_right));
                projectListView.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.left_to_right));
        		newDate(homeFragmentCalendar);
        	}
        });
        
        /**
         * Creates the onClick-function for the NEXTDATE-image
         */
        nextDate = (ImageButton) rootView.findViewById(R.id.imageButton3);
        nextDate.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		if(!(homeFragmentCalendar.get(Calendar.YEAR) == HomeActivity.getCal().get(Calendar.YEAR) && homeFragmentCalendar.get(Calendar.DAY_OF_YEAR) == HomeActivity.getCal().get(Calendar.DAY_OF_YEAR)))
            	{
	        		homeFragmentCalendar.add(Calendar.DAY_OF_YEAR, 1);
	        		dateText.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.right_to_left));
                    projectListView.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.right_to_left));
	        		newDate(homeFragmentCalendar);
            	}
        			
        	}
        });
        
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
            
        //Sets touch listener to be the homeFragment, which implements the touchlistener for swiping
        rootView.setOnTouchListener(this);
        newDate(homeFragmentCalendar); 
        return rootView;
        
    }//End OnCreate-function
	
	/**
	 * newDate updates the view. Should be called when a change has been made to the date in homeFragment.
	 * Also adds the touch event handler to the projectListView, so it's scrollable.
	 * @param c - calendar for the new date.
	 */
	public void newDate(Calendar c)
	{		
		homeFragmentCalendar = (Calendar) c.clone();
		
        dateText = (TextView) rootView.findViewById(R.id.projectNameTextView);
        nextDate = (ImageButton) rootView.findViewById(R.id.imageButton3);
        if( homeFragmentCalendar.get(Calendar.YEAR) == HomeActivity.getCal().get(Calendar.YEAR) && homeFragmentCalendar.get(Calendar.DAY_OF_YEAR) == HomeActivity.getCal().get(Calendar.DAY_OF_YEAR))
        {
        	dateText.setText("Today");
        	nextDate.setAlpha((float)0.25);
        }
        else
        {

        	dateText.setText(Html.fromHtml("<big>" + homeFragmentCalendar.get(Calendar.DAY_OF_MONTH)+"/"+(homeFragmentCalendar.get(Calendar.MONTH)+1) + "</big>" + "  -  " + "<small>" +  c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH) + "</center>" + "</small>" ));
        	nextDate.setAlpha((float)1.0);
        }
        projectListView = (ListView) rootView.findViewById(R.id.projectListView);
        projectListView.setOnTouchListener(new ListView.OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				switch(action){
				case MotionEvent.ACTION_DOWN:
					v.getParent().requestDisallowInterceptTouchEvent(true);
					break;
				case MotionEvent.ACTION_UP:
					v.getParent().requestDisallowInterceptTouchEvent(false);
					break;
				}
				
				// handle listview touch events.
				v.onTouchEvent(event);
				return true;
			}
        });
        
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
	    	
		}
		catch (Exception name) 		
		{
			Log.d("ERROR", name + "");
		}
    	
    	/**
    	 * Show the TimePickerDialog
    	 */
    	new TimePickerDialog(getActivity(), timeSetListener,  newHour, newMin, true).show();
    }
	
	//
	/**
	 * Creates the Dialog with the right time from which click   
	 * When you click Done in the dialog it will save it in the user and print the time out
	 */
    private TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			hour=hourOfDay;
			min=minute;
			
			// Update to see if timeblock already exists
			//user.getProjects().get(holder).addTime(homeFragmentCalendar.get(Calendar.YEAR), homeFragmentCalendar.get(Calendar.MONTH), homeFragmentCalendar.get(Calendar.DAY_OF_MONTH),hour, min);
			user.getProjects().get(holder).addTime(homeFragmentCalendar, hour, min);
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
    
    /**
     * Creating the dialog for confirming reported time
     * @param v - the View for this.
     */
   	public void showReportDialog(View v)
    {
   		//AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
   		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomDialogTheme);
   		//LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
   		//Add title
   		builder.setTitle(R.string.title_confirm_time);
   		
   		//Add message
   		builder.setMessage(R.string.confirm_dialog_message);
   		
   		/*QustomDialogBuilder qBuilder = new QustomDialogBuilder(getActivity());
   		qBuilder.setTitle(R.string.title_confirm_time);
   		qBuilder.setTitleColor("#063A70");
   		qBuilder.setDividerColor("#DB8C3A");
   		qBuilder.setMessage(R.string.confirm_dialog_message);
*/
   		
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

	       					TimeBlock t = p.getTimeByDate(homeFragmentCalendar);
	       					if(t!=null){
	       						
	       					t.setConfirmed(1);
	       					db.setConfirmed(t);}
	       				
	       		}
	       		//Create Submission fragment
	    	   	Fragment fragment = new SubmissionFragment();
	        	if (fragment != null) 
	        	{
		 			FragmentManager fragmentManager = getFragmentManager();
		 			fragmentManager.beginTransaction()
		 			.replace(R.id.frame_container, fragment).commit();
		 			getActivity().setTitle("Submissions");	
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
	   //qBuilder.show();
		
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
   		/**
   		 *Constructor, calls the superclass constructor which will call getView (below)
   		 *for each element in projectList
   		 */
  
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
    		ImageButton projectTime2 = (ImageButton) view.findViewById(R.id.imageButton1);

    		TimeBlock t1 = currentProject.getTimeByDate(homeFragmentCalendar);
    		
    		if(t1!=null){
    		if(t1.getConfirmed() == 1)
    		{
            	dateText.setTextColor(getResources().getColor(R.color.calender_green));
    		}
    		else if(t1.getConfirmed() == 0)
    		{
    			dateText.setTextColor(getResources().getColor(R.color.calender_yellow));
    		}}
    		else{
    			dateText.setTextColor(getResources().getColor(R.color.combitech_grey));
    		}
    		
    		
    		try{
    			int[] time = t1.getTimeAsArray();
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
    		
    		projectTime2.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {

					showTimeDialog(v);					
				}
			});	
    		
    		return view;
    	}
    }
   /**
   * OnTouch for swiping between days on the home screen
   */
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
            /**
             * Only check if we swipe some distance horizontally
             */
            if (Math.abs(deltaX) > MIN_DISTANCE) {
                /**
                 * LEFT -> RIGHT
                 */
                if (deltaX < 0  && (Math.abs(deltaY) < 100) ) {
                    homeFragmentCalendar.add(Calendar.DAY_OF_YEAR, -1);
                    dateText.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.left_to_right));
                    projectListView.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.left_to_right));
                    newDate(homeFragmentCalendar);
                    return false;
                }
                /**
                 * RIGHT -> LEFT
                 */
                if (deltaX > 0 && (Math.abs(deltaY) < 100) ) {
                	if(!(homeFragmentCalendar.get(Calendar.YEAR) == HomeActivity.getCal().get(Calendar.YEAR) && homeFragmentCalendar.get(Calendar.DAY_OF_YEAR) == HomeActivity.getCal().get(Calendar.DAY_OF_YEAR)))
                	{
                		homeFragmentCalendar.add(Calendar.DAY_OF_YEAR, 1);
                		dateText.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.right_to_left));
                        projectListView.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.right_to_left));
                		newDate(homeFragmentCalendar);
                	}
                    return false;
                }    
            return false; // allow other events like Click to be processed
            }
		}
		return false;
	}    
}

