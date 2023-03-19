package message;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class MessageEncryption {

    // encrypt the message using DES
    public static String encrypt(String message, String key) {
        try {
            // String paddedKey = String.format("%-8s", key).replace(' ', '0');
            // SecretKeySpec secretKey = new SecretKeySpec(paddedKey.getBytes(), "DES");
            // Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            // cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            // byte[] encryptedMessage = cipher.doFinal(message.getBytes());
            // return Base64.getEncoder().encodeToString(encryptedMessage);
            return message;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // decrypt the message using DES
    public static String decrypt(String message, String key) {
        try {
            // String paddedKey = String.format("%-8s", key).replace(' ', '0');
            // SecretKeySpec secretKey = new SecretKeySpec(paddedKey.getBytes(), "DES");
            // Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            // cipher.init(Cipher.DECRYPT_MODE, secretKey);
            // byte[] decryptedMessage = cipher.doFinal(Base64.getDecoder().decode(message));
            // return new String(decryptedMessage);
            return message;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
