var AiLearningModule = (function() {
	const $model = $('#aiLearningModel')[0];

	const cmmnModule = new CmmnModule();

	const SearchModel = function() {
		const self = this;
		self.searchImageNm = ko.observableArray();
		self.searchAiAiLearningNm = ko.observable();
		self.dataSetList = ko.observableArray();
		self.baseModelList = ko.observableArray();
		self.userModelList = ko.observableArray();

		self.modelId = ko.observable().extend({required: {params: true, message: '모델 목록을 선택해 주십시오.'}});
		self.aiDataSetId = ko.observable().extend({required: {params: true, message: 'AI 데이터셋 목록을 선택해 주십시오.'}});
		self.imageNm = ko.observable();
		self.progrsSttusCd = ko.observable().extend({
			validation: {
	            validator: function (val, someOtherVal) {
	            	if(val == "443" || val == "444") {
	            		return true;
	            	} else {
	            		return false;
	            	}
	            },
	            message: '모델 진행 상태가 완료 상태에서만 학습이 가능합니다.',
			}
		});

		self.beforeImageNm = ko.observable("");

		//모델 목록 클릭 이벤트
		self.clickModelId = function (data) {
//			console.log(data);
			if (data.progrsSttusCd == "444" && data.streYn == "N") {
//				alert("[" + data.modelNm + "] 모델은 저장 완료 후 학습이 가능합니다.\n저장 완료 팝업 조회 기능 추가 필요.");
				cmmnModule.getAiLearningCompleteModule().openPopup(data.modelId);
				return false;
			} else if (!(data.progrsSttusCd == "443" || data.progrsSttusCd == "444")) {
				alert("[" + data.modelNm + "] 모델은 학습 대기 또는 진행 상태입니다.\n모델 진행 상태가 완료 상태에서만 선택이 가능합니다.");
				return false;
			} else {
				self.modelId(data.modelId);
				self.progrsSttusCd(data.progrsSttusCd);
				self.imageNm(data.imageNm);

				if(self.beforeImageNm() != data.imageNm) {
					self.beforeImageNm(data.imageNm);

					const params = {
						searchImageNm : [data.imageNm]
					};

					$.ajax({
		                url: contextPath+'/cisc010/getAiDataSetList.do',
		                type: 'POST',
		                contentType: 'application/json; charset=utf-8',
		                data: JSON.stringify(params)
		            }).done(function(response){
		            	self.dataSetList(response);
		            	self.aiDataSetId("");
		            });
				}
				return true;
			}
		}

		//데이터셋 목록 클릭 이벤트
		self.clickAiDataSetId = function (data) {
			self.aiDataSetId(data);
			self.aiDataSetId(data.aiDataSetId);
			return true;
		}

		self.errors = ko.validation.group(self);
	}

	const SearchDetailModel = function() {
		const self = this;

		self.modelId = ko.observable();
		self.modelNm = ko.observable();
		self.aiDataSetId = ko.observable();
		self.aiDataSetNm = ko.observable();

		self.algrthCd = ko.observable();
		self.algrthNmOptions = ko.observableArray();

		self.modelSeCd = ko.observable();
		self.modelSeCdOptions = ko.observableArray();

		self.imageNm = ko.observable();
		self.imageNmOptions = ko.observableArray();

		self.progrsSttusCd = ko.observable();
		self.progrsSttusCdOptions = ko.observableArray();

		//성능 평가 그래프
		self.aiLrnId = ko.observable();
		self.lrnCo = ko.observable();
		self.acccuracyPt = ko.observable();
		self.mlouPt = ko.observable();
		self.precisionPt = ko.observable();
		self.recallPt = ko.observable();

		self.isShowGraph = ko.observable(false);
	}

	const FormModel = function() {
		const self = this;

		self.aiDataSetId = ko.observable();
		self.aiDataSetNm = ko.observable();

		self.modelId = ko.observable();
		self.modelNm = ko.observable();
		self.modelSeCd = ko.observable();
		self.algrthCd = ko.observable();
		self.aiAiLearningId = ko.observable();
		self.aiAiLearningNm = ko.observable();
		self.algrthNm = ko.observable();
		self.imageNm = ko.observable();
		self.rsoltnValue = ko.observable();
		self.bandNm1 = ko.observable();
		self.bandNm2 = ko.observable();
		self.bandNm3 = ko.observable();
		self.bandNm4 = ko.observable();
		self.imageDataList = ko.observableArray();
		self.lblDataList = ko.observableArray();
		self.imageDataCount = ko.observable(0);
		self.lblDataCount = ko.observable(0);
		self.algrthNmOptions = ko.observableArray();
		self.imageNmOptions = ko.observableArray();
		self.bandNm1Options = ko.observableArray();
		self.bandNm2Options = ko.observableArray();
		self.bandNm3Options = ko.observableArray();
		self.bandNm4Options = ko.observableArray();

		self.progrsSttusCd = ko.observable("441");
		self.patchSizeCd = ko.observable().extend({required: {params: true, message: '패치 사이즈를 입력해 주십시오.'}});
		self.patchSizeOptions = ko.observableArray();
		self.batchSizeCd = ko.observable().extend({required: {params: true, message: '배치 사이즈를 입력해 주십시오.'}});
		self.batchSizeOptions = ko.observableArray();
		self.modelSeCdOptions = ko.observableArray();

		self.lrnRateSetup = ko.observable("50");
		self.reptitCo = ko.observable("10");

		self.learning = ko.observable();
		self.verification = ko.observable();

		self.errors = ko.validation.group(self);
	}

	const AiLearningModel = function() {
		const self = this;

		self.search = ko.observable(new SearchModel());
		self.form = ko.observable(new FormModel());
		self.searchModelDetail = ko.observable(new SearchDetailModel());

		/**************************************************************
		* Common Event
		**************************************************************/
		self.setCode = function () {
			cmmnModule.getCmmnCode({groupCode:'40'}).then(result => self.form().algrthNmOptions(result));//알고리즘 코드
			cmmnModule.getCmmnCode({groupCode:'30'}).then(result => self.form().imageNmOptions(result));//위성영상 코드
	   	    cmmnModule.getCmmnCode({groupCode:'10'}).then(result => self.form().bandNm1Options(result));//밴드 코드
	   	    cmmnModule.getCmmnCode({groupCode:'10'}).then(result => self.form().bandNm2Options(result));//밴드 코드
	   	    cmmnModule.getCmmnCode({groupCode:'10'}).then(result => self.form().bandNm3Options(result));//밴드 코드
	   	    cmmnModule.getCmmnCode({groupCode:'10'}).then(result => self.form().bandNm4Options(result));//밴드 코드

			cmmnModule.getCmmnCode({groupCode:'41'}).then(result => self.form().patchSizeOptions(result));//패치 사이즈 코드
//			cmmnModule.getCmmnCode({groupCode:'42'}).then(result => self.form().overlySizeOptions(result));//중첩 사이즈 코드
			cmmnModule.getCmmnCode({groupCode:'43'}).then(result => self.form().batchSizeOptions(result));//배치 사이즈 코드
		};

		//코드 조회 : 모델 조회
		self.setCodeSearchDetail = function () {
			cmmnModule.getCmmnCode({groupCode:'45'}).then(result => self.searchModelDetail().modelSeCdOptions(result));//모델 타입
			cmmnModule.getCmmnCode({groupCode:'30'}).then(result => self.searchModelDetail().imageNmOptions(result));//위성영상 코드
			cmmnModule.getCmmnCode({groupCode:'40'}).then(result => self.searchModelDetail().algrthNmOptions(result));//알고리즘 코드
			cmmnModule.getCmmnCode({groupCode:'44'}).then(result => self.searchModelDetail().progrsSttusCdOptions(result));//모델 타입
		}

		/**************************************************************
		* Search Model Event
		**************************************************************/
		//검색 버튼 기능
		self.searchList = function () {
			//목록 선택 초기화
			self.search().modelId("");
			self.search().aiDataSetId("");
			self.search().beforeImageNm("");

			const params = {
				searchImageNm : self.search().searchImageNm(),
			};

			$.ajax({
                url: contextPath+'/cisc010/getAiModelList.do',
                type: 'POST',
                contentType: 'application/json; charset=utf-8',
                data: JSON.stringify(params)
            }).done(function(response){
            	self.search().baseModelList(response.baseModelList);
            	self.search().userModelList(response.userModelList);
            	self.search().dataSetList([]);
            });
		};

		//기본 모델 목록 : 상세 조회
		self.searchDetail = function (data) {
			self.searchModelDetail(new SearchDetailModel());
        	self.setCodeSearchDetail();

			//모델 조회
			$.ajax({
                url: contextPath+'/cisc010/getAiModel.do',
                type: 'GET',
                data: {
                	modelId : data.modelId
                }
            }).done(function(response){
            	const model = ko.mapping.fromJS(self.searchModelDetail());
            	ko.mapping.fromJS(response.modelVer, model);

            	if(self.searchModelDetail().progrsSttusCd() == "444") {
            		self.searchModelDetail().isShowGraph(true);
            		var chartId = "chartdiv2";
            		var chartData = [];
            		var isAiLrnId = false;

            		if(response.lrnHistList.length == 0)
            			self.searchModelDetail().isShowGraph(false);

            		$.each(response.lrnHistList, function (idx, val) {
            			chartData.push({
            				"lrnCo": val.lrnCo,
            				"acccuracyPt": val.acccuracyPt,
            				"mlouPt": val.mlouPt,
            				"precisionPt": val.precisionPt,
            				"recallPt": val.recallPt,
            			});

            			if(self.searchModelDetail().aiLrnId() == val.aiLrnId) {
            				self.searchModelDetail().lrnCo(val.lrnCo);
            				self.searchModelDetail().acccuracyPt(val.acccuracyPt);
            				self.searchModelDetail().mlouPt(val.mlouPt);
            				self.searchModelDetail().precisionPt(val.precisionPt);
            				self.searchModelDetail().recallPt(val.recallPt);
            				isAiLrnId = true;
            			}
//            			if(!isAiLrnId && (idx + 1) == response.lrnHistList.length) {
            			if(isAiLrnId) {
            				self.searchModelDetail().lrnCo(val.lrnCo);
            				self.searchModelDetail().acccuracyPt(val.acccuracyPt);
            				self.searchModelDetail().mlouPt(val.mlouPt);
            				self.searchModelDetail().precisionPt(val.precisionPt);
            				self.searchModelDetail().recallPt(val.recallPt);
            			}
            		});

            		if(response.lrnHistList.length > 0) {
            			//차트 조회
            			cmmnModule.makeChart(chartId, chartData);
            		}
            	}
            });
		};

		//데이터셋 상세 조회
		self.searchDataSetDetail = function (data) {
			$.ajax({
                url: contextPath+'/cisc010/getAiDataSet.do',
                type: 'GET',
                data: {aiDataSetId : data.aiDataSetId}
            }).done(function(response){

            	self.setCode();
            	$('#aiLearningDataSetForm').modal('show');

            	let result = response.tnAiDataSet;
            	result.imageDataCount = response.imageDataList.length;
            	result.lblDataCount = response.lblDataList.length;

            	const bandNm = result.bandNm.split(',');
            	result.bandNm1 = bandNm[0]||'';
            	result.bandNm2 = bandNm[1]||'';
            	result.bandNm3 = bandNm[2]||'';
            	result.bandNm4 = bandNm[3]||'';

            	const model = ko.mapping.fromJS(self.form());
            	ko.mapping.fromJS(result, model);
            	self.form().imageDataList(response.imageDataList);
            	self.form().lblDataList(response.lblDataList);

            });
		};

		/**************************************************************
		* Form Model Event
		**************************************************************/
		//AI 학습 수행
		self.newData = function() {
			if (self.search().errors().length > 0) {
                alert(self.search().errors()[0]);
                return false;
            } else {
    			self.clearForm();
    			self.setCode();
    			$('#aiLearningForm').modal('show');

    			//EVENT 추가
    			var beforeVal = 10, beforeLrnVal = 50;
    			$("#minusReptitCo").unbind();
    			$("#minusReptitCo").click(function() {
    				var reptitCo = $("input[name=reptitCo]").val();
    				if(reptitCo > 0) {
        				reptitCo = parseInt(reptitCo) - 1;
        				$("input[name=reptitCo]").val(reptitCo);
        				beforeVal = reptitCo;
    				}
    			});

    			$("#plusReptitCo").unbind();
    			$("#plusReptitCo").click(function() {
    				var reptitCo = $("input[name=reptitCo]").val();
    				if(reptitCo < 100000) {
	    				reptitCo = parseInt(reptitCo) + 1;
	    				$("input[name=reptitCo]").val(reptitCo);
	    				beforeVal = reptitCo;
    				}
    			});

    			$("input[name=reptitCo]").unbind();
    			$("input[name=reptitCo]").change(function() {
    				if($(this).val() == "") {
    					$(this).val(beforeVal);
    				} else {
    					beforeVal = $(this).val();
    				}
    			});

    			$("input[name=lrnRateSetup]").unbind();
    			$("input[name=lrnRateSetup]").change(function() {
    				var learning = $(this).val();
    				$("#learning").val(learning);
    				$("#verification").val(100 - learning);
    			});

    			$("#learning, #verification").unbind();
    			$("#learning").change(function() {
    				var learning = $("#learning").val();
    				if(learning > 100 || learning < 0) {
    					learning = $("input[name=lrnRateSetup]").val();
    				}
    				$("input[name=lrnRateSetup]").val(learning);
    				$("#learning").val(learning);
    				$("#verification").val(100 - learning);
    			});

    			$("#verification").change(function() {
    				var learning = $("#learning").val();
    				var verification = $(this).val();
    				if(verification > 100 || verification < 0) {
    					learning = $("input[name=lrnRateSetup]").val();
        				$("input[name=lrnRateSetup]").val(learning);
        				$("#learning").val(learning);
        				$("#verification").val(100 - learning);
    				} else {
        				learning = 100 - verification;
        				$("input[name=lrnRateSetup]").val(learning);
        				$("#learning").val(learning);
        				$("#verification").val(100 - learning);
    				}
    			});

    			//모델 조회
    			$.ajax({
                    url: contextPath+'/cisc010/getAiModel.do',
                    type: 'GET',
                    data: {
                    	modelId : self.search().modelId()
                    }
                }).done(function(response){
                	$('#aiLearningForm').modal('show');
                	const model = ko.mapping.fromJS(self.form());
                	ko.mapping.fromJS(response.modelVer, model);

                	model.learning(model.lrnRateSetup());
                	model.verification(100 - model.lrnRateSetup());
                });

    			//데이터셋 조회
    			$.ajax({
                    url: contextPath+'/cisc010/getAiDataSet.do',
                    type: 'GET',
                    data: {
                    	aiDataSetId : self.search().aiDataSetId()
                    }
                }).done(function(response){
                	$('#aiLearningForm').modal('show');

                	let result = response.tnAiDataSet;
                	const bandNm = result.bandNm.split(',');
                	result.bandNm1 = bandNm[0]||'';
                	result.bandNm2 = bandNm[1]||'';
                	result.bandNm3 = bandNm[2]||'';
                	result.bandNm4 = bandNm[3]||'';

                	const model = ko.mapping.fromJS(self.form());
//                	console.log(response.tnAiDataSet);
                	ko.mapping.fromJS(response.tnAiDataSet, model);

                	self.form().progrsSttusCd("441");
                });
            }
		};

        self.saveData = function (formElement) {
            if (self.form().errors().length > 0) {
                alert(self.form().errors()[0]);
                return false;
            } else {
            	if(confirm("모델 학습을 시작 하시겠습니까?")) {
                	const formData = new FormData($(formElement)[0]);
                    $.ajax({
                        url: contextPath+'/cisc010/saveAiLearning.do',
                        type: 'POST',
                        processData: false,
                        contentType: false,
                        data: formData
                    }).done(function(response){
                    	if(response) {
                   		   alert('선택한 자료의 학습을 시작합니다.\n학습이 완료되면 알림을 제공합니다.');
                   		   self.searchList();
                   		   $('#aiLearningForm').modal('hide');
                    	}
                    });
            	}
            }

        };

        self.clearForm = function () {
            self.form(new FormModel());
        };

	}

   	return {
        init : function() {
        	ko.cleanNode($model);
        	ko.applyBindings(new AiLearningModel(), $model);
        }
    };

}());
