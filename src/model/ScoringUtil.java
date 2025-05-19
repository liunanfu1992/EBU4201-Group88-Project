package src.model;

public class ScoringUtil {
    /**
     * 获取本题得分
     * @param isAdvanced 是否为高级题型
     * @param attempt 第几次答对（1/2/3）
     * @return 得分
     */
    public static int getScore(boolean isAdvanced, int attempt) {
        if (attempt == 1) return isAdvanced ? 6 : 3;
        if (attempt == 2) return isAdvanced ? 4 : 2;
        if (attempt == 3) return isAdvanced ? 2 : 1;
        return 0;
    }
} 