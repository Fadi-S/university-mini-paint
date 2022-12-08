package backend.shapes;

import java.awt.*;

public class Square extends Rectangle {
    public Square(Point point, int side) {
        super(point, side, side);
    }
    public Square() {super();}

    @Override
    public String toString() {
        return "square-" + seed;
    }
}
