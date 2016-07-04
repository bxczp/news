package Util;

/**
 * @date 2016年3月1日 PageUtil.java
 * @author CZP
 * @parameter
 */
public class PageUtil {
	// totalNum是总记录数 currentPage是当前页 pageSize是每页的记录数
	/**
	 * 获取上下页
	 * @param totalNum
	 * @param currentPage
	 * @param pageSize
	 * @param typeId
	 * @return
	 */
	public static String getUpAndDownPage(int totalNum, int currentPage, int pageSize, String typeId) {
		StringBuffer pageCode = new StringBuffer();
		// 总页数
		int totalPage = totalNum % pageSize == 0 ? totalNum / pageSize : totalNum / pageSize + 1;
		pageCode.append("<ul class='pager'>");
		if (currentPage == 1) {
			pageCode.append("<li class='disabled'><a href='#'>上一页</a></li>");
		} else {
			pageCode.append(
					"<li><a href='news?action=list&typeId=" + typeId + "&page=" + (currentPage - 1) + "'>上一页</a></li>");

		}
		pageCode.append("&nbsp;&nbsp;");
		if (currentPage == totalPage) {
			pageCode.append("<li class='disabled'><a href='#'>下一页</a></li>");

		} else {
			pageCode.append(
					"<li><a href='news?action=list&typeId=" + typeId + "&page=" + (currentPage + 1) + "'>下一页</a></li>");
		}

		pageCode.append("</ul>");
		return pageCode.toString();
	}

	public static String getPagation(String targetUrl, int totalNum, int currentPage, int pageSize) {
		int totalPage = totalNum % pageSize == 0 ? totalNum / pageSize : totalNum / pageSize + 1;
		StringBuffer pageCode = new StringBuffer();
		pageCode.append("<ul class='pager'>");
		pageCode.append("<li><a href='" + targetUrl + "&page=1'>首页</a></li>");
		if (currentPage == 1) {
			pageCode.append("<li class='disabled'><a href='#'>上一页</a></li>");
		} else {
			pageCode.append("<li><a href='" + targetUrl + "&page=" + (currentPage - 1) + "'>上一页</a></li>");
		}
		pageCode.append("&nbsp;&nbsp;");
		for (int i = currentPage - 2; i < currentPage + 2; i++) {
			if (i < 1 || i > totalPage) {
				continue;
			}
			if (i == currentPage) {
				pageCode.append("<li class='active'><a href='#'>" + i + "</a></li>");
			} else {
				pageCode.append("<li><a href='" + targetUrl + "&page=" + (i) + "'>" + i + "</a></li>");
			}
		}
		if (currentPage == totalPage) {
			pageCode.append("<li class='disabled'><a href='#'>下一页</a></li>");
		} else {
			pageCode.append("<li><a href='" + targetUrl + "&page=" + (currentPage + 1) + "'>下一页</a></li>");
		}
		pageCode.append("<li><a href='" + targetUrl + "&page=" + totalPage + "'>尾页</a></li>");
		pageCode.append("</ul>");
		return pageCode.toString();
	}

}
