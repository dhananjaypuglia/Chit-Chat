package com.dhansiddh.chatmessenger;

import static com.dhansiddh.chatmessenger.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.dhansiddh.chatmessenger.CommonUtilities.EXTRA_NUMBER;
import static com.dhansiddh.chatmessenger.CommonUtilities.SENDER_ID;
import static com.dhansiddh.chatmessenger.CommonUtilities.TAG;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gcm.GCMRegistrar;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;


public class MainActivity extends SlidingFragmentActivity  {
	
	public String[] contact()
	{
		return contact;
	}
    String recv,ms;
	int id=0,flag=0;
	ListView listv;
	int back=0;
	BirdMenuFragment bm,bm1;
	int mainchatid[];
	int msglength=0;
	String regId;
	TextView lblMessage;
	TextView senmbno;
	String message[];
	int userid[];
	int len=0;
	SQLiteDatabase myDB= null;
    String TableName = "register";
    String TableName1 = "friends";
    String TableName2 = "messages";
	String contact[]=new String[2000];
	int length=0;
	MessageAdapter adapter;
	ListView msglist=null;
	ListView lv ;
	MessageMainListAdapter mainadap;
	SampleListFragment sf;
	private Fragment mContent;
    final String url_code = "http://dhansiddh.netai.net/Chat/gcm_server_php/send_message.php";
    final String update_url_code = "http://dhansiddh.netai.net/Chat/gcm_server_php/update.php";
	// Asyntask
	AsyncTask<Void, Void, Void> mRegisterTask;
	Context mycontext;
	// Alert dialog manager
	AlertDialogManager alert = new AlertDialogManager();
	
