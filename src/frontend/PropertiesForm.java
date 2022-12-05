package frontend;

import backend.shapes.creator.ShapeCreator;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class PropertiesForm {
    private final JFrame frame;
    private JPanel panel;
    private JButton submitBtn;
    private JPanel formPanel;
    private JButton cancelBtn;
    private JButton outlineColorBtn;
    private JButton fillColorBtn;

    CompletableFuture<Boolean> response;

    public PropertiesForm(ShapeCreator shapeCreator) {
        frame = new JFrame("Properties of " + shapeCreator.getName());
        frame.setContentPane(panel);
//        frame.setMinimumSize(new Dimension(450, 400));
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        response = new CompletableFuture<>();

        JRootPane rootPane = SwingUtilities.getRootPane(panel);
        rootPane.setDefaultButton(submitBtn);

        String[] props = shapeCreator.properties();

        outlineColorBtn.setUI(new ColorPicker.CustomButton());
        outlineColorBtn.setBackground(Color.black);
        outlineColorBtn.setForeground(Color.white);
        outlineColorBtn.addActionListener((e) -> {
            Color color = JColorChooser.showDialog(frame, "Choose shape outline color", Color.black);

            outlineColorBtn.setBackground(color);

            shapeCreator.setColor(color);
        });

        fillColorBtn.setUI(new ColorPicker.CustomButton());
        fillColorBtn.setForeground(Color.black);
        fillColorBtn.addActionListener((e) -> {
            Color color = JColorChooser.showDialog(frame, "Choose shape fill color", null);

            fillColorBtn.setBackground(color);

            shapeCreator.setFillColor(color);
        });

        GridLayout layout = new GridLayout(props.length, 2);
        layout.setVgap(10);
        formPanel.setLayout(layout);
        JTextField[] customFields = new JTextField[props.length];
        for (int i=0; i<props.length; i++) {
            customFields[i] = new JTextField();
            JLabel label = new JLabel(props[i].substring(0, 1).toUpperCase() + props[i].substring(1));
            label.setLabelFor(customFields[i]);
            formPanel.add(label);
            formPanel.add(customFields[i]);
        }

        submitBtn.addActionListener((event) -> {

            String[] values = Arrays.stream(customFields)
                    .map(JTextField::getText)
                    .toArray(String[]::new);

            if(Arrays.stream(values).anyMatch(String::isBlank)) {
                JOptionPane.showMessageDialog(frame, "Some fields are empty");
                return;
            }

            try {
                for(int i=0; i<props.length; i++) {
                    int val = Integer.parseInt(values[i]);
                    if(val < 0) {
                        throw new NumberFormatException();
                    }

                    shapeCreator.set(props[i], val);
                }

                response.complete(true);
                frame.dispose();

            }catch (Exception exception) {
                JOptionPane.showMessageDialog(frame, "All fields should be positive numeric values");
            }
        });

        cancelBtn.addActionListener(e -> {
            response.complete(false);

            frame.dispose();
        });

        frame.pack();
    }

    public CompletableFuture<Boolean> getData() {
        frame.setVisible(true);

        return response;
    }
}
