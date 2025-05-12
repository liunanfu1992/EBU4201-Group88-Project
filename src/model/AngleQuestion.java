package src.model;

public class AngleQuestion extends Question {
    private AngleType angleType;
    private int angleValue; // 角度值（0-360）

    public AngleQuestion(AngleType angleType, int angleValue) {
        super("Identify the type of angle: " + angleValue + "°", angleType.name().toLowerCase());
        this.angleType = angleType;
        this.angleValue = angleValue;
    }

    public AngleType getAngleType() {
        return angleType;
    }

    public int getAngleValue() {
        return angleValue;
    }

    @Override
    public boolean checkAnswer(String userAnswer) {
        return userAnswer != null && userAnswer.trim().equalsIgnoreCase(correctAnswer);
    }
} 