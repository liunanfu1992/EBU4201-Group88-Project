package src.model;

// Singleton class for managing the total score across the application.
// This class ensures that there is only one instance managing the total score.
public class ScoreManager {
    private static ScoreManager instance = new ScoreManager();
    private int totalScore = 0;

    // Private constructor to prevent instantiation.
    private ScoreManager() {}

    // Gets the singleton instance of ScoreManager.
    public static ScoreManager getInstance() {
        return instance;
    }

    // Adds points to the total score.
    public void addScore(int points) {
        totalScore += points;
    }

    // Gets the total score.
    public int getTotalScore() {
        return totalScore;
    }

    // Resets the total score to zero.
    public void resetScore() {
        totalScore = 0;
    }
} 