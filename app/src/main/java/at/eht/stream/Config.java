package at.eht.stream;

import java.util.UUID;

/**
 * @author Markus Deutsch
 */
public class Config {

    private Config(){}

    public static final String WEBSERVICE_LINK = "http://xxx/%s.php";
    public final static UUID PEBBLE_UUID = UUID.fromString("1938e5ec-29fe-47f0-84e8-a237b4dc4e57");
    
    // Relating to acceleration data transmission Pebble -> Smartphone
    public final static int KEY_COMMAND = 0;
    public final static int COMMAND_DATA = 1;
    public final static int NUMBER_SAMPLES = 25;
    public final static int NUMBER_PARAMETERS = 1;
    public final static int SAMPLERATE = 25;
}
