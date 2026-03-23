package com.quiz_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.quiz_app.model.Question;
import com.quiz_app.service.QuestionService;

import java.util.List;

@RestController
@RequestMapping("question")
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @GetMapping("allQuestions")
    public ResponseEntity<List<Question>> getAllQuestions() {
        return questionService.getAllQuestions();
    }

    @GetMapping("category/{category}")
    public ResponseEntity<List<Question>> getQuestionsByCategory(@PathVariable String category) {
        return questionService.getQuestionsByCategory(category);
    }

    @PostMapping("add")
    public ResponseEntity<String> addQuestion(@RequestBody Question question) {
        return questionService.addQuestion(question);
    }

    // BULK UPLOAD
    @PostMapping("addMultiple")
    public ResponseEntity<String> addMultipleQuestions(@RequestBody List<Question> questions) {
        return questionService.addMultipleQuestions(questions);
    }

    // DELETE QUESTION
    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteQuestion(@PathVariable Integer id) {
        return questionService.deleteQuestion(id);
    }

    // ===== NEW: Get all categories =====
    @GetMapping("categories")
    public ResponseEntity<List<String>> getAllCategories() {
        return questionService.getAllCategories();
    }

    // ===== NEW: Count questions by category =====
    @GetMapping("count/{category}")
    public ResponseEntity<Integer> getQuestionCount(@PathVariable String category) {
        return questionService.getQuestionCountByCategory(category);
    }
}