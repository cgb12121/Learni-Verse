package com.hanu.leaniverse.repository;

import com.hanu.leaniverse.model.Quizz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizzRepository extends JpaRepository<Quizz, Integer> {
    @Query(value ="SELECT * FROM quizz q WHERE q.unit_id = ?1",
    nativeQuery = true)
    public List<Quizz> findQuizzByUnitId(int unitId);
}
