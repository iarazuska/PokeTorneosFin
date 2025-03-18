package com.example.demo.Entidades;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "combate")
public class Combate {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Temporal(TemporalType.DATE)
	private LocalDate fecha;

	@ManyToOne
	@JoinColumn(name = "id_torneo")
	private Torneo torneo;

	@ManyToOne
	@JoinColumn(name = "id_entrenador1")
	private Entrenador entrenador1;

	@ManyToOne
	@JoinColumn(name = "id_entrenador2")
	private Entrenador entrenador2;

	@Column(name = "ganador")
	private Long ganador;

	public Combate() {}

	public Combate(Long id, LocalDate fecha, Torneo torneo, Entrenador entrenador1, Entrenador entrenador2) {
		this.id = id;
		this.fecha = fecha;
		this.torneo = torneo;
		this.entrenador1 = entrenador1;
		this.entrenador2 = entrenador2;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public Torneo getTorneo() {
		return torneo;
	}

	public void setTorneo(Torneo torneo) {
		this.torneo = torneo;
	}

	public Entrenador getEntrenador1() {
		return entrenador1;
	}

	public void setEntrenador1(Entrenador entrenador1) {
		this.entrenador1 = entrenador1;
	}

	public Entrenador getEntrenador2() {
		return entrenador2;
	}

	public void setEntrenador2(Entrenador entrenador2) {
		this.entrenador2 = entrenador2;
	}

	public Long getGanador() {
		return ganador;
	}

	public void setGanador(Long ganador) {
		this.ganador = ganador;
	}

	@Override
	public String toString() {
		return "Combate{" +
				"id=" + id +
				", fecha=" + fecha +
				", ganador=" + ganador +
				'}';
	}
}
