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
<style>
#table_detail2 button.btn {
	padding: 0px 7px;
}

.ui-btn-clicked-layer-preview {
	box-shadow: inset 1px 1px 2px 1px #626262;
	background-color: #cbcbcb;
}

.ui-layer-title {
	width: 150px;
	display: inline-block;
}

#table_detail2 li.space_context_li {
	list-style-type: none;
}

#table_detail2 li.space_context_li input.ui-result-check {
	vertical-align: top;
	margin-top: 4px;
	margin-right: 4px;
}

#table_detail2 li.space_context_li label {
	font-weight: normal;
}
/* 	#table_detail2 li.space_context_li:before { */
/* 		content: "•"; */
/* 		font-size: 80%; */
/* 		padding-right: 10px; */
/*     	vertical-align: top; */
/* 	} */
</style>
</head>
<!-- ================================================
jQuery Library
================================================ -->
<script src="js/jquery.min.js"></script>
<!-- ================================================
jQuery UI
================================================ -->
<script src="js/jquery-ui/jquery-ui.min.js"></script>
<link rel="stylesheet" href="/css/jquery-ui.css">
<!-- ================================================
map Library
================================================ -->
<script src="js/map/v6.7.0/ol.js"></script>
<link rel="stylesheet" href="/js/map/v6.7.0/ol.css">
<%--<script src="https://cdnjs.cloudflare.com/ajax/libs/proj4js/1.1.0/proj4js-combined.min.js"></script>--%>
<script type="text/javascript" src="js/map/proj4js-combined.js"></script>
<script src="js/map/map.js"></script>
<script src="js/map/gis.js"></script>
<!-- 
<link rel="stylesheet" href="//code.jquery.com/ui/1.13.0/themes/base/jquery-ui.css">
-->
<script src="js/cm/cmsc003/lib/proj4js/proj4.js"></script>
<!-- <script
	src="js/cm/cmsc003/lib/bootstrap-datepicker-1.9.0-dist/js/bootstrap-datepicker.min.js"></script>
<script
	src="js/cm/cmsc003/lib/bootstrap-datepicker-1.9.0-dist/locales/bootstrap-datepicker.ko.min.js"></script> -->
<script src="js/cm/cmsc003/lib/shp2geojson/lib/jszip.js"></script>
<script src="js/cm/cmsc003/lib/shp2geojson/lib/jszip-utils.js"></script>
<script src="js/cm/cmsc003/lib/shp2geojson/preprocess.js"></script>
<script src="js/cm/cmsc003/lib/turf-6.5.0/turf.min.js"></script>
<!-- from cmsc003 -->
<script src="js/cm/cmsc003/src/cmsc003.js"></script>
<script src="js/cm/cmsc003/src/gis/gis.js"></script>
<script src="js/cm/cmsc003/src/storage/storage.js"></script>
<script src="js/cm/cmsc003/src/dom/dom.js"></script>
<script src="js/cm/cmsc003/src/converter/converter.js"></script>
<script src="js/cm/cmsc003/src/util/util.js"></script>

