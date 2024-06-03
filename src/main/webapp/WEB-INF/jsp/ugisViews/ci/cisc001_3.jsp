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

<style>
#cmsc003-modal table.js-table-disaster-result tbody tr:hover {
	background-color: #ededed;
	cursor: pointer;
}
</style>

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
CMSC003 JS Dev
================================================ -->
	<script src="js/cm/cmsc003/src/cmsc003.js"></script>
<!-- 	<script src="js/cm/cmsc003/src/gis/gis.js"></script> -->
	<script src="js/cm/cmsc003/src/storage/storage.js"></script>
	<script src="js/cm/cmsc003/src/dom/dom.js"></script>
<!-- 	<script src="js/cm/cmsc003/src/converter/converter.js"></script> -->
<!-- 	<script src="js/cm/cmsc003/src/util/util.js"></script> -->



<script type="text/javascript">
$(document).ready(function() {
	
	// 생성용 재난 id 인풋 클릭
	$('#disasterIdCreate').click(function(e) {
		CMSC003.DOM.showDisasterIDSearchPopup('js-create-project');
	});
	
	// 재난 id 검색결과 클릭
	$('body').on('click', '#cmsc003-modal table.js-table-disaster-result tbody tr.js-create-project', function(e) {
		if ($(e.currentTarget).children('td').length === 1) {
			return;
		}
		var child = $(e.currentTarget).children('td:first-child');
// 		var sido = $(e.currentTarget).children('td:nth-child(5)').text();
// 		//	CMSC003.Storage.set('currentSido', sido);
// 		//	console.log(sido);
// 		var gugun = $(e.currentTarget).children('td:nth-child(6)').text();
// 		//	CMSC003.Storage.set('currentGugun', gugun);
// 		//	console.log(gugun);
// 		var dong = $(e.currentTarget).children('td:nth-child(7)').text();
// 		//	CMSC003.Storage.set('currentDong', dong);

// 		getSido(sido, gugun, dong, true);

// 		$('#detailAddrForm').val($(e.currentTarget).children('td:nth-child(8)').text());
		//	console.log(dong);
		$('#disasterIdCreate').val($(child).text());
// 		CMSC003.Storage.set('currentDisasterCode', $(child).attr('data'));
// 		var x = parseFloat($(child).attr('x'));
// 		var y = parseFloat($(child).attr('y'));

// 		var bbox = $(child).attr('bbox');
// 		var bbox_proj = $(child).attr('mapprjctncn');

// 		var positionToNumber = [x, y];

// 		var transform = CMSC003.GIS.transform(positionToNumber, 'EPSG:4326', 'EPSG:5179');
// 		CMSC003.Storage.set('currentDisasterSpot', positionToNumber);

// 		// 지도 중심점 이동
// 		CMSC003.GIS.setMapCenter(transform);

// 		// ROI 중심점 포인트 생성
// 		CMSC003.GIS.createROICenter(transform);
// 		//	CMSC003.GIS.createBufferROI(positionToNumber, 100);

// 		// ROI 생성(bbox 있을경우)
// 		if (bbox) {
// 			var transform_bbox = [];
// 			// String => Array(float)
// 			bbox.split(',').forEach(e => {
// 				transform_bbox.push(parseFloat(e));
// 			})

// 			// 좌표 변환
// 			if (bbox_proj != 'EPSG:5179') {
// 				transform_bbox = CMSC003.GIS.transformExtent(transform_bbox, bbox_proj, 'EPSG:5179');
// 			}

// 			// ROI 피처 생성
// 			CMSC003.GIS.createPolygonROIfrom5179(transform_bbox);

// 		}

		$('#cmsc003-modal').remove();
	})
	
	$("ul#topnav li").hover(function() { //Hover over event on list item
		$(this).css({ 'background' : '#1376c9 url(topnav_active.gif) repeat-x'}); //Add background color + image on hovered list item
		$(this).find("span").show(); //Show the subnav
	} , function() { //on hover out...
		$(this).css({ 'background' : 'none'}); //Ditch the background
		$(this).find("span").hide(); //Hide the subnav
	});

	//$('#txt_prj_path').prop('disabled', true);
	var schDate = '${schDate}';
	
	//20211106011452529
	$("#sDate").val(schDate.substring(0,8)+'01');
	$("#eDate").val('${schDate}');

});

function fnShowPopup(id){
	$('#'+id).modal("show");
}

function fnTabControl(num){
	
	$("#dev_project_create").hide();
	$("#dev_project_edit").hide();
	$("#dev_project_env").hide();
	
	if (num == 1){
		$("#dev_project_create").show();
	}else if (num == 2){
		$("#dev_project_edit").show();
	}else{
		
		$("#dev_project_algorithm").hide();
		for(var i=1; i < 7; i++){
			$("#dev_project_batch_"+i).hide();
		}
		$("#inlineRadio1").prop("checked", true);
		$("#dev_project_algorithm").show();		
		$("#dev_project_env").show();		
		fnSearchAlgorithmList();
		fnSearchAlgorithmRegList();
	}
}

function fnSubMenu(menu){

	$("#dev_project_algorithm").hide();
	for(var i=1; i < 7; i++){
		$("#dev_project_batch_"+i).hide();
	}
	
	if (menu < 5){
		fnSearchAlgorithmList();
		fnSearchAlgorithmRegList();
		$("#dev_project_algorithm").show();
	}else{
		$("#tbody_algorithm").html("");
		$("#tbody_algorithm_reg").html("");
		$("#dev_project_batch_"+(menu-4)).show();
		fnSearchScript(menu-4);
	}
	
}

//프로젝트 생성
function fnProjectCreate(){
	
	//필수입력 체크
// 	if ($("#txt_prj_name").val() == ""){
// 		alert('프로젝트명을 입력하세요');
// 		return;
// 	}
	
	// 재난 ID 입력 여부 확인
	if ($("#disasterIdCreate").val() == ""){
		alert('재난 ID를 입력하세요');
		return;
	}
	
	if( ! confirm("생성하시겠습니까? ")) {
		return;
	}

	var param={
			 // projectNm:$("#txt_prj_name").val(),
			 projectNm:$("#disasterIdCreate").val(),
			 projectCoursNm:$("#txt_prj_path").val(),
			 coordNm:$("#sel_coordinate").val(),
// 			 coordExpn:$("#sel_coordinate").val()
	};	 
	$("#progress").show();
	$.ajax({
		type : "POST",
	    url: "cisc001/createProject.do",
	    data:param,
	    success:function(data){
	    	if (data.result > 0){
	    		$("#progress").hide();
	    		alert('프로젝트가 생성 되었습니다.');
	    		return;
	    	}else{
	    		$("#progress").hide();
	    		alert('프로젝트 생성에 실패했습니다. 잠시후 다시 시도하세요');
	    		return;
	    	}
	    },
	    error:function(data){
	    	$("#progress").hide();
	    	alert('프로젝트 생성에 실패했습니다. 잠시후 다시 시도하세요');
    		return;
	    }
	});
	
	
	
}

function fnSelectCoordinate(val){

	$("#epsg").text('EPSG:'+val);
	
	if (val == '5185'){
		$("#lon_do").text(125);
	}else if (val == '5186'){
		$("#lon_do").text(127);
	}else if (val == '5187'){
		$("#lon_do").text(129);
	}else{
		$("#lon_do").text(131);
	}
	
}

function fnProjectContext(id, name, reg, coord){
	$("#project_id").val(id);
	$("#txt_project_name").val(name);
	$("#txt_project_date").val(reg);
	$("#sel_project_coord").val(coord);
	
	var param = {
		projectId:id
	}
	
	//선택한 프로젝트 WorkList 조회
	$("#progress").show();
	$.ajax({
		type : "POST",
	    url: "cisc001/searchProjectLog.do",
	    data:param,
	    success:function(data){
	    	for(var i=1; i < 7; i++ ){
	    		$("#tbody_user_work"+i).html("");
	    		$("#div_user_work"+i).hide();
	    	}
	    	//1.상대대기보정,2.절대방사보정,3.상대방사보정,4.컬러합성,5.히스토그램조정,6.모자이크
	    	var strHtml1 = "", strHtml2 = "", strHtml3 = "", strHtml4 = "", strHtml5 = "", strHtml6 = "";
			var workcnt1 = 0, workcnt2 = 0, workcnt3 = 0, workcnt4 = 0, workcnt5 = 0, workcnt6 = 0;
	    	$.each(data.resultList, function(index, item){
	    		if (item.workKind == '1'){  //상대대기보정
	    			workcnt1++;
	    			strHtml1 += '<tr>'
		    				+ '<td class="text-center"><input type="radio" name="radio_work1" value="'+item.projectLogId+'"></td>'	
		    				+ '<td class="text-center"> '+workcnt1+'</td>'
		    				+ '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.inputFileNm+'"> '+item.inputFileNm+'</td>'
		    				+ '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.algorithmNm+'"> '+item.algorithmNm+'</td>'
		    				+ '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.outputFileNm+'"> '+item.outputFileNm+'</td>'
		    				+ '<td class="text-center"> '+item.regDt+'</td>'
		    				+ '</tr>';
    						
	    		}else if (item.workKind == '2'){  //절대방사보정
	    			workcnt2++;
	    			strHtml2 += '<tr>'
		    				+ '<td class="text-center"><input type="radio" name="radio_work2" value="'+item.projectLogId+'"></td>'	
		    				+ '<td class="text-center"> '+workcnt2+'</td>'
		    				+ '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.inputFileNm+'"> '+item.inputFileNm+'</td>'
		    				+ '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.metaDataNm+'"> '+item.metaDataNm+'</td>'
		    				+ '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.radiatingFormula+'"> '+item.radiatingFormula+'</td>'
		    				+ '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.outputFileNm+'"> '+item.outputFileNm+'</td>'
		    				+ '<td class="text-center"> '+item.regDt+'</td>'
		    				+ '</tr>';
    						
	    		}else if (item.workKind == '3'){  //상대방사보정
	    			workcnt3++;
	    			strHtml3 += '<tr>'
		    				+ '<td class="text-center"><input type="radio" name="radio_work3" value="'+item.projectLogId+'"></td>'	
		    				+ '<td class="text-center"> '+workcnt3+'</td>'
		    				+ '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.inputFileNm+'"> '+item.inputFileNm+'</td>'
		    				+ '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.targetFileNm+'"> '+item.targetFileNm+'</td>'
		    				+ '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.algorithmNm+'"> '+item.algorithmNm+'</td>'
		    				+ '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.outputFileNm+'"> '+item.outputFileNm+'</td>'
		    				+ '<td class="text-center"> '+item.regDt+'</td>'
		    				+ '</tr>';
    						
	    		}else if (item.workKind == '4'){  //컬러합성
	    			workcnt4++;
	    			strHtml4 += '<tr>'
		    				+ '<td class="text-center"><input type="radio" name="radio_work4"></td>'	
		    				+ '<td class="text-center"> '+workcnt4+'</td>'
		    				+ '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.redBand+'"> '+item.redBand+'</td>'
		    				+ '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.greenBand+'"> '+item.greenBand+'</td>'
		    				+ '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.blueBand+'"> '+item.blueBand+'</td>'
		    				+ '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.outputFileNm+'"> '+item.outputFileNm+'</td>'
		    				+ '<td class="text-center"> '+item.regDt+'</td>'
		    				+ '</tr>';
	    		}else if (item.workKind == '5'){  //히스토그램조정
	    			workcnt5++;
	    			strHtml5 += '<tr>'
		    				+ '<td class="text-center"><input type="radio" name="radio_work5"></td>'	
		    				+ '<td class="text-center"> '+workcnt5+'</td>'
		    				+ '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.inputFileNm+'"> '+item.inputFileNm+'</td>'
		    				+ '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.controlType+'"> '+item.controlType+'</td>'
		    				+ '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.autoAreaControl+'"> '+item.autoAreaControl+'</td>'
		    				+ '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.algorithmNm+'"> '+item.algorithmNm+'</td>'
		    				+ '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.outputFileNm+'"> '+item.outputFileNm+'</td>'
		    				+ '<td class="text-center"> '+item.regDt+'</td>'
		    				+ '</tr>';
	    		}else if (item.workKind == '6'){  //모자이크
	    			workcnt6++;
	    			strHtml6 += '<tr>'
		    				+ '<td class="text-center"><input type="radio" name="radio_work6"></td>'	
		    				+ '<td class="text-center"> '+workcnt6+'</td>'
		    				+ '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.targetFileNm+'"> '+item.targetFileNm+'</td>'
		    				+ '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.inputFileNm+'"> '+item.inputFileNm+'</td>'
		    				+ '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.algorithmNm+'"> '+item.algorithmNm+'</td>'
		    				+ '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.outputFileNm+'"> '+item.outputFileNm+'</td>'
		    				+ '<td class="text-center"> '+item.regDt+'</td>'
		    				+ '</tr>';
	    		}
			});
			
	    	if (workcnt1 > 0){
	    		$("#div_user_work1").show();
	    		$("#tbody_user_work1").html(strHtml1);
	    	}
	    	if (workcnt2 > 0){
	    		$("#div_user_work2").show();
	    		$("#tbody_user_work2").html(strHtml2);
	    	}
	    	if (workcnt3 > 0){
	    		$("#div_user_work3").show();
	    		$("#tbody_user_work3").html(strHtml3);
	    	}
	    	if (workcnt4 > 0){
	    		$("#div_user_work4").show();
	    		$("#tbody_user_work4").html(strHtml4);
	    	}
	    	if (workcnt5 > 0){
	    		$("#div_user_work5").show();
	    		$("#tbody_user_work5").html(strHtml5);
	    	}
	    	if (workcnt6 > 0){
	    		$("#div_user_work6").show();
	    		$("#tbody_user_work6").html(strHtml6);
	    	}
	    	$("#progress").hide();
			
	    },
	    error:function(data){
	    	$("#progress").hide();
	    	alert("실패", data);
	    }
	});
	
	 
}

//프로젝트 검색
function fnProjectSearch(){
	
	if($("#sDate").val()==""){
		alert("시작일자를 입력하세요");
		return false;
	}
	
	if($("#eDate").val()==""){
		alert("종료일자를 입력하세요");
		return false;
	}
	
	var param={
		sdate:$("#sDate").val(),
		edate:$("#eDate").val()
    };
	$("#progress").show();
	$.ajax({
		type : "POST",
	    url: "cisc001/searchProject.do",
	    data:param,
	    success:function(data){
	    	var strHtml = ""; 
			$.each(data.resultList, function(index, item){
				strHtml += '<dd> <input type="radio" id="rad_'+item.projectId+'" value="'+item.projectId+'" name="radio_project"> <a href="#" onclick="javascript:fnProjectContext(\''+item.projectId+'\', \''+item.projectNm+'\', \''+item.regDt+'\', \''+item.coordNm+'\');"><label for="rad_'+item.projectId+'">'+item.projectNm+'</label></a></dd>';
			});
			
			$("#dl_project_list").html(strHtml);
			$("#progress").hide();
	    },
	    error:function(data){
	    	$("#progress").hide();
	    	alert("실패", data);
	    }
	});
	
}

function fnProjectInitVal(){
	$("#project_id").val("");
	$("#txt_project_name").val("");
	$("#sel_project_coord").val("");
}

function fnProjectUpdate(kind){
	
	if ($("#project_id").val() == ""){
		alert('선택한 데이터가 없습니다. 데이터를 선택하세요');
		return;
	}
	
	var flag = 'Y';
	
	if (kind == 'U'){  //수정
		if( ! confirm("수정하시겠습니까? ")) {
			return;
		}
		
	}else{  //삭제
		if( ! confirm("삭제하시겠습니까? ")) {
			return;
		}
		flag = 'N'
	}
	
	var param={
			 projectId:$("#project_id").val(),
			 projectNm:$("#txt_project_name").val(),
			 coordNm:$("#sel_project_coord").val(),
			 useYn:flag
	};
	
	$("#progress").show();
	$.ajax({
		type : "POST",
	    url: "cisc001/updateProject.do",
	    data:param,
	    success:function(data){
	    	$("#progress").hide();
	    	if (data.result > 0){
	    		alert('정상적으로 처리되었습니다.');
	    		fnProjectInitVal();
	    		fnProjectSearch();  //reSearch
	    	}else{
	    		alert('실패했습니다. 잠시후 다시 시도하세요');
	    		return;
	    	}
	    },
	    error:function(data){
	    	$("#progress").hide();
	    	alert('실패했습니다. 잠시후 다시 시도하세요');
    		return;
	    }
	});
	
}

function fnProjectChoice(){
	if($("input[name='radio_project']:checked").length ==0){
		alert("프로젝트를 선택하세요");
		return false;
	}
	
	if( ! confirm("선택한 프로젝트는 브라우저 종료시 까지 유지됩니다.\n선택하시겠습니까? ")) {
		return;
	}
	
	var projectid = $("input[name='radio_project']:checked").val() 
	$("#progress").show();
	$.ajax({
		type : "POST",
	    url: "cisc001/setProjectKey.do",
	    data:{"projectId":projectid},
	    success:function(data){
	    	$("#progress").hide();
	    	if (data.result == 'OK'){
	    		alert('프로젝트가 선택 되었습니다.');	
	    	}else{
	    		alert('프로젝트 선택에 실패했습니다. 잠시후 다시 시도하세요');
	    		return;
	    	}
	    	
	    	
	    },
	    error:function(data){
	    	$("#progress").hide();
	    	alert('프로젝트 선택에 실패했습니다. 잠시후 다시 시도하세요');
    		return;
	    }
	});
	
}


