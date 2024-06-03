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



<body>
	<!-- 상단메뉴 -->
	<%@ include file="../topMenu.jsp"%>


<!-- //////////////////////////////////////////////////////////////////////////// --> 
<!-- START SIDEBAR -->
<div class="sidebar clearfix">
	<div role="tabpanel" class="sidepanel" style="display: block;">
		<div class="tab-content">
			<div role="tabpanel" class="tab-pane active" id="today">

				<div class="sidepanel-m-title">
				재난 발생 모니터링 키워드 설정
				</div>
				<div class="panel-body">
					<div class="form-group m-t-10">
						<label for="input002" class="col-sm-3 control-label form-label">키워드</label>
						<div class="col-sm-7">
							<input type="text" class="form-control" id="WEBCRWL_MNFRNG_KWRD">
						</div>
						<div class="col-sm-2">
							<button type="button" id="WEBCRWL_MNFRNG_KWRD_ADD_BTN" class="btn btn-secondary btn-icon" disabled><i class="fas fa-plus"></i></button>
						</div>
					</div>	
					<div class="panel-body m-t-10">
						<form class="fieldset-form">
							<fieldset>
								<div class="form-group" id = "keyword">
								</div>
							</fieldset>
						</form>

					</div>
				</div>
				<div class="sidepanel-m-title m-t-20">
				재난 정보 웹 크롤링 설정
				</div>
				<div class="panel-body">
					<div class="form-group m-t-10">
						<label for="WEBCRWL_POTAL_URL_ADDR" class="col-sm-3 control-label form-label">대상 포털</label>
						<div class="col-sm-7">
							<input type="text" class="form-control" value="" id="WEBCRWL_POTAL_URL_ADDR" readonly onkeyup="POTAL_Enterkey()">
						</div>
						<div class="col-sm-2">
							<button type="button" class="btn btn-default btn-icon" id="WEBCRWL_POTAL_URL_ADDR_BTN"><i class="fas fa-pen"></i></button>
						</div>
					</div>
					<div class="form-group m-t-10">
						<p class="col-sm-3 control-label form-label">수집 주기</p>
						<div class="col-sm-9">
							<div class="radio radio-info radio-inline">
								<input type="radio" id="WEBCRWL_COLCT_CYCLE_CO_5" name="WEBCRWL_COLCT_CYCLE_CO" value="5" onclick="WEBCRWL_COLCT_CYCLE_CHANGE(this.value)" >
								<label for="WEBCRWL_COLCT_CYCLE_CO_5" style="padding-left:0px;"> 5분 </label>
							</div>
							<div class="radio radio-inline">
								<input type="radio" id="WEBCRWL_COLCT_CYCLE_CO_10" name="WEBCRWL_COLCT_CYCLE_CO" value="10"  onclick="WEBCRWL_COLCT_CYCLE_CHANGE(this.value)" >
								<label for="WEBCRWL_COLCT_CYCLE_CO_10" style="padding-left:0px;"> 10분 </label>
							</div>
							<div class="radio radio-inline">
								<input type="radio" id="WEBCRWL_COLCT_CYCLE_CO_30" name="WEBCRWL_COLCT_CYCLE_CO" value="30"  onclick="WEBCRWL_COLCT_CYCLE_CHANGE(this.value)">
								<label for="WEBCRWL_COLCT_CYCLE_CO_30" style="padding-left:0px;"> 30분 </label>
							</div>
							<div class="radio radio-inline">
								<input type="radio" id="WEBCRWL_COLCT_CYCLE_CO_60" name="WEBCRWL_COLCT_CYCLE_CO" value="60"  onclick="WEBCRWL_COLCT_CYCLE_CHANGE(this.value)">
								<label for="WEBCRWL_COLCT_CYCLE_CO_60" style="padding-left:0px;"> 1시간 </label>
							</div>	
						</div>	
					</div>			
					<div class="form-group m-t-10">
						<label class="col-sm-3 control-label form-label">수집 시점</label>
						<div class="col-sm-9">
							<select class="form-basic" id="WEBCRWL_COLCT_YR" onchange="WEBCRWL_COLCT_YR_CHANGE(this.value)">
								<script>
								var date= new Date()
								var year = date.getFullYear()
								for(var i=1990; i<=year; i++){
									document.write("<option value="+i+">"+i+"</option>");
								}
								 var sort = $("select[id='WEBCRWL_COLCT_YR']>option").sort(
						                  function(a,b) {
						                           return a.value > b.value ? 1 : -1; }
								 );
								 $("select[id='WEBCRWL_COLCT_YR']").empty();         // 기존데이터 지우고
								 $("select[id='WEBCRWL_COLCT_YR']").append(sort); // 정렬된 데이터 넣어주고
								</script>
								<option value=1990>전체 과거연도</option>
							</select>             
						</div>
					</div>
					<div class="form-group m-t-10">
						<p class="col-sm-3 control-label form-label">수집 기간</p>
						<div class="col-sm-9">
							<div class="radio radio-info radio-inline">
								<input type="radio" id="WEBCRWL_COLCT_PD_CO_1" name="WEBCRWL_COLCT_PD_CO" value="1" onclick="WEBCRWL_COLCT_PD_CHANGE(this.value)" >
								<label for="WEBCRWL_COLCT_PD_CO_1" style="padding-left:0px"> 1일 </label>
							</div>
							<div class="radio radio-inline">
								<input type="radio" id="WEBCRWL_COLCT_PD_CO_3" name="WEBCRWL_COLCT_PD_CO" value="3" onclick="WEBCRWL_COLCT_PD_CHANGE(this.value)">
								<label for="WEBCRWL_COLCT_PD_CO_3" style="padding-left:0px"> 3일 </label>
							</div>
							<div class="radio radio-inline">
								<input type="radio" id="WEBCRWL_COLCT_PD_CO_7" name="WEBCRWL_COLCT_PD_CO" value="7" onclick="WEBCRWL_COLCT_PD_CHANGE(this.value)">
								<label for="WEBCRWL_COLCT_PD_CO_7" style="padding-left:0px"> 1주일 </label>
							</div>
							<div class="radio radio-inline">
								<input type="radio" id="WEBCRWL_COLCT_PD_CO_30" name="WEBCRWL_COLCT_PD_CO" value="30" onclick="WEBCRWL_COLCT_PD_CHANGE(this.value)">
								<label for="WEBCRWL_COLCT_PD_CO_30" style="padding-left:0px"> 1개월 </label>
							</div>	
						</div>
					</div>
					<!-- 
					<div class="form-group m-t-10">
						<label for="input002" class="col-sm-3 control-label form-label">키워드</label>
						<div class="col-sm-7">
							<input type="text" class="form-control" id="WEBCRWL_MNFRNG_KWRD">
						</div>
						<div class="col-sm-2">
							<button type="button" id="WEBCRWL_MNFRNG_KWRD_ADD_BTN" class="btn btn-secondary btn-icon" disabled><i class="fas fa-plus"></i></button>
						</div>
					</div>	
					<div class="panel-body m-t-10">
						<form class="fieldset-form">
							<fieldset>
								<div class="form-group" id = "keyword">
								</div>
							</fieldset>
						</form>

					</div>
					-->

				</div>


				<div class="sidepanel-m-title m-t-30">
				관계기관 정보시스템 설정
				</div>
				<div class="panel-body">
					<div class="form-group m-t-10">
						<label for="WEBCRWL_GVMAGNC_SYS_URL_ADDR" class="col-sm-3 control-label form-label">대상 기관</label>
						<div class="col-sm-7">
							<input type="text" class="form-control" id=WEBCRWL_GVMAGNC_SYS_URL_ADDR value="www.관계기관.or.kr" readonly onkeyup="GVMAGNC_Enterkey()">
						</div>
						<div class="col-sm-2">
							<button type="button" id="WEBCRWL_GVMAGNC_SYS_URL_ADDR_BTN" class="btn btn-default btn-icon"><i class="fas fa-pen"></i></button>
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

	<div class="page-header">
		<h1 class="title">재난정보 수집설정</h1>

	</div>
	<div class="container-widget">

		<div class="col-md-12">
			<div class="panel panel-default">
				<div class="panel-title">
					<i class="fas fa-list"></i>재난정보 수집 요청 목록
				</div>
				<div class="panel-body table-responsive">
					<table class="table table-hover table-striped" id="REQUST_TBL"></table>    
				</div>

			</div>
		</div>
		<div class="modal fade" id=delete_notice tabindex="-1" role="dialog" aria-hidden="false">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						<h4 class="modal-title">알림</h4>
					</div>
					<div class="modal-body">
						<div class="news-wrap">
							<p class="subj" id="delete_notice_context"></p>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" onclick="State_Delete()">예</button>
						<button type="button" class="btn btn-gray" data-dismiss="modal">아니오</button>
					</div>
				</div>
			</div>
		</div>
		<div class="modal fade" id=notice tabindex="-1" role="dialog" aria-hidden="false">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						<h4 class="modal-title">알림</h4>
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

