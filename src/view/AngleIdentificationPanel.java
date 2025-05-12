package src.view;

import src.model.AngleType;
import src.model.AngleQuestion;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class AngleIdentificationPanel extends JPanel {
    private JFrame parentFrame;
    private List<AngleQuestion> questions;
    private int currentIndex = 0;
    private int attempts = 0;
    private int score = 0;

    private JLabel angleLabel;
    private JTextField answerField;
    private JButton submitButton;
    private JButton homeButton;
    private JLabel feedbackLabel;
    private JLabel scoreLabel;
    private AngleDrawingPanel angleDrawingPanel;
    private JTextField angleInputField;
    private JButton angleInputButton;
    private int currentAngleValue = -1;
    private AngleType currentAngleType;
    private int typeAttempts = 0;
    private boolean showAnglePrompt = false;
    private JLabel identifyLabel;
    private JLabel tipLabel;

    public AngleIdentificationPanel(JFrame frame) {
        this.parentFrame = frame;
        setLayout(new BorderLayout(10, 10));

        // 生成所有角度类型的题目，角度值为10的倍数
        questions = new ArrayList<>();
        Random rand = new Random();
        for (AngleType type : AngleType.values()) {
            int angleValue = getRandomAngleForType(type, rand);
            questions.add(new AngleQuestion(type, angleValue));
        }
        Collections.shuffle(questions);

        // 顶部分数
        scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(scoreLabel, BorderLayout.NORTH);

        // 中间题目
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        angleInputField = new JTextField();
        angleInputField.setMaximumSize(new Dimension(120, 32));
        angleInputField.setFont(new Font("Arial", Font.PLAIN, 18));
        angleInputField.setAlignmentX(Component.CENTER_ALIGNMENT);
        angleInputButton = new JButton("Draw Angle");
        angleInputButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel angleInputPanel = new JPanel();
        angleInputPanel.setLayout(new BoxLayout(angleInputPanel, BoxLayout.X_AXIS));
        angleInputPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        angleInputPanel.add(new JLabel("Enter an angle (0-360, multiple of 10): "));
        angleInputPanel.add(angleInputField);
        angleInputPanel.add(angleInputButton);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(angleInputPanel);
        centerPanel.add(Box.createVerticalStrut(10));
        angleDrawingPanel = new AngleDrawingPanel();
        angleDrawingPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        angleDrawingPanel.setPreferredSize(new Dimension(260, 200));
        angleDrawingPanel.setAngle(-1); // 初始不显示角度图
        centerPanel.add(angleDrawingPanel);
        centerPanel.add(Box.createVerticalStrut(20)); // 增加图片和文字间距
        
        // 初始化并添加angleLabel，防止NullPointerException
        angleLabel = new JLabel("", SwingConstants.CENTER);
        angleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        angleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(angleLabel);
        identifyLabel = new JLabel("", SwingConstants.CENTER);
        identifyLabel.setFont(new Font("Arial", Font.BOLD, 24));
        identifyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(identifyLabel);
        tipLabel = new JLabel("", SwingConstants.CENTER);
        tipLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        tipLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(tipLabel);
        centerPanel.add(Box.createVerticalStrut(30));

        answerField = new JTextField();
        answerField.setMaximumSize(new Dimension(400, 40));
        answerField.setPreferredSize(new Dimension(400, 40));
        answerField.setFont(new Font("Arial", Font.PLAIN, 18));
        answerField.setAlignmentX(Component.CENTER_ALIGNMENT);
        answerField.setHorizontalAlignment(JTextField.CENTER);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(answerField);

        submitButton = new JButton("Submit");
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(submitButton);

        feedbackLabel = new JLabel(" ", SwingConstants.CENTER);
        feedbackLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        feedbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        feedbackLabel.setHorizontalAlignment(SwingConstants.CENTER);
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

        // 角度输入按钮事件
        angleInputButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAngleInput();
            }
        });

        angleDrawingPanel.setAngle(-1); // 每次重置时不显示角度图
        updateAnglePrompt();
    }

    private int getRandomAngleForType(AngleType type, Random rand) {
        switch (type) {
            case ACUTE:
                return (rand.nextInt(8) + 1) * 10; // 10-80
            case RIGHT:
                return 90;
            case OBTUSE:
                return (rand.nextInt(9) + 10) * 10; // 100-170
            case STRAIGHT:
                return 180;
            case REFLEX:
                return (rand.nextInt(18) + 19) * 10; // 190-350
            default:
                return 0;
        }
    }

    private void loadNextQuestion() {
        if (currentIndex >= questions.size()) {
            feedbackLabel.setText("All angles identified! Your final score: " + score);
            submitButton.setEnabled(false);
            answerField.setEnabled(false);
            angleDrawingPanel.setAngle(-1); // 清空
            angleLabel.setText("");
            return;
        }
        AngleQuestion question = questions.get(currentIndex);
        angleDrawingPanel.setAngle(question.getAngleValue());
        angleLabel.setText(question.getQuestionText());
        answerField.setText("");
        feedbackLabel.setText(" ");
        attempts = 0;
    }

    private void handleAngleInput() {
        String input = angleInputField.getText().trim();
        int angleValue = -1;
        showAnglePrompt = false;
        try {
            angleValue = Integer.parseInt(input);
        } catch (Exception e) {
            feedbackLabel.setText("Please enter a valid integer angle.");
            currentAngleValue = -1;
            angleDrawingPanel.setAngle(-1);
            angleLabel.setText("");
            updateAnglePrompt();
            return;
        }
        if (angleValue < 0 || angleValue > 360 || angleValue % 10 != 0) {
            feedbackLabel.setText("Angle must be 0-360 and a multiple of 10.");
            currentAngleValue = -1;
            angleDrawingPanel.setAngle(-1);
            angleLabel.setText("");
            updateAnglePrompt();
            return;
        }
        currentAngleValue = angleValue;
        currentAngleType = getAngleType(angleValue);
        angleDrawingPanel.setAngle(angleValue);
        angleLabel.setText("");
        showAnglePrompt = true;
        updateAnglePrompt();
        answerField.setText("");
        feedbackLabel.setText(" ");
        typeAttempts = 0;
    }

    private void updateAnglePrompt() {
        if (showAnglePrompt && currentAngleValue >= 0) {
            identifyLabel.setText("Identify the type of angle: " + currentAngleValue + "°");
            tipLabel.setText("Look at the angle and type its name below.");
        } else {
            identifyLabel.setText("");
            tipLabel.setText("");
        }
    }

    private AngleType getAngleType(int angle) {
        if (angle == 0 || angle == 360) return null;
        if (angle < 90) return AngleType.ACUTE;
        if (angle == 90) return AngleType.RIGHT;
        if (angle < 180) return AngleType.OBTUSE;
        if (angle == 180) return AngleType.STRAIGHT;
        return AngleType.REFLEX;
    }

    private void handleSubmit() {
        if (currentAngleValue < 0) {
            feedbackLabel.setText("Please enter and draw an angle first.");
            angleDrawingPanel.setAngle(-1);
            angleLabel.setText("");
            showAnglePrompt = false;
            updateAnglePrompt();
            return;
        }
        String userAnswer = answerField.getText().trim().toLowerCase();
        typeAttempts++;
        String correctType = currentAngleType == null ? "none" : currentAngleType.name().toLowerCase();
        if (userAnswer.equals(correctType)) {
            feedbackLabel.setText("Correct! This is a " + correctType + " angle.");
            currentAngleValue = -1;
            angleDrawingPanel.setAngle(-1);
            angleLabel.setText("");
            showAnglePrompt = false;
            updateAnglePrompt();
        } else {
            if (typeAttempts >= 3) {
                feedbackLabel.setText("Incorrect! The correct answer is: " + correctType + ".");
                currentAngleValue = -1;
                angleDrawingPanel.setAngle(-1);
                angleLabel.setText("");
                showAnglePrompt = false;
                updateAnglePrompt();
            } else {
                feedbackLabel.setText("Try again! Attempts left: " + (3 - typeAttempts));
            }
        }
    }

    private void goHome() {
        parentFrame.setContentPane(new MainMenuPanel(parentFrame));
        parentFrame.revalidate();
    }
}

