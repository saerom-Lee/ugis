<%@ page language="java" contentType="text/html; charset=UTF-8"    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>위성 기반의 긴급 공간정보(G119) 제공 서비스</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=1190">
<meta name="apple-mobile-web-app-title" content=""/>
<meta name="robots" content="index,nofollow"/>
<meta name="description" content=""/>
<meta property="og:title" content="">
<meta property="og:url" content="">
<meta property="og:image" content="">
<meta property="og:description" content=""/>


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

<!-- ================================================
jQuery Library
================================================ -->
<script src="js/jquery.min.js"></script>

<!-- ================================================
Bootstrap Core JavaScript File
================================================ -->
<script src="js/bootstrap/bootstrap.min.js"></script>

<!-- ================================================
Plugin.js - Some Specific JS codes for Plugin Settings
================================================ -->
<script src="js/plugins.js"></script>

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


<!-- ================================================
jstree
================================================ -->
<link rel="stylesheet"
	href="js/cm/cmsc003/lib/jstree-3.2.1/themes/default/style.min.css">
<script src="js/cm/cmsc003/lib/jstree-3.2.1/jstree.min.js"></script>

<!-- ================================================
map Library
================================================ -->
<script src="js/cm/cmsc003/lib/proj4js/proj4.js"></script>
<script src="js/map/v6.7.0/ol.js"></script>
<link rel="stylesheet" href="js/map/v6.7.0/ol.css">
<%--<script src="https://cdnjs.cloudflare.com/ajax/libs/proj4js/1.1.0/proj4js-combined.min.js"></script>--%>
	<script type="text/javascript" src="js/map/proj4js-combined.js"></script>
<script src="js/map/map.js"></script>
<script src="js/map/gis.js"></script>

	<!-- ================================================
CMSC003 JS Dev
================================================ -->

	<script src="js/cm/cmsc003/src/cmsc003.js"></script>
	<script src="js/cm/cmsc003/src/gis/gis.js"></script>
	<script src="js/cm/cmsc003/src/storage/storage.js"></script>
	<script src="js/cm/cmsc003/src/dom/dom.js"></script>
	<script src="js/cm/cmsc003/src/converter/converter.js"></script>
	<script src="js/cm/cmsc003/src/util/util.js"></script>
	
<script type="text/javascript" src="js/ci/cisc_map.js"></script>

<script type="text/javascript" src="js/ci/cisc005/event.js"></script>



<script type="text/javascript">
Proj4js.defs["EPSG:4326"] = "+proj=longlat +ellps=WGS84 +datum=WGS84 +no_defs";
Proj4js.defs["EPSG:4004"] = "+proj=longlat +ellps=bessel +no_defs +towgs84=-115.80,474.99,674.11,1.16,-2.31,-1.63,6.43";
Proj4js.defs["EPSG:4019"] = "+proj=longlat +ellps=GRS80 +no_defs";
Proj4js.defs["EPSG:3857"] = "+proj=merc +a=6378137 +b=6378137 +lat_ts=0.0 +lon_0=0.0 +x_0=0.0 +y_0=0 +k=1.0 +units=m +nadgrids=@null +no_defs";
Proj4js.defs["EPSG:32652"] = "+proj=utm +zone=52 +ellps=WGS84 +datum=WGS84 +units=m +no_defs";
Proj4js.defs["EPSG:32651"] = "+proj=utm +zone=51 +ellps=WGS84 +datum=WGS84 +units=m +no_defs";	
Proj4js.defs["EPSG:2096"] = "+proj=tmerc +lat_0=38 +lon_0=129 +k=1 +x_0=200000 +y_0=500000 +ellps=bessel +units=m +no_defs +towgs84=-115.80,474.99,674.11,1.16,-2.31,-1.63,6.43";
Proj4js.defs["EPSG:2097"] = "+proj=tmerc +lat_0=38 +lon_0=127 +k=1 +x_0=200000 +y_0=500000 +ellps=bessel +units=m +no_defs +towgs84=-115.80,474.99,674.11,1.16,-2.31,-1.63,6.43";
Proj4js.defs["EPSG:2098"] = "+proj=tmerc +lat_0=38 +lon_0=125 +k=1 +x_0=200000 +y_0=500000 +ellps=bessel +units=m +no_defs +towgs84=-115.80,474.99,674.11,1.16,-2.31,-1.63,6.43";
Proj4js.defs["EPSG:5173"] = "+proj=tmerc +lat_0=38 +lon_0=125.0028902777778 +k=1 +x_0=200000 +y_0=500000 +ellps=bessel +units=m +no_defs +towgs84=-115.80,474.99,674.11,1.16,-2.31,-1.63,6.43";
Proj4js.defs["EPSG:5174"] = "+proj=tmerc +lat_0=38 +lon_0=127.0028902777778 +k=1 +x_0=200000 +y_0=500000 +ellps=bessel +units=m +no_defs +towgs84=-115.80,474.99,674.11,1.16,-2.31,-1.63,6.43";
Proj4js.defs["EPSG:5175"] = "+proj=tmerc +lat_0=38 +lon_0=127.0028902777778 +k=1 +x_0=200000 +y_0=550000 +ellps=bessel +units=m +no_defs  +towgs84=-115.80,474.99,674.11,1.16,-2.31,-1.63,6.43";
Proj4js.defs["EPSG:5176"] = "+proj=tmerc +lat_0=38 +lon_0=129.0028902777778 +k=1 +x_0=200000 +y_0=500000 +ellps=bessel +units=m +no_defs +towgs84=-115.80,474.99,674.11,1.16,-2.31,-1.63,6.43";
Proj4js.defs["EPSG:5177"] =	"+proj=tmerc +lat_0=38 +lon_0=131.0028902777778 +k=1 +x_0=200000 +y_0=500000 +ellps=bessel +units=m +no_defs  +towgs84=-115.80,474.99,674.11,1.16,-2.31,-1.63,6.43";
Proj4js.defs["EPSG:5178"] =	"+proj=tmerc +lat_0=38 +lon_0=127.5 +k=0.9996 +x_0=1000000 +y_0=2000000 +ellps=bessel +units=m +no_defs +towgs84=-115.80,474.99,674.11,1.16,-2.31,-1.63,6.43";
Proj4js.defs["EPSG:5179"] = "+proj=tmerc +lat_0=38 +lon_0=127.5 +k=0.9996 +x_0=1000000 +y_0=2000000 +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs"; Proj4js.defs["EPSG:4326"] = "+proj=longlat +ellps=WGS84 +datum=WGS84 +no_defs";
Proj4js.defs["EPSG:5180"] = "+proj=tmerc +lat_0=38 +lon_0=125 +k=1 +x_0=200000 +y_0=500000 +ellps=GRS80 +units=m +no_defs"; 
Proj4js.defs["EPSG:5181"] = "+proj=tmerc +lat_0=38 +lon_0=127 +k=1 +x_0=200000 +y_0=500000 +ellps=GRS80 +units=m +no_defs";
Proj4js.defs["EPSG:5182"] = "+proj=tmerc +lat_0=38 +lon_0=127 +k=1 +x_0=200000 +y_0=550000 +ellps=GRS80 +units=m +no_defs";
Proj4js.defs["EPSG:5183"] = "+proj=tmerc +lat_0=38 +lon_0=129 +k=1 +x_0=200000 +y_0=500000 +ellps=GRS80 +units=m +no_defs";
Proj4js.defs["EPSG:5184"] = "+proj=tmerc +lat_0=38 +lon_0=131 +k=1 +x_0=200000 +y_0=500000 +ellps=GRS80 +units=m +no_defs";
Proj4js.defs["EPSG:5185"] = "+proj=tmerc +lat_0=38 +lon_0=125 +k=1 +x_0=200000 +y_0=600000 +ellps=GRS80 +units=m +no_defs";
Proj4js.defs["EPSG:5186"] = "+proj=tmerc +lat_0=38 +lon_0=127 +k=1 +x_0=200000 +y_0=600000 +ellps=GRS80 +units=m +no_defs";
Proj4js.defs["EPSG:5187"] = "+proj=tmerc +lat_0=38 +lon_0=129 +k=1 +x_0=200000 +y_0=600000 +ellps=GRS80 +units=m +no_defs";
Proj4js.defs["EPSG:5188"] = "+proj=tmerc +lat_0=38 +lon_0=131 +k=1 +x_0=200000 +y_0=600000 +ellps=GRS80 +units=m +no_defs";

var searchAbsolutList = [];
var searchRelativeList = [];

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


$(document).ready(function() {
	//컴포넌트 초기화
	fnInitComponent();
	
	// 재난 id 인풋 클릭
	$('#disasterId').click(function(e) {
		CMSC003.DOM.showDisasterIDSearchPopup('js-disast-relative');
	});
	
	// 재난 id 검색결과 클릭
    $('body').on('click', '#cmsc003-modal table.js-table-disaster-result tbody tr.js-disast-relative', function(e) {
    	if ($(e.currentTarget).children('td').length === 1) {
    		return;
    	}
    	var child = $(e.currentTarget).children('td:first-child');
    	$('#disasterId').val($(child).text());
    	
    	updateDisasterId(child);
    });
	
    var map = ciscMap.map;
    
// 	 map.addLayer(new ol.layer.Tile({
// 		isRequired: true,
// 		source: new ol.source.OSM()
// 	}));
	
	map.addLayer(selectionLayer);
	map.addLayer(selectionLayerPoint);
	
	// 절대방사 입력영상 변경
	$('#sel_input_photo').on('change', function() {
		var id = $(this).find(':selected').attr('storeid');
		CMSC003.Storage.set('currentAbsVid', id);
		fnChangeinputPictureNew(id);
	});
	
	// 상대방사 입력영상 변경
	$('#sel_photo_basic').on('change', function() {
		var id = $(this).find(':selected').attr('storeid');
		CMSC003.Storage.set('currentRelVid', id);
		fnChangeinputPictureNew(id);
	});
	
	// 상대방사 대상영상 변경
	$('#sel_photo_target').on('change', function() {
		var id = $(this).find(':selected').attr('storeid');
		CMSC003.Storage.set('currentRelVid2', id);
	});
});

function updateDisasterId(child) {
	var map = ciscMap.map;
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
	CMSC003.GIS.createROICenter(transform);	
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
		
		var bbox4326 = CMSC003.GIS.convert5179To4326Extent(transform_bbox);
		var bbox5186 = CMSC003.GIS.convert5179To5186Extent(transform_bbox);
		var bbox32652 = CMSC003.GIS.convert5179To32652Extent(transform_bbox);
		
		// 4326, 5186, 5179, 32652 순서
		var bboxArray = [bbox4326, bbox5186, transform_bbox, bbox32652];
		
		CMSC003.Storage.set('relBbox', bboxArray);
		
		// ROI 피처 생성
		CMSC003.GIS.createPolygonROIfrom5179(transform_bbox, selectionLayer, map);
		
	}
	
	$('#cmsc003-modal').remove();
}

//절대방사보정 검색	
function fnAbsoluteSearch(){
	
// 	if($("input[name='sate']:checked").length ==0){
// 		alert("위성종류를 선택하세요.");
// 		return false;
// 	}
	if($("#disasterIdCreate").val().trim() === ""){
		alert("재난 ID를 입력하세요.");
		return false;
	}
	
	if(!$("#dataKindCurrentAbs:checked").length && !$("#dataKindEmergencyAbs:checked").length && !$("#dataKindResultAbs:checked").length) {
		alert("데이터 종류를 선택하세요.");
		return false;
	}
	
	if(!$('input[name=dataTypeAbs]:checked').length) {
		alert("검색 대상을 선택하세요.");
		return false;
	}
	
// 	if($("#datepicker1").val()==""){
// 		alert("시작일자를 입력하세요");
// 		return false;
// 	}
	
// 	if($("#datepicker2").val()==""){
// 		alert("종료일자를 입력하세요");
// 		return false;
// 	}
	
// 	var searchKind = "";
//     $("input[name='sate']:checked").each(function(e){
//         var value = $(this).val();
        
//         if (searchKind == ""){
//         	searchKind = value;
//         }else{
//         	searchKind += ","+value;
//         }
        
//     });

//     var sate = "1,2"; 
//     var param={
//    		searchKind:searchKind,
//    		sate:sate,
//     	date1:$("#datepicker1").val(),
//     	date2:$("#datepicker2").val(),
//     	sResolution:$("#absolute_start_resolution").val(),
//     	eResolution:$("#absolute_end_resolution").val(),
//     	cloud:  $("input[name='absAmountCloud']:checked").val(),
//     	dataKind:'1'
//     };
    
	var param={
		disasterId: $('#disasterIdCreate').val(),
		dataKindCurrent: $("#dataKindCurrentAbs:checked").length ? 'on' : null,
		dataKindEmergency: $("#dataKindEmergencyAbs:checked").length ? 'on' : null,
		dataKindResult: $("#dataKindResultAbs:checked").length ? 'on' : null, 
		dataType: $('input[name=dataTypeAbs]:checked').val(),
// 		dateFrom: $("#datepicker1").val(),
// 		dateTo: $("#datepicker2").val(),
		
		ulx4326:null,
		uly4326:null,
		lrx4326:null,
		lry4326:null,
		
		ulx5186:null,
		uly5186:null,
		lrx5186:null,
		lry5186:null,
		
		ulx5179:null,
		uly5179:null,
		lrx5179:null,
		lry5179:null,
		
		ulx32652:null,
		uly32652:null,
		lrx32652:null,
		lry32652:null
    };
	
	var bbox = CMSC003.Storage.get('absBbox');
	
	if(bbox) {
		param['lrx4326'] = bbox[0][2];
		param['lry4326'] = bbox[0][1];
		param['ulx4326'] = bbox[0][0];
		param['uly4326'] = bbox[0][3];
		
		param['lrx5186'] = bbox[1][2];
		param['lry5186'] = bbox[1][1];
		param['ulx5186'] = bbox[1][0];
		param['uly5186'] = bbox[1][3];
		
		param['lrx5179'] = bbox[2][2];
		param['lry5179'] = bbox[2][1];
		param['ulx5179'] = bbox[2][0];
		param['uly5179'] = bbox[2][3];
		
		param['lrx32652'] = bbox[3][2];
		param['lry32652'] = bbox[3][1];
		param['ulx32652'] = bbox[3][0];
		param['uly32652'] = bbox[3][3];
	}
	
    $("#progress").show();
	$.ajax({
		type : "POST",
// 	    url: "cisc005/absSatSearch.do",
	    url: "/cmsc003searchOthers.do",
	    data:param,
	    success:function(data){
	    	$("#progress").hide();
	    	if (data){
	    		var objData = data;
	    		if(typeof objData === 'string') {
	    			objData = JSON.parse(objData); 
	    		}
	    		if(typeof objData === 'string') {
	    			objData = JSON.parse(objData); 
	    		}
	    		console.log(objData);
	    		
	    		// 기존에 보고있던 레이어를 삭제
	    		var map = ciscMap.map;
	    		CMSC003.GIS.resetLayers(map);
	    		
	    		CMSC003.DOM.showSearchResultTree(objData, $('#div_result_absolute_list')[0], $('#div_result_relative_list')[0]);
// 	    		fnResultAbsolutList(JSON.parse(data));
	    	}
	    },
	    error:function(data){
	    	$("#progress").hide();
	    	alert("실패", data);
	    }
	});
}

