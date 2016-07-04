<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!--被include的jsp页面-->

<div class="row-fluid">
	<div class="span12">
		<img alt="网站标识"
			src="${pageContext.request.contextPath}/images/logo.png">
	</div>

	<div class="row-fluid">
		<div class="span12">
			<div class="navbar">
				<div class="navbar navbar-inverse">
					<a class="brand" href="goIndex">首页</a>
					<ul class="nav nav-pills">
						<c:forEach var="newsType" items="${newsTypeList }">
								<li><a href="news?action=list&typeId=${newsType.newsTypeId }">${newsType.typeName }</a></li>
						</c:forEach>
					</ul>
				</div>
			</div>
		</div>
	</div>


</div>