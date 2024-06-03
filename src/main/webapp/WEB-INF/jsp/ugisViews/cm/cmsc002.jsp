<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
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

<!-- ================================================
jQuery Library
================================================ -->
<script src="js/jquery.min.js"></script>
<!-- ================================================
map Library
================================================ -->
<script src="js/map/v6.7.0/ol.js"></script>
<link rel="stylesheet" href="/js/map/v6.7.0/ol.css">
<%--<script src="https://cdnjs.cloudflare.com/ajax/libs/proj4js/1.1.0/proj4js-combined.min.js"></script>--%>
<script type="text/javascript" src="js/map/proj4js-combined.js"></script>
<script src="js/map/map.js"></script>
<script src="js/map/gis.js"></script>
<script src="js/map/common.js"></script>

<script type="text/javascript">
var map, imageLayer;
$(document).ready(function(){
	//1. 맵생성.
	map = createMap('map1');
	var baselyaer = baselayer();
	map.addLayer(baselyaer);

	//2. 선택기능 활성화 roi
	var select = new Select(map);
	imageLayer = new ol.layer.Image();
	map.addLayer(imageLayer);

	const imageExtent = [957225.1387,1921135.1404,959008.6342,1922918.6358];
	map.getView().fit(imageExtent);
	
	
	
    $("#roi").on("click", function () {
    	 select.once(
 		"Polygon",
 		"drawend",
 		function(event){
 			var extent = event.feature.getGeometry().extent_;
 			$('#input000').val(event.feature.getGeometry().extent_[0]);
 			$('#input001').val(event.feature.getGeometry().extent_[1]);
 			$('#input002').val(event.feature.getGeometry().extent_[2]);
 			$('#input003').val(event.feature.getGeometry().extent_[3]);
 		},
 		true
		 );
    });

$("ul#topnav li").hover(function(){ //Hover over event on list item
	$(this).css({ 'background' : '#1376c9 url(topnav_active.gif) repeat-x'}); //Add background color + image on hovered list item
	$(this).find("span").show(); //Show the subnav
} , function() { //on hover out...
	$(this).css({ 'background' : 'none'}); //Ditch the background
	$(this).find("span").hide(); //Hide the subnav
});
	function transform_5174(x,y){
		var pt = new Proj4js.Point(x,y);
		var result = Proj4js.transform(s_srs5174,t_srs,pt);
		return result;
	}

$('#si').change(function(){
	var si = this.value;
	si  = si.substring(0,2);
	var minx = $("select[id=si] option:selected").attr("minx");
	var miny = $("select[id=si] option:selected").attr("miny");
	var maxx = $("select[id=si] option:selected").attr("maxx");
	var maxy = $("select[id=si] option:selected").attr("maxy");

	$.ajax({
        url: "cmsc002/sgg.do",
        type: "post",
        data: pram={"si":si},
		v:{minx:minx,miny:miny,maxx:maxx,maxy:maxy}
    }).done(function (response) {

		var min =  transform_5174(this.v.minx,this.v.miny);
		var max =  transform_5174(this.v.maxx,this.v.maxy);
		var imageExtent = [min.x,min.y,max.x,max.y];
		map.getView().fit(imageExtent);
		var response = JSON.parse(response);
    	 var sgg = response.sggList; 
        $('#sgg').children('option').remove();
        for(var i = 0 ; i < sgg.length ; i++){	
        $('#sgg').append("<option value='"+sgg[i].bjcd+"' minx='"+sgg[i].stXmin+"' miny='"+sgg[i].stYmin+"' maxx='"+sgg[i].stXmax+"' maxy='"+sgg[i].stXmax+"' >"+sgg[i].name+"</option>");
        }
    });
});

	$('#sgg').change(function(){
		var sgg = this.value;
		sgg  = sgg.substring(0,5);
		var minx = $("select[id=sgg] option:selected").attr("minx");
		var miny = $("select[id=sgg] option:selected").attr("miny");
		var maxx = $("select[id=sgg] option:selected").attr("maxx");
		var maxy = $("select[id=sgg] option:selected").attr("maxy");
		$.ajax({
	        url: "cmsc002/emd.do",
	        type: "post",
	        data: pram={"sgg":sgg},
			v:{minx:minx,miny:miny,maxx:maxx,maxy:maxy}
	    }).done(function (response) {
			var min =  transform_5174(this.v.minx,this.v.miny);
			var max =  transform_5174(this.v.maxx,this.v.maxy);
			var imageExtent = [min.x,min.y,max.x,max.y];
			map.getView().fit(imageExtent);
			var response = JSON.parse(response);
	    	 var emd = response.sggList;
	        $('#emd').children('option').remove();
	        for(var i = 0 ; i < emd.length ; i++){
			$('#emd').append("<option value='"+emd[i].bjcd+"' minx='"+emd[i].stXmin+"' miny='"+emd[i].stYmin+"' maxx='"+emd[i].stXmax+"' maxy='"+emd[i].stXmax+"' >"+emd[i].name+"</option>");
	        }
	    });
	});

	$('#emd').change(function(){
		var minx = $("select[id=emd] option:selected").attr("minx");
		var miny = $("select[id=emd] option:selected").attr("miny");
		var maxx = $("select[id=emd] option:selected").attr("maxx");
		var maxy = $("select[id=emd] option:selected").attr("maxy");
		var min =  transform_5174(minx,miny);
		var max =  transform_5174(maxx,maxy);
		var imageExtent = [min.x,min.y,max.x,max.y];
		map.getView().fit(imageExtent);


	});


});


