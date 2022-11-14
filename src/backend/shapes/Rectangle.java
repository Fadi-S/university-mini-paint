package backend.shapes;

import backend.Shape;

import java.awt.*;

public class Rectangle extends DefaultShape implements Shape {

    @Override
    public void draw(Graphics canvas) {
        Point point = getPosition();
        canvas.setColor(getColor());

        canvas.drawRect(point.x, point.y, get("width").intValue(), get("height").intValue());
    }

    @Override
    public String[] properties() {
        return new String[] {
            "x", "y", "height", "width"
        };
    }
}
