package backend.shapes.creator;

import backend.interfaces.Shape;
import backend.shapes.Square;

import java.awt.*;

public class SquareCreator extends DefaultShapeCreator {
    @Override
    public String[] properties() {
        return new String[] { "x", "y", "side" };
    }

    @Override
    public Shape create() {
        Square square = new Square(new Point(get("x"), get("y")), get("side"));

        square.setColor(this.color);
        square.setFillColor(this.fillColor);

        return square;
    }

    @Override
    public String getName() {
        return "square";
    }
}
