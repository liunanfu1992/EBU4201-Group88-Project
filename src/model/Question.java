package src.model;

public abstract class Question {
    protected String questionText;
    protected String correctAnswer;
    protected Score score;

    public Question(String questionText, String correctAnswer) {
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.score = new Score();
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public Score getScore() {
        return score;
    }


    public abstract boolean checkAnswer(String userAnswer);
} 