<body>
	<!-- 상단메뉴 -->
	<%@ include file="../topMenu.jsp"%>


	<!-- //////////////////////////////////////////////////////////////////////////// -->
	<!-- START SIDEBAR -->
	<div class="sidebar clearfix">
		<div role="tabpanel" class="sidepanel" style="display: block;">

			<!-- Nav tabs -->
			<ul class="nav nav-tabs" role="tablist">
				<li role="presentation" class="active CHART" style="width: 50%">
					<a href="#today" aria-controls="today" role="tab" data-toggle="tab"
					aria-expanded="true" class="active"> <i class="fas fa-search"></i><br>재난정보
						조회
				</a>
				</li>
				<li role="presentation" class="SPACE" style="width: 50%"><a
					href="#tasks" aria-controls="tasks" role="tab" data-toggle="tab"
					class="" aria-expanded="false"> <i class="fas fa-globe-asia"></i><br>재난지역
						자료검색
				</a></li>
			</ul>
			<!-- Tab panes -->
			<div class="tab-content">
				<div role="tabpanel" class="tab-pane active" id="today">
					<div class="sidepanel-m-title">재난정보 검색</div>
					<div class="panel-body">
						<div class="form-group m-t-10">
							<label for="MID" class="col-sm-3 control-label form-label">재난 ID</label>
							<div class="col-sm-9">
								<select class="form-basic" id="MID">
									<option value="default" selected disabled hidden></option>
								</select>    
							</div>
						</div>
						<div class="form-group m-t-10">
							<label for="SEARCH_KEYWORD"
								class="col-sm-3 control-label form-label">키워드</label>
							<div class="col-sm-9">
								<input type="text" class="form-control" id="SEARCH_KEYWORD">
							</div>
						</div>
						<div class="form-group m-t-10">
							<div style="float: right">
								<input type="checkbox" id="within" name="within"
									onclick="handleClick()"> <label for="within">결과
									내 검색</label>
							</div>
						</div>
						<div class="form-group m-t-10">
							<label class="col-sm-3 control-label form-label">재난유형</label>
							<div class="col-sm-9">
								<select class="form-basic" id="MSFRTN_TY_CD">
									<option value="default" selected disabled hidden></option>
								</select>
							</div>
						</div>
						<div class="form-group m-t-10">
							<label class="col-sm-12 control-label form-label">재난지역</label>
							<div class="col-sm-4">
								<select class="form-basic" id="CTPRVN"
									onchange="CTPRVN_Change()">
									<option value="default" selected>광역시도</option>
								</select>
							</div>
							<div class="col-sm-4">
								<select class="form-basic" id="SGG" onchange="SGG_Change()">
									<option value="default" selected>시군구</option>
								</select>
							</div>
							<div class="col-sm-4">
								<select class="form-basic" id="EMD">
									<option value="default" selected>읍면동</option>
								</select>
							</div>
						</div>
						<div class="form-group m-t-10">
							<label class="col-sm-12 control-label form-label">기간</label>
							<div class="col-sm-4">
								<input class="form-control" id="start_period">
								<script>
							$.datepicker.setDefaults({
								  dateFormat: 'yy-mm-dd',
								  prevText: '이전 달',
								  nextText: '다음 달',
								  monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
								  monthNamesShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
								  dayNames: ['일', '월', '화', '수', '목', '금', '토'],
								  dayNamesShort: ['일', '월', '화', '수', '목', '금', '토'],
								  dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],
								  showMonthAfterYear: true,
								  yearSuffix: '년',
								  maxDate: 0
									});
							$('#start_period').datepicker({
						        onClose: function( selectedDate ) {    
						            // 시작일(start_period) datepicker가 닫힐때
						            // 종료일(end_period)의 선택할수있는 최소 날짜(minDate)를 선택한 시작일로 지정
						            $("#end_period").datepicker( "option", "minDate", selectedDate );
						        } 
							})
							$('#end_period').datepicker({
						        onClose: function( selectedDate ) {    
						              // 시작일(start_period) datepicker가 닫힐때
						              // 종료일(end_period)의 선택할수있는 최소 날짜(minDate)를 선택한 시작일로 지정
						          	$("#start_period").datepicker( "option", "maxDate", selectedDate );
						          } 
							})
							$(function(){
						  		$('#start_period').datepicker();
						  		$('#end_period').datepicker();
							})
							</script>
							</div>
							<div class="col-sm-2">
								<button type="button" class="btn btn-default btn-icon"
									onclick="Periodbtn_Click('start')">
									<i class="fas fa-calendar-alt"></i>
								</button>
							</div>
							<div class="col-sm-4">
								<input type="text" class="form-control datepicker"
									id="end_period">
							</div>
							<div class="col-sm-2">
								<button type="button" class="btn btn-default btn-icon"
									onclick="Periodbtn_Click('end')">
									<i class="fas fa-calendar-alt"></i>
								</button>
							</div>
						</div>
						<div class="form-group m-t-10">
							<div style="float: right">
								<button type="button" class="btn btn-default btn-icon" onclick="Search()">검색  <i class="fas fa-search"></i></button>
							</div>
						</div>
					</div>
				</div>
				<div class="modal fade" id="notice" tabindex="-1" role="dialog"
					aria-hidden="false">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal"
									aria-label="Close">
									<span aria-hidden="true">&times;</span>
								</button>
								<p class="modal-title" id="notice_title"></p>
							</div>
							<div class="modal-body">
								<div class="news-wrap">
									<p class="subj center" id="notice_context"></p>
								</div>
							</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-default"
									data-dismiss="modal">확인</button>
							</div>
						</div>
					</div>
				</div>
				<div role="tabpanel" class="tab-pane" id="tasks">
					<div class="sidepanel-m-title">재난 공간정보 검색</div>
					<div class="panel-body">
						<div class="form-group m-t-10">
							<label for="SPACE_MID" class="col-sm-3 control-label form-label">재난 ID</label>
							<div class="col-sm-9">
								<select class="form-basic" id="SPACE_MID">
									<option value="default" selected disabled hidden></option>
								</select>    
							</div>
						</div>
						<div class="form-group m-t-10">
							<label for="SPACE_KEYWORD"
								class="col-sm-3 control-label form-label">키워드</label>
							<div class="col-sm-9">
								<input type="text" class="form-control" id="SPACE_KEYWORD">
							</div>
						</div>
						<form class="">
							<div class="form-group m-t-10">
								<label class="col-sm-3 control-label form-label">재난유형</label>
								<div class="col-sm-9">
									<select class="form-basic" id="SPACE_MSFRTN_TY_CD">
										<option value="default" selected disabled hidden></option>
									</select>
								</div>
							</div>
							<div class="form-group m-t-10">
								<label class="col-sm-12 control-label form-label">재난지역</label>
								<div class="col-sm-4">
									<select class="form-basic" id="SPACE_CTPRVN"
										onchange="CTPRVN_Change('SPACE')">
										<option value="default" selected>광역시도</option>
									</select>
								</div>
								<div class="col-sm-4">
									<select class="form-basic" id="SPACE_SGG"
										onchange="SGG_Change('SPACE')">
										<option value="default" selected>시군구</option>
									</select>
								</div>
								<div class="col-sm-4">
									<select class="form-basic" id="SPACE_EMD">
										<option value="default" selected>읍면동</option>
									</select>
								</div>
							</div>
							<div class="form-group m-t-10">
								<div style="float: right">
									<button type="button" class="btn btn-default btn-icon" onclick="Space_Search()">검색  <i class="fas fa-search"></i></button>
								</div>
							</div>
						</form>
					</div>
					<div class="sidepanel-s-title m-t-30">검색 결과</div>
					<div class="panel-body">
						<div class="col-lg-12">
							<div class="panel panel-default" style="height: 300px">
								<div class="in-panel-title">재난정보 목록</div>
								<div class="result-list">
									<table id="table_detail" cellpadding=10>
										<tr onclick="show_hide_row('DTC001');" id="풍수해">
											<td></td>
										</tr>
										<tr id="DTC001" class="hidden_row">
											<td><ul id="DTC001_List"></ul></td>
										</tr>

										<tr onclick="show_hide_row('DTC002');" id="지진">
											<td></td>
										</tr>
										<tr id="DTC002" class="hidden_row">
											<td><ul id="DTC002_List"></ul></td>
										</tr>

										<tr onclick="show_hide_row('DTC003');" id="산불">
											<td></td>
										</tr>
										<tr id="DTC003" class="hidden_row">
											<td><ul id="DTC003_List"></ul></td>
										</tr>

										<tr onclick="show_hide_row('DTC004');" id="산사태">
											<td></td>
										<tr>
										<tr id="DTC004" class="hidden_row">
											<td><ul id="DTC004_List"></ul></td>
										</tr>

										<tr onclick="show_hide_row('DTC005');" id="대형화산폭발">
											<td></td>
										</tr>
										<tr id="DTC005" class="hidden_row">
											<td><ul id="DTC005_List"></ul></td>
										</tr>

										<tr onclick="show_hide_row('DTC006');" id="조수">
											<td></td>
										</tr>
										<tr id="DTC006" class="hidden_row">
											<td><ul id="DTC006_List"></ul></td>
										</tr>

										<tr onclick="show_hide_row('DTC007');" id="유해화학물질유출사고">
											<td></td>
										</tr>
										<tr id="DTC007" class="hidden_row">
											<td><ul id="DTC007_List"></ul></td>
										</tr>

										<tr onclick="show_hide_row('DTC008');" id="대규모해양오염">
											<td></td>
										</tr>
										<tr id="DTC008" class="hidden_row">
											<td><ul id="DTC008_List"></ul></td>
										</tr>

										<tr onclick="show_hide_row('DTC009');" id="다중밀집시설대형화재">
											<td></td>
										</tr>
										<tr id="DTC009" class="hidden_row">
											<td><ul id="DTC009_List"></ul></td>
										</tr>

										<tr onclick="show_hide_row('DTC010');" id="댐붕괴">
											<td></td>
										</tr>
										<tr id="DTC010" class="hidden_row">
											<td><ul id="DTC010_List"></ul></td>
										</tr>

										<tr onclick="show_hide_row('DTC011');" id="고속철도대형사고">
											<td></td>
										</tr>
										<tr id="DTC011" class="hidden_row">
											<td><ul id="DTC011_List"></ul></td>
										</tr>

										<tr onclick="show_hide_row('DTC012');" id="다중밀집건축물붕괴대형사고">
											<td></td>
										</tr>
										<tr id="DTC012" class="hidden_row">
											<td><ul id="DTC012_List"></ul></td>
										</tr>
										<tr onclick="show_hide_row('DTC013');" id="적조">
											<td></td>
										</tr>
										<tr id="DTC013" class="hidden_row">
											<td><ul id="DTC013_List"></ul></td>
										</tr>

									</table>
								</div>
								<div class="in-panel-title">공간정보 목록</div>
								<p style="color:gray; font-size:12px;"> · 재난 POI좌표 중심 2Km 공간 버퍼를 적용한 결과입니다.</p>
								<div class="result-list">
									<table id="table_detail2" cellpadding=10>
										<tr class="space_result" onclick="show_hide_row('con');"
											id="연속수치지도">
											<td></td>
										</tr>
										<tr id="con" class="hidden_row">
											<td><ul id="con_List" style="padding-left: 8px;"></ul></td>
										</tr>

										<tr class="space_result" onclick="show_hide_row('map');"
											id="수치지도">
											<td></td>
										</tr>
										<tr id="map" class="hidden_row">
											<td><ul id="map_List"></ul></td>
										</tr>

										<tr class="space_result" onclick="show_hide_row('air');"
											id="항공사진">
											<td></td>
										</tr>
										<tr id="air" class="hidden_row">
											<td><ul id="air_List"></ul></td>
										</tr>

										<tr class="space_result" onclick="show_hide_row('ort');"
											id="정사영상">
											<td></td>
										</tr>
										<tr id="ort" class="hidden_row">
											<td><ul id="ort_List"></ul></td>
										</tr>

										<tr class="space_result" onclick="show_hide_row('sat');"
											id="위성영상">
											<td></td>
										<tr>
										<tr id="sat" class="hidden_row">
											<td><ul id="sat_List"></ul></td>
										</tr>

										<tr class="space_result" onclick="show_hide_row('dem');"
											id="DEM">
											<td></td>
										</tr>
										<tr id="dem" class="hidden_row">
											<td><ul id="dem_List"></ul></td>
										</tr>
									</table>
								</div>
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

	</div>

	<!-- START CONTENT -->
	<div class="content" id="chart" style="display: inline-block">

		<div class="page-header">
			<h1 class="title">재난정보 조회</h1>

		</div>
		<div class="container-widget">
			<div id="loading">
				<img src="img/loading_1.svg">
			</div>
			<!-- 연합뉴스 -->
			<div class="col-lg-7">
				<div class="panel panel-default">
					<div class="panel-title">
						<i class="fas fa-chart-line"></i>재난정보 검색 결과 추이 그래프
					</div>
					<div class="panel-body">

						<!-- <script type="text/javascript" src="https://www.amcharts.com/lib/3/amcharts.js"></script>
					<script type="text/javascript" src="https://www.amcharts.com/lib/3/serial.js"></script> -->
						<script src="js/core.js"></script>
						<script src="js/charts.js"></script>
						<div id="chartdiv"
							style="width: 100%; height: 290px; background-color: #FFFFFF;"></div>
					</div>
				</div>
			</div>

			<!-- Start Chart -->
			<div class="col-lg-5">
				<div class="panel panel-default">
					<div class="panel-title">
						<i class="fas fa-cloud"></i>검색 결과 재난 정보 워드 클라우드
					</div>
					<div class="panel-body">
						<script src="js/core.js"></script>
						<script src="js/charts.js"></script>
						<script src="js/wordCloud.js"></script>
						<script src="js/animated.js"></script>
						<!-- <script type="text/javascript" src="js/material.js"></script> -->
						<!-- <script type="text/javascript" src="js/kelly.js"></script> -->

						<div id="wordclouddiv"
							style="width: 100%; height: 290px; background-color: #FFFFFF;"></div>
					</div>
				</div>
			</div>


			<div class="col-md-3">
				<div class="panel panel-default">
					<div class="panel-title">
						<i class="fas fa-chart-pie"></i>재난 유형별 통계
					</div>
					<div class="panel-body">
						<!-- <script type="text/javascript" src="https://www.amcharts.com/lib/3/amcharts.js"></script>
				<script type="text/javascript" src="https://www.amcharts.com/lib/3/pie.js"></script>
				<script type="text/javascript" src="https://www.amcharts.com/lib/3/themes/chalk.js"></script> -->
						<div id="chartdiv2"
							style="width: 100%; height: 400px; background-color: #fff;"></div>
					</div>
				</div>
			</div>

			<div class="col-md-3">
				<div class="panel panel-default">
					<div class="panel-title">
						<i class="fas fa-globe-asia"></i>재난 발생 지역별 통계
					</div>
					<div class="panel-body">
						<div id="chartdiv3"
							style="width: 100%; height: 400px; background-color: #fff;"></div>
					</div>
				</div>
			</div>

			<div class="col-md-3">
				<div class="panel panel-default">
					<div class="panel-title">
						<i class="fas fa-chart-pie"></i>재난 발생 기간별 통계
					</div>
					<div class="panel-body">
						<div id="chartdiv4"
							style="width: 100%; height: 400px; background-color: #fff;"></div>
					</div>
				</div>
			</div>

			<div class="col-md-3">
				<div class="panel panel-default">
					<div class="panel-title">
						<i class="fas fa-globe-asia"></i>재난 피해별 통계
					</div>
					<div class="panel-body">
						<div id="chartdiv5"
							style="width: 100%; height: 400px; background-color: #fff;"></div>
					</div>
				</div>
			</div>


			<div class="col-md-12">
				<div class="panel panel-default">
					<div class="panel-title">
						<i class="fas fa-list"></i>검색 결과 재난정보 목록
					</div>
					<div class="panel-body table-responsive">
						<table class="table table-hover table-striped" id="SEARCH_TBL"
							style="width: 100%;"></table>
					</div>
				</div>
			</div>
			<div class="modal fade" id="Search_Modal" tabindex="-1" role="dialog"
				aria-hidden="false">
				<div class="modal-dialog" style="width: 800px">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal"
								aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
							<h4 class="modal-title">재난 뉴스 상세 보기</h4>
						</div>
						<div class="modal-body">
							<div class="news-wrap">
								<div id="meta-wrap"></div>
								<p class="subj" id="SEARCH_TBL_SJ_NM"></p>
								<p class="date" id="SEARCH_TBL_NES_DT"></p>
								<div class="article-wrap" id="SEARCH_TBL_ORIGINL_FILE_COURS_NM"></div>
								<br>
								<div id="meta-wrap2"></div>
								<div class="row img-wrap">
									<div class="col-md-2 " id="search_img_list"
										style="border-right: 1px solid #ddd; max-height:400px; overflow-y:auto;"></div>
									<div class="col-md-10" id="search_img_viewer"></div>
								</div>
							</div>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-default"
								data-dismiss="modal">확인</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="content" id="space_map" style="width: 100%;">
		<div class="container-widget">
			<div id="loading2">
				<img src="img/loading_1.svg">
			</div>
			<!-- <div class="col-md-12"> -->
			<div class="map-wrap-n" id="map1" style="right:342px;">맵 영역</div>

		</div>
	</div>
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
Sweet alert
================================================ -->
	<script src="js/sweet-alert/sweet-alert.min.js"></script>

	<!-- ================================================
Kode alert
================================================ -->
	<script src="js/kode-alert/main.js"></script>



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
Below codes are only for index widgets
================================================ -->

<script>
var search_img="";
myStorage = window.localStorage;
var loading_img = document.getElementById("loading");
var loading_img2 = document.getElementById("loading2");
var con_list = [];
var search2Result = null;
var layerObject = {};
var currentSelectedPostion = null;

window.onload = function(){
	MSFRTN_TY_CD();
	CTPRVN();
	MID();
	myStorage.removeItem("Search");
	Placeholder();
// 	CMSC003.GIS.registProj4();
};

$("ul.nav-tabs li.SPACE").click(function(){
	//console.log("space click")
	MSFRTN_TY_CD('SPACE')
	CTPRVN('SPACE')
	MID('SPACE')
	var space_map = document.getElementById("space_map")
	var chart = document.getElementById("chart")
	space_map.style.display="inline-block"
	chart.style.display="none"
		
	
})

