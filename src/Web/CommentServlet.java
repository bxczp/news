package Web;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Dao.CommentDao;
import Dao.NewsDao;
import Model.Comment;
import Model.PageBean;
import Util.DbUtil;
import Util.NavUtil;
import Util.PageUtil;
import Util.PropertiesUtil;
import Util.ResponseUtil;
import Util.StringUtil;
import jdk.nashorn.api.scripting.JSObject;
import net.sf.json.JSONObject;

/**
 * @date 2016年3月2日 CommentServlet.java
 * @author CZP
 * @parameter
 */
public class CommentServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DbUtil dbUtil = new DbUtil();
	private CommentDao commentDao = new CommentDao();
	private NewsDao newsDao = new NewsDao();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		String action = req.getParameter("action");
		if ("save".equals(action)) {
			this.commentSave(req, resp);
		} else if ("backList".equals(action)) {
			this.commentBackList(req, resp);
		} else if ("delete".equals(action)) {
			this.commentDelete(req, resp);
		}
	}

	private void commentDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Connection conn = null;
		req.setCharacterEncoding("UTF-8");
		String commentIds = req.getParameter("commentIds");
		try {
			conn = dbUtil.getConn();
			int delNum = commentDao.commentDelete(conn, commentIds);
			JSONObject result = new JSONObject();
			if (delNum > 0) {
				result.put("success", true);
				result.put("delNum", delNum);
			} else {
				result.put("errorMsg", "删除失败！");
			}
			// 服务端在把response提交到客户端之前，会使用一个缓冲区，并向该缓冲区内写入响应头和状态码，
			// 然后将所有内容flush（flush包含两个步骤：先将缓冲区内容发送至客户端，然后将缓冲区清空）。
			// 这就标志着该次响应已经committed(提交)。对于当前页面中已经committed(提交)的response，
			// 就不能再使用这个response向缓冲区写任何东西
			req.getRequestDispatcher("/background/mainTemp.jsp").forward(req, resp);
			ResponseUtil.write(result, resp);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				dbUtil.closeConn(conn);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private void commentBackList(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		Connection conn = null;
		HttpSession session = req.getSession();
		String startCommentDate = req.getParameter("startCommentDate");
		String endCommentDate = req.getParameter("endCommentDate");
		// 当前页数
		String page = req.getParameter("page");
		if (StringUtil.isEmpty(page)) {
			page = "1";
			session.setAttribute("startCommentDate", startCommentDate);
			session.setAttribute("endCommentDate", endCommentDate);
		} else {
			endCommentDate = (String) session.getAttribute("endCommentDate");
			startCommentDate = (String) session.getAttribute("startCommentDate");
		}
		try {
			conn = dbUtil.getConn();
			PageBean pageBean = new PageBean(Integer.parseInt(page),
					Integer.parseInt(PropertiesUtil.getValue("pageSize")));
			List<Comment> commentBackList = commentDao.commentList(conn, new Comment(), pageBean, startCommentDate,
					endCommentDate);
			// 分页
			int totalNum = commentDao.commentCount(conn, new Comment(), startCommentDate, endCommentDate);
			String pageCode = PageUtil.getPagation(req.getContextPath() + "/comment?action=backList", totalNum,
					Integer.parseInt(page), Integer.parseInt(PropertiesUtil.getValue("pageSize")));
			req.setAttribute("commentBackList", commentBackList);
			req.setAttribute("pageCode", pageCode);
			req.setAttribute("navCode", NavUtil.genNewsManageNav("新闻评论管理", "新闻评论维护"));
			req.setAttribute("mainPage", "/background/comment/commentList.jsp");
			req.getRequestDispatcher("/background/mainTemp.jsp").forward(req, resp);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				dbUtil.closeConn(conn);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private void commentSave(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Connection conn = null;
		try {
			conn = dbUtil.getConn();
			String content = req.getParameter("content");
			// 获取请求的IP地址
			String userIP = req.getRemoteAddr();
			String newsId = req.getParameter("newsId");
			Comment comment = new Comment(Integer.parseInt(newsId), content, userIP);
			commentDao.commentAdd(conn, comment);
			// 直接进行跳转
			req.getRequestDispatcher("news?action=show&newsId=" + newsId).forward(req, resp);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				dbUtil.closeConn(conn);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
