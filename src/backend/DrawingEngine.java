package backend;

import backend.shapes.interfaces.Shape;

import java.awt.Graphics;

public interface DrawingEngine {
    void addShape(Shape shape);

    void removeShape(Shape shape);

    Shape[] getShapes();

    void refresh(Graphics canvas);
}
