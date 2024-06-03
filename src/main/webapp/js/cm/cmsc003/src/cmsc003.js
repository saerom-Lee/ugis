/**
 * CMSC003 JS 네임스페이스
 */
const CMSC003 = (() => {
	return {
		GIS: {},
		Storage: {},
		DOM: {},
		Converter: {},
		Util: {},
		Const: {
			geoserver: 'http://10.98.25.120:9999/geoserver/'
			//geoserver: 'http://222.108.12.130:9999/geoserver/'
		},
		init(callback) {
			//지도 초기화
			
			// 초기화 후 실행할 콜백 함수가 있다면 실행
			if(typeof callback === 'function') {
				callback();
			}
		}
	}
})();