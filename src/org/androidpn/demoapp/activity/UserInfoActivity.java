package org.androidpn.demoapp.activity;

import org.androidpn.demoapp.R;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

public class UserInfoActivity extends BaseActivity {
	//获取聊天对象
   String chatUserName =null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_user_info);		
		Intent intent = getIntent();   
		//获取聊天对象
		chatUserName = intent.getStringExtra("toUserNamer"); 
		//可更具用户名获取详情
		Log.d("ChatActivity", "取聊天对象" + chatUserName);
	}

	// 点击进入聊天界面
	public void onChatClick(View view){
		
		Intent intent = new Intent(this,ChatActivity.class);
		if(TextUtils.isEmpty(chatUserName)){
			
		}else{
			intent.putExtra("chatUserName", chatUserName);
			
		}
		startActivity(intent);
	}
}
