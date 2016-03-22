package org.androidpn.demoapp.model;

import java.util.List;

import org.litepal.crud.DataSupport;

public class Tag extends DataSupport {
	
	private int id;	
	private String tag_name;
	private String tag_creator;
	private String tag_create_time;
	private List<User> user_list ;
	

	public List<User> getUser_list() {
		return user_list;
	}
	public void setUser_list(List<User> user_list) {
		this.user_list = user_list;
	}
	@Override
	public String toString() {
		return "Tag [id=" + id + ", tag_name=" + tag_name + ", tag_creator="
				+ tag_creator + ", tag_create_time=" + tag_create_time + "]";
	}
	public Tag() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTag_name() {
		return tag_name;
	}
	public void setTag_name(String tag_name) {
		this.tag_name = tag_name;
	}
	public String getTag_creator() {
		return tag_creator;
	}
	public void setTag_creator(String tag_creator) {
		this.tag_creator = tag_creator;
	}
	public String getTag_create_time() {
		return tag_create_time;
	}
	public void setTag_create_time(String tag_create_time) {
		this.tag_create_time = tag_create_time;
	}
	
	
	
	
	
	
}
