package Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.News;
import Model.PageBean;
import Util.DateUtil;
import Util.PropertiesUtil;
import Util.StringUtil;

/**
 * @date 2016年2月28日 NewsDao.java
 * @author CZP
 * @parameter
 */
public class NewsDao {

	public List<News> newsList(Connection conn, String sql) throws Exception {
		List<News> newsList = new ArrayList<>();
		PreparedStatement preparedStatement = conn.prepareStatement(sql);
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			News news = new News();
			news.setAuthor(rs.getString("author"));
			news.setClick(rs.getInt("click"));
			news.setContent(rs.getString("content"));
			news.setImageName(PropertiesUtil.getValue("userImage") + rs.getString("imageName"));
			news.setIsHead(rs.getInt("isHead"));
			news.setIsHot(rs.getInt("isHot"));
			news.setIsImage(rs.getInt("isImage"));
			news.setNewsId(rs.getInt("newsId"));
			news.setPublishDate(DateUtil.formatString(rs.getString("publishDate"), "yyyy-MM-dd HH:mm:ss"));
			news.setTitle(rs.getString("title"));
			news.setTypeId(rs.getInt("typeId"));
			newsList.add(news);
		}
		return newsList;
	}

	public List<News> newsList(Connection conn, News t_news, PageBean pageBean, String startPublishDate,
			String endPublishDate) throws Exception {
		List<News> newsList = new ArrayList<>();
		StringBuffer sql = new StringBuffer("select * from t_news t1,t_newsType t2 where t1.typeId = t2.newsTypeId ");
		if (t_news.getTypeId() != -1) {
			sql.append(" and t1.typeId=" + t_news.getTypeId());
		}
		if (StringUtil.isNotEmpty(t_news.getTitle())) {
			sql.append(" and t1.title like '%" + t_news.getTitle() + "%'");
		}
		if (StringUtil.isNotEmpty(startPublishDate)) {
			// TO_DAYS(date)
			// 给定一个日期date, 返回一个天数 (从年份0开始的天数 )
			sql.append(" and TO_DAYS(t1.publishDate) >= TO_DAYS('" + startPublishDate + "') ");
		}
		if (StringUtil.isNotEmpty(endPublishDate)) {
			sql.append(" and TO_DAYS(t1.publishDate) <= TO_DAYS('" + endPublishDate + "') ");
		}
		sql.append(" order by t1.publishDate desc");
		if (pageBean != null) {
			sql.append(" limit " + pageBean.getStart() + "," + pageBean.getPageSize());
		}

		PreparedStatement preparedStatement = conn.prepareStatement(sql.toString());
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			News news = new News();
			news.setAuthor(rs.getString("author"));
			news.setClick(rs.getInt("click"));
			news.setContent(rs.getString("content"));
			news.setImageName(PropertiesUtil.getValue("userImage") + rs.getString("imageName"));
			news.setIsHead(rs.getInt("isHead"));
			news.setIsHot(rs.getInt("isHot"));
			news.setIsImage(rs.getInt("isImage"));
			news.setNewsId(rs.getInt("newsId"));
			news.setTypeName(rs.getString("typeName"));
			news.setPublishDate(DateUtil.formatString(rs.getString("publishDate"), "yyyy-MM-dd HH:mm:ss"));
			news.setTitle(rs.getString("title"));
			news.setTypeId(rs.getInt("typeId"));
			newsList.add(news);
		}
		return newsList;
	}

	// 获取总记录数
	public int newsCount(Connection conn, News t_news, String startPublishDate, String endPublishDate)
			throws Exception {
		StringBuffer sql = new StringBuffer("select count(*) as total from t_news");
		if (t_news.getTypeId() != -1) {
			sql.append(" where typeId= " + t_news.getTypeId());
		}
		if (StringUtil.isNotEmpty(t_news.getTitle())) {
			sql.append(" and title like '%" + t_news.getTitle() + "%'");
		}
		if (StringUtil.isNotEmpty(startPublishDate)) {
			sql.append(" and TO_DAYS(publishDate) >= TO_DAYS('" + startPublishDate + "') ");
		}
		if (StringUtil.isNotEmpty(endPublishDate)) {
			sql.append(" and TO_DAYS(publishDate) <= TO_DAYS('" + endPublishDate + "') ");
		}
		PreparedStatement preparedStatement = conn.prepareStatement(sql.toString().replaceFirst("and", "where"));
		ResultSet rs = preparedStatement.executeQuery();
		if (rs.next()) {
			return rs.getInt("total");
		} else {
			return 0;
		}
	}

	public News getNewsById(Connection conn, String newsId) throws Exception {
		News news = new News();
		String sql = "select * from t_news t1,t_newsType t2 where t1.newsId=? and t1.typeId=t2.newsTypeId ";
		PreparedStatement preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setInt(1, Integer.parseInt(newsId));
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			news.setTypeName(rs.getString("typeName"));
			news.setAuthor(rs.getString("author"));
			news.setClick(rs.getInt("click"));
			news.setContent(rs.getString("content"));
			news.setImageName(PropertiesUtil.getValue("userImage") + rs.getString("imageName"));
			news.setIsHead(rs.getInt("isHead"));
			news.setIsHot(rs.getInt("isHot"));
			news.setIsImage(rs.getInt("isImage"));
			news.setNewsId(rs.getInt("newsId"));
			news.setPublishDate(DateUtil.formatString(rs.getString("publishDate"), "yyyy-MM-dd HH:mm:ss"));
			news.setTitle(rs.getString("title"));
			news.setTypeId(rs.getInt("typeId"));
		}
		return news;
	}

	public int newsClick(Connection conn, String newsId) throws Exception {
		String sql = "update t_news set click=click+1 where newsId=? ";
		PreparedStatement preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setInt(1, Integer.parseInt(newsId));
		return preparedStatement.executeUpdate();
	}

	// 上一篇与下一篇
	public List<News> getUpAndDownPageID(Connection conn, String newsId) throws Exception {
		List<News> upAndDownPage = new ArrayList<>();
		String sql = "select * from t_news where newsId<? order by newsId desc limit 0,1";
		PreparedStatement preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setInt(1, Integer.parseInt(newsId));
		ResultSet rs = preparedStatement.executeQuery();
		if (rs.next()) {
			upAndDownPage.add(new News(rs.getInt("newsId"), rs.getString("title")));
		} else {
			upAndDownPage.add(new News(-1, ""));
		}
		sql = "select * from t_news where newsId>? order by newsId limit 0,1";
		preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setInt(1, Integer.parseInt(newsId));
		rs = preparedStatement.executeQuery();
		if (rs.next()) {
			upAndDownPage.add(new News(rs.getInt("newsId"), rs.getString("title")));
		} else {
			upAndDownPage.add(new News(-1, ""));
		}
		return upAndDownPage;
	}

	// 根据newsTypeId查找该类别下是否有新闻
	public boolean existNewsWithNewsTypeId(Connection conn, String newsTypeId) throws Exception {
		String sql = "select * from t_news where typeId=?";
		PreparedStatement preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setInt(1, Integer.parseInt(newsTypeId));
		ResultSet rs = preparedStatement.executeQuery();
		if (rs.next()) {
			return true;
		} else {
			return false;
		}
	}

	// 新闻添加
	public int newsAdd(Connection conn, News news) throws Exception {
		String sql = "insert into t_news values(null,?,?,now(),?,?,0,?,?,?,?)";
		PreparedStatement preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setString(1, news.getTitle());
		preparedStatement.setString(2, news.getContent());
		preparedStatement.setString(3, news.getAuthor());
		preparedStatement.setInt(4, news.getTypeId());
		preparedStatement.setInt(5, news.getIsHead());
		preparedStatement.setInt(6, news.getIsImage());
		preparedStatement.setString(7, news.getImageName());
		preparedStatement.setInt(8, news.getIsHot());
		return preparedStatement.executeUpdate();
	}

	public int newsUpdate(Connection conn, News news) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append(
				"update t_news set title=?,content=?,author=?,typeId=?,isHead=?,isImage=?,imageName=?,isHot=? where newsId=?");
		PreparedStatement preparedStatement = conn.prepareStatement(sb.toString());
		preparedStatement.setString(1, news.getTitle());
		preparedStatement.setString(2, news.getContent());
		preparedStatement.setString(3, news.getAuthor());
		preparedStatement.setInt(4, news.getTypeId());
		preparedStatement.setInt(5, news.getIsHead());
		preparedStatement.setInt(6, news.getIsImage());
		preparedStatement.setString(7, news.getImageName());
		preparedStatement.setInt(8, news.getIsHot());
		preparedStatement.setInt(9, news.getNewsId());
		return preparedStatement.executeUpdate();
	}

	public int newsDelete(Connection conn, String newsId) throws Exception {
		String sql = "delete from t_news where newsId=?";
		PreparedStatement preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setInt(1, Integer.parseInt(newsId));
		return preparedStatement.executeUpdate();
	}

}
