package src.main.java.com.shaperville.ui;

import javax.swing.*;
import java.awt.*;

public class AngleRecognitionPanel extends JPanel {
    private Integer currentAngle;
    private JTextField answerField;

    private void checkAnswer() {
        if (currentAngle == null) return;
        
        String userInput = answerField.getText().trim();
        if (userInput.isEmpty()) {
            showFeedback("Please enter an angle!", false);
            return;
        }
        
        try {
            int userAngle = Integer.parseInt(userInput);
            if (userAngle == currentAngle) {
                showFeedback("Correct! Well done!", true);
                updateProgress();
                // 只有在显示出答案后才更新进度条
                updateProgressBar();
            } else {
                showFeedback("Try again! The angle is " + currentAngle + "°", false);
            }
        } catch (NumberFormatException e) {
            showFeedback("Please enter a valid number!", false);
        }
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