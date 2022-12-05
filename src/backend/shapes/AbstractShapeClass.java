package backend.shapes;

import backend.interfaces.Movable;
import backend.interfaces.Shape;

import java.awt.*;

public abstract class AbstractShapeClass implements Shape, Movable {

    private static int key = 1;
    protected final String seed = String.format("%02d", (key++));

    private Color color;
    private Color fillColor;
    private Point point = new Point();

    private Point draggingPoint;

    public AbstractShapeClass(Point point)
    {
        setPosition(point);
    }

    @Override
    public void setPosition(Point position) {
        this.point = position;
    }

    @Override
    public Point getPosition() {
        return this.point;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public Color getColor() {
        return this.color == null ? Color.black : this.color;
    }

    @Override
    public void setFillColor(Color color) {
        this.fillColor = color;
    }

    @Override
    public Color getFillColor() {
        return this.fillColor;
    }

    @Override
    public void draw(Graphics canvas) {
        canvas.setColor(getColor());
        drawOutline(canvas);

        if(getFillColor() != null) {
            canvas.setColor(getFillColor());
            drawFill(canvas);
        }
    }

    protected abstract void drawOutline(Graphics canvas);
    protected abstract void drawFill(Graphics canvas);

    @Override
    public abstract boolean contains(Point point);

    @Override
    public void setDraggingPoint(Point point) {
        this.draggingPoint = point;
    }

    @Override
    public Point getDraggingPoint() {
        return draggingPoint;
    }

    @Override
    public void moveTo(Point point) {
        Point reference = getDraggingPoint();
        Point oldPoint = getPosition();

        point.x += oldPoint.x - reference.x;
        point.y += oldPoint.y - reference.y;

        setPosition(point);
    }

    abstract public String toString();

    public abstract double area();
}
