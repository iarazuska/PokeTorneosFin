package com.example.demo.Entidades;

import jakarta.persistence.*;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "entrenador")
public class Entrenador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String nacionalidad;

    @OneToOne(mappedBy = "entrenador")
    private Carnet carnet;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "participacion",
            joinColumns = @JoinColumn(name = "entrenador_id"),
            inverseJoinColumns = @JoinColumn(name = "torneo_id")
    )
    private Set<Torneo> torneos = new HashSet<>();

    public Entrenador() {}

    public Entrenador(Long id, String nombre, String nacionalidad, Carnet carnet, Set<Torneo> torneos) {
        this.id = id;
        this.nombre = nombre;
        this.nacionalidad = nacionalidad;
        this.carnet = carnet;
        this.torneos = torneos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public Carnet getCarnet() {
        return carnet;
    }

    public void setCarnet(Carnet carnet) {
        this.carnet = carnet;
    }

    public Set<Torneo> getTorneos() {
        return torneos;
    }

    public void setTorneos(Set<Torneo> torneos) {
        this.torneos = torneos;
    }

    @Transactional
    public void agregarTorneo(Torneo torneo) {
        this.torneos.add(torneo);
        torneo.getEntrenadores().add(this);
    }

    @Override
    public String toString() {
        return "Entrenador{" +
                "nacionalidad='" + nacionalidad + '\'' +
                ", nombre='" + nombre + '\'' +
                ", id=" + id +
                ", numVictorias=" + (carnet != null ? carnet.getNumVictorias() : "Sin carnet") +
                '}';
    }
}
