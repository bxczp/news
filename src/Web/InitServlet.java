package Web;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Dao.NewsDao;
import Dao.NewsTypeDao;
import Model.News;
import Model.NewsType;
import Util.DbUtil;
import Util.ResponseUtil;
import net.sf.json.JSONObject;

/**
 * @date 2016年3月1日 Servlet.java
 * @author CZP
 * @parameter
 */
public class InitServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DbUtil dbUtil = new DbUtil();
	private NewsDao newsDao = new NewsDao();
	private NewsTypeDao newsTypeDao = new NewsTypeDao();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		ServletContext application = session.getServletContext();
		this.refreshSystem(application);
		JSONObject result = new JSONObject();
		result.put("success", true);
		try {
			ResponseUtil.write(result, resp);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		ServletContext application = config.getServletContext();
		this.refreshSystem(application);
	}

	private void refreshSystem(ServletContext application) {
		Connection conn = null;
		try {
			conn = dbUtil.getConn();
			List<NewsType> newsTypeList = newsTypeDao.newsTypeList(conn);
			application.setAttribute("newsTypeList", newsTypeList);
			String sql = "select * from t_news order by publishDate desc limit 0,8";
			List<News> newNewsList = newsDao.newsList(conn, sql);
			application.setAttribute("newNewsList", newNewsList);
			sql = "select * from t_news order by click desc limit 0,8";
			List<News> hotNewsList = newsDao.newsList(conn, sql);
			application.setAttribute("hotNewsList", hotNewsList);

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