$("ul.nav-tabs li.CHART").click(function(){
	//console.log("chart click")
	var space_map = document.getElementById("space_map")
	var chart = document.getElementById("chart")
	space_map.style.display="none"
	chart.style.display="inline-block"
})

//미리보기 버튼 클릭 - all
$('#table_detail2').on('click', '.js-preview', function(e) {
	e.stopPropagation();
	console.log($(this).val());
	
	var value = $(this).val();
	var valueArr = value.split('_');
	if ($(this).hasClass('ui-btn-clicked-layer-preview')) {
		$(this).removeClass('ui-btn-clicked-layer-preview');
// 		CMSC003.GIS.removeLayerById($(this).val());
		hideLayerByLabel(value);
	} else {
		$(this).addClass('ui-btn-clicked-layer-preview');
		var selected = getOtherLayerObjectByValue(valueArr[0], valueArr[1], valueArr[2]);
		console.log(selected);
		loadRasterLayerByParam(value, selected);
// 		CMSC003.GIS.updateImageLayerFromGeoserverDemographic($(this).val());
		//currentPreviewLayer[value] = 
	}
});

// 미리보기 버튼 클릭 - 수치지도
$('#table_detail2').on('click', '.js-preview-con', function(e) {
	e.stopPropagation();
	console.log($(this).val());
	var value = $(this).val();
	var valueArr = value.split('_');
	if ($(this).hasClass('ui-btn-clicked-layer-preview')) {
		$(this).removeClass('ui-btn-clicked-layer-preview');
// 		CMSC003.GIS.removeLayerById($(this).val());
		hideLayerByLabel(value);
	} else {
		$(this).addClass('ui-btn-clicked-layer-preview');
		var selected = getConLayerObjectByValue(valueArr[0], valueArr[1]);
		console.log(selected);
		loadVectorLayerByParam(value, selected);
// 		CMSC003.GIS.updateImageLayerFromGeoserverDemographic($(this).val());
		//currentPreviewLayer[value] = 
	}
});
// 미리보기 - 항공사진
// $('#table_detail2').on('click', '.js-preview-air', function(e) {
// 	e.stopPropagation();
// 	console.log($(this).val());
// 	var value = $(this).val();
// 	if ($(this).hasClass('ui-btn-clicked-layer-preview')) {
// 		$(this).removeClass('ui-btn-clicked-layer-preview');
// 	} else {
// 		$(this).addClass('ui-btn-clicked-layer-preview');
// 	}
// });
function getOtherLayerObjectByValue(type, depth1, depth2) {
	if(search2Result && type && depth1 && depth2) {
		return search2Result[type]['data'][depth1]['data'][depth2];
	}
}

function getConLayerObjectByValue(type, depth1) {
	if(search2Result && type && depth1) {
		return search2Result[type]['data'][depth1];
	}
}

// 전송 데이터 체크박스 체크 이벤트
$('#table_detail2').on('click', 'label', function(e) {
	e.stopPropagation();
});
$('#table_detail2').on('click', 'input[type="checkbox"].js-result-check', function(e) {
	e.stopPropagation();
});
$('#table_detail2').on('change', 'input[type="checkbox"].js-result-check', function(e) {
	e.stopPropagation();
	console.log($(this).val());
	// todo: 전송전에 체크된 체크박스들 값 읽어서 전송 객체에 포함시키기
});

/*재난 ID 출력*/
function MID(ty){
	var today = new Date()
	var MIDList =[]
	var Separator = "-";
	var position1 = 4;
	var position2 = 6;
	
	if(ty=="SPACE"){
		$("select[id='SPACE_MID'] option").not("[value='default']").remove();
	    var selectEl = document.getElementById("SPACE_MID"); 
	}else{
		$("select[id='MID'] option").not("[value='default']").remove();
		console.log(document.getElementById("MID"))
	    var selectEl = document.getElementById("MID"); 
	}

 	$.ajax({
		type : "PUT",
	    url: "/request_tbl.do",
	    success:function(data){
	    	var result = JSON.parse(data)
	    	console.log('요청목록_mid ',result);
	    	for(var j in result){
	    		result[j].COLCT_END_DE = [result[j].COLCT_END_DE.slice(0, position1), Separator, result[j].COLCT_END_DE.slice(position1,position2),Separator,result[j].COLCT_END_DE.slice(position2)].join('');
	    		var check_End = new Date(result[j].COLCT_END_DE)
	    		if(result[j].COLCT_STTUS_CD!="2" && check_End > today){
	    			//console.log("22222222222",result[j])
	    			MIDList.push({"id": result[j].MSFRTN_INFO_COLCT_REQUST_ID
	    						,"Areakeyword": result[j].MSFRTN_AREA_SRCHWRD
	    						, "tykeyword" : result[j].MSFRTN_TY_SRCHWRD})    			
	    		}
	    	}
	    	console.log("MIDList",MIDList) 
			for (var i in MIDList){
				$(selectEl).append("<option value='"+ MIDList[i].id+"'>" + MIDList[i].id +"&nbsp"+ MIDList[i].Areakeyword +"&nbsp"+ MIDList[i].tykeyword + "</option>")
			} 
	    },
	    error:function(data){
	    	alert("실패", data)
	    }
	});
}

/*재난유형 출력*/
function MSFRTN_TY_CD(ty){
	//console.log("재난 유형", ty)
	if(ty=="SPACE"){
		$("select[id='SPACE_MSFRTN_TY_CD'] option").not("[value='default']").remove();
		$("#SPACE_MSFRTN_TY_CD").append("<option value='ALL'>전체</option>")
	    var selectEl = document.getElementById("SPACE_MSFRTN_TY_CD"); 
	}else{
		$("select[id='MSFRTN_TY_CD'] option").not("[value='default']").remove();
		$("#MSFRTN_TY_CD").append("<option value='ALL'>전체</option>")
	    var selectEl = document.getElementById("MSFRTN_TY_CD"); 
	}
	var param = "opt="+"type"
	$.ajax({
		type : "PUT",
	    url: "/col_requst.do",
	    data:param,
	    success:function(data){
	    	var result = JSON.parse(data);
	    	//console.log("재난정보검색-재난유형", result)
	    	for(var i in result.code){
	    		$(selectEl).append("<option value='"+ result.code[i].MSFRTN_TY_CD+"'>" +  result.code[i].MSFRTN_TY_NM + "</option>")
	    	}
	    },
	    error:function(data){
	    	//alert("실패", data)
	    }
	})
}

/*재난지역 광역시도 출력*/
function CTPRVN(ty){
	//console.log(ty)
	if(ty=="SPACE"){
		$("select[id='SPACE_CTPRVN'] option").not("[value='default']").remove();
		var selectEl = document.getElementById("SPACE_CTPRVN"); 
	}
	else{
		$("select[id='CTPRVN'] option").not("[value='default']").remove();
		var selectEl = document.getElementById("CTPRVN"); 
	}
	var param = "opt="+"ctprvn"
	$.ajax({
		type : "PUT",
	    url: "/col_requst.do",
	    data:param,
	    success:function(data){
	    	var result = JSON.parse(data);
	    	//console.log("재난정보검색- 광역시도", result)
	    	for(var i in result){
	    		$(selectEl).append("<option value='"+ result[i].code.slice(0,2)+"'>" +  result[i].name + "</option>")
	    	} 
	    },
	    error:function(data){
	    	////alert("실패", data)
	    }
	})
}

/*재난지역 광역시도 변경시 실행*/
function CTPRVN_Change(ty){
	if(ty=="SPACE"){
		var CTPRVN = document.getElementById("SPACE_CTPRVN");
		var selectValue = SPACE_CTPRVN.options[SPACE_CTPRVN.selectedIndex].value;
		//console.log("selectValue",selectValue)
		SGG(selectValue,"SPACE")
	}else{
		var CTPRVN = document.getElementById("CTPRVN");
		var selectValue = CTPRVN.options[CTPRVN.selectedIndex].value;
		//console.log("selectValue",selectValue)
		SGG(selectValue)
	}
}

/*광역시도에 맞는 시군구 출력*/
function SGG(code,ty){
	if(ty=="SPACE"){
		var selectEl = document.getElementById("SPACE_SGG");
		$("select[id='SPACE_SGG'] option").not("[value='default']").remove();
		$("select[id='SPACE_EMD'] option").not("[value='default']").remove();
	}else{
		var selectEl = document.getElementById("SGG");
		$("select[id='SGG'] option").not("[value='default']").remove();
		$("select[id='EMD'] option").not("[value='default']").remove();
	}
	var param = "opt="+"sgg"+"&code="+code
	//console.log(param)
	$.ajax({
		type : "PUT",
	    url: "/col_requst2.do",
	    data:param,
	    success:function(data){
	    	var result = JSON.parse(data);
	    	//console.log("재난정보검색 시군구", result)
	    	for(var i in result){
				if(result[i].name.indexOf(" ")==-1){
	    			$(selectEl).append("<option value='"+ result[i].code.slice(0,4)+"'>" +  result[i].name + "</option>")
	    		}else{
	    			$(selectEl).append("<option value='"+ result[i].code.slice(0,5)+"'>" +  result[i].name + "</option>")
	    		}
	    	}  
	    },
	    error:function(data){
	    	////alert("실패", data)
	    }
	})
}

/*재난 지역 시군구 변경시 실행*/
function SGG_Change(ty){
	if(ty=="SPACE"){
		var SGG = document.getElementById("SPACE_SGG");
		var selectValue = SPACE_SGG.options[SPACE_SGG.selectedIndex].value;
		//console.log("selectValue",selectValue)
		EMD(selectValue,"SPACE")
	}else{
		var SGG = document.getElementById("SGG");
		var selectValue = SGG.options[SGG.selectedIndex].value;
		//console.log("selectValue",selectValue)
		EMD(selectValue)
	}

}

/*시군구에 맞는 읍면동 출력*/
function EMD(code,ty){
	if(ty=="SPACE"){
		$("select[id='SPACE_EMD'] option").not("[value='default']").remove();
		var selectEl = document.getElementById("SPACE_EMD"); 
	}else{
		$("select[id='EMD'] option").not("[value='default']").remove();
		var selectEl = document.getElementById("EMD"); 
	}
	var param = "opt="+"emd"+"&code="+code
	//console.log(param)
	$.ajax({
		type : "PUT",
	    url: "/col_requst2.do",
	    data:param,
	    success:function(data){
	    	var result = JSON.parse(data);
	    	//console.log("재난정보검색  읍면동", result)
	    	for(var i in result){
	    		$(selectEl).append("<option value='"+ result[i].code.slice(0,5)+"'>" +  result[i].name + "</option>")
	    	}  
	    },
	    error:function(data){
	    	////alert("실패", data)
	    }
	})
}

