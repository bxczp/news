package Util;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @date 2016年2月28日 DbUtil.java
 * @author CZP
 * @parameter
 */
public class DbUtil {

	public Connection getConn() throws Exception {
		Class.forName(PropertiesUtil.getValue("jdbcName"));
		return DriverManager.getConnection(PropertiesUtil.getValue("dbUrl"), PropertiesUtil.getValue("userName"),
				PropertiesUtil.getValue("password"));

	}

	public void closeConn(Connection conn) throws Exception {
		if (conn != null) {
			conn.close();
		}
	}

}
