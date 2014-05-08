package com.example.tson;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.List;

import tson_utilities.Project;
import tson_utilities.TimeBlock;
import tson_utilities.User;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.TextView;

public class ExportFragment extends Fragment{
	
	 /***********************
	  *  	VARIABLES		*/	
	 /***********************/
	
	ImageButton btnStart, btnEnd;
	Button btnGo;
	Calendar startDate;
	Calendar endDate;
	int i;
	EditText startTime, endTime;

	public static User user = User.getInstance();

	TextView projectText;
	ArrayAdapter<Project> projectAdapter;
	List<Project> projectListStats = user.getProjects();
	ListView projectList;
	
	
	
	 /***********************
	  *  	OTHERS			*/	
	 /************************/
	
	public ExportFragment(){}

	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
	         Bundle savedInstanceState) {
		 
		  super.onCreate(savedInstanceState);
		  View statistics = inflater.inflate(R.layout.export_fragment, container, false);
		  projectList = (ListView) statistics.findViewById(R.id.export_list_view);
		  startDate = Calendar.getInstance();
		  endDate = Calendar.getInstance();
		  btnStart = (ImageButton) statistics.findViewById(R.id.imageButtonStartExport);
		  btnEnd = (ImageButton) statistics.findViewById(R.id.imageButtonEndExport);
		  btnGo = (Button) statistics.findViewById(R.id.export);
		  startTime = (EditText) statistics.findViewById(R.id.startTimeExport);
		  endTime = (EditText) statistics.findViewById(R.id.endTimeExport);
		  
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
					
					Intent intent = new Intent(getActivity(), SendEmailActivity.class);
					startActivity(intent);
					
					
					long daysSinceStartDate = -(Calendar.getInstance().getTimeInMillis() - startDate.getTimeInMillis())/(1000*60*60*24);
					long daysSinceEndDate = -(Calendar.getInstance().getTimeInMillis() - endDate.getTimeInMillis())/(1000*60*60*24);
					Log.d("StartDifference", ""+daysSinceStartDate);
					Log.d("EndDifference", ""+daysSinceEndDate);
					
					
					exportTime(startDate, endDate);
					
					
					
				}
		  });
		  
		  projectAdapter = new projectAdapter();
		  projectList.setAdapter(projectAdapter);
		  
		  return statistics;
		 }
	 
	 	/**
	 	 * A function that examines the projectlist, picks all timeblocks within start and end-date,
	 	 * prints it to a csv-file and gives the user the ability to send it via an external email-app
	 	 * 
	 	 * @author Albin Törnqvist
	 	 * @param start
	 	 * @param end
	 	 */
	 
	 	public void exportTime(Calendar start, Calendar end)
	 	{
	 		String outputString = "Projectname, Year, Month, Day, Hours, Min," + "\n";
	 		
	 		//Loops through all projects and adds all timeblocks in the timespan to the outputString
	 		for(int i=0;i<projectListStats.size();i++)
			{
				List<TimeBlock> tb = projectListStats.get(i).getSubmissionList();
				  for(int j=0; j < tb.size(); j++)
				  {
					  Calendar timeblockDate = tb.get(j).getDate();
				
					  if((start.before(timeblockDate) || isSameDay(start, timeblockDate)) && end.after(timeblockDate))
					  {
						  if(user.isDateConfirmed(timeblockDate) == 1)
						  {
							  outputString += projectListStats.get(i).getName() + ", ";
							  outputString += Integer.toString(tb.get(j).getDate().get(Calendar.YEAR)) + ", ";
							  outputString += Integer.toString(tb.get(j).getDate().get(Calendar.MONTH)+1) + ", ";
							  outputString += Integer.toString(tb.get(j).getDate().get(Calendar.DAY_OF_MONTH)) + ", ";
							  outputString += Integer.toString(tb.get(j).getTimeInMinutes()/60) + ", ";
							  outputString += Integer.toString(Math.round(tb.get(j).getTimeInMinutes()%60)) + ", " + "\n";
  
						  }
					  }  
				  }
			}
	 		

	 		
	 		String filename = "ReportedTime.csv";       
	 		
	 		try {
	 			//Test to write to file, "old-fashioned way". usually doesn't work. Might be good to have if the next case doesn't work. or not.
	 		    @SuppressWarnings("deprecation")
				FileOutputStream fos = getActivity().openFileOutput(filename, Context.MODE_APPEND | Context.MODE_WORLD_READABLE);
	 		    fos.write(outputString.getBytes());
	 		    fos.close();
	 		    
	 		    //Writing to file in external directory using externalstoragestate. The main way to write files.
	 		    String storageState = Environment.getExternalStorageState();
	 		    if (storageState.equals(Environment.MEDIA_MOUNTED)) 
	 		    {
	 		        File file = new File(getActivity().getExternalFilesDir(null), filename);
	 		        FileOutputStream fos2 = new FileOutputStream(file);
	 		        fos2.write(outputString.getBytes());
	 		        fos2.close();
	 		    }
	 		    
	 		} catch (Exception e) {
	 		    e.printStackTrace();
	 		    Log.d("Kunde inte skriva till fil", e.toString());
	 		}
	 		
            
            //Accessing the files directory
            File file = new File(getActivity().getExternalFilesDir(null), filename);
	        Uri u1  =   null;
	        u1  =   Uri.fromFile(file);
	        
	        //Creating intent for sending emails with attachment and subject.
	        Intent sendIntent = new Intent(Intent.ACTION_SEND);
	        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Tson, reported time");
	        sendIntent.putExtra(Intent.EXTRA_STREAM, u1);
	        sendIntent.setType("text/csv");
	        startActivity(sendIntent);
	 		
	 	}
	 	
	 
	 
	 	/**
		 * Calculates if two dates are equal
		 * @author Ramin Assadi
		 */
	 	private boolean isSameDay(Calendar cal1, Calendar cal2) {
	 		if (cal1 == null || cal2 == null)
		        return false;
		    return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA)
		            && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) 
		            && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
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
		 
		 private class projectAdapter extends ArrayAdapter<Project>{
				public projectAdapter() {
					super(getActivity(), R.layout.statistics_listview_item, projectListStats);
				}
				
				@Override
				public View getView(int position, View view, ViewGroup parent){
					if(view == null)
						view = getActivity().getLayoutInflater().inflate(R.layout.project_list_export, parent, false);
					projectText = (TextView) view.findViewById(R.id.projectNameTextView);
					projectText.setText(user.getProjects().get(position).getName());
						
			        return view;
				}
		    }
}
