<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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

<!-- ========== Css Files ========== -->
<link href="css/root.css" rel="stylesheet">

<link rel="stylesheet" href="https://pro.fontawesome.com/releases/v5.10.0/css/all.css" integrity="sha384-AYmEC3Yw5cVb3ZcuHtOA93w35dYTsvhLPVnYs9eStHfGJvOvKxVfELGroGkvsg+p" crossorigin="anonymous"/>
<script type="text/javascript">
$(document).ready(function() {
	
$("ul#topnav li").hover(function() { //Hover over event on list item
	$(this).css({ 'background' : '#1376c9 url(topnav_active.gif) repeat-x'}); //Add background color + image on hovered list item
	$(this).find("span").show(); //Show the subnav
} , function() { //on hover out...
	$(this).css({ 'background' : 'none'}); //Ditch the background
	$(this).find("span").hide(); //Hide the subnav
});
	
});
</script>
</head>

<body>

<!-- START TOP -->

<%@ include file="../topMenu.jsp"%>

<div class="sidebar clearfix">
	<div role="tabpanel" class="sidepanel" style="display: block;">

		<!-- Nav tabs -->
		<ul class="nav nav-tabs" role="tablist">
			<li role="presentation" class="active" style="width:33.333%">
				<a href="#tab-01" aria-controls="tab-01" role="tab" data-toggle="tab" aria-expanded="true" class="active">
					<i class="fas fa-satellite-dish"></i><br>무료위성영상
				</a>
			</li>
			<li role="presentation" class="" style="width:33.333%">
				<a href="#tab-02" aria-controls="tab-02" role="tab" data-toggle="tab" class="" aria-expanded="false">
					<i class="fas fa-camera"></i><br>긴급촬영영상
				</a>
			</li>
			<li role="presentation" class="" style="width:33.333%">
				<a href="#tab-03" aria-controls="tab-03" role="tab" data-toggle="tab" class="" aria-expanded="false">
					<i class="fas fa-satellite"></i><br>국토위성영상
				</a>
			</li>
		</ul>

		<!-- Tab panes -->
		<div class="tab-content">

			<!-- Start tab-01 -->
			<div role="tabpanel" class="tab-pane active" id="tab-01">

				<div class="sidepanel-m-title">
				무료위성영상 자동다운로드 설정
				</div>
				<div class="panel-body">			
					<form class="">
					<div class="form-group m-t-10">
						<label class="col-sm-3 control-label form-label">위성 종류</label>
						<div class="col-sm-9">
							<select class="form-basic">
								<option>전체</option>
							</select>            
						</div>
					</div> 
					<div class="form-group m-t-10">
						<label for="input002" class="col-sm-12 control-label form-label">촬영기간</label>
						<div class="col-sm-12">
							<div class="col-sm-4">
								<input type="text" class="form-control" id="input002">
							</div>
							<div class="col-sm-2">
								<button type="button" class="btn btn-default btn-icon"><i class="fas fa-calendar-alt"></i></button>
							</div>
							<div class="col-sm-4">
								<input type="text" class="form-control" id="input002">
							</div>
							<div class="col-sm-2">
								<button type="button" class="btn btn-default btn-icon"><i class="fas fa-calendar-alt"></i></button>
							</div>
						</div>
					</div>
					<div class="form-group m-t-10">
						<label for="input002" class="col-sm-3 control-label form-label">대상지역</label>
						<div class="col-sm-9">
							<div class="col-sm-2 in-tit">
								ULX
							</div>
							<div class="col-sm-4">
								<input type="text" class="form-control" id="input002">
							</div>
							<div class="col-sm-2 in-tit">
								ULY
							</div>
							<div class="col-sm-4">
								<input type="text" class="form-control" id="input002">
							</div>
							<div class="col-sm-2 in-tit m-t-5">
								LRX
							</div>
							<div class="col-sm-4 m-t-5">
								<input type="text" class="form-control" id="input002">
							</div>
							<div class="col-sm-2 in-tit m-t-5">
								LRY
							</div>
							<div class="col-sm-4 m-t-5">
								<input type="text" class="form-control" id="input002">
							</div>
						</div>
					</div>
					<div class="btn-wrap a-cent">
						<a href="#" class="btn btn-info"><i class="fas fa-mouse-pointer m-r-5"></i>사용자 정의 ROI</a>
					</div>
					<div class="form-group m-t-10">
						<label for="input002" class="col-sm-4 control-label form-label">외부파일위치</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="input002" value="D:\Users\JHJ\Videos">
						</div>
						<div class="col-sm-2">
							<button type="button" class="btn btn-default btn-icon"><i class="fas fa-ellipsis-h"></i></button>
						</div>
					</div>
					<div class="form-group m-t-10">
						<label for="input002" class="col-sm-4 control-label form-label">영상저장위치</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="input002" value="D:\Users\JHJ\Videos">
						</div>
						<div class="col-sm-2">
							<button type="button" class="btn btn-default btn-icon"><i class="fas fa-ellipsis-h"></i></button>
						</div>
					</div>
					<div class="form-group m-t-10">
						<label for="input002" class="col-sm-4 control-label form-label">시간설정</label>
						<div class="col-sm-8">
							<input type="text" class="form-control" id="input002" value="AM : 01:30">
						</div>
					</div>
					<div class="btn-wrap a-cent">
						<a href="#" class="btn btn-default"><i class="fas fa-search m-r-5"></i>자동 다운로드</a>
					</div>
					</form>
				</div>


			</div>
			<div role="tabpanel" class="tab-pane" id="tab-02">

				<div class="sidepanel-m-title">
					긴급 촬영영상 검색
				</div>
				<div class="panel-body">			
					<form class="">
					<div class="form-group m-t-10">
						<label class="col-sm-3 control-label form-label">영상명</label>
						<div class="col-sm-9">
							<select class="form-basic">
								<option>전체</option>
							</select>            
						</div>
					</div> 
					<div class="form-group m-t-10">
						<label for="input002" class="col-sm-12 control-label form-label">촬영기간</label>
						<div class="col-sm-12">
							<div class="col-sm-4">
								<input type="text" class="form-control" id="input002">
							</div>
							<div class="col-sm-2">
								<button type="button" class="btn btn-default btn-icon"><i class="fas fa-calendar-alt"></i></button>
							</div>
							<div class="col-sm-4">
								<input type="text" class="form-control" id="input002">
							</div>
							<div class="col-sm-2">
								<button type="button" class="btn btn-default btn-icon"><i class="fas fa-calendar-alt"></i></button>
							</div>
						</div>
					</div>
					<div class="btn-wrap a-cent">
						<a href="#" class="btn btn-default"><i class="fas fa-search m-r-5"></i>검색</a>
						<a href="" class="btn btn-light" data-toggle="modal" data-target="#myModal"><i class="fas fa-plus-square m-r-5"></i>등록</a>
					</div>
					<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-hidden="true">
						<div class="modal-dialog modal-sm">
							<div class="modal-content">
								<div class="modal-header">
									<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
									<h4 class="modal-title">촬영영상 등록</h4>
								</div>
								<div class="modal-body">
									<div class="panel-body">					
										<form class="">
										<div class="form-group m-t-10">
											<label class="col-sm-4 control-label form-label">영상명</label>
											<div class="col-sm-8">
												<input type="text" class="form-control" id="">
											</div>
										</div>
										<div class="form-group m-t-10">
											<label class="col-sm-4 control-label form-label">취득일</label>
											<div class="col-sm-6">
												<input type="text" class="form-control" id="">
											</div>
											<div class="col-sm-2">
												<button type="button" class="btn btn-default btn-icon"><i class="fas fa-calendar-alt"></i></button>
											</div>
										</div>
										<div class="form-group m-t-10">
											<label class="col-sm-4 control-label form-label">영상구분</label>
											<div class="col-sm-8">
												<select class="form-basic">
													<option>전체</option>
												</select>            
											</div>
										</div>
										<div class="form-group m-t-10">
											<label for="input002" class="col-sm-4 control-label form-label">입력영상위치</label>
											<div class="col-sm-6">
												<input type="text" class="form-control" id="input002" value="D:\Users\JHJ\Videos">
											</div>
											<div class="col-sm-2">
												<button type="button" class="btn btn-default btn-icon"><i class="fas fa-ellipsis-h"></i></button>
											</div>
										</div>
										<div class="form-group m-t-10">
											<label for="input002" class="col-sm-4 control-label form-label">영상저장위치</label>
											<div class="col-sm-6">
												<input type="text" class="form-control" id="input002" value="D:\Users\JHJ\Videos">
											</div>
											<div class="col-sm-2">
												<button type="button" class="btn btn-default btn-icon"><i class="fas fa-ellipsis-h"></i></button>
											</div>
										</div>
										</form> 

									</div>
								</div>
								<div class="modal-footer">
									<button type="button" class="btn btn-default"><i class="fas fa-plus-square m-r-5"></i>등록</button>
									<button type="button" class="btn btn-gray" data-dismiss="modal" aria-label="Close"><i class="fas fa-times m-r-5"></i>취소</button>
								</div>
							</div>
						</div>
					</div>
					<div class="sidepanel-s-title m-t-30">
					검색 결과
					</div>
					<div class="panel-body">
						<div class="col-lg-12">
							<div class="panel panel-default">
								<ul class="result-list">
									<li>영상명 : 긴급 재난 지역 촬영 2018</li>
									<li>취득일 : 2018/01/02</li>
									<li>영상구분 : 드론영상</li>
								</ul>
								<ul class="result-list">
									<li>영상명 : 태풍 피해 재난 지역 2019-07</li>
									<li>취득일 : 2019/07/02</li>
									<li>영상구분 : 국토위성</li>
								</ul>

							</div>
						</div>
					</div>
					</form>
				</div>


			</div>
			<div role="tabpanel" class="tab-pane" id="tab-03">

				<div class="sidepanel-m-title">
				 국토위성영상 검색
				</div>
				<div class="panel-body">			
					<form class="">
					<div class="form-group m-t-10">
						<label class="col-sm-4 control-label form-label">영상정보 종류</label>
						<div class="col-sm-8">
							<select class="form-basic">
								<option>전체</option>
							</select>            
						</div>
					</div> 
					<div class="form-group m-t-10">
						<label for="input002" class="col-sm-12 control-label form-label">촬영기간</label>
						<div class="col-sm-12">
							<div class="col-sm-4">
								<input type="text" class="form-control" id="input002">
							</div>
							<div class="col-sm-2">
								<button type="button" class="btn btn-default btn-icon"><i class="fas fa-calendar-alt"></i></button>
							</div>
							<div class="col-sm-4">
								<input type="text" class="form-control" id="input002">
							</div>
							<div class="col-sm-2">
								<button type="button" class="btn btn-default btn-icon"><i class="fas fa-calendar-alt"></i></button>
							</div>
						</div>
					</div>
					<div class="form-group m-t-10">
						<label class="col-sm-12 control-label form-label">재난지역</label>
						<div class="col-sm-4">
							<select class="form-basic">
								<option></option>
							</select>            
						</div>
						<div class="col-sm-4">
							<select class="form-basic">
								<option></option>
							</select>            
						</div>
						<div class="col-sm-4">
							<select class="form-basic">
								<option></option>
							</select>            
						</div>
					</div>
					<div class="form-group m-t-10">
						<label for="input002" class="col-sm-3 control-label form-label">좌표</label>
						<div class="col-sm-9">
							<div class="col-sm-2 in-tit">
								ULX
							</div>
							<div class="col-sm-4">
								<input type="text" class="form-control" id="input002">
							</div>
							<div class="col-sm-2 in-tit">
								ULY
							</div>
							<div class="col-sm-4">
								<input type="text" class="form-control" id="input002">
							</div>
							<div class="col-sm-2 in-tit m-t-5">
								LRX
							</div>
							<div class="col-sm-4 m-t-5">
								<input type="text" class="form-control" id="input002">
							</div>
							<div class="col-sm-2 in-tit m-t-5">
								LRY
							</div>
							<div class="col-sm-4 m-t-5">
								<input type="text" class="form-control" id="input002">
							</div>
						</div>
					</div>
					<div class="btn-wrap a-cent">
						<a href="#" class="btn btn-info"><i class="fas fa-mouse-pointer m-r-5"></i>사용자 정의 ROI</a>
						<a href="#" class="btn btn-default"><i class="fas fa-search m-r-5"></i>검색</a>
					</div>
					<div class="sidepanel-s-title m-t-30">
					검색 결과
					</div>
					<div class="panel-body">
						<div class="col-lg-12">
							<div class="panel panel-default">
								<ul class="result-list">
									<li>종류 : 국토위성영상</li>
									<li>촬영일 : 2018/01/02</li>
									<li class="btn-list-wrap"><a href="#" class="btn btn-light" data-toggle="modal" data-target="#myModal2"><i class="fas fa-download"></i></a></li>
								</ul>
								<ul class="result-list">
									<li>영상명 : 국토위성영상</li>
									<li>촬영일 : 2019/07/02</li>
									<li class="btn-list-wrap"><a href="#" class="btn btn-light"><i class="fas fa-download"></i></a></li>
								</ul>

							</div>
						</div>
					</div>
					<div class="modal fade" id="myModal2" tabindex="-1" role="dialog" aria-hidden="true">
						<div class="modal-dialog modal-sm">
							<div class="modal-content">
								<div class="modal-header">
									<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
									<h4 class="modal-title">다운로드 설정</h4>
								</div>
								<div class="modal-body">
									<div class="panel-body">					
										<form class="">
										<div class="form-group m-t-10">
											<label for="input002" class="col-sm-4 control-label form-label">입력파일위치</label>
											<div class="col-sm-6">
												<input type="text" class="form-control" id="input002" value="D:\Users\JHJ\Videos">
											</div>
											<div class="col-sm-2">
												<button type="button" class="btn btn-default btn-icon"><i class="fas fa-ellipsis-h"></i></button>
											</div>
										</div>
										<div class="form-group m-t-10">
											<label for="input002" class="col-sm-4 control-label form-label">영상저장위치</label>
											<div class="col-sm-6">
												<input type="text" class="form-control" id="input002" value="D:\Users\JHJ\Videos">
											</div>
											<div class="col-sm-2">
												<button type="button" class="btn btn-default btn-icon"><i class="fas fa-ellipsis-h"></i></button>
											</div>
										</div>
										</form> 

									</div>
								</div>
								<div class="modal-footer">
									<button type="button" class="btn btn-default"><i class="fas fa-plus-square m-r-5"></i>등록</button>
									<button type="button" class="btn btn-gray" data-dismiss="modal" aria-label="Close"><i class="fas fa-times m-r-5"></i>취소</button>
								</div>
							</div>
						</div>
					</div>
					</form>
				</div>


			</div>
		</div>
	</div>
