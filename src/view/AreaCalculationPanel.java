package src.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class AreaCalculationPanel extends JPanel {
    private JFrame parentFrame;
    private List<String> shapeTypes;
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

    public AreaCalculationPanel(JFrame frame) {
        this.parentFrame = frame;
        setLayout(new BorderLayout(10, 10));

        // 支持的图形类型
        shapeTypes = new ArrayList<>();
        shapeTypes.add("Rectangle");
        shapeTypes.add("Triangle");
        shapeTypes.add("Parallelogram");
        shapeTypes.add("Trapezium");
        Collections.shuffle(shapeTypes);

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
        JButton circleButton = new JButton("Go to Circle Calculation");
        bottomPanel.add(circleButton);
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
        circleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentFrame.setContentPane(new CircleCalculationPanel(parentFrame));
                parentFrame.revalidate();
            }
        });

        loadNextQuestion();
    }

    private void loadNextQuestion() {
        if (currentIndex >= shapeTypes.size()) {
            feedbackLabel.setText("All area questions completed! Your final score: " + score);
            submitButton.setEnabled(false);
            answerField.setEnabled(false);
            imageLabel.setIcon(null);
            questionLabel.setText("");
            return;
        }
        String shape = shapeTypes.get(currentIndex);
        // 加载图片
        String imageName = shape + ".png";
        String absPath = "src/resources/images/task3/" + imageName;
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
            imageLabel.setText("[No image for this shape]");
        }
        imageLabel.setIcon(icon);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        Random rand = new Random();
        switch (shape) {
            case "Rectangle": {
                double length = rand.nextInt(16) + 5; // 5-20
                double width = rand.nextInt(16) + 5;  // 5-20
                params = new double[]{length, width};
                correctAnswer = length * width;
                formula = "Area = length × width";
                questionLabel.setText("Rectangle: length = " + length + ", width = " + width + ". What is the area?");
                break;
            }
            case "Triangle": {
                double base = rand.nextInt(16) + 5;
                double height = rand.nextInt(16) + 5;
                params = new double[]{base, height};
                correctAnswer = 0.5 * base * height;
                formula = "Area = 0.5 × base × height";
                questionLabel.setText("Triangle: base = " + base + ", height = " + height + ". What is the area?");
                break;
            }
            case "Parallelogram": {
                double base = rand.nextInt(16) + 5;
                double height = rand.nextInt(16) + 5;
                params = new double[]{base, height};
                correctAnswer = base * height;
                formula = "Area = base × height";
                questionLabel.setText("Parallelogram: base = " + base + ", height = " + height + ". What is the area?");
                break;
            }
            case "Trapezium": {
                double base1 = rand.nextInt(16) + 5;
                double base2 = rand.nextInt(16) + 5;
                double height = rand.nextInt(16) + 5;
                params = new double[]{base1, base2, height};
                correctAnswer = 0.5 * (base1 + base2) * height;
                formula = "Area = 0.5 × (base1 + base2) × height";
                questionLabel.setText("Trapezium: base1 = " + base1 + ", base2 = " + base2 + ", height = " + height + ". What is the area?");
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
            correct = Math.abs(ans - correctAnswer) < 0.01;
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