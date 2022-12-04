package backend.shapes.creator;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public abstract class DefaultShapeCreator implements ShapeCreator {
    private final Map<String, Integer> properties;
    protected Color color;
    protected Color fillColor;
    protected Point point;

    DefaultShapeCreator() {
        properties = new HashMap<>();
    }

    public Integer get(String property) {
        return this.properties.getOrDefault(property, 0);
    }

    public void set(String property, Integer value) {
        this.properties.put(property, value);
    }

    public void setPosition(Point position) {
        this.point = position;
    }

    public void setFillColor(Color color) {
        this.fillColor = color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
