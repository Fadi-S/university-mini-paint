package backend;

import backend.events.ShapesChangedListener;

import java.awt.*;
import java.util.ArrayList;

public class Engine implements DrawingEngine {

    private final ArrayList<Shape> shapes = new ArrayList<>();
    private final ArrayList<ShapesChangedListener> listeners = new ArrayList<>();
    private Graphics canvas;

    public Engine(Graphics canvas) {
        this.canvas = canvas;
    }

    public void addListener(ShapesChangedListener toAdd) {
        listeners.add(toAdd);
    }

    public void setCanvas(Graphics canvas) {
        this.canvas = canvas;

        this.refresh();
    }

    @Override
    public void addShape(Shape shape) {
        shapes.add(shape);

        refresh(this.canvas);

        listeners.forEach(listener -> listener.shapeAdded(shape));
    }

    @Override
    public void removeShape(Shape shape) {
        shapes.removeIf(currentShape -> currentShape.getKey().equals(shape.getKey()));

        refresh(this.canvas);

        listeners.forEach(listener -> listener.shapeRemoved(shape));
    }

    @Override
    public Shape[] getShapes() {
        return shapes.toArray(new Shape[0]);
    }

    @Override
    public void refresh(Graphics canvas) {
        canvas.clearRect(0, 0, 9999, 9999);

        for (Shape shape : shapes) {
            shape.draw(this.canvas);
        }

        listeners.forEach(ShapesChangedListener::refreshed);
    }

    public Integer getShapeIndexAtPoint(Point point)
    {
        for (int i=0; i<shapes.size(); i++) {
            if(shapes.get(i).isPointInside(point))
                return i;
        }

        return null;
    }

    public void refresh() throws NullPointerException {
        if(this.canvas == null) {
            throw new NullPointerException("Canvas must not be null");
        }

        refresh(this.canvas);
    }
}
