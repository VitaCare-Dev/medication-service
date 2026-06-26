package com.grupo10.medication_service;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class MedicationServiceApplicationTests {

	@Test
	void constructor_InstanciaClaseCorrectamente() {
		MedicationServiceApplication app = new MedicationServiceApplication();
		assertNotNull(app);
	}

	@Test
	void main_IniciandoAplicacionSpring(){
		try (MockedStatic<SpringApplication> mockedSpringApplication = Mockito.mockStatic(SpringApplication.class)) {
			MedicationServiceApplication.main(new String[]{});
			mockedSpringApplication.verify(() -> SpringApplication.run(MedicationServiceApplication.class, new String[]{}));
		}
	}

}
