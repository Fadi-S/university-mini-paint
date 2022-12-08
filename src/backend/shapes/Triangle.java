package backend.shapes;

import backend.interfaces.Shape;
import org.json.simple.JsonObject;

import java.awt.*;

public class Triangle extends AbstractShapeClass {

    private Point point1;
    private Point point2;
    private Point point3;

    public Triangle(Point point1, Point point2, Point point3) {
        super(
                new Point(
                        (point1.x+point2.x+point3.x) / 3,
                        (point1.y+point2.y+point3.y) / 3
                )
        );

        this.point1 = point1;
        this.point2 = point2;
        this.point3 = point3;
    }

    public Triangle() {
        super(new Point());

        point1 = new Point();
        point2 = new Point();
        point3 = new Point();
    }

    @Override
    protected void drawOutline(Graphics canvas) {
        canvas.drawPolygon(
                new int[]{point1.x, point2.x, point3.x,},
                new int[]{point1.y, point2.y, point3.y,},
                3
        );
    }

    @Override
    protected void drawFill(Graphics canvas) {
        canvas.fillPolygon(
                new int[]{point1.x, point2.x, point3.x,},
                new int[]{point1.y, point2.y, point3.y,},
                3
        );
    }

    private boolean checkPositive(Point p1, Point p2, Point p3) {
        return (p1.x - p3.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p3.y) > 0;
    }

    @Override
    public boolean contains(Point point) {
        boolean isOne, isTwo, isThree;

        isOne = checkPositive(point, point1, point2);
        isTwo = checkPositive(point, point2, point3);
        isThree = checkPositive(point, point3, point1);

        boolean hasNegative = !isOne || !isTwo || !isThree;
        boolean hasPositive = isOne || isTwo || isThree;

        return !(hasNegative && hasPositive);
    }

    @Override
    public String toString() {
        return "triangle-" + seed;
    }

    @Override
    public double area() {
        double a = point1.distance(point2);
        double b = point1.distance(point3);
        double c = point2.distance(point3);

        double s = (a+b+c) / 2; // semi perimeter

        return Math.sqrt(s * (s-a) * (s-b) * (s-c));
    }

    public void moveTo(Point point) {
        Point reference = getDraggingPoint();
        Point oldPoint = point1;

        point.x += oldPoint.x - reference.x;
        point.y += oldPoint.y - reference.y;

        int x = point.x - oldPoint.x;
        int y = point.y - oldPoint.y;

        point1.x += x;
        point1.y += y;

        point2.x += x;
        point2.y += y;

        point3.x += x;
        point3.y += y;

        setPosition(
                new Point(
                        (point1.x+point2.x+point3.x) / 3,
                        (point1.y+point2.y+point3.y) / 3
                )
        );
    }

    @Override
    public Point[] points() {
        return new Point[] {
                point1, point2, point3
        };
    }

    @Override
    public Point resize(Point corner, Point to) {
        Point p = null;

        if(corner.equals(point1)) {
            point1.x = to.x;
            point1.y = to.y;
            p = point1;
        }

        if(corner.equals(point2)) {
            point2.x = to.x;
            point2.y = to.y;
            p = point2;
        }

        if(corner.equals(point3)) {
            point3.x = to.x;
            point3.y = to.y;
            p = point3;
        }

        setPosition(
                new Point(
                        (point1.x+point2.x+point3.x) / 3,
                        (point1.y+point2.y+point3.y) / 3
                )
        );

        return p;
    }

    @Override
    public JsonObject toJSON() {
        JsonObject json = super.toJSON();

        json.put("point1.x", point1.x);
        json.put("point1.y", point1.y);

        json.put("point2.x", point2.x);
        json.put("point2.y", point2.y);

        json.put("point3.x", point3.x);
        json.put("point3.y", point3.y);

        json.put("type", this.getClass().getName());

        return json;
    }

    @Override
    public void fromJSON(JsonObject json) {
        super.fromJSON(json);

        point1.x = json.getInteger("point1.x");
        point1.y = json.getInteger("point1.y");

        point2.x = json.getInteger("point2.x");
        point2.y = json.getInteger("point2.y");

        point3.x = json.getInteger("point3.x");
        point3.y = json.getInteger("point3.y");

        setPosition(
                new Point(
                        (point1.x+point2.x+point3.x) / 3,
                        (point1.y+point2.y+point3.y) / 3
                )
        );
    }
}
