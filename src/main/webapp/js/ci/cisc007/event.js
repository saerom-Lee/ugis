$(document).ready(function() {

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
							CMSC003.GIS.removeLayerById('ortho-' + $(`#${tree[i].id}`).attr('value'), false, map);
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
							CMSC003.GIS.updateImageLayerTemp('ortho-' + $(`#${tree[i].id}`).attr('value'), false, map);
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
							CMSC003.GIS.removeLayerById('aerial-' + $(`#${tree[i].id}`).attr('value'), false, map);
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
							CMSC003.GIS.updateImageLayerTemp('aerial-' + $(`#${tree[i].id}`).attr('value'), false, map);
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
							CMSC003.GIS.removeLayerById('dem-' + $(`#${tree[i].id}`).attr('value'), false, map);
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
							CMSC003.GIS.updateImageLayerTemp('dem-' + $(`#${tree[i].id}`).attr('value'), false, map);
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
							CMSC003.GIS.removeLayerById(tree[i].id.split('-')[2] + '-' + $(`#${tree[i].id}`).attr('value'), false, map);
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
							CMSC003.GIS.updateImageLayerTemp(tree[i].id.split('-')[2] + '-' + $(`#${tree[i].id}`).attr('value'), false, map);
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
							CMSC003.GIS.removeLayerById('kompsat-' + $(`#${tree[i].id}`).attr('value'), false, map);
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
							CMSC003.GIS.updateImageLayerTemp('kompsat-' + $(`#${tree[i].id}`).attr('value'), false, map);
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
							CMSC003.GIS.removeLayerById('landsat-' + $(`#${tree[i].id}`).attr('value'), false, map);
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
							CMSC003.GIS.updateImageLayerTemp('landsat-' + $(`#${tree[i].id}`).attr('value'), false, map);
						}

					}
				}
			}
		}
	});

	// 미리보기 버튼 클릭 - 위성영상 sentinel
	$('.js-search-result-area').on('click', '.ui-btn-preview-sentinel-image-layer', function(e) {
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
							CMSC003.GIS.removeLayerById('sentinel-' + $(`#${tree[i].id}`).attr('value'), false, map);
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
							CMSC003.GIS.updateImageLayerTemp('sentinel-' + $(`#${tree[i].id}`).attr('value'), false, map);
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
							CMSC003.GIS.removeLayerById('cas-' + $(`#${tree[i].id}`).attr('value'), false, map);
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
							CMSC003.GIS.updateImageLayerTemp('cas-' + $(`#${tree[i].id}`).attr('value'), false, map);
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
							CMSC003.GIS.removeLayerById('flood-' + $(`#${tree[i].id}`).attr('value'), false, map);
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
							CMSC003.GIS.updateImageLayerTemp('flood-' + $(`#${tree[i].id}`).attr('value'), false, map);
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
							CMSC003.GIS.removeLayerById('forestFire-' + $(`#${tree[i].id}`).attr('value'), false, map);
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
							CMSC003.GIS.updateImageLayerTemp('forestFire-' + $(`#${tree[i].id}`).attr('value'), false, map);
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
							CMSC003.GIS.removeLayerById('earthquake-' + $(`#${tree[i].id}`).attr('value'), false, map);
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
							CMSC003.GIS.updateImageLayerTemp('earthquake-' + $(`#${tree[i].id}`).attr('value'), false, map);
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
							CMSC003.GIS.removeLayerById('landslide-' + $(`#${tree[i].id}`).attr('value'), false, map);
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
							CMSC003.GIS.updateImageLayerTemp('landslide-' + $(`#${tree[i].id}`).attr('value'), false, map);
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
							CMSC003.GIS.removeLayerById('maritime-' + $(`#${tree[i].id}`).attr('value'), false, map);
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
							CMSC003.GIS.updateImageLayerTemp('maritime-' + $(`#${tree[i].id}`).attr('value'), false, map);
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
							CMSC003.GIS.removeLayerById('redTide-' + $(`#${tree[i].id}`).attr('value'), false, map);
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
							CMSC003.GIS.updateImageLayerTemp('redTide-' + $(`#${tree[i].id}`).attr('value'), false, map);
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
							CMSC003.GIS.removeLayerById('rasterAnalObj-' + $(`#${tree[i].id}`).attr('value'), false, map);
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
							CMSC003.GIS.updateImageLayerTemp('rasterAnalObj-' + $(`#${tree[i].id}`).attr('value'), false, map);
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
							CMSC003.GIS.removeLayerById('rasterAnalChg-' + $(`#${tree[i].id}`).attr('value'), false, map);
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
							CMSC003.GIS.updateImageLayerTemp('rasterAnalChg-' + $(`#${tree[i].id}`).attr('value'), false, map);
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
							CMSC003.GIS.removeLayerById('vectorAnalObj-' + $(`#${tree[i].id}`).attr('value'), false, map);
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
							// CMSC003.GIS.updateImageLayerAnalysisRequest('vectorAnalObj-' + $(`#${tree[i].id}`).attr('value'), false, map);
							var currentTab = getCurrentTab();
							var checked = null;
							if(currentTab === 1) {
								checked = $('input[name=dataTypeColor]:checked').val();								
							} else if(currentTab === 2) {
								checked = $('input[name=dataTypeHist]:checked').val();
							} else if(currentTab === 3) {
								checked = $('input[name=dataTypeMosa]:checked').val();
							}
							
							if(checked === '1') {
								CMSC003.GIS.updateImageLayerAnalysisRequest('vectorAnalObj-' + $(`#${tree[i].id}`).attr('value'), false, map);	
							} else if(checked === '2'){
								CMSC003.GIS.updateImageLayerAnalysisResult('vectorAnalObj-' + $(`#${tree[i].id}`).attr("value"), false, map);
							}
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
							CMSC003.GIS.removeLayerById('vectorAnalChg-' + $(`#${tree[i].id}`).attr('value'), false, map);
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
							// CMSC003.GIS.updateImageLayerAnalysisRequest('vectorAnalChg-' + $(`#${tree[i].id}`).attr('value'), false, map);
							
							var currentTab = getCurrentTab();
							var checked = null;
							if(currentTab === 1) {
								checked = $('input[name=dataTypeColor]:checked').val();								
							} else if(currentTab === 2) {
								checked = $('input[name=dataTypeHist]:checked').val();
							} else if(currentTab === 3) {
								checked = $('input[name=dataTypeMosa]:checked').val();
							}
							
							if(checked === '1') {
								CMSC003.GIS.updateImageLayerAnalysisRequest('vectorAnalChg-' + $(`#${tree[i].id}`).attr('value'), false, map);	
							} else if(checked === '2'){
								CMSC003.GIS.updateImageLayerAnalysisResult('vectorAnalChg-' + $(`#${tree[i].id}`).attr("value"), false, map);
							}
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
							CMSC003.GIS.removeLayerById($(`#${tree[i].id}`).attr('value'), false, map);
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
							// CMSC003.GIS.updateImageLayerFromGeoserver($(`#${tree[i].id}`).attr('value'), false, map);
							
							var currentTab = getCurrentTab();
							var checked = null;
							if(currentTab === 1) {
								checked = $('input[name=dataTypeColor]:checked').val();								
							} else if(currentTab === 2) {
								checked = $('input[name=dataTypeHist]:checked').val();
							} else if(currentTab === 3) {
								checked = $('input[name=dataTypeMosa]:checked').val();
							}
							
							if(checked === '1') {
								CMSC003.GIS.updateImageLayerFromGeoserver($(`#${tree[i].id}`).attr('value'), false, map);	
							} else if(checked === '2'){
								CMSC003.GIS.updateImageLayerAnalysisResult($(`#${tree[i].id}`).attr("value"), false, map);
							}
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
							CMSC003.GIS.removeLayerById($(`#${tree[i].id}`).attr('value'), false, map);
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
							// CMSC003.GIS.updateImageLayerFromGeoserverDemographic($(`#${tree[i].id}`).attr('value'), false, map);
							
							var currentTab = getCurrentTab();
							var checked = null;
							if(currentTab === 1) {
								checked = $('input[name=dataTypeColor]:checked').val();								
							} else if(currentTab === 2) {
								checked = $('input[name=dataTypeHist]:checked').val();
							} else if(currentTab === 3) {
								checked = $('input[name=dataTypeMosa]:checked').val();
							}
							
							if(checked === '1') {
								CMSC003.GIS.updateImageLayerFromGeoserverDemographic($(`#${tree[i].id}`).attr('value'), false, map);	
							} else if(checked === '2'){
								CMSC003.GIS.updateImageLayerAnalysisResult($(`#${tree[i].id}`).attr("value"), false, map);
							}
						}

					}
				}
			}
		}
	});

});