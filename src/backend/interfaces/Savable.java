package backend.interfaces;

import org.json.simple.JsonObject;

public interface Savable {
    JsonObject toJSON();
    void fromJSON(JsonObject json);
}
