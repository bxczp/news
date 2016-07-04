package Web;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Dao.LinkDao;
import Dao.NewsDao;
import Dao.NewsTypeDao;
import Model.Link;
import Model.NewsType;
import Util.DbUtil;
import Util.NavUtil;
import Util.ResponseUtil;
import Util.StringUtil;
import net.sf.json.JSONObject;

/**
 * @date 2016年3月2日 LinkServlet.java
 * @author CZP
 * @parameter
 */
public class NewsTypeServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DbUtil dbUtil = new DbUtil();
	private NewsTypeDao newsTypeDao = new NewsTypeDao();
	private NewsDao newsDao = new NewsDao();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");
		if ("preSave".equals(action)) {
			this.newsTypePreSave(req, resp);
		} else if ("save".equals(action)) {
			this.newsTypeSave(req, resp);
		} else if ("backList".equals(action)) {
			this.newsTypeBackList(req, resp);
		} else if ("delete".equals(action)) {
			this.newsTypeDelete(req, resp);
		}
	}

	private void newsTypePreSave(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		// 更新操作会带有linkId 而保存操作不带linkId
		String newsTypeId = req.getParameter("newsTypeId");
		Connection conn = null;
		try {
			conn = dbUtil.getConn();
			if (StringUtil.isNotEmpty(newsTypeId)) {
				NewsType newsType = newsTypeDao.getNewsTypeById(conn, newsTypeId);
				req.setAttribute("newsType", newsType);
			}
			if (StringUtil.isNotEmpty(newsTypeId)) {
				req.setAttribute("navCode", NavUtil.genNewsManageNav("新闻类别管理", "新闻类别修改"));
			} else {
				req.setAttribute("navCode", NavUtil.genNewsManageNav("新闻类别管理", "新闻类别添加"));
			}
			req.setAttribute("mainPage", "/background/newsType/newsTypeAve.jsp");
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

	private void newsTypeSave(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Connection conn = null;
		req.setCharacterEncoding("UTF-8");
		String newsTypeId = req.getParameter("newsTypeId");
		String typeName = req.getParameter("typeName");
		NewsType newsType = new NewsType();
		newsType.setTypeName(typeName);
		if (StringUtil.isNotEmpty(newsTypeId)) {
			// 更新操作
			newsType.setNewsTypeId(Integer.parseInt(newsTypeId));
		}
		try {
			conn = dbUtil.getConn();
			if (StringUtil.isNotEmpty(newsTypeId)) {
				newsTypeDao.newsTypeUpdate(conn, newsType);
			} else {
				newsTypeDao.newsTypeAdd(conn, newsType);
			}
			req.getRequestDispatcher("/newsType?action=backList").forward(req, resp);
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

	private void newsTypeDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Connection conn = null;
		req.setCharacterEncoding("UTF-8");
		String newsTypeId = req.getParameter("newsTypeId");
		try {
			conn = dbUtil.getConn();
			// 定义一个jSon对象
			JSONObject result = new JSONObject();
			// 删除前要判断该新闻类别下 是否有 新闻存在
			boolean existNews = newsDao.existNewsWithNewsTypeId(conn, newsTypeId);
			if (existNews) {
				result.put("errorMsg", "该类别下存在新闻，不能删除该类别");
			} else {
				int delNum = newsTypeDao.newsTypeDelete(conn, newsTypeId);
				if (delNum > 0) {
					result.put("success", true);
				} else {
					result.put("errorMsg", "删除失败");
				}
			}
			// 直接向页面输出
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

	// 查询所有的link
	private void newsTypeBackList(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		Connection conn = null;
		try {
			conn = dbUtil.getConn();
			List<NewsType> newsTypeBackList = newsTypeDao.newsTypeList(conn);
			req.setAttribute("newsTypeBackList", newsTypeBackList);
			req.setAttribute("navCode", NavUtil.genNewsManageNav("新闻类别管理", "新闻类别维护"));
			req.setAttribute("mainPage", "/background/newsType/newsTypeList.jsp");
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

}
