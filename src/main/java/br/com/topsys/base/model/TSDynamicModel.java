package br.com.topsys.base.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TSDynamicModel implements Serializable {

	private List<TSColumnModel> columns = new ArrayList<>();

	public void add(TSColumnModel column) {
		this.columns.add(column);
	}
	

}
