package com.quiz_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.quiz_app.model.QuestionWrapper;
import com.quiz_app.model.Quiz;
import com.quiz_app.model.Response;
import com.quiz_app.service.QuizService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("quiz")
public class QuizController {

    @Autowired
    QuizService quizService;

    @PostMapping("create")
    public ResponseEntity<String> createQuiz(
            @RequestParam String category, 
            @RequestParam int numQ, 
            @RequestParam String title) {
        return quizService.createQuiz(category, numQ, title);
    }

    @PostMapping("createWithDifficulty")
    public ResponseEntity<?> createQuizWithDifficulty(
            @RequestParam String category,
            @RequestParam String difficulty,
            @RequestParam int numQ,
            @RequestParam String title) {
        return quizService.createQuizWithDifficulty(category, difficulty, numQ, title);
    }

    @GetMapping("all")
    public ResponseEntity<List<Quiz>> getAllQuizzes() {
        return quizService.getAllQuizzes();
    }

    @GetMapping("get/{id}")
    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(@PathVariable Integer id) {
        return quizService.getQuizQuestions(id);
    }

    // OLD - Simple score
    @PostMapping("submit/{id}")
    public ResponseEntity<Integer> submitQuiz(
            @PathVariable Integer id, 
            @RequestBody List<Response> responses) {
        return quizService.calculateResult(id, responses);
    }

    // ===== NEW: Detailed Results =====
    @PostMapping("submitDetailed/{id}")
    public ResponseEntity<Map<String, Object>> submitQuizDetailed(
            @PathVariable Integer id,
            @RequestBody List<Response> responses) {
        return quizService.calculateDetailedResult(id, responses);
    }
}