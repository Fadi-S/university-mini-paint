package backend.shapes.creator;

import backend.interfaces.Shape;

import java.awt.*;

public interface ShapeCreator {
    String[] properties();
    void set(String key, Integer value);
    Integer get(String key);
    Shape create();
    String getName();
    void setColor(Color color);
    void setFillColor(Color color);
}
