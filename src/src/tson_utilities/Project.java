package tson_utilities;

import java.util.Calendar;
import java.util.List;

/**
 * Project class is used as an object for each project created
 * by the user. User can add time, get time and get the name
 * of the project.
 * @author mikpe201
 *
 */
public class Project {
	
	private String name;
	private List<TimeBlock> submissionList;
	
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
	public void addTime(Calendar d, int h, int m)
	{
		
		TimeBlock t = new TimeBlock(d, h, m);
		if(!submissionList.contains(t))
		{
			submissionList.add(t);
		}
	}
	
	/**
	 * Getter of string
	 * @param d  date the time should correspond to
	 * @return  the string of amount of hours of that 
	 * date and project
	 */
	public String getTimeByDate(Calendar d)
	{
		
		for(int i=this.submissionList.size(); i>0 ;i--)
		{
			if(this.submissionList.get(i).getDate() == d)
			{
				return this.submissionList.get(i).getTimeAsString();
			}
		}
		
		return "-";
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
	public void editTime(int h, int m, Date d)
	{
	 Adds time to a certain date...
	}
	*/

}

