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
</head>

<script type="text/javascript">
  var contextPath = '${pageContext.request.contextPath}';
</script>

<!-- ========== Css Files ========== -->
<link rel="stylesheet"
	href="js/cm/cmsc003/lib/openlayers-6.9.0-dist/ol.css">
<link rel="stylesheet" href="css/ol-ext.css" />
<link rel="stylesheet"
	href="js/cm/cmsc003/lib/jstree-3.2.1/themes/default/style.min.css">


<body>
	<!-- 상단메뉴 -->
	<%@ include file="../topMenu.jsp"%>

	<!-- //////////////////////////////////////////////////////////////////////////// -->
	<!-- START SIDEBAR -->
	<form method="post" id="searchForm" name="searchForm">
		<div class="sidebar clearfix">
			<div role="tabpanel" class="sidepanel"
				style="display: block; height: 98%; width: auto;">
				<div class="tab-content" style="height: 100%; padding: 0;">
					<!-- Nav tabs -->
					<ul class="nav nav-tabs" role="tablist">
						<li id="createTab" role="presentation" class="active"
							style="width: 50%"><a href="#tab-01" aria-controls="tab-01"
							role="tab" data-toggle="tab" aria-expanded="true" class="active"><i
								class="fas fa-plus-square"></i><br>긴급공간정보 생성 </a></li>
						<li id="manageTab" role="presentation" class="" style="width: 50%"><a
							href="#tab-02" aria-controls="tab-02" role="tab"
							data-toggle="tab" class="" aria-expanded="false"> <i
								class="fas fa-clipboard-list"></i><br>긴급공간정보 관리
						</a></li>
					</ul>
					<div role="tabpanel" class="tab-pane active" id="today"
						style="overflow-y: auto; height: 100%; padding: 10px;">
						<div class="sidepanel-m-title">긴급공간정보 데이터셋 검색</div>
						<div class="panel-body">
							<div class="form-group m-t-6" style="margin-top: 8px;">
								<label class="col-sm-3 control-label form-label">재난 ID</label>
								<div class="col-sm-9">
									<input type="text" class="form-control" id="disasterIdCreate"
										name="disasterId" readOnly="readOnly"
										style="cursor: auto; background-color: #fff;">
								</div>
							</div>


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
							<div id="datepickerContainer"
								class="form-group m-t-6 input-daterange">
								<label for="input002" class="col-sm-2 control-label form-label">기간</label>
								<div class="col-sm-5">
									<input type="text" class="form-control ui-input-datepicker"
										id="inputStartDate" name="dateFrom" value="2020-04-23">
									<button type="button" id="btnOpenDatepickerStart"
										class="btn btn-default btn-icon ui-btn-open-datepicker">
										<i class="fas fa-calendar-alt"></i>
									</button>
								</div>

								<div class="col-sm-5">
									<input type="text" class="form-control ui-input-datepicker"
										id="inputEndDate" name="dateTo" value="2020-04-26">
									<button type="button" id="btnOpenDatepickerEnd"
										class="btn btn-default btn-icon ui-btn-open-datepicker">
										<i class="fas fa-calendar-alt"></i>
									</button>
								</div>
							</div>
							<div class="form-group m-t-6">
								<label for="input002" class="col-sm-3 control-label form-label">해상도(m)</label>
								<div class="col-sm-4">
									<input type="number" class="form-basic" name="resolMin" />
									<!-- 									<select class="form-basic" name="resolMin"> -->
									<!-- 										<option value='5cm' selected>5</option> -->
									<!-- 										<option value='10cm'>10</option> -->
									<!-- 										<option value='25cm'>25</option> -->
									<!-- 									</select> -->
								</div>
								<div class="col-sm-1">
									<span class="txt-in-form">~</span>
								</div>
								<div class="col-sm-4">
									<input type="number" class="form-basic" name="resolMax" />
									<!-- 									<select class="form-basic" name="resolMax"> -->
									<!-- 										<option value='5cm'>5</option> -->
									<!-- 										<option value='10cm'>10</option> -->
									<!-- 										<option value='25cm' selected>25</option> -->
									</select>
								</div>
							</div>
							<div class="form-group m-t-6">
								<label class="col-sm-3 control-label form-label">재난 유형</label>
								<div class="col-sm-9">
									<select id="selectDisasterType" class="form-basic"
										name="disasterType">
										<option value='AllDisaster'>전체</option>
										<option value='Flood'>수해</option>
										<option value='Landslide'>산사태</option>
										<option value='ForestFire'>산불</option>
										<option value='Earthquake'>지진</option>
										<option value='MaritimeDisaster'>해양재난</option>
										<option value='RedTide'>적조</option>
									</select>
								</div>
							</div>

							<!-- <div class="form-group m-t-6">
								<label class="col-sm-3 control-label form-label">재난 ID</label>
								<div class="col-sm-9">
									<input type="text" class="form-control" id="disasterId"
										name="disasterId" readOnly="readOnly"
										style="cursor: auto; background-color: #fff;">
								</div>
							</div> -->

							<div class="form-group m-t-6">
								<label class="col-sm-12 control-label form-label">재난지역</label>
								<div class="col-sm-4">
									<select class="form-basic" name="sido"
										onChange="getSigungu(this.value)">
										<option selected disabled hidden>광역시도</option>
									</select>
								</div>
								<div class="col-sm-4">
									<select class="form-basic" name="sigungu"
										onChange="getEmd(this.value)" disabled>
										<option selected disabled hidden>시군구</option>
									</select>
								</div>
								<div class="col-sm-4">
									<select class="form-basic" name="emd"
										onChange="getEmdPosition(this.value)" disabled>
										<option selected disabled hidden>읍면동</option>
									</select>
								</div>
							</div>
							<div class="form-group m-t-6">
								<div class="col-sm-12">
									<input type="text" id="detailAddrForm" readOnly="readOnly"
										class="form-basic">
								</div>
							</div>


							<div class="form-group m-t-6">
								<label for="input002" class="col-sm-2 control-label form-label">ROI</label>
								<div class="col-sm-10">
									<div class="col-sm-2 in-tit">ULX</div>
									<div class="col-sm-4">
										<input type="text" class="form-control js-input-roi-extent"
											id="inputULX" name="ulx4326"> <input type="hidden"
											id="inputULX2" name="ulx5186"> <input type="hidden"
											id="inputULX3" name="ulx5179"> <input type="hidden"
											id="inputULX4" name="ulx32652"> <input type="hidden"
											id="inputULX5" name="ulx5187">
									</div>
									<div class="col-sm-2 in-tit">ULY</div>
									<div class="col-sm-4">
										<input type="text" class="form-control js-input-roi-extent"
											id="inputULY" name="uly4326"> <input type="hidden"
											id="inputULY2" name="uly5186"> <input type="hidden"
											id="inputULY3" name="uly5179"> <input type="hidden"
											id="inputULY4" name="uly32652"> <input type="hidden"
											id="inputULY5" name="uly5187">
									</div>
									<div class="col-sm-2 in-tit m-t-5">LRX</div>
									<div class="col-sm-4 m-t-5">
										<input type="text" class="form-control js-input-roi-extent"
											id="inputLRX" name="lrx4326"> <input type="hidden"
											id="inputLRX2" name="lrx5186"> <input type="hidden"
											id="inputLRX3" name="lrx5179"> <input type="hidden"
											id="inputLRX4" name="lrx32652"> <input type="hidden"
											id="inputLRX5" name="lrx5187">
									</div>
									<div class="col-sm-2 in-tit m-t-5">LRY</div>
									<div class="col-sm-4 m-t-5">
										<input type="text" class="form-control js-input-roi-extent"
											id="inputLRY" name="lry4326"> <input type="hidden"
											id="inputLRY2" name="lry5186"> <input type="hidden"
											id="inputLRY3" name="lry5179"> <input type="hidden"
											id="inputLRY4" name="lry32652"> <input type="hidden"
											id="inputLRY5" name="lry5187">
									</div>
								</div>
							</div>

							<div class="btn-wrap a-cent">
								<a href="javascript:CMSC003.GIS.startSelectingROI()"
									class="btn btn-info" style="width: 160px;"><i
									class="fas fa-mouse-pointer m-r-5"></i>사용자 정의 ROI</a> <a
									href="javascript:CMSC003.DOM.resetROISelection()"
									class="btn btn-default"
									style="background-color: #e7b33e; width: 130px;"><i
									class="fas fa-times m-r-5"></i>ROI 초기화</a>
								<div class="form-group"
									style="margin-top: 3px; margin-left: 5px; padding-right: 10px;">
									<span class="col-sm-4 control-label form-label"
										style="padding-left: 10px; border: 1px solid #e1e1e1; margin-top: 1px; background-color: #e1e1e1;">ROI
										반경(m)</span>
									<div class="col-sm-8">
										<input type="number" class="form-control" id="roiRadius">
									</div>
								</div>
								<a href="javascript:search()" style="width: 294px;"
									class="btn btn-default"><i class="fas fa-search m-r-5"></i>검색</a>
							</div>
							<div class="sidepanel-s-title"
								style="margin-top: 8px; padding-bottom: 8px;">검색 결과</div>
							<div class="col-lg-12 js-search-result-area">
								<!-- 								style="position: absolute; bottom: 13%; top: 516px; left: 26px; width: 298px;"> -->

								<div class="panel panel-default"
									style="min-height: 98px; max-height: 200px;">
									<!-- 										<div class="in-panel-title">재난정보 목록</div> -->
									<!-- 										<ul class="result-list"> -->
									<!-- 											<li>재난ID(00건)</li> -->
									<!-- 											<li>20210723안동 산불(29건)</li> -->
									<!-- 										</ul> -->

									<!-- 										기존데이터 (벡터, 영상, DEM) -->
									<!-- 										<div id="resultList1"> -->
									<!-- 											<div class="in-panel-title">기존 데이터</div> -->
									<!-- 											<dl class="result-list-input"> -->
									<!-- 												<dt>벡터 데이터</dt> -->
									<!-- 												<dd> -->
									<!-- 													<input type="checkbox" id="map-001" name="vehicle1" -->
									<!-- 														value="map-001"> <label for="map-001">수치지도_001</label> -->
									<!-- 													<button type="button" class="btn btn-xs f-right"> -->
									<!-- 														<i class="fas fa-search m-r-5"></i>미리보기 -->
									<!-- 													</button> -->
									<!-- 												</dd> -->
									<!-- 												<dt>영상 데이터</dt> -->
									<!-- 												<dd> -->
									<!-- 													<input type="checkbox" id="map-001" name="vehicle1" -->
									<!-- 														value="map-001"> <label for="map-001">위성영상_001</label> -->
									<!-- 													<button type="button" class="btn btn-xs f-right"> -->
									<!-- 														<i class="fas fa-search m-r-5"></i>미리보기 -->
									<!-- 													</button> -->
									<!-- 												</dd> -->
									<!-- 												<dd> -->
									<!-- 													<input type="checkbox" id="map-001" name="vehicle1" -->
									<!-- 														value="map-001"> <label for="map-001">위성영상_002</label> -->
									<!-- 													<button type="button" class="btn btn-xs f-right"> -->
									<!-- 														<i class="fas fa-search m-r-5"></i>미리보기 -->
									<!-- 													</button> -->
									<!-- 												</dd> -->
									<!-- 											</dl> -->
									<!-- 										</div> -->
									<!-- 										긴급 영상 -->
									<!-- 										<div id="resultList2"> -->
									<!-- 											<div class="in-panel-title">긴급 영상</div> -->
									<!-- 											<dl class="result-list-input"> -->
									<!-- 												<dd> -->
									<!-- 													<input type="checkbox" id="map-001" name="vehicle1" -->
									<!-- 														value="map-001"> <label for="map-001">드론영상_001</label> -->
									<!-- 													<button type="button" class="btn btn-xs f-right"> -->
									<!-- 														<i class="fas fa-search m-r-5"></i>미리보기 -->
									<!-- 													</button> -->
									<!-- 												</dd> -->
									<!-- 												<dd> -->
									<!-- 													<input type="checkbox" id="map-001" name="vehicle1" -->
									<!-- 														value="map-001"> <label for="map-001">항공영상_001</label> -->
									<!-- 													<button type="button" class="btn btn-xs f-right"> -->
									<!-- 														<i class="fas fa-search m-r-5"></i>미리보기 -->
									<!-- 													</button> -->
									<!-- 												</dd> -->
									<!-- 											</dl> -->
									<!-- 										</div> -->
									<!-- 										분석결과데이터 -->
									<!-- 										<div id="resultList3"> -->
									<!-- 											<div class="in-panel-title">분석결과 데이터</div> -->
									<!-- 											<dl class="result-list-input" id="dataList"> -->
									<!-- 												<dd> -->
									<!-- 													<input type="checkbox" id="map-001" name="vehicle1" -->
									<!-- 														value="map-001"> <label for="map-001">변화지역_001</label> -->
									<!-- 													<button type="button" class="btn btn-xs f-right"> -->
									<!-- 														<i class="fas fa-search m-r-5"></i>미리보기 -->
									<!-- 													</button> -->
									<!-- 												</dd> -->
									<!-- 											</dl> -->
									<!-- 										</div> -->
								</div>
							</div>

							<div>
								<!-- 								style="position: absolute; bottom: 16px; right: 25px; left: 25px; height: 10%;"> -->
								<div class="form-group m-t-6">
									<div class="col-sm-4">
										<label for="input002" class="control-label form-label">추출
											방법</label>
									</div>
									<div class="col-sm-4">
										<div class="radio radio-info radio-inline"
											style="margin-top: 6px;">
											<input type="radio" id="inlineRadio5" value="option1"
												name="radioInline" checked=""> <label
												for="inlineRadio5">영역추출</label>
										</div>
									</div>
									<div class="col-sm-4">
										<div class="radio radio-inline" style="margin-top: 6px;">
											<input type="radio" id="inlineRadio6" value="option1"
												name="radioInline"> <label for="inlineRadio6">전체
												데이터</label>
										</div>
									</div>
								</div>
								<div class="btn-wrap a-cent" style="height: 100px;">
									<!-- <a href="javascript:CMSC003.DOM.showCreateDataMsg()"
										class="btn btn-default modal-open"><i
										class="fas fa-plus-square m-r-5"></i>긴급공간정보 생성</a> -->
									<a href="javascript:CMSC003.DOM.showBuildDataMsg()"
										class="btn btn-default modal-open"><i
										class="fas fa-plus-square m-r-5"></i>긴급공간정보 생성</a> <a
										href="javascript:CMSC003.DOM.showCreateDataMsg()"
										class="btn btn-default modal-open"><i
										class="fas fa-plus-square m-r-5"></i>재난지역 영상저장</a>
								</div>
							</div>
						</div>
					</div>
					<div role="tabpanel" class="tab-pane active" id="manageEmergency"
						style="overflow-y: auto; height: 100%; padding: 10px;">
						<div class="sidepanel-m-title">긴급공간정보 데이터셋 관리</div>
						<div class="panel-body">
							<div class="form-group m-t-6" style="margin-top: 8px;">
								<label class="col-sm-3 control-label form-label">재난 ID</label>
								<div class="col-sm-9">
									<input type="text" class="form-control" id="disasterIdManage"
										name="disasterId" readOnly="readOnly"
										style="cursor: pointer; background-color: #fff;">
								</div>
							</div>
							<div class="form-group m-t-6" style="margin-top: 8px;">
								<label class="col-sm-3 control-label form-label">저장경로</label>
								<div class="col-sm-9">
									<input type="text" class="form-control" id="storeDir"
										readOnly="readOnly"
										style="cursor: auto; background-color: #fff;">
								</div>
							</div>
							<div class="form-group m-t-6" style="margin-top: 8px;">
								<label class="col-sm-3 control-label form-label">데이터셋명</label>
								<div class="col-sm-9">
									<input type="text" class="form-control" id="storeDatasetName"
										readOnly="readOnly"
										style="cursor: auto; background-color: #fff;">
								</div>
							</div>
							<div class="form-group m-t-6" style="margin-top: 8px;">
								<a href="javascript:searchManagement()" style="width: 100%;"
									class="btn btn-default"><i class="fas fa-search m-r-5"></i>검색</a>
							</div>
							<div class="sidepanel-s-title"
								style="margin-top: 8px; padding-bottom: 8px;">검색 결과</div>
							<div class="col-lg-12 js-request-result-area">
								<div class="panel panel-default"
									style="min-height: 400px; max-height: 529px;"></div>
							</div>
							<div class="form-group m-t-6"
								style="margin-top: 8px; height: 88px;">

								<a href="#" style="width: 100%;" class="btn btn-default"
									id="sendDatasetBtn2"><i class="fas fa-plus-square m-r-5"></i>국토영상공급시스템 전송</a>
								<a href="#" style="width: 100%; margin-top: 4px;" class="btn btn-default"
									id="sendDatasetBtn"><i class="fas fa-plus-square m-r-5"></i>국토정보플랫폼 전송</a>
							</div>
							
							<!-- 테스트_230417
							<div class="btn-wrap a-cent" style="height: 100px;">
								<a href="javascript:popup_test()" class="btn btn-default modal-open">
									<i class="fas fa-plus-square m-r-5"></i>팝업 열기
								</a>
							</div> -->
							<!-- 임시 테스트_230417 -->
							
						</div>
					</div>
				</div>

			</div>

		</div>
	</form>
	<a href="#" class="sidebar-open-button"><i class="fa fa-bars"></i></a>
	<a href="#" class="sidebar-open-button-mobile"><i
		class="fa fa-bars"></i></a>

	<!-- START CONTENT -->
	<div class="content" style="padding-left: 0px">

		<div id="map1" class="ui-map-area"></div>

		<!-- <div class="page-header">
		<h1 class="title">재난정보 수집설정</h1>
	</div> -->

		<!-- <div class="container-widget">		
		<div class="col-md-12">
			<div class="panel panel-default">
				<div class="panel-title">
					<i class="fas fa-list"></i>재난정보 수집 요청 목록
				</div>
				
				<div class="panel-body table-responsive">
					<table class="table table-hover table-striped">
						<thead>
							<tr>
								<td class="text-center">재난 ID</td>
								<td class="text-center">재난 유형</td>
								<td class="text-center">재난 지역</td>
								<td class="text-center">POI</td>
								<td class="text-center">수집요청일</td>
								<td class="text-center">수집종료일</td>
								<td class="text-center">동작</td>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td class="text-center"></td>
								<td class="text-center">대전</td>
								<td class="text-center">화재</td>
								<td class="text-center">대전 유성구 원내동</td>
								<td class="text-center">2021.07.23</td>
								<td class="text-center">2021.07.24</td>
								<td class="text-center">
									<a href="#" class="btn btn-rounded btn-light"><i class="fas fa-play"></i></a>
									<a href="#" class="btn btn-rounded btn-light"><i class="fas fa-stop"></i></a>
									<a href="#" class="btn btn-rounded btn-light"><i class="fas fa-times"></i></a>
								</td>
							</tr>
							<tr>
								<td class="text-center"></td>
								<td class="text-center">서울</td>
								<td class="text-center">침수</td>
								<td class="text-center">서울 동작구 대방동</td>
								<td class="text-center"></td>
								<td class="text-center"></td>
								<td class="text-center">
									<a href="#" class="btn btn-rounded btn-light"><i class="fas fa-stop"></i></a>
									<a href="#" class="btn btn-rounded btn-light"><i class="fas fa-times"></i></a>
								</td>
							</tr>
						</tbody>
					</table>    
				</div>

			</div>
		</div>

	</div> -->
	</div>
	<div class="load" id="progres" style="display: none;"></div>

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
Summernote
================================================ -->
	<!-- <script src="js/summernote/summernote.min.js"></script> -->

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
Below codes are only for index widgets
================================================ -->
	<!-- Today Sales -->

	<!-- <script src="js/jquery/jquery-2.1.1.min.js"></script> -->
	<script src="js/cm/cmsc003/lib/openlayers-6.9.0-dist/ol.js"></script>
	<script type="text/javascript" src="js/map/ol-ext.js"></script>
	<script type="text/javascript" src="js/map/proj4js-combined.js"></script>

	<!-- ================================================
