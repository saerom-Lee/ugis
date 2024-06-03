/**
 * SHP Converter 관련 네임스페이스
 */
CMSC003.Converter = class {
	constructor(opt) {
		const options = opt ? opt : {};
		this.url = options.url ? options.url : undefined;
		this.encoding = typeof options.encoding != 'utf-8' ? options.encoding : 'utf-8';
		this.EPSG = typeof options.EPSG != 'undefined' ? options.EPSG : 4326;
		this.inputData = {};
		this.EPSGUser = undefined;

		this.EPSGDest = proj4('EPSG:5179');
	}

	loadshp(returnData) {
		const reader = new FileReader();
		reader.onload = (e) => {
			const URL = window.URL || window.webkitURL || window.mozURL || window.msURL;
			const zip = new JSZip(e.target.result);
			const shpString = zip.file(/.shp$/i)[0].name;
			const dbfString = zip.file(/.dbf$/i)[0].name;
			const prjString = zip.file(/.prj$/i)[0];
			if (prjString) {
				try {
					proj4.defs('EPSGUSER', zip.file(prjString.name).asText());
					this.EPSGUser = proj4('EPSGUSER');
				} catch (e) {
					this.EPSGUser = proj4('EPSG:5186');
				}
				// try {
				// this.EPSGUser = proj4('EPSGUSER');
				// } catch (e) {
				// console.error('Unsuported Projection: ' + e);
				// }
			} else {
				this.EPSGUser = proj4('EPSG:5186');
			}

			SHPParser.load(URL.createObjectURL(new Blob([zip.file(shpString).asArrayBuffer()])), this.shpLoader, returnData, this);
			DBFParser.load(URL.createObjectURL(new Blob([zip.file(dbfString).asArrayBuffer()])), this.encoding, this.dbfLoader, returnData, this);
		}
		if (this.url) {
			reader.readAsArrayBuffer(this.url);
		} else {
			if (typeof returnData === 'function') {
				returnData(null);
			}
		}
	}

	TransCoord(x, y) {
		if (proj4) {
			const p = proj4(this.EPSGUser, this.EPSGDest, [parseFloat(x), parseFloat(y)]);
			return { x: p[0], y: p[1] };
		}
	}

	shpLoader(data, returnData, instance) {
		instance.inputData['shp'] = data;
		if (instance.inputData['shp'] && instance.inputData['dbf'])
			if (returnData) returnData(instance.toGeojson(instance.inputData));
	}

	dbfLoader(data, returnData, instance) {
		instance.inputData['dbf'] = data;
		if (instance.inputData['shp'] && instance.inputData['dbf'])
			if (returnData) returnData(instance.toGeojson(instance.inputData));
	}

	toGeojson(geojsonData) {
		const geojson = {};
		const features = [];
		let feature = undefined;
		let geometry = undefined;
		let points = undefined;

		const shpRecords = geojsonData.shp.records;
		const dbfRecords = geojsonData.dbf.records;

		geojson.type = "FeatureCollection";
		const min = this.TransCoord(geojsonData.shp.minX, geojsonData.shp.minY);
		const max = this.TransCoord(geojsonData.shp.maxX, geojsonData.shp.maxY);
		geojson.bbox = [
			min.x,
			min.y,
			max.x,
			max.y
		];

		geojson.features = features;

		for (let i = 0; i < shpRecords.length; i++) {
			feature = {};
			feature.type = 'Feature';
			geometry = feature.geometry = {};
			const properties = feature.properties = dbfRecords[i];
			if (properties.ftr_idn === "") {
				console.log("hi")
			}
			// point : 1 , polyline : 3 , polygon : 5, multipoint : 8
			if (shpRecords[i].hasOwnProperty('shape')) {
				switch (shpRecords[i].shape.type) {
					case 1:
						geometry.type = "Point";
						const reprj = this.TransCoord(shpRecords[i].shape.content.x, shpRecords[i].shape.content.y);
						geometry.coordinates = [
							reprj.x, reprj.y
						];
						break;
					case 3:
					case 8:
						geometry.type = (shpRecords[i].shape.type == 3 ? "LineString" : "MultiPoint");
						geometry.coordinates = [];
						for (let j = 0; j < shpRecords[i].shape.content.points.length; j += 2) {
							const reprj = this.TransCoord(shpRecords[i].shape.content.points[j], shpRecords[i].shape.content.points[j + 1]);
							geometry.coordinates.push([reprj.x, reprj.y]);
						};
						break;
					case 5:
						geometry.type = "Polygon";
						geometry.coordinates = [];

						for (let pts = 0; pts < shpRecords[i].shape.content.parts.length; pts++) {
							const partsIndex = shpRecords[i].shape.content.parts[pts];
							const part = [];
							let dataset = undefined;

							for (let j = partsIndex * 2; j < (shpRecords[i].shape.content.parts[pts + 1] * 2 || shpRecords[i].shape.content.points.length); j += 2) {
								const point = shpRecords[i].shape.content.points;
								const reprj = this.TransCoord(point[j], point[j + 1]);
								part.push([reprj.x, reprj.y]);
							};
							geometry.coordinates.push(part);

						};
						break;
					default:
						// console.log(shpRecords[i]);
						break;
				}
			} else {
				console.log(shpRecords[i]);
			}
			if ("coordinates" in feature.geometry) features.push(feature);
		};
		// console.log(JSON.stringify(geojson));
		return geojson;
	}

}