/*재난 정보 검색 - 기간 입력 */
function Periodbtn_Click(ty){
	//console.log("click",ty)
	$.datepicker.setDefaults({
	  dateFormat: 'yy-mm-dd',
	  prevText: '이전 달',
	  nextText: '다음 달',
	  monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
	  monthNamesShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
	  dayNames: ['일', '월', '화', '수', '목', '금', '토'],
	  dayNamesShort: ['일', '월', '화', '수', '목', '금', '토'],
	  dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],
	  showMonthAfterYear: true,
	  yearSuffix: '년',
	  maxDate: 0,
		});
	if(ty=="start"){
		$('#start_period').datepicker('show');
	}
	if(ty=="end"){
		$('#end_period').datepicker('show');
	}
}

/*재난 정보 검색 조건 확인 */
function Search(){
	var MID = document.getElementById("MID");
	var MID_Value = MID.options[MID.selectedIndex].value;
	var SEARCH_KEYWORD = document.getElementById("SEARCH_KEYWORD").value;
	var MSFRTN_TY_CD = document.getElementById("MSFRTN_TY_CD");
	var MSFRTN_TY_CD_Value = MSFRTN_TY_CD.options[MSFRTN_TY_CD.selectedIndex].value;
	var CTPRVN = document.getElementById("CTPRVN");
	var CTPRVN_Value = CTPRVN.options[CTPRVN.selectedIndex].text;
	var SGG = document.getElementById("SGG");
	var SGG_Value = SGG.options[SGG.selectedIndex].text;
	var EMD = document.getElementById("EMD");
	var EMD_Value = EMD.options[EMD.selectedIndex].text;
	var START_PERIOD = document.getElementById("start_period").value;
	var END_PERIOD = document.getElementById("end_period").value;
	console.log(MID_Value,SEARCH_KEYWORD,MSFRTN_TY_CD_Value,CTPRVN_Value,SGG_Value,EMD_Value,START_PERIOD,END_PERIOD)
	if(SEARCH_KEYWORD==""){
		if(MID_Value!="default" || MSFRTN_TY_CD_Value!="default"||CTPRVN_Value!="광역시도"||START_PERIOD!=""||END_PERIOD!=""){
		//if(MSFRTN_TY_CD_Value!="default"||CTPRVN_Value!="광역시도"||START_PERIOD!=""||END_PERIOD!=""){
			//console.log("!!!!!")
			//Search_Submit(SEARCH_KEYWORD,MSFRTN_TY_CD_Value,CTPRVN_Value,SGG_Value,EMD_Value,START_PERIOD,END_PERIOD)
			Search_Submit(MID_Value,SEARCH_KEYWORD,MSFRTN_TY_CD_Value,CTPRVN_Value,SGG_Value,EMD_Value,START_PERIOD,END_PERIOD)
		}else{
			notice_title.innerHTML = "알림 "
			notice_context.innerHTML = "검색어 및 검색 조건을 입력해주세요";
			$("#notice").modal();
			return
		}
	}else{
		Search_Submit(MID_Value,SEARCH_KEYWORD,MSFRTN_TY_CD_Value,CTPRVN_Value,SGG_Value,EMD_Value,START_PERIOD,END_PERIOD)
		//Search_Submit(SEARCH_KEYWORD,MSFRTN_TY_CD_Value,CTPRVN_Value,SGG_Value,EMD_Value,START_PERIOD,END_PERIOD)
	}
}

/*재난 정보 검색 */
function Search_Submit(MID_Value,SEARCH_KEYWORD,MSFRTN_TY_CD_Value,CTPRVN_Value,SGG_Value,EMD_Value,START_PERIOD,END_PERIOD){
	loading.style.display="block"
	$('#SEARCH_TBL').empty();
	//console.log("search_Submit")
	if(MID_Value =="default"){
		MID_Value =""
	}
	if(MSFRTN_TY_CD_Value=="ALL" || MSFRTN_TY_CD_Value=="default"){
		MSFRTN_TY_CD_Value=""
	}
	if(CTPRVN_Value=="광역시도"){
		CTPRVN_Value=""
		SGG_Value=""
		EMD_Value=""
	}
	if(SGG_Value=="시군구"){
		SGG_Value=""
		EMD_Value=""
	}
	if(EMD_Value=="읍면동"){
		EMD_Value=""
	}
	if(START_PERIOD!="" && END_PERIOD==""){
		var today = new Date()
		var edit_today = today.getFullYear()+'-'+(today.getMonth()+1)+'-'+("0" + today.getDate()).slice(-2);
		//console.log("today", today,edit_today)
		END_PERIOD = edit_today
	}
	if(START_PERIOD=="" && END_PERIOD!=""){
		START_PERIOD = "1990-01-01"
	}
	console.log("search_Submit",MID_Value,SEARCH_KEYWORD,MSFRTN_TY_CD_Value,CTPRVN_Value,SGG_Value,EMD_Value,START_PERIOD,END_PERIOD)
	var param = "mid="+ MID_Value+"&SEARCH_KEYWORD="+SEARCH_KEYWORD+"&MSFRTN_TY_CD="+MSFRTN_TY_CD_Value+"&CTPRVN_NM="+CTPRVN_Value
				+"&SGG_NM="+SGG_Value+"&EMD_NM="+EMD_Value+"&START_PERIOD="+START_PERIOD+"&END_PERIOD="+END_PERIOD
	console.log("param" , param)
	var save_param ={
		"mid": MID_Value,
		"keyword":SEARCH_KEYWORD,
		"MSFRTN_TY_CD":MSFRTN_TY_CD_Value,
		"CTPRVN_NM":CTPRVN_Value,
		"SGG_NM":SGG_Value,
		"EMD_NM":EMD_Value,
		"start_date":START_PERIOD,
		"end_date":END_PERIOD,
		"order_field":"NEWS_NES_DT",
        "order_type":"desc"
	}
	console.log(save_param)
	var within = $('#within').is(':checked');
	if(within == true){
		var check = myStorage.getItem("Search")
		//console.log("check",check)
		if(check==null){
			//notice_title.innerHTML = "알림 "
			//notice_context.innerHTML = "이전 검색 결과가 존재하지 않습니다.";
			//$("#notice").modal();
			return
		}else{
			Search_Within(save_param)
		}
		
	}else{
		myStorage.setItem("Search", JSON.stringify(save_param))
		//console.log("그냥 검색")
		
 		$.ajax({
			type : "PUT",
		    url: "/search.do",
		    data:param,
		    success:function(data){
		    	var result = JSON.parse(data);
		    	//console.log("재난정보검색", result)
		    	SEARCH_TBL_Edit(result)
		    	SEARCH_TBL_ROW_CLICK(result)
		    	SEARCH_PIE(result)
		    	SEARCH_WORDCLOUD(result)
		    	SEARCH_TIMELINE(param);
		    	Serch_Condition_init()
		    	loading.style.display="none"
		    },
		    error:function(data){
		    	alert("실패", data)
		    }
		})
		
	}
}

/*결과내 검색*/
function Search_Within(param){
	$('#SEARCH_TBL').empty();
	var option = myStorage.getItem("Search")
	//console.log("결과 내 검색 이전",JSON.parse(option))
	//console.log("결과 내 검색 이후",param)
	var arr_check =Array.isArray(JSON.parse(option))
	var option_arr = []
	if(arr_check==true){
		for(var i in JSON.parse(option)){
			option_arr.push(JSON.parse(option)[i])
		}
		option_arr.push(param)
	}else{
		option_arr.push(JSON.parse(option))
		option_arr.push(param)
	}
	
	var Withinparam = "options="+JSON.stringify(option_arr)
	//console.log("Withinparam",Withinparam)
	myStorage.setItem("Search", JSON.stringify(option_arr))
	$.ajax({
			type : "POST",
		    url: "/search_within.do",
		    data:Withinparam,
		    success:function(data){
		    	//console.log("성공")
		    	var result = JSON.parse(data);
		    	//console.log("재난정보 검색결과 내 검색 ", result)
		    	SEARCH_TBL_Edit(result)
		    	SEARCH_TBL_ROW_CLICK(result)
		    	SEARCH_PIE(result)
		    	SEARCH_WORDCLOUD(result)
		    	SEARCH_TIMELINE_WITHIN(Withinparam);
		    	loading_img.style.display="none"
		    	Search_Condition_init()
		    },
		    error:function(data){
		    	//alert("실패", data)
		    }
		})
	
}
/*결과 내 검색 버튼 해제시 검색 조건 삭제 , 결과내 버튼 선택시 검색 조건 입력 초기화  */
function handleClick(){
	var within = $('#within').is(':checked');
	if(within== false){
		myStorage.removeItem("Search")
		//var test = myStorage.getItem("Search")
		//console.log("검색 조건 삭제", test)
	}
	if(within ==true){
		var test = myStorage.getItem("Search")
		//console.log("검색 조건 검사",test)
		if(test ==null){
			notice_title.innerHTML = "알림 "
				notice_context.innerHTML = "1차 검색이 수행되지 않았습니다. ";
				$("#notice").modal();
				return
		}else{
			Search_Condition_init()
		}
		
		
	}
}

/*재난 정보 검색 조건 초기화*/
function Serch_Condition_init(){
	document.getElementById("SEARCH_KEYWORD").value =""
	document.getElementById("start_period").value=""
	document.getElementById("end_period").value=""
	$("select[id='MID'] option").remove();
	$("select[id='MSFRTN_TY_CD'] option").remove();
	$("select[id='CTPRVN'] option").remove();
	$("select[id='SGG'] option").remove();
	$("select[id='EMD'] option").remove();
	$("#MID").append("<option value='default' disabled selected hidden> </option>")
	$("#MSFRTN_TY_CD").append("<option value='default' disabled selected hidden> </option>")
	$("#CTPRVN").append("<option value='default' selected>광역시도</option>")
	$("#SGG").append("<option value='default' selected >시군구</option>")
	$("#EMD").append("<option value='default' selected >읍면동</option>")
	MSFRTN_TY_CD()
	CTPRVN()
	MID()
} 

/*검색 결과 재난정보 목록 제목 클릭시 실행*/
function SEARCH_TBL_ROW_CLICK(result){
	if(result.data.length!=0){
	    var table = $('#SEARCH_TBL').DataTable();
	    $('#SEARCH_TBL tbody').on('click', 'tr', function () {
	        var data = table.row( this ).data();
	        //console.log("data row", data)
	        SEARCH_TBL_DETAIL(data[7])
	        ////alert( 'You clicked on '+data[6]+'\'s row' );
	    } );
	}
}

