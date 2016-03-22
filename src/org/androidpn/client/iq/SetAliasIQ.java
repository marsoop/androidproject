package org.androidpn.client.iq;

import org.jivesoftware.smack.packet.IQ;

public class SetAliasIQ extends IQ {
	private String userName;
	private String alias;
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	@Override
	public String getChildElementXML() {
		StringBuilder buf = new StringBuilder();
        buf.append("<").append("setalias").append(" xmlns=\"").append(
                "androidpn:iq:setalias").append("\">");
        if (userName != null) {
            buf.append("<userName>").append(userName).append("</userName>");
        }
        if (alias != null) {
            buf.append("<alias>").append(alias).append("</alias>");
        }
        buf.append("</").append("setalias").append("> ");
        return buf.toString();
	}

}
