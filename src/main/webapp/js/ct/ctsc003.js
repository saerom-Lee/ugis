/**
 * 
 */

var ctsc003 = {

	init: function() {
		$("#tab-03 .roi").off("click");
		$("#tab-03 .roi").on("click", function () {
			ctscMap.select.once("Polygon", "drawend", function(event){
				var extent = event.feature.getGeometry().extent_;
				$("#tab-03 #ulx").val(event.feature.getGeometry().extent_[0]);
				$("#tab-03 #uly").val(event.feature.getGeometry().extent_[1]);
				$("#tab-03 #lrx").val(event.feature.getGeometry().extent_[2]);
				$("#tab-03 #lry").val(event.feature.getGeometry().extent_[3]);
			},
			true);
		});
		function transform_5174(x,y){
			var pt = new Proj4js.Point(x,y);
			var result = Proj4js.transform(s_srs5174,t_srs,pt);
			return result;
		}
		$(document).ready(function() {

			$('#si').change(function () {

				var si = this.value;
				si = si.substring(0, 2);
				var minx = $("select[id=si] option:selected").attr("minx");
				var miny = $("select[id=si] option:selected").attr("miny");
				var maxx = $("select[id=si] option:selected").attr("maxx");
				var maxy = $("select[id=si] option:selected").attr("maxy");

				$.ajax({
					url: "cmsc002/sgg.do",
					type: "post",
					data: pram = {"si": si},
					v: {minx: minx, miny: miny, maxx: maxx, maxy: maxy}
				}).done(function (response) {

					var min = transform_5174(this.v.minx, this.v.miny);
					var max = transform_5174(this.v.maxx, this.v.maxy);
					var imageExtent = [min.x, min.y, max.x, max.y];
					ctscMap.map.getView().fit(imageExtent);
					var response = JSON.parse(response);
					var sgg = response.sggList;
					$('#sgg').children('option').remove();
					for (var i = 0; i < sgg.length; i++) {
						$('#sgg').append("<option value='" + sgg[i].bjcd + "' minx='" + sgg[i].stXmin + "' miny='" + sgg[i].stYmin + "' maxx='" + sgg[i].stXmax + "' maxy='" + sgg[i].stXmax + "' >" + sgg[i].name + "</option>");
					}
				});
			});

			$('#sgg').change(function () {
				var sgg = this.value;
				sgg = sgg.substring(0, 5);
				var minx = $("select[id=sgg] option:selected").attr("minx");
				var miny = $("select[id=sgg] option:selected").attr("miny");
				var maxx = $("select[id=sgg] option:selected").attr("maxx");
				var maxy = $("select[id=sgg] option:selected").attr("maxy");
				$.ajax({
					url: "cmsc002/emd.do",
					type: "post",
					data: pram = {"sgg": sgg},
					v: {minx: minx, miny: miny, maxx: maxx, maxy: maxy}
				}).done(function (response) {
					var min = transform_5174(this.v.minx, this.v.miny);
					var max = transform_5174(this.v.maxx, this.v.maxy);
					var imageExtent = [min.x, min.y, max.x, max.y];
					ctscMap.map.getView().fit(imageExtent);
					var response = JSON.parse(response);
					var emd = response.sggList;
					$('#emd').children('option').remove();
					for (var i = 0; i < emd.length; i++) {
						$('#emd').append("<option value='" + emd[i].bjcd + "' minx='" + emd[i].stXmin + "' miny='" + emd[i].stYmin + "' maxx='" + emd[i].stXmax + "' maxy='" + emd[i].stXmax + "' >" + emd[i].name + "</option>");
					}
				});
			});

			$('#emd').change(function () {
				var minx = $("select[id=emd] option:selected").attr("minx");
				var miny = $("select[id=emd] option:selected").attr("miny");
				var maxx = $("select[id=emd] option:selected").attr("maxx");
				var maxy = $("select[id=emd] option:selected").attr("maxy");
				var min = transform_5174(minx, miny);
				var max = transform_5174(maxx, maxy);
				var imageExtent = [min.x, min.y, max.x, max.y];
				ctscMap.map.getView().fit(imageExtent);


			});
		});
	},
	
	showDownload: function() {
		
	},
	
	save: function() {
		
	},
	
	search: function() {
		
	},
	
	get: function() {
		
	},
	
	set: function() {
		
	}
}

ctsc003.init();