/*검색 결과 재난정보 목록 */
function SEARCH_TBL_Edit(result){
	//console.log("Search",result)
	var DataSet =[]		
	for(var i in result.data){
		switch (result.data[i].MSFRTN_TY_CD) {
        case "DTC001":  result.data[i].MSFRTN_TY_CD ="풍수해";
                 break;
        case "DTC002":  result.data[i].MSFRTN_TY_CD ="지진";
                 break;
        case "DTC003":  result.data[i].MSFRTN_TY_CD ="산불";
                 break;
        case "DTC004":  result.data[i].MSFRTN_TY_CD ="산사태";
                 break;
        case "DTC005":  result.data[i].MSFRTN_TY_CD ="대형화산폭발";
                 break;
        case "DTC006":  result.data[i].MSFRTN_TY_CD ="조수";
				break;
		case "DTC007":  result.data[i].MSFRTN_TY_CD ="유해화학물질유출사고";
				break;
		case "DTC008":  result.data[i].MSFRTN_TY_CD ="대규모해양오염";
				break;
		case "DTC009":  result.data[i].MSFRTN_TY_CD ="다중밀집시설대형화재";
				break;
		case "DTC010":  result.data[i].MSFRTN_TY_CD ="댐붕괴";
				break;
		case "DTC011":  result.data[i].MSFRTN_TY_CD ="고속철도대형사고";
				break;
		case "DTC012":  result.data[i].MSFRTN_TY_CD ="다중밀집건출물붕괴대형사고";
				break;
		case "DTC013":  result.data[i].MSFRTN_TY_CD ="적조";
				break;
    	}
		
		var replace = result.data[i].NEWS_NES_DT.replace("T"," ")
		var split = replace.split(".")
		result.data[i].NEWS_NES_DT = split[0]
		var index = parseInt(i)+1
		var request_name = "20"+String(result.data[i].MSFRTN_INFO_COLCT_REQUST_ID).slice(0,6)+" "+result.data[i].MSFRTN_AREA_SRCHWRD+" "+result.data[i].MSFRTN_TY_SRCHWRD
		DataSet[i] = [index,result.data[i].MSFRTN_INFO_COLCT_REQUST_ID,request_name,result.data[i].MSFRTN_TY_CD,result.data[i].POI,result.data[i].NEWS_NES_DT,result.data[i].NEWS_SJ_NM,result.data[i].NEWS_ID]
		//DataSet[i] = [index,result.data[i].MSFRTN_INFO_COLCT_REQUST_ID,result.data[i].MID,result.data[i].MSFRTN_TY_CD,result.data[i].POI,result.data[i].NEWS_NES_DT,result.data[i].NEWS_SJ_NM,result.data[i].NEWS_ID]
	  }
	  $('#SEARCH_TBL').DataTable({
	    data: DataSet,
	    columns: [
	      { title: "순번" },
		  { title: "재난 ID" },
	      { title: "재난 요청명" },
	      { title: "재난 유형" },
	      { title: "재난 지역" },
	      { title: "보도 일시" },
	      { title: "기사 제목",className: "title" },
	    ],
	    searching:false,
	    ordering:true,
	    info:false,
	    lengthChange:false,
	    destroy: true,
	    columnDefs: [
	    	{ targets: 0, width: "5%" },
			{ targets: 3, width: "7%" },
			{ targets: 6, width: "50%" },
			]
	  });
}

/*검색 결과 재난정보 목록 상세보기 */
function SEARCH_TBL_DETAIL(id){
	//console.log("search id", id)
	var param = "id="+ id
	$.ajax({
		type : "GET",
	    url: "/colct_tbl_detail.do",
	    data:param,
	    success:function(data){
	    	var result = JSON.parse(data);
	    	//console.log("detail",result)
	    	if(result.msg_no =="-100"){
	    		$("#Search_Modal").modal();
	    	}else{
		    	SEARCH_TBL_SJ_NM.innerHTML = result.article["제목"]
		    	SEARCH_TBL_NES_DT.innerHTML = result.article["보도 일시"]
		    	var Edit_article = result.article["기사 원문"].replace(/\n/g, '<br>');
		    	SEARCH_TBL_ORIGINL_FILE_COURS_NM.innerHTML = Edit_article
		    	SEARCH_TBL_DETAIL_IMG_LIST(result.image_full_path)
		    	SEARCH_TBL_DETAIL_META(result)
		    	$("#Search_Modal").modal();
	    	}
	    },
	    error:function(data){
	    	//alert("mntrng_news_detail 실패", data)
	    }
	})
}

/*검색 결과 재난정보 목록 기사 상세보기 이미지 리스트   */
function SEARCH_TBL_DETAIL_IMG_LIST(IMAGE_FULL_PATH){
	if(IMAGE_FULL_PATH.length!=0){
		$("#search_img_list").empty();
		search_img = IMAGE_FULL_PATH
		//var img = ["C:\test\img\0.jpg","C:\test\img\1.jpg","C:\test\img\2.jpg","C:\test\img\3.jpg","C:\test\img\4.jpg"]
		for(var i=0; i<search_img.length; i++){
			//$("#img_list").append("<button class='imglist' onclick='MNTRNG_NEWS_DETAIL_IMG_VIEWER('Image-"+i+"')>Image-"+(i+1)+"</button>");
			$("#search_img_list").append("<button id='search_img_list_"+i+"'class= 'img_list' onclick='SEARCH_TBL_DETAIL_IMG_VIEWER("+i+")'>Image-"+(i+1)+"</button>");
		}
		SEARCH_TBL_DETAIL_IMG_VIEWER(0)
	}else{
		$("#search_img_list").empty();
		$("#search_img_viewer").empty();
	}
}

/*검색 결과 재난정보 목록 기사 상세보기 이미지 뷰어 */
function SEARCH_TBL_DETAIL_IMG_VIEWER(id){
	for(var i=0; i<search_img.length; i++){
		var reset_id = "search_img_list_"+i
		document.getElementById(reset_id).className="img_list"
	}
	var img_id = "search_img_list_"+id
	/*배포시 수정 ! img_path (/home/ugismn 경로) 주석 해제 */
	//var img_path = search_img[id].replace("D:/UGIS_MN","/colct")
	var img_path = search_img[id].replace("/home/ugismn","/colct")
	$("#search_img_viewer").empty();
	document.getElementById(img_id).className= 'img_list_click'
	$("#search_img_viewer").append("<img src = '"+img_path+"' height=400px width=600px alt='Nothing'>");
}

/*검색 결과 재난정보 목록 기사 상세보기 메타 추출*/
function SEARCH_TBL_DETAIL_META(result){
	$("#meta-wrap").empty()
	$("#meta-wrap2").empty()
	var editmid ="20"+String(result.MSFRTN_INFO_COLCT_REQUST_ID).slice(0,6)+" "+result.MSFRTN_AREA_SRCHWRD+" "+result.MSFRTN_TY_SRCHWRD
	//console.log("mid", editmid)
	var meta =[editmid]
	var Edit_MSFRTN_DMGE_META=result.MSFRTN_DMGE_META.split(",")
	var Edit_MSFRTN_TY_META = result.MSFRTN_TY_META.split(",")
	var Edit_MSFRTN_OCCRRNC_AREA_META = result.MSFRTN_OCCRRNC_AREA_META.split(",")

	var final_meta = meta.concat(Edit_MSFRTN_DMGE_META, Edit_MSFRTN_TY_META,Edit_MSFRTN_OCCRRNC_AREA_META)
	final_meta  = final_meta.filter(function(item) {
  		return item !== null && item !== undefined && item !== '';
	});
	
	var Edit_NEWS_NES_DT = "보도일자 : " + result.NEWS_NES_DT.replace("T"," ")
	var Edit_NEWS_COLCT_DT ="수집일자 : " + result.NEWS_COLCT_DT.replace("T"," ")
	
	var fix_meta = [result.NEWS_OXPR_NM,Edit_NEWS_NES_DT,Edit_NEWS_COLCT_DT]
	for(var i in final_meta){
		$("#meta-wrap").append("<metadata>"+final_meta[i]+"</metadata>");
	}
	for(var j in fix_meta){
		$("#meta-wrap2").append("<metadata>"+fix_meta[j]+"</metadata>");
	}
}

/*재난 정보 조회 검색 결과 placeholder*/
function Placeholder(){
	var Placeholder_pie =[{id :"", value:1000}]
	
	Make_Pie("chartdiv2",Placeholder_pie,"ph")
	Make_Pie("chartdiv3", Placeholder_pie,"ph")
	Make_Pie("chartdiv4",Placeholder_pie,"ph")
	Make_Pie("chartdiv5",Placeholder_pie,"ph")
	
	var Placeholder_timeline ={total: 0, data: []}
	Make_Timeline(Placeholder_timeline)
	
	//var Placeholder_wordcloud = {wordcloud:[{word: 'No Data', count: 50}]}
	//SEARCH_WORDCLOUD(Placeholder_wordcloud)
	
	var Placeholder_searchlist={data:[]}
	SEARCH_TBL_Edit(Placeholder_searchlist)
	
}

/*재난 유형별 ,재난 발생 지역, 재난 발생 기간별 , 재난 피해별 통계*/
function SEARCH_PIE(result){
	console.log(result)
	
	var Ty_DataSet = result.ty_pi.data
	var Area_DataSet = result.area_pi.data
	var Period_DataSet = result.time_pi.data
	var Dmge_DataSet = result.dmge_pi.data
	
	console.log(result.dmge_pi.length)
	var empty_pie =[{id :"", value:1000}]
	if(result.data.length==0){
		if(Ty_DataSet==undefined){
			Make_Pie("chartdiv2",empty_pie,"ph")
		}
		if(Area_DataSet==undefined){
			Make_Pie("chartdiv3",empty_pie,"ph")
		}
		if(Period_DataSet==undefined){
			Make_Pie("chartdiv4",empty_pie,"ph")
		}
		if(Dmge_DataSet==undefined){
			Make_Pie("chartdiv5",empty_pie,"ph")
		}
		return
	}
	
	if(result.ty_pi.total==0 || result.ty_pi.length==0){
		Make_Pie("chartdiv2",empty_pie,"ph")
	}else{
		for(var i in Ty_DataSet){
			switch (Ty_DataSet[i].id) {
	        case "DTC001":  Ty_DataSet[i].id ="풍수해";
	                 break;
	        case "DTC002":  Ty_DataSet[i].id ="지진";
	                 break;
	        case "DTC003":  Ty_DataSet[i].id ="산불";
	                 break;
	        case "DTC004":  Ty_DataSet[i].id ="산사태";
	                 break;
	        case "DTC005":  Ty_DataSet[i].id ="대형화산폭발";
	                 break;
	        case "DTC006":  Ty_DataSet[i].id  ="조수";
					break;
			case "DTC007":  Ty_DataSet[i].id ="유해화학물질유출사고";
					break;
			case "DTC008":  Ty_DataSet[i].id ="대규모해양오염";
					break;
			case "DTC009":  Ty_DataSet[i].id ="다중밀집시설대형화재";
					break;
			case "DTC010":  Ty_DataSet[i].id ="댐붕괴";
					break;
			case "DTC011":  Ty_DataSet[i].id ="고속철도대형사고";
					break;
			case "DTC012":  Ty_DataSet[i].id ="다중밀집건출물붕괴대형사고";
					break;
			case "DTC013":  Ty_DataSet[i].id ="적조";
					break;
	    	}
		}
		Edit_Pie_DataSet("chartdiv2",Ty_DataSet)
	}
	
	if(result.area_pi.total==0 || result.area_pi.length==0){
		Make_Pie("chartdiv3",empty_pie,"ph")
	}else{
		Edit_Pie_DataSet("chartdiv3", Area_DataSet)
	}
	
	if(result.time_pi.total==0 || result.time_pi.length==0){
		Make_Pie("chartdiv4",empty_pie,"ph")
	}else{
		Edit_Pie_DataSet("chartdiv4",Period_DataSet)
	}
	
	if(result.dmge_pi.total==0 || result.dmge_pi.length==0){
		Make_Pie("chartdiv5",empty_pie,"ph")
	}else{
		Edit_Pie_DataSet("chartdiv5",Dmge_DataSet)
	}
	
}

