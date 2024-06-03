var ChangeDetctModule = (function() {
	const $model = $('#changeDetctModel')[0];

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
		self.chngeDetctAlgrthCd = ko.observable('').extend({required: {params: true, message: '변화탐지 방법을 선택해 주십시오.'}});
		self.classCd = ko.observable('401');
		self.stdrVidoId = ko.observable('').extend({required: {params: true, message: '기준 영상을 선택해 주십시오.'}});
		self.cvaPt = ko.observable('');
		self.chngeDetctAlgrthCdOptions = ko.observableArray([]);
		self.classCdOptions = ko.observableArray([]);
		self.stdrVidoIdOptions = ko.observableArray([]);
		self.vidoList = ko.observableArray([]);

		self.errors = ko.validation.group(self);
	}

	const ChangeDetctModel = function() {
		const self = this;

		self.search = ko.observable(new SearchModel());
		self.form = ko.observable(new FormModel());

		/**************************************************************
		* Common Event
		**************************************************************/
		self.setCode = function () {
			cmmnModule.getCmmnCode({groupCode:'50'}).then(result => self.form().chngeDetctAlgrthCdOptions(result));
			cmmnModule.getCmmnCode({groupCode:'40'}).then(result => self.form().classCdOptions(result));
		};

		/**************************************************************
		* Search Model Event
		**************************************************************/
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
//
//			fnSetVidoCd(item);

			const join = self.search().soilChecked().concat(self.search().etcChecked()).concat(item);
			fnSetVidoCd(join);
		});

		self.search().soilChecked.subscribe(function(item) {
//			if (item.length > 0) {
//				self.search().aeroChecked([]);
//				self.search().etcChecked([]);
//			}
//
//			fnSetVidoCd(item);

			const join = self.search().aeroChecked().concat(self.search().etcChecked()).concat(item);
			fnSetVidoCd(join);
		});

		self.search().etcChecked.subscribe(function(item) {
//			if (item.length > 0) {
//				self.search().soilChecked([]);
//				self.search().aeroChecked([]);
//			}
//
//			fnSetVidoCd(item);

			const join = self.search().aeroChecked().concat(self.search().soilChecked()).concat(item);
			fnSetVidoCd(join);
		});

		self.viewImg = function(item) {
			const min = cmmnModule.transform(item.rbtmCrdntY, item.ltopCrdntX, item.mapPrjctnCn, 'EPSG:5179');
			const max = cmmnModule.transform(item.ltopCrdntY, item.rbtmCrdntX, item.mapPrjctnCn, 'EPSG:5179');
//			const filePath = contextPath+'/img/thumnail/'+item.fileNm.replace('.tif','.png');
//			const filePath = item.fileCoursNm + "/" + item.allFileNm.replace('.tif','.png');
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

		/**************************************************************
		* Form Model Event
		**************************************************************/
		self.newData = function() {
			if (self.search().selectedVidoCd().length === 0) {
				alert('영상을 선택해 주십시오.');
				return false;
			}

			if (self.search().selectedVidoCd().length !== 2) {
				alert('영상을 2개 선택해 주십시오.');
				return false;
			}

			self.setCode();
			self.clearForm();
			self.form().vidoList(self.search().selectedVidoCd());
			self.form().stdrVidoIdOptions(
				self.search().selectedVidoCd().map(item => {
					return {
						value : item.vidoId,
						text : item.innerFileCoursNm
					}
				})
			);
			$('#changeDetctForm').modal('show');
		};

		self.saveData = function () {

			if (self.form().errors().length > 0) {
                alert(self.form().errors()[0]);
                return false;
            } else {
            	$.ajax({
                    url: contextPath+'/cisc017/saveThChngeDetct.do',
                    type: 'POST',
                    contentType: 'application/json; charset=utf-8',
                    data: ko.toJSON(self.form())
                }).done(function(response){
                	if(response) {
                		alert('저장 되었습니다.');
               		   	$('#changeDetctForm').modal('hide');
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
        	ko.applyBindings(new ChangeDetctModel(), $model);
        }
    };

}());
