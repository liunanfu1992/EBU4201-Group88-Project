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

public class ShapeIdentificationPanel extends JPanel {
    private JFrame parentFrame;
    private List<ShapeType> shapesToIdentify;
    private int currentIndex = 0;
    private ShapeQuestion currentQuestion;
    private int attempts = 0;
    private int score = 0;

    private JLabel shapeLabel;
    private JTextField answerField;
    private JButton submitButton;
    private JButton homeButton;
    private JLabel feedbackLabel;
    private JLabel scoreLabel;

    public ShapeIdentificationPanel(JFrame frame, boolean is2D) {
        this.parentFrame = frame;
        setLayout(new BorderLayout(10, 10));

        // Select 2D or 3D shapes
        shapesToIdentify = new ArrayList<>();
        for (ShapeType type : ShapeType.values()) {
            if (is2D && type.ordinal() <= ShapeType.KITE.ordinal()) {
                shapesToIdentify.add(type);
            } else if (!is2D && type.ordinal() > ShapeType.KITE.ordinal()) {
                shapesToIdentify.add(type);
            }
        }
        Collections.shuffle(shapesToIdentify);

        // Top score label
        scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(scoreLabel, BorderLayout.NORTH);

        // Center question panel
        JPanel centerPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        shapeLabel = new JLabel("", SwingConstants.CENTER);
        shapeLabel.setFont(new Font("Arial", Font.BOLD, 32));
        centerPanel.add(shapeLabel);

        answerField = new JTextField();
        centerPanel.add(answerField);

        submitButton = new JButton("Submit");
        centerPanel.add(submitButton);

        feedbackLabel = new JLabel(" ", SwingConstants.CENTER);
        feedbackLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        centerPanel.add(feedbackLabel);

        add(centerPanel, BorderLayout.CENTER);

        // Bottom Home button
        homeButton = new JButton("Home");
        JPanel bottomPanel = new JPanel();
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

    private void loadNextShape() {
        if (currentIndex >= shapesToIdentify.size()) {
            feedbackLabel.setText("All shapes identified! Your final score: " + score);
            submitButton.setEnabled(false);
            answerField.setEnabled(false);
            return;
        }
        ShapeType shape = shapesToIdentify.get(currentIndex);
        currentQuestion = new ShapeQuestion(shape);
        shapeLabel.setText(shape.name().charAt(0) + shape.name().substring(1).toLowerCase()); // Use English name for now
        answerField.setText("");
        feedbackLabel.setText(" ");
        attempts = 0;
    }

    private void handleSubmit() {
        String userAnswer = answerField.getText();
        attempts++;
        if (currentQuestion.checkAnswer(userAnswer)) {
            int points = 4 - attempts; // 1st:3, 2nd:2, 3rd:1
            if (points < 1) points = 1;
            score += points;
            scoreLabel.setText("Score: " + score);
            feedbackLabel.setText("Correct! +" + points + " point(s). Great job!");
            currentIndex++;
            Timer timer = new Timer(1200, e -> loadNextShape());
            timer.setRepeats(false);
            timer.start();
        } else {
            if (attempts >= 3) {
                feedbackLabel.setText("Incorrect! The correct answer is: " + currentQuestion.getCorrectAnswer());
                currentIndex++;
                Timer timer = new Timer(1800, e -> loadNextShape());
                timer.setRepeats(false);
                timer.start();
            } else {
                feedbackLabel.setText("Try again! Attempts left: " + (3 - attempts));
            }
        }
    }

    private void goHome() {
        parentFrame.setContentPane(new MainMenuPanel(parentFrame));
        parentFrame.revalidate();
    }
} 