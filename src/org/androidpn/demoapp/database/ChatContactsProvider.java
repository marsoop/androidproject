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

public class ChatContactsProvider extends ContentProvider {

	public static final String AUTHORITY = "org.android.provider.chatcontacts";
	public static final String CONTACTS_TYPE = "vnd.android.cursor.dir/vnd.may.chatcontacts";
	public static final String CONTACTS_ITEM_TYPE = "vnd.android.cursor.item/vnd.may.chatcontacts";
	public static final String TABLE_NAME = DBConfig.ContactsColumnName.TABLE_CONTACTS;
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + TABLE_NAME);
	private static final int CONTACTS = 1;
	private static final int CONTACTS_ID = 2;
	
	private static final UriMatcher URI_MATCHER = new UriMatcher(
			UriMatcher.NO_MATCH);
	
	static {
		URI_MATCHER.addURI(AUTHORITY, "contacts", CONTACTS);
		URI_MATCHER.addURI(AUTHORITY, "contacts/#", CONTACTS_ID);
	}
	private SQLiteOpenHelper mOpenHelper;
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
     int match = URI_MATCHER.match(uri);
		
		switch (match) {
		case CONTACTS:
			return ChatContactsProvider.CONTACTS_TYPE;
        case CONTACTS_ID:
        	return ChatContactsProvider.CONTACTS_ITEM_TYPE;
		
		default:
			throw new IllegalArgumentException("Unknown URL" + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		
		int match = URI_MATCHER.match(uri);
		long c;
		
		if (match == CONTACTS) {
			c = db.insert(DBConfig.ContactsColumnName.TABLE_CONTACTS, "", values);
		} else if (match == CONTACTS_ID) {
			c = db.insert(DBConfig.ContactsColumnName.TABLE_CONTACTS, "", values);
		} else {
			throw new IllegalArgumentException("Unknown URL");
		}
		Uri noteUri = ContentUris.withAppendedId(CONTENT_URI, c);
		// 通知监听者有数据插入。
		getContext().getContentResolver().notifyChange(noteUri, null);
		return noteUri;
	}

	@Override
	public boolean onCreate() {
		System.out.println("----------------------我是chatcontactsprovideronCreate()");
		mOpenHelper = new 	SqliteBaseDB(getContext(),DBConfig.SqliteBaseDBname);
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		
		int match = URI_MATCHER.match(uri);
		if(match ==CONTACTS){
			return db.query(DBConfig.ContactsColumnName.TABLE_CONTACTS, projection, selection, selectionArgs, null, null, sortOrder);
		}else if(match == CONTACTS_ID){
			return db.query(DBConfig.ContactsColumnName.TABLE_CONTACTS, projection, selection, selectionArgs, null, null, sortOrder);
		}else {
			throw new IllegalArgumentException("contants Unknown URL");
		}
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public int bulkInsert(Uri uri, ContentValues[] values) {
		int numValues = 0;
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		db.beginTransaction();
		try {
			numValues = values.length;
			for (int i = 0; i < numValues; i++) {
				insert(uri, values[i]);
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			db.close();
		}
		return numValues;
	}
}
