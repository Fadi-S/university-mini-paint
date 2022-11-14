package backend;

import java.awt.*;
import java.util.ArrayList;

public class DefaultEngine implements DrawingEngine {

    private final ArrayList<Shape> shapes = new ArrayList<>();
    private Graphics canvas = null;

    public DefaultEngine() {}

    public DefaultEngine(Graphics canvas) {
        this.canvas = canvas;
    }

    @Override
    public void addShape(Shape shape) {
        shapes.add(shape);

        if(this.canvas != null)
            refresh(this.canvas);
    }

    @Override
    public void removeShape(Shape shape) {
        shapes.removeIf((currentShape) -> currentShape.equals(shape));

        if(this.canvas != null)
            refresh(this.canvas);
    }

    @Override
    public Shape[] getShapes() {
        return shapes.toArray(new Shape[0]);
    }

    @Override
    public void refresh(Graphics canvas) {
        for (Shape shape : shapes) {
            shape.draw(canvas);
        }
    }

    public void refresh() throws NullPointerException {
        if(this.canvas == null) {
            throw new NullPointerException("Canvas must no be null");
        }

        refresh(this.canvas);
    }
}
