/*
 * Copyright 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.actionbarcompat;

import java.util.Locale;

import com.example.android.actionbarcompat.activity.NewActivity;
import com.example.android.actionbarcompat.fragment.MainFragment;
import com.example.android.actionbarcompat.services.ControlService;
import com.example.android.actionbarcompat.services.util.Const;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
	private boolean mAlternateTitle = false;

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mPlanetTitles;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);

		mTitle = mDrawerTitle = getTitle();
		mPlanetTitles = getResources().getStringArray(R.array.planets_array);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		// set up the drawer's list view with items and click listener
		mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mPlanetTitles));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			getActionBar().setHomeButtonEnabled(true);
		}
		

	}

	public void setTitleFrag(){
		if (mAlternateTitle) {
			setTitle(R.string.app_name);
		} else {
			setTitle(R.string.alternate_title);
		}
		mAlternateTitle = !mAlternateTitle;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		FragmentManager fragmentManager = getSupportFragmentManager();
		Fragment fragment = new MainFragment();
		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.main, menu);

		// Calling super after populating the menu is necessary here to ensure that the
		// action bar helpers have a chance to handle this event.
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Toast.makeText(this, "Tapped home", Toast.LENGTH_SHORT).show();
			if(mDrawerLayout.isDrawerOpen(mDrawerList))
				mDrawerLayout.closeDrawer(mDrawerList);
			else
				mDrawerLayout.openDrawer(mDrawerList);
			break;

		case R.id.menu_refresh:
			Toast.makeText(this, "Fake refreshing...", Toast.LENGTH_SHORT).show();
			getActionBarHelper().setRefreshActionItemState(true);
			getWindow().getDecorView().postDelayed(
					new Runnable() {
						@Override
						public void run() {
							getActionBarHelper().setRefreshActionItemState(false);
						}
					}, 1000);
			break;

		case R.id.menu_search:
			Toast.makeText(this, "Tapped search", Toast.LENGTH_SHORT).show();
			break;

		case R.id.menu_share:
			Toast.makeText(this, "Tapped share", Toast.LENGTH_SHORT).show();
			startActivity(new Intent(getApplicationContext(), NewActivity.class));
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			selectItem(position);
		}
	}

	private void selectItem(int position) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		// update the main content by replacing fragments
		if(position==0){
			Fragment fragment = new MainFragment();
			fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
		}else{
			Fragment fragment = new PlanetFragment();
			Bundle args = new Bundle();
			args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
			fragment.setArguments(args);


			fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

			// update selected item and title, then close the drawer
			//		      mDrawerList.setItemChecked(position, true);
			//			setTitle(mPlanetTitles[position]);
			//		      mDrawerLayout.closeDrawer(mDrawerList);
		}

		mDrawerList.setItemChecked(position, true);
		setTitle(mPlanetTitles[position]);
		mDrawerLayout.closeDrawer(mDrawerList);

	}

	/**
	 * Fragment that appears in the "content_frame", shows a planet
	 */
	public static class PlanetFragment extends Fragment {
		public static final String ARG_PLANET_NUMBER = "planet_number";

		public PlanetFragment() {
			// Empty constructor required for fragment subclasses
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_planet, container, false);
			int i = getArguments().getInt(ARG_PLANET_NUMBER);
			String planet = getResources().getStringArray(R.array.planets_array)[i];

			int imageId = getResources().getIdentifier(planet.toLowerCase(Locale.getDefault()), "drawable", getActivity().getPackageName());
			((ImageView) rootView.findViewById(R.id.image)).setImageResource(imageId);
			getActivity().setTitle(planet);
			return rootView;
		}
	}

	public void onClickBtn(View v) {

		int resId = v.getId();
		Intent i  = new Intent(getApplicationContext(), ControlService.class);
		i.putExtra(Const.SERVICE_ID, resId);
		startService(i);

	}
}
