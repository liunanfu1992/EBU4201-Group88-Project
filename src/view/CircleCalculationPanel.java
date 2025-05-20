// This panel allows users to calculate the area and circumference of circles.
// It includes question generation, image display, answer validation, and score tracking.
package src.view;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;
import java.util.Random;
import src.model.ScoreManager;
import src.model.ScoringUtil;

public class CircleCalculationPanel extends JPanel {
    // Reference to the main frame
    private JFrame parentFrame;
    // Labels for displaying various information
    private JLabel titleLabel, progressLabel, questionLabel, timerLabel, feedbackLabel, formulaLabel, calcLabel;
    // Text field for user answer input
    private JTextField answerField;
    // Buttons for different actions
    private JButton submitButton, homeButton, areaButton, circButton, nextButton;
    // Panels for organizing the UI components
    private JPanel mainPanel, questionPanel, explainPanel, buttonPanel;
    // Timer for tracking question time limit
    private Timer timer;
    // Time remaining in seconds
    private int timeLeft = 180;
    // Flag indicating whether calculating area (true) or circumference (false)
    private boolean isArea = true;
    // Flag indicating whether using radius (true) or diameter (false)
    private boolean isRadius = true;
    // Circle parameters
    private int radius = 0, diameter = 0;
    // Correct answer for the current question
    private double correctAnswer = 0;
    // Number of attempts for current question
    private int attempts = 0;
    // Set to track completed question types
    private static Set<String> finished = new HashSet<>(); 
    // Coefficient for calculating the correct answer
    private int correctCoeff = 0;

    // Constructor, initializes the panel and its components
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

    // Shows the main menu with options to calculate area or circumference
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

        ImageIcon areaIcon = new ImageIcon("src/resources/images/task4/area.png");
        ImageIcon circIcon = new ImageIcon("src/resources/images/task4/circumference.png");
        int iconMaxDim = 280;
        areaIcon = scaleIconProportionally(areaIcon, iconMaxDim);
        circIcon = scaleIconProportionally(circIcon, iconMaxDim);

        areaButton = new JButton("Area", areaIcon);
        StyleUtil.styleButton(areaButton, StyleUtil.MAIN_GREEN, Color.BLACK);
        areaButton.setFont(StyleUtil.BIG_FONT);
        areaButton.setPreferredSize(new Dimension(290, 320));
        areaButton.setHorizontalTextPosition(SwingConstants.CENTER);
        areaButton.setVerticalTextPosition(SwingConstants.BOTTOM);

        circButton = new JButton("Circumference", circIcon);
        StyleUtil.styleButton(circButton, StyleUtil.MAIN_GREEN, Color.BLACK);
        circButton.setFont(StyleUtil.BIG_FONT);
        circButton.setPreferredSize(new Dimension(290, 320));
        circButton.setHorizontalTextPosition(SwingConstants.CENTER);
        circButton.setVerticalTextPosition(SwingConstants.BOTTOM);

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