//상대방사보정 검색
function fnRelativeSearch(){
// 	if($("input[name='chk_photo']:checked").length ==0){
// 		alert("영상종류를 선택하세요.");
// 		return false;
// 	}
	
// 	if($("#datepicker3").val()==""){
// 		alert("시작일자를 입력하세요");
// 		return false;
// 	}
	
// 	if($("#datepicker4").val()==""){
// 		alert("종료일자를 입력하세요");
// 		return false;
// 	}
	
// 	if ($("#disasterId").val() == "") {
//         alert("재난 ID를 입력하세요");
//         return false;
//     }
	
// 	var searchKind = "";
//     $("input[name='chk_photo']:checked").each(function(e){
//         var value = $(this).val();
//         if (searchKind == ""){
//         	searchKind = value;
//         }else{
//         	searchKind += ","+value;
//         }
        
//     });
    
    
//     $('#progress').show();
// 	var param={
// 			searchKind:searchKind,    	
// 	    	date1:$("#datepicker3").val(),
// 	    	date2:$("#datepicker4").val(),
// 	    	sResolution:$("#relative_start_resolution").val(),
// 	    	eResolution:$("#relative_end_resolution").val(),
// 	    	cloud:$("input[name='radio_cloud']:checked").val(),
// 	    	dataKind:$("input[name='radio_db']:checked").val(),
// 	    	disasterId: $("#disasterId").val()
// 	    };
// 	$("#progress").show();

// 	var searchKind = "";
//     $("input[name='sate']:checked").each(function(e){
//         var value = $(this).val();
        
//         if (searchKind == ""){
//         	searchKind = value;
//         }else{
//         	searchKind += ","+value;
//         }
        
//     });

	if($("#disasterId").val().trim() === ""){
		alert("재난 ID를 입력하세요.");
		return false;
	}
	
	if(!$("#dataKindCurrent:checked").length && !$("#dataKindEmergency:checked").length && !$("#dataKindResult:checked").length) {
		alert("데이터 종류를 선택하세요.");
		return false;
	}
	
	if(!$('input[name=dataType]:checked').length) {
		alert("검색 대상을 선택하세요.");
		return false;
	}
	
// 	if($("#datepicker3").val()==""){
// 		alert("시작일자를 입력하세요");
// 		return false;
// 	}
	
// 	if($("#datepicker4").val()==""){
// 		alert("종료일자를 입력하세요");
// 		return false;
// 	}
	
	var param={
		disasterId: $('#disasterId').val(),
		dataKindCurrent: $("#dataKindCurrent:checked").length ? 'on' : null,
		dataKindEmergency: $("#dataKindEmergency:checked").length ? 'on' : null,
		dataKindResult: $("#dataKindResult:checked").length ? 'on' : null, 
		dataType: $('input[name=dataType]:checked').val(),
// 		dateFrom: $("#datepicker3").val(),
// 		dateTo: $("#datepicker4").val(),
		
		ulx4326:null,
		uly4326:null,
		lrx4326:null,
		lry4326:null,
		
		ulx5186:null,
		uly5186:null,
		lrx5186:null,
		lry5186:null,
		
		ulx5179:null,
		uly5179:null,
		lrx5179:null,
		lry5179:null,
		
		ulx32652:null,
		uly32652:null,
		lrx32652:null,
		lry32652:null
    };
	
	var bbox = CMSC003.Storage.get('relBbox');
	
	if(bbox) {
		param['lrx4326'] = bbox[0][2];
		param['lry4326'] = bbox[0][1];
		param['ulx4326'] = bbox[0][0];
		param['uly4326'] = bbox[0][3];
		
		param['lrx5186'] = bbox[1][2];
		param['lry5186'] = bbox[1][1];
		param['ulx5186'] = bbox[1][0];
		param['uly5186'] = bbox[1][3];
		
		param['lrx5179'] = bbox[2][2];
		param['lry5179'] = bbox[2][1];
		param['ulx5179'] = bbox[2][0];
		param['uly5179'] = bbox[2][3];
		
		param['lrx32652'] = bbox[3][2];
		param['lry32652'] = bbox[3][1];
		param['ulx32652'] = bbox[3][0];
		param['uly32652'] = bbox[3][3];
	}
	$("#progress").show();
	
	$.ajax({
		type : "POST",
	    // url: "cisc005/relPhotoSearch.do",
	    url: "/cmsc003searchOthers.do",
	    data:param,
	    success:function(data){

	    	$("#progress").hide();
	    	if (data){
	    		// fnResultRelativeList(JSON.parse(data));
	    		var objData = data;
	    		if(typeof objData === 'string') {
	    			objData = JSON.parse(objData); 
	    		}
	    		if(typeof objData === 'string') {
	    			objData = JSON.parse(objData); 
	    		}
	    		console.log(objData);
	    		
	    		// 기존에 보고있던 레이어를 삭제
	    		var map = ciscMap.map;
	    		CMSC003.GIS.resetLayers(map);
	    		
	    		CMSC003.DOM.showSearchResultTree(objData, $('#div_result_relative_list')[0], $('#div_result_absolute_list')[0]);
	    	}
	    	$('#progress').hide();
	    },
	    error:function(data){
	    	$("#progress").hide();
	    	alert("실패", data);
	    }
	});
}


function transform(x,y, sourceCrs, targetCrs){
    
	var pt = new Proj4js.Point(x,y);    
    var s_srs = new Proj4js.Proj(sourceCrs);
    var t_srs = new Proj4js.Proj(targetCrs);
    
    if (sourceCrs == 'EPSG:5179'  && targetCrs == 'EPSG:5179'){
    	return pt;
    }
    
    if (sourceCrs == 'UTM'){
    	return pt;
    }
    
    var result = Proj4js.transform(s_srs,t_srs,pt);
    return result;
}


function fnViewer(id, filename, x1, y1, x2, y2){
	
	var minx, mainy, maxx, maxy;
	
	minx = x1;
	maxx = x2;
	miny = y1;
	maxy = y2;
	
	if (minx > maxx){
		minx = x2;
		maxx = x1;
	}
	if (miny > maxy){
		miny = y2;
		maxy = y1;
	}	
	
	ciscMap.viewImgLayer(id, filename, minx, miny, maxx, maxy, '');	

}
function fnResultAbsolutList(data){

	searchAbsolutList.splice(0,searchAbsolutList.length);
	$("#div_result_absolute_list").empty();
	
	var soilList = data.soilList;
	var etcList = data.etcList;
	
	if (soilList){
		for(var i=0; i < soilList.length; i++){
			
			var min = transform(parseFloat(soilList[i].ltopCrdntY), parseFloat(soilList[i].ltopCrdntX), soilList[i].mapPrjctnCn, 'EPSG:5179');
		    var max = transform(parseFloat(soilList[i].rbtmCrdntY), parseFloat(soilList[i].rbtmCrdntX), soilList[i].mapPrjctnCn, 'EPSG:5179');
	        
			var soilObj = {
					satKind :		   "SOIL",
					vidoId :		   soilList[i].vidoId,
					potogrfBeginDt :   soilList[i].potogrfBeginDt,
					potogrfEndDt : 	   soilList[i].potogrfEndDt,
					ltopCrdntX : 	   soilList[i].ltopCrdntX,
					ltopCrdntY : 	   soilList[i].ltopCrdntY,
					rbtmCrdntX : 	   soilList[i].rbtmCrdntX,
					rbtmCrdntY :	   soilList[i].rbtmCrdntY,
					innerFileCoursNm : soilList[i].innerFileCoursNm,
					potogrfVidoCd :    soilList[i].potogrfVidoCd,
					//extent : 		   ol.proj.transformExtent([data[i].ltopCrdntY, data[i].ltopCrdntX, data[i].rbtmCrdntY, data[i].rbtmCrdntX], 'EPSG:4326', 'EPSG:3857'),
					minX : min.x,
					minY : min.y,
					maxX : max.x,
					maxY : max.y,
					fileName : 	   	   soilList[i].fileName,
					imgFullFileName :  soilList[i].imgFullFileName,
					tifFileName :	   soilList[i].tifFileName,	 
					satName  :         soilList[i].satName,
					gain  	:          soilList[i].gain,
					offset  :  	       soilList[i].offset,				
					radianceMult  :    soilList[i].radianceMult,
					radianceAdd  :     soilList[i].radianceAdd,
					reflectanceMult :  soilList[i].reflectanceMult,
					reflectanceAdd  :  soilList[i].reflectanceAdd,
					metaData  :  	   soilList[i].metaData,
					dirName   :        soilList[i].dirName,
					mapPrjctnCn :      soilList[i].mapPrjctnCn
			};
			searchAbsolutList.push(soilObj);
		}
	}
	
	if (etcList){
		for(var i=0; i < etcList.length; i++){
			var min = transform(parseFloat(etcList[i].ltopCrdntY), parseFloat(etcList[i].ltopCrdntX), etcList[i].mapPrjctnCn, 'EPSG:5179');
	        var max = transform(parseFloat(etcList[i].rbtmCrdntY), parseFloat(etcList[i].rbtmCrdntX), etcList[i].mapPrjctnCn, 'EPSG:5179');
	        
	        var etcObj = {
					satKind :		   "ETC",
					vidoId :		   etcList[i].vidoId,
					potogrfBeginDt :   etcList[i].potogrfBeginDt,
					potogrfEndDt : 	   etcList[i].potogrfEndDt,
					ltopCrdntX : 	   etcList[i].ltopCrdntX,
					ltopCrdntY : 	   etcList[i].ltopCrdntY,
					rbtmCrdntX : 	   etcList[i].rbtmCrdntX,
					rbtmCrdntY :	   etcList[i].rbtmCrdntY,
					innerFileCoursNm : etcList[i].innerFileCoursNm,
					potogrfVidoCd :    etcList[i].potogrfVidoCd,
					//extent : 		   ol.proj.transformExtent([data[i].ltopCrdntY, data[i].ltopCrdntX, data[i].rbtmCrdntY, data[i].rbtmCrdntX], 'EPSG:4326', 'EPSG:3857'),
					minX : min.x,
					minY : min.y,
					maxX : max.x,
					maxY : max.y,
					fileName : 	   	   etcList[i].fileName,
					imgFullFileName :  etcList[i].imgFullFileName,
					tifFileName :	   etcList[i].tifFileName,
					satName  :         etcList[i].satName,
					gain  	:          etcList[i].gain,
					offset  :  	       etcList[i].offset,
					radianceMult  :    etcList[i].radianceMult,
					radianceAdd  :     etcList[i].radianceAdd,
					reflectanceMult :  etcList[i].reflectanceMult,
					reflectanceAdd  :  etcList[i].reflectanceAdd,
					metaData  :  	   etcList[i].metaData,
					dirName   :        etcList[i].dirName,
					mapPrjctnCn :      etcList[i].mapPrjctnCn
			};
			searchAbsolutList.push(etcObj);
		}
	}
	
	var landcnt = 0, kcompcnt = 0; etccnt = 0, soilcnt = 0;;
	for(var i=0; i < searchAbsolutList.length; i++){
		if (searchAbsolutList[i].satKind == 'ETC'){
			etccnt++
			if (searchAbsolutList[i].potogrfVidoCd == '1'){
				landcnt++;
			}else if (searchAbsolutList[i].potogrfVidoCd == '2'){
				kcompcnt++;
			}
		}else if (searchAbsolutList[i].satKind == 'SOIL'){
			soilcnt++;
		}
	}
	
	var strHtml  = '';
	
	if ($("#sate_soil").prop("checked")){
		if (soilcnt > 0){
			var strHtml  = '<dl class="result-list-input">';
			strHtml += '<dt>국토위성 ('+soilcnt+')</dt>';
			for(var i=0; i < searchAbsolutList.length; i++){
				if (searchAbsolutList[i].satKind == 'SOIL'){
					strHtml += '<dd><a href="#" onclick="fnViewer(\''+searchAbsolutList[i].vidoId+'\', \''+searchAbsolutList[i].imgFullFileName+'\', '+searchAbsolutList[i].minX+', '+searchAbsolutList[i].minY+','+searchAbsolutList[i].maxX+','+searchAbsolutList[i].maxY+');">'+searchAbsolutList[i].innerFileCoursNm+'</a></dd>';
				}
			}
			strHtml += '</dl>';
			$("#div_result_absolute_list").append(strHtml);
			
			
			$("#sel_input_photo").empty();
			$.each(searchAbsolutList, function (index, item){
				$("#sel_input_photo").append("<option value="+item.vidoId+">"+(index +1)+". "+item.innerFileCoursNm+"</option>");
				if (index == 0){
					
					if (item.potogrfVidoCd == "1"){
						$("#txt_correction").val('Radiance = '+item.radianceMult+' X DN + '+item.radianceAdd);
						$("#txt_toa").val('TOA Reflectance = '+item.reflectanceMult+' X DN + '+item.reflectanceAdd);
						$("#sel_sat option:eq(2)").prop("selected", true);
						fnChangeSat('1');
					}else{
						$("#txt_correction").val('Radiance = '+item.gain+' X DN + '+item.offset);
						$("#sel_sat option:eq(1)").prop("selected", true);
						fnChangeSat('2');
					}
					$("#txt_result").val('AbsRadResult_');					
// 					$("#txt_result").val('AbsRadResult_'+item.satName+".tif");					
// 					$("#txt_result").val(item.dirName+'Result_'+item.satName+".tif");					
					$("#txt_meta").val(item.dirName+item.metaData);
					$("#txt_toa_output_file").val('TOA_AbsRadResult_');
				}
			});
			
		}else{
			var strHtml  = '<dl class="result-list-input">';
			strHtml += '<dt>국토위성 ('+soilcnt+')</dt>';
			strHtml += '<dd>검색된 데이터가 없습니다.</dd></dl>';
			$("#div_result_absolute_list").append(strHtml);
		}
	}
	
	
	if ($("#sate_etc").prop("checked")){
		if (etccnt > 0){
			var strHtml  = '<dl class="result-list-input">';
			strHtml += '<dt>기타위성 ('+etccnt+')</dt>';
			for(var i=0; i < searchAbsolutList.length; i++){
				if (searchAbsolutList[i].satKind == 'ETC'){
					if (searchAbsolutList[i].potogrfVidoCd == '1'){
						strHtml += '<dd><a href="#" onclick="fnViewer(\''+searchAbsolutList[i].vidoId+'\', \''+searchAbsolutList[i].imgFullFileName+'\', '+searchAbsolutList[i].minX+', '+searchAbsolutList[i].minY+','+searchAbsolutList[i].maxX+','+searchAbsolutList[i].maxY+');"> (Landsat)'+searchAbsolutList[i].innerFileCoursNm+'</a></dd>';
						//strHtml+= '<dd><code class="year">시작일자</code>'+searchAbsolutList[i].potogrfBeginDt+'</dd>';
					}else if (searchAbsolutList[i].potogrfVidoCd == '2'){
						strHtml += '<dd><a href="#" onclick="fnViewer(\''+searchAbsolutList[i].vidoId+'\', \''+searchAbsolutList[i].imgFullFileName+'\', '+searchAbsolutList[i].minX+', '+searchAbsolutList[i].minY+','+searchAbsolutList[i].maxX+','+searchAbsolutList[i].maxY+');"> (Kompsat)'+searchAbsolutList[i].innerFileCoursNm+'</a></dd>';
					}
					else if (searchAbsolutList[i].potogrfVidoCd == '6'){
						strHtml += '<dd><a href="#" onclick="fnViewer(\''+searchAbsolutList[i].vidoId+'\', \''+searchAbsolutList[i].imgFullFileName+'\', '+searchAbsolutList[i].minX+', '+searchAbsolutList[i].minY+','+searchAbsolutList[i].maxX+','+searchAbsolutList[i].maxY+');"> (sentinel)'+searchAbsolutList[i].innerFileCoursNm+'</a></dd>';
					}
				}
			}
			strHtml += '</dl>';
			$("#div_result_absolute_list").append(strHtml);
			
			//기타위성일 경우만
			$("#sel_input_photo").empty();
			$.each(searchAbsolutList, function (index, item){
				$("#sel_input_photo").append("<option value="+item.vidoId+">"+(index +1)+". "+item.innerFileCoursNm+"</option>");
				if (index == 0){
					
					if (item.potogrfVidoCd == "1"){
						$("#txt_correction").val('Radiance = '+item.radianceMult+' X DN + '+item.radianceAdd);
						$("#txt_toa").val('TOA Reflectance = '+item.reflectanceMult+' X DN + '+item.reflectanceAdd);
						$("#sel_sat option:eq(2)").prop("selected", true);
						fnChangeSat('1');
					}else{
						$("#txt_correction").val('Radiance = '+item.gain+' X DN + '+item.offset);
						$("#sel_sat option:eq(1)").prop("selected", true);
						fnChangeSat('2');
					}
					$("#txt_result").val('AbsRadResult_');					
// 					$("#txt_result").val('AbsRadResult_'+item.satName+".tif");					
// 					$("#txt_result").val(item.dirName+'Result_'+item.satName+".tif");					
					$("#txt_meta").val(item.dirName+item.metaData);
					$("#txt_toa_output_file").val('TOA_AbsRadResult_');
				}
			});
			
			
		}else{
			var strHtml  = '<dl class="result-list-input">';
			strHtml += '<dt>기타위성 ('+etccnt+')</dt>';
			strHtml += '<dd>검색된 데이터가 없습니다.</dd></dl>';
			$("#div_result_absolute_list").append(strHtml);
		}
		
		
	}
	
	
	/* if (landcnt > 0){
		var strHtml  = '<dl class="result-list-input">';
		strHtml += '<dt>Landsat ('+landcnt+')</dt>';
		for(var i=0; i < searchAbsolutList.length; i++){
			if (searchAbsolutList[i].satKind == 'ETC'){
				if (searchAbsolutList[i].potogrfVidoCd == '1'){
					strHtml += '<dd><a href="#" onclick="fnViewer(\''+searchAbsolutList[i].vidoId+'\', \''+searchAbsolutList[i].imgFullFileName+'\', '+searchAbsolutList[i].minX+', '+searchAbsolutList[i].minY+','+searchAbsolutList[i].maxX+','+searchAbsolutList[i].maxY+');">'+searchAbsolutList[i].innerFileCoursNm+'</a></dd>';
					//strHtml+= '<dd><code class="year">시작일자</code>'+searchAbsolutList[i].potogrfBeginDt+'</dd>';
				}
			}
		}
		strHtml += '</dl>';
		$("#div_result_absolute_list").append(strHtml);
	}
	if (kcompcnt > 0){
		var strHtml  = '<dl class="result-list-input">';
		strHtml += '<dt>Kompsat ('+kcompcnt+')</dt>';
		for(var i=0; i < searchAbsolutList.length; i++){
			if (searchAbsolutList[i].satKind == 'ETC'){
				if (searchAbsolutList[i].potogrfVidoCd == '2'){
					strHtml += '<dd><a href="#" onclick="fnViewer(\''+searchAbsolutList[i].vidoId+'\', \''+searchAbsolutList[i].imgFullFileName+'\', '+searchAbsolutList[i].minX+', '+searchAbsolutList[i].minY+','+searchAbsolutList[i].maxX+','+searchAbsolutList[i].maxY+');">'+searchAbsolutList[i].innerFileCoursNm+'</a></dd>';
					// strHtml+= '<dd><code class="year">시작일자</code>'+searchAbsolutList[i].potogrfBeginDt+'</dd>';
				}
			}
		}
		strHtml += '</dl>';
		$("#div_result_absolute_list").append(strHtml);
	} */
	
	


}




