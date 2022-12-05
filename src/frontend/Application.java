package frontend;

import backend.interfaces.Shape;
import backend.interfaces.ShapesChangedListener;
import backend.shapes.creator.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

public class Application {
    private final JFrame frame = new JFrame("Vector Drawing Application");
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

    private void setupFrame()
    {
        frame.setSize(1000, 600);
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        canvas.add(canvasEngine);
        sizeLabel.setText(canvas.getWidth() + "x" + canvas.getHeight());
    }

    Application()
    {
        setupFrame();

        setupEvents();

        circleBtn.addActionListener((e) -> create(new CircleCreator()));
        lineSegmentBtn.addActionListener((e) -> create(new LineSegmentCreator()));
        squareBtn.addActionListener((e) -> create(new SquareCreator()));
        rectangleBtn.addActionListener((e) -> create(new RectangleCreator()));
        triangleBtn.addActionListener((e) -> create(new TriangleCreator()));

        deleteBtn.addActionListener(this::delete);
        colorizeBtn.addActionListener(this::chooseColor);
    }

    private void setupEvents() {
        /*
        * Shapes listener
        * */
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


        /*
        * Frame resize listener
        * */
        frame.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                sizeLabel.setText(canvasEngine.getWidth() + "x" + canvasEngine.getHeight());
            }
            public void componentMoved(ComponentEvent e) {}
            public void componentShown(ComponentEvent e) {}
            public void componentHidden(ComponentEvent e) {}
        });


        /*
        * Combobox Listener
        * */
        shapesSelectBox.addItemListener(event -> {
            if (event.getStateChange() != ItemEvent.SELECTED || !shapesSelectBox.hasFocus()) {
                return;
            }
            Shape selectShape = Arrays.stream(canvasEngine.getShapes())
                    .filter((s) -> s.toString().equals(event.getItem()))
                    .findFirst()
                    .orElse(null);

            if(selectShape == null) return;

            select(selectShape);
        });


        /*
        * Canvas mouse Listeners
        * */
        canvas.addMouseListener(new MouseListener() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point point = e.getPoint();

                select(canvasEngine.getShapeAtPoint(point));

                if(selectedShape != null) {
                    selectedShape.setDraggingPoint(point);
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
                Point point = e.getPoint();
                mousePosition.setText("Mouse: " + point.x + "x" + point.y);

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

    private void delete(ActionEvent event) {
        if(selectedShape == null) {
            noShapeMessage();
            return;
        }

        canvasEngine.removeShape(selectedShape);
    }

    private void chooseColor(ActionEvent event) {
        if(selectedShape == null) {
            noShapeMessage();
            return;
        }

        ColorPicker colorPicker = new ColorPicker(selectedShape);
        enable(false);
        colorPicker.getColors().whenComplete((Boolean shouldRender, Object o2) -> {
            enable(true);

            canvasEngine.refresh();
        });
    }

    private void noShapeMessage()
    {
        JOptionPane.showMessageDialog(frame, "No shapes selected");
    }
}
