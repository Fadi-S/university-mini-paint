package frontend;

import backend.shapes.DefaultShape;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class PropertiesForm {
    private final JFrame frame;
    private JPanel panel;
    private JTextField yTextField;
    private JTextField xTextField;
    private JButton submitBtn;
    private JPanel formPanel;
    private JButton cancelBtn;

    CompletableFuture<Boolean> response;

    public PropertiesForm(DefaultShape shape, JPanel canvas) {
        frame = new JFrame("Properties of " + shape.getKey());
        frame.setContentPane(panel);
        frame.setSize(400, 350);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        response = new CompletableFuture<>();

        JRootPane rootPane = SwingUtilities.getRootPane(panel);
        rootPane.setDefaultButton(submitBtn);

        String[] props = shape.properties();

        GridLayout layout = new GridLayout(props.length*2, 1);
        formPanel.setLayout(layout);
        JTextField[] customFields = new JTextField[props.length];
        for (int i=0; i<props.length; i++) {
            customFields[i] = new JTextField();
            JLabel label = new JLabel(props[i].substring(0, 1).toUpperCase() + props[i].substring(1));
            label.setLabelFor(customFields[i]);
            formPanel.add(label, 2*i);
            formPanel.add(customFields[i], 2*i+1);
        }

        submitBtn.addActionListener((event) -> {
            String xStr = xTextField.getText();
            String yStr = yTextField.getText();

            String[] values = new String[customFields.length];
            for(int i=0; i<customFields.length; i++) {
                values[i] = customFields[i].getText();
            }

            if(xStr.isBlank() || yStr.isBlank() || Arrays.stream(values).anyMatch(String::isBlank)) {
                JOptionPane.showMessageDialog(frame, "Some fields are empty");

                return;
            }

            try {
                int x = Integer.parseInt(xStr);
                int y = Integer.parseInt(yStr);
                Dimension canvasSize = canvas.getSize();
                if(x > canvasSize.width || y > canvasSize.height) {
                    JOptionPane.showMessageDialog(frame, "X and Y coordinates must not be greater than " + canvasSize.width + " and " + canvasSize.height + " respectively");
                    return;
                }

                if(x < 0 || y < 0) {
                    throw new NumberFormatException();
                }

                shape.setPosition(new Point(x, y));

                for(int i=0; i<props.length; i++) {
                    double val = Double.parseDouble(values[i]);
                    if(val <= 0) {
                        throw new NumberFormatException();
                    }

                    shape.set(props[i], val);
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
    }

    public CompletableFuture<Boolean> getData() {
        frame.setVisible(true);

        return response;
    }

    public JFrame getFrame() {
        return frame;
    }
}
