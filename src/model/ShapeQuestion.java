package src.model;

public class ShapeQuestion extends Question {
    private ShapeType shapeType;

    public ShapeQuestion(ShapeType shapeType) {
        super("Identify the shape shown:", shapeType.name().toLowerCase());
        this.shapeType = shapeType;
    }

    public ShapeType getShapeType() {
        return shapeType;
    }

    @Override
    public boolean checkAnswer(String userAnswer) {
        return userAnswer != null && userAnswer.trim().equalsIgnoreCase(correctAnswer);
    }
} 