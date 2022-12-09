package backend.shapes;

import backend.interfaces.Shape;
import org.json.simple.JsonObject;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.util.Map;

public abstract class AbstractShapeClass implements Shape {

    private static int key = 1;
    protected String seed = String.format("%02d", (key++));

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
        if(getFillColor() != null) {
            canvas.setColor(getFillColor());
            drawFill(canvas);
        }

        canvas.setColor(getColor());
        drawOutline(canvas);
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

    public static void resetNumbering() {
        key = 1;
    }

    public static Shape fromString(JsonObject shapeJson) {
        String type = shapeJson.getString("type");

        try {
            Class shapeClass = Class.forName(type);

            Constructor<Shape> constructor = shapeClass.getConstructor();

            Shape shape = constructor.newInstance();

            shape.fromJSON(shapeJson);

            return shape;
        }catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public JsonObject toJSON() {
        JsonObject json = new JsonObject();

        json.put("type", this.getClass().getName());

        JsonObject color = null;
        if (this.color != null){
            color = new JsonObject();
            color.put("r", this.color.getRed());
            color.put("g", this.color.getGreen());
            color.put("b", this.color.getBlue());
        }
        json.put("color", color);

        JsonObject fillColor = null;
        if (this.fillColor != null){
            fillColor = new JsonObject();
            fillColor.put("r", this.fillColor.getRed());
            fillColor.put("g", this.fillColor.getGreen());
            fillColor.put("b", this.fillColor.getBlue());
        }
        json.put("fillColor", fillColor);

        return json;
    }

    public void fromJSON(JsonObject json) {
        Map<String, BigDecimal> color = json.getMap("color");

        if(color != null)
            setColor(new Color(
                    color.get("r").intValue(),
                    color.get("g").intValue(),
                    color.get("b").intValue()
            ));

        Map<String, BigDecimal> fillColor = json.getMap("fillColor");

        if(fillColor != null)
            setFillColor(new Color(
                    fillColor.get("r").intValue(),
                    fillColor.get("g").intValue(),
                    fillColor.get("b").intValue()
            ));
    }

    private int degree;

    public void rotate(int degree)
    {
        this.degree = degree;
    }

    public int getDegree() {
        return degree;
    }

    public Shape clone() {
        try {
            AbstractShapeClass clonedShape = (AbstractShapeClass) super.clone();
            clonedShape.seed = String.format("%02d", key++);

            return clonedShape;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    abstract public Point[] points();

    abstract public String toString();

    public abstract double area();
}
