/**
 *  GIS 관련 네임스페이스 
 */
CMSC003.GIS = (() => {

	/**
	 * ol.Map 객체
	 */
	let _map = null;

	/**
	 * 그린 피처의 id
	 */
	let _fId = 0;

	/**
	 * 미리보기 레이어들
	 */
	const _previewLayers = {};
	/**
	 * 생성결과 레이어들
	 */
	const _requestLayers = {};

	/**
	 * 그린 피처가 저장될 벡터 소스
	 */
	let _selectionSource = new ol.source.Vector({ wrapX: false });

	/**
	 * 재난지역 중심점 피처가 저장될 벡터 소스
	 */
	let _selectionSourcePoint = new ol.source.Vector({ wrapX: false });

	/**
	 * 분석 결과가 저장될 벡터 소스
	 */
	//	let _vectorAnalSource = new ol.source.Vector({ wrapX: false });

	/**
	 * 미리보기 이미지 소스
	 */
	//	let _imageSource = null;
	//	let _imageSource = new ol.source.ImageStatic({
	//		imageExtent: [958614.6213111808, 1919546.2119392853, 959745.5264455664, 1920618.00157801],
	//		url: 'img/main_logo.png'
	//	});

	/**
	 * 미리보기 이미지 레이어
	 */
	//	let _imageLayer = new ol.layer.Image();
	//	let _imageLayer = new ol.layer.Image({
	//		source: _imageSource
	//	});

	/**
	 * ROI 선택 드로잉 인터랙션
	 */
	let _draw = null;

	let _extentROI = null;
	/**
	 * 사용할 좌표계
	 */
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

	//	const _geoserver_vector = {
	//		'vector:N3A_B0010000': CMSC003.Const.geoserver + 'wms?service=WMS'
	//	}

	/**
	 * 좌표계 등록 함수 
	 */
	const registProj4 = () => {
		Object.keys(_projCode).forEach(key => proj4.defs(key, _projCode[key]));
		ol.proj.proj4.register(proj4);
	};

	/**
	 * 베경지도 레이어 생성(프록시 적용)
	 */
	const createBaseLayer = () => {
		return new ol.layer.Tile({
			isRequired: true,
			source: new ol.source.WMTS({
				projection: "EPSG:5179",
				tileGrid: new ol.tilegrid.WMTS({
					extent: [-200000.0, -3015.4524155292, 3803015.45241553, 4000000.0],
					origin: [-200000.0, 4000000.0],
					tileSize: 256,
					matrixIds: ["L05", "L06", "L07", "L08", "L09", "L10", "L11", "L12", "L13", "L14", "L15", "L16", "L16", "L17", "L18"],
					resolutions: [2088.96, 1044.48, 522.24, 261.12, 130.56, 65.28, 32.64,
						16.32, 8.16, 4.08, 2.04, 1.02, 0.51, 0.255],
				}),
				layer: "korean_map",
				style: "korean",
				format: "image/png",
				matrixSet: "korean",
				url: "",
				//url: "http://map.ngii.go.kr/openapi/Gettile.do?apikey=E57BF781E900D680853EF984632955F9",
				tileLoadFunction: function(imageTile, src) {
					src = src.replace("TileMatrix", "tilematrix");
					//src = src.replace("TileRow","tilerow")
					src = src.replace("TileRow", "tilerow");
					src = src.replace("TileCol", "tilecol");
					imageTile.getImage().src = contextPath + "/tile/proxy/baseMap.do" + src;
				}
			}),
			visible: true
		});
	};

	/** 
	 * 선택 영역을 보여주는 벡터 레이어 생성
	 */
	const createSelectionLayer = () => {
		return new ol.layer.Vector({
			isRequired: true,
			zIndex: 1003,
			source: _selectionSource,
			style: new ol.style.Style({
				fill: new ol.style.Fill({
					color: 'rgba(247, 17, 17, 0.05)'
				}),
				stroke: new ol.style.Stroke({
					color: '#f71111',
					width: 1
				}),
				image: new ol.style.Circle({
					radius: 7,
					fill: new ol.style.Fill({
						color: '#ffcc33'
					})
				})
			})
		});
	};

	/** 
	 * 선택 영역을 보여주는 벡터 레이어 생성
	 */
	const createSpotLayer = () => {
		return new ol.layer.Vector({
			isRequired: true,
			zIndex: 1003,
			source: _selectionSourcePoint,
			style: new ol.style.Style({
				fill: new ol.style.Fill({
					color: 'rgba(247, 17, 17, 0.05)'
				}),
				stroke: new ol.style.Stroke({
					color: '#f71111',
					width: 1
				}),
				image: new ol.style.Circle({
					radius: 7,
					fill: new ol.style.Fill({
						color: '#ffcc33'
					})
				})
			})
		});
	};

	/** 
	 * 벡터 분석 결과를 보여주는 벡터 레이어 생성
	 */
	const createVectorAnalysisLayer = () => {
		return new ol.layer.VectorImage({
			zIndex: 2,
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
	};

	const createDefaultLayers = () => {
		return [createBaseLayer(), createSelectionLayer(), createSpotLayer()
			// , new ol.layer.Tile({type: 'default', source: new ol.source.OSM()})
		];
	};
	/**
	 * 기본 탑재된 지도 컨트롤 객체 생성
	 */
	const createControls = () => {
		const zoom = new ol.control.Zoom();
		const scaleLine = new ol.control.ScaleLine();
		const zoomSlider = new ol.control.ZoomSlider();
		return [zoom, scaleLine, zoomSlider];
	};

	/**
	 * 지도 객체 초기화
	 */
	const createMap = (target) => {
		return new ol.Map({
			target: target,
			layers: createDefaultLayers(),
			controls: createControls(),
			//        controls: this.defaultControls(),
			view: new ol.View({
				projection: new ol.proj.Projection({
					code: "EPSG:5179",
					units: 'm',
					axisOrientation: 'enu',
					extent: [-200000.0, -3015.4524155292, 3803015.45241553, 4000000.0]
					//extent: [-200000.0, -3015.4524155292, 3803015.45241553, 4000000.0]
				}),
				resolutions: [2088.96, 1044.48, 522.24, 261.12, 130.56, 65.28, 32.64,
					16.32, 8.16, 4.08, 2.04, 1.02, 0.51, 0.255],
				center: [960363.606552286, 1920034.9139856],
				// zoom: 0

				// cmsc003 초기화 값
				zoom: 10,
				//				minZoom: 1,
				//				maxZoom: 14
			})
		});
	};

	/**
	 * 지도 영역 선택(ROI) 인터랙션 추가 
	 */
	const addSelectingROIInteraction = () => {
		const value = 'Circle';
		//geometryFunction = ol.interaction.Draw.createRegularPolygon(4);
		const geometryFunction = ol.interaction.Draw.createBox();

		_draw = new ol.interaction.Draw({
			source: _selectionSource,
			type: value,
			geometryFunction: geometryFunction,
		});

		_map.addInteraction(_draw);

		// 도형 그리기 시작
		_draw.on('drawstart', function(evt) {
			// 도형 ID 부여
			_fId += 1;
			evt.feature.setProperties({
				'id': _fId,
			});
		});

		// 도형 그리기 끝
		_draw.on('drawend', function(evt) {
			const f = evt.feature;
			// 좌표 취득
			const coord = f.getGeometry().getCoordinates();
			// extent
			const extent = f.getGeometry().getExtent();
			console.log(extent);
			_setROIExtent(extent);
			CMSC003.DOM.inputROIToForm(extent);
			$('#roiRadius').val('');
			// 좌표변환 예			
			//			var targetPt = new Proj4js.Point(960363.606552286, 1920034.9139856);
			//			Proj4js.transform(epsg5179, epsg4326, targetPt);			
			//alert(targetPt.x + ", " +targetPt.y);
		});

		// 도형 추가 완료
		_selectionSource.on('addfeature', function(evt) {
			var features = _selectionSource.getFeatures();

			if (features.length > 1) {
				for (f in features) {
					var fid = features[f].getProperties().id;
					_selectionSource.removeFeature(features[f]);
					break;
				}
			}
		});
	};

	const removeSelectingROIInteraction = () => {
		_map.removeInteraction(_draw);
	};

	const startSelectingROI = () => {
		removeSelectingROIInteraction();
		addSelectingROIInteraction();
	};

	/**
	 * EPSG:5179 좌표 1개를 EPSG:4326으로 변환한다
	 */
	const convert5179To4326Coordinate = (coordinate) => {
		return proj4('EPSG:5179', 'EPSG:4326', coordinate);
	};

	/**
	 * EPSG:4326 좌표 1개를 EPSG:5179로 변환한다
	 */
	const convert4326To5179Coordinate = (coordinate) => {
		return proj4('EPSG:4326', 'EPSG:5179', coordinate);
	};

	const convert4326To5186Coordinate = (coordinate) => {
		return proj4('EPSG:4326', 'EPSG:5186', coordinate);
	};

	const convert4326To32652Coordinate = (coordinate) => {
		return proj4('EPSG:4326', 'EPSG:32652', coordinate);
	};

	const convert5186To5179Coordinate = (coordinate) => {
		return proj4('EPSG:5186', 'EPSG:5179', coordinate);
	};

	const convert5179To5186Coordinate = (coordinate) => {
		return proj4('EPSG:5179', 'EPSG:5186', coordinate);
	};

	const convert5179To32652Coordinate = (coordinate) => {
		return proj4('EPSG:5179', 'EPSG:32652', coordinate);
	};

	/**
	 * EPSG:5179 바운딩 박스를 EPSG:4326으로 변환한다
	 */
	const _convert5179To4326Extent = (extent) => {
		const lu = convert5179To4326Coordinate([extent[0], extent[1]]);
		const rl = convert5179To4326Coordinate([extent[2], extent[3]]);
		return [...lu, ...rl];
	};

	/**
	 * EPSG:4326 바운딩 박스를 EPSG:5179로 변환한다
	 */
	const _convert4326To5179Extent = (extent) => {
		const lu = convert4326To5179Coordinate([extent[0], extent[1]]);
		const rl = convert4326To5179Coordinate([extent[2], extent[3]]);
		return [...lu, ...rl];
	};
	/**
	 * EPSG:4326 바운딩 박스를 EPSG:5186로 변환한다
	 */
	const _convert4326To5186Extent = (extent) => {
		const lu = convert4326To5186Coordinate([extent[0], extent[1]]);
		const rl = convert4326To5186Coordinate([extent[2], extent[3]]);
		return [...lu, ...rl];
	};
	/**
	 * EPSG:4326 바운딩 박스를 EPSG:32652로 변환한다
	 */
	const _convert4326To32652Extent = (extent) => {
		const lu = convert4326To32652Coordinate([extent[0], extent[1]]);
		const rl = convert4326To32652Coordinate([extent[2], extent[3]]);
		return [...lu, ...rl];
	};
	/**
	 * EPSG:5186 바운딩 박스를 EPSG:5179로 변환한다
	 */
	const _convert5186To5179Extent = (extent) => {
		const lu = convert5186To5179Coordinate([extent[0], extent[1]]);
		const rl = convert5186To5179Coordinate([extent[2], extent[3]]);
		return [...lu, ...rl];
	};

	/**
	 * EPSG:5179 바운딩 박스를 EPSG:5186로 변환한다
	 */
	const _convert5179To5186Extent = (extent) => {
		const lu = convert5179To5186Coordinate([extent[0], extent[1]]);
		const rl = convert5179To5186Coordinate([extent[2], extent[3]]);
		return [...lu, ...rl];
	};

	const _convert5179To32652Extent = (extent) => {
		const lu = convert5179To32652Coordinate([extent[0], extent[1]]);
		const rl = convert5179To32652Coordinate([extent[2], extent[3]]);
		return [...lu, ...rl];
	};
	/**
	 * ROI영역의 좌표값을 저장함
	 */
	const _setROIExtent = (extent) => {
		_extentROI = extent;
	}

	/**
	 * ROI영역의 좌표값을 반환함
	 */
	const _getROIExtent = () => {
		return _extentROI;
	}

	/**
	 * ROI피쳐 생성
	 */
	const _createROIFeature = (extent, layer) => {
		const coordinates = [[ol.extent.getTopLeft(extent), ol.extent.getTopRight(extent), ol.extent.getBottomRight(extent), ol.extent.getBottomLeft(extent), ol.extent.getTopLeft(extent)]];
		const geom = new ol.geom.Polygon(coordinates);
		const feature = new ol.Feature(geom);

		if (layer) {
			const source = layer.getSource();
			source.clear();
			source.addFeature(feature);
		} else {
			_selectionSource.clear();
			_selectionSource.addFeature(feature);
		}

	}

	/**
	 * OL Extent to ROI
	 */
	const _extentToROI = (extent) => {
		const copy = [...extent];
		return {
			'ulx': copy[0],
			'uly': copy[3],
			'lrx': copy[2],
			'lry': copy[1]
		}
	}

	/**
	 * OL ROI to Extent 
	 */
	const _roiToExtent = (ulx, uly, lrx, lry) => {
		return [ulx, lry, lrx, uly];
	}

	/**
	 * 4326좌표로 ROI 생성
	 */
	const _updateROIExtentBy4326 = (ulx, uly, lrx, lry) => {
		//_createROIFeature();
		const extent = _roiToExtent(ulx, uly, lrx, lry);
		const extent5179 = _convert4326To5179Extent(extent);
		const extent5186 = _convert4326To5186Extent(extent);
		const extent32652 = _convert4326To32652Extent(extent);
		_setROIExtent(extent5179);
		_createROIFeature(extent5179);
		_map.getView().fit(extent5179, {
			'padding': [10, 10, 10, 10]
		});
		return {
			'EPSG5179': _extentToROI(extent5179),
			'EPSG5186': _extentToROI(extent5186),
			'EPSG32652': _extentToROI(extent32652)
		};
	}

	/**
	 * shp 레이어를 업데이트
	 */
	const _updateSHPLayer = (geojson, lid, isRequest) => {
		//		_vectorAnalSource.clear();
		if (!geojson) {
			return;
		}
		const _vectorAnalSource = new ol.source.Vector({ wrapX: false });
		const _vectorLayer = new ol.layer.VectorImage({
			zIndex: 2,
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

		const existLayer = isRequest ? _requestLayers[lid] : _previewLayers[lid];
		if (existLayer) {
			_map.removeLayer(existLayer);
		}
		if (isRequest) {
			_requestLayers[lid] = _vectorLayer;
		} else {
			_previewLayers[lid] = _vectorLayer;
		}
		_map.addLayer(_vectorLayer);

		const extent = _vectorAnalSource.getExtent();
		_map.getView().fit(extent, {
			'padding': [10, 10, 10, 10]
		});
	}

	/**
	 * shp 레이어를 업데이트
	 */
	const _updateSHPLayerDigitalMap = (geojson, lid, isRequest) => {
		//		_vectorAnalSource.clear();
		if (!geojson) {
			return;
		}
		const _vectorAnalSource = new ol.source.Vector({ wrapX: false });
		const _vectorLayer = new ol.layer.VectorImage({
			zIndex: 2,
			source: _vectorAnalSource,
			style: new ol.style.Style({
				fill: new ol.style.Fill({
					color: '#aaaaaa'
				}),
				stroke: new ol.style.Stroke({
					color: '#000000',
					width: 1
				}),
				image: new ol.style.Circle({
					radius: 7,
					fill: new ol.style.Fill({
						color: '#aaaaaa'
					})
				})
			})
		});
		const format = new ol.format.GeoJSON();
		const features = format.readFeatures(geojson);
		//		console.log(features);
		_vectorAnalSource.addFeatures(features);

		const existLayer = isRequest ? _requestLayers[lid] : _previewLayers[lid];
		if (existLayer) {
			_map.removeLayer(existLayer);
		}
		if (isRequest) {
			_requestLayers[lid] = _vectorLayer;
		} else {
			_previewLayers[lid] = _vectorLayer;
		}
		_map.addLayer(_vectorLayer);

		const extent = _vectorAnalSource.getExtent();
		_map.getView().fit(extent, {
			'padding': [10, 10, 10, 10]
		});
	}

	const _updateSHPLayerDigitalMapAfterRequest = (geojson, lid) => {
		//		_vectorAnalSource.clear();
		const existLayer = _requestLayers[lid];

		if (existLayer) {
			existLayer.setVisible(true);
			const extent = existLayer.getSource().getExtent();
			_map.getView().fit(extent, {
				'padding': [10, 10, 10, 10]
			});
		} else {
			const _vectorAnalSource = new ol.source.Vector({ wrapX: false });
			const _vectorLayer = new ol.layer.VectorImage({
				zIndex: 2,
				source: _vectorAnalSource,
				style: new ol.style.Style({
					fill: new ol.style.Fill({
						color: '#aaaaaa'
					}),
					stroke: new ol.style.Stroke({
						color: '#000000',
						width: 1
					}),
					image: new ol.style.Circle({
						radius: 7,
						fill: new ol.style.Fill({
							color: '#aaaaaa'
						})
					})
				})
			});
			const format = new ol.format.GeoJSON();
			const features = format.readFeatures(geojson);
			//		console.log(features);
			_vectorAnalSource.addFeatures(features);
			_requestLayers[lid] = _vectorLayer;
			_map.addLayer(_vectorLayer);

			const extent = _vectorAnalSource.getExtent();
			_map.getView().fit(extent, {
				'padding': [10, 10, 10, 10]
			});
		}
	}

	/**
	 * 5179 좌표로 ROI 범위 피처를 생성
	 * INPUT : [ulx, uly, lrx, lry]
	 */
	const _createPolygonROIfrom5179 = (coordinate, layer, map) => {
		if (layer && map) {
			_setROIExtent(coordinate);
			_createROIFeature(coordinate, layer);
			console.log(coordinate);

			map.getView().fit([coordinate[0], coordinate[1], coordinate[2], coordinate[3]], {
				'padding': [10, 10, 10, 10]
			});

		} else {
			_setROIExtent(coordinate);
			_createROIFeature(coordinate);
			console.log(coordinate);

			_map.getView().fit([coordinate[0], coordinate[1], coordinate[2], coordinate[3]], {
				'padding': [10, 10, 10, 10]
			});

			CMSC003.DOM.inputROIToForm(coordinate);
		}

	}

	/**
	 * 4326 좌표와 반지름으로 roi 범위 피처를 생성
	 */
	const _createBufferROI = (coordinate, radius) => {
		const point = turf.point(coordinate);
		const buffered = turf.buffer(point, radius, { units: 'meters' });
		const features = turf.featureCollection([buffered]);
		const enveloped = turf.envelope(features);
		console.log(enveloped);
		const extent = _convert4326To5179Extent(enveloped.bbox);
		_createROIFeature(extent);

		console.log(extent);
		_setROIExtent(extent);
		CMSC003.DOM.inputROIToForm(extent);
	}

	const _createBufferROIMNSC003 = (coordinate, radius) => {
		const point = turf.point(coordinate);
		const buffered = turf.buffer(point, radius, { units: 'meters' });
		const features = turf.featureCollection([buffered]);
		const enveloped = turf.envelope(features);
		console.log(enveloped);
		const extent = _convert4326To5179Extent(enveloped.bbox);
		return extent;
	}

	const _createROICenter = (coordinate, layer) => {
		//		const center = convert4326To5179Coordinate(coordinate);
		const geom = new ol.geom.Point(coordinate);
		const feature = new ol.Feature({
			geometry: geom
		});
		if (layer) {
			const source = layer.getSource();
			source.clear();
			source.addFeature(feature);
		} else {
			_selectionSourcePoint.clear();
			_selectionSourcePoint.addFeature(feature);
		}
	}

	return {
		/**
		 * 지도 객체 초기화 함수
		 */
		initMap(target) {
			// 좌표계 등록
			registProj4();
			// ol.Map 객체 선언
			_map = createMap(target);
			return _map;
		},

		/**
		 * 지도 객체 반환
		 */
		getMap() {
			return _map;
		},

		/**
		 * 선택 영역 소스 반환
		 */
		getSelectionSource() {
			return _selectionSource;
		},

		/**
		 * 중심섬 소스 반환
		 */
		getROICenterSource() {
			return _selectionSourcePoint;
		},

		/**
		 * ROI 선택 활성화
		 */
		startSelectingROI() {
			startSelectingROI();
		},

		/**
		 * ROI 선택 비활성화
		 */
		endSelectingROI() {
			removeSelectingROIInteraction();
		},

		/**
		 * EPSG:5179 바운딩 박스를 EPSG:4326으로 변환한다
		 */
		convert5179To4326Extent(extent) {
			return _convert5179To4326Extent(extent);
		},

		/**
		 * EPSG:4326 바운딩 박스를 EPSG:5179으로 변환한다
		 */
		convert4326To5179Extent(extent) {
			return _convert4326To5179Extent(extent);
		},

		/**
		 * EPSG:5179 바운딩 박스를 EPSG:4326으로 변환한다
		 */
		convert5179To5186Extent(extent) {
			return _convert5179To5186Extent(extent);
		},

		convert5179To32652Extent(extent) {
			return _convert5179To32652Extent(extent);
		},

		/**
		 * ROI영역의 좌표값을 저장함
		 */
		setROIExtent(extent) {
			_setROIExtent(extent);
		},

		/**
		 * ROI영역의 좌표값을 반환함
		 */
		getROIExtent() {
			return _getROIExtent();
		},

		/**
		 * 이미지 레이어 초기화
		 */
		clearImageLayer() {
			_imageLayer.setSource(null)
		},

		/**
		 * ROI 선택 레이어 초기화
		 */
		clearROILayer() {
			_selectionSource.clear();
			_selectionSourcePoint.clear();
		},


		/**
		 * 레이어를 삭제한다
		 */
		removeLayerById(vid, isRequest, map) {
			let targetMap = _map;
			if(map) {
				targetMap = map;
			}
			const layer = isRequest ? _requestLayers[vid] : _previewLayers[vid];
			if (layer) {
				if (isRequest) {
					_requestLayers[vid] = undefined;
				} else {
					_previewLayers[vid] = undefined;
				}
				targetMap.removeLayer(layer);
			} else {
				console.log('no layer');
			}
		},

		/**
		 * 정보에 맞는 이미지 레이어를 보여줌 
		 */
		updateImageLayerTemp(vid, isRequest, map) {
			//_imageSource = null;
			let targetMap = _map;
			if(map) {
				targetMap = map;
			}
			
			const dataMap = isRequest ? CMSC003.Storage.get('requestDataMap') : CMSC003.Storage.get('searchDataMap');
			const video = dataMap[vid];

			if (video) {

				//				const leftTop = [parseFloat(video.ltopCrdntY), parseFloat(video.ltopCrdntX)];
				//				const rightBottom = [parseFloat(video.rbtmCrdntY), parseFloat(video.rbtmCrdntX)];
				//				const extent = [parseFloat(video.rbtmCrdntX), parseFloat(video.ltopCrdntY), parseFloat(video.ltopCrdntX), parseFloat(video.rbtmCrdntY)];
				let extent;
				let convExtent;
				if (isRequest) {
					if (video.ltopCrdntY && video.rbtmCrdntX && video.rbtmCrdntY && video.ltopCrdntX) {
						extent = [parseFloat(video.ltopCrdntY), parseFloat(video.rbtmCrdntX), parseFloat(video.rbtmCrdntY), parseFloat(video.ltopCrdntX)];
						// extent = [parseFloat(video.rbtmCrdntY), parseFloat(video.ltopCrdntX), parseFloat(video.ltopCrdntY), parseFloat(video.rbtmCrdntX)]; //minx, miny, maxx, maxy						
					} else if (video.xmin && video.ymin && video.xmax && video.ymax) {
						extent = [parseFloat(video.xmin), parseFloat(video.ymin), parseFloat(video.xmax), parseFloat(video.ymax)]; //minx, miny, maxx, maxy
					} else {
						console.log('영상 범위가 존재하지않습니다.');
						return;
					}
					convExtent = extent
				} else {
					if (video.ltopCrdntY && video.rbtmCrdntX && video.rbtmCrdntY && video.ltopCrdntX) {
						// extent = [parseFloat(video.rbtmCrdntY), parseFloat(video.ltopCrdntX), parseFloat(video.ltopCrdntY), parseFloat(video.rbtmCrdntX)];
						extent = [parseFloat(video.ltopCrdntY), parseFloat(video.rbtmCrdntX), parseFloat(video.rbtmCrdntY), parseFloat(video.ltopCrdntX)]; //minx, miny, maxx, maxy						
					} else if (video.xmin && video.ymin && video.xmax && video.ymax) {
						extent = [parseFloat(video.xmin), parseFloat(video.ymin), parseFloat(video.xmax), parseFloat(video.ymax)]; //minx, miny, maxx, maxy
					} else {
						console.log('영상 범위가 존재하지않습니다.');
						return;
					}
					convExtent = CMSC003.GIS.transformExtent(extent, video.mapPrjctnCn, 'EPSG:5179');
				}


				if (!video.thumbnail) {
					CMSC003.DOM.showErrorMsg('썸네일이 존재하지 않습니다.');
				}

				//D:/TestData/05_G119/Flood/2021_Busan/Disaster/Image/Drone/20210825/GiJang_Ortho_25cm.jpg
				let url = `cmsc003thumbnail.do?thumbnailFileCoursNm=${video.thumbnailFileCoursNm}`;
				//let url = `cmsc003thumbnail.do?thumbnailFileCoursNm=D:/TestData/05_G119/Flood/2021_Busan/Disaster/Image/Drone/20210825/GiJang_Ortho_25cm.jpg`;
				url = url.replaceAll("\\\\", "//");
				url = url.replaceAll("\\", "/");
				const encode = encodeURI(url);
				console.log('img', convExtent)
				const _imageLayer = new ol.layer.Image();

				_imageSource = new ol.source.ImageStatic({
					imageExtent: convExtent,
					url: url,
					//					imageSize: [160, 128]
					//imageSize: [1564, 1440]
				});
				_imageSource.on('imageloadstart', function() {
					CMSC003.DOM.showSpinner(true);
				});
				_imageSource.on('imageloadend', function() {
					CMSC003.DOM.showSpinner(false);
				});
				if (isRequest) {
					_imageSource.on('imageloaderror', function() {
						CMSC003.DOM.showSpinner(false);
						CMSC003.DOM.showErrorMsg('썸네일이 존재하지 않거나 파일 해상도가 너무 큽니다.');
						//					console.log('vido-id-' + vid);
						const btn = $('#vido-id-request-' + vid);
						//					console.log(btn);

						if ($(btn).hasClass('ui-btn-clicked-layer-preview')) {
							$(btn).removeClass('ui-btn-clicked-layer-preview');
							CMSC003.GIS.removeLayerById(vid, isRequest, targetMap);
						}
					});
				} else {
					_imageSource.on('imageloaderror', function() {
						CMSC003.DOM.showSpinner(false);
						CMSC003.DOM.showErrorMsg('썸네일이 존재하지 않거나 파일 해상도가 너무 큽니다.');
						//					console.log('vido-id-' + vid);
						const btn = $('#vido-id-' + vid).siblings('button');
						//					console.log(btn);

						if ($(btn).hasClass('ui-btn-clicked-layer-preview')) {
							$(btn).removeClass('ui-btn-clicked-layer-preview');
							//CMSC003.GIS.removeLayerById(vid);
							CMSC003.GIS.removeLayerById(vid, isRequest, targetMap);
						}
					});
				}


				_imageLayer.setSource(_imageSource);

				const existLayer = isRequest ? _requestLayers[vid] : _previewLayers[vid];
				if (existLayer) {
					targetMap.removeLayer(existLayer);
				}
				if (isRequest) {
					_requestLayers[vid] = _imageLayer;
				} else {
					_previewLayers[vid] = _imageLayer;
				}
				targetMap.addLayer(_imageLayer);

				targetMap.getView().fit(convExtent, {
					'padding': [10, 10, 10, 10]
				});
			}
		},

		/**
		 * 수치지도 요청결과 썸네일을 보여줌 
		 */
		updateImageLayerDigitalMap(vid, isRequest) {
			//_imageSource = null;

			const dataMap = isRequest ? CMSC003.Storage.get('requestDataMap') : CMSC003.Storage.get('searchDataMap');
			const video = dataMap[vid];

			if (video) {

				//				const leftTop = [parseFloat(video.ltopCrdntY), parseFloat(video.ltopCrdntX)];
				//				const rightBottom = [parseFloat(video.rbtmCrdntY), parseFloat(video.rbtmCrdntX)];
				//				const extent = [parseFloat(video.rbtmCrdntX), parseFloat(video.ltopCrdntY), parseFloat(video.ltopCrdntX), parseFloat(video.rbtmCrdntY)];
				let extent;
				let convExtent;
				if (isRequest) {
					// extent = [parseFloat(video.rbtmCrdntY), parseFloat(video.ltopCrdntX), parseFloat(video.ltopCrdntY), parseFloat(video.rbtmCrdntX)];
					// extent = [parseFloat(video.ltopCrdntX), parseFloat(video.rbtmCrdntY), parseFloat(video.rbtmCrdntX), parseFloat(video.ltopCrdntY)]; //minx, miny, maxx, maxy
					extent = [parseFloat(video.ltopCrdntY), parseFloat(video.rbtmCrdntX), parseFloat(video.rbtmCrdntY), parseFloat(video.ltopCrdntX)];
					convExtent = extent
				} else {
					if (video.ltopCrdntY && video.rbtmCrdntX && video.rbtmCrdntY && video.ltopCrdntX) {
						// extent = [parseFloat(video.rbtmCrdntY), parseFloat(video.ltopCrdntX), parseFloat(video.ltopCrdntY), parseFloat(video.rbtmCrdntX)];
						extent = [parseFloat(video.ltopCrdntY), parseFloat(video.rbtmCrdntX), parseFloat(video.rbtmCrdntY), parseFloat(video.ltopCrdntX)]; //minx, miny, maxx, maxy						
					} else if (video.xmin && video.ymin && video.xmax && video.ymax) {
						extent = [parseFloat(video.xmin), parseFloat(video.ymin), parseFloat(video.xmax), parseFloat(video.ymax)]; //minx, miny, maxx, maxy
					} else {
						console.log('영상 범위가 존재하지않습니다.');
						return;
					}
					convExtent = CMSC003.GIS.transformExtent(extent, video.mapPrjctnCn, 'EPSG:5179');
				}


				if (!video.thumbnail) {
					CMSC003.DOM.showErrorMsg('썸네일이 존재하지 않습니다.');
				}

				//D:/TestData/05_G119/Flood/2021_Busan/Disaster/Image/Drone/20210825/GiJang_Ortho_25cm.jpg
				let url = `cmsc003shpThumbnail2.do?thumbnailFileCoursNm=${video.thumbnailFileCoursNm}`;
				//let url = `cmsc003thumbnail.do?thumbnailFileCoursNm=D:/TestData/05_G119/Flood/2021_Busan/Disaster/Image/Drone/20210825/GiJang_Ortho_25cm.jpg`;
				url = url.replaceAll("\\\\", "//");
				url = url.replaceAll("\\", "/");
				const encode = encodeURI(url);
				console.log('img', convExtent)
				const _imageLayer = new ol.layer.Image();

				_imageSource = new ol.source.ImageStatic({
					imageExtent: convExtent,
					url: url,
					//					imageSize: [160, 128]
					//imageSize: [1564, 1440]
				});
				_imageSource.on('imageloadstart', function() {
					CMSC003.DOM.showSpinner(true);
				});
				_imageSource.on('imageloadend', function() {
					CMSC003.DOM.showSpinner(false);
				});
				if (isRequest) {
					_imageSource.on('imageloaderror', function() {
						CMSC003.DOM.showSpinner(false);
						CMSC003.DOM.showErrorMsg('썸네일이 존재하지 않거나 파일 해상도가 너무 큽니다.');
						//					console.log('vido-id-' + vid);
						const btn = $('#vido-id-request-' + vid);
						//					console.log(btn);

						if ($(btn).hasClass('ui-btn-clicked-layer-preview')) {
							$(btn).removeClass('ui-btn-clicked-layer-preview');
							CMSC003.GIS.removeLayerById(vid);
						}
					});
				} else {
					_imageSource.on('imageloaderror', function() {
						CMSC003.DOM.showSpinner(false);
						CMSC003.DOM.showErrorMsg('썸네일이 존재하지 않거나 파일 해상도가 너무 큽니다.');
						//					console.log('vido-id-' + vid);
						const btn = $('#vido-id-' + vid).siblings('button');
						//					console.log(btn);

						if ($(btn).hasClass('ui-btn-clicked-layer-preview')) {
							$(btn).removeClass('ui-btn-clicked-layer-preview');
							CMSC003.GIS.removeLayerById(vid);
						}
					});
				}


				_imageLayer.setSource(_imageSource);

				const existLayer = isRequest ? _requestLayers[vid] : _previewLayers[vid];
				if (existLayer) {
					_map.removeLayer(existLayer);
				}
				if (isRequest) {
					_requestLayers[vid] = _imageLayer;
				} else {
					_previewLayers[vid] = _imageLayer;
				}
				_map.addLayer(_imageLayer);

				_map.getView().fit(convExtent, {
					'padding': [10, 10, 10, 10]
				});
			}
		},

		/**
		 * 분석벡터 요청결과 썸네일을 보여줌 
		 */
		updateImageLayerAnalysisResult(vid, isRequest, map) {
			//_imageSource = null;
			let targetMap = _map;
			if(map) {
				targetMap = map;
			}
			const dataMap = isRequest ? CMSC003.Storage.get('requestDataMap') : CMSC003.Storage.get('searchDataMap');
			const video = dataMap[vid];

			if (video) {

				//				const leftTop = [parseFloat(video.ltopCrdntY), parseFloat(video.ltopCrdntX)];
				//				const rightBottom = [parseFloat(video.rbtmCrdntY), parseFloat(video.rbtmCrdntX)];
				//				const extent = [parseFloat(video.rbtmCrdntX), parseFloat(video.ltopCrdntY), parseFloat(video.ltopCrdntX), parseFloat(video.rbtmCrdntY)];
				let extent;
				let convExtent;
				if (isRequest) {
					// extent = [parseFloat(video.rbtmCrdntY), parseFloat(video.ltopCrdntX), parseFloat(video.ltopCrdntY), parseFloat(video.rbtmCrdntX)];
					// extent = [parseFloat(video.ltopCrdntX), parseFloat(video.rbtmCrdntY), parseFloat(video.rbtmCrdntX), parseFloat(video.ltopCrdntY)]; //minx, miny, maxx, maxy
					extent = [parseFloat(video.ltopCrdntY), parseFloat(video.rbtmCrdntX), parseFloat(video.rbtmCrdntY), parseFloat(video.ltopCrdntX)];
					convExtent = extent
				} else {
					if (video.ltopCrdntY && video.rbtmCrdntX && video.rbtmCrdntY && video.ltopCrdntX) {
						// extent = [parseFloat(video.rbtmCrdntY), parseFloat(video.ltopCrdntX), parseFloat(video.ltopCrdntY), parseFloat(video.rbtmCrdntX)];
						extent = [parseFloat(video.ltopCrdntY), parseFloat(video.rbtmCrdntX), parseFloat(video.rbtmCrdntY), parseFloat(video.ltopCrdntX)]; //minx, miny, maxx, maxy						
					} else if (video.xmin && video.ymin && video.xmax && video.ymax) {
						extent = [parseFloat(video.xmin), parseFloat(video.ymin), parseFloat(video.xmax), parseFloat(video.ymax)]; //minx, miny, maxx, maxy
					} else {
						console.log('영상 범위가 존재하지않습니다.');
						return;
					}
					convExtent = CMSC003.GIS.transformExtent(extent, video.mapPrjctnCn, 'EPSG:5179');
				}


				if (!video.thumbnail) {
					CMSC003.DOM.showErrorMsg('썸네일이 존재하지 않습니다.');
				}

				//D:/TestData/05_G119/Flood/2021_Busan/Disaster/Image/Drone/20210825/GiJang_Ortho_25cm.jpg
				let url = `cmsc003thumbnail.do?thumbnailFileCoursNm=${video.thumbnailFileCoursNm}`;
				//let url = `cmsc003thumbnail.do?thumbnailFileCoursNm=D:/TestData/05_G119/Flood/2021_Busan/Disaster/Image/Drone/20210825/GiJang_Ortho_25cm.jpg`;
				url = url.replaceAll("\\\\", "//");
				url = url.replaceAll("\\", "/");
				const encode = encodeURI(url);
				console.log('img', convExtent)
				const _imageLayer = new ol.layer.Image();

				_imageSource = new ol.source.ImageStatic({
					imageExtent: convExtent,
					url: url,
					//					imageSize: [160, 128]
					//imageSize: [1564, 1440]
				});
				_imageSource.on('imageloadstart', function() {
					CMSC003.DOM.showSpinner(true);
				});
				_imageSource.on('imageloadend', function() {
					CMSC003.DOM.showSpinner(false);
				});
				if (isRequest) {
					_imageSource.on('imageloaderror', function() {
						CMSC003.DOM.showSpinner(false);
						CMSC003.DOM.showErrorMsg('썸네일이 존재하지 않거나 파일 해상도가 너무 큽니다.');
						//					console.log('vido-id-' + vid);
						const btn = $('#vido-id-request-' + vid);
						//					console.log(btn);

						if ($(btn).hasClass('ui-btn-clicked-layer-preview')) {
							$(btn).removeClass('ui-btn-clicked-layer-preview');
							CMSC003.GIS.removeLayerById(vid);
						}
					});
				} else {
					_imageSource.on('imageloaderror', function() {
						CMSC003.DOM.showSpinner(false);
						CMSC003.DOM.showErrorMsg('썸네일이 존재하지 않거나 파일 해상도가 너무 큽니다.');
						//					console.log('vido-id-' + vid);
						const btn = $('#vido-id-' + vid).siblings('button');
						//					console.log(btn);

						if ($(btn).hasClass('ui-btn-clicked-layer-preview')) {
							$(btn).removeClass('ui-btn-clicked-layer-preview');
							CMSC003.GIS.removeLayerById(vid);
						}
					});
				}


				_imageLayer.setSource(_imageSource);

				const existLayer = isRequest ? _requestLayers[vid] : _previewLayers[vid];
				if (existLayer) {
					targetMap.removeLayer(existLayer);
				}
				if (isRequest) {
					_requestLayers[vid] = _imageLayer;
				} else {
					_previewLayers[vid] = _imageLayer;
				}
				targetMap.addLayer(_imageLayer);

				targetMap.getView().fit(convExtent, {
					'padding': [10, 10, 10, 10]
				});
			}
		},

		imageView(fileName, minx, miny, maxx, maxy) {
			var imageExtent = [minx, miny, maxx, maxy];
			var raster_image = new ol.layer.Image({
				source: new ol.source.ImageStatic({
					url: '/thumnail/' + fileName,
					crossOrigin: '',
					projection: 'EPSG:5179',
					imageExtent: imageExtent
				})
			});
			_map.addLayer(raster_image);

			/*var  source = new ol.source.ImageStatic({
				url:'/thumnail/'+fileName,
				crossOrigin: '',
				projection: 'EPSG:5179',
				imageExtent: imageExtent

			});*/
			//_imageLayer.setSource(source);
			_map.getView().fit(imageExtent, {
				'padding': [10, 10, 10, 10]
			});

		},

		/**
		 * 분석벡터 미리보기 썸네일을 보여줌 
		 */
		updateImageLayerAnalysisRequest(vid, isRequest, map) {
			//_imageSource = null;
			let targetMap = _map;
			if(map) {
				targetMap = map;
			}
			
			const dataMap = isRequest ? CMSC003.Storage.get('requestDataMap') : CMSC003.Storage.get('searchDataMap');
			const video = dataMap[vid];

			if (video) {
				const extent5179 = isRequest ? CMSC003.Storage.get('requestROIExtent') : CMSC003.GIS.getROIExtent();
				if (!extent5179) {
					alert('ROI 범위를 지정해주세요.');
					return;
				}

				//				//				const leftTop = [parseFloat(video.ltopCrdntY), parseFloat(video.ltopCrdntX)];
				//				//				const rightBottom = [parseFloat(video.rbtmCrdntY), parseFloat(video.rbtmCrdntX)];
				//				//				const extent = [parseFloat(video.rbtmCrdntX), parseFloat(video.ltopCrdntY), parseFloat(video.ltopCrdntX), parseFloat(video.rbtmCrdntY)];
				let extent;
				let convExtent;
				if (isRequest) {
					//extent = [parseFloat(video.rbtmCrdntY), parseFloat(video.ltopCrdntX), parseFloat(video.ltopCrdntY), parseFloat(video.rbtmCrdntX)];
					// extent = [parseFloat(video.ltopCrdntX), parseFloat(video.rbtmCrdntY), parseFloat(video.rbtmCrdntX), parseFloat(video.ltopCrdntY)]; //minx, miny, maxx, maxy
					extent = [parseFloat(video.ltopCrdntY), parseFloat(video.rbtmCrdntX), parseFloat(video.rbtmCrdntY), parseFloat(video.ltopCrdntX)];
					convExtent = extent
				} else {
					if (video.ltopCrdntY && video.rbtmCrdntX && video.rbtmCrdntY && video.ltopCrdntX) {
						// extent = [parseFloat(video.rbtmCrdntY), parseFloat(video.ltopCrdntX), parseFloat(video.ltopCrdntY), parseFloat(video.rbtmCrdntX)];
						extent = [parseFloat(video.ltopCrdntY), parseFloat(video.rbtmCrdntX), parseFloat(video.rbtmCrdntY), parseFloat(video.ltopCrdntX)];						
						// extent = [parseFloat(video.ltopCrdntX), parseFloat(video.rbtmCrdntY), parseFloat(video.rbtmCrdntX), parseFloat(video.ltopCrdntY)];
					} else if (video.xmin && video.ymin && video.xmax && video.ymax) {
						extent = [parseFloat(video.xmin), parseFloat(video.ymin), parseFloat(video.xmax), parseFloat(video.ymax)]; //minx, miny, maxx, maxy
					} else {
						console.log('영상 범위가 존재하지않습니다.');
						return;
					}
					convExtent = CMSC003.GIS.transformExtent(extent, video.mapPrjctnCn, 'EPSG:5179');
				}

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
				video['createInfo'] = selectExtent;

				if (!video.thumbnail) {
					CMSC003.DOM.showErrorMsg('썸네일이 존재하지 않습니다.');
				}


				//D:/TestData/05_G119/Flood/2021_Busan/Disaster/Image/Drone/20210825/GiJang_Ortho_25cm.jpg
				const param = {
					json: JSON.stringify(video)
				};
				let url = `cmsc003shpThumbnail.do?${$.param(param)}`;
				//let url = `cmsc003thumbnail.do?thumbnailFileCoursNm=D:/TestData/05_G119/Flood/2021_Busan/Disaster/Image/Drone/20210825/GiJang_Ortho_25cm.jpg`;
				url = url.replaceAll("\\\\", "//");
				url = url.replaceAll("\\", "/");
				const encode = encodeURI(url);
				console.log('img', convExtent)
				const _imageLayer = new ol.layer.Image();

				_imageSource = new ol.source.ImageStatic({
					imageExtent: convExtent,
					url: url,
					//					imageSize: [160, 128]
					//imageSize: [1564, 1440]
				});
				_imageSource.on('imageloadstart', function() {
					CMSC003.DOM.showSpinner(true);
				});
				_imageSource.on('imageloadend', function() {
					CMSC003.DOM.showSpinner(false);
				});
				if (isRequest) {
					_imageSource.on('imageloaderror', function() {
						CMSC003.DOM.showSpinner(false);
						CMSC003.DOM.showErrorMsg('썸네일이 존재하지 않거나 파일 해상도가 너무 큽니다.');
						//					console.log('vido-id-' + vid);
						const btn = $('#vido-id-request-' + vid);
						//					console.log(btn);

						if ($(btn).hasClass('ui-btn-clicked-layer-preview')) {
							$(btn).removeClass('ui-btn-clicked-layer-preview');
							CMSC003.GIS.removeLayerById(vid, isRequest, targetMap);
						}
					});
				} else {
					_imageSource.on('imageloaderror', function() {
						CMSC003.DOM.showSpinner(false);
						CMSC003.DOM.showErrorMsg('썸네일이 존재하지 않거나 파일 해상도가 너무 큽니다.');
						//					console.log('vido-id-' + vid);
						const btn = $('#vido-id-' + vid).siblings('button');
						//					console.log(btn);

						if ($(btn).hasClass('ui-btn-clicked-layer-preview')) {
							$(btn).removeClass('ui-btn-clicked-layer-preview');
							CMSC003.GIS.removeLayerById(vid, isRequest, targetMap);
						}
					});
				}


				_imageLayer.setSource(_imageSource);

				const existLayer = isRequest ? _requestLayers[vid] : _previewLayers[vid];
				if (existLayer) {
					targetMap.removeLayer(existLayer);
				}
				if (isRequest) {
					_requestLayers[vid] = _imageLayer;
				} else {
					_previewLayers[vid] = _imageLayer;
				}
				targetMap.addLayer(_imageLayer);

				targetMap.getView().fit(convExtent, {
					'padding': [10, 10, 10, 10]
				});
			}
		},

		/**
		 * 정보에 맞는 이미지 레이어를 보여줌 
		 */
		updateImageLayer(url, extent) {
			_imageSource = new ol.source.ImageStatic({
				imageExtent: extent,
				url: url
			});

			_imageLayer.setSource(_imageSource);
		},

		/**
		 * 좌표변환
		 */
		transform(position, s_epsg, t_epsg) {
			//			console.log(position, 'BEFORE_TS');
			//			console.log(ol.proj.transform(position, s_epsg, t_epsg), 'AFTER_TS');

			return ol.proj.transform(position, s_epsg, t_epsg);
		},

		/**
		 * 범위좌표변환
		 */
		transformExtent(position, s_epsg, t_epsg) {
			//			console.log(position, 'BEFORE_TS');
			//			console.log(ol.proj.transformExtent(position, s_epsg, t_epsg), 'AFTER_TS');

			return ol.proj.transformExtent(position, s_epsg, t_epsg);
		},

		setMapCenter(position, map) {
			return map ? map.getView().setCenter(position) : CMSC003.GIS.getMap().getView().setCenter(position);
		},

		// extent [Number] => POLYGON String
		extentToPolygon(extent) {
			const ulx = extent[0].toFixed(3); //minx
			const uly = extent[1].toFixed(3); //miny
			const lrx = extent[2].toFixed(3); //maxx
			const lry = extent[3]; //maxy

			const polygon = "POLYGON((" + ulx + ' ' + lry + ', ' + ulx + ' ' + uly + ', ' + lrx + ' ' + uly + ', ' + lrx + ' ' + lry + ', ' + ulx + ' ' + lry + "))";

			console.log(polygon);

			return polygon;
		},

		updateImageLayerFromGeoserver(vid, isRequest, map) {
			let targetMap = _map;
			if(map) {
				targetMap = map; 
			}
			//_imageSource = null;

			//const dataMap = CMSC003.Storage.get('searchDataMap');
			//const video = dataMap[vid];
			//const transform = CMSC003.GIS.transformExtent([video.ltopCrdntY, video.ltopCrdntX, video.rbtmCrdntY, video.rbtmCrdntX], video.mapPrjctnCn, 'EPSG:5179');
			let extent = isRequest ? CMSC003.Storage.get('requestROIExtent') : CMSC003.GIS.getROIExtent();
			if(!extent && isRequest) {
				const dataMap = CMSC003.Storage.get('requestDataMap');
				const layer = dataMap[vid];
				if(layer) {
					if(layer.ulx5179 && layer.lry5179 && layer.lrx5179 && layer.uly5179) {
						extent = [parseFloat(layer.ulx5179), parseFloat(layer.lry5179), parseFloat(layer.lrx5179), parseFloat(layer.uly5179)];
					} else if(layer.ltopCrdntX && layer.rbtmCrdntY && layer.rbtmCrdntX && layer.ltopCrdntY){
						// extent = [parseFloat(layer.rbtmCrdntY), parseFloat(layer.ltopCrdntX), parseFloat(layer.ltopCrdntY), parseFloat(layer.rbtmCrdntX)];
						extent = [parseFloat(layer.ltopCrdntY), parseFloat(layer.rbtmCrdntX), parseFloat(layer.rbtmCrdntY), parseFloat(layer.ltopCrdntX)]; //minx, miny, maxx, maxy
					}
				}
				
			}
			const polygon = CMSC003.GIS.extentToPolygon(extent);

			var geomstr = 'the_geom';
			if(vid === 'vector:N3A_B0010000' || vid === 'vector:N3A_D0010000' || vid === 'vector:N3A_A0010000') {
				geomstr = 'geom';
			}

			const _imageSource = new ol.source.ImageWMS({
				ratio: 1,
				//				url: 'http://192.168.0.12:9999/geoserver/wms?service=WMS',
				//url: CMSC003.Const.geoserver + 'wms?service=WMS',
				url: contextPath + "/geoserver/proxy.do?",
				params: {
					'VERSION': '1.1.1',
					//"CQL_FILTER": 'INTERSECTS(geom,POLYGON((956546.691 1964815.771, 956546.691 1952482.819, 969453.851 1952482.819, 969453.851 1964815.771, 956546.691  1964815.771)))',
					"CQL_FILTER": 'INTERSECTS(' + geomstr + ',' + polygon + ')',
					// "CQL_FILTER": 'INTERSECTS(the_geom,' + polygon + ')',
					//"LAYERS": "vector:N3A_B0010000",
					"LAYERS": vid,
					"exceptions": 'application/vnd.ogc.se_inimage',
					"WIDTH": "256",
					"HEIGHT": "256"
				}
			})
			//			_imageSource.on('imageloadstart', function() {
			//				CMSC003.DOM.showSpinner(true);
			//			});
			//			_imageSource.on('imageloadend', function() {
			//				CMSC003.DOM.showSpinner(false);
			//			});
			_imageSource.on('imageloaderror', function() {
				//				CMSC003.DOM.showSpinner(false);
				CMSC003.DOM.showErrorMsg('썸네일을 조회할 수 없습니다.');
				const btn = $('#vido-id-' + vid).siblings('button');
				//					console.log(btn);

				if ($(btn).hasClass('ui-btn-clicked-layer-preview')) {
					$(btn).removeClass('ui-btn-clicked-layer-preview');
					CMSC003.GIS.removeLayerById(vid);
				}
			});

			const _imageLayer = new ol.layer.Image({
				source: _imageSource,
				zIndex: 1000
			});

			const existLayer = isRequest ? _requestLayers[vid] : _previewLayers[vid];
			if (existLayer) {
				targetMap.removeLayer(existLayer);
			}
			if (isRequest) {
				_requestLayers[vid] = _imageLayer;
			} else {
				_previewLayers[vid] = _imageLayer;
			}
			targetMap.addLayer(_imageLayer);

			CMSC003.GIS.setMapCenter([(extent[0] + extent[2]) / 2, (extent[1] + extent[3]) / 2], targetMap);
			//CMSC003.GIS.setMapCenter([969453.851,1952482.819]);
		},

		updateImageLayerFromGeoserverMNSC003(vid, center, radius, isRequest) {
			//_imageSource = null;

			//const dataMap = CMSC003.Storage.get('searchDataMap');
			//const video = dataMap[vid];
			//const transform = CMSC003.GIS.transformExtent([video.ltopCrdntY, video.ltopCrdntX, video.rbtmCrdntY, video.rbtmCrdntX], video.mapPrjctnCn, 'EPSG:5179');
			const extent = isRequest ? CMSC003.Storage.get('requestROIExtent') : CMSC003.GIS.getROIExtent();
			const polygon = CMSC003.GIS.extentToPolygon(extent);

			const _imageSource = new ol.source.ImageWMS({
				ratio: 1,
				//				url: 'http://192.168.0.12:9999/geoserver/wms?service=WMS',
				//url: CMSC003.Const.geoserver + 'wms?service=WMS',
				url: contextPath + "/geoserver/proxy.do?",
				params: {
					'VERSION': '1.1.1',
					//"CQL_FILTER": 'INTERSECTS(geom,POLYGON((956546.691 1964815.771, 956546.691 1952482.819, 969453.851 1952482.819, 969453.851 1964815.771, 956546.691  1964815.771)))',
					"CQL_FILTER": 'INTERSECTS(geom,' + polygon + ')',
					//"LAYERS": "vector:N3A_B0010000",
					"LAYERS": vid,
					"exceptions": 'application/vnd.ogc.se_inimage',
					"WIDTH": "256",
					"HEIGHT": "256"
				}
			})
			_imageSource.on('imageloaderror', function() {
				//				CMSC003.DOM.showSpinner(false);
				CMSC003.DOM.showErrorMsg('썸네일을 조회할 수 없습니다.');
				const btn = $('#vido-id-' + vid).siblings('button');
				//					console.log(btn);

				if ($(btn).hasClass('ui-btn-clicked-layer-preview')) {
					$(btn).removeClass('ui-btn-clicked-layer-preview');
					CMSC003.GIS.removeLayerById(vid);
				}
			});

			const _imageLayer = new ol.layer.Image({
				source: _imageSource,
				zIndex: 1000
			});

			const existLayer = isRequest ? _requestLayers[vid] : _previewLayers[vid];
			if (existLayer) {
				_map.removeLayer(existLayer);
			}
			if (isRequest) {
				_requestLayers[vid] = _imageLayer;
			} else {
				_previewLayers[vid] = _imageLayer;
			}
			_map.addLayer(_imageLayer);

			CMSC003.GIS.setMapCenter([(extent[0] + extent[2]) / 2, (extent[1] + extent[3]) / 2]);
			//CMSC003.GIS.setMapCenter([969453.851,1952482.819]);
		},

		updateImageLayerFromGeoserverDemographic(vid, isRequest, map) {
			//_imageSource = null;
			let targetMap = _map;
			if(map) {
				targetMap = map;
			}
			//const dataMap = CMSC003.Storage.get('searchDataMap');
			//const video = dataMap[vid];
			//const transform = CMSC003.GIS.transformExtent([video.ltopCrdntY, video.ltopCrdntX, video.rbtmCrdntY, video.rbtmCrdntX], video.mapPrjctnCn, 'EPSG:5179');
			let extent = isRequest ? CMSC003.Storage.get('requestROIExtent') : CMSC003.GIS.getROIExtent();
			if(!extent && isRequest) {
				const dataMap = CMSC003.Storage.get('requestDataMap');
				const layer = dataMap[vid];
				if(layer) {
					if(layer.ulx5179 && layer.lry5179 && layer.lrx5179 && layer.uly5179) {
						extent = [parseFloat(layer.ulx5179), parseFloat(layer.lry5179), parseFloat(layer.lrx5179), parseFloat(layer.uly5179)];
					} else if(layer.ltopCrdntX && layer.rbtmCrdntY && layer.rbtmCrdntX && layer.ltopCrdntY){
						// extent = [parseFloat(layer.rbtmCrdntY), parseFloat(layer.ltopCrdntX), parseFloat(layer.ltopCrdntY), parseFloat(layer.rbtmCrdntX)];
						extent = [parseFloat(layer.ltopCrdntY), parseFloat(layer.rbtmCrdntX), parseFloat(layer.rbtmCrdntY), parseFloat(layer.ltopCrdntX)]; //minx, miny, maxx, maxy
					}
				}
				
			}
			const polygon = CMSC003.GIS.extentToPolygon(extent);

			const _imageSource = new ol.source.ImageWMS({
				ratio: 1,
				//				url: 'http://192.168.0.12:9999/geoserver/wms?service=WMS',
				//url: CMSC003.Const.geoserver + 'wms?service=WMS',
				url: contextPath + "/geoserver/proxy.do?",
				params: {
					'VERSION': '1.1.1',
					//"CQL_FILTER": 'INTERSECTS(geom,POLYGON((956546.691 1964815.771, 956546.691 1952482.819, 969453.851 1952482.819, 969453.851 1964815.771, 956546.691  1964815.771)))',
					"CQL_FILTER": 'INTERSECTS(the_geom,' + polygon + ')',
					//"LAYERS": "vector:N3A_B0010000",
					"LAYERS": vid,
					"exceptions": 'application/vnd.ogc.se_inimage',
					"WIDTH": "256",
					"HEIGHT": "256"
				}
			});
			//			_imageSource.on('imageloadstart', function() {
			//				CMSC003.DOM.showSpinner(true);
			//			});
			//			_imageSource.on('imageloadend', function() {
			//				CMSC003.DOM.showSpinner(false);
			//			});
			_imageSource.on('imageloaderror', function() {
				//				CMSC003.DOM.showSpinner(false);
				CMSC003.DOM.showErrorMsg('썸네일을 조회할 수 없습니다.');
				const btn = $('#vido-id-' + vid).siblings('button');
				//					console.log(btn);

				if ($(btn).hasClass('ui-btn-clicked-layer-preview')) {
					$(btn).removeClass('ui-btn-clicked-layer-preview');
					CMSC003.GIS.removeLayerById(vid, isRequest, map);
				}
			});

			const _imageLayer = new ol.layer.Image({
				source: _imageSource,
				zIndex: 1000
			});

			const existLayer = isRequest ? _requestLayers[vid] : _previewLayers[vid];
			if (existLayer) {
				targetMap.removeLayer(existLayer);
			}
			if (isRequest) {
				_requestLayers[vid] = _imageLayer;
			} else {
				_previewLayers[vid] = _imageLayer;
			}
			targetMap.addLayer(_imageLayer);

			CMSC003.GIS.setMapCenter([(extent[0] + extent[2]) / 2, (extent[1] + extent[3]) / 2], targetMap);
			//CMSC003.GIS.setMapCenter([969453.851,1952482.819]);
		},

		getVectorList(callback) {
			//			const url = CMSC003.Const.geoserver + "rest/workspaces/vector/featuretypes.json";
			const url = "http://192.168.0.12:9999/geoserver/rest/workspaces/vector/featuretypes.json";
			const xhr = new XMLHttpRequest();
			xhr.open("GET", url);

			//			xhr.setRequestHeader("Accept", "application/json");
			//			xhr.setRequestHeader("Content-Type", "application/json");

			xhr.onreadystatechange = function() {
				if (xhr.readyState === 4) {
					console.log(xhr.status);
					console.log(xhr.responseText);
				}
			};
			xhr.send();
		},

		updateROIExtentBy4326(ulx, uly, lrx, lry) {
			return _updateROIExtentBy4326(ulx, uly, lrx, lry);
		},

		updateSHPLayer(geojson, lid, isRequest) {
			return _updateSHPLayer(geojson, lid, isRequest);
		},

		updateSHPLayerDigitalMap(geojson, lid, isRequest) {
			return _updateSHPLayerDigitalMap(geojson, lid, isRequest);
		},

		updateSHPLayerDigitalMapAfterRequest(geojson, lid) {
			return _updateSHPLayerDigitalMapAfterRequest(geojson, lid);
		},


		createBufferROI(coordinate, radius) {
			return _createBufferROI(coordinate, radius);
		},

		createROICenter(coordinate) {
			return _createROICenter(coordinate);
		},

		createPolygonROIfrom5179(coordinate, layer, map) {
			return _createPolygonROIfrom5179(coordinate, layer, map);
		},

		resetLayers(map) {
			if(!map) {
				map = _map;
			}
			const layers = map.getLayers();
			const removingLayers = [];
			for (let i = 0; i < layers.getLength(); i++) {
				const layer = layers.item(i);
				if (!layer.get('isRequired')) {
					removingLayers.push(layer);
				}
			}
			for (let i = 0; i < removingLayers.length; i++) {
				map.removeLayer(removingLayers[i]);
			}
		},

		hideLayerById(lid, isRequest) {
			const layer = isRequest ? _requestLayers[lid] : _previewLayers[lid];
			if (layer) {
				layer.setVisible(false);
			}
		},

		convert4326To5179Coordinate(coordinate) {
			return convert4326To5179Coordinate(coordinate);
		},

		registProj4() {
			return registProj4();
		},

		createBufferROIMNSC003(coordinate, radius) {
			return _createBufferROIMNSC003(coordinate, radius);
		}


	}
})();

