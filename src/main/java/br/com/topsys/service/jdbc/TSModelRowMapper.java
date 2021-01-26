package br.com.topsys.service.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.jdbc.core.RowMapper;

public class TSModelRowMapper<T> implements RowMapper<T> {

	private String[] parametros;
	private T classe;
	private BeanWrapper wrapper;

	public TSModelRowMapper(Class<T> classe, String... parametros) {
		this.classe = BeanUtils.instantiateClass(classe);
		wrapper = PropertyAccessorFactory.forBeanPropertyAccess(this.classe);
		wrapper.setAutoGrowNestedPaths(true);
		this.parametros = parametros;
	}

	@Override
	public T mapRow(ResultSet rs, int rowNum) throws SQLException {

		if (parametros != null) {
			for (int x = 0; x < parametros.length; x++) {
				if(wrapper.isWritableProperty(parametros[x])){
					wrapper.setPropertyValue(parametros[x], rs.getObject(x + 1));
				}
				
			}
		}

		return classe;

	}

}
