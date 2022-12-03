package backend;

import backend.shapes.interfaces.Shape;

public interface DrawingEngine {
    void addShape(Shape shape);

    void removeShape(Shape shape);

    Shape[] getShapes();

    void refresh();
}