<!-- ================================================
Below codes are only for index widgets
================================================ -->

<script>
var Delete_id =""

window.onload = function(){
	/*현재 설정되어있는 관계기관  url 설정 */
	$.ajax({
		type : "GET",
	    url: "/today_gvmagnc.do",
	    success:function(data){
	    	//console.log("현재 설정된 관계기관 URL  ",data)
	    	var NDMSUrl = data
	    	var NDMSUrl_slice = NDMSUrl.slice(0,-2)
	    	var NDMSUrl_slice2 = NDMSUrl_slice.slice(1)
	    	 WEBCRWL_GVMAGNC_SYS_URL_ADDR.value = NDMSUrl_slice2
	    },
	    error:function(data){
	    	//alert("실패", data)
	    }
	})
	/*현재 설정되어있는 웹크롤링 설정 출력 - 대상 포털*/
	$.ajax({
		type : "GET",
	    url: "/today_potal.do",
	    success:function(data){
	    	var result = JSON.parse(data);
	    	WEBCRWL_POTAL_URL_ADDR.value = result.PORTAL_URL_ADDR
	    },
	    error:function(data){
	    	//alert("실패", data)
	    }
	})
	/*현재 설정되어있는 웹크롤링 설정 출력 - 수집주기, 수집 시점, 수집기간*/
	$.ajax({
		type : "GET",
	    url: "/webcrwl_today.do",
	    success:function(data){
	    	var result = JSON.parse(data)
	    	//console.log('웹크롤링 설정 ',result )
	    	document.getElementById("WEBCRWL_COLCT_CYCLE_CO_"+result.COLCT_CYCLE_CO).checked = true
	    	document.getElementById("WEBCRWL_COLCT_PD_CO_"+result.COLCT_PD_CO).checked = true
	        var YR_length = WEBCRWL_COLCT_YR.getElementsByTagName('option').length
	        for(var i=0; i<YR_length; i++){
	          if( WEBCRWL_COLCT_YR.getElementsByTagName('option')[i].value === result.COLCT_YR){
	            WEBCRWL_COLCT_YR.getElementsByTagName('option')[i].selected = true;
	            break;
	          }
	        }
	    },
	    error:function(data){
	    	//alert("실패", data)
	    }
	})
	
	/*현재 설정되어있는 웹크롤링 설정 출력 - 키워드*/
	$.ajax({
		type : "GET",
	    url: "/keyword_today.do",
	    success:function(data){
	    	var result = JSON.parse(data)
	    	//console.log('키워드 ',result);
	    	keyword_Today(result)
	    },
	    error:function(data){
	    	//alert("실패", data)
	    }
	})
	
	/*수집요청목록 출력*/
	$.ajax({
	type : "PUT",
    url: "/request_tbl.do",
    success:function(data){
    	var result = JSON.parse(data)
    	//console.log('요청목록 ',result);
    	Request_TBL(result);
    },
    error:function(data){
    	//alert("실패", data)
    }
});
	
}

