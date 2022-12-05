package backend.shapes;

import backend.interfaces.Shape;
import backend.utils.Distance;

import java.awt.*;

public class Circle extends AbstractShapeClass implements Shape {
    private final int radius;

    public Circle(Point point, int radius) {
        super(point);

        this.radius = radius;
    }

    @Override
    public boolean contains(Point point) {
        return radius > Distance.between(point, getPosition());
    }

    @Override
    protected void drawOutline(Graphics canvas) {
        Point point = getPosition();
        int diameter = radius * 2;
        canvas.drawOval(
                point.x - radius,
                point.y - radius,
                diameter, diameter
        );
    }

    @Override
    protected void drawFill(Graphics canvas) {
        Point point = getPosition();
        int diameter = radius * 2;
        canvas.fillOval(
                point.x - radius,
                point.y - radius,
                diameter, diameter
        );
    }

    @Override
    public String toString() {
        return "circle-" + seed;
    }

    public double area() {
        return Math.PI * radius * radius;
    }
}
