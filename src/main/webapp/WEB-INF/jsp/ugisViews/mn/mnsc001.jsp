<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
jQuery UI
================================================ -->
<script src="js/jquery-ui/jquery-ui.min.js"></script>
<link rel="stylesheet" href="/css/jquery-ui.css">

<!-- ========== Css Files ========== -->
<script type="text/javascript">

</script>
</head>

<body>
	<!-- 상단메뉴 -->
	<%@ include file="../topMenu.jsp"%>

<!-- START CONTENT -->
<div class="content" style="margin-left:0;">

	<div class="page-header">
		<h1 class="title">재난 모니터링</h1>
		<div class="right">
			<a href="#" class="btn btn-default" id ="Refresh"><i class="fas fa-sync-alt m-r-5"></i>새로고침</a>
			<a href="#" class="btn btn-primary" id="NDMS"><i class="fas fa-desktop m-r-5"></i>NDMS</a>
			<button type="button" class="btn btn-success" id="COLECT_REQUST_Click" onclick="COLECT_REQUST_Click()"><i class="fas fa-paste m-r-5"></i>정보수집</button>
			<div class="modal fade" id="REQUST_MSFRTN" tabindex="-1" role="dialog" aria-hidden="true">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
							<h4 class="modal-title">재난정보 수집 요청</h4>
						</div>
						<div class="modal-body">
							<div class="sidepanel-m-title">
							재난정보 웹크롤링 검색어
							</div>
							<div class="panel-body">					
								<form class="">
								<div class="form-group m-t-10">
									<label class="col-sm-2 control-label form-label">재난지역 키워드</label>
									<div class="col-sm-3">
										<input type="text" class="form-control" id="AREA_KEYWORD">            
									</div>
									<div class="col-sm-1">
									</div>
									<label class="col-sm-2 control-label form-label">재난내용 키워드</label>
									<div class="col-sm-3">
										<input type="text" class="form-control" id="TY_KEYWORD">
									</div>
								</div>
							</div>
							
							<div class="sidepanel-m-title m-t-20">
							재난 속성 정보 설정 
							</div>
							<div class="panel-body">
								<div class="form-group m-t-10">
									<label class="col-sm-2 control-label form-label">재난발생일</label>
										<div class="col-sm-5">
											<input class="form-control" id="disaster_date">
											<script>
											$('#disaster_date').datepicker()
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
											
										</script>
										</div>
										<div class="col-sm-1">
											<button type="button" class="btn btn-default btn-icon"
												onclick="Datebtn_Click()">
												<i class="fas fa-calendar-alt"></i>
											</button>
										</div>
									</div>	
								<div class="form-group m-t-10">
									<label class="col-sm-2 control-label form-label">재난 유형 분류</label>
									<div class="col-sm-6">
										<select class="form-basic" id="MSFRTN_TY_CD">
											<option value="default" disabled selected hidden> </option>
										</select>            
									</div>
								</div>
								<div class="form-group m-t-10">
									<label class="col-sm-12 control-label form-label">재난POI</label>
									<div style="float:left;">
										<div class="radio radio-info radio-inline">
											<input type="radio" id="POI_Number" name="POI" value="POI_Number" onclick="POI_Change(this.value)" checked>
											<label for="POI_Number" style="padding-left:0px;"> 지번 </label>
										</div>
										<div class="radio radio-inline">
											<input type="radio" id="POI_Roadname" name="POI" value="POI_Roadname"  onclick="POI_Change(this.value)" >
											<label for="POI_Roadname" style="padding-left:0px;"> 도로명 </label>
										</div>
									</div>
									<div class ="col-sm-12" id="POI_Number_div" style="display:none">
										<div class="col-sm-3" >
											<select class="form-basic" id="CTPRVN" onchange="CTPRVN_Change('number')">
												<option value="default" disabled selected hidden>광역시도</option>
											</select>            
										</div>
										<div class="col-sm-3">
											<select class="form-basic" id="SGG" onchange="SGG_Change('number')">
												<option  value="default" disabled selected hidden>시군구</option>
											</select>            
										</div>
										<div class="col-sm-3">
											<select class="form-basic" id="EMD" onchange="EMD_Change('number')" >
												<option  value="default" disabled selected hidden>읍면동</option>
											</select>            
										</div>
										<div class="col-sm-3">
											<input type="text" class="form-control" id="ADDRESS" placeholder="번지" readonly>         
										</div>
									</div>
									<div class ="col-sm-12" id="POI_Roadname_div" style="display:none">
										<div class="col-sm-3" >
											<select class="form-basic" id="CTPRVN_Roadname" onchange="CTPRVN_Change('roadname')">
												<option value="default" disabled selected hidden>광역시도</option>
											</select>            
										</div>
										<div class="col-sm-3">
											<select class="form-basic" id="SGG_Roadname" onchange="SGG_Change('roadname')">
												<option  value="default" disabled selected hidden>시군구</option>
											</select>            
										</div>
										<div class="col-sm-6">
											<input type="text" class="form-control" id="ADDRESS_Roadname" placeholder="번지" readonly>         
										</div>
									</div>
								</div>
								</form> 

							</div>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-default" onclick="Request_Submit()">확인</button>
							<button type="button" class="btn btn-gray" data-dismiss="modal">취소</button>
						</div>
					</div>
				</div>
			</div>
			<div class="modal fade" id=notice tabindex="-1" role="dialog" aria-hidden="false">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
							<h4 class="modal-title" id="notice_title"></h4>
						</div>
						<div class="modal-body">
							<div class="news-wrap">
								<p class="subj center" id="notice_context"></p>
							</div>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-default" data-dismiss="modal">확인</button>
						</div>
					</div>
				</div>
			</div>
			<div class="modal fade" id=duple_notice tabindex="-1" role="dialog" aria-hidden="false">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						<h4 class="modal-title">알림</h4>
					</div>
					<div class="modal-body">
						<div class="news-wrap">
							<p class="subj" id="duple_notice_context"></p>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" onclick="Request_Submit('duple')">예</button>
						<button type="button" class="btn btn-gray" data-dismiss="modal">아니오</button>
					</div>
				</div>
			</div>
		</div>
		</div>

	</div>
	<div class="container-widget">
		<!-- 연합뉴스 -->
		<div class="col-md-12">
			<div class="panel panel-default p-15">
				<div class="panel-title">
					<i class="fas fa-newspaper"></i>연합뉴스 기사 모니터링
				</div>
				<div class="panel-body table-responsive">
				<table id="MNTRNG_NEWS" class="display" width="100%"></table> 
				<!-- Modal -->
				<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-hidden="false">
					<div class="modal-dialog" style="width:800px ">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
								<h4 class="modal-title">재난 뉴스 상세 보기</h4>
							</div>
							<div class="modal-body">
								<div class="news-wrap">
									<p class="subj" id="MNTRNG_NEWS_SJ_NM"></p>
									<p class="date" id="MNTRNG_NEWS_NES_DT"></p>
									<div class="article-wrap"  id="MNTRNG_ORIGINL_FILE_COURS_NM"></div>
										<div class = "row img-wrap">
										<div class="col-md-2 " id="img_list" style="max-height:400px; overflow-y:auto;">
										</div> 
										<div class="col-md-10" id ="img_viewer">
										</div>
									</div>
								</div>
							</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-default" data-dismiss="modal">확인</button>
							</div>
							</div>
						</div>
					</div>
					</div>
				</div>
			</div>
		</div>

		<div class="col-md-12">
			<div class="panel panel-default p-15">		

				<div class="panel-title">
					<i class="fas fa-chart-line"></i>재난 정보 수집 현황
					<div class="btn-group fRight" role="group" aria-label="...">
						<button type="button" id="day_btn" class="btn btn-light" onclick="Transform_Unit('day')">일</button>
						<button type="button" id="week_btn" class="btn btn-light" onclick="Transform_Unit('week')">주</button>
						<button type="button" id ="month_btn"class="btn btn-light" onclick="Transform_Unit('month')">월</button>
					</div>
				</div>		
				<div class="col-lg-7">
					<div class="panel panel-default">
						<div class="panel-title">
							재난정보(언론뉴스) 수집 추이 그래프
						</div>
						<div class="panel-body">		
							<!-- amCharts javascript sources -->
							<script type="text/javascript" src="js/amcharts.js"></script>
							<script type="text/javascript" src="js/serial.js"></script>
							<div id="chartdiv" style="width: 100%; height: 360px; background-color: #FFFFFF;" ></div>
						</div>
					</div>
				</div>
		
				<!-- Start Chart -->
				<div class="col-lg-5">
					<div class="panel panel-default">
						<div class="panel-title">
							수집 재난정보 워드 클라우드
						</div>
						<div class="panel-body">
							<script type="text/javascript" src="js/core.js"></script>
							<script type="text/javascript" src="js/charts.js"></script>
							<script type="text/javascript" src="js/wordCloud.js"></script>
							<script type="text/javascript" src="js/animated.js"></script>
							<!-- <script type="text/javascript" src="js/material.js"></script> -->
							<!-- <script type="text/javascript" src="js/kelly.js"></script> -->
							<div id="wordclouddiv" style="width: 100%; height: 360px; background-color: #FFFFFF;" ></div>
						</div>
					</div>
				</div>
		
				
				<div class="col-md-12 col-lg-6">
					<div class="panel panel-default">
						<div class="panel-title">
							재난 유형별 수집 통계		
						</div>
						<div class="panel-body">
		
							<!-- amCharts javascript sources -->
							<script type="text/javascript" src="js/amcharts.js"></script>
							<script type="text/javascript" src="js/pie.js"></script>
							<script type="text/javascript" src="js/chalk.js"></script>
							<div id="chartdiv2" style="width: 100%; height: 400px; background-color: #fff;" ></div>
						</div>
					</div>
				</div>
		
				<div class="col-md-12 col-lg-6">
					<div class="panel panel-default">
						<div class="panel-title">
							재난 지역별 수집 통계
						</div>
						<div class="panel-body">
							<div id="chartdiv3" style="width: 100%; height: 400px; background-color: #fff;" ></div>
						</div>
					</div>
				</div>

			</div>
		</div>
		<div class="col-md-12 m-b-20">
			<div class="panel panel-default">
				<div class="panel-title">
					<i class="fas fa-list"></i>최근 수집 재난 정보 목록
				</div>
				<div class="panel-body table-responsive">
					<table class="display" width="100%" id="COLCT_TBL"></table>    
				</div>
			</div>
		</div>
		<div class="modal fade" id="Colct_Modal" tabindex="-1" role="dialog" aria-hidden="false">
			<div class="modal-dialog" style="width:800px ">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						<h4 class="modal-title">재난 뉴스 원문 보기</h4>
					</div>
					<div class="modal-body">
						<div class="news-wrap">
							<div id="meta-wrap"></div>
							<p class="subj" id="COLCT_TBL_SJ_NM"></p>
							<p class="date" id="COLCT_TBL_NES_DT"></p>
							<div class="article-wrap"  id="COLCT_TBL_ORIGINL_FILE_COURS_NM"></div>
							<br>
							<div id="meta-wrap2"></div>
							<div class = "row img-wrap">
								<div class="col-md-2 " id="colct_img_list" style="border-right:1px solid #ddd; max-height:400px; overflow-y:auto;"></div> 
								<div class="col-md-10" id ="colct_img_viewer"></div>
							</div>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">확인</button>
					</div>
					</div>
				</div>
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


