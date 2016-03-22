package org.androidpn.demoapp.core;


public class DBConfig {

	/**
	 * 数据库的名称
	 */
	public static String SqliteBaseDBname = "mayim.db";
	/**
	 * 数据库的版本号
	 */
	public static int SqliteBaseDBversions = 1;

	
	public static class ContactsColumnName{
	    
		public static String TABLE_CONTACTS = "contacts";
		public static String[] lanname = { "_id", "username", "password",
				"email", "name", "createdate", "tags"};
	
		public static final String CON_ID = "_id";
		public static final String CON_USERNAME = "username";
		public static final String CON_PASSWORD = "password";
		public static final String CON_EMAIL = "email";
		public static final String CON_NAME = "name";
		public static final String CON_CREATEDATE = "createdate";
		public static final String CON_TAGS = "tags";
	
	}
	
	/**
	 * 消息表字段
	 * @author dourl
	 *
	 */
	public static class ChatColumnName {
		/**
		 * 聊天记录的数据表
		 */
		public static String TABLE_MSG = "chatmsg";
		public static String[] lanname = { "_id", "msgdate", "msgfrom",
				"msgto", "msgtype", "msgbody", "iscoming", "isreaded" };
		/**
		 * 数据表id
		 */
		public static final String MSG_ID = "_id";
		/**
		 * 聊天的时间
		 */
		public static final String MSG_DATE = "msgdate";
	
		public static final String MSG_FROM = "msgfrom";
		/**
		 * 发送者的jid
		 */
		public static final String MSG_TO = "msgto";

		public static final String MSG_TYPE = "msgtype";
		/**
		 * 发送者发送的内容
		 */

		public static final String MSG_BODY = "msgbody";
		/**
		 * 消息的阅读状态0代表新的消息,1代表已阅读的消息
		 * 
		 */
		public static final String MSG_ISREADED = "isreaded";
		
		/**
		 * 标识是自己发送还是别人发送1代表自己发的消息,0代表别人发送的消息
		 */
		public static final String MSG_ISCOMING = "iscoming";

	}

}
