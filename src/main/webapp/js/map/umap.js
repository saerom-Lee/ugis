/**
 * @author bys
 * @since 2021.09.24
 * @alias 맵 초기화기능.
 * @type {Select}
 */
const Umap = class {
    /**
     * 생성자
     * @param {Element|string} target 타겟
     * @param {ol.View} view 뷰
     * @param {Array.<string>} moduleNames 모듈명 목록
     */
    constructor(target) {
        this.map = this.createMap(target);
        this.interactions = [];
        this.listeners = [];
        this.modules = {};
        // if (moduleNames) {
        //
        //     if (moduleNames.includes("select")) {
        //         this.modules["select"] = new Select(this);
        //     }

//        }
    }
    getMap() {
        return this.map;
    }

    clearInteraction() {
        this.interactions.forEach((interaction) => {
            this.map.removeInteraction(interaction);
        });
        this.interactions = [];
        this.notify();
    }

    setInteractions(interactions) {
        this.clearInteraction();
        interactions.forEach((interaction) => {
            this.map.addInteraction(interaction);
        });
        this.interactions = interactions;
    }

    /**
     * 지도 생성
     * @param {Element|string} target 타겟
     * @param {ol.View} view 뷰
     * @returns
     */
    createMap(target) {
        const map = new ol.Map({
            target: target,
            layers: [],
            controls: this.defaultControls(),
            view: new ol.View({
                projection:new ol.proj.Projection({
                    code:"EPSG:5179",
                    units:'m',
                    axisOrientation:'enu',
                    extent:[-200000.0, -3015.4524155292, 3803015.45241553, 4000000.0]
                }),
                resolutions:[2088.96, 1044.48, 522.24, 261.12, 130.56, 65.28, 32.64,
                    16.32, 8.16, 4.08, 2.04, 1.02, 0.51, 0.255],
                center:[960363.606552286,1920034.9139856],
                zoom:0
            })
        });

        return map;
    }
    /**
     * 기본 상호작용
     * @returns {Array.<ol.Interaction>} 상호작용 목록
     */
    defaultInteractions() {
        // 더블클릭 확대 막음. DrawEnd와 충돌남
        const interactions = defaultInteractions({
            doubleClickZoom: false,
        });
        return interactions;
    }
    /**
     * 기본 컨트롤
     * @returns {Array.<ol.Control>} 컨트롤 목록
     */
    defaultControls() {
        const controls = [];
        return controls;
    }

    baselayer(){
        var baseLyr1 = new ol.layer.Tile({
			isRequired: true,
            source: new ol.source.WMTS({
                projection:"EPSG:5179",
                tileGrid:new ol.tilegrid.WMTS({
                    extent:[-200000.0, -3015.4524155292, 3803015.45241553, 4000000.0],
                    origin:[-200000.0,4000000.0],
                    tileSize:256,
                    matrixIds:["L05","L06","L07","L08","L09","L10","L11","L12","L13","L14","L15","L16","L16","L17","L18"],
                    resolutions:[2088.96, 1044.48, 522.24, 261.12, 130.56, 65.28, 32.64,
                        16.32, 8.16, 4.08, 2.04, 1.02, 0.51, 0.255],

                }),
                layer:"korean_map",
                style:"korean",
                format:"image/png",
                matrixSet:"korean",
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
            visible:true
        })
        return baseLyr1 ;
    }
}


