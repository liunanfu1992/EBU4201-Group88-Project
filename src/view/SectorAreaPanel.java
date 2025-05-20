package src.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import src.model.ScoringUtil;
import src.model.ScoreManager;

public class SectorAreaPanel extends JPanel {
    private JFrame parentFrame;
    private int sectorIndex;
    private boolean[] completed;
    private int attempts = 0;
    private JLabel timerLabel;
    private Timer timer;
    private int timeLeft = 300;
    private JLabel questionLabel;
    private JTextField answerField;
    private JButton submitButton;
    private JButton homeButton;
    private JLabel feedbackLabel;
    private JLabel imageLabel;
    private JLabel answerImageLabel;

    // Question data
    private double correctAnswer;
    private String breakdown;

    private int score = 0;
    private final boolean isAdvanced = true;

    public SectorAreaPanel(JFrame frame, int sectorIndex, boolean[] completed) {
        this.parentFrame = frame;
        this.sectorIndex = sectorIndex;
        this.completed = completed;
        setLayout(new BorderLayout(10, 10));
        StyleUtil.stylePanel(this);

        // Top timer and score area
        JPanel topPanel = new JPanel(new GridLayout(1, 2));
        StyleUtil.stylePanel(topPanel);
        timerLabel = new JLabel("Time left: 300s", SwingConstants.CENTER);
        StyleUtil.styleLabel(timerLabel, StyleUtil.NORMAL_FONT, StyleUtil.MAIN_PURPLE);
        JLabel scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);
        StyleUtil.styleLabel(scoreLabel, StyleUtil.NORMAL_FONT, StyleUtil.MAIN_BLUE);
        topPanel.add(timerLabel);
        topPanel.add(scoreLabel);
        add(topPanel, BorderLayout.NORTH);

        // Middle content
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        StyleUtil.stylePanel(centerPanel);
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.setAlignmentY(Component.CENTER_ALIGNMENT);

        imageLabel = new JLabel("", SwingConstants.CENTER);
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imageLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        centerPanel.add(imageLabel);
        centerPanel.add(Box.createVerticalStrut(10));

        questionLabel = new JLabel("", SwingConstants.CENTER);
        StyleUtil.styleLabel(questionLabel, StyleUtil.BIG_FONT, StyleUtil.MAIN_BLUE);
        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(questionLabel);
        centerPanel.add(Box.createVerticalStrut(10));

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
        inputPanel.add(areaLabel);
        inputPanel.add(answerField);
        submitButton = new JButton("Submit");
        StyleUtil.styleButton(submitButton, StyleUtil.MAIN_GREEN, Color.BLACK);
        inputPanel.add(submitButton);
        centerPanel.add(inputPanel);
        centerPanel.add(Box.createVerticalStrut(10));

        // Feedback area
        feedbackLabel = new JLabel(" ", SwingConstants.CENTER);
        StyleUtil.styleLabel(feedbackLabel, StyleUtil.NORMAL_FONT, StyleUtil.MAIN_BLUE);
        feedbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel feedbackPanel = new JPanel();
        StyleUtil.stylePanel(feedbackPanel);
        feedbackPanel.setLayout(new BoxLayout(feedbackPanel, BoxLayout.X_AXIS));
        feedbackPanel.add(Box.createHorizontalGlue());
        feedbackPanel.add(feedbackLabel);
        feedbackPanel.add(Box.createHorizontalGlue());
        centerPanel.add(feedbackPanel);
        centerPanel.add(Box.createVerticalStrut(10));

        // Answer image area
        answerImageLabel = new JLabel("");
        answerImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        answerImageLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        centerPanel.add(answerImageLabel);

        add(centerPanel, BorderLayout.CENTER);

        // Bottom buttons
        homeButton = new JButton("Home");
        StyleUtil.styleButton(homeButton, StyleUtil.MAIN_YELLOW, Color.BLACK);
        homeButton.setFont(StyleUtil.NORMAL_FONT);
        JButton backButton = new JButton("Back to Selection");
        StyleUtil.styleButton(backButton, StyleUtil.MAIN_YELLOW, Color.BLACK);
        backButton.setFont(StyleUtil.NORMAL_FONT);
        JPanel bottomPanel = new JPanel();
        StyleUtil.stylePanel(bottomPanel);
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        bottomPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        bottomPanel.add(Box.createHorizontalGlue());
        bottomPanel.add(homeButton);
        bottomPanel.add(Box.createHorizontalStrut(20));
        bottomPanel.add(backButton);
        bottomPanel.add(Box.createHorizontalGlue());
        add(bottomPanel, BorderLayout.SOUTH);

