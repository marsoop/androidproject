package org.androidpn.demoapp.activity;

import java.util.ArrayList;
import java.util.List;

import org.androidpn.client.Constants;
import org.androidpn.client.NotificationDetailsActivity;
import org.androidpn.demoapp.R;
import org.androidpn.demoapp.adapter.NotificationAdapter;
import org.androidpn.demoapp.model.NotificationHistory;
import org.litepal.crud.DataSupport;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class NotificationHistoryActivity extends Activity {
    private ListView mHistoryListView;
    private NotificationAdapter mNotificationAdapter;
    private List<NotificationHistory> mlist = new ArrayList<NotificationHistory>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification_history);
		
		mlist = DataSupport.findAll(NotificationHistory.class);
		mHistoryListView = (ListView) findViewById(R.id.notification_his_listview);
		mHistoryListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				NotificationHistory history = mlist.get(arg2);
				Intent intent = new Intent(NotificationHistoryActivity.this,NotificationDetailsActivity.class);
               
                intent.putExtra(Constants.NOTIFICATION_API_KEY, history.getApiKey());
                intent.putExtra(Constants.NOTIFICATION_TITLE,history.getTitle());
                intent.putExtra(Constants.NOTIFICATION_MESSAGE,history.getMessage());
                intent.putExtra(Constants.NOTIFICATION_URI, history.getUri());
                intent.putExtra(Constants.NOTIFICATION_IMAGE_URL, history.getImage_url());
				startActivity(intent);
			}
		});
		
		mNotificationAdapter = new NotificationAdapter(this, 0, mlist);
		mHistoryListView.setAdapter(mNotificationAdapter);
		
		registerForContextMenu(mHistoryListView);
		
		
		
		
	}
	
	

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, 0, 0, "Remove");
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if(item.getItemId()==0){
			AdapterContextMenuInfo menuInfo =(AdapterContextMenuInfo) item.getMenuInfo();
		int index = menuInfo.position;
		
		NotificationHistory history =mlist.get(index);
		history.delete();
		mlist.remove(index);
		mNotificationAdapter.notifyDataSetChanged();
		
		}
		return super.onContextItemSelected(item);
	}
	
}
