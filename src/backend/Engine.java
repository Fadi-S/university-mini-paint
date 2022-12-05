package backend;

import backend.events.ShapesChangedListener;
import backend.shapes.AbstractShapeClass;
import backend.interfaces.Shape;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

abstract public class Engine extends JPanel implements DrawingEngine {

    protected final ArrayList<Shape> shapes = new ArrayList<>();
    protected final ArrayList<ShapesChangedListener> listeners = new ArrayList<>();

    public void addListener(ShapesChangedListener toAdd) {
        listeners.add(toAdd);
    }

    @Override
    public void addShape(Shape shape) {
        shapes.add(shape);

        refresh();

        listeners.forEach(listener -> listener.shapeAdded(shape));
    }

    @Override
    public void removeShape(Shape shape) {
        shapes.removeIf(currentShape -> currentShape.toString().equals(shape.toString()));

        refresh();

        listeners.forEach(listener -> listener.shapeRemoved(shape));
    }

    @Override
    public Shape[] getShapes() {
        return shapes.toArray(new Shape[0]);
    }

    public Integer getShapeIndexAtPoint(Point point)
    {
        Shape selectedShape = shapes.stream()
                .filter((shape) -> shape.contains(point))
                .min(Comparator.comparing((shape) -> ((AbstractShapeClass) shape).area()))
                .orElse(null);

        if(selectedShape == null) return null;

        return shapes.indexOf(selectedShape);
    }
}
