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

public class StatisticsFragment extends Fragment{
	
	 /***********************
	  *  	VARIABLES		*/	
	 /***********************/
	
	ImageButton btnStart, btnEnd;
	Button btnGo;
	Calendar cal;
	int day, day2, month, month2, year, year2, i, totalDays, totalDays2;
	EditText startTime, endTime;
	
	
	 /***********************
	  *  	OTHERS			*/	
	 /************************/
	
	public StatisticsFragment(){}

	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
	         Bundle savedInstanceState) {
		 
		  super.onCreate(savedInstanceState);
		  View statistics = inflater.inflate(R.layout.statistics_fragment, container, false);
		  btnStart = (ImageButton) statistics.findViewById(R.id.imageButtonStart);
		  btnEnd = (ImageButton) statistics.findViewById(R.id.imageButtonEnd);
		  btnGo = (Button) statistics.findViewById(R.id.goStatistics);
		  cal = Calendar.getInstance();
		  day2 = day = cal.get(Calendar.DAY_OF_MONTH);
		  month2 = month = cal.get(Calendar.MONTH);
		  year2 = year = cal.get(Calendar.YEAR);
		  startTime = (EditText) statistics.findViewById(R.id.startTime);
		  endTime = (EditText) statistics.findViewById(R.id.endTime);
		  
		  //onClick on btnStart
		  btnStart.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					i = 0;
					showDateDialog(v, year, month, day);					
				}
		  });		
		  
		  //onClick on btnEnd
		  btnEnd.setOnClickListener(new View.OnClickListener() {			
				@Override
				public void onClick(View v) {
					i = 1;
					showDateDialog(v, year2, month2, day2);					
				}
		  });	
		  
		  //onClick on btnGo
		  btnGo.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Calendar temp = Calendar.getInstance();
					temp.set(Calendar.YEAR, year);
					temp.set(Calendar.MONTH, month-1);
					temp.set(Calendar.DAY_OF_MONTH, day);
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
	 	public void showDateDialog(View v, int theYear, int theMonth, int theDay)
	    {
	    	//Visar dialogrutan med datum
	    	new DatePickerDialog(getActivity(), datePickerListener, theYear, theMonth, theDay).show();
	    }
	    
	 	/**
	 	 * When u click Done in the dialog it will save it in the user and print the time out 
	 	 */
	 	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() 
	 	{
	 		public void onDateSet(DatePicker view, int selectedYear,
		    int selectedMonth, int selectedDay) 
	 		{
	 			//Checks if the date is bigger then the actual and changes it if its true
	 			if(year > selectedYear || month > selectedMonth || day > selectedDay)
	 			{
	 				year = selectedYear;
	 				month = selectedMonth;
	 				day = selectedDay;
	 				startTime.setText(selectedDay + " / " + (selectedMonth + 1) + " / "
				  			+ selectedYear);
	 			}
	 			//Checks if the date2 is smaller then the actual and changes it if its true
	 			if(year2 < selectedYear || month2 < selectedMonth || day2 < selectedDay)
	 			{
	 				year2 = selectedYear;
	 				month2 = selectedMonth;
	 				day2 = selectedDay;
	 				endTime.setText(selectedDay + " / " + (selectedMonth + 1) + " / "
				  			+ selectedYear);
	 			}
	 			//Sets and shows the chosen date 
				if(i==0)
				{
					startTime.setText(selectedDay + " / " + (selectedMonth + 1) + " / "
							  			+ selectedYear);
					year = selectedYear;
					month = selectedMonth;
					day = selectedDay;
				}
				else
				{
					endTime.setText(selectedDay + " / " + (selectedMonth + 1) + " / "
							  + selectedYear);
					year2 = selectedYear;
					month2 = selectedMonth;
					day2 = selectedDay;
				}
	 		}
		 };
}
