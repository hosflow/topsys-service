package br.com.topsys.service.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.jdbc.core.RowMapper;

import br.com.topsys.base.util.TSUtil;

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

					if (Types.TIMESTAMP_WITH_TIMEZONE == rs.getMetaData().getColumnType(x + 1)) {

						wrapper.setPropertyValue(parametros[x], rs.getObject(x + 1, OffsetDateTime.class));

					} else if (Types.TIMESTAMP == rs.getMetaData().getColumnType(x + 1)) {

						wrapper.setPropertyValue(parametros[x], rs.getObject(x + 1, LocalDateTime.class));

					} else if (Types.DATE == rs.getMetaData().getColumnType(x + 1)) {

						wrapper.setPropertyValue(parametros[x], rs.getObject(x + 1, LocalDate.class));

					} else if (Types.TIME == rs.getMetaData().getColumnType(x + 1)) {

						wrapper.setPropertyValue(parametros[x], rs.getObject(x + 1, LocalTime.class));

					} else {
						wrapper.setPropertyValue(parametros[x], rs.getObject(x + 1));
					}

				}

			}
		}

		return objeto;

	}

}
