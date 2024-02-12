package br.com.hayann;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Robot {

	private final String LINK_PAGINA_EVENTO = "https://sites.pucgoias.edu.br/certificados/";
	private final String LINK_PAGINA_PARTICIPANTE = "https://sites.pucgoias.edu.br/certificados/participantes/";
	private final String REGEX_PROCURA_EVENTO = "\\(\\{\"id\":(\\d+),\"titulo\":\"(.+)\"}\\);";
	private final String REGEX_PROCURA_PARTICIPANTE_1 = "<a href=\"(https:\\/\\/sites.pucgoias.edu.br\\/certificados\\/ver\\/[a-zA-Z0-9]+)\">(";
	private final String REGEX_PROCURA_PARTICIPANTE_2 = ".+?)<\\/a>";
	private final String NOME_PARTICIPANTE = "HAYANN GON.ALVES";

	public void run() throws IOException {
		List<Evento> eventoList = new ArrayList<>();

		LocalTime localTimeInicio = LocalTime.now();

		int contTotalEventos = 0;

		URL urlEvento = new URL(LINK_PAGINA_EVENTO);
		BufferedReader readerEvento = forceRead(urlEvento);

		String lineEvento;
		while ((lineEvento = readerEvento.readLine()) != null) { //lê linha por linha para procurar ocorrência de eventos
			Pattern patternEvento = Pattern.compile(REGEX_PROCURA_EVENTO);
			Matcher matcherEvento = patternEvento.matcher(lineEvento);

			if (matcherEvento.find()) {
				contTotalEventos++;

				Integer idEvento = Integer.parseInt(matcherEvento.group(1));
				String tituloEvento = matcherEvento.group(2);

				URL urlPaginaParticipante = new URL(LINK_PAGINA_PARTICIPANTE + idEvento);
				BufferedReader readerPaginaParticipante = forceRead(urlPaginaParticipante);

				Evento evento;
				String lineParticipante;
				while ((lineParticipante = readerPaginaParticipante.readLine()) != null) { //lê linha por linha para procurar linha dos participantes
					Pattern patternParticipante = Pattern.compile(REGEX_PROCURA_PARTICIPANTE_1 + NOME_PARTICIPANTE + REGEX_PROCURA_PARTICIPANTE_2, Pattern.CASE_INSENSITIVE);
					Matcher matcherParticipante = patternParticipante.matcher(lineParticipante);

					while (matcherParticipante.find()) {
						evento = new Evento(idEvento, tituloEvento, matcherParticipante.group(1));
						eventoList.add(evento);
						System.out.println(evento);
					}
				}
				readerPaginaParticipante.close();
			}
		}
		readerEvento.close();
		LocalTime localTimeFim = LocalTime.now();
		Duration duration = Duration.between(localTimeFim, localTimeInicio);
		System.out.println("===================================\n" +
				"Horário de início: " + localTimeInicio + "\n" +
				"Horário de fim: " + localTimeFim + "\n" +
				"Duração total: " + duration + "\n" +
				"Quantidade de eventos: " + contTotalEventos + "\n" +
				"Quantidade de certificados encontrados: " + eventoList.size());
	}

	private BufferedReader forceRead(URL url) {
		BufferedReader bufferedReader = null;
		try {
			while (bufferedReader == null) {
				bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return forceRead(url);
		}
		return bufferedReader;
	}
}
