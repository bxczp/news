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
	    var linkName = document.getElementById ("linkName").value;
	    var linkUrl = document.getElementById ("linkUrl").value;
	    var linkEmail = document.getElementById ("linkEmail").value;
	    var orderNum = document.getElementById ("orderNum").value;
	    if (linkName == null || linkName == "")
	    {
		    document.getElementById ("error").innerHTML = "链接名称不能为空！";
		    return false;
	    }
	    if (linkUrl == null || linkUrl == "")
	    {
		    document.getElementById ("error").innerHTML = "链接地址不能为空！";
		    return false;
	    }
	    if (linkEmail == null || linkEmail == "")
	    {
		    document.getElementById ("error").innerHTML = "联系人邮件不能为空！";
		    return false;
	    }
	    if (orderNum == null || orderNum == "")
	    {
		    document.getElementById ("error").innerHTML = "排列顺序不能为空！";
		    return false;
	    }
	    else
	    {
		    var type = "^[0-9]*[1-9][0-9]*$";
		    var re = new RegExp (type);
		    if (orderNum.match (re) == null)
		    {
			    alert ("排列顺序必须为正整数!");
			    return false;
		    }
		    return true;
	    }
	    return true;
    }
</script>

<title>Insert title here</title>
</head>
<body>
	<div class="data_list backMain">
		<div class="dataHeader navi">${navCode}</div>

		<div class="data_content">
			<form action="link?action=save" method="post" onsubmit="return checkForm()">
				<!-- 				cellpadding单元格之间的填充距离 -->
				<table cellpadding="5">
					<tr>
						<td><label>连接名称：</label></td>
						<td><input type="text" id="linkName" name="linkName" value="${link.linkName }" /></td>
					</tr>
					<tr>
						<td><label>连接地址：</label></td>
						<td><input type="text" id="linkUrl" name="linkUrl" value="${link.linkUrl }" /></td>
					</tr>
					<tr>
						<td><label>连接邮箱：</label></td>
						<td><input type="text" id="linkEmail" name="linkEmail" value="${link.linkEmail }" /></td>
					</tr>
					<tr>
						<td><label>连接排序：</label></td>
						<td><input type="text" id="orderNum" name="orderNum" value="${link.orderNum }" /></td>
					</tr>

					<tr>
						<td><input type="hidden" id="linkId" name="linkId" value="${link.linkId }" /></td>
						<td><input class="btn btn-primary" type="submit" value="连接提交" /> &nbsp;&nbsp;<input class="btn btn-primary"
								type="button" onclick="javascript:history.back()" value="返回" />&nbsp;&nbsp; <font color="red" id="error"></font></td>
					</tr>

				</table>
			</form>
		</div>
	</div>
</body>
</html>