/*통계 데이터 수정 */
function Edit_Pie_DataSet(div_id,DataSet){
	var Edit_DataSet=[]
	console.log("DataSet", DataSet)
	if(DataSet.length>5){
		DataSet.sort(function(a, b)  {
			  return  b.value - a.value;
			});
		Edit_DataSet = DataSet.slice(0,5)
		var ETC = DataSet.slice(5)
		//console.log("Edit_DataSet",Edit_DataSet,"ETC",ETC)
		var total =0
		for(var i in ETC){
			total+=parseInt(ETC[i].value)
		}
		Edit_DataSet.push({id: '기타', value: total})
		//console.log("total", total,"Edit_DataSet",Edit_DataSet)
	}else{
		Edit_DataSet = DataSet
	}
	Make_Pie(div_id,Edit_DataSet)
}
	
/*통계(파이차트) 생성*/
function Make_Pie(div_id, DataSet,ty){
	// am4core.unuseTheme(am4themes_material);
	// am4core.unuseTheme(am4themes_kelly);
	// am4core.useTheme(am4themes_animated);
	
	var chart = am4core.create(div_id, am4charts.PieChart);
	chart.data=DataSet
	var pieSeries = chart.series.push(new am4charts.PieSeries());
	pieSeries.dataFields.value = "value";
	pieSeries.dataFields.category = "id";
	chart.radius = am4core.percent(55);
	if(ty=="ph"){
		pieSeries.labels.template.disabled = true;
		pieSeries.slices.template.tooltipText = "100%";
	}else{
		pieSeries.labels.template.fontSize =14
		pieSeries.labels.template.text = "{category}"
	}
}

/*워드클라우드 */
function SEARCH_WORDCLOUD(result){
	//console.log("wordcloud")

	//am4core.useTheme(am4themes_material);
	//am4core.useTheme(am4themes_kelly);

	am4core.useTheme(am4themes_animated);
	
	var chart = am4core.create("wordclouddiv", am4plugins_wordCloud.WordCloud);
	var series = chart.series.push(new am4plugins_wordCloud.WordCloudSeries());

	series.data = result.wordcloud
    series.dataFields.word = "word";
    series.dataFields.value = "count";
    series.colors = new am4core.ColorSet();
    series.colors.step = 1;
    series.colors.passOptions = {};
	series.maxFontSize =am4core.percent(80);
	series.minFontSize= 5;
	series.fontWeight = "700";
	series.randomness = 0.2

	// series.angles =0;
	// series.fontFamily ="Dotum";
	
}
/*재난 정보 검색 결과 추이 그래프 */
function SEARCH_TIMELINE(param){
  	$.ajax({
		type : "PUT",
	    url: "/search_timeline.do",
	    data:param,
	    success:function(data){
	    	var result = JSON.parse(data);
	    	 Make_Timeline(result)
	    },
	    error:function(data){
	    	//alert("실패", data)
	    }
	})
}

/*재난 정보 검색 결과 추이 그래프-결과내 검색 */
function SEARCH_TIMELINE_WITHIN(param){
  	$.ajax({
		type : "POST",
	    url: "/search_timeline_within.do",
	    data:param,
	    success:function(data){
	    	var result = JSON.parse(data);
	    	 Make_Timeline(result)
	    },
	    error:function(data){
	    	//alert("실패", data)
	    }
	})
}
/*timeline그래프 생성*/
function Make_Timeline(result){
	//console.log("Timeline" , result)

	// am4core.unuseTheme(am4themes_material);
	// am4core.unuseTheme(am4themes_kelly);
	// am4core.useTheme(am4themes_animated);
	var chart = am4core.create("chartdiv", am4charts.XYChart);
    chart.data = result.data

    // Create axes
    let dateAxis = chart.xAxes.push(new am4charts.DateAxis());
    dateAxis.renderer.minGridDistance = 60;

    dateAxis.dateFormats.setKey("month", "MM");
    dateAxis.dateFormats.setKey("week", "MM.dd");
    dateAxis.dateFormats.setKey("day", "MM.dd");
    
    dateAxis.periodChangeDateFormats.setKey("month", "[bold]yyyy[/]\nMM");
    dateAxis.periodChangeDateFormats.setKey("week", "[bold]MM.dd");
    dateAxis.periodChangeDateFormats.setKey("day", "[bold]MM.dd");
    dateAxis.periodChangeDateFormats.setKey("hour", "[bold]MM.dd[/]\nHH:mm");

    let valueAxis = chart.yAxes.push(new am4charts.ValueAxis());

    // Create series
    let series = chart.series.push(new am4charts.LineSeries());
    series.dataFields.valueY = "count";
    series.dataFields.dateX = "date";
    series.tooltipText = "{count}"
    series.tooltip.pointerOrientation = "vertical";

    chart.cursor = new am4charts.XYCursor();
    chart.cursor.snapToSeries = series;
    chart.cursor.xAxis = dateAxis;
}

/*공간 정보 검색 조건 초기화*/
function Space_Search_Condition_init(){
	document.getElementById("SPACE_KEYWORD").value =""
	$("select[id='SPACE_MID'] option").remove();
	$("select[id='SPACE_MSFRTN_TY_CD'] option").remove();
	$("select[id='SPACE_CTPRVN'] option").remove();
	$("select[id='SPACE_SGG'] option").remove();
	$("select[id='SPACE_EMD'] option").remove();
	$("#SPACE_MID").append("<option value='default' disabled selected hidden ></option>")
	$("#SPACE_MSFRTN_TY_CD").append("<option value='default' disabled selected hidden ></option>")
	$("#SPACE_CTPRVN").append("<option value='default' selected>광역시도</option>")
	$("#SPACE_SGG").append("<option value='default' selected >시군구</option>")
	$("#SPACE_EMD").append("<option value='default' selected >읍면동</option>")
	MSFRTN_TY_CD('SPACE')
	CTPRVN('SPACE')
	MID('SPACE')
	
}

