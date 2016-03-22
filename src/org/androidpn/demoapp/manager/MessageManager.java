package org.androidpn.demoapp.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.androidpn.client.Constants;
import org.androidpn.demoapp.R;
import org.androidpn.demoapp.application.MyApplication;
import org.androidpn.demoapp.core.DBConfig;
import org.androidpn.demoapp.database.ChatContactsProvider;
import org.androidpn.demoapp.database.ChatMsgProvider;
import org.androidpn.demoapp.model.ChatMsg;
import org.androidpn.demoapp.model.User;
import org.androidpn.demoapp.utils.NetUtil;
import org.androidpn.demoapp.utils.PreferencesUtils;
import org.androidpn.demoapp.utils.T;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

/**
 * 消息管理
 * 
 * @author dourl
 * 
 */
public class MessageManager {
	private String userName = "";
	private static MessageManager messageManager = null;

	public MessageManager(Context context) {
		userName = PreferencesUtils.getSharePreStr(context,
				Constants.SHARED_PREFERENCE_NAME);

	}

	public static MessageManager getInstance(Context context) {

		if (messageManager == null) {
			messageManager = new MessageManager(context);
		}
		return messageManager;
	}

	public int countUnreadNumber(Context context,String isRead){
		return 0;
		
	}
	
	//获取本地最近的聊天记录列表
	public List<ChatMsg> getMsgGroupByUser(Context context,String msgfrom){
		String [] projection = new String[]{
				DBConfig.ChatColumnName.MSG_ID,DBConfig.ChatColumnName.MSG_FROM,
				DBConfig.ChatColumnName.MSG_TO,DBConfig.ChatColumnName.MSG_TYPE,
				DBConfig.ChatColumnName.MSG_DATE,DBConfig.ChatColumnName.MSG_BODY,
				DBConfig.ChatColumnName.MSG_ISCOMING,DBConfig.ChatColumnName.MSG_ISREADED
		};
		String selection = DBConfig.ChatColumnName.MSG_DATE+" in (select max("
	                      +DBConfig.ChatColumnName.MSG_DATE+") from " +DBConfig.ChatColumnName.TABLE_MSG
	                      +" group by "+ DBConfig.ChatColumnName.MSG_TO+" having count(*)>0) and "
	                      +DBConfig.ChatColumnName.MSG_FROM+" = ?";
		String [] selectionArgs ={msgfrom};
		String sortOrder = DBConfig.ChatColumnName.MSG_DATE+ " DESC";
		List<ChatMsg> data = new ArrayList<ChatMsg>();
		ContentResolver resolver = context.getContentResolver();
		
		Cursor c = resolver.query(ChatMsgProvider.MSG_URI, projection, selection, selectionArgs, sortOrder);
		while(c.moveToNext()){
			ChatMsg msg = new ChatMsg();
			msg.setId(Integer.parseInt(c.getString(0)));
			msg.setMsgfrom(c.getString(1));
			msg.setMsgto(c.getString(2));
			msg.setMsgtype(c.getString(3));
			msg.setMsgdate(c.getString(4));
			msg.setMsgbody(c.getString(5));
			msg.setIscoming(Integer.parseInt(c.getString(6)));
			msg.setIsreaded(Integer.parseInt(c.getString(7)));
			data.add(msg);
		}
		c.close();
		
		return data;
		
		
	}

	// 保存接受到的数据库插入数据。
	
	public ChatMsg saveChatMsg(Context context, String jsonString) {

		ChatMsg chatMsg = JSON.parseObject(jsonString, ChatMsg.class);

		ContentResolver resolver = context.getContentResolver();
		ContentValues values = new ContentValues();
		values.put(DBConfig.ChatColumnName.MSG_DATE, chatMsg.getMsgdate());
		values.put(DBConfig.ChatColumnName.MSG_FROM, chatMsg.getMsgto());
		values.put(DBConfig.ChatColumnName.MSG_TO, chatMsg.getMsgfrom());
		values.put(DBConfig.ChatColumnName.MSG_TYPE, chatMsg.getMsgtype());
		values.put(DBConfig.ChatColumnName.MSG_BODY, chatMsg.getMsgbody());
		values.put(DBConfig.ChatColumnName.MSG_ISCOMING, "0");
		values.put(DBConfig.ChatColumnName.MSG_ISREADED, "0");

        
        long l = ContentUris.parseId(resolver.insert(ChatMsgProvider.MSG_URI, values));
        System.out.println("----------保存成功-----id----------" + l);
        chatMsg.setIscoming(0);
        chatMsg.setId((int)l);
        Intent localIntent = new Intent(Constants.ACTION_NOTIFICATION_NEWMSG);
        localIntent.putExtra("correctMsg", chatMsg);
        context.sendBroadcast(localIntent);
        
        //发送广播到 聊天页面
		return chatMsg;

	}

