package br.com.topsys.service.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.jdbc.core.RowMapper;

public class TSModelRowMapper<T> implements RowMapper<T> {

	private String[] parametros;
	private Class<T> classe;
	

	public TSModelRowMapper(Class<T> classe, String... parametros) {
		this.classe = classe;
		this.parametros = parametros;
	}

	@Override
	public T mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		T objeto = BeanUtils.instantiateClass(classe);
		BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(objeto);
		wrapper.setAutoGrowNestedPaths(true);

		if (parametros != null) {
			for (int x = 0; x < parametros.length; x++) {
				if (wrapper.isWritableProperty(parametros[x])) {
					wrapper.setPropertyValue(parametros[x], rs.getObject(x + 1));
				}

			}
		}

		return objeto;

	}

}
