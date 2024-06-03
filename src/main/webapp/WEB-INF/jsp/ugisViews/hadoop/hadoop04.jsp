<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko" data-dark="false">
<head>
<meta charset="utf-8">
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

	<!-- 상단메뉴 -->
	<%@ include file="../topMenu.jsp"%>

<!-- START CONTENT -->
<div class="content" style="margin-left:0;width:100%">
<iframe src="http://hd01.ngii.go.kr:38080/hue/editor/?type=hive" width="100%" height="800px"/>
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

<script type="text/javascript">
var img=""
var colct_img=""

window.onload = function(){
	MNTRNG_TBL()
	COLCT_TBL()
}

/*모니터링 테이블 출력 */
function MNTRNG_TBL(){
	$.ajax({
		type : "PUT",
	    url: "/mntrng_tbl.do",
	    success:function(data){
	    	var result = JSON.parse(data);
	    	MNTRNG_TBL_EDIT(result)
	    	MNTRNG_TBL_ROW_CLICK()
	    },
	    error:function(data){
	    	alert("실패", data)
	    }
	})
}
/*모니터링 테이블 제목 클릭시 실행*/
function MNTRNG_TBL_ROW_CLICK(){
    var table = $('#MNTRNG_NEWS').DataTable();
    $('#MNTRNG_NEWS tbody').on('click', 'tr', function () {
        var data = table.row( this ).data();
        MNTRNG_NEWS_DETAIL(data[3])
        //alert( 'You clicked on '+data[0]+'\'s row' );
    } );
}


/*모니터링 테이블 데이터 수정 */
function MNTRNG_TBL_EDIT(result){
	console.log("!!!!!!!!!!!",result)
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
	    	console.log("detail",result)
	    	MNTRNG_NEWS_SJ_NM.innerHTML = result["제목"]
	    	MNTRNG_NEWS_NES_DT.innerHTML = result["보도 일시"]
	    	MNTRNG_ORIGINL_FILE_COURS_NM.innerHTML = result["기사 원문"]	    	
	    	MNTRNG_NEWS_DETAIL_IMG_LIST(result.IMAGE_FULL_PATH)
	    	$("#myModal").modal();
	    },
	    error:function(data){
	    	alert("mntrng_news_detail 실패", data)
	    }
	})
}
/*모니터링 기사 상세보기 이미지 리스트   */
function MNTRNG_NEWS_DETAIL_IMG_LIST(IMAGE_FULL_PATH){
	if(IMAGE_FULL_PATH.length!=0){
		$("#img_list").empty();
		img = IMAGE_FULL_PATH
		console.log("IMAGE_FULL_PATH",IMAGE_FULL_PATH)
		for(var i=0; i<img.length; i++){
			$("#img_list").append("<button id='img_list_"+i+"'class= 'img_list' onclick='MNTRNG_NEWS_DETAIL_IMG_VIEWER("+i+")'>Image-"+(i+1)+"</button>");
		}
		MNTRNG_NEWS_DETAIL_IMG_VIEWER(0)
	}else{
		console.log("empty!!")
		$("#img_list").empty();
		$("#img_viewer").empty();
	}
}

/*모니터링 기사 상세보기 이미지 뷰어 */
function MNTRNG_NEWS_DETAIL_IMG_VIEWER(id){
	for(var i=0; i<img.length; i++){
		var reset_id = "img_list_"+i
		console.log("reset id ", reset_id)
		document.getElementById(reset_id).className="img_list"
	}
	var img_id = "img_list_"+id
	var img_path = img[id].replace("D:/UGIS_MN","/colct")
	$("#img_viewer").empty();
	console.log(id)
	document.getElementById(img_id).className= 'img_list_click'
	//$("#img_viewer").append("<p>"+ img[id]+"</p>");
	$("#img_viewer").append("<img src = '"+img_path+"' height=400px width=600px alt='Nothing'>");
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
	    	alert("실패", data)
	    }
	})
}

/*최근 수집 목록 제목 클릭시 실행*/
function COLCT_TBL_ROW_CLICK(){
    var table = $('#COLCT_TBL').DataTable();
    $('#COLCT_TBL tbody').on('click', 'tr', function () {
        var data = table.row( this ).data();
        COLCT_TBL_DETAIL(data[7])
        //alert( 'You clicked on '+data[7]+'\'s row' );
    } );
}

