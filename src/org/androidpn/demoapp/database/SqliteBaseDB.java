package org.androidpn.demoapp.database;

import org.androidpn.demoapp.core.DBConfig;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteBaseDB extends SQLiteOpenHelper {
	
	public SqliteBaseDB(Context contexts, String names){
		super(contexts, names, null, DBConfig.SqliteBaseDBversions);
		
	}
	

	public SqliteBaseDB(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		//MSG
		db.execSQL("create table "
			    +DBConfig.ChatColumnName.TABLE_MSG+"("
			    +DBConfig.ChatColumnName.MSG_ID
				+" integer primary key autoincrement,"
				+DBConfig.ChatColumnName.MSG_DATE+ " text ,"
				+DBConfig.ChatColumnName.MSG_FROM+ " text ,"
				+DBConfig.ChatColumnName.MSG_TO+ " text ,"
				+DBConfig.ChatColumnName.MSG_TYPE+ " text ,"
				+DBConfig.ChatColumnName.MSG_BODY+ " text ,"
				+DBConfig.ChatColumnName.MSG_ISCOMING+ " text ,"
				+DBConfig.ChatColumnName.MSG_ISREADED+ " text);"
				);
		//CONTACTS
		db.execSQL("create table "
			    +DBConfig.ContactsColumnName.TABLE_CONTACTS+"("
			    +DBConfig.ContactsColumnName.CON_ID
				+" integer primary key autoincrement,"
				+DBConfig.ContactsColumnName.CON_CREATEDATE+ " text ,"
				+DBConfig.ContactsColumnName.CON_EMAIL+ " text ,"
				+DBConfig.ContactsColumnName.CON_NAME+ " text ,"
				+DBConfig.ContactsColumnName.CON_PASSWORD+ " text ,"
				+DBConfig.ContactsColumnName.CON_USERNAME+ " text ,"
				+DBConfig.ContactsColumnName.CON_TAGS+ " text);"
				);	
		
		
		
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
