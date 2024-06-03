<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko" data-dark="false">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>위성 기반의 긴급 공간정보(G119) 제공 서비스</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=1190">
<meta name="apple-mobile-web-app-title" content="" />
<meta name="robots" content="index,nofollow" />
<meta name="description" content="" />
<meta property="og:title" content="">
<meta property="og:url" content="">
<meta property="og:image" content="">
<meta property="og:description" content="" />

<link rel="stylesheet"
	href="js/cm/cmsc003/lib/jstree-3.2.1/themes/default/style.min.css">
<style>
#cmsc003-modal table.js-table-disaster-result tbody tr:hover {
	background-color: #ededed;
	cursor: pointer;
}
.ui-btn-clicked-layer-preview > a > button {
	box-shadow: inset 1px 1px 2px 1px #626262;
	background-color: #cbcbcb !important;
}
.jstree-node > a > button {
	background-color:transparent;
	padding: 2px 5px;
	margin: 2px 4px 0px 4px;
}
.js-tree-parent-1 span, .js-tree-parent-2 span, .js-tree-parent-3 span, .js-tree-child-1 span, .js-tree-child-2 span, .js-tree-child-3 span {
	display: inline-block;
	overflow-x: hidden;
	text-overflow: ellipsis;
}

.js-tree-parent-1 span {
	font-weight: bold;
	max-width: 191px;
}

.js-tree-parent-2 span {
	font-weight: bold;
	max-width: 168px;
}

.js-tree-parent-3 span {
	font-weight: bold;
	max-width: 144px;
}

.js-tree-child-1 span {
	max-width: 168px;
}

.js-tree-child-2 span {
	max-width: 144px;
}

.js-tree-child-3 span {
	max-width: 120px;
}
</style>

<script src="js/jquery.min.js"></script>


<%--<script src="https://cdnjs.cloudflare.com/ajax/libs/proj4js/1.1.0/proj4js-combined.min.js"></script>--%>
<script type="text/javascript" src="js/map/proj4js-combined.js"></script>

<script src="js/cm/cmsc003/lib/proj4js/proj4.js"></script>

<script src="js/map/v6.7.0/ol.js"></script>
<link rel="stylesheet" href="/js/map/v6.7.0/ol.css">

