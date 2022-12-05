package backend.shapes.creator;

import backend.interfaces.Shape;
import backend.shapes.Triangle;

import java.awt.*;

public class TriangleCreator extends DefaultShapeCreator {
    @Override
    public String[] properties() {
        return new String[] { "x1", "y1", "x2", "y2", "x3", "y3" };
    }

    @Override
    public Shape create() {
        Triangle triangle = new Triangle(
                new Point(get("x1"), get("y1")),
                new Point(get("x2"), get("y2")),
                new Point(get("x3"), get("y3"))
        );

        triangle.setColor(this.color);
        triangle.setFillColor(this.fillColor);

        return triangle;
    }

    @Override
    public String getName() {
        return "triangle";
    }
}
