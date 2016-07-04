package Web;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Dao.UserDao;
import Model.User;
import Util.DbUtil;

/**
 * @date 2016年3月2日 UserServlet.java
 * @author CZP
 * @parameter
 */
public class UserServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DbUtil dbUtil = new DbUtil();
	private UserDao userDao = new UserDao();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");
		if ("login".equals(action)) {
			this.login(req, resp);
		} else if ("logout".equals(action)) {
			this.logOut(req, resp);
		}

	}

	private void login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String userName = req.getParameter("userName");
		String password = req.getParameter("password");
		HttpSession session = req.getSession();
		User user = new User(userName, password);
		Connection conn = null;
		try {
			conn = dbUtil.getConn();
			User currentUser = userDao.login(conn, user);
			if (currentUser == null) {
				req.setAttribute("error", "用户名或密码错误");
				req.setAttribute("userName", userName);
				req.setAttribute("password", password);
				req.getRequestDispatcher("/background/login.jsp").forward(req, resp);
			} else {
				session.setAttribute("currentUser", currentUser);
				req.setAttribute("mainPage", "/background/default.jsp");
				// resp.sendRedirect(req.getContextPath()+"/background/mainTemp.jsp");
				req.getRequestDispatcher("/background/mainTemp.jsp").forward(req, resp);
			}
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

	private void logOut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		// 清除session
		session.invalidate();
		resp.sendRedirect(req.getContextPath() + "/background/login.jsp");
	}

}
