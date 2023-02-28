package org.pretty.database;

import java.util.Map;
import java.util.UUID;

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
	
	public Integer getInt(String row) {
		try {
			return Integer.parseInt(getString(row));
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	public UUID getUUID(String row) {
		try {
			return UUID.fromString(getString(row));
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	public Long getLong(String row) {
		try {
			return Long.parseLong(getString(row));
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	public Boolean getBoolean(String row) {
		return Boolean.parseBoolean(getString(row));
	}
	
	public Map<String, Object> all() {
		return values;
	}
	
}