<script type="text/javascript">
var img=""
var colct_img=""
myStorage = window.localStorage;
var modalcount=0

window.onload = function(){
	MNTRNG_TBL()
	COLCT_TBL()
	Transform_Unit('week')
}

/*모니터링 알림 모달 생성 */
function CREATE_MNTRNG_NOTICE_MODAL(id, context){
	modalcount++;
	//console.log("modalcount",modalcount)
	var modalId = "notice"+ id
	var modalcontext = "notice_context"+id
	//console.log(modalId,modalcontext)
	
	var target = $('body')
    var html = '<div class="modal fade" id="'+modalId+'" tabindex="-1" role="dialog" aria-hidden="false">';
    html    +=     '<div class="modal-dialog">';
    html    +=         '<div class="modal-content">';
    html    +=     			'<div class="modal-header">';
    html    +=     				'<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>';
    html    +=     				'<p class="modal-title" id="notice_title">재난 발생 알림 </p>';
    html    +=     			'</div>';
    html    +=         		'<div class="modal-body">';
    html    +=         			'<div class="news-wrap">';
    html    +=     					'<p class="subj" id="'+modalcontext+'"></p>';
    html    += 					'</div>';
    html    +=    			 '</div>';
    html    += 				'<div class="modal-footer">';
    html    +=     				'<button type="button" class="btn btn-default" data-dismiss="modal">확인</button>';
    html    += 				'</div>';
    html    += 			'</div>';
    html    += 		'</div>';
    html    += 	'</div>';
    target.append(html);
    
    document.getElementById(modalcontext).innerHTML = context
    if(modalcount>1){
    	$('#'+modalId).modal({backdrop: false});
    	$('#'+modalId).modal();
    }else{
    	$('#'+modalId).modal();
    }
}

