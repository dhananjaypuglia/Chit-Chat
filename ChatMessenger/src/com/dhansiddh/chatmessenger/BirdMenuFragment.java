package com.dhansiddh.chatmessenger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.dhansiddh.chatmessenger.R.string;
import com.slidingmenu.lib.SlidingMenu;

import android.content.Context;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class BirdMenuFragment extends ListFragment {
SampleAdapter adapter;
String[] conc;
String [] number;
int len=0;
View broadcastview;
Context cn=getActivity();
LayoutInflater inflater;
boolean isbroadcastmsg;
String[] contt=new String[400];
public BirdMenuFragment(){}
    public BirdMenuFragment(String con[],int length,String num[],boolean isbroadcast) {
    len=length;
    conc=con;
    number=num;
    isbroadcastmsg=isbroadcast;
    }
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		broadcastview=inflater.inflate(R.layout.list, null);
		return broadcastview;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		adapter = new SampleAdapter(getActivity());
		for (int i=0;i < len; i++) {
			
			adapter.add(new SampleItem(conc[i],R.drawable.contact_icon));
		}
		setListAdapter(adapter);
		
	}
	void change(CharSequence ces)
	{
		//adapter.getFilter().filter(ces);
	}
	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		Log.d("sds","sdsd");
		MainActivity mv=(MainActivity) getActivity();
		mv.setContentView(R.layout.activity_main);
		Fragment newContent = new BirdGridFragment(position,conc[position],number[position]);
		if (newContent != null)
		switchFragment(newContent);
	}
private class SampleItem {
		
		public String tag;
		public int iconRes;
		public SampleItem(String tag, int iconRes) {
		this.tag = tag; 
		this.iconRes = iconRes;
		}
	}
  
	public class SampleAdapter extends ArrayAdapter<SampleItem> {
		public SampleAdapter(Context context) {
			super(context, 0);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.row, null);   
			}
			CheckBox cb=(CheckBox) convertView.findViewById(R.id.checkBox1);
			 ImageButton ib=(ImageButton)broadcastview.findViewById(R.id.imageButton1);
			if(isbroadcastmsg)
			{
			cb.setVisibility(1);
			ib.setVisibility(1);
			}
			ImageView icon = (ImageView) convertView.findViewById(R.id.row_icon);
			icon.setImageResource(getItem(position).iconRes);
			TextView title = (TextView) convertView.findViewById(R.id.row_title);
			title.setText(getItem(position).tag);
			return convertView;
		}
	}
	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;
		
		if (getActivity() instanceof MainActivity) {
			MainActivity ra = (MainActivity) getActivity();
			ra.switchContent(fragment);
		}
	}
	public void broadcastmsgset(SlidingMenu sm)
	{
		sm.setSlidingEnabled(false);
	}
}
