package org.androidpn.demoapp.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.androidpn.client.Constants;
import org.androidpn.demoapp.R;
import org.androidpn.demoapp.adapter.ChatMessageAdapter;
import org.androidpn.demoapp.adapter.FaceVPAdapter;
import org.androidpn.demoapp.database.ChatMsgProvider;
import org.androidpn.demoapp.manager.MessageManager;
import org.androidpn.demoapp.model.ChatMsg;
import org.androidpn.demoapp.utils.ExpressionUtil;
import org.androidpn.demoapp.utils.PreferencesUtils;
import org.androidpn.demoapp.view.PasteEditText;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ChatActivity extends BaseActivity implements OnClickListener,
		OnRefreshListener {
	private ViewPager mViewPager;
	private LinearLayout mDotsLayout;

	private PasteEditText input;
	private View send;
	// 表情容器
	private LinearLayout chat_face_container, chat_add_container;
	private RelativeLayout edittext_layout;
	private ImageView iv_emoticons_normal;
    private ImageView iv_emoticons_checked;
	private ImageView image_face;// 表情图标
	private Button image_add;// 更多图标
	private TextView tv_title, tv_pic,// 图片
			tv_camera,// 拍照
			tv_loc;// 位置
	// 表情图标每页6列4行
	private int columns = 6;
	private int rows = 4;
	// 每页显示的表情view
	private List<View> views = new ArrayList<View>();
	// 表情列表
	private List<String> staticFacesList;
	private LayoutInflater inflater;

	// 刷新控件
	private SwipeRefreshLayout mSwipeLayout;
	 private InputMethodManager manager;
	private ListView mListView;
	// 模拟数据
	SimpleDateFormat sd;
	private List<Integer> list = new ArrayList<Integer>();
	// 聊天模式
	String broadcast = "1";// 单聊类型
	// 发送消息的必要参数
	String fromUser, time, toUser;
	private int setOff;
	// 聊天信息列表
	private List<ChatMsg> chat_msg_list = new ArrayList<ChatMsg>();

	private ChatMessageAdapter chatMessageAdapter;
	private NewMsgReciver newMsgReciver;
    
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				
				break;
            case 1:
            	if(chat_msg_list.size()>0){
            		chatMessageAdapter = new ChatMessageAdapter(chat_msg_list, ChatActivity.this);
            		mListView.setAdapter(chatMessageAdapter);
            		mListView.setSelection(chat_msg_list.size());
            		chatMessageAdapter.notifyDataSetChanged();
            	}
            	
				break;
			default:
				break;
			}
		};
	};
	
	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_chat);
	
		inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		// 装载静态表情图标
		staticFacesList = ExpressionUtil.initStaticFaces(this);
		
		// 获取发送者本人
		fromUser = PreferencesUtils.getSharePreStr(context,Constants.XMPP_USERNAME);
		
		Intent intent = getIntent();
		// 获取聊天对象
		toUser = intent.getStringExtra("chatUserName");

		// 初始化控件
		initViews();
		// 初始化表情
		initViewPager();
		// 初始化更多选项（即表情图标右侧"+"号内容）
		initAdd();
		// 初始化数据
		initData();
		new Thread(new Runnable()
	    {
	      public void run()
	      {
	        MessageManager.getInstance(context).updateMsgToReaded(context, fromUser, toUser);
	      }
	    }).start();
		

	}

	private void setListener() {
		mSwipeLayout.setOnRefreshListener(this);

	}

	@SuppressLint("SimpleDateFormat")
	private void initData() {
		System.out.println("初始化数据");
		// 初始化偏移量
		setOff = 0;

		// 查询数据
		chat_msg_list = MessageManager.getInstance(context).updateChatContext(context, fromUser, toUser);
		
		setOff = chat_msg_list.size();

		chatMessageAdapter = new ChatMessageAdapter(chat_msg_list, this);
		mListView.setAdapter(chatMessageAdapter);
		mListView.setSelection(chat_msg_list.size());
		chatMessageAdapter.notifyDataSetChanged();
		Log.d("ChatActivity", "initData()" + fromUser + "---to-------" + toUser);
		// 获取接受者 从上一个activity（通讯录里面） 获取

	}
	

	@Override
	protected void onResume() {
		// 建议在 这个生命周期
		newMsgReciver = new NewMsgReciver();

		IntentFilter intentFilter = new IntentFilter(Constants.ACTION_NOTIFICATION_NEWMSG);

		registerReceiver(newMsgReciver, intentFilter);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				// 让输入框获取焦点
				input.requestFocus();
			}
		}, 100);
		super.onResume();
		
		
	}

	

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		unregisterReceiver(newMsgReciver);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			hideSoftInputView();
			if (chat_face_container.getVisibility() == View.VISIBLE) {
				chat_face_container.setVisibility(View.GONE);
			} else if (chat_add_container.getVisibility() == View.VISIBLE) {
				chat_add_container.setVisibility(View.GONE);
			} else {
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@SuppressLint({ "InlinedApi", "CutPasteId" })
	private void initViews() {
		
		manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		// 上拉刷新控件
		mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.id_swipe_ly);
		mSwipeLayout.setOnRefreshListener(this);
		mSwipeLayout.setBackgroundResource(R.drawable.actionbar_add_icon);
		mSwipeLayout.setColorScheme(android.R.color.holo_green_dark,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);

		mListView = (ListView) findViewById(R.id.id_listview);
		// 表情图标
		image_face = (ImageView) findViewById(R.id.iv_emoticons_normal);
		// 更多图标
		image_add = (Button) findViewById(R.id.btn_more);
		// 表情布局
		chat_face_container = (LinearLayout) findViewById(R.id.chat_face_container);
		// 更多
		chat_add_container = (LinearLayout) findViewById(R.id.chat_add_container);
		mViewPager = (ViewPager) findViewById(R.id.face_viewpager);
		mViewPager.setOnPageChangeListener(new PageChange());

		// 表情下小圆点
		mDotsLayout = (LinearLayout) findViewById(R.id.face_dots_container);
		//笑脸表情
		 iv_emoticons_normal = (ImageView) findViewById(R.id.iv_emoticons_normal);
	     iv_emoticons_checked = (ImageView) findViewById(R.id.iv_emoticons_checked);
		 iv_emoticons_normal.setVisibility(View.VISIBLE);
	     iv_emoticons_checked.setVisibility(View.INVISIBLE);
	     
	     iv_emoticons_checked.setOnClickListener(this);
	     iv_emoticons_normal.setOnClickListener(this);
	    
		edittext_layout = (RelativeLayout) findViewById(R.id.edittext_layout);
		edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_normal);
		input = (PasteEditText) findViewById(R.id.et_sendmessage);
		send = findViewById(R.id.btn_send);
		// 输入框
		input.setOnClickListener(this);
		input.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				 if (hasFocus) {
	                    edittext_layout
	                            .setBackgroundResource(R.drawable.input_bar_bg_active);
	                } else {
	                    edittext_layout
	                            .setBackgroundResource(R.drawable.input_bar_bg_normal);
	                }
				
			}
		});
		input.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				 if (!TextUtils.isEmpty(s)) {
					 image_add.setVisibility(View.GONE);
					 send.setVisibility(View.VISIBLE);
	                } else {
	                	image_add.setVisibility(View.VISIBLE);
	                	send.setVisibility(View.GONE);
	                }
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		// 表情按钮
		image_face.setOnClickListener(this);
		// 更多按钮
		image_add.setOnClickListener(this);
		// 发送
		send.setOnClickListener(this);
	}

	/**
	 * 初始化表情
	 */
	private void initViewPager() {
		int pagesize = ExpressionUtil.getPagerCount(staticFacesList.size(),
				columns, rows);
		// 获取页数
		for (int i = 0; i < pagesize; i++) {
			views.add(ExpressionUtil.viewPagerItem(this, i, staticFacesList,
					columns, rows, input));
			LayoutParams params = new LayoutParams(16, 16);
			// 动态添加 小点
			mDotsLayout.addView(dotsItem(i), params);
		}
		// 静态表情
		FaceVPAdapter mVpAdapter = new FaceVPAdapter(views);
		mViewPager.setAdapter(mVpAdapter);
		// 选中状态
		mDotsLayout.getChildAt(0).setSelected(true);
	}

	private ImageView dotsItem(int position) {
		View layout = inflater.inflate(R.layout.dot_image, null);
		ImageView iv = (ImageView) layout.findViewById(R.id.face_dot);
		iv.setId(position);
		return iv;
	}

	private void initAdd() {
		tv_pic = (TextView) findViewById(R.id.tv_pic);
		tv_camera = (TextView) findViewById(R.id.tv_camera);
		tv_loc = (TextView) findViewById(R.id.tv_loc);

		tv_pic.setOnClickListener(this);
		tv_camera.setOnClickListener(this);
		tv_loc.setOnClickListener(this);

	}

	/**
	 * 表情页改变时，dots效果也要跟着改变
	 * */
	class PageChange implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int arg0) {
			for (int i = 0; i < mDotsLayout.getChildCount(); i++) {
				mDotsLayout.getChildAt(i).setSelected(false);
			}
			mDotsLayout.getChildAt(arg0).setSelected(true);
		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btn_send:
			String content = input.getText().toString();
			if (TextUtils.isEmpty(content)) {
				return;
			}
			sendMsg(content, ChatMsg.TYPE[0]); // 最基本的文本
			break;
		case R.id.et_sendmessage:
			if (chat_face_container.getVisibility() == View.VISIBLE) {
				chat_face_container.setVisibility(View.GONE);
			}
			if (chat_add_container.getVisibility() == View.VISIBLE) {
				chat_add_container.setVisibility(View.GONE);
			}
			break;
			
		case R.id.iv_emoticons_normal:
			hideSoftInputView();// 隐藏软键盘
			iv_emoticons_normal.setVisibility(View.INVISIBLE);
            iv_emoticons_checked.setVisibility(View.VISIBLE);
			
			if (chat_add_container.getVisibility() == View.VISIBLE) {
				chat_add_container.setVisibility(View.GONE);
			}
			if (chat_face_container.getVisibility() == View.GONE) {
				chat_face_container.setVisibility(View.VISIBLE);
				
			} else {
				chat_face_container.setVisibility(View.GONE);
				iv_emoticons_normal.setVisibility(View.VISIBLE);
			}
			break;	
			
		case R.id.iv_emoticons_checked:
			iv_emoticons_normal.setVisibility(View.VISIBLE);
            iv_emoticons_checked.setVisibility(View.INVISIBLE);
            manager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			if (chat_add_container.getVisibility() == View.VISIBLE) {
				chat_add_container.setVisibility(View.GONE);
			}
			if (chat_face_container.getVisibility() == View.GONE) {
				chat_face_container.setVisibility(View.VISIBLE);
				
			} else {
				chat_face_container.setVisibility(View.GONE);
			}
			//显示输入盘
			
			break;
		case R.id.btn_more:
			
			if(image_add.getVisibility() ==View.GONE){
				hideSoftInputView();// 隐藏软键盘
				image_add.setVisibility(View.VISIBLE);
			}else{
				if (chat_face_container.getVisibility() == View.VISIBLE) {
					chat_face_container.setVisibility(View.GONE);
					 iv_emoticons_normal.setVisibility(View.VISIBLE);
		             iv_emoticons_checked.setVisibility(View.INVISIBLE);
				}
				if (chat_add_container.getVisibility() == View.GONE) {
					chat_add_container.setVisibility(View.VISIBLE);
				} else {
					chat_add_container.setVisibility(View.GONE);
				}
			}
			
			break;
		case R.id.tv_pic:// 模拟一张图片路径
			// sendMsgImg("http://my.csdn.net/uploads/avatar/3/B/9/1_baiyuliang2013.jpg");
			break;
		case R.id.tv_camera:// 拍照，换个美女图片吧
			// sendMsgImg("http://b.hiphotos.baidu.com/image/pic/item/55e736d12f2eb93872b0d889d6628535e4dd6fe8.jpg");
			break;
		case R.id.tv_loc:// 位置，正常情况下是需要定位的，可以用百度或者高德地图，现设置为北京坐标
			// sendMsgLocation("116.404,39.915");
			break;
		}

	}

	/**
	 * 
	 * @param content
	 *            json 对象
	 */

	private void sendMsg(String content, String type) {

		// 获取时间格式
		/*sd = new SimpleDateFormat("MM-dd HH:mm");
		time = sd.format(new Date());*/

		ChatMsg chatMsg = MessageManager.getInstance(context).packageChatMsg(context,fromUser,
				toUser, content, type, new Date().getTime()+"");

		chat_msg_list.add(chatMsg);

		setOff = chat_msg_list.size();

		Log.d("ChatActivity", "setOff-----sendMsg------->" + setOff);
		chatMessageAdapter.notifyDataSetChanged();

		input.setText("");

		// 发送消息
		MessageManager.getInstance(context).sendMessage(context, broadcast,
				chatMsg);

	}
	
	
	
	/**
	 * 隐藏软键盘
	 */
	public void hideSoftInputView() {
		if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getCurrentFocus() != null)
				manager.hideSoftInputFromWindow(getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	@Override
	public void onRefresh() {
		mSwipeLayout.postDelayed(new Runnable() {

			@Override
			public void run() {
				// 更新数据
				list.clear();
				for (int i = 0; i < 50; i++) {
					list.add(i);
				}
				// refresh();

				// 更新完后调用该方法结束刷新
				mSwipeLayout.setRefreshing(false);

			}
		}, 1000);

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub

		super.onStop();

		if (newMsgReciver != null) {
			newMsgReciver = null;
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}

	/**
	 * 接收新来的消息
	 * 
	 * @author dourl
	 * 
	 */
	private class NewMsgReciver extends BroadcastReceiver {

		 private NewMsgReciver()
		    {
		    }

		    private void updateMsgToReaded(int paramInt)
		    {
		      MessageManager.getInstance(ChatActivity.this.context).updateMsgToReaded(ChatActivity.this.context, String.valueOf(paramInt));
		    }

		    public void onReceive(Context paramContext, Intent paramIntent)
		    {
		      ChatMsg localChatMsg = (ChatMsg)paramIntent.getSerializableExtra("correctMsg");
		      
		      if (!"".equals(localChatMsg))
		      { 
		    	   if(toUser.equals(localChatMsg.getMsgfrom())){
		    		   ChatActivity.this.chat_msg_list.add(localChatMsg);
				       ChatActivity.this.setOff = ChatActivity.this.chat_msg_list.size();
				       ChatActivity.this.chatMessageAdapter.notifyDataSetChanged();
				      if(!TextUtils.isEmpty(String.valueOf(localChatMsg.getId()))){
					    	  updateMsgToReaded(localChatMsg.getId());
					      }
		    	   }
		    	   
		        
		      }
		      
		     
		    }
		

	}
	

}
