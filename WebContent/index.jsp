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
<script src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.js"></script>
</head>
<body>
	<div class="container">
		<jsp:include page="/foreground/common/head.jsp" />

		<div class="row-fluid">
			<div class="span9">
				<div>
					<DIV style="width: 330px; height: 228px;" class="tuhuo">

						<A href="" target="_blank">
							<IMG style="width: 330px; height: 208px;" id="fou_img" src="">
						</A>
						<!-- 图片新闻的代码 -->
						<c:forEach var="imageNews" items="${imageNewsList }">
							<A href="news?action=show&newsId=${imageNews.newsId }">
								<IMG style="display: none;" class="tu_img" src="${imageNews.imageName }" width="330" height="208" />
							</A>
							<P style="height: 20px;" class="tc">
								<A href="news?action=show&newsId=${imageNews.newsId }" target="_blank"
									<%-- ${fn:substring()  }使用jstl中的函数 字符串的截取 --%>
									title="${imageNews.title }">${fn:substring(imageNews.title,0,18) }</A>
							</P>
						</c:forEach>


						<!-- 图片新闻的图片幻灯片 -->
						<UL>
							<LI class="fouce">1</LI>
							<LI>2</LI>
							<LI>3</LI>
							<LI>4</LI>
							<LI>5</LI>
						</UL>
					</DIV>

				</div>
				<div class="newsHeader_list">
					<div class="newsHeader">
						<h3>
							<a href="news?action=show&newsId=${headNews.newsId }" title="${headNews.title }">${fn:substring(headNews.title,0,18)}</a>
						</h3>
						<p>
							${fn:substring(headNews.content,0,50)}...
							<a href="news?action=show&newsId=${headNews.newsId }">[查看全文]</a>
						</p>
					</div>
					<div class="currentUpdate">
						<div class="currentUpdateHeader">最近更新</div>
						<div class="currentUpdateDatas">
							<table width="100%">
								<!-- 	遍历的索引 varstatus -->
								<c:forEach var="newNews" items="${ newNewsList}" varStatus="status">
									<!-- 一行两列 -->
									<c:if test="${status.index%2==0 }">
										<tr>
									</c:if>
									<td width="50%"><a href="news?action=show&newsId=${newNews.newsId }" title="${fn:substring(newNews.title,0,8) }">${fn:substring(newNews.title,0,10) }</a></td>
									<c:if test="${status.index%2==1 }">
										</tr>
									</c:if>
								</c:forEach>
							</table>
						</div>
					</div>
				</div>
			</div>
			<div class="span3">
				<div class="data_list hotspot_news_list">
					<div class="dataHeader">热点新闻</div>
					<div class="datas">
						<ul>
							<c:forEach var="hotNews" items="${hotNewsList }">
								<li>
									<a href="news?action=show&newsId=${hotNews.newsId }" title="${hotNews.title }"> ${fn:substring(hotNews.title,0,20)} </a>
								</li>
							</c:forEach>
						</ul>
					</div>
				</div>
			</div>
		</div>

		<!-- 两层循环 -->
		<c:forEach var="allIndexNews" items="${allIndexNewsList }" varStatus="allStatus">
			<!-- 	allStatus.index为循环中的索引（从0开始） -->
			<c:if test="${allStatus.index%3==0 }">
				<div class="row-fluid">
			</c:if>
			<c:forEach var="indexNews" items="${allIndexNews }" varStatus="oneStatus">
				<c:if test="${oneStatus.first }">
					<div class="span4">
						<div class="data_list news_list">
							<div class="dataHeader">${newsTypeList.get(allStatus.index).typeName }<span class="more">
									<a href="news?action=list&typeId=${newsTypeList.get(allStatus.index).newsTypeId }">更多...</a>
								</span>
							</div>
							<div class="datas">
								<ul>
				</c:if>
				<li>
					<fmt:formatDate value="${indexNews.publishDate }" type="date" pattern="yyyy-MM-dd" />
					&nbsp;
					<a href="news?action=show&newsId=${indexNews.newsId }" title="${indexNews.title }">${fn:substring(indexNews.title,0,18) }</a>
				</li>
				<c:if test="${oneStatus.last }">
					</ul>
	</div>
	</div>
	</div>
	</c:if>
	</c:forEach>
	<c:if test="${allStatus.index%3==2 || allStatus.last }">
		</div>
	</c:if>
	</c:forEach>



	<jsp:include page="/foreground/common/link.jsp" />
	<jsp:include page="/foreground/common/foot.jsp" />
	<div style="float: right;">
		<a href="javascript:window.location.href='${pageContext.request.contextPath}/background/login.jsp '">后台管理</a>
	</div>
</body>


<!-- 新闻图片 幻灯片 的实现代码 -->
<script type="text/javascript">
	var auto;
    var index = 0;
    $ ('.tuhuo ul li').hover (function ()
    {
	    clearTimeout (auto);
	    index = $ (this).index ();
	    move (index);
    }, function ()
    {
	    auto = setTimeout ('autogo(' + index + ')', 3000);
    });
    
    function autogo ()
    {
	    if (index < 5)
	    {
		    move (index);
		    index++;
	    }
	    else
	    {
		    index = 0;
		    move (index);
		    index++;
	    }
    }
    function move (l)
    {
	    var src = $ ('.tu_img').eq (index).attr ('src');
	    $ ("#fou_img").css (
	    {
		    "opacity" : "0"
	    });
	    $ ('#fou_img').attr ('src', src);
	    $ ('#fou_img').stop (true).animate (
	    {
		    opacity : '1'
	    }, 1000);
	    $ ('.tuhuo ul li').removeClass ('fouce');
	    $ ('.tuhuo ul li').eq (index).addClass ('fouce');
	    $ ('.tuhuo p').hide ();
	    $ ('.tuhuo p').eq (index).show ();
	    var ao = $ ('.tuhuo p').eq (index).children ('a').attr ('href');
	    $ ('#fou_img').parent ('a').attr ("href", ao);
    }
    autogo ();
    setInterval ('autogo()', 3000);
</script>
</html>