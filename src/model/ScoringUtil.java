package src.model;

public class ScoringUtil {
    public static int getScore(boolean isAdvanced, int attempt) {
        if (attempt == 1) return isAdvanced ? 6 : 3;
        if (attempt == 2) return isAdvanced ? 4 : 2;
        if (attempt == 3) return isAdvanced ? 2 : 1;
        return 0;
    }
} 