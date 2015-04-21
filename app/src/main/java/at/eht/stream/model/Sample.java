package at.eht.stream.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Markus Deutsch
 */
public class Sample {

    private int x;
    private int y;
    private int z;
    private int verticalAcceleration;

    public Sample() {
    }

    public Sample(int verticalAcceleration){
        this.verticalAcceleration = verticalAcceleration;
    }

    public Sample(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Sample(JSONObject sampleAsJson){
        try {
            x = sampleAsJson.getInt("x");
            y = sampleAsJson.getInt("y");
            z = sampleAsJson.getInt("z");
            verticalAcceleration = sampleAsJson.getInt("v");
        } catch (JSONException e) {
            throw new InstantiationError("Error inflating Sample: " + sampleAsJson.toString());
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public void setX(int x) {

        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public int getVerticalAcceleration() {
        return verticalAcceleration;
    }

    public void setVerticalAcceleration(int verticalAcceleration) {
        this.verticalAcceleration = verticalAcceleration;
    }

    public String toString(){
        return x+","+y+","+z+","+ verticalAcceleration;
    }

    public JSONObject toJSON(){
        JSONObject json = new JSONObject();
        try {
            json.put("x", x);
            json.put("y", y);
            json.put("z", z);
            json.put("v", verticalAcceleration);
        } catch (JSONException e){
            // JSONException can only happen if one of the keys is null. (So never.)
            throw new RuntimeException(e);
        }

        return json;
    }
}
