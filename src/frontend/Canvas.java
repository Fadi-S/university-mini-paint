package frontend;

import backend.interfaces.DrawingEngine;
import backend.interfaces.ShapesChangedListener;
import backend.interfaces.Shape;
import backend.shapes.AbstractShapeClass;
import org.json.simple.JsonArray;
import org.json.simple.JsonObject;

import javax.swing.*;
import javax.swing.plaf.BorderUIResource;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Stream;

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

    public String toJSON() {
        JsonObject json = new JsonObject();

        JsonArray shapesJSON = new JsonArray();

        shapes.forEach((shape) -> shapesJSON.add(shape.toJSON()));

        json.put("shapes", shapesJSON);

        return json.toJson();
    }

    public void fromJSON(JsonObject json) {
        removeAll();
        Collection<JsonObject> shapesJson = json.getCollection("shapes");

        for (JsonObject shapeJson : shapesJson) {
            Shape shape = AbstractShapeClass.fromString(shapeJson);

            if(shape != null)
                this.addShape(shape);
        }
    }

    public void removeAll() {
        shapes.clear();

        refresh();

        listeners.forEach(ShapesChangedListener::shapesCleared);
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

    public Shape getShapeAtPoint(Point point, boolean chooseMinimum)
    {
        Stream<Shape> shapeStream =  shapes.stream()
                .filter((shape) -> shape.contains(point));

        if(chooseMinimum) {
            return shapeStream.min(Comparator.comparing((shape) -> ((AbstractShapeClass) shape).area()))
                    .orElse(null);
        }

        Shape[] selectedShapes = shapeStream.toArray(Shape[]::new);

        if(selectedShapes.length == 0) {
            return null;
        }

        return selectedShapes[selectedShapes.length-1];
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

        Point[] points = selectedShape.points();
        for (Point point : points) {
            graphics.fillRect(point.x-4, point.y-4, 8, 8);
        }
    }
}
