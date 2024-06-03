<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>위성 기반의 긴급 공간정보(G119) 제공 서비스</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=1190">
<meta name="apple-mobile-web-app-title" content="" />
<meta name="robots" content="index,nofollow" />
<meta name="description" content="" />
<meta property="og:title" content="">
<meta property="og:url" content="">
<meta property="og:image" content="">
<meta property="og:description" content="" />

<script src="js/jquery.min.js"></script>
<%--<script src="https://cdnjs.cloudflare.com/ajax/libs/proj4js/1.1.0/proj4js-combined.min.js"></script>--%>
<script type="text/javascript" src="js/map/proj4js-combined.js"></script>
<script src="js/cm/cmsc003/lib/proj4js/proj4.js"></script>
<script src="js/map/v6.7.0/ol.js"></script>
<link rel="stylesheet" href="/js/map/v6.7.0/ol.css">
<script src="js/map/map.js"></script>
<script src="js/map/gis.js"></script>
<script src="js/ci/cisc004.js"></script>

<link rel="stylesheet"
	href="js/cm/cmsc003/lib/jstree-3.2.1/themes/default/style.min.css">
<script src="js/cm/cmsc003/lib/jstree-3.2.1/jstree.min.js"></script>

<!-- CMSC003 JS Dev -->
<script src="js/ci/cisc004/event.js"></script>

<!-- ================================================ -->
<script src="js/cm/cmsc003/src/cmsc003.js"></script>
<script src="js/cm/cmsc003/src/gis/gis.js"></script>
<script src="js/cm/cmsc003/src/storage/storage.js"></script>
<script src="js/cm/cmsc003/src/dom/dom.js"></script>
<script src="js/cm/cmsc003/src/converter/converter.js"></script>
<script src="js/cm/cmsc003/src/util/util.js"></script>
<!-- ================================================ -->
<style>
#cmsc003-modal table.js-table-disaster-result tbody tr:hover {
	background-color: #ededed;
	cursor: pointer;
}
.ui-btn-clicked-layer-preview > a > button {
	box-shadow: inset 1px 1px 2px 1px #626262;
	background-color: #cbcbcb !important;
}
.jstree-node > a > button {
	background-color:transparent;
	padding: 2px 5px;
	margin: 2px 4px 0px 4px;
}
.js-tree-parent-1 span, .js-tree-parent-2 span, .js-tree-parent-3 span, .js-tree-child-1 span, .js-tree-child-2 span, .js-tree-child-3 span {
	display: inline-block;
	overflow-x: hidden;
	text-overflow: ellipsis;
}

.js-tree-parent-1 span {
	font-weight: bold;
	max-width: 191px;
}

.js-tree-parent-2 span {
	font-weight: bold;
	max-width: 168px;
}

.js-tree-parent-3 span {
	font-weight: bold;
	max-width: 144px;
}

.js-tree-child-1 span {
	max-width: 168px;
}

.js-tree-child-2 span {
	max-width: 144px;
}