function Request_TBL(result){
	//console.log("!!!!!!!!!!!",result)
	var DataSet =[]
	var today = new Date()
	var Separator = "-";
	var position1 = 4;
	var position2 = 6;
	var target=[]
	for(var j in result){
		result[j].COLCT_END_DE = [result[j].COLCT_END_DE.slice(0, position1), Separator, result[j].COLCT_END_DE.slice(position1,position2),Separator,result[j].COLCT_END_DE.slice(position2)].join('');
		var check_End = new Date(result[j].COLCT_END_DE)
		var comparison = check_End > today
		if(result[j].COLCT_STTUS_CD!="2" && check_End > today){
			//console.log("22222222222",result[j])
			target.push(result[j])
		}
	}
	for(var i in target){
		switch (target[i].MSFRTN_TY_CD) {
	    case "DTC001":  target[i].MSFRTN_TY_CD ="풍수해";
	             break;
	    case "DTC002":  target[i].MSFRTN_TY_CD ="지진";
	             break;
	    case "DTC003":  target[i].MSFRTN_TY_CD ="산불";
	             break;
	    case "DTC004":  target[i].MSFRTN_TY_CD ="산사태";
	             break;
	    case "DTC005":  target[i].MSFRTN_TY_CD ="대형화산폭발";
	             break;
        case "DTC006":  target[i].MSFRTN_TY_CD ="조수";
				break;
		case "DTC007":  target[i].MSFRTN_TY_CD ="유해화학물질유출사고";
				break;
		case "DTC008":  target[i].MSFRTN_TY_CD ="대규모해양오염";
				break;
		case "DTC009":  target[i].MSFRTN_TY_CD ="다중밀집시설대형화재";
				break;
		case "DTC010":  target[i].MSFRTN_TY_CD ="댐붕괴";
				break;
		case "DTC011":  target[i].MSFRTN_TY_CD ="고속철도대형사고";
				break;
		case "DTC012":  target[i].MSFRTN_TY_CD ="다중밀집건출물붕괴대형사고";
				break;
		case "DTC013":  target[i].MSFRTN_TY_CD ="적조";
				break;

		}
		var Disaster_Date = "20"+String(target[i].MSFRTN_INFO_COLCT_REQUST_ID).slice(0,6)
		var MSFRTN_ID =Disaster_Date+" " +target[i].MSFRTN_AREA_SRCHWRD+" "+target[i].MSFRTN_TY_SRCHWRD
		target[i].COLCT_BEGIN_DE = [target[i].COLCT_BEGIN_DE.slice(0, position1), Separator, target[i].COLCT_BEGIN_DE.slice(position1,position2),Separator,target[i].COLCT_BEGIN_DE.slice(position2)].join('');
		//target[i].COLCT_END_DE = [target[i].COLCT_END_DE.slice(0, position1), Separator, target[i].COLCT_END_DE.slice(position1,position2),Separator,target[i].COLCT_END_DE.slice(position2)].join('');
		var POI = target[i].CTPRVN_NM+" " + target[i].SGG_NM+" " + target[i].EMD_NM+" "+target[i].LNBR_ADDR
		DataSet[i] = [target[i].MSFRTN_INFO_COLCT_REQUST_ID,MSFRTN_ID,target[i].MSFRTN_TY_CD, target[i].MSFRTN_TY_SRCHWRD,target[i].MSFRTN_AREA_SRCHWRD,POI,target[i].COLCT_BEGIN_DE,target[i].COLCT_END_DE,target[i].MSFRTN_INFO_COLCT_REQUST_ID,target[i].COLCT_STTUS_CD]

	}

	  $('#REQUST_TBL').DataTable({
		    data: DataSet,
		    columns: [
			  { title: "재난 ID" },
		      { title: "재난 요청명" },
		      { title: "재난 유형" },
		      { title: "유형 검색어" },
		      { title: "지역 검색어" },
		      { title: "POI" },
		      { title: "수집 요청일" },
		      { title: "수집 종료일" },
		      { title: "동작 상태" },
		    ],
		    searching:false,
		    info:false,
		    lengthChange:false,
			order: [[ 6, "desc" ]],
			columnDefs: [
				{ targets: 0, width: 150 },
				{
		            "targets": -1,
		            "data": null,
		            "render": function(data, type, row){
		            	if(row[9]=='1'){
		            		return "<button onclick='State_Play("+ row[8]+")' id ='state_play_"+row[8]+"' class=' state_play btn btn-rounded btn-light'><i class='fas fa-play'></i></button> <button style='display:none;' onclick='State_Stop("+ row[8]+")' id ='state_stop_"+row[8]+"' class=' state_play btn btn-rounded btn-light'><i class='fas fa-stop'></i></button> <button onclick='State_Delete_Click("+ row[8]+")' class='btn btn-rounded btn-light'><i class='fas fa-times'></i></button>";
		            	}
		            	else{
		            		return "<button style='display:none;' onclick='State_Play("+ row[8]+")' id ='state_play_"+row[8]+"' class=' btn btn-rounded btn-light'><i class='fas fa-play'></i></button> <button onclick='State_Stop("+ row[8]+")' id ='state_stop_"+row[8]+"' class='  btn btn-rounded btn-light'><i class='fas fa-stop'></i></button> <button onclick='State_Delete_Click("+ row[8]+")' class='btn btn-rounded btn-light'><i class='fas fa-times'></i></button>";
		            	}
		                },        
		        }
			]
		  }); 
}
/*요청 상태가 수집 중 , play 버튼을 눌렀을 때 실행 
 * 실행 완료후 요청상태는 수집 중지
 */