function search(){
	if($("input[name='sate']:checked").length ==0){
		alert("위성종류를 선택하세요.");
		return false;
	}
	if($("#datepicker").val()==""){
		alert("시작일자를 입력하세요");
		return false;
	}
	if($("#datepicker2").val()==""){
		alert("종료일자를 입력하세요");
		return false;
	}
	var checkArr = [];
	$("input[name='sate']:checked").each(function(e){
		var value = $(this).val();
		checkArr.push(value);
	})

	param={checkArr:checkArr, date1:$("#datepicker").val(),date2:$("#datepicker2").val()};
	$.ajax({
		url: "cisc007/colorSearch.do",
		type: "post",
		data: param
	}).done(function (response) {
		var objData = JSON.parse(response);
		var htmlStr="";
		var AeroList = objData.AeroList;
		
		if(AeroList==null){
			htmlStr += "<dt>검색결과가 없습니다.</dt>";
			$("#simulSearchResult").html(htmlStr);
		}
		else{
			htmlStr += "<dt>기타위성("+AeroList.length+")</dt>";
			for(var i=0; i<AeroList.length; i++) {
				htmlStr += "<dd><input class='form-check-input'  name ='etcCheckDefault'  type='checkbox' minx="+AeroList[i].ltopCrdntX+" miny="+AeroList[i].ltopCrdntY+" maxx="+AeroList[i].rbtmCrdntX+" maxy="+AeroList[i].rbtmCrdntY+" value="+AeroList[i].innerFileCoursNm+" >기타위성("+AeroList[i].innerFileCoursNm+")</dd>";
				$("#simulSearchResult").html(htmlStr);
			}	
		}
		$("#Modal").click(function(){
			$('#etcCheckDefault').children('option').remove();
			$('input:checkbox[name="etcCheckDefault"]').each(function() {
				if(this.checked){//checked 처리된 항목의 값
					$('#input_video').append("<option value='"+this.value+"' minx='"+this.getAttribute("minx")+"' miny='"+this.getAttribute("miny")+"' maxx='"+this.getAttribute("maxx")+"' maxy='"+this.getAttribute("maxy")+"'>"+this.value+"</option>");
				}
				else{
					$("#input_video option[value='"+this.value+"']").remove();
					//$("#input_video option[value='"+this.value+"']").remove();
				}
			});
		});
		// $("#flexCheckDefault").change(function(){
		// 	if($("#flexCheckDefault").is(":checked")){
		// 		var id = $('input:checkbox[id="flexCheckDefault"]').val();
		// 		$('#input_video').append("<option value='"+id+"'>"+id+"</option>");
		// 		var  arr = id.split(".");
		// 		var result = arr[0]+"result."+arr[1];
		// 		$('#result_video').val(result);
		// 	}else{
		// 		$('#input_video').append("<option value='"+id+"'>"+id+"</option>");
		// 	}
		// });
		
	});
}


 function get() {
	return {
		//입력영상
		"input_video":$("#input_video option:selected").val(),
		//결과영상
		"result_video": $("#result_video").val(),
		//해상도
		"resolution": $("#resolution").val(),
		//각도
		"angle": $("#angle").val(),
		//
		"rearrangement" : $("#rearrangement option:selected").val(),
		"wavelength" : $("#wavelength option:selected").val()
	}
};
 function valid(data) {

	if(!data.input_video){
		alert("입력영상명이 입력되지 않았습니다.");
		return false;
	}
	if(!data.result_video){
		alert("결과영상이 입력되지 않았습니다.");
		return false;
	}
	if(!data.resolution){
		alert("해상도가 입력되지 않았습니다.");
		return false;
	}
	if(!data.angle){
		alert("각도가 입력되지 않았습니다.");
		return false;
	}
	if(!data.rearrangement){
		alert("영상재배열 선택되지 않앗습니다.");
		return false;
	}
	if(!data.wavelength){
		alert("파장대역이 선택되지 않앗습니다.");
		return false;
	}


	return true;
};

