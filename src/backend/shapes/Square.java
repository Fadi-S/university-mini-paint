package backend.shapes;

import backend.Shape;

import java.awt.*;

public class Square extends Rectangle implements Shape {
    @Override
    public String[] properties() {
        return new String[]{
           "side"
        };
    }

    @Override
    public boolean isPointInside(Point point) {
        set("height", get("side"));
        set("width", get("side"));

        return super.isPointInside(point);
    }

    @Override
    public String getKey() {
        return "square-" + seed;
    }

    @Override
    public void draw(Graphics canvas) {
        set("height", get("side"));
        set("width", get("side"));

        super.draw(canvas);
    }
}
