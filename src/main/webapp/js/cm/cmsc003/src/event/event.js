// 데이터픽커 초기화
$('#datepickerContainer').datepicker({
	format: "yyyy-mm-dd", //데이터 포맷 형식(yyyy : 년 mm : 월 dd : 일 )
	autoclose: true, //사용자가 날짜를 클릭하면 자동 캘린더가 닫히는 옵션
	calendarWeeks: false, //캘린더 옆에 몇 주차인지 보여주는 옵션 기본값 false 보여주려면 true
	disableTouchKeyboard: false, //모바일에서 플러그인 작동 여부 기본값 false 가 작동 true가 작동 안함.
	immediateUpdates: false, //사용자가 보는 화면으로 바로바로 날짜를 변경할지 여부 기본값 :false 
	multidate: false, //여러 날짜 선택할 수 있게 하는 옵션 기본값 :false 
	templates: {
		leftArrow: '&laquo;',
		rightArrow: '&raquo;'
	}, //다음달 이전달로 넘어가는 화살표 모양 커스텀 마이징 
	showWeekDays: true,// 위에 요일 보여주는 옵션 기본값 : true
	todayHighlight: true, //오늘 날짜에 하이라이팅 기능 기본값 :false 
	toggleActive: true, //이미 선택된 날짜 선택하면 기본값 : false인경우 그대로 유지 true인 경우 날짜 삭제
	weekStart: 0,//달력 시작 요일 선택하는 것 기본값은 0인 일요일 
	language: "ko" //달력의 언어 선택, 그에 맞는 js로 교체해줘야한다.
});

// 시작날짜 버튼 클릭
$("#btnOpenDatepickerStart").click(function() {
	$('#inputStartDate').focus();
});

// 종료날짜 버튼 클릭
$("#btnOpenDatepickerEnd").click(function() {
	$('#inputEndDate').focus();
});

