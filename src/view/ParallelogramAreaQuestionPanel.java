// This panel allows users to calculate the area of parallelograms.
// It includes question generation, image display, answer validation, and score tracking.
package src.view;

import javax.swing.*;
import java.awt.*;
import src.model.ScoreManager;

public class ParallelogramAreaQuestionPanel extends JPanel {
    // Reference to the main frame
    private JFrame parentFrame;
    // Parameters for the parallelogram
    private int base, height, correctArea, attempts = 0;
    // Labels for displaying various information
    private JLabel timerLabel, feedbackLabel, questionLabel, scoreLabel;
    // Text field for user answer input
    private JTextField answerField;
    // Buttons for different actions
    private JButton submitButton, nextButton, homeButton;
    // Timer for tracking question time limit
    private Timer timer;
    // Time remaining in seconds
    private int timeLeft = 180;
    // Panel for displaying explanation
    private JPanel explainPanel;
    // Labels for formula and calculation
    private JLabel formulaLabel, calcLabel;
    // Panel for drawing the parallelogram
    private ParallelogramDrawingPanel drawingPanel;
    // Total score
    private int score = 0;

    // Constructor, initializes the panel and its components
    public ParallelogramAreaQuestionPanel(JFrame frame) {
        this.parentFrame = frame;
        setLayout(new BorderLayout(10, 10));
        StyleUtil.stylePanel(this);
        base = 1 + (int)(Math.random() * 20);
        height = 1 + (int)(Math.random() * 20);
        correctArea = base * height;

        // Top timer and score
        JPanel topPanel = new JPanel(new GridLayout(1, 2));
        StyleUtil.stylePanel(topPanel);
        timerLabel = new JLabel("Time left: 180s", SwingConstants.CENTER);
        StyleUtil.styleLabel(timerLabel, StyleUtil.NORMAL_FONT, StyleUtil.MAIN_PURPLE);
        scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);
        StyleUtil.styleLabel(scoreLabel, StyleUtil.NORMAL_FONT, StyleUtil.MAIN_BLUE);
        topPanel.add(timerLabel);
        topPanel.add(scoreLabel);
        add(topPanel, BorderLayout.NORTH);

        // Middle content
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        StyleUtil.stylePanel(centerPanel);
        centerPanel.add(Box.createVerticalStrut(10));
        questionLabel = new JLabel("The base of the parallelogram is " + base + ", the height is " + height + ". Please calculate its area.", SwingConstants.CENTER);
        StyleUtil.styleLabel(questionLabel, StyleUtil.BIG_FONT, StyleUtil.MAIN_BLUE);
        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(questionLabel);
        centerPanel.add(Box.createVerticalStrut(20));
        JPanel inputPanel = new JPanel();
        StyleUtil.stylePanel(inputPanel);
        inputPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel areaLabel = new JLabel("Area = ");
        StyleUtil.styleLabel(areaLabel, StyleUtil.NORMAL_FONT, StyleUtil.MAIN_PURPLE);
        answerField = new JTextField();
        answerField.setMaximumSize(new Dimension(120, 32));
        answerField.setPreferredSize(new Dimension(120, 32));
        answerField.setFont(StyleUtil.NORMAL_FONT);
        answerField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(StyleUtil.MAIN_BLUE, 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        submitButton = new JButton("Submit");
        StyleUtil.styleButton(submitButton, StyleUtil.MAIN_GREEN, Color.BLACK);
        inputPanel.add(areaLabel);
        inputPanel.add(answerField);
        inputPanel.add(submitButton);
        centerPanel.add(inputPanel);
        feedbackLabel = new JLabel(" ", SwingConstants.CENTER);
        StyleUtil.styleLabel(feedbackLabel, StyleUtil.NORMAL_FONT, StyleUtil.MAIN_BLUE);
        feedbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(feedbackLabel);
        // Explanation area
        explainPanel = new JPanel();
        explainPanel.setLayout(new BoxLayout(explainPanel, BoxLayout.Y_AXIS));
        StyleUtil.stylePanel(explainPanel);
        explainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formulaLabel = new JLabel("Formula: A = base × height", SwingConstants.CENTER);
        StyleUtil.styleLabel(formulaLabel, StyleUtil.NORMAL_FONT, StyleUtil.MAIN_PURPLE);
        formulaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        drawingPanel = new ParallelogramDrawingPanel(base, height);
        drawingPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        calcLabel = new JLabel("Substitute: A = " + base + " × " + height + " = " + correctArea, SwingConstants.CENTER);
        StyleUtil.styleLabel(calcLabel, StyleUtil.NORMAL_FONT, StyleUtil.MAIN_PURPLE);
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
        StyleUtil.styleButton(nextButton, StyleUtil.MAIN_YELLOW, Color.BLACK);
        nextButton.setEnabled(true);
        homeButton = new JButton("Home");
        StyleUtil.styleButton(homeButton, StyleUtil.MAIN_YELLOW, Color.BLACK);
        JPanel bottomPanel = new JPanel();
        StyleUtil.stylePanel(bottomPanel);
        bottomPanel.add(nextButton);
        bottomPanel.add(homeButton);
        add(bottomPanel, BorderLayout.SOUTH);
        submitButton.addActionListener(e -> checkAnswer());
        nextButton.addActionListener(e -> goNext());
        homeButton.addActionListener(e -> goHome());
        timer = new Timer(1000, e -> updateTimer());
        timer.start();
    }
    // Updates the timer display and handles timeout
    private void updateTimer() {
        timeLeft--;
        timerLabel.setText("Time left: " + timeLeft + "s");
        if (timeLeft <= 0) {
            timer.stop();
            showSolution(false, true);
        }
    }
    // Checks the user's answer and provides feedback
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
            int points = 10; 
            score += points;
            scoreLabel.setText("Score: " + score);
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
    // Shows the solution with explanation
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
    // Returns to the shape selection panel
    private void goNext() {
        parentFrame.setContentPane(new ShapeAreaPanel(parentFrame));
        parentFrame.revalidate();
    }
    // Returns to the main menu
    private void goHome() {
        parentFrame.setContentPane(new MainMenuPanel(parentFrame));
        parentFrame.revalidate();
    }
    // Inner class for drawing parallelograms with measurements
    static class ParallelogramDrawingPanel extends JPanel {
        // Parameters for the parallelogram
        private int base, height;

        // Constructor for the drawing panel
        public ParallelogramDrawingPanel(int b, int h) {
            this.base = b;
            this.height = h;
            setPreferredSize(new Dimension(320, 160));
            StyleUtil.stylePanel(this);
        }
        // Paints the parallelogram with measurements
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
            int offset = b / 4;
            Polygon p = new Polygon();
            p.addPoint(x + offset, y);
            p.addPoint(x + b, y);
            p.addPoint(x + b - offset, y + h);
            p.addPoint(x, y + h);
            g2.setStroke(new BasicStroke(3));
            g2.setColor(new Color(255, 200, 100));
            g2.drawPolygon(p);
            g2.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
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