/*모니터링 알림 */
function MNTRNG_NOTICE(result){
	console.log("result", result,result.notice.length)
	var notice = result.notice
	if(notice.length!=0){
		for(var i in notice){
			var edit_notice_context = "[재난 발생 알림]"+"<br>"+notice[i].NEWS_SJ_NM+"<br>"+notice[i].MNTRNG_SITE_NM+notice[i].NEWS_NES_DT+"<br>"+notice[i].NEWS_URL_ADDR
			CREATE_MNTRNG_NOTICE_MODAL(notice[i].MNTRNG_COLCT_ID,edit_notice_context)
		}
	}
	
}


/*모니터링 테이블 출력 */
function MNTRNG_TBL(){
	$.ajax({
		type : "PUT",
	    url: "/mntrng_tbl.do",
	    success:function(data){
	    	var result = JSON.parse(data);
	    	MNTRNG_NOTICE(result)
	    	MNTRNG_TBL_EDIT(result)
	    	MNTRNG_TBL_ROW_CLICK()
	    },
	    error:function(data){
	    	//alert("실패", data)
	    }
	})
}
/*모니터링 테이블 제목 클릭시 실행*/
function MNTRNG_TBL_ROW_CLICK(){
    var table = $('#MNTRNG_NEWS').DataTable();
    $('#MNTRNG_NEWS tbody').on('click', 'tr', function () {
        var data = table.row( this ).data();
        MNTRNG_NEWS_DETAIL(data[3])
        ////alert( 'You clicked on '+data[0]+'\'s row' );
    } );
}


/*모니터링 테이블 데이터 수정 */
function MNTRNG_TBL_EDIT(result){
	//console.log("!!!!!!!!!!!",result)
	var DataSet =[]
	for(var i in result.rows){
		var replace = result.rows[i].NEWS_NES_DT.replace("T"," ")
		var split = replace.split(".")
		result.rows[i].NEWS_NES_DT = split[0]
		var index = parseInt(i)+1
		DataSet[i] = [index,result.rows[i].NEWS_NES_DT,result.rows[i].NEWS_SJ_NM,result.rows[i].MNTRNG_COLCT_ID]
	  }
	  $('#MNTRNG_NEWS').DataTable({
	    data: DataSet,
	    columns: [
	      { title: "순번" },
	      { title: "일시" },
	      { title: "제목" , className: "title"},
	    ],
	    searching:false,
	    ordering:false,
	    info:false,
	    destroy:true,
	    lengthChange:false,
	    columnDefs: [
			{ targets: 0, width: 150 }
			]
	  });

}

/*모니터링 기사 상세보기*/
function MNTRNG_NEWS_DETAIL(id){
	var param = "id="+ id
	$.ajax({
		type : "POST",
	    url: "mntrng_news_detail.do",
	    data:param,
	    success:function(data){
	    	var result = JSON.parse(data);
	    	//console.log("detail",result)
	    	if(result.msg_no =="-100"){
	    		$("#myModal").modal();
	    	}else{
	    		MNTRNG_NEWS_SJ_NM.innerHTML = result["제목"]
		    	MNTRNG_NEWS_NES_DT.innerHTML = result["보도 일시"]
		    	var Edit_article = result["기사 원문"].replace(/\n/g, '<br>');
		    	MNTRNG_ORIGINL_FILE_COURS_NM.innerHTML = Edit_article
		    	MNTRNG_NEWS_DETAIL_IMG_LIST(result.IMAGE_FULL_PATH)
		    	$("#myModal").modal();
	    	}
	    },
	    error:function(data){
	    	//alert("mntrng_news_detail 실패", data)
	    }
	})
}
/*모니터링 기사 상세보기 이미지 리스트   */
function MNTRNG_NEWS_DETAIL_IMG_LIST(IMAGE_FULL_PATH){
	if(IMAGE_FULL_PATH.length!=0){
		$("#img_list").empty();
		img = IMAGE_FULL_PATH
		//console.log("IMAGE_FULL_PATH",IMAGE_FULL_PATH)
		for(var i=0; i<img.length; i++){
			$("#img_list").append("<button id='img_list_"+i+"'class= 'img_list' onclick='MNTRNG_NEWS_DETAIL_IMG_VIEWER("+i+")'>Image-"+(i+1)+"</button>");
		}
		MNTRNG_NEWS_DETAIL_IMG_VIEWER(0)
	}else{
		//console.log("empty!!")
		$("#img_list").empty();
		$("#img_viewer").empty();
	}
}

/*모니터링 기사 상세보기 이미지 뷰어 */
function MNTRNG_NEWS_DETAIL_IMG_VIEWER(id){
	for(var i=0; i<img.length; i++){
		var reset_id = "img_list_"+i
		////console.log("reset id ", reset_id)
		document.getElementById(reset_id).className="img_list"
	}
	
	var img_id = "img_list_"+id
	/*배포시 수정 ! img_path (/home/ugismn 경로) 주석 해제 */
	//var img_path = img[id].replace("D:/UGIS_MN","/colct")
	var img_path = img[id].replace("/home/ugismn","/colct")
	$("#img_viewer").empty();
	//console.log(id)
	document.getElementById(img_id).className= 'img_list_click'
	//$("#img_viewer").append("<p>"+ img[id]+"</p>");
	$("#img_viewer").append("<img src = '"+img_path+"' height=400px width=600px alt='Nothing'>");
}

