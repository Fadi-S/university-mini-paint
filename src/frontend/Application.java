package frontend;

import backend.Engine;
import backend.shapes.interfaces.Shape;
import backend.events.ShapesChangedListener;
import backend.shapes.*;
import backend.shapes.Rectangle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

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
    private JLabel sizeLabel;
    private JLabel mousePosition;
    private JPanel controlsPanel;
    private JPanel shapesButtonsPanel;
    private JButton triangleBtn;

    private final Canvas canvasEngine = new Canvas();

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
        sizeLabel.setText(canvas.getWidth() + "x" + canvas.getHeight());
        canvas.add(canvasEngine);

        canvasEngine.addListener(new ShapesChangedListener() {
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
            if (event.getStateChange() != ItemEvent.SELECTED || !shapesSelectBox.hasFocus()) {
                return;
            }
            String shapeKey = (String) event.getItem();
            Shape selectShape = null;
            for (Shape shape : canvasEngine.getShapes()) {
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
        triangleBtn.addActionListener((e) -> create(new Triangle()));

        deleteBtn.addActionListener((e) -> {
            if(selectedShape == null) {
                JOptionPane.showMessageDialog(frame, "No shape selected");
                return;
            }
            canvasEngine.removeShape(selectedShape);
        });

        colorizeBtn.addActionListener((e) -> {
            if(selectedShape == null) {
                JOptionPane.showMessageDialog(frame, "No shape selected");
                return;
            }

            chooseColor((DefaultShape) selectedShape);
        });

        canvas.addMouseListener(new MouseListener() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(! SwingUtilities.isLeftMouseButton(e)) {
                    return;
                }

                Integer shapeIndex = canvasEngine.getShapeIndexAtPoint(e.getPoint());

                select(shapeIndex == null ? null : canvasEngine.getShapes()[shapeIndex]);
            }
            public void mouseClicked(MouseEvent e) {}
            public void mouseReleased(MouseEvent e) {}
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
        });

        canvas.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if(selectedShape == null) return;

                selectedShape.moveTo(e.getPoint());

                canvasEngine.refresh();
            }
            @Override
            public void mouseMoved(MouseEvent e) {
                mousePosition.setText("Mouse: " + e.getX() + "x" + e.getY());
            }
        });
    }

    private void select(Shape shape) {
        canvasEngine.setSelectedShape(shape);

        if(shape == null) {
            selectedShape = null;
            selectedShapeLabel.setText("No shape selected");
            shapesSelectBox.setSelectedItem(null);
            canvasEngine.refresh();
            return;
        }

        selectedShape = shape;
        selectedShapeLabel.setText("Shape: " + selectedShape.getKey());

        if(!shape.getKey().equals(shapesSelectBox.getSelectedItem()))
            shapesSelectBox.setSelectedItem(shape.getKey());

        canvasEngine.refresh();
    }

    private void enable(boolean value) {
        Arrays.stream(controlsPanel.getComponents()).forEach(i -> i.setEnabled(value));
        Arrays.stream(shapesButtonsPanel.getComponents()).forEach(i -> i.setEnabled(value));
    }

    private JFrame shapePropertiesForm = null;

    private void create(DefaultShape shape) {
        if(shapePropertiesForm != null) {
            shapePropertiesForm.requestFocus();
            return;
        }

        PropertiesForm form = new PropertiesForm(shape, canvas);
        shapePropertiesForm = form.getFrame();
        form.getData().whenComplete((Boolean shouldRender, Object o2) -> {
            shapePropertiesForm = null;

            if(!shouldRender) return;

            canvasEngine.addShape(shape);
        });
    }

    private void chooseColor(DefaultShape shape) {
        ColorPicker colorPicker = new ColorPicker(shape);
        enable(false);
        colorPicker.getColors().whenComplete((Boolean shouldRender, Object o2) -> {
            enable(true);

            canvasEngine.refresh();
        });
    }
}
