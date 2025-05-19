package src.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import src.model.ScoringUtil;
import src.model.ScoreManager;

public class CompoundShapePanel extends JPanel {
    private JFrame parentFrame;
    private int shapeIndex;
    private boolean[] completed;
    private int attempts = 0;
    private int score = 0;
    private JLabel timerLabel;
    private Timer timer;
    private int timeLeft = 300;

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

    private final boolean isAdvanced = true; // 复合图形为高级题型

    public CompoundShapePanel(JFrame frame, int shapeIndex, boolean[] completed) {
        this.parentFrame = frame;
        this.shapeIndex = shapeIndex;
        this.completed = completed;
        setLayout(new BorderLayout(10, 10));
        timerLabel = new JLabel("Time left: 300s", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(timerLabel, BorderLayout.NORTH);

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
        answerField.setMaximumSize(new Dimension(120, 32));
        answerField.setPreferredSize(new Dimension(120, 32));
        answerField.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(answerField);

        submitButton = new JButton("Submit");
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(submitButton);
        centerPanel.add(Box.createVerticalStrut(10));
        feedbackLabel = new JLabel(" ", SwingConstants.CENTER);
        feedbackLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        feedbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel feedbackPanel = new JPanel();
        feedbackPanel.setLayout(new BoxLayout(feedbackPanel, BoxLayout.X_AXIS));
        feedbackPanel.add(Box.createHorizontalGlue());
        feedbackPanel.add(feedbackLabel);
        feedbackPanel.add(Box.createHorizontalGlue());
        centerPanel.add(feedbackPanel);
        JLabel answerImageLabel = new JLabel("");
        answerImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(answerImageLabel);

        add(centerPanel, BorderLayout.CENTER);

        // 底部Home按钮
        homeButton = new JButton("Home");
        JButton backButton = new JButton("Back to Selection");
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        bottomPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        bottomPanel.add(Box.createHorizontalGlue());
        bottomPanel.add(homeButton);
        bottomPanel.add(Box.createHorizontalStrut(20));
        bottomPanel.add(backButton);
        bottomPanel.add(Box.createHorizontalGlue());
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
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBackToSelection();
            }
        });

        loadQuestionByIndex(shapeIndex);
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
        // 只保留剩余6题
        String[] imgPaths = {
            "src/resources/images/task5/Shape 2.jpg", // 原索引1
            "src/resources/images/task5/Shape 3.jpg", // 原索引2
            "src/resources/images/task5/Shape 4.jpg", // 原索引3
            "src/resources/images/task5/Shape 5.jpg", // 原索引4
            "src/resources/images/task5/Shape 8.jpg", // 原索引7
            "src/resources/images/task5/Shape 9.jpg"  // 原索引8
        };
        String[] questions = {
            "Calculate the area of this compound shape (split as shown).",
            "Calculate the area of this compound shape (split as shown).",
            "Calculate the area of this compound shape (split as shown).",
            "Calculate the area of this compound shape (split as shown).",
            "Calculate the area of this compound shape (split as shown).",
            "Calculate the area of this compound shape (split as shown)."
        };
        double[] answers = {310, 598, 288, 18, 3456, 174};
        String[] breakdowns = {
            "Area = 11×21 + 10×10 = 231 + 100 = 331 cm²",
            "Area = 18×19 + 16×16 = 342 + 256 = 598 cm²",
            "Area = 12×12 + 10×6 = 144 + 60 = 204 cm²",
            "Not available yet.",
            "Area = 36×36 + 36×60 = 1296 + 2160 = 3456 m²",
            "Area = 10×11 + 8×8 = 110 + 64 = 174 m²"
        };
        if (idx < 0 || idx >= imgPaths.length) {
            questionLabel.setText("This shape is not available.");
            submitButton.setEnabled(false);
            answerField.setEnabled(false);
            return;
        }
        ImageIcon icon = new ImageIcon(imgPaths[idx]);
        Image img = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(img));
        questionLabel.setText(questions[idx]);
        correctAnswer = answers[idx];
        breakdown = breakdowns[idx];
        answerField.setText("");
        feedbackLabel.setText(" ");
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
            // 保留两位小数进行比较
            double ans2 = Math.round(ans * 100.0) / 100.0;
            double correct2 = Math.round(correctAnswer * 100.0) / 100.0;
            correct = Math.abs(ans2 - correct2) < 0.05 * correct2;
        } catch (Exception e) {
            // ignore parse error
        }
        if (correct) {
            timer.stop();
            completed[shapeIndex] = true;
            int points = ScoringUtil.getScore(isAdvanced, attempts);
            score += points;
            ScoreManager.getInstance().addScore(points);
            scoreLabel.setText("Score: " + score);
            feedbackLabel.setText("Correct! +" + points + " points. Great job!");
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
        // 展示答案图片
        int[] validIdx = {1, 2, 3, 4, 7, 8};
        String answerImgPath = String.format("src/resources/images/task5/answer/answer%d.jpg", validIdx[shapeIndex] + 1);
        ImageIcon answerIcon = new ImageIcon(answerImgPath);
        int targetWidth = 320;
        int imgW = answerIcon.getIconWidth();
        int imgH = answerIcon.getIconHeight();
        int targetHeight = (int) (imgH * (targetWidth / (double) imgW));
        Image answerImg = answerIcon.getImage().getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        // 找到centerPanel中的answerImageLabel（submit下方的那个）
        JPanel centerPanel = (JPanel) imageLabel.getParent();
        JLabel answerImageLabel = null;
        // 由于插入了feedbackPanel，答案图片label索引需+3
        int submitIdx = -1;
        for (int i = 0; i < centerPanel.getComponentCount(); i++) {
            if (centerPanel.getComponent(i) == submitButton) {
                submitIdx = i;
            }
        }
        if (submitIdx != -1 && submitIdx + 3 < centerPanel.getComponentCount()) {
            Component comp = centerPanel.getComponent(submitIdx + 3);
            if (comp instanceof JLabel) {
                answerImageLabel = (JLabel) comp;
            }
        }
        if (answerImageLabel != null) {
            answerImageLabel.setIcon(new ImageIcon(answerImg));
            answerImageLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
            answerImageLabel.setVisible(true);
        }
        centerPanel.revalidate();
        centerPanel.repaint();
        feedbackLabel.setText(correct ? "Correct! Well done!" : "Incorrect, here is the answer.");
        // 不再自动跳转，由用户点击按钮决定下一步
    }

    private void goBackToSelection() {
        parentFrame.setContentPane(new CompoundShapeSelectionPanel(parentFrame, completed));
        parentFrame.revalidate();
    }

    private void goHome() {
        if (timer != null) timer.stop();
        parentFrame.setContentPane(new MainMenuPanel(parentFrame));
        parentFrame.revalidate();
    }
} 