import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

class Question {
    private String questionText;
    private String[] options;
    private char correctAnswer;

    public Question(String questionText, String[] options, char correctAnswer) {
        this.questionText = questionText;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String[] getOptions() {
        return options;
    }

    public char getCorrectAnswer() {
        return correctAnswer;
    }
}

class Quiz {
    private ArrayList<Question> questions;
    private int score;
    private ArrayList<Boolean> results;
    private ArrayList<Character> userAnswers;

    public Quiz() {
        questions = new ArrayList<>();
        score = 0;
        results = new ArrayList<>();
        userAnswers = new ArrayList<>();
    }

    public void addQuestion(Question question) {
        questions.add(question);
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            System.out.println("\nQuestion " + (i + 1) + ": " + question.getQuestionText());
            String[] options = question.getOptions();
            for (int j = 0; j < options.length; j++) {
                System.out.println((char) ('A' + j) + ". " + options[j]);
            }

            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    System.out.println("\nTime's up! Moving to the next question.");
                    results.add(false);
                    userAnswers.add(' ');
                    synchronized (scanner) {
                        scanner.notify();
                    }
                }
            };

            timer.schedule(task, 15000); // 15 seconds timer

            System.out.print("Enter your answer: ");
            char userAnswer = ' ';
            boolean answered = false;
            synchronized (scanner) {
                if (scanner.hasNext()) {
                    userAnswer = scanner.next().toUpperCase().charAt(0);
                    answered = true;
                }
            }

            timer.cancel();

            if (answered) {
                userAnswers.add(userAnswer);
                if (userAnswer == question.getCorrectAnswer()) {
                    score++;
                    results.add(true);
                } else {
                    results.add(false);
                }
            }
        }
        scanner.close();
        displayResults();
    }

    private void displayResults() {
        System.out.println("\nQuiz Completed!");
        System.out.println("Your final score is: " + score + "/" + questions.size());
        for (int i = 0; i < results.size(); i++) {
            String result = results.get(i) ? "Correct" : "Incorrect";
            System.out.println("Question " + (i + 1) + ": " + result);
            if (!results.get(i)) {
                System.out.println("Your answer: " + userAnswers.get(i));
                System.out.println("Correct answer: " + questions.get(i).getCorrectAnswer());
            }
        }
    }
}

public class QuizApplication {
    public static void main(String[] args) {
        Quiz quiz = new Quiz();

        // Add questions to the quiz
        quiz.addQuestion(new Question(
            "What is the capital of France?",
            new String[]{"Berlin", "Madrid", "Paris", "Rome"},
            'C'
        ));

        quiz.addQuestion(new Question(
            "Which planet is known as the Red Planet?",
            new String[]{"Earth", "Mars", "Jupiter", "Venus"},
            'B'
        ));

        quiz.addQuestion(new Question(
            "What is the largest ocean on Earth?",
            new String[]{"Atlantic Ocean", "Indian Ocean", "Southern Ocean", "Pacific Ocean"},
            'D'
        ));

        quiz.addQuestion(new Question(
            "Who wrote 'To Kill a Mockingbird'?",
            new String[]{"Harper Lee", "Mark Twain", "J.K. Rowling", "Ernest Hemingway"},
            'A'
        ));

        quiz.addQuestion(new Question(
            "Which element has the chemical symbol 'O'?",
            new String[]{"Gold", "Oxygen", "Silver", "Osmium"},
            'B'
        ));

        quiz.addQuestion(new Question(
            "What is the capital of Japan?",
            new String[]{"Seoul", "Beijing", "Tokyo", "Bangkok"},
            'C'
        ));

        // Start the quiz
        quiz.start();
    }
}
