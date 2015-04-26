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

    public static String getRandomString(){
        // Return only the first segment of a random UUID
        // b948dfb3-c0ba-4881-a67a-d326ae13a3fa would return b948dfb3
        String baseString = UUID.randomUUID().toString();
        return baseString.substring(0, baseString.indexOf("-"));
    }
}
