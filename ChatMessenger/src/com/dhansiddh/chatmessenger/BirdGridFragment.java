package com.dhansiddh.chatmessenger;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class BirdGridFragment extends Fragment {

	private int mPos = -1;
	private int mImgRes;
	String name,number,msg;
	EditText ms;
	TextView msbox;
	
	MainActivity ra;
	ListView msglist;
	public BirdGridFragment() { }
	public BirdGridFragment(int pos,String nm,String numb) {
		mPos = pos;
		name=nm;
		number=numb;
	}
	
	@SuppressLint("NewApi")
	@Override
	public RelativeLayout onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (mPos == -1 && savedInstanceState != null)
			mPos = savedInstanceState.getInt("mPos");	
		RelativeLayout gv =(RelativeLayout) inflater.inflate(R.layout.activity_main,null);
		msbox=(TextView) gv.findViewById(R.id.msbox);
		Button but=(Button) gv.findViewById(R.id.sendbut);
		msglist=(ListView) gv.findViewById(R.id.messagelist);
		ra = (MainActivity) getActivity();
		boolean hasActionBar = android.os.Build.VERSION.SDK_INT >= 11;
		if (hasActionBar) {
		ActionBar abActionBar = ra.getActionBar();
		abActionBar.setTitle(name);
		abActionBar.setSubtitle(number);
		}
		ra.setadap(msglist,number);
		but.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				MainActivity mm=(MainActivity) getActivity();
				msg=msbox.getText().toString();
				msbox.setText("");
				if(msg.length()==0)
				Toast.makeText(ra,"Enter the Message",Toast.LENGTH_LONG).show();
				else{
				mm.senddmsg(number,msg);
				}
			}
		});
	    return gv;
	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("mPos", mPos);
	}
}
