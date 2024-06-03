/**
 * 
 */
var ctsc004 = {

	init: function() {
		var _this = this;
		
		$('#ctsc004-search').on('click', function() {
			_this.search();
		})
	},
	
	showDownload: function() {
		
	},
	
	save: function() {
		
	},
	
	search : function() {
		
	},
	
	search: function() {
		
		Data = {
				'potogrfVidoCd' : $('#tnLogList').val(),
			}
		if($('#datepicker').val() != '') {
			Data['inqireBeginDate'] = $('#datepicker').val()
		}
		if($('#datepicker2').val() != '') {
			Data['inqireEndDate'] = $('#datepicker2').val()
		}
		if($('#tnLogList').val() == 'free') {
			$.ajax({
	            type : 'GET',
	            url : '/ctsc004/selectTnHistList.do',
	            dataType : 'json',
	            data : Data,
	        }).done(function(success) {
	        	var resultMap = success.result;
	        	$('#histInfoView').empty();
	        	$('#logInfoView').empty();
	        	$('#metaInfoView1').empty();
	        	$('#metaInfoView2').empty();
	        	$('#metaInfoView3').empty();
	        	$('#metaInfoView4').empty();
	        	
        		for(var i in resultMap) {
        			resultMap[i].potogrfEndDt = resultMap[i].potogrfEndDt.replace(/-/gi, "/");
            		$('#histInfoView').append(
        				'<ul class="result-list">'
							+ '<li>영상조회 종류 : 무료영상</li>'
							+ '<li>조회일 : '+ resultMap[i].potogrfEndDt +'</li>'
							+ '<li class="btn-list-wrap"><a href="#" class="btn btn-light" onclick="ctsc004.logSearch('+ resultMap[i].vidoId +')"><i class="far fa-file-alt"></i></a></li>'
						+ '</ul>'
                	);
            	}
	        	
	        }).fail(function(error) {
	            alert('이력정보를 불러올수 없습니다');
	        });
		}
		
		if($('#tnLogList').val() == 'emer') {
			$.ajax({
	            type : 'GET',
	            url : '/ctsc004/selectTnLogList.do',
	            dataType : 'json',
	            data : Data,
	        }).done(function(success) {
	        	var resultMap = success.result;
	        	$('#logInfoView').empty();
	        	$('#metaInfoView1').empty();
	        	$('#metaInfoView2').empty();
	        	$('#metaInfoView3').empty();
	        	$('#metaInfoView4').empty();
	        	
        		$('#histInfoView').empty();
        		$('#histInfoView').append('<li>검색결과가 없습니다</li>');
        		for(var i in resultMap) {
            		$('#logInfoView').append(
                			'<tr>'
        					+ '<td class="text-center">'+ resultMap[i].vidoId +'</td>'
        					+ '<td class="text-center">긴급영상</td>'
        					+ '<td class="text-center">Y</td>'
        					+ '<td class="text-center">'+ resultMap[i].streCoursNm +'</td>'
        					+ '<td class="text-center">'+ resultMap[i].potogrfEndDt +'</td>'
        					+ '<td class="text-center">'
        						+ '<a href="#" class="btn btn-rounded btn-light"><i class="far fa-file-alt"></i></a>'
        					+ '</td>'
        					+ '<td class="text-center"><input type=\'checkbox\' name=\'animal\' value=\'dog\'/></td>'
        				+ '</tr>'	
                	);
            	}	
	        }).fail(function(error) {
	            alert('로그정보를 불러올수 없습니다');
	        });
		}
		
	},
	
	logSearch : function(vidoId) {
		Data = {
		'vidoId' : vidoId,
		'potogrfVidoCd' : 'free',
		}
		
		$.ajax({
	      type : 'GET',
	      url : '/ctsc004/selectTnLogList.do',
	      dataType : 'json',
	      data : Data,
	  }).done(function(success) {
	  	var resultMap = success.result;
	  	$('#logInfoView').empty();
	  	$('#metaInfoView1').empty();
    	$('#metaInfoView2').empty();
    	$('#metaInfoView3').empty();
    	$('#metaInfoView4').empty();
    	
	  		for(var i in resultMap) {
	  			if (resultMap[i].flag == '1') {
	  				resultMap[i].flag = 'Y';
	  			} else {
	  				resultMap[i].flag = 'N';
	  			}
	  			if(resultMap[i].flag == undefined) resultMap[i].flag = '';
    			if(resultMap[i].innerFileCoursNm == undefined) resultMap[i].innerFileCoursNm = '';
    			if(resultMap[i].potogrfEndDt == undefined) resultMap[i].potogrfEndDt = '';
	      		$('#logInfoView').append(
	          			'<tr onclick="ctsc004.metaSearch('+ resultMap[i].vidoId +', \'free\')">'
	  					+ '<td class="text-center">'+ resultMap[i].vidoId +'</td>'
	  					+ '<td class="text-center">무료영상</td>'
	  					+ '<td class="text-center">' + resultMap[i].flag +'</td>'
	  					+ '<td class="text-center">'+ resultMap[i].innerFileCoursNm +'</td>'
	  					+ '<td class="text-center">'+ resultMap[i].potogrfEndDt +'</td>'
	  					+ '<td class="text-center">'
	  						+ '<a href="#" class="btn btn-rounded btn-light"><i class="far fa-file-alt"></i></a>'
	  					+ '</td>'
	  					+ '<td class="text-center"><input type=\'checkbox\' name=\'animal\' value=\'dog\'/></td>'
	  				+ '</tr>'	
	          	);
	      	}
	      }).fail(function(error) {
	    	  alert('로그정보를 불러올수 없습니다');
	      });
	},
	
	metaSearch : function(vidoId, vidoCd) {
		Data = {
				'vidoId' : vidoId,
				'potogrfVidoCd' : vidoCd,
			}
			$.ajax({
	            type : 'GET',
	            url : '/ctsc004/selectTnMetaList.do',
	            dataType : 'json',
	            data : Data,
	        }).done(function(success) {
	        	var resultMap = success.result;
	        	//document.querySelector('#metaInfoHead').style.display = 'block';
	        	$('#metaInfoView1').empty();
	        	$('#metaInfoView2').empty();
	        	$('#metaInfoView3').empty();
	        	$('#metaInfoView4').empty();
	        	for(var i in resultMap) {
	        		if(resultMap[i].rawSatlitVidoFileNm == undefined) resultMap[i].rawSatlitVidoFileNm = ' ';
	        		if(resultMap[i].bgngDt == undefined) resultMap[i].bgngDt = ' ';
	        		if(resultMap[i].endDt == undefined) resultMap[i].endDt = ' ';
	        		if(resultMap[i].satlitNo == undefined) resultMap[i].satlitNo = ' ';
	        		if(resultMap[i].mapPrjctnCn == undefined) resultMap[i].mapPrjctnCn = ' ';
	        		if(resultMap[i].spherCn == undefined) resultMap[i].spherCn = ' ';
	        		if(resultMap[i].acqsYr == undefined) resultMap[i].acqsYr = ' ';
	        		
            		$('#metaInfoView1').append(
                			'<tr>'
        					+ '<td class="text-center">'+ resultMap[i].vidoId +'</td>'
        					+ '<td class="text-center">' + resultMap[i].bgngDt +'</td>'
        					+ '<td class="text-center">' + resultMap[i].endDt +'</td>'
        					+ '<td class="text-center">'+ resultMap[i].satlitNo +'</td>'
        					+ '<td class="text-center">'+ resultMap[i].mapPrjctnCn +'</td>'
        					+ '<td class="text-center">'+ resultMap[i].spherCn +'</td>'
        					+ '<td class="text-center">'+ resultMap[i].acqsYr +'</td>'
        				+ '</tr>'	
                	);
            		
            		if(resultMap[i].acqsMm == undefined) resultMap[i].acqsMm = ' ';
	        		if(resultMap[i].bandCo == undefined) resultMap[i].bandCo = ' ';
	        		if(resultMap[i].prjctnCn == undefined) resultMap[i].prjctnCn = ' ';
	        		if(resultMap[i].datumCn == undefined) resultMap[i].datumCn = ' ';
	        		if(resultMap[i].vidoSensorNm == undefined) resultMap[i].vidoSensorNm = ' ';
	        		if(resultMap[i].sensorModelNm == undefined) resultMap[i].sensorModelNm = ' ';
	        		if(resultMap[i].potogrfBeginDt == undefined) resultMap[i].potogrfBeginDt = ' ';
	        		
            		$('#metaInfoView2').append(
                			'<tr>'
        					+ '<td class="text-center">'+ resultMap[i].acqsMm +'</td>'
        					+ '<td class="text-center">' + resultMap[i].bandCo +'</td>'
        					+ '<td class="text-center">' + resultMap[i].prjctnCn +'</td>'
        					+ '<td class="text-center">'+ resultMap[i].datumCn +'</td>'
        					+ '<td class="text-center">'+ resultMap[i].vidoSensorNm +'</td>'
        					+ '<td class="text-center">'+ resultMap[i].sensorModelNm +'</td>'
        					+ '<td class="text-center">'+ resultMap[i].potogrfBeginDt +'</td>'
        				+ '</tr>'	
                	);
            		
            		if(resultMap[i].potogrfEndDt == undefined) resultMap[i].potogrfEndDt = ' ';
	        		if(resultMap[i].trackNo == undefined) resultMap[i].trackNo = ' ';
	        		if(resultMap[i].potogrfModeNm == undefined) resultMap[i].potogrfModeNm = ' ';
	        		if(resultMap[i].rollangCo == undefined) resultMap[i].rollangCo = ' ';
	        		if(resultMap[i].pichangCo == undefined) resultMap[i].pichangCo = ' ';
	        		if(resultMap[i].prdctnNo == undefined) resultMap[i].prdctnNo = ' ';
	        		if(resultMap[i].rawSatlitVidoFileNm == undefined) resultMap[i].rawSatlitVidoFileNm = ' ';
            		
            		$('#metaInfoView3').append(
                			'<tr>'
        					+ '<td class="text-center">'+ resultMap[i].potogrfEndDt +'</td>'
        					+ '<td class="text-center">' + resultMap[i].trackNo +'</td>'
        					+ '<td class="text-center">' + resultMap[i].potogrfModeNm +'</td>'
        					+ '<td class="text-center">'+ resultMap[i].rollangCo +'</td>'
        					+ '<td class="text-center">'+ resultMap[i].pichangCo +'</td>'
        					+ '<td class="text-center">'+ resultMap[i].prdctnNo +'</td>'
        					+ '<td class="text-center">'+ resultMap[i].rawSatlitVidoFileNm +'</td>'
        				+ '</tr>'	
                	);
            		
            		if(resultMap[i].satlitVidoFileNm == undefined) resultMap[i].satlitVidoFileNm = ' ';
	        		if(resultMap[i].satlitVidoHderFileNm == undefined) resultMap[i].satlitVidoHderFileNm = ' ';
	        		if(resultMap[i].satlitVidoSumryFileNm == undefined) resultMap[i].satlitVidoSumryFileNm = ' ';
	        		if(resultMap[i].stereoVidoFileNm == undefined) resultMap[i].stereoVidoFileNm = ' ';
	        		if(resultMap[i].regDt == undefined) resultMap[i].regDt = ' ';
	        		if(resultMap[i].mdfcnDt == undefined) resultMap[i].mdfcnDt = ' ';
	        		if(resultMap[i].useRstrctCn == undefined) resultMap[i].useRstrctCn = ' ';
            		
            		$('#metaInfoView4').append(
                			'<tr>'
        					+ '<td class="text-center">'+ resultMap[i].satlitVidoFileNm +'</td>'
        					+ '<td class="text-center">' + resultMap[i].satlitVidoHderFileNm +'</td>'
        					+ '<td class="text-center">' + resultMap[i].satlitVidoSumryFileNm +'</td>'
        					+ '<td class="text-center">'+ resultMap[i].stereoVidoFileNm +'</td>'
        					+ '<td class="text-center">'+ resultMap[i].regDt +'</td>'
        					+ '<td class="text-center">'+ resultMap[i].mdfcnDt +'</td>'
        					+ '<td class="text-center">'+ resultMap[i].useRstrctCn +'</td>'
        				+ '</tr>'	
                	);
            	}
	        	
	        }).fail(function(error) {
	            alert('메타정보를 불러올수 없습니다');
	        });
	},
	
	get: function() {
		
	},
	
	set: function() {
		
	}
}

ctsc004.init();