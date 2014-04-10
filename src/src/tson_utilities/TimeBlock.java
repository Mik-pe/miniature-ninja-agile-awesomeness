package tson_utilities;

import java.util.Calendar;

import android.util.Log;

public class TimeBlock 
{
	private Calendar date;
	private int hours;
	private int minutes;
	
	public TimeBlock()
	{
		setTimeBlock(date.YEAR,date.MONTH,date.DAY_OF_MONTH,0,0);
	}
	
	public TimeBlock(int year, int month, int day, int theHours, int theMinutes)
	{
		setTimeBlock(year, month, day, theHours, theMinutes);
	}

	/**
	 * 
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
	}
	/**
	 * 
	 * @param d - Java Calendar type
	 */
	public void setDate(Calendar d)
	{
		date = d;
	}
	/**
	 * 
	 * @param h - hours
	 */
	public void setHours(int h)
	{
		hours = h;
	}
	/**
	 * 
	 * @param m - minutes
	 */
	public void setMinutes(int m)
	{
		minutes = m;
	}
	/**
	 * 
	 * @param h - hours
	 * @param m - minutes
	 */
	public void setDuration(int h, int m)
	{
		setHours(h);
		setMinutes(m);
	}
	/**
	 * 
	 * @return time of TimeBlock in minutes
	 */
	public int getTimeInMinutes()
	{
		return (hours*60 + minutes);
	}
	
	public int[] getTimeAsArray()
	{
		int[] t = {this.hours, this.minutes}; 
		return t;
	}
	
	
	/**
	 * 
	 * @return time of TimeBlock as String
	 */
	public String getTimeAsString()
	{
		return hours+" h : "+minutes+" m";
	}
	/**
	 * 
	 * @return the date as Calendar type
	 */
	public Calendar getDate()
	{
		return date;
	}
	
	public boolean isDate(int year, int month, int day)
	{
		Log.d("Inserted Day", ""+year);
		Log.d("TimeBlock Day", ""+date.get(Calendar.YEAR));
		if(this.date.get(Calendar.YEAR)==year && this.date.get(Calendar.MONTH) == month && this.date.get(Calendar.DAY_OF_MONTH)==day)
			return true;
		
		return false;
	}
	
	public String getDateAsString()
	{
		return date.toString();
		//return date.get(Calendar.DAY_OF_MONTH)+"/"+date.get(Calendar.MONTH)+ "-" + date.get(Calendar.YEAR); 
	}
	
}
