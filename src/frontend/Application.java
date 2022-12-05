package frontend;

import backend.interfaces.Shape;
import backend.interfaces.ShapesChangedListener;
import backend.shapes.*;
import backend.shapes.creator.*;

import javax.swing.*;
import java.awt.event.*;
import java.util.Arrays;

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
        canvas.add(canvasEngine);
        sizeLabel.setText(canvas.getWidth() + "x" + canvas.getHeight());

        canvasEngine.addListener(new ShapesChangedListener() {
            @Override
            public void shapeAdded(Shape shape) {
                shapesSelectBox.addItem(shape.toString());
            }

            @Override
            public void shapeRemoved(Shape shape) {
                shapesSelectBox.removeItem(shape.toString());
                select(null);
            }
        });

        frame.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                sizeLabel.setText(canvasEngine.getWidth() + "x" + canvasEngine.getHeight());
            }
            public void componentMoved(ComponentEvent e) {}
            public void componentShown(ComponentEvent e) {}
            public void componentHidden(ComponentEvent e) {}
        });

        shapesSelectBox.addItemListener(event -> {
            if (event.getStateChange() != ItemEvent.SELECTED || !shapesSelectBox.hasFocus()) {
                return;
            }
            String shapeKey = (String) event.getItem();
            Shape selectShape = null;
            for (Shape shape : canvasEngine.getShapes()) {
                if(shape.toString().equals(shapeKey)) {
                    selectShape = shape;
                    break;
                }
            }
            if(selectShape == null) return;
            select(selectShape);
        });

        circleBtn.addActionListener((e) -> create(new CircleCreator()));
        lineSegmentBtn.addActionListener((e) -> create(new LineSegmentCreator()));
        squareBtn.addActionListener((e) -> create(new SquareCreator()));
        rectangleBtn.addActionListener((e) -> create(new RectangleCreator()));
        triangleBtn.addActionListener((e) -> create(new TriangleCreator()));

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

            chooseColor((AbstractShapeClass) selectedShape);
        });

        canvas.addMouseListener(new MouseListener() {
            @Override
            public void mousePressed(MouseEvent e) {
                Integer shapeIndex = canvasEngine.getShapeIndexAtPoint(e.getPoint());

                select(shapeIndex == null ? null : canvasEngine.getShapes()[shapeIndex]);

                if(selectedShape != null) {
                    selectedShape.setDraggingPoint(e.getPoint());
                }
            }
            public void mouseClicked(MouseEvent e) {}
            public void mouseReleased(MouseEvent e) {}
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
        });

        canvas.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                mousePosition.setText("Mouse: " + e.getX() + "x" + e.getY());

                if(selectedShape == null) return;

                selectedShape.moveTo(e.getPoint());

                selectedShape.setDraggingPoint(e.getPoint());

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
        selectedShape = shape;

        if(shape == null) {
            selectedShapeLabel.setText("No shape selected");
            shapesSelectBox.setSelectedItem(null);
        } else {
            selectedShapeLabel.setText("Shape: " + selectedShape);

            if (!shape.toString().equals(shapesSelectBox.getSelectedItem()))
                shapesSelectBox.setSelectedItem(shape.toString());
        }

        canvasEngine.refresh();
    }

    private void enable(boolean value) {
        Arrays.stream(controlsPanel.getComponents()).forEach(i -> i.setEnabled(value));
        Arrays.stream(shapesButtonsPanel.getComponents()).forEach(i -> i.setEnabled(value));
//        frame.setVisible(value);
    }

    private void create(ShapeCreator shapeCreator) {
        PropertiesForm form = new PropertiesForm(shapeCreator);
        enable(false);
        form.getData().whenComplete((Boolean shouldRender, Object o2) -> {
            enable(true);

            if(!shouldRender) return;

            Shape shape = shapeCreator.create();

            canvasEngine.addShape(shape);
        });
    }

    private void chooseColor(AbstractShapeClass shape) {
        ColorPicker colorPicker = new ColorPicker(shape);
        enable(false);
        colorPicker.getColors().whenComplete((Boolean shouldRender, Object o2) -> {
            enable(true);

            canvasEngine.refresh();
        });
    }
}