function fnInitComponent(){
	
	//절대방사보정
	/* $("#start_resolution").empty();
	$("#start_resolution").append("<option value='12'>12</option>");
	$("#start_resolution").append("<option selected value='13'>13</option>");
	
	$("#end_resolution").empty();
	$("#end_resolution").append("<option value='50'>50</option>");
	$("#end_resolution").append("<option selected value='60'>60</option> "); */
	
	/* $("#datepicker1").val('2020-04-23');
	$("#datepicker2").val('2020-04-26'); */
	//$("#datepicker1").val('2018-04-25');
	//$("#datepicker2").val('2018-05-03');
	
	
	//상대방사보정
	/* $("#start_relative_resolution").empty();
	$("#start_relative_resolution").append("<option value='12'>12</option>");
	$("#start_relative_resolution").append("<option selected value='13'>13</option>");
	
	$("#end_relative_resolution").empty();
	$("#end_relative_resolution").append("<option value='50'>50</option>");
	$("#end_relative_resolution").append("<option selected value='60'>60</option> "); */
	
	/* $("#datepicker3").val('2020-04-23');
	$("#datepicker4").val('2020-04-26'); */
	//$("#datepicker3").val('2018-04-25');
	//$("#datepicker4").val('2018-05-03');
	
	$("#sel_algorithm").empty();
	
	$("#progress").show();
	$.ajax({
		type : "POST",
	    url: "cisc001/searchAlgorithmList.do",
	    data:{"workKind":'2'},
	    success:function(data){
	    	var strHtml = ""; 
	    	
	    	$.each(data.resultList, function(index, item){
	    		$("#sel_algorithm").append("<option value="+item.algorithmId+">"+item.algorithmNm+"</option>");
			});
			
			$("#progress").hide();
	    },
	    error:function(data){
	    	$("#progress").hide();
	    	alert("실패", data);
	    }
	});

	
}
function fnChangeSat(val){
	$("#div_toa").hide();
	$("#div_toa_output").hide();
	
	if (val == '1'){  //Landsat
		$("#div_toa").show();
		$("#div_toa_output").show();
	}
}

function fnChangeinputPictureNew(val){
	var list = CMSC003.Storage.get('searchDataMap');
	var layer = list[val];
	if(layer) {
		console.log(layer);
		
		if (layer.potogrfVidoCd == "1"){  //Landsat
			$("#txt_correction").val('Radiance = '+layer.radianceMult+' X DN + '+layer.radianceAdd);
			$("#txt_toa").val('TOA Reflectance = '+layer.reflectanceMult+' X DN + '+layer.reflectanceAdd);
			
			// 넣는거 맞는지 확인 필요
			fnChangeSat('1');
		}else if (layer.potogrfVidoCd == "2"){  //Komsat
			$("#txt_correction").val('Radiance = '+layer.gain+' X DN + '+layer.offset);
		
			// 넣는거 맞는지 확인 필요
			fnChangeSat('2');
		}		
		//$("#txt_result").val('Result_'+layer.satName+".tif");
		$("#txt_meta").val(layer.dirName+layer.metaData);
		$("#txt_toa_output_file").val('TOA_Result_');

		//$("#txt_meta").val(searchAbsolutList[i].metaData);
	}
// 	for(var i=0; i < searchAbsolutList.length; i++){
// 		if (val == searchAbsolutList[i].vidoId){
// 			if (searchAbsolutList[i].potogrfVidoCd == "1"){  //Landsat
// 				$("#txt_correction").val('Radiance = '+searchAbsolutList[i].radianceMult+' X DN + '+searchAbsolutList[i].radianceAdd);
// 				$("#txt_toa").val('TOA Reflectance = '+searchAbsolutList[i].reflectanceMult+' X DN + '+searchAbsolutList[i].reflectanceAdd);
// 			}else if (searchAbsolutList[i].potogrfVidoCd == "2"){  //Komsat
// 				$("#txt_correction").val('Radiance = '+searchAbsolutList[i].gain+' X DN + '+searchAbsolutList[i].offset);
// 			}		
// // 			$("#txt_result").val(searchAbsolutList[i].dirName+'Result_'+searchAbsolutList[i].satName+".tif");
// 			$("#txt_result").val('Result_'+searchAbsolutList[i].satName+".tif");
// 			$("#txt_meta").val(searchAbsolutList[i].dirName+searchAbsolutList[i].metaData);
// // 			$("#txt_toa_output_file").val(searchAbsolutList[i].dirName+'TOA_Result_'+searchAbsolutList[i].satName+".tif");
// 			$("#txt_toa_output_file").val('TOA_Result_'+searchAbsolutList[i].satName+".tif");
		
// 			//$("#txt_meta").val(searchAbsolutList[i].metaData);
			
// 			break;
// 		}
// 	}	
}

function fnChangeinputPicture(val){
	for(var i=0; i < searchAbsolutList.length; i++){
		if (val == searchAbsolutList[i].vidoId){
			if (searchAbsolutList[i].potogrfVidoCd == "1"){  //Landsat
				$("#txt_correction").val('Radiance = '+searchAbsolutList[i].radianceMult+' X DN + '+searchAbsolutList[i].radianceAdd);
				$("#txt_toa").val('TOA Reflectance = '+searchAbsolutList[i].reflectanceMult+' X DN + '+searchAbsolutList[i].reflectanceAdd);
			}else if (searchAbsolutList[i].potogrfVidoCd == "2"){  //Komsat
				$("#txt_correction").val('Radiance = '+searchAbsolutList[i].gain+' X DN + '+searchAbsolutList[i].offset);
			}		
// 			$("#txt_result").val(searchAbsolutList[i].dirName+'Result_'+searchAbsolutList[i].satName+".tif");
			$("#txt_result").val('Result_'+searchAbsolutList[i].satName+".tif");
			$("#txt_meta").val(searchAbsolutList[i].dirName+searchAbsolutList[i].metaData);
// 			$("#txt_toa_output_file").val(searchAbsolutList[i].dirName+'TOA_Result_'+searchAbsolutList[i].satName+".tif");
			$("#txt_toa_output_file").val('TOA_Result_'+searchAbsolutList[i].satName+".tif");
		
			//$("#txt_meta").val(searchAbsolutList[i].metaData);
			
			break;
		}
	}	
}

function fnAbsoluteProcess(){
	
	if( ! confirm("절대방사보정 작업을 진행 하시겠습니까? ")) {
		return;
	}
	
	var did = $('#disasterIdCreate').val();
	if(did.trim() === '') {
		alert('재난 ID를 입력해주세요.');
		return;
	}
	
	var param = {
			satKind : "2",
			inFilePath : "",
			outRadianceFilePath : "",
			gain: "",   //gain
			offset : "",    //offset */
			TOAReflectanceFilePath : "",
			ReflectanceMultiple : "", //(2.0000E-05);		//REFLECTANCE BAND 1
			ReflectanceAddtion : "",  //(-0.100000);
			RadianceMultiple : "",    //(1.2524E-02);			//RADIANCE BAND 1
			RadianceAddtion : "",     //(-62.62227);	
			radiatingCorrection : "",
			radiatingFormula:"",
			toaFormula : "",
			metaData : "",
			disasterId: did
	};
	
	var vidoId, minX, minY, maxX, maxY;
	
	var id = CMSC003.Storage.get('currentAbsVid');
	var list = CMSC003.Storage.get('searchDataMap');
	var layer = list[id];
	if(layer) {
		param.satKind = layer.potogrfVidoCd,
		param.inFilePath = layer.dirName+layer.tifFileName; //"36807062s_R.tif"
		//param.inFilePath = $("#sel_input_photo").val();			
		//param.outRadianceFilePath = layer.dirName+ $("#txt_result").val() + ".tif";
		param.outRadianceFilePath =  $("#txt_result").val();
		param.gain = layer.gain;
		param.offset = layer.offset;
		//param.TOAReflectanceFilePath = layer.dirName+'TOA_Result_'+layer.satName+".tif";
		param.TOAReflectanceFilePath = $("#txt_toa_output_file").val();			
		
		param.RadianceMultiple = layer.radianceMult; //(2.0000E-05);		//REFLECTANCE BAND 1
		param.RadianceAddtion = layer.radianceAdd;  //(-0.100000);
		param.ReflectanceMultiple = layer.reflectanceMult;    //(1.2524E-02);			//RADIANCE BAND 1
		param.ReflectanceAddtion = layer.reflectanceAdd;     //(-62.62227);
		param.radiatingFormula = $("#txt_correction").val();
		param.toaFormula = $("#txt_toa").val();
		param.metaData = $("#txt_meta").val();			
		vidoId = layer.vidoId;
		minX = layer.minX;
		minY = layer.minY;
		maxX = layer.maxX;
		maxY = layer.maxY;
		
		if(!minX) {
			minX = layer.ltopCrdntY;
		}
		if(!minY) {
			minY = layer.rbtmCrdntX;
		}
		if(!maxX) {
			maxX = layer.rbtmCrdntY;
		}
		if(!maxY) {
			maxY = layer.ltopCrdntX;
		}
		
		if(minX && minY && maxX && maxY) {
			var extent = [minX, minY, maxX, maxY];
			extent = CMSC003.GIS.convert4326To5179Extent(extent);
			minX = extent[0]; 
			minY = extent[1];
			maxX = extent[2];
			maxY = extent[3];
		}
	}
	
	
// 	for(var i=0; i < searchAbsolutList.length; i++){
// 		if ($("#sel_input_photo").val() == searchAbsolutList[i].vidoId){
// 			param.satKind = searchAbsolutList[i].potogrfVidoCd,
// 			param.inFilePath = searchAbsolutList[i].dirName+searchAbsolutList[i].tifFileName; //"36807062s_R.tif"
// 			//param.inFilePath = $("#sel_input_photo").val();			
// 			//param.outRadianceFilePath = searchAbsolutList[i].dirName+ $("#txt_result").val() + ".tif";
// 			param.outRadianceFilePath =  $("#txt_result").val();
// 			param.gain = searchAbsolutList[i].gain;
// 			param.offset = searchAbsolutList[i].offset;
// 			//param.TOAReflectanceFilePath = searchAbsolutList[i].dirName+'TOA_Result_'+searchAbsolutList[i].satName+".tif";
// 			param.TOAReflectanceFilePath = $("#txt_toa_output_file").val();			
			
// 			param.RadianceMultiple = searchAbsolutList[i].radianceMult; //(2.0000E-05);		//REFLECTANCE BAND 1
// 			param.RadianceAddtion = searchAbsolutList[i].radianceAdd;  //(-0.100000);
// 			param.ReflectanceMultiple = searchAbsolutList[i].reflectanceMult;    //(1.2524E-02);			//RADIANCE BAND 1
// 			param.ReflectanceAddtion = searchAbsolutList[i].reflectanceAdd;     //(-62.62227);
// 			param.radiatingFormula = $("#txt_correction").val();
// 			param.toaFormula = $("#txt_toa").val();
// 			param.metaData = $("#txt_meta").val();			
// 			vidoId = searchAbsolutList[i].vidoId;
// 			minX = searchAbsolutList[i].minX;
// 			minY = searchAbsolutList[i].minY;
// 			maxX = searchAbsolutList[i].maxX;
// 			maxY = searchAbsolutList[i].maxY;
			
// 			break;
// 		}
// 	}
	
	$("#progress").show();
	$.ajax({
		type : "POST",
	    url: "cisc005/absoluteRadiatingCorrection.do",
	    data:param,
	    success:function(data){
	    	var result = JSON.parse(data);	    	
	    	
	    	if (result.procCode == 'SUCCESS'){
	    		$("#progress").hide();
	    		alert('정상적으로 처리되었습니다.');
	    		//$("#div_absolute_result").append('<dd>'+$("#txt_result").val()+'</dd>')
	    		$("#div_absolute_result").append('<dd><a href="#" onclick="fnViewer(\''+vidoId+'\', \''+result.thumFullName+'\', '+minX+', '+minY+','+maxX+','+maxY+');">'+result.resultFileName+'</a></dd>');
	    		
	    		//if (param.satKind == "1"){
	    		if ($("#sel_sat").val() == "1"){	
	    			$("#div_absolute_result").append('<dd><a href="#" onclick="fnViewer(\''+vidoId+'\', \''+result.thumToaFullName+'\', '+minX+', '+minY+','+maxX+','+maxY+');">'+result.resultToaFileName+'</a></dd>');
	    		}    		
	    		
	    		$('#absoluteModal').modal("hide");
	    		
	    	}else{
	    		$("#progress").hide();
	    		alert('처리도중 에러가 발생했습니다.['+ result.procCode+']');
	    		return;
	    	}
	    },
	    error:function(data){
	    	$("#progress").hide();
	    	alert("실패", data);
	    }
	});
}

