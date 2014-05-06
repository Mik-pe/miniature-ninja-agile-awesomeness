/**
 * @author Pär Eriksson
 * A database helper class that takes care of all interaction with the SQLite database
 */
package tson.sqlite.helper;

import java.util.ArrayList;
import java.util.List;


import tson_utilities.Project;
import tson_utilities.TimeBlock;

import java.util.Calendar;
import tson_utilities.Project;


import tson_utilities.TimeBlock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper{

	//Logcat tag
	private static final String LOG = "DatabaseHelper";
	
	//Database Version
	private static final int DATABASE_VERSION = 24;
		
	//Database Name
	private static final String DATABASE_NAME = "timeManager.db";
	
	//Table Names
	private static final String TABLE_PROJECT = "projects";
	private static final String TABLE_TIME_BLOCK = "time_blocks";
	
	// Common column names
	private static final String KEY_ID = "id";	
	
	//PROJECTS Table
	private static final String KEY_PROJECT_NAME = "project_name";

	//TIME BLOCKS TABLE
	private static final String KEY_TIME_BLOCK_PROJECT_ID = "project_id";
	private static final String KEY_TIME_BLOCK_YEAR = "year";
	private static final String KEY_TIME_BLOCK_MONTH = "month";
	private static final String KEY_TIME_BLOCK_DAY = "day";
	private static final String KEY_TIME_BLOCK_MINUTES = "minutes";
	private static final String KEY_TIME_BLOCK_CONFIRMED = "confirmed";
	
	// Table Create Statements
	// Project table create statement
	private static final String CREATE_TABLE_PROJECT = "CREATE TABLE "
			+ TABLE_PROJECT + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_PROJECT_NAME
			+ " TEXT" + ")";

	private static final String CREATE_TABLE_TIME_BLOCK = "CREATE TABLE "
			+ TABLE_TIME_BLOCK + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TIME_BLOCK_PROJECT_ID + " INTEGER," + KEY_TIME_BLOCK_YEAR + " INTEGER,"
			+ KEY_TIME_BLOCK_MONTH + " INTEGER," + KEY_TIME_BLOCK_DAY + " INTEGER," + KEY_TIME_BLOCK_MINUTES + " INTEGER,"  + KEY_TIME_BLOCK_CONFIRMED + " INTEGER"  +")";
	
	public DatabaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_PROJECT);

		db.execSQL(CREATE_TABLE_TIME_BLOCK);
		//IF MORE TABLES: OTHER TABLES WILL ALSO BE EXECUTED		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		Log.d("UPGRADE DATABASE", "!!!!");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROJECT);
		
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIME_BLOCK);
		// IF MORE TABLES: OTHER TABLES WILL ALSO BE EXECUTED		
	
		//Create new tables
		onCreate(db);
	}
	
	
	
	
	
	
	
	
	
	//========================================================
	// PROJECT ===============================================
	//========================================================

	/**
	 * Creating a project.
	 * @param project - the project object.
	 * @param tag_ids - the tags, e.g. name.
	 * @return - returns the id of the row.
	 */
	public long createProject(Project project)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_PROJECT_NAME, project.getName());
		
		//insert row
		long project_id = db.insert(TABLE_PROJECT, null, values);
		
		//assigning tags to project	
		return project_id;		
	}
	
	/**
	 * Get a single project by an ID.
	 * @param project_id.
	 * @return A new project with the key name.
	 */
	public Project getProject(long project_id)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		
		String selectQuery = "SELECT * FROM " + TABLE_PROJECT + " WHERE "
				+ KEY_ID + " = " + project_id;
		//Log.e(LOG, selectQuery);
		Cursor c = db.rawQuery(selectQuery, null);
				
		if(c != null)
			c.moveToFirst();
		
		//Set all attributes before returning it
		// Create a project out of the row name
		Project p = new Project(c.getString(c.getColumnIndex(KEY_PROJECT_NAME)));
		
		return p;
	}
	
	/**
	 * Select all projects.
	 * @return List<Project> 
	 */
	public List<Project> getAllProjects()
	{
		List<Project> projects = new ArrayList<Project>();
		String selectQuery = "SELECT * FROM " + TABLE_PROJECT;
		
		//Log.e(LOG, selectQuery);
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);
		
		//Loop through all rows in the table and add to the list
		if(c.moveToFirst())
		{
			do
			{
				Project p = getProject(c.getInt(c.getColumnIndex(KEY_ID)));
				
				// Add to the projects list that will be returned
				projects.add(p);
			}while (c.moveToNext());
		}
		return projects;
	}
	/**
	 * Get the id of a project by it's name (which should be unique).
	 * @param project_name - String.
	 * @return auto incremented id from the database.
	 */
	public long getProjectId(String project_name){
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT "+ "*" + " FROM " + TABLE_PROJECT + " WHERE " + KEY_PROJECT_NAME + " = " + "'" + project_name + "'";
		Cursor c = db.rawQuery(selectQuery, null);
		//Log.e(LOG, c.toString());
		if(c != null)
			c.moveToFirst();
		return c.getLong(0);
	}
	
	/**
	 * Delete a project by an id, usually called after getProjectId.
	 * @param project_id
	 */
	public void deleteProject(long project_id)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_PROJECT, KEY_ID + " = ?",
				new String[] {String.valueOf(project_id) });
	}
	
	
	
	
	
	
	
	
	//========================================================
	// TIME BLOCK ============================================
	//========================================================
	/**	
	 * Create a timeblock instance in the database.
	 * @param timeblock - TimeBlock object.
	 * @param project	- Project object.
	 * @return			- (long) id of the inserted row.
	 */
	public long createTimeBlock(TimeBlock timeblock, Project project)
	{
		//Log.d("createTB", "ok");
		SQLiteDatabase db = this.getWritableDatabase();

		Calendar d = timeblock.getDate();
		
		ContentValues values = new ContentValues();
		values.put(KEY_TIME_BLOCK_PROJECT_ID, getProjectId(project.getName()));
		values.put(KEY_TIME_BLOCK_MINUTES, timeblock.getTimeInMinutes());
		values.put(KEY_TIME_BLOCK_YEAR, d.get(Calendar.YEAR));
		values.put(KEY_TIME_BLOCK_MONTH, d.get(Calendar.MONTH));
		values.put(KEY_TIME_BLOCK_DAY, d.get(Calendar.DAY_OF_MONTH));
		values.put(KEY_TIME_BLOCK_CONFIRMED, timeblock.getConfirmed());
				
		//insert row
		long timeblock_id = db.insert(TABLE_TIME_BLOCK, null, values);
		timeblock.setID(timeblock_id);
		logTimeblocks();
		return timeblock_id;
		
	}
	/**
	 * Set a confirmation tag for a TimeBlock object, this indicates that the user has sumbitted a timeblock for a specific date.
	 * @param TimeBlock object.
	 * @return an int indicator if the row was updated.
	 */
	public int setConfirmed(TimeBlock timeblock)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		  
		    ContentValues values = new ContentValues();
		    values.put(KEY_TIME_BLOCK_CONFIRMED, timeblock.getConfirmed());
		    //String strFilter = "year=" + timeblock.getYear() 
		   
		    //updating row
		    //Log.d("Set confirmed", "" + timeblock.getID());
		    int u = db.update(TABLE_TIME_BLOCK, values, KEY_ID + " = ?",
		            new String[] { String.valueOf(timeblock.getID()) });
		    Log.d("setConfirmedDB", "nr of rows updated: " + u + " with id: " + timeblock.getID());
		    return u;
	}
	/**
	 * Update a time block, only updates the time column for now.
	 * @param timeblock.
	 * @return indicator if the row was updated.
	 */
	public int updateTimeBlock(TimeBlock timeblock, Project p)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_TIME_BLOCK_MINUTES, timeblock.getTimeInMinutes());
		
		// This will be send as a parameter to db.update
		String[] args = new String[]{String.valueOf(getProjectId(p.getName())), String.valueOf(timeblock.getYear()), String.valueOf(timeblock.getMonth()), String.valueOf(timeblock.getDay())};
		//Update row
		return db.update(TABLE_TIME_BLOCK, values, KEY_TIME_BLOCK_PROJECT_ID + " = ?" + " AND " +KEY_TIME_BLOCK_YEAR + " = ?" + " AND " + KEY_TIME_BLOCK_MONTH + " = ?" + " AND " + KEY_TIME_BLOCK_DAY + " = ?",
				args);
	}
	
	/**
	 * Get all the time blocks of all projects.
	 * @return List<TimeBlock> of all time blocks.
	 */
	public List<TimeBlock> getAllTimeBlocks()
	{
		List<TimeBlock> timeblocks = new ArrayList<TimeBlock>();
		String selectQuery = "SELECT * FROM " + TABLE_TIME_BLOCK;
		
		//Log.e(LOG, selectQuery);
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);
		
		//Loop through all rows in the table and add to the list
		if(c != null)
			if(c.moveToFirst())
			{
				do
				{
					int year = c.getInt(c.getColumnIndex(KEY_TIME_BLOCK_YEAR));
					int month = c.getInt(c.getColumnIndex(KEY_TIME_BLOCK_MONTH));
					int day = c.getInt(c.getColumnIndex(KEY_TIME_BLOCK_DAY));
					int minutes = c.getInt(c.getColumnIndex(KEY_TIME_BLOCK_MINUTES));
					int confirmed = c.getInt(c.getColumnIndex(KEY_TIME_BLOCK_CONFIRMED));
					int hours = (int)minutes/60;
					minutes = minutes-hours*60;
					TimeBlock t = new TimeBlock(year, month, day, hours, minutes);
					t.setConfirmed(confirmed);
					timeblocks.add(t);
				}while (c.moveToNext());
			}
			return timeblocks;
	}
	
	/**
	 * Get all time blocks connected to a specific project.
	 * @param Project object.
	 * @return List<TimeBlock> of all time blocks for a specific project.
	 */
	public List<TimeBlock> getTimeBlocksByProject(Project p)
	{
		long pid = getProjectId(p.getName());
		String selectQuery = "SELECT * FROM " + TABLE_TIME_BLOCK + " WHERE "+ KEY_TIME_BLOCK_PROJECT_ID + " = " + pid;
		//Log.e(LOG, selectQuery);
		List<TimeBlock> timeblocks = new ArrayList<TimeBlock>();
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);
		if(c != null)
			if(c.moveToFirst())
			{
				do
				{
					int year = c.getInt(c.getColumnIndex(KEY_TIME_BLOCK_YEAR));
					int month = c.getInt(c.getColumnIndex(KEY_TIME_BLOCK_MONTH));
					int day = c.getInt(c.getColumnIndex(KEY_TIME_BLOCK_DAY));
					int minutes = c.getInt(c.getColumnIndex(KEY_TIME_BLOCK_MINUTES));
					int hours = minutes/60;
					int confirmed = c.getInt(c.getColumnIndex(KEY_TIME_BLOCK_CONFIRMED));
					
					minutes = minutes-hours*60;
					TimeBlock t = new TimeBlock(year, month, day, hours, minutes);
					t.setConfirmed(confirmed);
					timeblocks.add(t);
				}while (c.moveToNext());
			}
			return timeblocks;
	}
	
	/**
	 * A log time function, not fully implemented.
	 */
	public void logTimeblocks()
	{	
		getAllTimeBlocks();
	}
	
	
	//Close database
	/**
	 * Close the database.
	 */
	public void closeDB()
	{
		SQLiteDatabase db = this.getReadableDatabase();
		if(db!= null && db.isOpen())
			db.close();
	}

}
