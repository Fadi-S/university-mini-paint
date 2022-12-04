package backend;

import backend.interfaces.Shape;

public interface DrawingEngine {
    void addShape(Shape shape);

    void removeShape(Shape shape);

    Shape[] getShapes();

    void refresh();
}
