package backend.shapes;

import backend.shapes.interfaces.Shape;
import backend.utils.Distance;

import java.awt.*;

public class LineSegment extends DefaultShape implements Shape {

    public LineSegment() {
        super();

        set("outlineOnly", 1.0);
    }

    @Override
    public String getKey() {
        return "line-" + seed;
    }

    @Override
    protected void drawOutline(Graphics canvas) {
        Point startPoint = getPosition();
        int x2 = get("x2").intValue();
        int y2 = get("y2").intValue();
        canvas.drawLine(startPoint.x, startPoint.y, x2, y2);
    }

    @Override
    protected void drawFill(Graphics canvas) {
        drawOutline(canvas);
    }

    @Override
    public boolean contains(Point point) {
        Point startPoint = getPosition();
        Point endPoint = new Point(get("x2").intValue(), get("y2").intValue());

        double lineLength = Distance.between(startPoint, endPoint);
        double lengthFromPoint = Distance.between(point, startPoint) + Distance.between(point, endPoint);

        return Math.abs(lengthFromPoint - lineLength) <= 2;
    }

    public double area() {
        Point endPoint = new Point(get("x2").intValue(), get("y2").intValue());
        Point startPoint = getPosition();

        return Distance.between(startPoint, endPoint);
    }

    public void moveTo(Point point) {
        Point oldPoint = getPosition();

        int x = point.x - oldPoint.x;
        int y = point.y - oldPoint.y;

        set("x2", get("x2") + x);
        set("y2", get("y2") + y);

        setDraggingPoint(point);
    }

    public String[] properties() {
        return new String[]{
                "x2", "y2"
        };
    }
}
