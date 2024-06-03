/**
 * 
 */
var ctscMap = {
	map: null,
	imageLayer: null,
	select: null,
	init: function() {
		//1. 맵생성.
		this.map = createMap('map1');
		var baselyaer = baselayer();
		this.map.addLayer(baselyaer);
	
		//2. 선택기능 활성화 roi
		this.select = new Select(this.map);
		this.imageLayer = new ol.layer.Image();
		this.map.addLayer(this.imageLayer);
		const imageExtent = [957225.1387,1921135.1404,959008.6342,1922918.6358];
		this.map.getView().fit(imageExtent);
		
	},
	set: function() {
		
	}
}

ctscMap.init();