        // Event binding
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
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBackToSelection();
            }
        });

        loadQuestionByIndex(sectorIndex);
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

    private void loadQuestionByIndex(int idx) {
        // Question data (example, should be completed with all question data)
        String[] imgPaths = {
            "resources/images/task6/Shape 1.jpg",
            "resources/images/task6/Shape 2.jpg",
            "resources/images/task6/Shape 3.jpg",
            "resources/images/task6/Shape 4.jpg",
            "resources/images/task6/Shape 5.jpg",
            "resources/images/task6/Shape 6.jpg",
            "resources/images/task6/Shape 7.jpg",
            "resources/images/task6/Shape 8.jpg"
        };
        String[] questions = {
            "Sector: radius = 8 cm, angle = 90°. What is the area? (Use π ≈ 3.14)",
            "Sector: radius = 18 ft, angle = 130°. What is the area? (Use π ≈ 3.14)",
            "Sector: radius = 19 cm, angle = 240°. What is the area? (Use π ≈ 3.14)",
            "Sector: radius = 22 ft, angle = 110°. What is the area? (Use π ≈ 3.14)",
            "Sector: radius = 3.5 in, angle = 100°. What is the area? (Use π ≈ 3.14)",
            "Sector: radius = 8 in, angle = 270°. What is the area? (Use π ≈ 3.14)",
            "Sector: radius = 12 yd, angle = 280°. What is the area? (Use π ≈ 3.14)",
            "Sector: radius = 15 mm, angle = 250°. What is the area? (Use π ≈ 3.14)"
        };
        double[] radii = {8, 18, 19, 22, 3.5, 8, 12, 15};
        double[] angles = {90, 130, 240, 110, 100, 270, 280, 250};
        String[] units = {"cm", "ft", "cm", "ft", "in", "in", "yd", "mm"};
        double[] answers = new double[8];
        String[] breakdowns = new String[8];
        for (int i = 0; i < 8; i++) {
            answers[i] = angles[i] / 360.0 * 3.14 * radii[i] * radii[i];
            breakdowns[i] = String.format("Area = (%.0f/360) × π × %.2f² = (%.0f/360) × 3.14 × %.2f × %.2f = %.2f %s²", angles[i], radii[i], angles[i], radii[i], radii[i], answers[i], units[i]);
        }
        ImageIcon icon = new ImageIcon("src/" + imgPaths[idx]);
        Image img = icon.getImage().getScaledInstance(160, 160, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(img));
        questionLabel.setText(questions[idx]);
        correctAnswer = answers[idx];
        breakdown = breakdowns[idx];
        answerField.setText("");
        feedbackLabel.setText(" ");
        answerImageLabel.setIcon(null);
        attempts = 0;
        submitButton.setEnabled(true);
        answerField.setEnabled(true);
    }

    private void handleSubmit() {
        String userAnswer = answerField.getText();
        attempts++;
        boolean correct = false;
        try {
            double ans = Double.parseDouble(userAnswer);
            correct = Math.abs(ans - correctAnswer) < 0.05 * correctAnswer;
        } catch (Exception e) {
            // ignore parse error
        }
        if (correct) {
            timer.stop();
            completed[sectorIndex] = true;
            int points = ScoringUtil.getScore(isAdvanced, attempts);
            score += points;
            ScoreManager.getInstance().addScore(points);
            feedbackLabel.setText("Correct! +" + points + " points. Great job!");
            timerLabel.setText("Score: " + score);
            showSolution(true, false);
        } else {
            if (attempts >= 3) {
                timer.stop();
                showSolution(false, false);
            } else {
                feedbackLabel.setText("Try again! Attempts left: " + (3 - attempts));
            }
        }
    }

    private void showSolution(boolean correct, boolean timeout) {
        answerField.setEnabled(false);
        submitButton.setEnabled(false);
        String answerImgPath = String.format("src/resources/images/task6/answer/answer%d.jpg", sectorIndex + 1);
        ImageIcon answerIcon = new ImageIcon(answerImgPath);
        int targetWidth = 400;
        int imgW = answerIcon.getIconWidth();
        int imgH = answerIcon.getIconHeight();
        int targetHeight = (int) (imgH * (targetWidth / (double) imgW));
        Image answerImg = answerIcon.getImage().getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        if (answerImageLabel != null) {
            answerImageLabel.setIcon(new ImageIcon(answerImg));
            answerImageLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
            answerImageLabel.setVisible(true);
        }
        feedbackLabel.setText(correct ? "Correct! Well done!" : "Incorrect, here is the answer.");
    }

    private void goBackToSelection() {
        parentFrame.setContentPane(new SectorSelectionPanel(parentFrame, completed));
        parentFrame.revalidate();
    }

    private void goHome() {
        if (timer != null) timer.stop();
        parentFrame.setContentPane(new MainMenuPanel(parentFrame));
        parentFrame.revalidate();
    }
}