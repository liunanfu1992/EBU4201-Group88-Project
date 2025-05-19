package src.model;

public class ScoreManager {
    private static ScoreManager instance = new ScoreManager();
    private int totalScore = 0;
    private ScoreManager() {}
    public static ScoreManager getInstance() {
        return instance;
    }
    public void addScore(int points) {
        totalScore += points;
    }
    public int getTotalScore() {
        return totalScore;
    }
    public void resetScore() {
        totalScore = 0;
    }
} 