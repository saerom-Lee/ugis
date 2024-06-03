/**
 * 
 */
var ctsc001 = {
	type: [
		"landset_tm_c1",
		"landset_tm_c2_l1",
		"landset_tm_c2_l2",
		"landset_etm_ot_c1",
		"landset_etm_ot_c2_l1",
		"landset_etm_ot_c2_l2",
		"landset_8_c1",
		"landset_ot_c2_l1",
		"landset_ot_c2_l2",
		"Sentinel_2a"
	],
	init: function() {
		
		//tab-01
		var satType = $("#tab-01 #satlitNo");
		var option = "<option value='<V>'><V></option>";
		var html = [];
		for(var i = 0; i < this.type.length; i++) {
			var v = this.type[i];
			html.push(option.replace(/<V>/g, v));
		}
		
		html = [];
		var date = $("#tab-01 #dwldDate");
		for(var i=1; i < 24; i++) {
			var t = i < 10 ? '0' + i : i;
			html.push(option.replace(/<V>/g, t + ":00"));
		}
		date.html(html.join(""));
		
		$("#tab-01 .area").off("keyup");
		$("#tab-01 .area").on("keyup", function() {
			var v = this.value.replace(/[^0-9\.]/g, '');
			var list = v.split('.')
			v = list.length > 2 ? list[0] + '.' + list[1] : v;
			this.value = v;
		})
		$("#tab-01 #downloadBtn").off("click");
		$("#tab-01 #downloadBtn").on("click", function() {
			ctsc001.save();
		});
		$("#tab-01 .roi").off("click");
		$("#tab-01 .roi").on("click", function () {
			ctscMap.select.once("Box", "drawend", function(event){
				var extent = event.feature.getGeometry().extent_;
				$("#tab-01 #ulx").val(event.feature.getGeometry().extent_[0]);
				$("#tab-01 #uly").val(event.feature.getGeometry().extent_[1]);
				$("#tab-01 #lrx").val(event.feature.getGeometry().extent_[2]);
				$("#tab-01 #lry").val(event.feature.getGeometry().extent_[3]);
			},
			true);
		});
		
	},
	save: function() {

		var data = this.get();
		console.log(data);


		if(!data.potogrfBeginDt){
			alert("시작날짜가 선택되지 않았습니다.");
			return false;
		}
		if(!data.potogrfEndDt){
			alert("종료날짜가 선택되지 않았습니다.");
			return false;
		}
		if(!data.ulx){
			alert("영역이 선택되지 않았습니다.");
			return false;
		}
		if(!data.satlitVidoExtrlPath){
			alert("외부파일위치가 지정되지 않았습니다. ");
			return false;
		}
		if(!data.satlitVidoInnerPath){
			alert("내부파일위치가 지정되지 않았습니다. ");
			return false;
		}

		
		var form_data = new FormData();
		for ( var key in data ) {
			form_data.append(key, data[key]);
		}
		debugger;
		if(this.valid(data)) {
			$.ajax({
					type: "post",
					url: "/ctsc001/add.do",
					//dataType: "json",
					//data: JSON.stringify("{\"satlitNo\": \"dddd\"}"),
					data: data,
					success: function(result) {

						var cd  = result.insertcd;
						if(cd==1){
							alert("스케쥴이 등록되었습니다. ");
						}
					}
				});
			
		}
	},
	
	search: function() {
		
	},
	
	valid: function(data) {
		return true;
	},
	
	get: function() {
		return {
			//위성종류
			"potogrf_vido_nm": $("#tab-01 #satlitNo").val(),
			//촬영시작일
			"potogrfBeginDt": $("#tab-01 #potogrfBeginDt").val(),
			//촬영종료일
			"potogrfEndDt": $("#tab-01 #potogrfEndDt").val(),
			//ROI ulx
			"uly": $("#tab-01 #uly").val(),
			//ROI uly
			"ulx": $("#tab-01 #ulx").val(),
			//ROI lrx
			"lrx": $("#tab-01 #lrx").val(),
			//ROI lry
			"lry": $("#tab-01 #lry").val(),
			//내부 파일 경로 
			"satlitVidoInnerPath": $("#tab-01 #satlitVidoInnerPath").val(),
			//외부 파일 경로
			"satlitVidoExtrlPath": $("#tab-01 #satlitVidoExtrlPath").val(),
			//다운로드 시간
			"dwldDate": $("#tab-01 #dwldDate").val()
		}
	},
	
	set: function() {
		
	}
}

ctsc001.init();