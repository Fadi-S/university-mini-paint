package backend.interfaces;

public interface DrawingEngine {
    void addShape(Shape shape);

    void removeShape(Shape shape);

    Shape[] getShapes();

    void refresh();
}
