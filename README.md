[![](https://jitpack.io/v/TakuiasH/pretty-database.svg)](https://jitpack.io/#TakuiasH/pretty-database)

# pretty-database
A prettier solution of dealing with databases

## Usage
Here is an CRUD example
```java
public class User {
	
	private Long id;

	private String username;
	private String password;
	private String email;
	
	private Long created_at;
	
	public User() {}
	
	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public Long getCreated_at() {
		return created_at;
	}
	
	public void setCreated_at(Long created_at) {
		this.created_at = created_at;
	}
}
```

```java
public class UserRepository {
	
	private Database database;
	
	public UserRepository(Database database) {
		this.database = database;
		
		database.execute("CREATE TABLE IF NOT EXISTS users("
				+ " id BIGINT PRIMARY KEY,"
				+ " username TEXT UNIQUE NOT NULL,"
				+ " password TEXT NOT NULL,"
				+ " email TEXT NOT NULL,"
				+ " created_at BIGINT NOT NULL"
				+ ")");
	}
	
	public boolean create(User user) {		
		if(!database.select("SELECT * FROM users WHERE username=?", user.getUsername()).isEmpty())
			return false;
		
		user.setPassword(BCrypt.withDefaults().hashToString(10, user.getPassword().toCharArray()));
		
		return database.execute("INSERT INTO users(username, password, email, created_at) VALUES(?, ?, ?, ?)", user.getUsername(), user.getPassword(), user.getEmail(), System.currentTimeMillis());
	}
	
	public User read(String username) {
		DataResponse response = database.select("SELECT * FROM users WHERE username=?", username);
		
		if(response.isEmpty())
			return null;
		
		Table table = response.first();
		
		User entity = new User(username, table.getString("password"));
		entity.setId(table.getLong("id"));
		entity.setEmail(table.getString("email"));
		entity.setCreated_at(table.getLong("created_at"));
		
		return entity;
	}
	
	public boolean update(long id, User newUser) {
		DataResponse response = database.select("SELECT * FROM users WHERE id=?", id);
		
		if(response.isEmpty())
			return false;
		
		Table table = response.first();

		if(newUser.getUsername() == null) newUser.setUsername(table.getString("username"));
		if(newUser.getPassword() == null) newUser.setPassword(table.getString("password"));
		if(newUser.getEmail() == null) newUser.setEmail(table.getString("email"));
		if(newUser.getCreated_at() == null) newUser.setCreated_at(table.getLong("created_at"));
		if(newUser.getId() == null) newUser.setId(id);
		
		return database.execute("UPDATE users SET username=?, password=?, email=?, created_at=? WHERE id=?", 
				newUser.getUsername(),
				newUser.getPassword(),
				newUser.getEmail(),
				newUser.getCreated_at(),
				newUser.getId());
	}
	
	public boolean delete(long id) {
		return database.execute("DELETE FROM users WHERE id=?", id);
	}
}
```

## Setup
Before get started, make sure you have the [JitPack repository](https://jitpack.io/#TakuiasH/pretty-database) included in your build configuration.

Maven
```xml
<dependency>
    <groupId>com.github.TakuiasH</groupId>
    <artifactId>pretty-database</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```
