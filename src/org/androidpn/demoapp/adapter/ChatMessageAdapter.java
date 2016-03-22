package org.androidpn.demoapp.adapter;

import java.util.List;

import org.androidpn.demoapp.R;
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
import android.widget.ProgressBar;
import android.widget.TextView;

public class ChatMessageAdapter extends BaseAdapter {
	public List<ChatMsg> list;
    public Context context;
    public LayoutInflater layoutInflater;

	public ChatMessageAdapter(List<ChatMsg> list, Context context) {
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
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		ChatMsg m = list.get(position);
		return m.getIscoming();
	}
	
	@Override
	public int getViewTypeCount() {
		
		return 2;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		  ViewHolder holder = null;
		  if(convertView == null){
			  //0 表示进来的
			  if(getItemViewType(position)==0){
				  holder = new ViewHolder();
				  convertView = layoutInflater.inflate(R.layout.chat_message_itermin, null);
				  //时间
				  holder.dateTv= (TextView) convertView.findViewById(R.id.chatin_time);
				  //头像
				  holder.header= (ImageView) convertView.findViewById(R.id.chatin_header);
				  //文字
				  holder.contentTv= (TextView) convertView.findViewById(R.id.chatin_content);
				  //图片
				  holder.imgView = (ImageView) convertView.findViewById(R.id.chatin_img);
				  //定位
				  holder.locImgView=(ImageView) convertView.findViewById(R.id.chatin_location);
			  }else{
				  holder = new ViewHolder();
				  convertView = layoutInflater.inflate(R.layout.chat_message_itermout, null);
				  //时间
				  holder.dateTv = (TextView) convertView.findViewById(R.id.chatout_time);
				  holder.header= (ImageView) convertView.findViewById(R.id.chatout_header);
				  holder.contentTv= (TextView) convertView.findViewById(R.id.chatout_content);
				  
				  holder.imgView = (ImageView) convertView.findViewById(R.id.chatout_img);
				  holder.locImgView=(ImageView) convertView.findViewById(R.id.chatin_location);
			  }
			  convertView.setTag(holder);
			  
		  }else{
			  holder =(ViewHolder) convertView.getTag();
		  }
		  //设值
		  if(list.get(position).getMsgtype().equals(ChatMsg.TYPE[0])){
			  SpannableStringBuilder ssb = ExpressionUtil.prase(context, holder.contentTv, list.get(position).getMsgbody());
			  holder.contentTv.setText(ssb);
			 // Linkify.addLinks(holder.contentTv,Linkify.ALL);
		  }
		  //公共样式的时间格式
		  holder.dateTv.setText(TimeUtil.getChatTime(Long.parseLong(list.get(position).getMsgdate())));
		 

	      return convertView;
	}

	
	 public final class ViewHolder {
		 public ImageView  header,imgView,locImgView;
		 public TextView   dateTv,contentTv;
		 public ProgressBar pb;

	 }
}
