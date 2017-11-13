package com.jcs.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OracleConnection {

	private JCSFileUtils utils = null;
	public OracleConnection() throws IOException {
		utils = new JCSFileUtils();
	}
	
	public Connection getConneciton() {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(utils.getDbUrl(), utils.getDbAccount(), utils.getDbPassword());
			return connection;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;

		}
	}

}
