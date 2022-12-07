package backend.shapes.creator;

import backend.interfaces.Shape;
import backend.shapes.Oval;

import java.awt.*;

public class OvalCreator extends DefaultShapeCreator {
    @Override
    public String[] properties() {
        return new String[] { "x", "y", "width", "height" };
    }

    public String getName() {
        return "circle";
    }

    @Override
    public Shape create() {
        Oval oval = new Oval(new Point(get("x"), get("y")), get("width"), get("height"));

        oval.setColor(this.color);
        oval.setFillColor(this.fillColor);

        return oval;
    }
}
