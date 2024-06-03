$(document).ajaxStart($.blockUI).ajaxStop($.unblockUI);

$(document).ready(function() {
	$.ajaxSetup({
	    error : function(jqXHR, textStatus, errorThrown) {
	        alert(jqXHR.responseJSON.message);
	    }
	});

	//데이터셋 팝업 복사
	var dataSetForm = $("#dataSetForm").clone();
	dataSetForm.find("form").removeAttr("data-bind");
	dataSetForm.find("h4").text("데이터셋");
	dataSetForm.find(".btn-default").remove();
	dataSetForm.find(".tbl-content").find("tbody");

	dataSetForm.attr("id", "aiLearningDataSetForm");
	$("#aiLearningModel").children("div:eq(1)").append(dataSetForm);

	dataSetForm = dataSetForm.clone();
	dataSetForm.attr("id", "prfomncEvlDataSetForm");
	$("#prfomncEvlModel").children("div:eq(1)").append(dataSetForm);

	//모델 상세 팝업 복사
	var modelDetailModal = $("#modelDetailModal").clone();
	modelDetailModal.attr("id", "modelDetailEvlModal");
	modelDetailModal.find("#chartdiv2").attr("id", "chartdiv3");
	$("#prfomncEvlModel").children("div:eq(1)").append(modelDetailModal);

	DataSetModule.init();

	AiLearningModule.init();

	PrfomncEvlModule.init();

	PrfomncEvlResultModule.init();

	$('.nav-tabs li:eq(0)').click(function() {
		var isActive = $(this).hasClass("active");
		if(!isActive) {
			$('#searchDataSet').trigger('click');
		}
	});

	$('.nav-tabs li:eq(1)').click(function() {
		var isActive = $(this).hasClass("active");
		if(!isActive) {
			$('#searchAiLearningList').trigger('click');
		}
	});

	$('.nav-tabs li:eq(2)').click(function() {
		var isActive = $(this).hasClass("active");
		if(!isActive) {
			$('#searchPrfomncEvlList').trigger('click');
		}
	});

	$('.nav-tabs li:eq(3)').click(function() {
		var isActive = $(this).hasClass("active");
		if(!isActive) {
			$('#searchPrfomncEvlResultList').trigger('click');
		}
	});
});

var CmmnModule = function() {

	return {

		getCmmnCode : function(params) {
			return new Promise(function(resolve, reject){
	            $.ajax({
	                url: contextPath+'/getCmmnCode.do',
	                type: 'GET',
	                data: {groupCode : params.groupCode}
	            }).done(function(res){
	                const result = res.map(item => { return {value:item.cmmnCode,text:item.codeNm}} );
	                resolve(result);
	            });
	        });
		},

		getFileNames : function(params) {
			return new Promise(function(resolve, reject){
	            $.ajax({
	                url: contextPath+'/cisc010/getFileNames.do',
	                type: 'POST',
	                enctype: 'multipart/form-data',
	                processData: false,
	                contentType: false,
	                data: params
	            }).done(function(res){
	                const result = res.map(item => { return {fileNm:item} } );
	                resolve(result);
	            });
	        });
		},

		getAiLearningCompleteModule : function() {
			const aiLearningCompleteModule = AiLearningCompleteModule.init();
			return aiLearningCompleteModule;
		},

		makeChart : function(chartId, chartData) {
			//차트 조회 추가 필요
			var balloonText = "[[title]] : [[value]]";
			var chart = AmCharts.makeChart(chartId,
				{
					"type": "serial",
					"categoryField": "lrnCo",
					"startDuration": 0,
					"categoryAxis": {
						"gridPosition": "start"
					},
					"trendLines": [],
					"chartCursor": {
						"cursorPosition": "mouse"
					},
					"graphs": [
						{
							"balloonText": balloonText,
							"bullet": "round",
							"id": "AmGraph-1",
							"title": "Accuracy",
							"valueField": "acccuracyPt"
						},
						{
							"balloonText": balloonText,
							"bullet": "round",
							"id": "AmGraph-2",
							"title": "mIoU",
							"valueField": "mlouPt"
						},
						{
							"balloonText": balloonText,
							"bullet": "round",
							"id": "AmGraph-3",
							"title": "Precision",
							"valueField": "precisionPt"
						},
						{
							"balloonText": balloonText,
							"bullet": "round",
							"id": "AmGraph-4",
							"title": "Recall",
							"valueField": "recallPt"
						}
					],
					"guides": [],
					"valueAxes": [
						{
							"id": "ValueAxis-1",
							"title": "성능지표(%)",
							"minimum": 0,
							"maximum": 100
						}
					],
					"allLabels": [],
					"balloon": {},
					"legend": {
						"enabled": true,
						"useGraphSettings": true
					},
//					"titles": [
//						{
//							"id": "Title-1",
//							"size": 15,
//							"text": "Chart Title"
//						}
//					],
					"dataProvider": chartData
				}
			);
		}
	}

};