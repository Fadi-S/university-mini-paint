package backend.shapes;

import backend.Shape;
import backend.utils.Distance;

import java.awt.*;

public class LineSegment extends DefaultShape implements Shape {

    public String[] properties() {
        return new String[]{
                "x2", "y2"
        };
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
    public boolean isPointInside(Point point) {
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
}
