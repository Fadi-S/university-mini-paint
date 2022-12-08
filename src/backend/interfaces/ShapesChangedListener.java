package backend.interfaces;

import java.util.EventListener;

public interface ShapesChangedListener extends EventListener {
    void shapeAdded(Shape shape);
    void shapeRemoved(Shape shape);
    void shapesCleared();
}
