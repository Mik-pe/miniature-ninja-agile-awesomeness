package tson_utilities;

import java.util.Calendar;
import java.util.List;

/**
 * 
 * @author mikpe201
 *Project class is used as an object for each project created
 *by the user. User can add time, get time and get the name
 *of the project.
 */
public class Project {
	
private String name;
private List<TimeBlock> submissionList;

/**
 * 
 * @param d - date
 * @param h - hours
 * @param m - minutes
 */
	public void addTime(Calendar d, int h, int m)
	{
		TimeBlock t = new TimeBlock(d, h, m);
		submissionList.add(t);
	}
	
	/**
	 * 
	 * @param d - date
	 * @return - returns string of amount of hours of that 
	 * date and project
	 */
	public String getTimeByDate(Calendar d)
	{
		
		for(int i=this.submissionList.size(); i>0 ;i--)
		{
			if(this.submissionList.get(i).getDate() == d){
				return this.submissionList.get(i).getTimeAsString();
			}
		}
		
		return "-";
	}
	/**
	 * 
	 * @return - returns the name of the project.
	 */
	public String getName(){
		return name;
	}
	
	/*TODO
	public void editTime(int h, int m, Date d)
	{
	 Adds time to a certain date...
	}
	*/

}

