Proj4js.defs["EPSG:5179"] = "+proj=tmerc +lat_0=38 +lon_0=127.5 +k=0.9996 +x_0=1000000 +y_0=2000000 +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs"; Proj4js.defs["EPSG:4326"] = "+proj=longlat +ellps=WGS84 +datum=WGS84 +no_defs";
Proj4js.defs["EPSG:4326"] = "+proj=longlat +ellps=WGS84 +datum=WGS84 +no_defs";
Proj4js.defs["EPSG:5186"] = '+proj=tmerc +lat_0=38 +lon_0=127 +k=1 +x_0=200000 +y_0=600000 +ellps=GRS80 +units=m +no_defs';
Proj4js.defs["EPSG:32652"] = '+proj=utm +zone=52 +ellps=WGS84 +datum=WGS84 +units=m +no_def';

var t_srs = new Proj4js.Proj("EPSG:5179");
var s_srs = new Proj4js.Proj("EPSG:4326");
var srs_5186 = new Proj4js.Proj("EPSG:5186");
var srs_32652 = new Proj4js.Proj("EPSG:32652");

//항공영상 검색
function search() {
//	if ($("input[name='sate']:checked").length == 0) {
//		alert("위성종류를 선택하세요.");
//		return false;
//	}

	if($('#disasterIdCreate').val().trim() === '') {
		alert("재난 ID를 입력하세요.");
		return false;
	}
	if(!$("#dataKindCurrent:checked").length && ! $("#dataKindEmergency:checked").length && !$("#dataKindResult:checked").length) {
		alert("데이터 종류를 선택하세요.");
		return false;
	}
	if(!$("input[name=dataType]:checked").length) {
		alert("검색 대상을 선택하세요.");
		return false;
	}
//	if ($("#datepicker").val() == "") {
//		alert("시작일자를 입력하세요");
//		return false;
//	}
//	if ($("#datepicker2").val() == "") {
//		alert("종료일자를 입력하세요");
//		return false;
//	}
//	var checkArr = [];
//	$("input[name='sate']:checked").each(function(e) {
//		var value = $(this).val();
//		checkArr.push(value);
//	})
//
//	param = { checkArr: checkArr, date1: $("#datepicker").val(), date2: $("#datepicker2").val() };
	
	var param = {
		disasterId:$('#disasterIdCreate').val(),
		dataKindCurrent: $("#dataKindCurrent:checked").length ? 'on' : null,
		dataKindEmergency: $("#dataKindEmergency:checked").length ? 'on' : null,
		dataKindResult: $("#dataKindResult:checked").length ? 'on' : null, 
		dataType: $('input[name=dataType]:checked').val(),
//		dateFrom: $("#datepicker").val(),
//		dateTo: $("#datepicker2").val(),
		
		ulx4326:null,
		uly4326:null,
		lrx4326:null,
		lry4326:null,
		
		ulx5186:null,
		uly5186:null,
		lrx5186:null,
		lry5186:null,
		
		ulx5179:null,
		uly5179:null,
		lrx5179:null,
		lry5179:null,
		
		ulx32652:null,
		uly32652:null,
		lrx32652:null,
		lry32652:null
	};
	
	var bbox = CMSC003.Storage.get('atmoBbox');
	
	if(bbox) {
		param['lrx4326'] = bbox[0][2];
		param['lry4326'] = bbox[0][1];
		param['ulx4326'] = bbox[0][0];
		param['uly4326'] = bbox[0][3];
		
		param['lrx5186'] = bbox[1][2];
		param['lry5186'] = bbox[1][1];
		param['ulx5186'] = bbox[1][0];
		param['uly5186'] = bbox[1][3];
		
		param['lrx5179'] = bbox[2][2];
		param['lry5179'] = bbox[2][1];
		param['ulx5179'] = bbox[2][0];
		param['uly5179'] = bbox[2][3];
		
		param['lrx32652'] = bbox[3][2];
		param['lry32652'] = bbox[3][1];
		param['ulx32652'] = bbox[3][0];
		param['uly32652'] = bbox[3][3];
	}
	
	$.ajax({
//		 url: "cisc007/colorSearch.do",
		url: "/cmsc003searchOthers.do",
		type: "post",
		data: param
	}).done(function(response) {
		var objData = response;
		if(typeof objData === 'string') {
			objData = JSON.parse(objData); 
		}
		if(typeof objData === 'string') {
			objData = JSON.parse(objData); 
		}
		
		console.log(objData);
		
		CMSC003.DOM.showSearchResultTree(objData);
		
//		var htmlStr = "";
//		var AeroList = objData.AeroList;
//		if (AeroList == null) {
//			htmlStr += "<dt>기타위성(0)</dt>";
//			htmlStr += "<dd>기타위성 결과값이 없습니다.</dd>";
//			$("#AeroResult").html(htmlStr);
//		}
//		else {
//			htmlStr += "<dt>기타위성(" + AeroList.length + ")</dt>";
//			for (var i = 0; i < AeroList.length; i++) {
//				// htmlStr += "<dd><input class='form-check-input'  name ='etcCheckDefault'  id=" + AeroList[i].vidoId + " type='checkbox' minx=" + AeroList[i].ltopCrdntX + " miny=" + AeroList[i].ltopCrdntY + " maxx=" + AeroList[i].rbtmCrdntX + " maxy=" + AeroList[i].rbtmCrdntY + " value=" + AeroList[i].innerFileCoursNm + " >기타위성(" + AeroList[i].innerFileCoursNm + ")</dd>";
//
//				var innerfileNm = AeroList[i].innerFileCoursNm;
//				var idx = -1;
//				idx = innerfileNm.lastIndexOf('\\');
//				if (idx < 0) {
//					idx = innerfileNm.lastIndexOf('/');
//				}
//
//				var filePath = innerfileNm.replace(/tif/gi, "png");
//				var fileName = innerfileNm;
//
//				htmlStr += "<a href=javascript:imageView('" + filePath + "','" + fileName + "'," + AeroList[i].ltopCrdntX + "," + AeroList[i].ltopCrdntY + "," + AeroList[i].rbtmCrdntX + "," + AeroList[i].rbtmCrdntY + ",'" + AeroList[i].mapPrjctnCn + "');><dd><input class='form-check-input'  name ='etcCheckDefault'  type='checkbox' minx=" + AeroList[i].ltopCrdntX + " miny=" + AeroList[i].ltopCrdntY + " maxx=" + AeroList[i].rbtmCrdntX + " maxy=" + AeroList[i].rbtmCrdntY + " value=" + AeroList[i].innerFileCoursNm + " >기타위성(" + AeroList[i].innerFileCoursNm + ")</dd></a>";
//				$("#AeroResult").html(htmlStr);
//			}
//			$("#mymodal").click(function() {
//				$('#input_video').children('option').remove();
//				$('input:checkbox[name="etcCheckDefault"]').each(function() {
//
//					if (this.checked) {//checked 처리된 항목의 값
//						$('#input_video').append("<option value='" + this.value + "' id='" + this.id + "' minx='" + this.getAttribute("minx") + "' miny='" + this.getAttribute("miny") + "' maxx='" + this.getAttribute("maxx") + "' maxy='" + this.getAttribute("maxy") + "'>" + this.value + "</option>");
//					}
//					else {
//						$("#input_video option[value='" + this.value + "']").remove();
//					}
//				});
//			});
//		}
//
//		var soilList = objData.soilList;
//		if (soilList == null) {
//			htmlStr += "<dt>국토위성(0)</dt>";
//			htmlStr += "<dd>국토위성 결과값이 없습니다.</dd>";
//			$("#AeroResult").html(htmlStr);
//		}
//		else {
//			htmlStr += "<dt>국토위성(" + soilList.length + ")</dt>";
//			for (var i = 0; i < soilList.length; i++) {
//				// htmlStr += "<dd>국토위성("+soilList[i].innerFileCoursNm+")</dd>";
//				//htmlStr += "<dd><input class='form-check-input'  name ='soilCheckDefault'  type='checkbox' minx=" + soilList[i].ltopCrdntX + " miny=" + soilList[i].ltopCrdntY + " maxx=" + soilList[i].rbtmCrdntX + " maxy=" + soilList[i].rbtmCrdntY + " value=" + soilList[i].innerFileCoursNm + " >국토위성(" + soilList[i].innerFileCoursNm + ")</dd>";
//				var innerfileNm = soilList[i].innerFileCoursNm;
//				var idx = -1;
//				idx = innerfileNm.lastIndexOf('\\');
//
//				if (idx < 0) {
//					idx = innerfileNm.lastIndexOf('/');
//				}
//
//				var filePath = innerfileNm.replace(/tif/gi, "png");
//				var fileName = innerfileNm.substring(idx).replace(/tif/gi, "png");
//
//				htmlStr += "<a href=javascript:imageView('" + filePath + "','" + fileName + "'," + soilList[i].ltopCrdntX + "," + soilList[i].ltopCrdntY + "," + soilList[i].rbtmCrdntX + "," + soilList[i].rbtmCrdntY + ",'" + soilList[i].mapPrjctnCn + "');><dd><input class='form-check-input'  name ='soilCheckDefault'  type='checkbox' minx=" + soilList[i].ltopCrdntX + " miny=" + soilList[i].ltopCrdntY + " maxx=" + soilList[i].rbtmCrdntX + " maxy=" + soilList[i].rbtmCrdntY + " value=" + soilList[i].innerFileCoursNm + " >국토위성(" + soilList[i].innerFileCoursNm + ")</dd></a>";
//				$("#AeroResult").html(htmlStr);
//			}
//			$("#mymodal").click(function() {
//				$('#red').children('option').remove();
//				$('#green').children('option').remove();
//				$('#blue').children('option').remove();
//				$('input:checkbox[name="soilCheckDefault"]').each(function() {
//					if (this.checked) {//checked 처리된 항목의 값
//						$('#input_video').append("<option value='" + this.value + "' minx='" + this.getAttribute("minx") + "' miny='" + this.getAttribute("miny") + "' maxx='" + this.getAttribute("maxx") + "' maxy='" + this.getAttribute("maxy") + "'>" + this.value + "</option>");
//					}
//					else {
//						$("#input_video option[value='" + this.value + "']").remove();
//					}
//				});
//			});
//
//		}
	});
}


