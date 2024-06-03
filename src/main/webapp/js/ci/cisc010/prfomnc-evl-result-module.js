var PrfomncEvlResultModule = (function() {
	const $model = $('#prfomncEvlResultModel')[0];

	const cmmnModule = new CmmnModule();

	const SearchModel = function() {
		const self = this;

		self.prfomncEvlResultList = ko.observableArray();

		self.errors = ko.validation.group(self);
	}

	const FormModel = function() {
		const self = this;

		self.modelId = ko.observable();
		self.modelNm = ko.observable();
		self.aiDataSetNm = ko.observable();
		self.resultTitle = ko.observable();
		//성능
		self.acccuracyPt = ko.observable();
		self.mlouPt = ko.observable();
		self.precisionPt = ko.observable();
		self.recallPt = ko.observable();

		self.errors = ko.validation.group(self);
	}

	const PrfomncEvlResultModel = function() {
		const self = this;

		self.search = ko.observable(new SearchModel());
		self.form = ko.observable(new FormModel());

		/**************************************************************
		* Search Model Event
		**************************************************************/
		self.searchList = function () {
			//목록 선택 초기화
			const params = {

			};

			$.ajax({
                url: contextPath+'/cisc010/getPrfomncEvlResultList.do',
                type: 'POST',
                contentType: 'application/json; charset=utf-8',
                data: JSON.stringify(params)
            }).done(function(response){
            	self.search().prfomncEvlResultList(response.prfomncEvlResultList);
//            	console.log(response.prfomncEvlResultList);
            });
		};

		self.prfomncEvlResult = ko.observableArray([{
			text: '성능평가 결과 조회',
			action: function(data){
//            	console.log(data, self.form());
				$("#prfomncEvlResult").modal('show');
//				self.clearForm();
		    	const model = ko.mapping.fromJS(self.form());
            	ko.mapping.fromJS(data, model);
            	model.resultTitle(data.modelNm + " (" + data.aiDataSetNm + ")");
//            	console.log(model);
			}
		}]);

        self.clearForm = function () {
            self.form(new SearchModel());
        };

	}

   	return {
        init : function() {
        	ko.cleanNode($model);
        	ko.applyBindings(new PrfomncEvlResultModel(), $model);
        }
    };

}());
