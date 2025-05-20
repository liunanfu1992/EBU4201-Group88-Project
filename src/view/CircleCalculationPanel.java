package src.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;
import java.util.Random;
import src.model.ScoreManager;
import src.model.ScoringUtil;

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
    private static Set<String> finished = new HashSet<>(); // 记录已完成的四种组合
    private int correctCoeff = 0;

    public CircleCalculationPanel(JFrame frame) {
        this.parentFrame = frame;
        setLayout(new BorderLayout(10, 10));
        StyleUtil.stylePanel(this);
        titleLabel = new JLabel("Circle Area & Circumference Calculation", SwingConstants.CENTER);
        StyleUtil.styleLabel(titleLabel, StyleUtil.BIG_FONT, StyleUtil.MAIN_PURPLE);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(StyleUtil.MAIN_PINK);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        add(titleLabel, BorderLayout.NORTH);
        showMainMenu();
    }


    private void showMainMenu() {
        removeCenter();
        removeSouthPanel();
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 120, 40, 120));
        StyleUtil.stylePanel(mainPanel);
        JLabel prompt = new JLabel("Please select what you want to calculate:", SwingConstants.CENTER);
        StyleUtil.styleLabel(prompt, StyleUtil.BIG_FONT, StyleUtil.MAIN_BLUE);
        prompt.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(prompt);
        mainPanel.add(Box.createVerticalStrut(30));

        // 加载图片icon
        ImageIcon areaIcon = new ImageIcon("src/resources/images/task4/area.png");
        ImageIcon circIcon = new ImageIcon("src/resources/images/task4/circumference.png");
        int iconMaxDim = 280;
        areaIcon = scaleIconProportionally(areaIcon, iconMaxDim);
        circIcon = scaleIconProportionally(circIcon, iconMaxDim);

        areaButton = new JButton("Area", areaIcon);
        StyleUtil.styleButton(areaButton, StyleUtil.MAIN_GREEN, Color.BLACK);
        areaButton.setFont(StyleUtil.BIG_FONT);
        areaButton.setPreferredSize(new Dimension(300, 300));
        areaButton.setHorizontalTextPosition(SwingConstants.CENTER);
        areaButton.setVerticalTextPosition(SwingConstants.BOTTOM);

        circButton = new JButton("Circumference", circIcon);
        StyleUtil.styleButton(circButton, StyleUtil.MAIN_GREEN, Color.BLACK);
        circButton.setFont(StyleUtil.BIG_FONT);
        circButton.setPreferredSize(new Dimension(300, 300));
        circButton.setHorizontalTextPosition(SwingConstants.CENTER);
        circButton.setVerticalTextPosition(SwingConstants.BOTTOM);

        // 判断是否已完成，已完成则禁用并标记
        if (finished.contains("area-radius") && finished.contains("area-diameter")) {
            areaButton.setEnabled(false);
            areaButton.setText("Area (Completed)");
            areaButton.setBackground(new Color(220, 220, 220));
            areaButton.setForeground(Color.GRAY);
        }
        if (finished.contains("circ-radius") && finished.contains("circ-diameter")) {
            circButton.setEnabled(false);
            circButton.setText("Circumference (Completed)");
            circButton.setBackground(new Color(220, 220, 220));
            circButton.setForeground(Color.GRAY);
        }

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 60, 0));
        StyleUtil.stylePanel(buttonPanel);
        buttonPanel.add(areaButton);
        buttonPanel.add(circButton);
        mainPanel.add(buttonPanel);


        // 主菜单底部只保留Home按钮
        JButton homeBtn = new JButton("Home");
        StyleUtil.styleButton(homeBtn, StyleUtil.MAIN_YELLOW, Color.BLACK);
        homeBtn.setFont(StyleUtil.NORMAL_FONT);
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 18));
        StyleUtil.stylePanel(bottomPanel);
        bottomPanel.add(homeBtn);
        add(bottomPanel, BorderLayout.SOUTH);
        homeBtn.addActionListener(e -> {
            parentFrame.setContentPane(new MainMenuPanel(parentFrame));
            parentFrame.revalidate();
        });
        add(mainPanel, BorderLayout.CENTER);
        revalidate(); repaint();
        areaButton.addActionListener(e -> startQuestion(true));
        circButton.addActionListener(e -> startQuestion(false));
    }

    private void showQuestionPanel() {
        removeCenter();
        questionPanel = new JPanel();
        questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));
        questionPanel.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));
        StyleUtil.stylePanel(questionPanel);
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
        StyleUtil.styleLabel(questionLabel, StyleUtil.BIG_FONT, StyleUtil.MAIN_BLUE);
        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        questionPanel.add(questionLabel);
        questionPanel.add(Box.createVerticalStrut(20));
        // 输入区
        JPanel inputPanel = new JPanel();
        StyleUtil.stylePanel(inputPanel);
        inputPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        answerField = new JTextField();
        answerField.setMaximumSize(new Dimension(120, 32));
        answerField.setPreferredSize(new Dimension(120, 32));
        answerField.setFont(StyleUtil.NORMAL_FONT);
        answerField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(StyleUtil.MAIN_BLUE, 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        submitButton = new JButton("Submit");
        StyleUtil.styleButton(submitButton, StyleUtil.MAIN_GREEN, Color.BLACK);
        JLabel piLabel = new JLabel("π");
        StyleUtil.styleLabel(piLabel, StyleUtil.NORMAL_FONT, StyleUtil.MAIN_PURPLE);
        inputPanel.add(new JLabel((isArea ? "Area = " : "Circumference = ")));
        inputPanel.add(answerField);
        inputPanel.add(piLabel);
        inputPanel.add(submitButton);
        questionPanel.add(inputPanel);
        // 倒计时
        timerLabel = new JLabel("Time left: 180s", SwingConstants.CENTER);
        StyleUtil.styleLabel(timerLabel, StyleUtil.NORMAL_FONT, StyleUtil.MAIN_PURPLE);
        timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        questionPanel.add(timerLabel);
        // 反馈
        feedbackLabel = new JLabel(" ", SwingConstants.CENTER);
        StyleUtil.styleLabel(feedbackLabel, StyleUtil.NORMAL_FONT, StyleUtil.MAIN_BLUE);
        feedbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        questionPanel.add(feedbackLabel);
        add(questionPanel, BorderLayout.CENTER);
        // 底部按钮
        addQuestionBottomBar();
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

    private void addQuestionBottomBar() {
        // 先移除原有底部
        removeSouthPanel();
        JButton homeBtn = new JButton("Home");
        StyleUtil.styleButton(homeBtn, StyleUtil.MAIN_YELLOW, Color.BLACK);
        homeBtn.setFont(StyleUtil.NORMAL_FONT);
        JButton backBtn = new JButton("Back to Selection");
        StyleUtil.styleButton(backBtn, StyleUtil.MAIN_YELLOW, Color.BLACK);
        backBtn.setFont(StyleUtil.NORMAL_FONT);
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 18));
        StyleUtil.stylePanel(bottomPanel);
        bottomPanel.add(homeBtn);
        bottomPanel.add(backBtn);
        add(bottomPanel, BorderLayout.SOUTH);
        homeBtn.addActionListener(e -> {
            parentFrame.setContentPane(new MainMenuPanel(parentFrame));
            parentFrame.revalidate();
        });
        backBtn.addActionListener(e -> {
            showMainMenu();
        });
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
        showQuestionPanel();
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
            int points = ScoringUtil.getScore(false, attempts);
            ScoreManager.getInstance().addScore(points);
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
        if (correct) {
            finished.add(key);
            // 只要答对一次，Area或Circumference都算完成
            if (isArea) {
                finished.add("area-radius");
                finished.add("area-diameter");
            } else {
                finished.add("circ-radius");
                finished.add("circ-diameter");
            }
        }
        // 展示讲解区
        removeCenter();
        explainPanel = new JPanel();
        explainPanel.setLayout(new BoxLayout(explainPanel, BoxLayout.Y_AXIS));
        explainPanel.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));
        StyleUtil.stylePanel(explainPanel);
        explainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        // 反馈
        feedbackLabel = new JLabel(correct ? ("Correct! " + (isArea ? "Area" : "Circumference") + " = " + correctCoeff + "π")
                : (timeout ? "Time's up! The correct answer is: " + correctCoeff + "π"
                : "Incorrect! The correct answer is: " + correctCoeff + "π"), SwingConstants.CENTER);
        StyleUtil.styleLabel(feedbackLabel, StyleUtil.NORMAL_FONT, StyleUtil.MAIN_BLUE);
        feedbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        explainPanel.add(feedbackLabel);
        explainPanel.add(Box.createVerticalStrut(10));
        // 公式
        String formula = isArea ? "A = π × r²" : (isRadius ? "C = 2 × π × r" : "C = π × d");
        formulaLabel = new JLabel("Formula: " + formula, SwingConstants.CENTER);
        StyleUtil.styleLabel(formulaLabel, StyleUtil.NORMAL_FONT, StyleUtil.MAIN_PURPLE);
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
        StyleUtil.styleLabel(calcLabel, StyleUtil.NORMAL_FONT, StyleUtil.MAIN_PURPLE);
        calcLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        explainPanel.add(calcLabel);
        explainPanel.add(Box.createVerticalStrut(20));
        add(explainPanel, BorderLayout.CENTER);
        revalidate(); repaint();
    }

    private void showComplete() {
        removeCenter();
        JPanel donePanel = new JPanel();
        donePanel.setLayout(new BoxLayout(donePanel, BoxLayout.Y_AXIS));
        StyleUtil.stylePanel(donePanel);
        JLabel doneLabel = new JLabel("Congratulations! You have completed all circle area and circumference calculations.", SwingConstants.CENTER);
        StyleUtil.styleLabel(doneLabel, StyleUtil.BIG_FONT, StyleUtil.MAIN_GREEN);
        doneLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        donePanel.add(Box.createVerticalStrut(40));
        donePanel.add(doneLabel);
        donePanel.add(Box.createVerticalStrut(30));
        JButton homeBtn = new JButton("Home");
        StyleUtil.styleButton(homeBtn, StyleUtil.MAIN_YELLOW, Color.BLACK);
        homeBtn.setFont(StyleUtil.NORMAL_FONT);
        homeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        donePanel.add(homeBtn);
        add(donePanel, BorderLayout.CENTER);
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
            StyleUtil.stylePanel(this);
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
                g2.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
                g2.setColor(Color.BLACK);
                g2.drawString("radius = " + radius, cx + rPix / 2 - 10, cy - 10);
            } else {
                g2.drawLine(cx - rPix, cy, cx + rPix, cy);
                g2.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
                g2.setColor(Color.BLACK);
                g2.drawString("diameter = " + diameter, cx - 30, cy - 10);
            }
        }
    }

    // 等比例缩放图片，最大边为maxDim
    private ImageIcon scaleIconProportionally(ImageIcon icon, int maxDim) {
        int w = icon.getIconWidth();
        int h = icon.getIconHeight();
        double scale = 1.0 * maxDim / Math.max(w, h);
        int newW = (int)(w * scale);
        int newH = (int)(h * scale);
        Image img = icon.getImage().getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    // 移除BorderLayout.SOUTH已有组件
    private void removeSouthPanel() {
        // BorderLayout.SOUTH只能有一个组件，遍历移除
        for (Component comp : getComponents()) {
            Object cons = getLayout() instanceof BorderLayout ? ((BorderLayout)getLayout()).getConstraints(comp) : null;
            if (cons != null && cons.equals(BorderLayout.SOUTH)) {
                remove(comp);
                break;
            }
        }
    }

    public static boolean isAllCompleted() {
        return finished.contains("area-radius") && finished.contains("area-diameter") && finished.contains("circ-radius") && finished.contains("circ-diameter");
    }
}