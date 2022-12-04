package frontend;

import backend.shapes.DefaultShape;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public class ColorPicker {
    private JPanel panel;
    private JButton outlineColorBtn;
    private JButton fillColorBtn;
    private JButton okBtn;
    private final JFrame frame;
    CompletableFuture<Boolean> response;

    private static class CustomButton extends BasicButtonUI
    {
        @Override
        protected void paintFocus(Graphics g, AbstractButton b, Rectangle viewRect, Rectangle textRect, Rectangle iconRect) {
            g.setColor(Color.white);
            g.drawRect(viewRect.x, viewRect.y, viewRect.width, viewRect.height);
        }
    }

    public ColorPicker(DefaultShape shape) {
        frame = new JFrame("Pick color for " + shape.getKey());
        frame.setSize(300, 300);
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        response = new CompletableFuture<>();

        JRootPane rootPane = SwingUtilities.getRootPane(panel);
        rootPane.setDefaultButton(okBtn);

        AtomicReference<Color> outlineColor = new AtomicReference<>(shape.getColor());
        outlineColorBtn.setUI(new CustomButton());
        outlineColorBtn.setBackground(outlineColor.get());
        outlineColorBtn.setForeground(Color.white);
        outlineColorBtn.addActionListener((e) -> {
            outlineColor.set(JColorChooser.showDialog(frame, "Choose shape outline color", outlineColor.get()));

            outlineColorBtn.setBackground(outlineColor.get());

            shape.setColor(outlineColor.get());
        });

        AtomicReference<Color> fillColor = new AtomicReference<>(shape.getFillColor());
        fillColorBtn.setUI(new CustomButton());
        fillColorBtn.setBackground(fillColor.get());
        fillColorBtn.setForeground(Color.black);
        fillColorBtn.addActionListener((e) -> {
            fillColor.set(JColorChooser.showDialog(frame, "Choose shape fill color", fillColor.get()));

            fillColorBtn.setBackground(fillColor.get());

            shape.setFillColor(fillColor.get());
        });

        okBtn.addActionListener((e) -> {
            response.complete(true);

            frame.dispose();
        });
    }

    public CompletableFuture<Boolean> getColors()
    {
        frame.setVisible(true);
        return response;
    }
}