<script type="text/javascript">
/* 	function openCity(evt, cityName) {
var i, x, tablinks;
	x = document.getElementsByClassName("city");
	for (i = 0; i < x.length; i++) {
	x[i].style.display = "none";
	}
	tablinks = document.getElementsByClassName("tablink");
	for (i = 0; i < x.length; i++) {
	tablinks[i].className = tablinks[i].className.replace(" w3-red", "");
	}
	document.getElementById(cityName).style.display = "block";
	evt.currentTarget.className += " w3-red";
}  */
    Proj4js.defs["EPSG:5179"] = "+proj=tmerc +lat_0=38 +lon_0=127.5 +k=0.9996 +x_0=1000000 +y_0=2000000 +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs"; 
    Proj4js.defs["EPSG:4326"] = "+proj=longlat +ellps=WGS84 +datum=WGS84 +no_defs";
    Proj4js.defs["EPSG:5186"] = '+proj=tmerc +lat_0=38 +lon_0=127 +k=1 +x_0=200000 +y_0=600000 +ellps=GRS80 +units=m +no_defs';
    Proj4js.defs["EPSG:32652"] = '+proj=utm +zone=52 +ellps=WGS84 +datum=WGS84 +units=m +no_def';
    Proj4js.defs["EPSG:5187"] = '+proj=tmerc +lat_0=38 +lon_0=129 +k=1 +x_0=200000 +y_0=600000 +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs';
    Proj4js.defs["EPSG:5185"] = '+proj=tmerc +lat_0=38 +lon_0=125 +k=1 +x_0=200000 +y_0=600000 +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs';
    
    var t_srs = new Proj4js.Proj("EPSG:5179");
    var s_srs = new Proj4js.Proj("EPSG:4326");
    var srs_5186 = new Proj4js.Proj("EPSG:5186");
    var srs_32652 = new Proj4js.Proj("EPSG:32652");
    var srs_5187 = new Proj4js.Proj("EPSG:5187");
    var srs_5185 = new Proj4js.Proj("EPSG:5185");

    var selectionSource = new ol.source.Vector({ wrapX: false });
    var selectionLayer = new ol.layer.Vector({
		isRequired: true,
		zIndex: 1003,
		source: selectionSource,
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
    
    var selectionSourcePoint = new ol.source.Vector({ wrapX: false });
    var selectionLayerPoint = new ol.layer.Vector({
		isRequired: true,
		zIndex: 1003,
		source: selectionSourcePoint,
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

    $(document).ready(function(){
        $("ul#topnav li").hover(function() { //Hover over event on list item
            $(this).css({ 'background' : '#1376c9 url(topnav_active.gif) repeat-x'}); //Add background color + image on hovered list item
            $(this).find("span").show(); //Show the subnav
        } , function() { //on hover out...
            $(this).css({ 'background' : 'none'}); //Ditch the background
            $(this).find("span").hide(); //Hide the subnav
        });

        $("input:radio[name=radioInline]").click(function(){

            $('#auto_check').removeAttr('checked')
        });
        
        
        $("input[name='radioInline2']").prop("disabled", true);
        
     	// 컬러합성 재난 id 인풋 클릭
        $('#disasterIdColor').click(function(e) {
        	CMSC003.DOM.showDisasterIDSearchPopup('js-disast-color');
        });
     
     	// 히스토그램 재난 id 인풋 클릭
        $('#disasterIdHist').click(function(e) {
        	CMSC003.DOM.showDisasterIDSearchPopup('js-disast-hist');
        });
     	
     	// 모자이크 재난 id 인풋 클릭
        $('#disasterIdMosa').click(function(e) {
        	CMSC003.DOM.showDisasterIDSearchPopup('js-disast-mosa');
        });
     	
     	// 재난 id 검색결과 클릭
        $('body').on('click', '#cmsc003-modal table.js-table-disaster-result tbody tr.js-disast-hist', function(e) {
        	if ($(e.currentTarget).children('td').length === 1) {
        		return;
        	}
        	var child = $(e.currentTarget).children('td:first-child');
        	$('#disasterIdHist').val($(child).text());
        	
        	updateDisasterId(child, 'histBbox');
        });
     	
        $('body').on('click', '#cmsc003-modal table.js-table-disaster-result tbody tr.js-disast-mosa', function(e) {
        	if ($(e.currentTarget).children('td').length === 1) {
        		return;
        	}
        	var child = $(e.currentTarget).children('td:first-child');
        	$('#disasterIdMosa').val($(child).text());
        	
        	updateDisasterId(child, 'mosaBbox');
        });
        
        $('body').on('click', '#cmsc003-modal table.js-table-disaster-result tbody tr.js-disast-color', function(e) {
        	if ($(e.currentTarget).children('td').length === 1) {
        		return;
        	}
        	var child = $(e.currentTarget).children('td:first-child');
        	$('#disasterIdColor').val($(child).text());
        	
        	updateDisasterId(child, 'colorBbox');
        });
        
        // 모자이크 탭 수행 버튼 클릭
        $("#modal4").click(function(){
        	console.log(getCurrentTab());
        	// $('#mosaic_vido_result').val('MosaResult_');
			// callbackMosaic();
			
			onClickRunButton();
	    });
        
        // 히스토그램 탭 수행 버튼 클릭
        $("#mymodal2").click(function(){
        	console.log(getCurrentTab());
        	onClickRunButton();
//         	$('#input_video').children('option').remove();
//         	$('#result_video').val('HistResult_');
//         	 $('input:checkbox[name="flexCheckDefault1"]').each(function() {
//         	      if(this.checked){//checked 처리된 항목의 값
//                       $('#input_video').append("<option value='"+this.value+"' minx='"+this.getAttribute("minx")+"' miny='"+this.getAttribute("miny")+"' maxx='"+this.getAttribute("maxx")+"' maxy='"+this.getAttribute("maxy")+"'>"+this.value+"</option>");
//         	      }
//         	 });
        	 
//         	 $('input:checkbox[name="soilCheckDefault1"]').each(function() {
//        	      if(this.checked){//checked 처리된 항목의 값
//                      $('#input_video').append("<option value='"+this.value+"' minx='"+this.getAttribute("minx")+"' miny='"+this.getAttribute("miny")+"' maxx='"+this.getAttribute("maxx")+"' maxy='"+this.getAttribute("maxy")+"'>"+this.value+"</option>");
//        	      }
//        	 	});
        	 
//         	 $('input:checkbox[name="soilCheckDefault1"]').each(function() {
//        	      if(this.checked){//checked 처리된 항목의 값
//                      $('#input_video').append("<option value='"+this.value+"' minx='"+this.getAttribute("minx")+"' miny='"+this.getAttribute("miny")+"' maxx='"+this.getAttribute("maxx")+"' maxy='"+this.getAttribute("maxy")+"'>"+this.value+"</option>");

//        	      }
//        	 	});
        	 
//         	 $('input:checkbox[name="soilCheckDefault1"]').each(function() {
//        	      if(this.checked){//checked 처리된 항목의 값
//                      $('#input_video').append("<option value='"+this.value+"' minx='"+this.getAttribute("minx")+"' miny='"+this.getAttribute("miny")+"' maxx='"+this.getAttribute("maxx")+"' maxy='"+this.getAttribute("maxy")+"'>"+this.value+"</option>");
//        	      }
//        	 	});
        });
        
        $("#mymodal").click(function(){
        	onClickRunButton();
        });
        
    });
    
    function onClickRunButton() {
    	var currentTab = getCurrentTab();
    	console.log(currentTab);
    	
    	const vector = $("#resultList1 #resultList1_digital_tree").jstree().get_bottom_checked(true);
		const aerial = $("#resultList1 #resultList1_aerial_tree").jstree().get_bottom_checked(true);
		const ortho = $("#resultList1 #resultList1_ortho_tree").jstree().get_bottom_checked(true);
		const dem = $("#resultList1 #resultList1_dem_tree").jstree().get_bottom_checked(true);
		const graphics = $("#resultList1 #resultList1_graphics_tree").jstree().get_bottom_checked(true);
		const satellite = $("#resultList1 #resultList1_satellite_tree").jstree().get_bottom_checked(true);
		const earthquake = $("#resultList2 #resultList2_earthquake_tree").jstree().get_bottom_checked(true);
		const forestFire = $("#resultList2 #resultList2_forestFire_tree").jstree().get_bottom_checked(true);
		const flood = $("#resultList2 #resultList2_flood_tree").jstree().get_bottom_checked(true);
		const landslip = $("#resultList2 #resultList2_landslip_tree").jstree().get_bottom_checked(true);
		const maritime = $("#resultList2 #resultList2_maritime_tree").jstree().get_bottom_checked(true);
		const redTide = $("#resultList2 #resultList2_redTide_tree").jstree().get_bottom_checked(true);
		const vectorAnalObj = $("#resultList3 #resultList3_vectorAnalObj_tree").jstree().get_bottom_checked(true);
		const rasterAnalObj = $("#resultList3 #resultList3_rasterAnalObj_tree").jstree().get_bottom_checked(true);
		const vectorAnalChg = $("#resultList3 #resultList3_vectorAnalChg_tree").jstree().get_bottom_checked(true);
		const rasterAnalChg = $("#resultList3 #resultList3_rasterAnalChg_tree").jstree().get_bottom_checked(true);
		
    	if(currentTab === 1) {
    		
    		$('#red').children('option').remove();
            $('#green').children('option').remove();
            $('#blue').children('option').remove();
            $('#extra').children('option').remove();
            $('#colorinput').val('CompResult_');
            
            var voidOpt1 = $('<option value="none"></option>');
            var voidOpt2 = $('<option value="none"></option>');
            var voidOpt3 = $('<option value="none"></option>');
            var voidOpt4 = $('<option value="none"></option>');
            $('#red').append(voidOpt1);
            $('#green').append(voidOpt2);
            $('#blue').append(voidOpt3);
            $('#extra').append(voidOpt4);
            
//             $('input:checkbox[name="etcCheckDefault"]').each(function() {
//                 if(this.checked){//checked 처리된 항목의 값
//                     $('#red').append("<option value='"+this.value+"' minx='"+this.getAttribute("minx")+"' miny='"+this.getAttribute("miny")+"' maxx='"+this.getAttribute("maxx")+"' maxy='"+this.getAttribute("maxy")+"'>"+this.value+"</option>");
//                     $('#green').append("<option value='"+this.value+"'>"+this.value+"</option>");
//                     $('#blue').append("<option value='"+this.value+"'>"+this.value+"</option>");
//                     $('#extra').append("<option value='"+this.value+"'>"+this.value+"</option>");
//                 }
//             });
            
//             $('input:checkbox[name="soilCheckDefault"]').each(function() {
//       	      if(this.checked){//checked 처리된 항목의 값
//       	            $('#red').append("<option value='"+this.value+"' minx='"+this.getAttribute("minx")+"' miny='"+this.getAttribute("miny")+"' maxx='"+this.getAttribute("maxx")+"' maxy='"+this.getAttribute("maxy")+"'>"+this.value+"</option>");
//                       $('#green').append("<option value='"+this.value+"'>"+this.value+"</option>");
//                       $('#blue').append("<option value='"+this.value+"'>"+this.value+"</option>");
//                       $('#extra').append("<option value='"+this.value+"'>"+this.value+"</option>");
//       	      }
//       	 	});

			// kompsat 데이터
			// landsat 데이터
			// sentinel 데이터
			// cas 데이터
			satellite.forEach(function(item) {
				var list = CMSC003.Storage.get('searchDataMap');
				var sat = item.id.split('-')[2];
				var folder = item.parent.split('-')[5];
				var layer = null;
				if (sat == "kompsat") {
					layer = list['kompsat-'+item.li_attr.value];
					// innerFileCoursNm
				} else if (sat == "landsat") {
					layer = list['landsat-'+item.li_attr.value];
				} else if (sat == "sentinel") {
					layer = list['sentinel-'+item.li_attr.value];
				} else if (sat == "cas") {
					layer = list['cas-'+item.li_attr.value];
				}
				
				if(layer) {
					var opt1 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
					var opt2 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
					var opt3 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
					var opt4 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
					
					$('#red').append(opt1);
		            $('#green').append(opt2);
		            $('#blue').append(opt3);
		            $('#extra').append(opt4);
				}
			});
            
            $('#red').val('none');
            $('#green').val('none');
            $('#blue').val('none');
            $('#extra').val('none');
            
    	} else if(currentTab === 2) {
    		$('#input_video').children('option').remove();
    		
    		var voidOpt = $('<option></option>');
    		$('#input_video').append(voidOpt);
    		
        	$('#result_video').val('HistResult_');
        	
//         	 $('input:checkbox[name="flexCheckDefault1"]').each(function() {
//         	      if(this.checked){//checked 처리된 항목의 값
//                       $('#input_video').append("<option value='"+this.value+"' minx='"+this.getAttribute("minx")+"' miny='"+this.getAttribute("miny")+"' maxx='"+this.getAttribute("maxx")+"' maxy='"+this.getAttribute("maxy")+"'>"+this.value+"</option>");
//         	      }
//         	 });
        	 
//         	 $('input:checkbox[name="soilCheckDefault1"]').each(function() {
//        	      if(this.checked){//checked 처리된 항목의 값
//                      $('#input_video').append("<option value='"+this.value+"' minx='"+this.getAttribute("minx")+"' miny='"+this.getAttribute("miny")+"' maxx='"+this.getAttribute("maxx")+"' maxy='"+this.getAttribute("maxy")+"'>"+this.value+"</option>");
//        	      }
//        	 	});
        	 
//         	 $('input:checkbox[name="soilCheckDefault1"]').each(function() {
//        	      if(this.checked){//checked 처리된 항목의 값
//                      $('#input_video').append("<option value='"+this.value+"' minx='"+this.getAttribute("minx")+"' miny='"+this.getAttribute("miny")+"' maxx='"+this.getAttribute("maxx")+"' maxy='"+this.getAttribute("maxy")+"'>"+this.value+"</option>");

//        	      }
//        	 	});
        	 
//         	 $('input:checkbox[name="soilCheckDefault1"]').each(function() {
//        	      if(this.checked){//checked 처리된 항목의 값
//                      $('#input_video').append("<option value='"+this.value+"' minx='"+this.getAttribute("minx")+"' miny='"+this.getAttribute("miny")+"' maxx='"+this.getAttribute("maxx")+"' maxy='"+this.getAttribute("maxy")+"'>"+this.value+"</option>");
//        	      }
//        	 	});
        	 
        	// 항공영상 데이터
 			aerial.forEach(function(item) {
 				var list = CMSC003.Storage.get('searchDataMap');
 				var layer = list['aerial-'+item.li_attr.value];
 				console.log(layer);
 				
 				if(layer) {
 					var opt1 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
 					$('#input_video').append(opt1);
 				}
 			});
        	
 			// 정사영상 데이터
			ortho.forEach(function(item) {
				var list = CMSC003.Storage.get('searchDataMap');
				var layer = list['ortho-'+item.li_attr.value];
 				console.log(layer);
 				
 				if(layer) {
 					var opt1 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
 					$('#input_video').append(opt1);
 				}
			});
        	
 			// 위성영상 데이터
 			satellite.forEach(function(item) {
				var list = CMSC003.Storage.get('searchDataMap');
				var sat = item.id.split('-')[2];
				var folder = item.parent.split('-')[5];
				var layer = null;
				if (sat == "kompsat") {
					layer = list['kompsat-'+item.li_attr.value];
					// innerFileCoursNm
				} else if (sat == "landsat") {
					layer = list['landsat-'+item.li_attr.value];
				} else if (sat == "sentinel") {
					layer = list['sentinel-'+item.li_attr.value];
				} else if (sat == "cas") {
					layer = list['cas-'+item.li_attr.value];
				}
				
				if(layer) {
					var opt1 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
 					$('#input_video').append(opt1);
				}
			});
 			
 			// 긴급 데이터
			// 침수
			flood.forEach(function(item) {
				var list = CMSC003.Storage.get('searchDataMap');
				var layer = list['flood-'+item.li_attr.value];
				if (layer) {
					var opt1 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
 					$('#input_video').append(opt1);			
				}
			});
			// 산사태
			landslip.forEach(function(item) {
				var list = CMSC003.Storage.get('searchDataMap');
				var layer = list['landslide-'+item.li_attr.value];
				if (layer) {
					var opt1 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
 					$('#input_video').append(opt1);
				}
			});
			// 산불
			forestFire.forEach(function(item) {
				var list = CMSC003.Storage.get('searchDataMap');
				var layer = list['forestFire-'+item.li_attr.value];
				if (layer) {
					var opt1 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
 					$('#input_video').append(opt1);
				}
			});
			// 지진
			earthquake.forEach(function(item) {
				var list = CMSC003.Storage.get('searchDataMap');
				var layer = list['earthquake-'+item.li_attr.value];
				if (layer) {
					var opt1 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
 					$('#input_video').append(opt1);
				}
			});
			// 해양재난
			maritime.forEach(function(item) {
				var list = CMSC003.Storage.get('searchDataMap');
				var layer = list['maritime-'+item.li_attr.value];
				if (layer) {
					var opt1 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
 					$('#input_video').append(opt1);
				}
			});
			// 적조
			redTide.forEach(function(item) {
				var list = CMSC003.Storage.get('searchDataMap');
				var layer = list['redTide-'+item.li_attr.value];
				if (layer) {
					var opt1 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
 					$('#input_video').append(opt1);
				}
			});
			
    	} else if(currentTab === 3) {
    		$('#mosaic_vido_result').val('MosaResult_');
    		
    		$('#mosaic_vido').children('option').remove();
            $('#mosaic_vido_input').children('option').remove();
            $('#mosaic_vido_input2').children('option').remove();
            $('#mosaic_vido_input3').children('option').remove();
            $('#mosaic_vido_input4').children('option').remove();
            
            var voidOpt1 = $('<option></option>');
            var voidOpt2 = $('<option></option>');
            var voidOpt3 = $('<option></option>');
            var voidOpt4 = $('<option></option>');
            var voidOpt5 = $('<option></option>');
            $('#mosaic_vido').append(voidOpt1);
            $('#mosaic_vido_input').append(voidOpt2);
            $('#mosaic_vido_input2').append(voidOpt3);
            $('#mosaic_vido_input3').append(voidOpt4);
            $('#mosaic_vido_input4').append(voidOpt5);
            
         	// 항공영상 데이터
 			aerial.forEach(function(item) {
 				var list = CMSC003.Storage.get('searchDataMap');
 				var layer = list['aerial-'+item.li_attr.value];
 				console.log(layer);
 				
 				if(layer) {
 					var opt1 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
					var opt2 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
					var opt3 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
					var opt4 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
					var opt5 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
					
					$('#mosaic_vido').append(opt1);
					$('#mosaic_vido_input').append(opt2);
					$('#mosaic_vido_input2').append(opt3);
					$('#mosaic_vido_input3').append(opt4);
					$('#mosaic_vido_input4').append(opt5);
 				}
 			});
        	
 			// 정사영상 데이터
			ortho.forEach(function(item) {
				var list = CMSC003.Storage.get('searchDataMap');
				var layer = list['ortho-'+item.li_attr.value];
 				console.log(layer);
 				
 				if(layer) {
 					var opt1 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
					var opt2 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
					var opt3 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
					var opt4 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
					var opt5 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
					
					$('#mosaic_vido').append(opt1);
					$('#mosaic_vido_input').append(opt2);
					$('#mosaic_vido_input2').append(opt3);
					$('#mosaic_vido_input3').append(opt4);
					$('#mosaic_vido_input4').append(opt5);
 				}
			});
        	
 			// 위성영상 데이터
 			satellite.forEach(function(item) {
				var list = CMSC003.Storage.get('searchDataMap');
				var sat = item.id.split('-')[2];
				var folder = item.parent.split('-')[5];
				var layer = null;
				if (sat == "kompsat") {
					layer = list['kompsat-'+item.li_attr.value];
					// innerFileCoursNm
				} else if (sat == "landsat") {
					layer = list['landsat-'+item.li_attr.value];
				} else if (sat == "sentinel") {
					layer = list['sentinel-'+item.li_attr.value];
				} else if (sat == "cas") {
					layer = list['cas-'+item.li_attr.value];
				}
				
				if(layer) {
					var opt1 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
					var opt2 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
					var opt3 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
					var opt4 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
					var opt5 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
					
					$('#mosaic_vido').append(opt1);
					$('#mosaic_vido_input').append(opt2);
					$('#mosaic_vido_input2').append(opt3);
					$('#mosaic_vido_input3').append(opt4);
					$('#mosaic_vido_input4').append(opt5);
 					
				}
			});
 			
 			// 긴급 데이터
			// 침수
			flood.forEach(function(item) {
				var list = CMSC003.Storage.get('searchDataMap');
				var layer = list['flood-'+item.li_attr.value];
				if (layer) {
					var opt1 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
					var opt2 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
					var opt3 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
					var opt4 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
					var opt5 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
					
					$('#mosaic_vido').append(opt1);
					$('#mosaic_vido_input').append(opt2);
					$('#mosaic_vido_input2').append(opt3);
					
					$('#mosaic_vido_input3').append(opt4);
					$('#mosaic_vido_input4').append(opt5);
				}
			});
			// 산사태
			landslip.forEach(function(item) {
				var list = CMSC003.Storage.get('searchDataMap');
				var layer = list['landslide-'+item.li_attr.value];
				if (layer) {
					var opt1 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
					var opt2 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
					var opt3 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
					var opt4 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
					var opt5 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
					
					$('#mosaic_vido').append(opt1);
					$('#mosaic_vido_input').append(opt2);
					$('#mosaic_vido_input2').append(opt3);
					$('#mosaic_vido_input3').append(opt4);
					$('#mosaic_vido_input4').append(opt5);
				}
			});
			// 산불
			forestFire.forEach(function(item) {
				var list = CMSC003.Storage.get('searchDataMap');
				var layer = list['forestFire-'+item.li_attr.value];
				if (layer) {
					var opt1 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
					var opt2 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
					var opt3 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
					var opt4 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
					var opt5 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
					
					$('#mosaic_vido').append(opt1);
					$('#mosaic_vido_input').append(opt2);
					$('#mosaic_vido_input2').append(opt3);
					$('#mosaic_vido_input3').append(opt4);
					$('#mosaic_vido_input4').append(opt5);
				}
			});
			// 지진
			earthquake.forEach(function(item) {
				var list = CMSC003.Storage.get('searchDataMap');
				var layer = list['earthquake-'+item.li_attr.value];
				if (layer) {
					var opt1 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
					var opt2 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
					var opt3 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
					var opt4 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
					var opt5 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
					
					$('#mosaic_vido').append(opt1);
					$('#mosaic_vido_input').append(opt2);
					$('#mosaic_vido_input2').append(opt3);
					$('#mosaic_vido_input3').append(opt4);
					$('#mosaic_vido_input4').append(opt5);
				}
			});
			// 해양재난
			maritime.forEach(function(item) {
				var list = CMSC003.Storage.get('searchDataMap');
				var layer = list['maritime-'+item.li_attr.value];
				if (layer) {
					var opt1 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
					var opt2 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
					var opt3 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
					var opt4 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
					var opt5 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
					
					$('#mosaic_vido').append(opt1);
					$('#mosaic_vido_input').append(opt2);
					$('#mosaic_vido_input2').append(opt3);
					$('#mosaic_vido_input3').append(opt4);
					$('#mosaic_vido_input4').append(opt5);
				}
			});
			// 적조
			redTide.forEach(function(item) {
				var list = CMSC003.Storage.get('searchDataMap');
				var layer = list['redTide-'+item.li_attr.value];
				if (layer) {
					var opt1 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
					var opt2 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
					var opt3 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
					var opt4 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
					var opt5 = $('<option>').prop('value', layer.fullFileCoursNm).text(layer.fullFileCoursNm);
					
					$('#mosaic_vido').append(opt1);
					$('#mosaic_vido_input').append(opt2);
					$('#mosaic_vido_input2').append(opt3);
					$('#mosaic_vido_input3').append(opt4);
					$('#mosaic_vido_input4').append(opt5);
				}
			});
            
    	}
    	
    }
    
    function getCurrentTab() {
    	//tab-01
    	var result = null;
    	if($('#tab-01').is(':visible')) {
    		result = 1;
    	} else if($('#tab-02').is(':visible')) {
    		result = 2;
    	} else if($('#tab-03').is(':visible')) {
    		result = 3;
    	} 
    	return result;
    }
    
    function updateDisasterId(child, label) {
    	CMSC003.Storage.set('currentDisasterCode', $(child).attr('data'));
    	var x = parseFloat($(child).attr('x'));
    	var y = parseFloat($(child).attr('y'));
    	
    	var bbox = $(child).attr('bbox');
    	var bbox_proj = $(child).attr('mapprjctncn');
    	
    	var positionToNumber = [x, y];

    	var transform = CMSC003.GIS.transform(positionToNumber, 'EPSG:4326', 'EPSG:5179');
    	CMSC003.Storage.set('currentDisasterSpot', positionToNumber);
    	
    	// 지도 중심점 이동
    	CMSC003.GIS.setMapCenter(transform, map);
    	
    	// ROI 중심점 포인트 생성
    	CMSC003.GIS.createROICenter(transform);	
    	//	CMSC003.GIS.createBufferROI(positionToNumber, 100);
    	
    	// ROI 생성(bbox 있을경우)
    	if (bbox) {
    		var transform_bbox = [];
    		// String => Array(float)
    		bbox.split(',').forEach(e => {
    			transform_bbox.push(parseFloat(e));
    		})
    		
    		// 좌표 변환
    		if (bbox_proj != 'EPSG:5179') {
    			transform_bbox = CMSC003.GIS.transformExtent(transform_bbox, bbox_proj, 'EPSG:5179');	
    		}
    		
    		var bbox4326 = CMSC003.GIS.convert5179To4326Extent(transform_bbox);
			var bbox5186 = CMSC003.GIS.convert5179To5186Extent(transform_bbox);
			var bbox32652 = CMSC003.GIS.convert5179To32652Extent(transform_bbox);
			
			// 4326, 5186, 5179, 32652 순서
			var bboxArray = [bbox4326, bbox5186, transform_bbox, bbox32652];
			
			CMSC003.Storage.set(label, bboxArray);
    		
    		// ROI 피처 생성
    		CMSC003.GIS.createPolygonROIfrom5179(transform_bbox, selectionLayer, map);
    		
    	}
    	
    	$('#cmsc003-modal').remove();
    }
    function searchColor(){
//     	if($("input[name='sate']:checked").length ==0){
//     		alert("위성종류를 선택하세요.");
//     		return false;
//     	}
//     	if($("#datepicker").val()==""){
//     		alert("시작일자를 입력하세요");
//     		return false;
//     	}
//      	if($("#datepicker2").val()==""){
//     		alert("종료일자를 입력하세요");
//     		return false;
//     	}
//      	var checkArr = [];
//         $("input[name='sate']:checked").each(function(e){
//             var value = $(this).val();
//             checkArr.push(value);        
//         })
        
//      	 param={
//         		checkArr:checkArr, 
//         		date1:$("#datepicker").val(),
//         		date2:$("#datepicker2").val(),
//         		dataKind:$("input[name='color_db']:checked").val()
//         	};

		if($('#disasterIdColor').val().trim() === '') {
			alert("재난 ID를 입력하세요.");
			return false;
		}
		if(!$("#dataKindCurrentColor:checked").length && ! $("#dataKindEmergencyColor:checked").length && !$("#dataKindResultColor:checked").length) {
			alert("데이터 종류를 선택하세요.");
			return false;
		}
		if(!$("input[name=dataTypeColor]:checked").length) {
			alert("검색 대상을 선택하세요.");
			return false;
		}
// 		if ($("#datepicker").val() == "") {
// 			alert("시작일자를 입력하세요");
// 			return false;
// 		}
// 		if ($("#datepicker2").val() == "") {
// 			alert("종료일자를 입력하세요");
// 			return false;
// 		}
		
		var param = {
			disasterId:$('#disasterIdColor').val(),
			dataKindCurrent: $("#dataKindCurrentColor:checked").length ? 'on' : null,
			dataKindEmergency: $("#dataKindEmergencyColor:checked").length ? 'on' : null,
			dataKindResult: $("#dataKindResultColor:checked").length ? 'on' : null, 
			dataType: $('input[name=dataTypeColor]:checked').val(),
// 			dateFrom: $("#datepicker").val(),
// 			dateTo: $("#datepicker2").val(),
			
			ulx4326:null,
			uly4326:null,
			lrx4326:null,
			lry4326:null,
			
			ulx5186:null,
			uly5186:null,
			lrx5186:null,
			lry5186:null,
			
			ulx5179:null,
			uly5179:null,
			lrx5179:null,
			lry5179:null,
			
			ulx32652:null,
			uly32652:null,
			lrx32652:null,
			lry32652:null
		};
		
		var bbox = CMSC003.Storage.get('colorBbox');
		
		if(bbox) {
			param['lrx4326'] = bbox[0][2];
			param['lry4326'] = bbox[0][1];
			param['ulx4326'] = bbox[0][0];
			param['uly4326'] = bbox[0][3];
			
			param['lrx5186'] = bbox[1][2];
			param['lry5186'] = bbox[1][1];
			param['ulx5186'] = bbox[1][0];
			param['uly5186'] = bbox[1][3];
			
			param['lrx5179'] = bbox[2][2];
			param['lry5179'] = bbox[2][1];
			param['ulx5179'] = bbox[2][0];
			param['uly5179'] = bbox[2][3];
			
			param['lrx32652'] = bbox[3][2];
			param['lry32652'] = bbox[3][1];
			param['ulx32652'] = bbox[3][0];
			param['uly32652'] = bbox[3][3];
		}
		
        $.ajax({
            // url: "cisc007/colorSearch.do",
            url: "/cmsc003searchOthers.do",
            type: "post",
            data: param
        }).done(function (response){
            var objData = response;
            if(typeof objData === 'string') {
            	objData = JSON.parse(objData); 
            }
            if(typeof objData === 'string') {
            	objData = JSON.parse(objData); 
            }
            console.log(objData);
            CMSC003.GIS.resetLayers(map);
            CMSC003.DOM.showSearchResultTree(objData, $('#colorPanel')[0], [$('#histPanel')[0], $('#mosaPanel')[0]]);
            
//             var htmlStr="";
//             var AeroList = objData.AeroList;
//             if(AeroList==null){
//         	htmlStr += "<dt>기타위성(0)</dt>";
//         	htmlStr += "<dd>기타위성 결과값이 없습니다.</dd>"; 
//         	$("#AeroResult").html(htmlStr);
// 	        }
// 	        else{
// 	            htmlStr += "<dt>기타위성("+AeroList.length+")</dt>";
// 	            for(var i=0; i<AeroList.length; i++) {

// 	            	var innerfileNm =AeroList[i].innerFileCoursNm;
// 	            	var idx = -1;
// 	            	idx = innerfileNm.lastIndexOf('\\');	            	
// 	            	if (idx < 0){
// 	            		idx = innerfileNm.lastIndexOf('/');
// 	            	}	            	
	            	
// 	            	var filePath = innerfileNm.replace(/tif/gi, "png");
// 	            	var fileName = innerfileNm.substring(idx).replace(/tif/gi, "png");
	            	
// 	            	//htmlStr += "<dd><input class='form-check-input'  name ='etcCheckDefault'  type='checkbox' minx="+AeroList[i].ltopCrdntX+" miny="+AeroList[i].ltopCrdntY+" maxx="+AeroList[i].rbtmCrdntX+" maxy="+AeroList[i].rbtmCrdntY+" value="+AeroList[i].innerFileCoursNm+" >기타위성("+AeroList[i].innerFileCoursNm+")</dd>";
// 	            	htmlStr += "<a href=javascript:imageView('"+filePath+"','"+fileName+"',"+AeroList[i].ltopCrdntX+","+AeroList[i].ltopCrdntY+","+AeroList[i].rbtmCrdntX+","+AeroList[i].rbtmCrdntY+",'"+AeroList[i].mapPrjctnCn+"');><dd><input class='form-check-input'  name ='etcCheckDefault'  type='checkbox' minx="+AeroList[i].ltopCrdntX+" miny="+AeroList[i].ltopCrdntY+" maxx="+AeroList[i].rbtmCrdntX+" maxy="+AeroList[i].rbtmCrdntY+" value="+AeroList[i].innerFileCoursNm+" >기타위성("+AeroList[i].innerFileCoursNm+")</dd></a>";
	            	
// 	                $("#AeroResult").html(htmlStr);
// 	            }
// //                 $("#mymodal").click(function(){
// //                     $('#red').children('option').remove();
// //                     $('#green').children('option').remove();
// //                     $('#blue').children('option').remove();
// //                     $('input:checkbox[name="etcCheckDefault"]').each(function() {
// //                         if(this.checked){//checked 처리된 항목의 값
// //                             $('#red').append("<option value='"+this.value+"' minx='"+this.getAttribute("minx")+"' miny='"+this.getAttribute("miny")+"' maxx='"+this.getAttribute("maxx")+"' maxy='"+this.getAttribute("maxy")+"'>"+this.value+"</option>");
// //                             $('#green').append("<option value='"+this.value+"'>"+this.value+"</option>");
// //                             $('#blue').append("<option value='"+this.value+"'>"+this.value+"</option>");
// //                         }
// //                         else{
// //                             $("#red option[value='"+this.value+"']").remove();
// //                             $("#green option[value='"+this.value+"']").remove();
// //                             $("#blue option[value='"+this.value+"']").remove();
// //                         }
// //                     });
// //                 });
// 	        }

//         	var soilList = objData.soilList;
//             if(soilList==null){
//             	htmlStr += "<dt>국토위성(0)</dt>";
//             	htmlStr += "<dd>국토위성 결과값이 없습니다.</dd>";
//             	$("#AeroResult").html(htmlStr);
//     	        }
//     	        else{
//     	            htmlStr += "<dt>국토위성("+soilList.length+")</dt>";    	            
    	            
//     	            for(var i=0; i<soilList.length; i++) {
    	               
//     	            	var innerfileNm =soilList[i].innerFileCoursNm;
//     	            	var idx = -1;
//     	            	idx = innerfileNm.lastIndexOf('\\');
    	            	
//     	            	if (idx < 0){
//     	            		idx = innerfileNm.lastIndexOf('/');
//     	            	}
    	            	
//     	            	var filePath = innerfileNm.replace(/tif/gi, "png");
//     	            	var fileName = innerfileNm.substring(idx).replace(/tif/gi, "png");
    	            	
//     	            	// htmlStr += "<dd>국토위성("+soilList[i].innerFileCoursNm+")</dd>";
//     	                htmlStr += "<a href=javascript:imageView('"+filePath+"','"+fileName+"',"+soilList[i].ltopCrdntX+","+soilList[i].ltopCrdntY+","+soilList[i].rbtmCrdntX+","+soilList[i].rbtmCrdntY+",'"+soilList[i].mapPrjctnCn+"');><dd><input class='form-check-input'  name ='soilCheckDefault'  type='checkbox' minx="+soilList[i].ltopCrdntX+" miny="+soilList[i].ltopCrdntY+" maxx="+soilList[i].rbtmCrdntX+" maxy="+soilList[i].rbtmCrdntY+" value="+soilList[i].innerFileCoursNm+" >국토위성("+soilList[i].innerFileCoursNm+")</dd></a>";
//     	                $("#AeroResult").html(htmlStr);
//     	            }
// //     	            $("#mymodal").click(function(){
// //     	            	$('#red').children('option').remove();
// //     	            	$('#green').children('option').remove();
// //     	            	$('#blue').children('option').remove();
// //     	            	 $('input:checkbox[name="soilCheckDefault"]').each(function() {
// //     	            	      if(this.checked){//checked 처리된 항목의 값
// //     	            	            $('#red').append("<option value='"+this.value+"' minx='"+this.getAttribute("minx")+"' miny='"+this.getAttribute("miny")+"' maxx='"+this.getAttribute("maxx")+"' maxy='"+this.getAttribute("maxy")+"'>"+this.value+"</option>");
// //     	                            $('#green').append("<option value='"+this.value+"'>"+this.value+"</option>");
// //     	                            $('#blue').append("<option value='"+this.value+"'>"+this.value+"</option>");
// //     	            	      }
// //     	            	      else{
// //   	                          $("#red option[value='"+this.value+"']").remove();
// //   	                          $("#green option[value='"+this.value+"']").remove();
// //   	                          $("#blue option[value='"+this.value+"']").remove();
// //     	            	      }
// //     	            	 });
// //     	            });
    	        
//     	         }
         });
    }
 
 
 // 히스토그램 검색
    function search_hist() {

//         if ($("input[name='sate1']:checked").length == 0) {
//             alert("위성종류를 선택하세요.");
//             return false;
//         }
//         if ($("#datepicker3").val() == "") {
//             alert("시작일자를 입력하세요");
//             return false;
//         }
//         if ($("#datepicker4").val() == "") {
//             alert("종료일자를 입력하세요");
//             return false;
//         }
//         if ($("#disasterIdHist").val() == "") {
//             alert("재난 ID를 입력하세요");
//             return false;
//         }
//         var checkArr = [];
//         $("input[name='sate1']:checked").each(function (e) {
//             var value = $(this).val();
//             checkArr.push(value);
//         })
//         param = {
//         		checkArr: checkArr, 
//         		date1: $("#datepicker3").val(), 
//         		date2: $("#datepicker4").val(),
//         		dataKind:$("input[name='histogram_db']:checked").val(),
//         		disasterId: $("#disasterIdHist").val()
//        		};
        
        if($('#disasterIdHist').val().trim() === '') {
			alert("재난 ID를 입력하세요.");
			return false;
		}
		if(!$("#dataKindCurrentHist:checked").length && ! $("#dataKindEmergencyHist:checked").length && !$("#dataKindResultHist:checked").length) {
			alert("데이터 종류를 선택하세요.");
			return false;
		}
		if(!$("input[name=dataTypeHist]:checked").length) {
			alert("검색 대상을 선택하세요.");
			return false;
		}
// 		if ($("#datepicker3").val() == "") {
// 			alert("시작일자를 입력하세요");
// 			return false;
// 		}
// 		if ($("#datepicker4").val() == "") {
// 			alert("종료일자를 입력하세요");
// 			return false;
// 		}
		
		var param = {
			disasterId:$('#disasterIdHist').val(),
			dataKindCurrent: $("#dataKindCurrentHist:checked").length ? 'on' : null,
			dataKindEmergency: $("#dataKindEmergencyHist:checked").length ? 'on' : null,
			dataKindResult: $("#dataKindResultHist:checked").length ? 'on' : null, 
			dataType: $('input[name=dataTypeHist]:checked').val(),
// 			dateFrom: $("#datepicker3").val(),
// 			dateTo: $("#datepicker4").val(),
			
			ulx4326:null,
			uly4326:null,
			lrx4326:null,
			lry4326:null,
			
			ulx5186:null,
			uly5186:null,
			lrx5186:null,
			lry5186:null,
			
			ulx5179:null,
			uly5179:null,
			lrx5179:null,
			lry5179:null,
			
			ulx32652:null,
			uly32652:null,
			lrx32652:null,
			lry32652:null
		};
			
		var bbox = CMSC003.Storage.get('histBbox');
		
		if(bbox) {
			param['lrx4326'] = bbox[0][2];
			param['lry4326'] = bbox[0][1];
			param['ulx4326'] = bbox[0][0];
			param['uly4326'] = bbox[0][3];
			
			param['lrx5186'] = bbox[1][2];
			param['lry5186'] = bbox[1][1];
			param['ulx5186'] = bbox[1][0];
			param['uly5186'] = bbox[1][3];
			
			param['lrx5179'] = bbox[2][2];
			param['lry5179'] = bbox[2][1];
			param['ulx5179'] = bbox[2][0];
			param['uly5179'] = bbox[2][3];
			
			param['lrx32652'] = bbox[3][2];
			param['lry32652'] = bbox[3][1];
			param['ulx32652'] = bbox[3][0];
			param['uly32652'] = bbox[3][3];
		}
		
        $.ajax({
            // url: "cisc007/colorSearch.do",
            url: "/cmsc003searchOthers.do",
            type: "post",
            data: param
        }).done(function (response) {
        	
        	var objData = response;
            if(typeof objData === 'string') {
            	objData = JSON.parse(objData); 
            }
            if(typeof objData === 'string') {
            	objData = JSON.parse(objData); 
            }
            console.log(objData);
            CMSC003.GIS.resetLayers(map);
            CMSC003.DOM.showSearchResultTree(objData, $('#histPanel')[0], [$('#colorPanel')[0], $('#mosaPanel')[0]]);
            
//             var htmlStr = "";
//             var objData = JSON.parse(response);
//             var HistList = objData.AeroList;
//             if (HistList == null) {
//                 htmlStr += "<dt>기타위성(0)</dt>";
//                 htmlStr += "<dd>기타위성 결과값이 없습니다.</dd>";
//                 $("#HistResult").html(htmlStr);
//             } else {
//                 htmlStr += "<dt>기타위성(" + HistList.length + ")</dt>";
//                 for (var i = 0; i < HistList.length; i++) {
                	
//                 	var innerfileNm =HistList[i].innerFileCoursNm;
// 	            	var idx = -1;
// 	            	idx = innerfileNm.lastIndexOf('\\');
	            	
// 	            	if (idx < 0){
// 	            		idx = innerfileNm.lastIndexOf('/');
// 	            	}
	            	
// 	            	var filePath = innerfileNm.replace(/tif/gi, "png");
// 	            	var fileName = innerfileNm.substring(idx).replace(/tif/gi, "png");
	            	
// 	            	// htmlStr += "<dd>국토위성("+soilList[i].innerFileCoursNm+")</dd>";
//                 	htmlStr += "<a href=javascript:imageView('"+filePath+"','"+fileName+"',"+HistList[i].ltopCrdntX+","+HistList[i].ltopCrdntY+","+HistList[i].rbtmCrdntX+","+HistList[i].rbtmCrdntY+",'"+HistList[i].mapPrjctnCn+"');><dd><input class='form-check-input'  name ='flexCheckDefault1' id ='soilCheckDefault1'  type='checkbox' minx="+HistList[i].ltopCrdntX+" miny="+HistList[i].ltopCrdntY+" maxx="+HistList[i].rbtmCrdntX+" maxy="+HistList[i].rbtmCrdntY+" value="+HistList[i].innerFileCoursNm+" >기타위성("+HistList[i].innerFileCoursNm+")</dd></a>";
//                     $("#HistResult").html(htmlStr);
//                 }
// //                 $("#mymodal2").click(function(){
// // 	            	$('#flexCheckDefault1').children('option').remove();
// // 	            	 $('input:checkbox[name="flexCheckDefault1"]').each(function() {
// // 	            	      if(this.checked){//checked 처리된 항목의 값
// //                               $('#input_video').append("<option value='"+this.value+"' minx='"+this.getAttribute("minx")+"' miny='"+this.getAttribute("miny")+"' maxx='"+this.getAttribute("maxx")+"' maxy='"+this.getAttribute("maxy")+"'>"+this.value+"</option>");
// // 	            	      }
// // 	            	      else{
// //                               $("#input_video option[value='"+this.value+"']").remove();
// // 	                          //$("#input_video option[value='"+this.value+"']").remove();
// // 	            	      }
// // 	            	 });
// // 	            });
//             }
//             var soilList = objData.soilList;
//             if (soilList == null) {
//                 htmlStr += "<dt>국토위성(0)</dt>";
//                 htmlStr += "<dd>국토위성 결과값이 없습니다.</dd>";
//                 $("#HistResult").html(htmlStr);

//             } else {
//                 htmlStr += "<dt>국토위성(" + soilList.length + ")</dt>";
//                 for (var i = 0; i < soilList.length; i++) {
                    
//                 	var innerfileNm =soilList[i].innerFileCoursNm;
// 	            	var idx = -1;
// 	            	idx = innerfileNm.lastIndexOf('\\');
	            	
// 	            	if (idx < 0){
// 	            		idx = innerfileNm.lastIndexOf('/');
// 	            	}
	            	
// 	            	var filePath = innerfileNm.replace(/tif/gi, "png");
// 	            	var fileName = innerfileNm.substring(idx).replace(/tif/gi, "png");
	            	
// 	            	htmlStr += "<a href=javascript:imageView('"+filePath+"','"+fileName+"',"+soilList[i].ltopCrdntX+","+soilList[i].ltopCrdntY+","+soilList[i].rbtmCrdntX+","+soilList[i].rbtmCrdntY+",'"+soilList[i].mapPrjctnCn+"');><dd><input class='form-check-input'  name ='soilCheckDefault1' id ='soilCheckDefault1'  type='checkbox' minx="+soilList[i].ltopCrdntX+" miny="+soilList[i].ltopCrdntY+" maxx="+soilList[i].rbtmCrdntX+" maxy="+soilList[i].rbtmCrdntY+" value="+soilList[i].innerFileCoursNm+" >국토위성("+soilList[i].innerFileCoursNm+")</dd></a>";
                	
//                     $("#HistResult").html(htmlStr);
//                 }
// //                 $("#mymodal2").click(function(){
// // 	            	$('#soilCheckDefault1').children('option').remove();
// // 	            	 $('input:checkbox[name="soilCheckDefault1"]').each(function() {
// // 	            	      if(this.checked){//checked 처리된 항목의 값
// //                               $('#mosaic_vido').append("<option value='"+this.value+"' minx='"+this.getAttribute("minx")+"' miny='"+this.getAttribute("miny")+"' maxx='"+this.getAttribute("maxx")+"' maxy='"+this.getAttribute("maxy")+"'>"+this.value+"</option>");
// //                               $('#mosaic_vido_input').append("<option value='"+this.value+"' minx='"+this.getAttribute("minx")+"' miny='"+this.getAttribute("miny")+"' maxx='"+this.getAttribute("maxx")+"' maxy='"+this.getAttribute("maxy")+"'>"+this.value+"</option>");

// // 	            	      }
// // 	            	      else{
// // 	                          $("#mosaic_vido option[value='"+this.value+"']").remove();
// //                               $("#mosaic_vido_input option[value='"+this.value+"']").remove();
// // 	            	      }
// // 	            	 });
// // 	            });
                
//             }
//             // 항공/정사영상
//             var usgsAirList = objData.airList;
//             if (usgsAirList == null) {
//                 htmlStr += "<dt>항공/정사영상(0)</dt>";
//                 htmlStr += "<dd>항공/정사영상 결과값이 없습니다.</dd>";
//                 $("#HistResult").html(htmlStr);

//             } else {
//                 htmlStr += "<dt>항공/정사영상(" + usgsAirList.length + ")</dt>";
//                 for (var i = 0; i < usgsAirList.length; i++) {
                    
//                 	var innerfileNm =usgsAirList[i].innerFileCoursNm;
// 	            	var idx = -1;
// 	            	idx = innerfileNm.lastIndexOf('\\');
	            	
// 	            	if (idx < 0){
// 	            		idx = innerfileNm.lastIndexOf('/');
// 	            	}
	            	
// 	            	var filePath = innerfileNm.replace(/tif/gi, "png");
// 	            	var fileName = innerfileNm.substring(idx).replace(/tif/gi, "png");
	            	
// 	            	htmlStr += "<a href=javascript:imageView('"+filePath+"','"+fileName+"',"+usgsAirList[i].ltopCrdntX+","+usgsAirList[i].ltopCrdntY+","+usgsAirList[i].rbtmCrdntX+","+usgsAirList[i].rbtmCrdntY+",'"+usgsAirList[i].mapPrjctnCn+"');><dd><input class='form-check-input'  name ='soilCheckDefault1' id ='soilCheckDefault1'  type='checkbox' minx="+usgsAirList[i].ltopCrdntX+" miny="+usgsAirList[i].ltopCrdntY+" maxx="+usgsAirList[i].rbtmCrdntX+" maxy="+usgsAirList[i].rbtmCrdntY+" value="+usgsAirList[i].innerFileCoursNm+" >항공/정사영상("+usgsAirList[i].innerFileCoursNm+")</dd></a>";
                	
//                     $("#HistResult").html(htmlStr);
//                 }
// //                 $("#mymodal2").click(function(){
// // 	            	$('#soilCheckDefault1').children('option').remove();
// // 	            	 $('input:checkbox[name="soilCheckDefault1"]').each(function() {
// // 	            	      if(this.checked){//checked 처리된 항목의 값
// //                               $('#mosaic_vido').append("<option value='"+this.value+"' minx='"+this.getAttribute("minx")+"' miny='"+this.getAttribute("miny")+"' maxx='"+this.getAttribute("maxx")+"' maxy='"+this.getAttribute("maxy")+"'>"+this.value+"</option>");
// //                               $('#mosaic_vido_input').append("<option value='"+this.value+"' minx='"+this.getAttribute("minx")+"' miny='"+this.getAttribute("miny")+"' maxx='"+this.getAttribute("maxx")+"' maxy='"+this.getAttribute("maxy")+"'>"+this.value+"</option>");

// // 	            	      }
// // 	            	      else{
// // 	                          $("#mosaic_vido option[value='"+this.value+"']").remove();
// //                               $("#mosaic_vido_input option[value='"+this.value+"']").remove();
// // 	            	      }
// // 	            	 });
// // 	            });
                
//             }
            
//             // 드론영상
//             var droneList = objData.droneList;
//             if (droneList == null) {
//                 htmlStr += "<dt>드론영상(0)</dt>";
//                 htmlStr += "<dd>드론영상 결과값이 없습니다.</dd>";
//                 $("#HistResult").html(htmlStr);

//             } else {
//                 htmlStr += "<dt>드론영상(" + droneList.length + ")</dt>";
//                 for (var i = 0; i < droneList.length; i++) {
                    
//                 	var innerfileNm =droneList[i].innerFileCoursNm;
// 	            	var idx = -1;
// 	            	idx = innerfileNm.lastIndexOf('\\');
	            	
// 	            	if (idx < 0){
// 	            		idx = innerfileNm.lastIndexOf('/');
// 	            	}
	            	
// 	            	var filePath = innerfileNm.replace(/tif/gi, "png");
// 	            	var fileName = innerfileNm.substring(idx).replace(/tif/gi, "png");
	            	
// 	            	htmlStr += "<a href=javascript:imageView('"+filePath+"','"+fileName+"',"+droneList[i].ltopCrdntX+","+droneList[i].ltopCrdntY+","+droneList[i].rbtmCrdntX+","+droneList[i].rbtmCrdntY+",'"+droneList[i].mapPrjctnCn+"');><dd><input class='form-check-input'  name ='soilCheckDefault1' id ='soilCheckDefault1'  type='checkbox' minx="+droneList[i].ltopCrdntX+" miny="+droneList[i].ltopCrdntY+" maxx="+droneList[i].rbtmCrdntX+" maxy="+droneList[i].rbtmCrdntY+" value="+droneList[i].innerFileCoursNm+" >드론영상("+droneList[i].innerFileCoursNm+")</dd></a>";
                	
//                     $("#HistResult").html(htmlStr);
//                 }
// //                 $("#mymodal2").click(function(){
// // 	            	$('#soilCheckDefault1').children('option').remove();
// // 	            	 $('input:checkbox[name="soilCheckDefault1"]').each(function() {
// // 	            	      if(this.checked){//checked 처리된 항목의 값
// //                               $('#mosaic_vido').append("<option value='"+this.value+"' minx='"+this.getAttribute("minx")+"' miny='"+this.getAttribute("miny")+"' maxx='"+this.getAttribute("maxx")+"' maxy='"+this.getAttribute("maxy")+"'>"+this.value+"</option>");
// //                               $('#mosaic_vido_input').append("<option value='"+this.value+"' minx='"+this.getAttribute("minx")+"' miny='"+this.getAttribute("miny")+"' maxx='"+this.getAttribute("maxx")+"' maxy='"+this.getAttribute("maxy")+"'>"+this.value+"</option>");

// // 	            	      }
// // 	            	      else{
// // 	                          $("#mosaic_vido option[value='"+this.value+"']").remove();
// //                               $("#mosaic_vido_input option[value='"+this.value+"']").remove();
// // 	            	      }
// // 	            	 });
// // 	            });
                
//             }
            
        });
    }
 
 	// 모자이크 탭에서 수행버튼 클릭 콜백
 	function callbackMosaic() {
 		$('#mosaic_vido').children('option').remove();
        $('#mosaic_vido_input').children('option').remove();
        $('#mosaic_vido_input2').children('option').remove();
        $('#mosaic_vido_input3').children('option').remove();
        $('#mosaic_vido_input4').children('option').remove();
        
        $('input:checkbox[name="flexCheckDefault2"]').each(function() {
            if(this.checked){//checked 처리된 항목의 값
                $('#mosaic_vido').append("<option value='"+this.value+"' minx='"+this.getAttribute("minx")+"' miny='"+this.getAttribute("miny")+"' maxx='"+this.getAttribute("maxx")+"' maxy='"+this.getAttribute("maxy")+"'>"+this.value+"</option>");
                $('#mosaic_vido_input').append("<option value='"+this.value+"' minx='"+this.getAttribute("minx")+"' miny='"+this.getAttribute("miny")+"' maxx='"+this.getAttribute("maxx")+"' maxy='"+this.getAttribute("maxy")+"'>"+this.value+"</option>");
                $('#mosaic_vido_input2').append("<option value='"+this.value+"' minx='"+this.getAttribute("minx")+"' miny='"+this.getAttribute("miny")+"' maxx='"+this.getAttribute("maxx")+"' maxy='"+this.getAttribute("maxy")+"'>"+this.value+"</option>");
                $('#mosaic_vido_input3').append("<option value='"+this.value+"' minx='"+this.getAttribute("minx")+"' miny='"+this.getAttribute("miny")+"' maxx='"+this.getAttribute("maxx")+"' maxy='"+this.getAttribute("maxy")+"'>"+this.value+"</option>");
                $('#mosaic_vido_input4').append("<option value='"+this.value+"' minx='"+this.getAttribute("minx")+"' miny='"+this.getAttribute("miny")+"' maxx='"+this.getAttribute("maxx")+"' maxy='"+this.getAttribute("maxy")+"'>"+this.value+"</option>");
            }
//             else{
//                 $("#mosaic_vido option[value='"+this.value+"']").remove();
//                 $("#mosaic_vido_input option[value='"+this.value+"']").remove();
//                 $("#mosaic_vido_input2 option[value='"+this.value+"']").remove();
//                 $("#mosaic_vido_input3 option[value='"+this.value+"']").remove();
//                 $("#mosaic_vido_input4 option[value='"+this.value+"']").remove();
//             }
        });
        
        $('input:checkbox[name="soilCheckDefault2"]').each(function() {
        	if(this.checked){//checked 처리된 항목의 값
                $('#mosaic_vido').append("<option value='"+this.value+"' minx='"+this.getAttribute("minx")+"' miny='"+this.getAttribute("miny")+"' maxx='"+this.getAttribute("maxx")+"' maxy='"+this.getAttribute("maxy")+"'>"+this.value+"</option>");
                $('#mosaic_vido_input').append("<option value='"+this.value+"' minx='"+this.getAttribute("minx")+"' miny='"+this.getAttribute("miny")+"' maxx='"+this.getAttribute("maxx")+"' maxy='"+this.getAttribute("maxy")+"'>"+this.value+"</option>");
                $('#mosaic_vido_input2').append("<option value='"+this.value+"' minx='"+this.getAttribute("minx")+"' miny='"+this.getAttribute("miny")+"' maxx='"+this.getAttribute("maxx")+"' maxy='"+this.getAttribute("maxy")+"'>"+this.value+"</option>");
                $('#mosaic_vido_input3').append("<option value='"+this.value+"' minx='"+this.getAttribute("minx")+"' miny='"+this.getAttribute("miny")+"' maxx='"+this.getAttribute("maxx")+"' maxy='"+this.getAttribute("maxy")+"'>"+this.value+"</option>");
                $('#mosaic_vido_input4').append("<option value='"+this.value+"' minx='"+this.getAttribute("minx")+"' miny='"+this.getAttribute("miny")+"' maxx='"+this.getAttribute("maxx")+"' maxy='"+this.getAttribute("maxy")+"'>"+this.value+"</option>");
            }
//             else{
//                 $("#mosaic_vido option[value='"+this.value+"']").remove();
//                 $("#mosaic_vido_input option[value='"+this.value+"']").remove();
//                 $("#mosaic_vido_input2 option[value='"+this.value+"']").remove();
//                 $("#mosaic_vido_input3 option[value='"+this.value+"']").remove();
//                 $("#mosaic_vido_input4 option[value='"+this.value+"']").remove();
//             }
        });
        
        $('input:checkbox[name="airCheckDefault2"]').each(function() {
        	if(this.checked){//checked 처리된 항목의 값
                $('#mosaic_vido').append("<option value='"+this.value+"' minx='"+this.getAttribute("minx")+"' miny='"+this.getAttribute("miny")+"' maxx='"+this.getAttribute("maxx")+"' maxy='"+this.getAttribute("maxy")+"'>"+this.value+"</option>");
                $('#mosaic_vido_input').append("<option value='"+this.value+"' minx='"+this.getAttribute("minx")+"' miny='"+this.getAttribute("miny")+"' maxx='"+this.getAttribute("maxx")+"' maxy='"+this.getAttribute("maxy")+"'>"+this.value+"</option>");
                $('#mosaic_vido_input2').append("<option value='"+this.value+"' minx='"+this.getAttribute("minx")+"' miny='"+this.getAttribute("miny")+"' maxx='"+this.getAttribute("maxx")+"' maxy='"+this.getAttribute("maxy")+"'>"+this.value+"</option>");
                $('#mosaic_vido_input3').append("<option value='"+this.value+"' minx='"+this.getAttribute("minx")+"' miny='"+this.getAttribute("miny")+"' maxx='"+this.getAttribute("maxx")+"' maxy='"+this.getAttribute("maxy")+"'>"+this.value+"</option>");
                $('#mosaic_vido_input4').append("<option value='"+this.value+"' minx='"+this.getAttribute("minx")+"' miny='"+this.getAttribute("miny")+"' maxx='"+this.getAttribute("maxx")+"' maxy='"+this.getAttribute("maxy")+"'>"+this.value+"</option>");
            }
//             else{
//                 $("#mosaic_vido option[value='"+this.value+"']").remove();
//                 $("#mosaic_vido_input option[value='"+this.value+"']").remove();
//                 $("#mosaic_vido_input2 option[value='"+this.value+"']").remove();
//                 $("#mosaic_vido_input3 option[value='"+this.value+"']").remove();
//                 $("#mosaic_vido_input4 option[value='"+this.value+"']").remove();
//             }
        });
        
        $('input:checkbox[name="droneCheckDefault2"]').each(function() {
        	if(this.checked){//checked 처리된 항목의 값
                $('#mosaic_vido').append("<option value='"+this.value+"' minx='"+this.getAttribute("minx")+"' miny='"+this.getAttribute("miny")+"' maxx='"+this.getAttribute("maxx")+"' maxy='"+this.getAttribute("maxy")+"'>"+this.value+"</option>");
                $('#mosaic_vido_input').append("<option value='"+this.value+"' minx='"+this.getAttribute("minx")+"' miny='"+this.getAttribute("miny")+"' maxx='"+this.getAttribute("maxx")+"' maxy='"+this.getAttribute("maxy")+"'>"+this.value+"</option>");
                $('#mosaic_vido_input2').append("<option value='"+this.value+"' minx='"+this.getAttribute("minx")+"' miny='"+this.getAttribute("miny")+"' maxx='"+this.getAttribute("maxx")+"' maxy='"+this.getAttribute("maxy")+"'>"+this.value+"</option>");
                $('#mosaic_vido_input3').append("<option value='"+this.value+"' minx='"+this.getAttribute("minx")+"' miny='"+this.getAttribute("miny")+"' maxx='"+this.getAttribute("maxx")+"' maxy='"+this.getAttribute("maxy")+"'>"+this.value+"</option>");
                $('#mosaic_vido_input4').append("<option value='"+this.value+"' minx='"+this.getAttribute("minx")+"' miny='"+this.getAttribute("miny")+"' maxx='"+this.getAttribute("maxx")+"' maxy='"+this.getAttribute("maxy")+"'>"+this.value+"</option>");
            }
//             else{
//                 $("#mosaic_vido option[value='"+this.value+"']").remove();
//                 $("#mosaic_vido_input option[value='"+this.value+"']").remove();
//                 $("#mosaic_vido_input2 option[value='"+this.value+"']").remove();
//                 $("#mosaic_vido_input3 option[value='"+this.value+"']").remove();
//                 $("#mosaic_vido_input4 option[value='"+this.value+"']").remove();
//             }
        });
 	}
 	
    // 모자이크 검색
    function search_mosaic() {

//         if ($("input[name='sate2']:checked").length == 0) {
//             alert("위성종류를 선택하세요.");
//             return false;
//         }
//         if ($("#datepicker5").val() == "") {
//             alert("시작일자를 입력하세요");
//             return false;
//         }
//         if ($("#datepicker6").val() == "") {
//             alert("종료일자를 입력하세요");
//             return false;
//         }
//         if ($("#disasterIdMosa").val() == "") {
//             alert("재난 ID를 입력하세요");
//             return false;
//         }
//         var checkArr = [];
//         $("input[name='sate2']:checked").each(function (e) {
//             var value = $(this).val();
//             checkArr.push(value);
//         })
//         param = {
//         	checkArr: checkArr, 
//         	date1:$("#datepicker5").val(), 
//         	date2:$("#datepicker6").val(),
//         	dataKind:$("input[name='mosaic_db']:checked").val(),
//         	disasterId: $("#disasterIdMosa").val()
//         };

		if($('#disasterIdMosa').val().trim() === '') {
			alert("재난 ID를 입력하세요.");
			return false;
		}
		if(!$("#dataKindCurrentMosa:checked").length && ! $("#dataKindEmergencyMosa:checked").length && !$("#dataKindResultMosa:checked").length) {
			alert("데이터 종류를 선택하세요.");
			return false;
		}
		if(!$("input[name=dataTypeMosa]:checked").length) {
			alert("검색 대상을 선택하세요.");
			return false;
		}
// 		if ($("#datepicker5").val() == "") {
// 			alert("시작일자를 입력하세요");
// 			return false;
// 		}
// 		if ($("#datepicker6").val() == "") {
// 			alert("종료일자를 입력하세요");
// 			return false;
// 		}
		
		var param = {
			disasterId:$('#disasterIdMosa').val(),
			dataKindCurrent: $("#dataKindCurrentMosa:checked").length ? 'on' : null,
			dataKindEmergency: $("#dataKindEmergencyMosa:checked").length ? 'on' : null,
			dataKindResult: $("#dataKindResultMosa:checked").length ? 'on' : null, 
			dataType: $('input[name=dataTypeMosa]:checked').val(),
// 			dateFrom: $("#datepicker5").val(),
// 			dateTo: $("#datepicker6").val(),
			
			ulx4326:null,
			uly4326:null,
			lrx4326:null,
			lry4326:null,
			
			ulx5186:null,
			uly5186:null,
			lrx5186:null,
			lry5186:null,
			
			ulx5179:null,
			uly5179:null,
			lrx5179:null,
			lry5179:null,
			
			ulx32652:null,
			uly32652:null,
			lrx32652:null,
			lry32652:null
		};
		
		var bbox = CMSC003.Storage.get('mosaBbox');
		
		if(bbox) {
			param['lrx4326'] = bbox[0][2];
			param['lry4326'] = bbox[0][1];
			param['ulx4326'] = bbox[0][0];
			param['uly4326'] = bbox[0][3];
			
			param['lrx5186'] = bbox[1][2];
			param['lry5186'] = bbox[1][1];
			param['ulx5186'] = bbox[1][0];
			param['uly5186'] = bbox[1][3];
			
			param['lrx5179'] = bbox[2][2];
			param['lry5179'] = bbox[2][1];
			param['ulx5179'] = bbox[2][0];
			param['uly5179'] = bbox[2][3];
			
			param['lrx32652'] = bbox[3][2];
			param['lry32652'] = bbox[3][1];
			param['ulx32652'] = bbox[3][0];
			param['uly32652'] = bbox[3][3];
		}
		
        $.ajax({
            // url: "cisc007/colorSearch.do",
            url: "/cmsc003searchOthers.do",
            type: "post",
            data: param
        }).done(function (response) {

        	var objData = response;
            if(typeof objData === 'string') {
            	objData = JSON.parse(objData); 
            }
            if(typeof objData === 'string') {
            	objData = JSON.parse(objData); 
            }
            console.log(objData);
            CMSC003.GIS.resetLayers(map);
            CMSC003.DOM.showSearchResultTree(objData, $('#mosaPanel')[0], [$('#histPanel')[0], $('#colorPanel')[0]]);
            
//             var objData = JSON.parse(response);
//             var htmlStr = "";
//             // 기타 위성
//             var HistList = objData.AeroList;
//             if (HistList == null) {
//                 htmlStr += "<dt>기타위성(0)</dt>";
//                 htmlStr += "<dd>기타위성 결과값이 없습니다.</dd>";
// //                 $("#mosaic_result").html(htmlStr);
//             } else {
//                 htmlStr += "<dt>기타위성(" + HistList.length + ")</dt>";
//                 for (var i = 0; i < HistList.length; i++) {
                    
//                 	var innerfileNm =HistList[i].innerFileCoursNm;
// 	            	var idx = -1;
// 	            	idx = innerfileNm.lastIndexOf('\\');
	            	
// 	            	if (idx < 0){
// 	            		idx = innerfileNm.lastIndexOf('/');
// 	            	}
	            	
// 	            	var filePath = innerfileNm.replace(/tif/gi, "png");
// 	            	var fileName = innerfileNm.substring(idx).replace(/tif/gi, "png");

//                 	htmlStr += "<a href=javascript:imageView('"+filePath+"','"+fileName+"',"+HistList[i].ltopCrdntX+","+HistList[i].ltopCrdntY+","+HistList[i].rbtmCrdntX+","+HistList[i].rbtmCrdntY+",'"+HistList[i].mapPrjctnCn+"');><dd><input class='form-check-input'  name ='flexCheckDefault2' type='checkbox' minx="+HistList[i].ltopCrdntX+" miny="+HistList[i].ltopCrdntY+" maxx="+HistList[i].rbtmCrdntX+" maxy="+HistList[i].rbtmCrdntY+" value="+HistList[i].innerFileCoursNm+" >기타위성("+HistList[i].innerFileCoursNm+")</dd></a>";
//                     $("#mosaic_result").html(htmlStr);
//                 }
// //                 $("#modal4").click(function(){
// // //                     $('#flexCheckDefault2').children('option').remove();
// //                     callbackMosaic();
// //                 });
//             }
//             // 국토위성
//             var soilList = objData.soilList;
//             if (soilList == null) {
//                 htmlStr += "<dt>국토위성(0)</dt>";
//                 htmlStr += "<dd>국토위성 결과값이 없습니다.</dd>";
// //                 $("#mosaic_result").html(htmlStr);

//             } else {
//                 htmlStr += "<dt>국토위성(" + soilList.length + ")</dt>";
//                 for (var i = 0; i < soilList.length; i++) {
//                 	var innerfileNm =soilList[i].innerFileCoursNm;
// 	            	var idx = -1;
// 	            	idx = innerfileNm.lastIndexOf('\\');
	            	
// 	            	if (idx < 0){
// 	            		idx = innerfileNm.lastIndexOf('/');
// 	            	}
	            	
// 	            	var filePath = innerfileNm.replace(/tif/gi, "png");
// 	            	var fileName = innerfileNm.substring(idx).replace(/tif/gi, "png");

//                 	htmlStr += "<a href=javascript:imageView('"+filePath+"','"+fileName+"',"+soilList[i].ltopCrdntX+","+soilList[i].ltopCrdntY+","+soilList[i].rbtmCrdntX+","+soilList[i].rbtmCrdntY+",'"+soilList[i].mapPrjctnCn+"');><dd><input class='form-check-input'  name ='soilCheckDefault2'  type='checkbox' minx="+soilList[i].ltopCrdntX+" miny="+soilList[i].ltopCrdntY+" maxx="+soilList[i].rbtmCrdntX+" maxy="+soilList[i].rbtmCrdntY+" value="+soilList[i].innerFileCoursNm+" >국토위성("+soilList[i].innerFileCoursNm+")</dd></a>";

// //                 	$("#mosaic_result").html(htmlStr);
//                 }
// //                 $("#modal4").click(function(){
// // //                     $('input:checkbox[name="soilCheckDefault2"]').children('option').remove();
// // // 					$('#mosaic_vido').children('option').remove();
// // //                     $('#mosaic_vido_input').children('option').remove();
// // //                     $('#mosaic_vido_input2').children('option').remove();
// // //                     $('#mosaic_vido_input3').children('option').remove();
// // //                     $('#mosaic_vido_input4').children('option').remove();
// //                    callbackMosaic();
// //                 });

//             }
//             // 항공사진
//             var airList = objData.airList;
//             if (!airList) {
//                 htmlStr += "<dt>항공/정사영상(0)</dt>";
//                 htmlStr += "<dd>항공/정사영상 결과값이 없습니다.</dd>";
// //                 $("#mosaic_result").html(htmlStr);

//             } else {
//                 htmlStr += "<dt>항공/정사영상(" + airList.length + ")</dt>";
//                 for (var i = 0; i < airList.length; i++) {
//                 	var innerfileNm =airList[i].innerFileCoursNm;
// 	            	var idx = -1;
// 	            	idx = innerfileNm.lastIndexOf('\\');
	            	
// 	            	if (idx < 0){
// 	            		idx = innerfileNm.lastIndexOf('/');
// 	            	}
	            	
// 	            	var filePath = innerfileNm.replace(/tif/gi, "png");
// 	            	var fileName = innerfileNm.substring(idx).replace(/tif/gi, "png");

//                 	htmlStr += "<a href=javascript:imageView('"+filePath+"','"+fileName+"',"+airList[i].ltopCrdntX+","+airList[i].ltopCrdntY+","+airList[i].rbtmCrdntX+","+airList[i].rbtmCrdntY+",'"+airList[i].mapPrjctnCn+"');><dd><input class='form-check-input'  name ='airCheckDefault2'  type='checkbox' minx="+airList[i].ltopCrdntX+" miny="+airList[i].ltopCrdntY+" maxx="+airList[i].rbtmCrdntX+" maxy="+airList[i].rbtmCrdntY+" value="+airList[i].innerFileCoursNm+" >항공/정사영상("+airList[i].innerFileCoursNm+")</dd></a>";

// //                 	$("#mosaic_result").html(htmlStr);
//                 }
// //                 $("#modal4").click(function(){
// // //                     $('input:checkbox[name="airCheckDefault2"]').children('option').remove();
// // // 					$('#mosaic_vido').children('option').remove();
// // //                     $('#mosaic_vido_input').children('option').remove();
// // //                     $('#mosaic_vido_input2').children('option').remove();
// // //                     $('#mosaic_vido_input3').children('option').remove();
// // //                     $('#mosaic_vido_input4').children('option').remove();
// //                     callbackMosaic();
// //                 });
//             }
//             // 드론영상
//             var droneList = objData.droneList;
//             if (!droneList) {
//                 htmlStr += "<dt>드론영상(0)</dt>";
//                 htmlStr += "<dd>드론영상 결과값이 없습니다.</dd>";
// //                 $("#mosaic_result").html(htmlStr);

//             } else {
//                 htmlStr += "<dt>드론영상(" + droneList.length + ")</dt>";
//                 for (var i = 0; i < droneList.length; i++) {
//                 	var innerfileNm =droneList[i].innerFileCoursNm;
// 	            	var idx = -1;
// 	            	idx = innerfileNm.lastIndexOf('\\');
	            	
// 	            	if (idx < 0){
// 	            		idx = innerfileNm.lastIndexOf('/');
// 	            	}
	            	
// 	            	var filePath = innerfileNm.replace(/tif/gi, "png");
// 	            	var fileName = innerfileNm.substring(idx).replace(/tif/gi, "png");

//                 	htmlStr += "<a href=javascript:imageView('"+filePath+"','"+fileName+"',"+droneList[i].ltopCrdntX+","+droneList[i].ltopCrdntY+","+droneList[i].rbtmCrdntX+","+droneList[i].rbtmCrdntY+",'"+droneList[i].mapPrjctnCn+"');><dd><input class='form-check-input'  name ='droneCheckDefault2'  type='checkbox' minx="+droneList[i].ltopCrdntX+" miny="+droneList[i].ltopCrdntY+" maxx="+droneList[i].rbtmCrdntX+" maxy="+droneList[i].rbtmCrdntY+" value="+droneList[i].innerFileCoursNm+" >드론영상("+droneList[i].innerFileCoursNm+")</dd></a>";

// //                 	$("#mosaic_result").html(htmlStr);
//                 }
//             }
//             $("#mosaic_result").html(htmlStr);
        });
    }
    //모자이크 처리
    function mosaic_result(){

        var mosaic_vido = $('#mosaic_vido').val(); // 기준영상
        var mosaic_vido_input = $('#mosaic_vido_input').val(); // 입력영상
        var mosaic_vido_input2 = $('#mosaic_vido_input2').val(); // 입력영상2
        var mosaic_vido_input3 = $('#mosaic_vido_input3').val(); // 입력영상3
        var mosaic_vido_input4 = $('#mosaic_vido_input4').val(); // 입력영상4
		var did = $('#disasterIdMosa').val();
        if(did.trim() === '') {
        	alert('재난 ID를 입력해주세요.');
        	return;
        }

        var vido_resul = $('#mosaic_vido_result').val(); //결과 영상 이름
        var minx = $("#mosaic_vido option:selected").attr("minx");
        var maxx = $("#mosaic_vido option:selected").attr("maxx");
        var miny = $("#mosaic_vido option:selected").attr("miny");
        var maxy = $("#mosaic_vido option:selected").attr("maxy");
        $('#progress3').show();
        param={mosaic_vido:mosaic_vido,mosaic_vido_input:mosaic_vido_input,mosaic_vido_input2:mosaic_vido_input2,mosaic_vido_input3:mosaic_vido_input3,mosaic_vido_input4:mosaic_vido_input4,
            vido_resul:vido_resul, disasterId: did};
        $.ajax({
            url: "cisc009/mosaicResult.do",
            type: "post",
            data: param,
            v: {minx,miny,maxx,maxy}
        }).done(function (response) {
            var objData = JSON.parse(response);
            
            $('#progress3').hide();

            var htmlStr="";
    		var result_file_path = objData.filePath;
            var result_file = objData.fileName;
            var ltopCrdntX = objData.ltopCrdntX;
            var ltopCrdntY = objData.ltopCrdntY;
            var rbtmCrdntX = objData.rbtmCrdntX;
            var rbtmCrdntY = objData.rbtmCrdntY;
            var mapPrjctnCn = objData.mapPrjctnCn;
            
            htmlStr += "<a href=javascript:imageView('"+result_file+"','"+result_file+"',"+ltopCrdntX+","+ltopCrdntY+","+rbtmCrdntX+","+rbtmCrdntY+",'"+mapPrjctnCn+"');><dd>"+result_file+"</dd>";
            //$("#ColorResult").html(htmlStr);
            $("#mosaicResult").append(htmlStr);
            $("#mosaic_cancel").trigger("click");
        });

    };
    
   //히스토그램 처리 버튼 클릭시 수행.
    function perform_hist(){
		var input_vido =$('#input_video').val();
		var did = $('#disasterIdHist').val();
		if(did.trim() === '') {
			alert('재난 ID를 입력해주세요.');
			return;
		}
		$('#progress1').show();
		
    	param={input_vod:input_vido, disasterId: did};
    	
        $.ajax({
            url: "cisc008/histResult.do",
            type: "post",
            data: param
        }).done(function (response) {

        	var response = JSON.parse(response);
    		var data_r=[];
    		var data_g=[];
    		var data_b=[];
            for (var i = 0; i < response.histgR.length; i++) {
                var num = response.histgR[i];
                var list = {};
                list = {
                    "column-1": num
                };
                data_r.push(list);
            }
        	red.dataProvider = data_r;
        	red.validateNow();
    		
            for (var i = 0; i < response.histgG.length; i++) {
                var num = response.histgG[i];
                var list = {};
                list = {
                    "column-1": num
                };
                data_g.push(list);
            }
        	green.dataProvider = data_g;
        	green.validateNow();
        	
        	//green.validateData();
       
        for (var i = 0; i < response.histgB.length; i++) {
            var num = response.histgB[i];
            var list = {};
            list = {
                "column-1": num
            };
            data_b.push(list);
        }
    	blue.dataProvider = data_b;
    	blue.validateNow();
    	$('#progress1').hide();
    	$('#myModal3').modal('show');
    });
    }
    function transform(x,y){
        var pt = new Proj4js.Point(x,y);
        var result = Proj4js.transform(s_srs,t_srs,pt);
        return result;
    }
    function transformByEPSG(x, y, epsg){
        var pt = new Proj4js.Point(x,y);
        var originCRS = null;
        var result = null;
        switch (epsg) {
		case 'EPSG:4326':
			originCRS = s_srs;
			break;
		case 'EPSG:5186':
			originCRS = srs_5186;		
			break;
		case 'EPSG:5187':
			originCRS = srs_5187;		
			break;
		case 'EPSG:5185':
			originCRS = srs_5185;		
			break;
		case 'EPSG:5179':
			originCRS = t_srs;
			break;
		case 'EPSG:32652':
			originCRS = srs_32652;
			break;
		default:
			break;
		}
        var result = Proj4js.transform(originCRS,t_srs,pt);
        return result;
    }

    //3. 지도 이미지 활성화.
     function imageView(filePath, fileName, minx, miny, maxx, maxy, epsg){
    	
    	//var baseNameArr = filePath.split('.');
    	var thumbnail = filePath.replace(".tif", ".png");
    	var param = {
    		thumbnailFileCoursNm: thumbnail,
    		ltopCrdntX: minx,
    		ltopCrdntY: miny,
    		rbtmCrdntX: maxx,
    		rbtmCrdntY: maxy,
    		mapPrjctnCn: epsg
    	};
    	
    	var url = "cmsc003thumbnail.do?"+$.param(param);
		console.log(url);	
		
    	
    	// var min = transform(miny,minx);
        // var max  = transform(maxy,maxx);
        var min = transformByEPSG(miny, minx, epsg);
        var max  = transformByEPSG(maxy, maxx, epsg);
        
        //var imageExtent = [min.x,min.y,max.x,max.y ];


        var imageExtent = [min.x,max.y,max.x,min.y ];

        var  source = new ol.source.ImageStatic({
//             url:'/img/thumnail/'+fileName,
            url: url,
            crossOrigin: '',
            projection: 'EPSG:5179',
            imageExtent: imageExtent
            //imageSmoothing: imageSmoothing.checked,
        }); 
        imageLayer.setSource(source);
        map.getView().fit(imageExtent);
    }
    
    
     function imageView_default(fileName,minx,miny,maxx,maxy){
         var imageExtent = [min.x,max.y,max.x,min.y ];
           var  source = new ol.source.ImageStatic({
             url:'/thumnail/'+fileName,
             crossOrigin: '',
             projection: 'EPSG:5179',
             imageExtent: imageExtent
             //imageSmoothing: imageSmoothing.checked,
         }); 
         imageLayer.setSource(source);
         map.getView().fit(imageExtent);
     }
    
     //컬러합성 수행.
    function result(){
    	 
    	$('#progress').show()
    	var red = $("#red option:selected").val();
    	var green = $("#green option:selected").val();
    	var blue = $("#blue option:selected").val();
    	var extra = $("#extra option:selected").val();
    	var name = $("#colorinput").val();
        var minx = $("#red option:selected").attr("minx");
        var maxx = $("#red option:selected").attr("maxx");
        var miny = $("#red option:selected").attr("miny");
        var maxy = $("#red option:selected").attr("maxy");
        var did = $('#disasterIdColor').val();
        if(did.trim() === '') {
        	alert('재난 ID를 입력해주세요.');
        	return;
        }

    	param={red:red, green:green, blue:blue, extra:extra, name:name, disasterId: did};
        $.ajax({
            url: "cisc007/colorResult.do",
            type: "post",
            data: param,
            v: {minx,miny,maxx,maxy}
        }).done(function (response) {
        	
        	var objData = JSON.parse(response);
        	$('#progress').hide()
        	//if(response.procCode=="SUCCESS"){
        		var htmlStr="";       		
        		var result_file_path = objData.filePath;
                var result_file = objData.fileName;
                var ltopCrdntX = objData.ltopCrdntX;
                var ltopCrdntY = objData.ltopCrdntY;
                var rbtmCrdntX = objData.rbtmCrdntX;
                var rbtmCrdntY = objData.rbtmCrdntY;
                var mapPrjctnCn = objData.mapPrjctnCn;
                
//                 htmlStr += "<a href=javascript:imageView('"+result_file_path+"','"+result_file+"',"+this.v.minx+","+this.v.miny+","+this.v.maxx+","+this.v.maxy+",'"+this.v.mapPrjctnCn+"');><dd>"+result_file+"</dd>";
                htmlStr += "<a href=javascript:imageView('"+result_file_path+"','"+result_file+"',"+ltopCrdntX+","+ltopCrdntY+","+rbtmCrdntX+","+rbtmCrdntY+",'"+mapPrjctnCn+"');><dd>"+result_file+"</dd>";
                //$("#ColorResult").html(htmlStr);
                $("#ColorResult").append(htmlStr);
                $("#colorCancel").trigger("click");	
        	// }
        	// else{
        	// 	alert("수행에 실패 하였습니다.");
        	// }
        });
    }
    
    
    //히스토그램 완료 버튼 클릭시 수행. 
    function completion(){
    	$('#progress2').show()
    	var input_vido =$('#input_video').val();
    	var result_vido = $('#result_video').val();
    	//red
    	var red_min = $("#red_min").val();
    	var red_max = $("#red_max").val();
    	//green
    	var green_min = $("#green_min").val();

    	var green_max = $("#green_max").val();
    	//blue
    	var blue_min = $("#blue_min").val();
    	var blue_max = $("#blue_max").val();
    	//알고리즘
    	var algorithm = $("#algorithm option:selected").val();
        var minx = $("#input_video option:selected").attr("minx");
        var maxx = $("#input_video option:selected").attr("maxx");
        var miny = $("#input_video option:selected").attr("miny");
        var maxy = $("#input_video option:selected").attr("maxy");
    	var histo =[red_min,red_max,green_min,green_max,blue_min,blue_max];
    	
    	var did = $('#disasterIdHist').val();
        if(did.trim() === '') {
        	alert('재난 ID를 입력해주세요.');
        	return;
        }
        
    	histparam={
   			 histo :histo,
   			 input_vido:input_vido,
   			 result_vido:result_vido,
   			 algorithm:algorithm,
   			 auto:'false',
   			 disasterId: did
		};
    	 
        $.ajax({
            url: "cisc008/histcompl.do",
            type: "post",
              data: histparam,
            v: {minx,miny,maxx,maxy}
        }).done(function (response) {
        	var response = JSON.parse(response);
        	$('#progress2').hide()
            var htmlStr="";
        	
        	var result_file_path = response.filePath;
            var result_file = response.fileName;
            
            var ltopCrdntX = response.ltopCrdntX;
            var ltopCrdntY = response.ltopCrdntY;
            var rbtmCrdntX = response.rbtmCrdntX;
            var rbtmCrdntY = response.rbtmCrdntY;
            var mapPrjctnCn = response.mapPrjctnCn;
            
            htmlStr += "<a href=javascript:imageView('"+result_file+"','"+result_file+"',"+ltopCrdntX+","+ltopCrdntY+","+rbtmCrdntX+","+rbtmCrdntY+",'"+mapPrjctnCn+"');><dd>"+result_file+"</dd>";
//             htmlStr += "<a href=javascript:imageView('"+result_file_path+"','"+result_file+"',"+this.v.minx+","+this.v.miny+","+this.v.maxx+","+this.v.maxy+",'"+this.v.mapPrjctnCn+"');><dd>"+result_file+"</dd>";
            // htmlStr += "<a href=javascript:imageView('"+result_file+"');><dd>"+result_file+"</dd>";
            //$("#ColorResult").html(htmlStr);
            $("#hist_result").append(htmlStr);
            $("#hist_cancel2").trigger("click");
            $("#hist_cancel").trigger("click");
        });
    }


    function fnScriptSavePopup(kind){
    	$('#input_script_nm').val("");    	
    	$('#script_kind').val(kind);
    	$('#scriptModal').modal('show');
    }
    	
    function fnScriptNameCheck(scriptNm ){
    	var ret = false;
    	$("#progress").show();
    	$.ajax({
    		type : "POST",
    		async:false,
    	    url: "cisc001/searchScriptList.do",
    	    data:{"scriptNm":scriptNm},
    	    success:function(data){
    	    	$("#progress").hide();
    	    	if (data.resultList.length > 0){
    	    		ret = true;
    	    	}
    	    },
    	    error:function(data){
    	    	ret = false;
    	    	$("#progress").hide();
    	    }
    	}); 	
    	
    	return ret;
    }
    	
    function fnScriptSave(){
    	
    	if ($("#input_script_nm").val() == ""){
    		alert('스크립트명[영문]을 입력하세요.');
    		return false;
    	}
    	
    	if( ! confirm("스크립트를 저장 하시겠습니까? ")) {
    		return;
    	}
    	if (fnScriptNameCheck($("#input_script_nm").val())){
    		alert('입력하신 스크립트명이 존재합니다. 다른이름을 입력하세요.' );
    		return;
    	}

    	var workKind = $('#script_kind').val();
    	
    	if(workKind == ''){
    		workKind = '4';
    	}
    	
    	var param = {
    			scriptId:"",  //생성은 id ''
    			workKind:workKind,  //1:대기보정,2:절대방사보정,3:상대방사보정,4:컬러합성,5:히스토그램조정,6:모자이크
    			scriptNm:$("#input_script_nm").val(),
    			inputFileNm:$("#red option:selected").val(),  //필수값이므로 첫번째값 입력
    			redBand:'',			
				greenBand:'',
				blueBand:'',
				outputFileNm:''
    	}
    	
    	if (workKind == '4'){  //컬러합성
    		param.redBand = $("#red option:selected").val();			
    		param.greenBand = $("#green option:selected").val();
    		param.blueBand = $("#blue option:selected").val();
    		param.outputFileNm = $("#colorinput").val();
    		
    	}else if (workKind == '5'){  //히스토그램
    		param.algorithmNm = $("#algorithm option:selected").val();
    		param.inputFileNm = $('#input_video').val();
    		param.outputFileNm = $('#result_video').val();
    		param.controlType = $("input[name='radioInline']:checked").val();  //일괄 or 밴드별
    		//스크립트 작성은 무조건 자동으로 한다.
    		param.auto = "true";
    		param.autoAreaControl = $("input[name='radioInline2']:checked").val();    		
    		/* if ($('#auto_check').prop("checked")){
    			param.autoAreaControl = $("input[name='radioInline2']:checked").val();
    		} */
    		
    	}else{  //모자이크
    		param.targetFileNm = $('#mosaic_vido').val();
    		param.inputFileNm = $('#mosaic_vido_input').val();
    		param.inputFileNm2 = $('#mosaic_vido_input2').val();
    		param.inputFileNm3 = $('#mosaic_vido_input3').val();
    		param.inputFileNm4 = $('#mosaic_vido_input4').val();
    		param.outputFileNm = $('#mosaic_vido_result').val();
    	}
    	

    	$("#progress").show();
    	$.ajax({
    		type : "POST",
    	    url: "cisc001/createUpdateScript.do",
    	    data:param,
    	    success:function(data){
    	    	$("#progress").hide();
    	    	if (data.result > 0){
    	    		alert('정상적으로 처리되었습니다.');
    	    		$("#progress").hide();
    	    		$('#scriptModal').modal("hide");
    	    		
    	    	}else{
    	    		if (data.result < 0){
    	    			alert(data.msg);
    	    		}else{
    	    			alert('스크립트 저장에 실패했습니다. 잠시후 다시 시도하세요');
    	    		}
    	    	}
    	    },
    	    error:function(data){
    	    	$("#progress").hide();
    	    	alert('실패했습니다. 잠시후 다시 시도하세요');
       			return;
    	    }
    	});
    }    
    
    function fnAutoAreaControl(){
    	$("input[name='radioInline2']").prop("disabled", !$("#auto_check").prop("checked"));
    }
</script>
<script type="text/javascript" src="js/amcharts.js"></script>
<script type="text/javascript" src="js/serial.js"></script>
</head>

<body>
	<!-- 상단메뉴 -->
	<%@ include file="../topMenu.jsp"%>


	<!-- //////////////////////////////////////////////////////////////////////////// -->
	<!-- START SIDEBAR -->
	<div class="sidebar clearfix">
		<div role="tabpanel" class="sidepanel" style="display: block;">

			<!-- Nav tabs -->
			<ul class="nav nav-tabs" role="tablist">
				<li role="presentation" class="active" style="width: 33.333%">
					<a href="#tab-01" aria-controls="tab-01" role="tab"
					data-toggle="tab" aria-expanded="true" class="active"> <i
						class="fas fa-palette"></i><br>컬러합성
				</a>
				</li>
				<li role="presentation" class="" style="width: 33.333%"><a
					href="#tab-02" aria-controls="tab-02" role="tab" data-toggle="tab"
					class="" aria-expanded="false"> <i class="fas fa-chart-bar"></i><br>히스토그램
						조정
				</a></li>
				<li role="presentation" class="" style="width: 33.333%"><a
					href="#tab-03" aria-controls="tab-03" role="tab" data-toggle="tab"
					class="" aria-expanded="false"> <i class="fas fa-th-large"></i><br>모자이크
				</a></li>
			</ul>

			<!-- Tab panes -->
			<div class="tab-content">

				<!-- Start tab-01 -->
				<div role="tabpanel" class="tab-pane active" id="tab-01">

					<div class="sidepanel-m-title">대상영상 검색</div>
					<div class="panel-body">
						<!-- =========================== -->
						<div class="form-group m-t-6" style="margin-top: 8px;">
							<label class="col-sm-3 control-label form-label">재난 ID</label>
							<div class="col-sm-9">
								<input type="text" class="form-control" id="disasterIdColor"
									name="disasterId" readOnly="readOnly"
									style="cursor: auto; background-color: #fff;">
							</div>
						</div>
<!-- 						<div class="form-group m-t-10"> -->
<!-- 							<label for="color_db_01" -->
<!-- 								class="col-sm-3 control-label form-label">대상</label> -->
<!-- 							<div class="col-sm-4"> -->
<!-- 								<div class="radio radio-info radio-inline"> -->
<!-- 									<input type="radio" id="color_db_01" value="1" name="color_db" -->
<!-- 										checked> <label for="color_db_01">수집데이터</label> -->
<!-- 								</div> -->
<!-- 							</div> -->
<!-- 							<div class="col-sm-4"> -->
<!-- 								<div class="radio radio-inline"> -->
<!-- 									<input type="radio" id="color_db_02" value="2" name="color_db"> -->
<!-- 									<label for="color_db_02">처리데이터</label> -->
<!-- 								</div> -->
<!-- 							</div> -->
<!-- 						</div> -->
<!-- 						<div class="form-group m-t-10"> -->
<!-- 							<label for="input002" class="col-sm-3 control-label form-label">종류</label> -->
<!-- 							<div class="col-sm-9"> -->
<!-- 								<label class="ckbox-container">항공/정사영상
<!--                                 <input type="checkbox" checked="checked"> -->
<!--                                 <span class="checkmark"></span> -->
<!--                             </label> --> 
<!-- 								<label class="ckbox-container">국토위성 <input -->
<!-- 									type="checkbox" name="sate" value="soil"> <span -->
<!-- 									class="checkmark"></span> -->
<!-- 								</label> <label class="ckbox-container">기타위성 <input -->
<!-- 									type="checkbox" name="sate" value="etc"> <span -->
<!-- 									class="checkmark"></span> -->
<!-- 								</label> -->
<!-- 								<label class="ckbox-container">드론영상
<!--                                 <input type="checkbox"> -->
<!--                                 <span class="checkmark"></span> -->
<!--                             </label> --> 
<!-- 							</div> -->
<!-- 						</div> -->
						<div class="form-group m-t-6" id="dataKindContainerColor">
							<label for="input002" class="col-sm-2 control-label form-label">종류</label>
							<div class="col-sm-10">
								<label class="ckbox-container"
									style="margin-right: 1px; font-size: 12px;">기존 데이터 <input
									type="checkbox" id="dataKindCurrentColor" name="dataKindCurrent"
									checked="checked"> <span class="checkmark"></span>
								</label> <label class="ckbox-container"
									style="margin-right: 1px; font-size: 12px;">긴급 영상 <input
									type="checkbox" id="dataKindEmergencyColor"
									name="dataKindEmergency"> <span class="checkmark"></span>
								</label> <label class="ckbox-container"
									style="margin-right: 1px; font-size: 12px;">분석결과<input
									type="checkbox" id="dataKindResultColor" name="dataKindResult">
									<span class="checkmark"></span>
								</label>
							</div>
						</div>
						<div class="form-group m-t-6" id="targetDataKindContainer">
						<label for="input002" class="col-sm-2 control-label form-label">대상</label>
						<div class="col-sm-10">
							<label class="ckbox-container"
								style="margin-right: 1px; font-size: 12px;">원본 데이터 <input
								type="radio" id="originData" name="dataTypeColor" value="1"
								checked="checked"> <span class="checkmark"></span>
							</label> <label class="ckbox-container"
								style="margin-right: 1px; font-size: 12px;">긴급공간정보 <input
								type="radio" id="datasetData"
								name="dataTypeColor" value="2"> <span class="checkmark"></span>
							</label> <label class="ckbox-container"
								style="margin-right: 1px; font-size: 12px;">처리 데이터 <input
								type="radio" id="workData" name="dataTypeColor" value="3">
								<span class="checkmark"></span>
							</label>
						</div>
					</div>
<!-- 						<div class="form-group m-t-10"> -->
<!-- 							<label for="input002" class="col-sm-3 control-label form-label">기간</label> -->
<!-- 							<div class="col-sm-4"> -->
<!-- 								<input type="text" id="datepicker" class="form-control"> -->
<!-- 							</div> -->
<!-- 							<div class="col-sm-1"> -->
<!-- 								<span class="txt-in-form">~<span> -->
<!-- 							</div> -->
<!-- 							<div class="col-sm-4"> -->
<!-- 								<input type="text" id="datepicker2" class="form-control"> -->
<!-- 							</div> -->
<!-- 						</div> -->
<!-- 						<script src="js/datepicker.js"></script> -->
<!-- 						<script> -->
<!--                         let datepicker = new DatePicker(document.getElementById('datepicker')); -->
<!--                         let datepicker2 = new DatePicker(document.getElementById('datepicker2')); -->
<!--                     </script> -->
<!-- 						<div class="form-group m-t-10"> -->
<!-- 							<label for="input002" class="col-sm-3 control-label form-label">해상도(m)</label> -->
<!-- 							<div class="col-sm-4"> -->
<!-- 															<select class="form-basic"> -->
<!-- 																<option></option> -->
<!-- 															</select> -->
<!-- 								<input type="text" id="resolution1" class="form-control"> -->
<!-- 							</div> -->
<!-- 							<div class="col-sm-1"> -->
<!-- 								<span class="txt-in-form">~<span> -->
<!-- 							</div> -->
<!-- 							<div class="col-sm-4"> -->
<!-- 								<input type="text" id="resolution2" class="form-control"> -->
<!-- 															<select class="form-basic"> -->
<!-- 																<option></option> -->
<!-- 															</select> -->
<!-- 							</div> -->
<!-- 						</div> -->

						<!-- 					<div class="form-group m-t-6" id="dataKindContainer"> -->
						<!-- 						<label for="input002" class="col-sm-2 control-label form-label">종류</label> -->
						<!-- 						<div class="col-sm-10"> -->
						<!-- 							<label class="ckbox-container" -->
						<!-- 								style="margin-right: 1px; font-size: 12px;">기존 데이터 <input -->
						<!-- 								type="checkbox" id="dataKindCurrent" name="dataKindCurrent" -->
						<!-- 								checked="checked"> <span class="checkmark"></span> -->
						<!-- 							</label> <label class="ckbox-container" -->
						<!-- 								style="margin-right: 1px; font-size: 12px;">긴급 영상 <input -->
						<!-- 								type="checkbox" id="dataKindEmergency" -->
						<!-- 								name="dataKindEmergency"> <span class="checkmark"></span> -->
						<!-- 							</label> <label class="ckbox-container" -->
						<!-- 								style="margin-right: 1px; font-size: 12px;">분석결과 데이터 <input -->
						<!-- 								type="checkbox" id="dataKindResult" name="dataKindResult"> -->
						<!-- 								<span class="checkmark"></span> -->
						<!-- 							</label> -->
						<!-- 						</div> -->
						<!-- 					</div> -->
						<!-- 					<div id="datepickerContainer" -->
						<!-- 						class="form-group m-t-6 input-daterange"> -->
						<!-- 						<label for="input002" class="col-sm-2 control-label form-label">기간</label> -->
						<!-- 						<div class="col-sm-5"> -->
						<!-- 							<input type="text" class="form-control ui-input-datepicker" -->
						<!-- 								id="inputStartDate" name="dateFrom" value="2020-04-23"> -->
						<!-- 							<button type="button" id="btnOpenDatepickerStart" -->
						<!-- 								class="btn btn-default btn-icon ui-btn-open-datepicker"> -->
						<!-- 								<i class="fas fa-calendar-alt"></i> -->
						<!-- 							</button> -->
						<!-- 						</div> -->

						<!-- 						<div class="col-sm-5"> -->
						<!-- 							<input type="text" class="form-control ui-input-datepicker" -->
						<!-- 								id="inputEndDate" name="dateTo" value="2020-04-26"> -->
						<!-- 							<button type="button" id="btnOpenDatepickerEnd" -->
						<!-- 								class="btn btn-default btn-icon ui-btn-open-datepicker"> -->
						<!-- 								<i class="fas fa-calendar-alt"></i> -->
						<!-- 							</button> -->
						<!-- 						</div> -->
						<!-- 					</div> -->
						<!-- 					<div class="form-group m-t-6"> -->
						<!-- 						<label for="input002" class="col-sm-3 control-label form-label">해상도(m)</label> -->
						<!-- 						<div class="col-sm-4"> -->
						<!-- 							<input type="number" class="form-basic" name="resolMin" /> -->
						<!-- 																<select class="form-basic" name="resolMin"> -->
						<!-- 																	<option value='5cm' selected>5</option> -->
						<!-- 																	<option value='10cm'>10</option> -->
						<!-- 																	<option value='25cm'>25</option> -->
						<!-- 																</select> -->
						<!-- 						</div> -->
						<!-- 						<div class="col-sm-1"> -->
						<!-- 							<span class="txt-in-form">~</span> -->
						<!-- 						</div> -->
						<!-- 						<div class="col-sm-4"> -->
						<!-- 							<input type="number" class="form-basic" name="resolMax" /> -->
						<!-- 																<select class="form-basic" name="resolMax"> -->
						<!-- 																	<option value='5cm'>5</option> -->
						<!-- 																	<option value='10cm'>10</option> -->
						<!-- 																	<option value='25cm' selected>25</option> -->
						<!-- 							</select> -->
						<!-- 						</div> -->
						<!-- 					</div> -->
<!-- 						<div class="form-group m-t-6"> -->
<!-- 							<label class="col-sm-3 control-label form-label">재난 유형</label> -->
<!-- 							<div class="col-sm-9"> -->
<!-- 								<select id="selectDisasterType" class="form-basic" -->
<!-- 									name="disasterType"> -->
<!-- 									<option value='AllDisaster'>전체</option> -->
<!-- 									<option value='Flood'>수해</option> -->
<!-- 									<option value='Landslide'>산사태</option> -->
<!-- 									<option value='ForestFire'>산불</option> -->
<!-- 									<option value='Earthquake'>지진</option> -->
<!-- 									<option value='MaritimeDisaster'>해양재난</option> -->
<!-- 								</select> -->
<!-- 							</div> -->
<!-- 						</div> -->

						<!-- <div class="form-group m-t-6">
						<label class="col-sm-3 control-label form-label">재난 ID</label>
						<div class="col-sm-9">
							<input type="text" class="form-control" id="disasterId"
								name="disasterId" readOnly="readOnly"
								style="cursor: auto; background-color: #fff;">
						</div>
					</div> -->

<!-- 						<div class="form-group m-t-6"> -->
<!-- 							<label class="col-sm-12 control-label form-label">재난지역</label> -->
<!-- 							<div class="col-sm-4"> -->
<!-- 								<select class="form-basic" name="sido" -->
<!-- 									onChange="getSigungu(this.value)"> -->
<!-- 									<option selected disabled hidden>광역시도</option> -->
<!-- 								</select> -->
<!-- 							</div> -->
<!-- 							<div class="col-sm-4"> -->
<!-- 								<select class="form-basic" name="sigungu" -->
<!-- 									onChange="getEmd(this.value)" disabled> -->
<!-- 									<option selected disabled hidden>시군구</option> -->
<!-- 								</select> -->
<!-- 							</div> -->
<!-- 							<div class="col-sm-4"> -->
<!-- 								<select class="form-basic" name="emd" -->
<!-- 									onChange="getEmdPosition(this.value)" disabled> -->
<!-- 									<option selected disabled hidden>읍면동</option> -->
<!-- 								</select> -->
<!-- 							</div> -->
<!-- 						</div> -->
<!-- 						<div class="form-group m-t-6"> -->
<!-- 							<div class="col-sm-12"> -->
<!-- 								<input type="text" id="detailAddrForm" readOnly="readOnly" -->
<!-- 									class="form-basic"> -->
<!-- 							</div> -->
<!-- 						</div> -->


<!-- 						<div class="form-group m-t-6"> -->
<!-- 							<label for="input002" class="col-sm-2 control-label form-label">ROI</label> -->
<!-- 							<div class="col-sm-10"> -->
<!-- 								<div class="col-sm-2 in-tit">ULX</div> -->
<!-- 								<div class="col-sm-4"> -->
<!-- 									<input type="text" class="form-control js-input-roi-extent" -->
<!-- 										id="inputULX" name="ulx4326"> <input type="hidden" -->
<!-- 										id="inputULX2" name="ulx5186"> <input type="hidden" -->
<!-- 										id="inputULX3" name="ulx5179"> <input type="hidden" -->
<!-- 										id="inputULX4" name="ulx32652"> -->
<!-- 								</div> -->
<!-- 								<div class="col-sm-2 in-tit">ULY</div> -->
<!-- 								<div class="col-sm-4"> -->
<!-- 									<input type="text" class="form-control js-input-roi-extent" -->
<!-- 										id="inputULY" name="uly4326"> <input type="hidden" -->
<!-- 										id="inputULY2" name="uly5186"> <input type="hidden" -->
<!-- 										id="inputULY3" name="uly5179"> <input type="hidden" -->
<!-- 										id="inputULY4" name="uly32652"> -->
<!-- 								</div> -->
<!-- 								<div class="col-sm-2 in-tit m-t-5">LRX</div> -->
<!-- 								<div class="col-sm-4 m-t-5"> -->
<!-- 									<input type="text" class="form-control js-input-roi-extent" -->
<!-- 										id="inputLRX" name="lrx4326"> <input type="hidden" -->
<!-- 										id="inputLRX2" name="lrx5186"> <input type="hidden" -->
<!-- 										id="inputLRX3" name="lrx5179"> <input type="hidden" -->
<!-- 										id="inputLRX4" name="lrx32652"> -->
<!-- 								</div> -->
<!-- 								<div class="col-sm-2 in-tit m-t-5">LRY</div> -->
<!-- 								<div class="col-sm-4 m-t-5"> -->
<!-- 									<input type="text" class="form-control js-input-roi-extent" -->
<!-- 										id="inputLRY" name="lry4326"> <input type="hidden" -->
<!-- 										id="inputLRY2" name="lry5186"> <input type="hidden" -->
<!-- 										id="inputLRY3" name="lry5179"> <input type="hidden" -->
<!-- 										id="inputLRY4" name="lry32652"> -->
<!-- 								</div> -->
<!-- 							</div> -->
<!-- 						</div> -->
						<!-- =========================== -->

						<div class="btn-wrap a-cent">
							<a href="javascript:searchColor()" class="btn btn-default"><i
								class="fas fa-search m-r-5"></i>검색</a>
						</div>
						<div class="sidepanel-s-title m-t-30">
							검색 결과 <a href="#" id="mymodal" class="btn btn-light f-right"
								data-toggle="modal" data-target="#myModal"><i
								class="fas fa-project-diagram m-r-5"></i>수행</a></a>
						</div>
						<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
							aria-hidden="true">
							<div class="modal-dialog modal-sm">
								<div class="modal-content">
									<div class="modal-header">
										<button type="button" class="close" data-dismiss="modal"
											aria-label="Close">
											<span aria-hidden="true">&times;</span>
										</button>
										<h4 class="modal-title">컬러 합성</h4>
									</div>
									<div class="modal-body">
										<div class="panel-body">
											<form class="">
												<div class="form-group m-t-10">
													<label class="col-sm-4 control-label form-label">Red
														band</label>
													<div class="col-sm-8">
														<select class="form-basic" id="red" name="red">
															<!-- <option value="36807062s_R.tif">36807062s_R.tif</option>
                                                        <option value="36807062s_G.tif">36807062s_G.tif</option>
                                                        <option vlaue="36807062s_B.tif">36807062s_B.tif</option> -->
														</select>
													</div>
												</div>
												<div class="form-group m-t-10">
													<label class="col-sm-4 control-label form-label">Green
														band</label>
													<div class="col-sm-8">
														<select class="form-basic" id="green">
															<!-- <option>36807062s_R.tif</option>
                                                        <option>36807062s_G.tif</option>
                                                        <option>36807062s_B.tif</option> -->
														</select>
													</div>
												</div>
												<div class="form-group m-t-10">
													<label class="col-sm-4 control-label form-label">Blue
														band</label>
													<div class="col-sm-8">
														<select class="form-basic" id="blue">
															<!-- <option>36807062s_R.tif</option>
                                                        <option>36807062s_G.tif</option>
                                                        <option>36807062s_B.tif</option> -->
														</select>
													</div>
												</div>
												<div class="form-group m-t-10">
													<label class="col-sm-4 control-label form-label">Extra
													</label>
													<div class="col-sm-8">
														<select class="form-basic" id="extra">
															<!-- <option>36807062s_R.tif</option>
                                                        <option>36807062s_G.tif</option>
                                                        <option>36807062s_B.tif</option> -->
														</select>
													</div>
												</div>
												<div class="form-group m-t-10">
													<label for="input002"
														class="col-sm-4 control-label form-label">결과영상</label>
													<div class="col-sm-8">
														<input type="text" class="form-control" id="colorinput"
															value="CompResult_">
													</div>
												</div>
											</form>

										</div>
									</div>

									<div class="modal-footer">
										<button type="button" class="btn btn-default"
											onclick="result();">
											<i class="fas fa-check-square m-r-5"></i>처리
										</button>
										<button type="button" class="btn btn-gray"
											data-dismiss="modal" aria-label="Close" id="colorCancel">
											<i class="fas fa-times m-r-5"></i>취소
										</button>
										<button id="btn_process" type="button" class="btn btn-default"
											onclick="fnScriptSavePopup('4')">
											<i class="fas fa-check-square m-r-5"></i>스크립트저장
										</button>
									</div>
									<div class="load" id="progress" style="display: none;"></div>
								</div>
							</div>
						</div>
						<div class="panel-body">
							<div class="col-lg-12 js-search-result-area">
								<div id="colorPanel" class="panel panel-default" style="height: 200px;">
<!-- 									<dl class="result-list-input" id="AeroResult"> -->
										<%--                                    <dt>국토위성영상 (3)</dt>--%>
										<%--                                    <dd>국토위성-1호 xxxxxx001</dd>--%>
										<%--                                    <dd>국토위성-1호 xxxxxx002</dd>--%>
										<%--                                    <dd>국토위성-1호 xxxxxx003</dd>--%>
<!-- 									</dl> -->
<!-- 									<dl class="result-list-input"> -->
										<%--                                    <dt>기타위성 (6)</dt>--%>
										<%--                                    <dd>Landsat 5 TM Collection 1 B1</dd>--%>
										<%--                                    <dd>Landsat 5 TM Collection 1 B2</dd>--%>
										<%--                                    <dd>Landsat 5 TM Collection 1 B3</dd>--%>
										<%--                                    <dd>Landsat 5 TM Collection 1 B4</dd>--%>
										<%--                                    <dd>Landsat 5 TM Collection 1 B5</dd>--%>
										<%--                                    <dd>Landsat 5 TM Collection 1 B7</dd>--%>
<!-- 									</dl> -->
								</div>
							</div>
						</div>
						<div class="sidepanel-s-title m-t-30">처리 결과</div>
						<div class="panel-body">
							<div class="col-lg-12">
								<div class="panel panel-default" style="height: 100px;">
									<dl class="result-list-input" id="ColorResult">
										<%--                                    <dd>RGB_Landsat-5 TM Collection 1</dd>--%>
									</dl>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div role="tabpanel" class="tab-pane" id="tab-02">

					<div class="sidepanel-m-title">대상영상 검색</div>
					<div class="panel-body">
						<!-- =========================== -->
						<!-- 					<div class="form-group m-t-6" style="margin-top: 8px;"> -->
						<!-- 						<label class="col-sm-3 control-label form-label">재난 ID</label> -->
						<!-- 						<div class="col-sm-9"> -->
						<!-- 							<input type="text" class="form-control" id="disasterIdCreate" -->
						<!-- 								name="disasterId" readOnly="readOnly" -->
						<!-- 								style="cursor: auto; background-color: #fff;"> -->
						<!-- 						</div> -->
						<!-- 					</div> -->
						<div class="form-group m-t-6" style="margin-top: 8px;">
							<label class="col-sm-3 control-label form-label">재난 ID</label>
							<div class="col-sm-9">
								<input type="text" class="form-control disasterId"
									id="disasterIdHist" name="disasterId" readOnly="readOnly"
									style="cursor: auto; background-color: #fff;">
							</div>
						</div>

<!-- 						<div class="form-group m-t-10"> -->
<!-- 							<label for="histogram_db_01" -->
<!-- 								class="col-sm-3 control-label form-label">대상</label> -->
<!-- 							<div class="col-sm-4"> -->
<!-- 								<div class="radio radio-info radio-inline"> -->
<!-- 									<input type="radio" id="histogram_db_01" value="1" -->
<!-- 										name="histogram_db" checked> <label -->
<!-- 										for="histogram_db_01">수집데이터</label> -->
<!-- 								</div> -->
<!-- 							</div> -->
<!-- 							<div class="col-sm-4"> -->
<!-- 								<div class="radio radio-inline"> -->
<!-- 									<input type="radio" id="histogram_db_02" value="2" -->
<!-- 										name="histogram_db"> <label for="histogram_db_02">처리데이터</label> -->
<!-- 								</div> -->
<!-- 							</div> -->
<!-- 						</div> -->
<!-- 						<div class="form-group m-t-10"> -->
<!-- 							<label for="input002" class="col-sm-3 control-label form-label">종류</label> -->
<!-- 							<div class="col-sm-9"> -->
<!-- 								<label class="ckbox-container">항공/정사영상 <input -->
<!-- 									type="checkbox" name="sate1" value="air1"> <span -->
<!-- 									class="checkmark"></span> -->
<!-- 								</label> <label class="ckbox-container">국토위성 <input -->
<!-- 									type="checkbox" name="sate1" value="soil1"> <span -->
<!-- 									class="checkmark"></span> -->
<!-- 								</label> <label class="ckbox-container">기타위성 <input -->
<!-- 									type="checkbox" name="sate1" value="etc1"> <span -->
<!-- 									class="checkmark"></span> -->
<!-- 								</label> <label class="ckbox-container">드론영상 <input -->
<!-- 									type="checkbox" name="sate1" value="drone1"> <span -->
<!-- 									class="checkmark"></span> -->
<!-- 								</label> -->
<!-- 							</div> -->
<!-- 						</div> -->
						<div class="form-group m-t-6" id="dataKindContainerHist">
							<label for="input002" class="col-sm-2 control-label form-label">종류</label>
							<div class="col-sm-10">
								<label class="ckbox-container"
									style="margin-right: 1px; font-size: 12px;">기존 데이터 <input
									type="checkbox" id="dataKindCurrentHist" name="dataKindCurrent"
									checked="checked"> <span class="checkmark"></span>
								</label> <label class="ckbox-container"
									style="margin-right: 1px; font-size: 12px;">긴급 영상 <input
									type="checkbox" id="dataKindEmergencyHist"
									name="dataKindEmergency"> <span class="checkmark"></span>
								</label> <label class="ckbox-container"
									style="margin-right: 1px; font-size: 12px;">분석결과<input
									type="checkbox" id="dataKindResultHist" name="dataKindResult">
									<span class="checkmark"></span>
								</label>
							</div>
						</div>
						<div class="form-group m-t-6" id="targetDataKindContainer">
						<label for="input002" class="col-sm-2 control-label form-label">대상</label>
						<div class="col-sm-10">
							<label class="ckbox-container"
								style="margin-right: 1px; font-size: 12px;">원본 데이터 <input
								type="radio" id="originData" name="dataTypeHist" value="1"
								checked="checked"> <span class="checkmark"></span>
							</label> <label class="ckbox-container"
								style="margin-right: 1px; font-size: 12px;">긴급공간정보 <input
								type="radio" id="datasetData"
								name="dataTypeHist" value="2"> <span class="checkmark"></span>
							</label> <label class="ckbox-container"
								style="margin-right: 1px; font-size: 12px;">처리 데이터 <input
								type="radio" id="workData" name="dataTypeHist" value="3">
								<span class="checkmark"></span>
							</label>
						</div>
					</div>
<!-- 						<div class="form-group m-t-10"> -->
<!-- 							<label for="input002" class="col-sm-3 control-label form-label">기간</label> -->
<!-- 							<div class="col-sm-4"> -->
<!-- 								<input type="text" id="datepicker3" class="form-control"> -->
<!-- 							</div> -->
<!-- 							<div class="col-sm-1"> -->
<!-- 								<span class="txt-in-form">~<span> -->
<!-- 							</div> -->
<!-- 							<div class="col-sm-4"> -->
<!-- 								<input type="text" id="datepicker4" class="form-control"> -->
<!-- 							</div> -->
<!-- 						</div> -->
						<!-- 						<script src="js/datepicker.js"></script> -->
<!-- 						<script> -->
<!--                         let datepicker3 = new DatePicker(document.getElementById('datepicker3')); -->
<!--                         let datepicker4 = new DatePicker(document.getElementById('datepicker4')); -->
<!--                     </script> -->
<!-- 						<div class="form-group m-t-10"> -->
<!-- 							<label for="input002" class="col-sm-3 control-label form-label">해상도(m)</label> -->
<!-- 							<div class="col-sm-4"> -->
<!-- 															<select class="form-basic"> -->
<!-- 																<option></option> -->
<!-- 															</select> -->
<!-- 								<input type="text" id="resolution1" class="form-control"> -->
<!-- 							</div> -->
<!-- 							<div class="col-sm-1"> -->
<!-- 								<span class="txt-in-form">~<span> -->
<!-- 							</div> -->
<!-- 							<div class="col-sm-4"> -->
<!-- 								<input type="text" id="resolution2" class="form-control"> -->
<!-- 															<select class="form-basic"> -->
<!-- 																<option></option> -->
<!-- 															</select> -->
<!-- 							</div> -->
<!-- 						</div> -->
						<!-- 					<div class="form-group m-t-6" id="dataKindContainer"> -->
						<!-- 						<label for="input002" class="col-sm-2 control-label form-label">종류</label> -->
						<!-- 						<div class="col-sm-10"> -->
						<!-- 							<label class="ckbox-container" -->
						<!-- 								style="margin-right: 1px; font-size: 12px;">기존 데이터 <input -->
						<!-- 								type="checkbox" id="dataKindCurrent" name="dataKindCurrent" -->
						<!-- 								checked="checked"> <span class="checkmark"></span> -->
						<!-- 							</label> <label class="ckbox-container" -->
						<!-- 								style="margin-right: 1px; font-size: 12px;">긴급 영상 <input -->
						<!-- 								type="checkbox" id="dataKindEmergency" -->
						<!-- 								name="dataKindEmergency"> <span class="checkmark"></span> -->
						<!-- 							</label> <label class="ckbox-container" -->
						<!-- 								style="margin-right: 1px; font-size: 12px;">분석결과 데이터 <input -->
						<!-- 								type="checkbox" id="dataKindResult" name="dataKindResult"> -->
						<!-- 								<span class="checkmark"></span> -->
						<!-- 							</label> -->
						<!-- 						</div> -->
						<!-- 					</div> -->
						<!-- 					<div id="datepickerContainer" -->
						<!-- 						class="form-group m-t-6 input-daterange"> -->
						<!-- 						<label for="input002" class="col-sm-2 control-label form-label">기간</label> -->
						<!-- 						<div class="col-sm-5"> -->
						<!-- 							<input type="text" class="form-control ui-input-datepicker" -->
						<!-- 								id="inputStartDate" name="dateFrom" value="2020-04-23"> -->
						<!-- 							<button type="button" id="btnOpenDatepickerStart" -->
						<!-- 								class="btn btn-default btn-icon ui-btn-open-datepicker"> -->
						<!-- 								<i class="fas fa-calendar-alt"></i> -->
						<!-- 							</button> -->
						<!-- 						</div> -->

						<!-- 						<div class="col-sm-5"> -->
						<!-- 							<input type="text" class="form-control ui-input-datepicker" -->
						<!-- 								id="inputEndDate" name="dateTo" value="2020-04-26"> -->
						<!-- 							<button type="button" id="btnOpenDatepickerEnd" -->
						<!-- 								class="btn btn-default btn-icon ui-btn-open-datepicker"> -->
						<!-- 								<i class="fas fa-calendar-alt"></i> -->
						<!-- 							</button> -->
						<!-- 						</div> -->
						<!-- 					</div> -->
						<!-- 					<div class="form-group m-t-6"> -->
						<!-- 						<label for="input002" class="col-sm-3 control-label form-label">해상도(m)</label> -->
						<!-- 						<div class="col-sm-4"> -->
						<!-- 							<input type="number" class="form-basic" name="resolMin" /> -->
						<!-- 																<select class="form-basic" name="resolMin"> -->
						<!-- 																	<option value='5cm' selected>5</option> -->
						<!-- 																	<option value='10cm'>10</option> -->
						<!-- 																	<option value='25cm'>25</option> -->
						<!-- 																</select> -->
						<!-- 						</div> -->
						<!-- 						<div class="col-sm-1"> -->
						<!-- 							<span class="txt-in-form">~</span> -->
						<!-- 						</div> -->
						<!-- 						<div class="col-sm-4"> -->
						<!-- 							<input type="number" class="form-basic" name="resolMax" /> -->
						<!-- 																<select class="form-basic" name="resolMax"> -->
						<!-- 																	<option value='5cm'>5</option> -->
						<!-- 																	<option value='10cm'>10</option> -->
						<!-- 																	<option value='25cm' selected>25</option> -->
						<!-- 							</select> -->
						<!-- 						</div> -->
						<!-- 					</div> -->
<!-- 						<div class="form-group m-t-6"> -->
<!-- 							<label class="col-sm-3 control-label form-label">재난 유형</label> -->
<!-- 							<div class="col-sm-9"> -->
<!-- 								<select id="selectDisasterType" class="form-basic" -->
<!-- 									name="disasterType"> -->
<!-- 									<option value='AllDisaster'>전체</option> -->
<!-- 									<option value='Flood'>수해</option> -->
<!-- 									<option value='Landslide'>산사태</option> -->
<!-- 									<option value='ForestFire'>산불</option> -->
<!-- 									<option value='Earthquake'>지진</option> -->
<!-- 									<option value='MaritimeDisaster'>해양재난</option> -->
<!-- 								</select> -->
<!-- 							</div> -->
<!-- 						</div> -->

						<!-- <div class="form-group m-t-6">
						<label class="col-sm-3 control-label form-label">재난 ID</label>
						<div class="col-sm-9">
							<input type="text" class="form-control" id="disasterId"
								name="disasterId" readOnly="readOnly"
								style="cursor: auto; background-color: #fff;">
						</div>
					</div> -->

<!-- 						<div class="form-group m-t-6"> -->
<!-- 							<label class="col-sm-12 control-label form-label">재난지역</label> -->
<!-- 							<div class="col-sm-4"> -->
<!-- 								<select class="form-basic" name="sido" -->
<!-- 									onChange="getSigungu(this.value)"> -->
<!-- 									<option selected disabled hidden>광역시도</option> -->
<!-- 								</select> -->
<!-- 							</div> -->
<!-- 							<div class="col-sm-4"> -->
<!-- 								<select class="form-basic" name="sigungu" -->
<!-- 									onChange="getEmd(this.value)" disabled> -->
<!-- 									<option selected disabled hidden>시군구</option> -->
<!-- 								</select> -->
<!-- 							</div> -->
<!-- 							<div class="col-sm-4"> -->
<!-- 								<select class="form-basic" name="emd" -->
<!-- 									onChange="getEmdPosition(this.value)" disabled> -->
<!-- 									<option selected disabled hidden>읍면동</option> -->
<!-- 								</select> -->
<!-- 							</div> -->
<!-- 						</div> -->
<!-- 						<div class="form-group m-t-6"> -->
<!-- 							<div class="col-sm-12"> -->
<!-- 								<input type="text" id="detailAddrForm" readOnly="readOnly" -->
<!-- 									class="form-basic"> -->
<!-- 							</div> -->
<!-- 						</div> -->


<!-- 						<div class="form-group m-t-6"> -->
<!-- 							<label for="input002" class="col-sm-2 control-label form-label">ROI</label> -->
<!-- 							<div class="col-sm-10"> -->
<!-- 								<div class="col-sm-2 in-tit">ULX</div> -->
<!-- 								<div class="col-sm-4"> -->
<!-- 									<input type="text" class="form-control js-input-roi-extent" -->
<!-- 										id="inputULX" name="ulx4326"> <input type="hidden" -->
<!-- 										id="inputULX2" name="ulx5186"> <input type="hidden" -->
<!-- 										id="inputULX3" name="ulx5179"> <input type="hidden" -->
<!-- 										id="inputULX4" name="ulx32652"> -->
<!-- 								</div> -->
<!-- 								<div class="col-sm-2 in-tit">ULY</div> -->
<!-- 								<div class="col-sm-4"> -->
<!-- 									<input type="text" class="form-control js-input-roi-extent" -->
<!-- 										id="inputULY" name="uly4326"> <input type="hidden" -->
<!-- 										id="inputULY2" name="uly5186"> <input type="hidden" -->
<!-- 										id="inputULY3" name="uly5179"> <input type="hidden" -->
<!-- 										id="inputULY4" name="uly32652"> -->
<!-- 								</div> -->
<!-- 								<div class="col-sm-2 in-tit m-t-5">LRX</div> -->
<!-- 								<div class="col-sm-4 m-t-5"> -->
<!-- 									<input type="text" class="form-control js-input-roi-extent" -->
<!-- 										id="inputLRX" name="lrx4326"> <input type="hidden" -->
<!-- 										id="inputLRX2" name="lrx5186"> <input type="hidden" -->
<!-- 										id="inputLRX3" name="lrx5179"> <input type="hidden" -->
<!-- 										id="inputLRX4" name="lrx32652"> -->
<!-- 								</div> -->
<!-- 								<div class="col-sm-2 in-tit m-t-5">LRY</div> -->
<!-- 								<div class="col-sm-4 m-t-5"> -->
<!-- 									<input type="text" class="form-control js-input-roi-extent" -->
<!-- 										id="inputLRY" name="lry4326"> <input type="hidden" -->
<!-- 										id="inputLRY2" name="lry5186"> <input type="hidden" -->
<!-- 										id="inputLRY3" name="lry5179"> <input type="hidden" -->
<!-- 										id="inputLRY4" name="lry32652"> -->
<!-- 								</div> -->
<!-- 							</div> -->
<!-- 						</div> -->
						<!-- =========================== -->

						<div class="btn-wrap a-cent" id="search">
							<a href="javascript:search_hist()" class="btn btn-default"><i
								class="fas fa-search m-r-5"></i>검색</a>
						</div>
						<div class="sidepanel-s-title m-t-30">
							검색 결과 <a href="" class="btn btn-light f-right" id="mymodal2"
								data-toggle="modal" data-target="#myModal2"><i
								class="fas fa-project-diagram m-r-5"></i>수행</a>
						</div>
						<div class="modal fade" id="myModal2" tabindex="-1" role="dialog"
							aria-hidden="true">
							<div class="modal-dialog modal-sm">
								<div class="modal-content">
									<div class="modal-header">
										<button type="button" class="close" data-dismiss="modal"
											aria-label="Close">
											<span aria-hidden="true">&times;</span>
										</button>
										<h4 class="modal-title">히스토그램 조정</h4>
									</div>
									<div class="modal-body">
										<div class="panel-body">
											<form class="">
												<div class="form-group m-t-10">
													<label class="col-sm-4 control-label form-label">입력영상</label>
													<div class="col-sm-8">
														<select class="form-basic" id="input_video">
															<option></option>
														</select>
													</div>
												</div>
												<div class="form-group m-t-10">
													<label for="input002"
														class="col-sm-4 control-label form-label">결과영상</label>
													<div class="col-sm-8">
														<input type="text" class="form-control" id="result_video"
															value="HistResult_">
													</div>
												</div>
											</form>

										</div>

									</div>
									<div class="modal-footer">
										<button type="button" onclick="javascript:perform_hist();"
											class="btn btn-default">
											<i class="fas fa-check-square m-r-5"></i>처리
										</button>
										<button type="button" id="hist_cancel" class="btn btn-gray"
											data-dismiss="modal" aria-label="Close">
											<i class="fas fa-times m-r-5"></i>취소
										</button>
									</div>
									<div class="load" id="progress1" style="display: none;"></div>
								</div>
							</div>
						</div>
						<div class="modal fade" id="myModal3" tabindex="-1" role="dialog"
							aria-hidden="true">
							<div class="modal-dialog modal-sm">
								<div class="modal-content">
									<div class="modal-header">
										<button type="button" class="close" data-dismiss="modal"
											aria-label="Close">
											<span aria-hidden="true">&times;</span>
										</button>
										<h4 class="modal-title">히스토그램 조정 수행</h4>
									</div>
									<div class="modal-body">
										<div class="panel-body">
											<form class="">
												<div class="form-group m-t-10">
													<label class="col-sm-3 control-label form-label">조정방식</label>
													<div class="col-sm-9">
														<div class="col-sm-6">
															<div class="radio radio-info radio-inline">
																<input type="radio" id="inlineRadio1" value="band"
																	name="radioInline" checked> <label
																	for="inlineRadio1">밴드별 조정</label>
															</div>
														</div>
														<div class="col-sm-6">
															<div class="radio radio-info radio-inline">
																<input type="radio" id="inlineRadio2" value="all"
																	name="radioInline"> <label for="inlineRadio2">일괄조정</label>
															</div>
														</div>
														<div class="col-sm-5">
															<label class="ckbox-container">자동영역 설정 <input
																type="checkbox" id="auto_check"
																onclick="fnAutoAreaControl();"> <span
																class="checkmark"></span>
															</label>
														</div>
														<div class="col-sm-7">
															<div class="col-sm-8">
																<div class="radio radio-info radio-inline">
																	<input type="radio" id="inlineRadio3" value="minmax"
																		name="radioInline2" checked> <label
																		for="inlineRadio3">MIN/MAX</label>
																</div>
															</div>
															<div class="col-sm-4">
																<div class="radio radio-info radio-inline">
																	<input type="radio" id="inlineRadio4" value="1σ"
																		name="radioInline2"> <label for="inlineRadio4">1σ</label>
																</div>
															</div>
														</div>
													</div>
													<!-- 		<label class="col-sm-3 control-label form-label">알고리즘</label>
											<div class="col-sm-9">
												<select class="form-basic">
													<option></option>
												</select>
											</div> -->
												</div>
												<div class="form-group m-t-10">
													<label class="col-sm-3 control-label form-label">RED</label>
													<div class="col-sm-9">
														<script type="text/javascript">
													var data = [];

													function fun() {
														
														var list = {};
														for (var i = 0; i < 256; i++) {
															var num = Math.floor(Math.random() * 255) + 1;
															list = {
																"column-1": num
															};
															data.push(list);
														}
														return data;
													}


													 var red = AmCharts.makeChart("chartdiv-r", {
														"type": "serial",
														"categoryField": "category",
														"colors": [
															"#FF0000"
														],
														"startDuration": 1,
														"fontFamily": "",
														"fontSize": 0,
														"handDrawScatter": 0,
														"handDrawThickness": 0,
														"categoryAxis": {
															"gridPosition": "start"
														},
														"chartCursor": {
															"enabled": true
														},
														"chartScrollbar": {
															"enabled": true
														},
														"trendLines": [],
														"graphs": [
															{
																"balloonText": "[[title]] of [[category]]:[[value]]",
																"fillAlphas": 1,
																"id": "AmGraph-1",
																"title": "graph 1",
																"type": "column",
																"valueField": "column-1"
															},
															{
																"balloonText": "[[title]] of [[category]]:[[value]]",
																"fillAlphas": 1,
																"id": "AmGraph-2",
																"title": "graph 2",
																"type": "column",
																"valueField": "column-2"
															}
														],
														//"dataProvider": fun(),
														"listeners": [
															{
															"event": "zoomed",
															"method": function(e) { 
																//document.getElementById('log').innerHTML = 'Start zoom index: <b>' + e.chart.startIndex + '</b><br>End zoom index: <b>' + e.chart.endIndex + '</b>';
																
																$("#red_min").val(e.chart.startIndex);
																$("#red_max").val(e.chart.endIndex);
																
																var control  = $("input[name='radioInline']:checked").val();
																if(control=="all"){
																	green.dataProvider = e.chart.dataProvider
																	green.validateData();
																	green.zoom(e.startIndex,e.endIndex);
																	blue.dataProvider = e.chart.dataProvider
																	blue.validateData();
																	blue.zoom(e.startIndex,e.endIndex);
																}
																

															}
														}]
													});
												</script>
														<div id="chartdiv-r"
															style="width: 100%; height: 180px; background-color: #FFFFFF;"></div>
														<div id="log"></div>
														<div class="col-sm-2 in-tit">MIN</div>
														<div class="col-sm-4">
															<input type="text" class="form-control" id="red_min">
														</div>
														<div class="col-sm-2 in-tit">MAX</div>
														<div class="col-sm-4">
															<input type="text" class="form-control" id="red_max">
														</div>
													</div>
												</div>
												<div class="form-group m-t-10">
													<label class="col-sm-3 control-label form-label">GREEN</label>
													<div class="col-sm-9">
														<script type="text/javascript">
													var data = [];
													var green = AmCharts.makeChart("chartdiv-g", {
														"type": "serial",
														"categoryField": "category",
														"colors": [
															"#00ff00"
														],
														"startDuration": 1,
														"fontFamily": "",
														"fontSize": 0,
														"handDrawScatter": 0,
														"handDrawThickness": 0,
														"categoryAxis": {
															"gridPosition": "start"
														},
														"chartCursor": {
															"enabled": true
														},
														"chartScrollbar": {
															"enabled": true
														},
														"trendLines": [],
														"graphs": [
															{
																"balloonText": "[[title]] of [[category]]:[[value]]",
																"fillAlphas": 1,
																"id": "AmGraph-1",
																"title": "graph 1",
																"type": "column",
																"valueField": "column-1"
															},
															{
																"balloonText": "[[title]] of [[category]]:[[value]]",
																"fillAlphas": 1,
																"id": "AmGraph-2",
																"title": "graph 2",
																"type": "column",
																"valueField": "column-2"
															}
														],
													//	"dataProvider": fun(),
														"listeners": [
															{
															"event": "zoomed",
															"method": function(e) { 
																$("#green_min").val(e.chart.startIndex);
																$("#green_max").val(e.chart.endIndex);
															
															}
														}]
													});
												</script>
														<div id="chartdiv-g"
															style="width: 100%; height: 180px; background-color: #FFFFFF;"></div>
														<div class="col-sm-2 in-tit">MIN</div>
														<div class="col-sm-4">
															<input type="text" class="form-control" id="green_min">
														</div>
														<div class="col-sm-2 in-tit">MAX</div>
														<div class="col-sm-4">
															<input type="text" class="form-control" id="green_max">
														</div>
													</div>
												</div>
												<div class="form-group m-t-10">
													<label class="col-sm-3 control-label form-label">BLUE</label>
													<div class="col-sm-9">
														<script type="text/javascript">
													var data = [];
											var blue = 	AmCharts.makeChart("chartdiv-b", {
														"type": "serial",
														"categoryField": "category",
														"colors": [
															"#0000ff"
														],
														"startDuration": 1,
														"fontFamily": "",
														"fontSize": 0,
														"handDrawScatter": 0,
														"handDrawThickness": 0,
														"categoryAxis": {
															"gridPosition": "start"
														},
														"chartCursor": {
															"enabled": true
														},
														"chartScrollbar": {
															"enabled": true
														},
														"trendLines": [],
														"graphs": [
															{
																"balloonText": "[[title]] of [[category]]:[[value]]",
																"fillAlphas": 1,
																"id": "AmGraph-1",
																"title": "graph 1",
																"type": "column",
																"valueField": "column-1"
															},
															{
																"balloonText": "[[title]] of [[category]]:[[value]]",
																"fillAlphas": 1,
																"id": "AmGraph-2",
																"title": "graph 2",
																"type": "column",
																"valueField": "column-2"
															}
														],
													//	"dataProvider": fun(),
														"listeners": [
															{
															"event": "zoomed",
															"method": function(e) { 
																$("#blue_min").val(e.chart.startIndex);
																$("#blue_max").val(e.chart.endIndex);
																

															}
														}]
													});
												</script>
														<div id="chartdiv-b"
															style="width: 100%; height: 180px; background-color: #FFFFFF;"></div>
														<div class="col-sm-2 in-tit">MIN</div>
														<div class="col-sm-4">
															<input type="text" class="form-control" id="blue_min">
														</div>
														<div class="col-sm-2 in-tit">MAX</div>
														<div class="col-sm-4">
															<input type="text" class="form-control" id="blue_max">
														</div>
													</div>
												</div>
												<div class="form-group m-t-10">
													<label class="col-sm-3 control-label form-label">알고리즘</label>
													<div class="col-sm-9">
														<select class="form-basic" id="algorithm">
															<option value="Stretch">Linear Stretch</option>
															<option value="Equalization">Histogram
																Equalization</option>
														</select>
													</div>
												</div>
											</form>

										</div>

									</div>
									<div class="modal-footer">
										<button type="button" class="btn btn-default"
											onclick="completion();">
											<i class="fas fa-check-square m-r-5"></i>완료
										</button>
										<button type="button" class="btn btn-gray"
											data-dismiss="modal" id="hist_cancel2" aria-label="Close">
											<i class="fas fa-times m-r-5"></i>취소
										</button>
										<button id="btn_process" type="button" class="btn btn-default"
											onclick="fnScriptSavePopup('5')">
											<i class="fas fa-check-square m-r-5"></i>스크립트저장
										</button>

									</div>
									<div class="load" id="progress2" style="display: none;"></div>
								</div>
							</div>
						</div>
						<div class="panel-body">
							<div class="col-lg-12 js-search-result-area">
								<div id="histPanel" class="panel panel-default" style="height: 200px;">
<!-- 									<dl class="result-list-input" id="HistResult"> -->

<!-- 									</dl> -->
<!-- 									<dl class="result-list-input"> -->

<!-- 									</dl> -->


								</div>
							</div>
						</div>
						<div class="sidepanel-s-title m-t-30">처리 결과</div>
						<div class="panel-body">
							<div class="col-lg-12">
								<div class="panel panel-default" style="height: 100px;">
									<dl class="result-list-input" id="hist_result">

									</dl>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div role="tabpanel" class="tab-pane" id="tab-03">

					<div class="sidepanel-m-title">대상영상 검색</div>
					<div class="panel-body">
						<div class="form-group m-t-6" style="margin-top: 8px;">
							<label class="col-sm-3 control-label form-label">재난 ID</label>
							<div class="col-sm-9">
								<input type="text" class="form-control disasterId"
									id="disasterIdMosa" name="disasterId" readOnly="readOnly"
									style="cursor: auto; background-color: #fff;">
							</div>
						</div>
<!-- 						<div class="form-group m-t-10"> -->
<!-- 							<label for="mosaic_db_01" -->
<!-- 								class="col-sm-3 control-label form-label">대상</label> -->
<!-- 							<div class="col-sm-4"> -->
<!-- 								<div class="radio radio-info radio-inline"> -->
<!-- 									<input type="radio" id="mosaic_db_01" value="1" -->
<!-- 										name="mosaic_db" checked> <label for="mosaic_db_01">수집데이터</label> -->
<!-- 								</div> -->
<!-- 							</div> -->
<!-- 							<div class="col-sm-4"> -->
<!-- 								<div class="radio radio-inline"> -->
<!-- 									<input type="radio" id="mosaic_db_02" value="2" -->
<!-- 										name="mosaic_db"> <label for="mosaic_db_02">처리데이터</label> -->
<!-- 								</div> -->
<!-- 							</div> -->
<!-- 						</div> -->
<!-- 						<div class="form-group m-t-10"> -->
<!-- 							<label for="input002" class="col-sm-3 control-label form-label">종류</label> -->
<!-- 							<div class="col-sm-9"> -->
<!-- 								<label class="ckbox-container">항공/정사영상 <input -->
<!-- 									type="checkbox" name="sate2" value="air2"> <span -->
<!-- 									class="checkmark"></span> -->
<!-- 								</label> <label class="ckbox-container">국토위성 <input -->
<!-- 									type="checkbox" name="sate2" value="soil2"> <span -->
<!-- 									class="checkmark"></span> -->
<!-- 								</label> <label class="ckbox-container">기타위성 <input -->
<!-- 									type="checkbox" name="sate2" value="etc2"> <span -->
<!-- 									class="checkmark"></span> -->
<!-- 								</label> <label class="ckbox-container">드론영상 <input -->
<!-- 									type="checkbox" name="sate2" value="drone2"> <span -->
<!-- 									class="checkmark"></span> -->
<!-- 								</label> -->
<!-- 							</div> -->
<!-- 						</div> -->
						<div class="form-group m-t-6" id="dataKindContainerMosa">
							<label for="input002" class="col-sm-2 control-label form-label">종류</label>
							<div class="col-sm-10">
								<label class="ckbox-container"
									style="margin-right: 1px; font-size: 12px;">기존 데이터 <input
									type="checkbox" id="dataKindCurrentMosa" name="dataKindCurrent"
									checked="checked"> <span class="checkmark"></span>
								</label> <label class="ckbox-container"
									style="margin-right: 1px; font-size: 12px;">긴급 영상 <input
									type="checkbox" id="dataKindEmergencyMosa"
									name="dataKindEmergency"> <span class="checkmark"></span>
								</label> <label class="ckbox-container"
									style="margin-right: 1px; font-size: 12px;">분석결과<input
									type="checkbox" id="dataKindResultMosa" name="dataKindResult">
									<span class="checkmark"></span>
								</label>
							</div>
						</div>
						<div class="form-group m-t-6" id="targetDataKindContainer">
						<label for="input002" class="col-sm-2 control-label form-label">대상</label>
						<div class="col-sm-10">
							<label class="ckbox-container"
								style="margin-right: 1px; font-size: 12px;">원본 데이터 <input
								type="radio" id="originData" name="dataTypeMosa" value="1"
								checked="checked"> <span class="checkmark"></span>
							</label> <label class="ckbox-container"
								style="margin-right: 1px; font-size: 12px;">긴급공간정보 <input
								type="radio" id="datasetData"
								name="dataTypeMosa" value="2"> <span class="checkmark"></span>
							</label> <label class="ckbox-container"
								style="margin-right: 1px; font-size: 12px;">처리 데이터 <input
								type="radio" id="workData" name="dataTypeMosa" value="3">
								<span class="checkmark"></span>
							</label>
						</div>
					</div>
<!-- 						<div class="form-group m-t-10"> -->
<!-- 							<label for="input002" class="col-sm-3 control-label form-label">기간</label> -->
<!-- 							<div class="col-sm-4"> -->
<!-- 								<input type="text" id="datepicker5" class="form-control"> -->
<!-- 							</div> -->
<!-- 							<div class="col-sm-1"> -->
<!-- 								<span class="txt-in-form">~<span> -->
<!-- 							</div> -->
<!-- 							<div class="col-sm-4"> -->
<!-- 								<input type="text" id="datepicker6" class="form-control"> -->
<!-- 							</div> -->
<!-- 						</div> -->
						<!-- 						<script src="js/datepicker.js"></script> -->
<!-- 						<script> -->
<!--                         	let datepicker5 = new DatePicker(document.getElementById('datepicker5')); -->
<!--                         	let datepicker6 = new DatePicker(document.getElementById('datepicker6')); -->
<!--                     	</script> -->
<!-- 						<div class="form-group m-t-10"> -->
<!-- 							<label for="input002" class="col-sm-3 control-label form-label">해상도(m)</label> -->
<!-- 							<div class="col-sm-4"> -->
<%-- 															<select class="form-basic"> --%>
<%-- 																<option></option> --%>
<%-- 															</select> --%>
<!-- 								<input type="text" id="resolution1" class="form-control"> -->
<!-- 							</div> -->
<!-- 							<div class="col-sm-1"> -->
<!-- 								<span class="txt-in-form">~<span> -->
<!-- 							</div> -->
<!-- 							<div class="col-sm-4"> -->
<!-- 								<input type="text" id="resolution2" class="form-control"> -->
<%-- 															<select class="form-basic"> --%>
<%-- 																<option></option> --%>
<%-- 															</select> --%>
<!-- 							</div> -->
<!-- 						</div> -->
						<!-- =========================== -->
						<!-- 						<div class="form-group m-t-6" style="margin-top: 8px;"> -->
						<!-- 							<label class="col-sm-3 control-label form-label">재난 ID</label> -->
						<!-- 							<div class="col-sm-9"> -->
						<!-- 								<input type="text" class="form-control" id="disasterIdCreate" -->
						<!-- 									name="disasterId" readOnly="readOnly" -->
						<!-- 									style="cursor: auto; background-color: #fff;"> -->
						<!-- 							</div> -->
						<!-- 						</div> -->


						<!-- 						<div class="form-group m-t-6" id="dataKindContainer"> -->
						<!-- 							<label for="input002" class="col-sm-2 control-label form-label">종류</label> -->
						<!-- 							<div class="col-sm-10"> -->
						<!-- 								<label class="ckbox-container" -->
						<!-- 									style="margin-right: 1px; font-size: 12px;">기존 데이터 <input -->
						<!-- 									type="checkbox" id="dataKindCurrent" name="dataKindCurrent" -->
						<!-- 									checked="checked"> <span class="checkmark"></span> -->
						<!-- 								</label> <label class="ckbox-container" -->
						<!-- 									style="margin-right: 1px; font-size: 12px;">긴급 영상 <input -->
						<!-- 									type="checkbox" id="dataKindEmergency" name="dataKindEmergency"> -->
						<!-- 									<span class="checkmark"></span> -->
						<!-- 								</label> <label class="ckbox-container" -->
						<!-- 									style="margin-right: 1px; font-size: 12px;">분석결과 데이터 <input -->
						<!-- 									type="checkbox" id="dataKindResult" name="dataKindResult"> -->
						<!-- 									<span class="checkmark"></span> -->
						<!-- 								</label> -->
						<!-- 							</div> -->
						<!-- 						</div> -->
						<!-- 						<div id="datepickerContainer" -->
						<!-- 							class="form-group m-t-6 input-daterange"> -->
						<!-- 							<label for="input002" class="col-sm-2 control-label form-label">기간</label> -->
						<!-- 							<div class="col-sm-5"> -->
						<!-- 								<input type="text" class="form-control ui-input-datepicker" -->
						<!-- 									id="inputStartDate" name="dateFrom" value="2020-04-23"> -->
						<!-- 								<button type="button" id="btnOpenDatepickerStart" -->
						<!-- 									class="btn btn-default btn-icon ui-btn-open-datepicker"> -->
						<!-- 									<i class="fas fa-calendar-alt"></i> -->
						<!-- 								</button> -->
						<!-- 							</div> -->

						<!-- 							<div class="col-sm-5"> -->
						<!-- 								<input type="text" class="form-control ui-input-datepicker" -->
						<!-- 									id="inputEndDate" name="dateTo" value="2020-04-26"> -->
						<!-- 								<button type="button" id="btnOpenDatepickerEnd" -->
						<!-- 									class="btn btn-default btn-icon ui-btn-open-datepicker"> -->
						<!-- 									<i class="fas fa-calendar-alt"></i> -->
						<!-- 								</button> -->
						<!-- 							</div> -->
						<!-- 						</div> -->
						<!-- 						<div class="form-group m-t-6"> -->
						<!-- 							<label for="input002" class="col-sm-3 control-label form-label">해상도(m)</label> -->
						<!-- 							<div class="col-sm-4"> -->
						<!-- 								<input type="number" class="form-basic" name="resolMin" /> -->
						<!-- 																	<select class="form-basic" name="resolMin"> -->
						<!-- 																		<option value='5cm' selected>5</option> -->
						<!-- 																		<option value='10cm'>10</option> -->
						<!-- 																		<option value='25cm'>25</option> -->
						<!-- 																	</select> -->
						<!-- 							</div> -->
						<!-- 							<div class="col-sm-1"> -->
						<!-- 								<span class="txt-in-form">~</span> -->
						<!-- 							</div> -->
						<!-- 							<div class="col-sm-4"> -->
						<!-- 								<input type="number" class="form-basic" name="resolMax" /> -->
						<!-- 																	<select class="form-basic" name="resolMax"> -->
						<!-- 																		<option value='5cm'>5</option> -->
						<!-- 																		<option value='10cm'>10</option> -->
						<!-- 																		<option value='25cm' selected>25</option> -->
						<!-- 								</select> -->
						<!-- 							</div> -->
						<!-- 						</div> -->
<!-- 						<div class="form-group m-t-6"> -->
<!-- 							<label class="col-sm-3 control-label form-label">재난 유형</label> -->
<!-- 							<div class="col-sm-9"> -->
<!-- 								<select id="selectDisasterType" class="form-basic" -->
<!-- 									name="disasterType"> -->
<!-- 									<option value='AllDisaster'>전체</option> -->
<!-- 									<option value='Flood'>수해</option> -->
<!-- 									<option value='Landslide'>산사태</option> -->
<!-- 									<option value='ForestFire'>산불</option> -->
<!-- 									<option value='Earthquake'>지진</option> -->
<!-- 									<option value='MaritimeDisaster'>해양재난</option> -->
<!-- 								</select> -->
<!-- 							</div> -->
<!-- 						</div> -->

						<!-- <div class="form-group m-t-6">
						<label class="col-sm-3 control-label form-label">재난 ID</label>
						<div class="col-sm-9">
							<input type="text" class="form-control" id="disasterId"
								name="disasterId" readOnly="readOnly"
								style="cursor: auto; background-color: #fff;">
						</div>
					</div> -->

<!-- 						<div class="form-group m-t-6"> -->
<!-- 							<label class="col-sm-12 control-label form-label">재난지역</label> -->
<!-- 							<div class="col-sm-4"> -->
<!-- 								<select class="form-basic" name="sido" -->
<!-- 									onChange="getSigungu(this.value)"> -->
<!-- 									<option selected disabled hidden>광역시도</option> -->
<!-- 								</select> -->
<!-- 							</div> -->
<!-- 							<div class="col-sm-4"> -->
<!-- 								<select class="form-basic" name="sigungu" -->
<!-- 									onChange="getEmd(this.value)" disabled> -->
<!-- 									<option selected disabled hidden>시군구</option> -->
<!-- 								</select> -->
<!-- 							</div> -->
<!-- 							<div class="col-sm-4"> -->
<!-- 								<select class="form-basic" name="emd" -->
<!-- 									onChange="getEmdPosition(this.value)" disabled> -->
<!-- 									<option selected disabled hidden>읍면동</option> -->
<!-- 								</select> -->
<!-- 							</div> -->
<!-- 						</div> -->
<!-- 						<div class="form-group m-t-6"> -->
<!-- 							<div class="col-sm-12"> -->
<!-- 								<input type="text" id="detailAddrForm" readOnly="readOnly" -->
<!-- 									class="form-basic"> -->
<!-- 							</div> -->
<!-- 						</div> -->


<!-- 						<div class="form-group m-t-6"> -->
<!-- 							<label for="input002" class="col-sm-2 control-label form-label">ROI</label> -->
<!-- 							<div class="col-sm-10"> -->
<!-- 								<div class="col-sm-2 in-tit">ULX</div> -->
<!-- 								<div class="col-sm-4"> -->
<!-- 									<input type="text" class="form-control js-input-roi-extent" -->
<!-- 										id="inputULX" name="ulx4326"> <input type="hidden" -->
<!-- 										id="inputULX2" name="ulx5186"> <input type="hidden" -->
<!-- 										id="inputULX3" name="ulx5179"> <input type="hidden" -->
<!-- 										id="inputULX4" name="ulx32652"> -->
<!-- 								</div> -->
<!-- 								<div class="col-sm-2 in-tit">ULY</div> -->
<!-- 								<div class="col-sm-4"> -->
<!-- 									<input type="text" class="form-control js-input-roi-extent" -->
<!-- 										id="inputULY" name="uly4326"> <input type="hidden" -->
<!-- 										id="inputULY2" name="uly5186"> <input type="hidden" -->
<!-- 										id="inputULY3" name="uly5179"> <input type="hidden" -->
<!-- 										id="inputULY4" name="uly32652"> -->
<!-- 								</div> -->
<!-- 								<div class="col-sm-2 in-tit m-t-5">LRX</div> -->
<!-- 								<div class="col-sm-4 m-t-5"> -->
<!-- 									<input type="text" class="form-control js-input-roi-extent" -->
<!-- 										id="inputLRX" name="lrx4326"> <input type="hidden" -->
<!-- 										id="inputLRX2" name="lrx5186"> <input type="hidden" -->
<!-- 										id="inputLRX3" name="lrx5179"> <input type="hidden" -->
<!-- 										id="inputLRX4" name="lrx32652"> -->
<!-- 								</div> -->
<!-- 								<div class="col-sm-2 in-tit m-t-5">LRY</div> -->
<!-- 								<div class="col-sm-4 m-t-5"> -->
<!-- 									<input type="text" class="form-control js-input-roi-extent" -->
<!-- 										id="inputLRY" name="lry4326"> <input type="hidden" -->
<!-- 										id="inputLRY2" name="lry5186"> <input type="hidden" -->
<!-- 										id="inputLRY3" name="lry5179"> <input type="hidden" -->
<!-- 										id="inputLRY4" name="lry32652"> -->
<!-- 								</div> -->
<!-- 							</div> -->
<!-- 						</div> -->
						<!-- =========================== -->

						<div class="btn-wrap a-cent">
							<a href="javascript:search_mosaic()" class="btn btn-default"><i
								class="fas fa-search m-r-5"></i>검색</a>
						</div>
						<div class="sidepanel-s-title m-t-30">
							검색 결과 <a href="" class="btn btn-light f-right" id="modal4"
								data-toggle="modal" data-target="#myModal4"><i
								class="fas fa-project-diagram m-r-5"></i>수행</a>
						</div>
						<div class="modal fade" id="myModal4" tabindex="-1" role="dialog"
							aria-hidden="true">
							<div class="modal-dialog modal-sm">
								<div class="modal-content">
									<div class="modal-header">
										<button type="button" class="close" data-dismiss="modal"
											aria-label="Close">
											<span aria-hidden="true">&times;</span>
										</button>
										<h4 class="modal-title">모자이크</h4>
									</div>
									<div class="modal-body">
										<div class="panel-body">
											<form class="">
												<div class="form-group m-t-10">
													<label class="col-sm-4 control-label form-label">기준영상</label>
													<div class="col-sm-8">
														<select class="form-basic" id="mosaic_vido">
															<option></option>
														</select>
													</div>
												</div>
												<div class="form-group m-t-10">
													<label class="col-sm-4 control-label form-label">입력영상1</label>
													<div class="col-sm-8">
														<select class="form-basic" id="mosaic_vido_input">
															<option></option>
														</select>
													</div>
												</div>
												<div class="form-group m-t-10">
													<label class="col-sm-4 control-label form-label">입력영상2</label>
													<div class="col-sm-8">
														<select class="form-basic" id="mosaic_vido_input2">
															<option></option>
														</select>
													</div>
												</div>
												<div class="form-group m-t-10">
													<label class="col-sm-4 control-label form-label">입력영상3</label>
													<div class="col-sm-8">
														<select class="form-basic" id="mosaic_vido_input3">
															<option></option>
														</select>
													</div>
												</div>
												<div class="form-group m-t-10">
													<label class="col-sm-4 control-label form-label">입력영상4</label>
													<div class="col-sm-8">
														<select class="form-basic" id="mosaic_vido_input4">
															<option></option>
														</select>
													</div>
												</div>
												<div class="form-group m-t-10">
													<label for="mosaic_vido_input"
														class="col-sm-4 control-label form-label">결과영상</label>
													<div class="col-sm-8">
														<input type="text" class="form-control"
															id="mosaic_vido_result" value="MosaResult_">
													</div>
												</div>
											</form>

										</div>
									</div>
									<div class="modal-footer">
										<button type="button" onclick="mosaic_result();"
											class="btn btn-default">
											<i class="fas fa-check-square m-r-5"></i>처리
										</button>
										<button type="button" class="btn btn-gray"
											data-dismiss="modal" id="mosaic_cancel" aria-label="Close">
											<i class="fas fa-times m-r-5"></i>취소
										</button>
										<button id="btn_process" type="button" class="btn btn-default"
											onclick="fnScriptSavePopup('6')">
											<i class="fas fa-check-square m-r-5"></i>스크립트저장
										</button>
									</div>
									<div class="load" id="progress3" style="display: none;"></div>
								</div>
							</div>
						</div>
						<div class="panel-body">
							<div class="col-lg-12 js-search-result-area">
								<div id="mosaPanel" class="panel panel-default" style="height: 200px;">
<!-- 									<dl class="result-list-input" id="mosaic_result"> -->

<!-- 									</dl> -->
<!-- 									<dl class="result-list-input"> -->

<!-- 									</dl> -->
								</div>
							</div>
						</div>
						<div class="sidepanel-s-title m-t-30">처리 결과</div>
						<div class="panel-body">
							<div class="col-lg-12">
								<div class="panel panel-default" style="height: 100px;">
									<dl class="result-list-input" id="mosaicResult">
										<%--<dd>RGB_Landsat-5 TM Collection 1</dd>--%>
									</dl>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>

		</div>

	</div>

	<a href="#" class="sidebar-open-button"><i class="fa fa-bars"></i></a>
	<a href="#" class="sidebar-open-button-mobile"><i
		class="fa fa-bars"></i></a>


	<div class="modal fade" id="scriptModal" tabindex="-1" role="dialog"
		aria-hidden="true">
		<div class="modal-dialog modal-sm">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">스크립트저장</h4>
				</div>
				<div class="modal-body">
					<div class="panel-body">
						<form class="">
							<div class="form-group m-t-10">
								<label for="txt_meta" class="col-sm-4 control-label form-label">스크립트명</label>
								<div class="col-sm-8">
									<input id="input_script_nm" type="text" class="form-control"
										value="">
								</div>
								<input type="hidden" id="script_kind" value="">

							</div>
						</form>
					</div>
				</div>
				<div class="modal-footer">
					<button id="btn_process" type="button" class="btn btn-default"
						onclick="fnScriptSave();">
						<i class="fas fa-check-square m-r-5"></i>저장
					</button>
					<button type="button" class="btn btn-gray" data-dismiss="modal"
						aria-label="Close">
						<i clas="fas fa-times m-r-5"></i>취소
					</button>
				</div>
			</div>
		</div>
	</div>


	<!-- START CONTENT -->
	<div class="content">
		<div class="map-wrap-n" id="map1" style="background-size: 100% auto">

		</div>
	</div>


	<!-- ================================================
jQuery Library
================================================ -->
	<script src="js/jquery.min.js"></script>

	<!-- ================================================
Bootstrap Core JavaScript File
================================================ -->
	<script src="js/bootstrap/bootstrap.min.js"></script>

	<!-- ================================================
Plugin.js - Some Specific JS codes for Plugin Settings
================================================ -->
	<script src="js/plugins.js"></script>

	<!-- ================================================
Summernote
================================================ -->
	<script src="js/summernote/summernote.min.js"></script>

	<!-- ================================================
Data Tables
================================================ -->
	<script src="js/datatables/datatables.min.js"></script>

	<!-- ================================================
Sweet Alert
================================================ -->
	<script src="js/sweet-alert/sweet-alert.min.js"></script>

	<!-- ================================================
Kode Alert
================================================ -->
	<script src="js/kode-alert/main.js"></script>

	<!-- ================================================
jQuery UI
================================================ -->
	<script src="js/jquery-ui/jquery-ui.min.js"></script>

	<!-- ================================================
Moment.js
================================================ -->
	<script src="js/moment/moment.min.js"></script>

	<!-- ================================================
Full Calendar
================================================ -->
	<script src="js/full-calendar/fullcalendar.js"></script>

	<!-- ================================================
Bootstrap Date Range Picker
================================================ -->
	<script src="js/date-range-picker/daterangepicker.js"></script>
<!-- ================================================
jstree
================================================ -->
<script src="js/cm/cmsc003/lib/jstree-3.2.1/jstree.min.js"></script>

	<script src="js/map/map.js"></script>
	<script src="js/map/gis.js"></script>
	<!-- ================================================
Below codes are only for index widgets
================================================ -->
	<!-- ================================================
CMSC003 JS Dev
================================================ -->
	<script src="js/cm/cmsc003/src/cmsc003.js"></script>
	<script src="js/cm/cmsc003/src/gis/gis.js"></script>
	<script src="js/cm/cmsc003/src/storage/storage.js"></script>
	<script src="js/cm/cmsc003/src/dom/dom.js"></script>
	<script src="js/cm/cmsc003/src/converter/converter.js"></script>
	<script src="js/cm/cmsc003/src/util/util.js"></script>

	<script src="js/ci/cisc007/event.js"></script>
	<!-- Today Sales -->
	<script>

    // set up our data series with 50 random data points

/*     var seriesData = [ [], [], [] ];
    var random = new Rickshaw.Fixtures.RandomData(20);

    for (var i = 0; i < 110; i++) {
        random.addData(seriesData);
    }

    // instantiate our graph!

    var graph = new Rickshaw.Graph( {
        element: document.getElementById("todaysales"),
        renderer: 'bar',
        series: [
            {
                color: "#33577B",
                data: seriesData[0],
                name: 'Photodune'
            }, {
                color: "#77BBFF",
                data: seriesData[1],
                name: 'Themeforest'
            }, {
                color: "#C1E0FF",
                data: seriesData[2],
                name: 'Codecanyon'
            }
        ]
    } );

    graph.render();

    var hoverDetail = new Rickshaw.Graph.HoverDetail( {
        graph: graph,
        formatter: function(series, x, y) {
            var date = '<span class="date">' + new Date(x * 1000).toUTCString() + '</span>';
            var swatch = '<span class="detail_swatch" style="background-color: ' + series.color + '"></span>';
            var content = swatch + series.name + ": " + parseInt(y) + '<br>' + date;
            return content;
        }
    } );
 */
</script>

	<!-- Today Activity -->
	<script>
    // set up our data series with 50 random data points

/*     var seriesData = [ [], [], [] ];

    var random = new Rickshaw.Fixtures.RandomData(20);

    for (var i = 0; i < 50; i++) {
        random.addData(seriesData);
    } */

    // instantiate our graph!

    // var graph = new Rickshaw.Graph( {
    //   element: document.getElementById("todayactivity"),
    //   renderer: 'area',
    //   series: [
    //     {
    //       color: "#9A80B9",
    //       data: seriesData[0],
    //     }, {
    //       color: "#CDC0DC",
    //       data: seriesData[1],
    //       name: 'Tokyo'
    //     }
    //   ]
    // } );

    //graph.render();
    //
    // var hoverDetail = new Rickshaw.Graph.HoverDetail( {
    //   graph: graph,
    //   formatter: function(series, x, y) {
    //     var date = '<span class="date">' + new Date(x * 1000).toUTCString() + '</span>';
    //     var swatch = '<span class="detail_swatch" style="background-color: ' + series.color + '"></span>';
    //     var content = swatch + series.name + ": " + parseInt(y) + '<br>' + date;
    //     return content;
    //   }
    // } );

    //1. 맵생성.
    
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
    
    // var map = createMap('map1');
    var map = new ol.Map({
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
    map.addLayer(baselyaer);
    
//     map.addLayer(new ol.layer.Tile({
//     	isRequired: true,
//     	source: new ol.source.OSM()
//     }));

    map.addLayer(selectionLayer);
    map.addLayer(selectionLayerPoint);
    
    //2. 선택기능 활성화 roi
    var select = new Select(map);
    const imageLayer = new ol.layer.Image();
    imageLayer.set('isRequired', true);
    map.addLayer(imageLayer);

    const imageExtent = [957225.1387,1921135.1404,959008.6342,1922918.6358];
    map.getView().fit(imageExtent);

</script>

</body>
</html>