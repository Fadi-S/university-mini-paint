package frontend;

import backend.Engine;
import backend.interfaces.Shape;

import javax.swing.plaf.BorderUIResource;
import java.awt.*;

public class Canvas extends Engine {
    private Shape selectedShape;

    public Canvas() {
        setBackground(Color.white);
        setBorder(new BorderUIResource.LineBorderUIResource(Color.black));
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
