package backend.shapes;

import backend.interfaces.Shape;

import java.awt.*;

public class Oval extends AbstractShapeClass implements Shape {
    private final int width;
    private final int height;

    public Oval(Point point, int width, int height) {
        super(point);

        this.width = width;
        this.height = height;
    }

    @Override
    public boolean contains(Point point) {
        Point center = getPosition();

        return Math.abs(point.x - center.x) < (width / 2)
                && Math.abs(point.y - center.y) < (height / 2);
    }

    @Override
    public Shape clone() {
        return new Oval(getPosition(), width, height);
    }

    @Override
    public Point[] points() {
        Point center = getPosition();

        int x = width/2;
        int y = height/2;

        return new Point[] {
                new Point(center.x - x, center.y - y), // Top Left
                new Point(center.x - x, center.y + y), // Bottom Left
                new Point(center.x + x, center.y - y), // Top Right
                new Point(center.x + x, center.y + y), // Bottom Right
        };
    }

    @Override
    protected void drawOutline(Graphics canvas) {
        Point point = getPosition();
        canvas.drawOval(
                point.x - width/2,
                point.y - height/2,
                width, height
        );
    }

    @Override
    protected void drawFill(Graphics canvas) {
        Point point = getPosition();
        canvas.fillOval(
                point.x - width/2,
                point.y - height/2,
                width, height
        );
    }

    @Override
    public String toString() {
        return "circle-" + seed;
    }

    public double area() {
        return Math.PI * (width / 2.) * (height / 2.);
    }
}
