package com.example.tson;

//IMPORT OUR OWN CLASSES
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import model.NavDrawerItem;

import tson_utilities.Project;
import tson_utilities.TimeBlock;
import tson_utilities.User;
import adapter.NavDrawerListAdapter;
import android.R.array;
import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
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


import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class HomeActivity extends Activity 
{
	int hour,min, newHour, newMin;
	int holder = 0;
	static final int TIME_DIALOG_ID=0;
	String[] hourmin;
	View currentPage;
	ListView projectListView;
	
	public static User user = new User("sdf@sdf.com", "Bosse", "b1337");
	List<Project> projectList = user.getProjects();
	
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	//private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		mTitle = mDrawerTitle = getTitle();

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		/*navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);*/

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();
		Log.d("navMenu", Integer.toString(navDrawerItems.size()));
		// adding nav drawer items to array
		// Home
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0]));
		// Find People
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1]));
		// Photos
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2]));
		// Communities, Will add a counter here
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], true, "22"));
		// Pages
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4]));
		Log.d("navMenu", Integer.toString(navDrawerItems.size()));
		
		
		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());		
		
		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);		

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, //nav menu toggle icon
				R.string.app_name, // nav drawer open - description for accessibility
				R.string.app_name // nav drawer close - description for accessibility
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(0);
		}
		
	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
	
			ListView.OnItemClickListener {
		
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = new HomeFragment();
			break;
		/*case 1:
			fragment = new FindPeopleFragment();
			break;
		case 2:
			fragment = new PhotosFragment();
			break;
		case 3:
			fragment = new CommunityFragment();
			break;
		case 4:
			fragment = new PagesFragment();
			break;
		case 5:
			fragment = new WhatsHotFragment();
			break;*/

		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	
	
	/*int hour,min, newHour, newMin;
	int holder = 0;
	static final int TIME_DIALOG_ID=0;
	String[] hourmin;
	View currentPage;
	ListView projectListView;

	
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
        
        
        List<String> projectStrings = new ArrayList<String>();
        
        for(int i = 0; i < projectList.size(); i++)
        {
        	projectStrings.add(projectList.get(i).getName());
        }
        
        //ArrayAdapter<String> arrayAdapter = new Project<String>(this, android.R.layout.simple_list_item_1, projectStrings);
        
        ArrayAdapter<Project> projectAdapter = new ProjectListAdapter();
        
        projectListView.setAdapter(projectAdapter);
        
    }

    //Creating the dialog for the specific time
   	public void showTimeDialog(View v)
    {
   		//calculates what page and position we are at
   		holder = projectListView.getPositionForView(v);
   		currentPage = (View) v.getParent();
       	
       	//Calculate what hour and minute that we are at when we click
   		if((user.getProjects().get(holder).getTimeByDate(Calendar.getInstance())) != null){
	       	hourmin = (user.getProjects().get(holder).getTimeByDate(Calendar.getInstance())).getTimeAsString().split(" h : ");
	       	hourmin[1] = hourmin[1].replaceAll("m", "");
	       	hourmin[1] = hourmin[1].replaceAll(" ", "");
   		}
   		else
   		{

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
    private TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() 
    {
   		@Override
   		public void onTimeSet(TimePicker view, int hourOfDay, int minute) 
   		{
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
	/*
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
    		if((currentProject.getTimeByDate(Calendar.getInstance())) != null)
    			projectTime.setText((currentProject.getTimeByDate(Calendar.getInstance())).getTimeAsString());
    		
    		
    		Button editButton = (Button) view.findViewById(R.id.editTimeButton);		
    		
    		return view;
    	}
    }*/
   
    
}
