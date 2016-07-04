package Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.Comment;
import Model.PageBean;
import Util.DateUtil;
import Util.StringUtil;

/**
 * @date 2016年3月2日 CommentDao.java
 * @author CZP
 * @parameter
 */
public class CommentDao {
	public List<Comment> commentList(Connection conn, Comment comment, PageBean pageBean, String startCommentDate,
			String endCommentDate) throws Exception {
		List<Comment> commentList = new ArrayList<>();
		StringBuffer sql = new StringBuffer("select * from t_comment t1,t_news t2 where t1.newsId=t2.newsId");
		if (comment.getNewsId() != -1) {
			sql.append(" and t1.newsId=" + comment.getNewsId());
		}
		if (StringUtil.isNotEmpty(startCommentDate)) {
			sql.append(" and TO_DAYS(t1.commentDate) >= TO_DAYS('" + startCommentDate + "')");
		}
		if (StringUtil.isNotEmpty(endCommentDate)) {
			sql.append(" and TO_DAYS(t1.commentDate) <= TO_DAYS('" + endCommentDate + "')");
		}
		sql.append(" order by t1.commentDate desc ");
		if (pageBean != null) {
			sql.append(" limit " + pageBean.getStart() + "," + pageBean.getPageSize());
		}
		PreparedStatement preparedStatement = conn.prepareStatement(sql.toString());
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			Comment comment2 = new Comment();
			comment2.setNewsTitle(rs.getString("title"));
			comment2.setUserIP(rs.getString("userIP"));
			comment2.setCommentId(rs.getInt("commentId"));
			comment2.setNewsId(rs.getInt("newsId"));
			comment2.setContent(rs.getString("content"));
			comment2.setCommentDate(DateUtil.formatString(rs.getString("commentDate"), "yyyy-MM-dd"));
			commentList.add(comment2);
		}
		return commentList;
	}

	public int commentAdd(Connection conn, Comment comment) throws Exception {
		String sql = "insert into t_comment values(null,?,?,?,now())";
		PreparedStatement preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setInt(1, comment.getNewsId());
		preparedStatement.setString(2, comment.getContent());
		preparedStatement.setString(3, comment.getUserIP());
		return preparedStatement.executeUpdate();
	}

	public int commentCount(Connection conn, Comment comment, String startCommentDate, String endCommentDate)
			throws Exception {
		StringBuffer sql = new StringBuffer("select count(*) as total from t_comment t1");
		if (comment.getNewsId() != -1) {
			sql.append(" and newsId=" + comment.getNewsId());
		}
		if (StringUtil.isNotEmpty(startCommentDate)) {
			sql.append(" and TO_DAYS(t1.commentDate) >= TO_DAYS('" + startCommentDate + "')");
		}
		if (StringUtil.isNotEmpty(endCommentDate)) {
			sql.append(" and TO_DAYS(t1.commentDate) <= TO_DAYS('" + endCommentDate + "')");
		}
		sql.append(" order by t1.commentDate desc ");
		PreparedStatement preparedStatement = conn.prepareStatement(sql.toString().replaceFirst("and", "where"));
		ResultSet rs = preparedStatement.executeQuery();
		if (rs.next()) {
			return rs.getInt("total");
		} else {
			return 0;
		}
	}

	// 批量（单个）删除
	public int commentDelete(Connection conn, String commentIds) throws Exception {
		// commentIds的格式必须满足 1,2,. 之间用逗号隔开
		String sql = "delete from t_comment where commentId in (" + commentIds + ")";
		PreparedStatement preparedStatement = conn.prepareStatement(sql);
		return preparedStatement.executeUpdate();
	}

}
