package src.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class CompoundShapePanel extends JPanel {
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
    private double[] params;
    private double correctAnswer;
    private String formula;
    private String breakdown;

    public CompoundShapePanel(JFrame frame) {
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
        questionLabel.setFont(new Font("Arial", Font.BOLD, 20));
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
        JButton sectorButton = new JButton("Go to Sector Calculation");
        bottomPanel.add(sectorButton);
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
        sectorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentFrame.setContentPane(new SectorCalculationPanel(parentFrame));
                parentFrame.revalidate();
            }
        });

        loadNextQuestion();
    }

    private void loadNextQuestion() {
        if (currentIndex >= 3) { // 3 compound questions
            feedbackLabel.setText("All compound shape questions completed! Your final score: " + score);
            submitButton.setEnabled(false);
            answerField.setEnabled(false);
            imageLabel.setIcon(null);
            questionLabel.setText("");
            return;
        }
        Random rand = new Random();
        int type = rand.nextInt(3); // 0: rectangle+triangle, 1: rectangle+half circle, 2: rectangle+rectangle
        // 加载图片
        String imageName = "Compound" + (type + 1) + ".png";
        String absPath = "src/resources/images/task5/" + imageName;
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
            imageLabel.setText("[No image for this compound shape]");
        }
        imageLabel.setIcon(icon);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        switch (type) {
            case 0: { // rectangle + triangle
                double rectL = rand.nextInt(11) + 10; // 10-20
                double rectW = rand.nextInt(11) + 5;  // 5-15
                double triB = rectW;
                double triH = rand.nextInt(6) + 5;    // 5-10
                double areaRect = rectL * rectW;
                double areaTri = 0.5 * triB * triH;
                correctAnswer = areaRect + areaTri;
                formula = "Total Area = Rectangle + Triangle";
                breakdown = String.format("Rectangle: %.2f × %.2f = %.2f; Triangle: 0.5 × %.2f × %.2f = %.2f", rectL, rectW, areaRect, triB, triH, areaTri);
                questionLabel.setText("A compound shape consists of a rectangle (length=" + rectL + ", width=" + rectW + ") and a triangle (base=" + triB + ", height=" + triH + "). What is the total area?");
                break;
            }
            case 1: { // rectangle + half circle
                double rectL = rand.nextInt(11) + 10;
                double rectW = rand.nextInt(11) + 5;
                double radius = rectW / 2.0;
                double areaRect = rectL * rectW;
                double areaHalfCircle = 0.5 * Math.PI * radius * radius;
                correctAnswer = areaRect + areaHalfCircle;
                formula = "Total Area = Rectangle + Half Circle";
                breakdown = String.format("Rectangle: %.2f × %.2f = %.2f; Half Circle: 0.5 × π × %.2f² = %.2f", rectL, rectW, areaRect, radius, areaHalfCircle);
                questionLabel.setText("A compound shape consists of a rectangle (length=" + rectL + ", width=" + rectW + ") and a half circle (radius=" + radius + "). What is the total area? (Use π ≈ 3.14)");
                break;
            }
            case 2: { // rectangle + rectangle
                double rectL1 = rand.nextInt(11) + 10;
                double rectW1 = rand.nextInt(11) + 5;
                double rectL2 = rand.nextInt(6) + 5;
                double rectW2 = rand.nextInt(6) + 5;
                double area1 = rectL1 * rectW1;
                double area2 = rectL2 * rectW2;
                correctAnswer = area1 + area2;
                formula = "Total Area = Rectangle1 + Rectangle2";
                breakdown = String.format("Rectangle1: %.2f × %.2f = %.2f; Rectangle2: %.2f × %.2f = %.2f", rectL1, rectW1, area1, rectL2, rectW2, area2);
                questionLabel.setText("A compound shape consists of two rectangles (length1=" + rectL1 + ", width1=" + rectW1 + "; length2=" + rectL2 + ", width2=" + rectW2 + "). What is the total area?");
                break;
            }
        }
        answerField.setText("");
        feedbackLabel.setText(" ");
        attempts = 0;
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
                feedbackLabel.setText("Incorrect! The correct answer is: " + String.format("%.2f", correctAnswer) + " (" + formula + ")<br>Breakdown: " + breakdown);
                currentIndex++;
                Timer timer = new Timer(3200, e -> loadNextQuestion());
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