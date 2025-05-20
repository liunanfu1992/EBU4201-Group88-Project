// This panel allows users to identify different shapes.
// It includes question generation, image display, answer validation, and score tracking.
package src.view;

import src.model.ShapeType;
import src.model.ShapeQuestion;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import src.model.ScoringUtil;
import src.model.ScoreManager;

public class ShapeIdentificationPanel extends JPanel {
    // Reference to the main frame
    private JFrame parentFrame;
    // List of shapes to be identified
    private List<ShapeType> shapesToIdentify;
    // Current question index
    private int currentIndex = 0;
    // Current question object
    private ShapeQuestion currentQuestion;
    // Number of attempts for current question
    private int attempts = 0;
    // Total score
    private int score = 0;
    // Flag indicating whether in 2D mode
    private boolean is2DMode;
    // Flag indicating whether in advanced mode
    private boolean isAdvanced;
    // Number of practice questions
    private static final int PRACTICE_COUNT = 4;
    // Completion status for 2D shapes
    private static boolean completed2D = false;
    // Completion status for 3D shapes
    private static boolean completed3D = false;

    // Labels for displaying various information
    private JLabel shapeLabel;
    // Text field for user answer input
    private JTextField answerField;
    // Buttons for different actions
    private JButton submitButton;
    private JButton homeButton;
    // Labels for feedback and score
    private JLabel feedbackLabel;
    private JLabel scoreLabel;
    // Label for displaying shape image
    private JLabel imageLabel;
    // Labels for progress tracking
    private JLabel[] progressLabels = new JLabel[PRACTICE_COUNT];

    // Constructor, initializes the panel and its components
    public ShapeIdentificationPanel(JFrame frame, boolean is2D) {
        this.parentFrame = frame;
        this.is2DMode = is2D;
        this.isAdvanced = !is2D; // 3D identification is Advanced, 2D is Basic
        setLayout(new BorderLayout(10, 10));
        StyleUtil.stylePanel(this);

        // Select 2D or 3D question types
        shapesToIdentify = new ArrayList<>();
        for (ShapeType type : ShapeType.values()) {
            if (is2D && type.ordinal() <= ShapeType.KITE.ordinal()) {
                shapesToIdentify.add(type);
            } else if (!is2D && type.ordinal() > ShapeType.KITE.ordinal()) {
                shapesToIdentify.add(type);
            }
        }
        Collections.shuffle(shapesToIdentify);
        if (shapesToIdentify.size() > PRACTICE_COUNT) {
            shapesToIdentify = shapesToIdentify.subList(0, PRACTICE_COUNT);
        }

        // Top score panel
        scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);
        StyleUtil.styleLabel(scoreLabel, StyleUtil.TITLE_FONT, StyleUtil.MAIN_BLUE);
        add(scoreLabel, BorderLayout.NORTH);

        // Main question area
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        StyleUtil.stylePanel(centerPanel);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        imageLabel = new JLabel("", SwingConstants.CENTER);
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imageLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        centerPanel.add(imageLabel);

        shapeLabel = new JLabel("", SwingConstants.CENTER);
        StyleUtil.styleLabel(shapeLabel, StyleUtil.BIG_FONT, StyleUtil.MAIN_PURPLE);
        shapeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(shapeLabel);