/**재난 정보 수집 추이 그래프 , 워드클라우드, 재난 유형별 수집 통계 , 재난 발생 지역별 수집통계 단위 변경
 * before : 1 - 최근 하루동안의 데이터 , 7-최근 일주일 동안의 데이터 , 30- 최근 한달동안의 데이터 
 */
 function Transform_Unit(value){
	  console.log("Transform_Unit",value)
	  var day_btn = document.getElementById("day_btn")
	  var week_btn = document.getElementById("week_btn")
	  var month_btn = document.getElementById("month_btn")
	  if(value ==="day"){
	    MNTRNG_STATS(1)
	    WORDCLOUD(1)
	    MSFRTN_TY_PIE_CHART(1)
		MSFRTN_AREA_PIE_CHART(1)
		day_btn.className="btn btn-primary"
		week_btn.className="btn btn-light" 
		month_btn.className="btn btn-light" 
	  }
	  else if(value==="week"){
	    MNTRNG_STATS(7)
	    WORDCLOUD(7)
	    MSFRTN_TY_PIE_CHART(7)
		MSFRTN_AREA_PIE_CHART(7)
		day_btn.className="btn btn-light"
		week_btn.className="btn btn-primary" 
		month_btn.className="btn btn-light" 
	  }
	  else if(value==="month"){
	    MNTRNG_STATS(30)
	    WORDCLOUD(30)
	    MSFRTN_TY_PIE_CHART(30)
		MSFRTN_AREA_PIE_CHART(30)
		day_btn.className="btn btn-light"
		week_btn.className="btn btn-light" 
		month_btn.className="btn btn-primary" 
	  }
	 }

 /*재난 정보 수집 추이 그래프 */
 function MNTRNG_STATS(before){
 	var param = "before="+before
 	//console.log("timeline",param)
 	$.ajax({
 		type : "PUT",
 	    url: "/mntrng_stats.do",
 	    data:param,
 	    success:function(data){
 	    	var result = JSON.parse(data);
 	    	//console.log("timeline", result)
 	    	EDIT_MNTRNG_STATS(result,before)
 	    },
 	    error:function(data){
 	    	//alert("실패", data)
 	    }
 	})
 	
 }
 
 /*재난 정보 수집 추이 그래프 데이터 수정 */
 function EDIT_MNTRNG_STATS(result,before){
 	if(before==1){
 		for(var i in result){
 			var date = new Date(result[i].time)
			result[i].time = date
 		}		
 		MAKE_MNTRNG_STATS_GRAPH(result)
 	}
 	if(before==7){
 		for(var i in result){
 			var slice = result[i].time.split(" ")
 			result[i].time = slice[0]
 		}
 		MAKE_MNTRNG_STATS_GRAPH(result)
 	}
 	if(before==30){
 		MAKE_MNTRNG_STATS_GRAPH(result)
 	}
 }

/*재난 정보 수집 추이 그래프 생성*/
function MAKE_MNTRNG_STATS_GRAPH(result){
	// am4core.unuseTheme(am4themes_material);
	// am4core.unuseTheme(am4themes_kelly);
	// am4core.useTheme(am4themes_animated);

	var chart = am4core.create("chartdiv", am4charts.XYChart);
    chart.data = result

    // Create axes
    let dateAxis = chart.xAxes.push(new am4charts.DateAxis());
    dateAxis.renderer.minGridDistance = 60;
    dateAxis.dateFormats.setKey("month", "MM");
    dateAxis.dateFormats.setKey("week", "MM.dd");
    dateAxis.dateFormats.setKey("day", "MM.dd");
    dateAxis.dateFormats.setKey("hour", "HH:mm");
    
    dateAxis.periodChangeDateFormats.setKey("month", "[bold]yyyy[/]\nMM");
    dateAxis.periodChangeDateFormats.setKey("week", "[bold]MM.dd");
    dateAxis.periodChangeDateFormats.setKey("day", "[bold]MM.dd");
    dateAxis.periodChangeDateFormats.setKey("hour", "[bold]MM.dd[/]\nHH:mm");

    let valueAxis = chart.yAxes.push(new am4charts.ValueAxis());
    valueAxis.min = 0;
    valueAxis.maxPrecision = 0;
    
    // Create series
    let series = chart.series.push(new am4charts.LineSeries());
    series.dataFields.valueY = "count";
    series.dataFields.dateX = "time";
    series.tooltipText = "{count}"
    series.tooltip.pointerOrientation = "vertical";

    chart.cursor = new am4charts.XYCursor();
    chart.cursor.snapToSeries = series;
    chart.cursor.xAxis = dateAxis;
}

/*워드 클라우드*/
function WORDCLOUD(before){
	var param = "before="+before
	//console.log("wordcloud param", param)
	$.ajax({
		type : "PUT",
	    url: "/wordcloud.do",
	    data:param,
	    success:function(data){
	    	var result = JSON.parse(data);
	    	//console.log("wordcloud", result)
	    	MAKE_WORDCLOUD(result)
	    },
	    error:function(data){
	    	//alert("실패", data)
	    }
	})
}


