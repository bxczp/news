<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<div class="data_list">
		<div class="dataHeader navi">${navCode}</div>
		<div class="datas news_type_list">
			<ul>
				<c:forEach var="newsWithType" items="${newNewsListWithType }">
					<li><span><fmt:formatDate
								value="${newsWithType.publishDate }" type="date"
								pattern="yyyy-MM-dd" /></span>&nbsp;&nbsp;<a
						href="news?action=show&newsId=${newsWithType.newsId }"
						title="${newsWithType.title }">
							${fn:substring(newsWithType.title,0,42)} </a></li>
				</c:forEach>
			</ul>
		</div>
		<div>${pageCode}</div>
	</div>
</body>
</html>