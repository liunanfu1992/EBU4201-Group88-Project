package src.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    // 题目数据
    private double correctAnswer;
    private String breakdown;

    public SectorAreaPanel(JFrame frame, int sectorIndex, boolean[] completed) {
        this.parentFrame = frame;
        this.sectorIndex = sectorIndex;
        this.completed = completed;
        setLayout(new BorderLayout(10, 10));
        timerLabel = new JLabel("Time left: 300s", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(timerLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        imageLabel = new JLabel("", SwingConstants.CENTER);
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imageLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        centerPanel.add(imageLabel);
        centerPanel.add(Box.createVerticalStrut(10));
        questionLabel = new JLabel("", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 20));
        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(questionLabel);
        centerPanel.add(Box.createVerticalStrut(10));
        answerField = new JTextField();
        answerField.setMaximumSize(new Dimension(120, 32));
        answerField.setPreferredSize(new Dimension(120, 32));
        answerField.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(answerField);
        centerPanel.add(Box.createVerticalStrut(10));
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
        centerPanel.add(Box.createVerticalStrut(10));
        JLabel answerImageLabel = new JLabel("");
        answerImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        answerImageLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        centerPanel.add(answerImageLabel);
        add(centerPanel, BorderLayout.CENTER);

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
        // 题目数据（示例，实际应补全所有题目数据）
        String[] imgPaths = {
            "src/resources/images/task6/Shape 1.jpg",
            "src/resources/images/task6/Shape 2.jpg",
            "src/resources/images/task6/Shape 3.jpg",
            "src/resources/images/task6/Shape 4.jpg",
            "src/resources/images/task6/Shape 5.jpg",
            "src/resources/images/task6/Shape 6.jpg",
            "src/resources/images/task6/Shape 7.jpg",
            "src/resources/images/task6/Shape 8.jpg"
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
        // 计算正确答案
        double[] answers = new double[8];
        String[] breakdowns = new String[8];
        for (int i = 0; i < 8; i++) {
            answers[i] = angles[i] / 360.0 * 3.14 * radii[i] * radii[i];
            breakdowns[i] = String.format("Area = (%.0f/360) × π × %.2f² = (%.0f/360) × 3.14 × %.2f × %.2f = %.2f %s²", angles[i], radii[i], angles[i], radii[i], radii[i], answers[i], units[i]);
        }
        ImageIcon icon = new ImageIcon(imgPaths[idx]);
        Image img = icon.getImage().getScaledInstance(160, 160, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(img));
        questionLabel.setText(questions[idx]);
        correctAnswer = answers[idx];
        breakdown = breakdowns[idx];
        answerField.setText("");
        feedbackLabel.setText(" ");
        attempts = 0;
        submitButton.setEnabled(true);
        answerField.setEnabled(true);
        // 清空答案图片
        JPanel centerPanel = (JPanel) imageLabel.getParent();
        JLabel answerImageLabel = null;
        for (Component comp : centerPanel.getComponents()) {
            if (comp instanceof JLabel && comp != imageLabel && comp != feedbackLabel) {
                answerImageLabel = (JLabel) comp;
            }
        }
        if (answerImageLabel != null) {
            answerImageLabel.setIcon(null);
        }
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
        // 保留题目原图，在下方展示答案图片
        String answerImgPath = String.format("src/resources/images/task6/answer/answer%d.jpg", sectorIndex + 1);
        ImageIcon answerIcon = new ImageIcon(answerImgPath);
        int targetWidth = 400;
        int imgW = answerIcon.getIconWidth();
        int imgH = answerIcon.getIconHeight();
        int targetHeight = (int) (imgH * (targetWidth / (double) imgW));
        Image answerImg = answerIcon.getImage().getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        JPanel centerPanel = (JPanel) imageLabel.getParent();
        JLabel answerImageLabel = null;
        for (Component comp : centerPanel.getComponents()) {
            if (comp instanceof JLabel && comp != imageLabel && comp != feedbackLabel) {
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
        parentFrame.setContentPane(new SectorSelectionPanel(parentFrame, completed));
        parentFrame.revalidate();
    }

    private void goHome() {
        if (timer != null) timer.stop();
        parentFrame.setContentPane(new MainMenuPanel(parentFrame));
        parentFrame.revalidate();
    }
} 