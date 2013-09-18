package com.dhansiddh.chatmessenger;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;


public class SampleListFragment extends ListFragment{
   String username;
   View v;
   Button b,b1,b2;
   public SampleListFragment()
   {
   }
	public SampleListFragment(String name)
   {
	   username=name;
   }
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		v=inflater.inflate(R.layout.secondlist,null);
		b=(Button) v.findViewById(R.id.username);
		b.setText(username);
		b1=(Button) v.findViewById(R.id.button1);
		b1.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				MainActivity ra = (MainActivity) getActivity();
				ra.switchmenucontent();
			}
		});
		b2=(Button) v.findViewById(R.id.button3);
		/**b2.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				MainActivity ra = (MainActivity) getActivity();
				ra.setbroadcastcontent();
				
			}
		});**/
		
		return v;
	}
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
	}
}
