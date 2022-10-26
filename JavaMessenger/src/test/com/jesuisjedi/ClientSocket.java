package test.com.jesuisjedi;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;

public class ClientSocket {
    private Socket socket = null;
    private ObjectOutputStream outputStream = null;
    private ObjectInputStream inputStream = null;

    public ClientSocket(String serverName, int serverPort) throws IOException {
        socket = new Socket(serverName, serverPort);
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());
    }

    public void close() throws IOException {
        if (socket != null) {
            socket.close();
        }
        if (outputStream != null) {
            outputStream.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
    }

    public void sendMessage(Message message) {
        try {
            outputStream.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Message receiveMessage() {
        try {
            return (Message) inputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static class Message implements Serializable {
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
        
        public Object writeReplace() throws ObjectStreamException {
            return new MessageProxy(this);
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

    public enum MessageType {
        CONTROL,
        TEXT,
    }

    public static class MessageProxy implements Externalizable {
        private MessageType type;
        private Object payload;
        private String sender;
        private ArrayList<String> recipients;

        public MessageProxy() {
        }

        public MessageProxy(Message message) {
            this.type = message.type;
            this.payload = message.payload;
            this.sender = message.sender;
            this.recipients = message.recipients;
        }

        @Override
        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeObject(type);
            out.writeObject(payload);
            out.writeObject(sender);
            out.writeObject(recipients);
        }

        @Override
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            type = (MessageType) in.readObject();
            payload = in.readObject();
            sender = (String) in.readObject();
            recipients = (ArrayList<String>) in.readObject();
        }

        private Object readResolve() throws ObjectStreamException {
            return new Message(type, payload, sender, recipients);
        }
    }
}
