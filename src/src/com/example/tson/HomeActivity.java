package com.example.tson;

//IMPORT OUR OWN CLASSES
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import tson_utilities.Project;
import tson_utilities.User;
import android.R.array;

import java.util.Locale;

import tson.sqlite.helper.DatabaseHelper;
import tson_utilities.*;
//IMPORT ANDROID
import android.os.Bundle;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
//IMPORT ANDROID
import android.widget.TimePicker;

//IMPORT OTHER

public class HomeActivity extends Activity 
{

	int hour,min, newHour, newMin;
	int holder = 0;
	static final int TIME_DIALOG_ID=0;
	String[] hourmin;
	View currentPage;
	ListView projectListView;

	//Database Helper
	DatabaseHelper db;

	
	public static User user = new User("sdf@sdf.com", "Bosse", "b1337");
	List<Project> projectList = user.getProjects();
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        Calendar c = Calendar.getInstance();
        
        setContentView(R.layout.activity_home);
        user.getProjects().get(0).addTime(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 1, 30);
        final LinearLayout rl=(LinearLayout) findViewById(R.id.rl);
        final TextView[] tv=new TextView[10];

        projectListView = (ListView) findViewById(R.id.projectListView);
        
        

        
        ArrayAdapter<Project> projectAdapter = new ProjectListAdapter();
        
        projectListView.setAdapter(projectAdapter);
        
        //DB TEST
        
        db = new DatabaseHelper(getApplicationContext());
        
        Project p1 = new Project("Tester project");
        Log.d("Project Count", "Project Count BEFORE INITILIZATION: " + db.getAllProjects().size());
        
        long p1_id = db.createProject(p1);
        
        
        Log.d("Project Count", "Project Count: " + db.getAllProjects().size());
        
        // get all projects
        Log.d("Get projects", "get all projects");
        List<Project> projs = db.getAllProjects();
        for(Project proj: projs){
        	Log.d("PROJECT", proj.getName());
        }
        

        // create time block
        TimeBlock t1 = new TimeBlock(1,2,3,4,5);
        long t1_id = db.createTimeBlock(t1,p1);
        
        
        
        //delete project
        Log.d("Project Count", "Project Count before delete: " + db.getAllProjects().size());
        //db.deleteProject(p1_id);
        
        Log.d("Project Count", "Project Count after delete: " + db.getAllProjects().size());
        
        db.closeDB();


        
    }

    //Creating the dialog for the specific time
   	public void showTimeDialog(View v)
       {
   		//calculates what page and position we are at
   		holder = projectListView.getPositionForView(v);
   		currentPage = (View) v.getParent();
       	
       	//Calculate what hour and minute that we are at when we click
       	hourmin = user.getProjects().get(holder).getTimeByDate(Calendar.getInstance()).split(" h : ");
       	hourmin[1] = hourmin[1].replaceAll("m", "");
       	hourmin[1] = hourmin[1].replaceAll(" ", "");
       	if(hourmin[1].equals("--")) {
       		hourmin[0] = "0";
       		hourmin[1] = "0";
       	}
       	newHour = Integer.parseInt(hourmin[0]);
       	newMin = Integer.parseInt(hourmin[1]);
       	
       	//Calls the onCreateDialog
       	showDialog(holder);
       }
   	
   	//Creates the Dialog with the right time from which click
       protected Dialog onCreateDialog(int id)
       {
       	return new TimePickerDialog(this, timeSetListener, newHour, newMin, true);	
       }
       
       //When u click Done in the dialog it will save it in the user and print the time out
       private TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {
   		@Override
   		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
   			hour=hourOfDay;
   			min=minute;
   			Calendar c = Calendar.getInstance();
   			
   			user.getProjects().get(holder).addTime(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), hour, min);
   			
   			TextView et=(TextView) currentPage.findViewById(R.id.projectTimeTextView);
   			et.setText(hour+ " h : "+min + " m");
   		}
   	};
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }
    /**
     * Opens the Create Project View when the "Create Project" button is clicked
     * @param view - View for Create Project Screen
     */
    public void openCreateProjectActivity(View view)
    {    	
    	Intent intent = new Intent(this, CreateProjectActivity.class);
    	startActivity(intent);
    	
    }
    
    private class ProjectListAdapter extends ArrayAdapter<Project>
    {
    	public ProjectListAdapter()
    	{
    		super(HomeActivity.this, R.layout.project_listview_item, projectList);
    	}
    	
    	@Override
    	public View getView(int position, View view, ViewGroup parent)
    	{
    		if(view == null)
    			view = getLayoutInflater().inflate(R.layout.project_listview_item, parent, false);
    		
    		Project currentProject = projectList.get(position);
    		
    		TextView projectName = (TextView) view.findViewById(R.id.projectNameTextView);
    		projectName.setText(currentProject.getName());
    		
    		TextView projectTime = (TextView) view.findViewById(R.id.projectTimeTextView);
    		projectTime.setText(currentProject.getTimeByDate(Calendar.getInstance()));
    		
    		Button editButton = (Button) view.findViewById(R.id.editTimeButton);		
    		
    		return view;
    	}
    }
   
    
}