function fnScriptSavePopup(kind){
	$('#script_kind').val(kind);
	$('#scriptModal').modal('show');
}
	
	
function fnScriptNameCheck(scriptNm ){
	
	var ret = false;
	$("#progress").show();
	$.ajax({
		type : "POST",
		async:false,
	    url: "cisc001/searchScriptList.do",
	    data:{"scriptNm":scriptNm},
	    success:function(data){
	    	$("#progress").hide();
	    	if (data.resultList.length > 0){
	    		
	    		ret = true;
	    	}
	    	
	    },
	    error:function(data){
	    	
	    }
	}); 	
	
	return ret;
}
	
function fnScriptSave(){
	
	if ($("#input_script_nm").val() == ""){
		alert('스크립트명[영문]을 입력하세요.');
		return false;
	}
	
	if( ! confirm("스크립트를 저장 하시겠습니까? ")) {
		return;
	}
	if (fnScriptNameCheck($("#input_script_nm").val())){
		alert('입력하신 스크립트명이 존재합니다. 다른이름을 입력하세요.' );
		return;
	}
	
	var workKind = $('#script_kind').val();
	
	if(workKind == ''){
		workKind = '2';
	}
	
	var param = {
			scriptId:"",  //생성은 id ''
			workKind:workKind,  //1:대기보정,2:절대방사보정,3:상대방사보정
			scriptNm:$("#input_script_nm").val(),
			satKind:'1',  //1.Landsat, 2.Kompsat
			metaDataNm:'',				
			inputFileNm:'',			
			outputFileNm:'',				
			gain:'',
			offset:'',
			reflectGain:'',
			reflectOffset:'',
			radiatingFormula:'',
			toaOutputFileNm:'',
			histogramArea:'',
			algorithmNm:'',
			targetFileNm:''
	}
	if (workKind == '2'){  //절대방사보정
		
		var id = CMSC003.Storage.get('currentAbsVid');
		var list = CMSC003.Storage.get('searchDataMap');
		var layer = list[id];
		
		if(layer) {
			param.satKind = layer.potogrfVidoCd,
			param.metaDataNm = $("#txt_meta").val();	
			param.inputFileNm = layer.dirName+layer.tifFileName; //"36807062s_R.tif"
			param.outputFileNm =  $("#txt_result").val();
			if (layer.potogrfVidoCd == '1'){
				param.gain = layer.radianceMult;
				param.offset = layer.radianceAdd;
				param.reflectGain = layer.reflectanceMult;    //(1.2524E-02);			//RADIANCE BAND 1
				param.reflectOffset = layer.reflectanceAdd;     //(-62.62227);
				param.toaFormula = $("#txt_toa").val();
				param.toaOutputFileNm = $("#txt_toa_output_file").val();	
			}else{
				param.gain = layer.gain;
				param.offset = layer.offset;
				param.radiatingFormula = $("#txt_correction").val();
			}	
		}
		
// 		for(var i=0; i < searchAbsolutList.length; i++){
// 			if ($("#sel_input_photo").val() == searchAbsolutList[i].vidoId){
// 				param.satKind = searchAbsolutList[i].potogrfVidoCd,
// 				param.metaDataNm = $("#txt_meta").val();	
// 				param.inputFileNm = searchAbsolutList[i].dirName+searchAbsolutList[i].tifFileName; //"36807062s_R.tif"
// 				param.outputFileNm =  $("#txt_result").val();
// 				if (searchAbsolutList[i].potogrfVidoCd == '1'){
// 					param.gain = searchAbsolutList[i].radianceMult;
// 					param.offset = searchAbsolutList[i].radianceAdd;
// 					param.reflectGain = searchAbsolutList[i].reflectanceMult;    //(1.2524E-02);			//RADIANCE BAND 1
// 					param.reflectOffset = searchAbsolutList[i].reflectanceAdd;     //(-62.62227);
// 					param.toaFormula = $("#txt_toa").val();
// 					param.toaOutputFileNm = $("#txt_toa_output_file").val();	
// 				}else{
// 					param.gain = searchAbsolutList[i].gain;
// 					param.offset = searchAbsolutList[i].offset;
// 					param.radiatingFormula = $("#txt_correction").val();
// 				}	
				
// 				break;
// 			}
// 		}
	}else{  
		//상대방사보정
		param.histogramArea=$("input[name='histogram_radio']:checked").val();
		param.algorithmNm= $("#sel_algorithm option:selected").text();
		param.outputFileNm = $("#txt_relative_output").val();
		
		var id = CMSC003.Storage.get('currentRelVid');
		var id2 = CMSC003.Storage.get('currentRelVid2');
		
		var list = CMSC003.Storage.get('searchDataMap');
		
		var layer = list[id];
		var layer2 = list[id2];
		
		if(layer) {
			if ($("#sel_photo_basic").val() == layer.vidoId){
				param.inputFileNm = layer.dirName+layer.tifFileName; //"36807062s_R.tif"
			}
			if ($("#sel_photo_target").val() == layer2.vidoId){
				param.targetFileNm = layer2.dirName+layer2.tifFileName; //"36807062s_R.tif"
			}
		}
		
// 	 	for(var i=0; i < searchRelativeList.length; i++){
// 			if ($("#sel_photo_basic").val() == searchRelativeList[i].vidoId){
// 				param.inputFileNm = searchRelativeList[i].dirName+searchRelativeList[i].tifFileName; //"36807062s_R.tif"
// 			}
// 			if ($("#sel_photo_target").val() == searchRelativeList[i].vidoId){
// 				param.targetFileNm = searchRelativeList[i].dirName+searchRelativeList[i].tifFileName; //"36807062s_R.tif"
// 			}
// 			if (param.inputFileNm != "" && param.targetFileNm != ""){
// 				break;
// 			}
// 		}
	}

	$("#progress").show();
	$.ajax({
		type : "POST",
	    url: "cisc001/createUpdateScript.do",
	    data:param,
	    success:function(data){
	    	$("#progress").hide();
	    	if (data.result > 0){
	    		alert('정상적으로 처리되었습니다.');
	    		$('#scriptModal').modal("hide");
	    		
	    	}else{
	    		if (data.result < 0){
	    			alert(data.msg);
	    		}else{
	    			alert('스크립트 저장에 실패했습니다. 잠시후 다시 시도하세요');
	    		}
	    	}
	    },
	    error:function(data){
	    	$("#progress").hide();
	    	alert('실패했습니다. 잠시후 다시 시도하세요');
   			return;
	    }
	});
}


function fnResultRelativeList(data){
	
	searchRelativeList.splice(0,searchRelativeList.length);
	$("#div_result_relative_list").empty();
	
	var soilList = data.soilList;
	var etcList = data.etcList;
	var aeroList = data.aeroList;
	var ortoList = data.orthophotoList;

	if (soilList){
		for(var i=0; i < soilList.length; i++){
			
			var min = transform(parseFloat(soilList[i].ltopCrdntY), parseFloat(soilList[i].ltopCrdntX), soilList[i].mapPrjctnCn, 'EPSG:5179');
			var max = transform(parseFloat(soilList[i].rbtmCrdntY), parseFloat(soilList[i].rbtmCrdntX), soilList[i].mapPrjctnCn, 'EPSG:5179');
	        
			var soilObj = {
					satKind :		   "SOIL",
					vidoId :		   soilList[i].vidoId,
					potogrfBeginDt :   soilList[i].potogrfBeginDt,
					potogrfEndDt : 	   soilList[i].potogrfEndDt,
					ltopCrdntX : 	   soilList[i].ltopCrdntX,
					ltopCrdntY : 	   soilList[i].ltopCrdntY,
					rbtmCrdntX : 	   soilList[i].rbtmCrdntX,
					rbtmCrdntY :	   soilList[i].rbtmCrdntY,
					innerFileCoursNm : soilList[i].innerFileCoursNm,
					potogrfVidoCd :    soilList[i].potogrfVidoCd,
					//extent : 		   ol.proj.transformExtent([data[i].ltopCrdntY, data[i].ltopCrdntX, data[i].rbtmCrdntY, data[i].rbtmCrdntX], 'EPSG:4326', 'EPSG:3857'),
					minX : min.x,
					minY : min.y,
					maxX : max.x,
					maxY : max.y,
					fileName : 	   	   soilList[i].fileName,
					imgFullFileName :  soilList[i].imgFullFileName,
					tifFileName :	   soilList[i].tifFileName,	 
					satName  :         soilList[i].satName,
					gain  	:          soilList[i].gain,
					offset  :  	       soilList[i].offset,				
					radianceMult  :    soilList[i].radianceMult,
					radianceAdd  :     soilList[i].radianceAdd,
					reflectanceMult :  soilList[i].reflectanceMult,
					reflectanceAdd  :  soilList[i].reflectanceAdd,
					metaData  :  	   soilList[i].metaData,
					dirName   :        soilList[i].dirName,
					mapPrjctnCn :      soilList[i].mapPrjctnCn
			};
			searchRelativeList.push(soilObj);
		}
	}	
	
	if (etcList){
		for(var i=0; i < etcList.length; i++){
			var min = transform(parseFloat(etcList[i].ltopCrdntY), parseFloat(etcList[i].ltopCrdntX), etcList[i].mapPrjctnCn, 'EPSG:5179');
			var max = transform(parseFloat(etcList[i].rbtmCrdntY), parseFloat(etcList[i].rbtmCrdntX), etcList[i].mapPrjctnCn, 'EPSG:5179');
	        
	        var etcObj = {
					satKind :		   "ETC",
					vidoId :		   etcList[i].vidoId,
					potogrfBeginDt :   etcList[i].potogrfBeginDt,
					potogrfEndDt : 	   etcList[i].potogrfEndDt,
					ltopCrdntX : 	   etcList[i].ltopCrdntX,
					ltopCrdntY : 	   etcList[i].ltopCrdntY,
					rbtmCrdntX : 	   etcList[i].rbtmCrdntX,
					rbtmCrdntY :	   etcList[i].rbtmCrdntY,
					innerFileCoursNm : etcList[i].innerFileCoursNm,
					potogrfVidoCd :    etcList[i].potogrfVidoCd,
					//extent : 		   ol.proj.transformExtent([data[i].ltopCrdntY, data[i].ltopCrdntX, data[i].rbtmCrdntY, data[i].rbtmCrdntX], 'EPSG:4326', 'EPSG:3857'),
					minX : min.x,
					minY : min.y,
					maxX : max.x,
					maxY : max.y,
					fileName : 	   	   etcList[i].filePath,
					imgFullFileName :  etcList[i].imgFullFileName,
					tifFileName :	   etcList[i].tifFileName,	 
					satName  :         etcList[i].satName,
					gain  	:          etcList[i].gain,
					offset  :  	       etcList[i].offset,				
					radianceMult  :    etcList[i].radianceMult,
					radianceAdd  :     etcList[i].radianceAdd,
					reflectanceMult :  etcList[i].reflectanceMult,
					reflectanceAdd  :  etcList[i].reflectanceAdd,
					metaData  :  	   etcList[i].metaData,
					dirName   :        etcList[i].dirName,
					mapPrjctnCn :      etcList[i].mapPrjctnCn
			};
	        
	        searchRelativeList.push(etcObj);
		}
	}
	if (aeroList){
		for(var i=0; i < aeroList.length; i++){

			var min = transform(parseFloat(aeroList[i].ltopCrdntY), parseFloat(aeroList[i].ltopCrdntX), aeroList[i].mapPrjctnCn, 'EPSG:5179');
			var max = transform(parseFloat(aeroList[i].rbtmCrdntY), parseFloat(aeroList[i].rbtmCrdntX), aeroList[i].mapPrjctnCn, 'EPSG:5179');
			
			var filePath = aeroList[i].innerFileCoursNm.replace(/tif/gi, "png");
			
	        var aeroObj = {
					satKind :		   "AERO",
					vidoId :		   aeroList[i].zoneCode,
					//potogrfBeginDt :   aeroList[i].potogrfBeginDt,
					//potogrfEndDt : 	   aeroList[i].potogrfEndDt,
					ltopCrdntX : 	   aeroList[i].ltopCrdntX,
					ltopCrdntY : 	   aeroList[i].ltopCrdntY,
					rbtmCrdntX : 	   aeroList[i].rbtmCrdntX,
					rbtmCrdntY :	   aeroList[i].rbtmCrdntY,
					innerFileCoursNm : aeroList[i].innerFileCoursNm,
					potogrfVidoCd :    aeroList[i].potogrfVidoCd,
					//extent : 		   ol.proj.transformExtent([data[i].ltopCrdntY, data[i].ltopCrdntX, data[i].rbtmCrdntY, data[i].rbtmCrdntX], 'EPSG:4326', 'EPSG:3857'),
					minX : min.x,
					minY : min.y,
					maxX : max.x,
					maxY : max.y,
					fileName : 	   	   filePath,
					imgFullFileName :  filePath,
					tifFileName :	   aeroList[i].imageCde,
					// satName  :         aeroList[i].satName,
					// gain  	:          aeroList[i].gain,
					// offset  :  	       aeroList[i].offset,
					// radianceMult  :    aeroList[i].radianceMult,
					// radianceAdd  :     aeroList[i].radianceAdd,
					// reflectanceMult :  aeroList[i].reflectanceMult,
					// reflectanceAdd  :  aeroList[i].reflectanceAdd,
					// metaData  :  	   aeroList[i].metaData,
					// dirName   :        aeroList[i].dirName,
					// mapPrjctnCn :      aeroList[i].mapPrjctnCn
			};
	        
	        searchRelativeList.push(aeroObj);
		}
	}
	
	var landcnt = 0, kcompcnt = 0, etccnt = 0, soilcnt = 0, aerocnt = 0;
	for(var i=0; i < searchRelativeList.length; i++){
		if (searchRelativeList[i].satKind == 'ETC'){
			etccnt++
			if (searchRelativeList[i].potogrfVidoCd == '1'){
				landcnt++;
			}else if (searchRelativeList[i].potogrfVidoCd == '2'){
				kcompcnt++;
			}
		}else if (searchRelativeList[i].satKind == 'SOIL'){
			soilcnt++;
		}else if (searchRelativeList[i].satKind == 'AERO'){
			aerocnt++;
		}
	}
	
	var strHtml  = '';
	
	if ($("#chk_relative_aero").prop("checked")){
		
		if (aerocnt > 0){
			var strHtml  = '<dl class="result-list-input">';
			strHtml += '<dt>항공/정사영상 ('+aerocnt+')</dt>';
			for(var i=0; i < searchRelativeList.length; i++){
				if (searchRelativeList[i].satKind == 'AERO'){
					strHtml += '<dd><a href="#" onclick="fnViewer(\''+searchRelativeList[i].vidoId+'\', \''+searchRelativeList[i].imgFullFileName+'\', '+searchRelativeList[i].minX+', '+searchRelativeList[i].minY+','+searchRelativeList[i].maxX+','+searchRelativeList[i].maxY+');">'+searchRelativeList[i].innerFileCoursNm+'</a></dd>';
				}
			}
			strHtml += '</dl>';
			$("#div_result_relative_list").append(strHtml);
		}else{
			var strHtml  = '<dl class="result-list-input">';
			strHtml += '<dt>항공/정사영상 ('+aerocnt+')</dt>';
			strHtml += '<dd>검색된 데이터가 없습니다.</dd></dl>';
			$("#div_result_relative_list").append(strHtml);
		}
	}
	
	if ($("#chk_relative_soil").prop("checked")){
		if (soilcnt > 0){
			var strHtml  = '<dl class="result-list-input">';
			strHtml += '<dt>국토위성 ('+soilcnt+')</dt>';
			for(var i=0; i < searchRelativeList.length; i++){
				if (searchRelativeList[i].satKind == 'SOIL'){
					strHtml += '<dd><a href="#" onclick="fnViewer(\''+searchRelativeList[i].vidoId+'\', \''+searchRelativeList[i].imgFullFileName+'\', '+searchRelativeList[i].minX+', '+searchRelativeList[i].minY+','+searchRelativeList[i].maxX+','+searchRelativeList[i].maxY+');">'+searchRelativeList[i].innerFileCoursNm+'</a></dd>';
				}
			}
			strHtml += '</dl>';
			$("#div_result_relative_list").append(strHtml);
		}else{
			var strHtml  = '<dl class="result-list-input">';
			strHtml += '<dt>국토위성 ('+soilcnt+')</dt>';
			strHtml += '<dd>검색된 데이터가 없습니다.</dd></dl>';
			$("#div_result_relative_list").append(strHtml);
		}
	}
	
	
	if ($("#chk_relative_etc").prop("checked")){
		if (etccnt > 0){
			var strHtml  = '<dl class="result-list-input">';
			strHtml += '<dt>기타위성 ('+etccnt+')</dt>';
			for(var i=0; i < searchRelativeList.length; i++){
				if (searchRelativeList[i].satKind == 'ETC'){
					if (searchRelativeList[i].potogrfVidoCd == '1'){
						strHtml += '<dd><a href="#" onclick="fnViewer(\''+searchRelativeList[i].vidoId+'\', \''+searchRelativeList[i].imgFullFileName+'\', '+searchRelativeList[i].minX+', '+searchRelativeList[i].minY+','+searchRelativeList[i].maxX+','+searchRelativeList[i].maxY+');"> (Landsat)'+searchRelativeList[i].innerFileCoursNm+'</a></dd>';
						//strHtml+= '<dd><code class="year">시작일자</code>'+searchAbsolutList[i].potogrfBeginDt+'</dd>';
					}else if (searchRelativeList[i].potogrfVidoCd == '2'){
						strHtml += '<dd><a href="#" onclick="fnViewer(\''+searchRelativeList[i].vidoId+'\', \''+searchRelativeList[i].imgFullFileName+'\', '+searchRelativeList[i].minX+', '+searchRelativeList[i].minY+','+searchRelativeList[i].maxX+','+searchRelativeList[i].maxY+');"> (Kompsat)'+searchRelativeList[i].innerFileCoursNm+'</a></dd>';
					}
				}
			}
			strHtml += '</dl>';
			$("#div_result_relative_list").append(strHtml);
		}else{
			var strHtml  = '<dl class="result-list-input">';
			strHtml += '<dt>기타위성 ('+etccnt+')</dt>';
			strHtml += '<dd>검색된 데이터가 없습니다.</dd></dl>';
			$("#div_result_relative_list").append(strHtml);
		}
	}
	
	$("#sel_photo_basic").empty();	//입력영상
	$("#sel_photo_target").empty();	//대상영상
	$.each(searchRelativeList, function (index, item){
		$("#sel_photo_basic").append("<option value="+item.vidoId+">"+(index +1)+". "+item.innerFileCoursNm+"</option>");
		$("#sel_photo_target").append("<option value="+item.vidoId+">"+(index +1)+". "+item.innerFileCoursNm+"</option>");
		
		if (index == 0){
			
			$("#txt_relative_output").val('RelRadResult_');
// 			$("#txt_relative_output").val('Result_'+item.satName+".tif");
// 			$("#txt_relative_output").val(item.dirName+'Result_'+item.satName+".tif");
			
		}
	});
	
	
	
	/* for(var i=0; i < searchRelativeList.length; i++){
		$("#sel_input_photo").append("<option value="+searchRelativeList[i].vidoId+">"+searchRelativeList[i].innerFileCoursNm+"</option>");
	} */
	
	
	/* if (searchRelativeList.length > 0){
		if (searchRelativeList[0].potogrfVidoCd == "1"){  //Landsat
			$("#txt_correction").val('Radiance = '+searchAbsolutList[0].radianceMult+' X DN + '+searchAbsolutList[0].radianceAdd);
			$("#txt_toa").val('TOA Reflectance = '+searchAbsolutList[0].reflectanceMult+' X DN + '+searchAbsolutList[0].reflectanceAdd);
			$("#sel_sat option:eq(2)").prop("selected", true);
			fnChangeSat('1');
		}else if (searchRelativeList[0].potogrfVidoCd == "2"){ //Kompsat
			$("#txt_correction").val('Radiance = '+searchAbsolutList[0].gain+' X DN + '+searchAbsolutList[0].offset);
			$("#sel_sat option:eq(1)").prop("selected", true);
			fnChangeSat('2');
		}
		
		$("#txt_result").val('Result_'+searchAbsolutList[0].satName);		
		$("#txt_meta").val(searchAbsolutList[0].metaData);
	} */

}

