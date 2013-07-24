package com.bignerdranch.android.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import java.util.Locale;
import java.util.UUID;

public class CrimeFragment extends Fragment {
	
	public static final String EXTRA_CRIME_ID = 
			"com.bignerdranch.android.criminalintent.crime_id";
	
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
		
		// Format date using SimpleDateFormat class
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMM dd, yyyy", Locale.US);
        mDateButton.setText(sdf.format(mCrime.getDate()));
		//mDateButton.setText(mCrime.getDate().toString());
		mDateButton.setEnabled(false);
		
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
	

}
