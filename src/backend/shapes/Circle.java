package backend.shapes;

import backend.Shape;
import backend.utils.Distance;

import java.awt.*;

public class Circle extends DefaultShape implements Shape {

    public String[] properties() {
        return new String[] { "radius" };
    }

    @Override
    public boolean isPointInside(Point point) {
        return get("radius") > Distance.between(point, getPosition());
    }

    @Override
    protected void drawOutline(Graphics canvas) {
        Point point = getPosition();
        int radius = get("radius").intValue();
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
        int radius = get("radius").intValue();
        int diameter = radius * 2;
        canvas.fillOval(
                point.x - radius+1,
                point.y - radius+1,
                diameter-2, diameter-2
        );
    }

    @Override
    public String getKey() {
        return "circle-" + seed;
    }

    public double area() {
        double r = get("radius");
        return Math.PI * r*r;
    }
}
