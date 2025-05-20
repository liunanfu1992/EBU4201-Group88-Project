package src.model;

// Represents a question about identifying a shape.
// This class extends the Question class and is used to create questions related to shape types.
public class ShapeQuestion extends Question {
    private ShapeType shapeType;

    // Constructs a ShapeQuestion with the specified shape type.
    public ShapeQuestion(ShapeType shapeType) {
        super("Identify the shape shown:", shapeType.name().toLowerCase());
        this.shapeType = shapeType;
    }

    // Gets the type of the shape.
    public ShapeType getShapeType() {
        return shapeType;
    }

    @Override
    public boolean checkAnswer(String userAnswer) {
        return userAnswer != null && userAnswer.trim().equalsIgnoreCase(correctAnswer);
    }
} 