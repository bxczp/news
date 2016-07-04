package Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.NewsType;

/**
 * @date 2016年2月28日 NewsTypeDao.java
 * @author CZP
 * @parameter
 */
public class NewsTypeDao {

	public List<NewsType> newsTypeList(Connection conn) throws Exception {
		List<NewsType> newsTypesList = new ArrayList<>();
		String sql = "select * from t_newsType";
		PreparedStatement preparedStatement = conn.prepareStatement(sql);
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			NewsType newsType = new NewsType();
			newsType.setNewsTypeId(rs.getInt("newsTypeId"));
			newsType.setTypeName(rs.getString("typeName"));
			newsTypesList.add(newsType);
		}
		return newsTypesList;
	}

	public NewsType getNewsTypeById(Connection conn, String newsTypeId) throws Exception {
		NewsType newsType = new NewsType();
		String sql = "select * from t_newsType where newsTypeId=?";
		PreparedStatement preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setInt(1, Integer.parseInt(newsTypeId));
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			newsType.setNewsTypeId(rs.getInt("newsTypeId"));
			newsType.setTypeName(rs.getString("typeName"));
		}
		return newsType;
	}

	public int newsTypeAdd(Connection conn, NewsType newsType) throws Exception {
		String sql = "insert into t_newsType values(null,?)";
		PreparedStatement preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setString(1, newsType.getTypeName());
		return preparedStatement.executeUpdate();
	}

	public int newsTypeUpdate(Connection conn, NewsType newsType) throws Exception {
		String sql = "update t_newsType set typeName =? where newsTypeId=?";
		PreparedStatement preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setString(1, newsType.getTypeName());
		preparedStatement.setInt(2, newsType.getNewsTypeId());
		return preparedStatement.executeUpdate();
	}

	public int newsTypeDelete(Connection conn, String newsTypeId) throws Exception {
		String sql = "delete from t_newsType where newsTypeId=?";
		PreparedStatement preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setInt(1, Integer.parseInt(newsTypeId));
		return preparedStatement.executeUpdate();
	}

}
