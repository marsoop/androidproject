/*
 * Copyright (C) 2010 Moduad Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.androidpn.demoapp;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.androidpn.client.ServiceManager;
import org.androidpn.demoapp.activity.AddressListFragment;
import org.androidpn.demoapp.activity.AddressListFragment.BackAddressListFragmentInterface;
import org.androidpn.demoapp.activity.BaseActivity;
import org.androidpn.demoapp.activity.SeacherActivity;
import org.androidpn.demoapp.activity.TabFragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnActionExpandListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.SearchView;

/**
 * 
 * @author dourl
 *
 */
public class DemoAppActivity extends BaseActivity implements OnClickListener,OnPageChangeListener,BackAddressListFragmentInterface{
	private ViewPager mViewPager;
	private List<Fragment> mTabs ;
	private AddressListFragment addressListFragment;
	private FragmentPagerAdapter mAdapter;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        Log.d("DemoAppActivity", "onCreate()...");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        setTitle("微信");
        setOverflowButtonAlways();
        
		getActionBar().setDisplayShowHomeEnabled(false);

		
		initView();
		
		initDatas();
		
		mViewPager.setAdapter(mAdapter);
		
		initEvent();
		
		
		//Volley_post();
		
        // Settings
        /*Button okButton = (Button) findViewById(R.id.btn_settings);
        
       
        okButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ServiceManager.viewNotificationSettings(DemoAppActivity.this);
            }
        });*/
        
       

        // Start the service 初始化服务类，
        ServiceManager serviceManager = new ServiceManager(this);
         //消息图标
        serviceManager.setNotificationIcon(R.drawable.notification);
        serviceManager.startService();
        
      /*serviceManager.setAlias("xiaomi3");
        
        List<String> tagsList = new ArrayList<String>();
        tagsList.add("sports");
        tagsList.add("music");
        serviceManager.setTags(tagsList);
*/
    }
    
    /**
     * 初始化所有事件
     */
    private void initEvent() {
    	mViewPager.setOnPageChangeListener(this);		
	}


	private void initDatas() {
		
		
    	
		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager())
		{

			@Override
			public int getCount()
			{
				return initFragments().size();
			}

			@Override
			public Fragment getItem(int position)
			{
				return initFragments().get(position);
			}
		};
		
		
		
		
	}


	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
		//底部的小tab
		/*ChangeColorIconWithText one = (ChangeColorIconWithText) findViewById(R.id.id_indicator_one);
		mTabIndicators.add(one);
		ChangeColorIconWithText two = (ChangeColorIconWithText) findViewById(R.id.id_indicator_two);
		mTabIndicators.add(two);
		ChangeColorIconWithText three = (ChangeColorIconWithText) findViewById(R.id.id_indicator_three);
		mTabIndicators.add(three);
		ChangeColorIconWithText four = (ChangeColorIconWithText) findViewById(R.id.id_indicator_four);
		mTabIndicators.add(four);

		one.setOnClickListener(this);
		two.setOnClickListener(this);
		//three.setOnClickListener(this);
		four.setOnClickListener(this);

		one.setIconAlpha(1.0f);*/
	}
	
	private List<Fragment> initFragments(){
		
		mTabs = new ArrayList<Fragment>();
		
		TabFragment tabFragment = new TabFragment();
		Bundle bundle = new Bundle();
		bundle.putString(TabFragment.TITLE, "first");
		tabFragment.setArguments(bundle);
		
		mTabs.add(tabFragment);
		//通讯录
		addressListFragment = new AddressListFragment();
		Bundle bundle4 = new Bundle();
		bundle4.putString(TabFragment.TITLE, "addresss");
		addressListFragment.setArguments(bundle4);
		
		mTabs.add(addressListFragment);
		
		TabFragment tabFragment2 = new TabFragment();
		Bundle bundle2 = new Bundle();
		bundle2.putString(TabFragment.TITLE, "second");
		tabFragment2.setArguments(bundle2);
		
		mTabs.add(tabFragment2);
	
		
		return mTabs;
		
		
	}


	private void setOverflowButtonAlways() {
    	try
		{
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKey = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			menuKey.setAccessible(true);
			menuKey.setBoolean(config, false);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
    

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
    	// TODO Auto-generated method stub
    	if (featureId == Window.FEATURE_ACTION_BAR && menu != null)
		{
			if (menu.getClass().getSimpleName().equals("MenuBuilder"))
			{
				try
				{
					Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
					m.setAccessible(true);
					m.invoke(menu, true);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}

		return super.onMenuOpened(featureId, menu);
    }


	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		if (positionOffset > 0)
		{
			/*ChangeColorIconWithText left = mTabIndicators.get(position);
			ChangeColorIconWithText right = mTabIndicators.get(position + 1);
			left.setIconAlpha(1 - positionOffset);
			right.setIconAlpha(positionOffset);*/
		}
		
	}


	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onClick(View v) {
		clickTab(v);		
	}


	private void clickTab(View v) {
		resetOtherTabs();
		/*switch (v.getId())
		{
		case R.id.id_indicator_one:
			mTabIndicators.get(0).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(0, false);
			break;
		case R.id.id_indicator_two:
			mTabIndicators.get(1).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(1, false);
			break;
		 case R.id.id_indicator_three:
			mTabIndicators.get(2).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(2, false);
			break;
		case R.id.id_indicator_four:
			mTabIndicators.get(2).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(3, false);
			break;
		}*/
		
	}

	/**
	 * 重置其他的TabIndicator的颜色
	 */
	private void resetOtherTabs() {
	/*	for (int i = 0; i < mTabIndicators.size(); i++)
		{
			mTabIndicators.get(i).setIconAlpha(0);
		}*/
		
	}

	@Override
	public void onBackPressed() {
		//AnimateFirstDisplayListener.displayedImages.clear();
		imageLoader.stop();	
		addressListFragment.onBackPressedForActivity();
		super.onBackPressed();
	}

	
	@Override
	public void setFragment(AddressListFragment backFragment) {
		this.addressListFragment=backFragment;
		
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// 加载菜单
		getMenuInflater().inflate(R.menu.main, menu);
		
		//对SearchView的属性进行配置（比如添加监听事件等）
		
		MenuItem searchItem = menu.findItem(R.id.action_search);
		//在ActionView展开和合并的时候显示不同的界面
		searchItem.setOnActionExpandListener(new OnActionExpandListener() {
			
			@Override
			public boolean onMenuItemActionExpand(MenuItem arg0) {
				startActivity(new Intent(DemoAppActivity.this,SeacherActivity.class));
				return true;
			}
			
			@Override
			public boolean onMenuItemActionCollapse(MenuItem arg0) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		
		
		
		
		SearchView searchView = (SearchView) searchItem.getActionView();
		
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.item_clear_memory_cache:
				imageLoader.clearMemoryCache();		// 清除内存缓存
				return true;
			case R.id.item_clear_disc_cache:
				imageLoader.clearDiscCache();		// 清除SD卡中的缓存
				return true;
			default:
				return false;
		}
	}
	
  


  
}