function fnShowPopup(id){
	
// 	if (id == 'absoluteModal'){
// 		$('#txt_result').val('AbsRadResult_');
// 		if (searchAbsolutList.length == 0){
// 			alert('대상이 존재하지 않습니다. 처리할 대상을 검색 하세요');			
// 			return;
// 		}		
// 	}else{
// 		$('#txt_relative_output').val('RelRadResult_');
// 		if (searchRelativeList.length == 0){
// 			alert('대상이 존재하지 않습니다. 처리할 대상을 검색 하세요');			
// 			return;
// 		}
// 	}

	if (id == 'absoluteModal'){
		if ($('#div_result_absolute_list').children().length == 0){
			alert('대상이 존재하지 않습니다. 처리할 대상을 검색 하세요');			
			return;
		}		
	}else{
		if ($('#div_result_relative_list').children().length == 0){
			alert('대상이 존재하지 않습니다. 처리할 대상을 검색 하세요');			
			return;
		}
	}
	
	
	var vector = $("#resultList1 #resultList1_digital_tree").jstree().get_bottom_checked(true);
	var aerial = $("#resultList1 #resultList1_aerial_tree").jstree().get_bottom_checked(true);
	var ortho = $("#resultList1 #resultList1_ortho_tree").jstree().get_bottom_checked(true);
	var dem = $("#resultList1 #resultList1_dem_tree").jstree().get_bottom_checked(true);
	var graphics = $("#resultList1 #resultList1_graphics_tree").jstree().get_bottom_checked(true);
	var satellite = $("#resultList1 #resultList1_satellite_tree").jstree().get_bottom_checked(true);
	var earthquake = $("#resultList2 #resultList2_earthquake_tree").jstree().get_bottom_checked(true);
	var forestFire = $("#resultList2 #resultList2_forestFire_tree").jstree().get_bottom_checked(true);
	var flood = $("#resultList2 #resultList2_flood_tree").jstree().get_bottom_checked(true);
	var landslip = $("#resultList2 #resultList2_landslip_tree").jstree().get_bottom_checked(true);
	var maritime = $("#resultList2 #resultList2_maritime_tree").jstree().get_bottom_checked(true);
	var redTide = $("#resultList2 #resultList2_redTide_tree").jstree().get_bottom_checked(true);
	var vectorAnalObj = $("#resultList3 #resultList3_vectorAnalObj_tree").jstree().get_bottom_checked(true);
	var rasterAnalObj = $("#resultList3 #resultList3_rasterAnalObj_tree").jstree().get_bottom_checked(true);
	var vectorAnalChg = $("#resultList3 #resultList3_vectorAnalChg_tree").jstree().get_bottom_checked(true);
	var rasterAnalChg = $("#resultList3 #resultList3_rasterAnalChg_tree").jstree().get_bottom_checked(true);
	
	var count = 0;
	if (id == 'absoluteModal'){
		$('#txt_result').val('AbsRadResult_');
		$('#sel_input_photo').empty();
		// kompsat 데이터
		// landsat 데이터
		// sentinel 데이터
		// cas 데이터
		satellite.forEach(function(item) {
			var list = CMSC003.Storage.get('searchDataMap');
			var sat = item.id.split('-')[2];
			var folder = item.parent.split('-')[5];
			var layer = null;
			var label = null;
			if (sat == "kompsat") {
				label = 'kompsat-'+item.li_attr.value;
				layer = list['kompsat-'+item.li_attr.value];
			} else if (sat == "landsat") {
				label = 'landsat-'+item.li_attr.value;
				layer = list['landsat-'+item.li_attr.value];
			} else if (sat == "sentinel") {
				label = 'sentinel-'+item.li_attr.value;
				layer = list['sentinel-'+item.li_attr.value];
			} else if (sat == "cas") {
				label = 'cas-'+item.li_attr.value;
				layer = list['cas-'+item.li_attr.value];
			}
			
			if(layer) {
				var opt0 = $('<option>');
				var opt1 = $('<option>').prop({
					'value' : layer.fullFileCoursNm
				}).text(layer.fullFileCoursNm);
				
				opt1.attr('storeid', label);
				
				$('#sel_input_photo').append(opt0).append(opt1);
				
				count++;
				
				
				// todo: fnChangeSat 확인 필요 -> 특정 엘리먼트를 show/hide
				
				// todo: fnChangeinputPicture 확인 필요
// 				if (item.potogrfVidoCd == "1"){
// 					$("#txt_correction").val('Radiance = '+item.radianceMult+' X DN + '+item.radianceAdd);
// 					$("#txt_toa").val('TOA Reflectance = '+item.reflectanceMult+' X DN + '+item.reflectanceAdd);
// 					$("#sel_sat option:eq(2)").prop("selected", true);
// 					fnChangeSat('1');
// 				}else{
// 					$("#txt_correction").val('Radiance = '+item.gain+' X DN + '+item.offset);
// 					$("#sel_sat option:eq(1)").prop("selected", true);
// 					fnChangeSat('2');
// 				}
// 				$("#txt_meta").val(item.dirName+item.metaData);
// 				$("#txt_toa_output_file").val('TOA_AbsRadResult_');
			}
		});
		
		if(!count) {
			alert('대상이 존재하지 않습니다. 처리할 대상을 선택하세요');			
			return;
		}
		
		$('#sel_input_photo').trigger('change');
	}else{
		$('#txt_relative_output').val('RelRadResult_');
		
		$("#sel_photo_basic").empty();	//입력영상
		$("#sel_photo_target").empty();	//대상영상
		
		// todo: fnChangeinputPicture 확인 필요
		
		// 항공영상 데이터
		aerial.forEach(function(item) {
			var list = CMSC003.Storage.get('searchDataMap');
			var label = 'aerial-'+item.li_attr.value;
			var layer = list[label];
			console.log(layer);
			
			if(layer) {
				var opt0 = $('<option>');
				var opt1 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
				var opt2 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
				
				opt1.attr('storeid', label);
				opt2.attr('storeid', label);
				
				$("#sel_photo_basic").append(opt0).append(opt1);
				$("#sel_photo_target").append(opt0).append(opt2);
				
				count++;
			}
		});
		
		// 정사영상 데이터
		ortho.forEach(function(item) {
			var list = CMSC003.Storage.get('searchDataMap');
			var label = 'ortho-'+item.li_attr.value;
			var layer = list[label];
			console.log(layer);
			
			if(layer) {
				var opt0 = $('<option>');
				var opt1 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
				var opt2 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
				
				opt1.attr('storeid', label);
				opt2.attr('storeid', label);
				
				$("#sel_photo_basic").append(opt0).append(opt1);
				$("#sel_photo_target").append(opt0).append(opt2);
				
				count++;
			}
		});
		
		// 위성영상
		satellite.forEach(function(item) {
			var list = CMSC003.Storage.get('searchDataMap');
			var sat = item.id.split('-')[2];
			var folder = item.parent.split('-')[5];
			var layer = null;
			var label = null;
			if (sat == "kompsat") {
				label = 'kompsat-'+item.li_attr.value;
				layer = list[label];
				// innerFileCoursNm
			} else if (sat == "landsat") {
				label = 'landsat-'+item.li_attr.value;
				layer = list[label];
			} else if (sat == "sentinel") {
				label = 'sentinel-'+item.li_attr.value;
				layer = list[label];
			} else if (sat == "cas") {
				label = 'cas-'+item.li_attr.value;
				layer = list[label];
			}
			
			if(layer) {
				var opt0 = $('<option>');
				var opt1 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
				var opt2 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
				
				opt1.attr('storeid', label);
				opt2.attr('storeid', label);
				
				$("#sel_photo_basic").append(opt0).append(opt1);
				$("#sel_photo_target").append(opt0).append(opt2);
				
				count++;
			}
		});
		
		// todo: fnChangeinputPicture 확인 필요
//			if (item.potogrfVidoCd == "1"){
//				$("#txt_correction").val('Radiance = '+item.radianceMult+' X DN + '+item.radianceAdd);
//				$("#txt_toa").val('TOA Reflectance = '+item.reflectanceMult+' X DN + '+item.reflectanceAdd);
//				$("#sel_sat option:eq(2)").prop("selected", true);
//				fnChangeSat('1');
//			}else{
//				$("#txt_correction").val('Radiance = '+item.gain+' X DN + '+item.offset);
//				$("#sel_sat option:eq(1)").prop("selected", true);
//				fnChangeSat('2');
//			}
//			$("#txt_meta").val(item.dirName+item.metaData);
//			$("#txt_toa_output_file").val('TOA_AbsRadResult_');
		
		if(!count) {
			alert('대상이 존재하지 않습니다. 처리할 대상을 선택하세요');			
			return;
		}
		
		$('#sel_photo_basic').trigger('change');
	}
	$('#'+id).modal("show");
}