function State_Play(id){
	//console.log('State_Play!',id)
	var play_btn_id = 'state_play_'+id
	var stop_btn_id ='state_stop_'+id
	var play_btn = document.getElementById(play_btn_id)
	var stop_btn = document.getElementById(stop_btn_id)
	//console.log("!!!!", play_btn,stop_btn)
	var param = "id="+id+"&state="+"0"
	  //console.log("param",param)
	      $.ajax({
			type : "POST",
		    url: "/request_state.do",
		    data:param,
		    success:function(data){
		    	//console.log("요청상태 정지 완료", data);
		    	stop_btn.style.display=""
		    	play_btn.style.display="none"
		    },
		    error:function(data){
		    	//alert("실패", data);
		    }
		});

	
	
}
/*요청 상태가 수집 중지 ,  stop버튼을 눌렀을 때 실행 
 * 실행 완료후 요청상태는 수집중
 */
function State_Stop(id){
	//console.log('State_Stop!',id)
	var play_btn_id = 'state_play_'+id
	var stop_btn_id ='state_stop_'+id
	var play_btn = document.getElementById(play_btn_id)
	var stop_btn = document.getElementById(stop_btn_id)
	//console.log("!!!!", play_btn,stop_btn)
		var param = "id="+id+"&state="+"1"
	  //console.log("param",param)
	      $.ajax({
			type : "POST",
		    url: "/request_state.do",
		    data:param,
		    success:function(data){
		    	//console.log("요청상태 시작 완료", data);
		    	stop_btn.style.display="none";
		    	play_btn.style.display=""
		    },
		    error:function(data){
		    	//alert("실패", data);
		    }
		});
}
/*요청 삭제 확인 모달 출력*/
function State_Delete_Click(id){
	delete_notice_context.innerHTML = "선택한 목록을 삭제하시겠습니까?";
	$("#delete_notice").modal();
	Delete_id = id
}

