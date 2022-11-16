package frontend;

import backend.Engine;
import backend.Shape;
import backend.events.ShapesChangedListener;
import backend.shapes.*;
import backend.shapes.Rectangle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Application {
    private final JFrame frame;
    private JPanel panel;
    private JButton circleBtn;
    private JButton lineSegmentBtn;
    private JButton squareBtn;
    private JButton rectangleBtn;
    private JButton colorizeBtn;
    private JPanel canvas;
    private JButton deleteBtn;
    private JLabel selectedShapeLabel;
    private JComboBox<String> shapesSelectBox;

    private final Engine engine;

    Shape selectedShape;

    public static void main(String[] args) {
        new Application();
    }

    Application()
    {
        frame = new JFrame("Vector Drawing Application");
        frame.setSize(1000, 600);
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);

        engine = new Engine(canvas.getGraphics());
        engine.addListener(new ShapesChangedListener() {
            @Override
            public void shapeAdded(Shape shape) {
                shapesSelectBox.addItem(shape.getKey());
            }
            @Override
            public void shapeRemoved(Shape shape) {
                shapesSelectBox.removeItem(shape.getKey());

                select(null);
            }
        });

        shapesSelectBox.addItemListener(event -> {
            if (event.getStateChange() != ItemEvent.SELECTED) {
                return;
            }

            String shapeKey = (String) event.getItem();

            Shape selectShape = null;
            for (Shape shape : engine.getShapes()) {
                if(shape.getKey().equals(shapeKey)) {
                    selectShape = shape;
                    break;
                }
            }
            if(selectShape == null) return;

            select(selectShape);
        });

        circleBtn.addActionListener((e) -> create(new Circle()));
        lineSegmentBtn.addActionListener((e) -> create(new LineSegment()));
        squareBtn.addActionListener((e) -> create(new Square()));
        rectangleBtn.addActionListener((e) -> create(new Rectangle()));

        deleteBtn.addActionListener((e) -> {
            if(selectedShape == null) {
                JOptionPane.showMessageDialog(frame, "No shape selected");
                return;
            }
            engine.removeShape(selectedShape);
        });

        colorizeBtn.addActionListener((e) -> {
            if(selectedShape == null) {
                JOptionPane.showMessageDialog(frame, "No shape selected");
                return;
            }

            selectedShape.setColor(Color.decode("#4C5CFF"));
            selectedShape.setFillColor(Color.decode("#3ECAFF"));

            engine.refresh();
        });

        canvas.addMouseListener(new MouseListener() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(! SwingUtilities.isLeftMouseButton(e)) {
                    return;
                }
                Integer shapeIndex = engine.getShapeIndexAtPoint(e.getPoint());
                select(shapeIndex == null ? null : engine.getShapes()[shapeIndex]);
            }
            public void mouseClicked(MouseEvent e) {}
            public void mouseReleased(MouseEvent e) {}
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
        });
    }

    private void select(Shape shape) {
        if(shape == null) {
            selectedShape = null;
            selectedShapeLabel.setText("No shape selected");
            shapesSelectBox.setSelectedItem(null);
            engine.refresh();
            return;
        }

        selectedShape = shape;
        selectedShapeLabel.setText("Shape: " + selectedShape.getKey());

        if(!shape.getKey().equals(shapesSelectBox.getSelectedItem()))
            shapesSelectBox.setSelectedItem(shape.getKey());

        engine.refresh();
        drawCenterPoint();
    }

    private void drawCenterPoint()
    {
        Point center = selectedShape.getPosition();
        canvas.getGraphics().fillRect(center.x-1, center.y-3, 2, 6);
        canvas.getGraphics().fillRect(center.x-3, center.y-1, 6, 2);
    }

    private void create(DefaultShape shape) {
        if(collectProperties(shape))
            engine.addShape(shape);
    }

    private boolean collectProperties(DefaultShape shape)
    {
        String[] props = shape.properties();

        int i = 0;
        while (i < props.length) {
            String property = props[i];
            String value = JOptionPane.showInputDialog("Enter " + property + ": ");
            if(value == null) {
                return false;
            }

            try {
                shape.set(property, Double.parseDouble(value));
                i++;
            }catch (Exception error) {
                JOptionPane.showMessageDialog(frame, "Please enter a number!");
            }
        }

        return true;
    }
}
