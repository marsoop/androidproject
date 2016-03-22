package org.androidpn.demoapp.activity;

import org.androidpn.demoapp.application.MyApplication;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.nostra13.universalimageloader.core.ImageLoader;

public class BaseActivity extends FragmentActivity {
	
 protected ImageLoader imageLoader = ImageLoader.getInstance();
	// 上下文实例
 public Context context;	
	// 应用全局的实例	
 public  MyApplication application;
    // 还差一个核心层
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	context = getApplicationContext();
	application = (MyApplication) this.getApplication();
}
   

}
