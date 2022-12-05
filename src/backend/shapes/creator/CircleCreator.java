package backend.shapes.creator;

import backend.interfaces.Shape;
import backend.shapes.Circle;

import java.awt.*;

public class CircleCreator extends DefaultShapeCreator {
    @Override
    public String[] properties() {
        return new String[] { "x", "y", "radius" };
    }

    public String getName() {
        return "circle";
    }

    @Override
    public Shape create() {
        Circle circle = new Circle(new Point(get("x"), get("y")), get("radius"));

        circle.setColor(this.color);
        circle.setFillColor(this.fillColor);

        return circle;
    }
}
