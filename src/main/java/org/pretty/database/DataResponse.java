package org.pretty.database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class DataResponse {

	private List<Table> result = new ArrayList<Table>();
	
	public DataResponse(List<Table> result) {
		this.result = result;
	}
	
	public Table first() {
		return result.get(0);
	}
	
	public Table last() {
		return result.get(result.size() - 1);
	}
	
	public List<Table> all() {
		return Collections.unmodifiableList(result);
	}
	
	public boolean isEmpty() {
		return result.isEmpty();
	}
	
	public Table get(int index) {
		return result.get(index);
	}
	
	public Stream<Table> stream() {
		return result.stream();
	}
}
