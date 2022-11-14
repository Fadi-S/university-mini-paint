package backend.shapes;

import backend.Shape;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public abstract class DefaultShape implements Shape {

    protected final String seed = String.valueOf((int) (Math.random() * 100));

    private Color color;
    private Color fillColor;
    private Point point;
    private Map<String, Double> properties;

    DefaultShape() {
        properties = new HashMap<>();

        this.point = new Point();
    }

    DefaultShape(int x, int y) {
        properties = new HashMap<>();

        setPosition(new Point(x, y));
    }

    @Override
    public void setPosition(Point position) {
        this.point = position;
    }

    @Override
    public Point getPosition() {
        return this.point;
    }

    @Override
    public void setProperties(Map<String, Double> properties) {
        this.properties = properties;
    }

    @Override
    public Map<String, Double> getProperties() {
        return this.properties;
    }

    public Double get(String property) {
        return this.properties.getOrDefault(property, 0.0);
    }

    public void set(String property, Double value) {
        if(property.equals("x"))
            point.x = value.intValue();

        if(property.equals("y"))
            point.y = value.intValue();

        this.properties.put(property, value);
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public Color getColor() {
        return this.color == null ? Color.black : this.color;
    }

    @Override
    public void setFillColor(Color color) {
        this.fillColor = color;
    }

    @Override
    public Color getFillColor() {
        return this.fillColor == null ? Color.white : this.fillColor;
    }

    @Override
    public abstract void draw(Graphics canvas);

    public abstract boolean isPointInside(Point point);

    public abstract String[] properties();
}
