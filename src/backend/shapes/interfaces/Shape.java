package backend.shapes.interfaces;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public interface Shape extends Movable {

    void setPosition(Point position);
    Point getPosition();

    void setColor(Color color);
    Color getColor();
    void setFillColor(Color color);
    Color getFillColor();
    void draw(Graphics canvas);

    String getKey();
    double area();
}