function fnWorkFlowDelete(work){
	
	if ($("input[name='radio_work"+work+"']:checked").length == 0){
		alert("삭제할 데이터를 선택하세요.");
		return false;
	}
	
	if( ! confirm("삭제하시겠습니까? ")) {
		return;
	}
	$("#progress").show();
	$.ajax({
		type : "POST",
	    url: "cisc001/deleteProjectLog.do",
	    data:{"projectLogId":$("input[name='radio_work"+work+"']:checked").val()},
	    success:function(data){
	    	$("#progress").hide();
	    	if (data.result > 0){
	    		//research
	    		fnProjectContext($("#project_id").val(),$("#txt_project_name").val(), $("#txt_project_date").val(),$("#sel_project_coord").val());
	    	}else{
	    		alert('실패했습니다. 잠시후 다시 시도하세요');
	    		return;
	    	}
	    },
	    error:function(data){
	    	$("#progress").hide();
	    	alert('실패했습니다. 잠시후 다시 시도하세요');
   		return;
	    }
	});
}

//등록대상 알고리즘 리스트 조회
function fnSearchAlgorithmRegList(){
	$("#progress").show();
	$.ajax({
		type : "POST",
	    url: "cisc001/searchAlgorithmRegList.do",
	    data:{},
	    success:function(data){
	    	var strHtml = ""; 
	    	
	    	$.each(data.resultList, function(index, item){
				strHtml += '<tr>'
						+  '<td class="text-center"><input type="radio" name="radio_algorithm_reg" value="'+item.algorithmRegId+'" > </td>'
						+  '<td class="text-center">'+(index+1)+'</td>'
						+  '<td class="text-center"><input type="text" class="form-control" id="reg_algoNm'+item.algorithmRegId+'" value="'+item.algorithmNm+'"></td>'
						+  '<td class="text-center"><input type="text" class="form-control" id="reg_verNm'+item.algorithmRegId+'" value="'+item.versionNm+'"></td>'
						+  '<td class="text-center">'+item.classNm+'<input type="hidden" id="reg_classNm'+item.algorithmRegId+'" value="'+item.classNm+'"></td>'
						+  '<td class="text-center"><input type="text" class="form-control" id="reg_algoExpln'+item.algorithmRegId+'" value="'+item.algorithmExpln+'"></td>'
						+  '<td class="text-center">'+item.regDt+'</td>'
						+  '</tr>';
			});
			
			$("#tbody_algorithm_reg").html(strHtml);
			$("#progress").hide();
	    },
	    error:function(data){
	    	$("#progress").hide();
	    	alert("실패", data);
	    }
	});
}

function fnAlgorithmReg(){
	
	if($("input[name='radio_algorithm_reg']:checked").length ==0){
		alert("등록할 알고리즘을 선택하세요.");
		return false;
	}
	
	if( ! confirm("등록하시겠습니까? ")) {
		return;
	}	
	
	var algorithmRegId = $("input[name='radio_algorithm_reg']:checked").val();
	
	var param = {
			"workKind":$("input[name='env_menu']:checked").val(),	
    		"algorithmNm":$("#reg_algoNm"+algorithmRegId).val(),
    		"versionNm":$("#reg_verNm"+algorithmRegId).val(),
    		"classNm":$("#reg_classNm"+algorithmRegId).val(),
    		"algorithmExpln":$("#reg_algoExpln"+algorithmRegId).val()
	}
	$("#progress").show();
	$.ajax({
		type : "POST",
	    url: "cisc001/insertAlgorithm.do",
	    data:param,
	    success:function(data){
	    	if (data.result > 0){
	    		//research
	    		fnSearchAlgorithmList();
	    		$("#progress").hide();
	    	}else{
	    		$("#progress").hide();
	    		alert('등록에 실패했습니다. 잠시후 다시 시도하세요');
	    		return;
	    	}
	    },
	    error:function(data){
	    	$("#progress").hide();
	    	alert('등록에 실패했습니다. 잠시후 다시 시도하세요');
   		return;
	    }
	});
	
}

//업무별 알고리즘 조회
function fnSearchAlgorithmList(){
	var work = $("input[name='env_menu']:checked").val();
	if (work == 1){
		$("#lb_algorithm_title").text("알고리즘 관리 - 상대대기보정");
	}else if (work == 2){
		$("#lb_algorithm_title").text("알고리즘 관리 - 상대방사보정");
	}else if (work == 3){
		$("#lb_algorithm_title").text("알고리즘 관리 - 히스토그램조정");
	}else{
		$("#lb_algorithm_title").text("알고리즘 관리 - 모자이크");
	}	
	$("#progress").show();
	$.ajax({
		type : "POST",
	    url: "cisc001/searchAlgorithmList.do",
	    data:{"workKind":work},
	    success:function(data){
	    	var strHtml = ""; 
	    	
	    	$.each(data.resultList, function(index, item){
	    		strHtml += '<tr>'
						+  '<td class="text-center"><input type="radio" name="radio_algorithm" value="'+item.algorithmId+'" > </td>'
						+  '<td class="text-center">'+(index+1)+'</td>'
						+  '<td class="text-left">'+item.algorithmNm+'<input type="hidden" id="algoNm'+item.algorithmId+'" value="'+item.algorithmNm+'"></td>'
						+  '<td class="text-center">'+item.versionNm+'<input type="hidden" id="verNm'+item.algorithmId+'" value="'+item.versionNm+'"></td>'
						+  '<td class="text-center">'+item.classNm+'</td>'
						+  '<td class="text-left">'+item.algorithmExpln+'<input type="hidden" id="algoExpln'+item.algorithmId+'" value="'+item.algorithmExpln+'"></td>'
						+  '<td class="text-center">'+item.regDt+'</td>'						
						+  '</tr>';
			});
			
			$("#tbody_algorithm").html(strHtml);
			$("#progress").hide();
	    },
	    error:function(data){
	    	$("#progress").hide();
	    	alert("실패", data);
	    }
	});
}

//등록된 알고리즘 삭제
function fnDeleteAlgorithm(){
	if ($("input[name='radio_algorithm']:checked").length == 0){
		alert("삭제할 데이터를 선택하세요.");
		return false;
	}
	
	if( ! confirm("삭제하시겠습니까? ")) {
		return;
	}
	
	var algorithmId = $("input[name='radio_algorithm']:checked").val();
	
	var param = {
			"algorithmId" : algorithmId
			,"algorithmNm" : $("#algoNm"+algorithmId).val()
			,"versionNm" : $("#verNm"+algorithmId).val()
			,"algorithmExpln" : $("#algoExpln"+algorithmId).val()
			,"useYn" : 'N'
	}
	$("#progress").show();
	$.ajax({
		type : "POST",
	    url: "cisc001/updateAlgorithm.do",
	    data:param,
	    success:function(data){
	    	$("#progress").hide();
	    	if (data.result > 0){
	    		fnSearchAlgorithmList();
	    	}else{
	    		alert('실패했습니다. 잠시후 다시 시도하세요');
	    		return;
	    	}
	    },
	    error:function(data){
	    	$("#progress").hide();
	    	alert('실패했습니다. 잠시후 다시 시도하세요');
   		return;
	    }
	});
}

//알고리즘 수정 팝업
function fnUpdateAlgorithmPopup(){
	if ($("input[name='radio_algorithm']:checked").length == 0){
		alert("수정할 데이터를 선택하세요.");
		return false;
	}
	var algorithmId = $("input[name='radio_algorithm']:checked").val();
	
 	$("#txt_update_algorithmNm").val($("#algoNm"+algorithmId).val());
	$("#txt_update_versionNm").val($("#verNm"+algorithmId).val());
	$("#txt_update_algorithmExpln").val($("#algoExpln"+algorithmId).val());
 	$("#algorithm_update_modal").modal("show");
}


//등록된 알고리즘 수정
function fnUpdateAlgorithm(){
	if ($("input[name='radio_algorithm']:checked").length == 0){
		alert("수정할 데이터를 선택하세요.");
		return false;
	}

	if( ! confirm("수정하시겠습니까? ")) {
		return;
	}
	
	var param = {
		algorithmId : $("input[name='radio_algorithm']:checked").val()
		,algorithmNm : $("#txt_update_algorithmNm").val()
		,versionNm : $("#txt_update_versionNm").val()
		,algorithmExpln : $("#txt_update_algorithmExpln").val()
		,useYn : 'Y'
	}
	$("#progress").show();
	$.ajax({
		type : "POST",
	    url: "cisc001/updateAlgorithm.do",
	    data:param,
	    success:function(data){
	    	$("#progress").hide();
	    	if (data.result > 0){
	    		alert('수정되었습니다.');
	    		$("#algorithm_update_modal").modal("hide");
	    		fnSearchAlgorithmList();
	    	}else{
	    		alert('실패했습니다. 잠시후 다시 시도하세요');
	    		return;
	    	}
	    },
	    error:function(data){
	    	$("#progress").hide();
	    	alert('실패했습니다. 잠시후 다시 시도하세요');
   		return;
	    }
	});
}


function fnScriptCreate(kind){
	if (kind == '1'){  //상대대기보정
		if ($("#script_atmosphere_script_name").val() == ""){
			alert('스크립트명[영문]을 입력하세요.');
			return false;
		}
		if ($("#script_atmosphere_input_file").val() == ""){
			alert('입력영상을 입력하세요.');
			return false;
		}
		if ($("#script_atmosphere_algorithm").val() == ""){
			alert('알고리즘을 입력하세요.');
			return false;
		}
		if ($("#script_atmosphere_output_file").val() == ""){
			alert('결과영상을 입력하세요.');
			return false;
		}
		if( ! confirm("스크립트를 생성하시겠습니까? ")) {
			return;
		}
		
		var param = {
				workKind:kind,  //1:대기보정,2:절대방사보정,3:상대방사보정
				scriptNm:$("#script_atmosphere_script_name").val(),
				inputFileNm:$("#script_atmosphere_input_file").val(),			
				outputFileNm:$("#script_atmosphere_output_file").val(),
				algorithmNm:$("#script_atmosphere_algorithm").val()
		}
		$("#progress").show();
		$.ajax({
			type : "POST",
		    url: "cisc001/createUpdateScript.do",
		    data:param,
		    success:function(data){
		    	$("#progress").hide();
		    	if (data.result > 0){
		    		alert('정상적으로 처리되었습니다.');
		    		fnSearchScript(kind);
		    	}else{

		    		if (data.result < 0){
		    			alert(data.msg);
		    		}else{
		    			alert('스크립트 생성에 실패했습니다. 잠시후 다시 시도하세요');
		    		}
		    	}
		    	
		    },
		    error:function(data){
		    	$("#progress").hide();
		    	alert('실패했습니다. 잠시후 다시 시도하세요');
	   		return;
		    }
		});
		
		
	}else if(kind == '2'){  //절대방사보정
		
		if ($("#script_absolute_script_name").val() == ""){
			alert('스크립트명[영문]을 입력하세요.');
			return false;
		}
		if ($("#script_absolute_input_file").val() == ""){
			alert('입력영상을 입력하세요.');
			return false;
		}
		if ($("#script_absolute_output_file").val() == ""){
			alert('출력영상을 입력하세요.');
			return false;
		}
		if ($("#script_absolute_meta").val() == ""){
			alert('메타데이터를 입력하세요.');
			return false;
		}		
		if ($("#script_absolute_gain").val() == ""){
			alert('gain을 입력하세요.');
			return false;
		}
		if ($("#script_absolute_offset").val() == ""){
			alert('offset을 입력하세요.');
			return false;
		}
		
		if ($("#script_absolute_satName").val() == "1"){  //Landsat
			if ($("#script_absolute_toa_file").val() == ""){
				alert('TOA 출력영상을 입력하세요.');
				return false;
			}
			if ($("#script_absolute_toa_gain").val() == ""){
				alert('TOA gain을 입력하세요.');
				return false;
			}
			if ($("#script_absolute_toa_offset").val() == ""){
				alert('TOA offset을 입력하세요.');
				return false;
			}
		}
		if( ! confirm("스크립트를 생성하시겠습니까? ")) {
			return;
		}
		
		var param = {
				scriptId:"",  //생성은 id ''
				workKind:kind,  //1:대기보정,2:절대방사보정,3:상대방사보정
				scriptNm:$("#script_absolute_script_name").val(),
				satKind:$("#script_absolute_satName").val(),  //1.Landsat, 2.Kompsat
				metaDataNm:$("#script_absolute_meta").val(),				
				inputFileNm:$("#script_absolute_input_file").val(),			
				outputFileNm:$("#script_absolute_output_file").val(),				
				gain:$("#script_absolute_gain").val(),
				offset:$("#script_absolute_offset").val(),
				reflectGain:$("#script_absolute_toa_gain").val(),
				reflectOffset:$("#script_absolute_toa_offset").val(),
				radiatingFormula:'Radiance = '+$("#script_absolute_gain").val()+' X DN + '+$("#script_absolute_offset").val(),
				toaOutputFileNm:$("#script_absolute_toa_file").val()
		}
		$("#progress").show();
		$.ajax({
			type : "POST",
		    url: "cisc001/createUpdateScript.do",
		    data:param,
		    success:function(data){
		    	$("#progress").hide();
		    	if (data.result > 0){
		    		alert('정상적으로 처리되었습니다.');
		    		fnSearchScript(kind);
		    	}else{
		    		if (data.result < 0){
		    			alert(data.msg);
		    		}else{
		    			alert('스크립트 생성에 실패했습니다. 잠시후 다시 시도하세요');
		    		}
		    	}
		    },
		    error:function(data){
		    	$("#progress").hide();
		    	alert('실패했습니다. 잠시후 다시 시도하세요');
	   			return;
		    }
		});
		
	}else if (kind == '3'){  //상대방사보정
	
		if ($("#script_relative_script_name").val() == ""){
			alert('스크립트명[영문]을 입력하세요.');
			return false;
		}
		if ($("#script_relative_source_file").val() == ""){
			alert('기준영상을 입력하세요.');
			return false;
		}
		if ($("#script_relative_target_file").val() == ""){
			alert('대상영상을 입력하세요.');
			return false;
		}
		if ($("#script_relative_algorithm").val() == ""){
			alert('알고리즘을 입력하세요.');
			return false;
		}
		if ($("#script_relative_output_file").val() == ""){
			alert('결과영상을 입력하세요.');
			return false;
		}
		if( ! confirm("스크립트를 생성하시겠습니까? ")) {
			return;
		}
		
		var param = {
				scriptId:"",
				workKind:kind,  //1:대기보정,2:절대방사보정,3:상대방사보정
				scriptNm:$("#script_relative_script_name").val(),
				histogramArea: $("input[name='histogram_radio']:checked").val(),
				inputFileNm:$("#script_relative_source_file").val(),			
				targetFileNm:$("#script_relative_target_file").val(),
				outputFileNm:$("#script_relative_output_file").val(),
				algorithmNm:$("#script_relative_algorithm").val()
		}
		$("#progress").show();
		$.ajax({
			type : "POST",
		    url: "cisc001/createUpdateScript.do",
		    data:param,
		    success:function(data){
		    	$("#progress").hide();
		    	if (data.result > 0){
		    		alert('정상적으로 처리되었습니다.');
		    		fnSearchScript(kind);
		    	}else{

		    		if (data.result < 0){
		    			alert(data.msg);
		    		}else{
		    			alert('스크립트 생성에 실패했습니다. 잠시후 다시 시도하세요');
		    		}
		    	}
		    	
		    },
		    error:function(data){
		    	$("#progress").hide();
		    	alert('실패했습니다. 잠시후 다시 시도하세요');
	   			return;
		    }
		});
	
	}else if (kind == '4'){  //컬러합성
		
		if ($("#script_color_script_name").val() == ""){
			alert('스크립트명[영문]을 입력하세요.');
			return false;
		}
		if ($("#script_color_red_band").val() == ""){
			alert('Red Band 를입력하세요.');
			return false;
		}
		if ($("#script_color_green_band").val() == ""){
			alert('Green Band 를입력하세요.');
			return false;
		}
		if ($("#script_color_blue_band").val() == ""){
			alert('Blue Band 를입력하세요.');
			return false;
		}
		if ($("#script_color_output_file").val() == ""){
			alert('결과영상을 입력하세요.');
			return false;
		}
		if( ! confirm("스크립트를 생성하시겠습니까? ")) {
			return;
		}
		
		var param = {
				scriptId:"",
				workKind:kind,  //1:대기보정,2:절대방사보정,3:상대방사보정,4:컬러합성
				scriptNm:$("#script_color_script_name").val(),
				inputFileNm:$("#script_color_red_band").val(),
				redBand:$("#script_color_red_band").val(),			
				greenBand:$("#script_color_green_band").val(),
				blueBand:$("#script_color_blue_band").val(),
				outputFileNm:$("#script_color_output_file").val()
		}
		$("#progress").show();
		$.ajax({
			type : "POST",
		    url: "cisc001/createUpdateScript.do",
		    data:param,
		    success:function(data){
		    	$("#progress").hide();
		    	if (data.result > 0){
		    		alert('정상적으로 처리되었습니다.');
		    		fnSearchScript(kind);
		    	}else{

		    		if (data.result < 0){
		    			alert(data.msg);
		    		}else{
		    			alert('스크립트 생성에 실패했습니다. 잠시후 다시 시도하세요');
		    		}
		    	}
		    	
		    },
		    error:function(data){
		    	$("#progress").hide();
		    	alert('실패했습니다. 잠시후 다시 시도하세요');
	   			return;
		    }
		});
	
	}else if (kind == '5'){  //히스토그램
		
		if ($("#script_histogram_script_name").val() == ""){
			alert('스크립트명[영문]을 입력하세요.');
			return false;
		}
		if ($("#script_histogram_input_file").val() == ""){
			alert('입력영상을 입력하세요.');
			return false;
		}
		if ($("#script_histogram_control_type").val() == ""){
			alert('조정방식을 입력하세요.');
			return false;
		}
		if ($("#script_histogram_auto_area_control").val() == ""){
			alert('자동영역설정 방법을 입력하세요.');
			return false;
		}
		if ($("#script_histogram_algorithm").val() == ""){
			alert('알고리즘을 입력하세요.');
			return false;
		}
		if ($("#script_histogram_output_file").val() == ""){
			alert('결과영상을 입력하세요.');
			return false;
		}
		if( ! confirm("스크립트를 생성하시겠습니까? ")) {
			return;
		}
		
		var param = {
				scriptId:"",
				workKind:kind,  //1:대기보정,2:절대방사보정,3:상대방사보정,4:컬러합성,5:히스토그램조정
				scriptNm:$("#script_histogram_script_name").val(),
				inputFileNm:$("#script_histogram_input_file").val(),
				controlType:$("#script_histogram_control_type").val(),
				autoAreaControl:$("#script_histogram_auto_area_control").val(),
				algorithmNm:$("#script_histogram_algorithm option:selected").val(),
				outputFileNm:$("#script_histogram_output_file").val(),
				auto:"true"
		}
		$("#progress").show();
		$.ajax({
			type : "POST",
		    url: "cisc001/createUpdateScript.do",
		    data:param,
		    success:function(data){
		    	$("#progress").hide();
		    	if (data.result > 0){
		    		alert('정상적으로 처리되었습니다.');
		    		fnSearchScript(kind);
		    	}else{

		    		if (data.result < 0){
		    			alert(data.msg);
		    		}else{
		    			alert('스크립트 생성에 실패했습니다. 잠시후 다시 시도하세요');
		    		}
		    	}
		    	
		    },
		    error:function(data){
		    	$("#progress").hide();
		    	alert('실패했습니다. 잠시후 다시 시도하세요');
	   			return;
		    }
		});
	} else{  //모자이크
		
		if ($("#script_mosaic_script_name").val() == ""){
			alert('스크립트명[영문]을 입력하세요.');
			return false;
		}
		if ($("#script_mosaic_input_file").val() == ""){
			alert('입력영상을 입력하세요.');
			return false;
		}
		/* if ($("#script_mosaic_input_file2").val() == ""){
			alert('입력영상2을 입력하세요.');
			return false;
		}
		if ($("#script_mosaic_input_file3").val() == ""){
			alert('입력영상3을 입력하세요.');
			return false;
		}
		if ($("#script_mosaic_input_file4").val() == ""){
			alert('입력영상4을 입력하세요.');
			return false;
		} */
		if ($("#script_mosaic_target_file").val() == ""){
			alert('대상영상을 입력하세요.');
			return false;
		}
		/* if ($("#script_mosaic_algorithm").val() == ""){
			alert('알고리즘을 입력하세요.');
			return false;
		} */
		if ($("#script_mosaic_output_file").val() == ""){
			alert('결과영상을 입력하세요.');
			return false;
		}
		if( ! confirm("스크립트를 생성하시겠습니까? ")) {
			return;
		}
		
		var param = {
				scriptId:"",
				workKind:kind,  //1:대기보정,2:절대방사보정,3:상대방사보정,4:컬러합성,5:히스토그램조정,6:모자이크
				scriptNm:$("#script_mosaic_script_name").val(),
				inputFileNm:$("#script_mosaic_input_file").val(),
				inputFileNm2:$("#script_mosaic_input_file2").val(),
				inputFileNm3:$("#script_mosaic_input_file3").val(),
				inputFileNm4:$("#script_mosaic_input_file4").val(),				
				targetFileNm:$("#script_mosaic_target_file").val(),  //대상영상
				//algorithmNm:$("#script_mosaic_algorithm").val(),
				outputFileNm:$("#script_mosaic_output_file").val()   //결과영상
		}
		$("#progress").show();
		$.ajax({
			type : "POST",
		    url: "cisc001/createUpdateScript.do",
		    data:param,
		    success:function(data){
		    	$("#progress").hide();
		    	if (data.result > 0){
		    		alert('정상적으로 처리되었습니다.');
		    		fnSearchScript(kind);
		    	}else{

		    		if (data.result < 0){
		    			alert(data.msg);
		    		}else{
		    			alert('스크립트 생성에 실패했습니다. 잠시후 다시 시도하세요');
		    		}
		    	}
		    	
		    },
		    error:function(data){
		    	$("#progress").hide();
		    	alert('실패했습니다. 잠시후 다시 시도하세요');
	   			return;
		    }
		});
		
	}
}

