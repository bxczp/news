<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link href="${pageContext.request.contextPath}/style/news.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/bootstrap/css/bootstrap-responsive.css" rel="stylesheet">
<script src="${pageContext.request.contextPath}/bootstrap/js/jQuery.js"></script>
<script src="${pageContext.request.contextPath}/js/My97DatePicker/WdatePicker.js"></script>
<script src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.js"></script>

<%
	String mainPage = (String) request.getAttribute("mainPage");
	if (mainPage == null || "".equals(mainPage)) {
		mainPage = "/background/default.jsp";
	}
%>
<script type="text/javascript">
	function refreshSystem ()
    {
	    // 		第一个参数是请求的地址，第二个是参数
	    $.post ("${pageContext.request.contextPath}/init", {}, function (flag)
	    {
		    var flag = eval ('(' + flag + ')');
		    if (flag)
		    {
			    alert ("刷新缓存成功")
		    }
		    else
		    {
			    alert ("刷新缓存失败")
		    }
	    });
    }
</script>

</head>

<body>
	<!-- 使用Bootstrap3的布局 -->
	<div class="container">
		<jsp:include page="/background/common/head.jsp" />

		<div class="row-fluid">
			<div class="span3">
				<div class="newsMenu">
					<ul class="nav nav-tabs nav-stacked">
						<li>
							<a href="${pageContext.request.contextPath}/background/mainTemp.jsp">
								<strong>主页</strong>
							</a>
						</li>
						<li>
							<a href="#">
								<strong>新闻管理</strong>
							</a>
						</li>
						<li>
							<a href="${pageContext.request.contextPath}/news?action=preSave">&nbsp;&nbsp;新闻添加</a>
						</li>
						<li>
							<a href="${pageContext.request.contextPath}/news?action=backList">&nbsp;&nbsp;新闻维护</a>
						</li>
						<li>
							<a href="">
								<strong>新闻评论管理</strong>
							</a>
						</li>
						<li>
							<a href="${pageContext.request.contextPath}/comment?action=backList">&nbsp;&nbsp;新闻评论维护</a>
						</li>
						<li>
							<a href="">
								<strong>新闻类别管理</strong>
							</a>
						</li>
						<li>
							<a href="${pageContext.request.contextPath}/newsType?action=preSave">&nbsp;&nbsp;新闻类别添加</a>
						</li>
						<li>
							<a href="${pageContext.request.contextPath}/newsType?action=backList">&nbsp;&nbsp;新闻类别维护</a>
						</li>
						<li>
							<a href="">
								<strong>友情连接管理</strong>
							</a>
						</li>
						<li>
							<a href="${pageContext.request.contextPath}/link?action=preSave">&nbsp;&nbsp;友情连接添加</a>
						</li>
						<li>
							<a href="${pageContext.request.contextPath}/link?action=backList">&nbsp;&nbsp;友情连接维护</a>
						</li>
						<li>
							<a href="">
								<strong>系统管理</strong>
							</a>
						</li>
						<li>
							<a href="javascript:refreshSystem()" onclick="refreshSystem()">&nbsp;&nbsp;刷新服务器缓存</a>
						</li>
					</ul>
				</div>
			</div>
			<div class="span9">
				<%--<jsp:include page=""></jsp:include> 仅仅导入页面的body内容，页面的编译指令不会被导入 --%>
				<jsp:include page="${mainPage }"></jsp:include>
			</div>
		</div>

		<jsp:include page="/background/common/foot.jsp" />
	</div>
</body>


</html>