function result(){
	var data = get();
	// param={input_video:input_video, result_video:result_video,resolution:resolution,angle:angle,
	// 		rearrangement:rearrangement,wavelength:wavelength };

	if(valid(data)){
		$('#progres').show();
		$.ajax({
			url: "cmsc002/simulResult.do",
			type: "post",
			data: data
		}).done(function (response) {
			debugger;
			$('#progres').hide();
			var htmlStr="";
			var response = JSON.parse(response);
			var result_file = response.fileName;
			htmlStr += "<a href=javascript:imageView('"+result_file+"');><dd>"+result_file+"</dd>";
			//$("#ColorResult").html(htmlStr);
			$("#simulResult").append(htmlStr);
			$("#simulCancel").trigger("click");
		});
	}

}
function imageView(fileName){
	var test = fileName.split('\\');
	
	fileName = test[test.length-1];
	
	
	var imageExtent = [1091677.397,1842401.1093943,1093943.7997,1845198.6434];
	var  source = new ol.source.ImageStatic({
		url:'/thumnail/'+fileName,
		crossOrigin: '',
		projection: 'EPSG:5179',
		imageExtent: imageExtent
		//imageSmoothing: imageSmoothing.checked,
	});
	imageLayer.setSource(source);
	map.getView().fit(imageExtent);
}



</script>
</head>

<body>
	<!-- 상단메뉴 -->
	<%@ include file="../topMenu.jsp"%>
	
<!-- //////////////////////////////////////////////////////////////////////////// -->
<!-- START SIDEBAR -->
<div class="sidebar clearfix">
	<div role="tabpanel" class="sidepanel" style="display: block;">
		<!-- Tab panes -->
		<div class="tab-content">

			<!-- Start tab-01 -->
			<div role="tabpanel" class="tab-pane active" id="tab-01">

				<div class="sidepanel-m-title">
					입력영상 검색
				</div>
				<div class="panel-body">
					<div class="form-group m-t-10">
						<label for="input002" class="col-sm-3 control-label form-label">종류</label>
						<div class="col-sm-9">
							<label class="ckbox-container">항공영상
								<input type="checkbox" name="sate" value="air">
								<span class="checkmark"></span>
							</label>
							<label class="ckbox-container" >국토위성
								<input type="checkbox" name="sate" value="soil">
								<span class="checkmark"></span>
							</label>
							<label class="ckbox-container">기타위성
								<input type="checkbox" name="sate" value="etc">
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
						<label for="input002" class="col-sm-3 control-label form-label">해상도(m)</label>
						<div class="col-sm-4">
<%--							<select class="form-basic">--%>
<%--								<option></option>--%>
<%--							</select>--%>
						<input type="text" id="resolution1" class="form-control">
						</div>
						<div class="col-sm-1"><span class="txt-in-form">~<span></div>
						<div class="col-sm-4">
							<input type="text" id="resolution2" class="form-control">
<%--							<select class="form-basic">--%>
<%--								<option></option>--%>
<%--							</select>--%>
						</div>
					</div>
	<%--				<div class="form-group m-t-10">
						<label class="col-sm-3 control-label form-label">재난 유형</label>
						<div class="col-sm-9">
							<select class="form-basic">
								<option></option>
							</select>
						</div>
					</div>--%>
					<div class="form-group m-t-10">
						<label class="col-sm-12 control-label form-label">지역</label>
						<div class="col-sm-4">
							<select class="form-basic" id="si">
							<option value="00">시도</option>
								<c:forEach var="result" items="${siList}" varStatus="status">
								<option value="${result.bjcd}" minx="${result.stXmin}" miny="${result.stYmin}" maxx="${result.stXmax}" maxy="${result.stYmax}">
									<c:out value="${result.name}"/></option>
								</c:forEach>
							</select>       
						</div>
						<div class="col-sm-4">
							<select class="form-basic" id="sgg">
								<option>시군구</option>
							</select>            
						</div>
						<div class="col-sm-4">
							<select class="form-basic" id="emd">
								<option>읍면동</option>
							</select>            
						</div>
					</div>
					<div class="form-group m-t-10">
						<label for="input002" class="col-sm-3 control-label form-label">ROI</label>
						<div class="col-sm-9">
							<div class="col-sm-2 in-tit">
								ULX
							</div>
							<div class="col-sm-4">
								<input type="text" class="form-control" id="input000">
							</div>
							<div class="col-sm-2 in-tit">
								ULY
							</div>
							<div class="col-sm-4">
								<input type="text" class="form-control" id="input001">
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
								<input type="text" class="form-control" id="input003">
							</div>
						</div>
					</div>
					<div class="btn-wrap a-cent">
						<a href="#" class="btn btn-info" id="roi"><i class="fas fa-mouse-pointer m-r-5"></i>사용자 정의 ROI</a>
						<a href="javascript:search()" class="btn btn-default"><i class="fas fa-search m-r-5"></i>검색</a>
					</div>
					<div class="sidepanel-s-title m-t-30">
						검색 결과
						<a href="" class="btn btn-light f-right"  id="Modal"  data-toggle="modal" data-target="#myModal"><i class="fas fa-project-diagram m-r-5"></i>수행</a>
					</div>

					<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-hidden="true">
						<div class="modal-dialog modal-sm">
							<div class="modal-content">
								<div class="modal-header">
									<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
									<h4 class="modal-title">국토위성 시뮬레이터</h4>
								</div>
								<div class="modal-body">
									<div class="panel-body">	
										<div class="col-sm-12">	
											<div class="form-group m-t-10">
												<label class="col-sm-3 control-label form-label">입력 영상</label>
												<div class="col-sm-9">
													<select class="form-basic" id="input_video">
