package tyczj.extendedcalendarview;


/***********************
 *  	Class for representing days		*/	
/***********************/

import java.util.Calendar;

import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.text.format.Time;
import android.widget.BaseAdapter;

public class Day{
	
	/***********************
	  *  	VARIABLES		*/	
	 /***********************/
	
	int startDay;
	int monthEndDay;
	int day;
	int year;
	int month;
	Context context;
	BaseAdapter adapter;
	
	/***********************
	  *  	Constructor		*/	
	 /***********************/
	
	public Day(Context context,int day, int year, int month){
		this.day = day;
		this.year = year;
		this.month = month;
		this.context = context;
		Calendar cal = Calendar.getInstance();
		cal.set(year, month-1, day);
		int end = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		cal.set(year, month, end);
		TimeZone tz = TimeZone.getDefault();
		monthEndDay = Time.getJulianDay(cal.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cal.getTimeInMillis())));
	}
	
	
	/***********************
	  *  	Getters	and setters	*/	
	 /***********************/
	
	public int getMonth(){
		return month;
	}
	
	public int getYear(){
		return year;
	}
	
	public void setDay(int day){
		this.day = day;
	}
	
	public int getDay(){
		return day;
	}
	
	
	public int getStartDay(){
		return startDay;
	}

	
	public void setAdapter(BaseAdapter adapter){
		this.adapter = adapter;
	}

	

}
