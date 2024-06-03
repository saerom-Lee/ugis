<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<html lang="ko" data-dark="false">
<head>
<meta charset="utf-8">
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
</head>

<jsp:include page="./ctsc_header005.jsp"></jsp:include>

</head>

<body>

<!-- START TOP -->
<%--<jsp:include page="./ctsc_top.jsp"></jsp:include>--%>
<%@ include file="../topMenu.jsp"%>
 END TOP -->

<!-- //////////////////////////////////////////////////////////////////////////// --> 
<!-- START SIDEBAR -->
<div class="sidebar clearfix">
	<div role="tabpanel" class="sidepanel" style="display: block;">

		<!-- Tab panes -->
		<div class="tab-content">
			<div role="tabpanel" class="tab-pane active" id="today">

				<div class="sidepanel-m-title">
				대상영상 검색
				</div>
				<div class="panel-body">
					<div class="form-group m-t-10">
						<label for="input002" class="col-sm-12 control-label form-label">키워드</label>
						<div class="col-sm-12">
							<div class="col-sm-4">
								<label class="ckbox-container">긴급영상
									<input type="checkbox" checked="checked" name="emer"><!-- svn 테스트 -->
									<span class="checkmark"></span>
								</label>
							</div>
							<div class="col-sm-4">
								<label class="ckbox-container">국토위성
									<input type="checkbox" checked="checked" name="satell">
									<span class="checkmark"></span>
								</label>
							</div>
							<div class="col-sm-4">
								<label class="ckbox-container">무료영상
									<input type="checkbox" checked="checked" name="free">
									<span class="checkmark"></span>
								</label>
							</div>
						</div>
					</div>		
					<div class="form-group m-t-10">
						<label for="input002" class="col-sm-3 control-label form-label">조회기간</label>
						<div class="col-sm-4">
							<input type="text" id="datepicker" class="form-control">
						</div>
						<div class="col-sm-1"><span class="txt-in-form">~<span></div>
						<div class="col-sm-4">
							<input type="text" id="datepicker2" class="form-control">
						</div>
					</div>
					<script src="/js/datepicker.js"></script>
					<script>
						let datepicker = new DatePicker(document.getElementById('datepicker'));
						let datepicker2 = new DatePicker(document.getElementById('datepicker2'));
					</script>
					<div class="form-group m-t-10">
						<label class="col-sm-12 control-label form-label">위치</label>
						<div class="col-sm-6">
							<select class="form-basic">
								<option>광역시도</option>
							</select>
						</div>
						<div class="col-sm-6">
							<select class="form-basic">
								<option>시군구</option>
							</select>
						</div>
					</div>
					<div class="btn-wrap a-cent">
						<a href="#" class="btn btn-default" id="staticSearch"><i class="fas fa-search m-r-5"></i>검색</a>
					</div>

				</div>
			</div>

			<div role="tabpanel" class="tab-pane" id="tasks">
			</div>
		</div>
    <!-- End Tasks -->
    </div>
    <!-- End Chat -->

  </div>

</div>

    <a href="#" class="sidebar-open-button"><i class="fa fa-bars"></i></a>
    <a href="#" class="sidebar-open-button-mobile"><i class="fa fa-bars"></i></a>

</div>

