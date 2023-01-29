package org.pretty.database;

import java.util.Map;

public class Table {
	
	private final Map<String, Object> values;
	
	public Table(Map<String, Object> values) {
		this.values = values;
	}
	
	public Object get(String row) {
		return values.get(row);
	}
	
	public String getString(String row) {
		return get(row).toString();
	}
	
	public int getInt(String row) {
		return Integer.parseInt(getString(row));
	}
	
	public boolean getBoolean(String row) {
		return Boolean.parseBoolean(getString(row));
	}
	
	public Map<String, Object> all() {
		return values;
	}
	
}

