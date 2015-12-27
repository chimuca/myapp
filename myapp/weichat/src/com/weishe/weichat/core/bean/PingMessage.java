package com.weishe.weichat.core.bean;

import com.weishe.weichat.core.bean.Msg.MessageType;

public class PingMessage {
	private MessageType messageType;
	private String clientId;

	public MessageType getMessageType() {
		return messageType;
	}

	public String getClientId() {
		return clientId;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
}