// 미리보기 버튼 클릭 - 정사영상
$('.js-search-result-area').on('click', '.ui-btn-preview-ortho-image-layer', function(e) {
	console.log($(this).val());
	var id = $(this).data('tree'); // 현재 선택한 레벨의 노드 아이디
	var tree = $(`#${id}`).jstree().get_json('#' + id, { "flat": true }); // 선택 노드 + 자식 노드
	if (tree.length > 0) {
		if ($(`#${id}`).hasClass('ui-btn-clicked-layer-preview')) { // 미리보기 제거
			for (var i = 0; i < tree.length; i++) {
				if ($(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) { // 미리보기중인 자식노드만 제거
					var split = $(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class;
					var before = split.split(' ');
					var after = [];
					for (var j = 0; j < before.length; j++) {
						if (before[j].length != 'ui-btn-clicked-layer-preview') { after.push(before[j]); }
					}
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class = after.join(' '); // 트리가 닫혀도 상태 유지를 위해 jstree에서 클래스 수정이 필요함
					$(`#${tree[i].id}`).removeClass('ui-btn-clicked-layer-preview');
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.removeLayerById('ortho-' + $(`#${tree[i].id}`).attr('value'));
					}
				}
			}
		} else { //미리보기 생성
			for (var i = 0; i < tree.length; i++) {
				if (!$(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) { // 미리보기 상태가 아닌 경우
					$(`#${id}`).jstree().open_node(tree[i]);
					$(`#${tree[i].id}`).addClass('ui-btn-clicked-layer-preview');
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class += ' ui-btn-clicked-layer-preview';
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.updateImageLayerTemp('ortho-' + $(`#${tree[i].id}`).attr('value'));
					}
				}
			}
		}
	}
});

// 미리보기 버튼 클릭 - 항공사진
$('.js-search-result-area').on('click', '.ui-btn-preview-aerial-image-layer', function(e) {
	console.log($(this).val());
	var id = $(this).data('tree'); // 현재 선택한 레벨의 노드 아이디
	var tree = $(`#${id}`).jstree().get_json('#' + id, { "flat": true });
	if (tree.length > 0) {
		//$(`#${id}`).jstree("load_all");
		if ($(`#${id}`).hasClass('ui-btn-clicked-layer-preview')) { // 미리보기 제거
			for (var i = 0; i < tree.length; i++) {
				if ($(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					var split = $(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class;
					var before = split.split(' ');
					var after = [];
					for (var j = 0; j < before.length; j++) {
						if (before[j].length != 'ui-btn-clicked-layer-preview') { after.push(before[j]); }
					}
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class = after.join(' ');
					$(`#${tree[i].id}`).removeClass('ui-btn-clicked-layer-preview');
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.removeLayerById('aerial-' + $(`#${tree[i].id}`).attr('value'));
					}

				}
			}
		} else { //미리보기 생성
			for (var i = 0; i < tree.length; i++) {
				//$(`button[data-tree="${tree[i].id}"]`).addClass('ui-btn-clicked-layer-preview');
				if (!$(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					$(`#${id}`).jstree().open_node(tree[i]);
					$(`#${tree[i].id}`).addClass('ui-btn-clicked-layer-preview');
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class += ' ui-btn-clicked-layer-preview';
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.updateImageLayerTemp('aerial-' + $(`#${tree[i].id}`).attr('value'));
					}

				}
			}
		}
	}
});

// 미리보기 버튼 클릭 - dem 데이터
$('.js-search-result-area').on('click', '.ui-btn-preview-dem-layer', function(e) {
	console.log($(this).val());
	var id = $(this).data('tree'); // 현재 선택한 레벨의 노드 아이디
	var tree = $(`#${id}`).jstree().get_json('#' + id, { "flat": true });
	if (tree.length > 0) {
		//$(`#${id}`).jstree("load_all");
		if ($(`#${id}`).hasClass('ui-btn-clicked-layer-preview')) { // 미리보기 제거
			for (var i = 0; i < tree.length; i++) {
				if ($(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					var split = $(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class;
					var before = split.split(' ');
					var after = [];
					for (var j = 0; j < before.length; j++) {
						if (before[j].length != 'ui-btn-clicked-layer-preview') { after.push(before[j]); }
					}
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class = after.join(' ');
					$(`#${tree[i].id}`).removeClass('ui-btn-clicked-layer-preview');
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.removeLayerById('dem-' + $(`#${tree[i].id}`).attr('value'));
					}

				}
			}
		} else { //미리보기 생성
			for (var i = 0; i < tree.length; i++) {
				//$(`button[data-tree="${tree[i].id}"]`).addClass('ui-btn-clicked-layer-preview');
				if (!$(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					$(`#${id}`).jstree().open_node(tree[i]);
					$(`#${tree[i].id}`).addClass('ui-btn-clicked-layer-preview');
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class += ' ui-btn-clicked-layer-preview';
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.updateImageLayerTemp('dem-' + $(`#${tree[i].id}`).attr('value'));
					}

				}
			}
		}
	}
});

// 미리보기 버튼 클릭 - 위성영상
$('.js-search-result-area').on('click', '.ui-btn-preview-satellite-image-layer', function(e) {
	console.log($(this).val());
	var id = $(this).data('tree'); // 현재 선택한 레벨의 노드 아이디
	var tree = $(`#${id}`).jstree().get_json('#' + id, { "flat": true });
	if (tree.length > 0) {
		//$(`#${id}`).jstree("load_all");
		if ($(`#${id}`).hasClass('ui-btn-clicked-layer-preview')) { // 미리보기 제거
			for (var i = 0; i < tree.length; i++) {
				if ($(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					var split = $(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class;
					var before = split.split(' ');
					var after = [];
					for (var j = 0; j < before.length; j++) {
						if (before[j].length != 'ui-btn-clicked-layer-preview') { after.push(before[j]); }
					}
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class = after.join(' ');
					$(`#${tree[i].id}`).removeClass('ui-btn-clicked-layer-preview');
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.removeLayerById(tree[i].id.split('-')[2] + '-' + $(`#${tree[i].id}`).attr('value'));
					}
				}

			}
		} else { //미리보기 생성
			for (var i = 0; i < tree.length; i++) {
				if (!$(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					$(`#${id}`).jstree().open_node(tree[i]);
					$(`#${tree[i].id}`).addClass('ui-btn-clicked-layer-preview');
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class += ' ui-btn-clicked-layer-preview';
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.updateImageLayerTemp(tree[i].id.split('-')[2] + '-' + $(`#${tree[i].id}`).attr('value'));
					}

				}
			}
		}
	}
});

// 미리보기 버튼 클릭 - 위성영상 komsat
$('.js-search-result-area').on('click', '.ui-btn-preview-kompsat-image-layer', function(e) {
	console.log($(this).val());
	var id = $(this).data('tree'); // 현재 선택한 레벨의 노드 아이디
	var tree = $(`#${id}`).jstree().get_json('#' + id, { "flat": true });
	if (tree.length > 0) {
		//$(`#${id}`).jstree("load_all");
		if ($(`#${id}`).hasClass('ui-btn-clicked-layer-preview')) { // 미리보기 제거
			for (var i = 0; i < tree.length; i++) {
				if ($(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					var split = $(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class;
					var before = split.split(' ');
					var after = [];
					for (var j = 0; j < before.length; j++) {
						if (before[j].length != 'ui-btn-clicked-layer-preview') { after.push(before[j]); }
					}
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class = after.join(' ');
					$(`#${tree[i].id}`).removeClass('ui-btn-clicked-layer-preview');
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.removeLayerById('kompsat-' + $(`#${tree[i].id}`).attr('value'));
					}

				}
			}
		} else { //미리보기 생성
			for (var i = 0; i < tree.length; i++) {
				if (!$(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					$(`#${id}`).jstree().open_node(tree[i]);
					$(`#${tree[i].id}`).addClass('ui-btn-clicked-layer-preview');
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class += ' ui-btn-clicked-layer-preview';
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.updateImageLayerTemp('kompsat-' + $(`#${tree[i].id}`).attr('value'));
					}

				}
			}
		}
	}
});

// 미리보기 버튼 클릭 - 위성영상 landsat
$('.js-search-result-area').on('click', '.ui-btn-preview-landsat-image-layer', function(e) {
	console.log($(this).val());
	var id = $(this).data('tree'); // 현재 선택한 레벨의 노드 아이디
	var tree = $(`#${id}`).jstree().get_json('#' + id, { "flat": true });
	if (tree.length > 0) {
		//$(`#${id}`).jstree("load_all");
		if ($(`#${id}`).hasClass('ui-btn-clicked-layer-preview')) { // 미리보기 제거
			for (var i = 0; i < tree.length; i++) {
				if ($(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					var split = $(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class;
					var before = split.split(' ');
					var after = [];
					for (var j = 0; j < before.length; j++) {
						if (before[j].length != 'ui-btn-clicked-layer-preview') { after.push(before[j]); }
					}
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class = after.join(' ');
					$(`#${tree[i].id}`).removeClass('ui-btn-clicked-layer-preview');
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.removeLayerById('landsat-' + $(`#${tree[i].id}`).attr('value'));
					}
				}

			}
		} else { //미리보기 생성
			for (var i = 0; i < tree.length; i++) {
				if (!$(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					$(`#${id}`).jstree().open_node(tree[i]);
					$(`#${tree[i].id}`).addClass('ui-btn-clicked-layer-preview');
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class += ' ui-btn-clicked-layer-preview';
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.updateImageLayerTemp('landsat-' + $(`#${tree[i].id}`).attr('value'));
					}

				}
			}
		}
	}
});

// 미리보기 버튼 클릭 - 위성영상 sentinel (원본)
/*$('.js-search-result-area').on('click', '.ui-btn-preview-sentinel-image-layer', function(e) {
	console.log($(this).val());
	var id = $(this).data('tree'); // 현재 선택한 레벨의 노드 아이디
	var tree = $(`#${id}`).jstree().get_json('#' + id_temp, { "flat": true });
	if (tree.length > 0) {
		//$(`#${id}`).jstree("load_all");
		if ($(`#${id}`).hasClass('ui-btn-clicked-layer-preview')) { // 미리보기 제거
			for (var i = 0; i < tree.length; i++) {
				if ($(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					var split = $(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class;
					var before = split.split(' ');
					var after = [];
					for (var j = 0; j < before.length; j++) {
						if (before[j].length != 'ui-btn-clicked-layer-preview') { after.push(before[j]); }
					}
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class = after.join(' ');
					$(`#${tree[i].id}`).removeClass('ui-btn-clicked-layer-preview');
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.removeLayerById('sentinel-' + $(`#${tree[i].id}`).attr('value'));
					}

				}
			}
		} else { //미리보기 생성
			for (var i = 0; i < tree.length; i++) {
				if (!$(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					$(`#${id}`).jstree().open_node(tree[i]);
					$(`#${tree[i].id}`).addClass('ui-btn-clicked-layer-preview');
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class += ' ui-btn-clicked-layer-preview';
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.updateImageLayerTemp('sentinel-' + $(`#${tree[i].id}`).attr('value'));
					}

				}
			}
		}
	}
});*/

// 미리보기 버튼 클릭 - 위성영상 sentinel (수정본 - 새롬)
$('.js-search-result-area').on('click', '.ui-btn-preview-sentinel-image-layer', function(e) {
	console.log($(this).val());
	var id = $(this).data('tree'); // 현재 선택한 레벨의 노드 아이디
	var id_temp = id.replace(".","\\.");
	var tree = $(`#${id_temp}`).jstree().get_json('#' + id, { "flat": true });
	
	if (tree.length > 0) {
		//$(`#${id}`).jstree("load_all");
		if ($(`#${id_temp}`).hasClass('ui-btn-clicked-layer-preview')) { // 미리보기 제거
			for (var i = 0; i < tree.length; i++) {
				var treeId = tree[i].id;
				var treeIdTemp = tree[i].id.replace(".","\\.");
				if ($(`#${treeIdTemp}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${treeId} > a > button`).is(":disabled")) {
					var split = $(`#${id_temp}`).jstree()._model.data[tree[i].id].li_attr.class;
					var before = split.split(' ');
					var after = [];
					for (var j = 0; j < before.length; j++) {
						if (before[j].length != 'ui-btn-clicked-layer-preview') { after.push(before[j]); }
					}
					$(`#${id_temp}`).jstree()._model.data[tree[i].id].li_attr.class = after.join(' ');
					$(`#${treeIdTemp}`).removeClass('ui-btn-clicked-layer-preview');
					if ($(`li#${treeIdTemp}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.removeLayerById('sentinel-' + $(`#${treeIdTemp}`).attr('value'));
					}

				}
			}
		} else { //미리보기 생성
			for (var i = 0; i < tree.length; i++) {
				var treeId = tree[i].id;
				var treeIdTemp = tree[i].id.replace(".","\\.");
				if (!$(`#${treeIdTemp}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${treeId} > a > button`).is(":disabled")) {
					$(`#${id_temp}`).jstree().open_node(tree[i]);
					$(`#${treeIdTemp}`).addClass('ui-btn-clicked-layer-preview');
					$(`#${id_temp}`).jstree()._model.data[treeId].li_attr.class += ' ui-btn-clicked-layer-preview';
					if ($(`li#${treeIdTemp}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.updateImageLayerTemp('sentinel-' + $(`#${treeIdTemp}`).attr('value'));
					}

				}
			}
		}
	}
});




// 미리보기 버튼 클릭 - 위성영상 cas
$('.js-search-result-area').on('click', '.ui-btn-preview-cas-image-layer', function(e) {
	console.log($(this).val());
	var id = $(this).data('tree'); // 현재 선택한 레벨의 노드 아이디
	var tree = $(`#${id}`).jstree().get_json('#' + id, { "flat": true });
	if (tree.length > 0) {
		//$(`#${id}`).jstree("load_all");
		if ($(`#${id}`).hasClass('ui-btn-clicked-layer-preview')) { // 미리보기 제거
			for (var i = 0; i < tree.length; i++) {
				if ($(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					var split = $(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class;
					var before = split.split(' ');
					var after = [];
					for (var j = 0; j < before.length; j++) {
						if (before[j].length != 'ui-btn-clicked-layer-preview') { after.push(before[j]); }
					}
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class = after.join(' ');
					$(`#${tree[i].id}`).removeClass('ui-btn-clicked-layer-preview');
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.removeLayerById('cas-' + $(`#${tree[i].id}`).attr('value'));
					}
				}

			}
		} else { //미리보기 생성
			for (var i = 0; i < tree.length; i++) {
				if (!$(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					$(`#${id}`).jstree().open_node(tree[i]);
					$(`#${tree[i].id}`).addClass('ui-btn-clicked-layer-preview');
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class += ' ui-btn-clicked-layer-preview';
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.updateImageLayerTemp('cas-' + $(`#${tree[i].id}`).attr('value'));
					}
				}

			}
		}
	}
});

//미리보기 버튼 클릭 - 위성영상 sar  : 긴급공간생성 08.28
$('.js-search-result-area').on('click', '.ui-btn-preview-sar-image-layer', function(e) {
	console.log($(this).val());
	var id = $(this).data('tree'); // 현재 선택한 레벨의 노드 아이디
	var tree = $(`#${id}`).jstree().get_json('#' + id, { "flat": true });
	if (tree.length > 0) {
		//$(`#${id}`).jstree("load_all");
		if ($(`#${id}`).hasClass('ui-btn-clicked-layer-preview')) { // 미리보기 제거
			for (var i = 0; i < tree.length; i++) {
				if ($(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					var split = $(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class;
					var before = split.split(' ');
					var after = [];
					for (var j = 0; j < before.length; j++) {
						if (before[j].length != 'ui-btn-clicked-layer-preview') { after.push(before[j]); }
					}
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class = after.join(' ');
					$(`#${tree[i].id}`).removeClass('ui-btn-clicked-layer-preview');
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.removeLayerById('sar-' + $(`#${tree[i].id}`).attr('value'));
					}
				}

			}
		} else { //미리보기 생성
			for (var i = 0; i < tree.length; i++) {
				if (!$(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					$(`#${id}`).jstree().open_node(tree[i]);
					$(`#${tree[i].id}`).addClass('ui-btn-clicked-layer-preview');
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class += ' ui-btn-clicked-layer-preview';
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.updateImageLayerTemp('sar-' + $(`#${tree[i].id}`).attr('value'));
					}

				}
			}
		}
	}
});


// 미리보기 버튼 클릭 - 홍수
$('.js-search-result-area').on('click', '.ui-btn-preview-flood-image-layer', function(e) {
	console.log($(this).val());
	var id = $(this).data('tree'); // 현재 선택한 레벨의 노드 아이디
	var tree = $(`#${id}`).jstree().get_json('#' + id, { "flat": true });
	if (tree.length > 0) {
		//$(`#${id}`).jstree("load_all");
		if ($(`#${id}`).hasClass('ui-btn-clicked-layer-preview')) { // 미리보기 제거
			for (var i = 0; i < tree.length; i++) {
				if ($(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					var split = $(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class;
					var before = split.split(' ');
					var after = [];
					for (var j = 0; j < before.length; j++) {
						if (before[j].length != 'ui-btn-clicked-layer-preview') { after.push(before[j]); }
					}
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class = after.join(' ');
					$(`#${tree[i].id}`).removeClass('ui-btn-clicked-layer-preview');
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.removeLayerById('flood-' + $(`#${tree[i].id}`).attr('value'));
					}

				}
			}
		} else { //미리보기 생성
			for (var i = 0; i < tree.length; i++) {
				if (!$(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					$(`#${id}`).jstree().open_node(tree[i]);
					$(`#${tree[i].id}`).addClass('ui-btn-clicked-layer-preview');
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class += ' ui-btn-clicked-layer-preview';
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.updateImageLayerTemp('flood-' + $(`#${tree[i].id}`).attr('value'));
					}

				}
			}
		}
	}
});

// 미리보기 버튼 클릭 - 산불
$('.js-search-result-area').on('click', '.ui-btn-preview-forestfire-image-layer', function(e) {
	console.log($(this).val());
	var id = $(this).data('tree'); // 현재 선택한 레벨의 노드 아이디
	var tree = $(`#${id}`).jstree().get_json('#' + id, { "flat": true });
	if (tree.length > 0) {
		//$(`#${id}`).jstree("load_all");
		if ($(`#${id}`).hasClass('ui-btn-clicked-layer-preview')) { // 미리보기 제거
			for (var i = 0; i < tree.length; i++) {
				if ($(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					var split = $(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class;
					var before = split.split(' ');
					var after = [];
					for (var j = 0; j < before.length; j++) {
						if (before[j].length != 'ui-btn-clicked-layer-preview') { after.push(before[j]); }
					}
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class = after.join(' ');
					$(`#${tree[i].id}`).removeClass('ui-btn-clicked-layer-preview');
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.removeLayerById('forestFire-' + $(`#${tree[i].id}`).attr('value'));
					}

				}
			}
		} else { //미리보기 생성
			for (var i = 0; i < tree.length; i++) {
				if (!$(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					$(`#${id}`).jstree().open_node(tree[i]);
					$(`#${tree[i].id}`).addClass('ui-btn-clicked-layer-preview');
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class += ' ui-btn-clicked-layer-preview';
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.updateImageLayerTemp('forestFire-' + $(`#${tree[i].id}`).attr('value'));
					}

				}
			}
		}
	}
});

// 미리보기 버튼 클릭 - 지진
$('.js-search-result-area').on('click', '.ui-btn-preview-earthquake-image-layer', function(e) {
	console.log($(this).val());
	var id = $(this).data('tree'); // 현재 선택한 레벨의 노드 아이디
	var tree = $(`#${id}`).jstree().get_json('#' + id, { "flat": true });
	if (tree.length > 0) {
		//$(`#${id}`).jstree("load_all");
		if ($(`#${id}`).hasClass('ui-btn-clicked-layer-preview')) { // 미리보기 제거
			for (var i = 0; i < tree.length; i++) {
				if ($(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					var split = $(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class;
					var before = split.split(' ');
					var after = [];
					for (var j = 0; j < before.length; j++) {
						if (before[j].length != 'ui-btn-clicked-layer-preview') { after.push(before[j]); }
					}
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class = after.join(' ');
					$(`#${tree[i].id}`).removeClass('ui-btn-clicked-layer-preview');
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.removeLayerById('earthquake-' + $(`#${tree[i].id}`).attr('value'));
					}

				}
			}
		} else { //미리보기 생성
			for (var i = 0; i < tree.length; i++) {
				if (!$(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					$(`#${id}`).jstree().open_node(tree[i]);
					$(`#${tree[i].id}`).addClass('ui-btn-clicked-layer-preview');
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class += ' ui-btn-clicked-layer-preview';
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.updateImageLayerTemp('earthquake-' + $(`#${tree[i].id}`).attr('value'));
					}
				}

			}
		}
	}
});

// 미리보기 버튼 클릭 - 산사태
$('.js-search-result-area').on('click', '.ui-btn-preview-landslide-image-layer', function(e) {
	console.log($(this).val());
	var id = $(this).data('tree'); // 현재 선택한 레벨의 노드 아이디
	var tree = $(`#${id}`).jstree().get_json('#' + id, { "flat": true });
	if (tree.length > 0) {
		//$(`#${id}`).jstree("load_all");
		if ($(`#${id}`).hasClass('ui-btn-clicked-layer-preview')) { // 미리보기 제거
			for (var i = 0; i < tree.length; i++) {
				if ($(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					var split = $(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class;
					var before = split.split(' ');
					var after = [];
					for (var j = 0; j < before.length; j++) {
						if (before[j].length != 'ui-btn-clicked-layer-preview') { after.push(before[j]); }
					}
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class = after.join(' ');
					$(`#${tree[i].id}`).removeClass('ui-btn-clicked-layer-preview');
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.removeLayerById('landslide-' + $(`#${tree[i].id}`).attr('value'));
					}

				}
			}
		} else { //미리보기 생성
			for (var i = 0; i < tree.length; i++) {
				if (!$(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					$(`#${id}`).jstree().open_node(tree[i]);
					$(`#${tree[i].id}`).addClass('ui-btn-clicked-layer-preview');
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class += ' ui-btn-clicked-layer-preview';
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.updateImageLayerTemp('landslide-' + $(`#${tree[i].id}`).attr('value'));
					}

				}
			}
		}
	}
});

// 미리보기 버튼 클릭 - 해양재난
$('.js-search-result-area').on('click', '.ui-btn-preview-maritime-image-layer', function(e) {
	console.log($(this).val());
	var id = $(this).data('tree'); // 현재 선택한 레벨의 노드 아이디
	var tree = $(`#${id}`).jstree().get_json('#' + id, { "flat": true });
	if (tree.length > 0) {
		//$(`#${id}`).jstree("load_all");
		if ($(`#${id}`).hasClass('ui-btn-clicked-layer-preview')) { // 미리보기 제거
			for (var i = 0; i < tree.length; i++) {
				if ($(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					var split = $(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class;
					var before = split.split(' ');
					var after = [];
					for (var j = 0; j < before.length; j++) {
						if (before[j].length != 'ui-btn-clicked-layer-preview') { after.push(before[j]); }
					}
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class = after.join(' ');
					$(`#${tree[i].id}`).removeClass('ui-btn-clicked-layer-preview');
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.removeLayerById('maritime-' + $(`#${tree[i].id}`).attr('value'));
					}

				}
			}
		} else { //미리보기 생성
			for (var i = 0; i < tree.length; i++) {
				if (!$(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					$(`#${id}`).jstree().open_node(tree[i]);
					$(`#${tree[i].id}`).addClass('ui-btn-clicked-layer-preview');
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class += ' ui-btn-clicked-layer-preview';
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.updateImageLayerTemp('maritime-' + $(`#${tree[i].id}`).attr('value'));
					}

				}
			}
		}
	}
});

// 미리보기 버튼 클릭 - 적조
$('.js-search-result-area').on('click', '.ui-btn-preview-redTide-image-layer', function(e) {
	console.log($(this).val());
	var id = $(this).data('tree'); // 현재 선택한 레벨의 노드 아이디
	var tree = $(`#${id}`).jstree().get_json('#' + id, { "flat": true });
	if (tree.length > 0) {
		//$(`#${id}`).jstree("load_all");
		if ($(`#${id}`).hasClass('ui-btn-clicked-layer-preview')) { // 미리보기 제거
			for (var i = 0; i < tree.length; i++) {
				if ($(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					var split = $(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class;
					var before = split.split(' ');
					var after = [];
					for (var j = 0; j < before.length; j++) {
						if (before[j].length != 'ui-btn-clicked-layer-preview') { after.push(before[j]); }
					}
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class = after.join(' ');
					$(`#${tree[i].id}`).removeClass('ui-btn-clicked-layer-preview');
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.removeLayerById('redTide-' + $(`#${tree[i].id}`).attr('value'));
					}

				}
			}
		} else { //미리보기 생성
			for (var i = 0; i < tree.length; i++) {
				if (!$(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					$(`#${id}`).jstree().open_node(tree[i]);
					$(`#${tree[i].id}`).addClass('ui-btn-clicked-layer-preview');
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class += ' ui-btn-clicked-layer-preview';
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.updateImageLayerTemp('redTide-' + $(`#${tree[i].id}`).attr('value'));
					}

				}
			}
		}
	}
});

// 미리보기 버튼 클릭 - 추출 영상 데이터
$('.js-search-result-area').on('click', '.ui-btn-preview-raster-anal-obj-layer', function(e) {
	console.log($(this).val());
	var id = $(this).data('tree'); // 현재 선택한 레벨의 노드 아이디
	var tree = $(`#${id}`).jstree().get_json('#' + id, { "flat": true });
	if (tree.length > 0) {
		//$(`#${id}`).jstree("load_all");
		if ($(`#${id}`).hasClass('ui-btn-clicked-layer-preview')) { // 미리보기 제거
			for (var i = 0; i < tree.length; i++) {
				if ($(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					var split = $(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class;
					var before = split.split(' ');
					var after = [];
					for (var j = 0; j < before.length; j++) {
						if (before[j].length != 'ui-btn-clicked-layer-preview') { after.push(before[j]); }
					}
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class = after.join(' ');
					$(`#${tree[i].id}`).removeClass('ui-btn-clicked-layer-preview');
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.removeLayerById('rasterAnalObj-' + $(`#${tree[i].id}`).attr('value'));
					}

				}
			}
		} else { //미리보기 생성
			for (var i = 0; i < tree.length; i++) {
				if (!$(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					$(`#${id}`).jstree().open_node(tree[i]);
					$(`#${tree[i].id}`).addClass('ui-btn-clicked-layer-preview');
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class += ' ui-btn-clicked-layer-preview';
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.updateImageLayerTemp('rasterAnalObj-' + $(`#${tree[i].id}`).attr('value'));
					}
				}

			}
		}
	}
});

// 미리보기 버튼 클릭 - 변화 영상 데이터
$('.js-search-result-area').on('click', '.ui-btn-preview-raster-anal-chg-layer', function(e) {
	console.log($(this).val());
	var id = $(this).data('tree'); // 현재 선택한 레벨의 노드 아이디
	var tree = $(`#${id}`).jstree().get_json('#' + id, { "flat": true });
	if (tree.length > 0) {
		//$(`#${id}`).jstree("load_all");
		if ($(`#${id}`).hasClass('ui-btn-clicked-layer-preview')) { // 미리보기 제거
			for (var i = 0; i < tree.length; i++) {
				if ($(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					var split = $(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class;
					var before = split.split(' ');
					var after = [];
					for (var j = 0; j < before.length; j++) {
						if (before[j].length != 'ui-btn-clicked-layer-preview') { after.push(before[j]); }
					}
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class = after.join(' ');
					$(`#${tree[i].id}`).removeClass('ui-btn-clicked-layer-preview');
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.removeLayerById('rasterAnalChg-' + $(`#${tree[i].id}`).attr('value'));
					}

				}
			}
		} else { //미리보기 생성
			for (var i = 0; i < tree.length; i++) {
				if (!$(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					$(`#${id}`).jstree().open_node(tree[i]);
					$(`#${tree[i].id}`).addClass('ui-btn-clicked-layer-preview');
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class += ' ui-btn-clicked-layer-preview';
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.updateImageLayerTemp('rasterAnalChg-' + $(`#${tree[i].id}`).attr('value'));
					}

				}
			}
		}
	}
});

// 미리보기 버튼 클릭 - 추출 벡터 데이터
$('.js-search-result-area').on('click', '.ui-btn-preview-shp-obj-layer', function(e) {
	console.log($(this).val());
	var id = $(this).data('tree'); // 현재 선택한 레벨의 노드 아이디
	var tree = $(`#${id}`).jstree().get_json('#' + id, { "flat": true });
	if (tree.length > 0) {
		//$(`#${id}`).jstree("load_all");
		if ($(`#${id}`).hasClass('ui-btn-clicked-layer-preview')) { // 미리보기 제거
			for (var i = 0; i < tree.length; i++) {
				if ($(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					var split = $(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class;
					var before = split.split(' ');
					var after = [];
					for (var j = 0; j < before.length; j++) {
						if (before[j].length != 'ui-btn-clicked-layer-preview') { after.push(before[j]); }
					}
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class = after.join(' ');
					$(`#${tree[i].id}`).removeClass('ui-btn-clicked-layer-preview');
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.removeLayerById('vectorAnalObj-' + $(`#${tree[i].id}`).attr('value'));
					}

				}
			}
		} else { //미리보기 생성
			for (var i = 0; i < tree.length; i++) {
				if (!$(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					$(`#${id}`).jstree().open_node(tree[i]);
					$(`#${tree[i].id}`).addClass('ui-btn-clicked-layer-preview');
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class += ' ui-btn-clicked-layer-preview';
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.updateImageLayerAnalysisRequest('vectorAnalObj-' + $(`#${tree[i].id}`).attr('value'));
					}

				}
			}
		}
	}
});

// 미리보기 버튼 클릭 - 변화 벡터 데이터
$('.js-search-result-area').on('click', '.ui-btn-preview-shp-chg-layer', function(e) {
	console.log($(this).val());
	var id = $(this).data('tree'); // 현재 선택한 레벨의 노드 아이디
	var tree = $(`#${id}`).jstree().get_json('#' + id, { "flat": true });
	if (tree.length > 0) {
		if ($(`#${id}`).hasClass('ui-btn-clicked-layer-preview')) { // 미리보기 제거
			for (var i = 0; i < tree.length; i++) {
				if ($(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					var split = $(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class;
					var before = split.split(' ');
					var after = [];
					for (var j = 0; j < before.length; j++) {
						if (before[j].length != 'ui-btn-clicked-layer-preview') { after.push(before[j]); }
					}
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class = after.join(' ');
					$(`#${tree[i].id}`).removeClass('ui-btn-clicked-layer-preview');
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.removeLayerById('vectorAnalChg-' + $(`#${tree[i].id}`).attr('value'));
					}

				}
			}
		} else { //미리보기 생성
			for (var i = 0; i < tree.length; i++) {
				if (!$(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					$(`#${id}`).jstree().open_node(tree[i]);
					$(`#${tree[i].id}`).addClass('ui-btn-clicked-layer-preview');
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class += ' ui-btn-clicked-layer-preview';
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.updateImageLayerAnalysisRequest('vectorAnalChg-' + $(`#${tree[i].id}`).attr('value'));
					}

				}
			}
		}
	}
});

// 미리보기 버튼 클릭 - 수치지도 데이터
$('.js-search-result-area').on('click', '.ui-btn-preview-digital-layer', function(e, data) {
	console.log($(this).val());
	var id = $(this).data('tree'); // 현재 선택한 레벨의 노드 아이디
	//var select_tree = $(`#${id}`).jstree();
	//select_tree.is_checked(id) ? select_tree.uncheck_node(id) : select_tree.check_node(id);
	var tree = $(`#${id}`).jstree().get_json('#' + id, { "flat": true });
	//var children = [];
	//tree.forEach( function(leaf){ if($(`#${id}`).jstree().is_leaf(leaf)){children.push(leaf);} }); // 현재 노드의 자식 노드만 추출
	if (tree.length > 0) {
		//$(`#${id}`).jstree("load_all");
		if ($(`#${id}`).hasClass('ui-btn-clicked-layer-preview')) { // 미리보기 제거
			for (var i = 0; i < tree.length; i++) {
				if ($(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					var split = $(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class;
					var before = split.split(' ');
					var after = [];
					for (var j = 0; j < before.length; j++) {
						if (before[j].length != 'ui-btn-clicked-layer-preview') { after.push(before[j]); }
					}
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class = after.join(' ');
					$(`#${tree[i].id}`).removeClass('ui-btn-clicked-layer-preview');
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.removeLayerById($(`#${tree[i].id}`).attr('value'));
					}

				}
			}
		} else { //미리보기 생성
			for (var i = 0; i < tree.length; i++) {
				if (!$(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					$(`#${id}`).jstree().open_node(tree[i]);
					$(`#${tree[i].id}`).addClass('ui-btn-clicked-layer-preview');
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class += ' ui-btn-clicked-layer-preview';
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.updateImageLayerFromGeoserver($(`#${tree[i].id}`).attr('value'));
					}
				}

			}
		}
	}
});

/*// 미리보기 버튼 클릭 - 인구통계 데이터
$('.js-search-result-area').on('click', '.ui-btn-preview-demo-layer', function(e) {
	console.log($(this).val());
	var id = $(this).data('tree');
	var select_tree = $(`#${id}`).jstree();
	select_tree.is_checked(id) ? select_tree.uncheck_node(id) : select_tree.check_node(id);
	if ($(this).hasClass('ui-btn-clicked-layer-preview')) {
		$(this).removeClass('ui-btn-clicked-layer-preview');
		CMSC003.GIS.removeLayerById($(this).val());
	} else {
		$(this).addClass('ui-btn-clicked-layer-preview');
		CMSC003.GIS.updateImageLayerFromGeoserverDemographic($(this).val());
	}

});

// 미리보기 버튼 클릭 - 지가통계 데이터
$('.js-search-result-area').on('click', '.ui-btn-preview-landg-layer', function(e) {
	console.log($(this).val());
	var id = $(this).data('tree');
	var select_tree = $(`#${id}`).jstree();
	select_tree.is_checked(id) ? select_tree.uncheck_node(id) : select_tree.check_node(id);
	if ($(this).hasClass('ui-btn-clicked-layer-preview')) {
		$(this).removeClass('ui-btn-clicked-layer-preview');
		CMSC003.GIS.removeLayerById($(this).val());
	} else {
		$(this).addClass('ui-btn-clicked-layer-preview');
		CMSC003.GIS.updateImageLayerFromGeoserverDemographic($(this).val());
	}

});

// 미리보기 버튼 클릭 - 건물통계 데이터
$('.js-search-result-area').on('click', '.ui-btn-preview-buld-layer', function(e) {
	console.log($(this).val());
	var id = $(this).data('tree');
	var select_tree = $(`#${id}`).jstree();
	select_tree.is_checked(id) ? select_tree.uncheck_node(id) : select_tree.check_node(id);
	if ($(this).hasClass('ui-btn-clicked-layer-preview')) {
		$(this).removeClass('ui-btn-clicked-layer-preview');
		CMSC003.GIS.removeLayerById($(this).val());
	} else {
		$(this).addClass('ui-btn-clicked-layer-preview');
		CMSC003.GIS.updateImageLayerFromGeoserverDemographic($(this).val());
	}
});
*/
// 미리보기 버튼 클릭 - 통계
/*$('.js-search-result-area').on('click', '.ui-btn-preview-graphics-layer', function(e) {
	console.log($(this).val());
	var id = $(this).data('tree');
	if ($(`#${id}`).hasClass('ui-btn-clicked-layer-preview')) {
		$(`#${id}`).removeClass('ui-btn-clicked-layer-preview');
		$(`#${id}`).jstree()._model.data[id].li_attr.class += ' ui-btn-clicked-layer-preview';
		CMSC003.GIS.removeLayerById($(this).val());
	} else {
		//$(this).addClass('ui-btn-clicked-layer-preview');
		$(`#${id}`).addClass('ui-btn-clicked-layer-preview');
		$(`#${id}`).jstree()._model.data[id].li_attr.class += ' ui-btn-clicked-layer-preview';
		CMSC003.GIS.updateImageLayerFromGeoserverDemographic($(this).val());
	}

});*/
// 미리보기 버튼 클릭 - 통계 데이터
$('.js-search-result-area').on('click', '.ui-btn-preview-graphics-layer', function(e) {
	console.log($(this).val());
	var id = $(this).data('tree'); // 현재 선택한 레벨의 노드 아이디
	var tree = $(`#${id}`).jstree().get_json('#' + id, { "flat": true });
	if (tree.length > 0) {
		if ($(`#${id}`).hasClass('ui-btn-clicked-layer-preview')) { // 미리보기 제거
			for (var i = 0; i < tree.length; i++) {
				if ($(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					var split = $(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class;
					var before = split.split(' ');
					var after = [];
					for (var j = 0; j < before.length; j++) {
						if (before[j].length != 'ui-btn-clicked-layer-preview') { after.push(before[j]); }
					}
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class = after.join(' ');
					$(`#${tree[i].id}`).removeClass('ui-btn-clicked-layer-preview');
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.removeLayerById($(`#${tree[i].id}`).attr('value'));
					}

				}
			}
		} else { //미리보기 생성
			for (var i = 0; i < tree.length; i++) {
				if (!$(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					$(`#${id}`).jstree().open_node(tree[i]);
					$(`#${tree[i].id}`).addClass('ui-btn-clicked-layer-preview');
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class += ' ui-btn-clicked-layer-preview';
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.updateImageLayerFromGeoserverDemographic($(`#${tree[i].id}`).attr('value'));
					}

				}
			}
		}
	}
});

//============================================================================================================
// 요청 미리보기 버튼 클릭 - 영상 데이터
$('.js-request-result-area').on('click', '.ui-btn-preview-ortho-image-layer', function(e) {
	console.log($(this).val());
	var id = $(this).data('tree'); // 현재 선택한 레벨의 노드 아이디
	var tree = $(`#${id}`).jstree().get_json('#' + id, { "flat": true });
	if (tree.length > 0) {
		//$(`#${id}`).jstree("load_all");
		if ($(`#${id}`).hasClass('ui-btn-clicked-layer-preview')) { // 미리보기 제거
			for (var i = 0; i < tree.length; i++) {
				if ($(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					var split = $(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class;
					var before = split.split(' ');
					var after = [];
					for (var j = 0; j < before.length; j++) {
						if (before[j].length != 'ui-btn-clicked-layer-preview') { after.push(before[j]); }
					}
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class = after.join(' ');
					$(`#${tree[i].id}`).removeClass('ui-btn-clicked-layer-preview');
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.removeLayerById('ortho-' + $(`#${tree[i].id}`).attr('value'), true);
					}

				}
			}
		} else { //미리보기 생성
			for (var i = 0; i < tree.length; i++) {
				if (!$(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					$(`#${id}`).jstree().open_node(tree[i]);
					$(`#${tree[i].id}`).addClass('ui-btn-clicked-layer-preview');
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class += ' ui-btn-clicked-layer-preview';
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.updateImageLayerTemp('ortho-' + $(`#${tree[i].id}`).attr('value'), true);
					}

				}
			}
		}
	}
});

// 요청 미리보기 버튼 클릭 - 항공영상 데이터
$('.js-request-result-area').on('click', '.ui-btn-preview-aerial-image-layer', function(e) {
	console.log($(this).val());
	var id = $(this).data('tree'); // 현재 선택한 레벨의 노드 아이디
	var tree = $(`#${id}`).jstree().get_json('#' + id, { "flat": true });
	if (tree.length > 0) {
		//$(`#${id}`).jstree("load_all");
		if ($(`#${id}`).hasClass('ui-btn-clicked-layer-preview')) { // 미리보기 제거
			for (var i = 0; i < tree.length; i++) {
				if ($(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					var split = $(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class;
					var before = split.split(' ');
					var after = [];
					for (var j = 0; j < before.length; j++) {
						if (before[j].length != 'ui-btn-clicked-layer-preview') { after.push(before[j]); }
					}
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class = after.join(' ');
					$(`#${tree[i].id}`).removeClass('ui-btn-clicked-layer-preview');
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.removeLayerById('aerial-' + $(`#${tree[i].id}`).attr('value'), true);
					}

				}
			}
		} else { //미리보기 생성
			for (var i = 0; i < tree.length; i++) {
				if (!$(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					$(`#${id}`).jstree().open_node(tree[i]);
					$(`#${tree[i].id}`).addClass('ui-btn-clicked-layer-preview');
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class += ' ui-btn-clicked-layer-preview';
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.updateImageLayerTemp('aerial-' + $(`#${tree[i].id}`).attr('value'), true);
					}

				}
			}
		}
	}
});

// 요청 미리보기 버튼 클릭 - dem 데이터
$('.js-request-result-area').on('click', '.ui-btn-preview-dem-layer', function(e) {
	console.log($(this).val());
	var id = $(this).data('tree'); // 현재 선택한 레벨의 노드 아이디
	var tree = $(`#${id}`).jstree().get_json('#' + id, { "flat": true });
	if (tree.length > 0) {
		//$(`#${id}`).jstree("load_all");
		if ($(`#${id}`).hasClass('ui-btn-clicked-layer-preview')) { // 미리보기 제거
			for (var i = 0; i < tree.length; i++) {
				if ($(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					var split = $(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class;
					var before = split.split(' ');
					var after = [];
					for (var j = 0; j < before.length; j++) {
						if (before[j].length != 'ui-btn-clicked-layer-preview') { after.push(before[j]); }
					}
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class = after.join(' ');
					$(`#${tree[i].id}`).removeClass('ui-btn-clicked-layer-preview');
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.removeLayerById('dem-' + $(`#${tree[i].id}`).attr('value'), true);
					}

				}
			}
		} else { //미리보기 생성
			for (var i = 0; i < tree.length; i++) {
				if (!$(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					$(`#${id}`).jstree().open_node(tree[i]);
					$(`#${tree[i].id}`).addClass('ui-btn-clicked-layer-preview');
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class += ' ui-btn-clicked-layer-preview';
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.updateImageLayerTemp('dem-' + $(`#${tree[i].id}`).attr('value'), true);
					}
				}

			}
		}
	}
});

// 요청 미리보기 버튼 클릭 - kompsat
$('.js-request-result-area').on('click', '.ui-btn-preview-kompsat-image-layer', function(e) {
	console.log($(this).val());
	var id = $(this).data('tree'); // 현재 선택한 레벨의 노드 아이디
	var tree = $(`#${id}`).jstree().get_json('#' + id, { "flat": true });
	if (tree.length > 0) {
		//$(`#${id}`).jstree("load_all");
		if ($(`#${id}`).hasClass('ui-btn-clicked-layer-preview')) { // 미리보기 제거
			for (var i = 0; i < tree.length; i++) {
				if ($(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					var split = $(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class;
					var before = split.split(' ');
					var after = [];
					for (var j = 0; j < before.length; j++) {
						if (before[j].length != 'ui-btn-clicked-layer-preview') { after.push(before[j]); }
					}
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class = after.join(' ');
					$(`#${tree[i].id}`).removeClass('ui-btn-clicked-layer-preview');
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.removeLayerById($(`#${tree[i].id}`).attr('value'), true);
					}
				}
			}
		} else { //미리보기 생성
			for (var i = 0; i < tree.length; i++) {
				if (!$(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					$(`#${id}`).jstree().open_node(tree[i]);
					$(`#${tree[i].id}`).addClass('ui-btn-clicked-layer-preview');
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class += ' ui-btn-clicked-layer-preview';
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.updateImageLayerTemp($(`#${tree[i].id}`).attr('value'), true);
					}

				}
			}
		}
	}
});

// 요청 미리보기 버튼 클릭 - satellite
$('.js-request-result-area').on('click', '.ui-btn-preview-satellite-image-layer', function(e) {
	console.log($(this).val());
	var id = $(this).data('tree'); // 현재 선택한 레벨의 노드 아이디
	var tree = $(`#${id}`).jstree().get_json('#' + id, { "flat": true });
	if (tree.length > 0) {
		if ($(`#${id}`).hasClass('ui-btn-clicked-layer-preview')) { // 미리보기 제거
			for (var i = 0; i < tree.length; i++) {
				if ($(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					var split = $(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class;
					var before = split.split(' ');
					var after = [];
					for (var j = 0; j < before.length; j++) {
						if (before[j].length != 'ui-btn-clicked-layer-preview') { after.push(before[j]); }
					}
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class = after.join(' ');
					$(`#${tree[i].id}`).removeClass('ui-btn-clicked-layer-preview');
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.removeLayerById(tree[i].id.split('-')[2] + '-' + $(`#${tree[i].id}`).attr('value'), true);
					}

				}
			}
		} else { //미리보기 생성
			for (var i = 0; i < tree.length; i++) {
				if (!$(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					$(`#${id}`).jstree().open_node(tree[i]);
					$(`#${tree[i].id}`).addClass('ui-btn-clicked-layer-preview');
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class += ' ui-btn-clicked-layer-preview';
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.updateImageLayerTemp(tree[i].id.split('-')[2] + '-' + $(`#${tree[i].id}`).attr('value'), true);
					}
				}
			}
		}
	}
});

// 요청 미리보기 버튼 클릭 - landsat
$('.js-request-result-area').on('click', '.ui-btn-preview-landsat-image-layer', function(e) {
	console.log($(this).val());
	var id = $(this).data('tree'); // 현재 선택한 레벨의 노드 아이디
	var tree = $(`#${id}`).jstree().get_json('#' + id, { "flat": true });
	if (tree.length > 0) {
		//$(`#${id}`).jstree("load_all");
		if ($(`#${id}`).hasClass('ui-btn-clicked-layer-preview')) { // 미리보기 제거
			for (var i = 0; i < tree.length; i++) {
				if ($(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					var split = $(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class;
					var before = split.split(' ');
					var after = [];
					for (var j = 0; j < before.length; j++) {
						if (before[j].length != 'ui-btn-clicked-layer-preview') { after.push(before[j]); }
					}
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class = after.join(' ');
					$(`#${tree[i].id}`).removeClass('ui-btn-clicked-layer-preview');
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.removeLayerById($(`#${tree[i].id}`).attr('value'), true);
					}

				}
			}
		} else { //미리보기 생성
			for (var i = 0; i < tree.length; i++) {
				if (!$(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					$(`#${id}`).jstree().open_node(tree[i]);
					$(`#${tree[i].id}`).addClass('ui-btn-clicked-layer-preview');
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class += ' ui-btn-clicked-layer-preview';
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.updateImageLayerTemp($(`#${tree[i].id}`).attr('value'), true);
					}

				}
			}
		}
	}
});

// 요청 미리보기 버튼 클릭 - sentinel
$('.js-request-result-area').on('click', '.ui-btn-preview-sentinel-image-layer', function(e) {
	console.log($(this).val());
	var id = $(this).data('tree'); // 현재 선택한 레벨의 노드 아이디
	var tree = $(`#${id}`).jstree().get_json('#' + id, { "flat": true });
	if (tree.length > 0) {
		//$(`#${id}`).jstree("load_all");
		if ($(`#${id}`).hasClass('ui-btn-clicked-layer-preview')) { // 미리보기 제거
			for (var i = 0; i < tree.length; i++) {
				if ($(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					var split = $(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class;
					var before = split.split(' ');
					var after = [];
					for (var j = 0; j < before.length; j++) {
						if (before[j].length != 'ui-btn-clicked-layer-preview') { after.push(before[j]); }
					}
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class = after.join(' ');
					$(`#${tree[i].id}`).removeClass('ui-btn-clicked-layer-preview');
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.removeLayerById($(`#${tree[i].id}`).attr('value'), true);
					}

				}
			}
		} else { //미리보기 생성
			for (var i = 0; i < tree.length; i++) {
				if (!$(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					$(`#${tree[i].id}`).addClass('ui-btn-clicked-layer-preview');
					$(`#${id}`).jstree().open_node(tree[i]);
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class += ' ui-btn-clicked-layer-preview';
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.updateImageLayerTemp($(`#${tree[i].id}`).attr('value'), true);
					}

				}
			}
		}
	}
});


//요청 미리보기 버튼 클릭 - sar  :: 긴급공간정보 08.28
$('.js-request-result-area').on('click', '.ui-btn-preview-sar-image-layer', function(e) {
	console.log($(this).val());
	var id = $(this).data('tree'); // 현재 선택한 레벨의 노드 아이디
	var tree = $(`#${id}`).jstree().get_json('#' + id, { "flat": true });
	if (tree.length > 0) {
		//$(`#${id}`).jstree("load_all");
		if ($(`#${id}`).hasClass('ui-btn-clicked-layer-preview')) { // 미리보기 제거
			for (var i = 0; i < tree.length; i++) {
				if ($(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					var split = $(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class;
					var before = split.split(' ');
					var after = [];
					for (var j = 0; j < before.length; j++) {
						if (before[j].length != 'ui-btn-clicked-layer-preview') { after.push(before[j]); }
					}
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class = after.join(' ');
					$(`#${tree[i].id}`).removeClass('ui-btn-clicked-layer-preview');
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.removeLayerById($(`#${tree[i].id}`).attr('value'), true);
					}

				}
			}
		} else { //미리보기 생성
			for (var i = 0; i < tree.length; i++) {
				if (!$(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					$(`#${id}`).jstree().open_node(tree[i]);
					$(`#${tree[i].id}`).addClass('ui-btn-clicked-layer-preview');
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class += ' ui-btn-clicked-layer-preview';
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.updateImageLayerTemp($(`#${tree[i].id}`).attr('value'), true);
					}

				}
			}
		}
	}
});



// 요청 미리보기 버튼 클릭 - 홍수 데이터
$('.js-request-result-area').on('click', '.ui-btn-preview-flood-image-layer', function(e) {
	console.log($(this).val());
	var id = $(this).data('tree'); // 현재 선택한 레벨의 노드 아이디
	var tree = $(`#${id}`).jstree().get_json('#' + id, { "flat": true });
	if (tree.length > 0) {
		//$(`#${id}`).jstree("load_all");
		if ($(`#${id}`).hasClass('ui-btn-clicked-layer-preview')) { // 미리보기 제거
			for (var i = 0; i < tree.length; i++) {
				if ($(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					var split = $(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class;
					var before = split.split(' ');
					var after = [];
					for (var j = 0; j < before.length; j++) {
						if (before[j].length != 'ui-btn-clicked-layer-preview') { after.push(before[j]); }
					}
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class = after.join(' ');
					$(`#${tree[i].id}`).removeClass('ui-btn-clicked-layer-preview');
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.removeLayerById('flood-' + $(`#${tree[i].id}`).attr('value'), true);
					}

				}
			}
		} else { //미리보기 생성
			for (var i = 0; i < tree.length; i++) {
				if (!$(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					$(`#${id}`).jstree().open_node(tree[i]);
					$(`#${tree[i].id}`).addClass('ui-btn-clicked-layer-preview');
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class += ' ui-btn-clicked-layer-preview';
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.updateImageLayerTemp('flood-' + $(`#${tree[i].id}`).attr('value'), true);
					}

				}
			}
		}
	}
});

// 요청 미리보기 버튼 클릭 - 산불 데이터
$('.js-request-result-area').on('click', '.ui-btn-preview-forestfire-image-layer', function(e) {
	console.log($(this).val());
	var id = $(this).data('tree'); // 현재 선택한 레벨의 노드 아이디
	var tree = $(`#${id}`).jstree().get_json('#' + id, { "flat": true });
	if (tree.length > 0) {
		//$(`#${id}`).jstree("load_all");
		if ($(`#${id}`).hasClass('ui-btn-clicked-layer-preview')) { // 미리보기 제거
			for (var i = 0; i < tree.length; i++) {
				if ($(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					var split = $(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class;
					var before = split.split(' ');
					var after = [];
					for (var j = 0; j < before.length; j++) {
						if (before[j].length != 'ui-btn-clicked-layer-preview') { after.push(before[j]); }
					}
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class = after.join(' ');
					$(`#${tree[i].id}`).removeClass('ui-btn-clicked-layer-preview');
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.removeLayerById('forestFire-' + $(`#${tree[i].id}`).attr('value'), true);
					}

				}
			}
		} else { //미리보기 생성
			for (var i = 0; i < tree.length; i++) {
				if (!$(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					$(`#${id}`).jstree().open_node(tree[i]);
					$(`#${tree[i].id}`).addClass('ui-btn-clicked-layer-preview');
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class += ' ui-btn-clicked-layer-preview';
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.updateImageLayerTemp('forestFire-' + $(`#${tree[i].id}`).attr('value'), true);
					}

				}
			}
		}
	}
});

// 요청 미리보기 버튼 클릭 - 지진 데이터
$('.js-request-result-area').on('click', '.ui-btn-preview-earthquake-image-layer', function(e) {
	console.log($(this).val());
	var id = $(this).data('tree'); // 현재 선택한 레벨의 노드 아이디
	var tree = $(`#${id}`).jstree().get_json('#' + id, { "flat": true });
	if (tree.length > 0) {
		//$(`#${id}`).jstree("load_all");
		if ($(`#${id}`).hasClass('ui-btn-clicked-layer-preview')) { // 미리보기 제거
			for (var i = 0; i < tree.length; i++) {
				if ($(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					var split = $(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class;
					var before = split.split(' ');
					var after = [];
					for (var j = 0; j < before.length; j++) {
						if (before[j].length != 'ui-btn-clicked-layer-preview') { after.push(before[j]); }
					}
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class = after.join(' ');
					$(`#${tree[i].id}`).removeClass('ui-btn-clicked-layer-preview');
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.removeLayerById('earthquake-' + $(`#${tree[i].id}`).attr('value'), true);
					}

				}
			}
		} else { //미리보기 생성
			for (var i = 0; i < tree.length; i++) {
				if (!$(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					$(`#${id}`).jstree().open_node(tree[i]);
					$(`#${tree[i].id}`).addClass('ui-btn-clicked-layer-preview');
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class += ' ui-btn-clicked-layer-preview';
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.updateImageLayerTemp('earthquake-' + $(`#${tree[i].id}`).attr('value'), true);
					}

				}
			}
		}
	}
});

// 요청 미리보기 버튼 클릭 - 산사태 데이터
$('.js-request-result-area').on('click', '.ui-btn-preview-landslide-image-layer', function(e) {
	console.log($(this).val());
	var id = $(this).data('tree'); // 현재 선택한 레벨의 노드 아이디
	var tree = $(`#${id}`).jstree().get_json('#' + id, { "flat": true });
	if (tree.length > 0) {
		//$(`#${id}`).jstree("load_all");
		if ($(`#${id}`).hasClass('ui-btn-clicked-layer-preview')) { // 미리보기 제거
			for (var i = 0; i < tree.length; i++) {
				if ($(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					var split = $(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class;
					var before = split.split(' ');
					var after = [];
					for (var j = 0; j < before.length; j++) {
						if (before[j].length != 'ui-btn-clicked-layer-preview') { after.push(before[j]); }
					}
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class = after.join(' ');
					$(`#${tree[i].id}`).removeClass('ui-btn-clicked-layer-preview');
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.removeLayerById('landslide-' + $(`#${tree[i].id}`).attr('value'), true);
					}

				}
			}
		} else { //미리보기 생성
			for (var i = 0; i < tree.length; i++) {
				if (!$(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					$(`#${id}`).jstree().open_node(tree[i]);
					$(`#${tree[i].id}`).addClass('ui-btn-clicked-layer-preview');
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class += ' ui-btn-clicked-layer-preview';
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.updateImageLayerTemp('landslide-' + $(`#${tree[i].id}`).attr('value'), true);
					}

				}
			}
		}
	}
});

// 요청 미리보기 버튼 클릭 - 해양 재난 데이터
$('.js-request-result-area').on('click', '.ui-btn-preview-maritime-image-layer', function(e) {
	console.log($(this).val());
	var id = $(this).data('tree'); // 현재 선택한 레벨의 노드 아이디
	var tree = $(`#${id}`).jstree().get_json('#' + id, { "flat": true });
	if (tree.length > 0) {
		//$(`#${id}`).jstree("load_all");
		if ($(`#${id}`).hasClass('ui-btn-clicked-layer-preview')) { // 미리보기 제거
			for (var i = 0; i < tree.length; i++) {
				if ($(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					var split = $(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class;
					var before = split.split(' ');
					var after = [];
					for (var j = 0; j < before.length; j++) {
						if (before[j].length != 'ui-btn-clicked-layer-preview') { after.push(before[j]); }
					}
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class = after.join(' ');
					$(`#${tree[i].id}`).removeClass('ui-btn-clicked-layer-preview');
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.removeLayerById('maritime-' + $(`#${tree[i].id}`).attr('value'), true);
					}

				}
			}
		} else { //미리보기 생성
			for (var i = 0; i < tree.length; i++) {
				if (!$(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					$(`#${id}`).jstree().open_node(tree[i]);
					$(`#${tree[i].id}`).addClass('ui-btn-clicked-layer-preview');
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class += ' ui-btn-clicked-layer-preview';
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.updateImageLayerTemp('maritime-' + $(`#${tree[i].id}`).attr('value'), true);
					}

				}
			}
		}
	}
});

// 요청 미리보기 버튼 클릭 - 적조 데이터
$('.js-request-result-area').on('click', '.ui-btn-preview-redTide-image-layer', function(e) {
	console.log($(this).val());
	var id = $(this).data('tree'); // 현재 선택한 레벨의 노드 아이디
	var tree = $(`#${id}`).jstree().get_json('#' + id, { "flat": true });
	if (tree.length > 0) {
		//$(`#${id}`).jstree("load_all");
		if ($(`#${id}`).hasClass('ui-btn-clicked-layer-preview')) { // 미리보기 제거
			for (var i = 0; i < tree.length; i++) {
				if ($(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					var split = $(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class;
					var before = split.split(' ');
					var after = [];
					for (var j = 0; j < before.length; j++) {
						if (before[j].length != 'ui-btn-clicked-layer-preview') { after.push(before[j]); }
					}
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class = after.join(' ');
					$(`#${tree[i].id}`).removeClass('ui-btn-clicked-layer-preview');
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.removeLayerById('redTide-' + $(`#${tree[i].id}`).attr('value'), true);
					}

				}
			}
		} else { //미리보기 생성
			for (var i = 0; i < tree.length; i++) {
				if (!$(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					$(`#${id}`).jstree().open_node(tree[i]);
					$(`#${tree[i].id}`).addClass('ui-btn-clicked-layer-preview');
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class += ' ui-btn-clicked-layer-preview';
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.updateImageLayerTemp('redTide-' + $(`#${tree[i].id}`).attr('value'), true);
					}

				}
			}
		}
	}
});

// 요청 미리보기 버튼 클릭 - 추출 영상 데이터
$('.js-request-result-area').on('click', '.ui-btn-preview-raster-anal-obj-layer', function(e) {
	console.log($(this).val());
	var id = $(this).data('tree'); // 현재 선택한 레벨의 노드 아이디
	var tree = $(`#${id}`).jstree().get_json('#' + id, { "flat": true });
	if (tree.length > 0) {
		//$(`#${id}`).jstree("load_all");
		if ($(`#${id}`).hasClass('ui-btn-clicked-layer-preview')) { // 미리보기 제거
			for (var i = 0; i < tree.length; i++) {
				if ($(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					var split = $(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class;
					var before = split.split(' ');
					var after = [];
					for (var j = 0; j < before.length; j++) {
						if (before[j].length != 'ui-btn-clicked-layer-preview') { after.push(before[j]); }
					}
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class = after.join(' ');
					$(`#${tree[i].id}`).removeClass('ui-btn-clicked-layer-preview');
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.removeLayerById('rasterAnalObj-' + $(`#${tree[i].id}`).attr('value'), true);
					}

				}
			}
		} else { //미리보기 생성
			for (var i = 0; i < tree.length; i++) {
				if (!$(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					$(`#${id}`).jstree().open_node(tree[i]);
					$(`#${tree[i].id}`).addClass('ui-btn-clicked-layer-preview');
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class += ' ui-btn-clicked-layer-preview';
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.updateImageLayerTemp('rasterAnalObj-' + $(`#${tree[i].id}`).attr('value'), true);
					}

				}
			}
		}
	}
});

// 요청 미리보기 버튼 클릭 - 변화 영상 데이터
$('.js-request-result-area').on('click', '.ui-btn-preview-raster-anal-chg-layer', function(e) {
	console.log($(this).val());
	var id = $(this).data('tree'); // 현재 선택한 레벨의 노드 아이디
	var tree = $(`#${id}`).jstree().get_json('#' + id, { "flat": true });
	if (tree.length > 0) {
		//$(`#${id}`).jstree("load_all");
		if ($(`#${id}`).hasClass('ui-btn-clicked-layer-preview')) { // 미리보기 제거
			for (var i = 0; i < tree.length; i++) {
				if ($(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					var split = $(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class;
					var before = split.split(' ');
					var after = [];
					for (var j = 0; j < before.length; j++) {
						if (before[j].length != 'ui-btn-clicked-layer-preview') { after.push(before[j]); }
					}
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class = after.join(' ');
					$(`#${tree[i].id}`).removeClass('ui-btn-clicked-layer-preview');
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.removeLayerById('rasterAnalChg-' + $(`#${tree[i].id}`).attr('value'), true);
					}

				}
			}
		} else { //미리보기 생성
			for (var i = 0; i < tree.length; i++) {
				if (!$(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					$(`#${id}`).jstree().open_node(tree[i]);
					$(`#${tree[i].id}`).addClass('ui-btn-clicked-layer-preview');
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class += ' ui-btn-clicked-layer-preview';
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.updateImageLayerTemp('rasterAnalChg-' + $(`#${tree[i].id}`).attr('value'), true);
					}

				}
			}
		}
	}
});

// 요청 미리보기 버튼 클릭 - 추출 벡터 데이터
$('.js-request-result-area').on('click', '.ui-btn-preview-shp-obj-layer', function(e) {
	console.log($(this).val());
	var id = $(this).data('tree'); // 현재 선택한 레벨의 노드 아이디
	var tree = $(`#${id}`).jstree().get_json('#' + id, { "flat": true });
	if (tree.length > 0) {
		//$(`#${id}`).jstree("load_all");
		if ($(`#${id}`).hasClass('ui-btn-clicked-layer-preview')) { // 미리보기 제거
			for (var i = 0; i < tree.length; i++) {
				if ($(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					var split = $(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class;
					var before = split.split(' ');
					var after = [];
					for (var j = 0; j < before.length; j++) {
						if (before[j].length != 'ui-btn-clicked-layer-preview') { after.push(before[j]); }
					}
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class = after.join(' ');
					$(`#${tree[i].id}`).removeClass('ui-btn-clicked-layer-preview');
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.removeLayerById('vectorAnalObj-' + $(`#${tree[i].id}`).attr('value'), true);
					}

				}
			}
		} else { //미리보기 생성
			for (var i = 0; i < tree.length; i++) {
				if (!$(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					$(`#${id}`).jstree().open_node(tree[i]);
					$(`#${tree[i].id}`).addClass('ui-btn-clicked-layer-preview');
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class += ' ui-btn-clicked-layer-preview';
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.updateImageLayerAnalysisResult('vectorAnalObj-' + $(`#${tree[i].id}`).attr('value'), true);
						//CMSC003.GIS.updateImageLayerAnalysisRequest('vectorAnalObj-' + $(`#${tree[i].id}`).attr('value'), true);
					}

				}
			}
		}
	}
});

// 요청 미리보기 버튼 클릭 - 변화 벡터 데이터
$('.js-request-result-area').on('click', '.ui-btn-preview-shp-chg-layer', function(e) {
	console.log($(this).val());
	var id = $(this).data('tree'); // 현재 선택한 레벨의 노드 아이디
	var tree = $(`#${id}`).jstree().get_json('#' + id, { "flat": true });
	if (tree.length > 0) {
		if ($(`#${id}`).hasClass('ui-btn-clicked-layer-preview')) { // 미리보기 제거
			for (var i = 0; i < tree.length; i++) {
				if ($(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					var split = $(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class;
					var before = split.split(' ');
					var after = [];
					for (var j = 0; j < before.length; j++) {
						if (before[j].length != 'ui-btn-clicked-layer-preview') { after.push(before[j]); }
					}
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class = after.join(' ');
					$(`#${tree[i].id}`).removeClass('ui-btn-clicked-layer-preview');
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.removeLayerById('vectorAnalChg-' + $(`#${tree[i].id}`).attr('value'), true);
					}

				}
			}
		} else { //미리보기 생성
			for (var i = 0; i < tree.length; i++) {
				if (!$(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					$(`#${id}`).jstree().open_node(tree[i]);
					$(`#${tree[i].id}`).addClass('ui-btn-clicked-layer-preview');
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class += ' ui-btn-clicked-layer-preview';
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.updateImageLayerAnalysisResult('vectorAnalChg-' + $(`#${tree[i].id}`).attr('value'), true);
					}

				}
			}
		}
	}
});

// 요청 미리보기 버튼 클릭 - 수치지도 데이터
$('.js-request-result-area').on('click', '.ui-btn-preview-digital-layer', function(e, data) {
	console.log($(this).val());
	var id = $(this).data('tree'); // 현재 선택한 레벨의 노드 아이디
	//var select_tree = $(`#${id}`).jstree();
	//select_tree.is_checked(id) ? select_tree.uncheck_node(id) : select_tree.check_node(id);
	var tree = $(`#${id}`).jstree().get_json('#' + id, { "flat": true });
	//var children = [];
	//tree.forEach( function(leaf){ if($(`#${id}`).jstree().is_leaf(leaf)){children.push(leaf);} }); // 현재 노드의 자식 노드만 추출
	if (tree.length > 0) {
		//$(`#${id}`).jstree("load_all");
		if ($(`#${id}`).hasClass('ui-btn-clicked-layer-preview')) { // 미리보기 제거
			for (var i = 0; i < tree.length; i++) {
				if ($(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					var split = $(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class;
					var before = split.split(' ');
					var after = [];
					for (var j = 0; j < before.length; j++) {
						if (before[j].length != 'ui-btn-clicked-layer-preview') { after.push(before[j]); }
					}
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class = after.join(' ');
					$(`#${tree[i].id}`).removeClass('ui-btn-clicked-layer-preview');
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.hideLayerById($(`#${tree[i].id}`).attr("value"), true);
					}

				}
			}
		} else { //미리보기 생성
			for (var i = 0; i < tree.length; i++) {
				if (!$(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					$(`#${id}`).jstree().open_node(tree[i]);
					$(`#${tree[i].id}`).addClass('ui-btn-clicked-layer-preview');
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class += ' ui-btn-clicked-layer-preview';
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.updateImageLayerAnalysisResult($(`#${tree[i].id}`).attr("value"), true);
						// CMSC003.GIS.updateImageLayerFromGeoserver($(`#${tree[i].id}`).attr('value'), true);
					}

				}
			}
		}
	}
});
// 미리보기 버튼 클릭 - 인구통계 데이터
/*$('.js-request-result-area').on('click', '.ui-btn-preview-demo-layer', function(e) {
	console.log($(this).val());
	if ($(this).hasClass('ui-btn-clicked-layer-preview')) {
		$(this).removeClass('ui-btn-clicked-layer-preview');
		CMSC003.GIS.hideLayerById($(this).val(), true);
	} else {
		$(this).addClass('ui-btn-clicked-layer-preview');
//		CMSC003.DOM.getSHPBinaryGeoServer($(this).val(), true);
//		CMSC003.DOM.getSHPBinaryGeoServerAfterRequest($(this).val(), true);
		CMSC003.GIS.updateImageLayerAnalysisResult( $(this).val(), true);
	}
	//	CMSC003.GIS.updateImageLayerFromGeoserverDemographic($(this).val(), true);

});

// 미리보기 버튼 클릭 - 지가통계 데이터
$('.js-request-result-area').on('click', '.ui-btn-preview-landg-layer', function(e) {
	console.log($(this).val());
	if ($(this).hasClass('ui-btn-clicked-layer-preview')) {
		$(this).removeClass('ui-btn-clicked-layer-preview');
		CMSC003.GIS.hideLayerById($(this).val(), true);
	} else {
		$(this).addClass('ui-btn-clicked-layer-preview');
//		CMSC003.DOM.getSHPBinaryGeoServerAfterRequest($(this).val(), true);
		CMSC003.GIS.updateImageLayerAnalysisResult( $(this).val(), true);
	}
});

// 미리보기 버튼 클릭 - 건물통계 데이터
$('.js-request-result-area').on('click', '.ui-btn-preview-buld-layer', function(e) {
	console.log($(this).val());
	if ($(this).hasClass('ui-btn-clicked-layer-preview')) {
		$(this).removeClass('ui-btn-clicked-layer-preview');
		CMSC003.GIS.hideLayerById($(this).val(), true);
	} else {
		$(this).addClass('ui-btn-clicked-layer-preview');
//		CMSC003.DOM.getSHPBinaryGeoServerAfterRequest($(this).val(), true);
		CMSC003.GIS.updateImageLayerAnalysisResult( $(this).val(), true);
	}
});
*/

// 요청 미리보기 버튼 클릭 - 통계 데이터
/*$('.js-request-result-area').on('click', '.ui-btn-preview-graphics-layer', function(e) {
	console.log($(this).val());
	var id = $(this).data('tree');
	if ($(`#${id}`).hasClass('ui-btn-clicked-layer-preview')) {
		$(`#${id}`).removeClass('ui-btn-clicked-layer-preview');
		$(`#${id}`).jstree()._model.data[id].li_attr.class += ' ui-btn-clicked-layer-preview';
		CMSC003.GIS.hideLayerById($(this).val(), true);
	} else {
		//$(this).addClass('ui-btn-clicked-layer-preview');
		$(`#${id}`).addClass('ui-btn-clicked-layer-preview');
		$(`#${id}`).jstree()._model.data[id].li_attr.class += ' ui-btn-clicked-layer-preview';
		CMSC003.GIS.updateImageLayerAnalysisResult($(this).val(), true);
	}

});*/
// 미리보기 버튼 클릭 - 통계 데이터
$('.js-request-result-area').on('click', '.ui-btn-preview-graphics-layer', function(e) {
	console.log($(this).val());
	var id = $(this).data('tree'); // 현재 선택한 레벨의 노드 아이디
	var tree = $(`#${id}`).jstree().get_json('#' + id, { "flat": true });
	if (tree.length > 0) {
		if ($(`#${id}`).hasClass('ui-btn-clicked-layer-preview')) { // 미리보기 제거
			for (var i = 0; i < tree.length; i++) {
				if ($(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					var split = $(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class;
					var before = split.split(' ');
					var after = [];
					for (var j = 0; j < before.length; j++) {
						if (before[j].length != 'ui-btn-clicked-layer-preview') { after.push(before[j]); }
					}
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class = after.join(' ');
					$(`#${tree[i].id}`).removeClass('ui-btn-clicked-layer-preview');
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.hideLayerById(tree[i].li_attr.value, true);
					}

				}
			}
		} else { //미리보기 생성
			for (var i = 0; i < tree.length; i++) {
				if (!$(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					$(`#${id}`).jstree().open_node(tree[i]);
					$(`#${tree[i].id}`).addClass('ui-btn-clicked-layer-preview');
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class += ' ui-btn-clicked-layer-preview';
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						// CMSC003.GIS.updateImageLayerFromGeoserverDemographic($(`#${tree[i].id}`).val(), true);
						//CMSC003.GIS.updateImageLayerFromGeoserverDemographic(tree[i].li_attr.value, true);
						CMSC003.GIS.updateImageLayerAnalysisResult($(`#${tree[i].id}`).attr("value"), true);
						
					}

				}
			}
		}
	}
});

// 미리보기 버튼 클릭 - 위성영상 cas
$('.js-request-result-area').on('click', '.ui-btn-preview-cas-image-layer', function(e) {
	console.log($(this).val());
	var id = $(this).data('tree'); // 현재 선택한 레벨의 노드 아이디
	var tree = $(`#${id}`).jstree().get_json('#' + id, { "flat": true });
	if (tree.length > 0) {
		//$(`#${id}`).jstree("load_all");
		if ($(`#${id}`).hasClass('ui-btn-clicked-layer-preview')) { // 미리보기 제거
			for (var i = 0; i < tree.length; i++) {
				if ($(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					var split = $(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class;
					var before = split.split(' ');
					var after = [];
					for (var j = 0; j < before.length; j++) {
						if (before[j].length != 'ui-btn-clicked-layer-preview') { after.push(before[j]); }
					}
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class = after.join(' ');
					$(`#${tree[i].id}`).removeClass('ui-btn-clicked-layer-preview');
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.removeLayerById($(`#${tree[i].id}`).attr('value'), true);
						// CMSC003.GIS.hideLayerById(tree[i].li_attr.value, true);
					}
				}

			}
		} else { //미리보기 생성
			for (var i = 0; i < tree.length; i++) {
				if (!$(`#${tree[i].id}`).hasClass('ui-btn-clicked-layer-preview') && !$(`#${tree[i].id} > a > button`).is(":disabled")) {
					$(`#${id}`).jstree().open_node(tree[i]);
					$(`#${tree[i].id}`).addClass('ui-btn-clicked-layer-preview');
					$(`#${id}`).jstree()._model.data[tree[i].id].li_attr.class += ' ui-btn-clicked-layer-preview';
					if ($(`li#${tree[i].id}[class*="js-tree-child"]`).length) { // 최하위 자식인 경우
						CMSC003.GIS.updateImageLayerTemp($(`#${tree[i].id}`).attr('value'), true);
					}
				}

			}
		}
	}
});

// 사이드바 닫기 클릭
$('.sidebar-open-button').click(function() {
	setTimeout(function() {
		CMSC003.GIS.getMap().updateSize();
	}, 100);
});

/*$('a[href="javascript:showCreateDataMsg()"]').on('click', function(e) {
	CMSC003.DOM.showCreateDataMsg();
})*/

// 생성용 재난 id 인풋 클릭
$('#disasterIdCreate').click(function(e) {
	CMSC003.DOM.showDisasterIDSearchPopup('js-create-emergency-id');
});

// 생성용 재난 id 인풋 클릭
$('#disasterIdManage').click(function(e) {
	CMSC003.DOM.showDisasterIDSearchPopup('js-manage-emergency-id');
});

// 재난 id 검색결과 클릭
$('body').on('click', '#cmsc003-modal table.js-table-disaster-result tbody tr.js-create-emergency-id', function(e) {
	if ($(e.currentTarget).children('td').length === 1) {
		return;
	}
	var child = $(e.currentTarget).children('td:first-child');
	var sido = $(e.currentTarget).children('td:nth-child(5)').text();
	//	CMSC003.Storage.set('currentSido', sido);
	//	console.log(sido);
	var gugun = $(e.currentTarget).children('td:nth-child(6)').text();
	//	CMSC003.Storage.set('currentGugun', gugun);
	//	console.log(gugun);
	var dong = $(e.currentTarget).children('td:nth-child(7)').text();
	//	CMSC003.Storage.set('currentDong', dong);

	getSido(sido, gugun, dong, true);

	$('#detailAddrForm').val($(e.currentTarget).children('td:nth-child(8)').text());
	//	console.log(dong);
	$('#disasterIdCreate').val($(child).text());
	CMSC003.Storage.set('currentDisasterCode', $(child).attr('data'));
	var x = parseFloat($(child).attr('x'));
	var y = parseFloat($(child).attr('y'));

	var bbox = $(child).attr('bbox');
	var bbox_proj = $(child).attr('mapprjctncn');

	var positionToNumber = [x, y];

	var transform = CMSC003.GIS.transform(positionToNumber, 'EPSG:4326', 'EPSG:5179');
	CMSC003.Storage.set('currentDisasterSpot', positionToNumber);

	// 지도 중심점 이동
	CMSC003.GIS.setMapCenter(transform);

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

		// ROI 피처 생성
		CMSC003.GIS.createPolygonROIfrom5179(transform_bbox);

	}

	$('#cmsc003-modal').remove();
})

$('body').on('click', '#cmsc003-modal table.js-table-disaster-result tbody tr.js-manage-emergency-id', function(e) {
	if ($(e.currentTarget).children('td').length === 1) {
		return;
	}
	var child = $(e.currentTarget).children('td:first-child');
	$('#disasterIdManage').val($(child).text());

	CMSC003.Storage.set('currentDisasterCode', $(child).attr('data'));
	var x = parseFloat($(child).attr('x'));
	var y = parseFloat($(child).attr('y'));

	var bbox = $(child).attr('bbox');
	var bbox_proj = $(child).attr('mapprjctncn');

	var positionToNumber = [x, y];

	var transform = CMSC003.GIS.transform(positionToNumber, 'EPSG:4326', 'EPSG:5179');
	CMSC003.Storage.set('currentDisasterSpot', positionToNumber);

	// 지도 중심점 이동
	CMSC003.GIS.setMapCenter(transform);

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

		// ROI 피처 생성
		CMSC003.GIS.createPolygonROIfrom5179(transform_bbox);

	}

	$('#cmsc003-modal').remove();
});

// 4326 ROI 입력
$('.js-input-roi-extent').on('input', function(e) {
	setTimeout(function() {
		var ulx = parseFloat($('#inputULX').val());
		var uly = parseFloat($('#inputULY').val());
		var lrx = parseFloat($('#inputLRX').val());
		var lry = parseFloat($('#inputLRY').val());
		if (!isNaN(ulx) && !isNaN(uly) && !isNaN(lrx) && !isNaN(lry)) {
			console.log(ulx, uly, lrx, lry);
			CMSC003.DOM.inputROIToHiddenForm(ulx, uly, lrx, lry);
		}
	}, 300)
});

$('#roiRadius').on('input', function() {
	setTimeout(function() {
		var source = CMSC003.GIS.getROICenterSource();
		if (source.getFeatures().length > 0) {
			var meters = parseFloat($('#roiRadius').val());
			var spot = CMSC003.Storage.get('currentDisasterSpot');
			if (!isNaN(meters) && spot) {
				CMSC003.GIS.createBufferROI(spot, meters);
			}
		} else {
			$('#roiRadius').val('');
		}
	}, 300);
});

//============================================================================================================
// 체크박스 전체선택

/*// 수치지도
$('.js-search-result-area').on('click', '.js-control-checkbox-digital-all', function(e) {
	var checkType = $(this).prop('checked');
	console.log(checkType);
	$('input.js-search-result-digital').prop('checked', checkType);
});

// 항공영상
$('.js-search-result-area').on('click', '.js-control-checkbox-aerial-all', function(e) {
	var checkType = $(this).prop('checked');
	console.log(checkType);
	$('input.js-search-result-aerial').prop('checked', checkType);
});

// 정사영상
$('.js-search-result-area').on('click', '.js-control-checkbox-ortho-all', function(e) {
	var checkType = $(this).prop('checked');
	console.log(checkType);
	$('input.js-search-result-ortho').prop('checked', checkType);
});

// 위성영상
$('.js-search-result-area').on('click', '.js-control-checkbox-satellite-all', function(e) {
	var checkType = $(this).prop('checked');
	console.log(checkType);
	$('input.js-search-result-kompsat').prop('checked', checkType);
	$('input.js-search-result-landsat').prop('checked', checkType);
	$('input.js-search-result-sentinel').prop('checked', checkType);
	$('input.js-search-result-cas').prop('checked', checkType);
});

// dem
$('.js-search-result-area').on('click', '.js-control-checkbox-dem-all', function(e) {
	var checkType = $(this).prop('checked');
	console.log(checkType);
	$('input.js-search-result-dem').prop('checked', checkType);
});

// 인구밀도
$('.js-search-result-area').on('click', '.js-control-checkbox-demo-all', function(e) {
	var checkType = $(this).prop('checked');
	console.log(checkType);
	$('input.js-search-result-demo').prop('checked', checkType);
});

// 지가통계
$('.js-search-result-area').on('click', '.js-control-checkbox-landg-all', function(e) {
	var checkType = $(this).prop('checked');
	console.log(checkType);
	$('input.js-search-result-landg').prop('checked', checkType);
});

// 건물통계
$('.js-search-result-area').on('click', '.js-control-checkbox-buld-all', function(e) {
	var checkType = $(this).prop('checked');
	console.log(checkType);
	$('input.js-search-result-buld').prop('checked', checkType);
});

// 지진
$('.js-search-result-area').on('click', '.js-control-checkbox-earthquake-all', function(e) {
	var checkType = $(this).prop('checked');
	console.log(checkType);
	$('input.js-search-result-earthquake').prop('checked', checkType);
});

// 산불
$('.js-search-result-area').on('click', '.js-control-checkbox-forestFire-all', function(e) {
	var checkType = $(this).prop('checked');
	console.log(checkType);
	//$('input.js-search-result-forestfire').prop('checked', checkType);
	$('input.js-search-result-forestfire').prop('checked', checkType);
});

// 
$('.js-search-result-area').on('click', '.js-control-checkbox-flood-all', function(e) {
	var checkType = $(this).prop('checked');
	console.log(checkType);
	$('input.js-search-result-flood').prop('checked', checkType);
});

//
$('.js-search-result-area').on('click', '.js-control-checkbox-landslip-all', function(e) {
	var checkType = $(this).prop('checked');
	console.log(checkType);
	$('input.js-search-result-landslip').prop('checked', checkType);
});

//
$('.js-search-result-area').on('click', '.js-control-checkbox-maritime-all', function(e) {
	var checkType = $(this).prop('checked');
	console.log(checkType);
	$('input.js-search-result-maritime').prop('checked', checkType);
});

// 적조
$('.js-search-result-area').on('click', '.js-control-checkbox-redTide-all', function(e) {
	var checkType = $(this).prop('checked');
	console.log(checkType);
	//$('input.js-search-result-redTide').prop('checked', checkType);
	$('input.js-search-result-redTide').prop('checked', checkType);
});

//
$('.js-search-result-area').on('click', '.js-control-checkbox-vectorAnalObj-all', function(e) {
	var checkType = $(this).prop('checked');
	console.log(checkType);
	$('input.js-search-result-vectoranalobj').prop('checked', checkType);
	
});

//
$('.js-search-result-area').on('click', '.js-control-checkbox-rasterAnalObj-all', function(e) {
	var checkType = $(this).prop('checked');
	console.log(checkType);
	$('input.js-search-result-rasteranalobj').prop('checked', checkType);
});

//
$('.js-search-result-area').on('click', '.js-control-checkbox-vectorAnalChg-all', function(e) {
	var checkType = $(this).prop('checked');
	console.log(checkType);
	$('input.js-search-result-vectoranalchg').prop('checked', checkType);
});

//
$('.js-search-result-area').on('click', '.js-control-checkbox-rasterAnalChg-all', function(e) {
	var checkType = $(this).prop('checked');
	console.log(checkType);
	$('input.js-search-result-rasteranalchg').prop('checked', checkType);
});*/
$('#createTab').trigger('click');
$('#createTab').click(function() {
	$('#today').show();
	$('#manageEmergency').hide();
});
$('#manageTab').click(function() {
	$('#today').hide();
	$('#manageEmergency').show();
});

$('#sendDatasetBtn').click(function() {
	var did = $('#disasterIdManage').val();
	if (did.trim() === '') {
		CMSC003.DOM.showErrorMsg('재난 ID를 입력해주세요.');
		return;
	}
	CMSC003.DOM.sendEmergencyManagement(did,2);
});


$('#sendDatasetBtn2').click(function() {
	var did = $('#disasterIdManage').val();
	if (did.trim() === '') {
		CMSC003.DOM.showErrorMsg('재난 ID를 입력해주세요.');
		return;
	}
	CMSC003.DOM.sendEmergencyManagement(did,1);
});