/*
 * Copyright (C) 2010 Moduad Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.androidpn.client;

import org.androidpn.demoapp.manager.MessageManager;
import org.androidpn.demoapp.model.ChatMsg;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;

/** 
 * This class notifies the receiver of incoming notifcation packets asynchronously.  
 *
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class NotificationPacketListener implements PacketListener {

    private static final String LOGTAG = LogUtil
            .makeLogTag(NotificationPacketListener.class);

    private final XmppManager xmppManager;

    public NotificationPacketListener(XmppManager xmppManager) {
        this.xmppManager = xmppManager;
    }

    @SuppressLint("SimpleDateFormat")
	@Override
    public void processPacket(Packet packet) {
        Log.d(LOGTAG, "NotificationPacketListener.processPacket()...");
        Log.d(LOGTAG, "packet.toXML()=" + packet.toXML());
         //先会判断获得的数据包是否是 NotificationIQ 的一个实例
        if (packet instanceof NotificationIQ) {
        	/*如果是程序会调用 NotificationIQ 的getChildElementXML() 
        	方法将数据包中携带的信息拼装为一个字符串进行判断动作是否为发送广播*/
            NotificationIQ notification = (NotificationIQ) packet;
            
            if (notification.getChildElementXML().contains(
                    "androidpn:iq:notification")) {
                String notificationId = notification.getId();
                String notificationApiKey = notification.getApiKey();
                String notificationTitle = notification.getTitle();
                String notificationMessage = notification.getMessage();
                // String notificationTicker = notification.getTicker();
                String notificationUri = notification.getUri();
                String notificationImageUrl = notification.getImage_url();

                //存储  
             
                ChatMsg chatMsg = MessageManager.getInstance(xmppManager.getContext()).saveChatMsg(xmppManager.getContext(), notificationMessage);
              
                System.out.println("-----已读---------"+chatMsg.getIsreaded()+"mmmidmmm"+chatMsg.getId());
                
               Intent intent = new Intent(Constants.ACTION_SHOW_NOTIFICATION);
               intent.putExtra(Constants.NOTIFICATION_ID, notificationId);
               intent.putExtra(Constants.NOTIFICATION_API_KEY, notificationApiKey);
               intent.putExtra(Constants.NOTIFICATION_TITLE,notificationTitle);
               intent.putExtra(Constants.NOTIFICATION_MESSAGE,notificationMessage);
               intent.putExtra(Constants.NOTIFICATION_URI, notificationUri);
               intent.putExtra(Constants.NOTIFICATION_IMAGE_URL, notificationImageUrl);
              
               if(notificationUri.equals(Constants.CLIENT_TYPE_M)){
               	 xmppManager.getContext().sendBroadcast(intent);
               }else{
               //另外是PC端推送的	
               	
               }
       
                 //消息回执
                DeliverConfirmIQ deliverConfirmIQ = new DeliverConfirmIQ();
                deliverConfirmIQ.setUuid(notificationId);
                System.out.println("提醒服务器我已经收到你可以删掉这条记录"+notificationId);
                 //类型：向服务器发送数据的类型set;向服务器请求数据get
                deliverConfirmIQ.setType(IQ.Type.SET);
                //xmppManager.getConnection()唯一可和服务器交互的DD
                xmppManager.getConnection().sendPacket(deliverConfirmIQ);
            }
        }

    }

}
