package backend.shapes;

import backend.Shape;

import java.awt.*;

public class Rectangle extends DefaultShape implements Shape {

    @Override
    public void draw(Graphics canvas) {
        canvas.setColor(null);
        if(get("colorize") == 1) {
            canvas.setColor(getColor() == null ? getFillColor() : getColor());
        }

        Point point = getPosition();

        int height = get("height").intValue();
        int width = get("width").intValue();

        if(getColor() != null) {
            canvas.drawRect(point.x - (width / 2), point.y - (height / 2), width, height);
        }else {
            canvas.fillRect(point.x - (width/2), point.y - (height/2), width, height);
        }
    }

    @Override
    public boolean isPointInside(Point point) {
        int height = get("height").intValue();
        int width = get("width").intValue();
        Point center = getPosition();

        int x = point.x;
        int y = point.y;

        Point topRight = new Point(center.x + (width/2), center.y + (height/2));
        Point bottomLeft = new Point(center.x - (width/2), center.y - (height/2));

        return x > bottomLeft.x && x < topRight.x && y > bottomLeft.y && y < topRight.y;
    }

    @Override
    public String getKey() {
        return "rectangle-" + seed;
    }

    @Override
    public String[] properties() {
        return new String[] {
            "x", "y", "height", "width"
        };
    }
}
