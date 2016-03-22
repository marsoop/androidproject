package org.androidpn.demoapp.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.androidpn.demoapp.R;
import org.androidpn.demoapp.adapter.AddressForSelectedAdapter;
import org.androidpn.demoapp.manager.MessageManager;
import org.androidpn.demoapp.model.SortModel;
import org.androidpn.demoapp.model.User;
import org.androidpn.demoapp.utils.AnimateFirstDisplayListener;
import org.androidpn.demoapp.utils.LetterUtil;
import org.androidpn.demoapp.utils.PinyinComparator;
import org.androidpn.demoapp.view.SideBar;
import org.androidpn.demoapp.view.SideBar.OnTouchingLetterChangedListener;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

public class AddressForSelectActivity extends BaseActivity {
	protected static ImageLoader imageLoader = ImageLoader.getInstance();
	private ListView selectSortListView;
	private AddressForSelectedAdapter selectSortAdapter;
	private SideBar selectSortSideBar;
	private TextView selectSortDialog;
	private List<SortModel> sourceDateList;
	
	// 根据拼音来排列ListView里面的数据类
    private PinyinComparator pinyinComparator = new PinyinComparator();
    
    private LinearLayout menuLinerLayout;
    private EditText et_search;
    // 选中用户总数,右上角显示
    int total = 0;
    private String userId = null;
    private String groupId = null;
    /** 是否为一个新建的群组 */
    protected boolean isCreatingNewGroup;
  
    
    private ImageView iv_search;
    // 添加的列表
    private List<String> addList = new ArrayList<String>();
	List<User> userList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_address_list);
		
		groupId = getIntent().getStringExtra("groupId");
        userId = getIntent().getStringExtra("userId");
        //不是自己创建的
        if(groupId != null){
        	//已经存在的群
         isCreatingNewGroup = false;
             //获取群信息
        }else if(userId !=null){
        	 isCreatingNewGroup = true;
        	 total = 1;
        	 addList.add(userId);
        }else{
        	 isCreatingNewGroup = true;
        }
        
		initView();
		initDate();
		
		
	}

	private void initView() {
       selectSortListView  = (ListView) findViewById(R.id.select_address_listView);
	   selectSortDialog = (TextView) findViewById(R.id.select_address_dialog);
	   selectSortSideBar = (SideBar) findViewById(R.id.select_address_sidebar);
	   selectSortSideBar.setTextView(selectSortDialog);
	   
	   menuLinerLayout = (LinearLayout) this.findViewById(R.id.linearLayoutMenu);
	  
       iv_search = (ImageView) this.findViewById(R.id.iv_search);
	   
	   et_search = (EditText) this.findViewById(R.id.et_search);
	  
	}

	private List<SortModel> filleDate() {
		 //现在通讯录先包括自己
		userList = MessageManager.getInstance(this).getLocalAddressList(this);
		
		List<SortModel> mSortList = new ArrayList<SortModel>();
		
		if (!userList.isEmpty()) {

			for (int i = 0; i < userList.size(); i++) {
				
				//聊天用的name
				String toUsername = userList.get(i).getUsername();
				Log.d("DemoAppActivity", "toUsername"+toUsername);
				//显示的name 后期需要修改成 昵称
				String name = userList.get(i).getUsername();
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

	private void initDate() {
		//获取数据源
		 sourceDateList = filleDate();
		 Collections.sort(sourceDateList, pinyinComparator);
		 selectSortAdapter  = new AddressForSelectedAdapter(context, sourceDateList, selectSortListView, imageLoader, new AnimateFirstDisplayListener());
	     selectSortListView.setAdapter(selectSortAdapter);
	     selectSortListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	     selectSortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?>  parent, View view, int position,
					long id) {
				SortModel s =	(SortModel)selectSortAdapter.getItem(position);
				String name = s.getToUserName();
				if(selectSortListView.isItemChecked(position)){
	
					System.out.println("-----name------"+name);
					if(addList.contains(name)){
						System.out.println("-----我先-移除----"+name);
						addList.remove(name);
					}
					addList.add(name);
				
		                // 选中用户显示在滑动栏显示
		            showCheckImage(selectSortAdapter.getBitmap(position), s);

		          
				}else{
				   deleteImage(s);
				};
				
				System.out.println("-----我先-移除----"+addList.size());
				selectSortAdapter.notifyDataSetChanged();
				
			}
		});
	     
	     selectSortSideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
			
			@Override
			public void onTouchingLetterChanged(String s) {
				int  position = selectSortAdapter.getPositionForSection(s.charAt(0));
			    
				if(position!=-1){
					selectSortListView.setSelection(position);
				}
			
			
			}
		});
	}
	
	
	@Override
	public void onBackPressed() {
		imageLoader.stop();
		AnimateFirstDisplayListener.displayedImages.clear();
		super.onBackPressed();
	}

	 	 
	 private void showCheckImage(Bitmap bitmap,
				SortModel sortModel) {
			// TODO Auto-generated method stub
		/* if (addList.contains(sortModel.getToUserName())) {
	            return;
	        }*/
	        total++;
	        
	     // 包含TextView的LinearLayout
	        // 参数设置
	        android.widget.LinearLayout.LayoutParams menuLinerLayoutParames = new LinearLayout.LayoutParams(
	                108, 108, 1);
	        View view = LayoutInflater.from(this).inflate(
	                R.layout.item_chatroom_header_item, null);
	        ImageView images = (ImageView) view.findViewById(R.id.iv_avatar);
	        menuLinerLayoutParames.setMargins(6, 6, 6, 6);

	        // 设置id，方便后面删除
	        view.setTag(sortModel);
	        if (bitmap == null) {
	            images.setImageResource(R.drawable.default_header);
	        } else {
	            images.setImageBitmap(bitmap);
	        }
	       
	        menuLinerLayout.addView(view, menuLinerLayoutParames);
		
	        if (total > 0) {
	            if (iv_search.getVisibility() == View.VISIBLE) {
	                iv_search.setVisibility(View.GONE);
	            }
	            if(et_search.getVisibility() == View.VISIBLE){
	            	et_search.setVisibility(View.GONE);
	            }
	        }
	        
	       // addList.add(sortModel.getToUserName());
	 
	 }
	 private void deleteImage(SortModel sortModel) {
		 View view = (View) menuLinerLayout.findViewWithTag(sortModel);

	        menuLinerLayout.removeView(view);
	        total--;
	        
	        if (total < 1) {
	            if (iv_search.getVisibility() == View.GONE) {
	                iv_search.setVisibility(View.VISIBLE);
	            }
	            if (et_search.getVisibility() == View.GONE) {
	            	et_search.setVisibility(View.VISIBLE);
	            }
	        }
		}
	
}
