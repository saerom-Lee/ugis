/**
 * 
 */
var ctsc002 = {
	type: [
		"항공영상",
		"위성영상",
		"드론영상"
	],

	init: function() {
		//tab-02
		var satType = $("#tab-02 #potogrfVidoCd");
		var option = "<option value='<V>'><V></option>";
		var html = [];
		for(var i = 0; i < this.type.length; i++) {
			var v = this.type[i];
			html.push(option.replace(/<V>/g, v));
		}
		
		html = [];
		var date = $("#tab-02 #dwldDate");
		for(var i=1; i < 24; i++) {
			var t = i < 10 ? '0' + i : i;
			html.push(option.replace(/<V>/g, t + ":00"));
		}
		date.html(html.join(""));
		
		$("#tab-02 .area").off("keyup");
		$("#tab-02 .area").on("keyup", function() {
			var v = this.value.replace(/[^0-9\.]/g, '');
			var list = v.split('.')
			v = list.length > 2 ? list[0] + '.' + list[1] : v;
			this.value = v;
		})
		$("#tab-02 #downloadBtn").off("click");
		$("#tab-02 #downloadBtn").on("click", function() {
			ctsc002.save();
		});
		
		$("#tab-02 #search").on("click", function() {
			ctsc002.search();
		});
		
		$("#tab-02 .roi").off("click");
		$("#tab-02 .roi").on("click", function () {
			ctscMap.select.once("Polygon", "drawend", function(event){
				var extent = event.feature.getGeometry().extent_;
				$("#tab-01 #ulx").val(event.feature.getGeometry().extent_[0]);
				$("#tab-01 #uly").val(event.feature.getGeometry().extent_[1]);
				$("#tab-01 #lrx").val(event.feature.getGeometry().extent_[2]);
				$("#tab-01 #lry").val(event.feature.getGeometry().extent_[3]);
			},
			true);
		});

		
	},
	
	showDownload: function() {
		
	},
	  serialize :function(data) {
		let obj = {};
		for (let [key, value] of data) {
			if (obj[key] !== undefined) {
				if (!Array.isArray(obj[key])) {
					obj[key] = [obj[key]];
				}
				obj[key].push(value);
			} else {
				obj[key] = value;
			}
		}
		return obj;
	},
	
	save: function() {
		var data = this.get();
		if(this.valid(data)) {
			$('#progres2').show();
			$.ajax({
					type: "post",
					url: "/ctsc002/add.do",
					//dataType: "json",
					//data: JSON.stringify("{\"satlitNo\": \"dddd\"}"),
					//data: data.serialize(),
					data: data,
					success: function(result) {
						$('#progres2').hide();
						var cd  = result.ret;
						if(cd=="SUCCESS"){
							alert("등록이 완료 되었습니다.");
							$("#modalclose").trigger("click");
						}
						else{
							alert("등록이 실패하였습니다. ")
						}
					}
				});
			
		}
	},
	valid: function(data) {
		//위성종류


		if(!data.vidoNm){
			alert("영상명이 입력되지 않았습니다.");
			return false;
		}
		if(!data.potogrfBeginDt){
			alert("시작날짜가 선택되지 않았습니다.");
			return false;
		}
		if(!data.potogrfEndDt){
			alert("종료날짜가 선택되지 않았습니다.");
			return false;
		}
		if(!data.regDt){
			alert("취득일시가 선택되지 않았습니다.");
			return false;
		}
		if(!data.potogrfVidoCd){
			alert("영상구분이 선택되지 않앗습니다.");
			return false;
		}
		if(!data.satlitVidoExtrlPath){
			alert("외부파일위치가  선택되지 않앗습니다.");
			return false;
		}
		if(!data.satlitVidoInnerPath){
			alert("내부파일위치가  선택되지 않앗습니다.");
			return false;
		}

		return true;
	},
	validSearch: function(data) {
		//var data = this.getSearchingData();
		if(!data.potogrfBeginDt){
			alert("시작날짜가 선택되지 않았습니다.");
			return false;
		}
		if(!data.potogrfEndDt){
			alert("종료날짜가 선택되지 않았습니다.");
			return false;
		}

		return true;
	},
	search: function() {
		var form_data = this.getSearchingData()
		if(this.validSearch(form_data)) {
			$.ajax({
					type: "post",
					url: "ctsc002/get.do",
					//dataType: "json",
					//data: JSON.stringify("{\"satlitNo\": \"dddd\"}"),
					data: form_data
			}).done(function (response) {

				result = JSON.parse(response);
						var list = result.soilList;
						var htmlStr = "";
						if(list.length>0) {
							for (var i = 0; i < list.length; i++) {
								htmlStr += "<ul class='result-list'>";
								htmlStr += "<li>영상명 : " + list[i].vidoNm + "</li>";
								htmlStr += "<li>취득일 : " + list[i].acqsDt + "</li>";
								htmlStr += "<li>영상구분 : " + list[i].potogrfVidoCd + "</li>";
								htmlStr +="</ul>";
							}
							$("#ct002_result").html(htmlStr);
						}
						else{
							htmlStr += "<ul class='result-list'>";
							htmlStr += "<li>검색결과가 없습니다.</li>";
							htmlStr +='</ul>';
							$("#ct002_result").html(htmlStr);
						}

						console.log(result)

					});


		}
	},
	
	getSearchingData: function() {
		return {
			//위성명
			"vidoNmForSearch": $("#tab-02 #vidoNmForSearch").val(),
			//촬영시작일
			"potogrfBeginDt": $("#tab-02 #potogrfBeginDt3").val(),
			//촬영종료일
			"potogrfEndDt": $("#tab-02 #potogrfEndDt3").val()
		}
	},
	
	get: function() {
		return {
			//위성종류
			"vidoNm": $("#tab-02 #vidoNm").val(),
			//촬영시작일
			"potogrfBeginDt": $("#tab-02 #potogrfBeginDt2").val(),
			//촬영종료일
			"potogrfEndDt": $("#tab-02 #potogrfEndDt2").val(),
			//취득일시
			"regDt": $("#tab-02 #regDt").val(),
			//영상구분
			"potogrfVidoCd": $("#tab-02 #potogrfVidoCd").val(),
			//외부파일위치
			"satlitVidoExtrlPath": $("#tab-02 #satlitVidoExtrlPath").val(),
			//내부파일위치
			"satlitVidoInnerPath": $("#tab-02 #satlitVidoInnerPath").val(),
		}
	},
	
	set: function() {
		
	}
}

ctsc002.init();

// $(document).ready(function(){
// 	$('#si').change(function(){
// 		var si = this.value;
// 		si  = si.substring(0,2);
// 		$.ajax({
// 			url: "cmsc002/sgg.do",
// 			type: "post",
// 			data: pram={"si":si}
// 		}).done(function (response) {
// 			 var sgg = response.sggList;
// 			$('#sgg').children('option').remove();
// 			for(var i = 0 ; i < sgg.length ; i++){
// 			$('#sgg').append("<option value='"+sgg[i].bjcd+"'>"+sgg[i].name+"</option>");
// 			}
//
// 		});
// 	});
//
// 	$('#sgg').change(function(){
// 		var sgg = this.value;
// 		sgg  = sgg.substring(0,5);
// 		$.ajax({
// 			url: "cmsc002/emd.do",
// 			type: "post",
// 			data: pram={"sgg":sgg}
// 		}).done(function (response) {
// 			 var sgg = response.sggList;
// 			$('#emd').children('option').remove();
// 			for(var i = 0 ; i < sgg.length ; i++){
// 			$('#emd').append("<option value='"+sgg[i].bjcd+"'>"+sgg[i].name+"</option>");
// 			}
// 		});
// 	});
// });