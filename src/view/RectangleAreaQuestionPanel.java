// This panel allows users to calculate the area of rectangles.
// It includes question generation, image display, answer validation, and score tracking.
package src.view;

import javax.swing.*;
import java.awt.*;
import src.model.ScoringUtil;
import src.model.ScoreManager;

public class RectangleAreaQuestionPanel extends JPanel {
    // Reference to the main frame
    private JFrame parentFrame;
    // Parameters for the rectangle
    private int length, width, correctArea, attempts = 0;
    // Labels for displaying various information
    private JLabel timerLabel, feedbackLabel, questionLabel, scoreLabel;
    // Text field for user answer input
    private JTextField answerField;
    // Buttons for different actions
    private JButton submitButton, nextButton, homeButton;
    // Timer for tracking question time limit
    private Timer timer;
    // Time remaining in seconds
    private int timeLeft = 180; // 3 minutes
    // Panel for displaying explanation
    private JPanel explainPanel;
    // Labels for formula and calculation
    private JLabel formulaLabel, calcLabel;
    // Panel for drawing the rectangle
    private RectangleDrawingPanel drawingPanel;
    // Total score
    private int score = 0; // Total score
    // Flag indicating whether this is an advanced question
    private final boolean isAdvanced = false; // Basic shape type

    // Constructor, initializes the panel and its components
    public RectangleAreaQuestionPanel(JFrame frame) {
        this.parentFrame = frame;
        setLayout(new BorderLayout(10, 10));
        StyleUtil.stylePanel(this);

        // Generate random parameters
        length = 1 + (int)(Math.random() * 20);
        width = 1 + (int)(Math.random() * 20);
        correctArea = length * width;

        // Top panel for timer and score
        JPanel topPanel = new JPanel(new GridLayout(1, 2));
        StyleUtil.stylePanel(topPanel);
        timerLabel = new JLabel("Time left: 180s", SwingConstants.CENTER);
        StyleUtil.styleLabel(timerLabel, StyleUtil.NORMAL_FONT, StyleUtil.MAIN_PURPLE);
        scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);
        StyleUtil.styleLabel(scoreLabel, StyleUtil.NORMAL_FONT, StyleUtil.MAIN_BLUE);
        topPanel.add(timerLabel);
        topPanel.add(scoreLabel);
        add(topPanel, BorderLayout.NORTH);

        // Main content panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        StyleUtil.stylePanel(centerPanel);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        centerPanel.add(Box.createVerticalStrut(4));

        // Question area
        questionLabel = new JLabel("The length of the rectangle is " + length + ", the width is " + width + ". Please calculate its area.", SwingConstants.CENTER);
        StyleUtil.styleLabel(questionLabel, StyleUtil.BIG_FONT, StyleUtil.MAIN_BLUE);
        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(questionLabel);
        centerPanel.add(Box.createVerticalStrut(6));

        // Input area
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

        // Feedback area
        feedbackLabel = new JLabel(" ", SwingConstants.CENTER);
        StyleUtil.styleLabel(feedbackLabel, StyleUtil.NORMAL_FONT, StyleUtil.MAIN_BLUE);
        feedbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(feedbackLabel);

        // Explanation area (formula at top, image in center, substitution at bottom)
        explainPanel = new JPanel();
        explainPanel.setLayout(new BoxLayout(explainPanel, BoxLayout.Y_AXIS));
        StyleUtil.stylePanel(explainPanel);
        explainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        formulaLabel = new JLabel("Formula: A = length × width", SwingConstants.CENTER);
        StyleUtil.styleLabel(formulaLabel, StyleUtil.NORMAL_FONT, StyleUtil.MAIN_PURPLE);
        formulaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        drawingPanel = new RectangleDrawingPanel(length, width);
        drawingPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        calcLabel = new JLabel("Substitute: A = " + length + " × " + width + " = " + correctArea, SwingConstants.CENTER);
        StyleUtil.styleLabel(calcLabel, StyleUtil.NORMAL_FONT, StyleUtil.MAIN_PURPLE);
        calcLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        explainPanel.add(formulaLabel);
        explainPanel.add(Box.createVerticalStrut(10));
        explainPanel.add(drawingPanel);
        explainPanel.add(Box.createVerticalStrut(10));
        explainPanel.add(calcLabel);
        explainPanel.setVisible(false);
        centerPanel.add(explainPanel);

        add(centerPanel, BorderLayout.CENTER);

        // Bottom buttons
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

        // Event bindings
        submitButton.addActionListener(e -> checkAnswer());
        nextButton.addActionListener(e -> goNext());
        homeButton.addActionListener(e -> goHome());

        // Start countdown timer
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
            int points = ScoringUtil.getScore(isAdvanced, attempts);
            score += points;
            ScoreManager.getInstance().addScore(points);
            feedbackLabel.setText("Correct! +" + points + " points. Great job!");
            scoreLabel.setText("Score: " + score);
            ShapeAreaPanel.markShapeAsCompleted("Rectangle");
            showSolution(true, false);
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

    // Inner class for drawing rectangles with measurements
    static class RectangleDrawingPanel extends JPanel {
        // Parameters for the rectangle
        private int length, width;

        // Constructor for the drawing panel
        public RectangleDrawingPanel(int l, int w) {
            this.length = l;
            this.width = w;
            setPreferredSize(new Dimension(320, 110));
            StyleUtil.stylePanel(this);
        }

        // Paints the rectangle with measurements
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int panelW = getWidth();
            int panelH = getHeight();
            int maxRectW = 180, maxRectH = 80;
            double scale = Math.min(maxRectW / (double)length, maxRectH / (double)width);
            int w = (int)(length * scale);
            int h = (int)(width * scale);
            int x = (panelW - w) / 2;
            int y = (panelH - h) / 2;
            g2.setStroke(new BasicStroke(3));
            g2.setColor(new Color(120, 255, 180));
            g2.drawRect(x, y, w, h);
            g2.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
            g2.setColor(Color.BLACK);
            FontMetrics fm = g2.getFontMetrics();
            String lengthStr = "Length: " + length;
            int lengthWidth = fm.stringWidth(lengthStr);
            g2.drawString(lengthStr, x + (w - lengthWidth) / 2, y - 8);
            String widthStr = "Width: " + width;
            int widthWidth = fm.stringWidth(widthStr);
            g2.drawString(widthStr, x + w + 10, y + h / 2 + widthWidth / 2);
        }
    }
}