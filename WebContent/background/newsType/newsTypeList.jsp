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
<script src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>Insert title here</title>

<script type="text/javascript">
	function newsTypeDelete(newsTypeId){
		if(confirm("确认删除？")){
// 			使用JQuery
			$.post("newsType?action=delete",{newsTypeId:newsTypeId},
					function(result){
// 				把Json串转换从json对象
					var result=eval('('+result+')');
					if(result.success){
						alert("删除成功");
						window.location.href='${pageContext.request.contextPath}/newsType?action=backList';
					}else{
						alert(result.errorMsg);
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

		<div class="data_content ">
			<table class="table table-hover table-bordered">
				<tr>
					<!-- 				表格标题 -->
					<th>序号</th>
					<th>新闻类别名称</th>
					<th>操作</th>
				</tr>
				<c:forEach var="newsTypeBack" items="${newsTypeBackList}" varStatus="status">
					<tr>
						<td>${status.index+1}</td>
						<td>${newsTypeBack.typeName}</td>
						<td><button class="btn btn-mini btn-info"
								onclick="javascript:window.location='newsType?action=preSave&newsTypeId=${newsTypeBack.newsTypeId}'">修改</button>&nbsp;&nbsp;
							<button class="btn btn-mini btn-danger" onclick="newsTypeDelete(${newsTypeBack.newsTypeId})">删除</button></td>
					</tr>
				</c:forEach>

			</table>

		</div>


	</div>
</body>
</html>