	// Connection detector
	ConnectionDetector cd;
	EditText msbox;
    EditText recvmbnum;
    String a[]=new String[3000];
	public static String name;
	public static String email;
	public static String sendmbnum;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messagemain);
		myDB = this.openOrCreateDatabase("chatmessenger", MODE_PRIVATE, null);
		Cursor c1=myDB.rawQuery("SELECT COUNT(*) FROM " + TableName1,null);
		c1.moveToFirst();
		if (c1!=null && c1.getInt(0)!=0)
		{
		c1 = myDB.rawQuery("SELECT name,mobileno FROM " + TableName1, null);
		   c1.moveToFirst();
		   if(c1!=null)
		   { c1.moveToFirst();    
			do{	 	
		   	 contact[length]=c1.getString(0);
		   	 a[length]=c1.getString(1);
		   	 length++;
		   }while(c1.moveToNext());
		   	}
		}
		   getSlidingMenu().setMode(SlidingMenu.LEFT_RIGHT);
		   if (findViewById(R.id.menu_frame) == null) {
				setBehindContentView(R.layout.menu_frame);
				getSlidingMenu().setSlidingEnabled(true);
				getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
				boolean hasActionBar = android.os.Build.VERSION.SDK_INT >= 11;
				if (hasActionBar) {
				getActionBar().setDisplayHomeAsUpEnabled(true);
				}
			} else {
				// add a dummy view
				View v = new View(this);
				setBehindContentView(v);
				getSlidingMenu().setSlidingEnabled(false);
				getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
			}
		   Cursor c = myDB.rawQuery("SELECT username,email,mobileno FROM " + TableName , null);
			c.moveToFirst();
			if(c!=null)
			{
				c.moveToFirst();
				name=c.getString(0);
				email=c.getString(1);
				sendmbnum=c.getString(2);
				Log.d("num", sendmbnum);
					if (myDB != null)
			    		myDB.close();
				}
			sf=new SampleListFragment(name);
		   getSlidingMenu().setSecondaryMenu(R.layout.menu_frame_two);
			getSlidingMenu().setSecondaryShadowDrawable(R.drawable.shadowright);
			getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.menu_frame_two, sf)
			.commit();
		   if (savedInstanceState != null)
				mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
		 	
		   bm=new BirdMenuFragment(contact,length,a,false);
		   bm1=new BirdMenuFragment(contact,length,a,true);
		   getSupportFragmentManager()
		 	.beginTransaction()
			.replace(R.id.menu_frame,bm)
			.commit();
          
		   SlidingMenu sm = getSlidingMenu();
			sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
			sm.setShadowWidthRes(R.dimen.shadow_width);
			sm.setShadowDrawable(R.drawable.shadow);
			sm.setBehindScrollScale(0.25f);
			sm.setFadeDegree(0.25f);
			msglist=(ListView) findViewById(R.id.messagelist);
			setmessagemainadap();
		cd = new ConnectionDetector(getApplicationContext());
		if (!cd.isConnectingToInternet()) {
			
			
			alert.showAlertDialog(MainActivity.this,
					"Internet Connection Error",
					"Please connect to working Internet connection", false);
			return;
		}
		
        GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		registerReceiver(mHandleMessageReceiver, new IntentFilter(
				DISPLAY_MESSAGE_ACTION));
		regId = GCMRegistrar.getRegistrationId(this);
		if (regId.equals("")) {
			GCMRegistrar.register(this,SENDER_ID);
		} else {
			if (GCMRegistrar.isRegisteredOnServer(this)) {
				new updategsm().execute();
			/**	myDB = this.openOrCreateDatabase("chatmessenger", MODE_PRIVATE, null);
				Cursor c6 = myDB.rawQuery("SELECT regid FROM " +TableName , null);
				c6.moveToFirst();
				if(c!=null)
				{
					c6.moveToFirst();
					regId = GCMRegistrar.getRegistrationId(this);
					if(c6.getString(0).equals(regId)!=true)
				       {
				       new updategsm().execute();
				       myDB.execSQL("UPDATE "+TableName+" SET regid='"+regId+"' WHERE username='"+name+"'");						
				       }
				       }
				myDB.close();**/
			} else {
				final Context context = this;
				mRegisterTask = new AsyncTask<Void, Void, Void>() {
					@Override
					protected Void doInBackground(Void... params) {
						ServerUtilities.register(context, name, email, regId,sendmbnum);
						return null;
					}
					@Override
					protected void onPostExecute(Void result) {
						mRegisterTask = null;
						//new updategsm().execute();
					}
				};
				mRegisterTask.execute(null, null, null);
			}
		}
	}
	public int[] getuserid(String name)
	{
		int[] useris=new int[msglength];
		myDB = MainActivity.this.openOrCreateDatabase("chatmessenger", MODE_PRIVATE, null);
    	Cursor c2 = myDB.rawQuery("SELECT id FROM "+TableName1+" WHERE mobileno="+name, null);
    	 c2.moveToFirst();
    		int id=0;
    		if(c2!=null)
    		{
    			c2.moveToFirst();
    			id=c2.getInt(0);
    		}
    		try{
    			Cursor c3 = myDB.rawQuery("SELECT userid FROM " + TableName2+" WHERE chatid="+id+" ORDER BY timestamp ASC", null);
	    		c3.moveToFirst();
	    		int i=0;
	    		if(c3!=null)
	    		{
	    			c3.moveToFirst();
	    		do{
	    		    useris[i]=c3.getInt(0);
	    	        i++;
	    		}while(c3.moveToNext()==true);
	    		}
       		}catch (Exception e) {
       			Log.d("msg","Exception");
   			}
    		if (myDB != null)
	    		myDB.close();
    		return useris;
	}
	public void setmessagemainadap()
	{  String messagemain[]=null;
	   String maincontact[]=null;
		lv =(ListView) findViewById(R.id.messagemainlist);
		myDB = this.openOrCreateDatabase("chatmessenger", MODE_PRIVATE, null);
		int conlength=0;
		Cursor c1=myDB.rawQuery("SELECT COUNT(DISTINCT(chatid)) FROM "+TableName2+" ORDER BY timestamp DESC", null);
		c1.moveToFirst();
		if(c1!=null && c1.getInt(0)!=0)
		   {c1.moveToFirst();
		   String c="%12345";
			messagemain=new String[c1.getInt(0)];
		    maincontact=new String[c1.getInt(0)];
		    mainchatid=new int[c1.getInt(0)];
		    Log.d("tag",Integer.toString(c1.getInt(0)));
		  Cursor c5=myDB.rawQuery("SELECT DISTINCT(chatid) FROM "+TableName2+" ORDER BY timestamp DESC", null);
		 c5.moveToFirst();
		   if(c5!=null)
		   {  c5.moveToFirst();
		      do{	 	
			   	Cursor c6=myDB.rawQuery("SELECT message FROM "+TableName2+" WHERE chatid="+c5.getInt(0)+" ORDER BY timestamp DESC", null);
			   	c6.moveToFirst();
			   	mainchatid[conlength]=c5.getInt(0);
			   	messagemain[conlength]=c6.getString(0);
			   	messagemain[conlength]=messagemain[conlength].replaceAll(c,"'");
			    c6=myDB.rawQuery("SELECT name FROM " +TableName1+" WHERE id="+c5.getInt(0), null);
			    c6.moveToFirst();
			    maincontact[conlength]=c6.getString(0);
			    conlength++;
		   }while(c5.moveToNext());
		   mainadap=new MessageMainListAdapter(MainActivity.this,R.layout.messagemain,messagemain,maincontact);
		   lv.setDivider(MainActivity.this.getResources().getDrawable(R.drawable.transperent_color));
			lv.setAdapter(mainadap);
		   }}
		   myDB.close();
		 lv.setOnItemClickListener(new OnItemClickListener() {
			 @Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
					MainActivity.this.setContentView(R.layout.activity_main);
					myDB = MainActivity.this.openOrCreateDatabase("chatmessenger", MODE_PRIVATE, null);
			    	Cursor c2 = myDB.rawQuery("SELECT mobileno,name FROM "+TableName1+" WHERE id="+mainchatid[position], null);
			    	 c2.moveToFirst();
			    		String number=null;
			    		String name=null;
			    		if(c2!=null)
			    		{
			    			c2.moveToFirst();
			    			 number=c2.getString(0);
			    			 name=c2.getString(1);
			    		}
					Fragment newContent = new BirdGridFragment(position,name,number);
					if (newContent != null)
					switchContent(newContent);
					myDB.close();
			}
		});
	}
	public int getlength(String name)
	{
		myDB = MainActivity.this.openOrCreateDatabase("chatmessenger", MODE_PRIVATE, null);
    	Cursor c2 = myDB.rawQuery("SELECT id FROM "+TableName1+" WHERE mobileno="+name, null);
    	 c2.moveToFirst();
    		int id=0;
    		if(c2!=null)
    		{
    			c2.moveToFirst();
    			id=c2.getInt(0);
    		}
    		try{
       		 Cursor c3 = myDB.rawQuery("SELECT COUNT(message) FROM " + TableName2+" WHERE chatid="+id+" ORDER BY timestamp ASC", null);
   	    		c3.moveToFirst();
   	    		int i=0;
   	    		if(c3!=null)
   	    		{
   	    			c3.moveToFirst();
   	    			msglength=c3.getInt(0);
   	    		}
       		}catch (Exception e) {
       			Log.d("msg","Exception");
   			}
    		if (myDB != null)
	    		myDB.close();
    		return msglength;
	}
	public String[] getmessage(String name)
    {
    	myDB = MainActivity.this.openOrCreateDatabase("chatmessenger", MODE_PRIVATE, null);
    	Cursor c2 = myDB.rawQuery("SELECT id FROM "+TableName1+" WHERE mobileno="+name, null);
    	 String message[] = null;
    	 int length = 0;
    	 Log.d("getmsg",name);
    	 c2.moveToFirst();
    		int id=0;
    		if(c2!=null)
    		{
    			c2.moveToFirst();
    			id=c2.getInt(0);
    		}
    		if(msglength!=0)
    		{
    		try{
    		 Cursor c3 = myDB.rawQuery("SELECT message FROM " + TableName2+" WHERE chatid="+id+" ORDER BY timestamp ASC", null);
	    		c3.moveToFirst();
	    		int i=0;
	    		if(c3!=null)
	    		{
	    			c3.moveToFirst();
	    			message=new String[msglength];
	    		do{
	    			message[i]=c3.getString(0);
	    			String c="%12345";
	    	        message[i]=message[i].replaceAll(c,"'");
	    	        i++;
	    		}while(c3.moveToNext()==true);
	    		}
    		}catch (Exception e) {
    			Log.d("msg","Exception");
			}}
    		if (myDB != null)
	    		myDB.close();
			return message;    	
    }
	public void switchContent(final Fragment fragment) {
		back=1;
		mContent = fragment;
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, fragment)
		.commit();
		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			public void run() {
				getSlidingMenu().showContent();
			}
		}, 50);
	}
	public void switchmenucontent()
	{
		MainActivity.this.setContentView(R.layout.messagemain);
		setmessagemainadap();
		boolean hasActionBar = android.os.Build.VERSION.SDK_INT >= 11;
		if (hasActionBar) {
		ActionBar abActionBar = MainActivity.this.getActionBar();
		abActionBar.setTitle("Home");
		abActionBar.setSubtitle("");
		}
		back=0;
		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			public void run() {
				getSlidingMenu().showContent();
			}
		}, 50);
	}
	public void switchbroadcast()
	{
		getSupportFragmentManager()
	 	.beginTransaction()
		.replace(R.id.menu_frame,bm)
		.commit();
	Handler h = new Handler();
	h.postDelayed(new Runnable() {
		public void run() {
			getSlidingMenu().showContent();
		}
	}, 50);
	}
	public void setbroadcastcontent()
	{
		 getSupportFragmentManager()
		 	.beginTransaction()
			.replace(R.id.menu_frame,bm1)
			.commit();
		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			public void run() {
				getSlidingMenu().showMenu();
			}
		}, 50);
		SlidingMenu sm=getSlidingMenu();
		//bm.broadcastmsgset(sm);
		;
	}
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String sendermbno =intent.getExtras().getString(EXTRA_NUMBER);
			myDB = MainActivity.this.openOrCreateDatabase("chatmessenger", MODE_PRIVATE, null);
			   try{
			 Cursor c1 = myDB.rawQuery("SELECT name FROM friends WHERE mobileno=" + sendermbno , null);
	    	  c1.moveToFirst();
	    	  Log.d("broadcast","recieved");
	    	   if(c1!=null)
	    	   {
	    		   c1.moveToFirst();
	    		   name=c1.getString(0);
	    		   Log.d("number",c1.getString(0));
	    	   }
	    	   boolean hasActionBar = android.os.Build.VERSION.SDK_INT >= 11;
				if (hasActionBar) {  
				ActionBar ab=getActionBar();
	        	 if(ab.getTitle().toString().equals(name))
	    		  MainActivity.this.setadap(listv,sendermbno);
				}
				else
				{
					MainActivity.this.setadap(listv,sendermbno);
				}				
	    	  }catch (Exception e) {
				   Log.d("Exception","Error in broadcast");
			    }	
			   myDB.close();
		}
		
	};
	
	class sendmsg extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
        	flag=1;
            super.onPreExecute();
            myDB = MainActivity.this.openOrCreateDatabase("chatmessenger", MODE_PRIVATE, null);
            Cursor c2 = myDB.rawQuery("SELECT id FROM " + TableName1+" WHERE mobileno="+recv, null);
    		c2.moveToFirst();
    		int id=0;
    		if(c2!=null)
    		{
    			c2.moveToFirst();
    			id=c2.getInt(0);
    		}
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String strDate = sdf.format(new Date());
            ms=ms.replaceAll("\'","%12345");
            myDB.execSQL("INSERT INTO "+TableName2+" (name,mobileno,message,chatid,userid,timestamp) VALUES('"+name+"','"+sendmbnum+"','"+ms+"',"+id+",0,"+strDate+");");
            MainActivity.this.setadap(listv,recv);
	    		myDB.close();
        }
        protected String doInBackground(String... args) {
        	for (int i = 1; i <= 10; i++) {
           try{
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("sendmbno", sendmbnum));
            params.add(new BasicNameValuePair("message",ms));
            params.add(new BasicNameValuePair("recvmbno",recv));
            JSONParser.makeHttpRequest(url_code,"POST", params);
            Log.d("success1","Success Acheived1");
            break;
          
           }catch (Exception e) {
               
               Log.e(TAG, "Failed to send message " + e);}
        	}
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
        	// TODO Auto-generated method stub
        	super.onPostExecute(result);
        flag=0;
        }
        }
        class updategsm extends AsyncTask<String, String, String> {
   		 
            protected String doInBackground(String... args) {
            	
			        	List<NameValuePair> params = new ArrayList<NameValuePair>();
			            params.add(new BasicNameValuePair("name", name));
			            params.add(new BasicNameValuePair("email",email));
			            params.add(new BasicNameValuePair("regId",regId));
			            params.add(new BasicNameValuePair("mobile",sendmbnum));
			           JSONParser.makeHttpRequest(update_url_code,"POST", params);        	
                Log.d("update","update success");
                return null;
            }
    }
        @Override
        public boolean onKeyDown(int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
            	if(back==1)
            	{
            		MainActivity.this.setContentView(R.layout.messagemain);
            		setmessagemainadap();
            		boolean hasActionBar = android.os.Build.VERSION.SDK_INT >= 11;
            		if (hasActionBar) {
            		ActionBar abActionBar = MainActivity.this.getActionBar();
            		abActionBar.setTitle("Home");
            		abActionBar.setSubtitle("");
            		}
            		back=0;
            		return false;
            	}
            	else
            	{
            	if(flag==0)
            	MainActivity.this.finish();
            	else if(flag==1)
            	{
            	Intent startMain = new Intent(Intent.ACTION_MAIN);
            	startMain.addCategory(Intent.CATEGORY_HOME);
            	startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            	startActivity(startMain);}
                return true;
            	}
            	}
            return super.onKeyDown(keyCode, event);
        }
        public void setadap(ListView lv,String number)
        {    listv=lv;
        	len=MainActivity.this.getlength(number);
    		if(len!=0)
    		{
    		message=MainActivity.this.getmessage(number);
    		userid=MainActivity.this.getuserid(number);
        adapter=new MessageAdapter(MainActivity.this,R.layout.activity_main,message,userid);
       listv.setDivider(MainActivity.this.getResources().getDrawable(R.drawable.transperent_color));
        listv.setDividerHeight(8);
		listv.setAdapter(adapter);
		listv.setSelection(listv.getCount() - 1);
    		}
    		}
        public void senddmsg(String num,String mes)
        {   
        	recv=num;
        	ms=mes;
        	WakeLocker.acquire(getApplicationContext());
        	new sendmsg().execute();
        	WakeLocker.release();
        }
        @Override
    	protected void onDestroy() {
    		if (mRegisterTask != null) {
    			mRegisterTask.cancel(true);
    		}
    		try {
    			unregisterReceiver(mHandleMessageReceiver);
    			GCMRegistrar.onDestroy(this);
    		} catch (Exception e) {
    			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
    		}
    		super.onDestroy();
    	}
         	}
