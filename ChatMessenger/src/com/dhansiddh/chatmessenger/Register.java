package com.dhansiddh.chatmessenger;

import static com.dhansiddh.chatmessenger.CommonUtilities.SENDER_ID;
import static com.dhansiddh.chatmessenger.CommonUtilities.SERVER_URL;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import android.R.integer;
import android.R.string;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.StaticLayout;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
public class Register extends Activity {
	private ProgressDialog pDialog;
	int i=0;
    AlertDialogManager alert = new AlertDialogManager();	
	JSONParser jsonParser = new JSONParser();
	EditText inputNumber;
    EditText inputUsername;
    EditText inputstatus;
    String mobileno;
    String email;
    int random;
    String countrycode;
    int reg=0;
	private static final String TAG_SUCCESS = "success";
	//final String url_code = "http://172.16.84.141/Chat/reg.php";
	final String url_code = "http://dhansiddh.netai.net/Chat/reg.php";
	SQLiteDatabase myDB= null;
    String TableName = "register";
    String TableName1 = "friends";
    String username;
    String status;
    int verify;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		inputNumber = (EditText) findViewById(R.id.editText1);
        inputUsername = (EditText) findViewById(R.id.EditText01);
        inputstatus = (EditText) findViewById(R.id.EditText02);
        Intent i1=getIntent();
        mobileno =i1.getStringExtra("number");
        random=i1.getIntExtra("randomnumber",10);
        countrycode=i1.getStringExtra("countrycode");
        myDB = this.openOrCreateDatabase("chatmessenger", MODE_PRIVATE, null);
		// Check if GCM configuration is set
		if (SERVER_URL == null || SENDER_ID == null || SERVER_URL.length() == 0
				|| SENDER_ID.length() == 0) {
			// GCM sernder id / server url is missing
			alert.showAlertDialog(Register.this, "Configuration Error!",
					"Please set your Server URL and GCM Sender ID", false);
			// stop executing code by return
			 return;
		}
		Button but=(Button) findViewById(R.id.button1);
		 
	but.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			username =inputUsername.getText().toString();
	         status = inputstatus.getText().toString();
	         if(valueentered())
	       {
	        verify =Integer.parseInt(inputNumber.getText().toString());
	          if(isOnline())
	        {
			if(random==verify)
            {
			new NewUser().execute();
            }
			else
			{
				Toast.makeText(getBaseContext(), 
	                    "Please enter correct Verification Code.", 
	                    Toast.LENGTH_SHORT).show();
			}}
	         else
	         {
	        	 Toast.makeText(getBaseContext(), 
		                    "Internet Connection is Required!!!!.", 
		                    Toast.LENGTH_SHORT).show();
	         }}
		}

		private boolean valueentered() {
			// TODO Auto-generated method stub
			if(inputNumber.getText().toString().equals(""))
			{
				 Toast.makeText(getBaseContext(), 
		                    "Enter verification Code!!!!.", 
		                    Toast.LENGTH_SHORT).show();
				return false;
			}
			else
			{
				if(inputUsername.getText().toString().equals(""))
				{
					 Toast.makeText(getBaseContext(), 
			                    "Enter UserName!!!!.", 
			                    Toast.LENGTH_SHORT).show();
					return false;
			}
				if(inputstatus.getText().toString().equals(""))
				{
					 Toast.makeText(getBaseContext(), 
			                    "Enter Your Status!!!!.", 
			                    Toast.LENGTH_SHORT).show();
					return false;}}
				return true;
		}
	});
	}
	class NewUser extends AsyncTask<String, String, String> {
		 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            
            pDialog = new ProgressDialog(Register.this);
            pDialog.setMessage("Creating Account..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Creating product
         * */
       
        protected String doInBackground(String... args) {
           
        	email=username+"@s.dhansiddh.com";
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", username));
            params.add(new BasicNameValuePair("mobileno", mobileno));
            params.add(new BasicNameValuePair("status", status));
            JSONObject json = jsonParser.makeHttpRequest(url_code,"POST", params);
            Log.d("success1","Success Acheived1");
           Log.d("Create Response", json.toString());
            try {
                int success = json.getInt(TAG_SUCCESS);
 
                if (success == 1) {
                	Log.d("success","Success Acheived");
                	myDB.execSQL("INSERT INTO "+TableName+" (register,mobileno,username,email,regid) VALUES('TRUE','"+mobileno+"','"+username+"','"+email+"','null');");
                } else {
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
            Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
         	String[] projection    = new String[] {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,ContactsContract.CommonDataKinds.Phone.NUMBER};
         	Cursor people = getContentResolver().query(uri, projection, null, null, null);
         	int indexName = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
         	int indexNumber = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
         	people.moveToFirst();
         	do {
         		int len,num;
         	    String name   = people.getString(indexName);
         	    String number = people.getString(indexNumber);
         	    number=number.replaceAll("[-_+.^:,*/|%&@$!]","");
    	    	number=number.replaceAll(" ","");
         	    len=number.length();
         	    if(len>=10)
         	    {   
         	    if(len<12)
         	    {
         	    	char c[]=number.toCharArray();
         	    	char c1[]=new char[len-1];
          	       if(c[0]=='0')
          	    	 {
          	    	 for(int i=0;i<len-1;i++)
          	          {
          	         c[i]=c[i+1];
          	         c1[i]=c[i];
          	          }
          	        len=len-1;
          	    	number=new String(c1);
          	    	 }
         	    	number=countrycode+number;
         	    }
         	   String email=name+"@s.dhansiddh.com";
         	   i++;
         	   myDB.execSQL("INSERT INTO "+TableName1+" (id,name,mobileno,isregistered,username,email) VALUES("+i+",'"+name+"','"+number+"',1,'"+name+"','"+email+"');");
         	   }} while (people.moveToNext());
        		myDB.close();
         	Intent i = new Intent(Register.this, MainActivity.class);
  		    startActivity(i);
  		    Register.this.finish();
    }}
	
        
        	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_register, menu);
		return true;
	}
	 public boolean isOnline() {
  	    ConnectivityManager cm =(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
  	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
  	    if (netInfo != null && netInfo.isConnected()) {
  	        return true;
  	    }
  	    return false;
  	}
	}
