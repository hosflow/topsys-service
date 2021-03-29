package br.com.topsys.service.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;

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

				int column = rs.getMetaData().getColumnType(x + 1);

				if (Types.TIMESTAMP_WITH_TIMEZONE == column || Types.TIMESTAMP == column) {

					wrapper.setPropertyValue(parametros[x], rs.getObject(x + 1, OffsetDateTime.class));

				} else if (Types.DATE == column) {

					wrapper.setPropertyValue(parametros[x], rs.getObject(x + 1, LocalDate.class));

				} else if (Types.TIME == column) {

					wrapper.setPropertyValue(parametros[x], rs.getObject(x + 1, LocalTime.class));

				} else {
					wrapper.setPropertyValue(parametros[x], rs.getObject(x + 1));
				}

			}

		}

		return objeto;

	}

}
