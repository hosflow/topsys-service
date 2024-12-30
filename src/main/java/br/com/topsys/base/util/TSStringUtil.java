package br.com.topsys.base.util;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author andre
 * 
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public final class TSStringUtil {

		private TSStringUtil() {
		
	}


	public static boolean isNumeric(String str) {
		if (str == null || str.isBlank()) {
			return false;
		}
		int sz = str.length();
		for (int i = 0; i < sz; i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	

	public static String escapeSql(String str) {
		if (str == null) {
			return null;
		}
		return str.replace("'", "''");
	}

	public static String removeAccent(String palavra) {

		if (TSUtil.isEmpty(palavra)) {

			return null;

		} else {

			return Normalizer.normalize(new StringBuilder(palavra), Normalizer.Form.NFKD)
					.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

		}

	}

	public static String removeNotAlphaNumeric(String palavra) {

		if (TSUtil.isEmpty(palavra)) {

			return null;

		} else {

			return palavra.replaceAll("\\W", "");

		}

	}

	public static String formatName(String nome) {

		if (!TSUtil.isEmpty(nome)) {

			List<String> preposicoes = new ArrayList<>();

			preposicoes.add("da");

			preposicoes.add("das");

			preposicoes.add("de");

			preposicoes.add("do");

			preposicoes.add("dos");

			preposicoes.add("e");

			nome = nome.replace("  ", " ").trim().toLowerCase();

			String[] nomes = nome.split(" ");

			StringBuilder nomeFormatado = new StringBuilder();

			for (String parte : nomes) {

				if (!preposicoes.contains(parte)) {
					nomeFormatado.append(" ");	
					nomeFormatado.append(parte.substring(0, 1).toUpperCase() + parte.substring(1, parte.length()));

				} else {
					nomeFormatado.append(" ");	
					nomeFormatado.append(parte);

				}

			}

			return nomeFormatado.toString().trim();

		} else {

			return null;

		}

	}

	public static String removeFormat(String documento) {

		if (!TSUtil.isEmpty(documento)) {

			documento = documento.replace("-", "");

			documento = documento.replace("/", "");

			StringBuilder numero = new StringBuilder();

			for (int i = 0; i < documento.length(); i++) {

				if (documento.charAt(i) != '.') {

					numero.append(documento.charAt(i));

				}

			}

			documento = numero.toString();

		}

		return documento;

	}

}