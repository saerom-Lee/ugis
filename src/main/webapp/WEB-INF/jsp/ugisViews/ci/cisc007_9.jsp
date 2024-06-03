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

<!-- ========== Css Files ========== -->
<!-- 
<link href="/css/root.css" rel="stylesheet">

<link rel="stylesheet" href="https://pro.fontawesome.com/releases/v5.10.0/css/all.css" integrity="sha384-AYmEC3Yw5cVb3ZcuHtOA93w35dYTsvhLPVnYs9eStHfGJvOvKxVfELGroGkvsg+p" crossorigin="anonymous"/>
<script type="text/javascript">
$(document).ready(function() {

$("ul#topnav li").hover(function() {
	$(this).css({ 'background' : '#1376c9 url(topnav_active.gif) repeat-x'}); //Add background color + image on hovered list item
	$(this).find("span").show();
} , function() { //on hover out...
	$(this).css({ 'background' : 'none'});
	$(this).find("span").hide();
});

});
</script>
 -->
</head>

<body>
	<!-- 상단메뉴 -->
	<%@ include file="../topMenu.jsp"%>


<!-- //////////////////////////////////////////////////////////////////////////// --> 
<!-- START SIDEBAR -->
<div class="sidebar clearfix">
	<div role="tabpanel" class="sidepanel" style="display: block;">

		<!-- Nav tabs -->
		<ul class="nav nav-tabs" role="tablist">
			<li role="presentation" class="active" style="width:33.333%">
				<a href="#tab-01" aria-controls="tab-01" role="tab" data-toggle="tab" aria-expanded="true" class="active">
					<i class="fas fa-palette"></i><br>컬러합성
				</a>
			</li>
			<li role="presentation" class="" style="width:33.333%">
				<a href="#tab-02" aria-controls="tab-02" role="tab" data-toggle="tab" class="" aria-expanded="false">
					<i class="fas fa-chart-bar"></i><br>히스토그램 조정
				</a>
			</li>
			<li role="presentation" class="" style="width:33.333%">
				<a href="#tab-03" aria-controls="tab-03" role="tab" data-toggle="tab" class="" aria-expanded="false">
					<i class="fas fa-th-large"></i><br>모자이크
				</a>
			</li>
		</ul>

		<!-- Tab panes -->
		<div class="tab-content">

			<!-- Start tab-01 -->
			<div role="tabpanel" class="tab-pane active" id="tab-01">

				<div class="sidepanel-m-title">
					대상영상 검색
				</div>
				<div class="panel-body">
					<div class="form-group m-t-10">
						<label for="input002" class="col-sm-2 control-label form-label">종류</label>
						<div class="col-sm-10">
							<label class="ckbox-container">항공영상
								<input type="checkbox" checked="checked">
								<span class="checkmark"></span>
							</label>
							<label class="ckbox-container">국토위성
								<input type="checkbox">
								<span class="checkmark"></span>
							</label>
							<label class="ckbox-container">기타위성
								<input type="checkbox">
								<span class="checkmark"></span>
							</label>
							<label class="ckbox-container">정사영상
								<input type="checkbox">
								<span class="checkmark"></span>
							</label>
						</div>
					</div>			
					<div class="form-group m-t-10">
						<label for="input002" class="col-sm-3 control-label form-label">기간</label>
						<div class="col-sm-4">
							<input type="text" id="datepicker" class="form-control">
						</div>
						<div class="col-sm-1"><span class="txt-in-form">~<span></div>
						<div class="col-sm-4">
							<input type="text" id="datepicker2" class="form-control">
						</div>
					</div>
					<script src="js/datepicker.js"></script>
					<script>
						let datepicker = new DatePicker(document.getElementById('datepicker'));
						let datepicker2 = new DatePicker(document.getElementById('datepicker2'));
					</script>
					<div class="form-group m-t-10">
						<label for="input002" class="col-sm-3 control-label form-label">해상도</label>
						<div class="col-sm-4">
							<select class="form-basic">
								<option></option>
							</select>
						</div>
						<div class="col-sm-1"><span class="txt-in-form">~<span></div>
						<div class="col-sm-4">
							<select class="form-basic">
								<option></option>
							</select>
						</div>
					</div>
					<div class="btn-wrap a-cent">
						<a href="#" class="btn btn-default"><i class="fas fa-search m-r-5"></i>검색</a>
					</div>
					<div class="sidepanel-s-title m-t-30">
					검색 결과
					<a href="" class="btn btn-light f-right" data-toggle="modal" data-target="#myModal"><i class="fas fa-project-diagram m-r-5"></i>수행</a>
					</div>
					<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-hidden="true">
						<div class="modal-dialog modal-sm">
							<div class="modal-content">
								<div class="modal-header">
									<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
									<h4 class="modal-title">컬러 합성</h4>
								</div>
								<div class="modal-body">
									<div class="panel-body">					
										<form class="">
										<div class="form-group m-t-10">
											<label class="col-sm-4 control-label form-label">Red band</label>
											<div class="col-sm-8">
												<select class="form-basic">
													<option></option>
												</select>            
											</div>
										</div>
										<div class="form-group m-t-10">
											<label class="col-sm-4 control-label form-label">Green band</label>
											<div class="col-sm-8">
												<select class="form-basic">
													<option></option>
												</select>            
											</div>
										</div>
										<div class="form-group m-t-10">
											<label class="col-sm-4 control-label form-label">Blue band</label>
											<div class="col-sm-8">
												<select class="form-basic">
													<option></option>
												</select>            
											</div>
										</div>
										<div class="form-group m-t-10">
											<label for="input002" class="col-sm-4 control-label form-label">결과영상</label>
											<div class="col-sm-8">
												<input type="text" class="form-control" id="input002" value="">
											</div>
										</div>
										</form> 

									</div>
								</div>
								<div class="modal-footer">
									<button type="button" class="btn btn-default"><i class="fas fa-check-square m-r-5"></i>처리</button>
									<button type="button" class="btn btn-gray" data-dismiss="modal" aria-label="Close"><i class="fas fa-times m-r-5"></i>취소</button>
								</div>
							</div>
						</div>
					</div>
					<div class="panel-body">
						<div class="col-lg-12">
							<div class="panel panel-default" style="height:100px;">
								<dl class="result-list-input">
									<dt>국토위성영상 (3)</dt>
									<dd>국토위성-1호 xxxxxx001</dd>
									<dd>국토위성-1호 xxxxxx002</dd>
									<dd>국토위성-1호 xxxxxx003</dd>
								</dl>
								<dl class="result-list-input">
									<dt>기타위성 (6)</dt>
									<dd>Landsat 5 TM Collection 1 B1</dd>
									<dd>Landsat 5 TM Collection 1 B2</dd>
									<dd>Landsat 5 TM Collection 1 B3</dd>
									<dd>Landsat 5 TM Collection 1 B4</dd>
									<dd>Landsat 5 TM Collection 1 B5</dd>
									<dd>Landsat 5 TM Collection 1 B7</dd>
								</dl>
							</div>
						</div>
					</div>
					<div class="sidepanel-s-title m-t-30">
					처리 결과
					</div>
					<div class="panel-body">
						<div class="col-lg-12">
							<div class="panel panel-default" style="height:100px;">
								<dl class="result-list-input">
									<dd>RGB_Landsat-5 TM Collection 1</dd>
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
						<label for="input002" class="col-sm-2 control-label form-label">종류</label>
						<div class="col-sm-10">
							<label class="ckbox-container">항공영상
								<input type="checkbox" checked="checked">
								<span class="checkmark"></span>
							</label>
							<label class="ckbox-container">국토위성
								<input type="checkbox">
								<span class="checkmark"></span>
							</label>
							<label class="ckbox-container">기타위성
								<input type="checkbox">
								<span class="checkmark"></span>
							</label>
							<label class="ckbox-container">정사영상
								<input type="checkbox">
								<span class="checkmark"></span>
							</label>
						</div>
					</div>			
					<div class="form-group m-t-10">
						<label for="input002" class="col-sm-3 control-label form-label">기간</label>
						<div class="col-sm-4">
							<input type="text" id="datepicker3" class="form-control">
						</div>
						<div class="col-sm-1"><span class="txt-in-form">~<span></div>
						<div class="col-sm-4">
							<input type="text" id="datepicker4" class="form-control">
						</div>
					</div>
					<script src="js/datepicker.js"></script>
					<script>
						let datepicker3 = new DatePicker(document.getElementById('datepicker3'));
						let datepicker4 = new DatePicker(document.getElementById('datepicker4'));
					</script>
					<div class="form-group m-t-10">
						<label for="input002" class="col-sm-3 control-label form-label">해상도</label>
						<div class="col-sm-4">
							<select class="form-basic">
								<option></option>
							</select>
						</div>
						<div class="col-sm-1"><span class="txt-in-form">~<span></div>
						<div class="col-sm-4">
							<select class="form-basic">
								<option></option>
							</select>
						</div>
					</div>
					<div class="btn-wrap a-cent">
						<a href="#" class="btn btn-default"><i class="fas fa-search m-r-5"></i>검색</a>
					</div>
					<div class="sidepanel-s-title m-t-30">
					검색 결과
					<a href="" class="btn btn-light f-right" data-toggle="modal" data-target="#myModal3"><i class="fas fa-project-diagram m-r-5"></i>수행</a>
					</div>
					<div class="modal fade" id="myModal2" tabindex="-1" role="dialog" aria-hidden="true">
						<div class="modal-dialog modal-sm">
							<div class="modal-content">
								<div class="modal-header">
									<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
									<h4 class="modal-title">히스토그램 조정</h4>
								</div>
								<div class="modal-body">
									<div class="panel-body">					
										<form class="">
										<div class="form-group m-t-10">
											<label class="col-sm-4 control-label form-label">입력영상</label>
											<div class="col-sm-8">
												<select class="form-basic">
													<option></option>
												</select>            
											</div>
										</div>
										<div class="form-group m-t-10">
											<label for="input002" class="col-sm-4 control-label form-label">결과영상</label>
											<div class="col-sm-8">
												<input type="text" class="form-control" id="input002" value="">
											</div>
										</div>
										</form> 

									</div>
								</div>
								<div class="modal-footer">
									<button type="button" class="btn btn-default"><i class="fas fa-check-square m-r-5"></i>처리</button>
									<button type="button" class="btn btn-gray" data-dismiss="modal" aria-label="Close"><i class="fas fa-times m-r-5"></i>취소</button>
								</div>
							</div>
						</div>
					</div>
				<div class="modal fade" id="myModal3" tabindex="-1" role="dialog" aria-hidden="true">
						<div class="modal-dialog modal-sm">
							<div class="modal-content">
								<div class="modal-header">
									<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
									<h4 class="modal-title">히스토그램 조정 수행</h4>
								</div>
								<div class="modal-body">
									<div class="panel-body">					
										<form class="">
										<div class="form-group m-t-10">
											<label class="col-sm-3 control-label form-label">조정방식</label>
											<div class="col-sm-9">
												<div class="col-sm-6">
													<div class="radio radio-info radio-inline">
														<input type="radio" id="inlineRadio5" value="option1" name="radioInline" checked="">
														<label for="inlineRadio5">밴드별 조정</label>
													</div>
												</div>
												<div class="col-sm-6">
													<div class="radio radio-inline">
														<input type="radio" id="inlineRadio6" value="option1" name="radioInline">
														<label for="inlineRadio6">일괄조정</label>
													</div>
												</div>
												<div class="col-sm-5">
													<label class="ckbox-container">자동영역 설정
														<input type="checkbox" checked="checked">
														<span class="checkmark"></span>
													</label>
												</div>
												<div class="col-sm-7">
													<div class="col-sm-8">
														<div class="radio radio-info radio-inline">
															<input type="radio" id="inlineRadio5" value="option1" name="radioInline" checked="">
															<label for="inlineRadio5">MIN/MAX</label>
														</div>
													</div>
													<div class="col-sm-4">
														<div class="radio radio-inline">
															<input type="radio" id="inlineRadio6" value="option1" name="radioInline">
															<label for="inlineRadio6">1σ</label>
														</div>
													</div>
												</div>
											</div>
											<label class="col-sm-3 control-label form-label">알고리즘</label>
											<div class="col-sm-9">
												<select class="form-basic">
													<option></option>
												</select>
											</div>
										</div>
										<div class="form-group m-t-10">
											<label class="col-sm-3 control-label form-label">RED</label>
											<div class="col-sm-9">
												<script type="text/javascript">
													var data = [];

													function fun() {
														var list = {};
														for (var i = 0; i < 256; i++) {
															var num = Math.floor(Math.random() * 255) + 1;
															list = {
																"column-1": num
															};
															data.push(list);
														}
														return data;
													}


													AmCharts.makeChart("chartdiv-r", {
														"type": "serial",
														"categoryField": "category",
														"colors": [
															"#FF0000"
														],
														"startDuration": 1,
														"fontFamily": "",
														"fontSize": 0,
														"handDrawScatter": 0,
														"handDrawThickness": 0,
														"categoryAxis": {
															"gridPosition": "start"
														},
														"chartCursor": {
															"enabled": true
														},
														"chartScrollbar": {
															"enabled": true
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
														"dataProvider": fun()
													});
												</script>
												<div id="chartdiv-r" style="width: 100%; height: 180px; background-color: #FFFFFF;" ></div>
												<div class="col-sm-2 in-tit">MIN</div>
												<div class="col-sm-4">
													<input type="text" class="form-control" id="input002">
												</div>
												<div class="col-sm-2 in-tit">MAX</div>
												<div class="col-sm-4">
													<input type="text" class="form-control" id="input002">
												</div>
											</div>
										</div>
										<div class="form-group m-t-10">
											<label class="col-sm-3 control-label form-label">GREEN</label>
											<div class="col-sm-9">
												<script type="text/javascript">
													var data = [];

													function fun() {
														var list = {};
														for (var i = 0; i < 256; i++) {
															var num = Math.floor(Math.random() * 255) + 1;
															list = {
																"column-1": num
															};
															data.push(list);
														}
														return data;
													}


													AmCharts.makeChart("chartdiv-g", {
														"type": "serial",
														"categoryField": "category",
														"colors": [
															"#00ff00"
														],
														"startDuration": 1,
														"fontFamily": "",
														"fontSize": 0,
														"handDrawScatter": 0,
														"handDrawThickness": 0,
														"categoryAxis": {
															"gridPosition": "start"
														},
														"chartCursor": {
															"enabled": true
														},
														"chartScrollbar": {
															"enabled": true
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
														"dataProvider": fun()
													});
												</script>
												<div id="chartdiv-g" style="width: 100%; height: 180px; background-color: #FFFFFF;" ></div>
												<div class="col-sm-2 in-tit">MIN</div>
												<div class="col-sm-4">
													<input type="text" class="form-control" id="input002">
												</div>
												<div class="col-sm-2 in-tit">MAX</div>
												<div class="col-sm-4">
													<input type="text" class="form-control" id="input002">
												</div>
											</div>
										</div>
										<div class="form-group m-t-10">
											<label class="col-sm-3 control-label form-label">BLUE</label>
											<div class="col-sm-9">
												<script type="text/javascript">
													var data = [];

													function fun() {
														var list = {};
														for (var i = 0; i < 256; i++) {
															var num = Math.floor(Math.random() * 255) + 1;
															list = {
																"column-1": num
															};
															data.push(list);
														}
														return data;
													}


													AmCharts.makeChart("chartdiv-b", {
														"type": "serial",
														"categoryField": "category",
														"colors": [
															"#0000ff"
														],
														"startDuration": 1,
														"fontFamily": "",
														"fontSize": 0,
														"handDrawScatter": 0,
														"handDrawThickness": 0,
														"categoryAxis": {
															"gridPosition": "start"
														},
														"chartCursor": {
															"enabled": true
														},
														"chartScrollbar": {
															"enabled": true
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
														"dataProvider": fun()
													});
												</script>
												<div id="chartdiv-b" style="width: 100%; height: 180px; background-color: #FFFFFF;" ></div>
												<div class="col-sm-2 in-tit">MIN</div>
												<div class="col-sm-4">
													<input type="text" class="form-control" id="input002">
												</div>
												<div class="col-sm-2 in-tit">MAX</div>
												<div class="col-sm-4">
													<input type="text" class="form-control" id="input002">
												</div>
											</div>
										</div>
										<div class="form-group m-t-10">
											<label class="col-sm-3 control-label form-label">알고리즘</label>
											<div class="col-sm-9">
												<select class="form-basic">
													<option></option>
												</select>
											</div>
										</div>
										</form> 

									</div>
								</div>
								<div class="modal-footer">
									<button type="button" class="btn btn-default"><i class="fas fa-check-square m-r-5"></i>완료</button>
									<button type="button" class="btn btn-gray" data-dismiss="modal" aria-label="Close"><i class="fas fa-times m-r-5"></i>취소</button>
								</div>
							</div>
						</div>
					</div>
					<div class="panel-body">
						<div class="col-lg-12">
							<div class="panel panel-default" style="height:100px;">
								<dl class="result-list-input">
									<dt>항공사진 (4)</dt>
									<dd>국토위성-1호 xxxxxx001</dd>
									<dd>국토위성-1호 xxxxxx002</dd>
									<dd>국토위성-1호 xxxxxx003</dd>
								</dl>
								<dl class="result-list-input">
									<dt>기타위성 (6)</dt>
									<dd>Landsat 5 TM Collection 1 B1</dd>
									<dd>Landsat 5 TM Collection 1 B2</dd>
									<dd>Landsat 5 TM Collection 1 B3</dd>
									<dd>Landsat 5 TM Collection 1 B4</dd>
									<dd>Landsat 5 TM Collection 1 B5</dd>
									<dd>Landsat 5 TM Collection 1 B7</dd>
								</dl>


							</div>
						</div>
					</div>
					<div class="sidepanel-s-title m-t-30">
					처리 결과
					</div>
					<div class="panel-body">
						<div class="col-lg-12">
							<div class="panel panel-default" style="height:100px;">
								<dl class="result-list-input">
									<dd>Result_국토위성-1호 xxxxxx001</dd>
								</dl>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div role="tabpanel" class="tab-pane" id="tab-03">

				<div class="sidepanel-m-title">
					대상영상 검색
				</div>
				<div class="panel-body">
					<div class="form-group m-t-10">
						<label for="input002" class="col-sm-2 control-label form-label">종류</label>
						<div class="col-sm-10">
							<label class="ckbox-container">항공영상
								<input type="checkbox" checked="checked">
								<span class="checkmark"></span>
							</label>
							<label class="ckbox-container">국토위성
								<input type="checkbox">
								<span class="checkmark"></span>
							</label>
							<label class="ckbox-container">기타위성
								<input type="checkbox">
								<span class="checkmark"></span>
							</label>
							<label class="ckbox-container">정사영상
								<input type="checkbox">
								<span class="checkmark"></span>
							</label>
						</div>
					</div>			
					<div class="form-group m-t-10">
						<label for="input002" class="col-sm-3 control-label form-label">기간</label>
						<div class="col-sm-4">
							<input type="text" id="datepicker5" class="form-control">
						</div>
						<div class="col-sm-1"><span class="txt-in-form">~<span></div>
						<div class="col-sm-4">
							<input type="text" id="datepicker6" class="form-control">
						</div>
					</div>
					<script src="js/datepicker.js"></script>
					<script>
						let datepicker5 = new DatePicker(document.getElementById('datepicker5'));
						let datepicker6 = new DatePicker(document.getElementById('datepicker6'));
					</script>
					<div class="form-group m-t-10">
						<label for="input002" class="col-sm-3 control-label form-label">해상도</label>
						<div class="col-sm-4">
							<select class="form-basic">
								<option></option>
							</select>
						</div>
						<div class="col-sm-1"><span class="txt-in-form">~<span></div>
						<div class="col-sm-4">
							<select class="form-basic">
								<option></option>
							</select>
						</div>
					</div>
					<div class="btn-wrap a-cent">
						<a href="#" class="btn btn-default"><i class="fas fa-search m-r-5"></i>검색</a>
					</div>
					<div class="sidepanel-s-title m-t-30">
					검색 결과
					<a href="" class="btn btn-light f-right" data-toggle="modal" data-target="#myModal4"><i class="fas fa-project-diagram m-r-5"></i>수행</a>
					</div>
					<div class="modal fade" id="myModal4" tabindex="-1" role="dialog" aria-hidden="true">
						<div class="modal-dialog modal-sm">
							<div class="modal-content">
								<div class="modal-header">
									<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
									<h4 class="modal-title">모자이크</h4>
								</div>
								<div class="modal-body">
									<div class="panel-body">					
										<form class="">
										<div class="form-group m-t-10">
											<label class="col-sm-4 control-label form-label">기준영상</label>
											<div class="col-sm-8">
												<select class="form-basic">
													<option></option>
												</select>            
											</div>
										</div>
										<div class="form-group m-t-10">
											<label class="col-sm-4 control-label form-label">입력영상</label>
											<div class="col-sm-8">
												<select class="form-basic">
													<option></option>
												</select>            
											</div>
										</div>
										<div class="form-group m-t-10">
											<label for="input002" class="col-sm-4 control-label form-label">결과영상</label>
											<div class="col-sm-8">
												<input type="text" class="form-control" id="input002" value="">
											</div>
										</div>
										</form> 

									</div>
								</div>
								<div class="modal-footer">
									<button type="button" class="btn btn-default"><i class="fas fa-check-square m-r-5"></i>처리</button>
									<button type="button" class="btn btn-gray" data-dismiss="modal" aria-label="Close"><i class="fas fa-times m-r-5"></i>취소</button>
								</div>
							</div>
						</div>
					</div>
					<div class="panel-body">
						<div class="col-lg-12">
							<div class="panel panel-default" style="height:100px;">
								<dl class="result-list-input">
									<dt>국토위성영상 (3)</dt>
									<dd>국토위성-1호 xxxxxx001</dd>
									<dd>국토위성-1호 xxxxxx002</dd>
									<dd>국토위성-1호 xxxxxx003</dd>
								</dl>
								<dl class="result-list-input">
									<dt>기타위성 (6)</dt>
									<dd>Landsat 5 TM Collection 1 B1</dd>
									<dd>Landsat 5 TM Collection 1 B2</dd>
									<dd>Landsat 5 TM Collection 1 B3</dd>
									<dd>Landsat 5 TM Collection 1 B4</dd>
									<dd>Landsat 5 TM Collection 1 B5</dd>
									<dd>Landsat 5 TM Collection 1 B7</dd>
								</dl>
							</div>
						</div>
					</div>
					<div class="sidepanel-s-title m-t-30">
					처리 결과
					</div>
					<div class="panel-body">
						<div class="col-lg-12">
							<div class="panel panel-default" style="height:100px;">
								<dl class="result-list-input">
									<dd>RGB_Landsat-5 TM Collection 1</dd>
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
<a href="#" class="sidebar-open-button-mobile"><i class="fa fa-bars"></i></a>



<!-- START CONTENT -->
<div class="content">
	<div class="map-wrap"  id="map1" style="background-size:100% auto">

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

<script src="js/map/v6.7.0/ol.js"></script>
<link rel="stylesheet" href="/js/map/v6.7.0/ol.css">
<script src="js/map/map.js"></script>
<script src="js/map/gis.js"></script>
<!-- ================================================
Below codes are only for index widgets
================================================ -->
<!-- Today Sales -->
<script>

// set up our data series with 50 random data points

var seriesData = [ [], [], [] ];
var random = new Rickshaw.Fixtures.RandomData(20);

for (var i = 0; i < 110; i++) {
  random.addData(seriesData);
}

// instantiate our graph!

var graph = new Rickshaw.Graph( {
  element: document.getElementById("todaysales"),
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

<!-- Today Activity -->
<script>
// set up our data series with 50 random data points

var seriesData = [ [], [], [] ];

var random = new Rickshaw.Fixtures.RandomData(20);

for (var i = 0; i < 50; i++) {
  random.addData(seriesData);
}

// instantiate our graph!

// var graph = new Rickshaw.Graph( {
//   element: document.getElementById("todayactivity"),
//   renderer: 'area',
//   series: [
//     {
//       color: "#9A80B9",
//       data: seriesData[0],
//     }, {
//       color: "#CDC0DC",
//       data: seriesData[1],
//       name: 'Tokyo'
//     }
//   ]
// } );

//graph.render();
//
// var hoverDetail = new Rickshaw.Graph.HoverDetail( {
//   graph: graph,
//   formatter: function(series, x, y) {
//     var date = '<span class="date">' + new Date(x * 1000).toUTCString() + '</span>';
//     var swatch = '<span class="detail_swatch" style="background-color: ' + series.color + '"></span>';
//     var content = swatch + series.name + ": " + parseInt(y) + '<br>' + date;
//     return content;
//   }
// } );
var map = createMap('map1');
var baselyaer = baselayer();
map.addLayer(baselyaer);

var select = new Select(map);
// select.once(
// 		"Polygon",
// 		"drawend",
// 		(event) => {
// 		},
// 		true
// );

const imageLayer = new ol.layer.Image();
map.addLayer(imageLayer);

const imageExtent = [957225.1387,1921135.1404,959008.6342,1922918.6358];
const source = new ol.source.ImageStatic({
	url:'/img/sample/airImage.jpg',
	crossOrigin: '',
	projection: 'EPSG:5179',
	imageExtent: imageExtent,
	//imageSmoothing: imageSmoothing.checked,
});


imageLayer.setSource(source);

map.getView().fit(imageExtent);
</script>

</body>
</html>