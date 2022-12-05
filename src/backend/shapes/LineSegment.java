package backend.shapes;

import backend.interfaces.Shape;
import backend.utils.Distance;

import java.awt.*;

public class LineSegment extends AbstractShapeClass implements Shape {

    private final Point point1;
    private final Point point2;

    public LineSegment(Point point1, Point point2) {
        this.point1 = point1;
        this.point2 = point2;

        setPosition(new Point(
                (point1.x + point2.x) / 2,
                (point1.y + point2.y) / 2
        ));
    }

    @Override
    public String getKey() {
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
        double lineLength = Distance.between(point1, point2);
        double lengthFromPoint = Distance.between(point, point1) + Distance.between(point, point2);

        return Math.abs(lengthFromPoint - lineLength) <= 2;
    }

    public double area() {
        return Distance.between(point1, point2);
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

    public String[] properties() {
        return new String[]{
                "x2", "y2"
        };
    }
}
