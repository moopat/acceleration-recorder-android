package at.eht.stream;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * @author Markus Deutsch
 */
public class Util {

    // TODO: FIX!
    public static String getSHA256(String input) throws NoSuchAlgorithmException {
        //MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        //messageDigest.update(input.getBytes());
        //return new String(messageDigest.digest());
        return UUID.randomUUID().toString();
    }
}
