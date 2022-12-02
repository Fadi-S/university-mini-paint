package backend.shapes;

import backend.shapes.interfaces.Movable;
import backend.shapes.interfaces.Shape;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public abstract class DefaultShape implements Shape, Movable {

    private static int key = 1;
    protected final String seed = String.format("%02d", (key++));

    private Color color;
    private Color fillColor;
    private Point point;
    private final Map<String, Double> properties;

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
        canvas.setColor(getColor());
        drawOutline(canvas);

        if(getFillColor() != null) {
            canvas.setColor(getFillColor());
            drawFill(canvas);
        }
    }

    protected abstract void drawOutline(Graphics canvas);
    protected abstract void drawFill(Graphics canvas);
    public abstract String[] properties();

    @Override
    public abstract boolean contains(Point point);

    @Override
    public void setDraggingPoint(Point point) {
        setPosition(point);
    }

    @Override
    public Point getDraggingPoint() {
        return getPosition();
    }

    @Override
    public void moveTo(Point point) {
        setDraggingPoint(point);
    }

    public String toString()
    {
        return getKey();
    }
}
