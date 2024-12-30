package br.com.topsys.base.util;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import br.com.topsys.base.exception.TSSystemException;

public final class TSDateUtil {

	public static final String DD_MM_YYYY = "dd/MM/yyyy";

	public static final String MM_YYYY = "MM/yyyy";

	public static final String MM = "MM";

	public static final String YYYY = "yyyy";

	public static final String DD = "dd";

	public static final String MM_DD_YYYY = "MM/dd/yyyy";

	public static final String YYYY_MM_DD = "yyyy/MM/dd";

	public static final String HH_MM_SS = "HH:mm:ss";

	public static final String hh_MM_SS = "hh:mm:ss";

	public static final String DD_MM_YYYY_HH_MM_SS = "dd/MM/yyyy HH:mm:ss";

	public static final String DD_MM_YYYY_hh_MM_SS = "dd/MM/yyyy hh:mm:ss";

	public static final String DD_MM_YYYY_HH_MM = "dd/MM/yyyy HH:mm";

	public static final String DD_MM_YYYY_hh_MM = "dd/MM/yyyy hh:mm";

	public static final String HH_MM = "HH:mm";
	
	public static final String DD_MM_YYYY_HH_MM_Z = "dd/MM/yyyy HH:mm (Z)"; 
	
	

	private TSDateUtil() {

	}

	

	public static String getFormat(LocalDateTime localDateTime, String patterns) { 
		return TSUtil.isNotEmpty(localDateTime) ? localDateTime.format(DateTimeFormatter.ofPattern(patterns)) : null;
	}
	
	public static String getFormat(OffsetDateTime offsetDateTime, String patterns) { 
		return TSUtil.isNotEmpty(offsetDateTime) ? offsetDateTime.format(DateTimeFormatter.ofPattern(patterns)) : null;
	}
	
	
	
	public static Date addDayDate(Date data, int num) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		cal.add(Calendar.DAY_OF_MONTH, num);
		return cal.getTime();
	}

	
	public static boolean isValidDateBeforeDate(String dataInicial, String dataFinal) {
		Date dateIni = TSParseUtil.stringToDate(dataInicial);
		Date dateFim = TSParseUtil.stringToDate(dataFinal);
		return dateIni.before(dateFim);
	}

	
	public static boolean isValidDate(String data) {
		Date date = null;
		if (data != null && data.trim().length() > 0) {
			try {
				SimpleDateFormat dtFormat = new SimpleDateFormat(DD_MM_YYYY);
				date = dtFormat.parse(data);
			} catch (Exception e) {}
		}
		return date == null;

	}


	public static boolean isValidDate(String data, String patterns) {
		Date date = null;
		if (data != null && data.trim().length() > 0) {
			try {
				SimpleDateFormat dtFormat = new SimpleDateFormat(patterns);
				date = dtFormat.parse(data);
			} catch (Exception e) {}
		}
		return date == null;

	}


	public static long diffDays(String dataInicial, String dataFinal, String pattern) {

		SimpleDateFormat dfFormat = new SimpleDateFormat(pattern);
		long dtInicio = 0;
		long dtFim = 0;
		long resultado = 0;
		try {

			dtInicio = dfFormat.parse(dataInicial).getTime();
			dtFim = dfFormat.parse(dataFinal).getTime();
			resultado = (dtFim - dtInicio) / (24 * 60 * 60 * 1000);

		} catch (Exception e) {
			
			throw new TSSystemException(e);
		}
		return resultado;
	}

	
	public static boolean isEqualDate(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			return false;
		}

		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		return cal1.get(5) == cal2.get(5) && cal1.get(2) == cal2.get(2) && cal1.get(1) == cal2.get(1);
	}

	public static String obterDiaSemana(Date data) {

		Calendar calendario = new GregorianCalendar();

		calendario.setTime(TSParseUtil.stringToDate(TSParseUtil.dateToString(data, TSDateUtil.DD_MM_YYYY), TSDateUtil.DD_MM_YYYY));

		int dia = calendario.get(Calendar.DAY_OF_WEEK);

		String diaSemana = null;

		switch (dia) {

		case 1: {
			diaSemana = "Domingo";
			break;
		}
		case 2: {
			diaSemana = "Segunda";
			break;
		}
		case 3: {
			diaSemana = "Terça";
			break;
		}
		case 4: {
			diaSemana = "Quarta";
			break;
		}
		case 5: {
			diaSemana = "Quinta";
			break;
		}
		case 6: {
			diaSemana = "Sexta";
			break;
		}
		case 7: {
			diaSemana = "Sábado";
			break;
		}default:
			throw new TSSystemException("Dia da semana inválido.");
		}
		return diaSemana;

	}

}