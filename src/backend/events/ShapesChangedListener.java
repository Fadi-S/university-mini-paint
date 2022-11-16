package backend.events;

import backend.Shape;

import java.util.EventListener;

public interface ShapesChangedListener extends EventListener {
    void shapeAdded(Shape shape);
    void shapeRemoved(Shape shape);
}
