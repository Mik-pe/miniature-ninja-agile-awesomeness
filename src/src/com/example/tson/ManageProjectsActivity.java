package com.example.tson;

import java.util.List;


import tson_utilities.Project;
import tson_utilities.TimeBlock;
import tson_utilities.User;
import tson.sqlite.helper.*;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ManageProjectActivity Class - Activity accessed from settings. 
 * Displays all projects and offers he possibility to edit the name of a project or add it to the "archive"
 * 
 * @author John, Albin
 */


public class ManageProjectsActivity extends Activity {
	
	/***********************
	  *  	VARIABLES		*/	
	 /***********************/
	MenuItem createProject;
	
	List<Project> projectList = User.getInstance().getProjects();
	ListView manageProjectsListActive;
	ListView manageProjectsListHidden;
	ManageProjectListAdapter projectAdapter;
	DatabaseHelper db = HomeActivity.db;
	int hiddenPosition = -1;
	
	
	/**
	 * OnCreateView for ManageProjectsActivity
	 * 
	 * sets projectadapter, contains onclicklistener for backbutton.
	 *
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_projects);
		setTitle("Manage Projects");
		
		manageProjectsListActive = (ListView) findViewById(R.id.manage_project_list);
		projectAdapter = new ManageProjectListAdapter();
        
		manageProjectsListActive.setAdapter(projectAdapter);
		
		
		Button backButton = (Button) findViewById(R.id.backButton);
		backButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
		    	finish(); //finishes the activity and closes it
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{	
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.create_project, menu);
		
        createProject = (MenuItem) menu.findItem(R.id.createProjectItem);
        createProject.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Intent intent = new Intent(getApplicationContext(), CreateProjectActivity.class);
				startActivity(intent);
				// TODO Auto-generated method stub
				return true;
			}
		});
        return true;
	}
	
	/**
	 * Shows an alertDialog for textinput to change the name of a project, 
	 * sets the new name for the project object and updates the database.
	 * 
	 * @param p - received project object.
	 */
	public void showInputDialog(final Project p)
	{
		final EditText newNameInput = new EditText(this);
		newNameInput.setHint(p.getName());
		
		new AlertDialog.Builder(this)
		.setTitle("Edit project name")
		.setMessage("Give the project a new name.")
		.setView(newNameInput)
		.setPositiveButton("Set name", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String newName = newNameInput.getText().toString();
				db.updateProjectName(newName, p);
				p.setName(newName);
				projectAdapter.notifyDataSetChanged();
			}
		})
		.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		})
		.show();
		
	}
	
	public void showHideProjectDialog(final Project p)
	{
		final TextView projectText = new TextView(this);
		projectText.setText(p.getName());
		
		int hidden = p.getIsHidden(); // 0 for visable, 1 for hidden
		
		String message;
		String buttonTitle;
		
		if(hidden==0){
			message = "Hiding a project will not delete it, but will hide it from the Report screen. Are you sure you want to hide this project?";
			buttonTitle = "Hide project";
		}
		else{
			message = "Do you wish to make the project reportable again?";
			buttonTitle = "Show project";
		}
		new AlertDialog.Builder(this)
		.setTitle(buttonTitle)
		.setMessage(message)
	//	.setView(newNameInput)
		.setPositiveButton(buttonTitle, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// HIDE PROJECT IN DATABASE AND CLASS STRUCTURE
				if(p.getIsHidden() == 0){
					//Hide project
					p.setIsHidden(1);
					db.updateProjectIsHidden(1, p);
				}
				else{
					//Unhide project
					p.setIsHidden(0);
					db.updateProjectIsHidden(0, p);
				}
				User.getInstance().updateProjectlistFromDB();
				hiddenPosition = -1; // reset category check
				projectAdapter.notifyDataSetChanged();
			}
		})
		.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		})
		.show();
		
	}
	
	
	
	
	
	/**
     * The ManageProjectListAdapter class takes the projectList Array and converts the items into 
     * View objects to be loaded into the ListView container.
     */
	private class ManageProjectListAdapter extends ArrayAdapter<Project>
    {
   		/**
   		 *Constructor, calls the superclass constructor which will call getView (below)
   		 *for each element in projectList
   		 */
  
    	public ManageProjectListAdapter()
    	{
    		super(getApplicationContext(), R.layout.manage_projects_adapter, projectList);
    	}
    	
    	@Override
    	public View getView(int position, View view, ViewGroup parent)
    	{
    		

    		if(view == null)
    			view = getLayoutInflater().inflate(R.layout.manage_projects_adapter, parent, false);
    		
    		
    		TextView category = (TextView) view.findViewById(R.id.category);
    		final Project currentProject = projectList.get(position);
    		if(position==0 && currentProject.getIsHidden() == 0){
    			category.setText("Active projects");
    			category.setVisibility(TextView.VISIBLE);
    		}
    		else{
    			category.setVisibility(TextView.GONE);
    		}
    		
    		
    		
    		if(currentProject.getIsHidden()==1 && hiddenPosition == -1){
    			hiddenPosition = position;
    			category.setText("Hidden projects");
    			category.setVisibility(TextView.VISIBLE);
    		}
    		
    		TextView projectName = (TextView) view.findViewById(R.id.project_name);
    		
    		projectName.setText(currentProject.getName());
    		
    		//Edit project button
    		ImageButton editButton = (ImageButton) view.findViewById(R.id.edit_button);
    		editButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					showInputDialog(currentProject);					
				}
			});
    		
    		//Hide/Archive the project
    		ImageButton hideButton = (ImageButton) view.findViewById(R.id.archive_button);
    		hideButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// Show popup and ask user if it should hide the project from homescreen
					showHideProjectDialog(currentProject);
					
				}
			});
    		
    		
    		return view;
    	}
    }

}
