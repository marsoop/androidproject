package org.androidpn.demoapp.activity;

import java.util.List;

import org.androidpn.client.Constants;
import org.androidpn.demoapp.R;
import org.androidpn.demoapp.adapter.RecentChatBaseAdapter;
import org.androidpn.demoapp.database.ChatMsgProvider;
import org.androidpn.demoapp.manager.MessageManager;
import org.androidpn.demoapp.model.ChatMsg;
import org.androidpn.demoapp.utils.DialogUtil;
import org.androidpn.demoapp.utils.PreferencesUtils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

@SuppressLint("HandlerLeak")
public class HomeFragment extends Fragment{
	String login_name;
	private ListView mSwipeListView;
	//聊天消息数据
	private List<ChatMsg> mData;
	/* 数据加载时候弹出的dialog */
	private Dialog dialog;
	private ReChatMsgObserver reChatMsgObserver;
	private ContentResolver reContentResolver;
	private RecentChatBaseAdapter recentChatBaseAdapter;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				dialog.dismiss();
				break;
			case 1:
				dialog.dismiss();
				if(mData.size()>0){
					recentChatBaseAdapter = new RecentChatBaseAdapter(mData, getActivity());
					mSwipeListView.setAdapter(recentChatBaseAdapter);
					
				}
				
				break;
			default:
				break;
			}
			
		};
	};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view =inflater.inflate(R.layout.fragment_home, container,false);
		
		mSwipeListView  = (ListView) view.findViewById(R.id.id_recent_listview);
		mSwipeListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String str = ((ChatMsg)HomeFragment.this.recentChatBaseAdapter.getItem(arg2)).getMsgto();
		         Intent localIntent = new Intent(HomeFragment.this.getActivity(), ChatActivity.class);
		          if (!TextUtils.isEmpty(str))
		          {
		            localIntent.putExtra("chatUserName", str);
		            startActivity(localIntent);
		          }
				
			}
		});
		
		return view;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		reChatMsgObserver = new ReChatMsgObserver(handler);
		
		reContentResolver = getActivity().getContentResolver();
		dialog  = DialogUtil.getLoginDialog(getActivity(),"正在加载数据");
		dialog.show();
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	
		reContentResolver.registerContentObserver(ChatMsgProvider.MSG_URI, true, reChatMsgObserver);
	    login_name = PreferencesUtils.getSharePreStr(getActivity(),
				Constants.XMPP_USERNAME);
		initData();
		
	}

	private void initData() {
		
		
		new Thread(new Runnable()
		{
			@Override
			public void run()
			
			{
				mData = MessageManager.getInstance(getActivity()).getMsgGroupByUser(getActivity(),login_name);
				// 数据查询成功，对话框消失，显示数据
				
				if(mData.size()==0){
					handler.sendEmptyMessage(0);
					}else{
						handler.sendEmptyMessage(1);
					}
				
				
				
			}
		}).start();
	}

	
	private class ReChatMsgObserver extends ContentObserver{

		public ReChatMsgObserver(Handler handler) {
			super(handler);
			
		}
		@Override
		public void onChange(boolean selfChange) {
			// TODO Auto-generated method stub
			super.onChange(selfChange);
			initData();
			handler.sendEmptyMessage(1);
			
			
		}
		
	}
	
	
}
