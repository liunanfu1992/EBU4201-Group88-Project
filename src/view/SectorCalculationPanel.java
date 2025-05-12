package src.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class SectorCalculationPanel extends JPanel {
    private JFrame parentFrame;
    private int currentIndex = 0;
    private int attempts = 0;
    private int score = 0;

    private JLabel questionLabel;
    private JTextField answerField;
    private JButton submitButton;
    private JButton homeButton;
    private JLabel feedbackLabel;
    private JLabel scoreLabel;
    private JLabel imageLabel;

    // 当前题目参数
    private double radius;
    private double angle;
    private boolean isAreaQuestion;
    private double correctAnswer;
    private String formula;

    public SectorCalculationPanel(JFrame frame) {
        this.parentFrame = frame;
        setLayout(new BorderLayout(10, 10));

        // 顶部分数
        scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(scoreLabel, BorderLayout.NORTH);

        // 中间题目
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        imageLabel = new JLabel("", SwingConstants.CENTER);
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imageLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        centerPanel.add(imageLabel);
        questionLabel = new JLabel("", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 22));
        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(questionLabel);

        answerField = new JTextField();
        centerPanel.add(answerField);

        submitButton = new JButton("Submit");
        centerPanel.add(submitButton);

        feedbackLabel = new JLabel(" ", SwingConstants.CENTER);
        feedbackLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        centerPanel.add(feedbackLabel);

        add(centerPanel, BorderLayout.CENTER);

        // 底部Home按钮
        homeButton = new JButton("Home");
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(homeButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // 事件绑定
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

        loadNextQuestion();
    }

    private void loadNextQuestion() {
        if (currentIndex >= 4) { // 2 area + 2 arc length
            feedbackLabel.setText("All sector questions completed! Your final score: " + score);
            submitButton.setEnabled(false);
            answerField.setEnabled(false);
            imageLabel.setIcon(null);
            questionLabel.setText("");
            return;
        }
        Random rand = new Random();
        radius = rand.nextInt(11) + 5; // 5-15
        angle = (rand.nextInt(6) + 1) * 30; // 30, 60, 90, 120, 150, 180
        isAreaQuestion = (currentIndex % 2 == 0);
        if (isAreaQuestion) {
            correctAnswer = Math.PI * radius * radius * (angle / 360.0);
            formula = "Area = π × r² × (θ/360)";
            questionLabel.setText("Sector: radius = " + radius + ", angle = " + angle + "°. What is the area? (Use π ≈ 3.14)");
        } else {
            correctAnswer = 2 * Math.PI * radius * (angle / 360.0);
            formula = "Arc Length = 2 × π × r × (θ/360)";
            questionLabel.setText("Sector: radius = " + radius + ", angle = " + angle + "°. What is the arc length? (Use π ≈ 3.14)");
        }
        answerField.setText("");
        feedbackLabel.setText(" ");
        attempts = 0;

        // 加载扇形图片
        String absPath = "src/resources/images/task6/Sector.png";
        ImageIcon icon = null;
        try {
            ImageIcon rawIcon = new ImageIcon(absPath);
            int imgWidth = rawIcon.getIconWidth();
            int imgHeight = rawIcon.getIconHeight();
            int maxDim = 180;
            int newWidth = imgWidth, newHeight = imgHeight;
            if (imgWidth > maxDim || imgHeight > maxDim) {
                double scale = Math.min((double)maxDim / imgWidth, (double)maxDim / imgHeight);
                newWidth = (int)(imgWidth * scale);
                newHeight = (int)(imgHeight * scale);
            }
            Image scaledImg = rawIcon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            icon = new ImageIcon(scaledImg);
            imageLabel.setText("");
        } catch (Exception e) {
            imageLabel.setText("[No image for sector]");
        }
        imageLabel.setIcon(icon);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
    }

    private void handleSubmit() {
        String userAnswer = answerField.getText();
        attempts++;
        boolean correct = false;
        try {
            double ans = Double.parseDouble(userAnswer);
            correct = Math.abs(ans - correctAnswer) < 0.05 * correctAnswer; // 5% 容差
        } catch (Exception e) {
            // ignore parse error
        }
        if (correct) {
            int points = 4 - attempts; // 1st:3, 2nd:2, 3rd:1
            if (points < 1) points = 1;
            score += points;
            scoreLabel.setText("Score: " + score);
            feedbackLabel.setText("Correct! +" + points + " point(s). Well done!");
            currentIndex++;
            Timer timer = new Timer(1200, e -> loadNextQuestion());
            timer.setRepeats(false);
            timer.start();
        } else {
            if (attempts >= 3) {
                feedbackLabel.setText("Incorrect! The correct answer is: " + String.format("%.2f", correctAnswer) + " (" + formula + ")");
                currentIndex++;
                Timer timer = new Timer(2200, e -> loadNextQuestion());
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