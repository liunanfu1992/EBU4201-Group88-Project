// This panel allows users to calculate the area of trapeziums.
// It includes question generation, image display, answer validation, and score tracking.
package src.view;

import javax.swing.*;
import java.awt.*;
import src.model.ScoringUtil;
import src.model.ScoreManager;

public class TrapeziumAreaQuestionPanel extends JPanel {
    // Reference to the main frame
    private JFrame parentFrame;
    // Parameters for the trapezium
    private int base1, base2, height, correctArea, attempts = 0;
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
    // Panel for drawing the trapezium
    private TrapeziumDrawingPanel drawingPanel;
    // Total score
    private int score = 0;
    // Flag indicating whether this is an advanced question
    private final boolean isAdvanced = false;

    // Constructor, initializes the panel and its components
    public TrapeziumAreaQuestionPanel(JFrame frame) {
        this.parentFrame = frame;
        setLayout(new BorderLayout(10, 10));
        StyleUtil.stylePanel(this);
        base1 = 1 + (int)(Math.random() * 20);
        base2 = 1 + (int)(Math.random() * 20);
        height = 1 + (int)(Math.random() * 20);
        correctArea = (int)(0.5 * (base1 + base2) * height);

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
        centerPanel.add(Box.createVerticalStrut(10));
        questionLabel = new JLabel("The top base of the trapezium is " + base1 + ", the bottom base is " + base2 + ", the height is " + height + ". Please calculate its area.", SwingConstants.CENTER);
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
        formulaLabel = new JLabel("Formula: A = 1/2 × (base1 + base2) × height", SwingConstants.CENTER);
        StyleUtil.styleLabel(formulaLabel, StyleUtil.NORMAL_FONT, StyleUtil.MAIN_PURPLE);
        formulaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        drawingPanel = new TrapeziumDrawingPanel(base1, base2, height);
        drawingPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        calcLabel = new JLabel("Substitute: A = 1/2 × (" + base1 + " + " + base2 + ") × " + height + " = " + correctArea, SwingConstants.CENTER);
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
            int points = ScoringUtil.getScore(isAdvanced, attempts);
            score += points;
            ScoreManager.getInstance().addScore(points);
            feedbackLabel.setText("Correct! +" + points + " points. Great job!");
            scoreLabel.setText("Score: " + score);
            ShapeAreaPanel.markShapeAsCompleted("Trapezium");
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

    // Inner class for drawing trapeziums with measurements
    static class TrapeziumDrawingPanel extends JPanel {
        // Parameters for the trapezium
        private int base1, base2, height;

        // Constructor for the drawing panel
        public TrapeziumDrawingPanel(int b1, int b2, int h) {
            this.base1 = b1;
            this.base2 = b2;
            this.height = h;
            setPreferredSize(new Dimension(340, 170));
            StyleUtil.stylePanel(this);
        }

        // Paints the trapezium with measurements
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int panelW = getWidth();
            int panelH = getHeight();
            int maxBase = 180, maxHeight = 80;
            double scale = Math.min(maxBase / (double)Math.max(base1, base2), maxHeight / (double)height);
            int b1 = (int)(base1 * scale);
            int b2 = (int)(base2 * scale);
            int h = (int)(height * scale);
            int x = (panelW - Math.max(b1, b2)) / 2;
            int y = (panelH - h) / 2;
            int x1 = x + (b2 > b1 ? (b2 - b1) / 2 : 0);
            int x2 = x + (b2 > b1 ? (b2 - b1) / 2 + b1 : b2);
            int x3 = x;
            int x4 = x + b2;
            int y1 = y;
            int y2 = y;
            int y3 = y + h;
            int y4 = y + h;
            Polygon p = new Polygon();
            p.addPoint(x1, y1);
            p.addPoint(x2, y2);
            p.addPoint(x4, y4);
            p.addPoint(x3, y3);
            g2.setStroke(new BasicStroke(3));
            g2.setColor(new Color(255, 150, 200));
            g2.drawPolygon(p);
            g2.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
            g2.setColor(Color.BLACK);
            FontMetrics fm = g2.getFontMetrics();
            String base1Str = "Top: " + base1;
            int base1Width = fm.stringWidth(base1Str);
            g2.drawString(base1Str, x1 + (b1 - base1Width) / 2, y1 - 8);
            String base2Str = "Bottom: " + base2;
            int base2Width = fm.stringWidth(base2Str);
            g2.drawString(base2Str, x3 + (b2 - base2Width) / 2, y3 + 25);
            String heightStr = "Height: " + height;
            int heightWidth = fm.stringWidth(heightStr);
            g2.drawString(heightStr, x4 + 10, y4 - h / 2 + heightWidth / 2);
        }
    }
}