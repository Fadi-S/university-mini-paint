package backend.interfaces;

import backend.interfaces.Shape;

import java.util.EventListener;

public interface ShapesChangedListener extends EventListener {
    void shapeAdded(Shape shape);
    void shapeRemoved(Shape shape);
}
