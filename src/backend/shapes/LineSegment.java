package backend.shapes;

import backend.interfaces.Shape;
import org.json.simple.JsonObject;

import java.awt.*;

public class LineSegment extends AbstractShapeClass {

    protected Point point1;
    protected Point point2;

    public LineSegment(Point point1, Point point2) {
        super(new Point(
                (point1.x + point2.x) / 2,
                (point1.y + point2.y) / 2
        ));
        this.point1 = point1;
        this.point2 = point2;
    }

    public LineSegment() {
        super(new Point());

        point1 = new Point();
        point2 = new Point();
    }

    @Override
    public String toString() {
        return "line-" + seed;
    }

    @Override
    protected void drawOutline(Graphics canvas) {
        canvas.drawLine(point1.x, point1.y, point2.x, point2.y);
    }

    @Override
    protected void drawFill(Graphics canvas) {
        drawOutline(canvas);
    }

    @Override
    public boolean contains(Point point) {
        double lineLength = point1.distance(point2);
        double lengthFromPoint = point.distance(point1) + point.distance(point2);

        return Math.abs(lengthFromPoint - lineLength) <= 2;
    }

    public double area() {
        return point1.distance(point2);
    }

    public void moveTo(Point point) {
        Point reference = getDraggingPoint();
        Point oldPoint = getPosition();

        point.x += oldPoint.x - reference.x;
        point.y += oldPoint.y - reference.y;

        int x = point.x - oldPoint.x;
        int y = point.y - oldPoint.y;

        point1.x += x;
        point1.y += y;

        point2.x += x;
        point2.y += y;

        setPosition(new Point(
                (point1.x + point2.x) / 2,
                (point1.y + point2.y) / 2
        ));
    }

    @Override
    public Point[] points() {
        return new Point[] {
                point1,
                point2,
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

        setPosition(
                new Point(
                        (point1.x+point2.x) / 2,
                        (point1.y+point2.y) / 2
                )
        );

        return p;
    }

    public Shape clone() {
        LineSegment line = (LineSegment) super.clone();

        line.point1 = new Point(line.point1.x, line.point1.y);
        line.point2 = new Point(line.point2.x, line.point2.y);

        return line;
    }

    @Override
    public JsonObject toJSON() {
        JsonObject json = super.toJSON();

        json.put("point1.x", point1.x);
        json.put("point1.y", point1.y);

        json.put("point2.x", point2.x);
        json.put("point2.y", point2.y);

        return json;
    }

    @Override
    public void fromJSON(JsonObject json) {
        super.fromJSON(json);

        point1.x = json.getInteger("point1.x");
        point1.y = json.getInteger("point1.y");
        point2.x = json.getInteger("point2.x");
        point2.y = json.getInteger("point2.y");

        setPosition(new Point(
                (point1.x + point2.x) / 2,
                (point1.y + point2.y) / 2
        ));
    }
}
