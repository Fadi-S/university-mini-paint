package backend.shapes;

import backend.Shape;

import java.awt.*;

public class Square extends Rectangle implements Shape {
    @Override
    public String[] properties() {
        return new String[]{
            "x", "y", "side"
        };
    }

    @Override
    public void draw(Graphics canvas) {
        set("height", get("side"));
        set("width", get("side"));

        super.draw(canvas);
    }
}