//검색결과 조회
function result() {
	$('#progress').show()
	var input_vido = $("#input_video").val();
	var algorithm = $("#algorithm").val();
	var outputpath = $("#output").val();
	var minx = $("#input_video option:selected").attr("minx");
	var maxx = $("#input_video option:selected").attr("maxx");
	var miny = $("#input_video option:selected").attr("miny");
	var maxy = $("#input_video option:selected").attr("maxy");
	var id = $("#input_video option:selected").attr("id");
	var disasterId = $('#disasterIdCreate').val();
	if(disasterId.trim() === '') {
		alert('재난 ID를 입력하세요');
		return;
	}

	param = { innerFileCoursNm: input_vido, algorithm: algorithm, outputpath: outputpath, vidoId: id, disasterId: disasterId};
	$.ajax({
		url: "cisc004/AtmosphereResult.do",
		type: "post",
		data: param,
		v: { minx, miny, maxx, maxy }
	}).done(function(response) {
		var response = JSON.parse(response);
		$('#progress').hide()
		if (response.procCode == "SUCCESS") {
			var htmlStr = "";
			var result_file = response.fileName;
    		var result_file_path = response.filePath;
            var ltopCrdntX = response.ltopCrdntX;
            var ltopCrdntY = response.ltopCrdntY;
            var rbtmCrdntX = response.rbtmCrdntX;
            var rbtmCrdntY = response.rbtmCrdntY;
            var mapPrjctnCn = response.mapPrjctnCn;
			
//			htmlStr += "<a href=javascript:imageView('" + result_file + "'," + this.v.minx + "," + this.v.miny + "," + this.v.maxx + "," + this.v.maxy + ");><dd>" + result_file + "</dd>";
			htmlStr += "<a href=javascript:imageView('"+result_file_path+"','"+result_file+"',"+ltopCrdntX+","+ltopCrdntY+","+rbtmCrdntX+","+rbtmCrdntY+",'"+mapPrjctnCn+"');><dd>"+result_file+"</dd>";
			
			//$("#ColorResult").html(htmlStr);
			$("#AtomsResult").append(htmlStr);
			$("#AtomsCacel").trigger("click");
		}
		else {
			alert("수행에 실패 하였습니다.");
		}
	});
}


