package com.quiz_app.service;

import com.quiz_app.model.Question;
import com.quiz_app.dao.QuestionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    QuestionDao questionDao;

    public ResponseEntity<List<Question>> getAllQuestions() {
        return new ResponseEntity<>(questionDao.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<List<Question>> getQuestionsByCategory(String category) {
        return new ResponseEntity<>(questionDao.findByCategory(category), HttpStatus.OK);
    }

    public ResponseEntity<String> addQuestion(Question question) {
        questionDao.save(question);
        return new ResponseEntity<>("Question Added Successfully", HttpStatus.CREATED);
    }

    // BULK UPLOAD
    public ResponseEntity<String> addMultipleQuestions(List<Question> questions) {
        try {
            questionDao.saveAll(questions);
            return new ResponseEntity<>(questions.size() + " Questions Added Successfully!", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to add questions: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // DELETE QUESTION
    public ResponseEntity<String> deleteQuestion(Integer id) {
        try {
            questionDao.deleteById(id);
            return new ResponseEntity<>("Question Deleted Successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete question", HttpStatus.BAD_REQUEST);
        }
    }

    // ===== NEW: Get all categories =====
    public ResponseEntity<List<String>> getAllCategories() {
        return new ResponseEntity<>(questionDao.findAllCategories(), HttpStatus.OK);
    }

    // ===== NEW: Count questions by category =====
    public ResponseEntity<Integer> getQuestionCountByCategory(String category) {
        return new ResponseEntity<>(questionDao.countByCategory(category), HttpStatus.OK);
    }
}