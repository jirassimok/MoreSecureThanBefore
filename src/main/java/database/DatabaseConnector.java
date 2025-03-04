package database;

import java.sql.*;

/**
 * Class for creating connections to the database and initializing the tables
 *
 * - init(): Creates a connection
 * - close(): closes the connection
 */
class DatabaseConnector
{
	private static String driver = "org.apache.derby.jdbc.EmbeddedDriver";

	private Connection db_connection;
	private String connection_string;

	DatabaseConnector() throws DatabaseException {
		this.connection_string = "jdbc:derby:DB;create=true";
		this.init();
	}

	Connection getConnection() {
		return this.db_connection;
	}

	/**
	 * Attempt to connect to the database
	 *
	 * If the database does not exist, it is created and the tables are created.
	 *
	 * @throws DatabaseException if the connection fails
	 */
	void init()
			throws DatabaseException {
		boolean success = this.initDB();
		if (! success) {
			throw new DatabaseException("Connection failed");
		}

		SQLWarning warning;
		try {
			// db warning was issued if db existed before this function was called
			warning = this.db_connection.getWarnings();
			// if null, no warning = new database
		} catch (SQLException e) {
			throw new DatabaseException("Failed to check connecton warnings", e);
		}

		if (warning == null) { //if null, DB does not exist
			success = this.reInitDirectorySchema();
			if (! success) {
				throw new DatabaseException("Failed to initialize database schema");
			}
			this.reInitUsersSchema();
		}
	}

	/**
	 * Close the connection to the database
	 *
	 * @return true if successful
	 */
	boolean close() {
		try {
			this.db_connection.close();
			return true;
		} catch (SQLException e) {
			System.err.println("Failed to close connection");
			e.printStackTrace();
			return false;
		}
	}


	//initialize the database
	//returns true if success, false if failure
	// (do not add db calls after the line indicated below)
	private boolean initDB() {
		try {
			Class.forName(DatabaseConnector.driver);
		} catch(ClassNotFoundException e) {
			System.err.println("Java DB Driver not found. Add the classpath to your module.");
			return false;
		}
		System.out.println("Connected to database");

		try {
			this.db_connection = DriverManager.getConnection(this.connection_string);
			// MAKE NO MORE DB CALLS AFTER THIS POINT (they could break this.init())
		} catch (SQLException e) {
			System.err.println("Connection failed. Check output console.");
			e.printStackTrace();
			return false;
		}
		System.out.println("Java DB connection established!");
		return true;
	}

	/**
	 * Initialize the database schema
	 *
	 * Deletes and recreates all tables in the database
	 *
	 * @return true if successful
	 */
	// TODO: Refactor so reInitSchema throws SQLException to be handled elsewhere in the class
	boolean reInitDirectorySchema() {
		boolean result;
		Statement initSchema = null;
		try {
			initSchema = this.db_connection.createStatement();
		} catch (SQLException e) {
			//something's really bad if we get here. like "we don't have a database" bad
			e.printStackTrace();
			return false;
		}

		for (String dropStatement : StoredProcedures.getDirectoryDrops()) {
			//drop the table if it exists
			try {
				initSchema.executeUpdate(dropStatement);
			} catch (SQLException e) {
				System.err.println("Failed statement: " + dropStatement);
				System.err.println(e.getMessage());
			}
		}

		for (String table : StoredProcedures.getDirectorySchema()) {
			try {
				initSchema.executeUpdate(table);
			} catch (SQLException e) {
				System.err.println("Failed statement: " + table);
				System.err.println(e.getMessage());
			}
		}

		/**  Code below wasn't working and was replaced with for loops above. Keeping just
		 *   in case we want to use it again. */

		//close statement
		try {
			initSchema.close();
		} catch (SQLException e) {
			System.err.println("Failed to close connection");
			e.printStackTrace();
			return false;
		}

		//stop once we find the first match(assume one create statement per string)
		return true;
	}

	public void reInitUsersSchema() throws DatabaseException {
		try (Statement stmt = this.db_connection.createStatement()) {
			this.reInitUsersSchemaInner(stmt);
		} catch (SQLException e) {
			throw new DatabaseException("Error opening or closing statement", e);
		}
	}

	private void reInitUsersSchemaInner(Statement stmt) throws DatabaseException {
		for (String dropStatement : StoredProcedures.getUsersDrops()) {
			//drop the table if it exists
			try {
				stmt.executeUpdate(dropStatement);
			} catch (SQLException e) {
				// Only throw if the exception wasn't "couldn't drop because table doesn't exist"
				if (!"42Y55".equals(e.getSQLState())) {
					throw new DatabaseException("Failed to clear users schema", e);
				}
			}
		}

		for (String table : StoredProcedures.getUsersSchema()) {
			try {
				stmt.executeUpdate(table);
			} catch (SQLException e) {
				throw new DatabaseException("Failed to initialize users schema", e);
			}
		}
	}
}
