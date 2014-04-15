package tson_utilities;

import java.util.Calendar;

import android.util.Log;

public class TimeBlock 
{
	
	 /***********************
	  *  	VARIABLES		*/	
	 /***********************/
	
	private Calendar date;
	private int hours;
	private int minutes;
	private int confirmed;
	private long ID;
	
	 /***********************
	  *  	CONSTRUCTORS 	*/	
	 /***********************/
	
	public TimeBlock()
	{
		setTimeBlock(date.YEAR,date.MONTH,date.DAY_OF_MONTH,0,0);
	}
	
	public TimeBlock(int year, int month, int day, int theHours, int theMinutes)
	{
		setTimeBlock(year, month, day, theHours, theMinutes);
	}
	
	 /***********************
	  *  	SETTERS  		*/	
	 /***********************/
	
	/**
	 * @param theDate - Java Calendar type
	 * @param theHours - hours
	 * @param theMinutes - minutes
	 */
	public void setTimeBlock(int year, int month, int day, int theHours, int theMinutes)
	{
		date = Calendar.getInstance();
		date.set(Calendar.YEAR, year);
		date.set(Calendar.MONTH, month);
		date.set(Calendar.DAY_OF_MONTH, day);
		hours = theHours;
		minutes = theMinutes;
		confirmed = 0;
		//ID = 0;
	}
	/**
	 * Controls color in submission
	 * @param i = 0 for time added (yellow), 1 for time confirmed (green)
	 */
	public void setConfirmed(int i){
		confirmed = i;
		Log.d("Confirmed", "ok");
	}
	


	/**
	 * Sets unique ID for timeblock in DB
	 * @param i = index automatically returned from DB when timeblock inserted
	 */
	public void setID(long i)
	{
		ID = i;
		//Log.d("DB ID set", "ok");
	}

	/**
	 * @param d - Java Calendar type
	 */
	public void setDate(Calendar d)
	{
		date = d;
	}
	/**
	 * @param h - hours
	 */
	public void setHours(int h)
	{
		hours = h;
	}
	/**
	 * @param m - minutes
	 */
	public void setMinutes(int m)
	{
		minutes = m;
	}
	/**
	 * @param h - hours
	 * @param m - minutes
	 */
	public void setDuration(int h, int m)
	{
		setHours(h);
		setMinutes(m);
	}
	
	 /***********************
	  *  	GETTTERS  		*/	
	 /************************/
	
	/**
	 * @return unique ID for editing timeblock in DB
	 */
	public long getID()
	{
		return ID;
	}
	
	/**
	 * Return status of timeblock confirmed = 1 / not confirmed = 0
	 * @return
	 */	
	public int getConfirmed()
	{
		return confirmed;
	}
	
	/**
	 * @return time of TimeBlock in minutes
	 */
	public int getTimeInMinutes()
	{
		return (hours*60 + minutes);
	}
	
	/**
	 * @return time as array with time on form [hours, minutes]
	 */
	public int[] getTimeAsArray()
	{
		int[] t = {this.hours, this.minutes}; 
		return t;
	}
	
	/**
	 * @return time of TimeBlock as String
	 */
	public String getTimeAsString()
	{
		if(hours == 0 && minutes == 0)
			return " -- h : -- m";
		else
		return hours+" h : "+minutes+" m";
	}
	
	/**
	 * @return the date as Calendar type
	 */
	public Calendar getDate()
	{
		return date;
	}
	/**
	 * Check input of date, return true if correct
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	
	 /***********************
	  *  	OTEHRS  		*/	
	 /************************/
	
	public boolean isDate(int year, int month, int day)
	{
		//Log.d("Inserted Day", ""+year);
		//Log.d("TimeBlock Day", ""+date.get(Calendar.YEAR));
		if(this.date.get(Calendar.YEAR)==year && this.date.get(Calendar.MONTH) == month && this.date.get(Calendar.DAY_OF_MONTH)==day)
			return true;
		
		return false;
	}
	
	/**
	 * @return date as string
	 */
	public String getDateAsString()
	{
		return date.toString();
	}	
	
} //End of TimeBlock Class
