package org.androidpn.demoapp.broadcast;

import org.androidpn.client.Constants;
import org.androidpn.client.ServiceManager;
import org.androidpn.demoapp.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
/**
 * 开机自启动
 * @author dourl
 *
 */
public class BootCompleteReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent arg1) {
		SharedPreferences pref = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
       if(pref.getBoolean(Constants.SETTINGS_TOAST_ENABLED, true)){
    	   // Start the service
           ServiceManager serviceManager = new ServiceManager(context);
           serviceManager.setNotificationIcon(R.drawable.notification);
           serviceManager.startService();
       }
	}

}
