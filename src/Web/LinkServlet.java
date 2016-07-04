package Web;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Dao.CommentDao;
import Dao.LinkDao;
import Dao.NewsDao;
import Dao.NewsTypeDao;
import Model.Link;
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
public class LinkServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DbUtil dbUtil = new DbUtil();
	private LinkDao linkDao = new LinkDao();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");
		if ("preSave".equals(action)) {
			this.linkPreSave(req, resp);
		} else if ("save".equals(action)) {
			this.linkSave(req, resp);
		} else if ("backList".equals(action)) {
			this.linkBackList(req, resp);
		} else if ("delete".equals(action)) {
			this.linkDelete(req, resp);
		}
	}

	private void linkPreSave(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		// 更新操作会带有linkId 而保存操作不带linkId
		String linkId = req.getParameter("linkId");
		Connection conn = null;
		try {
			conn = dbUtil.getConn();
			if (StringUtil.isNotEmpty(linkId)) {
				Link link = linkDao.getLinkById(conn, linkId);
				req.setAttribute("link", link);
			}
			if (StringUtil.isNotEmpty(linkId)) {
				req.setAttribute("navCode", NavUtil.genNewsManageNav("友情链接管理", "友情链接修改"));
			} else {
				req.setAttribute("navCode", NavUtil.genNewsManageNav("友情链接管理", "友情链接添加"));
			}
			req.setAttribute("mainPage", "/background/link/linkAve.jsp");
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

	private void linkSave(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Connection conn = null;
		req.setCharacterEncoding("UTF-8");
		String linkId = req.getParameter("linkId");
		String linkName = req.getParameter("linkName");
		String linkUrl = req.getParameter("linkUrl");
		String linkEmail = req.getParameter("linkEmail");
		String orderNum = req.getParameter("orderNum");
		Link link = new Link();
		link.setLinkEmail(linkEmail);
		link.setLinkName(linkName);
		link.setLinkUrl(linkUrl);
		link.setOrderNum(Integer.parseInt(orderNum));
		if (StringUtil.isNotEmpty(linkId)) {
			// 更新操作
			link.setLinkId(Integer.parseInt(linkId));
		}
		try {
			conn = dbUtil.getConn();
			if (StringUtil.isNotEmpty(linkId)) {
				linkDao.linkUpdate(conn, link);
			} else {
				linkDao.linkAdd(conn, link);
			}
			req.getRequestDispatcher("/link?action=backList").forward(req, resp);
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

	private void linkDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Connection conn = null;
		req.setCharacterEncoding("UTF-8");
		String linkId = req.getParameter("linkId");
		try {
			conn = dbUtil.getConn();
			// 定义一个jSon对象
			JSONObject result = new JSONObject();
			int delNum = linkDao.linkDelete(conn, linkId);
			if (delNum > 0) {
				result.put("success", true);
			} else {
				result.put("errorMsg", "删除失败");
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
	private void linkBackList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Connection conn = null;
		req.setCharacterEncoding("UTF-8");
		try {
			conn = dbUtil.getConn();
			List<Link> linkBackList = linkDao.linkList(conn, "select * from t_link order by orderNum desc");
			req.setAttribute("linkBackList", linkBackList);
			req.setAttribute("navCode", NavUtil.genNewsManageNav("友情链接管理", "友情链接维护"));
			req.setAttribute("mainPage", "/background/link/linkList.jsp");
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
