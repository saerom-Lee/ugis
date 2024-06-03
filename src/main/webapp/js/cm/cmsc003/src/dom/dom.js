/**
 * DOM 조작 관련 네임스페이스 
 */
CMSC003.DOM = (() => {
	return {
		/**
		 * ROI 4모서리 좌표를 인풋에 입력
		 * @param {number[]} extent - 사용자 정의 ROI 영역의 extent ex) [959833.0460850189, 1919771.3596533784, 960605.3801928435, 1920161.5745799732] 
		 */
		inputROIToForm(extent) {
			if (Array.isArray(extent) && extent.length === 4) {
				// 4326
				const extent4326 = CMSC003.GIS.convert5179To4326Extent(extent);
				$('#inputULX').val(extent4326[0].toFixed(6));
				$('#inputULY').val(extent4326[3].toFixed(6));
				$('#inputLRX').val(extent4326[2].toFixed(6));
				$('#inputLRY').val(extent4326[1].toFixed(6));

				// 5186
				const extent5186 = CMSC003.GIS.convert5179To5186Extent(extent);
				$('#inputULX2').val(extent5186[0]);
				$('#inputULY2').val(extent5186[3]);
				$('#inputLRX2').val(extent5186[2]);
				$('#inputLRY2').val(extent5186[1]);
				//				console.log('ulx', extent5186[0]);
				//				console.log('uly', extent5186[3]);
				//				console.log('lrx', extent5186[2]);
				//				console.log('lry', extent5186[1]);

				// 5179
				$('#inputULX3').val(extent[0]);
				$('#inputULY3').val(extent[3]);
				$('#inputLRX3').val(extent[2]);
				$('#inputLRY3').val(extent[1]);

				// 32652
				const extent32652 = CMSC003.GIS.convert5179To32652Extent(extent);
				$('#inputULX4').val(extent32652[0]);
				$('#inputULY4').val(extent32652[3]);
				$('#inputLRX4').val(extent32652[2]);
				$('#inputLRY4').val(extent32652[1]);
				
				
				// 5187 tmp 
				var mincoord = [extent[0], extent[1]];
				var maxcoord = [extent[2], extent[3]];
				var min5187 = proj4('EPSG:5179', 'EPSG:5187', mincoord);
				var max5187 = proj4('EPSG:5179', 'EPSG:5187', maxcoord);
				$('#inputULX5').val(min5187[0]);
				$('#inputULY5').val(max5187[1]);
				$('#inputLRX5').val(max5187[0]);
				$('#inputLRY5').val(min5187[1]);			
				
				console.log();
			}
		},

		/**
		 * 경위도 ROI만 입력한 경우 나머지 좌표계의 ROI도 계산해서 입력함
		 */
		inputROIToHiddenForm(ulx, uly, lrx, lry) {
			const convExtent = CMSC003.GIS.updateROIExtentBy4326(ulx, uly, lrx, lry);
			console.log(convExtent);
			// 5186
			$('#inputULX2').val(convExtent.EPSG5186.ulx);
			$('#inputULY2').val(convExtent.EPSG5186.uly);
			$('#inputLRX2').val(convExtent.EPSG5186.lrx);
			$('#inputLRY2').val(convExtent.EPSG5186.lry);

			// 5179
			$('#inputULX3').val(convExtent.EPSG5179.ulx);
			$('#inputULY3').val(convExtent.EPSG5179.uly);
			$('#inputLRX3').val(convExtent.EPSG5179.lrx);
			$('#inputLRY3').val(convExtent.EPSG5179.lry);

			// 32652
			$('#inputULX4').val(convExtent.EPSG32652.ulx);
			$('#inputULY4').val(convExtent.EPSG32652.uly);
			$('#inputLRX4').val(convExtent.EPSG32652.lrx);
			$('#inputLRY4').val(convExtent.EPSG32652.lry);
		},

		/**
		 * ROI 초기화
		 */
		resetROISelection() {
			$('#inputULX').val('');
			$('#inputULY').val('');
			$('#inputLRX').val('');
			$('#inputLRY').val('');

			// 5186
			$('#inputULX2').val('');
			$('#inputULY2').val('');
			$('#inputLRX2').val('');
			$('#inputLRY2').val('');

			// 5179
			$('#inputULX3').val('');
			$('#inputLRY3').val('');
			$('#inputLRX3').val('');
			$('#inputULY3').val('');

			// 32652
			$('#inputULX4').val('');
			$('#inputLRY4').val('');
			$('#inputLRX4').val('');
			$('#inputULY4').val('');

			CMSC003.GIS.endSelectingROI();
			CMSC003.GIS.setROIExtent();
			CMSC003.GIS.clearROILayer();
		},

		/**
		 * 검색결과 목록을 업데이트함
		 */
		showSearchResult(json) {
			$(".js-search-result-area div.panel-default").empty();

			// 검색결과 아무것도 없으면 종료
			if (!json) {
				return;
			}
			if (typeof json === 'string') {
				json = JSON.parse(json);
			}
			const digital = [];
			const dem = [];
			const demo = [];
			const landgraphics = [];
			const buldgraphics = [];

			const landsat = [];
			const kompsat = [];
			const sentinel = [];
			const cas = [];

			const aerial = [];
			const ortho = [];

			const flood = [];
			const forestFire = [];
			const landslip = [];
			const earthquake = [];
			const maritime = [];
			const rain = [];
			const typhoon = [];
			const redTide = [];

			const rasterAnalObj = [];
			const vectorAnalObj = [];

			const rasterAnalChg = [];
			const vectorAnalChg = [];

			const dataMap = {};
			console.log(json, 'json')

			const existing = json.existing;

			const digitalList = existing.digitalMap;
			if (digitalList) {
				for (let i = 0; i < digitalList.length; i++) {
					const elem = digitalList[i];
					console.log(elem);
					dataMap[elem.vidoNm] = elem;
					digital.push(elem);
				}
			}

			const demList = existing.dem;
			if (demList) {
				for (let i = 0; i < demList.length; i++) {
					const elem = demList[i];
					console.log(elem);
					//					dataMap[elem.vidoNm] = elem;
					dataMap[`dem-${dem.length}`] = elem;
					dem.push(elem);
				}
			}

			const demoList = existing.demographics;
			if (demoList) {
				for (let i = 0; i < demoList.length; i++) {
					const elem = demoList[i];
					console.log(elem);
					dataMap[elem.vidoNm] = elem;
					demo.push(elem);
				}
			}

			const landgList = existing.landgraphics;
			if (landgList) {
				for (let i = 0; i < landgList.length; i++) {
					const elem = landgList[i];
					console.log(elem);
					dataMap[elem.vidoNm] = elem;
					landgraphics.push(elem);
				}
			}

			const buldList = existing.buldgraphics;
			if (buldList) {
				for (let i = 0; i < buldList.length; i++) {
					const elem = buldList[i];
					console.log(elem);
					dataMap[elem.vidoNm] = elem;
					buldgraphics.push(elem);
				}
			}

			const landsatList = existing.landsat;
			if (landsatList) {
				for (let i = 0; i < landsatList.length; i++) {
					const elem = landsatList[i];
					console.log(elem);
					dataMap[`landsat-${landsat.length}`] = elem;
					landsat.push(elem);
				}
			}

			const kompsatList = existing.kompsat;
			if (kompsatList) {
				for (let i = 0; i < kompsatList.length; i++) {
					const elem = kompsatList[i];
					console.log(elem);
					dataMap[`kompsat-${kompsat.length}`] = elem;
					kompsat.push(elem);
				}
			}

			const sentinelList = existing.sentinel;
			if (sentinelList) {
				for (let i = 0; i < sentinelList.length; i++) {
					const elem = sentinelList[i];
					console.log(elem);
					dataMap[`sentinel-${sentinel.length}`] = elem;
					sentinel.push(elem);
				}
			}

			const casList = existing.cas;
			if (casList) {
				for (let i = 0; i < casList.length; i++) {
					const elem = casList[i];
					console.log(elem);
					dataMap[`cas-${cas.length}`] = elem;
					cas.push(elem);
				}
			}

			const aerialList = existing.airOrientalMap;
			if (aerialList) {
				for (let i = 0; i < aerialList.length; i++) {
					const elem = aerialList[i];
					console.log(elem);
					dataMap[`aerial-${aerial.length}`] = elem;
					aerial.push(elem);
				}
			}

			const orthoList = existing.ortOrientalMap;
			if (orthoList) {
				for (let i = 0; i < orthoList.length; i++) {
					const elem = orthoList[i];
					console.log(elem);
					dataMap[`ortho-${ortho.length}`] = elem;
					ortho.push(elem);
				}
			}

			const disaster = json.disaster;

			const floodList = disaster.Flood;
			if (floodList) {
				for (let i = 0; i < floodList.length; i++) {
					const elem = floodList[i];
					console.log(elem);
					dataMap[`flood-${flood.length}`] = elem;
					flood.push(elem);
				}
			}

			const forestFireList = disaster.ForestFire;
			if (forestFireList) {
				for (let i = 0; i < forestFireList.length; i++) {
					const elem = forestFireList[i];
					console.log(elem);
					dataMap[`forestFire-${forestFire.length}`] = elem;
					forestFire.push(elem);
				}
			}

			const landslipList = disaster.Landslide;
			if (landslipList) {
				for (let i = 0; i < landslipList.length; i++) {
					const elem = landslipList[i];
					console.log(elem);
					dataMap[`landslide-${landslip.length}`] = elem;
					landslip.push(elem);
				}
			}

			const earthList = disaster.Earthquake;
			if (earthList) {
				for (let i = 0; i < earthList.length; i++) {
					const elem = earthList[i];
					console.log(elem);
					dataMap[`earthquake-${earthquake.length}`] = elem;
					earthquake.push(elem);
				}
			}

			const rainList = disaster.Rain;
			if (rainList) {
				for (let i = 0; i < rainList.length; i++) {
					const elem = rainList[i];
					console.log(elem);
					dataMap[`rain-${rain.length}`] = elem;
					rain.push(elem);
				}
			}

			const typhoonList = disaster.Typhoon;
			if (typhoonList) {
				for (let i = 0; i < typhoonList.length; i++) {
					const elem = typhoonList[i];
					console.log(elem);
					dataMap[`typhoon-${typhoon.length}`] = elem;
					typhoon.push(elem);
				}
			}

			const maritimeList = disaster.MaritimeDisaster;
			if (maritimeList) {
				for (let i = 0; i < maritimeList.length; i++) {
					const elem = maritimeList[i];
					console.log(elem);
					dataMap[`maritime-${maritime.length}`] = elem;
					maritime.push(elem);
				}
			}
			// 적조
			const redTideList = disaster.redTide;
			if (redTideList) {
				for (let i = 0; i < redTideList.length; i++) {
					const elem = redTideList[i];
					console.log(elem);
					dataMap[`forestFire-${redTide.length}`] = elem;
					redTide.push(elem);
				}
			}

			const analysis = json.analysis;

			const rasterAnalObjList = analysis.objectExt.raster;
			if (rasterAnalObjList) {
				for (let i = 0; i < rasterAnalObjList.length; i++) {
					const elem = rasterAnalObjList[i];
					console.log(elem);
					dataMap[`rasteranalobj-${rasterAnalObj.length}`] = elem;
					rasterAnalObj.push(elem);
				}
			}

			const vectorAnalObjList = analysis.objectExt.vector;
			if (vectorAnalObjList) {
				for (let i = 0; i < vectorAnalObjList.length; i++) {
					const elem = vectorAnalObjList[i];
					console.log(elem);
					dataMap[`vectoranalobj-${vectorAnalObj.length}`] = elem;
					vectorAnalObj.push(elem);
				}
			}

			const rasterAnalChgList = analysis.changeDet.raster;
			if (rasterAnalChgList) {
				for (let i = 0; i < rasterAnalChgList.length; i++) {
					const elem = rasterAnalChgList[i];
					console.log(elem);
					dataMap[`rasteranalchg-${rasterAnalChg.length}`] = elem;
					rasterAnalChg.push(elem);
				}
			}

			const vectorAnalChgList = analysis.changeDet.vector;
			if (vectorAnalChgList) {
				for (let i = 0; i < vectorAnalChgList.length; i++) {
					const elem = vectorAnalChgList[i];
					console.log(elem);
					dataMap[`vectoranalchg-${vectorAnalChg.length}`] = elem;
					vectorAnalChg.push(elem);
				}
			}

			CMSC003.Storage.set('searchDataMap', dataMap);


			const outerPanel = $(".js-search-result-area div.panel-default");

			// 제목
			const panelTitle = $('<div class="in-panel-title">').text('재난정보 목록');
			outerPanel.append(panelTitle);

			// 검색결과 요약
			const resultList = $('<ul class="result-list">');
			outerPanel.append(resultList);
			const resultListLi1 = $('<li>').text(`재난(${flood.length + forestFire.length + landslip.length + earthquake.length + maritime.length + rain.length + typhoon.length + redTide.length}건)`);
			resultList.append(resultListLi1);


			outerPanel.append(resultList);

			// 기존 데이터
			const existData = $('<div class="in-panel-title">').text(`기존 데이터`);

			// 검색결과 세부영역
			const resultList1 = $('<div id="resultList1">').append(existData)
			outerPanel.append(resultList1);

			// 기존 데이터 영역 dl
			const dl = $('<dl class="result-list-input">');
			resultList1.append(dl);

			//const vectorTitle = $('<dt>').text(`수치지도(${digital.length}건)`);
			const vectorTitle = $('<dt title="수치지도">');
			const dd_vector = $('<dd class="js-control-checkbox-digital">');
			dd_vector.append($('<input type="checkbox" class="js-control-checkbox-digital-all" id="vido-id-vector-all">'));
			dd_vector.append($('<label for="vido-id-vector-all">').text(`수치지도(${digital.length}건)`));
			vectorTitle.append(dd_vector);
			dl.append(vectorTitle);

			const aerialTitle = $('<dt title="항공영상">');
			const dd_aerial = $('<dd class="js-control-checkbox-aerial">');
			dd_aerial.append($('<input type="checkbox" class="js-control-checkbox-aerial-all" id="vido-id-aerial-all">'));
			dd_aerial.append($('<label for="vido-id-aerial-all">').text(`항공영상(${aerial.length}건)`));
			aerialTitle.append(dd_aerial);
			dl.append(aerialTitle);

			const orthoTitle = $('<dt title="정사영상">');
			const dd_ortho = $('<dd class="js-control-checkbox-ortho">');
			dd_ortho.append($('<input type="checkbox" class="js-control-checkbox-ortho-all" id="vido-id-ortho-all">'));
			dd_ortho.append($('<label for="vido-id-ortho-all">').text(`정사영상(${ortho.length}건)`));
			orthoTitle.append(dd_ortho);
			dl.append(orthoTitle);

			const satelliteTitle = $('<dt title="위성영상">');
			const dd_satellite = $('<dd class="js-control-checkbox-satellite">');
			dd_satellite.append($('<input type="checkbox" class="js-control-checkbox-satellite-all" id="vido-id-satellite-all">'));
			dd_satellite.append($('<label for="vido-id-satellite-all">').text(`위성영상(${landsat.length + kompsat.length + sentinel.length + cas.length}건)`));
			satelliteTitle.append(dd_satellite);
			dl.append(satelliteTitle);

			const demTitle = $('<dt title="DEM">');
			const dd_dem = $('<dd class="js-control-checkbox-dem">');
			dd_dem.append($('<input type="checkbox" class="js-control-checkbox-dem-all" id="vido-id-dem-all">'));
			dd_dem.append($('<label for="vido-id-dem-all">').text(`DEM(${dem.length}건)`));
			demTitle.append(dd_dem);
			dl.append(demTitle);

			const demoTitle = $('<dt title="인구통계">');
			const dd_demo = $('<dd class="js-control-checkbox-demo">');
			dd_demo.append($('<input type="checkbox" class="js-control-checkbox-demo-all" id="vido-id-demo-all">'));
			dd_demo.append($('<label for="vido-id-demo-all">').text(`인구통계(${demo.length}건)`));
			demoTitle.append(dd_demo);
			dl.append(demoTitle);

			const landgTitle = $('<dt title="지가통계">');
			const dd_landg = $('<dd class="js-control-checkbox-landg">');
			dd_landg.append($('<input type="checkbox" class="js-control-checkbox-landg-all" id="vido-id-landg-all">'));
			dd_landg.append($('<label for="vido-id-landg-all">').text(`지가통계(${demo.length}건)`));
			landgTitle.append(dd_landg);
			dl.append(landgTitle);

			const buldTitle = $('<dt title="건물통계">');
			const dd_buld = $('<dd class="js-control-checkbox-buld">');
			dd_buld.append($('<input type="checkbox" class="js-control-checkbox-buld-all" id="vido-id-buld-all">'));
			dd_buld.append($('<label for="vido-id-buld-all">').text(`건물통계(${demo.length}건)`));
			buldTitle.append(dd_buld);
			dl.append(buldTitle);

			// 수치지도 데이터 반복문
			for (let i = 0; i < digital.length; i++) {
				const elem = digital[i];
				console.log(elem);
				const dd_vector = $('<dd class="ui-result-child-indent" title="' + elem.vidoNmKr + '">');
				const div = $('<div>');
				const check_vector = $(`<input type="checkbox" class="js-search-result-digital" value="${elem.vidoNm}" id="vido-id-digital-${i}">`);
				const label_vector = $(`<label for="vido-id-digital-${i}">`).text(elem.vidoNmKr);
				const btn_vector = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-digital-layer" value="${elem.vidoNm}"><i class="fas fa-search"></i>미리보기</button>`);
				div.append(check_vector).append(label_vector).append(btn_vector);
				dd_vector.append(div);
				vectorTitle.append(dd_vector);
			}

			// 인구통계 데이터 반복문
			for (let i = 0; i < demo.length; i++) {
				const elem = demo[i];
				console.log(elem);
				const dd_vector = $('<dd class="ui-result-child-indent" title="' + elem.vidoNmKr + '">');
				const div = $('<div>');
				const check_vector = $(`<input type="checkbox" class="js-search-result-demo" value="${elem.vidoNm}" id="vido-id-demo-${i}">`);
				const label_vector = $(`<label for="vido-id-demo-${i}">`).text(elem.vidoNmKr);
				const btn_vector = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-demo-layer" value="${elem.vidoNm}"><i class="fas fa-search"></i>미리보기</button>`);
				div.append(check_vector).append(label_vector).append(btn_vector);
				dd_vector.append(div);
				demoTitle.append(dd_vector);
			}

			// 지가통계 데이터 반복문
			for (let i = 0; i < landgraphics.length; i++) {
				const elem = landgraphics[i];
				console.log(elem);
				const dd_vector = $('<dd class="ui-result-child-indent" title="' + elem.vidoNmKr + '">');
				const div = $('<div>');
				const check_vector = $(`<input type="checkbox" class="js-search-result-landg" value="${elem.vidoNm}" id="vido-id-landg-${i}">`);
				const label_vector = $(`<label for="vido-id-landg-${i}">`).text(elem.vidoNmKr);
				const btn_vector = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-landg-layer" value="${elem.vidoNm}"><i class="fas fa-search"></i>미리보기</button>`);
				div.append(check_vector).append(label_vector).append(btn_vector);
				dd_vector.append(div);
				landgTitle.append(dd_vector);
			}

			// 건물통계 데이터 반복문
			for (let i = 0; i < buldgraphics.length; i++) {
				const elem = buldgraphics[i];
				console.log(elem);
				const dd_vector = $('<dd class="ui-result-child-indent" title="' + elem.vidoNmKr + '">');
				const div = $('<div>');
				const check_vector = $(`<input type="checkbox" class="js-search-result-buld" value="${elem.vidoNm}" id="vido-id-buld-${i}">`);
				const label_vector = $(`<label for="vido-id-buld-${i}">`).text(elem.vidoNmKr);
				const btn_vector = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-buld-layer" value="${elem.vidoNm}"><i class="fas fa-search"></i>미리보기</button>`);
				div.append(check_vector).append(label_vector).append(btn_vector);
				dd_vector.append(div);
				buldTitle.append(dd_vector);
			}

			// dem 데이터 반복문
			for (let i = 0; i < dem.length; i++) {
				const elem = dem[i];
				console.log(elem);
				const dd_vector = $('<dd class="ui-result-child-indent" title="' + elem.fullFileCoursNm + '">');
				const div = $('<div>');
				const check_vector = $(`<input type="checkbox" class="js-search-result-dem" value="${i}" id="vido-id-dem-${i}">`);
				const label_vector = $(`<label for="vido-id-dem-${i}">`).text(elem.vidoNm);
				const btn_vector = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-dem-layer" value="${i}"><i class="fas fa-search"></i>미리보기</button>`);
				if (!elem.thumbnail) {
					$(btn_vector).prop('disabled', true);
				}
				div.append(check_vector).append(label_vector).append(btn_vector);
				dd_vector.append(div);
				demTitle.append(dd_vector);
			}

			// 위성영상 데이터 반복문
			if (kompsat) {
				for (let i = 0; i < kompsat.length; i++) {
					const elem = kompsat[i];
					console.log(elem);
					const dd = $('<dd class="ui-result-child-indent" title="' + elem.fullFileCoursNm + '">');
					const div = $('<div>');
					const check = $(`<input type="checkbox" class="js-search-result-kompsat" value="${i}" id="vido-id-kompsat-${i}">`);
					const label = $(`<label for="vido-id-kompsat-${i}">`).text(elem.vidoNm);
					const btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-kompsat-image-layer" value="${i}"><i class="fas fa-search"></i>미리보기</button>`);
					if (!elem.thumbnail) {
						$(btn).prop('disabled', true);
					}
					div.append(check).append(label).append(btn);
					dd.append(div);
					satelliteTitle.append(dd);

				}
			}

			// 위성영상 데이터 반복문
			if (landsat) {
				for (let i = 0; i < landsat.length; i++) {
					const elem = landsat[i];
					console.log(elem);
					const dd = $('<dd class="ui-result-child-indent" title="' + elem.fullFileCoursNm + '">');
					const div = $('<div>');
					const check = $(`<input type="checkbox" class="js-search-result-landsat" value="${i}" id="vido-id-landsat-${i}">`);
					const label = $(`<label for="vido-id-landsat-${i}">`).text(elem.vidoNm);
					const btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-landsat-image-layer" value="${i}"><i class="fas fa-search"></i>미리보기</button>`);
					if (!elem.thumbnail) {
						$(btn).prop('disabled', true);
					}
					div.append(check).append(label).append(btn);
					dd.append(div);
					satelliteTitle.append(dd);

				}
			}

			if (sentinel) {
				for (let i = 0; i < sentinel.length; i++) {
					const elem = sentinel[i];
					console.log(elem);
					const dd = $('<dd class="ui-result-child-indent" title="' + elem.fullFileCoursNm + '">');
					const div = $('<div>');
					const check = $(`<input type="checkbox" class="js-search-result-sentinel" value="${i}" id="vido-id-sentinel-${i}">`);
					const label = $(`<label for="vido-id-sentinel-${i}">`).text(elem.vidoNm);
					const btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-sentinel-image-layer" value="${i}"><i class="fas fa-search"></i>미리보기</button>`);
					if (!elem.thumbnail) {
						$(btn).prop('disabled', true);
					}
					div.append(check).append(label).append(btn);
					dd.append(div);
					satelliteTitle.append(dd);

				}
			}

			if (cas) {
				for (let i = 0; i < cas.length; i++) {
					const elem = cas[i];
					console.log(elem);
					const dd = $('<dd class="ui-result-child-indent" title="' + elem.fullFileCoursNm + '">');
					const div = $('<div>');
					const check = $(`<input type="checkbox" class="js-search-result-cas" value="${i}" id="vido-id-cas-${i}">`);
					const label = $(`<label for="vido-id-sentinel-${i}">`).text(elem.vidoNm);
					const btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-cas-image-layer" value="${i}"><i class="fas fa-search"></i>미리보기</button>`);
					if (!elem.thumbnail) {
						$(btn).prop('disabled', true);
					}
					div.append(check).append(label).append(btn);
					dd.append(div);
					satelliteTitle.append(dd);

				}
			}

			// 항공영상 데이터 반복문
			if (aerial) {
				for (let i = 0; i < aerial.length; i++) {
					const elem = aerial[i];
					console.log(elem);
					const dd = $('<dd class="ui-result-child-indent" title="' + elem.fullFileCoursNm + '">');
					const div = $('<div>');
					const check = $(`<input type="checkbox" class="js-search-result-aerial" value="${i}" id="vido-id-aerial-${i}">`);
					const label = $(`<label for="vido-id-aerial-${i}">`).text(elem.vidoNm);
					const btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-aerial-image-layer" value="${i}"><i class="fas fa-search"></i>미리보기</button>`);
					if (!elem.thumbnail) {
						$(btn).prop('disabled', true);
					}
					div.append(check).append(label).append(btn);
					dd.append(div);
					aerialTitle.append(dd);

				}
			}

			// 정사영상 데이터 반복문
			if (ortho) {
				for (let i = 0; i < ortho.length; i++) {
					const elem = ortho[i];
					console.log(elem);
					const dd = $('<dd class="ui-result-child-indent" title="' + elem.fullFileCoursNm + '">');
					const div = $('<div>');
					const check = $(`<input type="checkbox" class="js-search-result-ortho" value="${i}" id="vido-id-ortho-${i}">`);
					const label = $(`<label for="vido-id-ortho-${i}">`).text(elem.vidoNm);
					const btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-ortho-image-layer" value="${i}"><i class="fas fa-search"></i>미리보기</button>`);
					if (!elem.thumbnail) {
						$(btn).prop('disabled', true);
					}
					div.append(check).append(label).append(btn);
					dd.append(div);
					orthoTitle.append(dd);

				}
			}

			const resultList2 = $('<div id="resultList2">')
			outerPanel.append(resultList2);

			// 긴급 영상
			const emerData = $('<div class="in-panel-title">').text(`긴급 영상`);
			resultList2.append(emerData);

			// 긴급 영상 영역 dl
			const dl2 = $('<dl class="result-list-input">');
			resultList2.append(dl2);

			//			const rainTitle = $('<dt>').text(`폭우(${rain.length}건)`);
			//			dl2.append(rainTitle);

			//const earthquakeTitle = $('<dt>').text(`지진(${earthquake.length}건)`);
			const earthquakeTitle = $('<dt title="지진">');
			dl2.append(earthquakeTitle);

			const dd_earthquake = $('<dd class="js-control-checkbox-earthquake">');
			earthquakeTitle.append(dd_earthquake);

			dd_earthquake.append($('<input type="checkbox" class="js-control-checkbox-earthquake-all" id="vido-id-earthquake-all">'));
			dd_earthquake.append($('<label for="vido-id-earthquake-all">').text(`지진(${earthquake.length}건)`));



			const forestFireTitle = $('<dt title="산불">');
			dl2.append(forestFireTitle);

			const dd_forestFire = $('<dd class="js-control-checkbox-forestFire">');
			forestFireTitle.append(dd_forestFire);

			dd_forestFire.append($('<input type="checkbox" class="js-control-checkbox-forestFire-all" id="vido-id-forestFire-all">'));
			dd_forestFire.append($('<label for="vido-id-forestFire-all">').text(`산불(${forestFire.length}건)`));

			const redTideTitle = $('<dt title="적조">');
			dl2.append(redTideTitle);

			const dd_redTide = $('<dd class="js-control-checkbox-redTide">');
			redTideTitle.append(dd_redTide);

			dd_redTide.append($('<input type="checkbox" class="js-control-checkbox-redTide-all" id="vido-id-redTide-all">'));
			dd_redTide.append($('<label for="vido-id-redTide-all">').text(`적조(${redTide.length}건)`));


			//			const typhoonTitle = $('<dt>').text(`태풍(${typhoon.length}건)`);
			//			dl2.append(typhoonTitle);

			const floodTitle = $('<dt title="수해">');
			const dd_flood = $('<dd class="js-control-checkbox-flood">');
			dd_flood.append($('<input type="checkbox" class="js-control-checkbox-flood-all" id="vido-id-flood-all">'));
			dd_flood.append($('<label for="vido-id-flood-all">').text(`수해(${flood.length}건)`));
			floodTitle.append(dd_flood);
			dl2.append(floodTitle);

			const landslipTitle = $('<dt title="산사태">');
			const dd_landslip = $('<dd class="js-control-checkbox-landslip">');
			dd_landslip.append($('<input type="checkbox" class="js-control-checkbox-landslide-all" id="vido-id-landslide-all">'));
			dd_landslip.append($('<label for="vido-id-landslide-all">').text(`산사태(${landslip.length}건)`));
			landslipTitle.append(dd_landslip);
			dl2.append(landslipTitle);

			const maritimeTitle = $('<dt title="해양재난">');
			const dd_maritime = $('<dd class="js-control-checkbox-maritime">');
			dd_maritime.append($('<input type="checkbox" class="js-control-checkbox-maritime-all" id="vido-id-maritime-all">'));
			dd_maritime.append($('<label for="vido-id-maritime-all">').text(`해양재난(${maritime.length}건)`));
			maritimeTitle.append(dd_maritime);
			dl2.append(maritimeTitle);

			// floodType 데이터 반복문
			if (flood) {
				for (let i = 0; i < flood.length; i++) {
					const elem = flood[i];
					console.log(elem);
					const dd = $('<dd class="ui-result-child-indent" title="' + elem.fullFileCoursNm + '">');
					const div = $('<div>');
					const check = $(`<input type="checkbox" class="js-search-result-flood" value="${i}" id="vido-id-flood-${i}">`);
					const label = $(`<label for="vido-id-flood-${i}">`).text(elem.vidoNm);
					const btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-flood-image-layer" value="${i}"><i class="fas fa-search"></i>미리보기</button>`);
					if (!elem.thumbnail) {
						$(btn).prop('disabled', true);
					}
					div.append(check).append(label).append(btn);
					dd.append(div);
					floodTitle.append(dd);

				}
			}

			// forestFire 데이터 반복문
			if (forestFire) {
				for (let i = 0; i < forestFire.length; i++) {
					const elem = forestFire[i];
					console.log(elem);
					const dd = $('<dd class="ui-result-child-indent" title="' + elem.fullFileCoursNm + '">');
					const div = $('<div>');
					const check = $(`<input type="checkbox" class="js-search-result-forestfire" value="${i}" id="vido-id-forest-${i}">`);
					const label = $(`<label for="vido-id-forest-${i}">`).text(elem.vidoNm);
					const btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-forestfire-image-layer" value="${i}"><i class="fas fa-search"></i>미리보기</button>`);
					if (!elem.thumbnail) {
						$(btn).prop('disabled', true);
					}
					div.append(check).append(label).append(btn);
					dd.append(div);
					forestFireTitle.append(dd);

				}
			}

			// landslip 데이터 반복문
			if (landslip) {
				for (let i = 0; i < landslip.length; i++) {
					const elem = landslip[i];
					console.log(elem);
					const dd = $('<dd class="ui-result-child-indent" title="' + elem.fullFileCoursNm + '">');
					const div = $('<div>');
					const check = $(`<input type="checkbox" class="js-search-result-landslip" value="${i}" id="vido-id-land-${i}">`);
					const label = $(`<label for="vido-id-land-${i}">`).text(elem.vidoNm);
					const btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-landslide-image-layer" value="${i}"><i class="fas fa-search"></i>미리보기</button>`);
					if (!elem.thumbnail) {
						$(btn).prop('disabled', true);
					}
					div.append(check).append(label).append(btn);
					dd.append(div);
					landslipTitle.append(dd);

				}
			}

			// rain 데이터 반복문
			//			if (rain) {
			//				for (let i = 0; i < rain.length; i++) {
			//					const elem = rain[i];
			//					console.log(elem);
			//					let list = CMSC003.Storage.get('searchDataMap');
			//					if (!list) {
			//						list = {};
			//					}
			//					list[elem.vidoId] = elem;
			//					const dd = $('<dd>');
			//					const check = $(`<input type="checkbox" class="js-search-result-rain" value="${i}" id="vido-id-${i}">`);
			//					const label = $(`<label for="vido-id-${i}">`).text(elem.vidoNm);
			//					const btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-image-layer" value="${i}"><i class="fas fa-search"></i>미리보기</button>`);
			//					if(!elem.thumbnail) {
			//						$(btn).prop('disabled', true);
			//					}
			//					dd.append(check).append(label).append(btn);
			//					rainTitle.append(dd);
			//
			//					CMSC003.Storage.set('searchDataMap', list);
			//				}
			//			}

			// earthquake 데이터 반복문
			if (earthquake) {
				for (let i = 0; i < earthquake.length; i++) {
					const elem = earthquake[i];
					console.log(elem);
					const dd = $('<dd class="ui-result-child-indent" title="' + elem.fullFileCoursNm + '">');
					const div = $('<div>');
					const check = $(`<input type="checkbox" class="js-search-result-earthquake" value="${i}" id="vido-id-earth-${i}">`);
					const label = $(`<label for="vido-id-earth-${i}">`).text(elem.vidoNm);
					const btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-earthquake-image-layer" value="${i}"><i class="fas fa-search"></i>미리보기</button>`);
					if (!elem.thumbnail) {
						$(btn).prop('disabled', true);
					}
					div.append(check).append(label).append(btn);
					dd.append(div);
					earthquakeTitle.append(dd);

				}
			}

			// maritime 데이터 반복문
			if (maritime) {
				for (let i = 0; i < maritime.length; i++) {
					const elem = maritime[i];
					console.log(elem);
					const dd = $('<dd class="ui-result-child-indent" title="' + elem.fullFileCoursNm + '">');
					const div = $('<div>');
					const check = $(`<input type="checkbox" class="js-search-result-maritime" value="${i}" id="vido-id-mari-${i}">`);
					const label = $(`<label for="vido-id-mari-${i}">`).text(elem.vidoNm);
					const btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-maritime-image-layer" value="${i}"><i class="fas fa-search"></i>미리보기</button>`);
					if (!elem.thumbnail) {
						$(btn).prop('disabled', true);
					}
					div.append(check).append(label).append(btn);
					dd.append(div);
					maritimeTitle.append(dd);

				}
			}

			// typhoon 데이터 반복문
			//			if (typhoon) {
			//				for (let i = 0; i < typhoon.length; i++) {
			//					const elem = typhoon[i];
			//					console.log(elem);
			//					let list = CMSC003.Storage.get('searchDataMap');
			//					if (!list) {
			//						list = {};
			//					}
			//					list[elem.vidoId] = elem;
			//					const dd = $('<dd>');
			//					const check = $(`<input type="checkbox" class="js-search-result-typhoon" value="${i}" id="vido-id-${i}">`);
			//					const label = $(`<label for="vido-id-${i}">`).text(elem.vidoNm);
			//					const btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-image-layer" value="${i}"><i class="fas fa-search"></i>미리보기</button>`);
			//					if(!elem.thumbnail) {
			//						$(btn).prop('disabled', true);
			//					}
			//					dd.append(check).append(label).append(btn);
			//					typhoonTitle.append(dd);
			//
			//					CMSC003.Storage.set('searchDataMap', list);
			//				}
			//			}
			
			// redTide 적조 데이터 반복문
			if (redTide) {
				for (let i = 0; i < redTide.length; i++) {
					const elem = redTide[i];
					console.log(elem);
					const dd = $('<dd class="ui-result-child-indent" title="' + elem.fullFileCoursNm + '">');
					const div = $('<div>');
					const check = $(`<input type="checkbox" class="js-search-result-redTide" value="${i}" id="vido-id-redT-${i}">`);
					const label = $(`<label for="vido-id-redT-${i}">`).text(elem.vidoNm);
					const btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-redTide-image-layer" value="${i}"><i class="fas fa-search"></i>미리보기</button>`);
					if (!elem.thumbnail) {
						$(btn).prop('disabled', true);
					}
					div.append(check).append(label).append(btn);
					dd.append(div);
					redTideTitle.append(dd);

				}
			}

			// 분석 영상
			const classificationData = $('<div class="in-panel-title">').text(`분석결과 데이터`);

			// 검색결과 세부영역
			const resultList3 = $('<div id="resultList3">').append(classificationData);
			outerPanel.append(resultList3);

			// 분석 데이터 영역 dl
			const dl3 = $('<dl class="result-list-input">');
			resultList3.append(dl3);

			//const vectorAnalObjTitle = $('<dt>').text(`객체추출 벡터 데이터(${vectorAnalObj.length}건)`);
			const vectorAnalObjTitle = $('<dt title="객체추출 벡터 데이터">');
			const dd_vectorAnalObj = $('<dd class="js-control-checkbox-vectorAnalObj">');
			dd_vectorAnalObj.append($('<input type="checkbox" class="js-control-checkbox-vectorAnalObj-all" id="vido-id-vectorAnalObj-all">'));
			dd_vectorAnalObj.append($('<label for="vido-id-vectorAnalObj-all">').text(`객체추출 벡터 데이터(${vectorAnalObj.length}건)`));
			vectorAnalObjTitle.append(dd_vectorAnalObj);
			dl3.append(vectorAnalObjTitle);

			const rasterAnalObjTitle = $('<dt title="객체추출 영상 데이터">');
			const dd_rasterAnalObj = $('<dd class="js-control-checkbox-rasterAnalObj">');
			dd_rasterAnalObj.append($('<input type="checkbox" class="js-control-checkbox-rasterAnalObj-all" id="vido-id-rasterAnalObj-all">'));
			dd_rasterAnalObj.append($('<label for="vido-id-rasterAnalObj-all">').text(`객체추출 영상 데이터(${rasterAnalObj.length}건)`));
			rasterAnalObjTitle.append(dd_rasterAnalObj);
			dl3.append(rasterAnalObjTitle);

			// 분석 벡터 데이터 반복문
			if (vectorAnalObj) {
				for (let i = 0; i < vectorAnalObj.length; i++) {
					const elem = vectorAnalObj[i];
					console.log(elem);
					const dd = $('<dd class="ui-result-child-indent" title="' + elem.vectorFileCoursNm + '">');
					const div = $('<div>');
					const check = $(`<input type="checkbox" class="js-search-result-vectoranalobj" value="${i}" id="vido-id-vanalobj-${i}">`);
					const label = $(`<label for="vido-id-vanalobj-${i}">`).text(elem.vidoNm);
					const btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-shp-obj-layer" value="${i}"><i class="fas fa-search"></i>미리보기</button>`);
					if (!elem.thumbnail) {
						$(btn).prop('disabled', true);
					}
					div.append(check).append(label).append(btn);
					dd.append(div);
					vectorAnalObjTitle.append(dd);
				}
			}

			// 분석 영상 데이터 반복문
			if (rasterAnalObj) {
				for (let i = 0; i < rasterAnalObj.length; i++) {
					const elem = rasterAnalObj[i];
					console.log(elem);
					const dd = $('<dd class="ui-result-child-indent" title="' + elem.fullFileCoursNm + '">');
					const div = $('<div>');
					const check = $(`<input type="checkbox" class="js-search-result-rasteranalobj" value="${i}" id="vido-id-ranalobj-${i}">`);
					const label = $(`<label for="vido-id-ranalobj-${i}">`).text(elem.vidoNm);
					const btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-raster-anal-obj-layer" value="${i}"><i class="fas fa-search"></i>미리보기</button>`);
					if (!elem.thumbnail) {
						$(btn).prop('disabled', true);
					}
					div.append(check).append(label).append(btn);
					dd.append(div);
					rasterAnalObjTitle.append(dd);
				}
			}

			const vectorAnalChgTitle = $('<dt title="변화탐지 벡터 데이터">');
			const dd_vectorAnalChg = $('<dd class="js-control-checkbox-vectorAnalChg">');
			dd_vectorAnalChg.append($('<input type="checkbox" class="js-control-checkbox-vectorAnalChg-all" id="vido-id-vectorAnalChg-all">'));
			dd_vectorAnalChg.append($('<label for="vido-id-vectorAnalChg-all">').text(`변화탐지 벡터 데이터(${vectorAnalChg.length}건)`));
			vectorAnalChgTitle.append(dd_vectorAnalChg);
			dl3.append(vectorAnalChgTitle);

			const rasterAnalChgTitle = $('<dt title="변화탐지 영상 데이터">');
			const dd_rasterAnalChg = $('<dd class="js-control-checkbox-rasterAnalChg">');
			dd_rasterAnalChg.append($('<input type="checkbox" class="js-control-checkbox-rasterAnalChg-all" id="vido-id-rasterAnalChg-all">'));
			dd_rasterAnalChg.append($('<label for="vido-id-rasterAnalChg-all">').text(`변화탐지 영상 데이터(${rasterAnalChg.length}건)`));
			rasterAnalChgTitle.append(dd_rasterAnalChg);
			dl3.append(rasterAnalChgTitle);

			// 분석 벡터 데이터 반복문
			if (vectorAnalChg) {
				for (let i = 0; i < vectorAnalChg.length; i++) {
					const elem = vectorAnalChg[i];
					console.log(elem);
					const dd = $('<dd class="ui-result-child-indent" title="' + elem.vectorFileCoursNm + '">');
					const div = $('<div>');
					const check = $(`<input type="checkbox" class="js-search-result-vectoranalchg" value="${i}" id="vido-id-vanalchg-${i}">`);
					const label = $(`<label for="vido-id-vanalchg-${i}">`).text(elem.vidoNm);
					const btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-shp-chg-layer" value="${i}"><i class="fas fa-search"></i>미리보기</button>`);
					if (!elem.thumbnail) {
						$(btn).prop('disabled', true);
					}
					div.append(check).append(label).append(btn);
					dd.append(div);
					vectorAnalChgTitle.append(dd);
				}
			}

			// 분석 영상 데이터 반복문
			if (rasterAnalChg) {
				for (let i = 0; i < rasterAnalChg.length; i++) {
					const elem = rasterAnalChg[i];
					console.log(elem);
					const dd = $('<dd class="ui-result-child-indent" title="' + elem.fullFileCoursNm + '">');
					const div = $('<div>');
					const check = $(`<input type="checkbox" class="js-search-result-rasteranalchg" value="${i}" id="vido-id-ranalchg-${i}">`);
					const label = $(`<label for="vido-id-ranalchg-${i}">`).text(elem.vidoNm);
					const btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-raster-anal-chg-layer" value="${i}"><i class="fas fa-search"></i>미리보기</button>`);
					if (!elem.thumbnail) {
						$(btn).prop('disabled', true);
					}
					div.append(check).append(label).append(btn);
					dd.append(div);
					rasterAnalChgTitle.append(dd);
				}
			}
		},

		/**
		 * 검색결과 목록을 업데이트함(트리버전)
		 */
		showSearchResultTree(json, panelElem, resetPanelElem) {

			let outerPanel = null;

			if (panelElem) {
				$(panelElem).empty();
				outerPanel = $(panelElem);
			} else {
				$(".js-search-result-area div.panel-default").empty();
				outerPanel = $(".js-search-result-area div.panel-default");
			}

			if (!outerPanel[0]) {
				console.log('결과를 표시할 영역이 없습니다.');
				return;
			}

			if (Array.isArray(resetPanelElem)) {
				for (let i = 0; i < resetPanelElem.length; i++) {
					$(resetPanelElem[i]).empty();
				}

			} else if (resetPanelElem) {
				$(resetPanelElem).empty();
			}


			for (const data of ['resultList1_digital', 'resultList1_aerial', 'resultList1_ortho', 'resultList1_satellite', 'resultList1_dem', 'resultList1_graphics',
				'resultList2_earthquake', 'resultList2_forestFire', 'resultList2_flood', 'resultList2_landslip', 'resultList2_maritime', 'resultList2_redTide',
				'resultList3_vectorAnalObj', 'resultList3_rasterAnalObj', 'resultList3_vectorAnalChg', 'resultList3_rasterAnalChg']) {
				$(`#${data}_tree`).jstree('destroy');
				$(`#${data}_tree`).remove();
			}

			// 기존 데이터
			const digital = [];
			const dem = { length: 0, map: {} };
			const graphics = [];
			// const demo = [];
			// const landgraphics = [];
			// const buldgraphics = [];

			// 위성
			const landsat = { length: 0, map: {} };
			const kompsat = { length: 0, map: {} };
			const sentinel = { length: 0, map: {} };
			const cas = { length: 0, map: {} };

			// 영상
			const aerial = { length: 0, map: {} };
			const ortho = { length: 0, map: {} };

			// 재해
			const flood = [];
			const forestFire = [];
			const landslip = [];
			const earthquake = [];
			const maritime = [];
			const redTide = [];

			// 분석
			const rasterAnalObj = [];
			const vectorAnalObj = [];

			const rasterAnalChg = [];
			const vectorAnalChg = [];

			const dataMap = {};

			const existing = json.existing;

			let count = 0;

			// 수치지도
			const digitalList = existing.digitalMap;
			if (digitalList) {
				for (const e of digitalList) {
					//console.log(e);
					dataMap[e.vidoNm] = e;
					digital.push(e);
				}
			}

			// DEM (year, map)
			const demList = existing.demMap;
			if (demList) {
				/*count = 0;
				for (const e of demList) {
					const {year, map} = e;
					dem.map[year] = [];
					for (const elem of map) {
						//console.log('dem', elem);
						dataMap[`dem-${count}`] = elem;
						dem.map[year].push({index:count, elem:elem});
						count++;
					}
				}
				dem.length = count;*/

				count = 0;
				for (const e1 of demList) {
					const { year, maps } = e1;
					dem.map[year] = [];
					for (const e2 of maps) {
						const { dpi, map } = e2;
						dem.map[year][dpi] = [];
						for (const elem of map) {
							dataMap[`dem-${count}`] = elem;
							dem.map[year][dpi].push({ index: count, elem: elem });
							count++;
						}
					}
				}
				dem.length = count;
			}

			// 통계
			const graphicsList = existing.graphicsMap;
			if (graphicsList) {
				for (const e of graphicsList) {
					//console.log(e);
					dataMap[e.vidoNm] = e;
					graphics.push(e);
				}
			}

			// 위성 - kompsat (satNm, map)
			const kompsatList = existing.satellite[0];
			if (kompsatList) {
				count = 0;
				const { satNm, map } = kompsatList;
				for (const e of map) {
					//					const {folder, fileList} = e;
					//					kompsat.map[folder] = [];
					//					for (const elem of fileList) {
					//						//console.log('komp',elem);
					//						dataMap[`kompsat-${count}`] = elem;
					//						kompsat.map[folder].push({index:count, elem:elem});
					//						count++;
					//					}

					const { date, folderList } = e;

					kompsat.map[date] = {};

					for (const folder of folderList) {
						const { folderNm, fileList } = folder;
						kompsat.map[date][folderNm] = [];
						for (const elem of fileList) {
							//console.log('komp',elem);
							dataMap[`kompsat-${count}`] = elem;
							kompsat.map[date][folderNm].push({ index: count, elem: elem });
							count++;
						}
					}

				}
				kompsat.length = count;
			}

			// 위성 - landsat (satNm, map)
			const landsatList = existing.satellite[1];
			if (landsatList) {
				count = 0;
				const { satNm, map } = landsatList;
				for (const e of map) {
					//					const {folder, fileList} = e;
					//					landsat.map[folder] = [];
					//					for (const elem of fileList) {
					//						//console.log('landsat',elem);
					//						dataMap[`landsat-${count}`] = elem;
					//						landsat.map[folder].push({index:count, elem:elem});
					//						count++;
					//					}
					const { date, folderList } = e;

					landsat.map[date] = {};
					
					for (const folder of folderList) {
						const { folderNm, fileList } = folder;
						landsat.map[date][folderNm] = [];
						// landsat.map[folderNm] = [];
						for (const elem of fileList) {
							//console.log('komp',elem);
							dataMap[`landsat-${count}`] = elem;
							landsat.map[date][folderNm].push({ index: count, elem: elem });
							count++;
						}
					}
				}
				landsat.length = count;
			}

			// 위성 - sentinel (satNm, map)
			const sentinelList = existing.satellite[2];
			if (sentinelList) {
				count = 0;
				const { satNm, map } = sentinelList;
				for (const e of map) {
					//					const {folder, fileList} = e;
					//					sentinel.map[folder] = [];
					//					for (const elem of fileList) {
					//						//console.log('sentinel',elem);
					//						dataMap[`sentinel-${count}`] = elem;
					//						sentinel.map[folder].push({index:count, elem:elem});
					//						count++;
					//					}

					const { date, folderList } = e;

					sentinel.map[date] = {};
					
					for (const folder of folderList) {
						const { folderNm, fileList } = folder;
						sentinel.map[date][folderNm] = [];
						for (const elem of fileList) {
							//console.log('komp',elem);
							dataMap[`sentinel-${count}`] = elem;
							sentinel.map[date][folderNm].push({ index: count, elem: elem });
							count++;
						}
					}
				}
				sentinel.length = count;
			}

			// 위성 - cas (satNm, map)
			const casList = existing.satellite[3];
			if (casList) {
				count = 0;
				const { satNm, map } = casList;
				for (const e of map) {
					//					const {folder, fileList} = e;
					//					cas.map[folder] = [];
					//					for (const elem of fileList) {
					//						//console.log('cas',elem);
					//						dataMap[`cas-${count}`] = elem;
					//						cas.map[folder].push({index:count, elem:elem});
					//						count++;
					//					}
					const { date, folderList } = e;
					cas.map[date] = {};
					
					for (const folder of folderList) {
						const { folderNm, fileList } = folder;
						cas.map[date][folderNm] = [];
						for (const elem of fileList) {
							//console.log('komp',elem);
							dataMap[`cas-${count}`] = elem;
							cas.map[date][folderNm].push({ index: count, elem: elem });
							count++;
						}
					}
				}
				cas.length = count;
			}

			// 항공영상 (year, map)
			const aerialList = existing.airOrientalMap
			if (aerialList) {
				/*count = 0;
				for (const e of aerialList) {
					const {year, map} = e;
					aerial.map[year] = [];
					for (const elem of map) {
						//console.log('aerial', elem);
						dataMap[`aerial-${count}`] = elem;
						aerial.map[year].push({index:count, elem:elem});
						count++;
					}
				}
				aerial.length = count;*/
				count = 0;
				for (const e1 of aerialList) {
					const { year, maps } = e1;
					aerial.map[year] = {};
					for (const e2 of maps) {
						const { dpi, map } = e2;
						aerial.map[year][dpi] = [];
						for (const elem of map) {
							dataMap[`aerial-${count}`] = elem;
							aerial.map[year][dpi].push({ index: count, elem: elem });
							count++;
						}
					}
				}
				aerial.length = count;
			}

			// 정사영상 (year, map)
			const orthoList = existing.ortOrientalMap;
			if (orthoList) {
				/*count = 0;
				for (const e of orthoList) {
					const {year, map} = e;
					ortho.map[year] = [];
					for (const elem of map) {
						//console.log('ortho', elem);
						dataMap[`ortho-${count}`] = elem;
						ortho.map[year].push({index:count, elem:elem});
						count++;
					}
				}
				ortho.length = count;*/
				count = 0;
				for (const e1 of orthoList) {
					const { year, maps } = e1;
					ortho.map[year] = {};
					for (const e2 of maps) {
						const { dpi, map } = e2;
						ortho.map[year][dpi] = [];
						for (const elem of map) {
							dataMap[`ortho-${count}`] = elem;
							ortho.map[year][dpi].push({ index: count, elem: elem });
							count++;
						}
					}
				}
				ortho.length = count;
			}

			const disaster = json.disaster;

			// 산불
			const forestFireList = disaster.ForestFire;
			if (forestFireList) {
				for (const e of forestFireList) {
					//console.log(e);
					dataMap[`forestFire-${forestFire.length}`] = e;
					forestFire.push(e);
				}
			}

			// 지진
			const earthquakeList = disaster.Earthquake;
			if (earthquakeList) {
				for (const e of earthquakeList) {
					//console.log(e);
					dataMap[`earthquake-${earthquake.length}`] = e;
					earthquake.push(e);
				}
			}

			// 산사태
			const landslipList = disaster.Landslide;
			if (landslipList) {
				for (const e of landslipList) {
					//console.log(e);
					dataMap[`landslide-${landslip.length}`] = e;
					landslip.push(e);
				}
			}

			// 홍수 
			const floodList = disaster.Flood;
			if (floodList) {
				for (const e of floodList) {
					console.log(e);
					dataMap[`flood-${flood.length}`] = e;
					flood.push(e);
				}
			}

			// 해양재난 
			const maritimeList = disaster.MaritimeDisaster;
			if (maritimeList) {
				for (const e of maritimeList) {
					//console.log(e);
					dataMap[`maritime-${maritime.length}`] = e;
					maritime.push(e);
				}
			}
			
			// 적조
			const redTideList = disaster.RedTide;
			if (redTideList) {
				for (const e of redTideList) {
					//console.log(e);
					dataMap[`redTide-${redTide.length}`] = e;
					redTide.push(e);
				}
			}

			const analysis = json.analysis;

			// 객체추출 벡터 
			const vectorAnalObjList = analysis.objectExt.vector;
			if (vectorAnalObjList) {
				for (const e of vectorAnalObjList) {
					//console.log(e);
					dataMap[`vectorAnalObj-${vectorAnalObj.length}`] = e;
					vectorAnalObj.push(e);
				}
			}

			// 객체추출 영상 
			const rasterAnalObjList = analysis.objectExt.raster;
			if (rasterAnalObjList) {
				for (const e of rasterAnalObjList) {
					//console.log(e);
					dataMap[`rasterAnalObj-${rasterAnalObj.length}`] = e;
					rasterAnalObj.push(e);
				}
			}

			// 변화탐지 벡터 
			const vectorAnalChgList = analysis.changeDet.vector;
			if (vectorAnalChgList) {
				for (const e of vectorAnalChgList) {
					//console.log(e);
					dataMap[`vectorAnalChg-${vectorAnalChg.length}`] = e;
					vectorAnalChg.push(e);
				}
			}

			// 변화탐지 영상 
			const rasterAnalChgList = analysis.changeDet.raster;
			if (rasterAnalChgList) {
				for (const e of rasterAnalChgList) {
					//console.log(e);
					dataMap[`rasterAnalChg-${rasterAnalChg.length}`] = e;
					rasterAnalChg.push(e);
				}
			}

			CMSC003.Storage.set('searchDataMap', dataMap);

			// 입력 panel
			const panelTitle = $('<div class="in-panel-title">').text('재난정보 목록');
			outerPanel.append(panelTitle);

			// 검색결과 요약
			const resultList = $('<ul class="result-list">');
			outerPanel.append(resultList);
			const resultListLi1 = $('<li>').text(`재난(${flood.length + forestFire.length + landslip.length + earthquake.length + maritime.length+ redTide.length}건)`);
			resultList.append(resultListLi1);

			// ## 기존 데이터 ##
			const existData = $('<div class="in-panel-title">').text('기존 데이터');
			outerPanel.append(existData);

			// 기존 데이터(resultList1) 영역 생성
			const resultList1 = $('<div id="resultList1">');
			outerPanel.append(resultList1);
			const resultList1_div = $('<div class="result-list-input">');
			resultList1.append(resultList1_div);

			// 기존 데이터 - 수치지도
			const resultList1_vector = $('<div id="resultList1_digital_tree">');
			const vector_tree = $('<ul>');
			const vectorTitle = $('<li class="js-control-digital js-tree-parent-1" id="vido-id-digital-title">');
			const vector_span = $('<span>').text(`수치지도(${digital.length}건)`);
			const vector_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-digital-layer" data-tree="vido-id-digital-title"><i class="fas fa-search"></i></button>`);
			const vector_ul = $('<ul>');
			resultList1_div.append(resultList1_vector);
			resultList1_vector.append(vector_tree);
			vector_tree.append(vectorTitle);
			vectorTitle.append(vector_span);
			vectorTitle.append(vector_btn);
			vectorTitle.append(vector_ul);

			// 기존 데이터 - 항공영상
			const resultList1_aerial = $('<div id="resultList1_aerial_tree">');
			const aerial_tree = $('<ul>');
			const aerialTitle = $('<li class="js-control-aerial js-tree-parent-1" id="vido-id-aerial-title">');
			const aerial_span = $('<span>').text(`항공영상(${aerial.length}건)`)
			const aerial_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-aerial-image-layer" data-tree="vido-id-aerial-title"><i class="fas fa-search"></i></button>`);
			const aerial_ul = $('<ul>');
			resultList1_div.append(resultList1_aerial);
			resultList1_aerial.append(aerial_tree);
			aerial_tree.append(aerialTitle);
			aerialTitle.append(aerial_span);
			aerialTitle.append(aerial_btn);
			aerialTitle.append(aerial_ul);

			// 기존 데이터 - 정사영상
			const resultList1_ortho = $('<div id="resultList1_ortho_tree">');
			const ortho_tree = $('<ul>');
			const orthoTitle = $('<li class="js-control-ortho js-tree-parent-1" id="vido-id-ortho-title">');
			const ortho_span = $('<span>').text(`정사영상(${ortho.length}건)`);
			const ortho_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-ortho-image-layer" data-tree="vido-id-ortho-title"><i class="fas fa-search"></i></button>`);
			const ortho_ul = $('<ul>');
			resultList1_div.append(resultList1_ortho);
			resultList1_ortho.append(ortho_tree);
			ortho_tree.append(orthoTitle);
			orthoTitle.append(ortho_span);
			orthoTitle.append(ortho_btn);
			orthoTitle.append(ortho_ul);

			// 기존 데이터 - 위성영상
			const resultList1_satellite = $('<div id="resultList1_satellite_tree">');
			const satellite_tree = $('<ul>');
			const satelliteTitle = $('<li class="js-control-satellite js-tree-parent-1" id="vido-id-satellite-title">');
			const satellite_span = $('<span>').text(`위성영상(${kompsat.length + sentinel.length + landsat.length + cas.length}건)`);
			const satellite_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-satellite-image-layer" data-tree="vido-id-satellite-title"><i class="fas fa-search"></i></button>`);
			const satellite_ul = $('<ul>');
			resultList1_div.append(resultList1_satellite);
			resultList1_satellite.append(satellite_tree);
			satellite_tree.append(satelliteTitle);
			satelliteTitle.append(satellite_span);
			satelliteTitle.append(satellite_btn);
			satelliteTitle.append(satellite_ul);

			// 기존 데이터 - DEM
			const resultList1_dem = $('<div id="resultList1_dem_tree">');
			const dem_tree = $('<ul>');
			const demTitle = $('<li class="js-control-dem js-tree-parent-1" id="vido-id-dem-title">');
			const dem_span = $('<span>').text(`DEM(${dem.length}건)`);
			const dem_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-dem-layer" data-tree="vido-id-dem-title"><i class="fas fa-search"></i></button>`);
			const dem_ul = $('<ul>');
			resultList1_div.append(resultList1_dem);
			resultList1_dem.append(dem_tree);
			dem_tree.append(demTitle);
			demTitle.append(dem_span);
			demTitle.append(dem_btn);
			demTitle.append(dem_ul);

			// 기존 데이터 - 통계
			const resultList1_graphics = $('<div id="resultList1_graphics_tree">');
			const graphics_tree = $('<ul>');
			const graphicsTitle = $('<li class="js-control-graphics js-tree-parent-1" id="vido-id-graphics-title">');
			const graphics_span = $('<span>').text(`통계(${graphics.length}건)`);
			const graphics_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-graphics-layer" data-tree="vido-id-graphics-title"><i class="fas fa-search"></i></button>`);
			const graphics_ul = $('<ul>');
			resultList1_div.append(resultList1_graphics);
			resultList1_graphics.append(graphics_tree);
			graphics_tree.append(graphicsTitle);
			graphicsTitle.append(graphics_span);
			graphicsTitle.append(graphics_btn);
			graphicsTitle.append(graphics_ul);

			// 기존 데이터 - 수치지도 노드 추가 (image / file)
			for (let i = 0; i < digital.length; i++) {
				const li = $(`<li class="js-search-result-digital js-tree-child-1" value="${digital[i].vidoNm}" id="vido-id-digital-${i}" title="${digital[i].vidoNm}">`);
				const span = $('<span>').text(digital[i].vidoNmKr);
				const btn_vector = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-digital-layer" value="${digital[i].vidoNm}" data-tree="vido-id-digital-${i}"><i class="fas fa-search"></i></button>`);
				vector_ul.append(li.append(span).append(btn_vector));
			}

			// 기존 데이터 - 항공영상 노드 추가 (image / year / dpi / file)
			/*for (const year in aerial.map) {
				const fileList = aerial.map[year];
				// 년도 폴더 생성
				const year_li = $(`<li class="js-tree-parent-2" value="${year}" id="vido-id-aerial-title-${year}">`);
				const year_span = $('<span>').text(year);
				const year_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-aerial-image-layer" data-tree="vido-id-aerial-title-${year}"><i class="fas fa-search"></i></button>`);
				const year_ul = $('<ul>');
				year_li.append(year_span);
				year_li.append(year_btn);
				year_li.append(year_ul);
				for (const elem of fileList) {
					const li = $(`<li class="js-search-result-aerial js-tree-child-2" value="${elem.index}" id="vido-id-aerial-${elem.index}">`);
					const span = $('<span>').text(elem.elem.vidoNm);
					const btn_aerial = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-aerial-image-layer" value="${elem.elem.vidoNm}" data-tree="vido-id-aerial-${elem.index}"><i class="fas fa-search"></i></button>`);
					if (!elem.elem.thumbnail) {
						btn_aerial.attr("disabled", true);
					}
					year_ul.append(li.append(span).append(btn_aerial));
				}
				aerial_ul.append(year_li);
			}*/
			for (const year in aerial.map) {
				const year_li = $(`<li class="js-tree-parent-2" value="${year}" id="vido-id-aerial-title-${year}">`);
				const year_span = $('<span>').text(year);
				const year_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-aerial-image-layer" data-tree="vido-id-aerial-title-${year}"><i class="fas fa-search"></i></button>`);
				const year_ul = $('<ul>');
				year_li.append(year_span);
				year_li.append(year_btn);
				year_li.append(year_ul);
				for (const dpi in aerial.map[year]) {
					const fileList = aerial.map[year][dpi];
					const dpi_li = $(`<li class="js-tree-parent-3" value="${dpi}" id="vido-id-aerial-title-${year}-${dpi}">`);
					const dpi_span = $('<span>').text(dpi);
					const dpi_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-aerial-image-layer" data-tree="vido-id-aerial-title-${year}-${dpi}"><i class="fas fa-search"></i></button>`);
					const dpi_ul = $('<ul>');
					dpi_li.append(dpi_span);
					dpi_li.append(dpi_btn);
					dpi_li.append(dpi_ul);
					for (const elem of fileList) {
						const li = $(`<li class="js-search-result-aerial js-tree-child-3" value="${elem.index}" id="vido-id-aerial-${year}-${dpi}-${elem.index}" title="${elem.elem.fullFileCoursNm}">`);
						const span = $('<span>').text(elem.elem.vidoNm);
						const btn_aerial = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-aerial-image-layer" value="${elem.elem.vidoNm}" data-tree="vido-id-aerial-${year}-${dpi}-${elem.index}"><i class="fas fa-search"></i></button>`);
						if (!elem.elem.thumbnail) {
							btn_aerial.attr("disabled", true);
						}
						dpi_ul.append(li.append(span).append(btn_aerial));
					}
					year_ul.append(dpi_li);
				}
				aerial_ul.append(year_li);
			}

			// 기존 데이터 - 정사영상 노드 추가 (image / year / file)
			/*for (const year in ortho.map) {
				const fileList = ortho.map[year];
				// 년도 폴더 생성
				const year_li = $(`<li class="js-tree-parent-2" value="${year}" id="vido-id-ortho-title-${year}">`);
				const year_span = $('<span>').text(year);
				const year_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-ortho-image-layer" data-tree="vido-id-ortho-title-${year}"><i class="fas fa-search"></i></button>`);
				const year_ul = $('<ul>');
				year_li.append(year_span);
				year_li.append(year_btn);
				year_li.append(year_ul);
				for (const elem of fileList) {
					const li = $(`<li class="js-search-result-ortho js-tree-child-2" value="${elem.index}" id="vido-id-ortho-${elem.index}">`);
					const span = $('<span>').text(elem.elem.vidoNm);
					const btn_ortho = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-ortho-image-layer" value="${elem.elem.vidoNm}" data-tree="vido-id-ortho-${elem.index}"><i class="fas fa-search"></i></button>`);
					if (!elem.elem.thumbnail) {
						btn_ortho.attr("disabled", true);
					}
					year_ul.append(li.append(span).append(btn_ortho));
				}
				ortho_ul.append(year_li);
			}*/
			for (const year in ortho.map) {
				const year_li = $(`<li class="js-tree-parent-2" value="${year}" id="vido-id-ortho-title-${year}">`);
				const year_span = $('<span>').text(year);
				const year_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-ortho-image-layer" data-tree="vido-id-ortho-title-${year}"><i class="fas fa-search"></i></button>`);
				const year_ul = $('<ul>');
				year_li.append(year_span);
				year_li.append(year_btn);
				year_li.append(year_ul);
				for (const dpi in ortho.map[year]) {
					const fileList = ortho.map[year][dpi];
					const dpi_li = $(`<li class="js-tree-parent-3" value="${dpi}" id="vido-id-ortho-title-${year}-${dpi}">`);
					const dpi_span = $('<span>').text(dpi);
					const dpi_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-ortho-image-layer" data-tree="vido-id-ortho-title-${year}-${dpi}"><i class="fas fa-search"></i></button>`);
					const dpi_ul = $('<ul>');
					dpi_li.append(dpi_span);
					dpi_li.append(dpi_btn);
					dpi_li.append(dpi_ul);
					for (const elem of fileList) {
						const li = $(`<li class="js-search-result-ortho js-tree-child-3" value="${elem.index}" id="vido-id-ortho-${year}-${dpi}-${elem.index}" title="${elem.elem.fullFileCoursNm}">`);
						const span = $('<span>').text(elem.elem.vidoNm);
						const btn_ortho = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-ortho-image-layer" value="${elem.elem.vidoNm}" data-tree="vido-id-ortho-${year}-${dpi}-${elem.index}"><i class="fas fa-search"></i></button>`);
						if (!elem.elem.thumbnail) {
							btn_ortho.attr("disabled", true);
						}
						dpi_ul.append(li.append(span).append(btn_ortho));
					}
					year_ul.append(dpi_li);
				}
				ortho_ul.append(year_li);
			}

			//기존 데이터 - 위성영상(landsat) 노드 추가
			if (landsat) {
				const sat_li = $(`<li class="js-tree-parent-2" value="landsat" id="vido-id-satellite-title-landsat">`);
				const sat_span = $('<span>').text("Landsat");
				const sat_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-landsat-image-layer" data-tree="vido-id-satellite-title-landsat"><i class="fas fa-search"></i></button>`);
				const sat_ul = $('<ul>');
				sat_li.append(sat_span);
				sat_li.append(sat_btn);
				sat_li.append(sat_ul);
				satellite_ul.append(sat_li);

				for (const date in landsat.map) {
					const date_li = $(`<li class="js-tree-parent-3" value="${date}" id="vido-id-satellite-title-landsat-${date}">`);
					const date_span = $('<span>').text(date);
					const date_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-landsat-image-layer" data-tree="vido-id-satellite-title-landsat-${date}"><i class="fas fa-search"></i></button>`);
					const date_ul = $('<ul>');
					date_li.append(date_span);
					date_li.append(date_btn);
					date_li.append(date_ul);
					sat_ul.append(date_li);

					for (const folder in landsat.map[date]) {
						const fileList = landsat.map[date][folder];
						const folder_li = $(`<li class="js-tree-parent-4" value="${folder}" id="vido-id-satellite-title-landsat-${date}-${folder}">`);
						const folder_span = $('<span>').text(folder);
						const folder_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-landsat-image-layer" data-tree="vido-id-satellite-title-landsat-${date}-${folder}"><i class="fas fa-search"></i></button>`);
						const folder_ul = $('<ul>');
						folder_li.append(folder_span);
						folder_li.append(folder_btn);
						folder_li.append(folder_ul);
						date_ul.append(folder_li);

						for (const elem of fileList) {
							const li = $(`<li class="js-search-result-landsat js-tree-child-3" value="${elem.index}" id="vido-id-landsat-${date}-${folder}-${elem.index}"  title="${elem.elem.fullFileCoursNm}">`);
							const span = $('<span>').text(elem.elem.vidoNm);
							const btn_landsat = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-landsat-image-layer" value="${elem.elem.vidoNm}" data-tree="vido-id-landsat-${date}-${folder}-${elem.index}"><i class="fas fa-search"></i></button>`);
							if (!elem.elem.thumbnail) {
								btn_landsat.attr("disabled", true);
							}
							folder_ul.append(li.append(span).append(btn_landsat));
						}
					}
				}
			}

			//기존 데이터 - 위성영상(kompsat) 노드 추가
			if (kompsat) {
				const sat_li = $(`<li class="js-tree-parent-2" value="kompsat" id="vido-id-satellite-title-kompsat">`);
				const sat_span = $('<span>').text("Kompsat");
				const sat_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-kompsat-image-layer" data-tree="vido-id-satellite-title-kompsat"><i class="fas fa-search"></i></button>`);
				const sat_ul = $('<ul>');
				sat_li.append(sat_span);
				sat_li.append(sat_btn);
				sat_li.append(sat_ul);
				satellite_ul.append(sat_li);

				for (const date in kompsat.map) {

					const date_li = $(`<li class="js-tree-parent-3" value="${date}" id="vido-id-satellite-title-kompsat-${date}">`);
					const date_span = $('<span>').text(date);
					const date_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-kompsat-image-layer" data-tree="vido-id-satellite-title-kompsat-${date}"><i class="fas fa-search"></i></button>`);
					const date_ul = $('<ul>');
					date_li.append(date_span);
					date_li.append(date_btn);
					date_li.append(date_ul);
					sat_ul.append(date_li);

					for (const folder in kompsat.map[date]) {
						const fileList = kompsat.map[date][folder];
						const folder_li = $(`<li class="js-tree-parent-4" value="${folder}" id="vido-id-satellite-title-kompsat-${date}-${folder}">`);
						const folder_span = $('<span>').text(folder);
						const folder_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-kompsat-image-layer" data-tree="vido-id-satellite-title-kompsat-${date}-${folder}"><i class="fas fa-search"></i></button>`);
						const folder_ul = $('<ul>');
						folder_li.append(folder_span);
						folder_li.append(folder_btn);
						folder_li.append(folder_ul);
						date_ul.append(folder_li);

						for (const elem of fileList) {
							const li = $(`<li class="js-search-result-kompsat js-tree-child-3" value="${elem.index}" id="vido-id-kompsat-${date}-${folder}-${elem.index}"  title="${elem.elem.fullFileCoursNm}">`);
							const span = $('<span>').text(elem.elem.vidoNm);
							const btn_kompsat = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-kompsat-image-layer" value="${elem.elem.vidoNm}" data-tree="vido-id-kompsat-${date}-${folder}-${elem.index}"><i class="fas fa-search"></i></button>`);
							if (!elem.elem.thumbnail) {
								btn_kompsat.attr("disabled", true);
							}
							folder_ul.append(li.append(span).append(btn_kompsat));
						}
					}
				}
				//				for (const folder in kompsat.map) {
				//					const fileList = kompsat.map[folder];
				//					const folder_li = $(`<li class="js-tree-parent-3" value="${folder}" id="vido-id-satellite-title-kompsat-${folder}">`);
				//					const folder_span = $('<span>').text(folder);
				//					const folder_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-kompsat-image-layer" data-tree="vido-id-satellite-title-kompsat-${folder}"><i class="fas fa-search"></i></button>`);
				//					const folder_ul = $('<ul>');
				//					folder_li.append(folder_span);
				//					folder_li.append(folder_btn);
				//					folder_li.append(folder_ul);
				//					for (const elem of fileList) {
				//						const li = $(`<li class="js-search-result-kompsat js-tree-child-3" value="${elem.index}" id="vido-id-kompsat-${elem.index}"  title="${elem.elem.fullFileCoursNm}">`);
				//						const span = $('<span>').text(elem.elem.vidoNm);
				//						const btn_kompsat = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-kompsat-image-layer" value="${elem.elem.vidoNm}" data-tree="vido-id-kompsat-${elem.index}"><i class="fas fa-search"></i></button>`);
				//						if (!elem.elem.thumbnail) {
				//							btn_kompsat.attr("disabled", true);
				//						}
				//						folder_ul.append(li.append(span).append(btn_kompsat));
				//					}
				//					sat_ul.append(folder_li);
				//				}

			}

			//기존 데이터 - 위성영상(sentinel) 노드 추가
			if (sentinel) {
				const sat_li = $(`<li class="js-tree-parent-2" value="sentinel" id="vido-id-satellite-title-sentinel">`);
				const sat_span = $('<span>').text("Sentinel");
				const sat_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-sentinel-image-layer" data-tree="vido-id-satellite-title-sentinel"><i class="fas fa-search"></i></button>`);
				const sat_ul = $('<ul>');
				sat_li.append(sat_span);
				sat_li.append(sat_btn);
				sat_li.append(sat_ul);
				satellite_ul.append(sat_li);

				for (const date in sentinel.map) {

					const date_li = $(`<li class="js-tree-parent-3" value="${date}" id="vido-id-satellite-title-sentinel-${date}">`);
					const date_span = $('<span>').text(date);
					const date_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-sentinel-image-layer" data-tree="vido-id-satellite-title-sentinel-${date}"><i class="fas fa-search"></i></button>`);
					const date_ul = $('<ul>');
					date_li.append(date_span);
					date_li.append(date_btn);
					date_li.append(date_ul);
					sat_ul.append(date_li);

					for (const folder in sentinel.map[date]) {
						const fileList = sentinel.map[date][folder];

						const folder_li = $(`<li class="js-tree-parent-4" value="${folder}" id="vido-id-satellite-title-sentinel-${date}-${folder}">`);
						const folder_span = $('<span>').text(folder);
						const folder_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-sentinel-image-layer" data-tree="vido-id-satellite-title-sentinel-${date}-${folder}"><i class="fas fa-search"></i></button>`);
						const folder_ul = $('<ul>');
						folder_li.append(folder_span);
						folder_li.append(folder_btn);
						folder_li.append(folder_ul);
						date_ul.append(folder_li);

						for (const elem of fileList) {
							const li = $(`<li class="js-search-result-sentinel js-tree-child-3" value="${elem.index}" id="vido-id-sentinel-${date}-${folder}-${elem.index}"  title="${elem.elem.fullFileCoursNm}">`);
							const span = $('<span>').text(elem.elem.vidoNm);
							const btn_sentinel = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-sentinel-image-layer" value="${elem.elem.vidoNm}" data-tree="vido-id-sentinel-${date}-${folder}-${elem.index}"><i class="fas fa-search"></i></button>`);
							if (!elem.elem.thumbnail) {
								btn_sentinel.attr("disabled", true);
							}
							folder_ul.append(li.append(span).append(btn_sentinel));
						}
					}
				}
			}

			//기존 데이터 - 위성영상(cas) 노드 추가
			if (cas) {
				const sat_li = $(`<li class="js-tree-parent-2" value="cas" id="vido-id-satellite-title-cas">`);
				const sat_span = $('<span>').text("Cas");
				const sat_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-cas-image-layer" data-tree="vido-id-satellite-title-cas"><i class="fas fa-search"></i></button>`);
				const sat_ul = $('<ul>');
				sat_li.append(sat_span);
				sat_li.append(sat_btn);
				sat_li.append(sat_ul);
				satellite_ul.append(sat_li);

				for (const date in cas.map) {

					const date_li = $(`<li class="js-tree-parent-3" value="${date}" id="vido-id-satellite-title-cas-${date}">`);
					const date_span = $('<span>').text(date);
					const date_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-cas-image-layer" data-tree="vido-id-satellite-title-cas-${date}"><i class="fas fa-search"></i></button>`);
					const date_ul = $('<ul>');
					date_li.append(date_span);
					date_li.append(date_btn);
					date_li.append(date_ul);
					sat_ul.append(date_li);

					for (const folder in cas.map[date]) {
						const fileList = cas.map[date][folder];
						const folder_li = $(`<li class="js-tree-parent-4" value="${folder}" id="vido-id-satellite-title-cas-${date}-${folder}">`);
						const folder_span = $('<span>').text(folder);
						const folder_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-cas-image-layer" data-tree="vido-id-satellite-title-cas-${date}-${folder}"><i class="fas fa-search"></i></button>`);
						const folder_ul = $('<ul>');
						folder_li.append(folder_span);
						folder_li.append(folder_btn);
						folder_li.append(folder_ul);
						date_ul.append(folder_li);

						for (const elem of fileList) {
							const li = $(`<li class="js-search-result-cas js-tree-child-3" value="${elem.index}" id="vido-id-cas-${date}-${folder}-${elem.index}" title="${elem.elem.fullFileCoursNm}">`);
							const span = $('<span>').text(elem.elem.vidoNm)
							const btn_cas = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-cas-image-layer" value="${elem.elem.vidoNm}" data-tree="vido-id-cas-${date}-${folder}-${elem.index}"><i class="fas fa-search"></i></button>`);
							if (!elem.elem.thumbnail) {
								btn_cas.attr("disabled", true);
							}
							folder_ul.append(li.append(span).append(btn_cas));
						}
					}
				}
			}

			// 기존 데이터 - DEM 노드 추가
			/*for (const year in dem.map) {
				const fileList = dem.map[year];
				// 년도 폴더 생성
				const year_li = $(`<li class="js-tree-parent-2" value="${year}" id="vido-id-dem-title-${year}">`);
				const year_span = $('<span>').text(year);
				const year_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-dem-layer" data-tree="vido-id-dem-title-${year}"><i class="fas fa-search"></i></button>`);
				const year_ul = $('<ul>');
				year_li.append(year_span);
				year_li.append(year_btn);
				year_li.append(year_ul);
				for (const elem of fileList) {
					const li = $(`<li class="js-search-result-dem js-tree-child-2" value="${elem.index}" id="vido-id-dem-${elem.index}">`);
					const span = $('<span>').text(elem.elem.vidoNm);
					const btn_dem = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-dem-layer" value="${elem.elem.vidoNm}" data-tree="vido-id-dem-${elem.index}"><i class="fas fa-search"></i></button>`);
					if (!elem.elem.thumbnail) {
						btn_dem.attr("disabled", true);
					}	
					year_ul.append(li.append(span).append(btn_dem));
				}
				dem_ul.append(year_li);
			}*/
			for (const year in dem.map) {
				const year_li = $(`<li class="js-tree-parent-2" value="${year}" id="vido-id-dem-title-${year}">`);
				const year_span = $('<span>').text(year);
				const year_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-dem-layer" data-tree="vido-id-dem-title-${year}"><i class="fas fa-search"></i></button>`);
				const year_ul = $('<ul>');
				year_li.append(year_span);
				year_li.append(year_btn);
				year_li.append(year_ul);
				for (const dpi in dem.map[year]) {
					const fileList = dem.map[year][dpi];
					const dpi_li = $(`<li class="js-tree-parent-3" value="${dpi}" id="vido-id-dem-title-${year}-${dpi}">`);
					const dpi_span = $('<span>').text(dpi);
					const dpi_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-dem-layer" data-tree="vido-id-dem-title-${year}-${dpi}"><i class="fas fa-search"></i></button>`);
					const dpi_ul = $('<ul>');
					dpi_li.append(dpi_span);
					dpi_li.append(dpi_btn);
					dpi_li.append(dpi_ul);
					for (const elem of fileList) {
						const li = $(`<li class="js-search-result-dem js-tree-child-3" value="${elem.index}" id="vido-id-dem-${year}-${dpi}-${elem.index}" title="${elem.elem.fullFileCoursNm}">`);
						const span = $('<span>').text(elem.elem.vidoNm);
						const btn_dem = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-dem-layer" value="${elem.elem.vidoNm}" data-tree="vido-id-dem-${year}-${dpi}-${elem.index}"><i class="fas fa-search"></i></button>`);
						if (!elem.elem.thumbnail) {
							btn_dem.attr("disabled", true);
						}
						dpi_ul.append(li.append(span).append(btn_dem));
					}
					year_ul.append(dpi_li);
				}
				dem_ul.append(year_li);
			}

			// 기존 데이터 - 통계지도 노드 추가 (image / file)
			for (let i = 0; i < graphics.length; i++) {
				const li = $(`<li class="js-search-result-graphics js-tree-child-1" value="${graphics[i].vidoNm}" id="vido-id-graphics-${i}" title="${graphics[i].vidoNm}">`);
				const span = $('<span>').text(graphics[i].vidoNmKr);
				const btn_graphics = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-graphics-layer" value="${graphics[i].vidoNm}" data-tree="vido-id-graphics-${i}"><i class="fas fa-search"></i></button>`);
				if (!graphics[i].thumbnail) {
					btn_graphics.attr("disabled", true);
				}
				graphics_ul.append(li.append(span).append(btn_graphics));
			}

			// ## 긴급 영상 ##
			const emerData = $('<div class="in-panel-title">').text('긴급 영상');
			outerPanel.append(emerData);

			// 긴급 영상(resultList2) 영역 생성
			const resultList2 = $('<div id="resultList2">');
			outerPanel.append(resultList2);
			const resultList2_div = $('<div class="result-list-input">');
			resultList2.append(resultList2_div);

			// 긴급 영상 - 지진
			const resultList2_earthquake = $('<div id="resultList2_earthquake_tree">');
			const earthquake_tree = $('<ul>');
			const earthquakeTitle = $('<li class="js-control-earthquake js-tree-parent-1" id="vido-id-earthquake-title">');
			const earthquake_span = $('<span>').text(`지진(${earthquake.length}건)`);
			const earthquake_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-earthquake-image-layer" data-tree="vido-id-earthquake-title"><i class="fas fa-search"></i></button>`);
			const earthquake_ul = $('<ul>');
			resultList2_div.append(resultList2_earthquake);
			resultList2_earthquake.append(earthquake_tree);
			earthquake_tree.append(earthquakeTitle);
			earthquakeTitle.append(earthquake_span);
			earthquakeTitle.append(earthquake_btn);
			earthquakeTitle.append(earthquake_ul);

			// 긴급 영상 - 산불
			const resultList2_forestFire = $('<div id="resultList2_forestFire_tree">');
			const forestFire_tree = $('<ul>');
			const forestFireTitle = $('<li class="js-control-forestFire js-tree-parent-1" id="vido-id-forestfire-title">');
			const forestFire_span = $('<span>').text(`산불(${forestFire.length}건)`);
			const forestFire_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-forestfire-image-layer" data-tree="vido-id-forestfire-title"><i class="fas fa-search"></i></button>`);
			const forestFire_ul = $('<ul>');
			resultList2_div.append(resultList2_forestFire);
			resultList2_forestFire.append(forestFire_tree);
			forestFire_tree.append(forestFireTitle);
			forestFireTitle.append(forestFire_span);
			forestFireTitle.append(forestFire_btn);
			forestFireTitle.append(forestFire_ul);

			// 긴급 영상 - 수해 
			const resultList2_floodTitle = $('<div id="resultList2_flood_tree">');
			const flood_tree = $('<ul>');
			const floodTitle = $('<li class="js-control-floodTitle js-tree-parent-1" id="vido-id-flood-title">');
			const flood_span = $('<span>').text(`수해(${flood.length}건)`);
			const flood_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-flood-image-layer" data-tree="vido-id-flood-title"><i class="fas fa-search"></i></button>`);
			const flood_ul = $('<ul>');
			resultList2_div.append(resultList2_floodTitle);
			resultList2_floodTitle.append(flood_tree);
			flood_tree.append(floodTitle);
			floodTitle.append(flood_span);
			floodTitle.append(flood_btn);
			floodTitle.append(flood_ul);

			// 긴급 영상 - 산사태 
			const resultList2_landslip = $('<div id="resultList2_landslip_tree">');
			const landslip_tree = $('<ul>');
			const landslipTitle = $('<li class="js-control-landslip js-tree-parent-1" id="vido-id-landslide-title">');
			const landslip_span = $('<span>').text(`산사태(${landslip.length}건)`);
			const landslip_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-landslide-image-layer" data-tree="vido-id-landslide-title"><i class="fas fa-search"></i></button>`);
			const landslip_ul = $('<ul>');
			resultList2_div.append(resultList2_landslip);
			resultList2_landslip.append(landslip_tree);
			landslip_tree.append(landslipTitle);
			landslipTitle.append(landslip_span);
			landslipTitle.append(landslip_btn);
			landslipTitle.append(landslip_ul);

			// 긴급 영상 - 해양재난 
			const resultList2_maritime = $('<div id="resultList2_maritime_tree">');
			const maritime_tree = $('<ul>');
			const maritimeTitle = $('<li class="js-control-maritime js-tree-parent-1" id="vido-id-maritime-title">');
			const maritime_span = $('<span>').text(`해양재난(${maritime.length}건)`);
			const maritime_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-maritime-image-layer" data-tree="vido-id-maritime-title"><i class="fas fa-search"></i></button>`);
			const maritime_ul = $('<ul>');
			resultList2_div.append(resultList2_maritime);
			resultList2_maritime.append(maritime_tree);
			maritime_tree.append(maritimeTitle);
			maritimeTitle.append(maritime_span);
			maritimeTitle.append(maritime_btn);
			maritimeTitle.append(maritime_ul);

			// 긴급 영상 - 적조
			const resultList2_redTide = $('<div id="resultList2_redTide_tree">');
			const redTide_tree = $('<ul>');
			const redTideTitle = $('<li class="js-control-redTide js-tree-parent-1" id="vido-id-redTide-title">');
			const redTide_span = $('<span>').text(`적조(${redTide.length}건)`);
			const redTide_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-redTide-image-layer" data-tree="vido-id-redTide-title"><i class="fas fa-search"></i></button>`);
			const redTide_ul = $('<ul>');
			resultList2_div.append(resultList2_redTide);
			resultList2_redTide.append(redTide_tree);
			redTide_tree.append(redTideTitle);
			redTideTitle.append(redTide_span);
			redTideTitle.append(redTide_btn);
			redTideTitle.append(redTide_ul);

			// 긴급 영상 - 지진 노드 추가
			for (let i = 0; i < earthquake.length; i++) {
				const li = $(`<li class="js-search-result-earthquake js-tree-child-1" value="${i}" id="vido-id-earthquake-${i}" title="${earthquake[i].fullFileCoursNm}">`);
				const span = $('<span>').text(earthquake[i].vidoNm);
				const btn_earthquake = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-earthquake-image-layer" value="${earthquake[i].vidoNm}" data-tree="vido-id-earthquake-${i}"><i class="fas fa-search"></i></button>`);
				if (!earthquake[i].thumbnail) {
					btn_earthquake.attr("disabled", true);
				}
				earthquake_ul.append(li.append(span).append(btn_earthquake));
			}

			// 긴급 영상 - 산불 노드 추가
			for (let i = 0; i < forestFire.length; i++) {
				const li = $(`<li class="js-search-result-forestFire js-tree-child-1" value="${i}" id="vido-id-forestFire-${i}" title="${forestFire[i].fullFileCoursNm}">`);
				const span = $('<span>').text(forestFire[i].vidoNm);
				const btn_forestFire = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-forestfire-image-layer" value="${forestFire[i].vidoNm}" data-tree="vido-id-forestFire-${i}"><i class="fas fa-search"></i></button>`);
				if (!forestFire[i].thumbnail) {
					btn_forestFire.attr("disabled", true);
				}
				forestFire_ul.append(li.append(span).append(btn_forestFire));
			}

			// 긴급 영상 - 수해 노드 추가
			for (let i = 0; i < flood.length; i++) {
				const li = $(`<li class="js-search-result-flood js-tree-child-1" value="${i}" id="vido-id-flood-${i}" title="${flood[i].fullFileCoursNm}">`);
				const span = $('<span>').text(flood[i].vidoNm);
				const btn_flood = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-flood-image-layer" value="${flood[i].vidoNm}" data-tree="vido-id-flood-${i}"><i class="fas fa-search"></i></button>`);
				if (!flood[i].thumbnail) {
					btn_flood.attr("disabled", true);
				}
				flood_ul.append(li.append(span).append(btn_flood));
			}

			// 긴급 영상 - 산사태 노드 추가
			for (let i = 0; i < landslip.length; i++) {
				const li = $(`<li class="js-search-result-landslip js-tree-child-1" value="${i}" id="vido-id-landslide-${i}" title="${landslip[i].fullFileCoursNm}">`);
				const span = $('<span>').text(landslip[i].vidoNm);
				const btn_landslip = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-landslide-image-layer" value="${landslip[i].vidoNm}" data-tree="vido-id-landslide-${i}"><i class="fas fa-search"></i></button>`);
				if (!landslip[i].thumbnail) {
					btn_landslip.attr("disabled", true);
				}
				landslip_ul.append(li.append(span).append(btn_landslip));
			}

			// 긴급 영상 - 해양재난 노드 추가
			for (let i = 0; i < maritime.length; i++) {
				const li = $(`<li class="js-search-result-maritime js-tree-child-1" value="${i}" id="vido-id-maritime-${i}" title="${maritime[i].fullFileCoursNm}">`);
				const span = $('<span>').text(maritime[i].vidoNm);
				const btn_maritime = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-maritime-image-layer" value="${maritime[i].vidoNm}" data-tree="vido-id-maritime-${i}"><i class="fas fa-search"></i></button>`);
				if (!maritime[i].thumbnail) {
					btn_maritime.attr("disabled", true);
				}
				maritime_ul.append(li.append(span).append(btn_maritime));
			}

			// 긴급 영상 - 적조 노드 추가
			for (let i = 0; i < redTide.length; i++) {
				const li = $(`<li class="js-search-result-redTide js-tree-child-1" value="${i}" id="vido-id-redTide-${i}" title="${redTide[i].fullFileCoursNm}">`);
				const span = $('<span>').text(redTide[i].vidoNm);
				const btn_redTide = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-redTide-image-layer" value="${redTide[i].vidoNm}" data-tree="vido-id-redTide-${i}"><i class="fas fa-search"></i></button>`);
				if (!redTide[i].thumbnail) {
					btn_redTide.attr("disabled", true);
				}
				redTide_ul.append(li.append(span).append(btn_redTide));
			}



			// ## 분석결과 데이터 ##
			const classificationData = $('<div class="in-panel-title">').text('분석결과 데이터');
			outerPanel.append(classificationData);

			// 분석결과 데이터(resultList3) 영역 생성
			const resultList3 = $('<div id="resultList3">');
			outerPanel.append(resultList3);
			const resultList3_div = $('<div class="result-list-input">');
			resultList3.append(resultList3_div);

			// 분석결과 데이터 - 객체추출 벡터
			const resultList3_vectorAnalObj = $('<div id="resultList3_vectorAnalObj_tree">');
			const vectorAnalObj_tree = $('<ul>');
			const vectorAnalObjTitle = $('<li class="js-control-vectorAnalObj js-tree-parent-1" id="vido-id-vectorAnalObj-title">');
			const vectorAnalObj_span = $('<span>').text(`객체추출 벡터 데이터(${vectorAnalObj.length}건)`);
			const vectorAnalObj_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-shp-obj-layer" data-tree="vido-id-vectorAnalObj-title"><i class="fas fa-search"></i></button>`);
			const vectorAnalObj_ul = $('<ul>');
			resultList3_div.append(resultList3_vectorAnalObj);
			resultList3_vectorAnalObj.append(vectorAnalObj_tree);
			vectorAnalObj_tree.append(vectorAnalObjTitle);
			vectorAnalObjTitle.append(vectorAnalObj_span);
			vectorAnalObjTitle.append(vectorAnalObj_btn);
			vectorAnalObjTitle.append(vectorAnalObj_ul);

			// 분석결과 데이터 - 객체추출 영상
			const resultList3_rasterAnalObj = $('<div id="resultList3_rasterAnalObj_tree">');
			const rasterAnalObj_tree = $('<ul>');
			const rasterAnalObjTitle = $('<li class="js-control-rasterAnalObj js-tree-parent-1" id="vido-id-rasterAnalObj-title">');
			const rasterAnalObj_span = $('<span>').text(`객체추출 영상 데이터(${rasterAnalObj.length}건)`);
			const rasterAnalObj_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-raster-anal-obj-layer" data-tree="vido-id-rasterAnalObj-title"><i class="fas fa-search"></i></button>`);
			const rasterAnalObj_ul = $('<ul>');
			resultList3_div.append(resultList3_rasterAnalObj);
			resultList3_rasterAnalObj.append(rasterAnalObj_tree);
			rasterAnalObj_tree.append(rasterAnalObjTitle);
			rasterAnalObjTitle.append(rasterAnalObj_span);
			rasterAnalObjTitle.append(rasterAnalObj_btn);
			rasterAnalObjTitle.append(rasterAnalObj_ul);

			// 분석결과 데이터 - 변화탐지 벡터
			const resultList3_vectorAnalChg = $('<div id="resultList3_vectorAnalChg_tree">');
			const vectorAnalChg_tree = $('<ul>');
			const vectorAnalChgTitle = $('<li class="js-control-vectorAnalChg js-tree-parent-1" id="vido-id-vectorAnalChg-title">');
			const vectorAnalChg_span = $('<span>').text(`변화탐지 벡터 데이터(${vectorAnalChg.length}건)`)
			const vectorAnalChg_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-shp-chg-layer" data-tree="vido-id-vectorAnalChg-title"><i class="fas fa-search"></i></button>`);
			const vectorAnalChg_ul = $('<ul>');
			resultList3_div.append(resultList3_vectorAnalChg);
			resultList3_vectorAnalChg.append(vectorAnalChg_tree);
			vectorAnalChg_tree.append(vectorAnalChgTitle);
			vectorAnalChgTitle.append(vectorAnalChg_span);
			vectorAnalChgTitle.append(vectorAnalChg_btn);
			vectorAnalChgTitle.append(vectorAnalChg_ul);

			// 분석결과 데이터 - 변화탐지 영상
			const resultList3_raterAnalChg = $('<div id="resultList3_rasterAnalChg_tree">');
			const rasterAnalChg_tree = $('<ul>');
			const rasterAnalChgTitle = $('<li class="js-control-rasterAnalChg js-tree-parent-1" id="vido-id-rasterAnalChg-title">');
			const rasterAnalChg_span = $('<span>').text(`변화탐지 영상 데이터(${rasterAnalChg.length}건)`)
			const rasterAnalChg_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-raster-anal-chg-layer" data-tree="vido-id-rasterAnalChg-title"><i class="fas fa-search"></i></button>`);
			const rasterAnalChg_ul = $('<ul>');
			resultList3_div.append(resultList3_raterAnalChg);
			resultList3_raterAnalChg.append(rasterAnalChg_tree);
			rasterAnalChg_tree.append(rasterAnalChgTitle);
			rasterAnalChgTitle.append(rasterAnalChg_span);
			rasterAnalChgTitle.append(rasterAnalChg_btn);
			rasterAnalChgTitle.append(rasterAnalChg_ul);

			// 분석결과 데이터 - 객체추출 벡터 노드 추가
			for (let i = 0; i < vectorAnalObj.length; i++) {
				const li = $(`<li class="js-search-result-vectorAnalObj js-tree-child-1" value="${i}" id="vido-id-vectorAnalObj-${i}" title="${vectorAnalObj[i].fullFileCoursNm}">`);
				const span = $('<span>').text(vectorAnalObj[i].vidoNm);
				const btn_vectorAnalObj = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-shp-obj-layer" value="${vectorAnalObj[i].vidoNm}" data-tree="vido-id-vectorAnalObj-${i}"><i class="fas fa-search"></i></button>`);
				if (!vectorAnalObj[i].thumbnail) {
					btn_vectorAnalObj.attr("disabled", true);
				}
				vectorAnalObj_ul.append(li.append(span).append(btn_vectorAnalObj));
			}

			// 분석결과 데이터 - 객체추출 영상 노드 추가
			for (let i = 0; i < rasterAnalObj.length; i++) {
				const li = $(`<li class="js-search-result-rasterAnalObj js-tree-child-1" value="${i}" id="vido-id-rasterAnalObj-${i}" title="${rasterAnalObj[i].fullFileCoursNm}">`);
				const span = $('<span>').text(rasterAnalObj[i].vidoNm);
				const btn_rasterAnalObj = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-raster-anal-obj-layer" value="${rasterAnalObj[i].vidoNm}" data-tree="vido-id-rasterAnalObj-${i}"><i class="fas fa-search"></i></button>`);
				if (!rasterAnalObj[i].thumbnail) {
					btn_rasterAnalObj.attr("disabled", true);
				}
				rasterAnalObj_ul.append(li.append(span).append(btn_rasterAnalObj));
			}

			// 분석결과 데이터 - 변화탐지 벡터 노드 추가
			for (let i = 0; i < vectorAnalChg.length; i++) {
				const li = $(`<li class="js-search-result-vectorAnalChg js-tree-child-1" value="${i}" id="vido-id-vectorAnalChg-${i}" title="${vectorAnalChg[i].fullFileCoursNm}">`);
				const span = $('<span>').text(vectorAnalChg[i].vidoNm);
				const btn_vectorAnalChg = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-shp-chg-layer" value="${vectorAnalChg[i].vidoNm}" data-tree="vido-id-vectorAnalChg-${i}"><i class="fas fa-search"></i></button>`);
				if (!vectorAnalChg[i].thumbnail) {
					btn_vectorAnalChg.attr("disabled", true);
				}
				vectorAnalChg_ul.append(li.append(span).append(btn_vectorAnalChg));
			}

			// 분석결과 데이터 - 변화탐지 영상 노드 추가
			for (let i = 0; i < rasterAnalChg.length; i++) {
				const li = $(`<li class="js-search-result-rasterAnalChg js-tree-child-1" value="${i}" id="vido-id-rasterAnalChg-${i}" title="${rasterAnalChg[i].fullFileCoursNm}">`);
				const span = $('<span>').text(rasterAnalChg[i].vidoNm);
				const btn_rasterAnalChg = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-raster-anal-chg-layer" value="${rasterAnalChg[i].vidoNm}" data-tree="vido-id-rasterAnalChg-${i}"><i class="fas fa-search"></i></button>`);
				if (!rasterAnalChg[i].thumbnail) {
					btn_rasterAnalChg.attr("disabled", true);
				}
				rasterAnalChg_ul.append(li.append(span).append(btn_rasterAnalChg));
			}

			// jsTree 적용
			for (const data of ['resultList1_digital', 'resultList1_aerial', 'resultList1_ortho', 'resultList1_satellite', 'resultList1_dem', 'resultList1_graphics',
				'resultList2_earthquake', 'resultList2_forestFire', 'resultList2_flood', 'resultList2_landslip', 'resultList2_maritime', 'resultList2_redTide',
				'resultList3_vectorAnalObj', 'resultList3_rasterAnalObj', 'resultList3_vectorAnalChg', 'resultList3_rasterAnalChg']) {
				$(`#${data}_tree`).jstree({
					"checkbox": { "keep_selected_style": false, "three_state": true, "cascade": 'up', "tie_selection": false, "whole_node": false },
					"plugins": ["checkbox"],
					"core": { "themes": { "icons": false }, "dblclick_toggle": false }
				});
			}

		},

		showProgressBar() {

			const container = $('<div class="container" style="position:absolute; top:40%; left:40%; width:25%; height:10%; border:1px solid black; background-color:white; padding:0">');

			const content = $('<diV class="progress" style="position:absolute; top:40%; width: 80%;">');
			container.append(content);

			const progressbar = $('<div class="progress-bar" id="progress-bar" role="progressbar" aria-valuemin="0" aria-valuemax="100" style="width:0%">');
			progressbar.text('0%');
			content.append(progressbar);

			CMSC003.DOM.showModal('<h4>긴급공간정보 다운로드</h4>', content);

		},

		showModal(title, body, footer, size = 'modal-sm') {
			$('#cmsc003-modal').remove();

			const modal =
				'<div class="modal bs-example-modal-sm" id="cmsc003-modal" data-backdrop="false">' +
				'<div class="modal-dialog ' + size + '">' +
				'<div class="modal-content">' +
				'<div class="modal-header">' +
				'</div>' +
				'<div class="modal-body">' +
				'</div>' +
				'<div class="modal-footer">' +
				'</div>' +
				'</div>' +
				'</div>' +
				'</div>'

			$('body').append(modal);
			const header = $('<h4 class="modal-title">').text(title);
			const crossIcon = $('<span aria-hidden="true">×</span>');
			const closeBtn = $('<button type="button" onClick="$(\'#cmsc003-modal\').remove()" class="close" aria-label="Close" style="position: absolute; top: 11px; right: 11px;"></button>').append(crossIcon);
			$('#cmsc003-modal .modal-header').append(header).append(closeBtn);

			$('#cmsc003-modal .modal-body').append(body);
			$('#cmsc003-modal .modal-footer').append(footer);

			$('#cmsc003-modal').modal({ keyboard: false });
			$('#cmsc003-modal').modal('show');
		},
		
		showMosaicComfirm() {
			const title = '모자이크 영상 생성 확인';
			const body = '<span>모자이크 영상을 생성하시겠습니까?</span>';
			const createBtn = '<button type="button" class="btn btn-default" onClick="$(\'#cmsc003-modal\').remove(); createMosaicTree(true);">예</button>';
			const notBtn = '<button type="button" class="btn btn-default" onClick="$(\'#cmsc003-modal\').remove(); createMosaicTree(false);">아니오</button>';
			const cancelBtn = '<button type="button" class="btn btn-default" onClick="$(\'#cmsc003-modal\').remove();">취소</button>';
			const footer = '<div>' + createBtn + notBtn + cancelBtn + '</div>';

			CMSC003.DOM.showModal(title, body, footer);
		},
		
		showResultPathComfirm(paths) {
			const title = '모자이크 결과 파일 경로 확인';
			const msg1 = $('<span>모자이크 결과 파일의 경로는 아래와 같습니다.</span>');
			const list = $('<ul class="ui-path-list">');
			for(let i = 0; i < paths.length; i++) {
				const item = paths[i];
				const div = $('<li>').text(item);
				list.append(div);
			}
			const body = $('<div>').append(msg1).append(list);
			const createBtn = '<button type="button" class="btn btn-default" onClick="$(\'#cmsc003-modal\').remove(); createDataTree(\'build\')">확인</button>';
			const cancelBtn = '<button type="button" class="btn btn-default" onClick="$(\'#cmsc003-modal\').remove()">취소</button>';
			const footer = '<div>' + createBtn + cancelBtn + '</div>';

			CMSC003.DOM.showModal(title, body, footer, 'dummy');
		},

		showCreateDataMsg() {
			const title = '긴급공간정보 생성';
			const body = '<span>재난지역 영상을 저장하시겠습니까?</span>';
			const createBtn = '<button type="button" class="btn btn-default" onClick="$(\'#cmsc003-modal\').remove(); createDataTree(\'create\')">저장</button>';
			const cancelBtn = '<button type="button" class="btn btn-default" onClick="$(\'#cmsc003-modal\').remove()">취소</button>';
			const footer = '<div>' + createBtn + cancelBtn + '</div>';

			CMSC003.DOM.showModal(title, body, footer);
		},

		showBuildDataMsg_old() {
			const title = '긴급공간정보 구축';
			const body = '<span>긴급공간 정보를 구축하시겠습니까?</span>';
			const buildBtn = '<button type="button" class="btn btn-default" onClick="$(\'#cmsc003-modal\').remove(); createDataTree(\'build\')">구축</button>';
			const cancelBtn = '<button type="button" class="btn btn-default" onClick="$(\'#cmsc003-modal\').remove()">취소</button>';
			const footer = '<div>' + buildBtn + cancelBtn + '</div>';

			CMSC003.DOM.showModal(title, body, footer);
		},

		showBuildDataMsg() {
			const title = '긴급공간정보 생성';
			const msg1 = $('<div>긴급공간정보 데이터셋 이름을 입력해주세요.</div>').css({
				'margin-bottom': '10px'
			});
			const input = $('<input type="text" id="disasterNmUser" class="form-control js-input-dataset-name">').css({
				'margin-bottom': '10px'
			});
			const msg2 = $('<div>긴급공간 정보를 생성하시겠습니까?</div>');
			const body = $('<div style="text-align: center;">').append(msg1).append(input).append(msg2);
			// const buildBtn = '<button type="button" class="btn btn-default" onClick="createDataTree(\'build\');">생성</button>';
			// const buildBtn = '<button type="button" class="btn btn-default" onClick="CMSC003.DOM.showMosaicComfirm();">생성</button>';
			const buildBtn = '<button type="button" class="btn btn-default" onClick="determineMosaic();">생성</button>';
			const cancelBtn = '<button type="button" class="btn btn-default" onClick="$(\'#cmsc003-modal\').remove()">취소</button>';
			const footer = '<div>' + buildBtn + cancelBtn + '</div>';

			CMSC003.DOM.showModal(title, body, footer);
		},

		showErrorMsg(error) {
			const title = '알림';
			const body = '<span>' + error + '</span>';
			const cancelBtn = '<button type="button" class="btn btn-default" onClick="$(\'#cmsc003-modal\').remove()">확인</button>';
			const footer = '<div>' + cancelBtn + '</div>';

			CMSC003.DOM.showModal(title, body, footer);
		},

		showSpinner(isShow) {
			if (isShow) {
				$('.load').show();
			} else {
				$('.load').hide();
			}
		},

		/**
		 * @param {string} trClass - 테이블에 표시된 재난 ID행의 클래스명
		 */
		showDisasterIDSearchPopup(trClass) {
			const request = function(did) {
				//				console.log(did);
				did = did.replace(/\s\s+/g, ' ');
				//				if (did === "") {
				//					alert('검색 키워드를 입력해주세요.');
				//					return;
				//				}
				const splitId = did.split(' ');
				const names = ['colctBeginDe', 'msfrtnAreaSrchwrd'];
				const formData = new FormData();
				for (let i = 0; i < 2; i++) {
					const word = splitId[i];
					formData.append(names[i], word ? word : '');
				}

				const url = "cmsc003searchDisasterId.do";

				const xhr = new XMLHttpRequest();
				xhr.open("POST", url);

				xhr.onreadystatechange = function() {
					if (xhr.readyState === 4) {
						const returnJson = JSON.parse(xhr.response);

						$('#cmsc003-modal table.js-table-disaster-result tbody').empty();

						if (returnJson.length === 0) {
							const td = $('<td colspan="8" style="text-align: center; padding: 10px;">').text('검색된 결과가 없습니다.');
							const tr = $('<tr>').append(td);
							$('#cmsc003-modal table.js-table-disaster-result tbody').append(tr);
						} else {
							for (let i = 0; i < returnJson.length; i++) {
								const row = returnJson[i];
								const td1 = $('<td>').attr({
									'data': row.msfrtnTyCd,
									'x': row.spceCrdntX,
									'y': row.spceCrdntY
								}).text(row.msfrtnInfoColctRequstId);
								if (row.ltopCrdntX && row.ltopCrdntY && row.rbtmCrdntX && row.rbtmCrdntY) {
									td1.attr('bbox', [row.ltopCrdntX, row.rbtmCrdntY, row.rbtmCrdntX, row.ltopCrdntY]);
									td1.attr('mapPrjctnCn', row.mapPrjctnCn);
								}
								const td2 = $('<td>').text(row.colctBeginDe);
								const td3 = $('<td>').text(row.msfrtnAreaSrchwrd);
								const td4 = $('<td>').text(row.msfrtnTySrchwrd);
								const td5 = $('<td>').text(row.ctprvnNm);
								const td6 = $('<td>').text(row.sggNm);
								const td7 = $('<td>').text(row.emdNm);
								const td8 = $('<td>').text(row.lnbrAddr);
								const tr = $('<tr>').append(td1).append(td2).append(td3).append(td4).append(td5).append(td6).append(td7).append(td8);
								if (trClass) {
									tr.addClass(trClass);
								}
								$('#cmsc003-modal table.js-table-disaster-result tbody').append(tr);
							}
						}
					}
				};

				xhr.send(formData);

			}
			const title = '재난 ID 검색';

			const keywordLabel = $('<label class="control-label form-label">').text('검색 키워드');
			const col1 = $('<div class="col-sm-3">').append(keywordLabel);

			const keywordInput = $('<input type="text" class="form-control js-disaster-input-popup" placeholder="예시) 2021 안동">');
			const col2 = $('<div class="col-sm-6">').append(keywordInput);

			const searchBtn = '<button type="button" class="btn btn-default js-disaster-search">검색</button>';

			const col3 = $('<div class="col-sm-3">').append(searchBtn);

			const div1 = $('<div style="height: 40px;">').append(col1).append(col2).append(col3);

			const table = $('<table class="js-table-disaster-result" style="width: 100%;">');

			const headtd1 = $('<td>').text('재난 ID');
			const headtd2 = $('<td>').text('수집일');
			const headtd3 = $('<td>').text('재난 지역');
			const headtd4 = $('<td>').text('재난 분류');
			const headtd5 = $('<td>').text('시/도');
			const headtd6 = $('<td>').text('구/군');
			const headtd7 = $('<td>').text('읍/면/동');
			const headtd8 = $('<td>').text('세부주소');
			const headtr = $('<tr>').append(headtd1).append(headtd2).append(headtd3).append(headtd4).append(headtd5).append(headtd6).append(headtd7).append(headtd8);
			const thead = $('<thead>').append(headtr);
			table.append(thead);

			const tbody = $('<tbody>');
			table.append(tbody);

			const panel = $('<div class="panel panel-default" style="height: 200px; max-height: 250px;">').append(table);
			const div2 = $('<div>').append(panel);

			const body = $('<div style="min-height: 30px;">').append(div1).append(div2);

			const cancelBtn = '<button type="button" class="btn btn-default" onClick="$(\'#cmsc003-modal\').remove()">닫기</button>';
			const footer = '<div>' + cancelBtn + '</div>';

			CMSC003.DOM.showModal(title, body, footer, '');

			$('#cmsc003-modal').on('click', 'button.js-disaster-search', function() {
				const did = $('#cmsc003-modal input.js-disaster-input-popup').val();
				request(did);
			});
		},

		getSHPBinary(lid, isRequest) {
			console.log(lid);
			const searchDataMap = isRequest ? CMSC003.Storage.get('requestDataMap') : CMSC003.Storage.get('searchDataMap');
			const info = searchDataMap[lid];
			if (info) {
				const extent5179 = isRequest ? CMSC003.Storage.get('requestROIExtent') : CMSC003.GIS.getROIExtent();
				if (!extent5179) {
					alert('ROI 범위를 지정해주세요.');
					return;
				}

				CMSC003.DOM.showSpinner(true);
				const url = "cmsc003shpThumbnail.do";

				var xhr = new XMLHttpRequest();
				xhr.open("POST", url);

				xhr.setRequestHeader("Accept", "application/json");
				xhr.setRequestHeader("Content-Type", "application/json");

				xhr.onreadystatechange = function() {
					if (xhr.readyState === 4) {
						CMSC003.DOM.showSpinner(false);
						// 				console.log(xhr.status);
						//						console.log(xhr.response);
						CMSC003.DOM.showSpinner(true);
						const shp = new CMSC003.Converter({
							url: CMSC003.Util.base64ToBlob(xhr.response, 'application/zip'),
							//							encoding: 'EUC-KR',
							encoding: 'ISO-8859-1'
						});
						shp.loadshp((geojson) => {
							//							console.log(geojson);
							CMSC003.GIS.updateSHPLayer(geojson, lid, isRequest);
							CMSC003.DOM.showSpinner(false);
						});

					}
				};


				const extent4326 = CMSC003.GIS.convert5179To4326Extent(extent5179);
				const extent5186 = CMSC003.GIS.convert5179To5186Extent(extent5179);
				const extent32652 = CMSC003.GIS.convert5179To32652Extent(extent5179);
				var selectExtent = {
					"roi": {
						"roi4326": {
							"lrx": extent4326[2],
							"lry": extent4326[1],
							"ulx": extent4326[0],
							"uly": extent4326[3]
						},
						"roi5186": {
							"lrx": extent5186[2],
							"lry": extent5186[1],
							"ulx": extent5186[0],
							"uly": extent5186[3]
						},
						"roi5179": {
							"lrx": extent5179[2],
							"lry": extent5179[1],
							"ulx": extent5179[0],
							"uly": extent5179[3]
						},
						"roi32652": {
							"lrx": extent32652[2],
							"lry": extent32652[1],
							"ulx": extent32652[0],
							"uly": extent32652[3]
						}
					}
				};
				info['createInfo'] = selectExtent;
				console.log(info, 'shp preview');
				xhr.send(JSON.stringify(info));
			}
		},

		getSHPBinaryGeoServer(lid, isRequest) {
			console.log(lid);
			const searchDataMap = CMSC003.Storage.get('requestDataMap');
			const info = searchDataMap[lid];
			if (info) {
				const extent5179 = isRequest ? CMSC003.Storage.get('requestROIExtent') : CMSC003.GIS.getROIExtent();
				if (!extent5179) {
					alert('ROI 범위를 지정해주세요.');
					return;
				}

				CMSC003.DOM.showSpinner(true);
				const url = "cmsc003shpThumbnail2.do";

				var xhr = new XMLHttpRequest();
				xhr.open("POST", url);

				xhr.setRequestHeader("Accept", "application/json");
				xhr.setRequestHeader("Content-Type", "application/json");

				xhr.onreadystatechange = function() {
					if (xhr.readyState === 4) {
						CMSC003.DOM.showSpinner(false);
						// 				console.log(xhr.status);
						//						console.log(xhr.response);
						CMSC003.DOM.showSpinner(true);
						const shp = new CMSC003.Converter({
							url: CMSC003.Util.base64ToBlob(xhr.response, 'application/zip'),
							//							encoding: 'EUC-KR',
							encoding: 'ISO-8859-1'
						});
						shp.loadshp((geojson) => {
							//							console.log(geojson);
							CMSC003.GIS.updateSHPLayerDigitalMap(geojson, lid, isRequest);
							CMSC003.DOM.showSpinner(false);
						});

					}
				};


				const extent4326 = CMSC003.GIS.convert5179To4326Extent(extent5179);
				const extent5186 = CMSC003.GIS.convert5179To5186Extent(extent5179);
				const extent32652 = CMSC003.GIS.convert5179To32652Extent(extent5179);
				var selectExtent = {
					"roi": {
						"roi4326": {
							"lrx": extent4326[2],
							"lry": extent4326[1],
							"ulx": extent4326[0],
							"uly": extent4326[3]
						},
						"roi5186": {
							"lrx": extent5186[2],
							"lry": extent5186[1],
							"ulx": extent5186[0],
							"uly": extent5186[3]
						},
						"roi5179": {
							"lrx": extent5179[2],
							"lry": extent5179[1],
							"ulx": extent5179[0],
							"uly": extent5179[3]
						},
						"roi32652": {
							"lrx": extent32652[2],
							"lry": extent32652[1],
							"ulx": extent32652[0],
							"uly": extent32652[3]
						}
					}
				};
				info['createInfo'] = selectExtent;
				console.log(info, 'shp preview');
				xhr.send(JSON.stringify(info));
			}
		},

		getSHPBinaryGeoServerAfterRequest(lid, isRequest) {
			console.log(lid);
			const searchDataMap = CMSC003.Storage.get('requestDataMap');
			const info = searchDataMap[lid];
			if (info) {
				const extent5179 = isRequest ? CMSC003.Storage.get('requestROIExtent') : CMSC003.GIS.getROIExtent();
				if (!extent5179) {
					alert('ROI 범위를 지정해주세요.');
					return;
				}

				CMSC003.DOM.showSpinner(true);
				const url = "cmsc003shpThumbnail2.do";

				var xhr = new XMLHttpRequest();
				xhr.open("POST", url);

				xhr.setRequestHeader("Accept", "application/json");
				xhr.setRequestHeader("Content-Type", "application/json");

				xhr.onreadystatechange = function() {
					if (xhr.readyState === 4) {
						CMSC003.DOM.showSpinner(false);
						// 				console.log(xhr.status);
						//						console.log(xhr.response);
						CMSC003.DOM.showSpinner(true);
						const shp = new CMSC003.Converter({
							url: CMSC003.Util.base64ToBlob(xhr.response, 'application/zip'),
							//							encoding: 'EUC-KR',
							encoding: 'ISO-8859-1'
						});
						shp.loadshp((geojson) => {
							//							console.log(geojson);
							CMSC003.GIS.updateSHPLayerDigitalMapAfterRequest(geojson, lid, isRequest);
							CMSC003.DOM.showSpinner(false);
						});

					}
				};


				const extent4326 = CMSC003.GIS.convert5179To4326Extent(extent5179);
				const extent5186 = CMSC003.GIS.convert5179To5186Extent(extent5179);
				const extent32652 = CMSC003.GIS.convert5179To32652Extent(extent5179);
				var selectExtent = {
					"roi": {
						"roi4326": {
							"lrx": extent4326[2],
							"lry": extent4326[1],
							"ulx": extent4326[0],
							"uly": extent4326[3]
						},
						"roi5186": {
							"lrx": extent5186[2],
							"lry": extent5186[1],
							"ulx": extent5186[0],
							"uly": extent5186[3]
						},
						"roi5179": {
							"lrx": extent5179[2],
							"lry": extent5179[1],
							"ulx": extent5179[0],
							"uly": extent5179[3]
						},
						"roi32652": {
							"lrx": extent32652[2],
							"lry": extent32652[1],
							"ulx": extent32652[0],
							"uly": extent32652[3]
						}
					}
				};
				info['createInfo'] = selectExtent;
				console.log(info, 'shp preview');
				xhr.send(JSON.stringify(info));
			}
		},

		/**
		 * 요청결과 목록을 업데이트함
		 */
		showRequestResult(json) {
			if (typeof json === 'string') {
				json = JSON.parse(json);
			}
			// 원래 요청 결과
			// $(".js-request-result-area div.panel-default").empty();

			// 새로운 요청 결과
			$(".js-request-result-area div.panel-default").empty();

			if (!json) {
				return;
			}


			// 검색결과 아무것도 없으면 종료
			//if (json.length == 0) return $(".panel-default").append('<span>검색 결과가 없습니다.</span>');
			const digital = [];
			const dem = [];
			const demo = [];
			const landgraphics = [];
			const buldgraphics = [];
			const landsat = [];
			const kompsat = [];
			const sentinel = [];
			const cas = [];
			const aerial = [];
			const ortho = [];

			const flood = [];
			const forestFire = [];
			const landslip = [];
			const earthquake = [];
			const maritime = [];
			const rain = [];
			const typhoon = [];
			const redTide = [];

			const rasterAnalObj = [];
			const vectorAnalObj = [];

			const rasterAnalChg = [];
			const vectorAnalChg = [];

			const dataMap = {};
			console.log(json, 'json')

			const existing = json.existing;

			const digitalList = existing.digitalMap;
			if (digitalList) {
				for (let i = 0; i < digitalList.length; i++) {
					const elem = digitalList[i];
					console.log(elem);
					dataMap[elem.vidoNm] = elem;
					digital.push(elem);
				}
			}

			const demList = existing.dem;
			if (demList) {
				for (let i = 0; i < demList.length; i++) {
					const elem = demList[i];
					console.log(elem);
					dataMap[`dem-${dem.length}`] = elem;
					dem.push(elem);
				}
			}

			const demoList = existing.demographics;
			if (demoList) {
				for (let i = 0; i < demoList.length; i++) {
					const elem = demoList[i];
					console.log(elem);
					dataMap[elem.vidoNm] = elem;
					demo.push(elem);
				}
			}

			const landgList = existing.landgraphics;
			if (landgList) {
				for (let i = 0; i < landgList.length; i++) {
					const elem = landgList[i];
					console.log(elem);
					dataMap[elem.vidoNm] = elem;
					landgraphics.push(elem);
				}
			}

			const buldList = existing.buldgraphics;
			if (buldList) {
				for (let i = 0; i < buldList.length; i++) {
					const elem = buldList[i];
					console.log(elem);
					dataMap[elem.vidoNm] = elem;
					buldgraphics.push(elem);
				}
			}

			const landsatList = existing.landsat;
			if (landsatList) {
				for (let i = 0; i < landsatList.length; i++) {
					const elem = landsatList[i];
					console.log(elem);
					dataMap[`landsat-${landsat.length}`] = elem;
					landsat.push(elem);
				}
			}

			const kompsatList = existing.kompsat;
			if (kompsatList) {
				for (let i = 0; i < kompsatList.length; i++) {
					const elem = kompsatList[i];
					console.log(elem);
					dataMap[`kompsat-${kompsat.length}`] = elem;
					kompsat.push(elem);
				}
			}

			const sentinelList = existing.sentinel;
			if (sentinelList) {
				for (let i = 0; i < sentinelList.length; i++) {
					const elem = sentinelList[i];
					console.log(elem);
					dataMap[`sentinel-${sentinel.length}`] = elem;
					sentinel.push(elem);
				}
			}
			const casList = existing.cas;
			if (casList) {
				for (let i = 0; i < casList.length; i++) {
					const elem = casList[i];
					console.log(elem);
					dataMap[`cas-${cas.length}`] = elem;
					cas.push(elem);
				}
			}

			const aerialList = existing.airOrientalMap;
			if (aerialList) {
				for (let i = 0; i < aerialList.length; i++) {
					const elem = aerialList[i];
					console.log(elem);
					dataMap[`aerial-${aerial.length}`] = elem;
					aerial.push(elem);
				}
			}

			const orthoList = existing.ortOrientalMap;
			if (orthoList) {
				for (let i = 0; i < orthoList.length; i++) {
					const elem = orthoList[i];
					console.log(elem);
					dataMap[`ortho-${ortho.length}`] = elem;
					ortho.push(elem);
				}
			}

			const disaster = json.disaster;

			const floodList = disaster.Flood;
			if (floodList) {
				for (let i = 0; i < floodList.length; i++) {
					const elem = floodList[i];
					console.log(elem);
					dataMap[`flood-${flood.length}`] = elem;
					flood.push(elem);
				}
			}

			const forestFireList = disaster.ForestFire;
			if (forestFireList) {
				for (let i = 0; i < forestFireList.length; i++) {
					const elem = forestFireList[i];
					console.log(elem);
					dataMap[`forestFire-${forestFire.length}`] = elem;
					forestFire.push(elem);
				}
			}

			const landslipList = disaster.Landslide;
			if (landslipList) {
				for (let i = 0; i < landslipList.length; i++) {
					const elem = landslipList[i];
					console.log(elem);
					dataMap[`landslide-${landslip.length}`] = elem;
					landslip.push(elem);
				}
			}

			const earthList = disaster.Earthquake;
			if (earthList) {
				for (let i = 0; i < earthList.length; i++) {
					const elem = earthList[i];
					console.log(elem);
					dataMap[`earthquake-${earthquake.length}`] = elem;
					earthquake.push(elem);
				}
			}

			const rainList = disaster.Rain;
			if (rainList) {
				for (let i = 0; i < rainList.length; i++) {
					const elem = rainList[i];
					console.log(elem);
					dataMap[`rain-${rain.length}`] = elem;
					rain.push(elem);
				}
			}

			const typhoonList = disaster.Typhoon;
			if (typhoonList) {
				for (let i = 0; i < typhoonList.length; i++) {
					const elem = typhoonList[i];
					console.log(elem);
					dataMap[`typhoon-${typhoon.length}`] = elem;
					typhoon.push(elem);
				}
			}

			const maritimeList = disaster.MaritimeDisaster;
			if (maritimeList) {
				for (let i = 0; i < maritimeList.length; i++) {
					const elem = maritimeList[i];
					console.log(elem);
					dataMap[`maritime-${maritime.length}`] = elem;
					maritime.push(elem);
				}
			}
			
			const redTideList = disaster.RedTide;
			if (redTideList) {
				for (let i = 0; i < redTideList.length; i++) {
					const elem = redTideList[i];
					console.log(elem);
					dataMap[`redTide-${redTide.length}`] = elem;
					redTide.push(elem);
				}
			}

			const analysis = json.analysis;

			const rasterAnalObjList = analysis.objectExt.raster;
			if (rasterAnalObjList) {
				for (let i = 0; i < rasterAnalObjList.length; i++) {
					const elem = rasterAnalObjList[i];
					console.log(elem);
					dataMap[`rasteranalobj-${rasterAnalObj.length}`] = elem;
					rasterAnalObj.push(elem);
				}
			}

			const vectorAnalObjList = analysis.objectExt.vector;
			if (vectorAnalObjList) {
				for (let i = 0; i < vectorAnalObjList.length; i++) {
					const elem = vectorAnalObjList[i];
					console.log(elem);
					dataMap[`vectoranalobj-${vectorAnalObj.length}`] = elem;
					vectorAnalObj.push(elem);
				}
			}

			const rasterAnalChgList = analysis.changeDet.raster;
			if (rasterAnalChgList) {
				for (let i = 0; i < rasterAnalChgList.length; i++) {
					const elem = rasterAnalChgList[i];
					console.log(elem);
					dataMap[`rasteranalchg-${rasterAnalChg.length}`] = elem;
					rasterAnalChg.push(elem);
				}
			}

			const vectorAnalChgList = analysis.changeDet.vector;
			if (vectorAnalChgList) {
				for (let i = 0; i < vectorAnalChgList.length; i++) {
					const elem = vectorAnalChgList[i];
					console.log(elem);
					dataMap[`vectoranalchg-${vectorAnalChg.length}`] = elem;
					vectorAnalChg.push(elem);
				}
			}

			CMSC003.Storage.set('requestDataMap', dataMap);


			const outerPanel = $(".js-request-result-area div.panel-default");

			// 제목
			const panelTitle = $('<div class="in-panel-title">').text('재난정보 목록');
			outerPanel.append(panelTitle);

			// 검색결과 요약
			const resultList = $('<ul class="result-list">');
			outerPanel.append(resultList);

			const resultListLi1 = $('<li>').text(`재난(${flood.length + forestFire.length + landslip.length + redTide.length}건)`);
			resultList.append(resultListLi1);


			outerPanel.append(resultList);

			// 기존 데이터
			const existData = $('<div class="in-panel-title">').text(`기존 데이터`);

			// 검색결과 세부영역
			const resultList1 = $('<div id="resultList4">').append(existData)
			outerPanel.append(resultList1);

			// 기존 데이터 영역 dl
			const dl = $('<dl class="result-list-input">');
			resultList1.append(dl);

			const vectorTitle = $('<dt>').text(`수치지도(${digital.length}건)`);
			dl.append(vectorTitle);

			const aerialTitle = $('<dt>').text(`항공영상(${aerial.length}건)`);
			dl.append(aerialTitle);

			const orthoTitle = $('<dt>').text(`정사영상(${ortho.length}건)`);
			dl.append(orthoTitle);

			const satelliteTitle = $('<dt>').text(`위성영상(${landsat.length + kompsat.length + sentinel.length + cas.length}건)`);
			dl.append(satelliteTitle);

			const demTitle = $('<dt>').text(`DEM(${dem.length}건)`);
			dl.append(demTitle);

			const demoTitle = $('<dt>').text(`인구통계(${demo.length}건)`);
			dl.append(demoTitle);

			const landgTitle = $('<dt title="지가통계">');
			dl.append(landgTitle);

			const buldTitle = $('<dt title="건물통계">');
			dl.append(buldTitle);

			// 수치지도 데이터 반복문
			for (let i = 0; i < digital.length; i++) {
				const elem = digital[i];
				console.log(elem);
				const dd_vector = $('<dd title="' + elem.vidoNmKr + '">');
				//				const check_vector = $(`<input type="checkbox" class="js-search-result-digital" value="${elem.vidoNm}" id="vido-id-digital-${i}">`);
				//				const label_vector = $(`<label for="vido-id-digital-${i}">`).text(elem.vidoNmKr);
				const span = $('<span>').text(elem.vidoNmKr);
				const btn_vector = $(`<button id="vido-id-request-digital-${i}" type="button" class="btn btn-xs f-right ui-btn-preview-digital-layer" value="${elem.vidoNm}"><i class="fas fa-search"></i>미리보기</button>`);
				//				dd_vector.append(check_vector).append(label_vector).append(btn_vector);
				dd_vector.append(span).append(btn_vector);
				vectorTitle.append(dd_vector);
			}

			// 인구통계 데이터 반복문
			for (let i = 0; i < demo.length; i++) {
				const elem = demo[i];
				console.log(elem);
				const dd_vector = $('<dd title="' + elem.vidoNmKr + '">');
				//				const check_vector = $(`<input type="checkbox" class="js-search-result-demo" value="${elem.vidoNm}" id="vido-id-demo-${i}">`);
				//				const label_vector = $(`<label for="vido-id-demo-${i}">`).text(elem.vidoNmKr);
				const span = $('<span>').text(elem.vidoNmKr);
				const btn_vector = $(`<button id="vido-id-request-demo-${i}" type="button" class="btn btn-xs f-right ui-btn-preview-demo-layer" value="${elem.vidoNm}"><i class="fas fa-search"></i>미리보기</button>`);
				//				dd_vector.append(check_vector).append(label_vector).append(btn_vector);
				dd_vector.append(span).append(btn_vector);
				demoTitle.append(dd_vector);
			}

			// 지가통계 데이터 반복문
			for (let i = 0; i < landgraphics.length; i++) {
				const elem = landgraphics[i];
				console.log(elem);
				const dd_vector = $('<dd title="' + elem.vidoNmKr + '">');
				const span = $('<span>').text(elem.vidoNmKr);
				const btn_vector = $(`<button id="vido-id-request-landg-${i}" type="button" class="btn btn-xs f-right ui-btn-preview-landg-layer" value="${elem.vidoNm}"><i class="fas fa-search"></i>미리보기</button>`);
				dd_vector.append(span).append(btn_vector);
				landgTitle.append(dd_vector);
			}

			// 건물통계 데이터 반복문
			for (let i = 0; i < buldgraphics.length; i++) {
				const elem = buldgraphics[i];
				console.log(elem);
				const dd_vector = $('<dd title="' + elem.vidoNmKr + '">');
				const span = $('<span>').text(elem.vidoNmKr);
				const btn_vector = $(`<button id="vido-id-request-buld-${i}" type="button" class="btn btn-xs f-right ui-btn-preview-buld-layer" value="${elem.vidoNm}"><i class="fas fa-search"></i>미리보기</button>`);
				dd_vector.append(span).append(btn_vector);
				buldTitle.append(dd_vector);
			}

			// dem 데이터 반복문
			for (let i = 0; i < dem.length; i++) {
				const elem = dem[i];
				console.log(elem);
				const dd_vector = $('<dd title="' + elem.fullFileCoursNm + '">');
				//				const check_vector = $(`<input type="checkbox" class="js-search-result-dem" value="${elem.vidoNm}" id="vido-id-dem-${i}">`);
				//				const label_vector = $(`<label for="vido-id-dem-${i}">`).text(elem.vidoNm);
				const span = $('<span>').text(elem.vidoNm);
				const btn_vector = $(`<button id="vido-id-request-dem-${i}" type="button" class="btn btn-xs f-right ui-btn-preview-dem-layer" value="${i}"><i class="fas fa-search"></i>미리보기</button>`);
				//				dd_vector.append(check_vector).append(label_vector).append(btn_vector);
				dd_vector.append(span).append(btn_vector);
				demTitle.append(dd_vector);
			}

			// 위성영상 데이터 반복문
			if (kompsat) {
				for (let i = 0; i < kompsat.length; i++) {
					const elem = kompsat[i];
					console.log(elem);
					const dd = $('<dd title="' + elem.fullFileCoursNm + '">');
					//					const check = $(`<input type="checkbox" class="js-search-result-kompsat" value="${i}" id="vido-id-kompsat-${i}">`);
					//					const label = $(`<label for="vido-id-kompsat-${i}">`).text(elem.vidoNm);
					const span = $('<span>').text(elem.vidoNm);
					const btn = $(`<button id="vido-id-request-kompsat-${i}" type="button" class="btn btn-xs f-right ui-btn-preview-kompsat-image-layer" value="${i}"><i class="fas fa-search"></i>미리보기</button>`);
					if (!elem.thumbnail) {
						$(btn).prop('disabled', true);
					}
					//					dd.append(check).append(label).append(btn);
					dd.append(span).append(btn);
					satelliteTitle.append(dd);

				}
			}

			// 위성영상 데이터 반복문
			if (landsat) {
				for (let i = 0; i < landsat.length; i++) {
					const elem = landsat[i];
					console.log(elem);
					const dd = $('<dd title="' + elem.fullFileCoursNm + '">');
					//					const check = $(`<input type="checkbox" class="js-search-result-landsat" value="${i}" id="vido-id-landsat-${i}">`);
					//					const label = $(`<label for="vido-id-landsat-${i}">`).text(elem.vidoNm);
					const span = $('<span>').text(elem.vidoNm);
					const btn = $(`<button id="vido-id-request-landsat-${i}" type="button" class="btn btn-xs f-right ui-btn-preview-landsat-image-layer" value="${i}"><i class="fas fa-search"></i>미리보기</button>`);
					if (!elem.thumbnail) {
						$(btn).prop('disabled', true);
					}
					//					dd.append(check).append(label).append(btn);
					dd.append(span).append(btn);
					satelliteTitle.append(dd);

				}
			}

			if (sentinel) {
				for (let i = 0; i < sentinel.length; i++) {
					const elem = sentinel[i];
					console.log(elem);
					const dd = $('<dd title="' + elem.fullFileCoursNm + '">');
					//					const check = $(`<input type="checkbox" class="js-search-result-sentinel" value="${i}" id="vido-id-sentinel-${i}">`);
					//					const label = $(`<labelC for="vido-id-sentinel-${i}">`).text(elem.vidoNm);
					const span = $('<span>').text(elem.vidoNm);
					const btn = $(`<button id="vido-id-request-sentinel-${i}" type="button" class="btn btn-xs f-right ui-btn-preview-sentinel-image-layer" value="${i}"><i class="fas fa-search"></i>미리보기</button>`);
					if (!elem.thumbnail) {
						$(btn).prop('disabled', true);
					}
					//					dd.append(check).append(label).append(btn);
					dd.append(span).append(btn);
					satelliteTitle.append(dd);

				}
			}


			if (cas) {
				for (let i = 0; i < cas.length; i++) {
					const elem = cas[i];
					console.log(elem);
					const dd = $('<dd title="' + elem.fullFileCoursNm + '">');
					//					const check = $(`<input type="checkbox" class="js-search-result-sentinel" value="${i}" id="vido-id-sentinel-${i}">`);
					//					const label = $(`<labelC for="vido-id-sentinel-${i}">`).text(elem.vidoNm);
					const span = $('<span>').text(elem.vidoNm);
					const btn = $(`<button id="vido-id-request-cas-${i}" type="button" class="btn btn-xs f-right ui-btn-preview-cas-image-layer" value="${i}"><i class="fas fa-search"></i>미리보기</button>`);
					if (!elem.thumbnail) {
						$(btn).prop('disabled', true);
					}
					//					dd.append(check).append(label).append(btn);
					dd.append(span).append(btn);
					satelliteTitle.append(dd);

				}
			}

			// 항공영상 데이터 반복문
			if (aerial) {
				for (let i = 0; i < aerial.length; i++) {
					const elem = aerial[i];
					console.log(elem);
					const dd = $('<dd title="' + elem.fullFileCoursNm + '">');
					//					const check = $(`<input type="checkbox" class="js-search-result-aerial" value="${i}" id="vido-id-aerial-${i}">`);
					//					const label = $(`<label for="vido-id-aerial-${i}">`).text(elem.vidoNm);
					const span = $('<span>').text(elem.vidoNm);
					const btn = $(`<button id="vido-id-request-aerial-${i}" type="button" class="btn btn-xs f-right ui-btn-preview-aerial-image-layer" value="${i}"><i class="fas fa-search"></i>미리보기</button>`);
					if (!elem.thumbnail) {
						$(btn).prop('disabled', true);
					}
					dd.append(span).append(btn);
					aerialTitle.append(dd);

				}
			}

			// 정사영상 데이터 반복문
			if (ortho) {
				for (let i = 0; i < ortho.length; i++) {
					const elem = ortho[i];
					console.log(elem);
					const dd = $('<dd title="' + elem.fullFileCoursNm + '">');
					//					const check = $(`<input type="checkbox" class="js-search-result-ortho" value="${i}" id="vido-id-ortho-${i}">`);
					//					const label = $(`<label for="vido-id-ortho-${i}">`).text(elem.vidoNm);
					const span = $('<span>').text(elem.vidoNm);
					const btn = $(`<button id="vido-id-request-ortho-${i}" type="button" class="btn btn-xs f-right ui-btn-preview-ortho-image-layer" value="${i}"><i class="fas fa-search"></i>미리보기</button>`);
					if (!elem.thumbnail) {
						$(btn).prop('disabled', true);
					}
					dd.append(span).append(btn);
					orthoTitle.append(dd);

				}
			}


			const resultList2 = $('<div id="resultList5">')
			outerPanel.append(resultList2);

			// 긴급 영상
			const emerData = $('<div class="in-panel-title">').text(`긴급 영상`);
			resultList2.append(emerData);

			// 긴급 영상 영역 dl
			const dl2 = $('<dl class="result-list-input">');
			resultList2.append(dl2);

			//			const rainTitle = $('<dt>').text(`폭우(${rain.length}건)`);
			//			dl2.append(rainTitle);

			const earthquakeTitle = $('<dt>').text(`지진(${earthquake.length}건)`);
			dl2.append(earthquakeTitle);

			const forestFireTitle = $('<dt>').text(`산불(${forestFire.length}건)`);
			dl2.append(forestFireTitle);

			//			const typhoonTitle = $('<dt>').text(`태풍(${typhoon.length}건)`);
			//			dl2.append(typhoonTitle);

			const floodTitle = $('<dt>').text(`수해(${flood.length}건)`);
			dl2.append(floodTitle);

			const landslipTitle = $('<dt>').text(`산사태(${landslip.length}건)`);
			dl2.append(landslipTitle);

			const maritimeTitle = $('<dt>').text(`해양재난(${maritime.length}건)`);
			dl2.append(maritimeTitle);
			
			const redTideTitle = $('<dt>').text(`적조(${redTide.length}건)`);
			dl2.append(redTideTitle);

			// floodType 데이터 반복문
			if (flood) {
				for (let i = 0; i < flood.length; i++) {
					const elem = flood[i];
					console.log(elem);
					const dd = $('<dd title="' + elem.fullFileCoursNm + '">');
					//					const check = $(`<input type="checkbox" class="js-search-result-flood" value="${i}" id="vido-id-flood-${i}">`);
					//					const label = $(`<label for="vido-id-flood-${i}">`).text(elem.vidoNm);
					const span = $('<span>').text(elem.vidoNm);
					const btn = $(`<button id="vido-id-request-flood-${i}" type="button" class="btn btn-xs f-right ui-btn-preview-flood-image-layer" value="${i}"><i class="fas fa-search"></i>미리보기</button>`);
					if (!elem.thumbnail) {
						$(btn).prop('disabled', true);
					}
					//					dd.append(check).append(label).append(btn);
					dd.append(span).append(btn);
					floodTitle.append(dd);

				}
			}

			// forestFire 데이터 반복문
			if (forestFire) {
				for (let i = 0; i < forestFire.length; i++) {
					const elem = forestFire[i];
					console.log(elem);
					const dd = $('<dd title="' + elem.fullFileCoursNm + '">');
					//					const check = $(`<input type="checkbox" class="js-search-result-forestfire" value="${i}" id="vido-id-forest-${i}">`);
					//					const label = $(`<label for="vido-id-forest-${i}">`).text(elem.vidoNm);
					const span = $('<span>').text(elem.vidoNm);
					const btn = $(`<button id="vido-id-request-forest-${i}" type="button" class="btn btn-xs f-right ui-btn-preview-forestfire-image-layer" value="${i}"><i class="fas fa-search"></i>미리보기</button>`);
					if (!elem.thumbnail) {
						$(btn).prop('disabled', true);
					}
					//					dd.append(check).append(label).append(btn);
					dd.append(span).append(btn);
					forestFireTitle.append(dd);

				}
			}

			// landslip 데이터 반복문
			if (landslip) {
				for (let i = 0; i < landslip.length; i++) {
					const elem = landslip[i];
					console.log(elem);
					const dd = $('<dd title="' + elem.fullFileCoursNm + '">');
					//					const check = $(`<input type="checkbox" class="js-search-result-landslip" value="${i}" id="vido-id-land-${i}">`);
					//					const label = $(`<label for="vido-id-land-${i}">`).text(elem.vidoNm);
					const span = $('<span>').text(elem.vidoNm);
					const btn = $(`<button id="vido-id-request-land-${i}" type="button" class="btn btn-xs f-right ui-btn-preview-landslide-image-layer" value="${i}"><i class="fas fa-search"></i>미리보기</button>`);
					if (!elem.thumbnail) {
						$(btn).prop('disabled', true);
					}
					//					dd.append(check).append(label).append(btn);
					dd.append(span).append(btn);
					landslipTitle.append(dd);

				}
			}

			// rain 데이터 반복문
			//			if (rain) {
			//				for (let i = 0; i < rain.length; i++) {
			//					const elem = rain[i];
			//					console.log(elem);
			//					let list = CMSC003.Storage.get('searchDataMap');
			//					if (!list) {
			//						list = {};
			//					}
			//					list[elem.vidoId] = elem;
			//					const dd = $('<dd>');
			//					const check = $(`<input type="checkbox" class="js-search-result-rain" value="${i}" id="vido-id-${i}">`);
			//					const label = $(`<label for="vido-id-${i}">`).text(elem.vidoNm);
			//					const btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-image-layer" value="${i}"><i class="fas fa-search"></i>미리보기</button>`);
			//					if(!elem.thumbnail) {
			//						$(btn).prop('disabled', true);
			//					}
			//					dd.append(check).append(label).append(btn);
			//					rainTitle.append(dd);
			//
			//					CMSC003.Storage.set('searchDataMap', list);
			//				}
			//			}

			// earthquake 데이터 반복문
			if (earthquake) {
				for (let i = 0; i < earthquake.length; i++) {
					const elem = earthquake[i];
					console.log(elem);
					const dd = $('<dd title="' + elem.fullFileCoursNm + '">');
					//					const check = $(`<input type="checkbox" class="js-search-result-earthquake" value="${i}" id="vido-id-earth-${i}">`);
					//					const label = $(`<label for="vido-id-earth-${i}">`).text(elem.vidoNm);
					const span = $('<span>').text(elem.vidoNm);
					const btn = $(`<button id="vido-id-request-earth-${i}" type="button" class="btn btn-xs f-right ui-btn-preview-earthquake-image-layer" value="${i}"><i class="fas fa-search"></i>미리보기</button>`);
					if (!elem.thumbnail) {
						$(btn).prop('disabled', true);
					}
					//					dd.append(check).append(label).append(btn);
					dd.append(span).append(btn);
					earthquakeTitle.append(dd);

				}
			}

			// maritime 데이터 반복문
			if (maritime) {
				for (let i = 0; i < maritime.length; i++) {
					const elem = maritime[i];
					console.log(elem);
					const dd = $('<dd title="' + elem.fullFileCoursNm + '">');
					//					const check = $(`<input type="checkbox" class="js-search-result-maritime" value="${i}" id="vido-id-mari-${i}">`);
					//					const label = $(`<label for="vido-id-mari-${i}">`).text(elem.vidoNm);
					const span = $('<span>').text(elem.vidoNm);
					const btn = $(`<button id="vido-id-request-mari-${i}" type="button" class="btn btn-xs f-right ui-btn-preview-maritime-image-layer" value="${i}"><i class="fas fa-search"></i>미리보기</button>`);
					if (!elem.thumbnail) {
						$(btn).prop('disabled', true);
					}
					//					dd.append(check).append(label).append(btn);
					dd.append(span).append(btn);
					maritimeTitle.append(dd);

				}
			}

			// typhoon 데이터 반복문
			//			if (typhoon) {
			//				for (let i = 0; i < typhoon.length; i++) {
			//					const elem = typhoon[i];
			//					console.log(elem);
			//					let list = CMSC003.Storage.get('searchDataMap');
			//					if (!list) {
			//						list = {};
			//					}
			//					list[elem.vidoId] = elem;
			//					const dd = $('<dd>');
			//					const check = $(`<input type="checkbox" class="js-search-result-typhoon" value="${i}" id="vido-id-${i}">`);
			//					const label = $(`<label for="vido-id-${i}">`).text(elem.vidoNm);
			//					const btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-image-layer" value="${i}"><i class="fas fa-search"></i>미리보기</button>`);
			//					if(!elem.thumbnail) {
			//						$(btn).prop('disabled', true);
			//					}
			//					dd.append(check).append(label).append(btn);
			//					typhoonTitle.append(dd);
			//
			//					CMSC003.Storage.set('searchDataMap', list);
			//				}
			//			}
			// redTide 적조 데이터 반복문
			if (redTide) {
				for (let i = 0; i < redTide.length; i++) {
					const elem = redTide[i];
					console.log(elem);
					const dd = $('<dd title="' + elem.fullFileCoursNm + '">');
					//					const check = $(`<input type="checkbox" class="js-search-result-redTide" value="${i}" id="vido-id-redTide-${i}">`);
					//					const label = $(`<label for="vido-id-redTide-${i}">`).text(elem.vidoNm);
					const span = $('<span>').text(elem.vidoNm);
					const btn = $(`<button id="vido-id-request-redTide-${i}" type="button" class="btn btn-xs f-right ui-btn-preview-redTide-image-layer" value="${i}"><i class="fas fa-search"></i>미리보기</button>`);
					if (!elem.thumbnail) {
						$(btn).prop('disabled', true);
					}
					//					dd.append(check).append(label).append(btn);
					dd.append(span).append(btn);
					redTideTitle.append(dd);

				}
			}

			// 분석 영상
			const classificationData = $('<div class="in-panel-title">').text(`분석결과 데이터`);

			// 검색결과 세부영역
			const resultList3 = $('<div id="resultList6">').append(classificationData);
			outerPanel.append(resultList3);

			// 분석 데이터 영역 dl
			const dl3 = $('<dl class="result-list-input">');
			resultList3.append(dl3);

			const vectorAnalObjTitle = $('<dt>').text(`객체추출 벡터 데이터(${vectorAnalObj.length}건)`);
			dl3.append(vectorAnalObjTitle);

			const rasterAnalObjTitle = $('<dt>').text(`객체추출 영상 데이터(${rasterAnalObj.length}건)`);
			dl3.append(rasterAnalObjTitle);

			// 분석 벡터 데이터 반복문
			if (vectorAnalObj) {
				for (let i = 0; i < vectorAnalObj.length; i++) {
					const elem = vectorAnalObj[i];
					console.log(elem);
					const dd = $('<dd title="' + elem.vectorFileCoursNm + '">');
					//					const check = $(`<input type="checkbox" class="js-search-result-vectoranalobj" value="${i}" id="vido-id-vanalobj-${i}">`);
					//					const label = $(`<label for="vido-id-vanalobj-${i}">`).text(elem.vidoNm);
					const span = $('<span>').text(elem.vidoNm);
					const btn = $(`<button id="vido-id-request-vanalobj-${i}" type="button" class="btn btn-xs f-right ui-btn-preview-shp-obj-layer" value="${i}"><i class="fas fa-search"></i>미리보기</button>`);
					if (!elem.thumbnail) {
						$(btn).prop('disabled', true);
					}
					dd.append(span).append(btn);
					vectorAnalObjTitle.append(dd);
				}
			}

			// 분석 영상 데이터 반복문
			if (rasterAnalObj) {
				for (let i = 0; i < rasterAnalObj.length; i++) {
					const elem = rasterAnalObj[i];
					console.log(elem);
					const dd = $('<dd title="' + elem.fullFileCoursNm + '">');
					//					const check = $(`<input type="checkbox" class="js-search-result-rasteranalobj" value="${i}" id="vido-id-ranalobj-${i}">`);
					//					const label = $(`<label for="vido-id-ranalobj-${i}">`).text(elem.vidoNm);
					const span = $('<span>').text(elem.vidoNm);
					const btn = $(`<button id="vido-id-request-ranalobj-${i}" type="button" class="btn btn-xs f-right ui-btn-preview-raster-anal-obj-layer" value="${i}"><i class="fas fa-search"></i>미리보기</button>`);
					if (!elem.thumbnail) {
						$(btn).prop('disabled', true);
					}
					dd.append(span).append(btn);
					rasterAnalObjTitle.append(dd);
				}
			}

			const vectorAnalChgTitle = $('<dt>').text(`변화탐지 벡터 데이터(${vectorAnalChg.length}건)`);
			dl3.append(vectorAnalChgTitle);

			const rasterAnalChgTitle = $('<dt>').text(`변화탐지 영상 데이터(${rasterAnalChg.length}건)`);
			dl3.append(rasterAnalChgTitle);

			// 분석 벡터 데이터 반복문
			if (vectorAnalChg) {
				for (let i = 0; i < vectorAnalChg.length; i++) {
					const elem = vectorAnalChg[i];
					console.log(elem);
					const dd = $('<dd title="' + elem.vectorFileCoursNm + '">');
					//					const check = $(`<input type="checkbox" class="js-search-result-vectoranalchg" value="${i}" id="vido-id-vanalchg-${i}">`);
					//					const label = $(`<label for="vido-id-vanalchg-${i}">`).text(elem.vidoNm);
					const span = $('<span>').text(elem.vidoNm);
					const btn = $(`<button id="vido-id-request-vanalchg-${i}" type="button" class="btn btn-xs f-right ui-btn-preview-shp-chg-layer" value="${i}"><i class="fas fa-search"></i>미리보기</button>`);
					if (!elem.thumbnail) {
						$(btn).prop('disabled', true);
					}
					dd.append(span).append(btn);
					vectorAnalChgTitle.append(dd);
				}
			}

			// 분석 영상 데이터 반복문
			if (rasterAnalChg) {
				for (let i = 0; i < rasterAnalChg.length; i++) {
					const elem = rasterAnalChg[i];
					console.log(elem);
					const dd = $('<dd title="' + elem.fullFileCoursNm + '">');
					//					const check = $(`<input type="checkbox" class="js-search-result-rasteranalchg" value="${i}" id="vido-id-ranalchg-${i}">`);
					//					const label = $(`<label for="vido-id-ranalchg-${i}">`).text(elem.vidoNm);
					const span = $('<span>').text(elem.vidoNm);
					const btn = $(`<button id="vido-id-request-ranalchg-${i}" type="button" class="btn btn-xs f-right ui-btn-preview-raster-anal-chg-layer" value="${i}"><i class="fas fa-search"></i>미리보기</button>`);
					if (!elem.thumbnail) {
						$(btn).prop('disabled', true);
					}
					dd.append(span).append(btn);
					rasterAnalChgTitle.append(dd);
				}
			}

		},
		showRequestResultTree(json) {
			if (typeof json === 'string') {
				json = JSON.parse(json);
			}
			$(".js-request-result-area div.panel-default").empty();

			const outerPanel = $(".js-request-result-area div.panel-default");

			//console.log("showRequestResultTree", json)
			// 기존 데이터
			const digital = [];
			const dem = { length: 0, map: {} };
			const graphics = [];
			// const demo = [];
			// const landgraphics = [];
			// const buldgraphics = [];

			// 위성
			const landsat = { length: 0, map: {} };
			const kompsat = { length: 0, map: {} };
			const sentinel = { length: 0, map: {} };
			const cas = { length: 0, map: [] };

			// 영상
			const aerial = { length: 0, map: {} };
			const ortho = { length: 0, map: {} };

			// 재해
			const flood = [];
			const forestFire = [];
			const landslip = [];
			const earthquake = [];
			const maritime = [];
			const redTide = []; //적조

			// 분석
			const rasterAnalObj = [];
			const vectorAnalObj = [];

			const rasterAnalChg = [];
			const vectorAnalChg = [];

			const dataMap = {};

			const existing = json.existing;

			let count = 0;

			// 수치지도
			const digitalList = existing.digitalMap;
			if (digitalList) {
				for (const e of digitalList) {
					//console.log(e);
					dataMap[e.vidoNm] = e;
					digital.push(e);
				}
			}

			// DEM (year, map)
			const demList = existing.demMap;
			if (demList) {
				//				count = 0;
				//				for (const e of demList) {
				//					const {year, maps} = e;
				//					dem.map[year] = [];
				//					for (const elem of maps) {
				//						//console.log('dem', elem);
				//						dataMap[`dem-${count}`] = elem;
				//						dem.map[year].push({index:count, elem:elem});
				//						count++;
				//					}
				//				}
				//				dem.length = count;
				count = 0;
				for (const e1 of demList) {
					const { year, maps } = e1;
					dem.map[year] = [];
					for (const e2 of maps) {
						const { dpi, map } = e2;
						dem.map[year][dpi] = [];
						for (const elem of map) {
							dataMap[`dem-${count}`] = elem;
							dem.map[year][dpi].push({ index: count, elem: elem });
							count++;
						}
					}
				}
				dem.length = count;
			}

			// 통계
			const graphicsList = existing.graphicsMap;
			if (graphicsList) {
				for (const e of graphicsList) {
					//console.log(e);
					dataMap[e.vidoNm] = e;
					graphics.push(e);
				}
			}

			// 위성 - kompsat (satNm, map)
			const kompsatList = existing.satellite[0];
			if (kompsatList) {
				count = 0;
				const { satNm, map } = kompsatList;
				for (const e of map) {
					//					const {folder, fileList} = e;
					//					kompsat.map[folder] = [];
					//					for (const elem of fileList) {
					//						//console.log('komp',elem);
					//						dataMap[`kompsat-${count}`] = elem;
					//						kompsat.map[folder].push({index:count, elem:elem});
					//						count++;
					//					}

					const { date, folderList } = e;

					kompsat.map[date] = {};

					for (const folder of folderList) {
						const { folderNm, fileList } = folder;
						kompsat.map[date][folderNm] = [];
						for (const elem of fileList) {
							//console.log('komp',elem);
							dataMap[`kompsat-${count}`] = elem;
							kompsat.map[date][folderNm].push({ index: count, elem: elem });
							count++;
						}
					}

				}
				kompsat.length = count;
			}

			// 위성 - landsat (satNm, map)
			const landsatList = existing.satellite[1];
			if (landsatList) {
				count = 0;
				const { satNm, map } = landsatList;
				for (const e of map) {
					//					const {folder, fileList} = e;
					//					landsat.map[folder] = [];
					//					for (const elem of fileList) {
					//						//console.log('landsat',elem);
					//						dataMap[`landsat-${count}`] = elem;
					//						landsat.map[folder].push({index:count, elem:elem});
					//						count++;
					//					}
					const { date, folderList } = e;

					landsat.map[date] = {};
					
					for (const folder of folderList) {
						const { folderNm, fileList } = folder;
						landsat.map[date][folderNm] = [];
						// landsat.map[folderNm] = [];
						for (const elem of fileList) {
							//console.log('komp',elem);
							dataMap[`landsat-${count}`] = elem;
							landsat.map[date][folderNm].push({ index: count, elem: elem });
							count++;
						}
					}
				}
				landsat.length = count;
			}

			// 위성 - sentinel (satNm, map)
			const sentinelList = existing.satellite[2];
			if (sentinelList) {
				count = 0;
				const { satNm, map } = sentinelList;
				for (const e of map) {
					//					const {folder, fileList} = e;
					//					sentinel.map[folder] = [];
					//					for (const elem of fileList) {
					//						//console.log('sentinel',elem);
					//						dataMap[`sentinel-${count}`] = elem;
					//						sentinel.map[folder].push({index:count, elem:elem});
					//						count++;
					//					}

					const { date, folderList } = e;

					sentinel.map[date] = {};
					
					for (const folder of folderList) {
						const { folderNm, fileList } = folder;
						sentinel.map[date][folderNm] = [];
						for (const elem of fileList) {
							//console.log('komp',elem);
							dataMap[`sentinel-${count}`] = elem;
							sentinel.map[date][folderNm].push({ index: count, elem: elem });
							count++;
						}
					}
				}
				sentinel.length = count;
			}

			// 위성 - cas (satNm, map)
			const casList = existing.satellite[3];
			if (casList) {
				count = 0;
				const { satNm, map } = casList;
				for (const e of map) {
					//					const {folder, fileList} = e;
					//					cas.map[folder] = [];
					//					for (const elem of fileList) {
					//						//console.log('cas',elem);
					//						dataMap[`cas-${count}`] = elem;
					//						cas.map[folder].push({index:count, elem:elem});
					//						count++;
					//					}
					const { date, folderList } = e;
					cas.map[date] = {};
					
					for (const folder of folderList) {
						const { folderNm, fileList } = folder;
						cas.map[date][folderNm] = [];
						for (const elem of fileList) {
							//console.log('komp',elem);
							dataMap[`cas-${count}`] = elem;
							cas.map[date][folderNm].push({ index: count, elem: elem });
							count++;
						}
					}
				}
				cas.length = count;
			}

			// 항공영상 (year, map)
			const aerialList = existing.airOrientalMap
			if (aerialList) {
				//				count = 0;
				//				for (const e of aerialList) {
				//					const {year, map} = e;
				//					aerial.map[year] = [];
				//					for (const elem of map) {
				//						//console.log('aerial', elem);
				//						dataMap[`aerial-${count}`] = elem;
				//						aerial.map[year].push({index:count, elem:elem});
				//						count++;
				//					}
				//				}
				//				aerial.length = count;
				count = 0;
				for (const e1 of aerialList) {
					const { year, maps } = e1;
					aerial.map[year] = {};
					for (const e2 of maps) {
						const { dpi, map } = e2;
						aerial.map[year][dpi] = [];
						for (const elem of map) {
							dataMap[`aerial-${count}`] = elem;
							aerial.map[year][dpi].push({ index: count, elem: elem });
							count++;
						}
					}
				}
				aerial.length = count;
			}

			// 정사영상 (year, map)
			const orthoList = existing.ortOrientalMap;
			if (orthoList) {
				//				count = 0;
				//				for (const e of orthoList) {
				//					const {year, map} = e;
				//					ortho.map[year] = [];
				//					for (const elem of map) {
				//						//console.log('ortho', elem);
				//						dataMap[`ortho-${count}`] = elem;
				//						ortho.map[year].push({index:count, elem:elem});
				//						count++;
				//					}
				//				}
				//				ortho.length = count;
				count = 0;
				for (const e1 of orthoList) {
					const { year, maps } = e1;
					ortho.map[year] = {};
					for (const e2 of maps) {
						const { dpi, map } = e2;
						ortho.map[year][dpi] = [];
						for (const elem of map) {
							dataMap[`ortho-${count}`] = elem;
							ortho.map[year][dpi].push({ index: count, elem: elem });
							count++;
						}
					}
				}
				ortho.length = count;
			}

			const disaster = json.disaster;

			// 산불
			const forestFireList = disaster.ForestFire;
			if (forestFireList) {
				for (const e of forestFireList) {
					//console.log(e);
					dataMap[`forestFire-${forestFire.length}`] = e;
					forestFire.push(e);
				}
			}

			// 지진
			const earthquakeList = disaster.Earthquake;
			if (earthquakeList) {
				for (const e of earthquakeList) {
					//console.log(e);
					dataMap[`earthquake-${earthquake.length}`] = e;
					earthquake.push(e);
				}
			}

			// 산사태
			const landslipList = disaster.Landslide;
			if (landslipList) {
				for (const e of landslipList) {
					//console.log(e);
					dataMap[`landslide-${landslip.length}`] = e;
					landslip.push(e);
				}
			}

			// 홍수 
			const floodList = disaster.Flood;
			if (floodList) {
				for (const e of floodList) {
					//console.log(e);
					dataMap[`flood-${flood.length}`] = e;
					flood.push(e);
				}
			}

			// 해양재난 
			const maritimeList = disaster.MaritimeDisaster;
			if (maritimeList) {
				for (const e of maritimeList) {
					//console.log(e);
					dataMap[`maritime-${maritime.length}`] = e;
					maritime.push(e);
				}
			}
			
			// 적조
			const redTideList = disaster.RedTide;
			if (redTideList) {
				for (const e of redTideList) {
					//console.log(e);
					dataMap[`redTide-${redTide.length}`] = e;
					redTide.push(e);
				}
			}

			const analysis = json.analysis;

			// 객체추출 벡터 
			const vectorAnalObjList = analysis.objectExt.vector;
			if (vectorAnalObjList) {
				for (const e of vectorAnalObjList) {
					//console.log(e);
					dataMap[`vectorAnalObj-${vectorAnalObj.length}`] = e;
					vectorAnalObj.push(e);
				}
			}

			// 객체추출 영상 
			const rasterAnalObjList = analysis.objectExt.raster;
			if (rasterAnalObjList) {
				for (const e of rasterAnalObjList) {
					//console.log(e);
					dataMap[`rasterAnalObj-${rasterAnalObj.length}`] = e;
					rasterAnalObj.push(e);
				}
			}

			// 변화탐지 벡터 
			const vectorAnalChgList = analysis.changeDet.vector;
			if (vectorAnalChgList) {
				for (const e of vectorAnalChgList) {
					//console.log(e);
					dataMap[`vectorAnalChg-${vectorAnalChg.length}`] = e;
					vectorAnalChg.push(e);
				}
			}

			// 변화탐지 영상 
			const rasterAnalChgList = analysis.changeDet.raster;
			if (rasterAnalChgList) {
				for (const e of rasterAnalChgList) {
					//console.log(e);
					dataMap[`rasterAnalChg-${rasterAnalChg.length}`] = e;
					rasterAnalChg.push(e);
				}
			}

			CMSC003.Storage.set('requestDataMap', dataMap);

			// 입력 panel
			const panelTitle = $('<div class="in-panel-title">').text('재난정보 목록');
			outerPanel.append(panelTitle);

			// 검색결과 요약
			const resultList = $('<ul class="result-list">');
			outerPanel.append(resultList);
			const resultListLi1 = $('<li>').text(`재난(${flood.length + forestFire.length + landslip.length + earthquake.length + maritime.length + redTide.length}건)`);
			resultList.append(resultListLi1);

			// ## 기존 데이터 ##
			const existData = $('<div class="in-panel-title">').text('기존 데이터');
			outerPanel.append(existData);

			// 기존 데이터(resultList1) 영역 생성
			const resultList1 = $('<div id="resultList1_request">');
			outerPanel.append(resultList1);
			const resultList1_div = $('<div class="result-list-input">');
			resultList1.append(resultList1_div);

			// 기존 데이터 - 수치지도
			const resultList1_vector = $('<div id="resultList1_request_digital_tree">');
			const vector_tree = $('<ul>');
			const vectorTitle = $('<li class="js-control-digital js-tree-parent-1" id="vido-id-request-digital-title">');
			const vector_span = $('<span>').text(`수치지도(${digital.length}건)`);
			const vector_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-digital-layer" data-tree="vido-id-request-digital-title"><i class="fas fa-search"></i></button>`);
			const vector_ul = $('<ul>');
			resultList1_div.append(resultList1_vector);
			resultList1_vector.append(vector_tree);
			vector_tree.append(vectorTitle);
			vectorTitle.append(vector_span);
			vectorTitle.append(vector_btn);
			vectorTitle.append(vector_ul);

			// 기존 데이터 - 항공영상
			const resultList1_aerial = $('<div id="resultList1_request_aerial_tree">');
			const aerial_tree = $('<ul>');
			const aerialTitle = $('<li class="js-control-aerial js-tree-parent-1" id="vido-id-request-aerial-title">');
			const aerial_span = $('<span>').text(`항공영상(${aerial.length}건)`)
			const aerial_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-aerial-image-layer" data-tree="vido-id-request-aerial-title"><i class="fas fa-search"></i></button>`);
			const aerial_ul = $('<ul>');
			resultList1_div.append(resultList1_aerial);
			resultList1_aerial.append(aerial_tree);
			aerial_tree.append(aerialTitle);
			aerialTitle.append(aerial_span);
			aerialTitle.append(aerial_btn);
			aerialTitle.append(aerial_ul);

			// 기존 데이터 - 정사영상
			const resultList1_ortho = $('<div id="resultList1_request_ortho_tree">');
			const ortho_tree = $('<ul>');
			const orthoTitle = $('<li class="js-control-ortho js-tree-parent-1" id="vido-id-request-ortho-title">');
			const ortho_span = $('<span>').text(`정사영상(${ortho.length}건)`);
			const ortho_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-ortho-image-layer" data-tree="vido-id-request-ortho-title"><i class="fas fa-search"></i></button>`);
			const ortho_ul = $('<ul>');
			resultList1_div.append(resultList1_ortho);
			resultList1_ortho.append(ortho_tree);
			ortho_tree.append(orthoTitle);
			orthoTitle.append(ortho_span);
			orthoTitle.append(ortho_btn);
			orthoTitle.append(ortho_ul);

			// 기존 데이터 - 위성영상
			const resultList1_satellite = $('<div id="resultList1_request_satellite_tree">');
			const satellite_tree = $('<ul>');
			const satelliteTitle = $('<li class="js-control-satellite js-tree-parent-1" id="vido-id-request-satellite-title">');
			const satellite_span = $('<span>').text(`위성영상(${kompsat.length + sentinel.length + landsat.length + cas.length}건)`);
			const satellite_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-satellite-image-layer" data-tree="vido-id-request-satellite-title"><i class="fas fa-search"></i></button>`);
			const satellite_ul = $('<ul>');
			resultList1_div.append(resultList1_satellite);
			resultList1_satellite.append(satellite_tree);
			satellite_tree.append(satelliteTitle);
			satelliteTitle.append(satellite_span);
			satelliteTitle.append(satellite_btn);
			satelliteTitle.append(satellite_ul);

			// 기존 데이터 - DEM
			const resultList1_dem = $('<div id="resultList1_request_dem_tree">');
			const dem_tree = $('<ul>');
			const demTitle = $('<li class="js-control-dem js-tree-parent-1" id="vido-id-request-dem-title">');
			const dem_span = $('<span>').text(`DEM(${dem.length}건)`);
			const dem_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-dem-layer" data-tree="vido-id-request-dem-title"><i class="fas fa-search"></i></button>`);
			const dem_ul = $('<ul>');
			resultList1_div.append(resultList1_dem);
			resultList1_dem.append(dem_tree);
			dem_tree.append(demTitle);
			demTitle.append(dem_span);
			demTitle.append(dem_btn);
			demTitle.append(dem_ul);

			// 기존 데이터 - 통계
			const resultList1_graphics = $('<div id="resultList1_request_graphics_tree">');
			const graphics_tree = $('<ul>');
			const graphicsTitle = $('<li class="js-control-graphics js-tree-parent-1" id="vido-id-request-graphics-title">');
			const graphics_span = $('<span>').text(`통계(${graphics.length}건)`);
			const graphics_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-graphics-layer" data-tree="vido-id-request-graphics-title"><i class="fas fa-search"></i></button>`);
			const graphics_ul = $('<ul>');
			resultList1_div.append(resultList1_graphics);
			resultList1_graphics.append(graphics_tree);
			graphics_tree.append(graphicsTitle);
			graphicsTitle.append(graphics_span);
			graphicsTitle.append(graphics_btn);
			graphicsTitle.append(graphics_ul);

			// 기존 데이터 - 수치지도 노드 추가 (image / file)
			for (let i = 0; i < digital.length; i++) {
				const li = $(`<li class="js-search-result-digital js-tree-child-1" value="${digital[i].vidoNm}" id="vido-id-request-digital-${i}" title="${digital[i].vidoNm}">`);
				const span = $('<span>').text(digital[i].vidoNmKr);
				const btn_vector = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-digital-layer" value="${digital[i].vidoNm}" data-tree="vido-id-request-digital-${i}"><i class="fas fa-search"></i></button>`);
				vector_ul.append(li.append(span).append(btn_vector));
			}

			// 기존 데이터 - 항공영상 노드 추가 (image / year / file)
			//			for (const year in aerial.map) {
			//				const fileList = aerial.map[year];
			//				// 년도 폴더 생성
			//				const year_li = $(`<li class="js-tree-parent-2" value="${year}" id="vido-id-request-aerial-title-${year}">`);
			//				const year_span = $('<span>').text(year);
			//				const year_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-aerial-image-layer" data-tree="vido-id-request-aerial-title-${year}"><i class="fas fa-search"></i></button>`);
			//				const year_ul = $('<ul>');
			//				year_li.append(year_span);
			//				year_li.append(year_btn);
			//				year_li.append(year_ul);
			//				for (const elem of fileList) {
			//					const li = $(`<li class="js-search-result-aerial js-tree-child-2" value="${elem.index}" id="vido-id-request-aerial-${elem.index}">`);
			//					const span = $('<span>').text(elem.elem.vidoNm);
			//					const btn_aerial = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-aerial-image-layer" value="${elem.elem.vidoNm}" data-tree="vido-id-request-aerial-${elem.index}"><i class="fas fa-search"></i></button>`);
			//					if (!elem.thumbnail) {
			//						btn_aerial.attr("disabled", true);
			//					}
			//					year_ul.append(li.append(span).append(btn_aerial));
			//				}
			//				aerial_ul.append(year_li);
			//			}
			for (const year in aerial.map) {
				const year_li = $(`<li class="js-tree-parent-2" value="${year}" id="vido-id-aerial-title-${year}">`);
				const year_span = $('<span>').text(year);
				const year_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-aerial-image-layer" data-tree="vido-id-aerial-title-${year}"><i class="fas fa-search"></i></button>`);
				const year_ul = $('<ul>');
				year_li.append(year_span);
				year_li.append(year_btn);
				year_li.append(year_ul);
				for (const dpi in year) {
					//				const keys = Object.keys(aerial.map[year]);
					//				for (const dpi in keys) {
					// const fileList = aerial.map[year][dpi];
					const fileList = aerial.map[year][Object.keys(aerial.map[year])[dpi]];
					if (!fileList) {
						break;
					}
					const dpi_li = $(`<li class="js-tree-parent-3" value="${dpi}" id="vido-id-aerial-title-${year}-${dpi}">`);
					//const dpi_span = $('<span>').text(dpi);
					const dpi_span = $('<span>').text(Object.keys(aerial.map[year])[dpi]);
					const dpi_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-aerial-image-layer" data-tree="vido-id-aerial-title-${year}-${dpi}"><i class="fas fa-search"></i></button>`);
					const dpi_ul = $('<ul>');
					dpi_li.append(dpi_span);
					dpi_li.append(dpi_btn);
					dpi_li.append(dpi_ul);
					console.log(fileList);
					for (const elem of fileList) {
						const li = $(`<li class="js-search-result-aerial js-tree-child-3" value="${elem.index}" id="vido-id-aerial-${year}-${dpi}-${elem.index}" title="${elem.elem.fullFileCoursNm}">`);
						const span = $('<span>').text(elem.elem.vidoNm);
						const btn_aerial = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-aerial-image-layer" value="${elem.elem.vidoNm}" data-tree="vido-id-aerial-${year}-${dpi}-${elem.index}"><i class="fas fa-search"></i></button>`);
						if (!elem.elem.thumbnail) {
							btn_aerial.attr("disabled", true);
						}
						dpi_ul.append(li.append(span).append(btn_aerial));
					}
					year_ul.append(dpi_li);
				}
				aerial_ul.append(year_li);
			}

			// 기존 데이터 - 정사영상 노드 추가 (image / year / file)
			//			for (const year in ortho.map) {
			//				const fileList = ortho.map[year];
			//				// 년도 폴더 생성
			//				const year_li = $(`<li class="js-tree-parent-2" value="${year}" id="vido-id-request-ortho-title-${year}">`);
			//				const year_span = $('<span>').text(year);
			//				const year_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-ortho-image-layer" data-tree="vido-id-request-ortho-title-${year}"><i class="fas fa-search"></i></button>`);
			//				const year_ul = $('<ul>');
			//				year_li.append(year_span);
			//				year_li.append(year_btn);
			//				year_li.append(year_ul);
			//				for (const elem of fileList) {
			//					const li = $(`<li class="js-search-result-ortho js-tree-child-2" value="${elem.index}" id="vido-id-request-ortho-${elem.index}">`);
			//					const span = $('<span>').text(elem.elem.vidoNm);
			//					const btn_ortho = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-ortho-image-layer" value="${elem.elem.vidoNm}" data-tree="vido-id-request-ortho-${elem.index}"><i class="fas fa-search"></i></button>`);
			//					if (!elem.thumbnail) {
			//						btn_ortho.attr("disabled", true);
			//					}
			//					year_ul.append(li.append(span).append(btn_ortho));
			//				}
			//				ortho_ul.append(year_li);
			//			}
			for (const year in ortho.map) {
				const year_li = $(`<li class="js-tree-parent-2" value="${year}" id="vido-id-ortho-title-${year}">`);
				const year_span = $('<span>').text(year);
				const year_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-ortho-image-layer" data-tree="vido-id-ortho-title-${year}"><i class="fas fa-search"></i></button>`);
				const year_ul = $('<ul>');
				year_li.append(year_span);
				year_li.append(year_btn);
				year_li.append(year_ul);
				for (const dpi in year) {
					// const fileList = ortho.map[year][dpi];
					const fileList = ortho.map[year][Object.keys(ortho.map[year])[dpi]];
					if (!fileList) {
						break;
					}
					const dpi_li = $(`<li class="js-tree-parent-3" value="${dpi}" id="vido-id-ortho-title-${year}-${dpi}">`);
					// const dpi_span = $('<span>').text(dpi);
					const dpi_span = $('<span>').text(Object.keys(ortho.map[year])[dpi]);
					const dpi_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-ortho-image-layer" data-tree="vido-id-ortho-title-${year}-${dpi}"><i class="fas fa-search"></i></button>`);
					const dpi_ul = $('<ul>');
					dpi_li.append(dpi_span);
					dpi_li.append(dpi_btn);
					dpi_li.append(dpi_ul);
					for (const elem of fileList) {
						const li = $(`<li class="js-search-result-ortho js-tree-child-3" value="${elem.index}" id="vido-id-ortho-${year}-${dpi}-${elem.index}" title="${elem.elem.fullFileCoursNm}">`);
						const span = $('<span>').text(elem.elem.vidoNm);
						const btn_ortho = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-ortho-image-layer" value="${elem.elem.vidoNm}" data-tree="vido-id-ortho-${year}-${dpi}-${elem.index}"><i class="fas fa-search"></i></button>`);
						if (!elem.elem.thumbnail) {
							btn_ortho.attr("disabled", true);
						}
						dpi_ul.append(li.append(span).append(btn_ortho));
					}
					year_ul.append(dpi_li);
				}
				ortho_ul.append(year_li);
			}

//			//기존 데이터 - 위성영상(landsat) 노드 추가
//			if (landsat) {
//				const sat_li = $(`<li class="js-tree-parent-2" value="landsat" id="vido-id-request-satellite-title-landsat">`);
//				const sat_span = $('<span>').text("Landsat");
//				const sat_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-landsat-image-layer" data-tree="vido-id-request-satellite-title-landsat"><i class="fas fa-search"></i></button>`);
//				const sat_ul = $('<ul>');
//				sat_li.append(sat_span);
//				sat_li.append(sat_btn);
//				sat_li.append(sat_ul);
//				for (const folder in landsat.map) {
//					const fileList = landsat.map[folder];
//					const folder_li = $(`<li class="js-tree-parent-3" value="${folder}" id="vido-id-request-satellite-title-landsat-${folder}">`);
//					const folder_span = $('<span>').text(folder);
//					const folder_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-landsat-image-layer" data-tree="vido-id-request-satellite-title-landsat-${folder}"><i class="fas fa-search"></i></button>`);
//					const folder_ul = $('<ul>');
//					folder_li.append(folder_span);
//					folder_li.append(folder_btn);
//					folder_li.append(folder_ul);
//					for (const elem of fileList) {
//						const li = $(`<li class="js-search-result-landsat js-tree-child-3" value="${elem.index}" id="vido-id-request-landsat-${elem.index}" title="${elem.elem.fullFileCoursNm}">`);
//						const span = $('<span>').text(elem.elem.vidoNm);
//						const btn_landsat = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-landsat-image-layer" value="${elem.elem.vidoNm}" data-tree="vido-id-request-landsat-${elem.index}"><i class="fas fa-search"></i></button>`);
//						if (!elem.elem.thumbnail) {
//							btn_landsat.attr("disabled", true);
//						}
//						folder_ul.append(li.append(span).append(btn_landsat));
//					}
//					sat_ul.append(folder_li);
//				}
//				satellite_ul.append(sat_li);
//			}
//
//			//기존 데이터 - 위성영상(kompsat) 노드 추가
//			if (kompsat) {
//				const sat_li = $(`<li class="js-tree-parent-2" value="kompsat" id="vido-id-request-satellite-title-kompsat">`);
//				const sat_span = $('<span>').text("Kompsat");
//				const sat_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-kompsat-image-layer" data-tree="vido-id-request-satellite-title-kompsat"><i class="fas fa-search"></i></button>`);
//				const sat_ul = $('<ul>');
//				sat_li.append(sat_span);
//				sat_li.append(sat_btn);
//				sat_li.append(sat_ul);
//				for (const folder in kompsat.map) {
//					const fileList = kompsat.map[folder];
//					const folder_li = $(`<li class="js-tree-parent-3" value="${folder}" id="vido-id-request-satellite-title-kompsat-${folder}">`);
//					const folder_span = $('<span>').text(folder);
//					const folder_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-kompsat-image-layer" data-tree="vido-id-request-satellite-title-kompsat-${folder}"><i class="fas fa-search"></i></button>`);
//					const folder_ul = $('<ul>');
//					folder_li.append(folder_span);
//					folder_li.append(folder_btn);
//					folder_li.append(folder_ul);
//					for (const elem of fileList) {
//						const li = $(`<li class="js-search-result-kompsat js-tree-child-3" value="${elem.index}" id="vido-id-request-kompsat-${elem.index}" title="${elem.elem.fullFileCoursNm}">`);
//						const span = $('<span>').text(elem.elem.vidoNm);
//						const btn_kompsat = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-kompsat-image-layer" value="${elem.elem.vidoNm}" data-tree="vido-id-request-kompsat-${elem.index}"><i class="fas fa-search"></i></button>`);
//						if (!elem.elem.thumbnail) {
//							btn_kompsat.attr("disabled", true);
//						}
//						folder_ul.append(li.append(span).append(btn_kompsat));
//					}
//					sat_ul.append(folder_li);
//				}
//				satellite_ul.append(sat_li);
//			}
//
//			//기존 데이터 - 위성영상(sentinel) 노드 추가
//			if (sentinel) {
//				const sat_li = $(`<li class="js-tree-parent-2" value="sentinel" id="vido-id-request-satellite-title-sentinel">`);
//				const sat_span = $('<span>').text("Sentinel");
//				const sat_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-sentinel-image-layer" data-tree="vido-id-request-satellite-title-sentinel"><i class="fas fa-search"></i></button>`);
//				const sat_ul = $('<ul>');
//				sat_li.append(sat_span);
//				sat_li.append(sat_btn);
//				sat_li.append(sat_ul);
//				for (const folder in sentinel.map) {
//					const fileList = sentinel.map[folder];
//					const folder_li = $(`<li class="js-tree-parent-3" value="${folder}" id="vido-id-request-satellite-title-sentinel-${folder}">`);
//					const folder_span = $('<span>').text(folder);
//					const folder_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-sentinel-image-layer" data-tree="vido-id-request-satellite-title-sentinel-${folder}"><i class="fas fa-search"></i></button>`);
//					const folder_ul = $('<ul>');
//					folder_li.append(folder_span);
//					folder_li.append(folder_btn);
//					folder_li.append(folder_ul);
//					for (const elem of fileList) {
//						const li = $(`<li class="js-search-result-sentinel js-tree-child-3" value="${elem.index}" id="vido-id-request-sentinel-${elem.index}" title="${elem.elem.fullFileCoursNm}">`);
//						const span = $('<span>').text(elem.elem.vidoNm);
//						const btn_sentinel = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-sentinel-image-layer" value="${elem.elem.vidoNm}" data-tree="vido-id-request-sentinel-${elem.index}"><i class="fas fa-search"></i></button>`);
//						if (!elem.elem.thumbnail) {
//							btn_sentinel.attr("disabled", true);
//						}
//						folder_ul.append(li.append(span).append(btn_sentinel));
//					}
//					sat_ul.append(folder_li);
//				}
//				satellite_ul.append(sat_li);
//			}
//
//			//기존 데이터 - 위성영상(cas) 노드 추가
//			if (cas) {
//				const sat_li = $(`<li class="js-tree-parent-2" value="cas" id="vido-id-request-satellite-title-cas">`);
//				const sat_span = $('<span>').text("Cas");
//				const sat_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-cas-image-layer" data-tree="vido-id-request-satellite-title-cas"><i class="fas fa-search"></i></button>`);
//				const sat_ul = $('<ul>');
//				sat_li.append(sat_span);
//				sat_li.append(sat_btn);
//				sat_li.append(sat_ul);
//				for (const folder in cas.map) {
//					const fileList = cas.map[folder];
//					const folder_li = $(`<li class="js-tree-parent-3" value="${folder}" id="vido-id-request-satellite-title-cas-${folder}">`);
//					const folder_span = $('<span>').text(folder);
//					const folder_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-cas-image-layer" data-tree="vido-id-request-satellite-title-cas-${folder}"><i class="fas fa-search"></i></button>`);
//					const folder_ul = $('<ul>');
//					folder_li.append(folder_span);
//					folder_li.append(folder_btn);
//					folder_li.append(folder_ul);
//					for (const elem of fileList) {
//						const li = $(`<li class="js-search-result-cas js-tree-child-3" value="${elem.index}" id="vido-id-request-cas-${elem.index}" title="${elem.elem.fullFileCoursNm}">`);
//						const span = $('<span>').text(elem.elem.vidoNm)
//						const btn_cas = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-cas-image-layer" value="${elem.elem.vidoNm}" data-tree="vido-id-request-cas-${elem.index}"><i class="fas fa-search"></i></button>`);
//						if (!elem.elem.thumbnail) {
//							btn_cas.attr("disabled", true);
//						}
//						folder_ul.append(li.append(span).append(btn_cas));
//					}
//					sat_ul.append(folder_li);
//				}
//				satellite_ul.append(sat_li);
//			}

			//기존 데이터 - 위성영상(landsat) 노드 추가
			if (landsat) {
				const sat_li = $(`<li class="js-tree-parent-2" value="landsat" id="vido-id-request-satellite-title-landsat">`);
				const sat_span = $('<span>').text("Landsat");
				const sat_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-landsat-image-layer" data-tree="vido-id-request-satellite-title-landsat"><i class="fas fa-search"></i></button>`);
				const sat_ul = $('<ul>');
				sat_li.append(sat_span);
				sat_li.append(sat_btn);
				sat_li.append(sat_ul);
				satellite_ul.append(sat_li);

				for (const date in landsat.map) {
					const date_li = $(`<li class="js-tree-parent-3" value="${date}" id="vido-id-request-satellite-title-landsat-${date}">`);
					const date_span = $('<span>').text(date);
					const date_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-landsat-image-layer" data-tree="vido-id-request-satellite-title-landsat-${date}"><i class="fas fa-search"></i></button>`);
					const date_ul = $('<ul>');
					date_li.append(date_span);
					date_li.append(date_btn);
					date_li.append(date_ul);
					sat_ul.append(date_li);

					for (const folder in landsat.map[date]) {
						const fileList = landsat.map[date][folder];
						const folder_li = $(`<li class="js-tree-parent-4" value="${folder}" id="vido-id-request-satellite-title-landsat-${date}-${folder}">`);
						const folder_span = $('<span>').text(folder);
						const folder_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-landsat-image-layer" data-tree="vido-id-request-satellite-title-landsat-${date}-${folder}"><i class="fas fa-search"></i></button>`);
						const folder_ul = $('<ul>');
						folder_li.append(folder_span);
						folder_li.append(folder_btn);
						folder_li.append(folder_ul);
						date_ul.append(folder_li);

						for (const elem of fileList) {
							const li = $(`<li class="js-search-result-landsat js-tree-child-3" value="landsat-${elem.index}" id="vido-id-request-landsat-${date}-${folder}-${elem.index}"  title="${elem.elem.fullFileCoursNm}">`);
							const span = $('<span>').text(elem.elem.vidoNm);
							const btn_landsat = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-landsat-image-layer" value="${elem.elem.vidoNm}" data-tree="vido-id-request-landsat-${date}-${folder}-${elem.index}"><i class="fas fa-search"></i></button>`);
							if (!elem.elem.thumbnail) {
								btn_landsat.attr("disabled", true);
							}
							folder_ul.append(li.append(span).append(btn_landsat));
						}
					}
				}
			}

			//기존 데이터 - 위성영상(kompsat) 노드 추가
			if (kompsat) {
				const sat_li = $(`<li class="js-tree-parent-2" value="kompsat" id="vido-id-request-satellite-title-kompsat">`);
				const sat_span = $('<span>').text("Kompsat");
				const sat_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-kompsat-image-layer" data-tree="vido-id-request-satellite-title-kompsat"><i class="fas fa-search"></i></button>`);
				const sat_ul = $('<ul>');
				sat_li.append(sat_span);
				sat_li.append(sat_btn);
				sat_li.append(sat_ul);
				satellite_ul.append(sat_li);

				for (const date in kompsat.map) {

					const date_li = $(`<li class="js-tree-parent-3" value="${date}" id="vido-id-request-satellite-title-kompsat-${date}">`);
					const date_span = $('<span>').text(date);
					const date_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-kompsat-image-layer" data-tree="vido-id-request-satellite-title-kompsat-${date}"><i class="fas fa-search"></i></button>`);
					const date_ul = $('<ul>');
					date_li.append(date_span);
					date_li.append(date_btn);
					date_li.append(date_ul);
					sat_ul.append(date_li);

					for (const folder in kompsat.map[date]) {
						const fileList = kompsat.map[date][folder];
						const folder_li = $(`<li class="js-tree-parent-4" value="${folder}" id="vido-id-request-satellite-title-kompsat-${date}-${folder}">`);
						const folder_span = $('<span>').text(folder);
						const folder_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-kompsat-image-layer" data-tree="vido-id-request-satellite-title-kompsat-${date}-${folder}"><i class="fas fa-search"></i></button>`);
						const folder_ul = $('<ul>');
						folder_li.append(folder_span);
						folder_li.append(folder_btn);
						folder_li.append(folder_ul);
						date_ul.append(folder_li);

						for (const elem of fileList) {
							const li = $(`<li class="js-search-result-kompsat js-tree-child-3" value="kompsat-${elem.index}" id="vido-id-request-kompsat-${date}-${folder}-${elem.index}"  title="${elem.elem.fullFileCoursNm}">`);
							const span = $('<span>').text(elem.elem.vidoNm);
							const btn_kompsat = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-kompsat-image-layer" value="${elem.elem.vidoNm}" data-tree="vido-id-request-kompsat-${date}-${folder}-${elem.index}"><i class="fas fa-search"></i></button>`);
							if (!elem.elem.thumbnail) {
								btn_kompsat.attr("disabled", true);
							}
							folder_ul.append(li.append(span).append(btn_kompsat));
						}
					}
				}
				//				for (const folder in kompsat.map) {
				//					const fileList = kompsat.map[folder];
				//					const folder_li = $(`<li class="js-tree-parent-3" value="${folder}" id="vido-id-request-satellite-title-kompsat-${folder}">`);
				//					const folder_span = $('<span>').text(folder);
				//					const folder_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-kompsat-image-layer" data-tree="vido-id-request-satellite-title-kompsat-${folder}"><i class="fas fa-search"></i></button>`);
				//					const folder_ul = $('<ul>');
				//					folder_li.append(folder_span);
				//					folder_li.append(folder_btn);
				//					folder_li.append(folder_ul);
				//					for (const elem of fileList) {
				//						const li = $(`<li class="js-search-result-kompsat js-tree-child-3" value="${elem.index}" id="vido-id-request-kompsat-${elem.index}"  title="${elem.elem.fullFileCoursNm}">`);
				//						const span = $('<span>').text(elem.elem.vidoNm);
				//						const btn_kompsat = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-kompsat-image-layer" value="${elem.elem.vidoNm}" data-tree="vido-id-request-kompsat-${elem.index}"><i class="fas fa-search"></i></button>`);
				//						if (!elem.elem.thumbnail) {
				//							btn_kompsat.attr("disabled", true);
				//						}
				//						folder_ul.append(li.append(span).append(btn_kompsat));
				//					}
				//					sat_ul.append(folder_li);
				//				}

			}

			//기존 데이터 - 위성영상(sentinel) 노드 추가
			if (sentinel) {
				const sat_li = $(`<li class="js-tree-parent-2" value="sentinel" id="vido-id-request-satellite-title-sentinel">`);
				const sat_span = $('<span>').text("Sentinel");
				const sat_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-sentinel-image-layer" data-tree="vido-id-request-satellite-title-sentinel"><i class="fas fa-search"></i></button>`);
				const sat_ul = $('<ul>');
				sat_li.append(sat_span);
				sat_li.append(sat_btn);
				sat_li.append(sat_ul);
				satellite_ul.append(sat_li);

				for (const date in sentinel.map) {

					const date_li = $(`<li class="js-tree-parent-3" value="${date}" id="vido-id-request-satellite-title-sentinel-${date}">`);
					const date_span = $('<span>').text(date);
					const date_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-sentinel-image-layer" data-tree="vido-id-request-satellite-title-sentinel-${date}"><i class="fas fa-search"></i></button>`);
					const date_ul = $('<ul>');
					date_li.append(date_span);
					date_li.append(date_btn);
					date_li.append(date_ul);
					sat_ul.append(date_li);

					for (const folder in sentinel.map[date]) {
						const fileList = sentinel.map[date][folder];

						const folder_li = $(`<li class="js-tree-parent-4" value="${folder}" id="vido-id-request-satellite-title-sentinel-${date}-${folder}">`);
						const folder_span = $('<span>').text(folder);
						const folder_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-sentinel-image-layer" data-tree="vido-id-request-satellite-title-sentinel-${date}-${folder}"><i class="fas fa-search"></i></button>`);
						const folder_ul = $('<ul>');
						folder_li.append(folder_span);
						folder_li.append(folder_btn);
						folder_li.append(folder_ul);
						date_ul.append(folder_li);

						for (const elem of fileList) {
							const li = $(`<li class="js-search-result-sentinel js-tree-child-3" value="sentinel-${elem.index}" id="vido-id-request-sentinel-${date}-${folder}-${elem.index}"  title="${elem.elem.fullFileCoursNm}">`);
							const span = $('<span>').text(elem.elem.vidoNm);
							const btn_sentinel = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-sentinel-image-layer" value="${elem.elem.vidoNm}" data-tree="vido-id-request-sentinel-${date}-${folder}-${elem.index}"><i class="fas fa-search"></i></button>`);
							if (!elem.elem.thumbnail) {
								btn_sentinel.attr("disabled", true);
							}
							folder_ul.append(li.append(span).append(btn_sentinel));
						}
					}
				}
			}

			//기존 데이터 - 위성영상(cas) 노드 추가
			if (cas) {
				const sat_li = $(`<li class="js-tree-parent-2" value="cas" id="vido-id-request-satellite-title-cas">`);
				const sat_span = $('<span>').text("Cas");
				const sat_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-cas-image-layer" data-tree="vido-id-request-satellite-title-cas"><i class="fas fa-search"></i></button>`);
				const sat_ul = $('<ul>');
				sat_li.append(sat_span);
				sat_li.append(sat_btn);
				sat_li.append(sat_ul);
				satellite_ul.append(sat_li);

				for (const date in cas.map) {

					const date_li = $(`<li class="js-tree-parent-3" value="${date}" id="vido-id-request-satellite-title-cas-${date}">`);
					const date_span = $('<span>').text(date);
					const date_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-cas-image-layer" data-tree="vido-id-request-satellite-title-cas-${date}"><i class="fas fa-search"></i></button>`);
					const date_ul = $('<ul>');
					date_li.append(date_span);
					date_li.append(date_btn);
					date_li.append(date_ul);
					sat_ul.append(date_li);

					for (const folder in cas.map[date]) {
						const fileList = cas.map[date][folder];
						const folder_li = $(`<li class="js-tree-parent-4" value="${folder}" id="vido-id-request-satellite-title-cas-${date}-${folder}">`);
						const folder_span = $('<span>').text(folder);
						const folder_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-cas-image-layer" data-tree="vido-id-request-satellite-title-cas-${date}-${folder}"><i class="fas fa-search"></i></button>`);
						const folder_ul = $('<ul>');
						folder_li.append(folder_span);
						folder_li.append(folder_btn);
						folder_li.append(folder_ul);
						date_ul.append(folder_li);

						for (const elem of fileList) {
							const li = $(`<li class="js-search-result-cas js-tree-child-3" value="cas-${elem.index}" id="vido-id-request-cas-${date}-${folder}-${elem.index}" title="${elem.elem.fullFileCoursNm}">`);
							const span = $('<span>').text(elem.elem.vidoNm)
							const btn_cas = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-cas-image-layer" value="${elem.elem.vidoNm}" data-tree="vido-id-request-cas-${date}-${folder}-${elem.index}"><i class="fas fa-search"></i></button>`);
							if (!elem.elem.thumbnail) {
								btn_cas.attr("disabled", true);
							}
							folder_ul.append(li.append(span).append(btn_cas));
						}
					}
				}
			}

			// 기존 데이터 - DEM 노드 추가
			//			for (const year in dem.map) {
			//				const fileList = dem.map[year];
			//				// 년도 폴더 생성
			//				const year_li = $(`<li class="js-tree-parent-2" value="${year}" id="vido-id-request-dem-title-${year}">`);
			//				const year_span = $('<span>').text(year);
			//				const year_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-dem-layer" data-tree="vido-id-request-dem-title-${year}"><i class="fas fa-search"></i></button>`);
			//				const year_ul = $('<ul>');
			//				year_li.append(year_span);
			//				year_li.append(year_btn);
			//				year_li.append(year_ul);
			//				for (const elem of fileList) {
			//					const li = $(`<li class="js-search-result-dem js-tree-child-2" value="${elem.index}" id="vido-id-request-dem-${elem.index}">`);
			//					const span = $('<span>').text(elem.elem.vidoNm);
			//					const btn_dem = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-dem-layer" value="${elem.elem.vidoNm}" data-tree="vido-id-request-dem-${elem.index}"><i class="fas fa-search"></i></button>`);
			//					if (!elem.thumbnail) {
			//						btn_dem.attr("disabled", true);
			//					}
			//					year_ul.append(li.append(span).append(btn_dem));
			//				}
			//				dem_ul.append(year_li);
			//			}
			for (const year in dem.map) {
				const year_li = $(`<li class="js-tree-parent-2" value="${year}" id="vido-id-dem-title-${year}">`);
				const year_span = $('<span>').text(year);
				const year_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-dem-layer" data-tree="vido-id-dem-title-${year}"><i class="fas fa-search"></i></button>`);
				const year_ul = $('<ul>');
				year_li.append(year_span);
				year_li.append(year_btn);
				year_li.append(year_ul);
				for (const dpi in year) {
					//					const fileList = ortho.map[year][dpi];
					const fileList = dem.map[year][Object.keys(dem.map[year])[dpi]];
					if (!fileList) {
						break;
					}
					const dpi_li = $(`<li class="js-tree-parent-3" value="${dpi}" id="vido-id-dem-title-${year}-${dpi}">`);
					// const dpi_span = $('<span>').text(dpi);
					const dpi_span = $('<span>').text(Object.keys(dem.map[year])[dpi]);
					const dpi_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-dem-layer" data-tree="vido-id-dem-title-${year}-${dpi}"><i class="fas fa-search"></i></button>`);
					const dpi_ul = $('<ul>');
					dpi_li.append(dpi_span);
					dpi_li.append(dpi_btn);
					dpi_li.append(dpi_ul);
					for (const elem of fileList) {
						const li = $(`<li class="js-search-result-dem js-tree-child-3" value="${elem.index}" id="vido-id-dem-${year}-${dpi}-${elem.index}" title="${elem.elem.fullFileCoursNm}">`);
						const span = $('<span>').text(elem.elem.vidoNm);
						const btn_dem = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-dem-layer" value="${elem.elem.vidoNm}" data-tree="vido-id-dem-${year}-${dpi}-${elem.index}"><i class="fas fa-search"></i></button>`);
						if (!elem.elem.thumbnail) {
							btn_ortho.attr("disabled", true);
						}
						dpi_ul.append(li.append(span).append(btn_dem));
					}
					year_ul.append(dpi_li);
				}
				dem_ul.append(year_li);
			}

			// 기존 데이터 - 통계지도 노드 추가 (image / file)
			for (let i = 0; i < graphics.length; i++) {
				const li = $(`<li class="js-search-result-graphics js-tree-child-1" value="${graphics[i].vidoNm}" id="vido-id-request-graphics-${i}" title="${graphics[i].vidoNm}">`);
				const span = $('<span>').text(graphics[i].vidoNmKr);
				const btn_graphics = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-graphics-layer" value="${graphics[i].vidoNm}" data-tree="vido-id-request-graphics-${i}"><i class="fas fa-search"></i></button>`);
				if (!graphics[i].thumbnail) {
					btn_graphics.attr("disabled", true);
				}
				graphics_ul.append(li.append(span).append(btn_graphics));
			}

			// ## 긴급 영상 ##
			const emerData = $('<div class="in-panel-title">').text('긴급 영상');
			outerPanel.append(emerData);

			// 긴급 영상(resultList2) 영역 생성
			const resultList2 = $('<div id="resultList2_request">');
			outerPanel.append(resultList2);
			const resultList2_div = $('<div class="result-list-input">');
			resultList2.append(resultList2_div);

			// 긴급 영상 - 지진
			const resultList2_earthquake = $('<div id="resultList2_request_earthquake_tree">');
			const earthquake_tree = $('<ul>');
			const earthquakeTitle = $('<li class="js-control-earthquake js-tree-parent-1" id="vido-id-request-earthquake-title">');
			const earthquake_span = $('<span>').text(`지진(${earthquake.length}건)`);
			const earthquake_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-earthquake-image-layer" data-tree="vido-id-request-earthquake-title"><i class="fas fa-search"></i></button>`);
			const earthquake_ul = $('<ul>');
			resultList2_div.append(resultList2_earthquake);
			resultList2_earthquake.append(earthquake_tree);
			earthquake_tree.append(earthquakeTitle);
			earthquakeTitle.append(earthquake_span);
			earthquakeTitle.append(earthquake_btn);
			earthquakeTitle.append(earthquake_ul);

			// 긴급 영상 - 산불
			const resultList2_forestFire = $('<div id="resultList2_request_forestFire_tree">');
			const forestFire_tree = $('<ul>');
			const forestFireTitle = $('<li class="js-control-forestFire js-tree-parent-1" id="vido-id-request-forestfire-title">');
			const forestFire_span = $('<span>').text(`산불(${forestFire.length}건)`);
			const forestFire_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-forestfire-image-layer" data-tree="vido-id-request-forestfire-title"><i class="fas fa-search"></i></button>`);
			const forestFire_ul = $('<ul>');
			resultList2_div.append(resultList2_forestFire);
			resultList2_forestFire.append(forestFire_tree);
			forestFire_tree.append(forestFireTitle);
			forestFireTitle.append(forestFire_span);
			forestFireTitle.append(forestFire_btn);
			forestFireTitle.append(forestFire_ul);

			// 긴급 영상 - 수해 
			const resultList2_floodTitle = $('<div id="resultList2_request_flood_tree">');
			const flood_tree = $('<ul>');
			const floodTitle = $('<li class="js-control-floodTitle js-tree-parent-1" id="vido-id-request-flood-title">');
			const flood_span = $('<span>').text(`수해(${flood.length}건)`);
			const flood_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-flood-image-layer" data-tree="vido-id-request-flood-title"><i class="fas fa-search"></i></button>`);
			const flood_ul = $('<ul>');
			resultList2_div.append(resultList2_floodTitle);
			resultList2_floodTitle.append(flood_tree);
			flood_tree.append(floodTitle);
			floodTitle.append(flood_span);
			floodTitle.append(flood_btn);
			floodTitle.append(flood_ul);

			// 긴급 영상 - 산사태 
			const resultList2_landslip = $('<div id="resultList2_request_landslip_tree">');
			const landslip_tree = $('<ul>');
			const landslipTitle = $('<li class="js-control-landslip js-tree-parent-1" id="vido-id-request-landslide-title">');
			const landslip_span = $('<span>').text(`산사태(${landslip.length}건)`);
			const landslip_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-landslide-image-layer" data-tree="vido-id-request-landslide-title"><i class="fas fa-search"></i></button>`);
			const landslip_ul = $('<ul>');
			resultList2_div.append(resultList2_landslip);
			resultList2_landslip.append(landslip_tree);
			landslip_tree.append(landslipTitle);
			landslipTitle.append(landslip_span);
			landslipTitle.append(landslip_btn);
			landslipTitle.append(landslip_ul);

			// 긴급 영상 - 해양재난 
			const resultList2_maritime = $('<div id="resultList2_request_maritime_tree">');
			const maritime_tree = $('<ul>');
			const maritimeTitle = $('<li class="js-control-maritime js-tree-parent-1" id="vido-id-request-maritime-title">');
			const maritime_span = $('<span>').text(`해양재난(${maritime.length}건)`);
			const maritime_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-maritime-image-layer" data-tree="vido-id-request-maritime-title"><i class="fas fa-search"></i></button>`);
			const maritime_ul = $('<ul>');
			resultList2_div.append(resultList2_maritime);
			resultList2_maritime.append(maritime_tree);
			maritime_tree.append(maritimeTitle);
			maritimeTitle.append(maritime_span);
			maritimeTitle.append(maritime_btn);
			maritimeTitle.append(maritime_ul);

			// 긴급 영상 - 적조
			const resultList2_redTide = $('<div id="resultList2_request_redTide_tree">');
			const redTide_tree = $('<ul>');
			const redTideTitle = $('<li class="js-control-redTide js-tree-parent-1" id="vido-id-request-redTide-title">');
			const redTide_span = $('<span>').text(`적조(${redTide.length}건)`);
			const redTide_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-redTide-image-layer" data-tree="vido-id-request-redTide-title"><i class="fas fa-search"></i></button>`);
			const redTide_ul = $('<ul>');
			resultList2_div.append(resultList2_redTide);
			resultList2_redTide.append(redTide_tree);
			redTide_tree.append(redTideTitle);
			redTideTitle.append(redTide_span);
			redTideTitle.append(redTide_btn);
			redTideTitle.append(redTide_ul);

			// 긴급 영상 - 지진 노드 추가
			for (let i = 0; i < earthquake.length; i++) {
				const li = $(`<li class="js-search-result-earthquake js-tree-child-1" value="${i}" id="vido-id-request-earthquake-${i}" title="${earthquake[i].fullFileCoursNm}">`);
				const span = $('<span>').text(earthquake[i].vidoNm);
				const btn_earthquake = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-earthquake-image-layer" value="${earthquake[i].vidoNm}" data-tree="vido-id-request-earthquake-${i}"><i class="fas fa-search"></i></button>`);
				if (!earthquake[i].thumbnail) {
					btn_earthquake.attr("disabled", true);
				}
				earthquake_ul.append(li.append(span).append(btn_earthquake));
			}

			// 긴급 영상 - 산불 노드 추가
			for (let i = 0; i < forestFire.length; i++) {
				const li = $(`<li class="js-search-result-forestFire js-tree-child-1" value="${i}" id="vido-id-request-forestFire-${i}" title="${forestFire[i].fullFileCoursNm}">`);
				const span = $('<span>').text(forestFire[i].vidoNm);
				const btn_forestFire = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-forestfire-image-layer" value="${forestFire[i].vidoNm}" data-tree="vido-id-request-forestFire-${i}"><i class="fas fa-search"></i></button>`);
				if (!forestFire[i].thumbnail) {
					btn_forestFire.attr("disabled", true);
				}
				forestFire_ul.append(li.append(span).append(btn_forestFire));
			}

			// 긴급 영상 - 수해 노드 추가
			for (let i = 0; i < flood.length; i++) {
				const li = $(`<li class="js-search-result-flood js-tree-child-1" value="${i}" id="vido-id-request-flood-${i}" title="${flood[i].fullFileCoursNm}">`);
				const span = $('<span>').text(flood[i].vidoNm);
				const btn_flood = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-flood-image-layer" value="${flood[i].vidoNm}" data-tree="vido-id-request-flood-${i}"><i class="fas fa-search"></i></button>`);
				if (!flood[i].thumbnail) {
					btn_flood.attr("disabled", true);
				}
				flood_ul.append(li.append(span).append(btn_flood));
			}

			// 긴급 영상 - 산사태 노드 추가
			for (let i = 0; i < landslip.length; i++) {
				const li = $(`<li class="js-search-result-landslip js-tree-child-1" value="${i}" id="vido-id-request-landslide-${i}" title="${landslip[i].fullFileCoursNm}">`);
				const span = $('<span>').text(landslip[i].vidoNm);
				const btn_landslip = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-landslide-image-layer" value="${landslip[i].vidoNm}" data-tree="vido-id-request-landslide-${i}"><i class="fas fa-search"></i></button>`);
				if (!landslip[i].thumbnail) {
					btn_landslip.attr("disabled", true);
				}
				landslip_ul.append(li.append(span).append(btn_landslip));
			}

			// 긴급 영상 - 해양재난 노드 추가
			for (let i = 0; i < maritime.length; i++) {
				const li = $(`<li class="js-search-result-maritime js-tree-child-1" value="${i}" id="vido-id-request-maritime-${i}" title="${maritime[i].fullFileCoursNm}">`);
				const span = $('<span>').text(maritime[i].vidoNm);
				const btn_maritime = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-maritime-image-layer" value="${maritime[i].vidoNm}" data-tree="vido-id-request-maritime-${i}"><i class="fas fa-search"></i></button>`);
				if (!maritime[i].thumbnail) {
					btn_maritime.attr("disabled", true);
				}
				maritime_ul.append(li.append(span).append(btn_maritime));
			}

			// 긴급 영상 - 적조 노드 추가
			for (let i = 0; i < redTide.length; i++) {
				const li = $(`<li class="js-search-result-redTide js-tree-child-1" value="${i}" id="vido-id-request-redTide-${i}" title="${redTide[i].fullFileCoursNm}">`);
				const span = $('<span>').text(redTide[i].vidoNm);
				const btn_redTide = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-redTide-image-layer" value="${redTide[i].vidoNm}" data-tree="vido-id-request-redTide-${i}"><i class="fas fa-search"></i></button>`);
				if (!redTide[i].thumbnail) {
					btn_redTide.attr("disabled", true);
				}
				redTide_ul.append(li.append(span).append(btn_redTide));
			}

			// ## 분석결과 데이터 ##
			const classificationData = $('<div class="in-panel-title">').text('분석결과 데이터');
			outerPanel.append(classificationData);

			// 분석결과 데이터(resultList3) 영역 생성
			const resultList3 = $('<div id="resultList3_request">');
			outerPanel.append(resultList3);
			const resultList3_div = $('<div class="result-list-input">');
			resultList3.append(resultList3_div);

			// 분석결과 데이터 - 객체추출 벡터
			const resultList3_vectorAnalObj = $('<div id="resultList3_request_vectorAnalObj_tree">');
			const vectorAnalObj_tree = $('<ul>');
			const vectorAnalObjTitle = $('<li class="js-control-vectorAnalObj js-tree-parent-1" id="vido-id-request-vectorAnalObj-title">');
			const vectorAnalObj_span = $('<span>').text(`객체추출 벡터 데이터(${vectorAnalObj.length}건)`);
			const vectorAnalObj_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-shp-obj-layer" data-tree="vido-id-request-vectorAnalObj-title"><i class="fas fa-search"></i></button>`);
			const vectorAnalObj_ul = $('<ul>');
			resultList3_div.append(resultList3_vectorAnalObj);
			resultList3_vectorAnalObj.append(vectorAnalObj_tree);
			vectorAnalObj_tree.append(vectorAnalObjTitle);
			vectorAnalObjTitle.append(vectorAnalObj_span);
			vectorAnalObjTitle.append(vectorAnalObj_btn);
			vectorAnalObjTitle.append(vectorAnalObj_ul);

			// 분석결과 데이터 - 객체추출 영상
			const resultList3_rasterAnalObj = $('<div id="resultList3_request_rasterAnalObj_tree">');
			const rasterAnalObj_tree = $('<ul>');
			const rasterAnalObjTitle = $('<li class="js-control-rasterAnalObj js-tree-parent-1" id="vido-id-request-rasterAnalObj-title">');
			const rasterAnalObj_span = $('<span>').text(`객체추출 영상 데이터(${rasterAnalObj.length}건)`);
			const rasterAnalObj_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-raster-anal-obj-layer" data-tree="vido-id-request-rasterAnalObj-title"><i class="fas fa-search"></i></button>`);
			const rasterAnalObj_ul = $('<ul>');
			resultList3_div.append(resultList3_rasterAnalObj);
			resultList3_rasterAnalObj.append(rasterAnalObj_tree);
			rasterAnalObj_tree.append(rasterAnalObjTitle);
			rasterAnalObjTitle.append(rasterAnalObj_span);
			rasterAnalObjTitle.append(rasterAnalObj_btn);
			rasterAnalObjTitle.append(rasterAnalObj_ul);

			// 분석결과 데이터 - 변화탐지 벡터
			const resultList3_vectorAnalChg = $('<div id="resultList3_request_vectorAnalChg_tree">');
			const vectorAnalChg_tree = $('<ul>');
			const vectorAnalChgTitle = $('<li class="js-control-vectorAnalChg js-tree-parent-1" id="vido-id-request-vectorAnalChg-title">');
			const vectorAnalChg_span = $('<span>').text(`변화탐지 벡터 데이터(${vectorAnalChg.length}건)`)
			const vectorAnalChg_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-shp-chg-layer" data-tree="vido-id-request-vectorAnalChg-title"><i class="fas fa-search"></i></button>`);
			const vectorAnalChg_ul = $('<ul>');
			resultList3_div.append(resultList3_vectorAnalChg);
			resultList3_vectorAnalChg.append(vectorAnalChg_tree);
			vectorAnalChg_tree.append(vectorAnalChgTitle);
			vectorAnalChgTitle.append(vectorAnalChg_span);
			vectorAnalChgTitle.append(vectorAnalChg_btn);
			vectorAnalChgTitle.append(vectorAnalChg_ul);

			// 분석결과 데이터 - 변화탐지 영상
			const resultList3_raterAnalChg = $('<div id="resultList3_request_rasterAnalChg_tree">');
			const rasterAnalChg_tree = $('<ul>');
			const rasterAnalChgTitle = $('<li class="js-control-rasterAnalChg js-tree-parent-1" id="vido-id-request-rasterAnalChg-title">');
			const rasterAnalChg_span = $('<span>').text(`변화탐지 영상 데이터(${rasterAnalChg.length}건)`)
			const rasterAnalChg_btn = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-raster-anal-chg-layer" data-tree="vido-id-request-rasterAnalChg-title"><i class="fas fa-search"></i></button>`);
			const rasterAnalChg_ul = $('<ul>');
			resultList3_div.append(resultList3_raterAnalChg);
			resultList3_raterAnalChg.append(rasterAnalChg_tree);
			rasterAnalChg_tree.append(rasterAnalChgTitle);
			rasterAnalChgTitle.append(rasterAnalChg_span);
			rasterAnalChgTitle.append(rasterAnalChg_btn);
			rasterAnalChgTitle.append(rasterAnalChg_ul);

			// 분석결과 데이터 - 객체추출 벡터 노드 추가
			for (let i = 0; i < vectorAnalObj.length; i++) {
				const li = $(`<li class="js-search-result-vectorAnalObj js-tree-child-1" value="${i}" id="vido-id-request-vectorAnalObj-${i}" title="${vectorAnalObj[i].fullFileCoursNm}">`);
				const span = $('<span>').text(vectorAnalObj[i].vidoNm);
				const btn_vectorAnalObj = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-shp-obj-layer" value="${vectorAnalObj[i].vidoNm}" data-tree="vido-id-request-vectorAnalObj-${i}"><i class="fas fa-search"></i></button>`);
				if (!vectorAnalObj[i].thumbnail) {
					btn_vectorAnalObj.attr("disabled", true);
				}
				vectorAnalObj_ul.append(li.append(span).append(btn_vectorAnalObj));
			}

			// 분석결과 데이터 - 객체추출 영상 노드 추가
			for (let i = 0; i < rasterAnalObj.length; i++) {
				const li = $(`<li class="js-search-result-rasterAnalObj js-tree-child-1" value="${i}" id="vido-id-request-rasterAnalObj-${i}" title="${rasterAnalObj[i].fullFileCoursNm}">`);
				const span = $('<span>').text(rasterAnalObj[i].vidoNm);
				const btn_rasterAnalObj = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-raster-anal-obj-layer" value="${rasterAnalObj[i].vidoNm}" data-tree="vido-id-request-rasterAnalObj-${i}"><i class="fas fa-search"></i></button>`);
				if (!rasterAnalObj[i].thumbnail) {
					btn_rasterAnalObj.attr("disabled", true);
				}
				rasterAnalObj_ul.append(li.append(span).append(btn_rasterAnalObj));
			}

			// 분석결과 데이터 - 변화탐지 벡터 노드 추가
			for (let i = 0; i < vectorAnalChg.length; i++) {
				const li = $(`<li class="js-search-result-vectorAnalChg js-tree-child-1" value="${i}" id="vido-id-request-vectorAnalChg-${i}" title="${vectorAnalChg[i].fullFileCoursNm}">`);
				const span = $('<span>').text(vectorAnalChg[i].vidoNm);
				const btn_vectorAnalChg = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-shp-chg-layer" value="${vectorAnalChg[i].vidoNm}" data-tree="vido-id-request-vectorAnalChg-${i}"><i class="fas fa-search"></i></button>`);
				if (!vectorAnalChg[i].thumbnail) {
					btn_vectorAnalChg.attr("disabled", true);
				}
				vectorAnalChg_ul.append(li.append(span).append(btn_vectorAnalChg));
			}

			// 분석결과 데이터 - 변화탐지 영상 노드 추가
			for (let i = 0; i < rasterAnalChg.length; i++) {
				const li = $(`<li class="js-search-result-rasterAnalChg js-tree-child-1" value="${i}" id="vido-id-request-rasterAnalChg-${i}" title="${rasterAnalChg[i].fullFileCoursNm}">`);
				const span = $('<span>').text(rasterAnalChg[i].vidoNm);
				const btn_rasterAnalChg = $(`<button type="button" class="btn btn-xs f-right ui-btn-preview-raster-anal-chg-layer" value="${rasterAnalChg[i].vidoNm}" data-tree="vido-id-request-rasterAnalChg-${i}"><i class="fas fa-search"></i></button>`);
				if (!rasterAnalChg[i].thumbnail) {
					btn_rasterAnalChg.attr("disabled", true);
				}
				rasterAnalChg_ul.append(li.append(span).append(btn_rasterAnalChg));
			}

			// jsTree 적용
			for (const data of ['resultList1_request_digital', 'resultList1_request_aerial', 'resultList1_request_ortho', 'resultList1_request_satellite', 'resultList1_request_dem', 'resultList1_request_graphics',
				'resultList2_request_earthquake', 'resultList2_request_forestFire', 'resultList2_request_flood', 'resultList2_request_landslip', 'resultList2_request_maritime', 'resultList2_request_redTide',
				'resultList3_request_vectorAnalObj', 'resultList3_request_rasterAnalObj', 'resultList3_request_vectorAnalChg', 'resultList3_request_rasterAnalChg']) {
				$(`#${data}_tree`).jstree({
					"checkbox": { "keep_selected_style": false, "three_state": true, "cascade": 'up', "tie_selection": false, "whole_node": false },
					"plugins": ["checkbox"],
					"core": { "themes": { "icons": false }, "dblclick_toggle": false }
				});
			}

		},
		searchEmergencyManagement(disasterId) {
			const formData = new FormData();
			formData.append('msfrtnInfoColctRequstId', disasterId ? disasterId : '');

			const url = "cmsc003searchDataset.do";

			const xhr = new XMLHttpRequest();
			xhr.open("POST", url);

			xhr.onreadystatechange = function() {
				if (xhr.readyState === 4) {
					let returnJson = xhr.response;
					if (typeof returnJson === 'string') {
						returnJson = JSON.parse(returnJson);
					}
					if (typeof returnJson === 'string') {
						returnJson = JSON.parse(returnJson);
					}
					//					const returnJson = JSON.parse(xhr.response);
					console.log(returnJson);
					const dir = returnJson.datasetCoursNm;
					dir ? $('#storeDir').val(dir) : $('#storeDir').val('');

					const name = returnJson.datasetNm;
					name ? $('#storeDatasetName').val(name) : $('#storeDatasetName').val('');

					CMSC003.DOM.showRequestResultTree(returnJson);
				}
			};

			xhr.send(formData);
		},
		sendEmergencyManagement(disasterId, type) {
			// 데이터셋 id
			const datasetId = [];
			
			const dataMap = CMSC003.Storage.get('requestDataMap');
			
			const vector = $("#resultList1_request #resultList1_request_digital_tree").jstree().get_bottom_checked(true);
			const aerial = $("#resultList1_request #resultList1_request_aerial_tree").jstree().get_bottom_checked(true);
			const ortho = $("#resultList1_request #resultList1_request_ortho_tree").jstree().get_bottom_checked(true);
			const dem = $("#resultList1_request #resultList1_request_dem_tree").jstree().get_bottom_checked(true);
			const graphics = $("#resultList1_request #resultList1_request_graphics_tree").jstree().get_bottom_checked(true);
			const satellite = $("#resultList1_request #resultList1_request_satellite_tree").jstree().get_bottom_checked(true);
			const earthquake = $("#resultList2_request #resultList2_request_earthquake_tree").jstree().get_bottom_checked(true);
			const forestFire = $("#resultList2_request #resultList2_request_forestFire_tree").jstree().get_bottom_checked(true);
			const flood = $("#resultList2_request #resultList2_request_flood_tree").jstree().get_bottom_checked(true);
			const landslip = $("#resultList2_request #resultList2_request_landslip_tree").jstree().get_bottom_checked(true);
			const maritime = $("#resultList2_request #resultList2_request_maritime_tree").jstree().get_bottom_checked(true);
			const redTide = $("#resultList2_request #resultList2_request_redTide_tree").jstree().get_bottom_checked(true);
			const vectorAnalObj = $("#resultList3_request #resultList3_request_vectorAnalObj_tree").jstree().get_bottom_checked(true);
			const rasterAnalObj = $("#resultList3_request #resultList3_request_rasterAnalObj_tree").jstree().get_bottom_checked(true);
			const vectorAnalChg = $("#resultList3_request #resultList3_request_vectorAnalChg_tree").jstree().get_bottom_checked(true);
			const rasterAnalChg = $("#resultList3_request #resultList3_request_rasterAnalChg_tree").jstree().get_bottom_checked(true);
			
			console.log(dataMap);
			// 수치지도 데이터
			vector.forEach(function(item) {
				if (dataMap[item.li_attr.value]) {
					datasetId.push(dataMap[item.li_attr.value]['datasetId']);			
				}
			});
			
			// kompsat 데이터
			// landsat 데이터
			// sentinel 데이터
			// cas 데이터
			satellite.forEach(function(item) {
				if (dataMap[item.li_attr.value]) {
					datasetId.push(dataMap[item.li_attr.value]['datasetId']);			
				}
			});
			
			// 항공영상 데이터
			aerial.forEach(function(item) {
				const label = 'aerial-';
				if (dataMap[label+item.li_attr.value]) {
					datasetId.push(dataMap[label+item.li_attr.value]['datasetId']);			
				}
			});
			
			// 정사영상 데이터
			ortho.forEach(function(item) {
				const label = 'ortho-';
				if (dataMap[label+item.li_attr.value]) {
					datasetId.push(dataMap[label+item.li_attr.value]['datasetId']);			
				}
			});
			
			// 인구통계 데이터
			// 지가통계 데이터
			// 건물통계 데이터
			graphics.forEach(function(item) {
				if (dataMap[item.li_attr.value]) {
					datasetId.push(dataMap[item.li_attr.value]['datasetId']);				
				}
			});
			
			// dem 데이터
			dem.forEach(function(item) {
				const label = 'dem-';
				if (dataMap[label+item.li_attr.value]) {
					datasetId.push(dataMap[label+item.li_attr.value]['datasetId']);			
				}
			});
			
			// 긴급 데이터
			// 침수
			flood.forEach(function(item) {
				const label = 'flood-';
				if (dataMap[label+item.li_attr.value]) {
					datasetId.push(dataMap[label+item.li_attr.value]['datasetId']);			
				}
			});
			// 산사태
			landslip.forEach(function(item) {
				const label = 'landslide-';
				if (dataMap[label+item.li_attr.value]) {
					datasetId.push(dataMap[label+item.li_attr.value]['datasetId']);			
				}
			});
			// 산불
			forestFire.forEach(function(item) {
				const label = 'forestFire-';
				if (dataMap[label+item.li_attr.value]) {
					datasetId.push(dataMap[label+item.li_attr.value]['datasetId']);			
				}
			});
			// 지진
			earthquake.forEach(function(item) {
				const label = 'earthquake-';
				if (dataMap[label+item.li_attr.value]) {
					datasetId.push(dataMap[label+item.li_attr.value]['datasetId']);			
				}				
			});
			// 해양재난
			maritime.forEach(function(item) {
				const label = 'maritime-';
				if (dataMap[label+item.li_attr.value]) {
					datasetId.push(dataMap[label+item.li_attr.value]['datasetId']);			
				}
			});
			// 적조
			redTide.forEach(function(item) {
				const label = 'redTide-';
				if (dataMap[label+item.li_attr.value]) {
					datasetId.push(dataMap[label+item.li_attr.value]['datasetId']);			
				}
			});
			
			// 분석 벡터 객체추출
			vectorAnalObj.forEach(function(item) {
				const label = 'vectorAnalObj-';
				if (dataMap[label+item.li_attr.value]) {
					datasetId.push(dataMap[label+item.li_attr.value]['datasetId']);			
				}
			});
			
			// 분석 래스터 객체추출
			rasterAnalObj.forEach(function(item) {
				const label = 'rasterAnalObj-';
				if (dataMap[label+item.li_attr.value]) {
					datasetId.push(dataMap[label+item.li_attr.value]['datasetId']);			
				}
			});
			
			// 분석 벡터 변화탐지
			vectorAnalChg.forEach(function(item) {
				const label = 'vectorAnalChg-';
				if (dataMap[label+item.li_attr.value]) {
					datasetId.push(dataMap[label+item.li_attr.value]['datasetId']);			
				}
			});
			
			// 분석 래스터 변화탐지
			rasterAnalChg.forEach(function(item) {
				const label = 'rasterAnalChg-';
				if (dataMap[label+item.li_attr.value]) {
					datasetId.push(dataMap[label+item.li_attr.value]['datasetId']);			
				}
			});
			
			
			const formData = new FormData();
			formData.append('msfrtnInfoColctRequstId', disasterId ? disasterId : '');
			
			for(let i = 0; i < datasetId.length; i++) {
				formData.append('datasetId', datasetId[i]);
			}
			
			let url = "";
			if( type == 2){
				//rlsTy = 2 :긴급공간정보 전송 (대민)
				formData.append('rlsTy', '2' )
				url = "cmsc003sendDataset.do";
			}else{
				//rlsTy = 1 :국토영상공급시스템 전송(행정망)
				formData.append('rlsTy', '1' )
				url = "cmsc003sendDataset.do";
			}

			//const url = "cmsc003sendDataset.do";

			const xhr = new XMLHttpRequest();
			xhr.open("POST", url);

			xhr.onreadystatechange = function() {
				if (xhr.readyState === 4) {
//					const returnJson = JSON.parse(xhr.response);
//					console.log(returnJson);
					CMSC003.DOM.showErrorMsg('긴급공간정보 전송을 완료하였습니다.');
				}
			};

			xhr.send(formData);
		}
	}

})();

