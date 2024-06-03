/**
 * 
 */

var ciscMap;
var landSatResult = [];
$(function() {

	//console.log("cisc_map.js START");

	ciscMap = {
		map: null,
		//imageLayer: null,
		select: null,
		init: function() {
			const _projCode = {
				'EPSG:4326': '+title=WGS 84 (long/lat) +proj=longlat +ellps=WGS84 +datum=WGS84 +units=degrees',
				'EPSG:3857': '+title=WGS 84 / Pseudo-Mercator +proj=merc +a=6378137 +b=6378137 +lat_ts=0.0 +lon_0=0.0 +x_0=0.0 +y_0=0 +k=1.0 +units=m +nadgrids=@null +no_defs',
				'EPSG:5179': '+proj=tmerc +lat_0=38 +lon_0=127.5 +k=0.9996 +x_0=1000000 +y_0=2000000 +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs',
				'EPSG:5174': '+proj=tmerc +lat_0=38 +lon_0=127.0028902777778 +k=1 +x_0=200000 +y_0=500000 +ellps=bessel +units=m +no_defs +towgs84=-115.80,474.99,674.11,1.16,-2.31,-1.63,6.43',
				'EPSG:5186': '+proj=tmerc +lat_0=38 +lon_0=127 +k=1 +x_0=200000 +y_0=600000 +ellps=GRS80 +units=m +no_defs',
				'EPSG:32652': '+proj=utm +zone=52 +ellps=WGS84 +datum=WGS84 +units=m +no_def',
				'EPSG:5187': '+proj=tmerc +lat_0=38 +lon_0=129 +k=1 +x_0=200000 +y_0=600000 +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs',
				'EPSG:5185': '+proj=tmerc +lat_0=38 +lon_0=125 +k=1 +x_0=200000 +y_0=600000 +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs'
			};

			const registProj4 = () => {
				Object.keys(_projCode).forEach(key => proj4.defs(key, _projCode[key]));
				ol.proj.proj4.register(proj4);
			};
			registProj4();

			//1. 맵생성.
			this.map = new ol.Map({
				target: 'map1',
				view: new ol.View({
					projection: new ol.proj.Projection({
						code: "EPSG:5179",
						units: 'm',
						axisOrientation: 'enu',
						extent: [-200000.0, -3015.4524155292, 3803015.45241553, 4000000.0]
					}),
					resolutions: [2088.96, 1044.48, 522.24, 261.12, 130.56, 65.28, 32.64,
						16.32, 8.16, 4.08, 2.04, 1.02, 0.51, 0.255],
					center: [960363.606552286, 1920034.9139856],
					zoom: 10,
				})
			});
			var baselyaer = baselayer();
			this.map.addLayer(baselyaer);
			//this.map.addLayer(new ol.layer.Tile({
			//	source: new ol.source.OSM()
			//}));
			//2. 선택기능 활성화 roi
			this.select = new Select(this.map);

			/*this.imageLayer = new ol.layer.Image();
			this.map.addLayer(this.imageLayer);*/

			const imageExtent = [957225.1387, 1921135.1404, 959008.6342, 1922918.6358];
			this.map.getView().fit(imageExtent);

		},
		getLayerByName: function(layerName) {
			var layerList = ciscMap.map.getLayers();
			var layer;
			layerList.forEach(function(l) {
				if (l.get('name') == layerName) {
					layer = l;
				}
			});
			return layer;
		},

		viewImgLayer: function(layerName, url, min_x, min_y, max_x, max_y, tifName) {
			var param = {
				filePath: url,
				//				thumbnailFileCoursNm: url,
				//				ltopCrdntX: min_x,
				//				ltopCrdntY: min_y,
				//				rbtmCrdntX: max_x,
				//				rbtmCrdntY: max_y,
				//				mapPrjctnCn: epsg
			};

//			var controller = "cmsc003thumbnail.do?" + $.param(param);
			var controller = "cisc015/createThumb.do?" + $.param(param);
			console.log(controller);

			var _map = ciscMap.map;
			var _self = this;
			layerName = 'layer1';
			var existLayer = this.getLayerByName(layerName);
			if (existLayer) _map.removeLayer(existLayer);

			// var image_extend = [min_x, min_y, max_x, max_y];
			var image_extend = [min_x, min_y, max_x, max_y];
			var image_source = new ol.source.ImageStatic({
				url: controller,
				//					projection: new ol.proj.Projection({
				//						code: "EPSG:5179",  //'EPSG:4326'
				//						units: 'm',
				//						axisOrientation: 'enu',
				//						extent: image_extend
				//					}),
				imageExtent: image_extend
			});
			image_source.on('imageloaderror', function() {
				console.log('이미지 레이어 로드 에러');
			});
			image_source.on('imageloadstart', function() {
				console.log('이미지 레이어 로드 시작');
			});
			image_source.on('imageloadend', function() {
				console.log('이미지 레이어 로드 종료');
			});
			var imageLayer = new ol.layer.Image({
				//id: layerName,
				name: layerName,
				source: image_source,
				opacity: 1,
				zIndex: 600
			});
			_map.addLayer(imageLayer);
			_map.getView().fit(image_extend);

			// this.setCenter(this.getCenter(image_extend));

		},
		removeImgLayer: function(layerName) {
			var _map = ciscMap.map;
			var existLayer = this.getLayerByName(layerName);
			if (existLayer) _map.removeLayer(existLayer);
		},
		setCenter: function(center) {
			var _map = ciscMap.map;
			_map.getView().setCenter(center)
		},
		getCenter: function(extent) {
			var x = extent[0] + (extent[2] - extent[0]) / 2;
			var y = extent[1] + (extent[3] - extent[1]) / 2;
			return [x, y];
		},
		viewShpLayer: function(geojson) {
			if (!geojson) {
				return;
			}
			var _map = ciscMap.map;
			var _self = this;
			
			layerName = 'layerSHP';
			var existLayer = this.getLayerByName(layerName);
			if (existLayer) _map.removeLayer(existLayer);

			var _vectorAnalSource = new ol.source.Vector({ wrapX: false });
			var _vectorLayer = new ol.layer.VectorImage({
				zIndex: 600,
				source: _vectorAnalSource,
				style: new ol.style.Style({
					fill: new ol.style.Fill({
						color: '#ffffff'
					}),
					stroke: new ol.style.Stroke({
						color: '#53aad6',
						width: 1
					}),
					image: new ol.style.Circle({
						radius: 7,
						fill: new ol.style.Fill({
							color: '#ffffff'
						})
					})
				})
			});
			const format = new ol.format.GeoJSON();
			const features = format.readFeatures(geojson);
			//		console.log(features);
			_vectorAnalSource.addFeatures(features);
			_map.addLayer(_vectorLayer);

			const extent = _vectorAnalSource.getExtent();
			_map.getView().fit(extent, {
				'padding': [10, 10, 10, 10]
			});
		}
	}
	ciscMap.init();

});

