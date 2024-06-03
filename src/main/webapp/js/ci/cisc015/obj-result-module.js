var ObjResultModule = (function() {
	const $model = $('#objResultModel')[0];

	const cmmnModule = new CmmnModule();

	const ResultModel = function() {
		const self = this;
		self.modelId = ko.observable();
		self.aiModelId = ko.observable();
		self.modelAnalsId = ko.observable();
		self.resultList = ko.observableArray();
	}

	const VectorModel = function() {
		const self = this;
		self.modelId = ko.observable();
		self.aiModelId = ko.observable();
		self.modelAnalsId = ko.observable();
		self.analsVidoId = ko.observableArray();
		self.upsamplingRate = ko.observable().extend({
			required: {params: true, message: '업샘플링 비율을 입력해 주십시오.'},
			validation: {
	            validator: function (val, someOtherVal) {
	            	if(0 <= val && val <= 100) {
	            		return true;
	            	} else {
	            		return false;
	            	}
	            },
	            message: '업샘플링 비율은 0~100까지 입력 가능합니다.',
			}
		});

		self.vectorList = ko.observableArray();
		self.vectorDeleteList = ko.observableArray();

		self.errors = ko.validation.group(self);
	}

	const ObjResultModel = function() {
		const self = this;

		self.rslt = ko.observable(new ResultModel());
		self.vec = ko.observable(new VectorModel());

		/**************************************************************
		* Result Model Event
		**************************************************************/
		self.searchRsltList = function() {
			$.ajax({
                url: contextPath+'/cisc015/getObjResultList.do',
                type: 'GET'
            }).done(function(response){
            	const groupList = response.groupList;
            	const resultList = response.resultList;

            	if(groupList.some(item => item.streYn === 'N')) {
            		const data = groupList.find(item => item.streYn === 'N');
            		self.rslt().modelId(data.modelId);
            		self.rslt().aiModelId(data.aiModelId);
            		self.rslt().modelAnalsId(data.modelAnalsId);

            		$('#confirmModel').modal('show');
            	}

            	self.rslt().resultList(resultList.filter(item => item.streYn === 'Y'));
            });
		};

		self.searchVectorList = function() {
			$.ajax({
                url: contextPath+'/cisc015/getObjVectorList.do',
                type: 'GET'
            }).done(function(response){
            	self.vec().vectorList(response);
            });
		};

		self.deleteVector = function() {
			if (self.vec().length > 0) {
				$.ajax({
	                url: contextPath+'/cisc015/deleteVector.do',
	                type: 'PUT',
	                contentType: 'application/json; charset=utf-8',
	                data: ko.toJSON(self.vec())
	            }).done(function(response){
	            	alert('삭제 되었습니다.');
					self.searchVectorList();
	            });
			} else {
				alert('삭제할 항목을 선택해 주십시오.');
			}
		};

		self.resultView = function () {
			$.ajax({
                url: contextPath+'/cisc015/updateAiModelAnalsStreYn.do',
                type: 'PUT',
                contentType: 'application/json; charset=utf-8',
                data: ko.toJSON(self.rslt())
            }).done(function(response){
            	$('#confirmModel').modal('hide');
            	setTimeout(function() {
            		self.searchRsltList();
        		}, 300);
            });
        };

		self.viewImg = function(item) {
			const min = cmmnModule.transform(item.ltopCrdntY, item.ltopCrdntX, item.mapPrjctnCn, 'EPSG:5179');
			const max = cmmnModule.transform(item.rbtmCrdntY, item.rbtmCrdntX, item.mapPrjctnCn, 'EPSG:5179');
//			const filePath = contextPath+'/img/thumnail/'+item.allFileNm.replace('.tif','.png');

//			const filePath = item.fileCoursNm + "/" + item.allFileNm.replace('/.tif/i','.png');
			const filePath = item.fileCoursNm + "/" + item.allFileNm;
			// cmmnModule.viewMap(item.vidoId, filePath, min.x, min.y, max.x, max.y, '');
//			const filePath = item.innerFileCoursNm.replace(/.tif/i,'.png');
			cmmnModule.viewMap(item.vidoId, filePath, min.x, min.y, max.x, max.y, '');
		};

		self.viewVectorImg = function(item) {
			const filePath = item.vectorFileCoursNm + "/" + item.vectorFileNm;
			cmmnModule.viewVectorMap(item.vidoId, item.vectorFileNm, filePath);
		};

		self.vectorMenu = ko.observableArray([{
			text: '벡터로 변환',
			action: function(data){
				self.vec().modelId(data.modelId);
				self.vec().aiModelId(data.aiModelId);
				self.vec().modelAnalsId(data.modelAnalsId);
				self.vec().analsVidoId(data.analsVidoId);
				self.vec().upsamplingRate(100);

				$('#convertVector').modal('show');

//				$.ajax({
//	                url: contextPath+'/cisc015/updateVector.do',
//	                type: 'PUT',
//	                contentType: 'application/json; charset=utf-8',
//	                data: ko.toJSON(data)
//	            }).done(function(response){
//	            	alert('변환 되었습니다.');
//	            	self.searchVectorList();
//	            });

			}
		}]);

        self.convertVector = function (formElement) {
            if (self.vec().errors().length > 0) {
                alert(self.vec().errors()[0]);
                return false;
            } else {
            	if(confirm("벡터 변환을 시작 하시겠습니까?")) {
	            	const formData = new FormData($(formElement)[0]);
					$.ajax({
		                url: contextPath+'/cisc015/updateVector.do',
		                type: 'PUT',
		                contentType: 'application/json; charset=utf-8',
	                    data: ko.toJSON(self.vec())
		            }).done(function(response){
		            	alert('변환 되었습니다.');
		            	self.searchVectorList();
						$('#convertVector').modal('hide');
		            });
            	}
            }
        };
	}

   	return {
        init : function() {
        	ko.cleanNode($model);
        	ko.applyBindings(new ObjResultModel(), $model);
        }
    };

}());