//업무별 스크립트 리스트 조회
function fnSearchScript(work){
	
	$("#progress").show();
	$.ajax({
		type : "POST",
	    url: "cisc001/searchScriptList.do",
	    data:{"workKind":work},
	    success:function(data){
	    	var strHtml = ""; 
	    	
	    	if (work == 1){
				 $.each(data.resultList, function(index, item){
		    		strHtml += '<tr>'
		    				+  '<td class="text-center"><input type="radio" name="radio_script_atmosphere" value="'+item.scriptId+'" > </td>'
							+  '<td class="text-center">'+(index+1)+'</td>'
							+  '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.scriptNm+'">'+item.scriptNm+'<input type="hidden" id="batch1_scriptNm'+item.scriptId+'" value="'+item.scriptNm+'"></td>'
							+  '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.inputFileNm+'">'+item.inputFileNm+'<input type="hidden" id="batch1_inputFileNm'+item.scriptId+'" value="'+item.inputFileNm+'"></td>'
							+  '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.algorithmNm+'">'+item.algorithmNm+'<input type="hidden" id="batch1_algorithmNm'+item.scriptId+'" value="'+item.algorithmNm+'"></td>'
							+  '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.outputFileNm+'">'+item.outputFileNm+'<input type="hidden" id="batch1_outputFileNm'+item.scriptId+'" value="'+item.outputFileNm+'"></td>'
							+  '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.extrlFileCoursNm+'">'+item.extrlFileCoursNm+'<input type="hidden" id="batch1_path'+item.scriptId+'" value="'+item.extrlFileCoursNm+'"></td>'
							+  '<td class="text-center" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.regDt+'">'+item.regDt+'</td>'						
							+  '</tr>';
				});
			    $("#tbody_atmosphere_batch").html(strHtml);
				
	    	}else if (work == 2){
		    	 
	    		$.each(data.resultList, function(index, item){
		    		
	    			var satKindNm = "Landsat";
	    			if (item.satKind == "2"){
	    				satKindNm = "Kompsat";
	    			}else if (item.satKind == "3"){
	    				satKindNm = "Sentinel-2";
	    			}
	    			strHtml += '<tr>'
							+  '<td class="text-center"><input type="radio" name="radio_script_absolute" value="'+item.scriptId+'" > </td>'
							+  '<td class="text-center">'+(index+1)+'</td>'
							+  '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.scriptNm+'">'+item.scriptNm+'<input type="hidden" id="batch2_scriptNm'+item.scriptId+'" value="'+item.scriptNm+'"></td>'
							+  '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.inputFileNm+'">'+item.inputFileNm+'<input type="hidden" id="batch2_inputFile'+item.scriptId+'" value="'+item.inputFileNm+'"></td>'
							+  '<td class="text-center" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+satKindNm+'">'+satKindNm+'<input type="hidden" id="batch2_satKind'+item.scriptId+'" value="'+item.satKind+'"></td>'
							+  '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.metaDataNm+'">'+item.metaDataNm+'<input type="hidden" id="batch2_meta'+item.scriptId+'" value="'+item.metaDataNm+'"></td>'
							+  '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.gain+'">'+item.gain+'<input type="hidden" id="batch2_gain'+item.scriptId+'" value="'+item.gain+'"></td>'
							+  '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.offset+'">'+item.offset+'<input type="hidden" id="batch2_offset'+item.scriptId+'" value="'+item.offset+'"></td>'
							+  '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.reflectGain+'">'+item.reflectGain+'<input type="hidden" id="batch2_refgain'+item.scriptId+'" value="'+item.reflectGain+'"></td>'
							+  '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.reflectOffset+'">'+item.reflectOffset+'<input type="hidden" id="batch2_refoff'+item.scriptId+'" value="'+item.reflectOffset+'"></td>'
							+  '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.outputFileNm+'">'+item.outputFileNm+'<input type="hidden" id="batch2_outputFile'+item.scriptId+'" value="'+item.outputFileNm+'"></td>'
							+  '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.toaOutputFileNm+'">'+item.toaOutputFileNm+'<input type="hidden" id="batch2_toaFile'+item.scriptId+'" value="'+item.toaOutputFileNm+'"></td>'
							+  '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.extrlFileCoursNm+'">'+item.extrlFileCoursNm+'<input type="hidden" id="batch2_path'+item.scriptId+'" value="'+item.extrlFileCoursNm+'"></td>'
							+  '<td class="text-center" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.regDt+'">'+item.regDt+'</td>'						
							+  '</tr>';
					});
		    	 
		    	 $("#tbody_absolute_batch").html(strHtml);
	    	}else if (work == 3){
		    	 $.each(data.resultList, function(index, item){
		    		
		    		 var histogramArea = "전체"
		    		 if (item.histogramArea == "2")	 {
		    			 histogramArea = "중첩영역"
		    		 }
		    		 strHtml += '<tr>'
		    			+  '<td class="text-center"><input type="radio" name="radio_script_relative" value="'+item.scriptId+'" > </td>'
							+  '<td class="text-center">'+(index+1)+'</td>'
							+  '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.scriptNm+'">'+item.scriptNm+'<input type="hidden" id="batch3_scriptNm'+item.scriptId+'" value="'+item.scriptNm+'"></td>'
							+  '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.inputFileNm+'">'+item.inputFileNm+'<input type="hidden" id="batch3_inputFileNm'+item.scriptId+'" value="'+item.inputFileNm+'"></td>'
							+  '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.targetFileNm+'">'+item.targetFileNm+'<input type="hidden" id="batch3_targetFileNm'+item.scriptId+'" value="'+item.targetFileNm+'"></td>'
							+  '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.algorithmNm+'">'+item.algorithmNm+'<input type="hidden" id="batch3_algorithmNm'+item.scriptId+'" value="'+item.algorithmNm+'"></td>'
							+  '<td class="text-center" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.histogramArea+'">'+histogramArea+'<input type="hidden" id="batch3_histogramArea'+item.scriptId+'" value="'+item.histogramArea+'"></td>'
							+  '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.outputFileNm+'">'+item.outputFileNm+'<input type="hidden" id="batch3_outputFileNm'+item.scriptId+'" value="'+item.outputFileNm+'"></td>'
							+  '<td class="text-center" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.extrlFileCoursNm+'">'+item.extrlFileCoursNm+'<input type="hidden" id="batch3_path'+item.scriptId+'" value="'+item.extrlFileCoursNm+'"></td>'
							+  '<td class="text-center" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.regDt+'">'+item.regDt+'</td>'						
							+  '</tr>';
				});
		    	$("#tbody_relative_batch").html(strHtml);
	    	}else if (work == 4){  //컬러합성
	    		$.each(data.resultList, function(index, item){
		    		strHtml += '<tr>'
		    			+  '<td class="text-center"><input type="radio" name="radio_script_color" value="'+item.scriptId+'" > </td>'
							+  '<td class="text-center">'+(index+1)+'</td>'
							+  '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.scriptNm+'">'+item.scriptNm+'<input type="hidden" id="batch4_scriptNm'+item.scriptId+'" value="'+item.scriptNm+'"></td>'
							+  '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.redBand+'">'+item.redBand+'<input type="hidden" id="batch4_redBand'+item.scriptId+'" value="'+item.redBand+'"></td>'
							+  '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.greenBand+'">'+item.greenBand+'<input type="hidden" id="batch4_greenBand'+item.scriptId+'" value="'+item.greenBand+'"></td>'
							+  '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.blueBand+'">'+item.blueBand+'<input type="hidden" id="batch4_blueBand'+item.scriptId+'" value="'+item.blueBand+'"></td>'
							+  '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.outputFileNm+'">'+item.outputFileNm+'<input type="hidden" id="batch4_outputFileNm'+item.scriptId+'" value="'+item.outputFileNm+'"></td>'
							+  '<td class="text-center" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.extrlFileCoursNm+'">'+item.extrlFileCoursNm+'<input type="hidden" id="batch4_path'+item.scriptId+'" value="'+item.extrlFileCoursNm+'"></td>'
							+  '<td class="text-center" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.regDt+'">'+item.regDt+'</td>'						
							+  '</tr>';
				});
		    	$("#tbody_color_batch").html(strHtml);
	    	}else if (work == 5){  //히스토그램
	    		$.each(data.resultList, function(index, item){
		    		
	    			var controlType = '일괄 조정';
	    			var autoAreaControl = 'MIN/MAX';
	    			
	    			if (item.controlType == 'band'){
	    				controlType = '밴드별 조정';
	    			}
					if (item.autoAreaControl == '1σ'){
						autoAreaControl = '1σ'
	    			}
	    			
	    			strHtml += '<tr>'
		    			+  '<td class="text-center"><input type="radio" name="radio_script_histogram" value="'+item.scriptId+'" > </td>'
							+  '<td class="text-center">'+(index+1)+'</td>'
							+  '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.scriptNm+'">'+item.scriptNm+'<input type="hidden" id="batch5_scriptNm'+item.scriptId+'" value="'+item.scriptNm+'"></td>'
							+  '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.inputFileNm+'">'+item.inputFileNm+'<input type="hidden" id="batch5_inputFileNm'+item.scriptId+'" value="'+item.inputFileNm+'"></td>'
							+  '<td class="text-center" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.controlType+'">'+controlType+'<input type="hidden" id="batch5_controlType'+item.scriptId+'" value="'+item.controlType+'"></td>'
							+  '<td class="text-center" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.autoAreaControl+'">'+autoAreaControl+'<input type="hidden" id="batch5_autoAreaControl'+item.scriptId+'" value="'+item.autoAreaControl+'"></td>'
							+  '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.algorithmNm+'">'+item.algorithmNm+'<input type="hidden" id="batch5_algorithmNm'+item.scriptId+'" value="'+item.algorithmNm+'"></td>'
							+  '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.outputFileNm+'">'+item.outputFileNm+'<input type="hidden" id="batch5_outputFileNm'+item.scriptId+'" value="'+item.outputFileNm+'"></td>'
							+  '<td class="text-center" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.extrlFileCoursNm+'">'+item.extrlFileCoursNm+'<input type="hidden" id="batch5_path'+item.scriptId+'" value="'+item.extrlFileCoursNm+'"></td>'
							+  '<td class="text-center" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.regDt+'">'+item.regDt+'</td>'						
							+  '</tr>';
				});
		    	$("#tbody_histogram_batch").html(strHtml);
	    	}else{
	    		$.each(data.resultList, function(index, item){
		    		strHtml += '<tr>'
		    			+  '<td class="text-center"><input type="radio" name="radio_script_mosaic" value="'+item.scriptId+'" > </td>'
							+  '<td class="text-center">'+(index+1)+'</td>'
							+  '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.scriptNm+'">'+item.scriptNm+'<input type="hidden" id="batch6_scriptNm'+item.scriptId+'" value="'+item.scriptNm+'"></td>'
							+  '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.targetFileNm+'">'+item.targetFileNm+'<input type="hidden" id="batch6_targetFileNm'+item.scriptId+'" value="'+item.targetFileNm+'"></td>'
							+  '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.inputFileNm+'">'+item.inputFileNm+'<input type="hidden" id="batch6_inputFileNm'+item.scriptId+'" value="'+item.inputFileNm+'"></td>'
							+  '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.inputFileNm2+'">'+item.inputFileNm2+'<input type="hidden" id="batch6_inputFileNm2'+item.scriptId+'" value="'+item.inputFileNm2+'"></td>'
							+  '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.inputFileNm3+'">'+item.inputFileNm3+'<input type="hidden" id="batch6_inputFileNm3'+item.scriptId+'" value="'+item.inputFileNm3+'"></td>'
							+  '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.inputFileNm4+'">'+item.inputFileNm4+'<input type="hidden" id="batch6_inputFileNm4'+item.scriptId+'" value="'+item.inputFileNm4+'"></td>'
							/* +  '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.algorithmNm+'">'+item.algorithmNm+'<input type="hidden" id="batch6_algorithmNm'+item.scriptId+'" value="'+item.algorithmNm+'"></td>' */
							+  '<td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.outputFileNm+'">'+item.outputFileNm+'<input type="hidden" id="batch6_outputFileNm'+item.scriptId+'" value="'+item.outputFileNm+'"></td>'
							+  '<td class="text-center" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.extrlFileCoursNm+'">'+item.extrlFileCoursNm+'<input type="hidden" id="batch6_path'+item.scriptId+'" value="'+item.extrlFileCoursNm+'"></td>'
							+  '<td class="text-center" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="'+item.regDt+'">'+item.regDt+'</td>'						
							+  '</tr>';
				});
		    	$("#tbody_mosaic_batch").html(strHtml);
	    	}
	    	$("#progress").hide();
			
	    },
	    error:function(data){
	    	$("#progress").hide();
	    	alert("실패", data);
	    }
	});
	
	
}


//스크립트 절대방사보정 수정 팝업
function fnScriptAbsoluteUpdatePopup(){
	if ($("input[name='radio_script_absolute']:checked").length == 0){
		alert("수정할 데이터를 선택하세요.");
		return false;
	}
	var scriptId = $("input[name='radio_script_absolute']:checked").val();
	
	$("#absolute_batch_scriptNm").val($("#batch2_scriptNm"+scriptId).val());
	$("#absolute_batch_inputFileNm").val($("#batch2_inputFile"+scriptId).val());
	$("#absolute_batch_mataDataNm").val($("#batch2_meta"+scriptId).val());
	$("#absolute_batch_gain").val($("#batch2_gain"+scriptId).val());
	$("#absolute_batch_offset").val($("#batch2_offset"+scriptId).val());
	$("#absolute_batch_outputFileNm").val($("#batch2_outputFile"+scriptId).val());
	
	if ($("#batch2_satKind"+scriptId).val() == "1"){
		$("#absolute_batch_reflectGain").val($("#batch2_refgain"+scriptId).val());
		$("#absolute_batch_reflectOffset").val($("#batch2_refoff"+scriptId).val());
		$("#absolute_batch_reflectOutputFileNm").val($("#batch2_toaFile"+scriptId).val());
		$("#div_absolute_batch").show();
	}else{
		$("#div_absolute_batch").hide();
	}
	
 	$("#script_absolute_update_modal").modal("show");
}


