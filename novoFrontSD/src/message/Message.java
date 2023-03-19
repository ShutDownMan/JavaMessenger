package message;

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

    // getters and setters

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public ArrayList<String> getRecipients() {
        return recipients;
    }

    public void setRecipients(ArrayList<String> recipients) {
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