package org.androidpn.demoapp.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.androidpn.demoapp.R;
import org.androidpn.demoapp.adapter.SortAdapter;
import org.androidpn.demoapp.manager.MessageManager;
import org.androidpn.demoapp.model.SortModel;
import org.androidpn.demoapp.model.User;
import org.androidpn.demoapp.utils.AnimateFirstDisplayListener;
import org.androidpn.demoapp.utils.LetterUtil;
import org.androidpn.demoapp.utils.PinyinComparator;
import org.androidpn.demoapp.utils.T;
import org.androidpn.demoapp.view.SideBar;
import org.androidpn.demoapp.view.SideBar.OnTouchingLetterChangedListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

public class AddressListFragment extends Fragment implements OnClickListener{
	
	
	//定义回调函数及变量 
	protected BackAddressListFragmentInterface backAddressListFragmentInterface;
	
	public interface BackAddressListFragmentInterface{
		
		public void setFragment(AddressListFragment backAddressFragment);
	}

	public static final String TITLE = "title";

	protected static ImageLoader imageLoader = ImageLoader.getInstance();
	private ListView sortListView;
	private SortAdapter sortAdapter;
	private SideBar sortSideBar;
	private TextView sortDialog;
	
	private LinearLayout addressGroupLay,addressTagLay;
	private List<SortModel> SourceDateList;
	// 根据拼音来排列ListView里面的数据类
	private PinyinComparator pinyinComparator = new PinyinComparator();

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		//回调函数赋值 
	    if(!(getActivity() instanceof BackAddressListFragmentInterface)){
			 throw new ClassCastException("Hosting activity must implement BackAddressListFragment");
		 }else{
			backAddressListFragmentInterface= (BackAddressListFragmentInterface) activity;
		}
	    
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (getArguments() != null) {
			// mTitle = getArguments().getString(TITLE);
		}

		return inflater.inflate(R.layout.fragment_address_list, container,
				false);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		//将自己的实例传出去  
		backAddressListFragmentInterface.setFragment(this);
		
		initView();
		initDate();
	}

	@Override
	public void onResume() {

		super.onResume();
		
 
		
	}

	private void initDate() {
		// 后期可以从数据库中获取的数据
		SourceDateList =filledData();
		// 根据a-z进行排序源数据
		Collections.sort(SourceDateList, pinyinComparator);
		sortAdapter = new SortAdapter(getActivity(), SourceDateList,
				imageLoader, new AnimateFirstDisplayListener());
		
		
		sortListView.setAdapter(sortAdapter);

		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 这里要利用adapter.getItem(position)来获取当前position所对应的对象
				/*Toast.makeText(getActivity().getApplication(),
						((SortModel) sortAdapter.getItem(position)).getName(),
						Toast.LENGTH_SHORT).show();*/
				//说明是自定义的header
				if(position == 0){
				   LinearLayout lay = (LinearLayout) view;
				   
				   LinearLayout tag=(LinearLayout) lay.findViewById(R.id.fragment_address_head_tag_lay);
				   
				   tag.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						
						startActivity(new Intent(getActivity(),TagActivity.class));
					}
				});
				   LinearLayout group=(LinearLayout) lay.findViewById(R.id.fragment_address_head_group_lay);
				
				   group.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						T.show(getActivity(), "group", 0);
						
					}
				});
					
				}else{
					
					String toUserName = ((SortModel) parent.getAdapter().getItem(position)).getToUserName();
					T.show(getActivity(), toUserName, 0);
					Intent intent = new Intent(getActivity(), UserInfoActivity.class);
					
					intent.putExtra("toUserNamer", toUserName);
					startActivity(intent);
				}
				
			}
		});

		sortSideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

					@Override
					public void onTouchingLetterChanged(String s) {
						int position = sortAdapter.getPositionForSection(s
								.charAt(0));
						if (position != -1) {
							sortListView.setSelection(position);
						}

					}
				});
	}
	
	

	private List<SortModel> filledData() {
        //现在通讯录先包括自己
		
		
		List<User> addressList = MessageManager.getInstance(getActivity()).getLocalAddressList(getActivity());
		
		List<SortModel> mSortList = new ArrayList<SortModel>();
		Log.d("DemoAppActivity", addressList.size()+"-----通讯录-------");
		if (!addressList.isEmpty()) {

			for (int i = 0; i < addressList.size(); i++) {
				
				//聊天用的name
				String toUsername = addressList.get(i).getUsername();
				Log.d("DemoAppActivity", "toUsername"+toUsername);
				//显示的name
				//String name = addressList.get(i).getName();
				String name = addressList.get(i).getUsername();
				if(!TextUtils.isEmpty(name)){
					
				
				Log.d("DemoAppActivity", "name"+name);
				SortModel sortModel = new SortModel();
				sortModel.setToUserName(toUsername);
				sortModel.setName(name);
				if (!"".equals(name)) {
					// 汉字转换成拼音
					String py = LetterUtil.getPinYinHeadChar(name);
					String sortString = py.substring(0, 1).toUpperCase();
					// 正则表达式，判断首字母是否是英文字母
					if (sortString.matches("[A-Z]")) {
						sortModel.setSortLetters(sortString);
					} else {
						sortModel.setSortLetters("#");
					}

					mSortList.add(sortModel);
				}
				}
			}

		}

		return mSortList;

	}

	private void initView() {

		sortListView = (ListView) getActivity().findViewById(R.id.address_listView);
		View headView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_address_list_header,null);
		sortListView.addHeaderView(headView, null,true);
		
		addressGroupLay =  (LinearLayout) headView.findViewById(R.id.fragment_address_head_group_lay);
		
		addressTagLay = (LinearLayout) getActivity().findViewById(R.id.fragment_address_head_tag_lay);
		
		sortDialog = (TextView) getActivity().findViewById(R.id.address_dialog);

		sortSideBar = (SideBar) getActivity().findViewById(R.id.address_sidebar);
		sortSideBar.setTextView(sortDialog);
		
		

	}
	
	
	//让容器释放
	public void onBackPressedForActivity() {
		AnimateFirstDisplayListener.displayedImages.clear();
	
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fragment_address_head_group_lay:
			
			break;
        case R.id.fragment_address_head_tag_lay:
        	System.out.println("点击");
			startActivity(new Intent(getActivity(),TagActivity.class));
			break;

		default:
			break;
		}
		
	}
	


}
