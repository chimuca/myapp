package com.messake.messake.core;

import com.messake.messake.core.Msg.MessageType;

/**
 *
 * Created by messake on 2015/12/26.
 */
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
