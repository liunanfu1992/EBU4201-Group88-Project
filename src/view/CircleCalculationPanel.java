package src.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;
import java.util.Random;

public class CircleCalculationPanel extends JPanel {
    private JFrame parentFrame;
    private JLabel titleLabel, progressLabel, questionLabel, timerLabel, feedbackLabel, formulaLabel, calcLabel;
    private JTextField answerField;
    private JButton submitButton, homeButton, areaButton, circButton, nextButton;
    private JPanel mainPanel, questionPanel, explainPanel, buttonPanel;
    private Timer timer;
    private int timeLeft = 180;
    private boolean isArea = true;
    private boolean isRadius = true;
    private int radius = 0, diameter = 0;
    private double correctAnswer = 0;
    private int attempts = 0;
    private Set<String> finished = new HashSet<>(); // 记录已完成的四种组合
    private int correctCoeff = 0;

    public CircleCalculationPanel(JFrame frame) {
        this.parentFrame = frame;
        setLayout(new BorderLayout(10, 10));
        titleLabel = new JLabel("Circle Area & Circumference Calculation", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);
        progressLabel = new JLabel(getProgressText(), SwingConstants.CENTER);
        progressLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        add(progressLabel, BorderLayout.SOUTH);
        showMainMenu();
    }

    private String getProgressText() {
        return String.format("Progress: %s | %s | %s | %s",
                finished.contains("area-radius") ? "✓ Area-Radius" : "Area-Radius",
                finished.contains("area-diameter") ? "✓ Area-Diameter" : "Area-Diameter",
                finished.contains("circ-radius") ? "✓ Circumference-Radius" : "Circumference-Radius",
                finished.contains("circ-diameter") ? "✓ Circumference-Diameter" : "Circumference-Diameter");
    }

