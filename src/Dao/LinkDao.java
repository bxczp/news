package Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.Link;

/**
 * @date 2016年2月28日 LinkDao.java
 * @author CZP
 * @parameter
 */
public class LinkDao {

	public List<Link> linkList(Connection conn, String sql) throws Exception {
		List<Link> linkList = new ArrayList<Link>();
		PreparedStatement preparedStatement = conn.prepareStatement(sql);
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			Link link = new Link();
			link.setLinkName(rs.getString("linkName"));
			link.setLinkEmail(rs.getString("linkEmail"));
			link.setLinkUrl(rs.getString("linkUrl"));
			link.setOrderNum(rs.getInt("orderNum"));
			link.setLinkId(rs.getInt("linkId"));
			linkList.add(link);
		}
		return linkList;
	}

	public int linkAdd(Connection conn, Link link) throws Exception {
		String sql = "insert into t_link values(null,?,?,?,?)";
		PreparedStatement preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setString(1, link.getLinkName());
		preparedStatement.setString(2, link.getLinkUrl());
		preparedStatement.setString(3, link.getLinkEmail());
		preparedStatement.setInt(4, link.getOrderNum());
		return preparedStatement.executeUpdate();
	}

	public int linkUpdate(Connection conn, Link link) throws Exception {
		String sql = "update t_link set linkName=?,linkUrl=?,linkEmail=?,orderNum=? where linkId=?";
		PreparedStatement preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setString(1, link.getLinkName());
		preparedStatement.setString(2, link.getLinkUrl());
		preparedStatement.setString(3, link.getLinkEmail());
		preparedStatement.setInt(4, link.getOrderNum());
		preparedStatement.setInt(5, link.getLinkId());
		return preparedStatement.executeUpdate();
	}

	public int linkDelete(Connection conn, String linkId) throws Exception {
		String sql = "delete from t_link where linkId=?";
		PreparedStatement preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setInt(1, Integer.parseInt(linkId));
		return preparedStatement.executeUpdate();
	}

	public Link getLinkById(Connection conn, String linkId) throws Exception {
		Link link = new Link();
		String sql = "select * from t_link where linkId=?";
		PreparedStatement preparedStatemen = conn.prepareStatement(sql);
		preparedStatemen.setInt(1, Integer.parseInt(linkId));
		ResultSet rs = preparedStatemen.executeQuery();
		while (rs.next()) {
			link.setLinkEmail(rs.getString("linkEmail"));
			link.setLinkId(rs.getInt("linkId"));
			link.setLinkName(rs.getString("linkName"));
			link.setLinkUrl(rs.getString("linkUrl"));
			link.setOrderNum(rs.getInt("orderNum"));
		}
		return link;
	}

}