/*공간정보 검색 조건 설정  */
function Space_Search(){
	var SPACE_MID = document.getElementById("SPACE_MID");
	var SPACE_MID_Value = SPACE_MID.options[SPACE_MID.selectedIndex].value;
	var SPACE_KEYWORD = document.getElementById("SPACE_KEYWORD").value;
	var SPACE_MSFRTN_TY_CD = document.getElementById("SPACE_MSFRTN_TY_CD");
	var SPACE_MSFRTN_TY_CD_Value = SPACE_MSFRTN_TY_CD.options[SPACE_MSFRTN_TY_CD.selectedIndex].value;
	var SPACE_CTPRVN = document.getElementById("SPACE_CTPRVN");
	var SPACE_CTPRVN_Value = SPACE_CTPRVN.options[SPACE_CTPRVN.selectedIndex].text;
	var SPACE_SGG = document.getElementById("SPACE_SGG");
	var SPACE_SGG_Value = SPACE_SGG.options[SPACE_SGG.selectedIndex].text;
	var SPACE_EMD = document.getElementById("SPACE_EMD");
	var SPACE_EMD_Value = SPACE_EMD.options[SPACE_EMD.selectedIndex].text;
	if(SPACE_KEYWORD==""){
		if(SPACE_MID_Value!= "default" || SPACE_MSFRTN_TY_CD_Value!="default"||SPACE_CTPRVN_Value!="광역시도"){
		//if(SPACE_MSFRTN_TY_CD_Value!="default"||SPACE_CTPRVN_Value!="광역시도"){
			//console.log("!!!!!")
			Space_Search_Submit(SPACE_MID_Value,SPACE_KEYWORD,SPACE_MSFRTN_TY_CD_Value,SPACE_CTPRVN_Value,SPACE_SGG_Value,SPACE_EMD_Value)
		}else{
			notice_title.innerHTML = "알림 "
			notice_context.innerHTML = "검색어 및 검색 조건을 입력해주세요";
			$("#notice").modal();
			return
		}
	}else{
		Space_Search_Submit(SPACE_MID_Value,SPACE_KEYWORD,SPACE_MSFRTN_TY_CD_Value,SPACE_CTPRVN_Value,SPACE_SGG_Value,SPACE_EMD_Value)
	}
}
/*공간 정보 검색 */
function Space_Search_Submit(SPACE_MID_Value,SPACE_KEYWORD,SPACE_MSFRTN_TY_CD_Value,SPACE_CTPRVN_Value,SPACE_SGG_Value,SPACE_EMD_Value){
	//console.log("공간정보검색",SPACE_KEYWORD,SPACE_MSFRTN_TY_CD_Value,SPACE_CTPRVN_Value,SPACE_SGG_Value,SPACE_EMD_Value);
	if(SPACE_MID_Value=="default"){
		SPACE_MID_Value =""
	}
	if(SPACE_MSFRTN_TY_CD_Value=="ALL"||SPACE_MSFRTN_TY_CD_Value=="default"){
		SPACE_MSFRTN_TY_CD_Value=""
	}
	if(SPACE_CTPRVN_Value=="광역시도"){
		SPACE_CTPRVN_Value=""
		SPACE_SGG_Value=""
		SPACE_EMD_Value=""
	}
	if(SPACE_SGG_Value=="시군구"){
		SPACE_SGG_Value=""
		SPACE_EMD_Value=""
	}
	if(SPACE_EMD_Value=="읍면동"){
		SPACE_EMD_Value=""
	}
	// var param ="SEARCH_KEYWORD="+SPACE_KEYWORD+"&MSFRTN_TY_CD="+SPACE_MSFRTN_TY_CD_Value+"&CTPRVN_NM="+SPACE_CTPRVN_Value
	// +"&SGG_NM="+SPACE_SGG_Value+"&EMD_NM="+SPACE_EMD_Value
	var param ="mid="+SPACE_MID_Value+"&SEARCH_KEYWORD="+SPACE_KEYWORD+"&MSFRTN_TY_CD="+SPACE_MSFRTN_TY_CD_Value+"&CTPRVN_NM="+SPACE_CTPRVN_Value
	+"&SGG_NM="+SPACE_SGG_Value+"&EMD_NM="+SPACE_EMD_Value
	console.log("space search", param)
	
  	$.ajax({
		type : "PUT",
	    url: "/space_search.do",
	    data:param,
	    success:function(data){
	    	var result = JSON.parse(data);
	    	console.log("공간검색", result)
	    	Space_Search_Result_1(result)
	    	Space_Search_Condition_init()
	    	
	    },
	    error:function(data){
	    	alert("실패", data)
	    }
	})
	
}
/*공간정보 검색 결과- 재난 정보 목록*/
function Space_Search_Result_1(result){
	$( '#table_detail li').remove();
	$('#table_detail2').css("display", "none")
	//console.log("space search result", result)
	
	var data = result.data
	var editresult = {
		풍수해:[],
		지진:[],
		산불:[],
		산사태:[],
		대형화산폭발:[],
		조수:[],
		유해화학물질유출사고:[],
		대규모해양오염:[],
		다중밀집시설대형화재:[],
		댐붕괴:[],
		고속철도대형사고:[],
		다중밀집건축물붕괴대형사고:[],
		적조:[]
	}
	for(var a in editresult){
		document.getElementById(a).innerHTML=""
	}
	for(var i=0; i<data.length; i++){
		if(data[i].type =='DTC001'){
			editresult['풍수해'].push(data[i])
		}
		if(data[i].type =='DTC002'){
			editresult['지진'].push(data[i])
		}
		if(data[i].type =='DTC003'){
			editresult['산불'].push(data[i])
		}
		if(data[i].type =='DTC004'){
			editresult['산사태'].push(data[i])
		}
		if(data[i].type =='DTC005'){
			editresult['대형화산폭발'].push(data[i])
		}
		if(data[i].type =='DTC006'){
			editresult['조수'].push(data[i])
		}
		if(data[i].type =='DTC007'){
			editresult['유해화학물질유출사고'].push(data[i])
		}
		if(data[i].type =='DTC008'){
			editresult['대규모해양오염'].push(data[i])
		}
		if(data[i].type =='DTC009'){
			editresult['다중밀집시설대형화재'].push(data[i])
		}
		if(data[i].type =='DTC010'){
			editresult['댐붕괴'].push(data[i])
		}
		if(data[i].type =='DTC011'){
			editresult['고속철도대형사고'].push(data[i])
		}
		if(data[i].type =='DTC012'){
			editresult['다중밀집건축물붕괴대형사고'].push(data[i])
		}
		if(data[i].type =='DTC013'){
			editresult['적조'].push(data[i])
		}
	}
	//console.log("editresult", editresult)
	
	var editresult2={}
	for(var l in editresult){
		if(editresult[l].length != 0){
			editresult2[l] = editresult[l]
		}
	}
	for(var i in editresult2){
		var title = document.getElementById(i)
		var length = editresult2[i].length
		title.textContent="-"+i+"("+length+"건)"
		for(var j in editresult2[i]){
			var contextid =document.getElementById(editresult2[i][j].type+"_List")
			var request_name = editresult2[i][j].MID.split(" ")
			var request_name2 ="20"+String(editresult2[i][j].rid).slice(0,6)+" "+ request_name[1]+" "+request_name[2]
			$(contextid).append('<li onclick="test('+editresult2[i][j].rid+','+editresult2[i][j].x+','+editresult[i][j].y+')">['+editresult2[i][j].rid+"]&nbsp"+request_name2+" ("+editresult2[i][j].value+'건)</li>')
			//$(contextid).append('<li onclick="test('+editresult2[i][j].rid+','+editresult2[i][j].x+','+editresult2[i][j].y+')">['+editresult2[i][j].rid+"]&nbsp"+editresult2[i][j].MID+" ("+editresult2[i][j].value+'건)</li>')
		}
	}


}
/*재난 정보 목록에서 재난 유형 선택시 해당 유형 상세 정보 출력 */
function show_hide_row(row){
 	$("#"+row).toggle();
}

/*재난 유형 상세 정보 클릭시 실행 */
function test(rid,x,y){
	console.log("x,y좌표","x=",x,"y=",y)
	Space_Search_Result_1_Click(rid)
	movemap(x,y,10)
	currentSelectedPostion = [x, y];
	resetLayers();
}

/*재난 유형 상세 정보 클릭시 발생 -공간 정보 목록*/
function Space_Search_Result_1_Click(rid){
	loading2.style.display="block"
	$('#table_detail2').css("display", "none")
	//console.log("click rid", rid)
	
	var param = "rid="+rid
	$.ajax({
		type : "POST",
	    url: "/space_search2.do",
	    data:param,
	    success:function(data){
	    	var result = JSON.parse(data);
	    	search2Result = result; 
	    	console.log("공간검색-공간 정보 목록", result)
	    	if(result=="fail"){
	    		Empty_Space_Search_Result_2()
	    	}else{
	    		Space_Search_Result_2(result)
	    	}
	    	loading2.style.display="none"
	    },
	    error:function(data){
	    	//alert("실패", data)
	    	
	    }
	})
	
}

/*검색 결과 없는 공간정보 목록 출력*/
function Empty_Space_Search_Result_2(){
	$('#table_detail2').css("display", "")
	//document.getElementById("연속수치지도").innerText="-연속수치지도"
	//document.getElementById("항공사진").innerText="-항공사진(0건)"
	document.getElementById("DEM").innerText="-DEM(0건)"
	document.getElementById("정사영상").innerText="-정사영상(0건)"
	document.getElementById("위성영상").innerText="-위성영상(0건)"
	//document.getElementById("수치지도").innerText="-수치지도(0건)"
}

/*공간 정보 목록 출력*/
function Space_Search_Result_2(result){
	$('#table_detail2').css("display", "")
	$('#table_detail2 li').remove();
	$('#table_detail2 p').remove();
	/*
	result["con"]={data:[
		{
			name:"도로",
			oid:"1"
		
		},
		{
			name:"하천",
			oid:"2"
		
		},
		{
			name:"농경지",
			oid:"3"
		
		},
		{
			name:"건물",
			oid:"4"
		
		},
	]
	}*/
	
	result["con"]={data:con_list}
	
	console.log("con 추가", result)
	for(var i in result){
		var target =""
		switch (i) {
        case "air":  target ="항공사진"; 
                 break;
        case "dem":  target ="DEM"; 
                 break;
        case "ort":  target ="정사영상";
                 break;
        case "sat":  target ="위성영상"; 
                 break;
        case "map":  target ="수치지도"; 
                 break;
        case "con":  target ="연속수치지도"; 
        	break;       
    	}
		
		//console.log("result",result)
		
		if(i!="con"){
			var title = document.getElementById(target)
			var length = result[i].total
			title.innerHTML="-"+target+"("+length+"건)"
			for(var j in result[i].data){
				var contextid = document.getElementById(i+"_List")
				//console.log("contextid",contextid)
				 $(contextid).append('<li class="group_year" id="'+i+result[i].data[j].year+'">'+result[i].data[j].year+"("+result[i].data[j].count+'건)<ul class="hidden_row" id="'
							+i+result[i].data[j].year+'_ul")></ul></li>')
				for(var k in result[i].data[j].data){
					var contextid2 = document.getElementById(i+result[i].data[j].year+"_ul")
					//console.log("contextid2",contextid2)
					$(contextid2).append('<li class="space_context_li" id="'+i+"_"+result[i].data[j].data[k].oid+"_"+result[i].data[j].data[k].file+"_"+result[i].data[j].data[k].name+'"><div class="ui-layer-title"><label><input type="checkbox" class="ui-result-check js-result-check" value="'+i+'_'+j+'_'+k+'"/>'+result[i].data[j].data[k].name+'</label></div><button class="btn btn-xs f-right js-preview-'+i+' js-preview" value="'+i+'_'+j+'_'+k+'"><i class="fas fa-search m-r-5"></i>미리보기</button></li>');
					//$(contextid2).append('<li class="space_context_li" onclick="Space_Search_Result_2_Click(event)"id="'+i+"_"+result[i].data[j].data[k].oid+"_"+result[i].data[j].data[k].file+"_"+result[i].data[j].data[k].name+'"><div class="ui-layer-title"><label><input type="checkbox" class="ui-result-check js-result-check" value="'+i+'_'+result[i].data[j].year+'"/>'+result[i].data[j].data[k].name+'</label></div><button class="btn btn-xs f-right"><i class="fas fa-search m-r-5"></i>미리보기</button></li>');
				}
			}
		}else{
			var title = document.getElementById(target)
			title.innerHTML="-"+target+"("+result[i]['data'].length+"건)";
			for(var j in result[i].data){
				//console.log("con",result[i].data[j])
				var contextid = document.getElementById(i+"_List")
				$(contextid).append('<p class="con_div"><label for="'+result[i].data[j].name+'"><input type=checkbox name="con_click" class="ui-result-check js-result-check" id="'+result[i].data[j].name+'" value="'+i+'_'+j+'">&nbsp'+result[i].data[j].name+'</label><button class="btn btn-xs f-right js-preview-con" value="'+i+'_'+j+'"><i class="fas fa-search m-r-5"></i>미리보기</button></p>')
// 				$(contextid).append('<p class="con_div"><label for="'+result[i].data[j].name+'"><input type=checkbox name="con_click" onclick="Con_Click_Event()" id="'+result[i].data[j].name+'" value="'+result[i].data[j].name+'">&nbsp'+result[i].data[j].name+'</label><button class="btn btn-xs f-right js-preview-digital"><i class="fas fa-search m-r-5"></i>미리보기</button></p>')
			}
		}

	}
	
}
/*연속 수치 지도 목록 체크시 실행*/
function Con_Click_Event(){
    if($('[name="con_click"]').is(":checked")){
        const query = 'input[name="con_click"]:checked';
        const selectedEls = document.querySelectorAll(query);
        // 선택된 목록에서 value 찾기
        let result = '';
        selectedEls.forEach((el) => {
          result += el.value + ' ';
        });
        console.log("연속 수치지도 체크! 값=",result)
        
    }else{
    	console.log("연속 수치지도 체크 해제 ")
    }
}

/*이벤트 발생*/
function getEventTarget(e) {
    e = e || window.event;
    return e.target || e.srcElement; 
}

