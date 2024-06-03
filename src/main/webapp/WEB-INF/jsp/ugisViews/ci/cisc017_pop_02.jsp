<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=1190">
<meta name="apple-mobile-web-app-title" content="NAVER"/>
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

<script src="js/jquery.min.js"></script>
</head>

<body>

<div class="modal" id="" tabindex="-1" role="dialog" aria-hidden="true" style="display:block;">
	<div class="modal-dialog modal-sm">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title">벡터 변환</h4>
			</div>
			<div class="modal-body">

				<div class="inside-panel panel-default" style="height:300px;">
					<div class="inside-panel-tit">
						벡터 변환 목록
						<ul class="panel-tools">
							<li><a class="icon expand-tool"><i class="fas fa-folder"></i></a></li>
							<li><a class="icon closed-tool"><i class="fas fa-folder-open"></i></a></li>
						</ul>
					</div>
					<script>

					$(function(){
						$.contextMenu({
							selector: '.context-menu-one', 
								items: {
									vector: {
										name: "벡터로 변환",
										callback: function(key, opt){
										alert("Clicked on " + key);
									}
								},
									download: {
										name: "다운로드",
										callback: function(key, opt){
										alert("Clicked on " + key);
									}
								},
									result: {
										name: "성능평가 결과 조회",
										callback: function(key, opt){
										alert("Clicked on " + key);
									}
								}
							}, 
						});
					});
					</script>
					<div class="inside-panel-body">
						<ul class="tree">
							<li class="has">
								<input type="checkbox" name="domain[]" value="평가 결과 – XXXXXX 01">
								<label>평가 결과 – XXXXXX 01<span class="total">(3)</span></label>
								<ul class="tree-dep-02">
									<li class="">
										<input type="checkbox" name="subdomain[]" value="평가 결과 ID – xxxxxxxx001">
										<label class="context-menu-one btn-neutral">평가 결과 ID – xxxxxxxx001</label>
										<span class="label-color blue f-right"></span>
									</li>
									<li class="">
										<input type="checkbox" name="subdomain[]" value="평가 결과 ID – xxxxxxxx002">
										<label class="context-menu-one btn-neutral">평가 결과 ID – xxxxxxxx002</label>
										<span class="label-color pink f-right"></span>
									</li>
									<li class="">
										<input type="checkbox" name="subdomain[]" value="평가 결과 ID – xxxxxxxx003">
										<label class="context-menu-one btn-neutral">평가 결과 ID – xxxxxxxx003</label>
										<span class="label-color orange f-right"></span>
									</li>
								</ul>
							</li>
							<li class="has">
								<input type="checkbox" name="domain[]" value="평가 결과 – XXXXXX 02">
								<label>평가 결과 – XXXXXX 02<span class="total">(3)</span></label>
								<ul class="tree-dep-02">
									<li class="">
										<input type="checkbox" name="subdomain[]" value="평가 결과 ID – xxxxxxxx100">
										<label class="context-menu-one btn-neutral">평가 결과 ID – xxxxxxxx100</label>
									</li>
									<li class="">
										<input type="checkbox" name="subdomain[]" value="평가 결과 ID – xxxxxxxx101">
										<label class="context-menu-one btn-neutral">평가 결과 ID – xxxxxxxx101</label>
									</li>
									<li class="has">
										<input type="checkbox" name="subdomain[]" value="평가 결과 ID – xxxxxxxx102">
										<label class="context-menu-one btn-neutral">평가 결과 ID – xxxxxxxx102</label>
								</ul>
							</li>
						</ul>
					</div>
				</div>


				<script>
				$(document).on('click', '.tree label', function(e) {
				  $(this).next('ul').fadeToggle();
				  e.stopPropagation();
				});

				$(document).on('change', '.tree input[type=checkbox]', function(e) {
				  $(this).siblings('ul').find("input[type='checkbox']").prop('checked', this.checked);
				  $(this).parentsUntil('.tree').children("input[type='checkbox']").prop('checked', this.checked);
				  e.stopPropagation();
				});

				$(document).on('click', 'button', function(e) {
				  switch ($(this).text()) {
					case 'Collepsed':
					  $('.tree ul').fadeOut();
					  break;
					case 'Expanded':
					  $('.tree ul').fadeIn();
					  break;
					case 'Checked All':
					  $(".tree input[type='checkbox']").prop('checked', true);
					  break;
					case 'Unchek All':
					  $(".tree input[type='checkbox']").prop('checked', false);
					  break;
					default:
				  }
				});
				</script>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default"><i class="fas fa-check-square m-r-5"></i>확인</button>
				<button type="button" class="btn btn-gray" data-dismiss="modal" aria-label="Close"><i class="fas fa-times m-r-5"></i>취소</button>
			</div>
		</div>
	</div>
</div>



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

var graph = new Rickshaw.Graph( {
  element: document.getElementById("todayactivity"),
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