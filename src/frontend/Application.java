package frontend;

import backend.DefaultEngine;
import backend.DrawingEngine;
import backend.shapes.*;
import backend.shapes.Rectangle;

import javax.swing.*;
import java.awt.*;

public class Application {
    private final JFrame frame;
    private JPanel panel;
    private JButton circleBtn;
    private JButton lineSegmentBtn;
    private JButton squareBtn;
    private JButton rectangleBtn;
    private JButton colorizeBtn;
    private JButton deleteBtn;
    private JPanel canvas;

    private DrawingEngine engine;

    public static void main(String[] args) {
        new Application();
    }

    Application()
    {
        frame = new JFrame("Vector Drawing Application");
        frame.setSize(800, 500);
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        engine = new DefaultEngine(canvas.getGraphics());

        circleBtn.addActionListener((e) -> create(new Circle()));

        lineSegmentBtn.addActionListener((e) -> create(new LineSegment()));

        squareBtn.addActionListener((e) -> create(new Square()));

        rectangleBtn.addActionListener((e) -> create(new Rectangle()));
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
                shape.set(property, Double.parseDouble(value));
                i++;
            }catch (NumberFormatException error) {
                JOptionPane.showMessageDialog(frame, "Please enter a number!");
            }
        }
    }
}