/*최근 수집 목록 */
function COLCT_TBL_Edit(result){
	console.log("!!!!!!!!!!!",result)
	var DataSet =[]		
	for(var i in result.data){
		switch (result.data[i].MSFRTN_TY_CD) {
        case "DTC001":  result.data[i].MSFRTN_TY_CD ="폭우";
                 break;
        case "DTC002":  result.data[i].MSFRTN_TY_CD ="지진";
                 break;
        case "DTC003":  result.data[i].MSFRTN_TY_CD ="산불";
                 break;
        case "DTC004":  result.data[i].MSFRTN_TY_CD ="태풍";
                 break;
        case "DTC005":  result.data[i].MSFRTN_TY_CD ="침수";
                 break;
        case "DTC006":  result.data[i].MSFRTN_TY_CD ="산사태";
                 break;
        case "DTC007":  result.data[i].MSFRTN_TY_CD ="해양재난";
                 break;
        case "DTC020":  result.data[i].MSFRTN_TY_CD ="적조";
        		break;
    	}
		
		var replace = result.data[i].NEWS_NES_DT.replace("T"," ")
		var split = replace.split(".")
		result.data[i].NEWS_NES_DT = split[0]
		var index = parseInt(i)+1
		DataSet[i] = [index,result.data[i].MID,result.data[i].MSFRTN_TY_CD,result.data[i].MSFRTN_AREA_SRCHWRD,result.data[i].MSFRTN_TY_SRCHWRD,result.data[i].NEWS_NES_DT,result.data[i].NEWS_SJ_NM,result.data[i].NEWS_ID]
	  }
	  $('#COLCT_TBL').DataTable({
	    data: DataSet,
	    columns: [
	      { title: "순번" },
	      { title: "재난 요청명" },
	      { title: "재난 유형" },
	      { title: "지역 검색어" },
	      { title: "유형검색어" },
	      { title: "보도 일시" },
	      { title: "기사 제목",className: "title" },
	    ],
	    searching:false,
	    ordering:false,
	    info:false,
	    lengthChange:false,
	    columnDefs: [
			{ targets: 0, width: 100 }
			]
	  });
}
/*최근 수집 목록 상세보기 */
function COLCT_TBL_DETAIL(id){
	console.log("colct id", id)
	var param = "id="+ id
	$.ajax({
		type : "GET",
	    url: "/colct_tbl_detail.do",
	    data:param,
	    success:function(data){
	    	var result = JSON.parse(data);
	    	console.log("detail",result)
	    	COLCT_TBL_SJ_NM.innerHTML = result.article["제목"]
	    	COLCT_TBL_NES_DT.innerHTML = result.article["보도 일시"]
	    	COLCT_TBL_ORIGINL_FILE_COURS_NM.innerHTML = result.article["기사 원문"]	
	    	COLCT_TBL_DETAIL_IMG_LIST(result.image_full_path)
	    	$("#Colct_Modal").modal();
	    },
	    error:function(data){
	    	alert("mntrng_news_detail 실패", data)
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
	var img_path = colct_img[id].replace("D:/UGIS_MN","/colct")
	console.log(img_path)
	$("#colct_img_viewer").empty();
	document.getElementById(img_id).className= 'img_list_click'
	$("#colct_img_viewer").append("<img src = '" + img_path + "' height=400px width=600px alt='Nothing'>");
	
}
/*Refresh 버튼 클릭시 실행*/
Refresh.addEventListener('click', function(){
	window.location.reload()
})

/*NDMS 버튼 클릭시 실행 */
NDMS.addEventListener('click', function(){
	console.log("click")
	$.ajax({
		type : "GET",
	    url: "/test.do",
	    success:function(data){
	    	console.log(data)
	    	var NDMSUrl = data
	    	var NDMSUrl_slice = NDMSUrl.slice(0,-2)
	    	var NDMSUrl_slice2 = NDMSUrl_slice.slice(1)
	    	window.open(NDMSUrl_slice2,'_blank')
	    },
	    error:function(data){
	    	alert("실패", data)
	    }
	})
})

/*5분주기로 새로고침*/
 function refresh(){
	window.location.reload()
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
		    	console.log("수집요청-재난유형", result)
		    	var selectEl = document.getElementById("MSFRTN_TY_CD"); 
		    	for(var i in result.code){
		    		$(selectEl).append("<option value='"+ result.code[i].MSFRTN_TY_CD+"'>" +  result.code[i].MSFRTN_TY_NM + "</option>")
		    	}
		    },
		    error:function(data){
		    	alert("실패", data)
		    }
		})
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
	    	console.log("수집요청-재난 POI 광역시도", result)
	    	var selectEl = document.getElementById("CTPRVN"); 
	    	for(var i in result){
	    		$(selectEl).append("<option value='"+ result[i].code.slice(0,2)+"'>" +  result[i].name + "</option>")
	    	} 
	    },
	    error:function(data){
	    	alert("실패", data)
	    }
	})
}
/*재난 POI 광역시도 변경시 실행*/
function CTPRVN_Change(){
	var CTPRVN = document.getElementById("CTPRVN");
	var selectValue = CTPRVN.options[CTPRVN.selectedIndex].value;
	console.log("selectValue",selectValue)
	SGG(selectValue)
}