/*워드 클라우드 생성*/
function MAKE_WORDCLOUD(result){
	am4core.useTheme(am4themes_animated);
	//am4core.useTheme(am4themes_material);
	//am4core.useTheme(am4themes_kelly);
	
	var chart = am4core.create("wordclouddiv", am4plugins_wordCloud.WordCloud);
	var series = chart.series.push(new am4plugins_wordCloud.WordCloudSeries());
	series.maxFontSize =am4core.percent(80);
	series.minFontSize= 5;
	series.fontWeight = "700";
	series.randomness = 0.2
	series.data = result
    series.dataFields.word = "word";
    series.dataFields.value = "count";
    series.colors = new am4core.ColorSet();
    series.colors.step = 1;
    series.colors.passOptions = {};

	// series.angles =0;
	// series.fontFamily ="Dotum";
    
}
/*재난 유형별 수집 통계*/
function MSFRTN_TY_PIE_CHART(before){
	//console.log("typie", before)
	var param = "before="+before
	//console.log("pie param",param)
	$.ajax({
		type : "PUT",
	    url: "typie.do",
	    data:param,
	    success:function(data){
	    	var result = JSON.parse(data);
	    	//console.log("ty_pie", result)
    		for(var i in result.data){
				switch (result.data[i].code) {
		        case "DTC001":  result.data[i].code ="풍수해";
		                 break;
		        case "DTC002":  result.data[i].code ="지진";
		                 break;
		        case "DTC003":  result.data[i].code ="산불";
		                 break;
		        case "DTC004":  result.data[i].code ="산사태";
		                 break;
		        case "DTC005":  result.data[i].code ="대형화산폭발";
		                 break;
		        case "DTC006":  result.data[i].code ="조수";
                		break;
		        case "DTC007":  result.data[i].code ="유해화학물질유출사고";
                		break;
		        case "DTC008":  result.data[i].code ="대규모해양오염";
                		break;
		        case "DTC009":  result.data[i].code ="다중밀집시설대형화재";
                		break;
		        case "DTC010":  result.data[i].code ="댐붕괴";
                		break;
		        case "DTC011":  result.data[i].code ="고속철도대형사고";
                		break;
		        case "DTC012":  result.data[i].code ="다중밀집건출물붕괴대형사고";
                		break;
				case "DTC013":  result.data[i].code ="적조";
                		break;
                

		    	}
    		}
/* 	    	result.data=[
		    	{
		    		"code":"산사태 ",
		    		"count": 5
		    	} 
	    	]*/
	    	Edit_Pie_DataSet("chartdiv2",result)
	    },
	    error:function(data){
	    	//alert("ty_pie실패", data)
	    }
	})
}

/*재난 지역별 수집 통계*/
function MSFRTN_AREA_PIE_CHART(before){
	//console.log("area_pie", before)
	var param = "before="+before
	$.ajax({
		type : "PUT",
	    url: "areapie.do",
	    data:param,
	    success:function(data){
	    	var result = JSON.parse(data);
	    	console.log("area_pie", result)
	    	/* result.data=[
		    	{
		    		"code":"전라남도 광양시 ",
		    		"count": 5
		    	}
	    	] */
	    	Edit_Pie_DataSet("chartdiv3",result)
	    },
	    error:function(data){
	    	//alert("area_pie실패", data)
	    }
	})
}
/*파이차트 데이터 셋 수정 */
function Edit_Pie_DataSet(div_id,result){
	var Edit_DataSet=[]
	var DataSet = result.data
	//console.log("DataSet", DataSet)
	if(DataSet.length>5){
		DataSet.sort(function(a, b)  {
			  return  b.count - a.count;
			});
		Edit_DataSet = DataSet.slice(0,5)
		var ETC = DataSet.slice(5)
		//console.log("Edit_DataSet",Edit_DataSet,"ETC",ETC)
		var total =0
		for(var i in ETC){
			total+=parseInt(ETC[i].count)
		}
		Edit_DataSet.push({code: '기타', count: total})
		//console.log("total", total,"Edit_DataSet",Edit_DataSet)
	}else{
		Edit_DataSet = DataSet
	}
	MAKE_PIE_CHART(div_id,Edit_DataSet)
}

/*파이 차트 생성*/
function MAKE_PIE_CHART(div_id, DataSet){
	// am4core.unuseTheme(am4themes_material);
	// am4core.unuseTheme(am4themes_kelly);
	// am4core.useTheme(am4themes_animated);

	var chart = am4core.create(div_id, am4charts.PieChart);
	chart.data=DataSet
	var pieSeries = chart.series.push(new am4charts.PieSeries());
	pieSeries.dataFields.value = "count";
	pieSeries.dataFields.category = "code";
	chart.radius = am4core.percent(80);
	pieSeries.labels.template.fontSize =15;
	pieSeries.labels.template.text = "{category}"
}

/*최근 수집 목록  출력 */
function COLCT_TBL(){
	$.ajax({
		type : "PUT",
	    url: "/colct_tbl.do",
	    success:function(data){
	    	var result = JSON.parse(data);
	    	COLCT_TBL_Edit(result)
 			COLCT_TBL_ROW_CLICK()
	    },
	    error:function(data){
	    	//alert("실패", data)
	    }
	})
}

/*최근 수집 목록 제목 클릭시 실행*/
function COLCT_TBL_ROW_CLICK(){
    var table = $('#COLCT_TBL').DataTable();
    $('#COLCT_TBL tbody').on('click', 'tr', function () {
        var data = table.row( this ).data();
        COLCT_TBL_DETAIL(data[8])
        //alert( 'You clicked on '+data[7]+'\'s row' );
    } );
}

/*최근 수집 목록 */
function COLCT_TBL_Edit(result){
	//console.log("!!!!!!!!!!!",result)
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
		DataSet[i] = [index,result.data[i].MSFRTN_INFO_COLCT_REQUST_ID,request_name,result.data[i].MSFRTN_TY_CD,result.data[i].MSFRTN_AREA_SRCHWRD,result.data[i].MSFRTN_TY_SRCHWRD,result.data[i].NEWS_NES_DT,result.data[i].NEWS_SJ_NM,result.data[i].NEWS_ID]
		//DataSet[i] = [index,result.data[i].MSFRTN_INFO_COLCT_REQUST_ID,result.data[i].MID,result.data[i].MSFRTN_TY_CD,result.data[i].MSFRTN_AREA_SRCHWRD,result.data[i].MSFRTN_TY_SRCHWRD,result.data[i].NEWS_NES_DT,result.data[i].NEWS_SJ_NM,result.data[i].NEWS_ID]
	  }
	  $('#COLCT_TBL').DataTable({
	    data: DataSet,
	    columns: [
	      { title: "순번" },
		  { title: "재난ID" },
	      { title: "재난 요청명" },
	      { title: "재난 유형" },
	      { title: "지역 검색어" },
	      { title: "유형검색어" },
	      { title: "보도 일시" },
	      { title: "기사 제목",className: "title" },
	    ],
	    searching:false,
	    ordering:true,
	    info:false,
	    destroy:true,
	    lengthChange:false,
	    columnDefs: [
			{ targets: 0, width: 100 }
			]
	  });
}
/*최근 수집 목록 상세보기 */
function COLCT_TBL_DETAIL(id){
	//console.log("colct id", id)
	var param = "id="+ id
	$.ajax({
		type : "GET",
	    url: "/colct_tbl_detail.do",
	    data:param,
	    success:function(data){
	    	var result = JSON.parse(data);
	    	//console.log("detail",result)
	    	if(result.msg_no =="-100"){
	    		$("#Colct_Modal").modal();
	    	}else{
		    	COLCT_TBL_SJ_NM.innerHTML = result.article["제목"]
		    	COLCT_TBL_NES_DT.innerHTML = result.article["보도 일시"]
		    	var Edit_article = result.article["기사 원문"].replace(/\n/g, '<br>');
		    	COLCT_TBL_ORIGINL_FILE_COURS_NM.innerHTML = Edit_article	
		    	COLCT_TBL_DETAIL_IMG_LIST(result.image_full_path)
		    	COLCT_TBL_DETAIL_META(result)
		    	$("#Colct_Modal").modal();
	    	}
	    },
	    error:function(data){
	    	//alert("mntrng_news_detail 실패", data)
	    }
	})
}
/*최근 수집 목록 기사 상세보기 이미지 리스트   */
function COLCT_TBL_DETAIL_IMG_LIST(IMAGE_FULL_PATH){
	if(IMAGE_FULL_PATH.length!=0){
	$("#colct_img_list").empty();
	colct_img = IMAGE_FULL_PATH
	for(var i=0; i<colct_img.length; i++){
		$("#colct_img_list").append("<button id='colct_img_list_"+i+"'class= 'img_list' onclick='COLCT_TBL_DETAIL_IMG_VIEWER("+i+")'>Image-"+(i+1)+"</button>");
	}
	COLCT_TBL_DETAIL_IMG_VIEWER(0)
	}else{
		$("#colct_img_list").empty();
		$("#colct_img_viewer").empty();
	}
	
}

