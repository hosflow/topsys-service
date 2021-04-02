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
					if(parametros[x].startsWith(DECRYPT)) {
						wrapper.setPropertyValue(parametros[x].replace(DECRYPT,""),TSCryptoUtil.decrypt(String.valueOf(rs.getObject(x + 1))));
					}else {
						wrapper.setPropertyValue(parametros[x],rs.getObject(x + 1));
					} 
					
				}

			}

		}

		return objeto;

	}

}
