package frontend;

import backend.interfaces.DrawingEngine;
import backend.interfaces.ShapesChangedListener;
import backend.interfaces.Shape;
import backend.shapes.AbstractShapeClass;

import javax.swing.*;
import javax.swing.plaf.BorderUIResource;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

public class Canvas extends JPanel implements DrawingEngine {
    private Shape selectedShape;

    public Canvas() {
        setBackground(Color.white);
        setBorder(new BorderUIResource.LineBorderUIResource(Color.black));
    }

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
        return shapes.toArray(Shape[]::new);
    }

    public Shape getShapeAtPoint(Point point)
    {
        return shapes.stream()
                .filter((shape) -> shape.contains(point))
                .min(Comparator.comparing((shape) -> ((AbstractShapeClass) shape).area()))
                .orElse(null);
    }

    public void setSelectedShape(Shape selectedShape) {
        this.selectedShape = selectedShape;
    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        shapes.forEach(shape -> shape.draw(graphics));

        drawCenterPoint(graphics);
    }

    @Override
    public void refresh() {
        this.repaint();
    }

    private void drawCenterPoint(Graphics graphics)
    {
        if(selectedShape == null) return;

        Point center = selectedShape.getPosition();
        graphics.setColor(Color.black);
        graphics.drawLine(center.x, center.y-2, center.x, center.y+2);
        graphics.drawLine(center.x-2, center.y, center.x+2, center.y);
    }
}
