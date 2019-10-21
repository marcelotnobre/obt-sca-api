package br.com.obt.sca.api.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;

// Exemplos de formatação
// https://dicasdejava.com.br/java-8-como-formatar-localdate-e-localdatetime/
public class DataUtils {

	public static int getCalcularIdade(final LocalDate aniversario) {
		final LocalDate dataAtual = LocalDate.now();
		final Period periodo = Period.between(aniversario, dataAtual);
		return periodo.getYears();
	}

	public static String getDataFormatada(LocalDate data, String pattern) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		return data.format(formatter);
	}

	public static String getDataHoraFormatada(LocalDateTime dataHora, String pattern) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		return dataHora.format(formatter);
	}

}
