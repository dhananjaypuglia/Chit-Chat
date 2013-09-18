package com.dhansiddh.chatmessenger;

import android.annotation.SuppressLint;
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

@SuppressLint("NewApi")
public class MessageAdapter extends ArrayAdapter<String>
{Context mycont;
String item[];
int user[];
int length;
TextView title;
int flag=0;
android.view.ViewGroup.LayoutParams lp;
  public MessageAdapter(Context context, int textViewResourceId,
			String[] objects,int[] userid) {
		super(context, textViewResourceId, objects);
		this.mycont=context;
		this.item=objects;
		this.user=userid;
		length=item.length;
		// TODO Auto-generated constructor stub
	}
	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mycont).inflate(R.layout.messagerow, null);
		}
		LinearLayout ll=(LinearLayout) convertView;
		RelativeLayout rl=(RelativeLayout) convertView.findViewById(R.id.message_relative);
		title = (TextView) convertView.findViewById(R.id.message);
		title.setText(item[position]);
		lp=rl.getLayoutParams();
		  if((int)item[position].length()/35==0)
		     {float scale = getContext().getResources().getDisplayMetrics().density;
				int pix = (int) (48* scale + 0.5f);
			  lp.height=pix;
		     }
		      else
			  lp.height=LayoutParams.MATCH_PARENT;
		       title.setMaxWidth(450);
		if(user[position]==0)
		{
		   rl.setBackgroundResource(R.drawable.balloon_incoming_normal);
		   ll.setGravity(Gravity.LEFT);
		}
		else
		{
			rl.setBackgroundResource(R.drawable.balloon_outgoing_normal);
		    ll.setGravity(Gravity.RIGHT);
		}
		rl.setLayoutParams(lp);
		/**ImageView iv=(ImageView) convertView.findViewById(R.id.messageprogress);
		iv.setImageResource(R.drawable.contact_icon);**/
		return convertView;
	}
	
}