<%--														<option value="36807062s_R.tif">36807062s_R.tif</option>--%>
<%--														<option value="36807062s_G.tif">36807062s_G.tif</option>--%>
<%--														<option value="36807062s_B.tif">36807062s_B.tif</option>--%>
													</select>
												</div>
											</div>
										</div>
										<div class="col-sm-12">	
<%--											<div class="form-group m-t-10">--%>
<%--												<label class="col-sm-3 control-label form-label">결과 영상</label>--%>
<%--												<div class="col-sm-9">--%>
<%--													<select class="form-basic" id="result_video">--%>
<%--														<option  value="36807062s_HAE_result.tif">36807062s_HAE_result.tif</option>--%>
<%--													</select>--%>
<%--												</div>--%>
<%--											</div>--%>

											<div class="form-group m-t-10">
												<label for="input002" class="col-sm-3 control-label form-label">결과영상</label>
												<div class="col-sm-9">
													<input type="text" class="form-control" id="result_video" value="">
												</div>
											</div>
										</div>
										<div class="col-sm-12">	
											<div class="form-group m-t-10">
												<label class="col-sm-3 control-label form-label">해상도</label>
												<div class="col-sm-7">
													<input type="text" id="resolution" class="form-control">
												</div>
												<div class="col-sm-2 in-tit">cm</div>
											</div>
										</div>
										<div class="col-sm-12">	
											<div class="form-group m-t-10">
												<label class="col-sm-3 control-label form-label">촬영 각도</label>
												<div class="col-sm-7">
													<input type="text" id="angle" class="form-control">
												</div>
												<div class="col-sm-2 in-tit">degree</div>
											</div>
										</div>
										<div class="col-sm-12">	
											<div class="form-group m-t-10">
												<label class="col-sm-3 control-label form-label">영상 재배열</label>
												<div class="col-sm-9">
													<select class="form-basic" id="rearrangement">
														<option value="Neighbor">Nearest Neighbor</option>
														<option value="Bilinear">Bilinear</option>
														<option value="Convolution">Cubic Convolution </option>
													</select>
												</div>
											</div>
										</div>
										<div class="col-sm-12">	
											<div class="form-group m-t-10">
												<label class="col-sm-3 control-label form-label">파장대역</label>
												<div class="col-sm-9">
													<select class="form-basic" id="wavelength">
														<option value="red">red</option>
														<option value="green">green</option>
														<option value="blue">blue</option>
														<option value="nir">nir</option>
														<option value="Panchromatic">Panchromatic</option>
													</select>
												</div>
											</div>
										</div>
									</div>
								</div>
								<div class="modal-footer">
									<button type="button" class="btn btn-default" onclick="result();"><i class="fas fa-check-square m-r-5"></i>처리</button>
									<button type="button" class="btn btn-gray" data-dismiss="modal" aria-label="Close" id="simulCancel"><i class="fas fa-times m-r-5"></i>취소</button>
								</div>
								<div class="load" id="progres" style="display: none;"></div>
							</div>
						</div>
					</div>
					<div class="panel-body">
						<div class="col-lg-12">
							<div class="panel panel-default" style="height:180px;">
								<dl class="result-list-input" id="simulSearchResult">

								</dl>


							</div>
						</div>
					</div>

					<div class="sidepanel-s-title m-t-10">
					처리 결과
					</div>
					<div class="panel-body">
						<div class="col-lg-12">
							<div class="panel panel-default" style="height:100px;">
								<dl class="result-list-input" id="simulResult">
									<dt></dt>
									<dd>

									</dd>
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