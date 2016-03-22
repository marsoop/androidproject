package org.androidpn.demoapp.activity;

import org.androidpn.client.Constants;
import org.androidpn.client.ServiceManager;
import org.androidpn.demoapp.MainActivity;
import org.androidpn.demoapp.R;
import org.androidpn.demoapp.manager.MessageManager;
import org.androidpn.demoapp.utils.DialogUtil;
import org.androidpn.demoapp.utils.PreferencesUtils;
import org.androidpn.demoapp.utils.T;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends BaseActivity implements OnClickListener {
	private Button mLogin;
	private EditText account, password;
	private String username;// 用户名
	private String pwd;// 密码
	private Dialog mLoginDialog;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
				
		initView();

	}

	private void initData() {
		String login_name = PreferencesUtils.getSharePreStr(context,
				Constants.XMPP_USERNAME);
		System.out.println("---------------" + login_name);
		String login_pwd = PreferencesUtils.getSharePreStr(context,
				Constants.XMPP_PASSWORD);
		// 进入主界面

		if (!TextUtils.isEmpty(login_name) && !TextUtils.isEmpty(login_pwd)) {
			account.setText(login_pwd);
			password.setText(login_pwd);
			/*
			 * //获取通讯录数据 Intent intent=new Intent(this,DemoAppActivity.class);
			 * startActivity(intent);
			 */
		}
		//获取通讯录
		MessageManager.getInstance(getApplicationContext()).getAddressList(getApplicationContext());

	}

	private void initView() {

		mLogin = (Button) findViewById(R.id.login);
		mLogin.setOnClickListener(this);
		account = (EditText) findViewById(R.id.account);
		password = (EditText) findViewById(R.id.password);

		mLoginDialog = DialogUtil.getLoginDialog(this);
		mLoginDialog.setCancelable(true);
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initData();
	
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.login:
			doLogin();
			break;

		default:
			break;
		}

	}

	private void doLogin() {
		username = account.getText().toString();// 用户名
		pwd = password.getText().toString();// 密码
		if (TextUtils.isEmpty(username)) {
			T.showShort(context, "请输入您的账号");
			return;
		}
		if (TextUtils.isEmpty(pwd)) {
			T.showShort(context, "请输入您的密码");

			return;
		}
		
		if (mLoginDialog != null && !mLoginDialog.isShowing())
			mLoginDialog.show();
		
		// 先存储
		PreferencesUtils.putSharePre(context, Constants.LOGIN_USERNAME, username);
		PreferencesUtils.putSharePre(context, Constants.LOGIN_PASSWORD, pwd);
		

		
		       // Start the service 初始化服务类，
				ServiceManager serviceManager = new ServiceManager(this);
				// 消息图标
				serviceManager.setNotificationIcon(R.drawable.notification);
				serviceManager.startService();
			
				
				
       new Handler().postDelayed(new Runnable() {
		
		@Override
		public void run() {
			if (mLoginDialog != null && mLoginDialog.isShowing())
				mLoginDialog.dismiss();
			Intent intent = new Intent(LoginActivity.this, MainActivity.class);
			startActivity(intent);
			
		}
	 }, 5000);
       
		

	}
}