function fnRelativeProcess(){
	
	if( ! confirm("상대방사보정 작업을 진행 하시겠습니까? ")) {
		return;
	}
	
	if ($("#sel_photo_basic").val() == $("#sel_photo_target").val()){
		alert('기준영상과 대상영상이 동일합니다. 영상을 다시 선택하세요');
		return;
	}
	
	var did = $('#disasterId').val();
	if(did.trim() === '') {
		alert('재난 ID를 입력해주세요.');
		return;
	}
	
	var param = {
		algorithm   : $("#sel_algorithm").val(),
		histogramArea : $("input[name='histogram_radio']:checked").val(),
		srcFilePath : "",
		trgFilePath : "",
		outFilePath : "",
		disasterId: did
	};
	
	var vidoId, minX, minY, maxX, maxY;
	var id = CMSC003.Storage.get('currentRelVid');
	var id2 = CMSC003.Storage.get('currentRelVid2');
	var list = CMSC003.Storage.get('searchDataMap');
	var layer = list[id];
	var layer2 = list[id2];
	if(layer) {
		param.srcFilePath = layer.dirName+layer.tifFileName; //"36807062s_R.tif"
		//param.outFilePath = layer.dirName+'Result_'+layer.satName+".tif";
		param.outFilePath = $("#txt_relative_output").val();
		
		vidoId = layer.vidoId;
		minX = layer.minX;
		minY = layer.minY;
		maxX = layer.maxX;
		maxY = layer.maxY;
		
		if(!minX) {
			minX = layer.ltopCrdntY;
		}
		if(!minY) {
			minY = layer.rbtmCrdntX;
		}
		if(!maxX) {
			maxX = layer.rbtmCrdntY;
		}
		if(!maxY) {
			maxY = layer.ltopCrdntX;
		}
		
		if(minX && minY && maxX && maxY) {
			var extent = [minX, minY, maxX, maxY];
			extent = CMSC003.GIS.convert4326To5179Extent(extent);
			minX = extent[0]; 
			minY = extent[1];
			maxX = extent[2];
			maxY = extent[3];
		}
	}
	if(layer2) {
		param.trgFilePath = layer2.dirName+layer2.tifFileName; //"36807062s_R.tif"
	}
	
// 	for(var i=0; i < searchRelativeList.length; i++){
		
// 		if ($("#sel_photo_basic").val() == searchRelativeList[i].vidoId){
// 			param.srcFilePath = searchRelativeList[i].dirName+searchRelativeList[i].tifFileName; //"36807062s_R.tif"
// 			//param.outFilePath = searchRelativeList[i].dirName+'Result_'+searchRelativeList[i].satName+".tif";
// 			param.outFilePath = $("#txt_relative_output").val();
			
// 			vidoId = searchRelativeList[i].vidoId;
// 			minX = searchRelativeList[i].minX;
// 			minY = searchRelativeList[i].minY;
// 			maxX = searchRelativeList[i].maxX;
// 			maxY = searchRelativeList[i].maxY;
// 		}
// 		if ($("#sel_photo_target").val() == searchRelativeList[i].vidoId){
// 			param.trgFilePath = searchRelativeList[i].dirName+searchRelativeList[i].tifFileName; //"36807062s_R.tif"
// 		}
		
// 		if (param.srcFilePath != "" && param.trgFilePath != ""){
// 			break;
// 		}
// 	}
	
	$("#progress").show();
	$.ajax({
		type : "POST",
	    url: "cisc005/relativeRadiatingCorrection.do",
	    data:param,
	    success:function(data){
	    	var result = JSON.parse(data);	    	
	    	if (result.procCode == 'SUCCESS'){
	    		$("#progress").hide();
	    		alert('정상적으로 처리되었습니다.');
	    		//$("#div_relative_result").append('<dd><a href="#" onclick="fnViewer(\''+vidoId+'\', \''+result.thumFullName+'\', '+minX+', '+minY+','+maxX+','+maxY+');">'+$("#txt_relative_output").val()+'</a></dd>');
	    		$("#div_relative_result").append('<dd><a href="#" onclick="fnViewer(\''+vidoId+'\', \''+result.thumFullName+'\', '+minX+', '+minY+','+maxX+','+maxY+');">'+result.resultFileName+'</a></dd>');
	    		
	    		$('#relativeModal').modal("hide");
	    	}else{
	    		$("#progress").hide();
	    		alert('처리도중 에러가 발생했습니다.['+ result.procCode+']');
	    		return;
	    	}
	    },
	    error:function(data){
	    	$("#progress").hide();
	    	alert("실패", data);
	    }
	});
}

</script>
<script>
	
		$(document).ready(function() {
			// 시도 셀렉트 초기화
			getSido();
			
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

				var map = ciscMap.map;
				// 지도 중심점 이동
				CMSC003.GIS.setMapCenter(transform, map);

				// ROI 중심점 포인트 생성
//	 			CMSC003.GIS.createROICenter(transform);
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
					
					CMSC003.Storage.set('absBbox', bboxArray);
					
					//CMSC003.DOM.inputROIToForm(transform_bbox);
					// ROI 피처 생성
//	 				CMSC003.GIS.createPolygonROIfrom5179(transform_bbox);
					CMSC003.GIS.createPolygonROIfrom5179(transform_bbox, selectionLayer, map);

				}

				$('#cmsc003-modal').remove();
			});
		});
	
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
						
						var map = ciscMap.map;
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
						
						var map = ciscMap.map;
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
					
					var map = ciscMap.map;
					// 지도 중심점 이동
					CMSC003.GIS.setMapCenter(transform, map);
				},
				error: function(request, status, error) {
					alert("code:" + request.status + "\n" + "message:"
							+ request.respnseText + "\n" + "error:" + error);
				}
			})
		}
		
		function getCurrentTab() {
	    	//tab-01
	    	var result = null;
	    	if($('#tab-01').is(':visible')) {
	    		result = 1;
	    	} else if($('#tab-02').is(':visible')) {
	    		result = 2;
	    	}
	    	return result;
	    }
	</script>
</head>





<body>
<%@ include file="../topMenu.jsp"%>
<!-- //////////////////////////////////////////////////////////////////////////// --> 
<!-- START SIDEBAR -->
<div class="sidebar clearfix" style="OVERFLOW-Y:auto; height:100%;">

	<div role="tabpanel" class="sidepanel" style="display: block;">

		<!-- Nav tabs -->
		<ul class="nav nav-tabs" role="tablist">
			<li role="presentation" class="active" style="width:50%">
				<a href="#tab-01" aria-controls="tab-01" role="tab" data-toggle="tab" aria-expanded="true" class="active">
					<i class="fas fa-brush"></i><br>절대방사보정
				</a>
			</li>
			<li role="presentation" class="" style="width:50%">
				<a href="#tab-02" aria-controls="tab-02" role="tab" data-toggle="tab" class="" aria-expanded="false">
					<i class="fas fa-brush"></i><br>상대방사보정
				</a>
			</li>
		</ul>

		<!-- Tab panes -->
		<div class="tab-content" style="padding:10px;">

			<!-- Start tab-01 -->
			<div role="tabpanel" class="tab-pane active" id="tab-01">

				<div class="sidepanel-m-title">
					위성영상 검색
				</div>
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
<!-- 					<div class="form-group m-t-10"> -->
<!-- 						<label for="input002" class="col-sm-3 control-label form-label">종류</label> -->
<!--                         <div class="col-sm-9"> -->
<!-- 							<label class="ckbox-container">국토위성 -->
<!-- 	                        	<input type="checkbox" id="sate_soil" name="sate" value="soil"> -->
<!-- 	                        	<span class="checkmark"></span> -->
<!-- 	                        </label> -->
<!-- 	                        <label class="ckbox-container">기타위성 -->
<!-- 	                        	<input type="checkbox" id="sate_etc" name="sate" value="etc" checked> -->
<!-- 	                        	<span class="checkmark"></span> -->
<!-- 							</label> -->
<!-- 						</div> -->
<!-- 					</div> -->
					<div class="form-group m-t-6" id="dataKindContainer">
						<label for="input002" class="col-sm-2 control-label form-label">종류</label>
						<div class="col-sm-10">
							<label class="ckbox-container"
								style="margin-right: 1px; font-size: 12px;">기존 데이터 <input
								type="checkbox" id="dataKindCurrentAbs" name="dataKindCurrent"
								checked="checked"> <span class="checkmark"></span>
							</label> <label class="ckbox-container"
								style="margin-right: 1px; font-size: 12px;">긴급 영상 <input
								type="checkbox" id="dataKindEmergencyAbs"
								name="dataKindEmergency"> <span class="checkmark"></span>
							</label> <label class="ckbox-container"
								style="margin-right: 1px; font-size: 12px;">분석결과 데이터 <input
								type="checkbox" id="dataKindResultAbs" name="dataKindResult">
								<span class="checkmark"></span>
							</label>
						</div>
					</div>
					<div class="form-group m-t-6" id="targetDataKindContainer">
						<label for="input002" class="col-sm-2 control-label form-label">대상</label>
						<div class="col-sm-10">
							<label class="ckbox-container"
								style="margin-right: 1px; font-size: 12px;">원본 데이터 <input
								type="radio" id="originData" name="dataTypeAbs" value="1"
								checked="checked"> <span class="checkmark"></span>
							</label> <label class="ckbox-container"
								style="margin-right: 1px; font-size: 12px;">긴급공간정보 <input
								type="radio" id="datasetData"
								name="dataTypeAbs" value="2"> <span class="checkmark"></span>
							</label> <label class="ckbox-container"
								style="margin-right: 1px; font-size: 12px;">처리 데이터 <input
								type="radio" id="workData" name="dataTypeAbs" value="3">
								<span class="checkmark"></span>
							</label>
						</div>
					</div>	
					<!-- <div class="form-group m-t-10">
						<label for="input002" class="col-sm-3 control-label form-label">기간</label>
						<div class="col-sm-4">
							<input type="text" id="datepicker" class="form-control">
						</div>
						<div class="col-sm-1"><span class="txt-in-form">~</span></div>
						<div class="col-sm-4">
							<input type="text" id="datepicker2" class="form-control">
						</div>
					</div> -->
					
<!-- 					<div class="form-group m-t-10"> -->
<!--                         <label for="input002" class="col-sm-3 control-label form-label">기간</label> -->
<!--                         <div class="col-sm-4"> -->
<!--                             <input type="text" id="datepicker1" class="form-control"> -->
<!--                         </div> -->
<!--                         <div class="col-sm-1"><span class="txt-in-form">~</span></div> -->
<!--                         <div class="col-sm-4"> -->
<!--                             <input type="text" id="datepicker2" class="form-control"> -->
<!--                         </div> -->
<!--                     </div> -->
<!--                     <script src="/js/datepicker.js"></script> -->
<!--                     <script> -->
<!--                         let datepicker1 = new DatePicker(document.getElementById('datepicker1')); -->
<!--                         let datepicker2 = new DatePicker(document.getElementById('datepicker2')); -->
<!--                     </script> -->
					
					
					
<!-- 					<div class="form-group m-t-10"> -->
<!-- 						<label for="input002" class="col-sm-3 control-label form-label">해상도(m)</label> -->
<!-- 						<div class="col-sm-4"> -->
<!-- 							<input id="absolute_start_resolution" type="text" class="form-control" value=""> -->
<!-- 						</div> -->
<!-- 						<div class="col-sm-1"><span class="txt-in-form">~</span></div> -->
<!-- 						<div class="col-sm-4"> -->
<!-- 							<input id="absolute_end_resolution" type="text" class="form-control" value=""> -->
<!-- 						</div> -->
<!-- 					</div> -->
					
<!-- 					<div class="form-group m-t-10"> -->
<!-- 						<label for="input002" class="col-sm-3 control-label form-label">운량</label> -->
<!-- 						<div class="col-sm-4"> -->
<!-- 							<div class="radio radio-info radio-inline"> -->
<!-- 								<input type="radio" id="absAmountCloud_01" value="" name="absAmountCloud" checked> -->
<!-- 								<label for="absAmountCloud_01">전체</label> -->
<!-- 							</div> -->
<!-- 						</div> -->
<!-- 						<div class="col-sm-4"> -->
<!-- 							<div class="radio radio-inline"> -->
<!-- 								<input type="radio" id="absAmountCloud_02" value="10" name="absAmountCloud"> -->
<!-- 								<label for="absAmountCloud_02">10% 이내</label> -->
<!-- 							</div> -->
<!-- 						</div> -->
<!-- 					</div> -->
<!-- 					<div class="form-group m-t-6" id="dataKindContainer"> -->
<!-- 						<label for="input002" class="col-sm-2 control-label form-label">종류</label> -->
<!-- 						<div class="col-sm-10"> -->
<!-- 							<label class="ckbox-container" -->
<!-- 								style="margin-right: 1px; font-size: 12px;">기존 데이터 <input -->
<!-- 								type="checkbox" id="dataKindCurrent" name="dataKindCurrent" -->
<!-- 								checked="checked"> <span class="checkmark"></span> -->
<!-- 							</label> <label class="ckbox-container" -->
<!-- 								style="margin-right: 1px; font-size: 12px;">긴급 영상 <input -->
<!-- 								type="checkbox" id="dataKindEmergency" -->
<!-- 								name="dataKindEmergency"> <span class="checkmark"></span> -->
<!-- 							</label> <label class="ckbox-container" -->
<!-- 								style="margin-right: 1px; font-size: 12px;">분석결과 데이터 <input -->
<!-- 								type="checkbox" id="dataKindResult" name="dataKindResult"> -->
<!-- 								<span class="checkmark"></span> -->
<!-- 							</label> -->
<!-- 						</div> -->
<!-- 					</div> -->
<!-- 					<div id="datepickerContainer" -->
<!-- 						class="form-group m-t-6 input-daterange"> -->
<!-- 						<label for="input002" class="col-sm-2 control-label form-label">기간</label> -->
<!-- 						<div class="col-sm-5"> -->
<!-- 							<input type="text" class="form-control ui-input-datepicker" -->
<!-- 								id="inputStartDate" name="dateFrom" value="2020-04-23"> -->
<!-- 							<button type="button" id="btnOpenDatepickerStart" -->
<!-- 								class="btn btn-default btn-icon ui-btn-open-datepicker"> -->
<!-- 								<i class="fas fa-calendar-alt"></i> -->
<!-- 							</button> -->
<!-- 						</div> -->

