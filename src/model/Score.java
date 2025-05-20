package src.model;

// Represents a score with points and attempts.
// This class is used to track the score and number of attempts for a question.
public class Score {
    private int points;
    private int attempts;

    // Constructs a Score with initial points and attempts set to zero.
    public Score() {
        this.points = 0;
        this.attempts = 0;
    }

    // Gets the current points.
    public int getPoints() {
        return points;
    }

    // Adds points to the current score.
    public void addPoints(int points) {
        this.points += points;
    }

    // Gets the number of attempts made.
    public int getAttempts() {
        return attempts;
    }

    // Increments the number of attempts by one.
    public void incrementAttempts() {
        this.attempts++;
    }

    // Resets the number of attempts to zero.
    public void resetAttempts() {
        this.attempts = 0;
    }

    // Resets the score, setting points and attempts to zero.
    public void reset() {
        this.points = 0;
        this.attempts = 0;
    }
} 