function fnScriptAbsoluteDelete(){
	
	if ($("input[name='radio_script_absolute']:checked").length == 0){
		alert("삭제할 데이터를 선택하세요.");
		return false;
	}
	if( ! confirm("삭제하시겠습니까? ")) {
		return;
	}
	var scriptId = $("input[name='radio_script_absolute']:checked").val();
	
	var param = {
	   scriptId:scriptId
	  ,workKind:'2'
	  ,extrlFileCoursNm:$("#batch2_path"+scriptId).val()
	  ,useYn:'N'
	}
	$("#progress").show();
	$.ajax({
		type : "POST",
	    url: "cisc001/deleteScript.do",
	    data:param,
	    success:function(data){
	    	$("#progress").hide();
	    	if (data.result > 0){
	    		alert('삭제되었습니다.');
	    		fnSearchScript(2);
	    	}else{
	    		alert('실패했습니다. 잠시후 다시 시도하세요');
	    		return;
	    	}
	    },
	    error:function(data){
	    	$("#progress").hide();
	    	alert('실패했습니다. 잠시후 다시 시도하세요');
   		return;
	    }
	});
}

//스크립트 관리 절대방사 보정 수정
function fnScriptAbsoluteUpdate(){
	
	if( ! confirm("수정하시겠습니까? ")) {
		return;
	}
	var scriptId = $("input[name='radio_script_absolute']:checked").val();
	
	
	var param = {
		scriptId:scriptId
	   ,workKind:'2'
	   ,scriptNm:$("#absolute_batch_scriptNm").val()
	   ,satKind:$("#batch2_satKind"+scriptId).val()
	   ,inputFileNm:$("#absolute_batch_inputFileNm").val()
	   ,metaDataNm:$("#absolute_batch_mataDataNm").val()
	   ,gain:$("#absolute_batch_gain").val()
	   ,offset:$("#absolute_batch_offset").val()
	   ,outputFileNm:$("#absolute_batch_outputFileNm").val()
	   ,reflectGain:$("#absolute_batch_reflectGain").val()
	   ,reflectOffset:$("#absolute_batch_reflectOffset").val()
	   ,toaOutputFileNm:$("#absolute_batch_reflectOutputFileNm").val()
	   ,extrlFileCoursNm:$("#batch2_path"+scriptId).val()
	}
	$("#progress").show();
	$.ajax({
		type : "POST",
	    url: "cisc001/createUpdateScript.do",
	    data:param,
	    success:function(data){
	    	$("#progress").hide();
	    	if (data.result > 0){
	    		alert('수정되었습니다.');
	    		$("#script_absolute_update_modal").modal("hide");
	    		fnSearchScript(2);
	    	}else{
	    		alert('실패했습니다. 잠시후 다시 시도하세요');
	    		return;
	    	}
	    },
	    error:function(data){
	    	$("#progress").hide();
	    	alert('실패했습니다. 잠시후 다시 시도하세요');
   		return;
	    }
	});
	
}


function fnChangeAbsoluteSatName(val){
	if (val == 2){  //Kompsat
		$("#div_reflectance_multiple").hide();
		$("#div_reflectance_addition").hide();
		$("#div_toa_output").hide();
		/* $("#lb_absolute_gain").text("Gain");
		$("#lb_absolute_offset").text("Offset"); */
		
		
	}else{ //Landsat
		$("#div_reflectance_multiple").show();
		$("#div_reflectance_addition").show();
		$("#div_toa_output").show();
		/* $("#lb_absolute_gain").text("TOA방사도 gain");
		$("#lb_absolute_offset").text("TOA방사도 offset"); */
	}
}


//스크립트 상대방사보정 수정 팝업
function fnScriptRelativeUpdatePopup(){
	if ($("input[name='radio_script_relative']:checked").length == 0){
		alert("수정할 데이터를 선택하세요.");
		return false;
	}
	var scriptId = $("input[name='radio_script_relative']:checked").val();
	
	$("#relative_batch_scriptNm").val($("#batch3_scriptNm"+scriptId).val());
	$("#relative_batch_inputFileNm").val($("#batch3_inputFileNm"+scriptId).val());
	$("#relative_batch_targetFileNm").val($("#batch3_targetFileNm"+scriptId).val());
	$("#relative_batch_algoritmNm").val($("#batch3_algorithmNm"+scriptId).val());
	$("#relative_batch_outputFileNm").val($("#batch3_outputFileNm"+scriptId).val());
	
	if ($("#batch3_algorithmNm"+scriptId).val() == "1"){
		$("#relative_batch_histogram_area_01").prop("checked" ,true);
	}else{
		$("#relative_batch_histogram_area_02").prop("checked" ,true);
	}
	
	
	
	$("#script_relative_update_modal").modal("show");
}

//스크립트 관리 상대방사 보정 수정
function fnScriptRelativeUpdate(){
	
	if( ! confirm("수정하시겠습니까? ")) {
		return;
	}
	var scriptId = $("input[name='radio_script_relative']:checked").val();
	
	var param = {
		scriptId:scriptId
	   ,workKind:'2'
	   ,scriptNm:$("#relative_batch_scriptNm").val()
	   ,satKind:''
	   ,inputFileNm:$("#relative_batch_inputFileNm").val()
	   ,algorithmNm:$("#relative_batch_algoritmNm").val()
	   ,outputFileNm:$("#relative_batch_outputFileNm").val()
	   ,extrlFileCoursNm:$("#batch3_path"+scriptId).val()
	}
	$("#progress").show();
	$.ajax({
		type : "POST",
	    url: "cisc001/createUpdateScript.do",
	    data:param,
	    success:function(data){
	    	$("#progress").hide();
	    	if (data.result > 0){
	    		alert('수정되었습니다.');
	    		$("#script_relative_update_modal").modal("hide");
	    		fnSearchScript(3);
	    	}else{
	    		alert('실패했습니다. 잠시후 다시 시도하세요');
	    		return;
	    	}
	    },
	    error:function(data){
	    	$("#progress").hide();
	    	alert('실패했습니다. 잠시후 다시 시도하세요');
   		return;
	    }
	});
	
}

function fnScriptRelativeDelete(){
	
	if ($("input[name='radio_script_relative']:checked").length == 0){
		alert("삭제할 데이터를 선택하세요.");
		return false;
	}
	if( ! confirm("삭제하시겠습니까? ")) {
		return;
	}
	var scriptId = $("input[name='radio_script_relative']:checked").val();
	
	var param = {
	   scriptId:scriptId
	  ,workKind:'3'
	  ,extrlFileCoursNm:$("#batch3_path"+scriptId).val()
	  ,useYn:'N'
	}
	$("#progress").show();
	$.ajax({
		type : "POST",
	    url: "cisc001/deleteScript.do",
	    data:param,
	    success:function(data){
	    	$("#progress").hide();
	    	if (data.result > 0){
	    		alert('삭제되었습니다.');
	    		fnSearchScript(3);
	    	}else{
	    		alert('실패했습니다. 잠시후 다시 시도하세요');
	    		return;
	    	}
	    },
	    error:function(data){
	    	$("#progress").hide();
	    	alert('실패했습니다. 잠시후 다시 시도하세요');
   		return;
	    }
	});
}

//스크립트 상대대기보정 수정 팝업
function fnScriptAtmosphereUpdatePopup(){
	if ($("input[name='radio_script_atmosphere']:checked").length == 0){
		alert("수정할 데이터를 선택하세요.");
		return false;
	}
	var scriptId = $("input[name='radio_script_atmosphere']:checked").val();
	
	
	$("#atmosphere_batch_scriptNm").val($("#batch1_scriptNm"+scriptId).val());
	$("#atmosphere_batch_inputFileNm").val($("#batch1_inputFileNm"+scriptId).val());
	$("#atmosphere_batch_algorithm").val($("#batch1_algorithmNm"+scriptId).val());
	$("#atmosphere_batch_outputFileNm").val($("#batch1_outputFileNm"+scriptId).val());
	
	$("#script_atmosphere_update_modal").modal("show");
}

//스크립트 관리 대기방사 보정 수정
function fnScriptAtmosphereUpdate(){
	
	if( ! confirm("수정하시겠습니까? ")) {
		return;
	}
	var scriptId = $("input[name='radio_script_atmosphere']:checked").val();
	
	var param = {
		scriptId:scriptId
	   ,workKind:'1'
	   ,scriptNm:$("#atmosphere_batch_scriptNm").val()
	   ,satKind:''
	   ,inputFileNm:$("#atmosphere_batch_inputFileNm").val()
	   ,algorithmNm:$("#atmosphere_batch_algorithm").val()
	   ,outputFileNm:$("#atmosphere_batch_outputFileNm").val()
	   ,extrlFileCoursNm:$("#batch1_path"+scriptId).val()
	}
	$("#progress").show();
	$.ajax({
		type : "POST",
	    url: "cisc001/createUpdateScript.do",
	    data:param,
	    success:function(data){
	    	$("#progress").hide();
	    	if (data.result > 0){
	    		alert('수정되었습니다.');
	    		$("#script_atmosphere_update_modal").modal("hide");
	    		fnSearchScript(1);
	    	}else{
	    		alert('실패했습니다. 잠시후 다시 시도하세요');
	    		return;
	    	}
	    },
	    error:function(data){
	    	$("#progress").hide();
	    	alert('실패했습니다. 잠시후 다시 시도하세요');
   		return;
	    }
	});
	
}

function fnScriptAtmosphereDelete(){
	
	if ($("input[name='radio_script_atmosphere']:checked").length == 0){
		alert("삭제할 데이터를 선택하세요.");
		return false;
	}
	if( ! confirm("삭제하시겠습니까? ")) {
		return;
	}
	var scriptId = $("input[name='radio_script_atmosphere']:checked").val();
	
	var param = {
	   scriptId:scriptId
	  ,workKind:'3'
	  ,extrlFileCoursNm:$("#batch1_path"+scriptId).val()
	  ,useYn:'N'
	}
	$("#progress").show();
	$.ajax({
		type : "POST",
	    url: "cisc001/deleteScript.do",
	    data:param,
	    success:function(data){
	    	$("#progress").hide();
	    	if (data.result > 0){
	    		alert('삭제되었습니다.');
	    		fnSearchScript(1);
	    	}else{
	    		alert('실패했습니다. 잠시후 다시 시도하세요');
	    		return;
	    	}
	    },
	    error:function(data){
	    	$("#progress").hide();
	    	alert('실패했습니다. 잠시후 다시 시도하세요');
   		return;
	    }
	});
}



//스크립트 컬러합성 수정 팝업
function fnScriptColorUpdatePopup(){
	if ($("input[name='radio_script_color']:checked").length == 0){
		alert("수정할 데이터를 선택하세요.");
		return false;
	}
	var scriptId = $("input[name='radio_script_color']:checked").val();
	
	$("#color_batch_scriptNm").val($("#batch4_scriptNm"+scriptId).val());
	$("#color_batch_redBand").val($("#batch4_redBand"+scriptId).val());
	$("#color_batch_greenBand").val($("#batch4_greenBand"+scriptId).val());
	$("#color_batch_blueBand").val($("#batch4_blueBand"+scriptId).val());
	$("#color_batch_outputFileNm").val($("#batch4_outputFileNm"+scriptId).val());
	
	
	$("#script_color_update_modal").modal("show");
}

//스크립트 관리 컬러합성 수정
function fnScriptColorUpdate(){
	
	if( ! confirm("수정하시겠습니까? ")) {
		return;
	}
	var scriptId = $("input[name='radio_script_color']:checked").val();
	
	var param = {
		scriptId:scriptId
	   ,workKind:'4'
	   ,scriptNm:$("#color_batch_scriptNm").val()
	   ,satKind:''
	   ,redBand:$("#color_batch_redBand").val()
	   ,greenBand:$("#color_batch_greenBand").val()
	   ,blueBand:$("#color_batch_blueBand").val()
	   ,outputFileNm:$("#color_batch_outputFileNm").val()
	   ,extrlFileCoursNm:$("#batch4_path"+scriptId).val()
	}
	$("#progress").show();
	$.ajax({
		type : "POST",
	    url: "cisc001/createUpdateScript.do",
	    data:param,
	    success:function(data){
	    	$("#progress").hide();
	    	if (data.result > 0){
	    		alert('수정되었습니다.');
	    		$("#script_color_update_modal").modal("hide");
	    		fnSearchScript(4);
	    	}else{
	    		alert('실패했습니다. 잠시후 다시 시도하세요');
	    		return;
	    	}
	    },
	    error:function(data){
	    	$("#progress").hide();
	    	alert('실패했습니다. 잠시후 다시 시도하세요');
   		return;
	    }
	});
	
}

function fnScriptColorDelete(){
	
	if ($("input[name='radio_script_color']:checked").length == 0){
		alert("삭제할 데이터를 선택하세요.");
		return false;
	}
	if( ! confirm("삭제하시겠습니까? ")) {
		return;
	}
	var scriptId = $("input[name='radio_script_color']:checked").val();
	
	var param = {
	   scriptId:scriptId
	  ,workKind:'4'
	  ,extrlFileCoursNm:$("#batch4_path"+scriptId).val()
	  ,useYn:'N'
	}
	$("#progress").show();
	$.ajax({
		type : "POST",
	    url: "cisc001/deleteScript.do",
	    data:param,
	    success:function(data){
	    	$("#progress").hide();
	    	if (data.result > 0){
	    		alert('삭제되었습니다.');
	    		fnSearchScript(4);
	    	}else{
	    		alert('실패했습니다. 잠시후 다시 시도하세요');
	    		return;
	    	}
	    },
	    error:function(data){
	    	$("#progress").hide();
	    	alert('실패했습니다. 잠시후 다시 시도하세요');
   		return;
	    }
	});
}

//스크립트 히스토그램 수정 팝업
function fnScriptHistogramUpdatePopup(){
	if ($("input[name='radio_script_histogram']:checked").length == 0){
		alert("수정할 데이터를 선택하세요.");
		return false;
	}
	var scriptId = $("input[name='radio_script_histogram']:checked").val();
	
	$("#histogram_batch_scriptNm").val($("#batch5_scriptNm"+scriptId).val());
	$("#histogram_batch_inputFileNm").val($("#batch5_inputFileNm"+scriptId).val());
	$("#histogram_batch_controlType").val($("#batch5_controlType"+scriptId).val());
	$("#histogram_batch_autoAreaControl").val($("#batch5_autoAreaControl"+scriptId).val());
	$("#histogram_batch_algorithm").val($("#batch5_algorithmNm"+scriptId).val());	
	$("#histogram_batch_outputFileNm").val($("#batch5_outputFileNm"+scriptId).val());
	
	$("#script_histogram_update_modal").modal("show");
}

//스크립트 관리 히스토그램 수정
function fnScriptHistogramUpdate(){
	
	if( ! confirm("수정하시겠습니까? ")) {
		return;
	}
	var scriptId = $("input[name='radio_script_histogram']:checked").val();
	
	var param = {
		scriptId:scriptId
	   ,workKind:'5'
	   ,scriptNm:$("#histogram_batch_scriptNm").val()
	   ,satKind:''
	   ,inputFileNm:$("#histogram_batch_inputFileNm").val()
	   ,controlType:$("#histogram_batch_controlType").val()
	   ,autoAreaControl:$("#histogram_batch_autoAreaControl").val()
	   ,algorithmNm:$("#histogram_batch_algorithm").val()
	   ,outputFileNm:$("#histogram_batch_outputFileNm").val()
	   ,extrlFileCoursNm:$("#batch5_path"+scriptId).val()
	}
	$("#progress").show();
	$.ajax({
		type : "POST",
	    url: "cisc001/createUpdateScript.do",
	    data:param,
	    success:function(data){
	    	$("#progress").hide();
	    	if (data.result > 0){
	    		alert('수정되었습니다.');
	    		$("#script_histogram_update_modal").modal("hide");
	    		fnSearchScript(5);
	    	}else{
	    		alert('실패했습니다. 잠시후 다시 시도하세요');
	    		return;
	    	}
	    },
	    error:function(data){
	    	$("#progress").hide();
	    	alert('실패했습니다. 잠시후 다시 시도하세요');
 		return;
	    }
	});
	
}

function fnScriptHistogramDelete(){
	
	if ($("input[name='radio_script_histogram']:checked").length == 0){
		alert("삭제할 데이터를 선택하세요.");
		return false;
	}
	if( ! confirm("삭제하시겠습니까? ")) {
		return;
	}
	var scriptId = $("input[name='radio_script_histogram']:checked").val();
	
	var param = {
	   scriptId:scriptId
	  ,workKind:'5'
	  ,extrlFileCoursNm:$("#batch5_path"+scriptId).val()
	  ,useYn:'N'
	}
	$("#progress").show();
	$.ajax({
		type : "POST",
	    url: "cisc001/deleteScript.do",
	    data:param,
	    success:function(data){
	    	$("#progress").hide();
	    	if (data.result > 0){
	    		alert('삭제되었습니다.');
	    		fnSearchScript(5);
	    	}else{
	    		alert('실패했습니다. 잠시후 다시 시도하세요');
	    		return;
	    	}
	    },
	    error:function(data){
	    	$("#progress").hide();
	    	alert('실패했습니다. 잠시후 다시 시도하세요');
 		return;
	    }
	});
}


