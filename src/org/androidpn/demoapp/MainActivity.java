package org.androidpn.demoapp;

import org.androidpn.client.ServiceManager;
import org.androidpn.demoapp.activity.AddressListFragment;
import org.androidpn.demoapp.activity.AddressListFragment.BackAddressListFragmentInterface;
import org.androidpn.demoapp.activity.BaseActivity;
import org.androidpn.demoapp.activity.HomeFragment;
import org.androidpn.demoapp.activity.ProfileFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends BaseActivity implements
		BackAddressListFragmentInterface {
	// 未读消息textview
	private TextView unreadLabel;

	private ImageView[] imageBtns;
	private TextView[] textViews;
	private int index;
	// 当前fragment的index
	private int currentTabIndex;

	private Fragment[] fragments;

	private HomeFragment homeFragment;
	private AddressListFragment addressListFragment;
	private ProfileFragment profileFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
		initView();
		
	}

	private void initView() {
		unreadLabel = (TextView) findViewById(R.id.unread_msg_number);
		homeFragment = new HomeFragment();
		addressListFragment = new AddressListFragment();
		profileFragment = new ProfileFragment();

		fragments = new Fragment[] { homeFragment, addressListFragment,
				profileFragment };

		imageBtns = new ImageView[3];
		imageBtns[0] = (ImageView) findViewById(R.id.ib_weixin);
		imageBtns[1] = (ImageView) findViewById(R.id.ib_contact_list);
		imageBtns[2] = (ImageView) findViewById(R.id.ib_profile);

		imageBtns[0].setSelected(true);

		textViews = new TextView[3];
		textViews[0] = (TextView) findViewById(R.id.tv_weixin);
		textViews[1] = (TextView) findViewById(R.id.tv_contact_list);
		textViews[2] = (TextView) findViewById(R.id.tv_profile);
		textViews[0].setTextColor(0xFF45C01A);

		// 添加显示第一个fragment
		getSupportFragmentManager().beginTransaction()
				.add(R.id.fragment_container, homeFragment)
				.add(R.id.fragment_container, addressListFragment)
				.add(R.id.fragment_container, profileFragment)
				.hide(addressListFragment).hide(profileFragment)
				.show(homeFragment).commit();

	}
	
	

	public void onTabClicked(View view) {

		switch (view.getId()) {
		case R.id.re_weixin:
			index = 0;
			break;
		case R.id.re_contact_list:
			index = 1;
			break;
		case R.id.re_profile:
			index = 2;
			break;

		}

		if (currentTabIndex != index) {
			FragmentTransaction trx = getSupportFragmentManager()
					.beginTransaction();
			trx.hide(fragments[currentTabIndex]);
			if (!fragments[index].isAdded()) {
				trx.add(R.id.fragment_container, fragments[index]);
			}
			trx.show(fragments[index]).commit();
		}
		imageBtns[currentTabIndex].setSelected(false);
		// 把当前tab设为选中状态
		imageBtns[index].setSelected(true);
		textViews[currentTabIndex].setTextColor(0xFF999999);
		textViews[index].setTextColor(0xFF45C01A);
		currentTabIndex = index;
	}

	@Override
	public void onBackPressed() {
		// AnimateFirstDisplayListener.displayedImages.clear();
		imageLoader.stop();
		addressListFragment.onBackPressedForActivity();
		super.onBackPressed();
	}

	@Override
	public void setFragment(AddressListFragment backAddressFragment) {
		this.addressListFragment = backAddressFragment;

	}

}
