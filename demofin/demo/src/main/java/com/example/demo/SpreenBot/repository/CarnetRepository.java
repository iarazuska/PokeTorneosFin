package com.example.demo.SpreenBot.repository;


import com.example.demo.Entidades.Carnet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarnetRepository extends JpaRepository<Carnet, Long> {
}