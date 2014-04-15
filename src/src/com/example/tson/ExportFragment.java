package com.example.tson;
import java.util.Calendar;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

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
	
	
	 /***********************
	  *  	OTHERS			*/	
	 /************************/
	
	public ExportFragment(){}

	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
	         Bundle savedInstanceState) {
		 
		  super.onCreate(savedInstanceState);
		  View statistics = inflater.inflate(R.layout.export_fragment, container, false);
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
					long daysSinceStartDate = -(Calendar.getInstance().getTimeInMillis() - startDate.getTimeInMillis())/(1000*60*60*24);
					long daysSinceEndDate = -(Calendar.getInstance().getTimeInMillis() - endDate.getTimeInMillis())/(1000*60*60*24);
					Log.d("StartDifference", ""+daysSinceStartDate);
					Log.d("EndDifference", ""+daysSinceEndDate);
				}
		  });
		  
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
}
