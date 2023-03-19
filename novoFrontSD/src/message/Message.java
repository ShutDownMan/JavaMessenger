package message;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import javax.crypto.*;
import javax.crypto.spec.*;

/*
 * serializable message DTO
 */
public class Message implements Serializable {
    public MessageType type;
    public Object payload;
    public String sender;
    public ArrayList<String> recipients;
    public LocalTime time;
    private SecretKeySpec key;

    public Message(MessageType type, Object payload, String sender, ArrayList<String> recipients) {
        this.type = type;
        this.payload = payload;
        this.sender = sender;
        this.recipients = recipients;
        this.time = LocalTime.now();

        byte[] keyBytes = "minhakey".getBytes();
        this.key = new SecretKeySpec(keyBytes, "DES");
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

    public void encryptMessage() {
        // Encrypt using DES algorith
        try {
            // Create a cipher
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            // Encrypt the payload
            // byte[] encryptedPayload = cipher.doFinal(payload.toString().getBytes());

            // this.payload = encryptedPayload;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void decryptMessage() {
        // Decrypt using DES algorithm
        try {
            // Create a cipher
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);

            // Decrypt the payload
            // byte[] decryptedPayload = cipher.doFinal((byte[]) payload);

            // this.payload = new String(decryptedPayload);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}