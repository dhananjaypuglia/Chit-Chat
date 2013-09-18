package com.dhansiddh.chatmessenger;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

public class MainPage extends Activity {
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	                     WindowManager.LayoutParams.FLAG_FULLSCREEN);
	setContentView(R.layout.welcome);
SQLiteDatabase myDB= null;

String TableName = "register";
String TableName1 = "friends";
String TableName2 = "messages";
myDB = this.openOrCreateDatabase("chatmessenger", MODE_PRIVATE, null);
myDB.execSQL("CREATE TABLE IF NOT EXISTS " +TableName+ " (register VARCHAR,mobileno VARCHAR(12),username VARCHAR(30),email VARCHAR(50),regid VARCHAR(300));");
myDB.execSQL("CREATE TABLE IF NOT EXISTS " +TableName1+ " (id integer NOT_NULL AUTO_INCREAMENT,name VARCHAR(30),mobileno VARCHAR(12),isregistered INT(10),username VARCHAR(30),email VARCHAR(50),userid INT(10));");
myDB.execSQL("CREATE TABLE IF NOT EXISTS " +TableName2+ " (name VARCHAR(30),mobileno VARCHAR(12),message VARCHAR(500),chatid INT(10),userid INT(10),timestamp date DEFAULT CURRENT_DATE);");
Cursor c = myDB.rawQuery("SELECT COUNT(*) FROM " + TableName , null);
c.moveToFirst();
if(c!=null)
{
	c.moveToFirst();
	if (c.getInt (0) == 0) {
		 int secondsDelayed = 1;
         new Handler().postDelayed(new Runnable() {
                 public void run() {
                	
                	  Intent i=new Intent(MainPage.this,Register_Number.class);
       			   startActivity(i);
       			MainPage.this.finish();
                 }
         }, secondsDelayed * 1000);
         if (myDB != null)
	    		myDB.close();
			
	}
	else
	{int secondsDelayed = 1;
		new Handler().postDelayed(new Runnable() {
            public void run() {
            	Intent i=new Intent(MainPage.this,MainActivity.class);
            	startActivity(i);
            	MainPage.this.finish();
            }
    }, secondsDelayed * 1000);
	
	if (myDB != null)
		myDB.close();
	
	}
	}
}
}