/*광역시도에 맞는 시군구 출력*/
function SGG(code){
	$("select[id='SGG'] option").not("[value='']").remove();
	$("select[id='EMD'] option").not("[value='']").remove();
	var param = "opt="+"sgg"+"&code="+code
	console.log(param)
	$.ajax({
		type : "PUT",
	    url: "/col_requst2.do",
	    data:param,
	    success:function(data){
	    	var result = JSON.parse(data);
	    	console.log("수집요청-재난 POI 시군구", result)
	    	var selectEl = document.getElementById("SGG"); 
	    	for(var i in result){
	    		$(selectEl).append("<option value='"+ result[i].code.slice(0,4)+"'>" +  result[i].name + "</option>")
	    	}  
	    },
	    error:function(data){
	    	alert("실패", data)
	    }
	})
}

/*재난 POI 시군구 변경시 실행*/
function SGG_Change(){
	var SGG = document.getElementById("SGG");
	var selectValue = SGG.options[SGG.selectedIndex].value;
	console.log("selectValue",selectValue)
	EMD(selectValue)
}

/*시군구에 맞는 읍면동 출력*/
function EMD(code){
	$("select[id='EMD'] option").not("[value='']").remove();
	var param = "opt="+"emd"+"&code="+code
	console.log(param)
	$.ajax({
		type : "PUT",
	    url: "/col_requst2.do",
	    data:param,
	    success:function(data){
	    	var result = JSON.parse(data);
	    	console.log("수집요청-재난 POI 읍면동", result)
	    	var selectEl = document.getElementById("EMD"); 
	    	for(var i in result){
	    		$(selectEl).append("<option value='"+ result[i].code.slice(0,5)+"'>" +  result[i].name + "</option>")
	    	}  
	    },
	    error:function(data){
	    	alert("실패", data)
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
	$("select[id='CTPRVN'] option").remove();
	$("select[id='SGG'] option").remove();
	$("select[id='EMD'] option").remove();
	$("select[id='MSFRTN_TY_CD'] option").remove();
	$("select[id='MSFRTN_TY_CD']").append("<option value='' disabled selected >전체</option>")
	$("select[id='CTPRVN']").append("<option value='' disabled selected >광역시도</option>")
	$("select[id='SGG']").append("<option value='' disabled selected >시군구</option>")
	$("select[id='EMD']").append("<option value='' disabled selected>읍면동</option>")
}

/*정보수집 버튼 클릭시 실행 */
function COLECT_REQUST_Click(){
	Reset()
	MSFRTN_TY_CD()
	CTPRVN()
	$("#REQUST_MSFRTN").modal();
}

/*정보 수집 요청*/
function Request_Submit(){
	var AREA_KEYWORD = document.getElementById("AREA_KEYWORD").value;
	var TY_KEYWORD = document.getElementById("TY_KEYWORD").value;
	var MSFRTN_TY_CD = document.getElementById("MSFRTN_TY_CD");
	var MSFRTN_TY_CD_Value = MSFRTN_TY_CD.options[MSFRTN_TY_CD.selectedIndex].value;
	var CTPRVN = document.getElementById("CTPRVN");
	var CTPRVN_Value = CTPRVN.options[CTPRVN.selectedIndex].text;
	var SGG = document.getElementById("SGG");
	var SGG_Value = SGG.options[SGG.selectedIndex].text;
	var EMD = document.getElementById("EMD");
	var EMD_Value = EMD.options[EMD.selectedIndex].text;
	var ADDRESS = document.getElementById("ADDRESS").value;
	console.log(AREA_KEYWORD,TY_KEYWORD,MSFRTN_TY_CD_Value,CTPRVN_Value,SGG_Value,EMD_Value,ADDRESS)
	if(AREA_KEYWORD===""||TY_KEYWORD===""||MSFRTN_TY_CD_Value===""||CTPRVN_Value===""||EMD_Value===""){
		notice_context.innerHTML = "필수입력항목이 입력되지 않았습니다.";
		$("#notice").modal();
		return
	}
	if(EMD_Value==="읍면동"){
		EMD_Value=""
		ADDRESS=""
	}
	var param = "MSFRTN_TY_CD="+MSFRTN_TY_CD_Value+"&MSFRTN_TY_SRCHWRD="+TY_KEYWORD+"&MSFRTN_AREA_SRCHWRD="+AREA_KEYWORD
				+"&CTPRVN_NM="+CTPRVN_Value+"&SGG_NM="+SGG_Value+"&EMD_NM="+EMD_Value+"&LNBR_ADDR="+ADDRESS
	console.log(param)
	$.ajax({
		type : "POST",
	    url: "/col_requst_submit.do",
	    data:param,
	    success:function(data){
	    	var result = JSON.parse(data);
	    	console.log("수집요청!!!!", result)
	    	if(result==false){
	    		notice_context.innerHTML = "존재하지않는 POI 입니다.";
	    		$("#notice").modal();
	    	}else{
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
td.title{
	color:blue;
	text-decoration :underline;
	cursor: pointer;
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

</style>
</body>
</html>