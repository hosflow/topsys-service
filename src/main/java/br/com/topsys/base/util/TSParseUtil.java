package br.com.topsys.base.util;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import br.com.topsys.base.exception.TSSystemException;

/*
 * Created on 25/08/2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */


public final class TSParseUtil {
	
	private static final String DD_MM_YYYY_H_MM = "dd/MM/yyyy H:mm";
	private static final String DD_MM_YYYY = "dd/MM/yyyy";
	
	private static NumberFormat numFormat = NumberFormat.getNumberInstance();
	
	private TSParseUtil() {
		 
	}

	public static String numberToString(Number valor) {
		String number = null;
		if (valor != null) {
			NumberFormat numFormat = NumberFormat.getNumberInstance(Locale.of("pt", "BR"));
			number = numFormat.format(valor);
		} else {
			number = "";
		}
		return number;
	}

	public static Boolean stringToBoolean(String valor) {
		Boolean bool = null;

		if (valor == null || valor.trim().length() == 0) {
			bool = false;
		} else {
			bool = valor.trim().equals("0");
		}
		return bool;
	}

	public static String booleanToString(Boolean valor) {
		if (valor == null) {
			return "0";
		} else {
			return valor.booleanValue() ? "1" : "0";
		}
	}

	public static Integer stringToInteger(String valor) {
		Integer it = null;
		
		try {
			if (valor != null) {
				it = Integer.valueOf(valor);
			}
		} catch (Exception e) {
		}
		return it;
	}

	public static Long stringToLong(String valor) {
		Long it = null;
		try {
			if (valor != null) {
				it = Long.valueOf(valor);
			}
		} catch (Exception e) {
		}

		return it;
	}

	public static Float stringToFloat(String valor) {
		Float it = null;
		try {
			if (valor != null) {
				it = numFormat.parse(valor).floatValue();
			}
		} catch (Exception e) {
		}
		return it;
	}

	public static Date stringToDate(String data) {
		Date date = null;
		if (data != null && data.trim().length() > 0) {
			try {
				SimpleDateFormat dtFormat = new SimpleDateFormat(DD_MM_YYYY);
				date = dtFormat.parse(data);
			} catch (Exception e) {
			}
		}
		return date;
	}

	public static Date stringToDate(String data, String padrao) {
		Date date = null;
		if (data != null && data.trim().length() > 0) {
			try {
				SimpleDateFormat dtFormat = new SimpleDateFormat(padrao);
				date = dtFormat.parse(data);
			} catch (Exception e) {
			}
		}
		return date;
	}

	public static Timestamp stringToTimestamp(String data) {
		Timestamp ts = null;
		if ((data == null) || data.equals("")) {

			return null;
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat(DD_MM_YYYY);

		try {

			ParsePosition pos = new ParsePosition(0);
			java.util.Date dtu = dateFormat.parse(data, pos);

			ts = new Timestamp(dtu.getTime());

		} catch (Exception e) {
		}

		return ts;
	}

	public static Timestamp stringToTimestamp(String data, String pattern) {
		Timestamp ts = null;
		if ((data == null) || data.equals("")) {

			return null;
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

		try {

			ParsePosition pos = new ParsePosition(0);
			java.util.Date dtu = dateFormat.parse(data, pos);

			ts = new Timestamp(dtu.getTime());

		} catch (Exception e) {
		}

		return ts;
	}

	public static String timestampToString(Timestamp data) {
		if (data == null) {
			return null;
		}
		Date date = data;
		return dateToString(date, DD_MM_YYYY);

	}

	public static Timestamp stringDateHourToTimestamp(String data) {
		Timestamp ts = null;
		if ((data == null) || data.equals("")) {

			return null;
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

		try {

			ParsePosition pos = new ParsePosition(0);
			java.util.Date date = dateFormat.parse(data, pos);

			ts = new Timestamp(date.getTime());

		} catch (Exception e) {
		}

		return ts;
	}

	public static String timestampToStringHora(Timestamp data) {
		if (data == null) {
			return null;
		}
	
		return dateHourToString(data);

	}

	public static String dateHourToString(Date data) {
		SimpleDateFormat sdf = new SimpleDateFormat(DD_MM_YYYY_H_MM);
		return sdf.format(data);
	}

	public static Date stringToDateHour(String data) {
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(DD_MM_YYYY_H_MM);
			date = sdf.parse(data);
		} catch (Exception e) {

			throw new TSSystemException(e);
		}
		return date;
	}

	public static String dateToString(Date data) {
		String dataStr = null;
		if (data != null) {
			SimpleDateFormat dtFormat = new SimpleDateFormat(DD_MM_YYYY);
			dataStr = dtFormat.format(data);
		} else {
			dataStr = "";
		}
		return dataStr;
	}

	public static String dateToString(Date data, String padrao) {
		String dataStr = null;
		if (data != null) {
			SimpleDateFormat dtFormat = new SimpleDateFormat(padrao);
			dataStr = dtFormat.format(data);
		} else {
			dataStr = "";
		}
		return dataStr;
	}

	private static DecimalFormat monetario() {

		Locale loc = Locale.of("pt", "BR");
		NumberFormat nf = NumberFormat.getNumberInstance(loc);
		DecimalFormat df = (DecimalFormat) nf;
		df.applyPattern("###,##0.00");

		return df;

	}


	public static String stringToMonetario(String value) {
		String retorno = null;
		if (value == null) {
			return null;
		}
		try {
			retorno = monetario().parse(value).toString();
		
		} catch (Exception e) {

			throw new TSSystemException(e);
		}
		return retorno;

	}


	public static Double stringToDouble(String value) {
		Double retorno = null;
		if (value == null) {
			return null;
		}
		try {
			char charNumero[] = value.toCharArray();
			String numero = "";
			for (int i = 0; i < charNumero.length; i++) {
				if (charNumero[i] != '.') {
					numero += charNumero[i];
				}

			}

			retorno = Double.valueOf(numero.replace(',', '.'));

		} catch (Exception e) {

			throw new TSSystemException(e);
		} 
		return retorno;
	}

	public static String doubleToString(Double value) {
		String retorno = null;
		if (value == null) {
			return null;
		}
		try {

			retorno = monetario().format(value.doubleValue());
		} catch (Exception e) {

			throw new TSSystemException(e);
		}

		return retorno;

	}

	public static Timestamp dateToTimeStamp(Date data) {

		if (!TSUtil.isEmpty(data)) {

			return TSParseUtil.stringToTimestamp(TSParseUtil.dateToString(data, TSDateUtil.DD_MM_YYYY_HH_MM_SS),
					TSDateUtil.DD_MM_YYYY_HH_MM_SS);

		} else {

			return null;

		}

	}

	public static Timestamp dateToTimeStampFinal(Date data) {

		if (!TSUtil.isEmpty(data)) {

			Calendar cal = Calendar.getInstance();

			cal.setTime(data);

			cal.add(Calendar.HOUR, 23);

			cal.add(Calendar.MINUTE, 59);

			cal.add(Calendar.SECOND, 59);

			return TSParseUtil.stringToTimestamp(
					TSParseUtil.dateToString(cal.getTime(), TSDateUtil.DD_MM_YYYY_HH_MM_SS),
					TSDateUtil.DD_MM_YYYY_HH_MM_SS);

		} else {

			return null;

		}

	}
}