</div>

<a href="#" class="sidebar-open-button"><i class="fa fa-bars"></i></a>
<a href="#" class="sidebar-open-button-mobile"><i class="fa fa-bars"></i></a>

</div>

<!-- START CONTENT -->
<div class="content">
	<div class="map-wrap" style="background:url(img/map_default_02.png) no-repeat 50% 50%;background-size:100% auto">

	</div>
</div>
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
Bootstrap Select
================================================ -->
<script src="js/bootstrap-select/bootstrap-select.js"></script>

<!-- ================================================
Bootstrap Toggle
================================================ -->
<script src="js/bootstrap-toggle/bootstrap-toggle.min.js"></script>

<!-- ================================================
Bootstrap WYSIHTML5
================================================ -->
<!-- main file -->
<script src="js/bootstrap-wysihtml5/wysihtml5-0.3.0.min.js"></script>
<!-- bootstrap file -->
<script src="js/bootstrap-wysihtml5/bootstrap-wysihtml5.js"></script>

<!-- ================================================
Summernote
================================================ -->
<script src="js/summernote/summernote.min.js"></script>

<!-- ================================================
Flot Chart
================================================ -->
<!-- main file -->
<script src="js/flot-chart/flot-chart.js"></script>
<!-- time.js -->
<script src="js/flot-chart/flot-chart-time.js"></script>
<!-- stack.js -->
<script src="js/flot-chart/flot-chart-stack.js"></script>
<!-- pie.js -->
<script src="js/flot-chart/flot-chart-pie.js"></script>
<!-- demo codes -->
<script src="js/flot-chart/flot-chart-plugin.js"></script>

