package com.dhansiddh.chatmessenger;

import android.R.string;
import android.app.ListFragment;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MessageMainListAdapter extends ArrayAdapter<String>{
	String messagemain[];
	String maincontact[];
	Context mycont;
public MessageMainListAdapter(Context context, int textViewResourceId,String[] objects,String[] maincontacts) {
	super(context, textViewResourceId, objects);
	this.mycont=context;
	this.messagemain=objects;
	this.maincontact=maincontacts;
}
@Override
public View getView(int position, View convertView, ViewGroup parent) {
	if (convertView == null) {
		convertView = LayoutInflater.from(mycont).inflate(R.layout.messagemainrow, null);
	}
	RelativeLayout rl=(RelativeLayout) convertView;
	TextView contacttitle = (TextView) convertView.findViewById(R.id.concname);
	TextView message = (TextView) convertView.findViewById(R.id.smallmessage);
	contacttitle.setText(maincontact[position]);
	message.setText(messagemain[position]);
	return convertView;
}
}
