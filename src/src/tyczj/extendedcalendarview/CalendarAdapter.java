package tyczj.extendedcalendarview;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import tson_utilities.Project;
import tson_utilities.TimeBlock;
import android.content.Context;
import android.graphics.Color;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tson.HomeActivity;
import com.example.tson.R;

/***********************
 *  	Calendar adapter for visualizing the calendar		*/	
/***********************/

public class CalendarAdapter extends BaseAdapter{
	
	/***********************
	  *  	VARIABLES		*/	
	 /***********************/	
	
	static final int FIRST_DAY_OF_WEEK = 1;
	Context context;
	Calendar cal;
	public String[] days;	
	ArrayList<Day> dayList = new ArrayList<Day>();
	
	
	/***********************
	  *  	Constructors		*/	
	 /***********************/
	
	public CalendarAdapter(Context context, Calendar cal){
		this.cal = cal;
		this.context = context;
		cal.set(Calendar.DAY_OF_MONTH, 1);
		refreshDays();
	}
	
	/***********************
	  *  	Getters		*/	
	 /***********************/

	@Override
	public int getCount() {
		return days.length;
	}

	@Override
	public Object getItem(int position) {
		return dayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	
	public int getPrevMonth(){
		if(cal.get(Calendar.MONTH) == cal.getActualMinimum(Calendar.MONTH)){
			cal.set(Calendar.YEAR, cal.get(Calendar.YEAR-1));
		}else{
			
		}
		int month = cal.get(Calendar.MONTH);
		if(month == 0){
			return month = 11;
		}
		
		return month-1;
	}
	
	public int getMonth(){
		return cal.get(Calendar.MONTH);
	}
	
	/***********************
	  *  	Coloring of calendar-boxes happens here!		*/	
	 /***********************/
	
	@SuppressWarnings("deprecation")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View v = convertView;
		LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(position >= 0 && position < 7){
			v = vi.inflate(R.layout.day_of_week, null);
			TextView day = (TextView)v.findViewById(R.id.textView1);
			if(position == 0){
				day.setText(R.string.monday);
			}else if(position == 1){
				day.setText(R.string.tuesday);
			}else if(position == 2){
				day.setText(R.string.wednesday);
			}else if(position == 3){
				day.setText(R.string.thursday);
			}else if(position == 4){
				day.setText(R.string.friday);
			}else if(position == 5){
				day.setText(R.string.saturday);
			}else if(position == 6){
				day.setText(R.string.sunday);
			}
			
		}else{
			
	        v = vi.inflate(R.layout.day_view, null);
			FrameLayout today = (FrameLayout)v.findViewById(R.id.today_frame);
			Calendar cal = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
			Day d = dayList.get(position);
			if(d.getYear() == cal.get(Calendar.YEAR) && d.getMonth() == cal.get(Calendar.MONTH) && d.getDay() == cal.get(Calendar.DAY_OF_MONTH)){
				today.setVisibility(View.VISIBLE);
			}else{
				today.setVisibility(View.GONE);
			}
			
			
			TextView dayTV = (TextView)v.findViewById(R.id.textView1);
			//rl is the box for a date.
			RelativeLayout rl = (RelativeLayout)v.findViewById(R.id.rl);
			
			//Get the day of the date that is currently printed
			Day day = dayList.get(position);
			
			//List of projects
			List<Project> projectList = (ArrayList<Project>) HomeActivity.user.getProjects();
			
			//Create a calendarObject of the day which is currently being printed
			Calendar tempCal = Calendar.getInstance();
			tempCal.set(day.getYear(), day.getMonth(), day.getDay());
			//get todays date
			Calendar thisDay = Calendar.getInstance();
			
			
			//----functionality for color coding the calendar----//
			
       		if(tempCal.after(thisDay)) //check if current calendar-block is after todays date
       		{
           		rl.setBackgroundColor(Color.rgb(120, 120, 120)); //Grey
       			
       		}
       		else //the current calendar-block is before todays date
       		{
       			if(projectList.size()>0)
       			{
	       			for(int i = 0; i < projectList.size(); i++)
	           		{
	           			Project p = projectList.get(i);
	           			
	           			TimeBlock t = p.getTimeByDate(tempCal);
	          
	           			if(t != null)
	           			{
	           				if(t.getConfirmed()==1) //coloring for confirmed timeblocks
	           				{
	           					rl.setBackgroundColor(Color.rgb(145, 218, 149));//green
	           					break;
	           				}       					
	           				else //coloring for unconfirmed timeblocks
	           				{
	           					rl.setBackgroundColor(Color.rgb(246, 241, 171)); //yellow       					
	           					break;
	           				}
	           					
	           			}
	           			else //coloring for past blocks with unreported time
	           			{
	           				rl.setBackgroundColor(Color.rgb(229, 229, 229)); //dark gray
	           			}
	           		}
       			}
       			else //coloring for past blocks with unreported time
       				rl.setBackgroundColor(Color.rgb(229, 229, 229)); //dark gray
       		}
       		//-----------end of color coding functionality of the calendar-------------//
       		
				
			if(day.getDay() == 0){
				rl.setVisibility(View.GONE);
			}else{
				dayTV.setVisibility(View.VISIBLE);
				dayTV.setText(String.valueOf(day.getDay()));
				if(tempCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || tempCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
					dayTV.setTextColor(context.getResources().getColor(R.color.combitech_orange));
				else
					dayTV.setTextColor(context.getResources().getColor(R.color.combitech_blue));
			}
		}
		
		return v;
	}
	
	/***********************
	  *  	Some Magic included in the class	*/	
	 /***********************/
	
	public void refreshDays()
    {
    	// clear items
    	dayList.clear();
    	
    	int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH)+7;
        int firstDay = (int)cal.get(Calendar.DAY_OF_WEEK);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        TimeZone tz = TimeZone.getDefault();
        
        // figure size of the array
        if(firstDay==1){
        	days = new String[lastDay+(FIRST_DAY_OF_WEEK*6)];
        }
        else {
        	days = new String[lastDay+firstDay-(FIRST_DAY_OF_WEEK+1)];
        }
        
        int j=FIRST_DAY_OF_WEEK;
        
        // populate empty days before first real day
        if(firstDay>1) {
	        for(j=0;j<(firstDay-FIRST_DAY_OF_WEEK)+7;j++) {
	        	days[j] = "";
	        	Day d = new Day(context,0,0,0);
	        	dayList.add(d);
	        }
        }
	    else {
	    	for(j=0;j<(FIRST_DAY_OF_WEEK*6)+7;j++) {
	        	days[j] = "";
	        	Day d = new Day(context,0,0,0);
	        	dayList.add(d);
	        }
	    	j=FIRST_DAY_OF_WEEK*6+1; // sunday => 1, monday => 7
	    }
        
        // populate days
        int dayNumber = 1;
        
        if(j>0 && dayList.size() > 0 && j != 1){
        	dayList.remove(j-1);
        }
        
        for(int i=j-1;i<days.length;i++) {
        	Day d = new Day(context,dayNumber,year,month);
        	
        	Calendar cTemp = Calendar.getInstance();
        	cTemp.set(year, month, dayNumber);
        	int startDay = Time.getJulianDay(cTemp.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cTemp.getTimeInMillis())));
        	
        	d.setAdapter(this);

        	
        	days[i] = ""+dayNumber;
        	dayNumber++;
        	dayList.add(d);
        }
    }
	
}
