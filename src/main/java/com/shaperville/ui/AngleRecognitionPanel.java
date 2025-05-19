package src.main.java.com.shaperville.ui;

import javax.swing.*;
import java.awt.*;

public class AngleRecognitionPanel extends JPanel {
    private Integer currentAngle;
    private JTextField answerField;
    private int attemptsLeft = 3; // 每题3次机会
    private boolean answered = false;

    private void checkAnswer() {
        if (currentAngle == null || answered) return;
        
        String userInput = answerField.getText().trim();
        if (userInput.isEmpty()) {
            showFeedback("Please enter an angle!", false);
            return;
        }
        
        try {
            int userAngle = Integer.parseInt(userInput);
            if (userAngle == currentAngle) {
                showFeedback("Correct! Well done!", true);
                revealAndMarkAnswered();
            } else {
                attemptsLeft--;
                if (attemptsLeft <= 0) {
                    showFeedback("No attempts left! The correct answer is: " + currentAngle, false);
                    revealAndMarkAnswered();
                } else {
                    showFeedback("Try again! Attempts left: " + attemptsLeft, false);
                }
            }
        } catch (NumberFormatException e) {
            showFeedback("Please enter a valid number!", false);
        }
    }

    private void revealAndMarkAnswered() {
        answered = true;
        updateProgress();
        updateProgressBar();
        answerField.setEnabled(false); // 禁用输入框
        // 如有提交按钮，也应禁用
    }

    private void showFeedback(String message, boolean isCorrect) {
        // 显示反馈信息
    }

    private void updateProgress() {
        // 更新进度
    }

    private void updateProgressBar() {
        // 更新进度条
    }
} 