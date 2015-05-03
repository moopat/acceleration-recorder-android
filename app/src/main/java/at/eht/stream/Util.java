package at.eht.stream;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * @author Markus Deutsch
 */
public class Util {

    public static String getSHA256(String input) {
        return new String(Hex.encodeHex(DigestUtils.sha256(input)));
    }

    public static String getRandomString(){
        // Return only the first segment of a random UUID
        // b948dfb3-c0ba-4881-a67a-d326ae13a3fa would return b948dfb3
        String baseString = UUID.randomUUID().toString();
        return baseString.substring(0, baseString.indexOf("-"));
    }
}
