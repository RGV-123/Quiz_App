package com.quiz_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.quiz_app.dao.QuestionDao;
import com.quiz_app.dao.QuizDao;
import com.quiz_app.model.Question;
import com.quiz_app.model.QuestionWrapper;
import com.quiz_app.model.Quiz;
import com.quiz_app.model.Response;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class QuizService {

    @Autowired
    QuizDao quizDao;
    
    @Autowired
    QuestionDao questionDao;

    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {
        List<Question> questions = questionDao.findRandomQuestionsByCategory(category, numQ);
        
        if (questions.isEmpty()) {
            return new ResponseEntity<>("No questions found for this category!", HttpStatus.BAD_REQUEST);
        }

        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestions(questions);
        quizDao.save(quiz);

        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }

    public ResponseEntity<?> createQuizWithDifficulty(String category, String difficulty, int numQ, String title) {
        List<Question> questions;
        
        if (difficulty.equals("All")) {
            questions = questionDao.findRandomQuestionsByCategory(category, numQ);
        } else {
            questions = questionDao.findRandomQuestionsByCategoryAndDifficulty(category, difficulty, numQ);
        }
        
        if (questions.isEmpty() || questions.size() < numQ) {
            return new ResponseEntity<>("Not enough questions available! Only " + questions.size() + " found.", HttpStatus.BAD_REQUEST);
        }

        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestions(questions);
        Quiz savedQuiz = quizDao.save(quiz);

        return new ResponseEntity<>(savedQuiz.getId(), HttpStatus.CREATED);
    }

    public ResponseEntity<List<Quiz>> getAllQuizzes() {
        return new ResponseEntity<>(quizDao.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {
        Optional<Quiz> quiz = quizDao.findById(id);
        
        if (quiz.isEmpty()) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NOT_FOUND);
        }
        
        List<Question> questionsFromDB = quiz.get().getQuestions();
        List<QuestionWrapper> questionsForUser = new ArrayList<>();
        
        for (Question q : questionsFromDB) {
            QuestionWrapper qw = new QuestionWrapper(
                q.getId(), 
                q.getQuestionTitle(), 
                q.getOption1(), 
                q.getOption2(), 
                q.getOption3(), 
                q.getOption4()
            );
            questionsForUser.add(qw);
        }

        return new ResponseEntity<>(questionsForUser, HttpStatus.OK);
    }

    // OLD METHOD - Keep for backward compatibility
    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {
        Optional<Quiz> quizOptional = quizDao.findById(id);
        
        if (quizOptional.isEmpty()) {
            return new ResponseEntity<>(0, HttpStatus.NOT_FOUND);
        }
        
        Quiz quiz = quizOptional.get();
        List<Question> questions = quiz.getQuestions();
        int right = 0;
        int i = 0;
        
        for (Response response : responses) {
            if (i < questions.size() && response.getResponse().equals(questions.get(i).getRightAnswer())) {
                right++;
            }
            i++;
        }
        
        return new ResponseEntity<>(right, HttpStatus.OK);
    }

    // ===== NEW: Detailed Results =====
    public ResponseEntity<Map<String, Object>> calculateDetailedResult(Integer id, List<Response> responses) {
        Optional<Quiz> quizOptional = quizDao.findById(id);
        
        if (quizOptional.isEmpty()) {
            Map<String, Object> error = new LinkedHashMap<>();
            error.put("error", "Quiz not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        
        Quiz quiz = quizOptional.get();
        List<Question> questions = quiz.getQuestions();
        
        int correctCount = 0;
        int wrongCount = 0;
        int unansweredCount = 0;
        
        List<Map<String, Object>> questionResults = new ArrayList<>();
        
        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            String userAnswer = "";
            
            // Find user's response for this question
            for (Response response : responses) {
                if (response.getId().equals(q.getId())) {
                    userAnswer = response.getResponse();
                    break;
                }
            }
            
            String correctAnswer = q.getRightAnswer();
            boolean isCorrect = userAnswer.equals(correctAnswer);
            boolean isUnanswered = userAnswer == null || userAnswer.trim().isEmpty();
            
            if (isUnanswered) {
                unansweredCount++;
            } else if (isCorrect) {
                correctCount++;
            } else {
                wrongCount++;
            }
            
            // Build question result
            Map<String, Object> questionResult = new LinkedHashMap<>();
            questionResult.put("questionNumber", i + 1);
            questionResult.put("questionTitle", q.getQuestionTitle());
            questionResult.put("option1", q.getOption1());
            questionResult.put("option2", q.getOption2());
            questionResult.put("option3", q.getOption3());
            questionResult.put("option4", q.getOption4());
            questionResult.put("userAnswer", userAnswer);
            questionResult.put("correctAnswer", correctAnswer);
            questionResult.put("isCorrect", isCorrect);
            questionResult.put("isUnanswered", isUnanswered);
            
            questionResults.add(questionResult);
        }
        
        // Build final response
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("totalQuestions", questions.size());
        result.put("correctAnswers", correctCount);
        result.put("wrongAnswers", wrongCount);
        result.put("unanswered", unansweredCount);
        result.put("percentage", Math.round((correctCount * 100.0) / questions.size()));
        result.put("questionResults", questionResults);
        
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}