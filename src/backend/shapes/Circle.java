package backend.shapes;

import backend.Shape;

import java.awt.*;

public class Circle extends DefaultShape implements Shape {

    public String[] properties() {
        return new String[]{
                "x", "y", "radius"
        };
    }

    @Override
    public boolean isPointInside(Point point) {
        double radius = get("radius");
        Point shapePosition = this.getPosition();

        double distance = Math.sqrt(
                Math.pow(point.x - shapePosition.x, 2) + Math.pow(point.y - shapePosition.y, 2)
        );

        return radius > distance;
    }

    @Override
    public String getKey() {
        return "circle-" + seed;
    }

    @Override
    public void draw(Graphics canvas) {
        Point point = getPosition();
        int radius = get("radius").intValue();
        int diameter = radius * 2;

        canvas.setColor(getColor());

        canvas.drawOval(
                point.x - radius,
                point.y - radius,
                diameter, diameter
        );
    }
}