//스크립트 모자이크 수정 팝업
function fnScriptMosaicUpdatePopup(){
	if ($("input[name='radio_script_mosaic']:checked").length == 0){
		alert("수정할 데이터를 선택하세요.");
		return false;
	}
	var scriptId = $("input[name='radio_script_mosaic']:checked").val();
	
	$("#mosaic_batch_scriptNm").val($("#batch6_scriptNm"+scriptId).val());
	$("#mosaic_batch_targetFileNm").val($("#batch6_targetFileNm"+scriptId).val());
	$("#mosaic_batch_inputFileNm").val($("#batch6_inputFileNm"+scriptId).val());
	$("#mosaic_batch_inputFileNm2").val($("#batch6_inputFileNm2"+scriptId).val());
	$("#mosaic_batch_inputFileNm3").val($("#batch6_inputFileNm3"+scriptId).val());
	$("#mosaic_batch_inputFileNm4").val($("#batch6_inputFileNm4"+scriptId).val());
	/* $("#mosaic_batch_algorithm").val($("#batch6_algorithmNm"+scriptId).val()); */	
	$("#mosaic_batch_outputFileNm").val($("#batch6_outputFileNm"+scriptId).val());
	
	$("#script_mosaic_update_modal").modal("show");
}

//스크립트 관리 모자이크수정
function fnScriptMosaicUpdate(){
	
	if( ! confirm("수정하시겠습니까? ")) {
		return;
	}
	var scriptId = $("input[name='radio_script_mosaic']:checked").val();
	
	var param = {
		scriptId:scriptId
	   ,workKind:'6'
	   ,scriptNm:$("#mosaic_batch_scriptNm").val()
	   ,satKind:''
	   ,targetFileNm:$("#mosaic_batch_targetFileNm").val()
	   ,inputFileNm:$("#mosaic_batch_inputFileNm").val()
	   ,inputFileNm2:$("#mosaic_batch_inputFileNm2").val()
	   ,inputFileNm3:$("#mosaic_batch_inputFileNm3").val()
	   ,inputFileNm4:$("#mosaic_batch_inputFileNm4").val()
	   /* ,algorithmNm:$("#mosaic_batch_algorithm").val() */
	   ,outputFileNm:$("#mosaic_batch_outputFileNm").val()
	   ,extrlFileCoursNm:$("#batch6_path"+scriptId).val()
	}
	$("#progress").show();
	$.ajax({
		type : "POST",
	    url: "cisc001/createUpdateScript.do",
	    data:param,
	    success:function(data){
	    	$("#progress").hide();
	    	if (data.result > 0){
	    		alert('수정되었습니다.');
	    		$("#script_mosaic_update_modal").modal("hide");
	    		fnSearchScript(6);
	    	}else{
	    		alert('실패했습니다. 잠시후 다시 시도하세요');
	    		return;
	    	}
	    },
	    error:function(data){
	    	$("#progress").hide();
	    	alert('실패했습니다. 잠시후 다시 시도하세요');
		return;
	    }
	});
	
}

function fnScriptMosaicDelete(){
	
	if ($("input[name='radio_script_mosaic']:checked").length == 0){
		alert("삭제할 데이터를 선택하세요.");
		return false;
	}
	if( ! confirm("삭제하시겠습니까? ")) {
		return;
	}
	var scriptId = $("input[name='radio_script_mosaic']:checked").val();
	
	var param = {
	   scriptId:scriptId
	  ,workKind:'6'
	  ,extrlFileCoursNm:$("#batch6_path"+scriptId).val()
	  ,useYn:'N'
	}
	$("#progress").show();
	$.ajax({
		type : "POST",
	    url: "cisc001/deleteScript.do",
	    data:param,
	    success:function(data){
	    	$("#progress").hide();
	    	if (data.result > 0){
	    		alert('삭제되었습니다.');
	    		fnSearchScript(6);
	    	}else{
	    		alert('실패했습니다. 잠시후 다시 시도하세요');
	    		return;
	    	}
	    },
	    error:function(data){
	    	$("#progress").hide();
	    	alert('실패했습니다. 잠시후 다시 시도하세요');
		return;
	    }
	});
}
</script>
</head>

<body>
	<!-- 상단메뉴 -->
	<%@ include file="../topMenu.jsp"%>

<div class="sidebar clearfix">
	<div role="tabpanel" class="sidepanel" style="display: block;">

		<!-- Nav tabs -->
		<ul class="nav nav-tabs" role="tablist">
			<li role="presentation" class="active" style="width:33.333%">
				<a href="#tab-01" aria-controls="tab-01" role="tab" data-toggle="tab" aria-expanded="true" class="active" onclick="fnTabControl(1);">
					<i class="fas fa-plus-square"></i><br>프로젝트 생성
				</a>
			</li>
			<li role="presentation" class="" style="width:33.333%">
				<a href="#tab-02" aria-controls="tab-02" role="tab" data-toggle="tab" class="" aria-expanded="false" onclick="fnTabControl(2);">
					<i class="fas fa-edit"></i><br>프로젝트 편집
				</a>
			</li>
			<li role="presentation" class="" style="width:33.333%">
				<a href="#tab-03" aria-controls="tab-03" role="tab" data-toggle="tab" class="" aria-expanded="false" onclick="fnTabControl(3);">
					<i class="fas fa-cog"></i><br>환경설정
				</a>
			</li>
		</ul>

		<!-- Tab panes -->
		<div class="tab-content">

			<!-- Start tab-01 -->
			<div role="tabpanel" class="tab-pane active" id="tab-01" >

				<div class="sidepanel-m-title">
					프로젝트
				</div>
				<div class="panel-body">
							
<!-- 					<div class="form-group m-t-10"> -->
<!--                         <label for="txt_prj_name" class="col-sm-3 control-label form-label">프로젝트명</label> -->
<!--                         <div class="col-sm-9"> -->
<!--                             <input id="txt_prj_name" type="text" class="form-control"> -->
<!--                         </div> -->
<!--                     </div> -->
                    
                    <div class="form-group m-t-6" style="margin-top: 8px;">
						<label class="col-sm-3 control-label form-label">재난 ID</label>
						<div class="col-sm-9">
							<input type="text" class="form-control" id="disasterIdCreate"
								name="disasterId" readOnly="readOnly"
								style="cursor: auto; background-color: #fff;">
						</div>
					</div>
							
                    <!-- <div class="form-group m-t-10">
                        <label for="input002" class="col-sm-3 control-label form-label">경로</label>
<<<<<<< .mine
						<div class="col-sm-7">
							<input type="text" class="form-control" id="txt_path" value="D:/***/***">
						</div>
						<div class="col-sm-2">
							<button type="button" class="btn btn-default btn-icon" onclick="fnFileDialog();"><i class="fas fa-ellipsis-h"></i></button>
||||||| .r211
=======
						<div class="col-sm-9">
							<input id="txt_prj_path" type="text" class="form-control" value="/Project/" disabled;>
>>>>>>> .r236
						</div>
                    </div> -->
				</div>
				<!-- <div class="sidepanel-s-title m-t-30">
				
				</div> -->
<!-- 				<div class="sidepanel-m-title m-t-30"> -->
<!-- 					좌표계 설정 -->
<!-- 				</div> -->
				<div class="panel-body">
							
<!-- 					<div class="form-group m-t-10"> -->
<!--                         <label for="input002" class="col-sm-3 control-label form-label">좌표계:</label> -->
<!--                         <div class="col-sm-9"> -->
<!--                             <select id="sel_coordinate" class="form-basic"  onchange="fnSelectCoordinate(this.value);"> -->
<!-- 								<option value="5185">EPSG:5185</option> -->
<!-- 								<option value="5186">EPSG:5186</option> -->
<!-- 								<option value="5187">EPSG:5187</option> -->
<!-- 								<option value="5188">EPSG:5188</option> -->
<!-- 							</select> -->
<!--                         </div> -->
<!--                     </div> -->
                    
                    <div class="form-group m-t-10 a-cent" >
						<button type="button" class="btn btn-default" onclick="fnProjectCreate();"><i class="fas fa-project-diagram m-r-5"></i>프로젝트 생성</button>
                    </div>
				</div>

			</div>
			<div role="tabpanel" class="tab-pane" id="tab-02">
				<div class="sidepanel-m-title">
					프로젝트 검색
				</div>
				<div class="panel-body">
							
					<div class="form-group m-t-10">
                        <label for="input002" class="col-sm-3 control-label form-label">생성일자</label>
                        <div class="col-sm-4">
                            <input type="text" id="sDate" class="form-control">
                        </div>
                        <div class="col-sm-1"><span class="txt-in-form">~</span></div>
                        <div class="col-sm-4">
                            <input type="text" id="eDate" class="form-control">
                        </div>
                    </div>
                    <script src="/js/datepicker.js"></script>
                    <script>
                        let sDate = new DatePicker(document.getElementById('sDate'));
                        let eDate = new DatePicker(document.getElementById('eDate'));
                    </script>
                    
                    <div class="sidepanel-s-title m-t-30 a-cent">
						<button type="button" class="btn btn-default" onclick="fnProjectSearch();"><i class="fas fa-search m-r-5"></i>검색</button>
					</div>
					
				</div>
				
				<div class="sidepanel-s-title m-t-10">
				검색 결과
				</div>
				<div class="panel-body">
					<div class="col-lg-12">
						<div class="panel panel-default" style="height:300px;">
							<dl id="dl_project_list" class="result-list-input">
							
							</dl>
						</div>
					</div>
					<div class="form-group m-t-10 a-cent" >
						<button type="button" class="btn btn-default" onclick="fnProjectChoice();"><i class="fas fa-check-square m-r-5"></i>선택</button>
					</div>
				</div>

			</div>
			<div role="tabpanel" class="tab-pane" id="tab-03">

				<div class="inside-panel panel-default">
					<!-- <div class="inside-panel-tit">
						알고리즘 관리
					</div> -->
	
					<div class="panel-body">
						<div class="col-lg-12">
							<div class="panel panel-default" style="height:200px;">
								<div class="in-panel-title">
								알고리즘 관리
								</div>
								<dl class="result-list-input">
									<dd>
										<div class="radio radio-info radio-inline">
											<input type="radio" id="inlineRadio1" value="1" name="env_menu" checked onclick="fnSubMenu(this.value);">
											<label for="inlineRadio1">상대대기보정</label>
										</div>										
									</dd>
									<dd>
										<div class="radio radio-info radio-inline">
											<input type="radio" id="inlineRadio2" value="2" name="env_menu" onclick="fnSubMenu(this.value);" >
											<label for="inlineRadio2">상대방사보정</label>
										</div>										
									</dd>
									<dd>
										<div class="radio radio-info radio-inline">
											<input type="radio" id="inlineRadio3" value="3" name="env_menu" onclick="fnSubMenu(this.value);">
											<label for="inlineRadio3">히스토그램조정</label>
										</div>										
									</dd>
									<dd>
										<div class="radio radio-info radio-inline">
											<input type="radio" id="inlineRadio4" value="4" name="env_menu" onclick="fnSubMenu(this.value);">
											<label for="inlineRadio4">모자이크</label>
										</div>										
									</dd>
								</dl>
							</div>
							<div class="panel panel-default" style="height:300px;">
								<div class="in-panel-title">
								배치 스크립트 관리
								</div>
								<dl class="result-list-input">
									<dd>
										<div class="radio radio-info radio-inline">
											<input type="radio" id="inlineRadio5" value="5" name="env_menu" onclick="fnSubMenu(this.value);" >
											<label for="inlineRadio5">상대대기보정</label>
										</div>										
									</dd>
									<dd>
										<div class="radio radio-info radio-inline">
											<input type="radio" id="inlineRadio6" value="6" name="env_menu" onclick="fnSubMenu(this.value);">
											<label for="inlineRadio6">절대방사보정</label>
										</div>										
									</dd>
									<dd>
										<div class="radio radio-info radio-inline">
											<input type="radio" id="inlineRadio7" value="7" name="env_menu" onclick="fnSubMenu(this.value);">
											<label for="inlineRadio7">상대방사보정</label>
										</div>										
									</dd>
									<dd>
										<div class="radio radio-info radio-inline">
											<input type="radio" id="inlineRadio8" value="8" name="env_menu" onclick="fnSubMenu(this.value);">
											<label for="inlineRadio8">컬러합성</label>
										</div>										
									</dd>
									<dd>
										<div class="radio radio-info radio-inline">
											<input type="radio" id="inlineRadio9" value="9" name="env_menu" onclick="fnSubMenu(this.value);">
											<label for="inlineRadio9">히스토그램조정</label>
										</div>										
									</dd>
									<dd>
										<div class="radio radio-info radio-inline">
											<input type="radio" id="inlineRadio10" value="10" name="env_menu" onclick="fnSubMenu(this.value);">
											<label for="inlineRadio10">모자이크</label>
										</div>										
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
<div id="content_body" class="content">
	<div id="dev_project_create">
		<div class="page-header">
			<h1 class="title"><label>프로젝트 생성</label></h1>
	
		</div>
		<div class="container-widget">
	
			<div class="col-md-12">
<!-- 				<div class="panel panel-default"> -->
<!-- 					<div class="panel-title"> -->
<!-- 						<i class="fas fa-list"></i>좌표계 정보 -->
<!-- 					</div> -->
<!-- 					<div class="panel-body table-responsive"> -->
<!-- 						<table class="table table-hover table-striped"> -->
<!-- 							<tbody id= "tbody_coordinate"> -->
<!-- 								<tr> -->
<!-- 									<td class="text-left"> -->
<!-- 											좌표계 명:<label id="epsg">EPSG:5185</label>(선택한 좌표계) <br> -->
<!-- 											타원체:GRS80 <br> -->
<!-- 											투영법:Transverse Mercator <br> -->
<!-- 											원점 : X = 38º N, Y = <label id="lon_do">125</label>º E <br> -->
<!-- 											가원점 X = 2000,000 m, Y=600,000 m <br>										 -->
<!-- 									</td> -->
<!-- 								</tr> -->
<!-- 							</tbody> -->
<!-- 						</table>     -->
<!-- 					</div> -->
<!-- 				</div> -->
			</div>
		</div>	
	</div>
	<div id="dev_project_edit" style="display:none;">
		<div class="page-header">
			<h1 class="title"><label>프로젝트 편집</label></h1>
		</div>
		<div class="container-widget">
			<div class="col-md-12">
				<div class="panel panel-default">
					<div class="panel-title">
						<i class="fas fa-list"></i>프로젝트 정보
					</div>
					<div class="panel-body table-responsive">
						<table class="table table-hover table-striped">
							<tbody>
								<tr>
									<td class="text-left">
										<div class="form-group m-t-1">
											<label for="txt_project_name" class="col-sm-1 control-label form-label">프로젝트명</label>
											<div class="col-sm-3"> 
												<input type="text" class="form-control" id="txt_project_name" value="">
											</div>
										</div>	