/*요청 삭제 */
function State_Delete(){
	//console.log('State_Delete!',Delete_id, typeof(Delete_id))
	  var param = "id="+ Delete_id
	  //console.log("param",param)
	      $.ajax({
			type : "DELETE",
		    url: "/request_delete.do",
		    data:param,
		    success:function(data){
		    	//console.log("요청목록 삭제 완료", data);
		    	$("#notice").modal("hide");
		    	window.location.reload()
		    },
		    error:function(data){
		    	//alert("실패", data);
		    }
		}); 
}
/*현재 설정되어있는 키워드 출력*/
function keyword_Today(result){
  for(var i in result.code){
	    var code = document.createElement("div")
	    code.innerHTML ="<code>"+result.code[i].MSFRTN_TY_NM+"</code>"
	    document.getElementById("keyword").appendChild(code)
	    code.style.float="left"
	    code.style.padding="2px"
	    code.style.margin="2px"
	  }
	  for(var j in result.user){
	    var user = document.createElement("div")
	    user.innerHTML ="<mark>"+result.user[j].MNTRNG_KWRD+
	    "<button type='button' class='KWRD_Deletebtn' value='"
	    +result.user[j].MNTRNG_KWRD+"'onclick='WEBCRWL_MNFRNG_KWRD_DELETE(this.value);'><i class='fas fa-times m-l-5'></i></button></mark>"
	    document.getElementById("keyword").appendChild(user)
	    user.style.float="left"
		user.style.padding="1px"
		user.style.margin="2px"	    
	  }
}

