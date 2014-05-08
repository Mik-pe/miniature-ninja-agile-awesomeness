package com.example.tson;

import com.example.tson.HomeActivity;

import tson.sqlite.helper.DatabaseHelper;
import tson_utilities.Project;
import tson_utilities.User;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Build;

public class CreateProjectActivity extends Activity {
	
	public HomeActivity homeActivity = new HomeActivity();
	DatabaseHelper db = homeActivity.db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_project);
		

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}
	
	//Hej! HEJHEEEJ!!! :DD:D:D:D:DDD
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_project, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void createProject(View view)
	{
		EditText projectNameEditText = (EditText) findViewById(R.id.project_name_editText);
		String projectName = projectNameEditText.getText().toString();

    
        Project p1 = new Project(projectName);
        
		homeActivity.user.addProject(p1);
		db.createProject(p1);

		Toast.makeText(getApplicationContext(), projectName + " has been added to your list!", Toast.LENGTH_SHORT).show();
		
		Intent intent = new Intent(this, HomeActivity.class);
    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //set flag on intent to clear history stack of all activities
    	
    	startActivity(intent);
    	finish(); //finish this activity, remove from history	
       	
	}
	/**
	 * Function that takes you back to home page from create project page, if user change his/her mind
	 * @param view
	 */
	public void cancelProject(View view)
	{

		Intent intent = new Intent(this, HomeActivity.class);
    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //set flag on intent to clear history stack of all activities
    	
    	startActivity(intent);
    	finish(); //finish this activity, remove from history	
       	
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_create_project,
					container, false);
			return rootView;
		}
	}

}
