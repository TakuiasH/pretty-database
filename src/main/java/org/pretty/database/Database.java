package org.pretty.database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {

	private final ConnectionType type;
	
	private Connection connection;
	
	private File sqliteFile;
	private String host, databaseName, username, password;
	private String postgresqlHost, postgresqlDatabase;
	
	public Database(String host, String databaseName, String username, String password) {
		this.type = ConnectionType.MYSQL;
		
		this.host = host;
		this.databaseName = databaseName;
		this.username = username;
		this.password = password;
	}
	
	public Database(String sqliteFilePath) {
		this(new File(sqliteFilePath));
	}
	
	public Database(String postgresqlHost, String postgresqlDatabase) {
		this.type = ConnectionType.POSGRESQL;
		
		this.postgresqlDatabase = postgresqlDatabase;
		this.postgresqlHost = postgresqlHost;
	}


	public Database(File sqliteFile) {
		this.type = ConnectionType.SQLITE;
		this.sqliteFile = sqliteFile;
		
		try { if(!sqliteFile.exists()) sqliteFile.createNewFile(); } catch (IOException e) { e.printStackTrace(); }
	}
	
	public Database(File sqliteDirectory, String sqliteFileName) {
		if(!sqliteDirectory.exists())
			sqliteDirectory.mkdirs();
		
		this.type = ConnectionType.SQLITE;
		this.sqliteFile = new File(sqliteDirectory, sqliteFileName);
		
		try { if(!sqliteFile.exists()) sqliteFile.createNewFile(); } catch (IOException e) { e.printStackTrace(); }
	}
	
	public ConnectionType getConnectionType() {
		return this.type;
	}
	
	public Connection getConnection() {
		try {
			if(connection == null || connection.isClosed()) {
				if(type == ConnectionType.MYSQL)
					connection = DriverManager.getConnection("jdbc:mysql://" + host + "/" + databaseName + "?autoReconnect=true", username, password);
				else if(type == ConnectionType.SQLITE)
					connection = DriverManager.getConnection("jdbc:sqlite:" + sqliteFile.getAbsolutePath() + "?autoReconnect=true");
				else
					connection = DriverManager.getConnection("jdbc:postgresql://" + postgresqlHost + "/" + postgresqlDatabase + "?autoReconnect=true");
			}
			return connection;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public DataResponse select(String query, Object... arguments) {
		try {
			List<Table> results = new ArrayList<Table>();
			
			PreparedStatement st = getConnection().prepareStatement(query);

		    for( int i = 0; i < arguments.length;i++){
		        st.setObject(i+1,arguments[i]);
		    }
			
			ResultSet rs = st.executeQuery();
		
			
			while (rs.next()) {
				 Map<String, Object> values = new HashMap<String, Object>();
				
				for (int y = 1; y <= rs.getMetaData().getColumnCount(); y++) {
					values.put(rs.getMetaData().getColumnLabel(y), rs.getObject(y));
				}
				
				results.add(new Table(values));
			}
			
			st.close();
			
			return new DataResponse(results);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Integer execute(String query, Object... arguments) {
		try {
			PreparedStatement st = getConnection().prepareStatement(query);

		    for( int i = 0; i < arguments.length;i++){
		        st.setObject(i+1,arguments[i]);
		    }
			
			int result = st.executeUpdate();
			st.close();		
			
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public enum ConnectionType {
		MYSQL, POSGRESQL, SQLITE;
	}
}
