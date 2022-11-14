package backend.shapes;

import backend.Shape;

import java.awt.*;

public class LineSegment extends DefaultShape implements Shape {

    public String[] properties() {
        return new String[]{
                "x", "y", "width"
        };
    }

    @Override
    public void draw(Graphics canvas) {
        Point point = getPosition();
        canvas.setColor(getColor());
        canvas.drawLine(get("width").intValue(), 0, get("width").intValue(), point.y);
    }
}
