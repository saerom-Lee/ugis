$(document).ajaxStart($.blockUI).ajaxStop($.unblockUI);
$.ajaxSetup({ error: function(jqXHR, textStatus, errorThrown) { alert(jqXHR.responseJSON.message); } });

$(document).ready(function() {

	ChangeDetctModule.init();
	ChangeResultModule.init();

	new DatePicker(document.getElementById('searchPotogrfBeginDt'));
	new DatePicker(document.getElementById('searchPotogrfEndDt'));

	$('a[data-toggle="tab"]').on('shown.bs.tab', function(e) {
		if (e.target.id === 'detctResult') {
			$('#resultSearch').trigger('click');
			$('#vectorSearch').trigger('click');
		}
	});

});

var CmmnModule = function() {

	return {

		getCmmnCode: function(params) {
			return new Promise(function(resolve, reject) {
				$.ajax({
					url: contextPath + '/getCmmnCode.do',
					type: 'GET',
					data: { groupCode: params.groupCode }
				}).done(function(res) {
					const result = res.map(item => { return { value: item.cmmnCode, text: item.codeNm } });
					resolve(result);
				});
			});
		},

		transform: function(x, y, sourceCrs, targetCrs) {
//			const point = new Proj4js.Point(x, y);
//			const source = new Proj4js.Proj(sourceCrs);
//			const target = new Proj4js.Proj(targetCrs);

			if (sourceCrs === 'EPSG:5179' && targetCrs === 'EPSG:5179') {
				return {
					x: x,
					y: y
				};
			}
			var transform = proj4(sourceCrs, 'EPSG:5179', [x, y]);
			return {
				x: transform[0],
				y: transform[1]
			};; 
//			return Proj4js.transform(source, target, point);
		},

		viewMap: function(id, filename, x1, y1, x2, y2) {
			let minx = x1 > x2 ? x2 : x1;
			let miny = y1 > y2 ? y2 : y1;
			let maxx = x1 > x2 ? x1 : x2;
			let maxy = y1 > y2 ? y1 : y2;

			ciscMap.viewImgLayer(id, filename, minx, miny, maxx, maxy, '');

		},

		viewVectorMap: function(id, fileName, allFileName) {
			console.log(id, fileName, allFileName);
			var url = "cmsc003shpThumbnail2.do";
			var info = {
				thumbnailFileCoursNm: allFileName
			};
			var xhr = new XMLHttpRequest();
			xhr.open("POST", url);

			xhr.setRequestHeader("Accept", "application/json");
			xhr.setRequestHeader("Content-Type", "application/json");

			xhr.onreadystatechange = function() {
				if (xhr.readyState === 4) {
					$.unblockUI();
					const shp = new CMSC003.Converter({
						url: CMSC003.Util.base64ToBlob(xhr.response, 'application/zip'),
						//							encoding: 'EUC-KR',
						encoding: 'ISO-8859-1'
					});
					shp.loadshp((geojson) => {
						ciscMap.viewShpLayer(geojson);
					});

				}
			};
			xhr.onerror = function() {
				$.unblockUI();
			}
			xhr.send(JSON.stringify(info));
			$.blockUI();
		}

	}

};