/*공간 정보 목록 내용 클릭 시 실행*/
function Space_Search_Result_2_Click(event){
	event.stopPropagation();
	var target = getEventTarget(event);
	var edit_name = target.id.split("_")
	var tablename = ""
	if(edit_name[0]=="map"){
		var map_slice = edit_name[3].split("/")
		map_slice = map_slice[1].replace(")","")
	}

	switch (edit_name[0]) {
        case "air":  tablename ="NGII_AIR3.AIR_ORIENTMAP_AS"; 
                 break;
        case "dem":  tablename ="NGII_AIR3.DEM_ORIENTMAP_AS"; 
                 break;
        case "ort":  tablename ="NGII_AIR3.ORT_ORIENTMAP_AS";
                 break;
        case "sat":  tablename ="NGII_AIR3.SAT_ORIENTMAP_AS"; 
                 break;
        case "map":  tablename ="NGII_MAP.INDEX_00"+map_slice+"_V2"; 
                 break;    
    	}
	console.log("Table=",tablename,"Objectid=",edit_name[1],"file name=",edit_name[2])
}


/*공간 정보 목록 연도 클릭 시 실행 */
$(document).on('click','.group_year',function(){
	var row = $(this).attr('id')+"_ul"
	show_hide_row(row)
});

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
//1. 맵생성.
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
var baselyaer = baselayer();
//console.log("baselyaer",baselyaer)
map.addLayer(baselyaer);

// map.addLayer(new ol.layer.Tile({type: 'default', source: new ol.source.OSM()}));

const imageExtent = [957225.1387,1921135.1404,959008.6342,1922918.6358];
map.getView().fit(imageExtent);

/*지도 이동*/
function movemap(x,y,z){
	console.log("x,y,z",x,y,z)
	var pt  = transforms(x,y);
	map.getView().setZoom(z);
	map.getView().setCenter([pt.x,pt.y]);
	Continuous_Numerical_Map_list(pt.x,pt.y)
}

/*수치지형도 목록 조회*/
function Continuous_Numerical_Map_list(x,y){
	console.log("연속수치지도조회 x=",x,"y=",y)
	var param = "x="+x+"&y="+y
	//var param = "x=1072497.9047"+x+"&y="+y
	$.ajax({
		type : "POST",
	    url: "/space_search3.do",
	    data: param,
	    success:function(data){
	    	//var result = JSON.parse(data);
	    	console.log("공간검색-연속 수치 지도 목록", data);
	    	edit_con_list(data)
	    },
	    error:function(data){
	    	$('#table_detail2').css("display", "");
	    	document.getElementById("연속수치지도").innerText="-연속수치지도";
	    	//Empty_Space_Search_Result_2()
	    	alert("연속 수치 지도 목록 실패", data);
	    }
	});
}

/*연속 수치 지도 목록 변경*/
function edit_con_list(data){
	con_list = []
	var result =[]
	var edit_name=""
	for(var i in data){
		var name = data[i].split("_");
		//console.log("name", name)
		switch (name[1]) {
        case "B0010000":  edit_name ="건물 레이어"; 
                 break;
        case "A0010000":  edit_name ="도로 레이어"; 
                 break;
        case "E0010001":  edit_name ="하천 레이어";
                 break;
        case "D0010000":  edit_name ="경지 레이어"; 
                 break;
    	}
		con_list[i]={"name":edit_name, "code": data[i]};
	}
	Space_Search_Result_2(result)
	//console.log("con_list", con_list)
}
function transformByEPSG(x, y, epsg){
    return proj4(epsg, 'EPSG:5179', [x, y]);
}

function loadRasterLayerByParam(label, param) {
	var layer = layerObject[label];
	var imageExtent = null;
	if(layer) {
		layer.setVisible(true);
		imageExtent = layer.getSource().getImageExtent();
	} else {
// 		var param = {
// 			thumbnailFileCoursNm: param.path,
// 			ltopCrdntX: param.minx,
// 			ltopCrdntY: param.miny,
// 			rbtmCrdntX: param.maxx,
// 			rbtmCrdntY: param.maxy,
// 			mapPrjctnCn: param.epsg
// 		};

		var paramStr = {
			"json": JSON.stringify(param)
		};	  	
	  	
		var url = "mnsc003thumbnail.do?"+$.param(paramStr);
		console.log(url);	
	  	var epsg = 'EPSG:5179';
// 	  	switch (param.ORIGIN) {
// 		case '동부':
// 			epsg = 'EPSG:5187';
// 			break;
// 		case '중부':
// 			epsg = 'EPSG:5186';
// 			break;
// 		case '서부':
// 			epsg = 'EPSG:5185';
// 			break;
// 		default:
// 			break;
// 		}
	  	if(!epsg) {
	  		return;
	  	}
		var min = transformByEPSG(param.XMIN, param.YMIN, epsg);
		var max = transformByEPSG(param.XMAX, param.YMAX, epsg);
	   
		imageExtent = [min[0], min[1], max[0], max[1]];

		var source = new ol.source.ImageStatic({
			url: url,
			crossOrigin: '',
			projection: 'EPSG:5179',
			imageExtent: imageExtent
		}); 
		const imageLayer = new ol.layer.Image({
			source: source,
			zIndex: 1000
		});

		map.addLayer(imageLayer);
		
		layerObject[label] = imageLayer;
	}
	map.getView().fit(imageExtent);
// 	map.getView().setCenter([(imageExtent[0] + imageExtent[2]) / 2, (imageExtent[1] + imageExtent[3]) / 2]);
}
function loadVectorLayerByParam(label, param) {
	if(!currentSelectedPostion) {
		console.log('no position');
		return;
	}
	var layer = layerObject[label];
	var imageExtent = null;
	if(layer) {
		layer.setVisible(true);
		imageExtent = layer.get('extent');
	} else {
		imageExtent = CMSC003.GIS.createBufferROIMNSC003(currentSelectedPostion, 2000);
		const polygon = CMSC003.GIS.extentToPolygon(imageExtent);
		var point5179 = transformByEPSG(currentSelectedPostion[0], currentSelectedPostion[1], "EPSG:4326");
		const _imageSource = new ol.source.ImageWMS({
			ratio: 1,
			url: contextPath + "/geoserver/proxy.do?",
			params: {
				'VERSION': '1.1.1',
				//"CQL_FILTER": 'INTERSECTS(geom,POLYGON((956546.691 1964815.771, 956546.691 1952482.819, 969453.851 1952482.819, 969453.851 1964815.771, 956546.691  1964815.771)))',
				"CQL_FILTER": 'INTERSECTS(geom,' + polygon + ')',
// 				"CQL_FILTER": "DWithin(geom,POINT("+point5179[0]+" "+point5179[1]+"),2000,meters)",
// 				"LAYERS": "vector:N3A_B0010000",
				"LAYERS": param.code,
				"exceptions": 'application/vnd.ogc.se_inimage',
				"WIDTH": "256",
				"HEIGHT": "256"
			}
		})
		_imageSource.on('imageloaderror', function() {
			console.log('썸네일을 조회할 수 없습니다.');
		});

		const _imageLayer = new ol.layer.Image({
			source: _imageSource,
			zIndex: 1000
		});
		_imageLayer.set('extent', imageExtent);
		map.addLayer(_imageLayer);
		layerObject[label] = _imageLayer;
	}
	map.getView().fit(imageExtent);
// 	map.getView().setCenter([(imageExtent[0] + imageExtent[2]) / 2, (imageExtent[1] + imageExtent[3]) / 2]);
}
function showLayerByLabel(label) {
	var layer = layerObject[label];
	if(layer) {
		layer.setVisible(true);
		var source = layer.getSource();
		var extent = null;
		if(source instanceof ol.source.ImageWMS) {
			extent = source.get('extent');
		} else if(source instanceof ol.source.ImageStatic) {
			extent = source.getImageExtent();
		}
		map.getView().fit(extent);
	}
}
function hideLayerByLabel(label) {
	var layer = layerObject[label];
	if(layer) {
		layer.setVisible(false);
	}
}
function createParamsByCheckbox() {
	var params = {};
	var checks = $('#table_detail2 input[type="checkbox"]:checked.js-result-check');
	for(var i = 0; i < checks.length; i++) {
		var check = checks[i];
		var value = $(check).val();
		var split = value.split('_');
		if(!params.hasOwnProperty(split[0])) {
			params[split[0]] = [];
		}
		if(split.length === 3) {
			params[split[0]].push(getOtherLayerObjectByValue(split[0], split[1], split[2]));
		} else if(split.length === 2) {
			params[split[0]].push(getConLayerObjectByValue(split[0], split[1]));
		}
	}
	console.log(params);
	return params;
}
function resetLayers() {
	var layers = map.getLayers();
	for(var i = 0; i < layers.getLength(); i++) {
		var layer = layers.item(i);
		if(layer.get('type') !== 'default') {
			map.removeLayer(layer);
		}
	}
	layerObject = {};
}
</script>
</body>
<style>
table.dataTable thead th{
	font-weight: bold;
    text-transform: uppercase;
    font-size: 14px;
}
table.dataTable td {
	font-size: 15px;
}
td.title {
	color: #226FCB;
	text-decoration: underline;
	cursor: pointer;
}

.img_list {
	display: block;
	width: 100%;
	text-align: left;
	padding: 10px 15px;
	border: none;
	background: none;
	cursor: pointer;
	transition: 0.3s;
}

.img_list:hover {
	text-decoration: underline;
	cursor: pointer;
}

.img_list_click {
	color: #226FCB;
	font-weight: bold;
	display: block;
	width: 100%;
	text-align: left;
	padding: 10px 15px;
	border: none;
	background: none;
	cursor: pointer;
	transition: 0.3s;
}

.news-wrap .article-wrap {
	width: 100%;
	height: 250px;
	color: #474747;
	font-size: 14px;
	text-align: left;
	padding: 0 10px 10px 0;
	box-sizing: border-box;
	overflow-y: scroll;
}

.img-wrap {
	width: 100%;
	margin-top: 20px;
	padding-top: 20px;
	border-top: 1px solid #ddd;
}

metadata {
	color: #fff;
	text-align:center;
	background:  #33577B;
	padding: 2px 4px;
	margin: 3px 3px;
	border-radius:2px;
	display: inline-block; 
}

#table_detail .hidden_row {
	display: none;
}

#table_detail2 .hidden_row {
	display: none;
	/* 	padding-left: 10px; */
	padding-left: 0;
}

#table_detail li:hover {
	text-decoration: underline;
	cursor: pointer;
}

#table_detail tr {
	cursor: pointer;
}

#table_detail2 tr {
	cursor: pointer;
}

.news-wrap .center {
	width: 100%;
	color: #474747;
	font-size: 16px;
	font-weight: bold;
	text-align: center;
}

.con_div {
	margin: 0px;
	width: 262px;
}

.con_div label {
	font-weight: 400
}

#table_detail td ul {
	padding-left: 25px;
}

#table_detail2 td ul {
	padding-left: 25px;
}

#loading {
	position: absolute;
	left: 50%;
	top: 50%;
	z-index: 1000;
	display: none;
}

#loading2 {
	position: absolute;
	left: 40%;
	top: 50%;
	z-index: 1000;
	display: none;
}
}
</style>
</html>