/*최근 수집 목록 기사 상세보기 이미지 뷰어 */
function COLCT_TBL_DETAIL_IMG_VIEWER(id){
	for(var i=0; i<colct_img.length; i++){
		var reset_id = "colct_img_list_"+i
		document.getElementById(reset_id).className="img_list"
	}
	var img_id = "colct_img_list_"+id
	/*배포시 수정 img_path(/home/ugismn 경로)변수 주석 해제 */
	//var img_path = colct_img[id].replace("D:/UGIS_MN","/colct")
	var img_path = colct_img[id].replace("/home/ugismn","/colct")
	//console.log(img_path)
	$("#colct_img_viewer").empty();
	document.getElementById(img_id).className= 'img_list_click'
	$("#colct_img_viewer").append("<img src = '" + img_path + "' height=400px width=600px alt='Nothing'>");
	
}

/*최근 수집 목록 기사 상세보기 메타 추출*/
function COLCT_TBL_DETAIL_META(result){
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
	var Edit_NEWS_COLCT_DT = "수집일자 : " + result.NEWS_COLCT_DT.replace("T"," ")
	
	var fix_meta = [result.NEWS_OXPR_NM,Edit_NEWS_NES_DT,Edit_NEWS_COLCT_DT]
	for(var i in final_meta){
		$("#meta-wrap").append("<metadata>"+final_meta[i]+"</metadata>");
	}
	for(var j in fix_meta){
		$("#meta-wrap2").append("<metadata>"+fix_meta[j]+"</metadata>");
	}
} 

/*Refresh 버튼 클릭시 실행*/
Refresh.addEventListener('click', function(){
	window.location.reload()
})

/*NDMS 버튼 클릭시 실행 */
NDMS.addEventListener('click', function(){
	//console.log("click")
	$.ajax({
		type : "GET",
	    url: "/test.do",
	    success:function(data){
	    	//console.log(data)
	    	var NDMSUrl = data
	    	var NDMSUrl_slice = NDMSUrl.slice(0,-2)
	    	var NDMSUrl_slice2 = NDMSUrl_slice.slice(1)
	    	window.open(NDMSUrl_slice2,'_blank')
	    },
	    error:function(data){
	    	//alert("실패", data)
	    }
	})
})

/*5분주기로 새로고침*/
 function refresh(){
	console.log("새로고침")
	MNTRNG_TBL()
	COLCT_TBL()
	Transform_Unit('week')
	//window.location.reload()
}
setInterval(refresh, 300000);

/*수집요청 -재난 유형 출력*/
function MSFRTN_TY_CD(){
	var param = "opt="+"type"
		$.ajax({
			type : "PUT",
		    url: "/col_requst.do",
		    data:param,
		    success:function(data){
		    	var result = JSON.parse(data);
		    	//console.log("수집요청-재난유형", result)
		    	var selectEl = document.getElementById("MSFRTN_TY_CD"); 
		    	for(var i in result.code){
		    		$(selectEl).append("<option value='"+ result.code[i].MSFRTN_TY_CD+"'>" +  result.code[i].MSFRTN_TY_NM + "</option>")
		    	}
		    },
		    error:function(data){
		    	//alert("실패", data)
		    }
		})
}

/*수집요청 - 재난 발생일 선택 */
function Datebtn_Click(){
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
	$('#disaster_date').datepicker('show');
}


/*수집요청 - POI 입력 방식 선택 */
function POI_Change(value){
	if(value =="POI_Number"){
		console.log("poi change",value)
		document.getElementById("POI_Number_div").style.display = "";
		document.getElementById("POI_Roadname_div").style.display = "none";	
	}
	if(value =="POI_Roadname"){
		console.log("poi change",value)
		document.getElementById("POI_Number_div").style.display = "none";
		document.getElementById("POI_Roadname_div").style.display = "";
	}
	document.getElementById("ADDRESS").value =""
	document.getElementById("ADDRESS").readOnly =true
	$("select[id='CTPRVN'] option").remove();
	$("select[id='SGG'] option").remove();
	$("select[id='EMD'] option").remove();
	$("select[id='CTPRVN']").append("<option value='' disabled selected >광역시도</option>")
	$("select[id='SGG']").append("<option value='' disabled selected >시군구</option>")
	$("select[id='EMD']").append("<option value='' disabled selected>읍면동</option>")
	document.getElementById("ADDRESS_Roadname").value =""
	document.getElementById("ADDRESS_Roadname").readOnly =true
	$("select[id='CTPRVN_Roadname'] option").remove();
	$("select[id='SGG_Roadname'] option").remove();
	$("select[id='CTPRVN_Roadname']").append("<option value='' disabled selected >광역시도</option>")
	$("select[id='SGG_Roadname']").append("<option value='' disabled selected >시군구</option>")
	CTPRVN()
}

