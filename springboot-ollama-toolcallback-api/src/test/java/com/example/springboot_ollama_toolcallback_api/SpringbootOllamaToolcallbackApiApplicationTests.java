package com.example.springboot_ollama_toolcallback_api;

import org.junit.jupiter.api.Test;

import java.time.*;


class szaSpringbootOllamaToolcallbackApiApplicationTests {

	@Test
	void contextLoads() {
		String name = "hehe";
		String name2 = new String("hehe");
//		assertTrue(name == name2);

		ZoneId zoneId = ZoneId.of("America/Chicago");
		ZonedDateTime zdt = ZonedDateTime.of(
				LocalDate.of(2021, 11, 7),
				LocalTime.of(1, 30),
				zoneId
		);
		ZoneOffset zdtOffset = zdt.getOffset();
		int zdtHour = zdt.getHour();

		ZonedDateTime anHourLater = zdt.plusHours(1);
		ZoneOffset anHourLaterOffset = anHourLater.getOffset();
		int anHourLaterHour = anHourLater.getHour();

		System.out.println(zdt.getHour() == anHourLater.getHour());
		System.out.println(zdt.getOffset() == anHourLater.getOffset());


	}

}
