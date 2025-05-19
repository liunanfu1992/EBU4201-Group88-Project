package src.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import src.model.ScoreManager;

public class ParallelogramAreaQuestionPanel extends JPanel {
    private JFrame parentFrame;
    private int base, height, correctArea, attempts = 0;
    private JLabel timerLabel, feedbackLabel, questionLabel;
    private JTextField answerField;
    private JButton submitButton, nextButton, homeButton;
    private Timer timer;
    private int timeLeft = 180;
    private JPanel explainPanel;
    private JLabel formulaLabel, calcLabel;
    private ParallelogramDrawingPanel drawingPanel;

    public ParallelogramAreaQuestionPanel(JFrame frame) {
        this.parentFrame = frame;
        setLayout(new BorderLayout(10, 10));
        base = 1 + (int)(Math.random() * 20);
        height = 1 + (int)(Math.random() * 20);
        correctArea = base * height;
        timerLabel = new JLabel("Time left: 180s", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(timerLabel, BorderLayout.NORTH);
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(Box.createVerticalStrut(10));
        questionLabel = new JLabel("The base of the parallelogram is " + base + ", the height is " + height + ". Please calculate its area.", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 18));
        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(questionLabel);
        centerPanel.add(Box.createVerticalStrut(20));
        JPanel inputPanel = new JPanel();
        inputPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        answerField = new JTextField();
        answerField.setMaximumSize(new Dimension(120, 32));
        answerField.setPreferredSize(new Dimension(120, 32));
        answerField.setFont(new Font("Arial", Font.PLAIN, 18));
        submitButton = new JButton("Submit");
        inputPanel.add(new JLabel("Area = "));
        inputPanel.add(answerField);
        inputPanel.add(submitButton);
        centerPanel.add(inputPanel);
        feedbackLabel = new JLabel(" ", SwingConstants.CENTER);
        feedbackLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        feedbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(feedbackLabel);
        explainPanel = new JPanel();
        explainPanel.setLayout(new BoxLayout(explainPanel, BoxLayout.Y_AXIS));
        explainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formulaLabel = new JLabel("Formula: A = base × height", SwingConstants.CENTER);
        formulaLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        formulaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        drawingPanel = new ParallelogramDrawingPanel(base, height);
        drawingPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        calcLabel = new JLabel("Substitute: A = " + base + " × " + height + " = " + correctArea, SwingConstants.CENTER);
        calcLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        calcLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        explainPanel.add(formulaLabel);
        explainPanel.add(Box.createVerticalStrut(10));
        explainPanel.add(drawingPanel);
        explainPanel.add(Box.createVerticalStrut(10));
        explainPanel.add(calcLabel);
        explainPanel.setVisible(false);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(explainPanel);
        add(centerPanel, BorderLayout.CENTER);
        nextButton = new JButton("Back to Selection");
        nextButton.setEnabled(true);
        homeButton = new JButton("Home");
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(nextButton);
        bottomPanel.add(homeButton);
        add(bottomPanel, BorderLayout.SOUTH);
        submitButton.addActionListener(e -> checkAnswer());
        nextButton.addActionListener(e -> goNext());
        homeButton.addActionListener(e -> goHome());
        timer = new Timer(1000, e -> updateTimer());
        timer.start();
    }
    private void updateTimer() {
        timeLeft--;
        timerLabel.setText("Time left: " + timeLeft + "s");
        if (timeLeft <= 0) {
            timer.stop();
            showSolution(false, true);
        }
    }
    private void checkAnswer() {
        String input = answerField.getText().trim();
        int ans = -1;
        try {
            ans = Integer.parseInt(input);
        } catch (Exception e) {
            feedbackLabel.setText("Please enter a valid integer.");
            return;
        }
        attempts++;
        if (ans == correctArea) {
            timer.stop();
            showSolution(true, false);
            int points = 10; // Assuming a default points value
            ScoreManager.getInstance().addScore(points);
            ShapeAreaPanel.markShapeAsCompleted("Parallelogram");
        } else {
            if (attempts >= 3) {
                timer.stop();
                showSolution(false, false);
            } else {
                feedbackLabel.setText("Incorrect! Attempts left: " + (3 - attempts));
            }
        }
    }
    private void showSolution(boolean correct, boolean timeout) {
        answerField.setEnabled(false);
        submitButton.setEnabled(false);
        nextButton.setEnabled(true);
        explainPanel.setVisible(true);
        if (correct) {
            feedbackLabel.setText("Correct! Area = " + correctArea);
        } else if (timeout) {
            feedbackLabel.setText("Time's up! The correct area is: " + correctArea);
        } else {
            feedbackLabel.setText("Incorrect! The correct area is: " + correctArea);
        }
    }
    private void goNext() {
        parentFrame.setContentPane(new ShapeAreaPanel(parentFrame));
        parentFrame.revalidate();
    }
    private void goHome() {
        parentFrame.setContentPane(new MainMenuPanel(parentFrame));
        parentFrame.revalidate();
    }
    static class ParallelogramDrawingPanel extends JPanel {
        private int base, height;
        public ParallelogramDrawingPanel(int b, int h) {
            this.base = b;
            this.height = h;
            setPreferredSize(new Dimension(320, 160));
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int panelW = getWidth();
            int panelH = getHeight();
            int maxBase = 180, maxHeight = 80;
            double scale = Math.min(maxBase / (double)base, maxHeight / (double)height);
            int b = (int)(base * scale);
            int h = (int)(height * scale);
            int x = (panelW - b) / 2;
            int y = (panelH - h) / 2;
            int offset = b / 4; // 平行四边形倾斜
            Polygon p = new Polygon();
            p.addPoint(x + offset, y);
            p.addPoint(x + b, y);
            p.addPoint(x + b - offset, y + h);
            p.addPoint(x, y + h);
            g2.setStroke(new BasicStroke(3));
            g2.setColor(new Color(255, 200, 100));
            g2.drawPolygon(p);
            g2.setFont(new Font("Arial", Font.BOLD, 16));
            g2.setColor(Color.BLACK);
            FontMetrics fm = g2.getFontMetrics();
            String baseStr = "Base: " + base;
            int baseWidth = fm.stringWidth(baseStr);
            g2.drawString(baseStr, x + (b - baseWidth) / 2, y + h + 25);
            String heightStr = "Height: " + height;
            int heightWidth = fm.stringWidth(heightStr);
            g2.drawString(heightStr, x + b + 10, y + h / 2 + heightWidth / 2);
        }
    }
} 