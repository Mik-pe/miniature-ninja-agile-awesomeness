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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

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
	
	List<Project> projectList = User.getInstance().getProjects();
	ListView manageProjectsList;
	ManageProjectListAdapter projectAdapter;
	DatabaseHelper db = HomeActivity.db;
	
	
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
		
		manageProjectsList = (ListView) findViewById(R.id.manage_project_list);
		projectAdapter = new ManageProjectListAdapter();
        
		manageProjectsList.setAdapter(projectAdapter);
		
		Button backButton = (Button) findViewById(R.id.backButton);
		backButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				
		    	finish();
			}
		});
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
		.setTitle("Set new name!")
		.setMessage("Give the project a new name!")
		.setView(newNameInput)
		.setPositiveButton("Set name!", new DialogInterface.OnClickListener() {
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
    		
    		final Project currentProject = projectList.get(position);
    		
    		TextView projectName = (TextView) view.findViewById(R.id.project_name);
    		projectName.setText(currentProject.getName());
    		
    		Button editButton = (Button) view.findViewById(R.id.edit_button);
    		editButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					showInputDialog(currentProject);					
				}
			});
    		
    		return view;
    	}
    }

}
