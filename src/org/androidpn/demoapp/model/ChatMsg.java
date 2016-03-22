package org.androidpn.demoapp.model;

import java.io.Serializable;

public class ChatMsg implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;// id
	private String msgdate;// 发送者
	private String msgfrom;// 接收者
	private String msgto;// 信息类型
	private String msgtype;// 信息内容
	private int iscoming;// 0表接收的消息，1表发送的消息
	private String msgbody;// 时间
	private int isreaded;// 0表未读的消息，1表已读消息
	
	public String getCountNumber() {
		return countNumber;
	}

	public void setCountNumber(String countNumber) {
		this.countNumber = countNumber;
	}
	private String countNumber;
	
	
	public static final String[] TYPE= {"msg_type_text","msg_type_img","msg_type_voice","msg_type_location","msg_type_link"};
	
	 /** 
     * 默认的构造方法必须不能省，阿里不然不能解析 
     */
	public ChatMsg() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMsgdate() {
		return msgdate;
	}
	public void setMsgdate(String msgdate) {
		this.msgdate = msgdate;
	}
	public String getMsgfrom() {
		return msgfrom;
	}
	public void setMsgfrom(String msgfrom) {
		this.msgfrom = msgfrom;
	}
	public String getMsgto() {
		return msgto;
	}
	public void setMsgto(String msgto) {
		this.msgto = msgto;
	}
	public String getMsgtype() {
		return msgtype;
	}
	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}
	public int getIscoming() {
		return iscoming;
	}
	public void setIscoming(int iscoming) {
		this.iscoming = iscoming;
	}
	public String getMsgbody() {
		return msgbody;
	}
	public void setMsgbody(String msgbody) {
		this.msgbody = msgbody;
	}
	public int getIsreaded() {
		return isreaded;
	}
	public void setIsreaded(int isreaded) {
		this.isreaded = isreaded;
	}

}
