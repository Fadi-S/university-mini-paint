package backend.shapes;

import backend.interfaces.Shape;

import java.awt.*;

public class Square extends Rectangle implements Shape {
    public Square(Point point, int side) {
        super(point, side, side);
    }

    @Override
    public String toString() {
        return "square-" + seed;
    }
}