.js-tree-child-3 span {
	max-width: 120px;
}
</style>
<script type="text/javascript">
	const imageLayer = new ol.layer.Image();
	var selectionSource = new ol.source.Vector({ wrapX: false });
	var selectionLayer = new ol.layer.Vector({
		isRequired: true,
		zIndex: 1003,
		source: selectionSource,
		style: new ol.style.Style({
			fill: new ol.style.Fill({
				color: 'rgba(247, 17, 17, 0.05)'
			}),
			stroke: new ol.style.Stroke({
				color: '#f71111',
				width: 1
			}),
			image: new ol.style.Circle({
				radius: 7,
				fill: new ol.style.Fill({
					color: '#ffcc33'
				})
			})
		})
	});
	var selectionSourcePoint = new ol.source.Vector({ wrapX: false });
	var selectionLayerPoint = new ol.layer.Vector({
		isRequired: true,
		zIndex: 1003,
		source: selectionSourcePoint,
		style: new ol.style.Style({
			fill: new ol.style.Fill({
				color: 'rgba(247, 17, 17, 0.05)'
			}),
			stroke: new ol.style.Stroke({
				color: '#f71111',
				width: 1
			}),
			image: new ol.style.Circle({
				radius: 7,
				fill: new ol.style.Fill({
					color: '#ffcc33'
				})
			})
		})
	});
	var map, baselyaer;
	$(document).ready(function() {
		const _projCode = {
			'EPSG:4326': '+title=WGS 84 (long/lat) +proj=longlat +ellps=WGS84 +datum=WGS84 +units=degrees',
			'EPSG:3857': '+title=WGS 84 / Pseudo-Mercator +proj=merc +a=6378137 +b=6378137 +lat_ts=0.0 +lon_0=0.0 +x_0=0.0 +y_0=0 +k=1.0 +units=m +nadgrids=@null +no_defs',
			'EPSG:5179': '+proj=tmerc +lat_0=38 +lon_0=127.5 +k=0.9996 +x_0=1000000 +y_0=2000000 +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs',
			'EPSG:5174': '+proj=tmerc +lat_0=38 +lon_0=127.0028902777778 +k=1 +x_0=200000 +y_0=500000 +ellps=bessel +units=m +no_defs +towgs84=-115.80,474.99,674.11,1.16,-2.31,-1.63,6.43',
			'EPSG:5186': '+proj=tmerc +lat_0=38 +lon_0=127 +k=1 +x_0=200000 +y_0=600000 +ellps=GRS80 +units=m +no_defs',
			'EPSG:32652': '+proj=utm +zone=52 +ellps=WGS84 +datum=WGS84 +units=m +no_def',
			'EPSG:5187': '+proj=tmerc +lat_0=38 +lon_0=129 +k=1 +x_0=200000 +y_0=600000 +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs',
			'EPSG:5185': '+proj=tmerc +lat_0=38 +lon_0=125 +k=1 +x_0=200000 +y_0=600000 +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs'
		};
	    
	    const registProj4 = () => {
			Object.keys(_projCode).forEach(key => proj4.defs(key, _projCode[key]));
			ol.proj.proj4.register(proj4);
		};
		registProj4();
			
		map = new ol.Map({
			target: 'map1',
			view: new ol.View({
				projection: new ol.proj.Projection({
					code: "EPSG:5179",
					units: 'm',
					axisOrientation: 'enu',
					extent: [-200000.0, -3015.4524155292, 3803015.45241553, 4000000.0]
				}),
				resolutions: [2088.96, 1044.48, 522.24, 261.12, 130.56, 65.28, 32.64,
					16.32, 8.16, 4.08, 2.04, 1.02, 0.51, 0.255],
				center: [960363.606552286, 1920034.9139856],
				zoom: 10,
			})
		});
		baselyaer = baselayer();
		map.addLayer(baselyaer);
		
// 		map.addLayer(new ol.layer.Tile({
// 	    	source: new ol.source.OSM()
// 	    }));
		
		map.addLayer(imageLayer);
		
		map.addLayer(selectionLayer);
		map.addLayer(selectionLayerPoint);
		
		const imageExtent = [ 957225.1387, 1921135.1404,
				959008.6342, 1922918.6358 ];
		map.getView().fit(imageExtent);
		$("ul#topnav li").hover(function() { //Hover over event on list item
			$(this).css({
				'background' : '#1376c9 url(topnav_active.gif) repeat-x'
			}); //Add background color + image on hovered list item
			$(this).find("span").show(); //Show the subnav
		}, function() { //on hover out...
			$(this).css({
				'background' : 'none'
			}); //Ditch the background
			$(this).find("span").hide(); //Hide the subnav
		});

	});
	//3. 지도 이미지 활성화.
	function imageView(filePath, fileName, minx, miny, maxx, maxy, epsg) {

		// var baseNameArr = filePath.split('.');
		var thumbnail = filePath.replace(".tif", ".png");
		var param = {
			thumbnailFileCoursNm : thumbnail,
			ltopCrdntX : minx,
			ltopCrdntY : miny,
			rbtmCrdntX : maxx,
			rbtmCrdntY : maxy,
			mapPrjctnCn : epsg
		};

		var url = "cmsc003thumbnail.do?" + $.param(param);

		// var min = transform(miny,minx);
		// var max  = transform(maxy,maxx);
		var min = transformByEPSG(miny, minx, epsg);
		var max = transformByEPSG(maxy, maxx, epsg);

		//var imageExtent = [min.x,min.y,max.x,max.y ];

		var imageExtent = [ min.x, max.y, max.x, min.y ];

		var source = new ol.source.ImageStatic({
			url : url,
			crossOrigin : '',
			projection : 'EPSG:5179',
			imageExtent : imageExtent
		//imageSmoothing: imageSmoothing.checked,
		});
		imageLayer.setSource(source);
		map.getView().fit(imageExtent);
	}
	// function imageView(filePath,fileName,minx,miny,maxx,maxy){

	// 	var min = transform(miny,minx);
	// 	var max  = transform(maxy,maxx);
	// 	var imageExtent = [min.x,max.y,max.x,min.y ];
	// 	var  source = new ol.source.ImageStatic({
	// 		url:filePath,
	// // 		url:'/img/thumnail/'+fileName,
	// 		crossOrigin: '',
	// 		projection: 'EPSG:5179',
	// 		imageExtent: imageExtent
	// 		//imageSmoothing: imageSmoothing.checked,
	// 	});
	// 	imageLayer.setSource(source);
	// 	map.getView().fit(imageExtent);
	// }

	function transform(x, y) {
		var pt = new Proj4js.Point(x, y);
		var result = Proj4js.transform(s_srs, t_srs, pt);
		return result;
	}
	
	function transformByEPSG(x, y, epsg){
        var pt = new Proj4js.Point(x,y);
        var originCRS = null;
        var result = null;
        switch (epsg) {
		case 'EPSG:4326':
			originCRS = s_srs;
			break;
		case 'EPSG:5186':
			originCRS = srs_5186;		
			break;
		case 'EPSG:5179':
			originCRS = t_srs;
			break;
		case 'EPSG:32652':
			originCRS = srs_32652;
			break;
		default:
			break;
		}
        var result = Proj4js.transform(originCRS,t_srs,pt);
        return result;
    }
</script>
</head>

<body>
	<!-- 상단메뉴 -->
	<%@ include file="../topMenu.jsp"%>


	<!-- //////////////////////////////////////////////////////////////////////////// -->
	<!-- START SIDEBAR -->
	<div class="sidebar clearfix">
		<div role="tabpanel" class="sidepanel"
			style="display: block; padding: 10px;">
			<div class="tab-content" style="padding: 0;">
				<div role="tabpanel" class="tab-pane active" id="today">

					<div class="sidepanel-m-title">위성영상 검색</div>
					<div class="panel-body">
						<!-- =========================== -->
						<div class="form-group m-t-6" style="margin-top: 8px;">
							<label class="col-sm-3 control-label form-label">재난 ID</label>
							<div class="col-sm-9">
								<input type="text" class="form-control" id="disasterIdCreate"
									name="disasterId" readOnly="readOnly"
									style="cursor: auto; background-color: #fff;">
							</div>
						</div>