/*  버튼을 클릭하여 수정 가능한 상태로 변경 - 관계기관*/
WEBCRWL_GVMAGNC_SYS_URL_ADDR_BTN.addEventListener('click', function(){
  WEBCRWL_GVMAGNC_SYS_URL_ADDR.readOnly = false;
})

/**엔터키를 입력하여 변경된 관계기관 URL 저장  */
function GVMAGNC_Enterkey() {
   if (window.event.keyCode == 13) { 
	   var regExp =  /^(https:\/\/)?((\w+)[.])+(asia|biz|cc|cn|com|de|eu|in|info|jobs|jp|kr|mobi|mx|name|net|nz|org|travel|tv|tw|uk|us)(\/(\w*))*$/i;
  	   var regExp2 =  /^(http:\/\/)?((\w+)[.])+(asia|biz|cc|cn|com|de|eu|in|info|jobs|jp|kr|mobi|mx|name|net|nz|org|travel|tv|tw|uk|us)(\/(\w*))*$/i;
  	
	   if(WEBCRWL_GVMAGNC_SYS_URL_ADDR.value.match(regExp) != null||WEBCRWL_GVMAGNC_SYS_URL_ADDR.value.match(regExp2) != null){
		   //console.log("right Email")
		    WEBCRWL_GVMAGNC_SYS_URL_ADDR.readOnly = true;
		    var param = "gvmagnc="+ WEBCRWL_GVMAGNC_SYS_URL_ADDR.value
		    //var param ={ 'url' : WEBCRWL_GVMAGNC_SYS_URL_ADDR.value }
		    //console.log("enter" , param)
  		    $.ajax({
				type : "POST",
			    url: "gvmagnc.do",
			    data : param,
			    success:function(data){
			    	console.log("관계기관 URL 변경 완료 ")
			    },
			    error:function(data){
			    	//alert("실패", data)
			    }
			})
		   
	   }else{
		   //console.log("Email error")
		   notice_context.innerHTML = "URL 입력이 잘못되었습니다 .\n URL 포맷을 확인하세요";
		   $("#notice").modal();
	   }
    } 
  }
/*  버튼을 클릭하여 대상포털 수정 가능한 상태로 변경*/
WEBCRWL_POTAL_URL_ADDR_BTN.addEventListener('click', function(){
  //console.log("potal click")
  WEBCRWL_POTAL_URL_ADDR.readOnly = false;
})

