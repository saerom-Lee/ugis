
function getContextPath() {
    var hostIndex = location.href.indexOf( location.host ) + location.host.length;
    return location.href.substring( hostIndex, location.href.indexOf('/', hostIndex + 1) );
}

Proj4js.defs["EPSG:5179"] = "+proj=tmerc +lat_0=38 +lon_0=127.5 +k=0.9996 +x_0=1000000 +y_0=2000000 +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs"; Proj4js.defs["EPSG:4326"] = "+proj=longlat +ellps=WGS84 +datum=WGS84 +no_defs";
Proj4js.defs["EPSG:4326"] = "+proj=longlat +ellps=WGS84 +datum=WGS84 +no_defs";
var t_srs = new Proj4js.Proj("EPSG:5179");
var s_srs = new Proj4js.Proj("EPSG:4326");
var contextPath = getContextPath();
const createMap =  (target)=> {
    return new ol.Map({
        target: target,
        layers: [],
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
            zoom: 0
        })
    });
}
const baselayer=()=>{
    return new ol.layer.Tile({
		type: 'default',
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
				//imageTile.getImage().src = src;
				imageTile.getImage().src = contextPath + "/tile/proxy/baseMap.do" + src;
			}
        }),
        visible: true
    }) ;
}

const  transforms=(x,y)=>{
    var pt = new Proj4js.Point(x,y);
    var result = Proj4js.transform(s_srs,t_srs,pt);
    return result;
}

/**
 * WMS 레이어 목록 설정
 * @param {Array.<Data>} data 데이터 목록
 * @param {DMap} dMap  지도
 * @param {String} context 컨텍스트
 */
 const setWMSLayers = (data, dMap, context) => {
    const layers = createWMSLayers(data, context);
    dMap.setWMSLayers(layers);
};


/**
 * WMS 레이어 생성
 * @param {Array.<Data>} data 데이터 목록
 * @param {String} context 컨텍스트
 * @returns
 */
 const createWMSLayers = (data, context) => {
    const layers = [];
    data.forEach((item) => {
        const layer = new ImageLayer({
            source: new ImageWMS({
                url: `${context}/map/wms.do`,
                params: { LAYERS: item["nm"], SLD_BODY: item["style"] },
                serverType: "geoserver",
                type: "wms",
                imageLoadFunction: function (imageTile, src) {
                    // Get 으로 전송하는 경우 SLD_BODY 와 SQL_FILTER 가 동시에 적용되지 않아 POST로만 요청
                    const index = src.indexOf("SLD_BODY");
                    const sldBody = src.substring(index, src.indexOf("&", index));
                    const imageSrc = src.replace(sldBody, "");
                    const body = decodeURIComponent(sldBody.replace("SLD_BODY=", ""));
                    fetch(imageSrc, {
                        method: "POST",
                        body: body,
                    })
                        .then((response) => {
                            if (response.ok) {
                                return response.blob();
                            }
                        })
                        .then(function (blob) {
                            const objectUrl = URL.createObjectURL(blob);
                            imageTile.getImage().src = objectUrl;
                        });
                },
            }),
            visible: item["lyrIndictAt"] == "Y" ? true : false,
            opacity: item["opacity"] ? item["opacity"] / 100 : 1,
        });
        layer.set("name", item["nm"]);
        layers.push(layer);
    });
    return layers;
};



/**
 * 배경지도 레이어 목록 생성1
 */
 const createBackgroundLayers = (backgroundMapNo, backgroundMapData) => {
    const backgroundMaps = [];
    const findMap = backgroundMapData.find(
        (map) => map.bcrnMapNo == backgroundMapNo
    );
    if (findMap) {
        // 하이브리드의 경우 항공지도가 필요
        if (findMap["bassBcrnMapNo"]) {
            const findBassMap = backgroundMapData.find(
                (map) => map.bcrnMapNo == findMap["bassBcrnMapNo"]
            );
            if (findBassMap) {
                backgroundMaps.push(createBackgroundLayer(findBassMap));
            }
        }
        backgroundMaps.push(createBackgroundLayer(findMap));
    }
    return backgroundMaps;
};

/**
 * 배경지도 레이어 생성
 * @param {Object} backgroundMap 배경 지도
 */
 const createBackgroundLayer = (backgroundMap) => {
    let layer = null;
    const bcrnMapTy = backgroundMap.bcrnMapTy;
     if (bcrnMapTy == "EMAP") {
        layer = createEmapBackgroundMap(backgroundMap["urls"]);
    }
    return layer;
};

/**
 * 바로e맵 배경지도 생성
 * @param {Array.<string>} urls 주소 목록
 * @returns {ol.layer.Tile} 타일 레이어
 */
 function  createEmapBackgroundMap  (urls) {
    return new ol.layer.Tile({
        zIndex: 1,
        source: new ol.source.WMTS({
            projection: "EPSG:5179",
            urls: urls,
            tileGrid: new ol.tilegrid.WMTS({
                extent: [-200000.0, -3015.4524155292, 3803015.45241553, 4000000.0],
                origin: [-200000.0, 4000000.0],
                tileSize: 256,
                matrixIds: [
                    "L05",
                    "L06",
                    "L07",
                    "L08",
                    "L09",
                    "L10",
                    "L11",
                    "L12",
                    "L13",
                    "L14",
                    "L15",
                    "L16",
                    "L16",
                    "L17",
                    "L18",
                ],
                resolutions: [
                    2088.96, 1044.48, 522.24, 261.12, 130.56, 65.28, 32.64, 16.32, 8.16,
                    4.08, 2.04, 1.02, 0.51, 0.255,
                ],
            }),
            layer: "korean_map",
            style: "korean",
            format: "image/png",
            matrixSet: "korean",
            tileLoadFunction: function (imageTile, src) {
                src = src.replace("TileMatrix", "tilematrix");
                src = src.replace("TileRow", "tilerow");
                src = src.replace("TileCol", "tilecol");
                imageTile.getImage().src = src + "&";
            }
        }),
    });
};


function  createView  (option) {
    if (!option) {
        option = {};
    }
    const center = option["center"] || [163962, 354246.5];
    const zoom = option["zoom"] || 11;
    return new ol.View({
        projection: "EPSG:5187",
        center: center,
        zoom: zoom,
        minZoom: 11,
        maxZoom: 19,
        constrainResolution: true,
    });
};
