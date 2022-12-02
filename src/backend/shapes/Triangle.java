package backend.shapes;

import backend.utils.Distance;

import java.awt.*;

public class Triangle extends DefaultShape {

    @Override
    protected void drawOutline(Graphics canvas) {
        Point[] points = getPoints();

        canvas.drawPolygon(
                new int[]{points[0].x, points[1].x, points[2].x,},
                new int[]{points[0].y, points[1].y, points[2].y,},
                3
        );
    }

    @Override
    protected void drawFill(Graphics canvas) {
        Point[] points = getPoints();

        canvas.fillPolygon(
                new int[]{points[0].x, points[1].x, points[2].x,},
                new int[]{points[0].y, points[1].y, points[2].y,},
                3
        );
    }

    private Point[] getPoints()
    {
        final Point p1 = getPosition();
        final Point p2 = new Point(get("x2").intValue(), get("y2").intValue());
        final Point p3 = new Point(get("x3").intValue(), get("y3").intValue());

        return new Point[] {
                p1, p2, p3
        };
    }

    private double sign(Point p1, Point p2, Point p3) {
        return (p1.x - p3.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p3.y);
    }

    @Override
    public boolean contains(Point point) {
        Point[] points = getPoints();

        double d1, d2, d3;
        boolean hasNegative, hasPositive;

        d1 = sign(point, points[0], points[1]);
        d2 = sign(point, points[1], points[2]);
        d3 = sign(point, points[2], points[0]);

        hasNegative = (d1 < 0) || (d2 < 0) || (d3 < 0);
        hasPositive = (d1 > 0) || (d2 > 0) || (d3 > 0);

        return !(hasNegative && hasPositive);
    }

    @Override
    public String getKey() {
        return "triangle-" + seed;
    }

    @Override
    public double area() {
        Point[] points = getPoints();

        double a = Distance.between(points[0], points[1]);
        double b = Distance.between(points[0], points[2]);
        double c = Distance.between(points[1], points[2]);

        double s = (a+b+c) / 2; // semi perimeter

        return Math.sqrt(s * (s-a) * (s-b) * (s-c));
    }

    public void moveTo(Point point) {
        Point oldPoint = getPosition();

        int x = point.x - oldPoint.x;
        int y = point.y - oldPoint.y;

        set("x2", get("x2") + x);
        set("x3", get("x3") + x);
        set("y2", get("y2") + y);
        set("y3", get("y3") + y);

        setDraggingPoint(point);
    }

    @Override
    public String[] properties() {
        return new String[]{
                "x2", "y2", "x3", "y3"
        };
    }
}
