package com.example.demo.SpreenBot.repository;


import com.example.demo.Entidades.Torneo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TorneoRepository extends JpaRepository<Torneo, Long> {

}