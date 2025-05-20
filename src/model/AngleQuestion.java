package src.model;

// Represents a question about identifying the type of an angle.
// This class extends the Question class and is used to create questions related to angle types.
public class AngleQuestion extends Question {
    private AngleType angleType;
    private int angleValue; 

    // Constructs an AngleQuestion with the specified angle type and value.
    public AngleQuestion(AngleType angleType, int angleValue) {
        super("Identify the type of angle: " + angleValue + "°", angleType.name().toLowerCase());
        this.angleType = angleType;
        this.angleValue = angleValue;
    }

    // Gets the type of the angle.
    public AngleType getAngleType() {
        return angleType;
    }

    // Gets the value of the angle in degrees.
    public int getAngleValue() {
        return angleValue;
    }

    @Override
    public boolean checkAnswer(String userAnswer) {
        return userAnswer != null && userAnswer.trim().equalsIgnoreCase(correctAnswer);
    }
} 