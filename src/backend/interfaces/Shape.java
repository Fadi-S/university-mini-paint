package backend.interfaces;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public interface Shape extends Movable, Resizable, Savable, Cloneable {
    void setPosition(Point position);
    Point getPosition();

    void setColor(Color color);
    Color getColor();

    void setFillColor(Color color);
    Color getFillColor();

    void draw(Graphics canvas);
}
