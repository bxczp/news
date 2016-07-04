package Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import Model.User;

/**
 * @date 2016年3月2日 UserDao.java
 * @author CZP
 * @parameter
 */
public class UserDao {

	public User login(Connection conn, User user) throws Exception {
		User resultUser = null;
		String sql = "select * from t_user where userName=? and password=? ";
		PreparedStatement preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setString(1, user.getUserName());
		preparedStatement.setString(2, user.getPassword());
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			resultUser = new User();
			resultUser.setUserName(rs.getString("userName"));
			resultUser.setPassword(rs.getString("password"));
			resultUser.setUserId(rs.getInt("userId"));
		}
		return resultUser;

	}

}
