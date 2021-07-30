package br.com.topsys.service.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.jdbc.core.RowMapper;

import br.com.topsys.base.util.TSCryptoUtil;

public class TSModelRowMapper<T> implements RowMapper<T> {

	private static final String DECRYPT = "decrypt.";
	private String[] parametros;
	private Class<T> classe;

	public TSModelRowMapper(Class<T> classe, String... parametros) {
		this.classe = classe;
		this.parametros = parametros;
	}

	@Override
	public T mapRow(ResultSet rs, int rowNum) throws SQLException {

		T objeto = BeanUtils.instantiateClass(classe);

		try {

			objeto.getClass().getMethod("build").invoke(objeto);

		} catch (Exception e) {
		}

		BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(objeto);
		wrapper.setAutoGrowNestedPaths(true);

		if (parametros != null) {

			for (int x = 0; x < parametros.length; x++) {
			
				if (parametros[x].startsWith(DECRYPT)) {
					wrapper.setPropertyValue(parametros[x].replace(DECRYPT, ""),
							TSCryptoUtil.decrypt(rs.getObject(x + 1)));
				} else {
					wrapper.setPropertyValue(parametros[x], rs.getObject(x + 1));
				}

			}

		}

		return objeto;

	}

}