<!-- 						<div class="col-sm-5"> -->
<!-- 							<input type="text" class="form-control ui-input-datepicker" -->
<!-- 								id="inputEndDate" name="dateTo" value="2020-04-26"> -->
<!-- 							<button type="button" id="btnOpenDatepickerEnd" -->
<!-- 								class="btn btn-default btn-icon ui-btn-open-datepicker"> -->
<!-- 								<i class="fas fa-calendar-alt"></i> -->
<!-- 							</button> -->
<!-- 						</div> -->
<!-- 					</div> -->
<!-- 					<div class="form-group m-t-6"> -->
<!-- 						<label for="input002" class="col-sm-3 control-label form-label">해상도(m)</label> -->
<!-- 						<div class="col-sm-4"> -->
<!-- 							<input type="number" class="form-basic" name="resolMin" /> -->
<!-- 																<select class="form-basic" name="resolMin"> -->
<!-- 																	<option value='5cm' selected>5</option> -->
<!-- 																	<option value='10cm'>10</option> -->
<!-- 																	<option value='25cm'>25</option> -->
<!-- 																</select> -->
<!-- 						</div> -->
<!-- 						<div class="col-sm-1"> -->
<!-- 							<span class="txt-in-form">~</span> -->
<!-- 						</div> -->
<!-- 						<div class="col-sm-4"> -->
<!-- 							<input type="number" class="form-basic" name="resolMax" /> -->
<!-- 																<select class="form-basic" name="resolMax"> -->
<!-- 																	<option value='5cm'>5</option> -->
<!-- 																	<option value='10cm'>10</option> -->
<!-- 																	<option value='25cm' selected>25</option> -->
<!-- 							</select> -->
<!-- 						</div> -->
<!-- 					</div> -->
<!-- 					<div class="form-group m-t-6"> -->
<!-- 						<label class="col-sm-3 control-label form-label">재난 유형</label> -->
<!-- 						<div class="col-sm-9"> -->
<!-- 							<select id="selectDisasterType" class="form-basic" -->
<!-- 								name="disasterType"> -->
<!-- 								<option value='AllDisaster'>전체</option> -->
<!-- 								<option value='Flood'>수해</option> -->
<!-- 								<option value='Landslide'>산사태</option> -->
<!-- 								<option value='ForestFire'>산불</option> -->
<!-- 								<option value='Earthquake'>지진</option> -->
<!-- 								<option value='MaritimeDisaster'>해양재난</option> -->
<!-- 							</select> -->
<!-- 						</div> -->
<!-- 					</div> -->

					<!-- <div class="form-group m-t-6">
						<label class="col-sm-3 control-label form-label">재난 ID</label>
						<div class="col-sm-9">
							<input type="text" class="form-control" id="disasterId"
								name="disasterId" readOnly="readOnly"
								style="cursor: auto; background-color: #fff;">
						</div>
					</div> -->

<!-- 					<div class="form-group m-t-6"> -->
<!-- 						<label class="col-sm-12 control-label form-label">재난지역</label> -->
<!-- 						<div class="col-sm-4"> -->
<!-- 							<select class="form-basic" name="sido" -->
<!-- 								onChange="getSigungu(this.value)"> -->
<!-- 								<option selected disabled hidden>광역시도</option> -->
<!-- 							</select> -->
<!-- 						</div> -->
<!-- 						<div class="col-sm-4"> -->
<!-- 							<select class="form-basic" name="sigungu" -->
<!-- 								onChange="getEmd(this.value)" disabled> -->
<!-- 								<option selected disabled hidden>시군구</option> -->
<!-- 							</select> -->
<!-- 						</div> -->
<!-- 						<div class="col-sm-4"> -->
<!-- 							<select class="form-basic" name="emd" -->
<!-- 								onChange="getEmdPosition(this.value)" disabled> -->
<!-- 								<option selected disabled hidden>읍면동</option> -->
<!-- 							</select> -->
<!-- 						</div> -->
<!-- 					</div> -->
<!-- 					<div class="form-group m-t-6"> -->
<!-- 						<div class="col-sm-12"> -->
<!-- 							<input type="text" id="detailAddrForm" readOnly="readOnly" -->
<!-- 								class="form-basic"> -->
<!-- 						</div> -->
<!-- 					</div> -->


<!-- 					<div class="form-group m-t-6"> -->
<!-- 						<label for="input002" class="col-sm-2 control-label form-label">ROI</label> -->
<!-- 						<div class="col-sm-10"> -->
<!-- 							<div class="col-sm-2 in-tit">ULX</div> -->
<!-- 							<div class="col-sm-4"> -->
<!-- 								<input type="text" class="form-control js-input-roi-extent" -->
<!-- 									id="inputULX" name="ulx4326"> <input type="hidden" -->
<!-- 									id="inputULX2" name="ulx5186"> <input type="hidden" -->
<!-- 									id="inputULX3" name="ulx5179"> <input type="hidden" -->
<!-- 									id="inputULX4" name="ulx32652"> -->
<!-- 							</div> -->
<!-- 							<div class="col-sm-2 in-tit">ULY</div> -->
<!-- 							<div class="col-sm-4"> -->
<!-- 								<input type="text" class="form-control js-input-roi-extent" -->
<!-- 									id="inputULY" name="uly4326"> <input type="hidden" -->
<!-- 									id="inputULY2" name="uly5186"> <input type="hidden" -->
<!-- 									id="inputULY3" name="uly5179"> <input type="hidden" -->
<!-- 									id="inputULY4" name="uly32652"> -->
<!-- 							</div> -->
<!-- 							<div class="col-sm-2 in-tit m-t-5">LRX</div> -->
<!-- 							<div class="col-sm-4 m-t-5"> -->
<!-- 								<input type="text" class="form-control js-input-roi-extent" -->
<!-- 									id="inputLRX" name="lrx4326"> <input type="hidden" -->
<!-- 									id="inputLRX2" name="lrx5186"> <input type="hidden" -->
<!-- 									id="inputLRX3" name="lrx5179"> <input type="hidden" -->
<!-- 									id="inputLRX4" name="lrx32652"> -->
<!-- 							</div> -->
<!-- 							<div class="col-sm-2 in-tit m-t-5">LRY</div> -->
<!-- 							<div class="col-sm-4 m-t-5"> -->
<!-- 								<input type="text" class="form-control js-input-roi-extent" -->
<!-- 									id="inputLRY" name="lry4326"> <input type="hidden" -->
<!-- 									id="inputLRY2" name="lry5186"> <input type="hidden" -->
<!-- 									id="inputLRY3" name="lry5179"> <input type="hidden" -->
<!-- 									id="inputLRY4" name="lry32652"> -->
<!-- 							</div> -->
<!-- 						</div> -->
<!-- 					</div> -->
					<!-- =========================== -->
					
					
					<div class="sidepanel-s-title m-t-30 a-cent ">
						<a href="javascript:fnAbsoluteSearch()" class="btn btn-default"><i class="fas fa-search m-r-5"></i>검색</a>
					</div>
					<div class="sidepanel-s-title m-t-30">
					검색 결과
					<!-- <a href="" class="btn btn-light f-right"><i class="fas fa-project-diagram m-r-5"></i>수행</a> -->
					<button type="button" class="btn btn-light  f-right" onclick="fnShowPopup('absoluteModal');" ><i class="fas fa-project-diagram m-r-5"></i>수행</button>
										
					</div>
					<div class="modal fade" id="absoluteModal" name="absoluteModal" tabindex="-1" role="dialog" aria-hidden="true">
						<div class="modal-dialog modal-sm">
							<div class="modal-content">
								<div class="modal-header">
									<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
									<h4 class="modal-title">절대방사보정</h4>
								</div>
								<div class="modal-body">
									<div class="panel-body">					
										<form class="">
										<div class="form-group m-t-10">
											<label for="sel_input_photo" class="col-sm-4 control-label form-label">입력영상</label>
											<div class="col-sm-8">
<!-- 												<select id="sel_input_photo" class="form-basic" onchange="fnChangeinputPicture(this.value);"> -->
												<select id="sel_input_photo" class="form-basic"">
													<option></option>
												</select>            
											</div>
										</div>
										<div class="form-group m-t-10">
											<label class="col-sm-4 control-label form-label">위성영상</label>
											<div class="col-sm-8">
												<select id="sel_sat" class="form-basic" onchange="fnChangeSat(this.value);">
													<option value="4">국토위성</option>
													<option value="2">Kompsat</option>
													<option value="1">Landsat</option>
													<option value="3">Santinel-2</option>
												</select>            
											</div>
										</div>
										<div class="form-group m-t-10">
											<label for="txt_meta" class="col-sm-4 control-label form-label">메타데이터</label>
											<div class="col-sm-8">
												<input id="txt_meta" type="text" class="form-control" value="">
											</div>
										</div>
										<div class="form-group m-t-10">
											<label for="txt_correction" class="col-sm-4 control-label form-label">방사보정수식</label>
											<div class="col-sm-8">
												<input id="txt_correction" type="text" class="form-control" value="">
											</div>
										</div>
										<div class="form-group m-t-10">
											<label for="txt_result" class="col-sm-4 control-label form-label">결과영상</label>
											<div class="col-sm-8">
												<input id="txt_result" type="text" class="form-control" value="">
											</div>
										</div>
										<div id="div_toa" class="form-group m-t-10" style="display:none;">
											<label for="txt_toa" class="col-sm-4 control-label form-label">TOA 보정수식</label>
											<div class="col-sm-8">
												<input id="txt_toa" type="text" class="form-control" value="">
											</div>
										</div>
										<div id="div_toa_output" class="form-group m-t-10" style="display:none;">
											<label for="txt_toa_output_file" class="col-sm-4 control-label form-label">TOA 결과영상</label>
											<div class="col-sm-8">
												<input id="txt_toa_output_file" type="text" class="form-control" value="">
											</div>
										</div>
										</form> 

									</div>
								</div>
								<div class="modal-footer">
									<button id="btn_process" type="button" class="btn btn-default" onclick="fnAbsoluteProcess();"><i class="fas fa-check-square m-r-5"></i>처리</button>
									<button type="button" class="btn btn-gray" data-dismiss="modal" aria-label="Close"><i clas="fas fa-times m-r-5"></i>취소</button>
									<button id="btn_process" type="button" class="btn btn-default" onclick="fnScriptSavePopup('2')"><i class="fas fa-check-square m-r-5"></i>스크립트저장</button>
								</div>
							</div>
						</div>
					</div>
					
					<div class="panel-body">
						<div class="col-lg-12 js-search-result-area">
							<div id="div_result_absolute_list" class="panel panel-default" style="height:200px;">
							</div>
						</div>
					</div>
					<div class="sidepanel-s-title m-t-30">
					처리 결과
					</div>
					<div class="panel-body">
						<div class="col-lg-12">
							<div id="div_absolute_result" class="panel panel-default" style="height:200px;">
								<dl class="result-list-input">
									<!-- <dd>Result_Landsat-5 TM Collection 1 Level 1</dd> -->
								</dl>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div role="tabpanel" class="tab-pane" id="tab-02">

				<div class="sidepanel-m-title">
					대상영상 검색
				</div>
				<div class="panel-body">
					<div class="form-group m-t-10">
						<label class="col-sm-3 control-label form-label">재난 ID</label>
						<div class="col-sm-9">
							<input type="text" class="form-control" id="disasterId"
								name="disasterId" readOnly="readOnly"
								style="cursor: auto; background-color: #fff;">
					</div>
<!-- 					<div class="form-group m-t-10"> -->
<!-- 						<label for="input002" class="col-sm-3 control-label form-label">대상</label> -->
<!-- 						<div class="col-sm-4"> -->
<!-- 							<div class="radio radio-info radio-inline"> -->
<!-- 								<input type="radio" id="radio_db_01" value="1" name="radio_db" checked> -->
<!-- 								<label for="radio_db_01">수집데이터</label> -->
<!-- 							</div> -->
<!-- 						</div> -->
<!-- 						<div class="col-sm-4"> -->
<!-- 							<div class="radio radio-inline"> -->
<!-- 								<input type="radio" id="radio_db_02" value="2" name="radio_db"> -->
<!-- 								<label for="radio_db_02">처리데이터</label> -->
<!-- 							</div> -->
<!-- 						</div> -->
<!-- 					</div> -->
<!-- 					<div class="form-group m-t-10"> -->
<!-- 						<label for="input002" class="col-sm-3 control-label form-label">종류</label> -->
<!-- 						<div class="col-sm-9"> -->
<!-- 							<label class="ckbox-container">항공/정사영상 -->
<!-- 								<input id="chk_relative_aero" type="checkbox" name="chk_photo" value="aero"> -->
<!-- 								<span class="checkmark"></span> -->
<!-- 							</label> -->
<!-- 							<label class="ckbox-container">국토위성 -->
<!-- 								<input id="chk_relative_soil" type="checkbox" name="chk_photo" value="soil"> -->
<!-- 								<span class="checkmark"></span> -->
<!-- 							</label> -->
<!-- 							<label class="ckbox-container">기타위성 -->
<!-- 								<input id="chk_relative_etc" type="checkbox" name="chk_photo" value="etc" checked="checked"> -->
<!-- 								<span class="checkmark"></span> -->
<!-- 							</label> -->
<!-- 						</div> -->
<!-- 					</div> -->
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
<!-- 					<div class="form-group m-t-0"> -->
<!-- 						<label for="input002" class="col-sm-3 control-label form-label"></label> -->
<!-- 						<div class="col-sm-9"> -->
<!-- 							<label class="ckbox-container">정사영상 -->
<!-- 								<input id="chk_relative_orthophoto" type="checkbox" name="chk_photo" value="orthophoto"> -->
<!-- 								<span class="checkmark"></span> -->
<!-- 							</label> -->
<!-- 						</div> -->
<!-- 					</div> -->
					
					</div>			
<!-- 					<div class="form-group m-t-10"> -->
<!--                         <label for="input002" class="col-sm-3 control-label form-label">기간</label> -->
<!--                         <div class="col-sm-4"> -->
<!--                             <input type="text" id="datepicker3" class="form-control"> -->
<!--                         </div> -->
<!--                         <div class="col-sm-1"><span class="txt-in-form">~</span></div> -->
<!--                         <div class="col-sm-4"> -->
<!--                             <input type="text" id="datepicker4" class="form-control"> -->
<!--                         </div> -->
<!--                     </div> -->

<!--                     <script> -->
<!--                         let datepicker3 = new DatePicker(document.getElementById('datepicker3')); -->
<!--                         let datepicker4 = new DatePicker(document.getElementById('datepicker4')); -->
<!--                     </script> -->
					
<!-- 					<div class="form-group m-t-10"> -->
<!-- 						<label for="input002" class="col-sm-3 control-label form-label">해상도(m)</label> -->
<!-- 						<div class="col-sm-4"> -->
<!-- <!-- 							<select id="start_relative_resolution" class="form-basic">							 -->
<!-- 								<option></option> -->
<!-- 							</select> --> 
<!-- 							<input id="relative_start_resolution" type="text" class="form-control" value=""> -->
<!-- 						</div> -->
<!-- 						<div class="col-sm-1"><span class="txt-in-form">~</span></div> -->
<!-- 						<div class="col-sm-4"> -->
<!-- <!-- 							<select id="end_relative_resolution" class="form-basic"> -->
<!-- 								<option></option> -->
<!-- 							</select> --> 
<!-- 							<input id="relative_end_resolution" type="text" class="form-control" value=""> -->
<!-- 						</div> -->
<!-- 					</div> -->
					