<!-- 						<div class="form-group m-t-10"> -->
<!-- 							<label for="input002" class="col-sm-3 control-label form-label">종류</label> -->
<!-- 							<div class="col-sm-9"> -->
<!-- 								<label class="ckbox-container">국토 위성 <input -->
<!-- 									type="checkbox" name="sate" value="soil"> <span -->
<!-- 									class="checkmark"></span> -->
<!-- 								</label> <label class="ckbox-container">기타 위성 <input -->
<!-- 									type="checkbox" name="sate" value="etc"> <span -->
<!-- 									class="checkmark"></span> -->
<!-- 								</label> -->
<!-- 							</div> -->
<!-- 						</div> -->
						<div class="form-group m-t-6" id="dataKindContainer">
							<label for="input002" class="col-sm-2 control-label form-label">종류</label>
							<div class="col-sm-10">
								<label class="ckbox-container"
									style="margin-right: 1px; font-size: 12px;">기존 데이터 <input
									type="checkbox" id="dataKindCurrent" name="dataKindCurrent"
									checked="checked"> <span class="checkmark"></span>
								</label> <label class="ckbox-container"
									style="margin-right: 1px; font-size: 12px;">긴급 영상 <input
									type="checkbox" id="dataKindEmergency"
									name="dataKindEmergency"> <span class="checkmark"></span>
								</label> <label class="ckbox-container"
									style="margin-right: 1px; font-size: 12px;">분석결과 데이터 <input
									type="checkbox" id="dataKindResult" name="dataKindResult">
									<span class="checkmark"></span>
								</label>
							</div>
						</div>
						<div class="form-group m-t-6" id="targetDataKindContainer">
							<label for="input002" class="col-sm-2 control-label form-label">대상</label>
							<div class="col-sm-10">
								<label class="ckbox-container"
									style="margin-right: 1px; font-size: 12px;">원본 데이터 <input
									type="radio" id="originData" name="dataType" value="1"
									checked="checked"> <span class="checkmark"></span>
								</label> <label class="ckbox-container"
									style="margin-right: 1px; font-size: 12px;">긴급공간정보 <input
									type="radio" id="datasetData"
									name="dataType" value="2"> <span class="checkmark"></span>
								</label> <label class="ckbox-container"
									style="margin-right: 1px; font-size: 12px;">처리 데이터 <input
									type="radio" id="workData" name="dataType" value="3">
									<span class="checkmark"></span>
								</label>
							</div>
						</div>
<!-- 						<div class="form-group m-t-10"> -->
<!-- 							<label for="input002" class="col-sm-3 control-label form-label">기간</label> -->
<!-- 							<div class="col-sm-4"> -->
<!-- 								<input type="text" id="datepicker" class="form-control"> -->
<!-- 							</div> -->
<!-- 							<div class="col-sm-1"> -->
<!-- 								<span class="txt-in-form">~<span> -->
<!-- 							</div> -->
<!-- 							<div class="col-sm-4"> -->
<!-- 								<input type="text" id="datepicker2" class="form-control"> -->
<!-- 							</div> -->
<!-- 						</div> -->
<!-- 						<script src="/js/datepicker.js"></script> -->
<!-- 						<script> -->
<!-- 							let datepicker = new DatePicker(document -->
<!-- 									.getElementById('datepicker')); -->
<!-- 							let datepicker2 = new DatePicker(document -->
<!-- 									.getElementById('datepicker2')); -->
<!-- 						</script> -->
<!-- 						<div class="form-group m-t-10"> -->
<!-- 							<label for="input002" class="col-sm-3 control-label form-label">해상도(m)</label> -->
<!-- 							<div class="col-sm-4"> -->
<%-- 															<select class="form-basic"> --%>
<%-- 																<option></option> --%>
<%-- 															</select> --%>
<!-- 								<input type="text" id="resolution1" class="form-control"> -->
<!-- 							</div> -->
<!-- 							<div class="col-sm-1"> -->
<!-- 								<span class="txt-in-form">~<span> -->
<!-- 							</div> -->
<!-- 							<div class="col-sm-4"> -->
<!-- 								<input type="text" id="resolution2" class="form-control"> -->
<%-- 															<select class="form-basic"> --%>
<%-- 																<option></option> --%>
<%-- 															</select> --%>
<!-- 							</div> -->
<!-- 						</div> -->
						<!-- 						<div class="form-group m-t-6" id="dataKindContainer"> -->
						<!-- 							<label for="input002" class="col-sm-2 control-label form-label">종류</label> -->
						<!-- 							<div class="col-sm-10"> -->
						<!-- 								<label class="ckbox-container" -->
						<!-- 									style="margin-right: 1px; font-size: 12px;">기존 데이터 <input -->
						<!-- 									type="checkbox" id="dataKindCurrent" name="dataKindCurrent" -->
						<!-- 									checked="checked"> <span class="checkmark"></span> -->
						<!-- 								</label> <label class="ckbox-container" -->
						<!-- 									style="margin-right: 1px; font-size: 12px;">긴급 영상 <input -->
						<!-- 									type="checkbox" id="dataKindEmergency" -->
						<!-- 									name="dataKindEmergency"> <span class="checkmark"></span> -->
						<!-- 								</label> <label class="ckbox-container" -->
						<!-- 									style="margin-right: 1px; font-size: 12px;">분석결과 데이터 <input -->
						<!-- 									type="checkbox" id="dataKindResult" name="dataKindResult"> -->
						<!-- 									<span class="checkmark"></span> -->
						<!-- 								</label> -->
						<!-- 							</div> -->
						<!-- 						</div> -->
						<!-- 						<div id="datepickerContainer" -->
						<!-- 							class="form-group m-t-6 input-daterange"> -->
						<!-- 							<label for="input002" class="col-sm-2 control-label form-label">기간</label> -->
						<!-- 							<div class="col-sm-5"> -->
						<!-- 								<input type="text" class="form-control ui-input-datepicker" -->
						<!-- 									id="inputStartDate" name="dateFrom" value="2020-04-23"> -->
						<!-- 								<button type="button" id="btnOpenDatepickerStart" -->
						<!-- 									class="btn btn-default btn-icon ui-btn-open-datepicker"> -->
						<!-- 									<i class="fas fa-calendar-alt"></i> -->
						<!-- 								</button> -->
						<!-- 							</div> -->

						<!-- 							<div class="col-sm-5"> -->
						<!-- 								<input type="text" class="form-control ui-input-datepicker" -->
						<!-- 									id="inputEndDate" name="dateTo" value="2020-04-26"> -->
						<!-- 								<button type="button" id="btnOpenDatepickerEnd" -->
						<!-- 									class="btn btn-default btn-icon ui-btn-open-datepicker"> -->
						<!-- 									<i class="fas fa-calendar-alt"></i> -->
						<!-- 								</button> -->
						<!-- 							</div> -->
						<!-- 						</div> -->
						<!-- 						<div class="form-group m-t-6"> -->
						<!-- 							<label for="input002" class="col-sm-3 control-label form-label">해상도(m)</label> -->
						<!-- 							<div class="col-sm-4"> -->
						<!-- 								<input type="number" class="form-basic" name="resolMin" /> -->
						<!-- 																	<select class="form-basic" name="resolMin"> -->
						<!-- 																		<option value='5cm' selected>5</option> -->
						<!-- 																		<option value='10cm'>10</option> -->
						<!-- 																		<option value='25cm'>25</option> -->
						<!-- 																	</select> -->
						<!-- 							</div> -->
						<!-- 							<div class="col-sm-1"> -->
						<!-- 								<span class="txt-in-form">~</span> -->
						<!-- 							</div> -->
						<!-- 							<div class="col-sm-4"> -->
						<!-- 								<input type="number" class="form-basic" name="resolMax" /> -->
						<!-- 																	<select class="form-basic" name="resolMax"> -->
						<!-- 																		<option value='5cm'>5</option> -->
						<!-- 																		<option value='10cm'>10</option> -->
						<!-- 																		<option value='25cm' selected>25</option> -->
						<!-- 								</select> -->
						<!-- 							</div> -->
						<!-- 						</div> -->
