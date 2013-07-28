package com.bignerdranch.android.criminalintent;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

public class DatePickerFragment extends DialogFragment {
	
	public final static String EXTRA_DATE =
			"com.bignerdranch.android.criminalintent.date";
	private Date mDate;
	private int mMonth;
	private int mDay;
	private int mYear;
	private int mHour;
	private int mMinute;
	
	/**
	 * Static method that provides the ability to set the
	 * arguments (in a bundle) needed by the fragment
	 * 
	 * @param date
	 * @return
	 */
	static DatePickerFragment newInstance( Date date)
	{
		/* Create bundle and add arguments */
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_DATE, date);
		
		/* Create the fragment and set the arguments */
		DatePickerFragment fragment = new DatePickerFragment();
		fragment.setArguments(args);
		
		return fragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		/**
		 * Dialogs consist of three areas:
		 * Title
		 * View (optional)
		 * Button
		 */
		
		/* use the DatePicker defined in XML file */
		LayoutInflater li = getActivity().getLayoutInflater();
		View v = li.inflate(R.layout.dialog_date, null);
		
		AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
		ad.setView(v);
		ad.setTitle(R.string.date_picker_title);
		ad.setPositiveButton(android.R.string.ok, 
				new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Send the result to the hosting fragment when
				// dialog is closed
				sendResult(Activity.RESULT_OK);			
			}
		});
		
		
		/** 
		 * set the DatePicker and TimePicker fields according to the 
		 * argument passed in Bundle
		 */
		mDate = (Date)getArguments().getSerializable(EXTRA_DATE);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(mDate);
		mMonth = calendar.get(Calendar.MONTH);
		mDay = calendar.get(Calendar.DAY_OF_MONTH);
		mYear = calendar.get(Calendar.YEAR);
		mHour = calendar.get(Calendar.HOUR_OF_DAY);
		mMinute = calendar.get(Calendar.MINUTE);
		
		DatePicker datePicker = (DatePicker)v.findViewById(R.id.dialog_date_datePicker);
		datePicker.init(mYear, mMonth, mDay, new DatePicker.OnDateChangedListener() {

			@Override
			public void onDateChanged(DatePicker view, int year, 
					int monthOfYear,
					int dayOfMonth) {
				mMonth = monthOfYear;
				mDay = dayOfMonth;
				mYear = year;
				// update result argument
				updateResult();
			}
			
		});
		
		TimePicker timePicker = (TimePicker)v.findViewById(R.id.dialog_date_timePicker);
		timePicker.setCurrentHour(mHour);
		timePicker.setCurrentMinute(mMinute);
		timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
			
			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				mHour = hourOfDay;
				mMinute = minute;
				// update result argument
				updateResult();
			}
		});
		
		/* create the dialog */
		return ad.create();
	}
	
	/**
	 * Update the result argument
	 */
	private void updateResult()
	{
		/**
		 * translate year, monthOfYear, dayOfMonth to a Date using
		 * a Gregorian calendar
		 */
		mDate = new GregorianCalendar(mYear, mMonth, mDay, mHour, mMinute).getTime();
		
		/**
		 * Update the argument to preserve selected value on rotation
		 */
		getArguments().putSerializable(EXTRA_DATE, mDate);		
	}

	/**
	 * Return result to calling fragment
	 * @param resultCode
	 */
	private void sendResult( int resultCode )
	{
		/* first make sure the target is set */
		if ( getTargetFragment() == null ) return;
		
		/* put the result in an Intent */
		Intent i = new Intent();
		i.putExtra(EXTRA_DATE, mDate);
		
		/* directly invoke target fragment's onActivityResult() method */
		getTargetFragment().onActivityResult(getTargetRequestCode(), 
				resultCode, i);
	}
}
