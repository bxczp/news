package Web;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import Dao.CommentDao;
import Dao.NewsDao;
import Dao.NewsTypeDao;
import Model.Comment;
import Model.News;
import Model.NewsType;
import Model.PageBean;
import Util.DateUtil;
import Util.DbUtil;
import Util.NavUtil;
import Util.PageUtil;
import Util.PropertiesUtil;
import Util.ResponseUtil;
import Util.StringUtil;
import net.sf.json.JSONObject;

/**
 * @date 2016年3月1日 NewsServlet.java
 * @author CZP
 * @parameter
 */
public class NewsServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DbUtil dbUtil = new DbUtil();
	private NewsDao newsDao = new NewsDao();
	private NewsTypeDao newsTypeDao = new NewsTypeDao();
	private CommentDao commentDao = new CommentDao();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");
		if ("list".equals(action)) {
			this.newsList(req, resp);
		} else if ("show".equals(action)) {
			this.newsShow(req, resp);
		} else if ("preSave".equals(action)) {
			this.newsPreSave(req, resp);
		} else if ("save".equals(action)) {
			this.newsSave(req, resp);
		} else if ("backList".equals(action)) {
			this.newsBackList(req, resp);
		} else if ("delete".equals(action)) {
			this.newsDelete(req, resp);
		}
	}

	private void newsDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Connection conn = null;
		req.setCharacterEncoding("UTF-8");
		String newsId = req.getParameter("newsId");
		try {
			conn = dbUtil.getConn();
			boolean delFlag;
			int delNum = newsDao.newsDelete(conn, newsId);
			JSONObject result = new JSONObject();
			if (delNum > 0) {
				delFlag = true;
			} else {
				delFlag = false;
			}
			ResponseUtil.write(delFlag, resp);
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

	private void newsBackList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Connection conn = null;
		HttpSession session = req.getSession();
		String title = req.getParameter("title");
		String startPublishDate = req.getParameter("startPublishDate");
		String endPublishDate = req.getParameter("endPublishDate");
		// 当前页数
		String page = req.getParameter("page");
		if (StringUtil.isEmpty(page)) {
			page = "1";
			session.setAttribute("startPublishDate", startPublishDate);
			session.setAttribute("endPublishDate", endPublishDate);
			session.setAttribute("title", title);
		} else {
			endPublishDate = (String) session.getAttribute("endPublishDate");
			startPublishDate = (String) session.getAttribute("startPublishDate");
			title = (String) session.getAttribute("title");
		}
		try {
			conn = dbUtil.getConn();
			PageBean pageBean = new PageBean(Integer.parseInt(page),
					Integer.parseInt(PropertiesUtil.getValue("pageSize")));
			News news = new News();
			if (StringUtil.isNotEmpty(title)) {
				news.setTitle(title);
			}
			List<News> newsBackList = newsDao.newsList(conn, news, pageBean, startPublishDate, endPublishDate);
			// 分页
			int totalNum = newsDao.newsCount(conn, news, startPublishDate, endPublishDate);
			String pageCode = PageUtil.getPagation(req.getContextPath() + "/news?action=backList", totalNum,
					Integer.parseInt(page), Integer.parseInt(PropertiesUtil.getValue("pageSize")));
			req.setAttribute("newsBackList", newsBackList);
			req.setAttribute("pageCode", pageCode);
			req.setAttribute("navCode", NavUtil.genNewsManageNav("新闻管理", "新闻维护"));
			req.setAttribute("mainPage", "/background/news/newsList.jsp");
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

	private void newsSave(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		FileItemFactory factory = new DiskFileItemFactory();
		// 使用tomcat自带的fileUpload组件
		ServletFileUpload upload = new ServletFileUpload(factory);
		req.setCharacterEncoding("UTF-8");
		News news = new News();
		List<FileItem> items = null;
		try {
			// 获取上传的所有字段
			items = upload.parseRequest(req);
			Iterator<FileItem> itr = items.iterator();
			while (itr.hasNext()) {
				FileItem item = (FileItem) itr.next();
				if (item.isFormField()) {
					// 表单字段
					String fieldName = item.getFieldName();
					if ("newsId".equals(fieldName)) {
						if (StringUtil.isNotEmpty(item.getString("UTF-8"))) {
							news.setNewsId(Integer.parseInt(item.getString("UTF-8")));
						}
					}
					if ("title".equals(fieldName)) {
						news.setTitle(item.getString("UTF-8"));
					}
					// 不能写成"".equals(news.getImageName())
					if ("imageName".equals(fieldName) && news.getImageName() == null) {
						if (StringUtil.isNotEmpty(item.getString("UTF-8"))) {
							news.setImageName(item.getString("UTF-8").split("/")[1]);
						}
					}
					if ("content".equals(fieldName)) {
						news.setContent(item.getString("UTF-8"));
					}
					if ("author".equals(fieldName)) {
						news.setAuthor(item.getString("UTF-8"));
					}
					if ("typeId".equals(fieldName)) {
						news.setTypeId(Integer.parseInt(item.getString("UTF-8")));
					}
					if ("isHead".equals(fieldName)) {
						news.setIsHead(Integer.parseInt(item.getString("UTF-8")));
					}
					if ("isHot".equals(fieldName)) {
						news.setIsHot(Integer.parseInt(item.getString("UTF-8")));
					}
					if ("isImage".equals(fieldName)) {
						news.setIsImage(Integer.parseInt(item.getString("UTF-8")));
					}
				} else if (!"".equals(item.getName())) {
					try {
						String imageName = DateUtil.getCurrentDateStr();
						// 实际写入图片的位置(在电脑上的位置路径)
						String filePath = PropertiesUtil.getValue("imagePath") + imageName + "."
								+ item.getName().split("\\.")[1];
						// 设置文件名
						news.setImageName(imageName + "." + item.getName().split("\\.")[1]);
						item.write(new File(filePath));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
		}

		Connection conn = null;
		try {
			conn = dbUtil.getConn();
			if (news.getNewsId() != 0) {
				newsDao.newsUpdate(conn, news);
			} else {
				newsDao.newsAdd(conn, news);
			}
			req.getRequestDispatcher("/news?action=backList").forward(req, resp);
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

	private void newsPreSave(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		Connection conn = null;
		req.setCharacterEncoding("UTF-8");
		String newsId = req.getParameter("newsId");
		try {
			conn = dbUtil.getConn();
			if (StringUtil.isNotEmpty(newsId)) {
				// newsId不为空 是修改操作
				News news = newsDao.getNewsById(conn, newsId);
				req.setAttribute("news", news);
			}
			List<NewsType> newsTypeList = newsTypeDao.newsTypeList(conn);
			if (StringUtil.isNotEmpty(newsId)) {
				req.setAttribute("navCode", NavUtil.genNewsManageNav("新闻管理", "新闻修改"));
			} else {
				req.setAttribute("navCode", NavUtil.genNewsManageNav("新闻管理", "新闻添加"));
			}
			req.setAttribute("newsTypeList", newsTypeList);
			req.setAttribute("mainPage", "/background/news/newsAve.jsp");
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

	private void newsList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String typeId = req.getParameter("typeId");
		Connection conn = null;
		String page = req.getParameter("page");
		if (StringUtil.isEmpty(page)) {// 开始时没有page
			page = "1";
		}
		News news = new News();
		if (StringUtil.isNotEmpty(typeId)) {
			news.setTypeId(Integer.parseInt(typeId));
		}
		try {
			conn = dbUtil.getConn();
			PageBean pageBean = new PageBean(Integer.parseInt(page),
					Integer.parseInt(PropertiesUtil.getValue("pageSize")));
			List<News> newNewsListWithType = newsDao.newsList(conn, news, pageBean, null, null);
			NewsType newsType = newsTypeDao.getNewsTypeById(conn, typeId);
			String navCode = NavUtil.genNewsListNav(newsType.getTypeName(), String.valueOf(newsType.getNewsTypeId()));
			int totalNum = newsDao.newsCount(conn, news, null, null);
			String pageCode = PageUtil.getUpAndDownPage(totalNum, Integer.parseInt(page),
					Integer.parseInt(PropertiesUtil.getValue("pageSize")), typeId);
			req.setAttribute("navCode", navCode);
			req.setAttribute("pageCode", pageCode);
			req.setAttribute("newNewsListWithType", newNewsListWithType);
			req.setAttribute("mainPage", "news/newsList.jsp");
			req.getRequestDispatcher("foreground/newsTemp.jsp").forward(req, resp);
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

	private void newsShow(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String newsId = req.getParameter("newsId");
		Connection conn = null;
		try {
			conn = dbUtil.getConn();
			newsDao.newsClick(conn, newsId);
			News news = newsDao.getNewsById(conn, newsId);
			String navCode = NavUtil.genNewsNav(news.getTypeName(), String.valueOf(news.getTypeId()), news.getTitle());
			Comment s_comment = new Comment();
			s_comment.setNewsId(Integer.parseInt(newsId));
			List<Comment> commentList = commentDao.commentList(conn, s_comment, null, null, null);

			req.setAttribute("commentList", commentList);
			req.setAttribute("pageCode", this.genUpAndDownPageCode(newsDao.getUpAndDownPageID(conn, newsId)));
			req.setAttribute("news", news);
			req.setAttribute("navCode", navCode);
			req.setAttribute("mainPage", "news/newsShow.jsp");
			req.getRequestDispatcher("foreground/newsTemp.jsp").forward(req, resp);
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

	private String genUpAndDownPageCode(List<News> upAndDownPage) {
		News upNews = upAndDownPage.get(0);
		News downNews = upAndDownPage.get(1);
		StringBuffer sb = new StringBuffer();
		if (upNews.getNewsId() == -1) {
			sb.append("<p>上一篇没有了</p>");
		} else {
			sb.append("<p>上一篇：<a href='news?action=show&newsId=" + upNews.getNewsId() + "'>" + ">>" + upNews.getTitle()
					+ "</a></p>");
		}
		if (downNews.getNewsId() == -1) {
			sb.append("<p>下一篇没有了</p>");
		} else {
			sb.append("<p>下一篇：<a href='news?action=show&newsId=" + downNews.getNewsId() + "'>" + ">>"
					+ downNews.getTitle() + "</a></p>");
		}
		return sb.toString();
	}

}
