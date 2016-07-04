<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<!-- 引入ckeditor组件 -->
<script
	src="${pageContext.request.contextPath}/js/ckeditor/ckeditor.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<!-- 使用ckeditor的class即可使用ckeditor -->
<!-- 需要现在ckeditor的config.js中配置允许上传图片 -->
<textarea id="content" name="content" class="ckeditor">

</textarea>
</body>
</html>