package backend.utils;

import java.awt.*;

public class Distance {
    public static Double between(Point point1, Point point2) {
        return point1.distance(point2); // Math.sqrt(Math.pow(point1.x - point2.x, 2) + Math.pow(point1.y - point2.y, 2));
    }
}
