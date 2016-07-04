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
// 使用JQuery操纵dom节点
//当文档加载完毕后执行
$(document).ready(function(){
	$('#checkedAll').click(function(){
// 		$(this)是对当前dom对象的JQuery封装 this是指的是当前的dom对象
		if($(this).prop("checked")==true){
			$("input[name='commentIds']").prop("checked",true);
		}else{
			$("input[name='commentIds']").prop("checked",false);
		}
	})
})


function commentDelete(commentId) {
	if(confirm("确认删除评论？")){
		$.post("comment?action=delete",{commentIds:commentId},
				function(result){
			var result=eval("("+result+")");
			if(result.success){
				alert("删除成功！");
				window.location.href='${pageContext.request.contextPath}/comment?action=backList';
			}else{
				alert("result.errorMsg");
			}
		}
	);
	}
}

function commentsDelete() {
	//定义数组
	var chk_value=[];
	//指定name 和 属性的标签的值
	$("input[name='commentIds']:checked").each(function(){
		chk_value.push($(this).val());
	});
	
	if(chk_value.length==0){
		alert("请选择要删除的评论");
		return;
	}
	//在每个id之间加 , 
	var commentIds=chk_value.join(",");
	if(confirm("确认删除评论？")){
		$.post("comment?action=delete",{commentIds:commentIds},
				function(result){
			var result=eval("("+result+")");
			if(result.success){
				alert("成功删除"+result.delNum+"条评论");
				window.location.href='${pageContext.request.contextPath}/comment?action=backList';
			}else{
				alert("result.errorMsg");
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
			<div style="float: left; padding-top: 20px;">
				<button class="btn btn-mini btn-danger" onclick="javascript:commentsDelete()">批量删除</button>
			</div>
			<!-- 对日期的封装 -->
			<div style="float: right">
				<form action="${pageContext.request.contextPath}/comment?action=backList" method="post">
					评论日期：
					<input type="text" id="startCommentDate" style="width: 100px;" name="startCommentDate" class="Wdate" onclick="WdatePicker()"
						value="${startCommentDate }">
					&nbsp;到&nbsp;
					<input type="text" id="endCommentDate" name="endCommentDate" style="width: 100px;" value="${endCommentDate }" class="Wdate"
						onclick="WdatePicker()">
					&nbsp;&nbsp;
					<button class="btn btn-mini btn-primary" type="submit" style="margin-top: -8px;">查找</button>
				</form>
			</div>
		</div>
		<div class="data_content ">
			<table class="table table-hover table-bordered">
				<tr>
					<!-- 表格标题 -->
					<th><input type="checkbox" id="checkedAll" name="cheackedAll" /></th>
					<th>序号</th>
					<th>评论内容</th>
					<th>评论主题</th>
					<th>评论时间</th>
					<th>操作</th>
				</tr>
				<c:forEach var="commentBack" items="${commentBackList}" varStatus="status">
					<tr>
						<td><input type="checkbox" id="commentIds" name="commentIds" value="${commentBack.commentId }" /></td>
						<td>${status.index+1}</td>
						<!-- 截取评论的内容 -->
						<td><a title="${commentBack.content}">${  fn:substring(commentBack.content,0,10)  }...</a></td>
						<td><a title="${commentBack.newsTitle}" target="blank" href="news?action=show&newsId=${commentBack.newsId }">${  fn:substring(commentBack.newsTitle,0,15)  }...</a></td>
						<td><fmt:formatDate value="${commentBack.commentDate}" type="date" pattern="yyyy-MM-dd" /></td>
						<td>
							<button class="btn btn-mini btn-danger" onclick="javascript:commentDelete(${commentBack.commentId })">删除</button>
						</td>
					</tr>
				</c:forEach>

			</table>

		</div>
		<div class="pagination pagination-centered">${pageCode }</div>


	</div>
</body>
</html>