//스크립트명 체크
function fnScriptNameCheck(scriptNm) {

	var ret = false;
	$("#progress").show();
	$.ajax({
		type: "POST",
		async: false,
		url: "cisc001/searchScriptList.do",
		data: { "scriptNm": scriptNm },
		success: function(data) {
			$("#progress").hide();
			if (data.resultList.length > 0) {

				ret = true;
			}

		},
		error: function(data) {

		}
	});

	return ret;
}

//스크립트 저장
function fnScriptSave() {

	if ($("#input_script_nm").val() == "") {
		alert('스크립트명[영문]을 입력하세요.');
		return false;
	}

	if (!confirm("스크립트를 저장 하시겠습니까? ")) {
		return;
	}
	if (fnScriptNameCheck($("#input_script_nm").val())) {
		alert('입력하신 스크립트명이 존재합니다. 다른이름을 입력하세요.');
		return;
	}

	var workKind = '1';

	var param = {
		scriptId: "",  //생성은 id ''
		workKind: workKind,  //1:대기보정,2:절대방사보정,3:상대방사보정
		scriptNm: $("#input_script_nm").val(),
		satKind: '',  //1.Landsat, 2.Kompsat
		metaDataNm: '',
		inputFileNm: $("#input_video").val(),
		outputFileNm: $("#output").val(),
		gain: '',
		offset: '',
		reflectGain: '',
		reflectOffset: '',
		radiatingFormula: '',
		toaOutputFileNm: '',
		histogramArea: '',
		algorithmNm: $("#algorithm").val(),
		targetFileNm: ''
	}


	$("#progress").show();
	$.ajax({
		type: "POST",
		url: "cisc001/createUpdateScript.do",
		data: param,
		success: function(data) {
			$("#progress").hide();
			if (data.result > 0) {
				alert('정상적으로 처리되었습니다.');
				$('#scriptModal').modal("hide");

			} else {
				if (data.result < 0) {
					alert(data.msg);
				} else {
					alert('스크립트 저장에 실패했습니다. 잠시후 다시 시도하세요');
				}
			}
		},
		error: function(data) {
			$("#progress").hide();
			alert('실패했습니다. 잠시후 다시 시도하세요');
			return;
		}
	});
}	