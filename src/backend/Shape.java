package backend;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Map;

public interface Shape {

    void setPosition(Point position);
    Point getPosition();

    void setProperties(Map<String, Double> properties);
    Map<String, Double> getProperties();

    void setColor(Color color);
    Color getColor();
    void setFillColor(Color color);
    Color getFillColor();

    boolean isPointInside(Point point);

    String getKey();

    double area();

    void draw(Graphics canvas);
}
