package br.com.topsys.service.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.ZoneId;

import org.springframework.jdbc.core.RowMapper;

public class TSOffsetDateTimeRowMapper implements RowMapper<OffsetDateTime> {
   
	@SuppressWarnings("null")
    @Override
    public OffsetDateTime mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getObject(rowNum + 1, OffsetDateTime.class)
                 .withOffsetSameInstant(ZoneId.of("America/Sao_Paulo").getRules().getOffset(rs.getTimestamp(rowNum + 1).toInstant()));
    }

}
