package src.model;

public class Score {
    private int points;
    private int attempts;

    public Score() {
        this.points = 0;
        this.attempts = 0;
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public int getAttempts() {
        return attempts;
    }

    public void incrementAttempts() {
        this.attempts++;
    }

    public void resetAttempts() {
        this.attempts = 0;
    }

    public void reset() {
        this.points = 0;
        this.attempts = 0;
    }
} 