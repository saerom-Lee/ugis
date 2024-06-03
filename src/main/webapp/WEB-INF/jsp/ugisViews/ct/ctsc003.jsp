<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<html lang="ko" data-dark="false">
<head>
<meta charset="utf-8">
<title></title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=1190">
<meta name="apple-mobile-web-app-title" content=""/>
<meta name="robots" content="index,nofollow"/>
<meta name="description" content=""/>
<meta property="og:title" content="">
<meta property="og:url" content="">
<meta property="og:image" content="">
<meta property="og:description" content=""/>
<meta name="twitter:card" content="summary">
<meta name="twitter:title" content="">
<meta name="twitter:url" content="">
<meta name="twitter:image" content="">
<meta name="twitter:description" content=""/> 
</head>

<jsp:include page="./ctsc_header.jsp"></jsp:include>

</head>

<body>

<!-- START TOP -->
<%--<jsp:include page="./ctsc_top.jsp"></jsp:include>--%>
<%@ include file="../topMenu.jsp"%>

<div class="sidebar clearfix">
	<div role="tabpanel" class="sidepanel" style="display: block;">
		<jsp:include page="./module/ctsc003_tab.jsp"></jsp:include>
		<!-- Tab panes -->
		<div class="tab-content">
			<jsp:include page="./module/ctsc001_menu.jsp"></jsp:include>
			<jsp:include page="./module/ctsc002_menu.jsp"></jsp:include>
			<jsp:include page="./module/ctsc003_menu.jsp"></jsp:include>
		</div>
	</div>
</div>

<a href="#" class="sidebar-open-button"><i class="fa fa-bars"></i></a>
<a href="#" class="sidebar-open-button-mobile"><i class="fa fa-bars"></i></a>

<!-- START CONTENT -->
<div class="content">
	<div class="map-wrap" id="map1" style="background-size:100% auto"></div>
</div>
<%pageContext.setAttribute("timeStamp", new SimpleDateFormat("yyyyMMdd").format(new Date()));%>
<script type="text/javascript" src="/js/ct/ctsc_map.js?t=${timeStamp}"></script>
<script type="text/javascript" src="/js/ct/ctsc001.js?t=${timeStamp}"></script>
<script type="text/javascript" src="/js/ct/ctsc002.js?t=${timeStamp}"></script>
<script type="text/javascript" src="/js/ct/ctsc003.js?t=${timeStamp}"></script>
<script>


</script>
</body>
</html>