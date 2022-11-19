package backend;

import backend.events.ShapesChangedListener;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

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

        this.refresh(canvas);
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

        shapes.forEach(shape -> shape.draw(canvas));

        listeners.forEach(ShapesChangedListener::refreshed);
    }

    public Integer getShapeIndexAtPoint(Point point)
    {
        Shape selectedShape = shapes.stream()
                .filter((shape) -> shape.isPointInside(point))
                .min(Comparator.comparing(Shape::area))
                .orElse(null);

        if(selectedShape == null) return null;

        return shapes.indexOf(selectedShape);
    }

    public void refresh() throws NullPointerException {
        if(this.canvas == null) {
            throw new NullPointerException("Canvas must not be null");
        }

        refresh(this.canvas);
    }
}
