package at.eht.stream;

import java.util.UUID;

/**
 * @author Markus Deutsch
 */
public class Config {

    private Config(){}

    public static final String WEBSERVICE_LINK = "http://xxx/%s.php";
    public final static UUID PEBBLE_UUID = UUID.fromString("e892fa89-8790-4fc7-a77e-19b4e09d53f4");
    
    // Relating to acceleration data transmission Pebble -> Smartphone
    public final static int KEY_COMMAND = 0;
    public final static int COMMAND_DATA = 1;
    public final static int NUMBER_SAMPLES = 20;
    public final static int NUMBER_PARAMETERS = 3;
}
