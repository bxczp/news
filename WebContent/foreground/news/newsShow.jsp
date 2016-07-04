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
		<div>
			<div class="news_title">
				<h3>${news.title }</h3>
			</div>
			<div class="news_info">
				发布时间：【
				<fmt:formatDate value="${news.publishDate }" type="date"
					pattern="yyyy-MM-dd HH:mm:ss" />
				】 &nbsp;&nbsp;作者：【${news.author }】&nbsp;&nbsp;新闻类别：【${news.typeName}】
				&nbsp;&nbsp;阅读次数：【${news.click}】
			</div>
			<div class="news_content">新闻内容：${news.content }</div>
		</div>

		<div class="upDownPage">${pageCode}</div>
	</div>

	<div class="data_list comment_list">
		<div class="dataHeader">用户评论：</div>
		<c:forEach var="comment" items="${commentList }">
			<div class="commentData">
				<font> 【${comment.userIP }】:</font>${comment.content }&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;评论时间：【<fmt:formatDate
					value="${comment.commentDate }" type="date" pattern="yyyy-MM-dd " />
				】
			</div>
		</c:forEach>

	</div>
	
	<div class="publish_list">
		<form action="comment?action=save" method="post">
			<div cllass="">
				<input type="hidden" value="${news.newsId }" id="newsId" name="newsId"/>
				<textarea style="width: 100%" rows="3" cols="10" id="content" name="content"></textarea>
			</div>
			<div class="publishButton"> 
				<button class="btn btn-primary" type="submit">发表评论</button>
			</div>
		</form>
	</div>
</body>
</html>