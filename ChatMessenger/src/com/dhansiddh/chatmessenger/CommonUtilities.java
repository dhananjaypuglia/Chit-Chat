package com.dhansiddh.chatmessenger;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public final class CommonUtilities {
    static final String SERVER_URL = "http://dhansiddh.netai.net/Chat/gcm_server_php/register.php"; 

    // Google project id
    static final String SENDER_ID = "316826193296"; 
          
    /**
     * Tag used on log messages.
     */
    static final String TAG = "Chat Messenger GCM";

    static final String DISPLAY_MESSAGE_ACTION =
            "com.dhansiddh.chatmessenger.DISPLAY_MESSAGE";
    static final String EXTRA_MESSAGE = "message";
    static final String EXTRA_NUMBER = "number";

    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    static void displayMessage(Context context, String message,String numb) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_NUMBER, numb);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
}
