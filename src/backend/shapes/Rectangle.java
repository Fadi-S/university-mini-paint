package backend.shapes;

import backend.Shape;

import java.awt.*;

public class Rectangle extends DefaultShape implements Shape {

    @Override
    public boolean isPointInside(Point point) {
        Point center = getPosition();
        int height = get("height").intValue();
        int width = get("width").intValue();

        return new java.awt.Rectangle(
                center.x - (width/2),
                center.y - (height/2),
                width,
                height
        ).contains(point.x, point.y);

        /*
        * Get top right point and bottom left
        * and check if point is inside them.
        * old function:
        *
        * int x = point.x;
        * int y = point.y;
        * Point topRight = new Point(center.x + (width/2), center.y + (height/2));
        * Point bottomLeft = new Point(center.x - (width/2), center.y - (height/2));
        *
        * return x > bottomLeft.x && x < topRight.x && y > bottomLeft.y && y < topRight.y;
        **/
    }

    @Override
    protected void drawOutline(Graphics canvas) {
        Point center = getPosition();
        int height = get("height").intValue();
        int width = get("width").intValue();
        canvas.drawRect(center.x - (width / 2), center.y - (height / 2), width, height);
    }

    @Override
    protected void drawFill(Graphics canvas) {
        Point point = getPosition();
        int height = get("height").intValue();
        int width = get("width").intValue();
        canvas.fillRect(point.x - (width/2)+1, point.y - (height/2)+1, width-1, height-1);
    }

    @Override
    public String getKey() {
        return "rectangle-" + seed;
    }

    public double area() {
        return get("width") * get("height");
    }

    @Override
    public String[] properties() {
        return new String[] {
            "height", "width"
        };
    }
}