<!-- 						<div class="form-group m-t-6"> -->
<!-- 							<label class="col-sm-3 control-label form-label">재난 유형</label> -->
<!-- 							<div class="col-sm-9"> -->
<!-- 								<select id="selectDisasterType" class="form-basic" -->
<!-- 									name="disasterType"> -->
<!-- 									<option value='AllDisaster'>전체</option> -->
<!-- 									<option value='Flood'>수해</option> -->
<!-- 									<option value='Landslide'>산사태</option> -->
<!-- 									<option value='ForestFire'>산불</option> -->
<!-- 									<option value='Earthquake'>지진</option> -->
<!-- 									<option value='MaritimeDisaster'>해양재난</option> -->
<!-- 								</select> -->
<!-- 							</div> -->
<!-- 						</div> -->

						<!-- <div class="form-group m-t-6">
							<label class="col-sm-3 control-label form-label">재난 ID</label>
							<div class="col-sm-9">
								<input type="text" class="form-control" id="disasterId"
									name="disasterId" readOnly="readOnly"
									style="cursor: auto; background-color: #fff;">
							</div>
						</div> -->

<!-- 						<div class="form-group m-t-6"> -->
<!-- 							<label class="col-sm-12 control-label form-label">재난지역</label> -->
<!-- 							<div class="col-sm-4"> -->
<!-- 								<select class="form-basic" name="sido" -->
<!-- 									onChange="getSigungu(this.value)"> -->
<!-- 									<option selected disabled hidden>광역시도</option> -->
<!-- 								</select> -->
<!-- 							</div> -->
<!-- 							<div class="col-sm-4"> -->
<!-- 								<select class="form-basic" name="sigungu" -->
<!-- 									onChange="getEmd(this.value)" disabled> -->
<!-- 									<option selected disabled hidden>시군구</option> -->
<!-- 								</select> -->
<!-- 							</div> -->
<!-- 							<div class="col-sm-4"> -->
<!-- 								<select class="form-basic" name="emd" -->
<!-- 									onChange="getEmdPosition(this.value)" disabled> -->
<!-- 									<option selected disabled hidden>읍면동</option> -->
<!-- 								</select> -->
<!-- 							</div> -->
<!-- 						</div> -->
<!-- 						<div class="form-group m-t-6"> -->
<!-- 							<div class="col-sm-12"> -->
<!-- 								<input type="text" id="detailAddrForm" readOnly="readOnly" -->
<!-- 									class="form-basic"> -->
<!-- 							</div> -->
<!-- 						</div> -->


