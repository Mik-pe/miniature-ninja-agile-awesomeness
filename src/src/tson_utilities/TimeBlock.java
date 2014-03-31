package tson_utilities;

import java.util.Calendar;

public class TimeBlock 
{
	private Calendar date;
	private int hours;
	private int minutes;
	
	public TimeBlock(Calendar theDate, int theHours, int theMinutes)
	{
		setTimeBlock(theDate, theHours, theMinutes);
	}
	/**
	 * 
	 * @param theDate - Java Calendar type
	 * @param theHours - hours
	 * @param theMinutes - minutes
	 */
	public void setTimeBlock(Calendar theDate, int theHours, int theMinutes)
	{
		date = theDate;
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
		return hours*60 + minutes;
	}
	/**
	 * 
	 * @return time of TimeBlock as String
	 */
	public String getTimeAsString()
	{
		return hours+"h"+minutes+"m";
	}
	/**
	 * 
	 * @return the date as Calendar type
	 */
	public Calendar getDate()
	{
		return date;
	}
	
}