        answerField = new JTextField();
        answerField.setMaximumSize(new Dimension(400, 40));
        answerField.setPreferredSize(new Dimension(400, 40));
        answerField.setFont(StyleUtil.NORMAL_FONT);
        answerField.setAlignmentX(Component.CENTER_ALIGNMENT);
        answerField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(StyleUtil.MAIN_BLUE, 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(answerField);

        submitButton = new JButton("Submit");
        StyleUtil.styleButton(submitButton, StyleUtil.MAIN_GREEN, Color.BLACK);
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(submitButton);

        feedbackLabel = new JLabel(" ", SwingConstants.CENTER);
        StyleUtil.styleLabel(feedbackLabel, StyleUtil.NORMAL_FONT, StyleUtil.MAIN_BLUE);
        feedbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(feedbackLabel);

        // Progress bar
        JPanel progressPanel = new JPanel();
        progressPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        StyleUtil.stylePanel(progressPanel);
        progressPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        for (int i = 0; i < PRACTICE_COUNT; i++) {
            progressLabels[i] = new JLabel("□");
            progressLabels[i].setFont(new Font("Dialog", Font.PLAIN, 36));
            progressLabels[i].setForeground(StyleUtil.MAIN_BLUE);
            progressPanel.add(progressLabels[i]);
        }
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(progressPanel);
        add(centerPanel, BorderLayout.CENTER);

        // Bottom Home button
        homeButton = new JButton("Home");
        StyleUtil.styleButton(homeButton, StyleUtil.MAIN_YELLOW, Color.BLACK);
        JPanel bottomPanel = new JPanel();
        StyleUtil.stylePanel(bottomPanel);
        bottomPanel.add(homeButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Event bindings
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSubmit();
            }
        });
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goHome();
            }
        });

        loadNextShape();
    }

    // Loads the next shape question
    private void loadNextShape() {
        if (currentIndex < shapesToIdentify.size()) {
            ShapeType currentShape = shapesToIdentify.get(currentIndex);
            currentQuestion = new ShapeQuestion(currentShape);

            String rawName = currentShape.name().toLowerCase().replace("_", " ");
            String[] words = rawName.split(" ");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < words.length; i++) {
                sb.append(Character.toUpperCase(words[i].charAt(0)))
                  .append(words[i].substring(1));
                if (i < words.length - 1) sb.append(" ");
            }
            String imageName = sb.toString();
            String filePath = "src/resources/images/task1/" + (is2DMode ? "2DShapes/" : "3DShapes/") + imageName + ".png";
            try {
                ImageIcon icon = new ImageIcon(filePath); // Use local file path directly
                if (icon.getIconWidth() == -1) {
                    imageLabel.setText("Image not found: " + imageName);
                    imageLabel.setIcon(null);
                } else {
                    Image image = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(image));
                    imageLabel.setText("");
                }
            } catch (Exception e) {
                imageLabel.setText("Error loading image: " + imageName);
                imageLabel.setIcon(null);
            }

            shapeLabel.setText("What shape is this?");
            answerField.setText("");
            feedbackLabel.setText(" ");
            attempts = 0;
            submitButton.setEnabled(true);
        } else {
            imageLabel.setIcon(null);
            shapeLabel.setText("Congratulations! You've completed all shapes!");
            answerField.setVisible(false);
            submitButton.setVisible(false);
            feedbackLabel.setText("Final Score: " + score + "/" + getMaxScore());
            if (is2DMode) completed2D = true;
            else completed3D = true;
        }
    }

    // Updates the progress bar with the result
    private void updateProgressBar(int idx, boolean correct) {
        if (idx >= 0 && idx < PRACTICE_COUNT) {
            progressLabels[idx].setText(correct ? "√" : "×");
            progressLabels[idx].setForeground(correct ? StyleUtil.MAIN_GREEN : StyleUtil.MAIN_PINK);
        }
    }

    // Handles the submission of an answer
    private void handleSubmit() {
        String userAnswer = answerField.getText();
        attempts++;
        submitButton.setEnabled(false);
        boolean correct = currentQuestion.checkAnswer(userAnswer);
        if (correct) {
            int points = ScoringUtil.getScore(isAdvanced, attempts);
            score += points;
            ScoreManager.getInstance().addScore(points);
            scoreLabel.setText("Score: " + score);
            feedbackLabel.setText("Correct! +" + points + " point(s). Great job!");
            updateProgressBar(currentIndex, true);
            currentIndex++;
            Timer timer = new Timer(1200, e -> loadNextShape());
            timer.setRepeats(false);
            timer.start();
        } else {
            if (attempts >= 3) {
                String correctAns = currentQuestion.getCorrectAnswer().replace("_", " ");
                feedbackLabel.setText("Incorrect! The correct answer is: " + correctAns);
                updateProgressBar(currentIndex, false);
                currentIndex++;
                Timer timer = new Timer(1800, e -> loadNextShape());
                timer.setRepeats(false);
                timer.start();
            } else {
                feedbackLabel.setText("Try again! Attempts left: " + (3 - attempts));
                submitButton.setEnabled(true);
            }
        }
    }

    // Returns to the main menu
    private void goHome() {
        parentFrame.setContentPane(new MainMenuPanel(parentFrame));
        parentFrame.revalidate();
    }

    // Calculates the maximum possible score
    private int getMaxScore() {
        return isAdvanced ? 24 : 12;
    }

    // Static methods to check completion status
    public static boolean is2DCompleted() { return completed2D; }
    public static boolean is3DCompleted() { return completed3D; }
}