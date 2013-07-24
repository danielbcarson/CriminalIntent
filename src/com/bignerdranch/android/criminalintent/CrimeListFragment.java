package com.bignerdranch.android.criminalintent;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class CrimeListFragment extends ListFragment {

	private ArrayList<Crime> mCrimes;
	private final static String TAG = "CrimeListFragment";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/**
		 * use the Fragment getActivity convenience method
		 * to get a handle to the hosting activity.  This
		 * allows the fragment to handle more of the activity's
		 * affairs 
		 */
		getActivity().setTitle(R.string.crimes_title);
		mCrimes = CrimeLab.get(getActivity()).getCrimes();
		
		/**
		 * using pre-defined adapter for the list view
		 * ArrayAdapter<Crime> mAdapter = 
		 *		new ArrayAdapter<Crime>(getActivity(), 
		 *				android.R.layout.simple_list_item_1, mCrimes);
	     */
		
		/* custom adapter for the list view */
		CrimeAdapter mAdapter = new CrimeAdapter(mCrimes);
						
		setListAdapter(mAdapter);
	}

	@Override
	/**
	 * Need to update list because the CrimeActivity started by 
	 * a user click on a list item could have changed the 
	 * details of the crime. OnResume() is generally the safest
	 * place to update a fragment's view.
	 */
	public void onResume() {
		super.onResume();
		
		((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Crime c = ((CrimeAdapter)getListAdapter()).getItem(position);
		//Log.d(TAG, c.getTitle() + " was clicked");
		
		/* Create Intent to start CrimeActivity */
		Intent i = new Intent(getActivity(), CrimeActivity.class);
		
		/** 
		 * CrimeActivity needs access to the selected crime's data.
		 * One method is to put the crimeId in an extra attached to 
		 * the intent used to start CrimeActivity.
		 */
		i.putExtra(CrimeFragment.EXTRA_CRIME_ID, c.getId());
		
		/* Start CrimeActivity */
		startActivity(i);
	}

	private class CrimeAdapter extends ArrayAdapter<Crime> {
		public CrimeAdapter( ArrayList<Crime> crimes )
		{
			/**
			 * We will not be using a pre-defined layout (such as
			 * android.R.layout.simple_list_item_1) so we pass 0
			 * for the layout ID.
			 */
			super( getActivity(), 0, crimes );
		}

		
		/**
		 * The place to create and return a custom list item is to
		 * override the ArrayAdapter<T> method getView()
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			// If we weren't given a view inflate one 
			if ( convertView == null )
			{
				LayoutInflater li = getActivity().getLayoutInflater();
				convertView = li.inflate(R.layout.list_item_crime, null);
			}
			
			// Configure the view for this crime
			Crime c = getItem( position );
			
			TextView titleTextView = 
					(TextView)convertView.findViewById(R.id.crime_list_item_titleTextView);
			titleTextView.setText(c.getTitle());
			TextView dateTextView =
					(TextView)convertView.findViewById(R.id.crime_list_item_dateTextView);
			dateTextView.setText(c.getDate().toString());
			CheckBox solvedCheckBox = 
					(CheckBox)convertView.findViewById(R.id.crime_list_item_solvedCheckBox);
			solvedCheckBox.setChecked(c.isSolved());
					
			return convertView;
		}
		
	}
}