<!-- 						<div class="form-group m-t-6"> -->
<!-- 							<label for="input002" class="col-sm-2 control-label form-label">ROI</label> -->
<!-- 							<div class="col-sm-10"> -->
<!-- 								<div class="col-sm-2 in-tit">ULX</div> -->
<!-- 								<div class="col-sm-4"> -->
<!-- 									<input type="text" class="form-control js-input-roi-extent" -->
<!-- 										id="inputULX" name="ulx4326"> <input type="hidden" -->
<!-- 										id="inputULX2" name="ulx5186"> <input type="hidden" -->
<!-- 										id="inputULX3" name="ulx5179"> <input type="hidden" -->
<!-- 										id="inputULX4" name="ulx32652"> -->
<!-- 								</div> -->
<!-- 								<div class="col-sm-2 in-tit">ULY</div> -->
<!-- 								<div class="col-sm-4"> -->
<!-- 									<input type="text" class="form-control js-input-roi-extent" -->
<!-- 										id="inputULY" name="uly4326"> <input type="hidden" -->
<!-- 										id="inputULY2" name="uly5186"> <input type="hidden" -->
<!-- 										id="inputULY3" name="uly5179"> <input type="hidden" -->
<!-- 										id="inputULY4" name="uly32652"> -->
<!-- 								</div> -->
<!-- 								<div class="col-sm-2 in-tit m-t-5">LRX</div> -->
<!-- 								<div class="col-sm-4 m-t-5"> -->
<!-- 									<input type="text" class="form-control js-input-roi-extent" -->
<!-- 										id="inputLRX" name="lrx4326"> <input type="hidden" -->
<!-- 										id="inputLRX2" name="lrx5186"> <input type="hidden" -->
<!-- 										id="inputLRX3" name="lrx5179"> <input type="hidden" -->
<!-- 										id="inputLRX4" name="lrx32652"> -->
<!-- 								</div> -->
<!-- 								<div class="col-sm-2 in-tit m-t-5">LRY</div> -->
<!-- 								<div class="col-sm-4 m-t-5"> -->
<!-- 									<input type="text" class="form-control js-input-roi-extent" -->
<!-- 										id="inputLRY" name="lry4326"> <input type="hidden" -->
<!-- 										id="inputLRY2" name="lry5186"> <input type="hidden" -->
<!-- 										id="inputLRY3" name="lry5179"> <input type="hidden" -->
<!-- 										id="inputLRY4" name="lry32652"> -->
<!-- 								</div> -->
<!-- 							</div> -->
<!-- 						</div> -->
						<!-- =========================== -->
						<!-- 
					<div class="form-group m-t-10">
						<label class="col-sm-3 control-label form-label">재난 유형</label>
						<div class="col-sm-9">
							<select class="form-basic">
								<option></option>
							</select>             
						</div>
					</div>
					 -->
<!-- 						<div class="form-group m-t-10"> -->
<!-- 							<label for="input002" class="col-sm-3 control-label form-label">운량</label> -->
<!-- 							<div class="col-sm-4"> -->
<!-- 								<div class="radio radio-info radio-inline"> -->
<!-- 									<input type="radio" id="inlineRadio5" value="option1" -->
<!-- 										name="radioInline" checked=""> <label -->
<!-- 										for="inlineRadio5">전체</label> -->
<!-- 								</div> -->
<!-- 							</div> -->
<!-- 							<div class="col-sm-4"> -->
<!-- 								<div class="radio radio-inline"> -->
<!-- 									<input type="radio" id="inlineRadio6" value="option1" -->
<!-- 										name="radioInline"> <label for="inlineRadio6">10% -->
<!-- 										이내</label> -->
<!-- 								</div> -->
<!-- 							</div> -->
<!-- 						</div> -->
						<div class="btn-wrap a-cent">
							<a href="javascript:search()" class="btn btn-default"><i
								class="fas fa-search m-r-5"></i>검색</a>
						</div>
						<div class="sidepanel-s-title m-t-30">
							검색 결과 
<!-- 							<a href="" id="mymodal" class="btn btn-light f-right" data-toggle="modal" data-target="#myModal"> -->
							<a href="#" id="btnMyModal" class="btn btn-light f-right">
								<i class="fas fa-project-diagram m-r-5"></i>수행</a>
						</div>
						<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
							aria-hidden="true">
							<div class="modal-dialog modal-sm">
								<div class="modal-content">
									<div class="modal-header">
										<button type="button" class="close" data-dismiss="modal"
											aria-label="Close">
											<span aria-hidden="true">&times;</span>
										</button>
										<h4 class="modal-title">대기보정</h4>
									</div>
									<div class="modal-body">
										<div class="panel-body">
											<form class="">
												<div class="form-group m-t-10">
													<label class="col-sm-4 control-label form-label">입력영상</label>
													<div class="col-sm-8">
														<select class="form-basic" id="input_video">
															<option></option>
														</select>
													</div>
												</div>
												<div class="form-group m-t-10">
													<label class="col-sm-4 control-label form-label">알고리즘</label>
													<div class="col-sm-8">
														<select class="form-basic" id="algorithm">
															<option value="Dark Object Subtraction">Dark
																Object Subtraction</option>
														</select>
													</div>
												</div>
												<div class="form-group m-t-10">
													<label for="output"
														class="col-sm-4 control-label form-label">결과영상</label>
													<div class="col-sm-8">
														<input type="text" class="form-control" id="output"
															value="AtmoResult_">
													</div>
												</div>
											</form>

										</div>
									</div>
									<div class="modal-footer">
										<button type="button" class="btn btn-default"
											onclick="result();">
											<i class="fas fa-check-square m-r-5"></i>처리
										</button>
										<button type="button" class="btn btn-gray" id="AtomsCacel"
											data-dismiss="modal" aria-label="Close">
											<i class="fas fa-times m-r-5"></i>취소
										</button>
										<button id="btn_process" type="button" class="btn btn-default"
											onclick="$('#scriptModal').modal('show');">
											<i class="fas fa-check-square m-r-5"></i>스크립트저장
										</button>
									</div>
									<div class="load" id="progres" style="display: none;"></div>
								</div>
							</div>
						</div>
						<div class="panel-body">
							<div class="col-lg-12 js-search-result-area">
								<div class="panel panel-default" style="height: 200px;">
