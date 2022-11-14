package backend.shapes;

import backend.Shape;

import java.awt.*;

public class LineSegment extends Rectangle implements Shape {

    public String[] properties() {
        return new String[]{
                "x", "y", "width"
        };
    }

    @Override
    public void draw(Graphics canvas) {
        set("height", 1.0);

        super.draw(canvas);
    }

    @Override
    public String getKey() {
        return "line-" + seed;
    }

    @Override
    public boolean isPointInside(Point point) {
        set("height", 5.0);

        return super.isPointInside(point);
    }
}
