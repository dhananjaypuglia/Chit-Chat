package com.dhansiddh.chatmessenger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register_Number extends Activity{
	EditText inputNumber;
	EditText countrycode;
	@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.registernumber);
	Button b=(Button) findViewById(R.id.button10);
	inputNumber = (EditText) findViewById(R.id.editText10);
	countrycode=(EditText) findViewById(R.id.countrycode);
	b.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			int randomNumber = ( int )( Math.random() * 9999 );

			if( randomNumber <= 1000 ) {
			    randomNumber = randomNumber + 1000;}
			String message="Your Chat Messanger Verification Code is "+randomNumber;
			if (inputNumber.length()>=10&&countrycode.length()==2)                
			{
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(inputNumber.getText().toString(), null, message, null, null);
			Intent i=new Intent(Register_Number.this, Register.class);
			Log.d("sdsf",message);
			String number=countrycode.getText().toString()+inputNumber.getText().toString();
			i.putExtra("number", number);
			i.putExtra("countrycode", countrycode.getText().toString());
			i.putExtra("randomnumber",randomNumber);
			startActivity(i);
			Register_Number.this.finish();
			}               
            else
            {  Toast.makeText(getBaseContext(), 
                    "Please enter correct phone number.", 
                    Toast.LENGTH_SHORT).show();
            }
					
		}
	});
}
}