class AngleDrawingPanel extends JPanel {
    private int angle = -1;
    public void setAngle(int angle) {
        this.angle = angle;
        repaint();
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (angle < 0) return;
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int cx = getWidth() / 2;
        int cy = getHeight() - 70;
        int r = 70;
        // 主射线（0°，水平向右）
        g2.setStroke(new BasicStroke(4));
        g2.setColor(new Color(60, 60, 60));
        g2.drawLine(cx, cy, cx + r, cy);
        // 目标射线（逆时针angle度）
        double rad = Math.toRadians(angle);
        int x2 = cx + (int)(r * Math.cos(rad));
        int y2 = cy - (int)(r * Math.sin(rad));
        g2.drawLine(cx, cy, x2, y2);
        // 端点小圆点
        g2.setColor(new Color(60, 60, 60));
        g2.fillOval(cx - 5, cy - 5, 10, 10);
        g2.fillOval(cx + r - 5, cy - 5, 10, 10);
        g2.fillOval(x2 - 5, y2 - 5, 10, 10);
        // 角弧（始终画angle度）
        g2.setColor(angle > 180 ? new Color(255, 140, 0) : new Color(255, 99, 71));
        g2.setStroke(new BasicStroke(3));
        int arcStart = 0;
        int arcExtent = angle;
        g2.drawArc(cx - r/2, cy - r/2, r, r, arcStart, arcExtent);
        // 角度数值
        double midAngle = angle / 2.0;
        int tx = cx + (int)((r/2 + 22) * Math.cos(Math.toRadians(midAngle)));
        int ty = cy - (int)((r/2 + 22) * Math.sin(Math.toRadians(midAngle)));
        g2.setColor(new Color(30, 30, 30));
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.drawString(angle + "°", tx - 15, ty + 8);
    }
} 