package backend.shapes;

import org.json.simple.JsonObject;

import java.awt.*;

public class Rectangle extends AbstractShapeClass {

    private int width;
    private int height;

    public Rectangle(Point point, int width, int height) {
        super(point);
        this.width = width;
        this.height = height;
    }

    public Rectangle() {
        super(new Point());
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
    public Point[] points() {
        Point center = getPosition();

        int x = width/2;
        int y = height/2;

        return new Point[] {
                new Point(center.x - x, center.y - y), // Top Left
                new Point(center.x - x, center.y + y), // Bottom Left
                new Point(center.x + x, center.y - y), // Top Right
                new Point(center.x + x, center.y + y), // Bottom Right
        };
    }

    @Override
    public Point resize(Point corner, Point to) {
        Point[] points = points();

        int x = 0;
        int y = 0;

        int index = 0;

        if(corner.equals(points[0])) {
            x = corner.x - to.x;
            y = corner.y - to.y;
        }

        if(corner.equals(points[1])) {
            x = corner.x - to.x;
            y = to.y - corner.y;
            index = 1;
        }

        if(corner.equals(points[2])) {
            x = to.x - corner.x;
            y = corner.y - to.y;
            index = 2;
        }

        if(corner.equals(points[3])) {
            x = to.x - corner.x;
            y = to.y - corner.y;
            index = 3;
        }

        width = Math.abs(width + x);
        height = Math.abs(height + y);

        return points()[index];
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
    public String toString() {
        return "rectangle-" + seed;
    }

    public double area() {
        return width * height;
    }

    @Override
    public JsonObject toJSON() {
        JsonObject json = super.toJSON();

        json.put("point.x", getPosition().x);
        json.put("point.y", getPosition().y);

        json.put("width", width);
        json.put("height", height);

        return json;
    }

    @Override
    public void fromJSON(JsonObject json) {
        super.fromJSON(json);

        setPosition(new Point(
                json.getInteger("point.x"),
                json.getInteger("point.y")
        ));

        this.width = json.getInteger("width");
        this.height = json.getInteger("height");
    }
}