    // Shows the question panel with the current question
    private void showQuestionPanel() {
        removeCenter();
        questionPanel = new JPanel();
        questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));
        questionPanel.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));
        StyleUtil.stylePanel(questionPanel);
        Random rand = new Random();
        if (isRadius) {
            radius = rand.nextInt(20) + 1;
            diameter = radius * 2;
        } else {
            diameter = (rand.nextInt(10) + 1) * 2; // 2, 4, ..., 20
            radius = diameter / 2;
        }
        String paramStr = isRadius ? ("radius = " + radius) : ("diameter = " + diameter);
        String what = isArea ? "area" : "circumference";
        questionLabel = new JLabel("Given " + paramStr + ", please calculate the " + what + " of the circle.", SwingConstants.CENTER);
        StyleUtil.styleLabel(questionLabel, StyleUtil.BIG_FONT, StyleUtil.MAIN_BLUE);
        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        questionPanel.add(questionLabel);
        questionPanel.add(Box.createVerticalStrut(20));
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
        timerLabel = new JLabel("Time left: 180s", SwingConstants.CENTER);
        StyleUtil.styleLabel(timerLabel, StyleUtil.NORMAL_FONT, StyleUtil.MAIN_PURPLE);
        timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        questionPanel.add(timerLabel);
        feedbackLabel = new JLabel(" ", SwingConstants.CENTER);
        StyleUtil.styleLabel(feedbackLabel, StyleUtil.NORMAL_FONT, StyleUtil.MAIN_BLUE);
        feedbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        questionPanel.add(feedbackLabel);
        add(questionPanel, BorderLayout.CENTER);
        addQuestionBottomBar();
        revalidate(); repaint();
        timeLeft = 180;
        attempts = 0;
        if (isArea) {
            correctCoeff = isRadius ? (radius * radius) : ((diameter / 2) * (diameter / 2));
        } else {
            correctCoeff = isRadius ? (2 * radius) : diameter;
        }
        timer = new Timer(1000, e -> updateTimer());
        timer.start();
        submitButton.addActionListener(e -> checkAnswer());
    }

    // Adds the bottom bar with navigation buttons
    private void addQuestionBottomBar() {
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

    // Starts a new question based on the selected type (area or circumference)
    private void startQuestion(boolean area) {
        isArea = area;
        String keyRadius = (isArea ? "area-radius" : "circ-radius");
        String keyDiameter = (isArea ? "area-diameter" : "circ-diameter");
        if (!finished.contains(keyRadius) && !finished.contains(keyDiameter)) {
            isRadius = new Random().nextBoolean();
        } else if (!finished.contains(keyRadius)) {
            isRadius = true;
        } else if (!finished.contains(keyDiameter)) {
            isRadius = false;
        } else {
            showMainMenu();
            return;
        }
        showQuestionPanel();
    }

    // Updates the timer display and handles timeout
    private void updateTimer() {
        timeLeft--;
        timerLabel.setText("Time left: " + timeLeft + "s");
        if (timeLeft <= 0) {
            timer.stop();
            showSolution(false, true);
        }
    }

    // Checks the user's answer and provides feedback
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

    // Shows the solution with explanation
    private void showSolution(boolean correct, boolean timeout) {
        answerField.setEnabled(false);
        submitButton.setEnabled(false);
        String key = (isArea ? "area-" : "circ-") + (isRadius ? "radius" : "diameter");
        if (correct) {
            finished.add(key);
            if (isArea) {
                finished.add("area-radius");
                finished.add("area-diameter");
            } else {
                finished.add("circ-radius");
                finished.add("circ-diameter");
            }
        }
        removeCenter();
        explainPanel = new JPanel();
        explainPanel.setLayout(new BoxLayout(explainPanel, BoxLayout.Y_AXIS));
        explainPanel.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));
        StyleUtil.stylePanel(explainPanel);
        explainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        feedbackLabel = new JLabel(correct ? ("Correct! " + (isArea ? "Area" : "Circumference") + " = " + correctCoeff + "π")
                : (timeout ? "Time's up! The correct answer is: " + correctCoeff + "π"
                : "Incorrect! The correct answer is: " + correctCoeff + "π"), SwingConstants.CENTER);
        StyleUtil.styleLabel(feedbackLabel, StyleUtil.NORMAL_FONT, StyleUtil.MAIN_BLUE);
        feedbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        explainPanel.add(feedbackLabel);
        explainPanel.add(Box.createVerticalStrut(10));
        String formula = isArea ? "A = π × r²" : (isRadius ? "C = 2 × π × r" : "C = π × d");
        formulaLabel = new JLabel("Formula: " + formula, SwingConstants.CENTER);
        StyleUtil.styleLabel(formulaLabel, StyleUtil.NORMAL_FONT, StyleUtil.MAIN_PURPLE);
        formulaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        explainPanel.add(formulaLabel);
        explainPanel.add(Box.createVerticalStrut(10));
        CircleDrawingPanel drawingPanel = new CircleDrawingPanel(radius, diameter, isRadius);
        drawingPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        explainPanel.add(drawingPanel);
        explainPanel.add(Box.createVerticalStrut(10));
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

    // Shows the completion message
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

    // Removes the center panel
    private void removeCenter() {
        if (mainPanel != null) remove(mainPanel);
        if (questionPanel != null) remove(questionPanel);
        if (explainPanel != null) remove(explainPanel);
        revalidate(); repaint();
    }

    // Inner class for drawing circles with measurements
    static class CircleDrawingPanel extends JPanel {
        // Circle parameters
        private int radius, diameter;
        // Flag indicating whether to show radius (true) or diameter (false)
        private boolean showRadius;

        // Constructor for the drawing panel
        public CircleDrawingPanel(int r, int d, boolean showRadius) {
            this.radius = r;
            this.diameter = d;
            this.showRadius = showRadius;
            setPreferredSize(new Dimension(200, 200));
            StyleUtil.stylePanel(this);
        }

        // Paints the circle with measurements
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

    // Scales an icon proportionally to fit within maximum dimensions
    private ImageIcon scaleIconProportionally(ImageIcon icon, int maxDim) {
        int w = icon.getIconWidth();
        int h = icon.getIconHeight();
        double scale = 1.0 * maxDim / Math.max(w, h);
        int newW = (int)(w * scale);
        int newH = (int)(h * scale);
        Image img = icon.getImage().getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    // Removes the south panel
    private void removeSouthPanel() {
        for (Component comp : getComponents()) {
            Object cons = getLayout() instanceof BorderLayout ? ((BorderLayout)getLayout()).getConstraints(comp) : null;
            if (cons != null && cons.equals(BorderLayout.SOUTH)) {
                remove(comp);
                break;
            }
        }
    }

    // Checks if all question types are completed
    public static boolean isAllCompleted() {
        return finished.contains("area-radius") && finished.contains("area-diameter") && finished.contains("circ-radius") && finished.contains("circ-diameter");
    }
}