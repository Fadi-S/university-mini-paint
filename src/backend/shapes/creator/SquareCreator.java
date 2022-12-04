package backend.shapes.creator;

import backend.interfaces.Shape;
import backend.shapes.Square;

public class SquareCreator extends DefaultShapeCreator {
    @Override
    public String[] properties() {
        return new String[] { "side" };
    }

    @Override
    public Shape create() {
        Square square = new Square(this.point, get("side"));

        square.setColor(this.color);
        square.setFillColor(this.fillColor);

        return square;
    }

    @Override
    public String getName() {
        return "square";
    }
}
