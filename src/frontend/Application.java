package frontend;

import backend.DefaultEngine;
import backend.Shape;
import backend.shapes.*;
import backend.shapes.Rectangle;

import javax.swing.*;
import java.awt.*;
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

    private DefaultEngine engine;

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
        engine = new DefaultEngine(canvas.getGraphics());

        circleBtn.addActionListener((e) -> create(new Circle()));

        lineSegmentBtn.addActionListener((e) -> create(new LineSegment()));

        squareBtn.addActionListener((e) -> create(new Square()));

        rectangleBtn.addActionListener((e) -> create(new Rectangle()));

        deleteBtn.addActionListener((e) -> {
            if(selectedShape == null) {
                JOptionPane.showMessageDialog(frame, "No shape selected");

                return;
            };

            engine.removeShape(selectedShape);

            selectedShape = null;
            selectedShapeLabel.setText("No Shape selected");
        });

        colorizeBtn.addActionListener((e) -> {
            if(selectedShape == null) {
                JOptionPane.showMessageDialog(frame, "No shape selected");

                return;
            }

            selectedShape.setColor(Color.red);

            engine.refresh();
        });

        canvas.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {
                Integer shapeIndex = engine.getShapeIndexAtPoint(e.getPoint());

                if(shapeIndex == null) {
                    selectedShape = null;

                    selectedShapeLabel.setText("No shape selected");

                    engine.refresh();

                    return;
                }

                selectedShape = engine.getShapes()[shapeIndex];

                selectedShapeLabel.setText("Shape: " + selectedShape.getKey());

                engine.refresh();

                drawCenterPoint();
            }
            public void mouseReleased(MouseEvent e) {}
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
        });

        frame.setResizable(false);
    }

    private void drawCenterPoint()
    {
        Point center = selectedShape.getPosition();
        canvas.getGraphics().fillRect(center.x-1, center.y-3, 2, 6);
        canvas.getGraphics().fillRect(center.x-3, center.y-1, 6, 2);
    }

    private void create(DefaultShape shape) {
        collectProperties(shape);

        engine.addShape(shape);
    }

    private void collectProperties(DefaultShape shape)
    {
        String[] props = shape.properties();

        int i = 0;
        while (i < props.length) {
            String property = props[i];
            String value = JOptionPane.showInputDialog("Enter " + property + ": ");
            try {
                if(value == null || value.isBlank()) {
                    throw new NumberFormatException();
                }
                shape.set(property, Double.parseDouble(value));
                i++;
            }catch (Exception error) {
                JOptionPane.showMessageDialog(frame, "Please enter a number!");
            }
        }
    }
}
