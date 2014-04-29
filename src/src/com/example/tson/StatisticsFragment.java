package com.example.tson;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

import tson_utilities.Project;
import tson_utilities.TimeBlock;
import tson_utilities.User;
import android.app.DatePickerDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 
 * @author Ramin
 *
 */
public class StatisticsFragment extends Fragment{
	
	 /***********************
	  *  	VARIABLES		*/	
	 /***********************/
	
	ImageButton btnStart, btnEnd;
	Calendar startDate;
	Calendar endDate;
	int dialogChooser;
	int totalMinutes;
    int updateWidth = 0;
    int projectHours = 0;
    boolean mFirst;
    DecimalFormat decimalFormat=new DecimalFormat("#0.#");
	EditText startTime, endTime;
	ListView projectListView;
	ArrayAdapter<Project> statsAdapter;
	public static User user = HomeActivity.user;
	List<Project> projectListStats = user.getProjects();
	double[] projectMinutes = new double[projectListStats.size()];
	
	 /***********************
	  *  	OTHERS			*/	
	 /************************/
	
	public StatisticsFragment(){}
	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
	         Bundle savedInstanceState) {
		 
		  super.onCreate(savedInstanceState);
		  View statistics = inflater.inflate(R.layout.statistics_fragment, container, false);
		  startDate = Calendar.getInstance();
		  startDate.set(startDate.get(Calendar.YEAR),startDate.get(Calendar.MONTH),1);
		  endDate = Calendar.getInstance();
		  btnStart = (ImageButton) statistics.findViewById(R.id.imageButtonStart);
		  btnEnd = (ImageButton) statistics.findViewById(R.id.imageButtonEnd);
		  startTime = (EditText) statistics.findViewById(R.id.startTime);
		  endTime = (EditText) statistics.findViewById(R.id.endTime);
		  projectListView = (ListView) statistics.findViewById(R.id.statistics_view);
		  
		  //Calculate the whole month on first create
		  calculateTime(startDate, endDate);
		  
		  //onClick on btnStart
		  btnStart.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialogChooser = 0;
					showDateDialog(v, startDate);					
				}
		  });		
		  
		  //onClick on btnEnd
		  btnEnd.setOnClickListener(new View.OnClickListener() {			
				@Override
				public void onClick(View v) {
					dialogChooser = 1;
					showDateDialog(v, endDate);					
				}
		  });	
		  
		  return statistics;
		 }
	 	
	 
	 	/**
	 	 * When u click on btnStart or btnEnd u will jump in here and create a DatePickerDialog
	 	 * @param v Send in the View
	 	 * @param theYear Get what year you have
	 	 * @param theMonth Get what month you have
	 	 * @param theDay Get what day you have
	 	 */
	 	public void showDateDialog(View v, Calendar theCalendar)
	    {
	    	//Displays one of the dialogs for the datepicker
	 		if(dialogChooser==0)
	 		{
	 			mFirst = true;
	 			DatePickerDialog dialog = new DatePickerDialog(getActivity(), datePickerListener,theCalendar.get(Calendar.YEAR), theCalendar.get(Calendar.MONTH), theCalendar.get(Calendar.DAY_OF_MONTH));
	 			dialog.getDatePicker().setMaxDate(endDate.getTimeInMillis());
	 			dialog.getDatePicker().setCalendarViewShown(false);
	 			dialog.show();
	 		}
	 		else
	 		{
	 			mFirst = true;
	 			DatePickerDialog dialog = new DatePickerDialog(getActivity(), datePickerListener,theCalendar.get(Calendar.YEAR), theCalendar.get(Calendar.MONTH), theCalendar.get(Calendar.DAY_OF_MONTH)); 
	 			dialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
	 			dialog.getDatePicker().setCalendarViewShown(false);
		    	dialog.getDatePicker().setMinDate(startDate.getTimeInMillis());
		    	dialog.show();
	 		}
	    	
	    }
	    
	 	/**
	 	 * When u click Done in the dialog it will save it in the user and print the time out 
	 	 */
	 	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() 
	 	{
	 		public void onDateSet(DatePicker view, int selectedYear,
		    int selectedMonth, int selectedDay) 
	 		{
	 			if (mFirst) 
	 			{
		 	        mFirst = false;
		 			Calendar temp = Calendar.getInstance();
		 			temp.set(selectedYear, selectedMonth, selectedDay);
		 			//Sets and shows the chosen date 	 			
					if(dialogChooser==0)
					{	
						calculateTime(temp, endDate);				
					}
					else
					{
						calculateTime(startDate, temp);
					}
	 			}
	 		}
		 };
		 
		 /**
		  * Calculates all the timeblocks that is in the timespand
		  * @param start The start date 
		  * @param end The end date
		  */
		 public void calculateTime(Calendar start, Calendar end)
		 {	
			totalMinutes = 0;
			for(int i=0;i<projectListStats.size();i++)
			{
				List<TimeBlock> tb = projectListStats.get(i).getSubmissionList();
				  for(int j=0;j < tb.size(); j++)
				  {
					  Calendar compareDate = tb.get(j).getDate();
					  // 0 if equals, -1 if the time of this calendar is before the other one,
					  // 1 if the time of this calendar is after the other one.
					  if(start.compareTo(compareDate) == -1 && end.compareTo(compareDate) == 1)
					  {
						  if(user.isDateConfirmed(compareDate) == 1)
						  {
							  totalMinutes = totalMinutes + tb.get(j).getTimeInMinutes();
							  projectMinutes[i] += tb.get(j).getTimeInMinutes();									  
						  }
					  }
					  //Also checks if the two dates are equal
					  else if(isSameDay(start, compareDate) && isSameDay(end, compareDate))
					  {
						  totalMinutes = totalMinutes + tb.get(j).getTimeInMinutes();
						  projectMinutes[i] += tb.get(j).getTimeInMinutes();
					  }
				  }
			}
			
			//Checks which of the dates that has been changes and then sets the new time and date
			if(isSameDay(end, endDate))
			{				
				startTime.setText(start.get(Calendar.DAY_OF_MONTH) + " / " + (start.get(Calendar.MONTH) + 1) + " / "
						+ start.get(Calendar.YEAR));
				startDate = (Calendar) start.clone();
			}
			if(isSameDay(start, startDate))
			{
				endTime.setText(end.get(Calendar.DAY_OF_MONTH) + " / " + (end.get(Calendar.MONTH) + 1) + " / "
						+ end.get(Calendar.YEAR));
				endDate = (Calendar) end.clone();
			}
			
			//Updates the view
			statsAdapter = new statsAdapter();
		    projectListView.setAdapter(statsAdapter);
		 }
		 
		 
		/**
		 * Calculates if two dates are equal
		 */
		public boolean isSameDay(Calendar cal1, Calendar cal2) {
			    if (cal1 == null || cal2 == null)
			        return false;
			    return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA)
			            && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) 
			            && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
		}
		
		/**
		 * Calculates the dp value to pixels
		 * @param dp The value of dp
		 * @return Returns the converted pixel value
		 */
		public static int dpToPx(int dp)
		{
			return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
		}
		
		/**
		 * Updates the view
		 * @author Ramin
		 *
		 */
	    private class statsAdapter extends ArrayAdapter<Project>{
			public statsAdapter() {
				super(getActivity(), R.layout.statistics_listview_item, projectListStats);
			}
			
			@Override
			public View getView(int position, View view, ViewGroup parent){
				if(view == null)
					view = getActivity().getLayoutInflater().inflate(R.layout.statistics_listview_item, parent, false);
			
		        TextView progBar = (TextView) view.findViewById(R.id.statistics_progress_bar);
		        TextView projectName = (TextView) view.findViewById(R.id.textProject);
		        TextView percentValue = (TextView) view.findViewById(R.id.percantValue);
		        TextView hourValue = (TextView) view.findViewById(R.id.hourValue);
		        TextView minuteValue = (TextView) view.findViewById(R.id.minuteValue);
		        projectName.setText(user.getProjects().get(position).getName());
		        
		        //Sets the text for the hour and minutes
		        hourValue.setText(""+(int) projectMinutes[position]/60 + " h");
		        minuteValue.setText(""+(int) Math.round(projectMinutes[position]%60) + " m");
		        
		        //Calculates the percentage of the width
		        double widthHolder = parent.getWidth();
		        double percent = 0;
		        updateWidth = 0;
		        if(totalMinutes != 0)
		        {
		        	percent = projectMinutes[position]/totalMinutes;

		        }
		        
		        //Resets the amount of project minutes
		        projectMinutes[position] = 0; 
		        
		        //Sets the width of percent bar
		        updateWidth = (int)(percent*widthHolder);
		        
		        //Sets the text for the percentage
		        percentValue.setText(decimalFormat.format(percent*100) + " %"); 
		        
		        //Updates the progBar with the new width
		        RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(updateWidth, dpToPx(40));
		        rl.setMargins(0,0,0,dpToPx(5));
		        progBar.setLayoutParams(rl);
		        
		        return view;
			}
	    }

}
