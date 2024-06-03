/**
 * @author bys
 * @since 2021.09.24
 * @alias 맵 초기화기능.
 * @type {Select}
 */
const MapAera = class {
    constructor(container, store) {
        this.store = store;
        this.context = this.store["context"];
        this.data = this.store["data"].getData();
        this.element = this.render(container);
        // 지도 생성 및 설정
        const moduleNames = ["measure", "zoomSlider", "history", "select", "highlight", "dragZoom"];
        this.dMap = new UMap("map", createView(), moduleNames);
        this.store.setUMap(this.dMap);

        // 항공지도

        // WMS 레이어
        setWMSLayers(this.data, this.dMap, this.context);

        // 지도 선택
        new MapSelect(this.element, this.store, this.setBackgroundLayers.bind(this));

        // 지도 컨트롤
        this.mapControl = new MapControl(this.element, this.createControlList());

        // 검색 영역
        this.searchArea = new SearchArea(this.element, this.store); // 검색 영역

        // 하단 영역
        this.btmArea = new BtmArea(this.element, this.store);

        this.bindEvents();

        // 맵 초기화 이후 메뉴 초기화
        this.store["menu"].setPanelByName("TotalSearchPanel");
    }




}
