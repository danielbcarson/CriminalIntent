package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class CrimeFragment extends Fragment {
	
	public static final String EXTRA_CRIME_ID = 
			"com.bignerdranch.android.criminalintent.crime_id";
	private static final String DIALOG_DATE = "date";
	private static int REQUEST_DATE= 0;
	
	private Crime mCrime;
	private EditText mTitleField;
	private Button mDateButton;
	private CheckBox mSolvedCheckBox;
	
	/**
	 * Static method that should be used to create a new
	 * CrimeFragment.  This puts the data needed by a 
	 * CrimeFragment in the fragment's Bundle before the
	 * fragment is created.
	 * 
	 * @param crimeId
	 * @return
	 */
	public static CrimeFragment newInstance( UUID crimeId )
	{
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_CRIME_ID, crimeId);
		
		CrimeFragment fragment = new CrimeFragment();
		fragment.setArguments(args);
		
		return fragment;
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/** The fragment needs the crimeId to populate
		 *  it's view.  One way is to have the Id 
		 *  passed as an extra in the Intent from the 
		 *  hosting Activity...but this introduces a
		 *  dependency of the fragment on the 
		 *  hosting activity intent
		 */
		//UUID crimeId = (UUID)getActivity().getIntent()
		//		.getSerializableExtra(EXTRA_CRIME_ID);
		
		/**
		 * A Better way is to have the crimeID as part
		 * of the fragments 'bundle' so that it is 
		 * encapsulated as part of the fragment's data.
		 * The hosting activity must set this fragment 
		 * argument (crimeId) in the fragment's bundle 
		 * before the fragment is created.
		 */
		UUID crimeId = (UUID)getArguments().getSerializable(EXTRA_CRIME_ID);
		
		
		mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		
		/* inflate the fragment View */
		View v =  inflater.inflate(R.layout.fragment_crime, parent, 
				false);
		
		mTitleField = (EditText)v.findViewById(R.id.crime_title);
		mTitleField.setText(mCrime.getTitle());
		mTitleField.addTextChangedListener( new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, 
					int before, int count) {
				mCrime.setTitle(s.toString());		
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				/* not used */
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				/* not used */				
			}
		});
		
		mDateButton = (Button)v.findViewById(R.id.crime_date);		
		updateDate(mCrime.getDate());        
		mDateButton.setOnClickListener( new View.OnClickListener() {
			/**
			 * Show a DatePicker dialog
			 */
			@Override
			public void onClick(View v) {
				FragmentManager fm = 
						getActivity().getSupportFragmentManager();
				
				/* create the DatePicker fragment */
				DatePickerFragment dialog =
						DatePickerFragment.newInstance(mCrime.getDate());
				
				/* set up this fragment to receive data that is returned from the 
				 * DatePicker fragment 
	             */
				dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
				
				/* show the DatePicker fragment. DIALOG_DATE is a string
				 * that uniquely identifies the DialogFragment in the
				 * Fragment Manager's list  */
				dialog.show(fm, DIALOG_DATE);
			}
		} );
		
		mSolvedCheckBox = (CheckBox)v.findViewById(R.id.crime_solved);
		mSolvedCheckBox.setChecked(mCrime.isSolved());
		mSolvedCheckBox.setOnCheckedChangeListener( 
				new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				mCrime.setSolved(isChecked);
			}
		});
		
		return v;
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if ( resultCode != Activity.RESULT_OK) return;
		
		if ( requestCode == REQUEST_DATE )
		{
			Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
			mCrime.setDate(date);
			updateDate(date);
		}
	}
	
	private void updateDate( Date date )
	{
		// Format date using SimpleDateFormat class
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        mDateButton.setText(sdf.format(date));
	}
	

}