CMSC003 JS Lib
================================================ -->
	<script src="js/cm/cmsc003/lib/proj4js/proj4.js"></script>
	<script
		src="js/cm/cmsc003/lib/bootstrap-datepicker-1.9.0-dist/js/bootstrap-datepicker.min.js"></script>
	<script
		src="js/cm/cmsc003/lib/bootstrap-datepicker-1.9.0-dist/locales/bootstrap-datepicker.ko.min.js"></script>
	<!-- shp2geojson -->
	<script src="js/cm/cmsc003/lib/shp2geojson/lib/jszip.js"></script>
	<script src="js/cm/cmsc003/lib/shp2geojson/lib/jszip-utils.js"></script>
	<script src="js/cm/cmsc003/lib/shp2geojson/preprocess.js"></script>
	<script src="js/cm/cmsc003/lib/turf-6.5.0/turf.min.js"></script>
	<!-- jstree -->
	<script src="js/cm/cmsc003/lib/jstree-3.2.1/jstree.min.js"></script>
	<!-- ================================================
CMSC003 JS Dev
================================================ -->
	<script src="js/cm/cmsc003/src/cmsc003.js"></script>
	<script src="js/cm/cmsc003/src/gis/gis.js"></script>
	<script src="js/cm/cmsc003/src/storage/storage.js"></script>
	<script src="js/cm/cmsc003/src/dom/dom2.js"></script>
	<script src="js/cm/cmsc003/src/converter/converter.js"></script>
	<script src="js/cm/cmsc003/src/util/util.js"></script>
	<!-- ================================================
CMSC003 JS Dist
================================================ -->

	<!-- ================================================