<!-- 									<dl class="result-list-input" id="AeroResult"> -->
										<%--			<dt>국토위성영상 (2)</dt>
									<dd>국토위성-1호 xxxxxx001</dd>
									<dd>
										<code class="year">연도</code>2020/05/20
										<code class="position">위치</code>안양/37612079
									</dd>--%>
<!-- 									</dl> -->
									<%--				<dl class="result-list-input">
									<dd>국토위성-1호 xxxxxx002</dd>
									<dd>
										<code class="year">연도</code>2020/09/30
										<code class="position">위치</code>안양/37612080
									</dd>
								</dl>--%>


								</div>
							</div>
						</div>
						<div class="sidepanel-s-title m-t-30">처리 결과</div>
						<div class="panel-body">
							<div class="col-lg-12">
								<div class="panel panel-default" style="height: 200px;">
									<dl class="result-list-input" id="AtomsResult">

									</dl>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>

		</div>

	</div>

	<a href="#" class="sidebar-open-button"><i class="fa fa-bars"></i></a>
	<a href="#" class="sidebar-open-button-mobile"><i
		class="fa fa-bars"></i></a>

	<div class="modal fade" id="scriptModal" tabindex="-1" role="dialog"
		aria-hidden="true">
		<div class="modal-dialog modal-sm">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">스크립트저장</h4>
				</div>
				<div class="modal-body">
					<div class="panel-body">
						<form class="">
							<div class="form-group m-t-10">
								<label for="txt_meta" class="col-sm-4 control-label form-label">스크립트명</label>
								<div class="col-sm-8">
									<input id="input_script_nm" type="text" class="form-control"
										value="">
								</div>
								<input type="hidden" id="script_kind" value="">

							</div>
						</form>
					</div>
				</div>
				<div class="modal-footer">
					<button id="btn_process" type="button" class="btn btn-default"
						onclick="fnScriptSave();">
						<i class="fas fa-check-square m-r-5"></i>저장
					</button>
					<button type="button" class="btn btn-gray" data-dismiss="modal"
						aria-label="Close">
						<i clas="fas fa-times m-r-5"></i>취소
					</button>
				</div>
			</div>
		</div>
	</div>

	<!-- START CONTENT -->
	<div class="content">
		<div class="map-wrap-n" id="map1" style="background-size: 100% auto">
		</div>
	</div>

	<script>
		// 수행 버튼 선택 시 결과영상값 'AtmoResult_' 초기화
