var ObjChangeModule = (function() {
	const $model = $('#objChangeModel')[0];

	const cmmnModule = new CmmnModule();

	const SearchModel = function() {
		const self = this;
		self.searchPotogrfVidoCd = ko.observableArray().extend({required: {params: true, message: '영상 종류를 선택해 주십시오.'}});
		self.searchPotogrfBeginDt = ko.observable();
		self.searchPotogrfEndDt = ko.observable();
		self.aeroCount = ko.observable(0);
		self.soilCount = ko.observable(0);
		self.etcCount = ko.observable(0);
		self.aeroList = ko.observableArray();
		self.soilList = ko.observableArray();
		self.etcList = ko.observableArray();
		self.droneList = ko.observableArray();
		self.aeroChecked = ko.observableArray();
		self.soilChecked = ko.observableArray();
		self.etcChecked = ko.observableArray();
		self.selectedVidoCd = ko.observableArray();

		self.errors = ko.validation.group(self);
	}

	const FormModel = function() {
		const self = this;
		self.modelId = ko.observable('');
		self.modelNm = ko.observable('');
		self.aiModelId = ko.observable('');
		self.algrthCd = ko.observable('');
		self.patchSizeCd = ko.observable('');
		self.aiDataSetId = ko.observable('');
		self.aiDataSetNm = ko.observable('');
		self.classNm = ko.observable('');
		self.imageNm = ko.observable('');
		self.rsoltnValue = ko.observable('');
		self.bandNm1 = ko.observable('');
		self.bandNm2 = ko.observable('');
		self.bandNm3 = ko.observable('');
		self.bandNm4 = ko.observable('');
		self.algrthCdOptions = ko.observableArray([]);
		self.patchSizeCdOptions = ko.observableArray([]);
		self.imageNmOptions = ko.observableArray([]);
		self.bandNm1Options = ko.observableArray([]);
		self.bandNm2Options = ko.observableArray([]);
		self.bandNm3Options = ko.observableArray([]);
		self.bandNm4Options = ko.observableArray([]);
		self.vidoList = ko.observableArray([]);
	}

	const ModelFormModel = function() {
		const self = this;
		self.modelList = ko.observableArray();
		self.modelChecked = ko.observableArray();
		self.modelSelect = ko.observable(null);
	}

	const ObjChangeModel = function() {
		const self = this;

		self.search = ko.observable(new SearchModel());
		self.form = ko.observable(new FormModel());
		self.modelForm = ko.observable(new ModelFormModel());

		/***********************************************************************
		 * Common Event
		 **********************************************************************/
		self.setCode = function () {
			cmmnModule.getCmmnCode({groupCode:'40'}).then(result => self.form().algrthCdOptions(result));
			cmmnModule.getCmmnCode({groupCode:'41'}).then(result => self.form().patchSizeCdOptions(result));
			cmmnModule.getCmmnCode({groupCode:'30'}).then(result => self.form().imageNmOptions(result));
	   	    cmmnModule.getCmmnCode({groupCode:'10'}).then(result => self.form().bandNm1Options(result));
	   	    cmmnModule.getCmmnCode({groupCode:'10'}).then(result => self.form().bandNm2Options(result));
	   	    cmmnModule.getCmmnCode({groupCode:'10'}).then(result => self.form().bandNm3Options(result));
	   	    cmmnModule.getCmmnCode({groupCode:'10'}).then(result => self.form().bandNm4Options(result));
		};

		/***********************************************************************
		 * Search Model Event
		 **********************************************************************/
		self.searchList = function () {

			if(!$('#searchPotogrfBeginDt').val() || !$('#searchPotogrfEndDt').val()) {
				alert('촬영기간을 입력해 주십시오.');
				return false;
			}

			if (self.search().errors().length > 0) {
				alert(self.search().errors()[0]);
				return false;
			} else {
				const params = {
					searchPotogrfVidoCd : self.search().searchPotogrfVidoCd().join(',').split(','),
					searchPotogrfBeginDt : $('#searchPotogrfBeginDt').val(),
					searchPotogrfEndDt : $('#searchPotogrfEndDt').val()
				};

				$.ajax({
	                url: contextPath+'/cisc015/getSateList.do',
	                type: 'POST',
	                contentType: 'application/json; charset=utf-8',
	                data: JSON.stringify(params)
	            }).done(function(response){
	            	const aeroList = response.filter(item => ['7','8'].includes(item.potogrfVidoCd));
	            	const soilList = response.filter(item => ['00'].includes(item.potogrfVidoCd));
	            	const etcList = response.filter(item => ['1','2','6'].includes(item.potogrfVidoCd));
	            	const droneList = response.filter(item => ['3','4','5'].includes(item.potogrfVidoCd));

	            	const vidoCd = self.search().searchPotogrfVidoCd().join(',').split(',');
	            	self.search().aeroList(aeroList.length > 0 ? aeroList : vidoCd.some(v => ['7','8'].includes(v)) ? [{message:'검색된 데이터가 없습니다.'}] : []);
	            	self.search().soilList(soilList.length > 0 ? soilList : vidoCd.some(v => ['00'].includes(v)) ? [{message:'검색된 데이터가 없습니다.'}] : []);
	            	self.search().etcList(etcList.length > 0 ? etcList : vidoCd.some(v => ['1','2','6'].includes(v)) ? [{message:'검색된 데이터가 없습니다.'}] : []);
	            	self.search().droneList(droneList.length > 0 ? droneList : vidoCd.some(v => ['3','4','5'].includes(v)) ? [{message:'검색된 데이터가 없습니다.'}] : []);
	            });
			}

		};

		self.search().aeroChecked.subscribe(function(item) {
//			if (item.length > 0) {
//				self.search().soilChecked([]);
//				self.search().etcChecked([]);
//			}

//			fnSetVidoCd(item);

			const join = self.search().soilChecked().concat(self.search().etcChecked()).concat(item);
			fnSetVidoCd(join);
		});

		self.search().soilChecked.subscribe(function(item) {
//			if (item.length > 0) {
//				self.search().aeroChecked([]);
//				self.search().etcChecked([]);
//			}

//			fnSetVidoCd(item);

			const join = self.search().aeroChecked().concat(self.search().etcChecked()).concat(item);
			fnSetVidoCd(join);
		});

		self.search().etcChecked.subscribe(function(item) {
//			if (item.length > 0) {
//				self.search().soilChecked([]);
//				self.search().aeroChecked([]);
//			}

//			fnSetVidoCd(item);

			const join = self.search().aeroChecked().concat(self.search().soilChecked()).concat(item);
			fnSetVidoCd(join);
		});

		self.viewImg = function(item) {
			const min = cmmnModule.transform(item.ltopCrdntY, item.ltopCrdntX, item.mapPrjctnCn, 'EPSG:5179');
			const max = cmmnModule.transform(item.rbtmCrdntY, item.rbtmCrdntX, item.mapPrjctnCn, 'EPSG:5179');
// const filePath =
// contextPath+'/img/thumnail/'+item.fileNm.replace('.tif','.png');
			//const filePath = item.fileCoursNm + "/" + item.allFileNm.replace('.tif','.png');
			const filePath = item.innerFileCoursNm.replace(/.tif/i,'.png');
			
			cmmnModule.viewMap(item.vidoId, filePath, min.x, min.y, max.x, max.y, '');
		};

		function fnSetVidoCd(item) {
			const result = item.map(item => {
				return {
					vidoId : item.vidoId,
					vidoNm : item.vidoNm,
					innerFileCoursNm : item.innerFileCoursNm,
					potogrfVidoCd : item.potogrfVidoCd
				}
			});
			self.search().selectedVidoCd(result);
			console.log(self.search().selectedVidoCd());
		}

		/***********************************************************************
		 * Form Model Event
		 **********************************************************************/
		self.newData = function() {
			if (self.search().selectedVidoCd().length === 0) {
				alert('영상을 선택해 주십시오.');
				return false;
			}

			self.setCode();
			self.clearForm();
			self.form().vidoList(self.search().selectedVidoCd());
			$('#objChangeForm').modal('show');
		};

		self.searchModel = function() {
			const vidoCd = self.form().vidoList().map(item => item.potogrfVidoCd);
			const imageNm = vidoCd.some(v => ['3','4','5','7','8'].includes(v)) ? '301' : vidoCd.some(v => ['00'].includes(v)) ? '302' : '303';

			const params = {
				searchImageNm : [imageNm],
				searchProgrsSttusCd : ['444'],
				streYn : 'Y'
			};

			$.ajax({
                url: contextPath+'/cisc010/getAiModelList.do',
                type: 'POST',
                contentType: 'application/json; charset=utf-8',
                data: JSON.stringify(params)
            }).done(function(response){
            	self.modelForm().modelList([...response.baseModelList, ...response.userModelList]);
            });

			$('#searchModel').modal('show');
		};

		self.saveData = function () {

			if (self.form().modelId() === '') {
				alert('모델을 선택해 주십시오.');
				return false;
			}

            $.ajax({
                url: contextPath+'/cisc015/saveAiModelAnals.do',
                type: 'POST',
                contentType: 'application/json; charset=utf-8',
                data: ko.toJSON(self.form())
            }).done(function(response){
            	if(response) {
            		alert('저장 되었습니다.');
           		   	$('#objChangeForm').modal('hide');
            	}
            });

        };

		self.clearForm = function () {
            self.form(new FormModel());
        };

		/***********************************************************************
		 * ModelForm Model Event
		 **********************************************************************/
		self.modelForm().modelChecked.subscribe(function(item) {
			self.modelForm().modelSelect(item);
		});

		self.modelSelect = function() {
			const data = self.modelForm().modelSelect();

			if (data === null) {
				alert('선택된 모델이 없습니다.');
				return false;
			}

			$.ajax({
                url: contextPath+'/cisc010/getAiDataSet.do',
                type: 'GET',
                data: {aiDataSetId : data.aiDataSetId}
            }).done(function(response){
            	const result = response.tnAiDataSet;

            	const bandNm = result.bandNm.split(',');
            	result.bandNm1 = bandNm[0]||'';
            	result.bandNm2 = bandNm[1]||'';
            	result.bandNm3 = bandNm[2]||'';
            	result.bandNm4 = bandNm[3]||'';

            	result.classNm = result.imageNm === '301' ? '건물,주차장,도로,가로수,논,밭,산림,나지,비대상지' : '건물,도로,논,밭,산림,비대상지';

            	const model = ko.mapping.fromJS(self.form());
            	ko.mapping.fromJS(Object.assign(data,result), model);
            });

			$('#searchModel').modal('hide');
		};

	}

   	return {
        init : function() {
        	ko.cleanNode($model);
        	ko.applyBindings(new ObjChangeModel(), $model);
        }
    };

}());
