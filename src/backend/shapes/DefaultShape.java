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
        return this.fillColor;
    }

    @Override
    public void draw(Graphics canvas) {
        canvas.setColor(Color.black);

        if(getColor() != null)
            canvas.setColor(getColor());
        drawOutline(canvas);

        if(getFillColor() != null) {
            canvas.setColor(getFillColor());
            drawFill(canvas);
        }
    }

    protected abstract void drawOutline(Graphics canvas);
    protected abstract void drawFill(Graphics canvas);

    public abstract boolean isPointInside(Point point);

    public abstract String[] properties();
}
