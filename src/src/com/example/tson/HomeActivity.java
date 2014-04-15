package com.example.tson;

//IMPORT OUR OWN CLASSES
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import model.NavDrawerItem;

import tson_utilities.Project;
import tson_utilities.User;
import adapter.NavDrawerListAdapter;
import android.R.array;

import java.util.Locale;

import tson.sqlite.helper.DatabaseHelper;
import tson_utilities.*;

//IMPORT ANDROID
import tson_utilities.Project;
import tson_utilities.TimeBlock;
import tson_utilities.User;
import adapter.NavDrawerListAdapter;
import android.R.array;
import android.app.ActionBar;
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

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
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

public class HomeActivity extends FragmentActivity
{
	 /***********************
	  *  	VARIABLES		*/	
	 /***********************/
	
	int hour,min, newHour, newMin;
	int holder = 0;
	static final int TIME_DIALOG_ID=0;
	String[] hourmin;
	View currentPage;
	ListView projectListView;
	ActionBar ab;
	private static Calendar c;
	
	//DATABASE
	public static DatabaseHelper db;
	List<Project> projectList;
	public static User user = new User("sdf@sdf.com", "Bosse", "b1337");
	
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	//nav drawer title
	private CharSequence mDrawerTitle;

	//used to store app title
	private CharSequence mTitle;

	//slide menu items
	private String[] navMenuTitles;
	//private TypedArray navMenuIcons;
	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;

	 /***********************
	  *  	OTHERS			*/	
	 /************************/
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{

		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		c = Calendar.getInstance();
        db = new DatabaseHelper(getApplicationContext());
        db.getAllProjects();
        db.getAllTimeBlocks();
        db.logTimeblocks();
        projectList = db.getAllProjects();
        user.getProjects().clear();
        for (int i = 0; i < projectList.size(); i++)
        {
	        user.addProject(projectList.get(i));
	        user.getProjects().get(i).setSubmissionList(db.getTimeBlocksByProject(user.getProjects().get(i)));	       	
	        List<TimeBlock> temp = db.getTimeBlocksByProject(user.getProjects().get(i));
	        //Log.d("Listing all tprojects", projectList.get(i).getName() + "");
	        for (TimeBlock time : temp) {
	            //Log.d("Listing all times for a project", time.getTimeAsString());
        }
       
    }//End onCreate-function
	
    mTitle = mDrawerTitle = getTitle();

	//load slide menu items
	navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

	mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
	mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

	navDrawerItems = new ArrayList<NavDrawerItem>();
	Log.d("navMenu", Integer.toString(navDrawerItems.size()));
	//adding nav drawer items to array
	//Home
	navDrawerItems.add(new NavDrawerItem(navMenuTitles[0]));
	//Find People
	navDrawerItems.add(new NavDrawerItem(navMenuTitles[1]));
	//Photos
	navDrawerItems.add(new NavDrawerItem(navMenuTitles[2]));
	//Communities, Will add a counter here
	navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], true, "22"));
	//Pages
	navDrawerItems.add(new NavDrawerItem(navMenuTitles[4]));
	Log.d("navMenu", Integer.toString(navDrawerItems.size()));


	mDrawerList.setOnItemClickListener(new SlideMenuClickListener());	

	//setting the nav drawer list adapter
	adapter = new NavDrawerListAdapter(getApplicationContext(),
			navDrawerItems);
	mDrawerList.setAdapter(adapter);

	//enabling action bar app icon and behaving it as toggle button
	getActionBar().setDisplayHomeAsUpEnabled(true);
	getActionBar().setHomeButtonEnabled(true);	

	mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
			R.drawable.ic_drawer, //nav menu toggle icon
			R.string.app_name, // nav drawer open - description for accessibility
			R.string.app_name // nav drawer close - description for accessibility
			) {
		public void onDrawerClosed(View view) {
			getActionBar().setTitle(mTitle);
			//calling onPrepareOptionsMenu() to show action bar icons
			invalidateOptionsMenu();
		}

		public void onDrawerOpened(View drawerView) {
			getActionBar().setTitle(mDrawerTitle);
			//calling onPrepareOptionsMenu() to hide action bar icons
			invalidateOptionsMenu();
		}
	};
	mDrawerLayout.setDrawerListener(mDrawerToggle);

	if (savedInstanceState == null) {
		//on first time display view for first nav item
		displayView(0);
	}
	
	
}
	
	/**
	 * Getter of Calendar from the Homeactivity
	 * @return the date of HomeActivity
	 */
	public static Calendar getCal(){
		return c;	
	}
	
	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements

	ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			//display view for selected nav drawer item
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
		//toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		//Handle action bar actions click
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
		//if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}
	

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
		private void displayView(int position) {
		//update the main content by replacing fragments
		Fragment fragment = null;
		ab = getActionBar();
		ab.removeAllTabs();
		ab.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		switch (position) {
		case 0:
			fragment = new HomeFragment();
			break;
		case 1:
			fragment = new SubmissionFragment();
			break;
		case 2:
			fragment = new StatisticsFragment();
			break;
		case 3:
			fragment = new ExportFragment();
			break;
			/*case 4:
			fragment = new PagesFragment();
			break;
			case 5:
			fragment = new WhatsHotFragment();
			break;*/

		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction()
			.replace(R.id.frame_container, fragment).commit();

			//update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			//error in creating fragment
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
		//Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		//Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
}