/*
 * Created on 08/05/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

package br.com.topsys.base.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.topsys.base.model.TSMainModel;

public final class TSUtil {

	private static final int[] PESO_CPF = { 11, 10, 9, 8, 7, 6, 5, 4, 3, 2 };

	private static final int[] PESO_CNPJ = { 6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };

	private static final int TAM_CNPJ = 14;

	private static final int TAM_CPF = 11;

	private static final String STRING_DEFAULT = "0";

	private TSUtil() {

	}

	public static String cpfWithMask(String cpf) {
		if (TSUtil.isEmpty(cpf)) {
			return null;
		}

		cpf = removeCharacter(cpf);

		cpf = fillString(cpf, TAM_CPF);

		return cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
	}

	public static String cnpjWithMask(String cnpj) {
		if (TSUtil.isEmpty(cnpj)) {
			return null;
		}

		cnpj = removeCharacter(cnpj);

		cnpj = fillString(cnpj, TAM_CNPJ);

		return cnpj.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
	}

	public static String removeCharacter(String param) {

		return TSUtil.isEmpty(param) ? null : param.replaceAll("\\D", "");

	}

	private static String fillString(String param, int tamanho) {

		if (param == null) {
			param = "";
		}
		int dif = tamanho - param.length();

		for (; dif > 0; dif--) {
			param = STRING_DEFAULT + param;
		}
		return param;
	}

	public static boolean isNotEmpty(String value) {
		return !isEmpty(value);
	}

	public static boolean isEmpty(String value) {
		return value == null || String.valueOf(value).isBlank();
	}

	public static boolean isNotEmpty(Object... values) {
		return !isEmpty(values);
	}

	@SuppressWarnings("rawtypes")
	public static boolean isEmpty(Object... values) {
		boolean retorno = false;

		for (Object value : values) {

			if (value == null 
					|| (value instanceof Collection && ((Collection) value).isEmpty())
					|| (value instanceof String && String.valueOf(value).isBlank())) {

				retorno = true;
				break;
			}
		}

		return retorno;
	}

	public static boolean isEmailValid(String email) {
		Pattern patterns = Pattern.compile(".+@.+\\.[a-z]+");
		Matcher matcher = patterns.matcher(email);

		return matcher.matches();
	}

	public static boolean isNumeric(String numero) {
		return TSStringUtil.isNumeric(numero);

	}

	public static String generateId() {

		Calendar calendario = Calendar.getInstance();

		int diaAtual = calendario.get(Calendar.DAY_OF_MONTH);
		int mesAtual = calendario.get(Calendar.MONTH) + 1;
		int anoAtual = calendario.get(Calendar.YEAR);
		int horaAtual = calendario.get(Calendar.HOUR_OF_DAY);
		int minutoAtual = calendario.get(Calendar.MINUTE);
		int segAtual = calendario.get(Calendar.SECOND);
		int miliAtual = calendario.get(Calendar.MILLISECOND);

		return String.valueOf(anoAtual) + String.valueOf(mesAtual) + String.valueOf(diaAtual)
				+ String.valueOf(horaAtual) + String.valueOf(minutoAtual) + String.valueOf(segAtual)
				+ String.valueOf(miliAtual);

	}

	public static String calculatePercentage(Double total, Double valorParcial) {

		if (!TSUtil.isEmpty(total)) {

			return "0";

		} else {

			DecimalFormat df = new DecimalFormat("0.00");

			Double parcial = valorParcial * 100;

			Double porcetagem = parcial / total;

			return df.format(porcetagem).replace(",00", "");

		}

	}

	private static int calculateDigit(String str, int[] peso) {

		int soma = 0;

		for (int indice = str.length() - 1, digito; indice >= 0; indice--) {

			digito = Integer.parseInt(str.substring(indice, indice + 1));

			soma += digito * peso[peso.length - str.length() + indice];

		}

		soma = 11 - soma % 11;

		return soma > 9 ? 0 : soma;

	}

	public static boolean isValidCPF(String cpf) {

		cpf = removeCharacter(cpf);

		if (cpf == null || cpf.length() != 11) {
			return false;
		}

		Integer digito1 = calculateDigit(cpf.substring(0, 9), PESO_CPF);

		Integer digito2 = calculateDigit(cpf.substring(0, 9) + digito1, PESO_CPF);

		return cpf.equals(cpf.substring(0, 9) + digito1.toString() + digito2.toString());

	}

	public static boolean isValidCNPJ(String cnpj) {

		cnpj = removeCharacter(cnpj);

		if (cnpj == null || cnpj.length() != 14) {
			return false;
		}

		Integer digito1 = calculateDigit(cnpj.substring(0, 12), PESO_CNPJ);

		Integer digito2 = calculateDigit(cnpj.substring(0, 12) + digito1, PESO_CNPJ);

		return cnpj.equals(cnpj.substring(0, 12) + digito1.toString() + digito2.toString());

	}

	public static String formatCurrency(Double valor) {

		String valorFormatado = "";

		if (!TSUtil.isEmpty(valor)) {

			NumberFormat nf = new DecimalFormat("###,###,##0.00");

			valorFormatado = nf.format(valor);

		}

		return valorFormatado;

	}

	public static Double unformatCurrency(String valor) {

		Number n = 0;

		if (!TSUtil.isEmpty(valor)) {

			NumberFormat nf = new DecimalFormat("###,###,###,##0.00");

			try {

				n = nf.parse(valor);

			} catch (ParseException e) {
				e.printStackTrace();
			}

		}

		return n.doubleValue();

	}

	public static String getYearMonth(Date data) {

		if (!TSUtil.isEmpty(data)) {

			return TSParseUtil.dateToString(data, TSDateUtil.YYYY) + "/" + TSParseUtil.dateToString(data, TSDateUtil.MM)
					+ "/";

		}

		return null;

	}

	public static String getSituation(Boolean flagAtivo) {

		if (!TSUtil.isEmpty(flagAtivo)) {

			return "Ativo";

		}

		return "Inativo";

	}

	public static String generatePassword() {

		try {

			return TSCryptoUtil.encrypt(generateId());

		} catch (Exception e) {

			e.printStackTrace();

			return null;

		}

	}

	public static String removeEmptySpaces(String valor) {

		return TSUtil.isNotEmpty(valor) ? valor.trim().replace("  ", " ") : null;

	}
	
	public static boolean isTrue(Boolean flag) {
		return flag != null ? flag : false;
	}
    
	public static boolean isEmptyId(TSMainModel model) {
		return model == null ? true : model.getId() == null;
	}
	
	public static String getLikeString(String campo, boolean percentDuplo) {
		return campo == null ? null : percentDuplo ? "%" + campo.trim() + "%" : campo.trim() + "%";
	}
    
	public static Long extrairId(TSMainModel model) {
		return model == null ? null : model.getId();
	}

}