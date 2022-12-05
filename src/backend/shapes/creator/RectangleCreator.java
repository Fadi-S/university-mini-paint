package backend.shapes.creator;

import backend.interfaces.Shape;
import backend.shapes.Rectangle;

import java.awt.*;

public class RectangleCreator extends DefaultShapeCreator {

    @Override
    public String[] properties() {
        return new String[] {
                "x", "y", "width", "height"
        };
    }

    @Override
    public Shape create() {
        Rectangle rectangle = new Rectangle(new Point(get("x"), get("y")), get("width"), get("height"));

        rectangle.setColor(this.color);
        rectangle.setFillColor(this.fillColor);

        return rectangle;
    }

    @Override
    public String getName() {
        return "rectangle";
    }
}
