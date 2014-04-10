package tson_utilities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import tson.sqlite.helper.DatabaseHelper;
import android.util.Log;

import com.example.tson.HomeActivity;

/**
 * Project class is used as an object for each project created
 * by the user. User can add time, get time and get the name
 * of the project.
 * @author Mike
 *
 */
public class Project {
	
	private String name;
	private List<TimeBlock> submissionList = new ArrayList<TimeBlock>();
	
	/**
	 * Constructor for Project Class, submissions are added
	 * at a later point.
	 * @param name Adds name to Project
	 */
	public Project(String name)
	{
		this.name = name;
	}
	
	/**
	 * Adds a TimeBlock to a project
	 * @param d - date
	 * @param h - hours
	 * @param m - minutes
	 */
	public void addTime(int year, int month, int day, int h, int m)
	{
		
		TimeBlock t = new TimeBlock(year, month, day, h, m);
		
		if(!submissionList.contains(t)){
			submissionList.add(t);
			HomeActivity.db.createTimeBlock(t, this);
		}
		//else
			//editTime();
				
	}
	
	public void setSubmissionList(List<TimeBlock> list)
	{
		this.submissionList = list;
	}
	/**
	 * Getter of string
	 * @param d  date the time should correspond to
	 * @return  the string of amount of hours of that 
	 * date and project
	 */
	public TimeBlock getTimeByDate(Calendar cal)
	{
		if(!this.submissionList.isEmpty())			
			for(int i=this.submissionList.size()-1; i>=0 ;i--)
			{
				if(this.submissionList.get(i).isDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))){
					Log.d("TIME BLOCKKKK", "asdasdjpaoöfjkladkjajkldfkladklfjaldf");
					return this.submissionList.get(i);	
				}
			}
		Log.d("NO TIME BLOCK FOUND", "<-----------");
		return new TimeBlock();
	}
	
	/**
	 * Getter
	 * @return  a String of the name of the project.
	 */
	public String getName()
	{
		return name;
	}
	

	
	/*TODO
	public void editTime(Calendar cal,int h, int m)
	{
	 TimeBlock t;
	 if(!this.submissionList.isEmpty())			
			for(int i=this.submissionList.size()-1; i>=0 ;i--)
			{
				
				if(this.submissionList.get(i).isDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)))
					t = this.submissionList.get(i);
				
			}
		t.setMinutes(m);
		t.setHours(h);
	}
	*/

}

