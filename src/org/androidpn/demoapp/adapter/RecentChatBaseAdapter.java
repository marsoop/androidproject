package org.androidpn.demoapp.adapter;

import java.util.List;

import org.androidpn.demoapp.R;
import org.androidpn.demoapp.manager.MessageManager;
import org.androidpn.demoapp.model.ChatMsg;
import org.androidpn.demoapp.utils.ExpressionUtil;
import org.androidpn.demoapp.utils.TimeUtil;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RecentChatBaseAdapter extends BaseAdapter{
	public List<ChatMsg> list;
    public Context context;
    
    public LayoutInflater layoutInflater;
    
	public RecentChatBaseAdapter(List<ChatMsg> list, Context context) {
		super();
		this.list = list;
		this.context = context;
		layoutInflater =LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		 ViewHolder holder = null;
		 int i = 0;
		 if(convertView == null){
			 holder = new ViewHolder();
			 convertView = layoutInflater.inflate(R.layout.fragment_home_listview_item, null);
		     holder.dateTv = (TextView) convertView.findViewById(R.id.recent_list_item_time);
		     holder.contentTv = (TextView) convertView.findViewById(R.id.recent_list_item_msg);
             holder.nameTv = (TextView) convertView.findViewById(R.id.recent_list_item_name);
		     holder.unReadTv = (TextView) convertView.findViewById(R.id.recent_unreadmsg);
		     holder.headerView  = (ImageView) convertView.findViewById(R.id.recent_list_item_header);
		     convertView.setTag(holder);
		 }else{
			 holder =(ViewHolder) convertView.getTag();
		 }
		 
		 //设值 内容文本
		 if(list.get(position).getMsgtype().equals(ChatMsg.TYPE[0])){
			  SpannableStringBuilder ssb = ExpressionUtil.prase(context, holder.contentTv, list.get(position).getMsgbody());
			  holder.contentTv.setText(ssb);
			  //Linkify.addLinks(holder.contentTv,Linkify.ALL);
		  }
		 
		 
		 //聊天对象
		 holder.nameTv.setText(list.get(position).getMsgto());
		 //聊天时间
		 holder.dateTv.setText(TimeUtil.getChatTime(Long.parseLong(list.get(position).getMsgdate())));
		 //未读便签
		 if(list.get(position).getIsreaded()==0){
			 holder.unReadTv.setVisibility(View.VISIBLE);
			 
			 i = MessageManager.getInstance(this.context).getUnReadMsgCount(this.context,list.get(position).getMsgfrom(), list.get(position).getMsgto(), "0");
		    if(i>0){
		    	holder.unReadTv.setText(String.valueOf(i));
		    }
		 
		 }else{
			   holder.unReadTv.setVisibility(View.INVISIBLE); 
		   }
		 
		 
		 
		 return convertView;
	}
	
	
	public final class ViewHolder {
		 public ImageView  headerView;
		 public TextView   dateTv,contentTv,unReadTv,nameTv;
		

	 }

}
