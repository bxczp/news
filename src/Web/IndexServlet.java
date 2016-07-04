package Web;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Dao.LinkDao;
import Dao.NewsDao;
import Dao.NewsTypeDao;
import Model.Link;
import Model.News;
import Model.NewsType;
import Util.DbUtil;
import Util.StringUtil;

/**
 * @date 2016年2月29日 IndexServlet.java
 * @author CZP
 * @parameter
 */
public class IndexServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DbUtil dbUtil = new DbUtil();
	private NewsDao newsDao = new NewsDao();
	private NewsTypeDao newsTypeDao = new NewsTypeDao();
	private LinkDao linkDao = new LinkDao();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		Connection conn = null;
		try {
			conn = dbUtil.getConn();
			List<NewsType> newsTypeList = newsTypeDao.newsTypeList(conn);
			req.setAttribute("newsTypeList", newsTypeList);
			// 查找图片新闻(分页查找)
			String sql = "select * from t_news where isImage =1 order by publishDate desc limit 0,5";
			List<News> imageNewsList = newsDao.newsList(conn, sql);
			req.setAttribute("imageNewsList", imageNewsList);
			// 查找头条新闻(分页查找)
			sql = "select * from t_news where isHead=1 order by publishDate desc limit 0,1";
			List<News> headNewsList = newsDao.newsList(conn, sql);
			News headNews = headNewsList.get(0);
			headNews.setContent(StringUtil.Html2Text(headNews.getContent()));
			req.setAttribute("headNews", headNews);
			req.setAttribute("headNewsList", headNewsList);
			// 查找热点新闻(分页查找)
			sql = "select * from t_news where isHot=1 order by publishDate desc limit 0,8";
			List<News> hotNewsList = newsDao.newsList(conn, sql);
			req.setAttribute("hotNewsList", hotNewsList);

			// 取出每一种新闻和该种新闻下的前8个新闻
			List<Object> allIndexNewsList = new ArrayList<>();
			if (newsTypeList != null && newsTypeList.size() != 0) {
				for (int i = 0; i < newsTypeList.size(); i++) {
					// 取出所有的新闻类别
					NewsType newsType = newsTypeList.get(i);
					sql = "select * from t_news,t_newsType where typeId=newsTypeId and typeId ="
							+ newsType.getNewsTypeId() + " order by publishDate desc limit 0,8";
					List<News> oneSubList = newsDao.newsList(conn, sql);
					allIndexNewsList.add(oneSubList);
				}
			}
			req.setAttribute("allIndexNewsList", allIndexNewsList);

			sql = "select * from t_link order by orderNum ";
			List<Link> linkList = linkDao.linkList(conn, sql);
			req.setAttribute("linkList", linkList);

			req.getRequestDispatcher("index.jsp").forward(req, resp);

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
