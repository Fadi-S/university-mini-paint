package backend.shapes;

import backend.shapes.interfaces.Shape;

public class Square extends Rectangle implements Shape {

    @Override
    public void set(String property, Double value) {
        super.set(property, value);

        if (property.equals("side")) {
            super.set("height", value);
            super.set("width", value);
        }
    }

    @Override
    public String getKey() {
        return "square-" + seed;
    }

    @Override
    public String[] properties() {
        return new String[]{
                "side"
        };
    }
}
