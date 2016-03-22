package org.androidpn.demoapp.model;

import org.litepal.crud.DataSupport;

public class Tag_user extends DataSupport{
	
	private int tag_id;
	private int user_id;
	
	
	
	public Tag_user() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getTag_id() {
		return tag_id;
	}
	public void setTag_id(int tag_id) {
		this.tag_id = tag_id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	
	
	

}
