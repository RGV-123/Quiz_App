package com.quiz_app.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.quiz_app.model.Question;

import java.util.List;

@Repository
public interface QuestionDao extends JpaRepository<Question, Integer> {

    List<Question> findByCategory(String category);

    @Query(value = "SELECT * FROM question q Where q.category=:category ORDER BY RAND() LIMIT :numQ", nativeQuery = true)
    List<Question> findRandomQuestionsByCategory(String category, int numQ);

     // ===== NEW: Get all unique categories =====
    @Query(value = "SELECT DISTINCT category FROM question", nativeQuery = true)
    List<String> findAllCategories();

    // ===== NEW: Get questions by category and difficulty =====
    @Query(value = "SELECT * FROM question q WHERE q.category=:category AND q.difficulty_level=:difficulty ORDER BY RAND() LIMIT :numQ", nativeQuery = true)
    List<Question> findRandomQuestionsByCategoryAndDifficulty(String category, String difficulty, int numQ);

    // ===== NEW: Count questions by category =====
    @Query(value = "SELECT COUNT(*) FROM question WHERE category=:category", nativeQuery = true)
    Integer countByCategory(String category);


}
