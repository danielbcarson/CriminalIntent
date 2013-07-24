package com.bignerdranch.android.criminalintent;


import java.util.UUID;

import android.support.v4.app.Fragment;

public class CrimeActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		/** 
		 * used when the crimeId is passed as an extra
		 * in the hosting activity's intent that starts
		 * this fragment and the fragment retrieves the
		 * crimeId from the hosting activity's intent
		 */
		//return new CrimeFragment();
		
		/**
		 * used when the fragment's bundle is populated
		 * with the crimeId from the hosting activity's
		 * intent before the fragment is created.  This
		 * way the fragment doesn't need to know what
		 * hosting activity created it...it just knows
		 * that the crimeId is included in it's bundle as
		 * an argument.
		 */
		UUID crimeId = (UUID)getIntent()
				.getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
		
		return CrimeFragment.newInstance(crimeId);
	}

}
