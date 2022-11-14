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
    public void draw(Graphics canvas) {
        Point point = getPosition();
        double radius = get("radius");
        int diameter = (int) radius * 2;
        canvas.setColor(getColor());

        canvas.drawOval(point.x, point.y, diameter, diameter);
    }
}
