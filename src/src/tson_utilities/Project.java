package tson_utilities;

import java.util.Date;
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
 * @param h - hours
 * @param m - minutes
 * @param d - date
 */
	public void addTime(int h, int m, Date d)
	{
		TimeBlock t = new TimeBlock();
		t.setTimeBlock(h, m, d);
		submissionList.add(t);
	}
	
	/**
	 * 
	 * @param d - date
	 * @return - returns string of amount of hours of that 
	 * date and project
	 */
	public String getTimeByDate(Date d)
	{
		
		for(int i=this.submissionList.size(); i>0 ;i--)
		{
			if(this.submissionList.get(i).getDate() == d){
				return this.submissionList.get(i).getTime();
			}
		}
		
		return "0h";
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

