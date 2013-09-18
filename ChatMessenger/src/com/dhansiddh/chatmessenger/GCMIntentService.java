package com.dhansiddh.chatmessenger;

import static com.dhansiddh.chatmessenger.CommonUtilities.SENDER_ID;
import static com.dhansiddh.chatmessenger.CommonUtilities.displayMessage;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.ActionBar;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dhansiddh.chatmessenger.MainActivity.sendmsg;
import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {
	private static final String TAG = "GCMIntentService";
	 String TableName = "register";
	    String TableName1 = "friends";
	    String TableName2 = "messages";
    public GCMIntentService() {
    	super(SENDER_ID);
    }
    /**
     * Method called on device registered
     **/
    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i(TAG, "Device registered: regId = " + registrationId);
        displayMessage(context, "Your device registred with GCM","appplication");
        Log.d("NAME", MainActivity.name);
        ServerUtilities.register(context, MainActivity.name, MainActivity.email, registrationId,MainActivity.sendmbnum);
    }

    /**
     * Method called on device un registred
     * */
    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");
        displayMessage(context, getString(R.string.gcm_unregistered),"appplication");
        ServerUtilities.unregister(context, registrationId);
    }

    /**
     * Method called on Receiving a new message
     * */
    @Override
    protected void onMessage(Context context, Intent intent) {
        WakeLocker.acquire(getApplicationContext());
        String message = intent.getExtras().getString("chat_msg");
        String numb = intent.getExtras().getString("sender");
        displayMessage(context, message, numb);
        Log.d("Message recieved from",numb);
        String name=null;
        int id = 0;
        SQLiteDatabase myDB=context.openOrCreateDatabase("chatmessenger", MODE_PRIVATE, null);
        if(numb.equals("Chit Chat Web"))
        name="Chit Chat Web";
        else
        {
        try{
			  Cursor c1 = myDB.rawQuery("SELECT name FROM friends WHERE mobileno=" + numb , null);
	    	  c1.moveToFirst();
	    	   if(c1!=null)
	    	   {
	    		   c1.moveToFirst();
	    		   name=c1.getString(0);
	    	   }
	            Cursor c2 = myDB.rawQuery("SELECT id FROM " +TableName1+" WHERE mobileno="+numb, null);
	    		c2.moveToFirst();
	    		if(c2!=null)
	    		{
	    			c2.moveToFirst();
	    			id=c2.getInt(0);
	    		}
	            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	            String strDate = sdf.format(new Date());
	            myDB.execSQL("INSERT INTO "+TableName2+" (name,mobileno,message,chatid,userid,timestamp) VALUES('"+name+"','"+numb+"','"+message+"',"+id+","+id+","+strDate+");");
	            String c="%12345";
  	            message=message.replaceAll(c,"'");
  	            if (myDB != null)
		       myDB.close();
			   }catch (Exception e) {
				   name=numb;
				   Log.d("Exception","Error or webmessage");
				// TODO: handle exception
			    }
        }
        generateNotification(context,message,name); 
        WakeLocker.release();
    }
    /**
     * Method called on receiving a deleted message
     * */
    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
        String message = getString(R.string.gcm_deleted, total);
        displayMessage(context, message,"appplication");
        // notifies user
        generateNotification(context, message,"appl");
    }

    /**
     * Method called on Error
     * */
    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
        displayMessage(context, getString(R.string.gcm_error, errorId),"appplication");
    }
    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        Log.i(TAG, "Received recoverable error: " + errorId);
        displayMessage(context, getString(R.string.gcm_recoverable_error,
                errorId),"appplication");
        return super.onRecoverableError(context, errorId);
    }
    @SuppressWarnings("deprecation")
    private static void generateNotification(Context context, String message,String name) {
        int icon = R.drawable.ic_launcher;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(icon, message, when);
        Intent notificationIntent = new Intent(context, MainActivity.class);
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent =
                PendingIntent.getActivity(context, 0, notificationIntent, 0);
        notification.setLatestEventInfo(context, name, message, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        
        // Play default notification sound
        notification.defaults |= Notification.DEFAULT_SOUND;
        // Vibrate if vibrate is enabled
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(0, notification);      
    }

}