CMSC003 JS event
================================================ -->
	<script src="js/cm/cmsc003/src/event/event.js"></script>

	<script type="text/javascript">
	
	var searchmapnm;
	
		$(document).ready(function() {
			// 재난 유형 셀렉박스 옵션 입력
// 			MSFRTN_TY_CD();
			
			// 지도 초기화
			CMSC003.GIS.initMap('map1');
			
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
			
			
			// 시도 셀렉트 초기화
			getSido();
		});
		
		// 관리용 검색
		function searchManagement() {
			var disasterId = $('#disasterIdManage').val();
			//console.log(disasterId);
			if(disasterId.trim() === '') {
				CMSC003.DOM.showErrorMsg('재난 ID를 입력해주세요.');
				return;
			}
			CMSC003.DOM.searchEmergencyManagement(disasterId);
		}

		// 검색
		function search() {
			//_map.removeInteraction(_draw);
			// ROI 선택 중지
			CMSC003.GIS.endSelectingROI();
			// 리스트 초기화
			$(".panel-default").empty();
			
			// form 데이터 가져오기 [{name:value},] 
			const formObject = {}; 
			$("#searchForm").serializeArray().forEach(data => formObject[data.name] = data.value);
						
			console.log(formObject);
			/* var id = $("#id").val();
			var	pwd = $("#pwd").val(); */

			// 영상-종류-미선택 시 경고창
			if (!formObject.dataKindCurrent && !formObject.dataKindEmergency && !formObject.dataKindResult) {
				return CMSC003.DOM.showErrorMsg("종류를 선택해주세요.");
				//return alert("종류를 선택하세요.");
			}
			
			// 기간-시작일-미입력 시 경고창
			if (formObject.dateFrom == '') {
				return CMSC003.DOM.showErrorMsg("시작일자를 입력해주세요.");
			}
			
			// 기간-종료일-미입력 시 경고창
			if (formObject.dateTo == '') {
				return CMSC003.DOM.showErrorMsg("종료일자를 입력해주세요.");
			}

			// ROI 미선택 시
			if (!formObject.lrx4326 || !formObject.lry4326 || !formObject.ulx4326 || !formObject.uly4326) {
				return CMSC003.DOM.showErrorMsg("ROI 범위를 지정해주세요.");
			}

			/* var checkboxValues = [];
			$("input[name='kind']:checked").each(function(i) {
			    checkboxValues.push($(this).val());
			}); */
			
			// 긴급 영상이 체크되었을 경우 EPSG:5186 변환 좌표값도 같이 전달
// 			if (formObject.dataKindEmergency) {
// 				const transform = CMSC003.GIS.transformExtent([Number(formObject.ulx), Number(formObject.uly), Number(formObject.lrx), Number(formObject.lry)], 'EPSG:4326', 'EPSG:5186');
// 				formObject.uly2 = transform[0];
// 				formObject.ulx2 = transform[1];
// 				formObject.lrx2 = transform[2];
// 				formObject.lry2 = transform[3];
// 			}
			
			// 전달용 form 데이터 가공 [{name:value},] => name=value&
			var formData = Object.entries(formObject).map(e => e.join('=')).join('&');
			
			// 스피너
			CMSC003.DOM.showSpinner(true);
			$.ajax({
				url : "cmsc003search.do",
				type : "post",
				data : formData,
				dataType : "json",
			 	success : function(returnData) {
					CMSC003.DOM.showSpinner(false);
					if(typeof returnData === 'string') {
						returnData = JSON.parse(returnData);
					}
					if(typeof returnData === 'string') {
						returnData = JSON.parse(returnData);
					}
					console.log(returnData); 
					// 리턴데이터 길이 계산
 					var returnDataSize = 0;
					returnDataSize += returnData.existing.digitalMap.length;
					returnDataSize += returnData.existing.airOrientalMap.length;
					returnDataSize += returnData.existing.ortOrientalMap.length;
					returnDataSize += returnData.existing.demMap.length;
					returnDataSize += returnData.existing.graphicsMap.length;
					returnData.existing.satellite.forEach(function(data) {
						returnDataSize += data.map.length;
					})
					returnDataSize += returnData.disaster.Flood.length;
					returnDataSize += returnData.disaster.Earthquake.length;
					returnDataSize += returnData.disaster.MaritimeDisaster.length;
					returnDataSize += returnData.disaster.Landslide.length;
					returnDataSize += returnData.disaster.ForestFire.length;
					returnDataSize += returnData.disaster.RedTide.length; //적조
					returnDataSize += returnData.analysis.objectExt.raster.length;
					returnDataSize += returnData.analysis.objectExt.vector.length;
					returnDataSize += returnData.analysis.changeDet.raster.length;
					returnDataSize += returnData.analysis.changeDet.vector.length;
					
					if(returnData.mapNm) {
						searchmapnm = returnData.mapNm;
					}
					
					//console.log('returnDataSize', returnDataSize);
			
					if (returnDataSize == 0) {
						$(".panel-default").empty();
						$(".panel-default").append('<span>검색 결과가 없습니다.</span>');
					// 검색 후 결과 있음
					} else {
						CMSC003.DOM.showSearchResultTree(returnData);					
					}
				},
				error : function(request, status, error) {
					CMSC003.DOM.showSpinner(false);
					alert("code:" + request.status + "\n" + "message:"
							+ request.respnseText + "\n" + "error:" + error);
				}
			});

		}
		
		// TODO :
		// 긴급공간정보 생성 및 구축 
		// INPUT : 'create', 'build'
		function createData(type) {
// 			CMSC003.DOM.showProgressBar();
			
			if($('#disasterIdCreate').val().trim() === '') {
				CMSC003.DOM.showErrorMsg('재난 ID를 입력해주세요');
				return;
			}
			var formData = $("#searchForm").serializeArray();
			/* var id = $("#id").val();
			var	pwd = $("#pwd").val(); */
			const vidoIdList = [];
			
			const extent5179 = CMSC003.GIS.getROIExtent();
			if(!extent5179) {
				alert('ROI 범위를 지정해주세요.');
				return;
			}
			
			const extent4326 = CMSC003.GIS.convert5179To4326Extent(extent5179);
			const extent5186 = CMSC003.GIS.convert5179To5186Extent(extent5179);
			const extent32652 = CMSC003.GIS.convert5179To32652Extent(extent5179);
			var selectData = {
					"mapNm":searchmapnm,
					"disasterId" : $('#disasterIdCreate').val(),
					"disasterCd" : CMSC003.Storage.get('currentDisasterCode'),
					"existing" : {
						"digitalMap" : [],
						"dem" : [],
						"aerial" : [],
						"ortho" : [],
						"satellite" : {
							"kompsat" : [],
							"landsat" : [],	
							"sentinel" : [],
							"cas" : [],
							"sar" : [],
						},
						"demographic" : [],
						"buldgraphics" : [],
						"landgraphics" : []
					},
					"analysis" : {
						"objectExt" : {
							"vector" : [],
							"raster" : []
						},
						"changeDet" : {
							"vector" : [],
							"raster" : []
						}
					},
					"disaster" : {
						"flood" : [],
						"landslip" : [],
						"forestFire" : [],
						"rain" : [],
						"earthquake" : [],
						"typhoon" : [],
						"maritmeDisaster" : [],
						"redTide" : []
					},
					"createInfo": {
						"roi": {
							"roi4326" : {
								"lrx": extent4326[2],
								"lry": extent4326[1],
								"ulx": extent4326[0],
								"uly": extent4326[3]
							},
							"roi5186" : {
								"lrx": extent5186[2],
								"lry": extent5186[1],
								"ulx": extent5186[0],
								"uly": extent5186[3]
							},
							"roi5179" : {
								"lrx": extent5179[2],
								"lry": extent5179[1],
								"ulx": extent5179[0],
								"uly": extent5179[3]
							},
							"roi32652" : {
								"lrx": extent32652[2],
								"lry": extent32652[1],
								"ulx": extent32652[0],
								"uly": extent32652[3]
							}
						}
					}
			};	
			
			const dataMap = CMSC003.Storage.get('searchDataMap');
			console.log(dataMap);
			// 수치지도 데이터
			$("#resultList1 input.js-search-result-digital:checkbox:checked").each(function(i, e) {
				var list = CMSC003.Storage.get('searchDataMap');
				selectData.existing.digitalMap.push(list[$(this).val()]);
			});
			
			// komsat 데이터
			$("#resultList1 input.js-search-result-kompsat:checkbox:checked").each(function(i, e) {
				var list = CMSC003.Storage.get('searchDataMap');
				selectData.existing.satellite.kompsat.push(list['kompsat-'+$(this).val()]);
			});
			
			// landsat 데이터
			$("#resultList1 input.js-search-result-landsat:checkbox:checked").each(function(i, e) {
				var list = CMSC003.Storage.get('searchDataMap');
				selectData.existing.satellite.landsat.push(list['landsat-'+$(this).val()]);
			});
			
			// sentinel 데이터
			$("#resultList1 input.js-search-result-sentinel:checkbox:checked").each(function(i, e) {
				var list = CMSC003.Storage.get('searchDataMap');
				selectData.existing.satellite.sentinel.push(list['sentinel-'+$(this).val()]);
			});
			
			// cas 데이터
			$("#resultList1 input.js-search-result-cas:checkbox:checked").each(function(i, e) {
				var list = CMSC003.Storage.get('searchDataMap');
				selectData.existing.satellite.cas.push(list['cas-'+$(this).val()]);
			});
			
			// sar 데이터
			$("#resultList1 input.js-search-result-sar:checkbox:checked").each(function(i, e) {
				var list = CMSC003.Storage.get('searchDataMap');
				selectData.existing.satellite.sar.push(list['sar-'+$(this).val()]);
			});
			
			
			// 항공영상 데이터
			$("#resultList1 input.js-search-result-aerial:checkbox:checked").each(function(i, e) {
				var list = CMSC003.Storage.get('searchDataMap');
				selectData.existing.aerial.push(list['aerial-'+$(this).val()]);
			});
			
			// 정사영상 데이터
			$("#resultList1 input.js-search-result-ortho:checkbox:checked").each(function(i, e) {
				var list = CMSC003.Storage.get('searchDataMap');
				selectData.existing.ortho.push(list['ortho-'+$(this).val()]);
			});
			
			// 인구통계 데이터
			$("#resultList1 input.js-search-result-demo:checkbox:checked").each(function(i, e) {
				var list = CMSC003.Storage.get('searchDataMap');
				selectData.existing.demographic.push(list[$(this).val()]);
			});
			
			// 지가통계 데이터
			$("#resultList1 input.js-search-result-landg:checkbox:checked").each(function(i, e) {
				var list = CMSC003.Storage.get('searchDataMap');
				selectData.existing.landgraphics.push(list[$(this).val()]);
			});
			
			// 건물통계 데이터
			$("#resultList1 input.js-search-result-buld:checkbox:checked").each(function(i, e) {
				var list = CMSC003.Storage.get('searchDataMap');
				selectData.existing.buldgraphics.push(list[$(this).val()]);
			});
			
			// dem 데이터
			$("#resultList1 input.js-search-result-dem:checkbox:checked").each(function(i, e) {
				var list = CMSC003.Storage.get('searchDataMap');
				selectData.existing.dem.push(list['dem-'+$(this).val()]);
			});
			
			// 긴급 데이터
			// 침수
			$("#resultList2 input.js-search-result-flood:checkbox:checked").each(function(i, e) {
				var list = CMSC003.Storage.get('searchDataMap');
				selectData.disaster.flood.push(list['flood-'+$(this).val()]);
			});
			// 산사태
			$("#resultList2 input.js-search-result-landslip:checkbox:checked").each(function(i, e) {
				var list = CMSC003.Storage.get('searchDataMap');
				selectData.disaster.landslip.push(list['landslide-'+$(this).val()]);
			});
			// 산불
			$("#resultList2 input.js-search-result-forestfire:checkbox:checked").each(function(i, e) {
				var list = CMSC003.Storage.get('searchDataMap');
				selectData.disaster.forestFire.push(list['forestFire-'+$(this).val()]);
			});
			// 폭우
			$("#resultList2 input.js-search-result-rain:checkbox:checked").each(function(i, e) {
				var list = CMSC003.Storage.get('searchDataMap');
				selectData.disaster.rain.push(list['rain-'+$(this).val()]);
			});
			// 태풍
			$("#resultList2 input.js-search-result-typhoon:checkbox:checked").each(function(i, e) {
				var list = CMSC003.Storage.get('searchDataMap');
				selectData.disaster.typhoon.push(list['typhoon-'+$(this).val()]);
			});
			// 지진
			$("#resultList2 input.js-search-result-earthquake:checkbox:checked").each(function(i, e) {
				var list = CMSC003.Storage.get('searchDataMap');
				selectData.disaster.earthquake.push(list['earthquake-'+$(this).val()]);
			});
			// 해양재난
			$("#resultList2 input.js-search-result-maritime:checkbox:checked").each(function(i, e) {
				var list = CMSC003.Storage.get('searchDataMap');
				selectData.disaster.maritmeDisaster.push(list['maritime-'+$(this).val()]);
			});
			// 적조
			$("#resultList2 input.js-search-result-redTide:checkbox:checked").each(function(i, e) {
				var list = CMSC003.Storage.get('searchDataMap');
				selectData.disaster.redTide.push(list['redTide-'+$(this).val()]);
			});
			// 분석 벡터 객체추출
			$("#resultList3 input.js-search-result-vectoranalobj:checkbox:checked").each(function(i, e) {
				var list = CMSC003.Storage.get('searchDataMap');
				selectData.analysis.objectExt.vector.push(list['vectoranalobj-'+$(this).val()]);
			});
			
			// 분석 래스터 객체추출
			$("#resultList3 input.js-search-result-rasteranalobj:checkbox:checked").each(function(i, e) {
				var list = CMSC003.Storage.get('searchDataMap');
				selectData.analysis.objectExt.raster.push(list['rasteranalobj-'+$(this).val()]);
			});
			
			// 분석 벡터 변화탐지
			$("#resultList3 input.js-search-result-vectoranalchg:checkbox:checked").each(function(i, e) {
				var list = CMSC003.Storage.get('searchDataMap');
				selectData.analysis.changeDet.vector.push(list['vectoranalchg-'+$(this).val()]);
			});
			
			// 분석 래스터 변화탐지
			$("#resultList3 input.js-search-result-rasteranalchg:checkbox:checked").each(function(i, e) {
				var list = CMSC003.Storage.get('searchDataMap');
				selectData.analysis.changeDet.raster.push(list['rasteranalchg-'+$(this).val()]);
			});

			console.log(selectData);
// 			console.log(JSON.stringify(selectData));
			
			if(
					!selectData.disaster.forestFire.length && 
					!selectData.disaster.landslip.length &&
					!selectData.disaster.flood.length &&
					!selectData.disaster.earthquake.length &&
					!selectData.disaster.redTide.length && //적조
					!selectData.existing.dem.length &&
					!selectData.existing.demographic.length &&
					!selectData.existing.aerial.length &&
					!selectData.existing.ortho.length &&
					!selectData.existing.satellite.landsat.length &&
					!selectData.existing.satellite.kompsat.length &&
					!selectData.existing.satellite.sentinel.length &&
					!selectData.existing.satellite.cas.length &&
					!selectData.existing.satellite.sar.length &&
					!selectData.existing.digitalMap.length &&
					!selectData.analysis.objectExt.vector.length &&
					!selectData.analysis.objectExt.raster.length &&
					!selectData.analysis.changeDet.vector.length &&
					!selectData.analysis.changeDet.raster.length &&
					!selectData.existing.landgraphics.length &&
					!selectData.existing.buldgraphics.length 
			) {
				var disasterMsg = type == 'create' ? '저장할 공간정보를 선택해주세요.' : '생성할 공간정보를 선택해주세요.';
				CMSC003.DOM.showErrorMsg(disasterMsg);
				return;
			}
			
			// ajax로 json 보낼 경우 인식을 못하는 경우가 생김
			var url = type == "create" ? "cmsc003saveData.do" : "cmsc003createData2.do";
			console.log(type + ':' + url);
			
			CMSC003.DOM.showSpinner(true);
			var xhr = new XMLHttpRequest();
			xhr.open("POST", url);
			
			xhr.setRequestHeader("Accept", "application/json");
			xhr.setRequestHeader("Content-Type", "application/json");
			xhr.responseType='json';
			
			xhr.onreadystatechange = function () {
			if (xhr.readyState === 4) {
				CMSC003.DOM.showSpinner(false);
				
				const copyROI = [extent5179[0], extent5179[1], extent5179[2], extent5179[3]];
				CMSC003.Storage.set('requestROIExtent', copyROI);
				
				CMSC003.DOM.showRequestResult(xhr.response);
				
// 				console.log(xhr.status);
// 				console.log(xhr.responseText);
				var successMsg = type == 'create' ? '재난지역 영상저장을 완료하였습니다.' : '긴급공간정보 생성을 완료하였습니다.';
				CMSC003.DOM.showErrorMsg(successMsg);
				
			}};
			console.log(JSON.stringify(selectData), 'createData');
			xhr.send(JSON.stringify(selectData));
		}
		
		function determineMosaic() {
			if($('#disasterIdCreate').val().trim() === '') {
				CMSC003.DOM.showErrorMsg('재난 ID를 입력해주세요');
				return;
			}
			
			if($('#disasterNmUser').val().trim() === '') {
				CMSC003.DOM.showErrorMsg('데이터셋 이름을 입력해주세요');
				return;
			}
			
			var name = $('#disasterNmUser').val();
			CMSC003.Storage.set('datasetName', name);
			
			var formData = $("#searchForm").serializeArray();
			const vidoIdList = [];
			
			const extent5179 = CMSC003.GIS.getROIExtent();
			if(!extent5179) {
				alert('ROI 범위를 지정해주세요.');
				return;
			}
			
			const extent4326 = CMSC003.GIS.convert5179To4326Extent(extent5179);
			const extent5186 = CMSC003.GIS.convert5179To5186Extent(extent5179);
			const extent32652 = CMSC003.GIS.convert5179To32652Extent(extent5179);
			var selectData = {
					"mapNm":searchmapnm,
					"disasterNmUser" : $('#disasterNmUser').val(),
					"disasterId" : $('#disasterIdCreate').val(),
					"disasterCd" : CMSC003.Storage.get('currentDisasterCode'),
					"existing" : {
						"digitalMap" : [],
						"demMap" : [],
						"airOrientalMap" : [],
						"ortOrientalMap" : [],
						"satellite" : [
							{
								"satNm": "Kompsat",
								"map": []
							},
							{
								"satNm": "Landsat",
								"map": []
							},
							{
								"satNm": "Sentinel",
								"map": []
							},
							{
								"satNm": "CAS",
								"map": []
							},
							{
								"satNm": "SAR",
								"map": []
							}
						],

						"graphicsMap" : []
					},
					"analysis" : {
						"objectExt" : {
							"vector" : [],
							"raster" : []
						},
						"changeDet" : {
							"vector" : [],
							"raster" : []
						}
					},
					"disaster" : {
						"Flood" : [],
						"Landslip" : [],
						"ForestFire" : [],
						//"Rain" : [],
						"Earthquake" : [],
						//"Typhoon" : [],
						"MaritmeDisaster" : [],
						"RedTide" : [] //적조
					},
					"createInfo": {
						"roi": {
							"roi4326" : {
								"lrx": extent4326[2],
								"lry": extent4326[1],
								"ulx": extent4326[0],
								"uly": extent4326[3]
							},
							"roi5186" : {
								"lrx": extent5186[2],
								"lry": extent5186[1],
								"ulx": extent5186[0],
								"uly": extent5186[3]
							},
							"roi5179" : {
								"lrx": extent5179[2],
								"lry": extent5179[1],
								"ulx": extent5179[0],
								"uly": extent5179[3]
							},
							"roi32652" : {
								"lrx": extent32652[2],
								"lry": extent32652[1],
								"ulx": extent32652[0],
								"uly": extent32652[3]
							}
						}
					}
			};	
			
			const dataMap = CMSC003.Storage.get('searchDataMap');
			
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
			const redTide = $("#resultList2 #resultList2_redTide_tree").jstree().get_bottom_checked(true); //적조
			const vectorAnalObj = $("#resultList3 #resultList3_vectorAnalObj_tree").jstree().get_bottom_checked(true);
			const rasterAnalObj = $("#resultList3 #resultList3_rasterAnalObj_tree").jstree().get_bottom_checked(true);
			const vectorAnalChg = $("#resultList3 #resultList3_vectorAnalChg_tree").jstree().get_bottom_checked(true);
			const rasterAnalChg = $("#resultList3 #resultList3_rasterAnalChg_tree").jstree().get_bottom_checked(true);
			
			var needMosaic = false;
			
			if(ortho.length > 0) {
				console.log(ortho);
				
				// 정사영상 데이터
				var yearKey = {};
				ortho.forEach(function(item) {
					var list = CMSC003.Storage.get('searchDataMap');
					var orthoItem = list['ortho-'+item.li_attr.value];
					console.log(orthoItem);
					var year = item.parent.split('-')[4];
					console.log(year);
					if(yearKey[year] === undefined) {
						yearKey[year] = 0;
					}
					yearKey[year] = yearKey[year] + 1;
				});
				
				var keys = Object.keys(yearKey);
				for(var i = 0; i < keys.length; i++) {
					var total = yearKey[keys[i]];
					if(total > 1) {
						needMosaic = true;
					}
				}
				
				console.log(needMosaic);
				
				if(needMosaic) {
					CMSC003.DOM.showMosaicComfirm();
				} else {
					createDataTree('build');
				}
				
			} else {
				createDataTree('build');
			}
		}
		
		function createMosaicTree(doMosaic) {
			
			console.log(doMosaic);
			CMSC003.Storage.set('doMosaic', doMosaic);
			
			if(!doMosaic) {
				createDataTree('build');
				return;
			}
			
			// type -> create : 재난 지역 영상 저장
			// type -> build : 긴급 공간정보 생성
// 			CMSC003.DOM.showProgressBar();
			
			if($('#disasterIdCreate').val().trim() === '') {
				CMSC003.DOM.showErrorMsg('재난 ID를 입력해주세요');
				return;
			}
			
// 			if(type === 'build' && $('#disasterNmUser').val().trim() === '') {
// 				CMSC003.DOM.showErrorMsg('데이터셋 이름을 입력해주세요');
// 				return;
// 			}
			
			var formData = $("#searchForm").serializeArray();
			const vidoIdList = [];
			
			const extent5179 = CMSC003.GIS.getROIExtent();
			if(!extent5179) {
				alert('ROI 범위를 지정해주세요.');
				return;
			}
			
			const extent4326 = CMSC003.GIS.convert5179To4326Extent(extent5179);
			const extent5186 = CMSC003.GIS.convert5179To5186Extent(extent5179);
			const extent32652 = CMSC003.GIS.convert5179To32652Extent(extent5179);
			var selectData = {
					"mapNm":searchmapnm,
					"disasterNmUser" : $('#disasterNmUser').val(),
					"disasterId" : $('#disasterIdCreate').val(),
					"disasterCd" : CMSC003.Storage.get('currentDisasterCode'),
					"existing" : {
						"digitalMap" : [],
						"demMap" : [],
						"airOrientalMap" : [],
						"ortOrientalMap" : [],
						"satellite" : [
							{
								"satNm": "Kompsat",
								"map": []
							},
							{
								"satNm": "Landsat",
								"map": []
							},
							{
								"satNm": "Sentinel",
								"map": []
							},
							{
								"satNm": "CAS",
								"map": []
							},
							{
								"satNm": "SAR",
								"map": []
							}
						],

						"graphicsMap" : []
					},
					"analysis" : {
						"objectExt" : {
							"vector" : [],
							"raster" : []
						},
						"changeDet" : {
							"vector" : [],
							"raster" : []
						}
					},
					"disaster" : {
						"Flood" : [],
						"Landslip" : [],
						"ForestFire" : [],
						//"Rain" : [],
						"Earthquake" : [],
						//"Typhoon" : [],
						"MaritmeDisaster" : [],
						"RedTide" : []
					},
					"createInfo": {
						"roi": {
							"roi4326" : {
								"lrx": extent4326[2],
								"lry": extent4326[1],
								"ulx": extent4326[0],
								"uly": extent4326[3]
							},
							"roi5186" : {
								"lrx": extent5186[2],
								"lry": extent5186[1],
								"ulx": extent5186[0],
								"uly": extent5186[3]
							},
							"roi5179" : {
								"lrx": extent5179[2],
								"lry": extent5179[1],
								"ulx": extent5179[0],
								"uly": extent5179[3]
							},
							"roi32652" : {
								"lrx": extent32652[2],
								"lry": extent32652[1],
								"ulx": extent32652[0],
								"uly": extent32652[3]
							}
						}
					}
			};	
			
			const dataMap = CMSC003.Storage.get('searchDataMap');
			
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
			
			console.log(dataMap);
			// 수치지도 데이터
			vector.forEach(function(item) {
				var list = CMSC003.Storage.get('searchDataMap');
				if (list[item.li_attr.value]) {
					selectData.existing.digitalMap.push(list[item.li_attr.value]);					
				}
			});
			
			// kompsat 데이터
			// landsat 데이터
			// sentinel 데이터
			// cas 데이터
			// sar 데이터
			satellite.forEach(function(item) {
				var list = CMSC003.Storage.get('searchDataMap');
				var sat = item.id.split('-')[2];
				var folder = item.parent.split('-')[5];
				if (sat == "kompsat") {
					if (list['kompsat-'+item.li_attr.value]) {
						// 뎁스 확인해서 날짜 노드 폴더 리스트 입력
						var dateLabel = item.parents[1].split('vido-id-satellite-title-kompsat-')[1];
						
						var dateExist = false;
						var dateIdx = null;
						if(dateLabel) {
							for (var i=0; i<selectData.existing.satellite[0].map.length; i++) {
								if (selectData.existing.satellite[0].map[i].date === dateLabel) {
									dateIdx = i;
									dateExist = true;
									break;
								}
							}
							if (!dateExist) {
								var folderList = {
									'date': dateLabel,
									'folderList': []
								};
								selectData.existing.satellite[0].map.push(folderList);
								dateIdx = selectData.existing.satellite[0].map.length - 1; 
							}
						}
						if(!isNaN(dateIdx) && dateIdx !== null) {
							var check = false;
							for (var i=0; i<selectData.existing.satellite[0].map[dateIdx].folderList.length; i++) {
								if(selectData.existing.satellite[0].map[dateIdx].folderList[i].folderNm === folder) {
									selectData.existing.satellite[0].map[dateIdx].folderList[i].fileList.push(list['kompsat-'+item.li_attr.value]);
									check = true;
									break;
								}
							}
							
							if (!check) {
								var fileList = {
									'folderNm' : folder,
									'fileList' : [list['kompsat-'+item.li_attr.value]]
								};
								selectData.existing.satellite[0].map[dateIdx].folderList.push(fileList);
							}
						}
// 						var check = false;
						
// 						// 뎁스 확인해서 날짜 노드 폴더 리스트 입력
// 						var dateLabel = item.parents[1].split('vido-id-satellite-title-kompsat-')[1];
// 						console.log(dateLabel);
						
// 						// 뎁스 확인해서 리프노드면 파일리스트에 입력
// 						for (var i=0; i<selectData.existing.satellite[0].map.length; i++) {
// 							if (selectData.existing.satellite[0].map[i].folder == folder) {
// 								selectData.existing.satellite[0].map[i].fileList.push(list['kompsat-'+item.li_attr.value]);
// 								check = true;
// 								break;
// 							}
// 						}
// 						// 뎁스 확인해서 페런트 노드면 파일 리스트 생성
// 						if (!check) {
// 							selectData.existing.satellite[0].map.push( {"folder": folder, "fileList":[list['kompsat-'+item.li_attr.value]]} );
// 						}
					}
				} else if (sat == "landsat") {
					if (list['landsat-'+item.li_attr.value]) {
						var dateLabel = item.parents[1].split('vido-id-satellite-title-landsat-')[1];
						
						var dateExist = false;
						var dateIdx = null;
						if(dateLabel) {
							for (var i=0; i<selectData.existing.satellite[1].map.length; i++) {
								if (selectData.existing.satellite[1].map[i].date === dateLabel) {
									dateIdx = i;
									dateExist = true;
									break;
								}
							}
							if (!dateExist) {
								var folderList = {
									'date': dateLabel,
									'folderList': []
								};
								selectData.existing.satellite[1].map.push(folderList);
								dateIdx = selectData.existing.satellite[1].map.length - 1; 
							}
						}
						if(!isNaN(dateIdx) && dateIdx !== null) {
							var check = false;
							for (var i=0; i<selectData.existing.satellite[1].map[dateIdx].folderList.length; i++) {
								if(selectData.existing.satellite[1].map[dateIdx].folderList[i].folderNm === folder) {
									selectData.existing.satellite[1].map[dateIdx].folderList[i].fileList.push(list['landsat-'+item.li_attr.value]);
									check = true;
									break;
								}
							}
							
							if (!check) {
								var fileList = {
									'folderNm' : folder,
									'fileList' : [list['landsat-'+item.li_attr.value]]
								};
								selectData.existing.satellite[1].map[dateIdx].folderList.push(fileList);
							}
						}
// 						var check = false;
// 						for (var i=0; i<selectData.existing.satellite[1].map.length; i++) {
// 							if (selectData.existing.satellite[1].map[i].folder == folder) {
// 								selectData.existing.satellite[1].map[i].fileList.push(list['landsat-'+item.li_attr.value]);
// 								check = true;
// 								break;
// 							}
// 						}
// 						if (!check) {
// 							selectData.existing.satellite[1].map.push( {"folder": folder, "fileList":[list['landsat-'+item.li_attr.value]]} );
// 						}
					}
				} else if (sat == "sentinel") {
					if (list['sentinel-'+item.li_attr.value]) {
						var dateLabel = item.parents[1].split('vido-id-satellite-title-sentinel-')[1];
						
						var dateExist = false;
						var dateIdx = null;
						if(dateLabel) {
							for (var i=0; i<selectData.existing.satellite[2].map.length; i++) {
								if (selectData.existing.satellite[2].map[i].date === dateLabel) {
									dateIdx = i;
									dateExist = true;
									break;
								}
							}
							if (!dateExist) {
								var folderList = {
									'folderList': []
								};
								selectData.existing.satellite[2].map.push(folderList);
								dateIdx = selectData.existing.satellite[2].map.length - 1; 
							}
						}
						if(!isNaN(dateIdx) && dateIdx !== null) {
							var check = false;
							for (var i=0; i<selectData.existing.satellite[2].map[dateIdx].folderList.length; i++) {
								if(selectData.existing.satellite[2].map[dateIdx].folderList[i].folderNm === folder) {
									
									//새롬 수정
									//var fileList = list['sentinel-'+item.li_attr.value];

									//for(var cnt = 0; cnt < fileList.length; cnt++){
									//	fileList[cnt].innerFileCoursNm = fileList[cnt].innerFileCoursNm.replaceAll('period','.');
									//	fileList[cnt].fullFileCoursNm = fileList[cnt].fullFileCoursNm.replaceAll('period','.');
									//}
									
									//selectData.existing.satellite[2].map[dateIdx].folderList[i].fileList.push(fileList);
									//console.log("--------------sentinel-1")
									//원본
									selectData.existing.satellite[2].map[dateIdx].folderList[i].fileList.push(list['sentinel-'+item.li_attr.value]);
									check = true;
									break;
								}
							}
							
							if (!check) {
								var fileList = {
									'folderNm' : folder,
									'fileList' : [list['sentinel-'+item.li_attr.value]]
								};
								
								//새롬 수정
								//for(var cnt = 0; cnt < fileList.fileList.length; cnt++){
								//	fileList.fileList[cnt].innerFileCoursNm = fileList.fileList[cnt].innerFileCoursNm.replaceAll('period','.');
								//	fileList.fileList[cnt].fullFileCoursNm = fileList.fileList[cnt].fullFileCoursNm.replaceAll('period','.');
								//}
								
								//console.log("--------------sentinel-2")
								
								selectData.existing.satellite[2].map[dateIdx].folderList.push(fileList);
							}
						}
// 						var check = false;
// 						for (var i=0; i<selectData.existing.satellite[2].map.length; i++) {
// 							if (selectData.existing.satellite[2].map[i].folder == folder) {
// 								selectData.existing.satellite[2].map[i].fileList.push(list['sentinel-'+item.li_attr.value]);
// 								check = true;
// 								break;
// 							}
// 						}
// 						if (!check) {
// 							selectData.existing.satellite[2].map.push( {"folder": folder, "fileList":[list['sentinel-'+item.li_attr.value]]} );
// 						}
					}
				} else if (sat == "cas") {
					if (list['cas-'+item.li_attr.value]) {
						var dateLabel = item.parents[1].split('vido-id-satellite-title-cas-')[1];
						
						var dateExist = false;
						var dateIdx = null;
						if(dateLabel) {
							for (var i=0; i<selectData.existing.satellite[3].map.length; i++) {
								if (selectData.existing.satellite[3].map[i].date === dateLabel) {
									dateIdx = i;
									dateExist = true;
									break;
								}
							}
							if (!dateExist) {
								var folderList = {
									'date': dateLabel,
									'folderList': []
								};
								selectData.existing.satellite[3].map.push(folderList);
								dateIdx = selectData.existing.satellite[3].map.length - 1; 
							}
						}
						if(!isNaN(dateIdx) && dateIdx !== null) {
							var check = false;
							for (var i=0; i<selectData.existing.satellite[3].map[dateIdx].folderList.length; i++) {
								if(selectData.existing.satellite[3].map[dateIdx].folderList[i].folderNm === folder) {
									
									
									selectData.existing.satellite[3].map[dateIdx].folderList[i].fileList.push(list['cas-'+item.li_attr.value]);
									check = true;
									break;
								}
							}
							
							if (!check) {
								var fileList = {
									'folderNm' : folder,
									'fileList' : [list['cas-'+item.li_attr.value]]
								};
								selectData.existing.satellite[3].map[dateIdx].folderList.push(fileList);
							}
						}
// 						var check = false;
// 						for (var i=0; i<selectData.existing.satellite[3].map.length; i++) {
// 							if (selectData.existing.satellite[3].map[i].folder == folder) {
// 								selectData.existing.satellite[3].map[i].fileList.push(list['cas-'+item.li_attr.value]);
// 								check = true;
// 								break;
// 							}
// 						}
// 						if (!check) {
// 							selectData.existing.satellite[3].map.push( {"folder": folder, "fileList":[list['cas-'+item.li_attr.value]]} );
// 						}
					}
				}else if (sat == "sar") {
					if (list['sar-'+item.li_attr.value]) {
						var dateLabel = item.parents[1].split('vido-id-satellite-title-sar-')[1];
						
						var dateExist = false;
						var dateIdx = null;
						if(dateLabel) {
							for (var i=0; i<selectData.existing.satellite[4].map.length; i++) {
								if (selectData.existing.satellite[4].map[i].date === dateLabel) {
									dateIdx = i;
									dateExist = true;
									break;
								}
							}
							if (!dateExist) {
								var folderList = {
									'date': dateLabel,
									'folderList': []
								};
								selectData.existing.satellite[4].map.push(folderList);
								dateIdx = selectData.existing.satellite[4].map.length - 1; 
							}
						}
						if(!isNaN(dateIdx) && dateIdx !== null) {
							var check = false;
							for (var i=0; i<selectData.existing.satellite[4].map[dateIdx].folderList.length; i++) {
								if(selectData.existing.satellite[4].map[dateIdx].folderList[i].folderNm === folder) {
									selectData.existing.satellite[4].map[dateIdx].folderList[i].fileList.push(list['sar-'+item.li_attr.value]);
									check = true;
									break;
								}
							}
							
							if (!check) {
								var fileList = {
									'folderNm' : folder,
									'fileList' : [list['sar-'+item.li_attr.value]]
								};
								selectData.existing.satellite[4].map[dateIdx].folderList.push(fileList);
							}
						}
// 						var check = false;
// 						for (var i=0; i<selectData.existing.satellite[1].map.length; i++) {
// 							if (selectData.existing.satellite[1].map[i].folder == folder) {
// 								selectData.existing.satellite[1].map[i].fileList.push(list['landsat-'+item.li_attr.value]);
// 								check = true;
// 								break;
// 							}
// 						}
// 						if (!check) {
// 							selectData.existing.satellite[1].map.push( {"folder": folder, "fileList":[list['landsat-'+item.li_attr.value]]} );
// 						}
					}
				}
				
			});
			
			// 항공영상 데이터
			aerial.forEach(function(item) {
				var list = CMSC003.Storage.get('searchDataMap');
				var year = item.parent.split('-')[4];
				var dpi = item.parent.split('-')[5];
				
				var year_check = false;
				var dpi_check = false;
				
				var year_index = 0;
				var dpi_index = 0;
				
				// year -- 현재 영상의 year값에 해당되는 객체의 index를 추출함(없으면 생성해줌)
				for (var i=0; i<selectData.existing.airOrientalMap.length; i++) {
					var cur_year = selectData.existing.airOrientalMap[i].year;
					if (cur_year == year) {
						year_check = true;
						year_index = i; // selectData에서 현재 영상 year에 해당하는 index
						break;
					}
				}
				if (!year_check) { // selectData에 현재 영상 year에 해당하는 객체가 없으면 생성해줌
					selectData.existing.airOrientalMap.push( {"year": year, "maps": []} );
					year_index = selectData.existing.airOrientalMap.length-1; 
				}
				
				// dpi -- 추출한 year_index에서 현재 영상의 dpi값에 해당하는 객체가 있으면 데이터를 추가하고 없으면 객체 생성 후 데이터 추가함
				for (var i=0; i<selectData.existing.airOrientalMap[year_index].maps.length; i++) {
					var cur_dpi = selectData.existing.airOrientalMap[year_index].maps[i].dpi;
					if (cur_dpi == dpi) { // 현재 영상의 year와 dpi를 갖는 객체일때 
						selectData.existing.airOrientalMap[year_index].maps[i].map.push(list['aerial-'+item.li_attr.value]);
						dpi_check = true;
						break;
					}
				}
				if (!dpi_check) { // selectData에 현재 영상 year값의 객체는 있지만 dpi가 없을 경우 추가해줌
					selectData.existing.airOrientalMap[year_index].maps.push( {"dpi": dpi, "map": [list['aerial-'+item.li_attr.value]]} );
				}
			});
			
			// 정사영상 데이터
			ortho.forEach(function(item) {
				var list = CMSC003.Storage.get('searchDataMap');
				var year = item.parent.split('-')[4];
				var dpi = item.parent.split('-')[5];
				
				var year_check = false;
				var dpi_check = false;
				
				var year_index = 0;
				
				// year
				for (var i=0; i<selectData.existing.ortOrientalMap.length; i++) {
					var cur_year = selectData.existing.ortOrientalMap[i].year;
					if (cur_year == year) {
						year_check = true;
						year_index = i;
						break;
					}
				}
				if (!year_check) {
					selectData.existing.ortOrientalMap.push( {"year": year, "maps": []} );
					year_index = selectData.existing.ortOrientalMap.length-1; 
				}
				
				// dpi
				for (var i=0; i<selectData.existing.ortOrientalMap[year_index].maps.length; i++) {
					var cur_dpi = selectData.existing.ortOrientalMap[year_index].maps[i].dpi;
					if (cur_dpi == dpi) {
						selectData.existing.ortOrientalMap[year_index].maps[i].map.push(list['ortho-'+item.li_attr.value]);
						dpi_check = true;
						break;
					}
				}
				if (!dpi_check) {
					selectData.existing.ortOrientalMap[year_index].maps.push( {"dpi": dpi, "map": [list['ortho-'+item.li_attr.value]]} );
				}
			});
			
			// 인구통계 데이터
			// 지가통계 데이터
			// 건물통계 데이터
			graphics.forEach(function(item) {
				var list = CMSC003.Storage.get('searchDataMap');
				if (list[item.li_attr.value]) {
					selectData.existing.graphicsMap.push(list[item.li_attr.value]);					
				}
			});
			
			// dem 데이터
			dem.forEach(function(item) {
				var list = CMSC003.Storage.get('searchDataMap');
				var year = item.parent.split('-')[4];
				var dpi = item.parent.split('-')[5];
				
				var year_check = false;
				var dpi_check = false;
				
				var year_index = 0;
				
				// year
				for (var i=0; i<selectData.existing.demMap.length; i++) {
					var cur_year = selectData.existing.demMap[i].year;
					if (cur_year == year) {
						year_check = true;
						year_index = i;
						break;
					}
				}
				if (!year_check) {
					selectData.existing.demMap.push( {"year": year, "maps": []} );
					year_index = selectData.existing.demMap.length-1; 
				}
				
				// dpi
				for (var i=0; i<selectData.existing.demMap[year_index].maps.length; i++) {
					var cur_dpi = selectData.existing.demMap[year_index].maps[i].dpi;
					if (cur_dpi == dpi) {
						selectData.existing.demMap[year_index].maps[i].map.push(list['dem-'+item.li_attr.value]);
						dpi_check = true;
						break;
					}
				}
				if (!dpi_check) {
					selectData.existing.demMap[year_index].maps.push( {"dpi": dpi, "map": [list['dem-'+item.li_attr.value]]} );
				}
			});
			
			// 긴급 데이터
			// 침수
			flood.forEach(function(item) {
				var list = CMSC003.Storage.get('searchDataMap');
				if (list['flood-'+item.li_attr.value]) {
					selectData.disaster.Flood.push(list['flood-'+item.li_attr.value]);					
				}
			});
			// 산사태
			landslip.forEach(function(item) {
				var list = CMSC003.Storage.get('searchDataMap');
				if (list['landslide-'+item.li_attr.value]) {
					selectData.disaster.Landslip.push(list['landslide-'+item.li_attr.value]);
				}
			});
			// 산불
			forestFire.forEach(function(item) {
				var list = CMSC003.Storage.get('searchDataMap');
				if (list['forestFire-'+item.li_attr.value]) {
					selectData.disaster.ForestFire.push(list['forestFire-'+item.li_attr.value]);					
				}
			});
			// 지진
			earthquake.forEach(function(item) {
				var list = CMSC003.Storage.get('searchDataMap');
				if (list['earthquake-'+item.li_attr.value]) {
					selectData.disaster.Earthquake.push(list['earthquake-'+item.li_attr.value]);					
				}				
			});
			// 해양재난
			maritime.forEach(function(item) {
				var list = CMSC003.Storage.get('searchDataMap');
				if (list['maritime-'+item.li_attr.value]) {
					selectData.disaster.MaritmeDisaster.push(list['maritime-'+item.li_attr.value]);					
				}
			});
			// 적조
			redTide.forEach(function(item) {
				var list = CMSC003.Storage.get('searchDataMap');
				if (list['redTide-'+item.li_attr.value]) {
					selectData.disaster.RedTide.push(list['redTide-'+item.li_attr.value]);					
				}
			});
			
			// 분석 벡터 객체추출
			vectorAnalObj.forEach(function(item) {
				var list = CMSC003.Storage.get('searchDataMap');
				if (list['vectorAnalObj-'+item.li_attr.value]) {
					selectData.analysis.objectExt.vector.push(list['vectorAnalObj-'+item.li_attr.value]);					
				}
			});
			
			// 분석 래스터 객체추출
			rasterAnalObj.forEach(function(item) {
				var list = CMSC003.Storage.get('searchDataMap');
				if (list['rasterAnalObj-'+item.li_attr.value]) {
					selectData.analysis.objectExt.raster.push(list['rasterAnalObj-'+item.li_attr.value]);					
				}
			});
			
			// 분석 벡터 변화탐지
			vectorAnalChg.forEach(function(item) {
				var list = CMSC003.Storage.get('searchDataMap');
				if (list['vectorAnalChg-'+item.li_attr.value]) {
					selectData.analysis.changeDet.vector.push(list['vectorAnalChg-'+item.li_attr.value]);
				}
			});
			
			// 분석 래스터 변화탐지
			rasterAnalChg.forEach(function(item) {
				var list = CMSC003.Storage.get('searchDataMap');
				if (list['rasterAnalChg-'+item.li_attr.value]) {
					selectData.analysis.changeDet.raster.push(list['rasterAnalChg-'+item.li_attr.value]);					
				}
			});

			console.log(selectData);
// 			console.log(JSON.stringify(selectData));
		
			if(
					!selectData.disaster.ForestFire.length && 
					!selectData.disaster.Landslip.length &&
					!selectData.disaster.Flood.length &&
					!selectData.disaster.Earthquake.length &&
					!selectData.disaster.RedTide.length && //적조
					!selectData.existing.digitalMap.length &&
					!selectData.existing.demMap.length &&
					!selectData.existing.airOrientalMap.length &&
					!selectData.existing.ortOrientalMap.length &&
					!selectData.existing.graphicsMap.length &&
					!selectData.existing.satellite[0].map.length &&
					!selectData.existing.satellite[1].map.length &&
					!selectData.existing.satellite[2].map.length &&
					!selectData.existing.satellite[3].map.length &&
					!selectData.existing.satellite[4].map.length &&
					!selectData.analysis.objectExt.vector.length &&
					!selectData.analysis.objectExt.raster.length &&
					!selectData.analysis.changeDet.vector.length &&
					!selectData.analysis.changeDet.raster.length
			) {
				var disasterMsg = '생성할 공간정보를 선택해주세요.';
				CMSC003.DOM.showErrorMsg(disasterMsg);
				return;
			}
			
			// ajax로 json 보낼 경우 인식을 못하는 경우가 생김
			var url = "cmsc003mosaicResult.do";
			
			CMSC003.DOM.showSpinner(true);
			var xhr = new XMLHttpRequest();
			xhr.open("POST", url);
			
			xhr.setRequestHeader("Accept", "application/json");
			xhr.setRequestHeader("Content-Type", "application/json");
			xhr.responseType='json';
			
			xhr.onreadystatechange = function () {
			if (xhr.readyState === 4) {
				CMSC003.DOM.showSpinner(false);
				
				var result = xhr.response;
				if(typeof result === 'string') {
					result = JSON.parse(result);
				}
				if(typeof result === 'string') {
					result = JSON.parse(result);
				}
				
				CMSC003.DOM.showResultPathComfirm(result.pathList);
				
				// const copyROI = [extent5179[0], extent5179[1], extent5179[2], extent5179[3]];
				// CMSC003.Storage.set('requestROIExtent', copyROI);
			
// 				CMSC003.DOM.showRequestResultTree(xhr.response);
				//CMSC003.DOM.showRequestResultTree(selectData);
				
				// var successMsg = type == 'create' ? '재난지역 영상 저장을 완료하였습니다.' : '긴급공간정보 생성을 완료하였습니다.';
				// CMSC003.DOM.showErrorMsg(successMsg);
				
			}};
			xhr.addEventListener('error', function() {
				CMSC003.DOM.showErrorMsg("오류가 발생하였습니다.");
			});
			console.log(JSON.stringify(selectData), 'createData');
			xhr.send(JSON.stringify(selectData)); 
		}
		
		// TODO :
		// 긴급공간정보 생성 및 구축 
		// INPUT : 'create', 'build'
		function createDataTree(type) {
			// type -> create : 재난 지역 영상 저장
			// type -> build : 긴급 공간정보 생성
// 			CMSC003.DOM.showProgressBar();
			
			if($('#disasterIdCreate').val().trim() === '') {
				CMSC003.DOM.showErrorMsg('재난 ID를 입력해주세요');
				return;
			}
			
			if(type === 'build' && CMSC003.Storage.get('datasetName') === '') {
				CMSC003.DOM.showErrorMsg('데이터셋 이름을 입력해주세요');
				return;
			}
			
			var formData = $("#searchForm").serializeArray();
			const vidoIdList = [];
			
			const extent5179 = CMSC003.GIS.getROIExtent();
			if(!extent5179) {
				alert('ROI 범위를 지정해주세요.');
				return;
			}
			
			const extent4326 = CMSC003.GIS.convert5179To4326Extent(extent5179);
			const extent5186 = CMSC003.GIS.convert5179To5186Extent(extent5179);
			const extent32652 = CMSC003.GIS.convert5179To32652Extent(extent5179);
			var selectData = {
					"mapNm":searchmapnm,
					"autoMosasic": CMSC003.Storage.get('doMosaic') ? true : false,
					"disasterNmUser" : CMSC003.Storage.get('datasetName'),
					"disasterId" : $('#disasterIdCreate').val(),
					"disasterCd" : CMSC003.Storage.get('currentDisasterCode'),
					"existing" : {
						"digitalMap" : [],
						"demMap" : [],
						"airOrientalMap" : [],
						"ortOrientalMap" : [],
						"satellite" : [
							{
								"satNm": "Kompsat",
								"map": []
							},
							{
								"satNm": "Landsat",
								"map": []
							},
							{
								"satNm": "Sentinel",
								"map": []
							},
							{
								"satNm": "CAS",
								"map": []
							},
							{
								"satNm": "SAR",
								"map": []
							}
						],

						"graphicsMap" : []
					},
					"analysis" : {
						"objectExt" : {
							"vector" : [],
							"raster" : []
						},
						"changeDet" : {
							"vector" : [],
							"raster" : []
						}
					},
					"disaster" : {
						"Flood" : [],
						"Landslip" : [],
						"ForestFire" : [],
						//"Rain" : [],
						"Earthquake" : [],
						//"Typhoon" : [],
						"MaritmeDisaster" : [],
						"RedTide" : [] //적조
					},
					"createInfo": {
						"roi": {
							"roi4326" : {
								"lrx": extent4326[2],
								"lry": extent4326[1],
								"ulx": extent4326[0],
								"uly": extent4326[3]
							},
							"roi5186" : {
								"lrx": extent5186[2],
								"lry": extent5186[1],
								"ulx": extent5186[0],
								"uly": extent5186[3]
							},
							"roi5179" : {
								"lrx": extent5179[2],
								"lry": extent5179[1],
								"ulx": extent5179[0],
								"uly": extent5179[3]
							},
							"roi32652" : {
								"lrx": extent32652[2],
								"lry": extent32652[1],
								"ulx": extent32652[0],
								"uly": extent32652[3]
							}
						}
					}
			};	
			
			const dataMap = CMSC003.Storage.get('searchDataMap');
			
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
			
			console.log(dataMap);
			// 수치지도 데이터
			vector.forEach(function(item) {
				var list = CMSC003.Storage.get('searchDataMap');
				if (list[item.li_attr.value]) {
					selectData.existing.digitalMap.push(list[item.li_attr.value]);					
				}
			});
			
			// kompsat 데이터
			// landsat 데이터
			// sentinel 데이터
			// cas 데이터
			satellite.forEach(function(item) {
				var list = CMSC003.Storage.get('searchDataMap');
				var sat = item.id.split('-')[2];
				console.log(item.parent);
				var folder = item.parent.split('-')[5];
				console.log(folder);
				if (sat == "kompsat") {
					if (list['kompsat-'+item.li_attr.value]) {
						
						// 뎁스 확인해서 날짜 노드 폴더 리스트 입력
						var dateLabel = item.parents[1].split('vido-id-satellite-title-kompsat-')[1];
						
						var dateExist = false;
						var dateIdx = null;
						if(dateLabel) {
							for (var i=0; i<selectData.existing.satellite[0].map.length; i++) {
								if (selectData.existing.satellite[0].map[i].date === dateLabel) {
									dateIdx = i;
									dateExist = true;
									break;
								}
							}
							if (!dateExist) {
								var folderList = {
									'date': dateLabel,
									'folderList': []
								};
								selectData.existing.satellite[0].map.push(folderList);
								dateIdx = selectData.existing.satellite[0].map.length - 1; 
							}
						}
						if(!isNaN(dateIdx) && dateIdx !== null) {
							var check = false;
							for (var i=0; i<selectData.existing.satellite[0].map[dateIdx].folderList.length; i++) {
								if(selectData.existing.satellite[0].map[dateIdx].folderList[i].folderNm === folder) {
									selectData.existing.satellite[0].map[dateIdx].folderList[i].fileList.push(list['kompsat-'+item.li_attr.value]);
									check = true;
									break;
								}
							}
							
							if (!check) {
								var fileList = {
									'folderNm' : folder,
									'fileList' : [list['kompsat-'+item.li_attr.value]]
								};
								selectData.existing.satellite[0].map[dateIdx].folderList.push(fileList);
							}
						}
						
						
// 						for (var i=0; i<selectData.existing.satellite[0].map.length; i++) {
// 							if (selectData.existing.satellite[0].map[i].folder == folder) {
// 								selectData.existing.satellite[0].map[i].fileList.push(list['kompsat-'+item.li_attr.value]);
// 								check = true;
// 								break;
// 							}
// 						}
// 						if (!check) {
// 							selectData.existing.satellite[0].map.push( {"folder": folder, "fileList":[list['kompsat-'+item.li_attr.value]]} );
// 						}
					}
				} else if (sat == "landsat") {
					if (list['landsat-'+item.li_attr.value]) {
						var dateLabel = item.parents[1].split('vido-id-satellite-title-landsat-')[1];
						
						var dateExist = false;
						var dateIdx = null;
						if(dateLabel) {
							for (var i=0; i<selectData.existing.satellite[1].map.length; i++) {
								if (selectData.existing.satellite[1].map[i].date === dateLabel) {
									dateIdx = i;
									dateExist = true;
									break;
								}
							}
							if (!dateExist) {
								var folderList = {
									'date': dateLabel,
									'folderList': []
								};
								selectData.existing.satellite[1].map.push(folderList);
								dateIdx = selectData.existing.satellite[1].map.length - 1; 
							}
						}
						if(!isNaN(dateIdx) && dateIdx !== null) {
							var check = false;
							for (var i=0; i<selectData.existing.satellite[1].map[dateIdx].folderList.length; i++) {
								if(selectData.existing.satellite[1].map[dateIdx].folderList[i].folderNm === folder) {
									selectData.existing.satellite[1].map[dateIdx].folderList[i].fileList.push(list['landsat-'+item.li_attr.value]);
									check = true;
									break;
								}
							}
							
							if (!check) {
								var fileList = {
									'folderNm' : folder,
									'fileList' : [list['landsat-'+item.li_attr.value]]
								};
								selectData.existing.satellite[1].map[dateIdx].folderList.push(fileList);
							}
						}
// 						var check = false;
// 						for (var i=0; i<selectData.existing.satellite[1].map.length; i++) {
// 							if (selectData.existing.satellite[1].map[i].folder == folder) {
// 								selectData.existing.satellite[1].map[i].fileList.push(list['landsat-'+item.li_attr.value]);
// 								check = true;
// 								break;
// 							}
// 						}
// 						if (!check) {
// 							selectData.existing.satellite[1].map.push( {"folder": folder, "fileList":[list['landsat-'+item.li_attr.value]]} );
// 						}
					}
				} else if (sat == "sentinel") {
					if (list['sentinel-'+item.li_attr.value]) {
						var dateLabel = item.parents[1].split('vido-id-satellite-title-sentinel-')[1];
						
						var dateExist = false;
						var dateIdx = null;
						if(dateLabel) {
							for (var i=0; i<selectData.existing.satellite[2].map.length; i++) {
								if (selectData.existing.satellite[2].map[i].date === dateLabel) {
									dateIdx = i;
									dateExist = true;
									break;
								}
							}
							if (!dateExist) {
								var folderList = {
									'date': dateLabel,
									'folderList': []
								};
								selectData.existing.satellite[2].map.push(folderList);
								dateIdx = selectData.existing.satellite[2].map.length - 1; 
							}
						}
						if(!isNaN(dateIdx) && dateIdx !== null) {
							var check = false;
							for (var i=0; i<selectData.existing.satellite[2].map[dateIdx].folderList.length; i++) {
								if(selectData.existing.satellite[2].map[dateIdx].folderList[i].folderNm === folder) {
									//새롬 수정
									//var fileList = list['sentinel-'+item.li_attr.value];

									//for(var cnt = 0; cnt < fileList.length; cnt++){
									//	fileList[cnt].innerFileCoursNm = fileList[cnt].innerFileCoursNm.replaceAll('period','.');
									//	fileList[cnt].fullFileCoursNm = fileList[cnt].fullFileCoursNm.replaceAll('period','.');
									//}
									
									//selectData.existing.satellite[2].map[dateIdx].folderList[i].fileList.push(fileList);
									//console.log("--------------sentinel-3")
									//원본
									selectData.existing.satellite[2].map[dateIdx].folderList[i].fileList.push(list['sentinel-'+item.li_attr.value]);
									check = true;
									break;
								}
							}
							
							if (!check) {
								var fileList = {
									'folderNm' : folder,
									'fileList' : [list['sentinel-'+item.li_attr.value]]
								};
								
								//새롬 수정
								//for(var cnt = 0; cnt < fileList.fileList.length; cnt++){
								//	fileList.fileList[cnt].innerFileCoursNm = fileList.fileList[cnt].innerFileCoursNm.replaceAll('period','.');
								//	fileList.fileList[cnt].fullFileCoursNm = fileList.fileList[cnt].fullFileCoursNm.replaceAll('period','.');
								//}
								//console.log("--------------sentinel-4")
								selectData.existing.satellite[2].map[dateIdx].folderList.push(fileList);
							}
						}
// 						var check = false;
// 						for (var i=0; i<selectData.existing.satellite[2].map.length; i++) {
// 							if (selectData.existing.satellite[2].map[i].folder == folder) {
// 								selectData.existing.satellite[2].map[i].fileList.push(list['sentinel-'+item.li_attr.value]);
// 								check = true;
// 								break;
// 							}
// 						}
// 						if (!check) {
// 							selectData.existing.satellite[2].map.push( {"folder": folder, "fileList":[list['sentinel-'+item.li_attr.value]]} );
// 						}
					}
				} else if (sat == "cas") {
					if (list['cas-'+item.li_attr.value]) {
						var dateLabel = item.parents[1].split('vido-id-satellite-title-cas-')[1];
						
						var dateExist = false;
						var dateIdx = null;
						if(dateLabel) {
							for (var i=0; i<selectData.existing.satellite[3].map.length; i++) {
								if (selectData.existing.satellite[3].map[i].date === dateLabel) {
									dateIdx = i;
									dateExist = true;
									break;
								}
							}
							if (!dateExist) {
								var folderList = {
									'date': dateLabel,
									'folderList': []
								};
								selectData.existing.satellite[3].map.push(folderList);
								dateIdx = selectData.existing.satellite[3].map.length - 1; 
							}
						}
						if(!isNaN(dateIdx) && dateIdx !== null) {
							var check = false;
							for (var i=0; i<selectData.existing.satellite[3].map[dateIdx].folderList.length; i++) {
								if(selectData.existing.satellite[3].map[dateIdx].folderList[i].folderNm === folder) {
									selectData.existing.satellite[3].map[dateIdx].folderList[i].fileList.push(list['cas-'+item.li_attr.value]);
									check = true;
									break;
								}
							}
							
							if (!check) {
								var fileList = {
									'folderNm' : folder,
									'fileList' : [list['cas-'+item.li_attr.value]]
								};
								selectData.existing.satellite[3].map[dateIdx].folderList.push(fileList);
							}
						}
					
// 						var check = false;
// 						for (var i=0; i<selectData.existing.satellite[3].map.length; i++) {
// 							if (selectData.existing.satellite[3].map[i].folder == folder) {
// 								selectData.existing.satellite[3].map[i].fileList.push(list['cas-'+item.li_attr.value]);
// 								check = true;
// 								break;
// 							}
// 						}
// 						if (!check) {
// 							selectData.existing.satellite[3].map.push( {"folder": folder, "fileList":[list['cas-'+item.li_attr.value]]} );
// 						}
					}
				} else if (sat == "sar") {
					if (list['sar-'+item.li_attr.value]) {
						var dateLabel = item.parents[1].split('vido-id-satellite-title-sar-')[1];
						
						var dateExist = false;
						var dateIdx = null;
						if(dateLabel) {
							for (var i=0; i<selectData.existing.satellite[4].map.length; i++) {
								if (selectData.existing.satellite[4].map[i].date === dateLabel) {
									dateIdx = i;
									dateExist = true;
									break;
								}
							}
							if (!dateExist) {
								var folderList = {
									'date': dateLabel,
									'folderList': []
								};
								selectData.existing.satellite[4].map.push(folderList);
								dateIdx = selectData.existing.satellite[4].map.length - 1; 
							}
						}
						if(!isNaN(dateIdx) && dateIdx !== null) {
							var check = false;
							for (var i=0; i<selectData.existing.satellite[4].map[dateIdx].folderList.length; i++) {
								if(selectData.existing.satellite[4].map[dateIdx].folderList[i].folderNm === folder) {
									selectData.existing.satellite[4].map[dateIdx].folderList[i].fileList.push(list['sar-'+item.li_attr.value]);
									check = true;
									break;
								}
							}
							
							if (!check) {
								var fileList = {
									'folderNm' : folder,
									'fileList' : [list['sar-'+item.li_attr.value]]
								};
								selectData.existing.satellite[4].map[dateIdx].folderList.push(fileList);
							}
						}
					
// 						var check = false;
// 						for (var i=0; i<selectData.existing.satellite[3].map.length; i++) {
// 							if (selectData.existing.satellite[3].map[i].folder == folder) {
// 								selectData.existing.satellite[3].map[i].fileList.push(list['cas-'+item.li_attr.value]);
// 								check = true;
// 								break;
// 							}
// 						}
// 						if (!check) {
// 							selectData.existing.satellite[3].map.push( {"folder": folder, "fileList":[list['cas-'+item.li_attr.value]]} );
// 						}
					}
				}
				
			});
			
			// 항공영상 데이터
			aerial.forEach(function(item) {
				var list = CMSC003.Storage.get('searchDataMap');
				var year = item.parent.split('-')[4];
				var dpi = item.parent.split('-')[5];
				
				var year_check = false;
				var dpi_check = false;
				
				var year_index = 0;
				var dpi_index = 0;
				
				// year -- 현재 영상의 year값에 해당되는 객체의 index를 추출함(없으면 생성해줌)
				for (var i=0; i<selectData.existing.airOrientalMap.length; i++) {
					var cur_year = selectData.existing.airOrientalMap[i].year;
					if (cur_year == year) {
						year_check = true;
						year_index = i; // selectData에서 현재 영상 year에 해당하는 index
						break;
					}
				}
				if (!year_check) { // selectData에 현재 영상 year에 해당하는 객체가 없으면 생성해줌
					selectData.existing.airOrientalMap.push( {"year": year, "maps": []} );
					year_index = selectData.existing.airOrientalMap.length-1; 
				}
				
				// dpi -- 추출한 year_index에서 현재 영상의 dpi값에 해당하는 객체가 있으면 데이터를 추가하고 없으면 객체 생성 후 데이터 추가함
				for (var i=0; i<selectData.existing.airOrientalMap[year_index].maps.length; i++) {
					var cur_dpi = selectData.existing.airOrientalMap[year_index].maps[i].dpi;
					if (cur_dpi == dpi) { // 현재 영상의 year와 dpi를 갖는 객체일때 
						selectData.existing.airOrientalMap[year_index].maps[i].map.push(list['aerial-'+item.li_attr.value]);
						dpi_check = true;
						break;
					}
				}
				if (!dpi_check) { // selectData에 현재 영상 year값의 객체는 있지만 dpi가 없을 경우 추가해줌
					selectData.existing.airOrientalMap[year_index].maps.push( {"dpi": dpi, "map": [list['aerial-'+item.li_attr.value]]} );
				}
			});
			
			// 정사영상 데이터
			ortho.forEach(function(item) {
				var list = CMSC003.Storage.get('searchDataMap');
				var year = item.parent.split('-')[4];
				var dpi = item.parent.split('-')[5];
				
				var year_check = false;
				var dpi_check = false;
				
				var year_index = 0;
				
				// year
				for (var i=0; i<selectData.existing.ortOrientalMap.length; i++) {
					var cur_year = selectData.existing.ortOrientalMap[i].year;
					if (cur_year == year) {
						year_check = true;
						year_index = i;
						break;
					}
				}
				if (!year_check) {
					selectData.existing.ortOrientalMap.push( {"year": year, "maps": []} );
					year_index = selectData.existing.ortOrientalMap.length-1; 
				}
				
				// dpi
				for (var i=0; i<selectData.existing.ortOrientalMap[year_index].maps.length; i++) {
					var cur_dpi = selectData.existing.ortOrientalMap[year_index].maps[i].dpi;
					if (cur_dpi == dpi) {
						selectData.existing.ortOrientalMap[year_index].maps[i].map.push(list['ortho-'+item.li_attr.value]);
						dpi_check = true;
						break;
					}
				}
				if (!dpi_check) {
					selectData.existing.ortOrientalMap[year_index].maps.push( {"dpi": dpi, "map": [list['ortho-'+item.li_attr.value]]} );
				}
			});
			
			// 인구통계 데이터
			// 지가통계 데이터
			// 건물통계 데이터
			graphics.forEach(function(item) {
				var list = CMSC003.Storage.get('searchDataMap');
				if (list[item.li_attr.value]) {
					selectData.existing.graphicsMap.push(list[item.li_attr.value]);					
				}
			});
			
			// dem 데이터
			dem.forEach(function(item) {
				var list = CMSC003.Storage.get('searchDataMap');
				var year = item.parent.split('-')[4];
				var dpi = item.parent.split('-')[5];
				
				var year_check = false;
				var dpi_check = false;
				
				var year_index = 0;
				
				// year
				for (var i=0; i<selectData.existing.demMap.length; i++) {
					var cur_year = selectData.existing.demMap[i].year;
					if (cur_year == year) {
						year_check = true;
						year_index = i;
						break;
					}
				}
				if (!year_check) {
					selectData.existing.demMap.push( {"year": year, "maps": []} );
					year_index = selectData.existing.demMap.length-1; 
				}
				
				// dpi
				for (var i=0; i<selectData.existing.demMap[year_index].maps.length; i++) {
					var cur_dpi = selectData.existing.demMap[year_index].maps[i].dpi;
					if (cur_dpi == dpi) {
						selectData.existing.demMap[year_index].maps[i].map.push(list['dem-'+item.li_attr.value]);
						dpi_check = true;
						break;
					}
				}
				if (!dpi_check) {
					selectData.existing.demMap[year_index].maps.push( {"dpi": dpi, "map": [list['dem-'+item.li_attr.value]]} );
				}
			});
			
			// 긴급 데이터
			// 침수
			flood.forEach(function(item) {
				var list = CMSC003.Storage.get('searchDataMap');
				if (list['flood-'+item.li_attr.value]) {
					selectData.disaster.Flood.push(list['flood-'+item.li_attr.value]);					
				}
			});
			// 산사태
			landslip.forEach(function(item) {
				var list = CMSC003.Storage.get('searchDataMap');
				if (list['landslide-'+item.li_attr.value]) {
					selectData.disaster.Landslip.push(list['landslide-'+item.li_attr.value]);
				}
			});
			// 산불
			forestFire.forEach(function(item) {
				var list = CMSC003.Storage.get('searchDataMap');
				if (list['forestFire-'+item.li_attr.value]) {
					selectData.disaster.ForestFire.push(list['forestFire-'+item.li_attr.value]);					
				}
			});
			// 지진
			earthquake.forEach(function(item) {
				var list = CMSC003.Storage.get('searchDataMap');
				if (list['earthquake-'+item.li_attr.value]) {
					selectData.disaster.Earthquake.push(list['earthquake-'+item.li_attr.value]);					
				}				
			});
			// 해양재난
			maritime.forEach(function(item) {
				var list = CMSC003.Storage.get('searchDataMap');
				if (list['maritime-'+item.li_attr.value]) {
					selectData.disaster.MaritmeDisaster.push(list['maritime-'+item.li_attr.value]);					
				}
			});
			// 적조
			redTide.forEach(function(item) {
				var list = CMSC003.Storage.get('searchDataMap');
				if (list['redTide-'+item.li_attr.value]) {
					selectData.disaster.RedTide.push(list['redTide-'+item.li_attr.value]);					
				}
			});
			
			// 분석 벡터 객체추출
			vectorAnalObj.forEach(function(item) {
				var list = CMSC003.Storage.get('searchDataMap');
				if (list['vectorAnalObj-'+item.li_attr.value]) {
					selectData.analysis.objectExt.vector.push(list['vectorAnalObj-'+item.li_attr.value]);					
				}
			});
			
			// 분석 래스터 객체추출
			rasterAnalObj.forEach(function(item) {
				var list = CMSC003.Storage.get('searchDataMap');
				if (list['rasterAnalObj-'+item.li_attr.value]) {
					selectData.analysis.objectExt.raster.push(list['rasterAnalObj-'+item.li_attr.value]);					
				}
			});
			
			// 분석 벡터 변화탐지
			vectorAnalChg.forEach(function(item) {
				var list = CMSC003.Storage.get('searchDataMap');
				if (list['vectorAnalChg-'+item.li_attr.value]) {
					selectData.analysis.changeDet.vector.push(list['vectorAnalChg-'+item.li_attr.value]);
				}
			});
			
			// 분석 래스터 변화탐지
			rasterAnalChg.forEach(function(item) {
				var list = CMSC003.Storage.get('searchDataMap');
				if (list['rasterAnalChg-'+item.li_attr.value]) {
					selectData.analysis.changeDet.raster.push(list['rasterAnalChg-'+item.li_attr.value]);					
				}
			});
			
			console.log(selectData);
// 			console.log(JSON.stringify(selectData));
		
			if(
					!selectData.disaster.ForestFire.length && 
					!selectData.disaster.Landslip.length &&
					!selectData.disaster.Flood.length &&
					!selectData.disaster.Earthquake.length &&
					!selectData.disaster.RedTide.length &&
					!selectData.existing.digitalMap.length &&
					!selectData.existing.demMap.length &&
					!selectData.existing.airOrientalMap.length &&
					!selectData.existing.ortOrientalMap.length &&
					!selectData.existing.graphicsMap.length &&
					!selectData.existing.satellite[0].map.length &&
					!selectData.existing.satellite[1].map.length &&
					!selectData.existing.satellite[2].map.length &&
					!selectData.existing.satellite[3].map.length &&
					!selectData.existing.satellite[4].map.length &&
					!selectData.analysis.objectExt.vector.length &&
					!selectData.analysis.objectExt.raster.length &&
					!selectData.analysis.changeDet.vector.length &&
					!selectData.analysis.changeDet.raster.length
			) {
				var disasterMsg = type == 'create' ? '저장할 공간정보를 선택해주세요.' : '생성할 공간정보를 선택해주세요.';
				CMSC003.DOM.showErrorMsg(disasterMsg);
				return;
			}
			
			// ajax로 json 보낼 경우 인식을 못하는 경우가 생김
			var url = type == "create" ? "cmsc003saveData.do" : "cmsc003createData2.do";
			console.log(type + ':' + url);
			
			CMSC003.DOM.showSpinner(true);
			var xhr = new XMLHttpRequest();
			xhr.open("POST", url);
			
			xhr.setRequestHeader("Accept", "application/json");
			xhr.setRequestHeader("Content-Type", "application/json");
			xhr.responseType='json';
			
			xhr.onreadystatechange = function () {
			if (xhr.readyState === 4) {
				CMSC003.DOM.showSpinner(false);
				
				const copyROI = [extent5179[0], extent5179[1], extent5179[2], extent5179[3]];
				CMSC003.Storage.set('requestROIExtent', copyROI);
			
// 				CMSC003.DOM.showRequestResultTree(xhr.response);
				//CMSC003.DOM.showRequestResultTree(selectData);
				
				var successMsg = type == 'create' ? '재난지역 영상 저장을 완료하였습니다.' : '긴급공간정보 생성을 완료하였습니다.';
				CMSC003.DOM.showErrorMsg(successMsg);
				
			}};
			xhr.addEventListener('error', function() {
				CMSC003.DOM.showErrorMsg("오류가 발생하였습니다.");
			});
			console.log(JSON.stringify(selectData), 'createData');
			xhr.send(JSON.stringify(selectData)); 
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
// 						$(sido).trigger('change');
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
						CMSC003.GIS.setMapCenter(transform);
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
					console.log("code:" + request.status + "\n" + "message:"
							+ request.respnseText + "\n" + "error:" + error)
// 					alert("code:" + request.status + "\n" + "message:"
// 							+ request.respnseText + "\n" + "error:" + error);
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
						CMSC003.GIS.setMapCenter(transform);	
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
					console.log("code:" + request.status + "\n" + "message:"
							+ request.respnseText + "\n" + "error:" + error)
// 					alert("code:" + request.status + "\n" + "message:"
// 							+ request.respnseText + "\n" + "error:" + error);
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
					CMSC003.GIS.setMapCenter(transform);
				},
				error: function(request, status, error) {
					alert("code:" + request.status + "\n" + "message:"
							+ request.respnseText + "\n" + "error:" + error);
				}
			})
		}
		
		/*재난유형 출력*/
		function MSFRTN_TY_CD(){
			//console.log("재난 유형", ty)
			$("select[id='selectDisasterType'] option").not("[value='AllDisaster']").remove();
		    var selectEl = document.getElementById("selectDisasterType"); 
		    
			var param = "opt="+"type"
			$.ajax({
				type : "PUT",
// 			    url: "/col_requst.do",
				url: "/ugis/col_requst.do",
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
		
		// 테스트_230417
		function popup_test() {
			window.open("http://localhost:8086/main/main.do?state=1","_blank");
		}
	</script>


</body>
<!-- ================================================
CMSC003 Lib CSS
================================================ -->
<link rel="stylesheet"
	href="js/cm/cmsc003/lib/bootstrap-datepicker-1.9.0-dist/css/bootstrap-datepicker.min.css" />
<link rel="stylesheet" href="js/cm/cmsc003/css/cmsc003.css" />
</html>