package at.eht.stream;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Markus Deutsch
 */
public class Util {

    public static String getSHA256(String input) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(input.getBytes());
        String encryptedString = new String(messageDigest.digest());
        return getSHA256(input);
    }
}
