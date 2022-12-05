package backend.shapes;

import backend.interfaces.Shape;

import java.awt.*;

public class Rectangle extends AbstractShapeClass implements Shape {

    private final int width;
    private final int height;

    public Rectangle(Point point, int width, int height) {
        this.width = width;
        this.height = height;

        setPosition(point);
    }

    @Override
    public boolean contains(Point point) {
        Point center = getPosition();

        return new java.awt.Rectangle(
                center.x - (width/2),
                center.y - (height/2),
                width,
                height
        ).contains(point);

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
        canvas.drawRect(center.x - (width / 2), center.y - (height / 2), width, height);
    }

    @Override
    protected void drawFill(Graphics canvas) {
        Point point = getPosition();
        canvas.fillRect(point.x - (width/2)+1, point.y - (height/2)+1, width-1, height-1);
    }

    @Override
    public String getKey() {
        return "rectangle-" + seed;
    }

    public double area() {
        return width * height;
    }
}
