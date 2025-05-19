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
    private boolean is2DMode;

    private JLabel shapeLabel;
    private JTextField answerField;
    private JButton submitButton;
    private JButton homeButton;
    private JLabel feedbackLabel;
    private JLabel scoreLabel;
    private JLabel imageLabel;

    public ShapeIdentificationPanel(JFrame frame, boolean is2D) {
        this.parentFrame = frame;
        this.is2DMode = is2D;
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
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        imageLabel = new JLabel("", SwingConstants.CENTER);
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imageLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0)); // 上下留白
        centerPanel.add(imageLabel);
        shapeLabel = new JLabel("", SwingConstants.CENTER);
        shapeLabel.setFont(new Font("Arial", Font.BOLD, 32));
        shapeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(shapeLabel);
        answerField = new JTextField();
        answerField.setMaximumSize(new Dimension(400, 40));
        answerField.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(answerField);
        submitButton = new JButton("Submit");
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(submitButton);
        feedbackLabel = new JLabel(" ", SwingConstants.CENTER);
        feedbackLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        feedbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(Box.createVerticalStrut(10));
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
        if (currentIndex < shapesToIdentify.size()) {
            ShapeType currentShape = shapesToIdentify.get(currentIndex);
            currentQuestion = new ShapeQuestion(currentShape);
            
            // 优化图片名生成逻辑：下划线转空格，单词首字母大写
            String rawName = currentShape.name().toLowerCase().replace("_", " ");
            String[] words = rawName.split(" ");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < words.length; i++) {
                sb.append(Character.toUpperCase(words[i].charAt(0)))
                  .append(words[i].substring(1));
                if (i < words.length - 1) sb.append(" ");
            }
            String imageName = sb.toString();
            String filePath = "resources/images/task1/" + (is2DMode ? "2DShapes/" : "3DShapes/") + imageName + ".png";
            try {
                ImageIcon icon = new ImageIcon(filePath); // 直接用本地文件路径
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
        } else {
            imageLabel.setIcon(null);
            shapeLabel.setText("Congratulations! You've completed all shapes!");
            answerField.setVisible(false);
            submitButton.setVisible(false);
            feedbackLabel.setText("Final Score: " + score + "/" + shapesToIdentify.size());
        }
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
                // 正确答案下划线替换为空格
                String correct = currentQuestion.getCorrectAnswer().replace("_", " ");
                feedbackLabel.setText("Incorrect! The correct answer is: " + correct);
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