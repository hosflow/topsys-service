package br.com.topsys.service.log;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.com.topsys.base.util.TSUtil;

@Component
public class TSLog {

	private static final String DATA_HORA_MINUTOS = "dd/MM/yyyy HH:mm:ss";
	private static final String DATA = "dd-MM-yyyy";

	private Instant instant;

	private String query;
	private String parametrosQuery;

	@Value("${topsys.log.path.file}")
	private String pathFile;

	@Value("${topsys.log.query.max.seconds}")
	private long maxSeconds;

	public TSLog(String query, Object[] parametros) {
		this.query = query;
		this.parametrosQuery = Arrays.toString(parametros);
	}

	public void begin() {
		this.instant = Instant.now();

	}

	public void end() {

		if (this.hasConfig()) {

			Duration duration = Duration.between(this.instant, Instant.now());

			long duracaoSegundos = duration.toSeconds();

			if (duracaoSegundos >= this.maxSeconds) {

				this.saveInFile(duracaoSegundos);

			}

		} 

	}

	private boolean hasConfig() {

		return !TSUtil.isEmpty(this.pathFile) && !TSUtil.isEmpty(this.maxSeconds);

	}

	private void saveInFile(long duracaoSegundos) {

		Path path = Paths.get(this.pathFile + LocalDate.now().format(DateTimeFormatter.ofPattern(DATA)) + ".csv");

		StringBuilder builder = new StringBuilder();

		String dataFormatada = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATA_HORA_MINUTOS));

		builder.append("[").append(dataFormatada).append("];")
				.append(this.query).append(";")
				.append(this.parametrosQuery).append(";")
				.append(duracaoSegundos).append(";")
				.append(System.getProperty("line.separator"));

		try {

			Files.write(path, builder.toString().getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