<!-- START CONTENT -->
<div class="content">

	<div class="page-header">
		<h1 class="title">활용내역 통계</h1>

	</div>
	<div class="container-widget">
		<!-- 연합뉴스 -->
		<div class="col-lg-12">
			<div class="panel panel-default">
				<div class="panel-title">
					<i class="fas fa-chart-line"></i>연도별 통계
				</div>
				<div class="panel-body">		
		
					<!-- amCharts javascript sources -->
					<script type="text/javascript" src="/js/amcharts.js"></script>
					<script type="text/javascript" src="/js/serial.js"></script>
					

					<!-- amCharts javascript code -->
					<script type="text/javascript">
						AmCharts.makeChart("chartdiv",
							{
								"type": "serial",
								"categoryField": "category",
								"startDuration": 1,
								"categoryAxis": {
									"gridPosition": "start"
								},
								"trendLines": [],
								"graphs": [
									{
										"balloonText": "[[title]] of [[category]]:[[value]]",
										"bullet": "round",
										"id": "AmGraph-1",
										"title": "graph 1",
										"valueField": "column-1"
									},
									{
										"balloonText": "[[title]] of [[category]]:[[value]]",
										"bullet": "square",
										"id": "AmGraph-2",
										"title": "graph 2",
										"valueField": "column-2"
									}
								],
								"guides": [],
								"valueAxes": [
									{
										"id": "ValueAxis-1",
										"title": "Axis title"
									}
								],
								"allLabels": [],
								"balloon": {},
								"legend": {
									"enabled": true,
									"useGraphSettings": true
								},
								"titles": [
									{
										"id": "Title-1",
										"size": 15,
										"text": "Chart Title"
									}
								],
								"dataProvider": [
									{
										"category": "2012",
										"column-1": "25"
									},
									{
										"category": "2013",
										"column-1": "25"
									},
									{
										"category": "2014",
										"column-1": "25"
									},
									{
										"category": "2015",
										"column-1": "25"
									},
									{
										"category": "2016",
										"column-1": "25"
									},
									{
										"category": "2017",
										"column-1": "25"
									},
									{
										"category": "2018",
										"column-1": "100"
									},
									{
										"category": "2019",
										"column-1": "150"
									},
									{
										"category": "2020",
										"column-1": "200"
									},
									{
										"category": "2021",
										"column-1": "180"
									}
								]
							}
						);
					</script>
					<div id="chartdiv" style="width: 100%; height: 400px; background-color: #FFFFFF;" ></div>
				</div>
			</div>
		</div>
		
		<div class="col-md-6">
			<div class="panel panel-default">
				<div class="panel-title">
					<i class="fas fa-chart-pie"></i>지역별 통계
				</div>
				<div class="panel-body">

					<!-- amCharts javascript sources -->
					<script type="text/javascript" src="/js/amcharts.js"></script>
					<script type="text/javascript" src="/js/pie.js"></script>
					<script type="text/javascript" src="/js/chalk.js"></script>
					

					<!-- amCharts javascript code -->
					<script type="text/javascript">
						AmCharts.makeChart("chartdiv2",
							{
								"type": "serial",
								"categoryField": "category",
								"startDuration": 1,
								"categoryAxis": {
									"gridPosition": "start"
								},
								"trendLines": [],
								"graphs": [
									{
										"balloonText": "[[title]] of [[category]]:[[value]]",
										"fillAlphas": 1,
										"id": "AmGraph-1",
										"title": "graph 1",
										"type": "column",
										"valueField": "column-1"
									},
									{
										"balloonText": "[[title]] of [[category]]:[[value]]",
										"fillAlphas": 1,
										"id": "AmGraph-2",
										"title": "graph 2",
										"type": "column",
										"valueField": "column-2"
									}
								],
								"guides": [],
								"valueAxes": [
									{
										"id": "ValueAxis-1",
										"title": "Axis title"
									}
								],
								"allLabels": [],
								"balloon": {},
								"legend": {
									"enabled": true,
									"useGraphSettings": true
								},
								"titles": [
									{
										"id": "Title-1",
										"size": 15,
										"text": "Chart Title"
									}
								],
								"dataProvider": [
									{
										"category": "서울시",
										"column-1": 8,
										"column-2": 5,
										"column-3": 7
									},
									{
										"category": "대구시",
										"column-1": 6,
										"column-2": 7,
										"column-3": 7
									},
									{
										"category": "대전시",
										"column-1": 2,
										"column-2": 3,
										"column-3": 1
									},
									{
										"category": "부산시",
										"column-1": "3",
										"column-2": "5",
										"column-3": 7
									},
									{
										"category": "인천시",
										"column-1": "5",
										"column-2": "9",
										"column-3": 10
									},
									{
										"category": "광주시",
										"column-1": "2",
										"column-2": "1",
										"column-3": "2"
									},
									{
										"category": "울산시",
										"column-1": "3",
										"column-2": "8",
										"column-3": "8"
									}
								]
							}
						);
					</script>
					<div id="chartdiv2" style="width: 100%; height: 400px; background-color: #FFFFFF;" ></div>
				</div>
			</div>
		</div>

		<div class="col-md-6">
			<div class="panel panel-default">
				<div class="panel-title">
					<i class="fas fa-globe-asia"></i>영상 종류별 통계
				</div>
				<div class="panel-body">


					<!-- amCharts javascript sources -->
					<script type="text/javascript" src="/js/amcharts.js"></script>
					<script type="text/javascript" src="/js/pie.js"></script>
					<script type="text/javascript" src="/js/chalk.js"></script>
					

					<!-- amCharts javascript code -->
					<script type="text/javascript">
						AmCharts.makeChart("chartdiv3",
							{
								"type": "pie",
								"balloonText": "[[title]]<br><span style='font-size:14px'><b>[[value]]</b> ([[percents]]%)</span>",
								"colors": [
									"#226FCB",
									"#B2FE96",
									"#00B4FF",
									"#F57806",
									"#D1F0FF",
									"#00CCCC"
								],
								"labelColorField": "#FFFFFF",
								"outlineAlpha": 0,
								"outlineThickness": 0,
								"titleField": "country",
								"valueField": "litres",
								"color": "#666666",
								"fontFamily": "",
								"fontSize": 12,
								"handDrawn": false,
								"handDrawScatter": 1,
								"theme": "chalk",
								"allLabels": [],
								"balloon": {},
								"titles": [],
								"dataProvider": [
									{
										"country": "화재",
										"litres": "356.9"
									},
									{
										"country": "붕괴사고",
										"litres": 131.1
									},
									{
										"country": "폭발사고",
										"litres": 115.8
									},
									{
										"country": "도로교통사고",
										"litres": 109.9
									},
									{
										"country": "환경오염사고",
										"litres": 108.3
									},
									{
										"country": "해양사고",
										"litres": 65
									}
								]
							}
						);
					</script>
					<div id="chartdiv3" style="width: 100%; height: 400px; background-color: #fff;" ></div>
				</div>

			</div>
		</div>

	</div>
</div>
<script src="/js/ct/ctsc005.js"></script>
<script>
$(document).ready(function() {
	$('#chartdiv').empty();
	ctsc005.yearStatic();
	ctsc005.kindsStatic();
});
</script>
</body>
</html>