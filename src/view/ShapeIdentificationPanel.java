package src.view;

import src.model.ShapeType;
import src.model.ShapeQuestion;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import src.model.ScoringUtil;
import src.model.ScoreManager;

public class ShapeIdentificationPanel extends JPanel {
    private JFrame parentFrame;
    private List<ShapeType> shapesToIdentify;
    private int currentIndex = 0;
    private ShapeQuestion currentQuestion;
    private int attempts = 0;
    private int score = 0;
    private boolean is2DMode;
    private boolean isAdvanced;
    private static final int PRACTICE_COUNT = 4;
    private static boolean completed2D = false;
    private static boolean completed3D = false;

    private JLabel shapeLabel;
    private JTextField answerField;
    private JButton submitButton;
    private JButton homeButton;
    private JLabel feedbackLabel;
    private JLabel scoreLabel;
    private JLabel imageLabel;
    private JLabel[] progressLabels = new JLabel[PRACTICE_COUNT];

    public ShapeIdentificationPanel(JFrame frame, boolean is2D) {
        this.parentFrame = frame;
        this.is2DMode = is2D;
        this.isAdvanced = !is2D; // 3D识别为Advanced，2D为Basic
        setLayout(new BorderLayout(10, 10));

        // Select 2D or 3D shapes
        shapesToIdentify = new ArrayList<>();
        for (ShapeType type : ShapeType.values()) {
            if (is2D && type.ordinal() <= ShapeType.KITE.ordinal()) {
                shapesToIdentify.add(type);
            } else if (!is2D && type.ordinal() > ShapeType.KITE.ordinal()) {
                shapesToIdentify.add(type);
            }
        }
        Collections.shuffle(shapesToIdentify);
        // 只保留前4个
        if (shapesToIdentify.size() > PRACTICE_COUNT) {
            shapesToIdentify = shapesToIdentify.subList(0, PRACTICE_COUNT);
        }

        // Top score label
        scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(scoreLabel, BorderLayout.NORTH);

        // Center question panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        imageLabel = new JLabel("", SwingConstants.CENTER);
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imageLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // 上下留白更小
        centerPanel.add(imageLabel);
        shapeLabel = new JLabel("", SwingConstants.CENTER);
        shapeLabel.setFont(new Font("Arial", Font.BOLD, 32));
        shapeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(shapeLabel);
        answerField = new JTextField();
        answerField.setMaximumSize(new Dimension(400, 40));
        answerField.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(answerField);
        submitButton = new JButton("Submit");
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(Box.createVerticalStrut(4));
        centerPanel.add(submitButton);
        feedbackLabel = new JLabel(" ", SwingConstants.CENTER);
        feedbackLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        feedbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(Box.createVerticalStrut(4));
        centerPanel.add(feedbackLabel);
        // 进度条
        JPanel progressPanel = new JPanel();
        progressPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        progressPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        for (int i = 0; i < PRACTICE_COUNT; i++) {
            progressLabels[i] = new JLabel("⬜");
            progressLabels[i].setFont(new Font("Arial", Font.BOLD, 32));
            progressPanel.add(progressLabels[i]);
        }
        centerPanel.add(Box.createVerticalStrut(8));
        centerPanel.add(progressPanel);
        add(centerPanel, BorderLayout.CENTER);

        // Bottom Home button
        homeButton = new JButton("Home");
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(homeButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Event bindings
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

        loadNextShape();
    }

    private void loadNextShape() {
        if (currentIndex < shapesToIdentify.size()) {
            ShapeType currentShape = shapesToIdentify.get(currentIndex);
            currentQuestion = new ShapeQuestion(currentShape);
            
            // 优化图片名生成逻辑：下划线转空格，单词首字母大写
            String rawName = currentShape.name().toLowerCase().replace("_", " ");
            String[] words = rawName.split(" ");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < words.length; i++) {
                sb.append(Character.toUpperCase(words[i].charAt(0)))
                  .append(words[i].substring(1));
                if (i < words.length - 1) sb.append(" ");
            }
            String imageName = sb.toString();
            String filePath = "src/resources/images/task1/" + (is2DMode ? "2DShapes/" : "3DShapes/") + imageName + ".png";
            try {
                ImageIcon icon = new ImageIcon(filePath); // 直接用本地文件路径
                if (icon.getIconWidth() == -1) {
                    imageLabel.setText("Image not found: " + imageName);
                    imageLabel.setIcon(null);
                } else {
                    Image image = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(image));
                    imageLabel.setText("");
                }
            } catch (Exception e) {
                imageLabel.setText("Error loading image: " + imageName);
                imageLabel.setIcon(null);
            }
            
            shapeLabel.setText("What shape is this?");
            answerField.setText("");
            feedbackLabel.setText(" ");
            attempts = 0;
        } else {
            imageLabel.setIcon(null);
            shapeLabel.setText("Congratulations! You've completed all shapes!");
            answerField.setVisible(false);
            submitButton.setVisible(false);
            feedbackLabel.setText("Final Score: " + score + "/" + getMaxScore());
            if (is2DMode) completed2D = true;
            else completed3D = true;
        }
    }

    private void updateProgressBar(int idx, boolean correct) {
        if (idx >= 0 && idx < PRACTICE_COUNT) {
            progressLabels[idx].setText(correct ? "✅" : "✗");
            progressLabels[idx].setForeground(correct ? new Color(34,197,94) : new Color(239,68,68));
        }
    }

    private void handleSubmit() {
        String userAnswer = answerField.getText();
        attempts++;
        boolean correct = currentQuestion.checkAnswer(userAnswer);
        if (correct) {
            int points = ScoringUtil.getScore(isAdvanced, attempts);
            score += points;
            ScoreManager.getInstance().addScore(points);
            scoreLabel.setText("Score: " + score);
            feedbackLabel.setText("Correct! +" + points + " point(s). Great job!");
            updateProgressBar(currentIndex, true);
            currentIndex++;
            Timer timer = new Timer(1200, e -> loadNextShape());
            timer.setRepeats(false);
            timer.start();
        } else {
            if (attempts >= 3) {
                // 正确答案下划线替换为空格
                String correctAns = currentQuestion.getCorrectAnswer().replace("_", " ");
                feedbackLabel.setText("Incorrect! The correct answer is: " + correctAns);
                updateProgressBar(currentIndex, false);
                currentIndex++;
                Timer timer = new Timer(1800, e -> loadNextShape());
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

    private int getMaxScore() {
        return isAdvanced ? 24 : 12;
    }

    public static boolean is2DCompleted() { return completed2D; }
    public static boolean is3DCompleted() { return completed3D; }
} 