<!-- 										<div class="form-group m-t-1"> -->
<!-- 											<label for="sel_project_coord" class="col-sm-1 control-label form-label">좌표계</label> -->
<!-- 											<div class="col-sm-3">  -->
<!-- 												<select id="sel_project_coord" class="form-basic"> -->
<!-- 													<option value="5185">EPSG:5185</option> -->
<!-- 													<option value="5186">EPSG:5186</option> -->
<!-- 													<option value="5187">EPSG:5187</option> -->
<!-- 													<option value="5188">EPSG:5188</option> -->
<!-- 												</select> -->
<!-- 											</div> -->
<!-- 										</div> -->
										<input type="hidden" id="project_id" name="project_id" value="">
										
										<div class="form-group m-t-1">
											<label for="txt_project_date" class="col-sm-1 control-label form-label">생성일자</label>
											<div class="col-sm-3"> 
												<input type="text" class="form-control" id="txt_project_date" value="" disabled;>
											</div>
										</div>
										<div class="sidepanel-s-title m-t-10 a-cent">
											<button type="button" class="btn btn-default" onClick="fnProjectUpdate('U');"><i class="fas fa-save m-r-5"></i>저장</button>
											<button type="button" class="btn btn-default" onClick="fnProjectUpdate('D');" ><i class="fas fa-check-square m-r-5"></i>삭제</button>
										</div>	
																				
									</td>
								</tr>
							</tbody>
						</table>    
					</div>	
					<div id="div_user_work1" style="display:none;">
						<div class="panel-title">
							<i class="fas fa-list"></i>상대대기보정
						</div>						
						<div class="panel-body table-responsive">
							<table class="table table-hover table-striped"  style="table-layout: fixed">
								<colgroup>
									<col width="5%">
									<col width="5%">
									<col width="25%">
									<col width="25%">
									<col width="25%">
									<col width="*">
								</colgroup>
								<thead>
									<tr>
										<td class="text-center">선택</td>
										<td class="text-center">연번</td>
										<td class="text-center">입력영상</td>
										<td class="text-center">알고리즘</td>
										<td class="text-center">결과영상</td>
										<td class="text-center">처리일자</td>
									</tr>
								</thead>
								<tbody id="tbody_user_work1">
								</tbody>
							</table>    
						</div>
						<div class="sidepanel-s-title m-t-10 a-right">
							<button type="button" class="btn btn-default" onClick="fnWorkFlowDelete('1')"><i class="fas fa-check-square m-r-5"></i>삭제</button>
						</div>
					</div>
					<div id="div_user_work2" style="display:none;">
						<div class="panel-title">
							<i class="fas fa-list"></i>절대방사보정
						</div>
						
						<div class="panel-body table-responsive">
							<table class="table table-hover table-striped"  style="table-layout: fixed">
								<colgroup>
									<col width="5%">
									<col width="5%">
									<col width="20%">
									<col width="20%">
									<col width="20%">
									<col width="20%">
									<col width="*">
								</colgroup>
								<thead>
									<tr>
										<td class="text-center">선택</td>
										<td class="text-center">연번</td>
										<td class="text-center">입력영상</td>
										<td class="text-center">메타데이터</td>
										<td class="text-center">보정수식</td>
										<td class="text-center">결과영상</td>
										<td class="text-center">처리일자</td>
									</tr>
								</thead>
								<tbody id="tbody_user_work2">
								</tbody>
							</table>    
						</div>
						<div class="sidepanel-s-title m-t-10 a-right">
							<button type="button" class="btn btn-default" onClick="fnWorkFlowDelete('2')"><i class="fas fa-check-square m-r-5"></i>삭제</button>
						</div>
					</div>
					
					<div id="div_user_work3" style="display:none;">
						<div class="panel-title">
							<i class="fas fa-list"></i>상대방사보정
						</div>
						
						<div class="panel-body table-responsive">
							<table class="table table-hover table-striped" style="table-layout: fixed">
								<colgroup>
									<col width="5%">
									<col width="5%">
									<col width="20%">
									<col width="20%">
									<col width="20%">
									<col width="20%">
									<col width="*">
								</colgroup>
								<thead>
									<tr>
										<td class="text-center">선택</td>
										<td class="text-center">연번</td>
										<td class="text-center">입력영상</td>
										<td class="text-center">대상영상</td>
										<td class="text-center">알고리즘</td>
										<td class="text-center">결과영상</td>
										<td class="text-center">처리일자</td>
									</tr>
								</thead>
								<tbody id="tbody_user_work3">
									
								</tbody>
							</table>    
						</div>
						<div class="sidepanel-s-title m-t-10 a-right">
							<button type="button" class="btn btn-default" onClick="fnWorkFlowDelete('3')"><i class="fas fa-check-square m-r-5"></i>삭제</button>
						</div>
					</div>
					<div id="div_user_work4" style="display:none;">
						<div class="panel-title">
							<i class="fas fa-list"></i>컬러합성
						</div>
						
						<div class="panel-body table-responsive">
							<table class="table table-hover table-striped" style="table-layout: fixed">
								<colgroup>
									<col width="5%">
									<col width="5%">
									<col width="20%">
									<col width="20%">
									<col width="20%">
									<col width="20%">
									<col width="*">
								</colgroup>
								<thead>
									<tr>
										<td class="text-center">선택</td>
										<td class="text-center">연번</td>
										<td class="text-center">Red Band</td>
										<td class="text-center">Green Band</td>
										<td class="text-center">Blue Band</td>
										<td class="text-center">결과영상</td>
										<td class="text-center">처리일자</td>
									</tr>
								</thead>
								<tbody id="tbody_user_work4">
									
								</tbody>
							</table>    
						</div>
						<div class="sidepanel-s-title m-t-10 a-right">
							<button type="button" class="btn btn-default" onClick="fnWorkFlowDelete('4')"><i class="fas fa-check-square m-r-5"></i>삭제</button>
						</div>
					</div>
					<div id="div_user_work5" style="display:none;">
						<div class="panel-title">
							<i class="fas fa-list"></i>히스토그램조정
						</div>
						
						<div class="panel-body table-responsive">
							<table class="table table-hover table-striped" style="table-layout: fixed">
								<colgroup>
									<col width="5%">
									<col width="5%">
									<col width="20%">
									<col width="20%">
									<col width="20%">
									<col width="20%">
									<col width="*">
								</colgroup>
								<thead>
									<tr>
										<td class="text-center">선택</td>
										<td class="text-center">연번</td>
										<td class="text-center">입력영상</td>
										<td class="text-center">조장방식</td>
										<td class="text-center">자동영역설정</td>
										<td class="text-center">알고리즘</td>
										<td class="text-center">결과영상</td>
										<td class="text-center">처리일자</td>
									</tr>
								</thead>
								<tbody id="tbody_user_work5">
									
								</tbody>
							</table>    
						</div>
						<div class="sidepanel-s-title m-t-10 a-right">
							<button type="button" class="btn btn-default" onClick="fnWorkFlowDelete('5')"><i class="fas fa-check-square m-r-5"></i>삭제</button>
						</div>
					</div>
					<div id="div_user_work6" style="display:none;">
						<div class="panel-title">
							<i class="fas fa-list"></i>상대방사보정
						</div>
						
						<div class="panel-body table-responsive">
							<table class="table table-hover table-striped" style="table-layout: fixed">
								<colgroup>
									<col width="5%">
									<col width="5%">
									<col width="20%">
									<col width="20%">
									<col width="20%">
									<col width="20%">
									<col width="*">
								</colgroup>
								<thead>
									<tr>
										<td class="text-center">선택</td>
										<td class="text-center">연번</td>
										<td class="text-center">대상영상</td>
										<td class="text-center">입력영상</td>
										<td class="text-center">알고리즘</td>
										<td class="text-center">결과영상</td>
										<td class="text-center">처리일자</td>
									</tr>
								</thead>
								<tbody id="tbody_user_work6">
									
								</tbody>
							</table>    
						</div>
						<div class="sidepanel-s-title m-t-10 a-right">
							<button type="button" class="btn btn-default" onClick="fnWorkFlowDelete('6')"><i class="fas fa-check-square m-r-5"></i>삭제</button>
						</div>
					</div>
					
	
				</div>
			</div>	
		</div>
	</div>
	<div id="dev_project_env" style="display:none;">
		<!-- dev_project_algorithm start !!! -->
		<div id="dev_project_algorithm" style="display:none;">
			<div class="page-header">
				<h1 class="title"><label id="lb_algorithm_title">알고리즘 관리 -상대대기보정</label></h1>
			</div>
			<div class="container-widget">
		
				<div class="col-md-12">
					<div class="panel panel-default">
						<div class="panel-title">
							<i class="fas fa-list"></i>등록 알고리즘
						</div>
						<div class="panel-body table-responsive">
							<table class="table table-hover table-striped">
								<thead>
									<tr>
										<td class="text-center">선택</td>
										<td class="text-center">연번</td>
										<td class="text-center">알고리즘명</td>
										<td class="text-center">버전</td>
										<td class="text-center">클래스명</td>
										<td class="text-center">알고리즘 설명</td>
										<td class="text-center">등록일자</td>										
									</tr>
								</thead>
								<tbody id="tbody_algorithm">
									
								</tbody>
							</table>  
							
							<div class="btn-wrap a-right">
								<button type="button" class="btn btn-default" onclick="fnUpdateAlgorithmPopup();" ><i class="fas fa-save m-r-5"></i>수정</button>
								<button type="button" class="btn btn-default" onclick="fnDeleteAlgorithm();"><i class="fas fa-check-square m-r-5"></i>삭제</button>
							</div>	
							<div class="modal fade" id="algorithm_update_modal" tabindex="-1" role="dialog"  aria-hidden="true">
								<div class="modal-dialog modal-sm">
									<div class="modal-content">
										<div class="modal-header">
											<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
											<h4 class="modal-title">등록 알고리즘 수정</h4>
										</div>
										<div class="modal-body">
											<div class="panel-body">					
												<form class="">
												<div class="form-group m-t-10">
													<label class="col-sm-4 control-label form-label">알고리즘명</label>
													<div class="col-sm-8">
														  <input id="txt_update_algorithmNm" type="text" class="form-control"  value="">          
													</div>
												</div>
												<div class="form-group m-t-10">
													<label class="col-sm-4 control-label form-label">버전</label>
													<div class="col-sm-8">
														<input id="txt_update_versionNm" type="text" class="form-control"  value="">            
													</div>
												</div>
												<div class="form-group m-t-10">
													<label class="col-sm-4 control-label form-label">알고리즘 설명 </label>
													<div class="col-sm-8">
															<input id="txt_update_algorithmExpln" type="text" class="form-control" value="">            
													</div>											
												</div>
												</form> 
		
											</div>
										</div>
										<div class="modal-footer">
											<button type="button" class="btn btn-default" onclick="fnUpdateAlgorithm();" ><i class="fas fa-check-square m-r-5"></i>확인</button>
											<button type="button" class="btn btn-gray" data-dismiss="modal" aria-label="Close"><i class="fas fa-times m-r-5"></i>취소</button>
										</div>
									</div>
								</div>
							</div>				
						</div>
					</div>
				</div>
				<div class="col-md-12">
					<div class="panel panel-default">
						<div class="panel-title">
							<i class="fas fa-list"></i>알고리즘 목록
						</div>
						<div class="panel-body table-responsive">
							<table class="table table-hover table-striped">
								<colgroup>
									<col width="5%">
									<col width="5%">
									<col width="20%">
									<col width="5%">
									<col width="20%">
									<col width="*">
									<col width="10%">
								</colgroup>
								<thead>
									<tr>
										<td class="text-center">선택</td>
										<td class="text-center">연번</td>
										<td class="text-center">알고리즘명</td>
										<td class="text-center">버전</td>
										<td class="text-center">클래스명</td>										
										<td class="text-center">알고리즘 설명</td>
										<td class="text-center">등록일자</td>
									</tr>
								</thead>
								<tbody id ="tbody_algorithm_reg">
									
								</tbody>
							</table>  
							<div class="btn-wrap a-right">
								<button type="button" class="btn btn-default" onClick="fnAlgorithmReg();"><i class="fas fa-save m-r-5"></i>등록</button>
							</div> 
							
						</div>
			
					</div>
				</div>
			</div>
		</div>
		<!-- dev_project_algorithm end !!! -->
		<!-- dev_project_batch_1 start !!! -->
		<div id="dev_project_batch_1" style="display:none;">
			<div class="page-header">
				<h1 class="title"><label>배치 스크립트 관리 - 상대대기보정</label></h1>
			</div>
			<div class="container-widget">
				<div class="col-md-12">
					<div class="panel panel-default">
						<div class="panel-title">
							<i class="fas fa-list"></i>등록 알고리즘
						</div>
						<div class="panel-body table-responsive">
							<table class="table table-hover table-striped">
								<colgroup>
									<col width="60px;">
									<col width="60px;">
									<col width="120px">
									<col width="200px">
									<col width="120px">
									<col width="200px">									
									<col width="*">
									<col width="100px">
								</colgroup>
								<thead>
									<tr>
										<td class="text-center" nowrap>선택</td>
										<td class="text-center" nowrap>연번</td>
										<td class="text-center" nowrap>스크립트명</td>
										<td class="text-center" nowrap>입력영상</td>
										<td class="text-center" nowrap>알고리즘</td>
										<td class="text-center" nowrap>결과영상</td>
										<td class="text-center" nowrap>스크립트위치</td>
										<td class="text-center" nowrap>생성일자</td>
									</tr>
								</thead>
								<tbody id="tbody_atmosphere_batch">
									
								</tbody>
							</table>  
							
							<div class="btn-wrap a-right">
								<button type="button" class="btn btn-default" onclick="fnScriptAtmosphereUpdatePopup();" ><i class="fas fa-save m-r-5"></i>수정</button>
								<button type="button" class="btn btn-default" onclick="fnScriptAtmosphereDelete();"><i class="fas fa-check-square m-r-5"></i>삭제</button>
							</div>
							
							<div class="modal fade" id="script_atmosphere_update_modal" tabindex="-1" role="dialog"  aria-hidden="true">
								<div class="modal-dialog modal-sm">
									<div class="modal-content">
										<div class="modal-header">
											<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
											<h4 class="modal-title">등록 스크립트 수정</h4>
										</div>
										<div class="modal-body">
											<div class="panel-body">					
												<form class="">
												<div class="form-group m-t-10">
													<label class="col-sm-4 control-label form-label">스크립트명</label>
													<div class="col-sm-8">
														  <input id="atmosphere_batch_scriptNm" type="text" class="form-control"  value="">          
													</div>
												</div>
												<div class="form-group m-t-10">
													<label class="col-sm-4 control-label form-label">입력영상</label>
													<div class="col-sm-8">
														<input id="atmosphere_batch_inputFileNm" type="text" class="form-control"  value="">            
													</div>
												</div>
												<div class="form-group m-t-10">
													<label class="col-sm-4 control-label form-label">알고리즘 </label>
													<div class="col-sm-8">
														<select id="atmosphere_batch_algorithm" class="form-basic" >
															<option value="Histogram Matching">Histogram Matching</option>												
														</select>            
													</div>											
												</div>
												<div class="form-group m-t-10">
													<label class="col-sm-4 control-label form-label">결과영상</label>
													<div class="col-sm-8">
														<input id="atmosphere_batch_outputFileNm" type="text" class="form-control"  value="">            
													</div>
												</div>
												</form> 
		
											</div>
										</div>
										<div class="modal-footer">
											<button type="button" class="btn btn-default" ><i class="fas fa-check-square m-r-5"></i>확인</button>
											<button type="button" class="btn btn-gray" data-dismiss="modal" aria-label="Close"><i class="fas fa-times m-r-5"></i>취소</button>
										</div>
									</div>
								</div>
							</div>				
						</div>
					</div>
				</div>
				<div class="col-md-12">
					<div class="panel panel-default">
						<div class="panel-title">
							<i class="fas fa-list"></i>스크립트 생성
						</div>
						<div class="panel-body table-responsive">
							<table class="table table-hover table-striped">
								<tbody>
									<tr>
										<td class="text-left">
											<div class="form-group m-t-1">
												<label for="script_atmosphere_script_name" class="col-sm-3 control-label form-label">스크립트명</label>
												<div class="col-sm-7"> 
													<input id="script_atmosphere_script_name" type="text" class="form-control" value="">
												</div>
											</div>	
											<div class="form-group m-t-1">
												<label for="script_atmosphere_input_file" class="col-sm-3 control-label form-label">입력영상</label>
												<div class="col-sm-7"> 
													<input id= "script_atmosphere_input_file" type="text" class="form-control" value="">
												</div>
											</div>
										</td>
										<td>	
											<div class="form-group m-t-1">
												<label for="script_atmosphere_algorithm" class="col-sm-3 control-label form-label">알고리즘</label>
												<div class="col-sm-7"> 
													<select id="script_atmosphere_algorithm" class="form-basic" >
														<option value="Histogram Matching">Histogram Matching</option>												
													</select>
												</div>
											</div>
											<div class="form-group m-t-1">
												<label for="script_atmosphere_output_file" class="col-sm-3 control-label form-label">결과영상</label>
												<div class="col-sm-7"> 
													<input id="script_atmosphere_output_file" type="text" class="form-control" value="">
												</div>
											</div>
										</td>
									</tr>
									<tr>
										<td colspan=2>
											<div class="sidepanel-s-title m-t-1 a-cent">
												<button type="button" class="btn btn-default" onClick="fnScriptCreate('1');"><i class="fas fa-save m-r-5"></i>생성</button>
											</div>
										</td>		
									</tr>
								</tbody>
							</table>    
						</div>
					</div>
				</div>
								
			</div>
		</div>  
		<!-- dev_project_batch_1 end !!! -->
		<!-- dev_project_batch_2 start !!! -->
		<div id="dev_project_batch_2" style="display:none;">
			<div class="page-header">
				<h1 class="title"><label>배치 스크립트 관리 - 절대방사보정</label></h1>
			</div>
			<div class="container-widget">
				<div class="col-md-12">
					<div class="panel panel-default">
						<div class="panel-title">
							<i class="fas fa-list"></i>등록 스크립트
						</div>
						<div class="panel-body table-responsive">
							<table class="table table-hover table-striped">
								<colgroup>
									<col width="60px;">
									<col width="60px;">
									<col width="100px">
									<col width="100px">
									<col width="100px">
									<col width="100px">
									<col width="60px">
									<col width="60px">
									<col width="*">
								</colgroup>
								<thead>
									<tr>
										<td class="text-center" nowrap>선택</td>
										<td class="text-center" nowrap>연번</td>
										<td class="text-center" nowrap>스크립트명</td>
										<td class="text-center" nowrap>입력영상</td>
										<td class="text-center" nowrap>위성영상</td>
										<td class="text-center" nowrap>메타데이터</td>
										<td class="text-center" nowrap>gain</td>
										<td class="text-center" nowrap>offset</td>
										<td class="text-center" nowrap>TOA Gain</td>
										<td class="text-center" nowrap>TOA Offset</td>
										<td class="text-center" nowrap>결과영상</td>
										<td class="text-center" nowrap>TOA결과영상</td>
										<td class="text-center" nowrap>스크립트위치</td>
										<td class="text-center" nowrap>생성일자</td>
									</tr>
								</thead>
								<tbody id="tbody_absolute_batch">
									
								</tbody>
							</table>  
							
							<div class="btn-wrap a-right">
								<button type="button" class="btn btn-default" onclick="fnScriptAbsoluteUpdatePopup();" ><i class="fas fa-save m-r-5"></i>수정</button>
								<button type="button" class="btn btn-default" onclick="fnScriptAbsoluteDelete();"><i class="fas fa-check-square m-r-5"></i>삭제</button>
							</div>	
							<div class="modal fade" id="script_absolute_update_modal" tabindex="-1" role="dialog"  aria-hidden="true">
								<div class="modal-dialog modal-sm">
									<div class="modal-content">
										<div class="modal-header">
											<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
											<h4 class="modal-title">등록 스크립트 수정</h4>
										</div>
										<div class="modal-body">
											<div class="panel-body">					
												<form class="">
												<div class="form-group m-t-10">
													<label class="col-sm-4 control-label form-label">스크립트명</label>
													<div class="col-sm-8">
														  <input id="absolute_batch_scriptNm" type="text" class="form-control"  value="">          
													</div>
												</div>
												<!-- <div class="form-group m-t-10">
													<label class="col-sm-4 control-label form-label">위성영상</label>
													<div class="col-sm-8">
														<input id="absolute_batch_satKind" type="text" class="form-control"  value="">            
													</div>
												</div> -->
												<div class="form-group m-t-10">
													<label class="col-sm-4 control-label form-label">입력영상</label>
													<div class="col-sm-8">
														<input id="absolute_batch_inputFileNm" type="text" class="form-control"  value="">            
													</div>
												</div>
												<div class="form-group m-t-10">
													<label class="col-sm-4 control-label form-label">메타데이터 </label>
													<div class="col-sm-8">
														<input id="absolute_batch_mataDataNm" type="text" class="form-control"  value="">            
													</div>										
												</div>
												<div class="form-group m-t-10">
													<label class="col-sm-4 control-label form-label">Gain </label>
													<div class="col-sm-8">
														<input id="absolute_batch_gain" type="text" class="form-control"  value="">            
													</div>										
												</div>
												<div class="form-group m-t-10">
													<label class="col-sm-4 control-label form-label">Offset </label>
													<div class="col-sm-8">
														<input id="absolute_batch_offset" type="text" class="form-control"  value="">            
													</div>										
												</div>
												<div class="form-group m-t-10">
													<label class="col-sm-4 control-label form-label">결과영상</label>
													<div class="col-sm-8">
														<input id="absolute_batch_outputFileNm" type="text" class="form-control"  value="">            
													</div>
												</div>
												<div id="div_absolute_batch"  style="display:none;">
												<div class="form-group m-t-10">
													<label class="col-sm-4 control-label form-label">TOA Gain </label>
													<div class="col-sm-8">
														<input id="absolute_batch_reflectGain" type="text" class="form-control"  value="">            
													</div>										
												</div>
												<div class="form-group m-t-10">
													<label class="col-sm-4 control-label form-label">TOA Offset </label>
													<div class="col-sm-8">
														<input id="absolute_batch_reflectOffset" type="text" class="form-control"  value="">            
													</div>										
												</div>
												<div class="form-group m-t-10">
													<label class="col-sm-4 control-label form-label">TOA 결과영상 </label>
													<div class="col-sm-8">
														<input id="absolute_batch_reflectOutputFileNm" type="text" class="form-control"  value="">            
													</div>										
												</div>
												</div>
												</form> 
		
											</div>
										</div>
										<div class="modal-footer">
											<button type="button" class="btn btn-default" onclick="fnScriptAbsoluteUpdate();"><i class="fas fa-check-square m-r-5"></i>확인</button>
											<button type="button" class="btn btn-gray" data-dismiss="modal" aria-label="Close"><i class="fas fa-times m-r-5"></i>취소</button>
										</div>
									</div>
								</div>
							</div>				
						</div>
					</div>
				</div>
				<div class="col-md-12">
				<div class="panel panel-default">
					<div class="panel-title">
						<i class="fas fa-list"></i>스크립트 생성
					</div>
					<div class="panel-body table-responsive">
						<table class="table table-hover table-striped">
							<colgroup>
								<col width="50%">
								<col width="*">
							</colgroup>
							<tbody>
								<tr>
									
									<td class="text-left">
										<div class="form-group m-t-1">
											<label for="absolute_algorithm" class="col-sm-3 control-label form-label">위성영상</label>
											<div class="col-sm-7"> 
												<select id="script_absolute_satName" class="form-basic" onchange="fnChangeAbsoluteSatName(this.value)">
													<option value="2">Kompsat</option>												
													<option value="1">Landsat</option>
												</select>
											</div>
										</div>
										<div class="form-group m-t-1">
											<label for="script_absolute_script_name" class="col-sm-3 control-label form-label">스크립트명</label>
											<div class="col-sm-7"> 
												<input id="script_absolute_script_name"  type="text" class="form-control" value="">
											</div>
										</div>
										<div class="form-group m-t-1">
											<label for="script_absolute_input_file" class="col-sm-3 control-label form-label">입력영상</label>
											<div class="col-sm-7"> 
												<input id="script_absolute_input_file" type="text" class="form-control"  value="">
											</div>
										</div>
										<div class="form-group m-t-1">
											<label for="script_absolute_output_file" class="col-sm-3 control-label form-label">결과영상</label>
											<div class="col-sm-7"> 
												<input type="text" id="script_absolute_output_file" class="form-control"  value="">
											</div>
										</div>
										<div id="div_toa_output" class="form-group m-t-1" style="display:none;">
											<label for="script_absolute_toa_file" class="col-sm-3 control-label form-label">TOA 결과영상</label>
											<div class="col-sm-7"> 
												<input type="text" id="script_absolute_toa_file" class="form-control"  value="">
											</div>
										</div>
									</td>
									<td>
										<div class="form-group m-t-1">
											<label for="script_absolute_meta" class="col-sm-3 control-label form-label">메타데이터</label>
											<div class="col-sm-7"> 
												<input id="script_absolute_meta" type="text" class="form-control"  value="">
											</div>
										</div>
										<div class="form-group m-t-1">
											<label id="lb_absolute_gain"  for="script_absolute_gain" class="col-sm-3 control-label form-label">Gain</label>
											<div class="col-sm-7"> 
												<input id="script_absolute_gain" type="text" class="form-control"  value="">
											</div>
										</div>
										<div class="form-group m-t-1">
											<label id="lb_absolute_offset" for="script_absolute_offset" class="col-sm-3 control-label form-label">Offset</label>
											<div class="col-sm-7"> 
												<input id="script_absolute_offset" type="text" class="form-control"  value="">
											</div>
										</div>										
										
										<div id="div_reflectance_multiple" class="form-group m-t-1" style="display:none;">
											<label for="script_absolute_toa_gain" class="col-sm-3 control-label form-label">TOA Gain</label>
											<div class="col-sm-7"> 
												<input type="text" id="script_absolute_toa_gain" class="form-control"  value="">
											</div>
										</div>
										<div id="div_reflectance_addition" class="form-group m-t-1" style="display:none;">
											<label for="script_absolute_toa_offset" class="col-sm-3 control-label form-label">TOA Offset</label>
											<div class="col-sm-7"> 
												<input type="text" id="script_absolute_toa_offset" class="form-control"  value="">
											</div>
										</div>										

									</td>
								</tr>
								<tr>
									<td colspan=2>
										<div class="sidepanel-s-title m-t-1 a-cent">
											<button type="button" class="btn btn-default" onClick="fnScriptCreate('2');"><i class="fas fa-save m-r-5"></i>생성</button>
										</div>
									</td>
								</tr>
							</tbody>
						</table>    
					</div>
				</div>
			</div>
			</div>
		</div>  
		<!-- dev_project_batch_2 end !!! -->
		<!-- dev_project_batch_3 start !!! -->
		<div id="dev_project_batch_3" style="display:none;">
			<div class="page-header">
				<h1 class="title"><label>배치 스크립트 관리 - 상대방사보정</label></h1>
			</div>
			<div class="container-widget">
				<div class="col-md-12">
					<div class="panel panel-default">
						<div class="panel-title">
							<i class="fas fa-list"></i>등록 스크립트
						</div>
						<div class="panel-body table-responsive">
						<!-- style="table-layout: fixed" -->
							<table class="table table-hover table-striped">
								<colgroup>
									<col width="60px;">
									<col width="60px;">
									<col width="150px">
									<col width="150px">
									<col width="150px">
									<col width="100px">									
									<col width="100px">
									<col width="150px">
									<col width="*">
									<col width="120px">
								</colgroup>
								<thead>
									<tr>
										<td class="text-center" nowrap>선택</td>
										<td class="text-center" nowrap>연번</td>
										<td class="text-center" nowrap>스크립트명</td>
										<td class="text-center" nowrap>기준영상</td>
										<td class="text-center" nowrap>대상영상</td>
										<td class="text-center" nowrap>알고리즘</td>
										<td class="text-center" nowrap>히스토그램영역</td>										
										<td class="text-center" nowrap>결과영상</td>
										<td class="text-center" nowrap>스크립트위치</td>
										<td class="text-center" nowrap>생성일자</td>
									</tr>
								</thead>
								<tbody id="tbody_relative_batch">
								</tbody>
							</table>  
							
							
							<div class="btn-wrap a-right">
								<button type="button" class="btn btn-default" onclick="fnScriptRelativeUpdatePopup();"  ><i class="fas fa-save m-r-5"></i>수정</button>
								<button type="button" class="btn btn-default" onclick="fnScriptRelativeDelete();"><i class="fas fa-check-square m-r-5"></i>삭제</button>
							</div>	
							<div class="modal fade" id="script_relative_update_modal" tabindex="-1" role="dialog"  aria-hidden="true">
								<div class="modal-dialog modal-sm">
									<div class="modal-content">
										<div class="modal-header">
											<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
											<h4 class="modal-title">등록 스크립트 수정</h4>
										</div>
										<div class="modal-body">
											<div class="panel-body">					
												<form class="">
												<div class="form-group m-t-10">
													<label class="col-sm-4 control-label form-label">스크립트명</label>
													<div class="col-sm-8">
														  <input id="relative_batch_scriptNm" type="text" class="form-control"  value="">          
													</div>
												</div>
												<div class="form-group m-t-10">
													<label class="col-sm-4 control-label form-label">입력영상</label>
													<div class="col-sm-8">
														<input id="relative_batch_inputFileNm" type="text" class="form-control"  value="">            
													</div>
												</div>
												<div class="form-group m-t-10">
													<label class="col-sm-4 control-label form-label">대상영상</label>
													<div class="col-sm-8">
														<input id="relative_batch_targetFileNm" type="text" class="form-control"  value="">            
													</div>
												</div>
												<div class="form-group m-t-10">
													<label class="col-sm-4 control-label form-label">알고리즘 </label>
													<div class="col-sm-8">
														<input id="relative_batch_algoritmNm" type="text" class="form-control"  value="">            
													</div>										
												</div>
												<div class="form-group m-t-1">
													<label for="histogram_01" class="col-sm-4 control-label form-label">히스토그램영역</label>
													<div class="col-sm-3">
														<div class="radio radio-info radio-inline">
															<input type="radio" id="relative_batch_histogram_area_01" value="1" name="histogram_radio">
															<label for="relative_batch_histogram_area_01">전체영역</label>
														</div>
													</div>
													<div class="col-sm-3">
														<div class="radio radio-inline">
															<input type="radio" id="relative_batch_histogram_area_02" value="2" name="histogram_radio">
															<label for="relative_batch_histogram_area_02">중첩영역</label>
														</div>
													</div>
												</div>	
												
												<div class="form-group m-t-10">
													<label class="col-sm-4 control-label form-label">결과영상</label>
													<div class="col-sm-8">
														<input id="relative_batch_outputFileNm" type="text" class="form-control"  value="">            
													</div>
												</div>
												</form> 
		
											</div>
										</div>
										<div class="modal-footer">
											<button type="button" class="btn btn-default" onclick="fnScriptRelativeUpdate();"><i class="fas fa-check-square m-r-5"></i>확인</button>
											<button type="button" class="btn btn-gray" data-dismiss="modal" aria-label="Close"><i class="fas fa-times m-r-5"></i>취소</button>
										</div>
									</div>
								</div>
							</div>				
						</div>
					</div>
				</div>
				<div class="col-md-12">
				<div class="panel panel-default">
					<div class="panel-title">
						<i class="fas fa-list"></i>스크립트 생성
					</div>
					<div class="panel-body table-responsive">
						<table class="table table-hover table-striped">
							<colgroup>
								<col width="50%">
								<col width="*">
							</colgroup>
							<tbody>
								<tr>
									<td class="text-left">
										<div class="form-group m-t-1">
											<label for="script_relative_script_name" class="col-sm-3 control-label form-label">스크립트명</label>
											<div class="col-sm-7"> 
												<input id="script_relative_script_name"  type="text" class="form-control" value="">
											</div>
										</div>
										<div class="form-group m-t-1">
											<label for="script_relative_source_file" class="col-sm-3 control-label form-label">기준영상</label>
											<div class="col-sm-7"> 
												<input id="script_relative_source_file" type="text" class="form-control"  value="">
											</div>
										</div>
										<div class="form-group m-t-1">
											<label for="script_relative_target_file" class="col-sm-3 control-label form-label">대상영상</label>
											<div class="col-sm-7"> 
												<input id="script_relative_target_file" type="text" class="form-control"  value="">
											</div>
										</div>
									</td>									
									<td class="text-left">
										<div class="form-group m-t-1">
											<label for="script_relative_algorithm" class="col-sm-3 control-label form-label">알고리즘</label>
											<div class="col-sm-7"> 
												<select id="script_relative_algorithm" class="form-basic" >
													<option value="Histogram">Histogram Matching</option>												
												</select>
											</div>
										</div>
										
										<div class="form-group m-t-1">
											<label for="histogram_01" class="col-sm-4 control-label form-label">히스토그램영역</label>
											<div class="col-sm-3">
												<div class="radio radio-info radio-inline">
													<input type="radio" id="histogram_01" value="1" name="histogram_radio">
													<label for="histogram_01">전체영역</label>
												</div>
											</div>
											<div class="col-sm-3">
												<div class="radio radio-inline">
													<input type="radio" id="histogram_02" value="2" name="histogram_radio" checked>
													<label for="histogram_02">중첩영역</label>
												</div>
											</div>
										</div>										
										
										<div class="form-group m-t-1">
											<label for="script_relative_output_file" class="col-sm-3 control-label form-label">결과영상</label>
											<div class="col-sm-7"> 
												<input type="text" id="script_relative_output_file" class="form-control"  value="">
											</div>
										</div>
									</td>
								<tr>
									<td colspan=2>
										<div class="sidepanel-s-title m-t-1 a-cent">
											<button type="button" class="btn btn-default" onClick="fnScriptCreate('3');"><i class="fas fa-save m-r-5"></i>생성</button>
										</div>
									</td>
								</tr>
							</tbody>
						</table>    
					</div>
				</div>
			</div>
			</div>
		</div>		
		<!-- dev_project_batch_3 end !!! -->
		<!-- dev_project_batch_4 start !!! -->
		<div id="dev_project_batch_4" style="display:none;">	
			<div class="page-header">
				<h1 class="title"><label>배치 스크립트 관리 - 컬러합성</label></h1>
			</div>
			<div class="container-widget">
				<div class="col-md-12">
				<div class="panel panel-default">
					<div class="panel-title">
						<i class="fas fa-list"></i>등록 스크립트
					</div>
					<div class="panel-body table-responsive">
						<table class="table table-hover table-striped">
							<colgroup>
								<col width="5%">
								<col width="5%">
								<col width="15%">
								<col width="20%">
								<col width="20%">
								<col width="10%">
								<col width="*">
							</colgroup>
							<thead>
								<tr>
									<td class="text-center" nowrap>선택</td>
									<td class="text-center" nowrap>연번</td>
									<td class="text-center" nowrap>스크립트명</td>
									<td class="text-center" nowrap>Red Band</td>
									<td class="text-center" nowrap>Green Band</td>
									<td class="text-center" nowrap>Blue Band</td>
									<td class="text-center" nowrap>결과영상</td>
									<td class="text-center" nowrap>스크립트위치</td>
									<td class="text-center" nowrap>생성일자</td>
								</tr>
							</thead>
							<tbody id="tbody_color_batch">
								
							</tbody>
						</table>  
						<div class="btn-wrap a-right">
							<button type="button" class="btn btn-default" onclick="fnScriptColorUpdatePopup();" ><i class="fas fa-save m-r-5"></i>수정</button>
							<button type="button" class="btn btn-default" onclick="fnScriptColorDelete();"><i class="fas fa-check-square m-r-5"></i>삭제</button>
						</div>
						<div class="modal fade" id="script_color_update_modal" tabindex="-1" role="dialog"  aria-hidden="true">
							<div class="modal-dialog modal-sm">
								<div class="modal-content">
									<div class="modal-header">
										<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
										<h4 class="modal-title">등록 스크립트 수정</h4>
									</div>
									<div class="modal-body">
										<div class="panel-body">					
											<form class="">
											<div class="form-group m-t-10">
												<label class="col-sm-4 control-label form-label">스크립트명</label>
												<div class="col-sm-8">
													  <input id="color_batch_scriptNm" type="text" class="form-control" value="">          
												</div>
											</div>
											<div class="form-group m-t-10">
												<label class="col-sm-4 control-label form-label">Red</label>
												<div class="col-sm-8">
													<input id="color_batch_redBand" type="text" class="form-control" value="">            
												</div>
											</div>
											<div class="form-group m-t-10">
												<label class="col-sm-4 control-label form-label">Green </label>
												<div class="col-sm-8">
													<input id="color_batch_greenBand" type="text" class="form-control" value="">            
												</div>
											</div>
											<div class="form-group m-t-10">
												<label class="col-sm-4 control-label form-label">Blue </label>
												<div class="col-sm-8">
													<input id="color_batch_blueBand" type="text" class="form-control" value="">            
												</div>
											</div>
											<div class="form-group m-t-10">
												<label for="input002" class="col-sm-4 control-label form-label">결과영상</label>
												<div class="col-sm-8">
													<input id="color_batch_outputFileNm" type="text" class="form-control" value="">
												</div>
											</div>
											</form> 
	
										</div>
									</div>
									<div class="modal-footer">
										<button type="button" class="btn btn-default" onclick="fnScriptColorUpdate();"><i class="fas fa-check-square m-r-5"></i>확인</button>
										<button type="button" class="btn btn-gray" data-dismiss="modal" aria-label="Close"><i class="fas fa-times m-r-5"></i>취소</button>
									</div>
								</div>
							</div>
						</div>	 
						  
					</div>
	
				</div>
			</div>
			<div class="col-md-12">
				<div class="panel panel-default">
					<div class="panel-title">
						<i class="fas fa-list"></i>스크립트 생성
					</div>
					<div class="panel-body table-responsive">
						<table class="table table-hover table-striped">
							<colgroup>
								<col width="50%">
								<col width="*">
							</colgroup>
							<tbody>
								<tr>
									<td class="text-left">
										<div class="form-group m-t-1">
											<label for="script_color_script_name" class="col-sm-3 control-label form-label">스크립트명</label>
											<div class="col-sm-7"> 
												<input id="script_color_script_name" type="text" class="form-control" value="">
											</div>
										</div>	
										<div class="form-group m-t-1">
											<label for="script_color_red_band" class="col-sm-3 control-label form-label">Red Band</label>
											<div class="col-sm-7"> 
												<input id="script_color_red_band" type="text" class="form-control"value="">
											</div>
										</div>
										<div class="form-group m-t-1">
											<label for="script_color_green_band" class="col-sm-3 control-label form-label">Green Band</label>
											<div class="col-sm-7"> 
												<input id="script_color_green_band" type="text" class="form-control" value="">
											</div>
										</div>
								    </td>
								    <td class="text-left">	
										<div class="form-group m-t-1">
											<label for="script_color_blue_band" class="col-sm-3 control-label form-label">Blue Band</label>
											<div class="col-sm-7"> 
												<input id="script_color_blue_band" type="text" class="form-control" value="">
											</div>
										</div>
										<div class="form-group m-t-1">
											<label for="input002" class="col-sm-3 control-label form-label">결과영상</label>
											<div class="col-sm-7"> 
												<input id="script_color_output_file" type="text" class="form-control" value="">
											</div>
										</div>
									</td>
								</tr>
								<tr>
									<td colspan=2>
										<div class="sidepanel-s-title m-t-1 a-cent">
											<button type="button" class="btn btn-default" onClick="fnScriptCreate('4');"><i class="fas fa-save m-r-5"></i>생성</button>
										</div>
									</td>
								</tr>
							</tbody>
						</table>    
					</div>
	
				</div>
			</div>
		</div>
		</div>	
		<!-- dev_project_batch_4 end !!! -->
		<!-- dev_project_batch_5 start !!! -->
		<div id="dev_project_batch_5" style="display:none;">	
			<div class="page-header">
				<h1 class="title"><label>배치 스크립트 관리 - 히스토그램조정</label></h1>
			</div>
			<div class="container-widget">
			<div class="col-md-12">
				<div class="panel panel-default">
					<div class="panel-title">
						<i class="fas fa-list"></i>등록 스크립트
					</div>
					<div class="panel-body table-responsive">
						<table class="table table-hover table-striped">
							<colgroup>
								<col width="5%">
								<col width="5%">
								<col width="15%">
								<col width="20%">
								<col width="20%">
								<col width="10%">
								<col width="*">
							</colgroup>
							<thead>
								<tr>
									<td class="text-center" nowrap>선택</td>
									<td class="text-center" nowrap>연번</td>
									<td class="text-center" nowrap>스크립트명</td>
									<td class="text-center" nowrap>입력영상</td>
									<td class="text-center" nowrap>조정방식</td>
									<td class="text-center" nowrap>자동영역설정방법</td>
									<td class="text-center" nowrap>알고리즘</td>
									<td class="text-center" nowrap>결과영상</td>
									<td class="text-center" nowrap>스크립트위치</td>
									<td class="text-center" nowrap>생성일자</td>
								</tr>
							</thead>
							<tbody id="tbody_histogram_batch">
								
							</tbody>
						</table>  
						<div class="btn-wrap a-right">
							<button type="button" class="btn btn-default" onclick="fnScriptHistogramUpdatePopup();" ><i class="fas fa-save m-r-5"></i>수정</button>
							<button type="button" class="btn btn-default" onclick="fnScriptHistogramDelete();"><i class="fas fa-check-square m-r-5"></i>삭제</button>
						</div>
						<div class="modal fade" id="script_histogram_update_modal" tabindex="-1" role="dialog"  aria-hidden="true">
							<div class="modal-dialog modal-sm">
								<div class="modal-content">
									<div class="modal-header">
										<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
										<h4 class="modal-title">등록 스크립트 수정</h4>
									</div>
									<div class="modal-body">
										<div class="panel-body">					
											<form class="">
											<div class="form-group m-t-10">
												<label class="col-sm-6 control-label form-label">스크립트명</label>
												<div class="col-sm-8">
													  <input id="histogram_batch_scriptNm" type="text" class="form-control" value="">          
												</div>
											</div>
											<div class="form-group m-t-10">
												<label class="col-sm-6 control-label form-label">입력영상</label>
												<div class="col-sm-8">
													<input id="histogram_batch_inputFileNm" type="text" class="form-control" value="">            
												</div>
											</div>
											<div class="form-group m-t-10">
												<label class="col-sm-6 control-label form-label">조정방식 </label>
												<div class="col-sm-8">
													<select id="histogram_batch_controlType" class="form-basic">
														<option value="1">조정방식 선택</option>
													</select>            
												</div>
											</div>
											<div class="form-group m-t-10">
												<label for="histogram_batch_autoAreaControl" class="col-sm-6 control-label form-label">자동영역설정</label>
												<div class="col-sm-8">
													<select id="histogram_batch_autoAreaControl" class="form-basic">
														<option value="1">MIN/MAX</option>
														<option value="2">1∂</option>
													</select>
												</div>
											</div>
											<div class="form-group m-t-10">
												<label for="histogram_batch_algorithm" class="col-sm-6 control-label form-label">알고리즘</label>
												<div class="col-sm-8">
													<select id="histogram_batch_algorithm" class="form-basic">
														<option value="1">Linear Stretch</option>															
														<option value="2">Equalization</option>
													</select> 
												</div>
											</div>
											<div class="form-group m-t-10">
												<label for="histogram_batch_outputFileNm" class="col-sm-6 control-label form-label">결과영상</label>
												<div class="col-sm-8">
													<input id="histogram_batch_outputFileNm" type="text" class="form-control" value="">            
												</div>
											</div>
											</form> 
	
										</div>
									</div>
									<div class="modal-footer">
										<button type="button" class="btn btn-default" onclick="fnScriptHistogramUpdate();"><i class="fas fa-check-square m-r-5"></i>확인</button>
										<button type="button" class="btn btn-gray" data-dismiss="modal" aria-label="Close"><i class="fas fa-times m-r-5"></i>취소</button>
									</div>
								</div>
							</div>
						</div>	 
						  
					</div>
	
				</div>
			</div>
			<div class="col-md-12">
						<div class="panel panel-default">
							<div class="panel-title">
								<i class="fas fa-list"></i>스크립트 생성
							</div>
							<div class="panel-body table-responsive">
								<table class="table table-hover table-striped">
									<tbody>
										<tr>
											<td class="text-left">
												<div class="form-group m-t-1">
													<label for="script_histogram_script_name" class="col-sm-3 control-label form-label">스크립트명</label>
													<div class="col-sm-7"> 
														<input id="script_histogram_script_name"type="text" class="form-control" value="">
													</div>
												</div>	
												<div class="form-group m-t-1">
													<label for="script_histogram_input_file" class="col-sm-3 control-label form-label">입력영상</label>
													<div class="col-sm-7"> 
														<input id="script_histogram_input_file" type="text" class="form-control" value="">
													</div>
												</div>
												<div class="form-group m-t-1">
													<label for="script_histogram_control_type" class="col-sm-3 control-label form-label">조정방식</label>
													<div class="col-sm-7">
														<select id="script_histogram_control_type" class="form-basic" >
															<option value="band">밴드별 조정</option>
															<option value="all">일괄 조정</option>
														</select>
													</div>	
												</div>
											</td>
											<td>	
												<div class="form-group m-t-1">
													<label for="script_histogram_auto_area_control" class="col-sm-3 control-label form-label">자동영역설정방법</label>
													<div class="col-sm-7">
														<select id="script_histogram_auto_area_control" class="form-basic" >
															<option value="minmax">MIN/MAX</option>
															<option value="1σ">1s</option>
														</select>
													</div>	
												</div>
												<div class="form-group m-t-1">
													<label for="script_histogram_algorithm" class="col-sm-3 control-label form-label">알고리즘</label>
													<div class="col-sm-7"> 
														<select id="script_histogram_algorithm" class="form-basic" >
															<option value="Auto Linear Stretch">Linear Stretch</option>												
															<option value="Equalization">Equalization</option>
														</select>
													</div>
												</div>
												<div class="form-group m-t-1">
													<label for="script_histogram_output_file" class="col-sm-3 control-label form-label">결과영상</label>
													<div class="col-sm-7"> 
														<input id="script_histogram_output_file" type="text" class="form-control" value="">
													</div>
												</div>
											</td>
										</tr>
										<tr>
											<td colspan=2>
												<div class="sidepanel-s-title m-t-1 a-cent">
													<button type="button" class="btn btn-default" onClick="fnScriptCreate('5');"><i class="fas fa-save m-r-5"></i>생성</button>
												</div>	
											</td>
										</tr>
									</tbody>
								</table>    
							</div>
			
						</div>
					</div>
			</div>
		</div>		
		<!-- dev_project_batch_5 end !!! -->
		<!-- dev_project_batch_6 start !!! -->
		<div id="dev_project_batch_6" style="display:none;">	
			<div class="page-header">
				<h1 class="title"><label>배치 스크립트 관리 - 모자이크</label></h1>
			</div>
			<div class="container-widget">
				<div class="col-md-12">
					<div class="panel panel-default">
						<div class="panel-title">
							<i class="fas fa-list"></i>등록 스크립트
						</div>
						<div class="panel-body table-responsive">
							<table class="table table-hover table-striped">
								<colgroup>
									<col width="5%">
									<col width="5%">
									<col width="15%">									
									<col width="*">
								</colgroup>
								<thead>
									<tr>
										<td class="text-center" nowrap>선택</td>
										<td class="text-center" nowrap>연번</td>
										<td class="text-center" nowrap>스크립트명</td>
										<td class="text-center" nowrap>기준영상</td>
										<td class="text-center" nowrap>입력영상1</td>										
										<td class="text-center" nowrap>입력영상2</td>
										<td class="text-center" nowrap>입력영상3</td>
										<td class="text-center" nowrap>입력영상4</td>
										<td class="text-center" nowrap>결과영상</td>
										<td class="text-center" nowrap>스크립트위치</td>
										<td class="text-center" nowrap>생성일자</td>
									</tr>
								</thead>
								<tbody id="tbody_mosaic_batch">
									<!-- <tr>
										<td class="text-center">
											<input type="radio" value="1">
		                        		</td>
										<td class="text-center">1</td>
										<td class="text-center">모자이크01</td>
										<td class="text-center">기준영상명</td>
										<td class="text-center">입력영상01</td>
										<td class="text-center">Histogram Matching</td>
										<td class="text-center">결과영상명</td>
									</tr>
									<tr>
										<td class="text-center">
											<input type="radio" value="1">
		                        		</td>
										<td class="text-center">2</td>
										<td class="text-center">모자이크02</td>
										<td class="text-center">기준영상명</td>
										<td class="text-center">입력영상02</td>
										<td class="text-center">Histogram Matching</td>
										<td class="text-center">결과영상명</td>
									</tr> -->
									
								</tbody>
							</table>  
							
							<div class="btn-wrap a-right">
								<button type="button" class="btn btn-default" onclick="fnScriptMosaicUpdatePopup();" ><i class="fas fa-save m-r-5"></i>수정</button>
								<button type="button" class="btn btn-default" onclick="fnScriptMosaicDelete();"><i class="fas fa-check-square m-r-5"></i>삭제</button>
							</div>
							<div class="modal fade" id="script_mosaic_update_modal" tabindex="-1" role="dialog"  aria-hidden="true">
								<div class="modal-dialog modal-sm">
									<div class="modal-content">
										<div class="modal-header">
											<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
											<h4 class="modal-title">등록 스크립트 수정</h4>
										</div>
										<div class="modal-body">
											<div class="panel-body">					
												<form class="">
												<div class="form-group m-t-10">
													<label for="mosaic_batch_scriptNm" class="col-sm-4 control-label form-label">스크립트명</label>
													<div class="col-sm-8">
														  <input id="mosaic_batch_scriptNm" type="text" class="form-control"  value="">          
													</div>
												</div>
												<div class="form-group m-t-10">
													<label class="col-sm-4 control-label form-label">기준영상</label>
													<div class="col-sm-7">
														<input id="mosaic_batch_targetFileNm" type="text" class="form-control" value="">            
													</div>
													<!-- <div class="col-sm-1">
														<button type="button" class="btn btn-default btn-icon" ><i class="fas fa-ellipsis-h"></i></button>
													</div> -->
												</div>
												<div class="form-group m-t-10">
													<label class="col-sm-4 control-label form-label">입력영상1 </label>
													<div class="col-sm-7">
														<input id="mosaic_batch_inputFileNm" type="text" class="form-control" value="">            
													</div>													
												</div>
												<div class="form-group m-t-10">
													<label class="col-sm-4 control-label form-label">입력영상2 </label>
													<div class="col-sm-7">
														<input id="mosaic_batch_inputFileNm2" type="text" class="form-control" value="">            
													</div>													
												</div>
												<div class="form-group m-t-10">
													<label class="col-sm-4 control-label form-label">입력영상3 </label>
													<div class="col-sm-7">
														<input id="mosaic_batch_inputFileNm3" type="text" class="form-control" value="">            
													</div>													
												</div>
												<div class="form-group m-t-10">
													<label class="col-sm-4 control-label form-label">입력영상4 </label>
													<div class="col-sm-7">
														<input id="mosaic_batch_inputFileNm4" type="text" class="form-control" value="">            
													</div>													
												</div>
												<!-- <div class="form-group m-t-10">
													<label for="mosaic_batch_algorithm" class="col-sm-4 control-label form-label">알고리즘</label>
													<div class="col-sm-8">
														<select id="mosaic_batch_algorithm"class="form-basic">
															<option value="1">Histogram Matching</option>															
														</select> 
													</div>
												</div> -->
												<div class="form-group m-t-10">
													<label for="mosaic_batch_outputFileNm" class="col-sm-4 control-label form-label">결과영상</label>
													<div class="col-sm-7">
														<input id="mosaic_batch_outputFileNm" type="text" class="form-control" value="">            
													</div>
													
												</div>
												</form> 
		
											</div>
										</div>
										<div class="modal-footer">
											<button type="button" class="btn btn-default" onclick="fnScriptMosaicUpdate();"><i class="fas fa-check-square m-r-5"></i>확인</button>
												<button type="button" class="btn btn-gray" data-dismiss="modal" aria-label="Close"><i class="fas fa-times m-r-5"></i>취소</button>
										</div>
									</div>
								</div>
							</div>	 
							  
						</div>
		
					</div>
				</div>
				<div class="col-md-12">
					<div class="panel panel-default">
						<div class="panel-title">
							<i class="fas fa-list"></i>스크립트 생성
						</div>
						<div class="panel-body table-responsive">
							<table class="table table-hover table-striped">
								<tbody>
									<tr>
										<td class="text-left">
											<div class="form-group m-t-1">
												<label for="script_mosaic_script_name" class="col-sm-3 control-label form-label">스크립트명</label>
												<div class="col-sm-7"> 
													<input id="script_mosaic_script_name" type="text" class="form-control" value="">
												</div>
											</div>
											<div class="form-group m-t-1">
												<label for="script_mosaic_target_file" class="col-sm-3 control-label form-label">기준영상</label>
												<div class="col-sm-7"> 
													<input id="script_mosaic_target_file" type="text" class="form-control" value="">
												</div>
											</div>
											<div class="form-group m-t-1">
												<label for="script_mosaic_output_file" class="col-sm-3 control-label form-label">결과영상</label>
												<div class="col-sm-7"> 
													<input id="script_mosaic_output_file" type="text" class="form-control" value="">
												</div>
											</div>
																							
										</td>
										<td>		
											<!-- <div class="form-group m-t-1">
												<label for="script_mosaic_algorithm" class="col-sm-3 control-label form-label">알고리즘</label>
												<div class="col-sm-7"> 
													<select id="script_mosaic_algorithm" class="form-basic" >
														<option value="1">Histogram Matching</option>												
													</select>
												</div>
											</div> -->
											<div class="form-group m-t-1">
												<label for="script_mosaic_input_file" class="col-sm-3 control-label form-label">입력영상</label>
												<div class="col-sm-7"> 
													<input id="script_mosaic_input_file" type="text" class="form-control" value="">
												</div>
											</div>
											<div class="form-group m-t-1">
												<label for="script_mosaic_input_file2" class="col-sm-3 control-label form-label">입력영상2</label>
												<div class="col-sm-7"> 
													<input id="script_mosaic_input_file2" type="text" class="form-control" value="">
												</div>
											</div>
											<div class="form-group m-t-1">
												<label for="script_mosaic_input_file3" class="col-sm-3 control-label form-label">입력영상3</label>
												<div class="col-sm-7"> 
													<input id="script_mosaic_input_file3" type="text" class="form-control" value="">
												</div>
											</div>
											<div class="form-group m-t-1">
												<label for="script_mosaic_input_file4" class="col-sm-3 control-label form-label">입력영상4</label>
												<div class="col-sm-7"> 
													<input id="script_mosaic_input_file4" type="text" class="form-control" value="">
												</div>
											</div>
										</td>
									</tr>
									<tr>
										<td colspan=2>
											<div class="sidepanel-s-title m-t-1 a-cent">
												<button type="button" class="btn btn-default" onClick="fnScriptCreate('6');"><i class="fas fa-save m-r-5"></i>생성</button>
											</div>
										</td>
									
									</tr>
								</tbody>
							</table>    
						</div>
		
					</div>
				</div>
				
			</div>
		</div>
		<!-- dev_project_batch_6 end !!! -->

		
		
	</div>	
</div>
<div class="load" id="progress" style="display: none;"></div>
</body>
</html>