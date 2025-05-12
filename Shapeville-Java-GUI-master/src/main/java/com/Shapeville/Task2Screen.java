package com.Shapeville;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

public class Task2Screen extends JFrame {
    private Set<String> recognizedTypes = new HashSet<>();
    private int attempts;
    private int currentAngle;
    private String correctType;

    private JPanel shapePanel;
    private JLabel hintLabel;
    private JLabel attemptsLabel;
    private JComboBox<String> typeCombo;
    private JButton submitButton;
    private JButton nextButton;

    // Panel to draw the angle
    class AnglePanel extends JPanel {
        private int angle;
        private static final int RADIUS = 100;

        public AnglePanel(int angle) {
            this.angle = angle;
            setPreferredSize(new Dimension(300, 300));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(3));
            int cx = getWidth() / 2;
            int cy = getHeight() / 2;
            // Draw base line
            g2.drawLine(cx, cy, cx + RADIUS, cy);
            // Draw rotated line
            double rad = Math.toRadians(angle);
            int x2 = cx + (int) (RADIUS * Math.cos(rad));
            int y2 = cy - (int) (RADIUS * Math.sin(rad));
            g2.drawLine(cx, cy, x2, y2);
            // Draw arc indicating angle
            g2.drawArc(cx - 30, cy - 30, 60, 60, 0, -angle);
            // Draw angle label
            g2.drawString(angle + "°", cx + 5, cy - 5);
        }
    }


    public Task2Screen() {
        setTitle("Task 2: Angle Type Identification");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Top navigation
        JPanel topWrapper = ShapevilleGUI.getJPanel();
        TopNavBarPanel top = new TopNavBarPanel();
        topWrapper.add(top);
        add(topWrapper, BorderLayout.NORTH);
        top.homeButton.addActionListener(e -> dispose());
        top.endSessionButton.addActionListener(e -> dispose());

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        add(mainPanel, BorderLayout.CENTER);

        // Shape drawing panel
        shapePanel = new AnglePanel(0);
        mainPanel.add(shapePanel);

        // Hint and attempts
        hintLabel = new JLabel("Select the type for the displayed angle.");
        hintLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(hintLabel);

        attemptsLabel = new JLabel();
        attemptsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(attemptsLabel);

        // Type selection
        typeCombo = new JComboBox<>(new String[]{"Acute", "Obtuse", "Right", "Straight", "Reflex"});
        typeCombo.setMaximumSize(typeCombo.getPreferredSize());
        typeCombo.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(typeCombo);

        // Buttons
        JPanel buttonPanel = new JPanel();
        submitButton = new JButton("Submit");
        nextButton = new JButton("Next");
        nextButton.setVisible(false);
        buttonPanel.add(submitButton);
        buttonPanel.add(nextButton);
        mainPanel.add(buttonPanel);

        // Handlers
        submitButton.addActionListener(e -> onSubmit());
        nextButton.addActionListener(e -> selectNext());

        // Start
        selectNext();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void selectNext() {
        if (recognizedTypes.size() == 5) {
            JOptionPane.showMessageDialog(this, "You have identified all angle types!");
            dispose();
            return;
        }
        // Reset attempts
        attempts = 3;

        // Prompt for angle input
        Integer angle = null;
        while (angle == null) {
            String input = JOptionPane.showInputDialog(
                    this,
                    "Enter an angle (0-360, multiple of 10):",
                    "Input Angle",
                    JOptionPane.PLAIN_MESSAGE
            );
            if (input == null) {
                // User cancelled
                dispose();
                return;
            }
            try {
                int val = Integer.parseInt(input.trim());
                if (val >= 0 && val <= 360 && val % 10 == 0) {
                    angle = val;
                } else {
                    JOptionPane.showMessageDialog(this, "Please enter a multiple of 10 between 0 and 360.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid number. Try again.");
            }
        }
        currentAngle = angle;
        correctType = determineType(currentAngle);

        // Update UI
        shapePanel.getParent().remove(shapePanel);
        shapePanel = new AnglePanel(currentAngle);
        ((JPanel)getContentPane().getComponent(1)).add(shapePanel, 0);
        revalidate();
        repaint();

        hintLabel.setText("Identify the type of this angle.");
        updateAttemptsLabel();
        submitButton.setEnabled(true);
        nextButton.setVisible(false);
    }

    private String determineType(int angle) {
        if (angle == 90) return "Right";
        if (angle == 180) return "Straight";
        if (angle > 0 && angle < 90) return "Acute";
        if (angle > 90 && angle < 180) return "Obtuse";
        if (angle > 180 && angle < 360) return "Reflex";
        // angle 0 or 360 treat as straight
        return "Straight";
    }

    private void updateAttemptsLabel() {
        attemptsLabel.setText("Attempts left: " + attempts);
    }

    private void onSubmit() {
        String selected = (String) typeCombo.getSelectedItem();
        if (selected.equals(correctType)) {
            recognizedTypes.add(selected);
            hintLabel.setText("Correct! You have recognized: " + recognizedTypes);
            submitButton.setEnabled(false);
            nextButton.setVisible(true);
        } else {
            attempts--;
            updateAttemptsLabel();
            if (attempts > 0) {
                hintLabel.setText("Incorrect. Try again.");
            } else {
                hintLabel.setText("Out of attempts. Correct type: " + correctType);
                submitButton.setEnabled(false);
                nextButton.setVisible(true);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Task2Screen::new);
    }
}

