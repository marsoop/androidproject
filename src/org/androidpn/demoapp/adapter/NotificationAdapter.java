package org.androidpn.demoapp.adapter;

import java.util.List;

import org.androidpn.demoapp.R;
import org.androidpn.demoapp.model.NotificationHistory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NotificationAdapter extends ArrayAdapter<NotificationHistory> {

	public NotificationAdapter(Context context, int resource,
			List<NotificationHistory> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		NotificationHistory notificationHistory =getItem(position);	
		View view;
		if(convertView == null){
			view = LayoutInflater.from(getContext()).inflate(R.layout.notification_item, null);
		}else{
			view=convertView;
		}
		TextView titleTv = (TextView) view.findViewById(R.id.tv_notification_title);
		TextView timeTv = (TextView) view.findViewById(R.id.tv_notification_createTime);
		
		titleTv.setText(notificationHistory.getTitle());
		timeTv.setText(notificationHistory.getTime());
		return view;
	}

	

}
