package org.androidpn.client.iq;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.packet.IQ;

public class SetTagIQ extends IQ {
	private String userName;
	
	private List<String> tagList = new ArrayList<String>();
	
	public List<String> getTagList() {
		return tagList;
	}

	public void setTagList(List<String> tagList) {
		this.tagList = tagList;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	

	@Override
	public String getChildElementXML() {
		StringBuilder buf = new StringBuilder();
        buf.append("<").append("settags").append(" xmlns=\"").append(
                "androidpn:iq:settags").append("\">");
        if (userName != null) {
            buf.append("<userName>").append(userName).append("</userName>");
        }
        
        if(tagList!=null&&!tagList.isEmpty()){
        	buf.append("<tags>");
        	boolean needSeperate = false;
        	 for(String tag:tagList){
        		 if(needSeperate){
        			 buf.append(","); 	 
        		 }
        		 buf.append(tag);
        		 needSeperate=true;
        	 }
        	buf.append("</tags>");
        }
        
        buf.append("</").append("settags").append("> ");
        return buf.toString();
	}

}
