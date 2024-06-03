var DataSetModule = (function() {
	const $model = $('#dataSetModel')[0];

	const cmmnModule = new CmmnModule();

	const SearchModel = function() {
		const self = this;
		self.searchImageNm = ko.observableArray();
		self.searchAiDataSetNm = ko.observable();
		self.dataSetList = ko.observableArray();
	}

	const FormModel = function() {
		const self = this;
		self.aiDataSetId = ko.observable();
		self.aiDataSetNm = ko.observable().extend({required: {params: true, message: '이름을 입력해 주십시오.'}});
		self.imageNm = ko.observable().extend({required: {params: true, message: '영상을 선택해 주십시오.'}});
		self.rsoltnValue = ko.observable();
		self.bandNm1 = ko.observable().extend({required: {params: true, message: 'Band1을 입력해 주십시오.'}});
		self.bandNm2 = ko.observable().extend({required: {params: true, message: 'Band2을 입력해 주십시오.'}});
		self.bandNm3 = ko.observable().extend({required: {params: true, message: 'Band3을 입력해 주십시오.'}});
		self.bandNm4 = ko.observable();
		self.imageDataList = ko.observableArray().extend({required: {params: true, message: '영상을 첨부해 주십시오.'}});
		self.lblDataList = ko.observableArray().extend({required: {params: true, message: '라벨링을 첨부해 주십시오.'}});
		self.imageDataCount = ko.observable(0);
		self.lblDataCount = ko.observable(0);
		self.imageNmOptions = ko.observableArray();
		self.bandNm1Options = ko.observableArray();
		self.bandNm2Options = ko.observableArray();
		self.bandNm3Options = ko.observableArray();
		self.bandNm4Options = ko.observableArray();

		self.errors = ko.validation.group(self);
	}

	const DataSetModel = function() {
		const self = this;

		self.search = ko.observable(new SearchModel());
		self.form = ko.observable(new FormModel());

		/**************************************************************
		* Common Event
		**************************************************************/
		self.setCode = function () {
			cmmnModule.getCmmnCode({groupCode:'30'}).then(result => self.form().imageNmOptions(result));
	   	    cmmnModule.getCmmnCode({groupCode:'10'}).then(result => self.form().bandNm1Options(result));
	   	    cmmnModule.getCmmnCode({groupCode:'10'}).then(result => self.form().bandNm2Options(result));
	   	    cmmnModule.getCmmnCode({groupCode:'10'}).then(result => self.form().bandNm3Options(result));
	   	    cmmnModule.getCmmnCode({groupCode:'10'}).then(result => self.form().bandNm4Options(result));
		};

		/**************************************************************
		* Search Model Event
		**************************************************************/
		self.searchList = function () {
			const params = {
				searchImageNm : self.search().searchImageNm(),
				searchAiDataSetNm : self.search().searchAiDataSetNm()
			};

			$.ajax({
                url: contextPath+'/cisc010/getAiDataSetList.do',
                type: 'POST',
                contentType: 'application/json; charset=utf-8',
                data: JSON.stringify(params)
            }).done(function(response){
            	self.search().dataSetList(response);
            });
		};
		self.searchList();

		self.searchDetail = function (data) {
			$.ajax({
                url: contextPath+'/cisc010/getAiDataSet.do',
                type: 'GET',
                data: {aiDataSetId : data.aiDataSetId}
            }).done(function(response){
            	self.setCode();
            	$('#dataSetForm').modal('show');

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
		self.newData = function() {
			self.clearForm();
			self.setCode();
			$('#dataSetForm').modal('show');
		};

		self.imageNmChange = function() {
			if(self.form().imageNm() === '301') {
				self.form().bandNm1('101');
				self.form().bandNm2('102');
				self.form().bandNm3('103');
				self.form().bandNm4('');
			} else {
				self.form().bandNm1('101');
				self.form().bandNm2('102');
				self.form().bandNm3('103');
				self.form().bandNm4('104');
			}
		};

   	    self.imageFileChange = function(elemet, event) {
   	    	const files = event.target.files;
   	    	if(files[0].name.slice(files[0].name.lastIndexOf(".") + 1).toLowerCase() !== 'zip') {
   	    		alert('zip 파일만 첨부 가능 합니다.');
   	    		return false;
			}

   	    	const formData = new FormData();
   	    	formData.append('file', files[0]);

   	    	cmmnModule.getFileNames(formData).then(result => {
   	    		self.form().imageDataCount(result.length);
   	    		self.form().imageDataList(result);
   	        });
   	    };

   	    self.lblFileChange = function(elemet, event) {
            const files = event.target.files;
            if(files[0].name.slice(files[0].name.lastIndexOf(".") + 1).toLowerCase() !== 'zip') {
   	    		alert('zip 파일만 첨부 가능 합니다.');
   	    		return false;
			}

            const formData = new FormData();
            formData.append('file', files[0]);

            cmmnModule.getFileNames(formData).then(result => {
            	self.form().lblDataCount(result.length);
            	self.form().lblDataList(result);
            });
        };

        self.saveData = function (formElement) {

            if (self.form().errors().length > 0) {
                alert(self.form().errors()[0]);
                return false;
            } else {
            	const formData = new FormData($(formElement)[0]);
                formData.append('bandNm', [self.form().bandNm1(),self.form().bandNm2(),self.form().bandNm3(),self.form().bandNm4()].filter(Boolean).join());
                formData.append('imageDataFile', $('input[name=uploadFile]')[0].files[0]);
                formData.append('lblDataFile', $('input[name=uploadFile]')[1].files[0]);
                formData.append('rsoltnValue', 1); // default 1

                $.ajax({
                    url: contextPath+'/cisc010/saveDataSet.do',
                    type: 'POST',
                    enctype: 'multipart/form-data',
                    processData: false,
                    contentType: false,
                    data: formData
                }).done(function(response){
                	if(response) {
               		   alert('저장 되었습니다.');
               		   self.searchList();
               		   $('#dataSetForm').modal('hide');
                	}
                });
            }

        };

        self.deleteData = function () {
        	if(confirm("삭제 하시겠습니까?")){
        		$.ajax({
                    url: contextPath+'/cisc010/deleteDataSet.do',
                    type: 'GET',
                    data: {aiDataSetId : self.form().aiDataSetId()}
                }).done(function(response){
                	if(response) {
               		   alert('삭제 되었습니다.');
               		   self.searchList();
               		   $('#dataSetForm').modal('hide');
                	}
                });
        	}
        };

        self.clearForm = function () {
            self.form(new FormModel());
        };

	}

   	return {
        init : function() {
        	ko.cleanNode($model);
        	ko.applyBindings(new DataSetModel(), $model);
        }
    };

}());