// 		$('#mymodal').click(function() {
// 			$('#output').val('AtmoResult_');
// 		});
	</script>
	<script>
	
		$(document).ready(function() {
			// 시도 셀렉트 초기화
			getSido();
			// 수행 버튼 클릭시
			$('#btnMyModal').click(function() {
				onClickRunButton();
			});
		});
		
		function onClickRunButton() {
			$('#input_video').empty();
			$('#input_video').append($('<option>'));
			
			$('#output').val('AtmoResult_');
			$('#myModal').modal('show', true);
			const vector = $("#resultList1 #resultList1_digital_tree").jstree().get_bottom_checked(true);
			const aerial = $("#resultList1 #resultList1_aerial_tree").jstree().get_bottom_checked(true);
			const ortho = $("#resultList1 #resultList1_ortho_tree").jstree().get_bottom_checked(true);
			const dem = $("#resultList1 #resultList1_dem_tree").jstree().get_bottom_checked(true);
			const graphics = $("#resultList1 #resultList1_graphics_tree").jstree().get_bottom_checked(true);
			const satellite = $("#resultList1 #resultList1_satellite_tree").jstree().get_bottom_checked(true);
			const earthquake = $("#resultList2 #resultList2_earthquake_tree").jstree().get_bottom_checked(true);
			const forestFire = $("#resultList2 #resultList2_forestFire_tree").jstree().get_bottom_checked(true);
			const flood = $("#resultList2 #resultList2_flood_tree").jstree().get_bottom_checked(true);
			const landslip = $("#resultList2 #resultList2_landslip_tree").jstree().get_bottom_checked(true);
			const maritime = $("#resultList2 #resultList2_maritime_tree").jstree().get_bottom_checked(true);
			const redTide = $("#resultList2 #resultList2_redTide_tree").jstree().get_bottom_checked(true);
			const vectorAnalObj = $("#resultList3 #resultList3_vectorAnalObj_tree").jstree().get_bottom_checked(true);
			const rasterAnalObj = $("#resultList3 #resultList3_rasterAnalObj_tree").jstree().get_bottom_checked(true);
			const vectorAnalChg = $("#resultList3 #resultList3_vectorAnalChg_tree").jstree().get_bottom_checked(true);
			const rasterAnalChg = $("#resultList3 #resultList3_rasterAnalChg_tree").jstree().get_bottom_checked(true);
			
			// kompsat 데이터
			// landsat 데이터
			// sentinel 데이터
			// cas 데이터
			satellite.forEach(function(item) {
				var list = CMSC003.Storage.get('searchDataMap');
				var sat = item.id.split('-')[2];
				var folder = item.parent.split('-')[5];
				var layer = null;
				if (sat == "kompsat") {
					layer = list['kompsat-'+item.li_attr.value];
					// innerFileCoursNm
				} else if (sat == "landsat") {
					layer = list['landsat-'+item.li_attr.value];
				} else if (sat == "sentinel") {
					layer = list['sentinel-'+item.li_attr.value];
				} else if (sat == "cas") {
					layer = list['cas-'+item.li_attr.value];
				}
				if(layer) {
					var opt = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
					$('#input_video').append(opt);	
				}
			});
		}
	
		function getSido(sidoText, sigunText, dongText, noTrigger) {
			var sido = $('select[name="sido"]');
			
			$.ajax({
				url: "cmsc003G001List.do",
				type: "GET",
				dataTyp: "JSON",
				success: function(returnData) {
					for (var item of returnData.selectG001List) {
						sido.append($("<option value="+item.bjcd+">"+item.name+"</option>"));
					}
					if(sidoText) {
						var sidoCode = $(sido).find('option:contains("'+sidoText+'")').val();
						$(sido).val(sidoCode);
	//						$(sido).trigger('change');
						getSigungu(sidoCode, sigunText, dongText, noTrigger);
					}
				},
				error: function(request, status, error) {
					alert("code:" + request.status + "\n" + "message:"
							+ request.respnseText + "\n" + "error:" + error);
				}
	
			});
		}
		
		// 시도 중심점 설정, 시군구 가져옴
		function getSigungu(select, sigunText, dongText, noTrigger) {
	
			// 시군구 초기화
			var sigungu = $('select[name="sigungu"]');
			//sigungu.children('option:not(:first)').remove();
			sigungu.children('option').remove();
			sigungu.append($("<option selected disabled hidden>시군구</option>"));
			sigungu.attr("disabled", false); //시군구 활성화
			
			// 읍면동 초기화
			var emd = $('select[name="emd"]');
			emd.children('option').remove();
			emd.append($("<option selected disabled hidden>읍면동</option>"));
			
			$.ajax({
				url: "cmsc003G010List.do",
				type: "GET",
				data: "g010Bjcd="+select.substring(0,2)+"&g001Bjcd="+select,
				dataTyp: "JSON",
				success: function(returnData) {
					//console.log(returnData.selectG001Geom[0], 'TS_BEFORE_SIGUNGU');
					if(!noTrigger) {
						// 좌표변환
						var position = returnData.selectG001Geom[0].centeroid.replace(/[^0-9.\s-]/g,"").split(' ');
						var positionToNumber = [Number(position[0]), Number(position[1])];
						var transform = CMSC003.GIS.transform(positionToNumber, 'EPSG:5174', 'EPSG:5179');
						
						// 지도 중심점 이동
						CMSC003.GIS.setMapCenter(transform, map);
					}
					
					//console.log(returnData, 'sigungu');
					// <option> 시군구 추가
					for (var item of returnData.selectG010List) {
						sigungu.append($("<option value="+item.bjcd+">"+item.name+"</option>"));
					}
					
					if(sigunText) {
						var sigunCode = $(sigungu).find('option:contains("'+sigunText+'")').val();
						$(sigungu).val(sigunCode);
						getEmd(sigunCode, dongText, noTrigger);
					}
				},
				error: function(request, status, error) {
					alert("code:" + request.status + "\n" + "message:"
							+ request.respnseText + "\n" + "error:" + error);
				}
			})
		}
		
		// 시군구 중심점으로 설정, 읍면동 리스트 가져옴
		function getEmd(select, dongText, noTrigger) {
			
			// 시군구
			var sigungu = $('select[name="sigungu"] option:checked');
			
			// 읍면동 초기화
			var emd = $('select[name="emd"]');
			emd.children('option').remove();
			emd.append($("<option selected disabled hidden>읍면동</option>"));
			emd.attr("disabled", false); // 읍면동 활성화
			
			// ~~시 => 4자리, ~~구 => 5자리
			var g011Bjcd = sigungu.text().charAt(sigungu.text().length-1) == '시' ? select.substring(0,4) : select.substring(0,5);
			var g010Bjcd = select;
			
			$.ajax({
				url: "cmsc003G011List.do",
				type: "GET",
				data: "g011Bjcd="+g011Bjcd+"&g010Bjcd="+g010Bjcd,
				dataTyp: "JSON",
				success: function(returnData) {	
					//console.log(returnData.selectG010Geom[0], 'TS_BEFORE_EMD');
					if(!noTrigger) {
						// 좌표변환
						var position = returnData.selectG010Geom[0].centeroid.replace(/[^0-9.\s-]/g,"").split(' ');
						var positionToNumber = [Number(position[0]), Number(position[1])];
						var transform = CMSC003.GIS.transform(positionToNumber, 'EPSG:5174', 'EPSG:5179');
						
						// 지도 중심점 이동
						CMSC003.GIS.setMapCenter(transform, map);	
					}
					
					// <option> 읍면동 추가
					//console.log(returnData,'emd');
					for (var item of returnData.selectG010List) {
						emd.append($("<option value="+item.bjcd+">"+item.name+"</option>"));
					}
					
					if(dongText) {
						var dongCode = $(emd).find('option:contains("'+dongText+'")').val();
						$(emd).val(dongCode);
					}
				},
				error: function(request, status, error) {
					alert("code:" + request.status + "\n" + "message:"
							+ request.respnseText + "\n" + "error:" + error);
				}
			})
		}
		
		// 읍면동 좌표 추출 후 지도 수정
		function getEmdPosition(select) {
			var emd = $('select[name="emd"]');
			
			$.ajax({
				url: "cmsc003G011Geom.do",
				type: "GET",
				data: "g011Bjcd="+select,
				dataTyp: "JSON",
				success: function(returnData) {
					// 좌표변환
					var position = returnData.selectG010Geom[0].centeroid.replace(/[^0-9.\s-]/g,"").split(' ');
					var positionToNumber = [Number(position[0]), Number(position[1])];
					var transform = CMSC003.GIS.transform(positionToNumber, 'EPSG:5174', 'EPSG:5179');
					
					// 지도 중심점 이동
					CMSC003.GIS.setMapCenter(transform, map);
				},
				error: function(request, status, error) {
					alert("code:" + request.status + "\n" + "message:"
							+ request.respnseText + "\n" + "error:" + error);
				}
			})
		}
	
		// 생성용 재난 id 인풋 클릭
		$('#disasterIdCreate').click(function(e) {
			CMSC003.DOM.showDisasterIDSearchPopup('js-create-emergency-id');
		});
		
		// 재난 id 검색결과 클릭
		$('body').on('click', '#cmsc003-modal table.js-table-disaster-result tbody tr.js-create-emergency-id', function(e) {
			if ($(e.currentTarget).children('td').length === 1) {
				return;
			}
			var child = $(e.currentTarget).children('td:first-child');
			var sido = $(e.currentTarget).children('td:nth-child(5)').text();
			//	CMSC003.Storage.set('currentSido', sido);
			//	console.log(sido);
			var gugun = $(e.currentTarget).children('td:nth-child(6)').text();
			//	CMSC003.Storage.set('currentGugun', gugun);
			//	console.log(gugun);
			var dong = $(e.currentTarget).children('td:nth-child(7)').text();
			//	CMSC003.Storage.set('currentDong', dong);

			// getSido(sido, gugun, dong, true);

			$('#detailAddrForm').val($(e.currentTarget).children('td:nth-child(8)').text());
			//	console.log(dong);
			$('#disasterIdCreate').val($(child).text());
			CMSC003.Storage.set('currentDisasterCode', $(child).attr('data'));
			var x = parseFloat($(child).attr('x'));
			var y = parseFloat($(child).attr('y'));

			var bbox = $(child).attr('bbox');
			var bbox_proj = $(child).attr('mapprjctncn');

			var positionToNumber = [x, y];

			var transform = CMSC003.GIS.transform(positionToNumber, 'EPSG:4326', 'EPSG:5179');
			CMSC003.Storage.set('currentDisasterSpot', positionToNumber);

			// 지도 중심점 이동
			CMSC003.GIS.setMapCenter(transform, map);

			// ROI 중심점 포인트 생성
// 			CMSC003.GIS.createROICenter(transform);
			//	CMSC003.GIS.createBufferROI(positionToNumber, 100);

			// ROI 생성(bbox 있을경우)
			if (bbox) {
				var transform_bbox = [];
				// String => Array(float)
				bbox.split(',').forEach(e => {
					transform_bbox.push(parseFloat(e));
				})

				// 좌표 변환
				if (bbox_proj != 'EPSG:5179') {
					transform_bbox = CMSC003.GIS.transformExtent(transform_bbox, bbox_proj, 'EPSG:5179');
				}
				console.log(transform_bbox);
				
				var bbox4326 = CMSC003.GIS.convert5179To4326Extent(transform_bbox);
				var bbox5186 = CMSC003.GIS.convert5179To5186Extent(transform_bbox);
				var bbox32652 = CMSC003.GIS.convert5179To32652Extent(transform_bbox);
				
				// 4326, 5186, 5179, 32652 순서
				var bboxArray = [bbox4326, bbox5186, transform_bbox, bbox32652];
				
				CMSC003.Storage.set('atmoBbox', bboxArray);
				// CMSC003.DOM.inputROIToForm(transform_bbox);
				// ROI 피처 생성
// 				CMSC003.GIS.createPolygonROIfrom5179(transform_bbox);
				CMSC003.GIS.createPolygonROIfrom5179(transform_bbox, selectionLayer, map);

			}

			$('#cmsc003-modal').remove();
		});
	</script>


	<!-- ================================================