/*수집 요청- 재난POI 광역시도 출력*/
function CTPRVN(){
	var param = "opt="+"ctprvn"
	$.ajax({
		type : "PUT",
	    url: "/col_requst.do",
	    data:param,
	    success:function(data){
	    	var result = JSON.parse(data);
	    	//console.log("수집요청-재난 POI 광역시도", result)
	    	var selectEl = document.getElementById("CTPRVN"); 
			var selectEl2 = document.getElementById("CTPRVN_Roadname"); 
	    	for(var i in result){
	    		$(selectEl).append("<option value='"+ result[i].code.slice(0,2)+"'>" +  result[i].name + "</option>")
				$(selectEl2).append("<option value='"+ result[i].code.slice(0,2)+"'>" +  result[i].name + "</option>")
	    	} 
	    },
	    error:function(data){
	    	//alert("실패", data)
	    }
	})
}

/*재난 POI 광역시도 변경시 실행*/
function CTPRVN_Change(type){
	console.log("type", type)
	if(type =="roadname"){
		var CTPRVN = document.getElementById("CTPRVN_Roadname");
		var selectValue = CTPRVN.options[CTPRVN.selectedIndex].value;
		console.log("selectValue Roadname",selectValue)
		SGG(selectValue,type)
	}else{
		var CTPRVN = document.getElementById("CTPRVN");
		var selectValue = CTPRVN.options[CTPRVN.selectedIndex].value;
		console.log("selectValue",selectValue)
		SGG(selectValue,type)
	}

}

/*광역시도에 맞는 시군구 출력*/
function SGG(code,type){
	if(type =="roadname"){
		$("select[id='SGG_Roadname'] option").not("[value='']").remove();
		var param = "opt="+"sgg"+"&code="+code
		console.log(param)
	    var selectEl = document.getElementById("SGG_Roadname"); 
	}else{
		$("select[id='SGG'] option").not("[value='']").remove();
		$("select[id='EMD'] option").not("[value='']").remove();
		var param = "opt="+"sgg"+"&code="+code
		console.log(param)
	    var selectEl = document.getElementById("SGG"); 
	}
	$.ajax({
		type : "PUT",
	    url: "/col_requst2.do",
	    data:param,
	    success:function(data){
	    	var result = JSON.parse(data);
	    	console.log("수집요청-재난 POI 시군구", result)
	    	for(var i in result){
				if(result[i].name.indexOf(" ")==-1){
	    			$(selectEl).append("<option value='"+ result[i].code.slice(0,4)+"'>" +  result[i].name + "</option>")
	    		}else{
	    			$(selectEl).append("<option value='"+ result[i].code.slice(0,5)+"'>" +  result[i].name + "</option>")
	    		}
	    	}  
	    },
	    error:function(data){
	    	alert("실패", data)
	    }
	})
}

/*재난 POI 시군구 변경시 실행*/
function SGG_Change(type){
	if(type=="roadname"){
		var SGG = document.getElementById("SGG_Roadname");
		var selectValue = SGG.options[SGG.selectedIndex].value;
		if(selectValue.length>0){
			ADDRESS_Roadname.readOnly = false
		}
	}else{
		var SGG = document.getElementById("SGG");
		var selectValue = SGG.options[SGG.selectedIndex].value;
		console.log("selectValue",selectValue)
		EMD(selectValue)
	}
}


/*시군구에 맞는 읍면동 출력*/
function EMD(code){
	$("select[id='EMD'] option").not("[value='']").remove();
	var param = "opt="+"emd"+"&code="+code
	//console.log(param)
	$.ajax({
		type : "PUT",
	    url: "/col_requst2.do",
	    data:param,
	    success:function(data){
	    	var result = JSON.parse(data);
	    	//console.log("수집요청-재난 POI 읍면동", result)
	    	var selectEl = document.getElementById("EMD"); 
	    	for(var i in result){
	    		$(selectEl).append("<option value='"+ result[i].code.slice(0,5)+"'>" +  result[i].name + "</option>")
	    	}  
	    },
	    error:function(data){
	    	//alert("실패", data)
	    }
	})
}
/*읍면동까지 선택시 번지 활성화*/
function EMD_Change(){
	var EMD = document.getElementById("EMD");
	var ADDRESS = document.getElementById("ADDRESS");
	var selectValue = EMD.options[EMD.selectedIndex].value;
	if(selectValue.length>0){
		ADDRESS.readOnly = false
	}
}

/*수집요청 초기화*/
function Reset(){
	document.getElementById("AREA_KEYWORD").value = ""
	document.getElementById("TY_KEYWORD").value=""
	document.getElementById("ADDRESS").value =""
	document.getElementById("ADDRESS").readOnly =true
	document.getElementById("ADDRESS_Roadname").value =""
	document.getElementById("ADDRESS_Roadname").readOnly =true
	$("select[id='CTPRVN'] option").remove();
	$("select[id='SGG'] option").remove();
	$("select[id='EMD'] option").remove();
	$("select[id='CTPRVN_Roadname'] option").remove();
	$("select[id='SGG_Roadname'] option").remove();
	$("select[id='MSFRTN_TY_CD'] option").remove();
	$("select[id='MSFRTN_TY_CD']").append("<option value='' disabled selected hidden></option>")
	$("select[id='CTPRVN']").append("<option value='' disabled selected >광역시도</option>")
	$("select[id='SGG']").append("<option value='' disabled selected >시군구</option>")
	$("select[id='EMD']").append("<option value='' disabled selected>읍면동</option>")
	$("select[id='CTPRVN_Roadname']").append("<option value='' disabled selected >광역시도</option>")
	$("select[id='SGG_Roadname']").append("<option value='' disabled selected >시군구</option>")
	$('#disaster_date').val($.datepicker.formatDate('yy-mm-dd', new Date()));
}

/*정보수집 버튼 클릭시 실행 */
function COLECT_REQUST_Click(){
	Reset()
	MSFRTN_TY_CD()
	POI_Change("POI_Number")
	$("#REQUST_MSFRTN").modal();
}