	/**
	 * 封装发送的信息
	 * 
	 * @param toUser
	 *            接受者
	 * @param content
	 *            发送的内容
	 * @param type
	 *            内容类型
	 * @param time
	 *            发送时间时间
	 * @return
	 */

	public ChatMsg packageChatMsg(Context context, String fromUser,
			String toUser, String content, String type, String time) {

		ChatMsg msg = new ChatMsg();
		// 发出去的
		msg.setIscoming(1);
		// 已读
		msg.setIsreaded(1);
		msg.setMsgbody(content);

		msg.setMsgdate(time);
		msg.setMsgfrom(fromUser);
		msg.setMsgto(toUser);
		msg.setMsgtype(type);

		ContentResolver resolver = context.getContentResolver();
		ContentValues values = new ContentValues();
		values.put(DBConfig.ChatColumnName.MSG_DATE, time);
		values.put(DBConfig.ChatColumnName.MSG_FROM, fromUser);
		values.put(DBConfig.ChatColumnName.MSG_TO, toUser);
		values.put(DBConfig.ChatColumnName.MSG_TYPE, type);
		values.put(DBConfig.ChatColumnName.MSG_BODY, content);
		values.put(DBConfig.ChatColumnName.MSG_ISCOMING, "1");
		values.put(DBConfig.ChatColumnName.MSG_ISREADED, "1");

		resolver.insert(ChatMsgProvider.MSG_URI, values);

		return msg;

	}

	// 发送消息
	/**
	 * 
	 * @param context
	 * @param broadcast
	 *            发送类型 0All (Broadcast)1By UserName2By Alias3By Alias
	 */
	public void sendMessage(final Context context, final String broadcast,
			ChatMsg msg) {

        //json 发送给服务器
		String pcContent = JSON.toJSONString(msg);

		Log.d("MessageManager", "sendMessage()..." + pcContent);

		Log.d("MessageManager", "sendMessage()..." + msg.getMsgfrom());

		if (!TextUtils.isEmpty(pcContent)) {
			// 在注意此content 是jsonString

			if (!NetUtil.isNetConnect(context)) {
				T.showLong(context, R.string.net_warn);
				return;
			}

			// 先存储数据 （状态是等待） 然后 发送 （状态是 成功或失败）最后是 已读状态
			sentMessageByVolly(context, broadcast, msg.getMsgto(), pcContent);
		} else {
			Log.d("MessageManager", "阿里巴巴 解析json 出错" + pcContent);
		}

	}