<!-- ================================================
Chartist
================================================ -->
<!-- main file -->
<script src="js/chartist/chartist.js"></script>
<!-- demo codes -->
<script src="js/chartist/chartist-plugin.js"></script>

<!-- ================================================
Easy Pie Chart
================================================ -->
<!-- main file -->
<script src="js/easypiechart/easypiechart.js"></script>
<!-- demo codes -->
<script src="js/easypiechart/easypiechart-plugin.js"></script>

<!-- ================================================
Rickshaw
================================================ -->
<!-- d3 -->
<script src="js/rickshaw/d3.v3.js"></script>
<!-- main file -->
<script src="js/rickshaw/rickshaw.js"></script>
<!-- demo codes -->
<script src="js/rickshaw/rickshaw-plugin.js"></script>

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
<!-- tab-01 Sales -->
<script>

// set up our data series with 50 random data points

var seriesData = [ [], [], [] ];
var random = new Rickshaw.Fixtures.RandomData(20);

for (var i = 0; i < 110; i++) {
  random.addData(seriesData);
}

// instantiate our graph!

var graph = new Rickshaw.Graph( {
  element: document.getElementById("tab-01sales"),
  renderer: 'bar',
  series: [
    {
      color: "#33577B",
      data: seriesData[0],
      name: 'Photodune'
    }, {
      color: "#77BBFF",
      data: seriesData[1],
      name: 'Themeforest'
    }, {
      color: "#C1E0FF",
      data: seriesData[2],
      name: 'Codecanyon'
    }
  ]
} );

