<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="${pageContext.request.contextPath}/style/news.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/bootstrap/css/bootstrap-responsive.css" rel="stylesheet">
<script src="${pageContext.request.contextPath}/bootstrap/js/jQuery.js"></script>
<script src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript">
	function checkForm ()
    {
	    var typeName = document.getElementById ("typeName").value;
	    if (typeName == null || typeName == "")
	    {
		    document.getElementById ("error").innerHTML = "新闻类别名称不能为空！";
		    return false;
	    }
	    else
	    {
		    return true;
	    }
    }
</script>

<title>Insert title here</title>
</head>
<body>
	<div class="data_list backMain">
		<div class="dataHeader navi">${navCode}</div>

		<div class="data_content">
			<form action="newsType?action=save" method="post" onsubmit="return checkForm()">
				<!-- 				cellpadding单元格之间的填充距离 -->
				<table cellpadding="5">
					<tr>
						<td><label>新闻类别名称：</label></td>
						<td><input type="text" id="typeName" name="typeName" value="${newsType.typeName }" /></td>
					</tr>

					<tr>
						<td><input type="hidden" id="newsTypeId" name="newsTypeId" value="${newsType.newsTypeId }" /></td>
						<td><input class="btn btn-primary" type="submit" value="新闻类别提交" /> &nbsp;&nbsp;<input class="btn btn-primary"
								type="button" onclick="javascript:history.back()" value="返回" />&nbsp;&nbsp; <font color="red" id="error"></font></td>
					</tr>


				</table>
			</form>
		</div>
	</div>
</body>
</html>