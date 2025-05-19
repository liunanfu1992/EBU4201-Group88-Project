package src.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import src.model.ScoringUtil;
import src.model.ScoreManager;

public class RectangleAreaQuestionPanel extends JPanel {
    private JFrame parentFrame;
    private int length, width, correctArea, attempts = 0;
    private JLabel timerLabel, feedbackLabel, questionLabel, scoreLabel;
    private JTextField answerField;
    private JButton submitButton, nextButton, homeButton;
    private Timer timer;
    private int timeLeft = 180; // 3分钟
    // 讲解区
    private JPanel explainPanel;
    private JLabel formulaLabel, calcLabel;
    private RectangleDrawingPanel drawingPanel;
    private int score = 0; // 总分
    private final boolean isAdvanced = false; // 普通图形为基础题型

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

        // 顶部分数
        scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(scoreLabel, BorderLayout.NORTH);

        // 中部主面板
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0)); // 底部边距设为0
        centerPanel.add(Box.createVerticalStrut(4));

        // 题干区
        questionLabel = new JLabel("The length of the rectangle is " + length + ", the width is " + width + ". Please calculate its area.", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 18));
        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(questionLabel);
        centerPanel.add(Box.createVerticalStrut(6));

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

        // 图片区（单独放在反馈消息下方）
        drawingPanel = new RectangleDrawingPanel(length, width);
        drawingPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        drawingPanel.setVisible(false);
        centerPanel.add(Box.createVerticalStrut(4));
        centerPanel.add(drawingPanel);
        centerPanel.add(Box.createVerticalStrut(2)); // 图形和explainPanel之间更紧凑

        // 讲解区（只保留公式和Substitute说明）
        explainPanel = new JPanel();
        explainPanel.setLayout(new BoxLayout(explainPanel, BoxLayout.Y_AXIS));
        explainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formulaLabel = new JLabel("Formula: A = length × width", SwingConstants.CENTER);
        formulaLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        formulaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        calcLabel = new JLabel("Substitute: A = " + length + " × " + width + " = " + correctArea, SwingConstants.CENTER);
        calcLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        calcLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        explainPanel.add(formulaLabel);
        explainPanel.add(Box.createVerticalStrut(2));
        explainPanel.add(calcLabel);
        explainPanel.setVisible(false);
        centerPanel.add(explainPanel);

        add(centerPanel, BorderLayout.CENTER);

        // 底部按钮
        nextButton = new JButton("Back to Selection");
        nextButton.setEnabled(true);
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
            showSolution(false, true);
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
            int points = ScoringUtil.getScore(isAdvanced, attempts);
            score += points;
            ScoreManager.getInstance().addScore(points);
            feedbackLabel.setText("Correct! +" + points + " points. Great job!");
            scoreLabel.setText("Score: " + score);
            ShapeAreaPanel.markShapeAsCompleted("Rectangle");
            showSolution(true, false);
        } else {
            if (attempts >= 3) {
                timer.stop();
                showSolution(false, false);
            } else {
                feedbackLabel.setText("Incorrect! Attempts left: " + (3 - attempts));
            }
        }
    }

    private void showSolution(boolean correct, boolean timeout) {
        answerField.setEnabled(false);
        submitButton.setEnabled(false);
        nextButton.setEnabled(true);
        drawingPanel.setVisible(true);
        explainPanel.setVisible(true);
        if (correct) {
            feedbackLabel.setText("Correct! Area = " + correctArea);
        } else if (timeout) {
            feedbackLabel.setText("Time's up! The correct area is: " + correctArea);
        } else {
            feedbackLabel.setText("Incorrect! The correct area is: " + correctArea);
        }
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
            setPreferredSize(new Dimension(320, 110)); // 减小高度让下方更靠近
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int panelW = getWidth();
            int panelH = getHeight();
            int maxRectW = 180, maxRectH = 80;
            // 按比例缩放
            double scale = Math.min(maxRectW / (double)length, maxRectH / (double)width);
            int rectW = (int)(length * scale);
            int rectH = (int)(width * scale);
            int x = (panelW - rectW) / 2;
            int y = (panelH - rectH) / 2;
            g2.setStroke(new BasicStroke(3));
            g2.setColor(new Color(100, 180, 255));
            g2.drawRect(x, y, rectW, rectH);
            g2.setFont(new Font("Arial", Font.BOLD, 16));
            g2.setColor(Color.BLACK);
            FontMetrics fm = g2.getFontMetrics();
            // Length 居中下方
            String lengthStr = "Length: " + length;
            int lengthWidth = fm.stringWidth(lengthStr);
            g2.drawString(lengthStr, x + (rectW - lengthWidth) / 2, y + rectH + 25);
            // Width 居中右侧
            String widthStr = "Width: " + width;
            int widthHeight = fm.getHeight();
            g2.drawString(widthStr, x + rectW + 10, y + rectH / 2 + widthHeight / 2);
        }
    }
} 