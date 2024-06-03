<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%
	/**
	 * @Class Name : egovSampleList.jsp
	 * @Description : Sample List 화면
	 * @Modification Information
	 *
	 *   수정일         수정자                   수정내용
	 *  -------    --------    ---------------------------
	 *  2009.02.01            최초 생성
	 *
	 * author 실행환경 개발팀
	 * since 2009.02.01
	 *
	 * Copyright (C) 2009 by MOPAS  All right reserved.
	 */
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>국가인터넷지도 | 지도 오픈API 샘플</title>
	<style type="text/css">
		.olImageLoadError {
			display: none !important;
		}
	</style>
	<!-- <script src="/js/jquery@1.9.1/jquery.min.js"></script> -->
	<script src="js/jquery/jquery-2.1.1.min.js"></script>
	<script src="js/map/v6.7.0/ol.js"></script>
	<link rel="stylesheet" href="js/map/v6.7.0/ol.css">
	<script src="js/map/map.js"></script>
	<script src="js/map/gis.js"></script>
	<script>
		window.onload = function() {
			var map = createMap('map1');
			var baselyaer = baselayer();
			map.addLayer(baselyaer);

			var select = new Select(map);
			select.once(
					"Polygon",
					"drawend",
					(event) => {
					},
					true
			);
		}

	</script>

</head>
<body>
<div id="map1" style="width:700px;height:700px;position:relative;">

</div>
</body>
</html>