jQuery Library
================================================ -->


	<!-- ================================================
Bootstrap Core JavaScript File
================================================ -->
	<script src="js/bootstrap/bootstrap.min.js"></script>

	<!-- ================================================
Plugin.js - Some Specific JS codes for Plugin Settings
================================================ -->
	<script src="js/plugins.js"></script>

	<!-- ================================================
Summernote
================================================ -->
	<script src="js/summernote/summernote.min.js"></script>

	<!-- ================================================
Data Tables
================================================ -->
	<script src="js/datatables/datatables.min.js"></script>

	<!-- ================================================
Sweet Alert
================================================ -->
	<script src="js/sweet-alert/sweet-alert.min.js"></script>

	<!-- ================================================
Kode Alert
================================================ -->
	<script src="js/kode-alert/main.js"></script>

	<!-- ================================================
jQuery UI
================================================ -->
	<script src="js/jquery-ui/jquery-ui.min.js"></script>

	<!-- ================================================
Moment.js
================================================ -->
	<script src="js/moment/moment.min.js"></script>

	<!-- ================================================
Full Calendar
================================================ -->
	<script src="js/full-calendar/fullcalendar.js"></script>

	<!-- ================================================
Bootstrap Date Range Picker
================================================ -->
	<script src="js/date-range-picker/daterangepicker.js"></script>

</body>
</html>