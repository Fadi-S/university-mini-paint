package backend.interfaces;

import java.awt.*;

public interface Resizable {
    Point[] points();

    Point resize(Point corner, Point to);
}
