package com.quiz_app.service;

import com.quiz_app.model.Question;
import com.quiz_app.dao.QuestionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        // Check for duplicate
        List<Question> existing = questionDao.findByCategory(question.getCategory());
        for (Question q : existing) {
            if (q.getQuestionTitle().equals(question.getQuestionTitle())) {
                return new ResponseEntity<>("Question already exists!", HttpStatus.BAD_REQUEST);
            }
        }
        questionDao.save(question);
        return new ResponseEntity<>("Question Added Successfully", HttpStatus.CREATED);
    }

    // BULK UPLOAD — Skip Duplicates
    public ResponseEntity<String> addMultipleQuestions(List<Question> questions) {
        try {
            List<Question> allExisting = questionDao.findAll();
            List<Question> newQuestions = new ArrayList<>();
            int skipped = 0;

            for (Question q : questions) {
                boolean isDuplicate = false;
                for (Question existing : allExisting) {
                    if (existing.getQuestionTitle().equals(q.getQuestionTitle()) &&
                        existing.getCategory().equals(q.getCategory())) {
                        isDuplicate = true;
                        skipped++;
                        break;
                    }
                }
                if (!isDuplicate) {
                    newQuestions.add(q);
                }
            }

            if (!newQuestions.isEmpty()) {
                questionDao.saveAll(newQuestions);
            }

            String message = newQuestions.size() + " Questions Added Successfully!";
            if (skipped > 0) {
                message += " (" + skipped + " duplicates skipped)";
            }
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed: " + e.getMessage(), HttpStatus.BAD_REQUEST);
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

    // GET ALL CATEGORIES
    public ResponseEntity<List<String>> getAllCategories() {
        return new ResponseEntity<>(questionDao.findAllCategories(), HttpStatus.OK);
    }

    // COUNT BY CATEGORY
    public ResponseEntity<Integer> getQuestionCountByCategory(String category) {
        return new ResponseEntity<>(questionDao.countByCategory(category), HttpStatus.OK);
    }
}