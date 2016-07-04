<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="${pageContext.request.contextPath}/style/news.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/bootstrap/css/bootstrap-responsive.css" rel="stylesheet">
<script src="${pageContext.request.contextPath}/bootstrap/js/jQuery.js"></script>

<!-- 引入时间组件 -->
<script src="${pageContext.request.contextPath}/js/My97DatePicker/WdatePicker.js"></script>
<script src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>Insert title here</title>
<script type="text/javascript">
function newsDelete(newsId){
	if(confirm("确认删除？")){
//			使用JQuery
		$.post("news?action=delete",{newsId:newsId},
				function(delFlag){
//				把Json串转换从json对象
				var result=eval('('+delFlag+')');
				if(result){
					alert("删除成功");
					window.location.href='${pageContext.request.contextPath}/news?action=backList';
				}else{
					alert("删除失败");
				}
				
			}
		);
	}
}
</script>
</head>
<body>
	<div class="data_list backMain">
		<div class="dataHeader navi">${navCode}</div>
		<div class="search_content" style="vertical-align: middle; padding-right: 20px;">
			<!-- 对日期的封装 -->
			<form action="${pageContext.request.contextPath}/news?action=backList" method="post">
				新闻标题：
				<input type="text" id="title" name="title" style="width: 180px;" value="${title }" />
				发布日期：
				<input type="text" id="startPublishDate" style="width: 100px;" name="startPublishDate" class="Wdate" onclick="WdatePicker()"
					value="${startPublishDate }">
				&nbsp;到&nbsp;
				<input type="text" id="endPublishDate" name="endPublishDate" style="width: 100px;" value="${endPublishDate }" class="Wdate"
					onclick="WdatePicker()">
				&nbsp;&nbsp;
				<button class="btn btn-mini btn-primary" type="submit" style="margin-top: -8px;">查找</button>
			</form>
		</div>
		<div class="data_content ">
			<table class="table table-hover table-bordered">
				<tr>
					<!-- 				表格标题 -->
					<th><input type="checkbox" id="checkedAll" name="cheackedAll" /></th>
					<th>序号</th>
					<th>新闻类别</th>
					<th>新闻主题</th>
					<th>发布时间</th>
					<th>操作</th>
				</tr>
				<c:forEach var="newsBack" items="${newsBackList}" varStatus="status">
					<tr>
						<td><input type="checkbox" id="newsIds" name="newsIds" value="${newsBack.newsId }" /></td>
						<td>${status.index+1}</td>
						<td><a title="${newsBack.typeName}">${  fn:substring(newsBack.typeName,0,10)  }...</a></td>
						<td><a title="${newsBack.title}" target="blank" href="news?action=show&newsId=${newsBack.newsId }">${  fn:substring(newsBack.title,0,15)  }...</a></td>
						<td><fmt:formatDate value="${newsBack.publishDate}" type="date" pattern="yyyy-MM-dd" /></td>
						<td>
							<button class="btn btn-mini btn-danger" onclick="newsDelete(${newsBack.newsId })">删除</button>
							<button class="btn btn-mini btn-info" onclick="javascript:window.location='news?action=preSave&newsId=${newsBack.newsId}'">修改</button>
						</td>
					</tr>
				</c:forEach>

			</table>

		</div>
		<div class="pagination pagination-centered">${pageCode }</div>


	</div>
</body>
</html>