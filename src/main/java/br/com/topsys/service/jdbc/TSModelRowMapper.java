package br.com.topsys.service.jdbc;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.StringTokenizer;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.jdbc.core.RowMapper;

import br.com.topsys.base.model.TSAttributeModel;
import br.com.topsys.base.model.TSColumnModel;
import br.com.topsys.base.model.TSDynamicModel;
import br.com.topsys.base.util.TSCryptoUtil;
import br.com.topsys.base.util.TSStringUtil;
import br.com.topsys.base.util.TSUtil;

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

		if (objeto instanceof TSDynamicModel) {

			for (int x = 0; x < rs.getMetaData().getColumnCount(); x++) {

				String values = rs.getString(x + 1);

				values = values.substring(1, values.lastIndexOf("}"));

				TSColumnModel columnModel = new TSColumnModel();
 
				for(String value : values.split(",")) {

					columnModel.addAttribute(value);

				}

				try {
					objeto.getClass().getMethod("add", TSColumnModel.class).invoke(objeto, columnModel);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		} else if (parametros != null) {

			for (int x = 0; x < parametros.length; x++) {

				int column = rs.getMetaData().getColumnType(x + 1);

				if (Types.TIMESTAMP_WITH_TIMEZONE == column || Types.TIMESTAMP == column) {

					if (rs.getTimestamp(x + 1) != null) {

						wrapper.setPropertyValue(parametros[x],
								OffsetDateTime.ofInstant(rs.getTimestamp(x + 1).toInstant(), ZoneId.systemDefault()));

					}

				} else if (Types.DATE == column) {

					wrapper.setPropertyValue(parametros[x], rs.getObject(x + 1, LocalDate.class));

				} else if (Types.TIME == column) {

					wrapper.setPropertyValue(parametros[x], rs.getObject(x + 1, LocalTime.class));

				} else {
					if (parametros[x].startsWith(DECRYPT)) {
						wrapper.setPropertyValue(parametros[x].replace(DECRYPT, ""),
								TSCryptoUtil.decrypt(rs.getObject(x + 1)));
					} else {
						wrapper.setPropertyValue(parametros[x], rs.getObject(x + 1));
					}

				}

			}

		}

		return objeto;

	}

}
