package backend.shapes.creator;

import backend.interfaces.Shape;
import backend.shapes.LineSegment;

import java.awt.*;

public class LineSegmentCreator extends DefaultShapeCreator {

    public LineSegmentCreator() {
        set("outlineOnly", 1);
    }

    @Override
    public String[] properties() {
        return new String[] { "x2", "y2" };
    }

    @Override
    public Shape create() {
        LineSegment line = new LineSegment(this.point, new Point(get("x1"), get("y2")));

        line.setColor(this.color);

        return line;
    }

    @Override
    public String getName() {
        return "line";
    }
}
