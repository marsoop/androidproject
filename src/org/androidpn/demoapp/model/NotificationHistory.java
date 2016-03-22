package org.androidpn.demoapp.model;

import org.litepal.crud.DataSupport;

public class NotificationHistory extends DataSupport {
    
	private int id;

	private String apiKey;

    private String title;

    private String message;

    private String uri;
    
    private String image_url;
    
    private String isRead;
    
    private String time;
    
    
    
    public NotificationHistory() {
		super();
		// TODO Auto-generated constructor stub
	}

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
    
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getIsRead() {
		return isRead;
	}

	public void setIsRead(String isRead) {
		this.isRead = isRead;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}
    
    
    
}
