package com.example.android.actionbarcompat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.actionbarcompat.MainActivity;
import com.example.android.actionbarcompat.R;
import com.example.android.actionbarcompat.services.ControlService;
import com.example.android.actionbarcompat.services.util.Const;


public class MainFragment extends Fragment{
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.tasti_layout, container, false);
		
		rootView.findViewById(R.id.toggle_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	((MainActivity)getActivity()).setTitleFrag();
            }
        });
		
        return rootView;
	}
	


}
