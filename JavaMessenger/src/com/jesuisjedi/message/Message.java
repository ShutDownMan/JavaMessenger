package com.jesuisjedi.message;

import java.io.Serializable;
import java.util.ArrayList;

/*
 * serializable message DTO
 */
public class Message implements Serializable {
    public MessageType type;
    public Object payload;
    public String sender;
    public ArrayList<String> recipients;

    public Message(MessageType type, Object payload, String sender, ArrayList<String> recipients) {
        this.type = type;
        this.payload = payload;
        this.sender = sender;
        this.recipients = recipients;
    }

    @Override
    public String toString() {
        final String typeStr = type.toString();
        final String payloadStr = (payload != null) ? payload.toString() : "null";
        final String senderStr = (sender != null) ? sender.toString() : "null";
        final String recipientsStr = (recipients != null) ? recipients.toString() : "null";
        return "Message{" +
                "type=\'" + typeStr + '\'' +
                ", payload=\'" + payloadStr + '\'' +
                ", sender='" + senderStr + '\'' +
                ", recipients=" + recipientsStr +
                '}';
    }
}