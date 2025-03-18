package com.example.demo.SpreenBot.repository;


import com.example.demo.Entidades.Combate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CombateRepository extends JpaRepository<Combate, Long> {



}