graph.render();

var hoverDetail = new Rickshaw.Graph.HoverDetail( {
  graph: graph,
  formatter: function(series, x, y) {
    var date = '<span class="date">' + new Date(x * 1000).toUTCString() + '</span>';
    var swatch = '<span class="detail_swatch" style="background-color: ' + series.color + '"></span>';
    var content = swatch + series.name + ": " + parseInt(y) + '<br>' + date;
    return content;
  }
} );

</script>

<!-- tab-01 Activity -->
<script>
// set up our data series with 50 random data points

var seriesData = [ [], [], [] ];
var random = new Rickshaw.Fixtures.RandomData(20);

for (var i = 0; i < 50; i++) {
  random.addData(seriesData);
}

// instantiate our graph!

var graph = new Rickshaw.Graph( {
  element: document.getElementById("tab-01activity"),
  renderer: 'area',
  series: [
    {
      color: "#9A80B9",
      data: seriesData[0],
      name: 'London'
    }, {
      color: "#CDC0DC",
      data: seriesData[1],
      name: 'Tokyo'
    }
  ]
} );

graph.render();

var hoverDetail = new Rickshaw.Graph.HoverDetail( {
  graph: graph,
  formatter: function(series, x, y) {
    var date = '<span class="date">' + new Date(x * 1000).toUTCString() + '</span>';
    var swatch = '<span class="detail_swatch" style="background-color: ' + series.color + '"></span>';
    var content = swatch + series.name + ": " + parseInt(y) + '<br>' + date;
    return content;
  }
} );
</script>

</body>
</html>