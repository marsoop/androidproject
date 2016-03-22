package org.androidpn.client;

import org.jivesoftware.smack.packet.IQ;

/**
 * 写之前要清楚 IQ 的结构
 * @author dou
 *
 */
public class DeliverConfirmIQ extends IQ {
	private String uuid;
	//有get set 的方法 目的是共外部读写
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/**
	 * deliverconfirm 子xml的命名空间
	 */
	@Override
	public String getChildElementXML() {
		 StringBuilder buf = new StringBuilder();
	        buf.append("<").append("deliverconfirm").append(" xmlns=\"").append(
	                "androidpn:iq:deliverconfirm").append("\">");
	        if (uuid != null) {
	            buf.append("<uuid>").append(uuid).append("</uuid>");
	        }
	        buf.append("</").append("deliverconfirm").append("> ");
	        return buf.toString();
	}

}