	// 文本中这个
	private void sentMessageByVolly(final Context context,
			final String broadcast, final String username, final String content) {

		StringRequest stringRequest = new StringRequest(Method.POST,
				Constants.MOBILE_SEND_API, new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Toast.makeText(context, "提交成功" + response, 1).show();

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						Toast.makeText(context, "提交失败" + error, 1).show();
					}
				}) {

			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> hasMap = new HashMap<String, String>();
				// 发送类型（*）
				hasMap.put("broadcast", broadcast);
				// 发送对象（*）
				hasMap.put("username", username);
				System.out.println("--------------"+content);
				// 发送内容（*）
				hasMap.put("message", content);
				// 发送客户端类型（*）
				hasMap.put("uri", Constants.CLIENT_TYPE_M);

				return hasMap;
			}

		};
		stringRequest.setTag("abcTag");

		MyApplication.getHttpRequestQueue().add(stringRequest);
	}

	public int getUnReadMsgCount(Context context,String msgfrom,String msgto,String isread){
		int count = 0;
		String [] projection = new String[]{
				DBConfig.ChatColumnName.MSG_ID,DBConfig.ChatColumnName.MSG_FROM,
				DBConfig.ChatColumnName.MSG_TO,DBConfig.ChatColumnName.MSG_TYPE,
				DBConfig.ChatColumnName.MSG_DATE,DBConfig.ChatColumnName.MSG_BODY,
				DBConfig.ChatColumnName.MSG_ISCOMING,DBConfig.ChatColumnName.MSG_ISREADED
		};
		String selection =  DBConfig.ChatColumnName.MSG_FROM+"=? and "
                +DBConfig.ChatColumnName.MSG_TO+"=? and "
				+DBConfig.ChatColumnName.MSG_ISREADED+"=?";
		String [] selectionArgs ={msgfrom,msgto,isread};
		
		ContentResolver resolver = context.getContentResolver();
		Cursor c = resolver.query(ChatMsgProvider.MSG_URI, projection, selection, selectionArgs, null);
		
		if(c!= null){
			count =c.getCount();
		}
		c.close();
		System.out.println("----未读条数------"+count);
		return count;
	
	}
	
	//更新聊天记录

	public List<ChatMsg> updateChatContext(Context context,String msgfrom,String msgto){
		
		String [] projection = new String[]{
				DBConfig.ChatColumnName.MSG_ID,DBConfig.ChatColumnName.MSG_FROM,
				DBConfig.ChatColumnName.MSG_TO,DBConfig.ChatColumnName.MSG_TYPE,
				DBConfig.ChatColumnName.MSG_DATE,DBConfig.ChatColumnName.MSG_BODY,
				DBConfig.ChatColumnName.MSG_ISCOMING,DBConfig.ChatColumnName.MSG_ISREADED
		};
 		List<ChatMsg> allChatMsg = new  ArrayList<ChatMsg>();
		
		String selection =  DBConfig.ChatColumnName.MSG_FROM+"=? and "
		                    +DBConfig.ChatColumnName.MSG_TO+"=?";
		String [] selectionArgs ={msgfrom,msgto};
		ContentResolver resolver = context.getContentResolver();
		Cursor c = resolver.query(ChatMsgProvider.MSG_URI, projection, selection, selectionArgs, null);
		
		while(c.moveToNext()){
			ChatMsg msg = new ChatMsg();
			msg.setId(Integer.parseInt(c.getString(0)));
			msg.setMsgfrom(c.getString(1));
			msg.setMsgto(c.getString(2));
			msg.setMsgtype(c.getString(3));
			msg.setMsgdate(c.getString(4));
			msg.setMsgbody(c.getString(5));
			msg.setIscoming(Integer.parseInt(c.getString(6)));
			msg.setIsreaded(Integer.parseInt(c.getString(7)));
			allChatMsg.add(msg);
		}
		c.close();
		return allChatMsg;
		
		
	}
	
	
	// 保存到本地的通讯录
	public void saveUserList(Context context, List<User> list) {
		ContentValues[] values = new ContentValues[list.size()];
		for (int i = 0; i < list.size(); i++) {
			ContentValues val = new ContentValues();
			val.put(DBConfig.ContactsColumnName.CON_USERNAME, list.get(i)
					.getUsername());
			val.put(DBConfig.ContactsColumnName.CON_PASSWORD, list.get(i)
					.getPassword());
			val.put(DBConfig.ContactsColumnName.CON_EMAIL, list.get(i)
					.getEmail());
			val.put(DBConfig.ContactsColumnName.CON_CREATEDATE, list.get(i)
					.getCreatedDate());
			val.put(DBConfig.ContactsColumnName.CON_NAME, list.get(i).getName());
			val.put(DBConfig.ContactsColumnName.CON_TAGS, list.get(i).getTags());
			values[i] = val;
		}

		ContentResolver resolver = context.getContentResolver();
		int count = resolver.bulkInsert(ChatContactsProvider.CONTENT_URI,
				values);
		System.out.println("--------插入的联系人数------sql--" + count);
	}

	// 获取本地的通讯录

	public List<User> getLocalAddressList(Context context) {
		List<User> allUser = new ArrayList<User>();
		String[] projection = { DBConfig.ContactsColumnName.CON_USERNAME,
				DBConfig.ContactsColumnName.CON_NAME };
		Cursor c = context.getContentResolver().query(
				ChatContactsProvider.CONTENT_URI, projection, null, null, null);
		while (c.moveToNext()) {
			User u = new User();

			u.setUsername(c.getString(0));
			u.setName(c.getString(1));
			allUser.add(u);
		}
		c.close();

		return allUser;

	}

	// 获取服务端的通讯录
	// List<User> addressList = null;

	public void getAddressList(final Context context) {

		StringRequest stringRequest = new StringRequest(Method.GET,
				Constants.USER_LIST_API, new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						
						Log.d("MessageManager", "通讯列表" + response);

						List<User> addressList = JSONArray.parseArray(response,
								User.class);



						// 存储数据
						if (!addressList.isEmpty()) {
							// DataSupport.saveAll(addressList);
							saveUserList(context, addressList);

						}

						Log.d("MessageManager", "通讯列表" + addressList.size());

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						Toast.makeText(context, "提交失败" + error, 1).show();
					}
				});

		stringRequest.setTag("addressTag");

		MyApplication.getHttpRequestQueue().add(stringRequest);

	}

	public void updateMsgToReaded(Context context, String valueOf) {
		 ContentValues localContentValues = new ContentValues();
		    localContentValues.put("isreaded", Integer.valueOf(1));
		    int i = context.getContentResolver().update(ChatMsgProvider.MSG_URI, localContentValues, "_id =? and isreaded=?", new String[] { valueOf, "0" });
		    System.out.println(valueOf + "----------我在修改 ----" + i);
		
	}
	
   /**
    * 进入聊天界面是 更新当前的聊天
    * @param context
    * @param fromUser
    * @param toUser
    */
	public void updateMsgToReaded(Context context, String fromUser,
			String toUser) {
		 ContentValues localContentValues = new ContentValues();
		    localContentValues.put("isreaded", Integer.valueOf(1));
		    int i = context.getContentResolver().update(ChatMsgProvider.MSG_URI, localContentValues, "msgfrom=? and msgto=? and isreaded=?", new String[] { fromUser, toUser, "0" });
		    System.out.println("----------我在修改 ----" + i);
		
	}

}
