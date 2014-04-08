package com.example.android.actionbarcompat.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.android.actionbarcompat.ActionBarActivity;
import com.example.android.actionbarcompat.MainActivity;
import com.example.android.actionbarcompat.R;

public class NewActivity extends ActionBarActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			getActionBar().setHomeButtonEnabled(true);
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}else{
			getActionBarHelper().setBackHomeButtonActionItem(true);
		}
	}
	
	 @Override
	   public boolean onOptionsItemSelected(MenuItem item) {
	       // This callback is used only when mSoloFragment == true (see onActivityCreated above)
	       switch (item.getItemId()) {
	       case android.R.id.home:
	           // App icon in Action Bar clicked; go up
	           Intent intent = new Intent(getApplicationContext(), MainActivity.class);
	           intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Reuse the existing instance
	           startActivity(intent);
	           return true;
	       default:
	           return super.onOptionsItemSelected(item);
	       }
	   }

}
