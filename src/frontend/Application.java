package frontend;

import backend.interfaces.Shape;
import backend.interfaces.ShapesChangedListener;
import backend.shapes.AbstractShapeClass;
import backend.shapes.creator.*;
import org.json.simple.JsonObject;
import org.json.simple.Jsoner;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Application {
    private final JFrame frame = new JFrame("Vector Drawing Application");
    private JPanel panel;
    private JButton ovalBtn;
    private JButton lineSegmentBtn;
    private JButton rectangleBtn;
    private JButton colorizeBtn;
    private JPanel canvas;
    private JButton deleteBtn;
    private JLabel savedLabel;
    private JComboBox<String> shapesSelectBox;
    private JLabel sizeLabel;
    private JLabel mousePosition;
    private JPanel controlsPanel;
    private JPanel shapesButtonsPanel;
    private JButton triangleBtn;
    private JButton copyBtn;
    private JCheckBox selectSmallerCheck;

    private final Canvas canvasEngine = new Canvas();

    Shape selectedShape;
    Point resizingBy = null;

    private File selectedFile = null;

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

    private void setupMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");

        JMenuItem newFileItem = new JMenuItem("New", KeyEvent.VK_N);
        newFileItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        newFileItem.getAccessibleContext().setAccessibleDescription(
                "New canvas");
        newFileItem.addActionListener((e) -> {
            canvasEngine.removeAll();
            selectedFile = null;
        });

        JMenuItem saveMenuItem = new JMenuItem("Save", KeyEvent.VK_S);
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        saveMenuItem.getAccessibleContext().setAccessibleDescription(
                "Save shapes to JSON file");
        saveMenuItem.addActionListener((e) -> save(false));

        JMenuItem saveAsMenuItem = new JMenuItem("Save As...", KeyEvent.VK_A);
        saveAsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));
        saveAsMenuItem.getAccessibleContext().setAccessibleDescription(
                "Save shapes to new JSON file");
        saveAsMenuItem.addActionListener((e) -> save(true));


        JMenuItem loadMenuItem = new JMenuItem("Load", KeyEvent.VK_L);
        loadMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_DOWN_MASK));
        loadMenuItem.getAccessibleContext().setAccessibleDescription(
                "Load shapes from JSON file");
        loadMenuItem.addActionListener(this::load);


        menu.add(newFileItem);
        menu.add(saveMenuItem);
        menu.add(saveAsMenuItem);
        menu.add(loadMenuItem);

        menuBar.add(menu);
        frame.setJMenuBar(menuBar);
    }

    Application()
    {
        setupMenu();

        setupFrame();

        setupEvents();

        ovalBtn.addActionListener((e) -> create(new OvalCreator()));
        lineSegmentBtn.addActionListener((e) -> create(new LineSegmentCreator()));
        rectangleBtn.addActionListener((e) -> create(new RectangleCreator()));
        triangleBtn.addActionListener((e) -> create(new TriangleCreator()));

        deleteBtn.addActionListener(this::delete);
        colorizeBtn.addActionListener(this::chooseColor);
        copyBtn.addActionListener(this::copy);
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

            @Override
            public void shapesCleared() {
                shapesSelectBox.removeAllItems();
                select(null);
                AbstractShapeClass.resetNumbering();
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
                resizingBy = getCornerPoint(e.getPoint());
                if(resizingBy != null) return;

                select(canvasEngine.getShapeAtPoint(e.getPoint(), selectSmallerCheck.isSelected()));

                if(selectedShape != null) {
                    selectedShape.setDraggingPoint(e.getPoint());
                }
            }
            public void mouseClicked(MouseEvent e) {}
            public void mouseReleased(MouseEvent e) {
                resizingBy = null;
            }
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
        });

        canvas.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                mousePosition.setText("Mouse: " + e.getX() + "x" + e.getY());

                if(selectedShape == null) return;

                Point mousePoint = e.getPoint();

                if(resizingBy == null)
                    resizingBy = getCornerPoint(mousePoint);

                if(resizingBy != null) {
                    resizingBy = selectedShape.resize(resizingBy, mousePoint);

                    canvasEngine.refresh();

                    return;
                }

                selectedShape.moveTo(e.getPoint());
                selectedShape.setDraggingPoint(e.getPoint());
                canvasEngine.refresh();
            }
            @Override
            public void mouseMoved(MouseEvent e) {
                mousePosition.setText("Mouse: " + e.getX() + "x" + e.getY());

                Point mousePoint = e.getPoint();

                Point p = getCornerPoint(mousePoint);
                Cursor cursor = new Cursor(p != null ? Cursor.CROSSHAIR_CURSOR : Cursor.DEFAULT_CURSOR);
                frame.setCursor(cursor);
            }
        });
    }

    private Point getCornerPoint(Point mousePoint) {
        if(selectedShape == null) return null;

        for (Point point : selectedShape.points()) {
            if(point.distance(mousePoint) < 10) {
                return point;
            }
        }

        return null;
    }

    private File getFile(boolean open) {
        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));

        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                String[] fileSections = f.getName().split("\\.");

                return fileSections[fileSections.length-1].equalsIgnoreCase("json") || f.isDirectory();
            }

            @Override
            public String getDescription() {
                return "JSON files";
            }
        });

        fileChooser.setMultiSelectionEnabled(false);

        int result = open ? fileChooser.showOpenDialog(frame) : fileChooser.showSaveDialog(frame);

        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }

        return null;
    }

    private void save(boolean forceChooser) {
        if(!forceChooser && selectedFile != null) {
            save(selectedFile);
            return;
        }

        File file = getFile(false);
        if(file != null) {
            selectedFile = file;
            save(selectedFile);
        }
    }

    private void save(File file) {
        try {
            FileWriter writer = new FileWriter(file);

            writer.write(canvasEngine.toJSON());

            writer.close();

            savedLabel.setText("Saved");
            new Thread(() -> {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    return;
                }
                savedLabel.setText(".");
            }).start();
        } catch (IOException ignored) {
            System.err.println("Error saving file");
        }
    }

    private void load(ActionEvent e) {
        File file = getFile(true);
        if(file == null)
            return;

        String[] fileSections = file.getName().split("\\.");

        if(! fileSections[fileSections.length-1].equalsIgnoreCase("json")) {
            JOptionPane.showMessageDialog(frame, "Invalid file type!");
            return;
        }

        try (Scanner scanner = new Scanner(file)) {
            StringBuilder json = new StringBuilder();
            while (scanner.hasNextLine()) {
                json.append(scanner.nextLine());
            }

            canvasEngine.fromJSON(Jsoner.deserialize(json.toString(), new JsonObject()));

            selectedFile = file;
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(frame, "Could not open file");
        }
    }

    private void select(Shape shape) {
        canvasEngine.setSelectedShape(shape);
        selectedShape = shape;

        if(shape == null) {
            shapesSelectBox.setSelectedItem(null);
        } else {
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

    private void copy(ActionEvent e) {
        if(selectedShape == null) {
            noShapeMessage();
            return;
        }

        Shape clonedShape = ((AbstractShapeClass) selectedShape).clone();

        canvasEngine.addShape(clonedShape);
    }

    private void noShapeMessage()
    {
        JOptionPane.showMessageDialog(frame, "No shapes selected");
    }

}
