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
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
	<title>국가인터넷지도 | 지도 오픈API 샘플</title>
	<style type="text/css">
		.olImageLoadError {
			display: none !important;
		}
	</style>
	<!-- <script src="/js/jquery@1.9.1/jquery.min.js"></script> -->
	<script src="js/jquery/jquery-2.1.1.min.js"></script>

	<script src="js/map/v6.7.0/ol.js"></script>
	<link rel="stylesheet" href="js/map/v6.7.0/ol.css"/>
	<script src="js/map/gis.js"></script>
	<script>
		var map1,view;

		window.onload = function(){

			var map = new ol.Map({
				target: 'map1'
			});
			view =  new ol.View({
				projection:new ol.proj.Projection({
					code:"EPSG:5179",
					units:'m',
					axisOrientation:'enu',
					extent:[-200000.0, -3015.4524155292, 3803015.45241553, 4000000.0]
				}),
				resolutions:[2088.96, 1044.48, 522.24, 261.12, 130.56, 65.28, 32.64,
					16.32, 8.16, 4.08, 2.04, 1.02, 0.51, 0.255],
				center:[960363.606552286,1920034.9139856],
				zoom:0
			});
			map.setView(view);

			var baseLyr1 = new ol.layer.Tile({
				source: new ol.source.WMTS({
					projection:"EPSG:5179",
					tileGrid:new ol.tilegrid.WMTS({
						extent:[-200000.0, -3015.4524155292, 3803015.45241553, 4000000.0],
						origin:[-200000.0,4000000.0],
						tileSize:256,
						matrixIds:["L05","L06","L07","L08","L09","L10","L11","L12","L13","L14","L15","L16","L16","L17","L18"],
						resolutions:[2088.96, 1044.48, 522.24, 261.12, 130.56, 65.28, 32.64,
							16.32, 8.16, 4.08, 2.04, 1.02, 0.51, 0.255],
					}),
					layer:"korean_map",
					style:"korean",
					format:"image/png",
					matrixSet:"korean",
					url:"http://map.ngii.go.kr/openapi/Gettile.do?apikey=E57BF781E900D680853EF984632955F9",
					tileLoadFunction:function(imageTile,src){
						src = src.replace("TileMatrix","tilematrix");
						//src = src.replace("TileRow","tilerow")
						src = src.replace("TileRow","tilerow")
						src = src.replace("TileCol","tilecol")
						imageTile.getImage().src = src;


					}

				}),
				visible:true
			})

			map.addLayer(baseLyr1);

			var select = new Select(map);
			select.once(
					"Polygon",
					"drawend",
					(event) => {
						debugger;
						// that.source.clear();
						// const feature = event.feature.clone();
						// that.data["feature"] = feature;
						// that.setStyle();
						// that.source.addFeature(feature);
						// that.renderStyle();
					},
					true
			);

		};

	</script>

</head>
<body>
<div id="map1" style="width:700px;height:700px;position:relative;">

</div>
</body>
</html>