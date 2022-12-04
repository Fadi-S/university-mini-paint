package backend.shapes.creator;

import backend.interfaces.Shape;
import backend.shapes.Circle;

public class CircleCreator extends DefaultShapeCreator {
    @Override
    public String[] properties() {
        return new String[] { "radius" };
    }

    public String getName() {
        return "circle";
    }

    @Override
    public Shape create() {
        Circle circle = new Circle(this.point, get("radius"));

        circle.setColor(this.color);
        circle.setFillColor(this.fillColor);

        return circle;
    }
}
