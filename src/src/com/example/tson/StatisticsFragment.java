package com.example.tson;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import tson_utilities.Project;
import tson_utilities.User;

import android.app.DatePickerDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class StatisticsFragment extends Fragment{
	
	 /***********************
	  *  	VARIABLES		*/	
	 /***********************/
	
	ImageButton btnStart, btnEnd;
	Button btnGo;
	Calendar startDate;
	Calendar endDate;
	int i;
	int j = 0;
	EditText startTime, endTime;
	ListView projectListView;
	ArrayAdapter<Project> statsAdapter;
	
	public static User user = HomeActivity.user;
	List<Project> projectListStats = user.getProjects();
	List<StatisticsProjectItem> statItems = new ArrayList<StatisticsProjectItem>();
	
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
		  endDate = Calendar.getInstance();
		  btnStart = (ImageButton) statistics.findViewById(R.id.imageButtonStart);
		  btnEnd = (ImageButton) statistics.findViewById(R.id.imageButtonEnd);
		  btnGo = (Button) statistics.findViewById(R.id.goStatistics);
		  startTime = (EditText) statistics.findViewById(R.id.startTime);
		  endTime = (EditText) statistics.findViewById(R.id.endTime);
		  
	        projectListView = (ListView) statistics.findViewById(R.id.statistics_view);
	        
	        
		  for(int i=0;i<projectListStats.size();i++){
			  statItems.add(new StatisticsProjectItem(projectListStats.get(i).getName(), tWorked))
		  }
		  //onClick on btnStart
		  btnStart.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					i = 0;
					showDateDialog(v, startDate);					
				}
		  });		
		  
		  //onClick on btnEnd
		  btnEnd.setOnClickListener(new View.OnClickListener() {			
				@Override
				public void onClick(View v) {
					i = 1;
					showDateDialog(v, endDate);					
				}
		  });	
		  
		  //onClick on btnGo
		  btnGo.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					long daysSinceStartDate = -(Calendar.getInstance().getTimeInMillis() - startDate.getTimeInMillis())/(1000*60*60*24);
					long daysSinceEndDate = -(Calendar.getInstance().getTimeInMillis() - endDate.getTimeInMillis())/(1000*60*60*24);
					Log.d("StartDifference", ""+daysSinceStartDate);
					Log.d("EndDifference", ""+daysSinceEndDate);
				}
		  });
		  
		  
		  statsAdapter = new statsAdapter();
	        
	      projectListView.setAdapter(statsAdapter);
		  
		  
		  return statistics;
		 }
		
	 	/**
	 	 * When u click on btnStart or btnEnd u will jump in here and create a DatePickerDialog
	 	 * @param v -- Send in the View
	 	 * @param theYear -- Get what year you have
	 	 * @param theMonth -- Get what month you have
	 	 * @param theDay -- Get what day you have
	 	 */
	 	public void showDateDialog(View v, Calendar theCalendar)
	    {
	    	//Visar dialogrutan med datum
	    	DatePickerDialog dialog = new DatePickerDialog(getActivity(), datePickerListener,theCalendar.get(Calendar.YEAR), theCalendar.get(Calendar.MONTH), theCalendar.get(Calendar.DAY_OF_MONTH));
	    	dialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
	    	dialog.show();
	    }
	    
	 	/**
	 	 * When u click Done in the dialog it will save it in the user and print the time out 
	 	 */
	 	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() 
	 	{
	 		public void onDateSet(DatePicker view, int selectedYear,
		    int selectedMonth, int selectedDay) 
	 		{
	 			Calendar temp = Calendar.getInstance();
	 			
	 			temp.set(selectedYear, selectedMonth, selectedDay);
	 			//Checks if the date is bigger then the actual and changes it if its true
	 			if(startDate.after(temp))
	 			{
	 				startDate = (Calendar) temp.clone();
	 				startTime.setText(selectedDay + " / " + (selectedMonth + 1) + " / "
				  			+ selectedYear);
	 			}
	 			//Checks if the date2 is smaller then the actual and changes it if its true
	 			if(endDate.before(temp))
	 			{
	 				endDate = (Calendar) temp.clone();
	 				endTime.setText(selectedDay + " / " + (selectedMonth + 1) + " / "
				  			+ selectedYear);
	 			}
	 			//Sets and shows the chosen date 
				if(i==0)
				{
					startTime.setText(selectedDay + " / " + (selectedMonth + 1) + " / "
							  			+ selectedYear);
					startDate = (Calendar) temp.clone();
				}
				else
				{
					endTime.setText(selectedDay + " / " + (selectedMonth + 1) + " / "
							  + selectedYear);
					endDate = (Calendar) temp.clone();
				}
	 		}
		 };
		 
		 public static int dpToPx(int dp)
		    {
		        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
		    }
		    
		    private class statsAdapter extends ArrayAdapter<StatisticsProjectItem>{
		    	
		    	
				public statsAdapter() {
					super(getActivity(), R.layout.statistics_listview_item, statItems);
					// TODO Auto-generated constructor stub
				}
				
				@Override
				public View getView(int position, View view, ViewGroup parent){
					if(view == null)
						view = getActivity().getLayoutInflater().inflate(R.layout.statistics_listview_item, parent, false);
					
					Calendar temp = (Calendar) startDate.clone();
			        TextView projectItem = (TextView) view.findViewById(R.id.statistics_project_item);
			        TextView progBar = (TextView) view.findViewById(R.id.statistics_progress_bar);
			        //double x = 0.2;
			        
			        double widthHolder = 300;
			        double x = 0;
			        
			        while(temp.before(endDate))
			        {
			        	x += user.getProjects().get(position).getTimeByDate(temp).getTimeInMinutes();
			        	temp.add(Calendar.DAY_OF_YEAR, 1);
			        }
			        
			        
			        int y = (int) (x * widthHolder);
			        
			        Log.d("YYYYYYY",""+holder);
			        
			        RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(dpToPx(y), dpToPx(40));
			        rl.setMargins(dpToPx(5),dpToPx(5),dpToPx(5),dpToPx(5));
			        
			        progBar.setLayoutParams(rl);
			        
			        
			        return view;
					
				}
		    }
		    public class StatisticsProjectItem
		    {
		    	String projectName;
		    	int timeWorked;
		    	double percentWorked;
		    	public StatisticsProjectItem(String pName, int tWorked)
		    	{
		    		projectName = pName;
		    		timeWorked = tWorked;
		    	}
		    	
		    	public void setPercentWorked(double p)
		    	{
		    		percentWorked = p;
		    	}
		    }
}
