/**
 * 
 */
var ctsc005 = {

	init: function() {
		var _this = this;
		
		$('#staticSearch').on('click', function() {
			_this.search();
		})
	},
	
	yearStatic : function() {
		
		Data = {
				'satell' : $("input:checkbox[name='satell']:checked").val(),
		}
		if($("input:checkbox[name='free']:checked").val() != undefined) {
			Data['free'] = $("input:checkbox[name='free']:checked").val();
		}
		if($("input:checkbox[name='emer']:checked").val() != undefined) {
			Data['emer'] = $("input:checkbox[name='emer']:checked").val();
		}
		if($('#datepicker').val() != '') {
			Data['inqireBeginDate'] = $('#datepicker').val();
		}
		if($('#datepicker2').val() != '') {
			Data['inqireEndDate'] = $('#datepicker2').val();
		}
		
		$.ajax({
            type : 'GET',
            url : '/ctsc005/selectYearStatic.do',
            dataType : 'json',
            data : Data,
        }).done(function(success) {
        	var staticFree = success.staticFree;
        	var staticEmer = success.staticEmer;
        	var providerData = new Array(); 
        	for(var i in staticFree) {
        		providerData.push(staticFree[i]);
        	}
        	for(var i in staticEmer) {
        		providerData.push(staticEmer[i]);
        	}
        	providerData.sort(function(a, b) {
    			return a['category'] - b['category'];  
    			})
    		var hash = new Map();
    		var result = [];
    		for(var i in providerData) {
    			if(hash.has(providerData[i].category)) {
    				var idx = hash.get(providerData[i].category);
    				result[idx] = $.extend({}, providerData[idx], providerData[i]);
    			} else {
    				hash.set(providerData[i].category, i);
    				result.push(providerData[i]);
    			}
    		}
    		AmCharts.makeChart("chartdiv",
    				{
    					"type": "serial",
    					"categoryField": "category",
    					"startDuration": 1,
    					"categoryAxis": {
    						"gridPosition": "start"
    					},
    					"trendLines": [],
    					"graphs": [
    						{
    							"balloonText": "[[title]] of [[category]]:[[value]]",
    							"bullet": "round",
    							"id": "AmGraph-1",
    							"title": "무료영상",
    							"valueField": "column1"
    						},
    						{
    							"balloonText": "[[title]] of [[category]]:[[value]]",
    							"bullet": "square",
    							"id": "AmGraph-2",
    							"title": "긴급영상",
    							"valueField": "column2"
    						},
    						{
    							"balloonText": "[[title]] of [[category]]:[[value]]",
    							"bullet": "square",
    							"id": "AmGraph-3",
    							"title": "국토위성",
    							"valueField": "column3"
    						}
    					],
    					"guides": [],
    					"valueAxes": [
    						{
    							"id": "ValueAxis-1",
    							"title": "건수"
    						}
    					],
    					"allLabels": [],
    					"balloon": {},
    					"legend": {
    						"enabled": true,
    						"useGraphSettings": true
    					},
    					"titles": [
    						{
    							"id": "Title-1",
    							"size": 15,
    							"text": "연도별 통계"
    						}
    					],
    					"dataProvider": result
    				}
    			);
        }).fail(function(error) {
            alert('연도별 통계를 불러올수 없습니다');
        });
		
		
	},
	
	kindsStatic : function() {
		
		var Data = {};
		if($('#datepicker').val() != '') {
			Data['inqireBeginDate'] = $('#datepicker').val()
		}
		if($('#datepicker2').val() != '') {
			Data['inqireEndDate'] = $('#datepicker2').val()
		}
		
		$.ajax({
            type : 'GET',
            url : '/ctsc005/selectKindsStatic.do',
            dataType : 'json',
            data : Data,
        }).done(function(success) {
        	var res = success.staticKinds;
        	res = res.filter(function(item) {
        	    return item.country !== ' ';
        	});
        	for(var i in res) {
        		if(res[i].country == '1') res[i].country = 'Lasndsat';
        		if(res[i].country == '2') res[i].country = 'Kompsat';
        		if(res[i].country == '3') res[i].country = 'Flood';
        		if(res[i].country == '4') res[i].country = 'ForestFire';
        		if(res[i].country == '5') res[i].country = 'Landslide';
        		if(res[i].country == '6') res[i].country = 'Sentinal';
        	}
        	AmCharts.makeChart("chartdiv3",
    				{
    					"type": "pie",
    					"balloonText": "[[title]]<br><span style='font-size:14px'><b>[[value]]</b> ([[percents]]%)</span>",
    					"colors": [
    						"#226FCB",
    						"#B2FE96",
    						"#00B4FF",
    						"#F57806",
    						"#D1F0FF",
    						"#00CCCC"
    					],
    					"labelColorField": "#FFFFFF",
    					"outlineAlpha": 0,
    					"outlineThickness": 0,
    					"titleField": "country",
    					"valueField": "count",
    					"color": "#666666",
    					"fontFamily": "",
    					"fontSize": 12,
    					"handDrawn": false,
    					"handDrawScatter": 1,
    					"theme": "chalk",
    					"allLabels": [],
    					"balloon": {},
    					"titles": [],
    					"dataProvider": res
    				}
    			);
        }).fail(function(error) {
            alert('영상종류별 통계를 불러올수 없습니다');
        });
		
		
	},
	
	showDownload: function() {
		
	},
	
	save: function() {
		
	},
	
	search: function() {
		ctsc005.yearStatic();
	},
	
	get: function() {
		
	},
	
	set: function() {
		
	}
}

ctsc005.init();