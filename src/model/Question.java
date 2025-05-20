package src.model;

// Abstract class representing a question with a question text, correct answer, and score.
// This class serves as a base for different types of questions in the application.
public abstract class Question {
    protected String questionText;
    protected String correctAnswer;
    protected Score score;

    // Constructs a Question with the specified question text and correct answer.
    public Question(String questionText, String correctAnswer) {
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.score = new Score();
    }

    // Gets the text of the question.
    public String getQuestionText() {
        return questionText;
    }

    // Gets the correct answer to the question.
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    // Gets the score associated with the question.
    public Score getScore() {
        return score;
    }

    // Checks if the user's answer is correct.
    public abstract boolean checkAnswer(String userAnswer);
} 