package src.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RectangleAreaQuestionPanel extends JPanel {
    private JFrame parentFrame;
    private int length, width, correctArea, attempts = 0;
    private JLabel timerLabel, feedbackLabel, formulaLabel, calcLabel;
    private JTextField answerField;
    private JButton submitButton, nextButton, homeButton;
    private Timer timer;
    private int timeLeft = 180; // 3分钟

    public RectangleAreaQuestionPanel(JFrame frame) {
        this.parentFrame = frame;
        setLayout(new BorderLayout(10, 10));

        // 随机生成参数
        length = 1 + (int)(Math.random() * 20);
        width = 1 + (int)(Math.random() * 20);
        correctArea = length * width;

        // 顶部倒计时
        timerLabel = new JLabel("Time left: 180s", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(timerLabel, BorderLayout.NORTH);

        // 中部图形和参数
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(new RectangleDrawingPanel(length, width));
        centerPanel.add(Box.createVerticalStrut(10));

        // 输入区
        JPanel inputPanel = new JPanel();
        inputPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        answerField = new JTextField();
        answerField.setMaximumSize(new Dimension(120, 32));
        answerField.setPreferredSize(new Dimension(120, 32));
        answerField.setFont(new Font("Arial", Font.PLAIN, 18));
        submitButton = new JButton("Submit");
        inputPanel.add(new JLabel("Area = "));
        inputPanel.add(answerField);
        inputPanel.add(submitButton);
        centerPanel.add(inputPanel);

        // 反馈区
        feedbackLabel = new JLabel(" ", SwingConstants.CENTER);
        feedbackLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        feedbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(feedbackLabel);

        // 公式与计算过程区（初始隐藏）
        formulaLabel = new JLabel(" ", SwingConstants.CENTER);
        formulaLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        formulaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        calcLabel = new JLabel(" ", SwingConstants.CENTER);
        calcLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        calcLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(formulaLabel);
        centerPanel.add(calcLabel);

        add(centerPanel, BorderLayout.CENTER);

        // 底部按钮
        nextButton = new JButton("Next Shape");
        nextButton.setEnabled(false);
        homeButton = new JButton("Home");
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(nextButton);
        bottomPanel.add(homeButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // 事件绑定
        submitButton.addActionListener(e -> checkAnswer());
        nextButton.addActionListener(e -> goNext());
        homeButton.addActionListener(e -> goHome());

        // 启动倒计时
        timer = new Timer(1000, e -> updateTimer());
        timer.start();
    }

    private void updateTimer() {
        timeLeft--;
        timerLabel.setText("Time left: " + timeLeft + "s");
        if (timeLeft <= 0) {
            timer.stop();
            showSolution(false);
        }
    }

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
            showSolution(true);
        } else {
            if (attempts >= 3) {
                timer.stop();
                showSolution(false);
            } else {
                feedbackLabel.setText("Incorrect! Attempts left: " + (3 - attempts));
            }
        }
    }

    private void showSolution(boolean correct) {
        answerField.setEnabled(false);
        submitButton.setEnabled(false);
        nextButton.setEnabled(true);
        if (correct) {
            feedbackLabel.setText("Correct! Area = " + correctArea);
        } else {
            feedbackLabel.setText("Incorrect! The correct area is: " + correctArea);
        }
        formulaLabel.setText("公式：A = 长 × 宽");
        calcLabel.setText("带入：A = " + length + " × " + width + " = " + correctArea);
    }

    private void goNext() {
        parentFrame.setContentPane(new ShapeAreaPanel(parentFrame));
        parentFrame.revalidate();
    }

    private void goHome() {
        parentFrame.setContentPane(new MainMenuPanel(parentFrame));
        parentFrame.revalidate();
    }

    // 内部类：绘制带标注的矩形
    static class RectangleDrawingPanel extends JPanel {
        private int length, width;
        public RectangleDrawingPanel(int l, int w) {
            this.length = l;
            this.width = w;
            setPreferredSize(new Dimension(260, 140));
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int x = 40, y = 40, w = 160, h = 60;
            g2.setStroke(new BasicStroke(3));
            g2.setColor(new Color(100, 180, 255));
            g2.drawRect(x, y, w, h);
            g2.setFont(new Font("Arial", Font.BOLD, 16));
            g2.setColor(Color.BLACK);
            g2.drawString("Length: " + length, x + w/2 - 30, y + h + 25);
            g2.drawString("Width: " + width, x + w + 10, y + h/2);
        }
    }
} 