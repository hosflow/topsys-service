package br.com.topsys.base.util;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import br.com.topsys.base.exception.TSSystemException;

public final class TSCryptoUtil {

	private static final String DES_ECB_PKCS5_PADDING = "DES/ECB/PKCS5Padding";

	private static final String AES_ECB_PKCS5_PADDING = "AES/ECB/PKCS5Padding";
	private static final byte[] CHAVE = "top10sysSistemas".getBytes();

	private TSCryptoUtil() {
	}

	public static String encrypt(Object texto) {
		 
		byte[] retorno = encryptByte(texto);
		
		return retorno != null ? String.valueOf(hexDump(retorno)) : null;

	

	}

	public static byte[] encryptByte(Object texto) {

		byte[] cipherText = null;
		byte[] plainText = null;

		try {
			if (TSUtil.isEmpty(texto) || !(texto instanceof String || texto instanceof Long)) {
				return null;
			}

			plainText = texto.toString().trim().getBytes(StandardCharsets.UTF_8);

			Key key = new SecretKeySpec(CHAVE, "AES");

			Cipher cipher = Cipher.getInstance(AES_ECB_PKCS5_PADDING);

			cipher.init(Cipher.ENCRYPT_MODE, key);

			cipherText = cipher.doFinal(plainText);

		} catch (Exception e) {
			throw new TSSystemException(e);
		}
		return cipherText;
	}


	public static boolean isEncrypt(String hexTexto) {

		if (TSUtil.isEmpty(hexTexto)) {
			return false;
		}

		try {

			Key chave = new SecretKeySpec(CHAVE, "DES");
			Cipher cipher = Cipher.getInstance(DES_ECB_PKCS5_PADDING);

			cipher.init(Cipher.DECRYPT_MODE, chave);

			cipher.doFinal(byteDump(hexTexto));

		} catch (Exception e) {
			return false;
		}

		return true;
	}

	public static String generateHash(String plainText, String algorithm) {

		MessageDigest mdAlgorithm;

		StringBuffer hexString = new StringBuffer();

		try {

			mdAlgorithm = MessageDigest.getInstance(algorithm);

			mdAlgorithm.update(plainText.getBytes());

			byte[] digest = mdAlgorithm.digest();

			for (int i = 0; i < digest.length; i++) {

				plainText = Integer.toHexString(0xFF & digest[i]);

				if (plainText.length() < 2) {

					plainText = "0" + plainText;
				}

				hexString.append(plainText);
			}

		} catch (Exception e) {

			throw new TSSystemException(e);
		}

		return hexString.toString();
	}

	public static String decrypt(Object texto) {
		String retorno = null;

		try {
			byte[] newPlainText = null;

			if (texto == null) {
				return null;
			}

			Key chave = new SecretKeySpec(CHAVE, "AES");

			Cipher cipher = Cipher.getInstance(AES_ECB_PKCS5_PADDING);

			cipher.init(Cipher.DECRYPT_MODE, chave);

			if (texto instanceof byte[]) {

				newPlainText = cipher.doFinal((byte[]) texto);

			} else {

				newPlainText = cipher
						.doFinal(DatatypeConverter.parseHexBinary(String.valueOf(texto).replace("\\x", "")));

			}

			retorno = new String(newPlainText, StandardCharsets.UTF_8);

		} catch (Exception e) {
			throw new TSSystemException(e);
		}

		return retorno;
	}

	private static char[] hexDump(byte[] entrada) {
		char[] buf = new char[entrada.length * 2];

		for (int b = 0; b < entrada.length; b++) {
			String hexByte = Integer.toHexString((int) entrada[b] & 0xff);

			if (hexByte.length() < 2) {
				buf[(b * 2) + 0] = '0';
				buf[(b * 2) + 1] = hexByte.charAt(0);
			} else {
				buf[(b * 2) + 0] = hexByte.charAt(0);
				buf[(b * 2) + 1] = hexByte.charAt(1);
			}
		}

		return buf;
	}

	private static byte[] byteDump(String hexTexto) {
		byte[] byteRetorno = new byte[hexTexto.length() / 2];

		for (int i = 0, j = 0; i <= (hexTexto.length() - 2); i += 2, j++) {
			byteRetorno[j] = (byte) (Integer.parseInt(hexTexto.substring(i, i + 2), 16));
		}

		return byteRetorno;
	}

}