<!-- 					<div class="form-group m-t-10"> -->
<!-- 						<label for="input002" class="col-sm-3 control-label form-label">운량</label> -->
<!-- 						<div class="col-sm-4"> -->
<!-- 							<div class="radio radio-info radio-inline"> -->
<!-- 								<input type="radio" id="radio_cloud_01" value="" name="radio_cloud" checked> -->
<!-- 								<label for="radio_cloud_01">전체</label> -->
<!-- 							</div> -->
<!-- 						</div> -->
<!-- 						<div class="col-sm-4"> -->
<!-- 							<div class="radio radio-inline"> -->
<!-- 								<input type="radio" id="radio_cloud_02" value="10" name="radio_cloud"> -->
<!-- 								<label for="radio_cloud_02">10% 이내</label> -->
<!-- 							</div> -->
<!-- 						</div> -->
<!-- 					</div> -->
					<!-- =========================== -->
<!-- 					<div class="form-group m-t-6" style="margin-top: 8px;"> -->
<!-- 						<label class="col-sm-3 control-label form-label">재난 ID</label> -->
<!-- 						<div class="col-sm-9"> -->
<!-- 							<input type="text" class="form-control" id="disasterIdCreate" -->
<!-- 								name="disasterId" readOnly="readOnly" -->
<!-- 								style="cursor: auto; background-color: #fff;"> -->
<!-- 						</div> -->
<!-- 					</div> -->


<!-- 					<div class="form-group m-t-6" id="dataKindContainer"> -->
<!-- 						<label for="input002" class="col-sm-2 control-label form-label">종류</label> -->
<!-- 						<div class="col-sm-10"> -->
<!-- 							<label class="ckbox-container" -->
<!-- 								style="margin-right: 1px; font-size: 12px;">기존 데이터 <input -->
<!-- 								type="checkbox" id="dataKindCurrent" name="dataKindCurrent" -->
<!-- 								checked="checked"> <span class="checkmark"></span> -->
<!-- 							</label> <label class="ckbox-container" -->
<!-- 								style="margin-right: 1px; font-size: 12px;">긴급 영상 <input -->
<!-- 								type="checkbox" id="dataKindEmergency" -->
<!-- 								name="dataKindEmergency"> <span class="checkmark"></span> -->
<!-- 							</label> <label class="ckbox-container" -->
<!-- 								style="margin-right: 1px; font-size: 12px;">분석결과 데이터 <input -->
<!-- 								type="checkbox" id="dataKindResult" name="dataKindResult"> -->
<!-- 								<span class="checkmark"></span> -->
<!-- 							</label> -->
<!-- 						</div> -->
<!-- 					</div> -->
<!-- 					<div id="datepickerContainer" -->
<!-- 						class="form-group m-t-6 input-daterange"> -->
<!-- 						<label for="input002" class="col-sm-2 control-label form-label">기간</label> -->
<!-- 						<div class="col-sm-5"> -->
<!-- 							<input type="text" class="form-control ui-input-datepicker" -->
<!-- 								id="inputStartDate" name="dateFrom" value="2020-04-23"> -->
<!-- 							<button type="button" id="btnOpenDatepickerStart" -->
<!-- 								class="btn btn-default btn-icon ui-btn-open-datepicker"> -->
<!-- 								<i class="fas fa-calendar-alt"></i> -->
<!-- 							</button> -->
<!-- 						</div> -->

<!-- 						<div class="col-sm-5"> -->
<!-- 							<input type="text" class="form-control ui-input-datepicker" -->
<!-- 								id="inputEndDate" name="dateTo" value="2020-04-26"> -->
<!-- 							<button type="button" id="btnOpenDatepickerEnd" -->
<!-- 								class="btn btn-default btn-icon ui-btn-open-datepicker"> -->
<!-- 								<i class="fas fa-calendar-alt"></i> -->
<!-- 							</button> -->
<!-- 						</div> -->
<!-- 					</div> -->
<!-- 					<div class="form-group m-t-6"> -->
<!-- 						<label for="input002" class="col-sm-3 control-label form-label">해상도(m)</label> -->
<!-- 						<div class="col-sm-4"> -->
<!-- 							<input type="number" class="form-basic" name="resolMin" /> -->
<!-- 																<select class="form-basic" name="resolMin"> -->
<!-- 																	<option value='5cm' selected>5</option> -->
<!-- 																	<option value='10cm'>10</option> -->
<!-- 																	<option value='25cm'>25</option> -->
<!-- 																</select> -->
<!-- 						</div> -->
<!-- 						<div class="col-sm-1"> -->
<!-- 							<span class="txt-in-form">~</span> -->
<!-- 						</div> -->
<!-- 						<div class="col-sm-4"> -->
<!-- 							<input type="number" class="form-basic" name="resolMax" /> -->
<!-- 																<select class="form-basic" name="resolMax"> -->
<!-- 																	<option value='5cm'>5</option> -->
<!-- 																	<option value='10cm'>10</option> -->
<!-- 																	<option value='25cm' selected>25</option> -->
<!-- 							</select> -->
<!-- 						</div> -->
<!-- 					</div> -->
<!-- 					<div class="form-group m-t-6"> -->
<!-- 						<label class="col-sm-3 control-label form-label">재난 유형</label> -->
<!-- 						<div class="col-sm-9"> -->
<!-- 							<select id="selectDisasterType" class="form-basic" -->
<!-- 								name="disasterType"> -->
<!-- 								<option value='AllDisaster'>전체</option> -->
<!-- 								<option value='Flood'>수해</option> -->
<!-- 								<option value='Landslide'>산사태</option> -->
<!-- 								<option value='ForestFire'>산불</option> -->
<!-- 								<option value='Earthquake'>지진</option> -->
<!-- 								<option value='MaritimeDisaster'>해양재난</option> -->
<!-- 							</select> -->
<!-- 						</div> -->
<!-- 					</div> -->

					<!-- <div class="form-group m-t-6">
						<label class="col-sm-3 control-label form-label">재난 ID</label>
						<div class="col-sm-9">
							<input type="text" class="form-control" id="disasterId"
								name="disasterId" readOnly="readOnly"
								style="cursor: auto; background-color: #fff;">
						</div>
					</div> -->

<!-- 					<div class="form-group m-t-6"> -->
<!-- 						<label class="col-sm-12 control-label form-label">재난지역</label> -->
<!-- 						<div class="col-sm-4"> -->
<!-- 							<select class="form-basic" name="sido" -->
<!-- 								onChange="getSigungu(this.value)"> -->
<!-- 								<option selected disabled hidden>광역시도</option> -->
<!-- 							</select> -->
<!-- 						</div> -->
<!-- 						<div class="col-sm-4"> -->
<!-- 							<select class="form-basic" name="sigungu" -->
<!-- 								onChange="getEmd(this.value)" disabled> -->
<!-- 								<option selected disabled hidden>시군구</option> -->
<!-- 							</select> -->
<!-- 						</div> -->
<!-- 						<div class="col-sm-4"> -->
<!-- 							<select class="form-basic" name="emd" -->
<!-- 								onChange="getEmdPosition(this.value)" disabled> -->
<!-- 								<option selected disabled hidden>읍면동</option> -->
<!-- 							</select> -->
<!-- 						</div> -->
<!-- 					</div> -->
<!-- 					<div class="form-group m-t-6"> -->
<!-- 						<div class="col-sm-12"> -->
<!-- 							<input type="text" id="detailAddrForm" readOnly="readOnly" -->
<!-- 								class="form-basic"> -->
<!-- 						</div> -->
<!-- 					</div> -->


<!-- 					<div class="form-group m-t-6"> -->
<!-- 						<label for="input002" class="col-sm-2 control-label form-label">ROI</label> -->
<!-- 						<div class="col-sm-10"> -->
<!-- 							<div class="col-sm-2 in-tit">ULX</div> -->
<!-- 							<div class="col-sm-4"> -->
<!-- 								<input type="text" class="form-control js-input-roi-extent" -->
<!-- 									id="inputULX" name="ulx4326"> <input type="hidden" -->
<!-- 									id="inputULX2" name="ulx5186"> <input type="hidden" -->
<!-- 									id="inputULX3" name="ulx5179"> <input type="hidden" -->
<!-- 									id="inputULX4" name="ulx32652"> -->
<!-- 							</div> -->
<!-- 							<div class="col-sm-2 in-tit">ULY</div> -->
<!-- 							<div class="col-sm-4"> -->
<!-- 								<input type="text" class="form-control js-input-roi-extent" -->
<!-- 									id="inputULY" name="uly4326"> <input type="hidden" -->
<!-- 									id="inputULY2" name="uly5186"> <input type="hidden" -->
<!-- 									id="inputULY3" name="uly5179"> <input type="hidden" -->
<!-- 									id="inputULY4" name="uly32652"> -->
<!-- 							</div> -->
<!-- 							<div class="col-sm-2 in-tit m-t-5">LRX</div> -->
<!-- 							<div class="col-sm-4 m-t-5"> -->
<!-- 								<input type="text" class="form-control js-input-roi-extent" -->
<!-- 									id="inputLRX" name="lrx4326"> <input type="hidden" -->
<!-- 									id="inputLRX2" name="lrx5186"> <input type="hidden" -->
<!-- 									id="inputLRX3" name="lrx5179"> <input type="hidden" -->
<!-- 									id="inputLRX4" name="lrx32652"> -->
<!-- 							</div> -->
<!-- 							<div class="col-sm-2 in-tit m-t-5">LRY</div> -->
<!-- 							<div class="col-sm-4 m-t-5"> -->
<!-- 								<input type="text" class="form-control js-input-roi-extent" -->
<!-- 									id="inputLRY" name="lry4326"> <input type="hidden" -->
<!-- 									id="inputLRY2" name="lry5186"> <input type="hidden" -->
<!-- 									id="inputLRY3" name="lry5179"> <input type="hidden" -->
<!-- 									id="inputLRY4" name="lry32652"> -->
<!-- 							</div> -->
<!-- 						</div> -->
<!-- 					</div> -->
					<!-- =========================== -->
					
					<div class="sidepanel-s-title m-t-30 a-cent">
						<a href="javascript:fnRelativeSearch()" class="btn btn-default"><i class="fas fa-search m-r-5"></i>검색</a>
					</div>					
					<div class="sidepanel-s-title m-t-30">
					검색 결과
					<!-- <a href="" class="btn btn-light f-right" data-toggle="modal" data-target="#myModal2"><i class="fas fa-project-diagram m-r-5"></i>수행</a> -->
					<button type="button" class="btn btn-light  f-right" onclick="fnShowPopup('relativeModal');" ><i class="fas fa-project-diagram m-r-5"></i>수행</button>
					
					</div>
					<div class="modal fade" id="relativeModal" tabindex="-1" role="dialog" aria-hidden="true">
						<div class="modal-dialog modal-sm">
							<div class="modal-content">
								<div class="modal-header">
									<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
									<h4 class="modal-title">상대방사보정</h4>
								</div>
								<div class="modal-body">
									<div class="panel-body">					
										<form class="">
										<div class="form-group m-t-10">
											<label class="col-sm-4 control-label form-label">기준영상</label>
											<div class="col-sm-8">
<!-- 												<select id="sel_photo_basic" class="form-basic" onchange="fnChangeinputPicture(this.value);"> -->
												<select id="sel_photo_basic" class="form-basic">
													<option></option>
												</select>            
											</div>
										</div>
										<div class="form-group m-t-10">
											<label class="col-sm-4 control-label form-label">대상영상</label>
											<div class="col-sm-8">
												<select id="sel_photo_target" class="form-basic" >
													<option></option>
												</select>            
											</div>
										</div>
										<div class="form-group m-t-10">
											<label class="col-sm-4 control-label form-label">알고리즘 </label>
											<div class="col-sm-8">
												<select id="sel_algorithm" class="form-basic">
													<!-- <option value="Histogram Matching">Histogram Matching</option> -->													
												</select>            
											</div>
										</div>
										<div class="form-group m-t-10">
											<label for="histogram_01" class="col-sm-4 control-label form-label">히스토그램영역</label>
											<div class="col-sm-3">
												<div class="radio radio-info radio-inline">
													<input type="radio" id="histogram_01" value="1" name="histogram_radio">
													<label for="histogram_01">전체영역</label>
												</div>
											</div>
											<div class="col-sm-3">
												<div class="radio radio-inline">
													<input type="radio" id="histogram_02" value="2" name="histogram_radio" checked>
													<label for="histogram_02">중첩영역</label>
												</div>
											</div>
										</div>										
										<div class="form-group m-t-10">
											<label for="txt_relative_output" class="col-sm-4 control-label form-label">결과영상</label>
											<div class="col-sm-8">
												<input type="text" id="txt_relative_output" class="form-control" value="">
											</div>
										</div>
										</form> 

									</div>
								</div>
								<div class="modal-footer">
									<button type="button" class="btn btn-default" onclick="javascript:fnRelativeProcess();"><i class="fas fa-check-square m-r-5"></i>처리</button>
									<button type="button" class="btn btn-gray" data-dismiss="modal" aria-label="Close"><i class="fas fa-times m-r-5"></i>취소</button>
									<button id="btn_process" type="button" class="btn btn-default" onclick="fnScriptSavePopup('3')"><i class="fas fa-check-square m-r-5"></i>스크립트저장</button>
								</div>
							</div>
						</div>
					</div>
					
					
					<div class="panel-body">
						<div class="col-lg-12 js-search-result-area">
							<div id="div_result_relative_list" class="panel panel-default" style="height:200px;">
							</div>
						</div>
					</div>
					<div class="sidepanel-s-title m-t-30">
					처리 결과
					</div>
					<div class="panel-body">
						<div class="col-lg-12">
							<div class="panel panel-default" style="height:200px;">
								<dl id="div_relative_result" class="result-list-input">
									<!-- <dd>Result_Landsat 5 TM Collection 1 Level 1</dd>
									<dd>
										<code class="year">연도</code>2020/05/20
										<code class="position">위치</code>안양/37612079
									</dd> -->
								</dl>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>

	</div>
</div>

	<div class="modal fade" id="scriptModal" tabindex="-1" role="dialog" aria-hidden="true">
		<div class="modal-dialog modal-sm">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title">스크립트저장</h4>
				</div>
				<div class="modal-body">
					<div class="panel-body">					
						<form class="">
							<div class="form-group m-t-10">
							<label for="txt_meta" class="col-sm-4 control-label form-label">스크립트명</label>
							<div class="col-sm-8">
								<input id="input_script_nm" type="text" class="form-control" value="">
							</div>
							<input type="hidden" id="script_kind" value="">
							
							</div>
						</form> 
					</div>
				</div>
				<div class="modal-footer">
					<button id="btn_process" type="button" class="btn btn-default" onclick="fnScriptSave();"><i class="fas fa-check-square m-r-5"></i>저장</button>
					<button type="button" class="btn btn-gray" data-dismiss="modal" aria-label="Close"><i clas="fas fa-times m-r-5"></i>취소</button>
				</div>
			</div>
		</div>
	</div>

<!-- START CONTENT -->
<div class="content">
	<div class="map-wrap"  id="map1" style="background-size:100% auto; height:100%;">

	</div>
</div>

<a href="#" class="sidebar-open-button"><i class="fa fa-bars"></i></a>
<a href="#" class="sidebar-open-button-mobile"><i class="fa fa-bars"></i></a>

<div class="load" id="progress" style="display: none;"></div>
<!-- 
<div class="content">

	<div class="page-header">
		<h1 class="title">재난정보 수집설정</h1>

	</div>
</div>
 -->

</body>
</html>