/**엔터키를 입력하여 변경된 대상포털 URL 저장  */
function POTAL_Enterkey() {
    if (window.event.keyCode == 13) { 
    	var regExp =  /^(https:\/\/)?((\w+)[.])+(asia|biz|cc|cn|com|de|eu|in|info|jobs|jp|kr|mobi|mx|name|net|nz|org|travel|tv|tw|uk|us)(\/(\w*))*$/i;
   		var regExp2 =  /^(http:\/\/)?((\w+)[.])+(asia|biz|cc|cn|com|de|eu|in|info|jobs|jp|kr|mobi|mx|name|net|nz|org|travel|tv|tw|uk|us)(\/(\w*))*$/i;
   		if(WEBCRWL_POTAL_URL_ADDR.value.match(regExp) != null || WEBCRWL_POTAL_URL_ADDR.value.match(regExp2) != null){
	   		 WEBCRWL_POTAL_URL_ADDR.readOnly = true;
	   		 var param ="potal="+ WEBCRWL_POTAL_URL_ADDR.value
	   		 //console.log(param)
			 $.ajax({
				type : "POST",
			   	url: "/potal_change.do",
			   	data : param,
			   	success:function(data){
			   		console.log("대상포털 변경 완료 ")
			   	},
			   	error:function(data){
			   		//alert("실패", data)
			   }
			}) 
    	}else{
			//console.log("Email error")
			notice_context.innerHTML = "URL 입력이 잘못되었습니다 .\n URL 포맷을 확인하세요";
			$("#notice").modal();
    	}
    }
}


  /*수집 주기 변경 */
 function WEBCRWL_COLCT_CYCLE_CHANGE(value){
	  //console.log("value", value)
	  var param ="cycle="+parseInt(value)
	  //var param = {"cycle": parseInt(value) }
	  //console.log("param",param ,typeof(param))
	  $.ajax({
			type : "POST",
		    url: "/cycle_change.do",
		    data:param,
		    success:function(data){
		    	console.log(data)
		    	//var result = JSON.parse(data);
		    	//console.log("수집주기", result)
		    },
		    error:function(data){
		    	//alert("실패", data)
		    }
		})
}
  
 /**수집 기간 변경 */
 function WEBCRWL_COLCT_PD_CHANGE(value){
	 var param ="period="+parseInt(value)
	 //var param = {"period": parseInt(value) }
	 console.log("param", param)
	  $.ajax({
			type : "POST",
		    url: "/pd_change.do",
		    data:param,
		    success:function(data){
		    	var result = JSON.parse(data);
		    	//console.log("수집 기간", result)
		    }
		})
 }
  /*수집 시점 변경*/
  function WEBCRWL_COLCT_YR_CHANGE(value){
	  var param ="year="+parseInt(value)
	  //var param ={"year" : value}
	  //console.log(param)
	  $.ajax({
			type : "POST",
		    url: "/yr_change.do",
		    data:param,
		    success:function(data){
		    	var result = JSON.parse(data);
		    	//console.log("수집 시점", result)
		    },
		    error:function(data){
		    	//alert("실패", data);
		    }
		}) 
  }
  /*키워드 추가 버튼 활성화*/
  const keywordForm = document.querySelector('#WEBCRWL_MNFRNG_KWRD');
  const addButton = document.querySelector('#WEBCRWL_MNFRNG_KWRD_ADD_BTN');

  keywordForm.addEventListener('keyup', listener);

  function listener() {
      switch (!(keywordForm.value)) {
          case true: addButton.disabled = true; $('#WEBCRWL_MNFRNG_KWRD_ADD_BTN').attr('class','btn btn-secondary btn-icon');  break;
          case false: addButton.disabled = false; $('#WEBCRWL_MNFRNG_KWRD_ADD_BTN').attr('class','btn btn-default btn-icon'); break;
      }
  }
  
  /*키워드 추가*/
  WEBCRWL_MNFRNG_KWRD_ADD_BTN.addEventListener('click', function(){
	var pattern = /\s/g; 
	var space_check = WEBCRWL_MNFRNG_KWRD.value.match(pattern)
	//console.log('space_check',space_check)
	if(WEBCRWL_MNFRNG_KWRD.value=="" || space_check!=null){
		notice_context.innerHTML = "키워드 입력이 잘못되었습니다 .";
		$("#notice").modal();
		return
	}
  //console.log("keword add click" ,WEBCRWL_MNFRNG_KWRD.value )
  var param = "keyword="+ WEBCRWL_MNFRNG_KWRD.value
  //console.log(param)
   $.ajax({
		type : "POST",
	    url: "/keyword_add.do",
	    data:param,
	    success:function(data){
	    	//console.log("키워드추가완료", data);
	    	window.location.reload()
	    },
	    error:function(data){
	    	//alert("실패", data);
	    }
	}) 
	})
/**키워드 삭제 */
function WEBCRWL_MNFRNG_KWRD_DELETE(value){
  //var param = {"keyword" : value}
  var param = "Delete_keyword="+ value
  //console.log("param",param)
      $.ajax({
		type : "DELETE",
	    url: "/keyword_delete.do",
	    data:param,
	    success:function(data){
	    	//console.log("키워드 삭제 완료", data);
	    	window.location.reload()
	    },
	    error:function(data){
	    	//alert("실패", data);
	    }
	}) 
}
</script>

</body>
</html>
<style>
table.dataTable thead th{
	font-weight: bold;
    text-transform: uppercase;
    font-size: 14px;
}
table.dataTable td {
  font-size: 15px;
}
.KWRD_Deletebtn{
  background: #FBDDAC;
  border: 1px solid #FBDDAC;
  padding: 0;
}
.news-wrap .center {
    width: 100%;
    color: #474747;
    font-size: 16px;
    font-weight: bold;
    text-align: center;
}
</style>