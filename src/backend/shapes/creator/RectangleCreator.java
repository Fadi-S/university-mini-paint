package backend.shapes.creator;

import backend.interfaces.Shape;
import backend.shapes.Rectangle;

public class RectangleCreator extends DefaultShapeCreator {

    @Override
    public String[] properties() {
        return new String[] {
                "width", "height"
        };
    }

    @Override
    public Shape create() {
        Rectangle rectangle = new Rectangle(this.point, get("width"), get("height"));

        rectangle.setColor(this.color);
        rectangle.setFillColor(this.fillColor);

        return rectangle;
    }

    @Override
    public String getName() {
        return "rectangle";
    }
}
