package org.androidpn.demoapp.database;

import org.androidpn.demoapp.core.DBConfig;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;


public class ChatMsgProvider extends ContentProvider {
	
	public static final String AUTHORITY = "org.android.provider.chatmsg";
	public static final String MESSAGE_TYPE = "vnd.android.cursor.dir/vnd.may.chatmsg";
	public static final String MESSAGE_ITEM_TYPE = "vnd.android.cursor.item/vnd.may.chatmsg";
	public static final String TABLE_NAME = DBConfig.ChatColumnName.TABLE_MSG;
	public static final Uri MSG_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + TABLE_NAME);
	
	
	private static final int MESSAGES = 1;
	private static final int MESSAGE_ID = 2;
	
	
	private static final UriMatcher URI_MATCHER = new UriMatcher(
			UriMatcher.NO_MATCH);
	static {
		URI_MATCHER.addURI(AUTHORITY, "chatmsg", MESSAGES);
		URI_MATCHER.addURI(AUTHORITY, "chatmsg/#", MESSAGE_ID);
	}
	
	private SQLiteOpenHelper mOpenHelper;
	
	public ChatMsgProvider() {
		super();
		System.out.println("----------------------我是chatprovider");
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		//match方法匹配后会返回一个匹配码Code，即在使用注册方法addURI时传入的第三个参数。
		int match = URI_MATCHER.match(uri);
		
		switch (match) {
		case MESSAGES:
			return ChatMsgProvider.MESSAGE_TYPE;
        case MESSAGE_ID:
        	return ChatMsgProvider.MESSAGE_ITEM_TYPE;
		
		default:
			throw new IllegalArgumentException("Unknown URL" + uri);
		}
	
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int match = URI_MATCHER.match(uri);
		long c;
		
		if(match == MESSAGES){
			c = db.insert(DBConfig.ChatColumnName.TABLE_MSG, "", values);
		}else if(match == MESSAGE_ID){
			c = db.insert(DBConfig.ChatColumnName.TABLE_MSG, "", values);
		}else{
			throw new IllegalArgumentException("Unknown DBcontentprovider-URL");
		}
		
		Uri noteUri = ContentUris.withAppendedId(MSG_URI, c);
		// 通知监听者有数据插入。
		getContext().getContentResolver().notifyChange(noteUri, null);
		return noteUri;
	}

	@Override
	public boolean onCreate() {
		System.out.println("----------------------我是chatprovideronCreate()");
		mOpenHelper = new SqliteBaseDB(getContext(), DBConfig.SqliteBaseDBname);
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int match = URI_MATCHER.match(uri);
		
		if(match ==MESSAGES){
			return db.query(DBConfig.ChatColumnName.TABLE_MSG, projection, selection, selectionArgs, null, null, sortOrder);
		}else if(match ==MESSAGE_ID){
			return db.query(DBConfig.ChatColumnName.TABLE_MSG, projection, selection, selectionArgs, null, null, sortOrder);
		}else {
			throw new IllegalArgumentException("chatmsg Unknown URL");
		}
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase localSQLiteDatabase = this.mOpenHelper.getWritableDatabase();
	    int i = URI_MATCHER.match(uri);
	    if (i == 1)
	      return localSQLiteDatabase.update(DBConfig.ChatColumnName.TABLE_MSG, values, selection, selectionArgs);
	    if (i == 2)
	      return localSQLiteDatabase.update(DBConfig.ChatColumnName.TABLE_MSG, values, selection, selectionArgs);
	    throw new IllegalArgumentException("chatmsg Unknown URL");
	}
	
	//批量导入
	@Override
	public int bulkInsert(Uri uri, ContentValues[] values) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int numValues = 0;
		db.beginTransaction();
		try {
			numValues = values.length;
			
			for(int i =0;i<numValues;i++){
				insert(uri, values[i]);
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			db.endTransaction();
		}
		return numValues;
	}

}
