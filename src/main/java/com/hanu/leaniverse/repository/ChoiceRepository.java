package com.hanu.leaniverse.repository;

import com.hanu.leaniverse.model.Choice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChoiceRepository extends JpaRepository<Choice,Integer> {

}
