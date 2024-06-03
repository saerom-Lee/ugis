var AiLearningCompleteModule = (function() {
	const $model = $('#aiLearningCompleteModel')[0];

	const cmmnModule = new CmmnModule();

	const FormModel = function() {
		const self = this;

		self.aiDataSetId = ko.observable();
		self.aiDataSetNm = ko.observable();
		self.aiModelId = ko.observable();

		self.modelId = ko.observable().extend({required: {params: true, message: '모델 목록을 선택해 주십시오.'}});
		self.modelNm = ko.observable().extend({required: {params: true, message: '모델 명을 입력해 주십시오.'}});
		self.beforeModelNm = ko.observable();
		self.modelSeCd = ko.observable();
		self.algrthCd = ko.observable();
		self.aiAiLearningId = ko.observable();
		self.aiAiLearningNm = ko.observable();
		self.algrthNm = ko.observable();
		self.imageNm = ko.observable();
		self.rsoltnValue = ko.observable();
		self.isUserModel = ko.observable(false);

		//성능 기존
		self.beforeAiLrnId = ko.observable();
		self.beforeLrnCo = ko.observable();
		self.beforeAcccuracyPt = ko.observable();
		self.beforeMlouPt = ko.observable();
		self.beforePrecisionPt = ko.observable();
		self.beforeRecallPt = ko.observable();

		//성능 신규
		self.aiLrnId = ko.observable();
		self.lrnCo = ko.observable();
		self.acccuracyPt = ko.observable();
		self.mlouPt = ko.observable();
		self.precisionPt = ko.observable();
		self.recallPt = ko.observable();

		self.overwrite = ko.observable('N');
		self.isOverwrite = ko.observable(false);
		self.click = function(data){
			if(data.overwrite() == "Y") {
				self.isOverwrite(true);
				self.modelNm(data.beforeModelNm());
			} else {
				self.isOverwrite(false);
			}

        	return true;
        }

		self.errors = ko.validation.group(self);
	}

	const ResultFormModel = function() {
		const self = this;

		self.aiDataSetId = ko.observable();
		self.aiDataSetNm = ko.observable();
		self.aiModelId = ko.observable();

		self.modelId = ko.observable().extend({required: {params: true, message: '모델 목록을 선택해 주십시오.'}});
		self.modelNm = ko.observable().extend({required: {params: true, message: '모델 명을 입력해 주십시오.'}});
		self.modelSeCd = ko.observable();
		self.algrthCd = ko.observable();
		self.aiAiLearningId = ko.observable();
		self.aiAiLearningNm = ko.observable();
		self.algrthNm = ko.observable();
		self.imageNm = ko.observable();
		self.rsoltnValue = ko.observable();
		self.isUserModel = ko.observable(false);

		//성능 신규
		self.aiLrnId = ko.observable();
		self.lrnCo = ko.observable();
		self.acccuracyPt = ko.observable();
		self.mlouPt = ko.observable();
		self.precisionPt = ko.observable();
		self.recallPt = ko.observable();

		self.errors = ko.validation.group(self);
	}

	const AiLearningCompleteModel = function() {
		const self = this;
		self.form = ko.observable(new FormModel());
		self.resultForm = ko.observable(new ResultFormModel());

        self.openPopup = function (modelId) {
        	const model = ko.mapping.fromJS(self.form());
        	$.ajax({
                url: contextPath+'/cisc010/getAiModel.do',
                type: 'GET',
                data: {
                	modelId : modelId
                }
            }).done(function(response){
            	$("#aiLearningComplete").modal('show');
//            	$("#aiLearningComplete").find(".radio-inline").find(".validationMessage").remove();
            	let result = response.modelVer;
            	ko.mapping.fromJS(result, model);

            	//기본모델
            	if(result.modelSeCd == "451") {
            		model.isUserModel(false);
            	} else {
            		model.isUserModel(true);
            		model.overwrite('Y');
            		model.isOverwrite(true);
            	}
            	model.beforeModelNm(result.modelNm);

        		$.each(response.lrnHistList, function (idx, val) {
        			if(model.aiLrnId() == val.aiLrnId) {
        				model.lrnCo(val.lrnCo);
        				model.acccuracyPt(val.acccuracyPt);
        				model.mlouPt(val.mlouPt);
        				model.precisionPt(val.precisionPt);
        				model.recallPt(val.recallPt);
        			}
        		});

            	//기존 학습이력 조회
            	$.ajax({
                    url: contextPath+'/cisc010/getAiModel.do',
                    type: 'GET',
                    data: {
                    	modelId : modelId,
                    	aiModelId : model.aiModelId(),
                    }
                }).done(function(response){
            		$.each(response.lrnHistList, function (idx, val) {
            			if(model.aiLrnId() == val.aiLrnId) {
            				model.beforeAiLrnId(val.aiLrnId);
            				model.beforeLrnCo(val.lrnCo);
            				model.beforeAcccuracyPt(val.acccuracyPt);
            				model.beforeMlouPt(val.mlouPt);
            				model.beforePrecisionPt(val.precisionPt);
            				model.beforeRecallPt(val.recallPt);
            			}
            		});
                });
            });
        }

        self.openPopupPrfomncEvlResult = function (modelId, aiModelId, modelPrfomncEvlId) {
        	const model = ko.mapping.fromJS(self.resultForm());

        	$("#prfomncEvlResultComplete").modal('show');
			//목록 선택 초기화
			var params = {
				streYn : "N",
				modelId : modelId,
				aiModelId : aiModelId,
				modelPrfomncEvlId : modelPrfomncEvlId
			};
			$.ajax({
                url: contextPath+'/cisc010/getPrfomncEvlResultList.do',
                type: 'POST',
                contentType: 'application/json; charset=utf-8',
                data: JSON.stringify(params),
                global: false
            }).done(function(response){
            	let result = response.prfomncEvlResultList;
            	if(result.length == 1) {
            		ko.mapping.fromJS(result[0], model);
            		console.log(result[0], self.resultForm());
            		var val = result[0];
            		self.resultForm().modelNm(val.modelNm);

            		//성능평가 결과 확인
        			$.ajax({
                        url: contextPath+'/cisc010/updatePrfomncEvlResultComplete.do',
                        type: 'POST',
                        contentType: 'application/json; charset=utf-8',
                        data: JSON.stringify(params),
                        global: false
                    }).done(function(response){

                    });
            	}
            });
        }

        self.saveData = function (formElement) {
        	const model = ko.mapping.fromJS(self.resultForm());

            if (self.form().errors().length > 0) {
                alert(self.form().errors()[0]);
                return false;
            } else {
        		var saveUrl = contextPath+'/cisc010/overwriteAiLearning.do'//덮어쓰기
        		var confirmMsg = "기존 모델명으로 덮어쓰기 하시겠습니까?";
        		if(self.form().overwrite() == "N") {
        			saveUrl = contextPath+'/cisc010/saveAsAiLearning.do'//다른 이름으로 저장
        			confirmMsg = "‘" + self.form().modelNm() + "’ 모델명으로 저장합니다.\n다른 이름으로 저장 하시겠습니까?";
        		}

            	if(confirm(confirmMsg)){
            		const formData = new FormData($(formElement)[0]);
	                $.ajax({
	                    url: saveUrl,
	                    type: 'POST',
	                    processData: false,
	                    contentType: false,
	                    data: formData
	                }).done(function(response){
	                	if(response) {
	                		$("#aiLearningComplete").modal('hide');
	            			$('.nav-tabs li, .tab-pane').removeClass("active");
	            			$('#searchAiLearningList').trigger('click');
	            			$('.nav-tabs li:eq(1), #aiLearningModel').addClass("active");
	                	}
	                });
            	}
            }
        }

        self.deleteData = function () {
        	if(confirm("신규 학습 데이터가 삭제됩니다.\n저장하지 않겠습니까?")){
        		$.ajax({
                    url: contextPath+'/cisc010/deleteAiLearning.do',
                    type: 'GET',
                    data: {
                    	modelId : self.form().modelId(),
                    	aiModelId : self.form().aiModelId()
                    }
                }).done(function(response){
                	if(response) {
                		alert('삭제 되었습니다.');
                		$("#aiLearningComplete").modal('hide');
            			$('.nav-tabs li, .tab-pane').removeClass("active");
            			$('#searchAiLearningList').trigger('click');
            			$('.nav-tabs li:eq(1), #aiLearningModel').addClass("active");
                	}
                });
        	}
        };

        self.clearForm = function () {
            self.form(new FormModel());
        };
		return self;
	}

   	return {
        init : function () {
        	const model = new AiLearningCompleteModel();
        	ko.cleanNode($model);
        	ko.applyBindings(model, $model);
        	return model;
        }
    };

}());
