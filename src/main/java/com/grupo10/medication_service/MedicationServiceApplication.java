package com.grupo10.medication_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada principal del microservicio de gestión de medicamentos.
 *
 * <p>Inicializa el contexto de Spring Boot y arranca el servidor embebido.
 * El microservicio expone una API REST para la creación, consulta,
 * desactivación y eliminación de tratamientos farmacológicos asociados a pacientes.
 */
@SpringBootApplication
public class MedicationServiceApplication {

	/**
	 * Método principal que lanza la aplicación Spring Boot.
	 *
	 * @param args argumentos de línea de comandos pasados al arranque
	 */
	public static void main(String[] args) {
		SpringApplication.run(MedicationServiceApplication.class, args);
	}

}
