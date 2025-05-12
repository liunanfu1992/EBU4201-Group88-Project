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

    public AngleIdentificationPanel(JFrame frame) {
        this.parentFrame = frame;
        setLayout(new BorderLayout(10, 10));

        // 生成所有角度类型的题目，角度值随机
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
        angleDrawingPanel = new AngleDrawingPanel();
        angleDrawingPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        angleDrawingPanel.setPreferredSize(new Dimension(260, 200));
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(angleDrawingPanel);
        centerPanel.add(Box.createVerticalStrut(10));
        angleLabel = new JLabel("", SwingConstants.CENTER);
        angleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        angleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(angleLabel);
        JLabel tipLabel = new JLabel("Look at the angle and type its name below.", SwingConstants.CENTER);
        tipLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        tipLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(tipLabel);

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

    private int getRandomAngleForType(AngleType type, Random rand) {
        switch (type) {
            case ACUTE:
                return rand.nextInt(89) + 1; // 1-89
            case RIGHT:
                return 90;
            case OBTUSE:
                return rand.nextInt(89) + 91; // 91-179
            case STRAIGHT:
                return 180;
            case REFLEX:
                return rand.nextInt(179) + 181; // 181-359
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

    private void handleSubmit() {
        String userAnswer = answerField.getText();
        attempts++;
        AngleQuestion question = questions.get(currentIndex);
        if (question.checkAnswer(userAnswer)) {
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
                feedbackLabel.setText("Incorrect! The correct answer is: " + question.getCorrectAnswer());
                currentIndex++;
                Timer timer = new Timer(1800, e -> loadNextQuestion());
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
        int cy = getHeight() - 40;
        int r = 70;
        // 主射线
        g2.setStroke(new BasicStroke(4));
        g2.setColor(new Color(60, 60, 60));
        g2.drawLine(cx, cy, cx + (int)(r * Math.cos(Math.toRadians(0))), cy - (int)(r * Math.sin(Math.toRadians(0))));
        g2.drawLine(cx, cy, cx + (int)(r * Math.cos(Math.toRadians(angle))), cy - (int)(r * Math.sin(Math.toRadians(angle))));
        // 端点小圆点
        g2.setColor(new Color(60, 60, 60));
        g2.fillOval(cx - 5, cy - 5, 10, 10);
        g2.fillOval(cx + (int)(r * Math.cos(Math.toRadians(0))) - 5, cy - (int)(r * Math.sin(Math.toRadians(0))) - 5, 10, 10);
        g2.fillOval(cx + (int)(r * Math.cos(Math.toRadians(angle))) - 5, cy - (int)(r * Math.sin(Math.toRadians(angle))) - 5, 10, 10);
        // 角弧
        g2.setColor(new Color(255, 99, 71));
        g2.setStroke(new BasicStroke(3));
        g2.drawArc(cx - r/2, cy - r/2, r, r, 0, -angle);
        // 角度数值
        int tx = cx + (int)((r/2 + 18) * Math.cos(Math.toRadians(angle/2)));
        int ty = cy - (int)((r/2 + 18) * Math.sin(Math.toRadians(angle/2)));
        g2.setColor(new Color(30, 30, 30));
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.drawString(angle + "°", tx - 15, ty + 8);
    }
} 