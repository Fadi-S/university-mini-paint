package backend.shapes;

import backend.utils.Distance;

import java.awt.*;

public class Triangle extends AbstractShapeClass {

    private final Point point1;
    private final Point point2;
    private final Point point3;

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
        double a = Distance.between(point1, point2);
        double b = Distance.between(point1, point3);
        double c = Distance.between(point2, point3);

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
}