/*정보 수집 요청*/
function Request_Submit(duple){
	var AREA_KEYWORD = document.getElementById("AREA_KEYWORD").value;
	var TY_KEYWORD = document.getElementById("TY_KEYWORD").value;
	var Disaster_Date = document.getElementById("disaster_date").value;
	var MSFRTN_TY_CD = document.getElementById("MSFRTN_TY_CD");
	var MSFRTN_TY_CD_Value = MSFRTN_TY_CD.options[MSFRTN_TY_CD.selectedIndex].value;
	var CTPRVN = document.getElementById("CTPRVN");
	var CTPRVN_Value = CTPRVN.options[CTPRVN.selectedIndex].text;
	console.log("CTPRVN_Value",CTPRVN_Value)
	if(CTPRVN_Value=="광역시도" ){
		var CTPRVN = document.getElementById("CTPRVN_Roadname");
		var CTPRVN_Value = CTPRVN.options[CTPRVN.selectedIndex].text;
		var SGG = document.getElementById("SGG_Roadname");
		var SGG_Value = SGG.options[SGG.selectedIndex].text;
		var EMD_Value = "읍면동"
		var ADDRESS = document.getElementById("ADDRESS_Roadname").value;
	}else{
		var SGG = document.getElementById("SGG");
		var SGG_Value = SGG.options[SGG.selectedIndex].text;
		var EMD = document.getElementById("EMD");
		var EMD_Value = EMD.options[EMD.selectedIndex].text;
		var ADDRESS = document.getElementById("ADDRESS").value;
	}
	
	console.log(Disaster_Date,AREA_KEYWORD,TY_KEYWORD,MSFRTN_TY_CD_Value,CTPRVN_Value,SGG_Value,EMD_Value,ADDRESS)
	
	
	if(AREA_KEYWORD==="" || AREA_KEYWORD.length<2){
		notice_title.innerHTML = "알림 "
		notice_context.innerHTML = "필수입력항목인 재난지역 키워드가 입력되지 않았습니다.";
		$("#notice").modal();
		return
		
	}
	if(TY_KEYWORD==="" || TY_KEYWORD.length<2){
		notice_title.innerHTML = "알림 "
		notice_context.innerHTML = "필수입력항목인 재난유형 키워드가 입력되지 않았습니다.";
		$("#notice").modal();
		return
	}
	if(Disaster_Date===""){
		notice_title.innerHTML = "알림 "
		notice_context.innerHTML = "필수입력항목인 재난발생일이 입력되지 않았습니다.";
		$("#notice").modal();
		return
	}
	if(MSFRTN_TY_CD_Value===""){
		notice_title.innerHTML = "알림 "
		notice_context.innerHTML = "필수입력항목인 재난 유형이 입력되지 않았습니다.";
		$("#notice").modal();
		return
	}
	if(CTPRVN_Value==="광역시도" || SGG_Value==="시군구"){
		notice_title.innerHTML = "알림 "
		notice_context.innerHTML = "필수입력항목인 재난POI가 입력되지 않았습니다.";
		$("#notice").modal();
		return
	}
	if(EMD_Value==="읍면동"){
		EMD_Value=""
		//ADDRESS=""
	}
	if(duple=="duple"){
		$("#duple_notice").modal("hide");
		var param ="Disaster_Date="+Disaster_Date+"&MSFRTN_TY_CD="+MSFRTN_TY_CD_Value+"&MSFRTN_TY_SRCHWRD="+TY_KEYWORD+"&MSFRTN_AREA_SRCHWRD="+AREA_KEYWORD
		+"&CTPRVN_NM="+CTPRVN_Value+"&SGG_NM="+SGG_Value+"&EMD_NM="+EMD_Value+"&LNBR_ADDR="+ADDRESS+"&force=true"
	}else{
		var param ="Disaster_Date="+Disaster_Date+"&MSFRTN_TY_CD="+MSFRTN_TY_CD_Value+"&MSFRTN_TY_SRCHWRD="+TY_KEYWORD+"&MSFRTN_AREA_SRCHWRD="+AREA_KEYWORD
		+"&CTPRVN_NM="+CTPRVN_Value+"&SGG_NM="+SGG_Value+"&EMD_NM="+EMD_Value+"&LNBR_ADDR="+ADDRESS+"&force=false"
	}
	
	console.log(param)
   	$.ajax({
		type : "POST",
	    url: "/col_requst_submit.do",
	    data:param,
	    success:function(data){
	    	var result = JSON.parse(data);
	    	console.log("수집요청!!!!", result)
	    	if(result=="fail"){
				notice_title.innerHTML = "알림 "
	    		notice_context.innerHTML = "존재하지않는 POI 입니다.";
	    		$("#notice").modal();
	    	}else if(result=="duple"){
	    		duple_notice_context.innerHTML = "같은 재난요청이 존재합니다. 그래도 추가하시겠습니까?";
	    		$("#duple_notice").modal();
	    	}
	    	else{
	    		$("#REQUST_MSFRTN").modal("hide");
	    	}
	    	
	    },
	    error:function(data){
	    	alert("실패", data)
	    }
	}) 
	
}
</script>
<style>
table.dataTable thead th{
	font-weight: bold;
    text-transform: uppercase;
    font-size: 14px;
}
table.dataTable td {
  	font-size: 15px;
}
td.title{
	color:#226FCB;
	text-decoration :underline;
	cursor : pointer;
}
.img_list{
	display: block;
	width: 100%;
	text-align: left;
	padding: 10px 15px;
	border:none;
	background:none;
	cursor: pointer;
	transition: 0.3s;
}
.img_list:hover{
	text-decoration :underline;
	cursor : pointer;
}
.img_list_click{
	color:#226FCB;
	font-weight:bold;
	display: block;
	width: 100%;
	text-align: left;
	padding: 10px 15px;
	border:none;
	background:none;
	cursor: pointer;
	transition: 0.3s;
}
.news-wrap .article-wrap {
	width:100%;
	height:250px;
	color:#474747;
	font-size:14px;
	text-align:left;
	padding:0 10px 10px 0;
	box-sizing:border-box;
	overflow-y:scroll;
}
.img-wrap {
	width:100%;
	margin-top:20px;
	padding-top:20px;
	border-top:1px solid #ddd;
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
.news-wrap .center {
    width: 100%;
    color: #474747;
    font-size: 16px;
    font-weight: bold;
    text-align: center;
}
}
</style>
</body>
</html>