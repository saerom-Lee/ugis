<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div id="aiLearningCompleteModel">
	<div id="aiLearningComplete" data-bind="with: form" class="modal fade" tabindex="-1" role="dialog" aria-hidden="true" data-backdrop="static" data-keyboard="false">
		<form data-bind="submit: $parent.saveData">
		<div data-bind="validationOptions: { insertMessages: false }" class="modal-dialog modal-sm">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title">학습 완료</h4>
				</div>
				<div class="modal-body">
					<div class="panel-body">
						<div class="col-sm-12">
							<div class="kode-alert kode-alert-icon alert1-light">
								<i class="fa fa-envelope-o"></i>
								‘<span data-bind="text: beforeModelNm"></span>’의 학습이 완료되었습니다.
							</div>
							<div class="tbl-header-n-scr">
								<table cellpadding="0" cellspacing="0" border="0">
									<thead>
									<tr>
										<th width="20%">구분</th>
										<th width="20%">Accuracy</th>
										<th width="20%">mIoU</th>
										<th width="20%">Precision</th>
										<th>Recall</th>
									</tr>
									</thead>
								</table>
							</div>
							<div class="tbl-content-n-scr">
								<table cellpadding="0" cellspacing="0" border="0">
									<tbody>
									<tr data-bind="visible: beforeAiLrnId()">
										<td width="20%">기존</td>
										<td width="20%"><span data-bind="text: beforeAcccuracyPt">0</span>%</td>
										<td width="20%"><span data-bind="text: beforeMlouPt">0</span>%</td>
										<td width="20%"><span data-bind="text: beforePrecisionPt">0</span>%</td>
										<td><span data-bind="text: beforeRecallPt">0</span>%</td>
									</tr>
									<tr>
										<td>신규</td>
										<td><span data-bind="text: acccuracyPt">0</span>%</td>
										<td><span data-bind="text: mlouPt">0</span>%</td>
										<td><span data-bind="text: precisionPt">0</span>%</td>
										<td><span data-bind="text: recallPt">0</span>%</td>
									</tr>
									</tbody>
								</table>
							</div>
						</div>
						<div class="form-group m-t-10">
							<div class="notice">
								모델 저장 방식을 선택해주세요.<br>표준 모델을 사용하여 학습을 진행한 경우, 다른 이름으로 저장만 가능합니다.
							</div>
							<div class="col-sm-6">
								<div class="radio radio-info radio-inline">
									<input type="radio" id="overwriteYn1" name="overwrite" value="Y" data-bind="checked: overwrite, click:click, disable: !isUserModel()" />
									<label for="overwriteYn1">기존 파일 덮어쓰기</label>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="radio radio-info radio-inline">
									<input type="radio" id="overwriteYn2" name="overwrite" value="N" data-bind="checked: overwrite, click:click" />
									<label for="overwriteYn2">다른 이름으로 저장</label>
								</div>
							</div>
						</div>
						<div class="form-group m-t-10" data-bind="visible: !isOverwrite()">
							<label class="col-sm-3 control-label form-label">모델 명</label>
							<div class="col-sm-9">
								<input data-bind="value: modelNm" type="text" name="modelNm" class="form-control">
								<input data-bind="value: modelId" type="hidden" name="modelId" class="form-control">
								<input data-bind="value: modelId" type="hidden" name="beforeModelId" class="form-control">
								<input data-bind="value: aiModelId" type="hidden" name="aiModelId" class="form-control">
								<input data-bind="value: aiModelId" type="hidden" name="beforeAiModelId" class="form-control">
							</div>
						</div>
						<div class="col-sm-12">
							<p class="notice-sm" style="text-align: left; marin: 5px 0;">* 학습결과를 저장하지 않을 경우, 결과는 삭제됩니다.</p>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button data-bind="click: $parent.deleteData" type="button" class="btn btn-gray"><i class="fas fa-times m-r-5"></i>저장하지 않음</button>
					<button type="submit" type="button" class="btn btn-default"><i class="fas fa-pencil-alt m-r-5"></i>저장</button>
				</div>
			</div>
		</div>
		</form>
	</div>

	<div id="prfomncEvlResultComplete" class="modal fade" tabindex="-1" role="dialog" aria-hidden="true" data-backdrop="static" data-keyboard="false">
		<div data-bind="with: resultForm" class="modal-dialog modal-sm">
			<div data-bind="validationOptions: { insertMessages: false }" class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title">성능평가 결과</h4>
				</div>
				<div class="modal-body">
					<div class="panel-body">
						<div class="kode-alert kode-alert-icon alert1-light">
							<i class="fa fa-envelope-o"></i>
							‘<span data-bind="text: modelNm"></span> (<span data-bind="text: aiDataSetNm"></span>)’ 의 성능평가가 완료되었습니다. 성능지표 결과는 아래와 같습니다.
						</div>
	<!-- 						<div class="form-group m-t-10"> -->
	<!-- 							<label for="input002" class="col-sm-3 control-label form-label">모델 명</label> -->
	<!-- 							<div class="col-sm-9"> -->
	<!-- 								<input data-bind="value: modelNm, disable: modelId()" type="text" class="form-control"> -->
	<!-- 							</div> -->
	<!-- 						</div> -->
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
						<div class="form-group m-t-10">
							<div class="notice">
								분석 결과는 평가 결과 목록에 추가됩니다.
							</div>
						</div>

					</div>
				</div>
			</div>
		</div>
	</div>

</div>