    private void showMainMenu() {
        removeCenter();
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 120, 40, 120));
        JLabel prompt = new JLabel("Please select what you want to calculate:", SwingConstants.CENTER);
        prompt.setFont(new Font("Arial", Font.BOLD, 18));
        prompt.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(prompt);
        mainPanel.add(Box.createVerticalStrut(30));
        areaButton = new JButton("Area");
        areaButton.setFont(new Font("Arial", Font.PLAIN, 20));
        areaButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        circButton = new JButton("Circumference");
        circButton.setFont(new Font("Arial", Font.PLAIN, 20));
        circButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(areaButton);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(circButton);
        add(mainPanel, BorderLayout.CENTER);
        revalidate(); repaint();
        areaButton.addActionListener(e -> startQuestion(true));
        circButton.addActionListener(e -> startQuestion(false));
    }

    private void startQuestion(boolean area) {
        isArea = area;
        // 随机分配未完成的参数类型
        String keyRadius = (isArea ? "area-radius" : "circ-radius");
        String keyDiameter = (isArea ? "area-diameter" : "circ-diameter");
        if (!finished.contains(keyRadius) && !finished.contains(keyDiameter)) {
            isRadius = new Random().nextBoolean();
        } else if (!finished.contains(keyRadius)) {
            isRadius = true;
        } else if (!finished.contains(keyDiameter)) {
            isRadius = false;
        } else {
            // 该类型已全部完成
            showMainMenu();
            return;
        }
        removeCenter();
        questionPanel = new JPanel();
        questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));
        questionPanel.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));
        // 随机生成参数
        Random rand = new Random();
        if (isRadius) {
            radius = rand.nextInt(20) + 1;
            diameter = radius * 2;
        } else {
            diameter = (rand.nextInt(10) + 1) * 2; // 2, 4, ..., 20
            radius = diameter / 2;
        }
        // 题干
        String paramStr = isRadius ? ("radius = " + radius) : ("diameter = " + diameter);
        String what = isArea ? "area" : "circumference";
        questionLabel = new JLabel("Given " + paramStr + ", please calculate the " + what + " of the circle.", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 18));
        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        questionPanel.add(questionLabel);
        questionPanel.add(Box.createVerticalStrut(20));
        // 输入区
        JPanel inputPanel = new JPanel();
        inputPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        answerField = new JTextField();
        answerField.setMaximumSize(new Dimension(120, 32));
        answerField.setPreferredSize(new Dimension(120, 32));
        answerField.setFont(new Font("Arial", Font.PLAIN, 18));
        submitButton = new JButton("Submit");
        JLabel piLabel = new JLabel("π");
        piLabel.setFont(new Font("Arial", Font.BOLD, 18));
        inputPanel.add(new JLabel((isArea ? "Area = " : "Circumference = ")));
        inputPanel.add(answerField);
        inputPanel.add(piLabel);
        inputPanel.add(submitButton);
        questionPanel.add(inputPanel);
        // 倒计时
        timerLabel = new JLabel("Time left: 180s", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        questionPanel.add(timerLabel);
        // 反馈
        feedbackLabel = new JLabel(" ", SwingConstants.CENTER);
        feedbackLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        feedbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        questionPanel.add(feedbackLabel);
        add(questionPanel, BorderLayout.CENTER);
        revalidate(); repaint();
        // 计时
        timeLeft = 180;
        attempts = 0;
        // 计算正确答案的系数
        if (isArea) {
            correctCoeff = isRadius ? (radius * radius) : ((diameter / 2) * (diameter / 2));
        } else {
            correctCoeff = isRadius ? (2 * radius) : diameter;
        }
        timer = new Timer(1000, e -> updateTimer());
        timer.start();
        submitButton.addActionListener(e -> checkAnswer());
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
        if (ans == correctCoeff) {
            timer.stop();
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
        // 只有答对才算完成
        String key = (isArea ? "area-" : "circ-") + (isRadius ? "radius" : "diameter");
        if (correct) finished.add(key);
        // 展示讲解区
        removeCenter();
        explainPanel = new JPanel();
        explainPanel.setLayout(new BoxLayout(explainPanel, BoxLayout.Y_AXIS));
        explainPanel.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));
        explainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        // 反馈
        feedbackLabel = new JLabel(correct ? ("Correct! " + (isArea ? "Area" : "Circumference") + " = " + correctCoeff + "π")
                : (timeout ? "Time's up! The correct answer is: " + correctCoeff + "π"
                : "Incorrect! The correct answer is: " + correctCoeff + "π"), SwingConstants.CENTER);
        feedbackLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        feedbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        explainPanel.add(feedbackLabel);
        explainPanel.add(Box.createVerticalStrut(10));
        // 公式
        String formula = isArea ? "A = π × r²" : (isRadius ? "C = 2 × π × r" : "C = π × d");
        formulaLabel = new JLabel("Formula: " + formula, SwingConstants.CENTER);
        formulaLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        formulaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        explainPanel.add(formulaLabel);
        explainPanel.add(Box.createVerticalStrut(10));
        // 图形
        CircleDrawingPanel drawingPanel = new CircleDrawingPanel(radius, diameter, isRadius);
        drawingPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        explainPanel.add(drawingPanel);
        explainPanel.add(Box.createVerticalStrut(10));
        // 带入
        String calc;
        if (isArea) {
            calc = isRadius ?
                    ("Substitute: A = π × " + radius + "² = " + correctCoeff + "π") :
                    ("Substitute: A = π × (" + (diameter/2) + ")² = " + correctCoeff + "π");
        } else {
            calc = isRadius ?
                    ("Substitute: C = 2 × π × " + radius + " = " + correctCoeff + "π") :
                    ("Substitute: C = π × " + diameter + " = " + correctCoeff + "π");
        }
        calcLabel = new JLabel(calc, SwingConstants.CENTER);
        calcLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        calcLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        explainPanel.add(calcLabel);
        explainPanel.add(Box.createVerticalStrut(20));
        // 下一题或完成
        nextButton = new JButton(finished.size() < 4 ? "Next" : "Finish");
        nextButton.setFont(new Font("Arial", Font.PLAIN, 16));
        nextButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        explainPanel.add(nextButton);
        homeButton = new JButton("Home");
        homeButton.setFont(new Font("Arial", Font.PLAIN, 16));
        homeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        explainPanel.add(Box.createVerticalStrut(10));
        explainPanel.add(homeButton);
        add(explainPanel, BorderLayout.CENTER);
        progressLabel.setText(getProgressText());
        revalidate(); repaint();
        nextButton.addActionListener(e -> {
            if (finished.size() < 4) showMainMenu();
            else showComplete();
        });
        homeButton.addActionListener(e -> {
            parentFrame.setContentPane(new MainMenuPanel(parentFrame));
            parentFrame.revalidate();
        });
    }

    private void showComplete() {
        removeCenter();
        JPanel donePanel = new JPanel();
        donePanel.setLayout(new BoxLayout(donePanel, BoxLayout.Y_AXIS));
        JLabel doneLabel = new JLabel("Congratulations! You have completed all circle area and circumference calculations.", SwingConstants.CENTER);
        doneLabel.setFont(new Font("Arial", Font.BOLD, 18));
        doneLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        donePanel.add(Box.createVerticalStrut(40));
        donePanel.add(doneLabel);
        donePanel.add(Box.createVerticalStrut(30));
        JButton homeBtn = new JButton("Home");
        homeBtn.setFont(new Font("Arial", Font.PLAIN, 16));
        homeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        donePanel.add(homeBtn);
        add(donePanel, BorderLayout.CENTER);
        progressLabel.setText(getProgressText());
        revalidate(); repaint();
        homeBtn.addActionListener(e -> {
            parentFrame.setContentPane(new MainMenuPanel(parentFrame));
            parentFrame.revalidate();
        });
    }

    private void removeCenter() {
        if (mainPanel != null) remove(mainPanel);
        if (questionPanel != null) remove(questionPanel);
        if (explainPanel != null) remove(explainPanel);
        revalidate(); repaint();
    }

    // 圆形绘图面板
    static class CircleDrawingPanel extends JPanel {
        private int radius, diameter;
        private boolean showRadius;
        public CircleDrawingPanel(int r, int d, boolean showRadius) {
            this.radius = r;
            this.diameter = d;
            this.showRadius = showRadius;
            setPreferredSize(new Dimension(200, 200));
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int cx = getWidth() / 2;
            int cy = getHeight() / 2;
            int rPix = 70;
            g2.setStroke(new BasicStroke(3));
            g2.setColor(new Color(120, 180, 255));
            g2.drawOval(cx - rPix, cy - rPix, 2 * rPix, 2 * rPix);
            g2.setColor(Color.ORANGE);
            g2.setStroke(new BasicStroke(2));
            if (showRadius) {
                g2.drawLine(cx, cy, cx + rPix, cy);
                g2.setFont(new Font("Arial", Font.BOLD, 15));
                g2.setColor(Color.BLACK);
                g2.drawString("radius = " + radius, cx + rPix / 2 - 10, cy - 10);
            } else {
                g2.drawLine(cx - rPix, cy, cx + rPix, cy);
                g2.setFont(new Font("Arial", Font.BOLD, 15));
                g2.setColor(Color.BLACK);
                g2.drawString("diameter = " + diameter, cx - 30, cy - 10);
            }
        }
    }
} 