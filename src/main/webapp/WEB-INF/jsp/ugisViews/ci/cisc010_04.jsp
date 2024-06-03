<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div id="prfomncEvlResultModel" role="tabpanel" class="tab-pane" data-backdrop="static" data-keyboard="false">
   	<div data-bind="with: search">
		<div class="inside-panel panel-default">
			<div id="searchPrfomncEvlResultList" data-bind="click: $parent.searchList" class="inside-panel-tit">성능평가 목록
				<ul class="panel-tools">
				    <li><a id="resultSearch" class="icon search-tool"><i class="fas fa-sync-alt"></i></a></li>
				</ul>
			</div>

			<div class="panel inside-panel-body" style="height: 600px;">
				<ul data-bind="foreach: prfomncEvlResultList" class="result-list">
					<li class="has" data-bind="text: modelNm + ' (' + aiDataSetNm + ')', contextMenu: $root.prfomncEvlResult">
<!-- 						<input data-bind="value: modelPrfomncEvlId, attr: {'id': 'modelPrfomncEvlId' + modelPrfomncEvlId}" type="checkbox" name="modelPrfomncEvlId" value=""> -->
<!-- 						<label class="context-menu-one btn-neutral"> -->
<!-- 						</label> -->
					</li>
				</ul>
			</div>
		</div>
	</div>

	<div id="prfomncEvlResult" class="modal fade" tabindex="-1" role="dialog" aria-hidden="true" data-backdrop="static" data-keyboard="false">
		<div data-bind="with: form" class="modal-dialog modal-sm">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title">성능평가 결과</h4>
				</div>
				<div class="modal-body">
					<div class="panel-body">
						<div class="form-group m-t-10">
							<label for="input002" class="col-sm-3 control-label form-label">모델 명</label>
							<div class="col-sm-9">
								<input data-bind="value: modelNm, disable: modelId()" type="text" class="form-control">
							</div>
						</div>
						<div class="form-group m-t-10">
							<div class="col-sm-12">
								<div class="tbl-header-n-scr">
									<table cellpadding="0" cellspacing="0" border="0">
										<thead>
										<tr>
											<th width="25%">Accuracy</th>
											<th width="25%">mIoU</th>
											<th width="25%">Precision</th>
											<th>Recall</th>
										</tr>
										</thead>
									</table>
								</div>
								<div class="tbl-content-n-scr">
									<table cellpadding="0" cellspacing="0" border="0">
										<tbody>
										<tr>
											<td width="25%"><span data-bind="text: acccuracyPt">0</span>%</td>
											<td width="25%"><span data-bind="text: mlouPt">0</span>%</td>
											<td width="25%"><span data-bind="text: precisionPt">0</span>%</td>
											<td><span data-bind="text: recallPt">0</span>%</td>
										</tr>
										</tbody>
									</table>
								</div>
							</div>
						</div>

					</div>
				</div>
			</div>
		</div>
	</div>

</div>

<script>
$(document).on('click', '.tree label', function(e) {
  $(this).next('ul').fadeToggle();
  e.stopPropagation();
});

$(document).on('change', '.tree input[type=checkbox]', function(e) {
  $(this).siblings('ul').find("input[type='checkbox']").prop('checked', this.checked);
  $(this).parentsUntil('.tree').children("input[type='checkbox']").prop('checked', this.checked);
  e.stopPropagation();
});

$(document).on('click', 'button', function(e) {
  switch ($(this).text()) {
	case 'Collepsed':
	  $('.tree ul').fadeOut();
	  break;
	case 'Expanded':
	  $('.tree ul').fadeIn();
	  break;
	case 'Checked All':
	  $(".tree input[type='checkbox']").prop('checked', true);
	  break;
	case 'Unchek All':
	  $(".tree input[type='checkbox']").prop('checked', false);
	  break;
	default:
  }
});

$(function(){
// 	$.contextMenu({
// 		selector: '.context-menu-one',
// 		callback: function(key, opt){
// 			console.log(key, opt, opt.$trigger.attr("id").val());
// 			$("#prfomncEvlResult").modal('show');
// 		},
// 		items:
// 		{
// 			vector: {
// 					name: "벡터로 변환",
// 					callback: function(key, opt){
// 					alert("Clicked on " + key);
// 				}
// 			},
// 			download: {
// 					name: "다운로드",
// 					callback: function(key, opt){
// 					alert("Clicked on " + key);
// 				}
// 			},
// 			result: {
// 				name: "성능평가 결과 조회"
// 			}
// 		},
// 	});
});
</script>