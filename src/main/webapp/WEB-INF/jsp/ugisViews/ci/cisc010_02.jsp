<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div id="aiLearningModel" role="tabpanel" class="tab-pane">
	<div class="sidepanel-m-title">모델 & AI 데이터셋 검색</div>
	<div class="panel-body">
    	<div data-bind="with: search">

	   		<!-- 검색 체크박스 공통 -->
			<%@ include file="cisc010_chkbox.jsp" %>

			<div class="btn-wrap a-cent">
				<a data-bind="click: $parent.searchList" id="searchAiLearningList" href="#" class="btn btn-default">
					<i class="fas fa-search m-r-5"></i>검색
				</a>
			</div>

			<div class="sidepanel-s-title m-t-30">검색 결과</div>
			<div class="panel-body">
				<div class="col-lg-12">
					<div class="panel panel-default" style="height: 150px;">
						<div class="in-panel-title">기본 모델 목록</div>
						<dl data-bind="foreach: baseModelList" class="result-list-input">
							<dd>
								<div class="radio radio-info radio-inline">
									<input data-bind="value: modelId, attr: {'id': 'modelId' + modelId}, click: $parent.clickModelId" type="radio" name="modelId">
									<label data-bind="text: modelNm, attr: {'for': 'modelId' + modelId}"></label>
								</div>
								<a data-bind="click: $root.searchDetail" href="#" class="btn btn-xs f-right" data-toggle="modal" data-target="#modelDetailModal">
<!-- 								<a href="#" class="btn btn-xs f-right" data-toggle="modal" data-target="#modelDetailModal"> -->
									<i class="fas fa-search"></i>
								</a>
							</dd>
						</dl>
						<div class="in-panel-title">사용자 모델 목록</div>
						<dl data-bind="foreach: userModelList" class="result-list-input">
							<dd>
								<div class="radio radio-info radio-inline">
									<input data-bind="value: modelId, attr: {'id': 'modelId' + modelId}, click: $parent.clickModelId" type="radio" name="modelId">
									<label data-bind="text: modelNm, attr: {'for': 'modelId' + modelId}"></label>
								</div>
								<a data-bind="click: $root.searchDetail" href="#" class="btn btn-xs f-right" data-toggle="modal" data-target="#modelDetailModal">
									<i class="fas fa-search"></i>
								</a>
							</dd>
						</dl>
					</div>
				</div>
			</div>
			<div class="panel-body">
				<div class="col-lg-12">
					<div class="panel panel-default" style="height: 150px;">
						<div class="in-panel-title">AI 데이터셋 목록</div>
						<dl data-bind="foreach: dataSetList" class="result-list-input">
							<dd>
								<div class="radio radio-info radio-inline">
									<input data-bind="value: aiDataSetId, attr: {'id': 'aiDataSetId' + aiDataSetId}, click: $parent.clickAiDataSetId" type="radio" name="aiDataSetId">
									<label data-bind="text: aiDataSetNm, attr: {'for': 'aiDataSetId' + aiDataSetId}"></label>
								</div>
								<a data-bind="click: $root.searchDataSetDetail" href="#" class="btn btn-xs f-right" data-toggle="modal">
									<i class="fas fa-search"></i>
								</a>
							</dd>
						</dl>
					</div>
				</div>
			</div>
		</div>

		<div class="btn-wrap a-cent">
			<a data-bind="click: newData" id="btnNewDataAi" href="#" class="btn btn-default" data-toggle="modal">
				<i class="fas fa-pencil-alt m-r-5"></i>AI 학습 수행
			</a>
		</div>

		<div data-bind="with: form" class="modal fade" id="aiLearningForm" tabindex="-1" role="dialog" aria-hidden="true" data-backdrop="static" data-keyboard="false">
			<form data-bind="submit: $parent.saveData">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h4 class="modal-title">모델 학습</h4>
					</div>
					<div class="modal-body">
						<div class="panel-body">
							<div class="col-sm-6 p-r-15">
								<div class="sidepanel-m-title">데이터셋 상세</div>
								<div class="form-group m-t-10">
									<label class="col-sm-3 control-label form-label">모델</label>
									<div class="col-sm-9">
										<input data-bind="value: modelNm, disable: modelId()" type="text" class="form-control">
										<input data-bind="value: modelId" name="modelId" type="hidden">
										<input data-bind="value: modelSeCd" type="hidden">
										<input data-bind="value: progrsSttusCd" name="progrsSttusCd" type="hidden">
									</div>
								</div>
								<div class="form-group m-t-10">
									<label class="col-sm-3 control-label form-label">알고리즘</label>
									<div class="col-sm-9">
<!-- 										<input data-bind="value: algrthCd, disable: modelId()" type="text" class="form-control"> -->
										<select data-bind="options: algrthNmOptions, optionsValue: 'value', optionsText: 'text', value: algrthCd, valueAllowUnset: true, optionsCaption: '', disable: modelId()" class="form-basic"></select>
									</div>
								</div>
								<div class="sidepanel-m-title m-t-10">선택 AI 데이터셋 정보</div>
								<div class="form-group m-t-10">
									<label class="col-sm-3 control-label form-label">데이터셋</label>
									<div class="col-sm-9">
										<input data-bind="value: aiDataSetNm, disable: aiDataSetId()" type="text" class="form-control">
										<input data-bind="value: aiDataSetId" name="aiDataSetId" type="hidden">
									</div>
								</div>
<!-- 								<div class="form-group m-t-10"> -->
<!-- 									<label class="col-sm-3 control-label form-label">클래스</label> -->
<!-- 									<div class="col-sm-9"> -->
<!-- 										<input data-bind="value: '', disable: aiDataSetId()" type="text" class="form-control"> -->
<!-- 									</div> -->
<!-- 								</div> -->
								<div class="form-group m-t-10">
									<label class="col-sm-3 control-label form-label">영상</label>
									<div class="col-sm-9">
										<select name="imageNm" data-bind="options: imageNmOptions, optionsValue: 'value', optionsText: 'text', value: imageNm, valueAllowUnset: true, optionsCaption: '', disable: aiDataSetId()" class="form-basic"></select>
										<input data-bind="numeric, value: rsoltnValue, disable: aiDataSetId()" type="hidden">
									</div>
								</div>
								<label class="col-sm-12 control-label form-label">밴드 목록</label>
								<div class="tbl-header">
									<table cellpadding="0" cellspacing="0" border="0">
										<thead>
											<tr>
												<th width="50%">이름</th>
												<th>스펙트럼</th>
											</tr>
										</thead>
									</table>
								</div>
								<div class="tbl-content">
									<table cellpadding="0" cellspacing="0" border="0">
										<tbody>
											<tr>
												<td width="50%">Band 1</td>
												<td>
													<select name="bandNm1" data-bind="options: bandNm1Options, optionsValue: 'value', optionsText: 'text', value: bandNm1, valueAllowUnset: true, optionsCaption: '', disable: aiDataSetId()" class="form-basic"></select>
												</td>
											</tr>
											<tr>
												<td>Band 2</td>
												<td>
													<select name="bandNm2" data-bind="options: bandNm2Options, optionsValue: 'value', optionsText: 'text', value: bandNm2, valueAllowUnset: true, optionsCaption: '', disable: aiDataSetId()" class="form-basic"></select>
												</td>
											</tr>
											<tr>
												<td>Band 3</td>
												<td>
													<select name="bandNm3" data-bind="options: bandNm3Options, optionsValue: 'value', optionsText: 'text', value: bandNm3, valueAllowUnset: true, optionsCaption: '', disable: aiDataSetId()" class="form-basic"></select>
												</td>
											</tr>
											<tr>
												<td>Band 4</td>
												<td>
													<select name="bandNm4" data-bind="options: bandNm4Options, optionsValue: 'value', optionsText: 'text', value: bandNm4, valueAllowUnset: true, optionsCaption: '', disable: aiDataSetId()" class="form-basic"></select>
												</td>
											</tr>
										</tbody>
									</table>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="sidepanel-m-title">파라미터 설정</div>
								<div class="form-group m-t-10">
									<label class="col-sm-3 control-label form-label">패치 사이즈</label>
									<div class="col-sm-9">
										<select data-bind="options: patchSizeOptions, optionsValue: 'value', optionsText: 'text', valueAllowUnset: true, optionsCaption: '--- 선택 ---', value: patchSizeCd, disable: aiDataSetId()" name="patchSizeCd" class="form-basic">
										</select>
										<input data-bind="value: patchSizeCd" name="patchSizeCd" type="hidden">
									</div>
								</div>
								<div class="form-group m-t-10">
									<label class="col-sm-3 control-label form-label">배치 사이즈</label>
									<div class="col-sm-9">
										<select data-bind="options: batchSizeOptions, optionsValue: 'value', optionsText: 'text', valueAllowUnset: true, optionsCaption: '--- 선택 ---', value: batchSizeCd" name="batchSizeCd" class="form-basic">
										</select>
									</div>
								</div>
								<div class="form-group m-t-10">
									<label class="col-sm-3 control-label form-label">GPU</label>
									<div class="col-sm-9">
										<select class="form-basic" name="gpu">
											<option value="1">1</option>
										</select>
									</div>
								</div>
								<div class="form-group m-t-10">
									<label class="col-sm-3 control-label form-label">반복횟수</label>
									<div class="col-sm-9">
										<div class="col-sm-2">
											<button id="minusReptitCo" type="button" class="btn btn-light btn-icon">
												<i class="fas fa-minus"></i>
											</button>
										</div>
										<div class="col-sm-8">
											<input data-bind="value: reptitCo" type="text" class="form-control" name="reptitCo" maxlength="5" oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');">
										</div>
										<div class="col-sm-2">
											<button id="plusReptitCo" type="button" class="btn btn-light btn-icon">
												<i class="fas fa-plus"></i>
											</button>
										</div>
									</div>
								</div>
								<div class="form-group m-t-10">
									<label class="col-sm-12 control-label form-label">데이터셋 학습/검증 비율 설정</label>
									<div class="col-sm-12">
										<div class="col-sm-3">
											<div class="col-sm-6 in-tit">학습</div>
											<div class="col-sm-6">
												<input data-bind="value: learning" type="text" class="form-control" id="learning" value="50" maxlength="3" oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');">
											</div>
										</div>
										<div class="col-sm-6">
											<div class="slidecontainer">
												<input data-bind="value: lrnRateSetup" type="range" min="0" max="100" value="50" name="lrnRateSetup">
											</div>
										</div>
										<div class="col-sm-3">
											<div class="col-sm-6 in-tit">검증</div>
											<div class="col-sm-6">
												<input data-bind="value: verification" type="text" class="form-control" id="verification" value="50" maxlength="3" oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');">
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="modal-footer">
						<button data-bind="visible: aiDataSetId()" type="submit" type="button" class="btn btn-default">
							<i class="fas fa-pencil-alt m-r-5"></i>학습 시작
						</button>
						<button type="button" class="btn btn-gray" data-dismiss="modal" aria-label="Close">
							<i class="fas fa-times m-r-5"></i>취소
						</button>
					</div>
				</div>
			</div>
			</form>
		</div>

		<div data-bind="with: searchModelDetail" class="modal fade" id="modelDetailModal" tabindex="-1" role="dialog" aria-hidden="true" data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog modal-sm">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						<h4 class="modal-title">모델 조회</h4>
					</div>
					<div class="modal-body">
						<div class="panel-body">
							<div class="sidepanel-m-title">
								모델 정보
							</div>
							<div class="col-sm-12">
								<div class="form-group m-t-10">
									<label class="col-sm-3 control-label form-label">모델 구분</label>
									<div class="col-sm-9">
<!-- 										<select class="form-basic"></select> -->
										<select data-bind="options: modelSeCdOptions, optionsValue: 'value', value: modelSeCd, optionsText: 'text', valueAllowUnset: true, optionsCaption: '', disable: modelId()" class="form-basic"></select>
									</div>
								</div>
							</div>
							<div class="col-sm-12">
								<div class="form-group m-t-10">
									<label class="col-sm-3 control-label form-label">모델 명</label>
									<div class="col-sm-9">
										<input data-bind="value: modelNm, disable: modelId()" type="text" class="form-control">
									</div>
								</div>
							</div>
							<div class="col-sm-12">
								<div class="form-group m-t-10">
									<label class="col-sm-3 control-label form-label">알고리즘</label>
									<div class="col-sm-9">
<!-- 										<select class="form-basic"></select> -->
										<select data-bind="options: algrthNmOptions, optionsValue: 'value', optionsText: 'text', value: algrthCd, valueAllowUnset: true, optionsCaption: '', disable: modelId()" class="form-basic"></select>
									</div>
								</div>
							</div>
							<div class="col-sm-12">
								<div class="form-group m-t-10">
									<label class="col-sm-3 control-label form-label">데이터셋</label>
									<div class="col-sm-9">
										<input data-bind="value: aiDataSetNm, disable: aiDataSetId()" type="text" class="form-control">
									</div>
								</div>
							</div>
							<div class="col-sm-12">
								<div class="form-group m-t-10">
									<label class="col-sm-3 control-label form-label">영상</label>
									<div class="col-sm-9">
										<select data-bind="options: imageNmOptions, optionsValue: 'value', optionsText: 'text', value: imageNm, valueAllowUnset: true, optionsCaption: '', disable: aiDataSetId()" class="form-basic"></select>
									</div>
								</div>
							</div>
							<div class="col-sm-12">
								<div class="form-group m-t-10">
									<label class="col-sm-3 control-label form-label">진행 상태</label>
									<div class="col-sm-9">
										<select data-bind="options: progrsSttusCdOptions, optionsValue: 'value', optionsText: 'text', value: progrsSttusCd, valueAllowUnset: true, optionsCaption: '', disable: aiDataSetId()" class="form-basic"></select>
									</div>
								</div>
							</div>
							<div data-bind="visible: isShowGraph()" class="sidepanel-m-title m-t-10 b-none">
								성능 평가 그래프
							</div>
							<div data-bind="visible: isShowGraph()" class="col-sm-12">
								<div id="chartdiv2" class="graph-wrap" style="height:400px;">
<!-- 									<img src="img/test_graph.png" width="100%"> -->
								</div>
							</div>
							<div data-bind="visible: isShowGraph()" class="col-sm-12">
								<div class="form-group m-t-10">
									<label class="col-sm-12 control-label form-label">- 최종 상태 정보</label>
									<div class="col-sm-12">
										<label class="col-sm-2 control-label form-label">Epoch</label>
										<div class="col-sm-2">
											<input data-bind="value: lrnCo, disable: aiDataSetId()" type="text" class="form-control">
										</div>
										<label class="col-sm-2 control-label form-label">Accuracy</label>
										<div class="col-sm-2">
											<input data-bind="value: acccuracyPt, disable: aiDataSetId()" type="text" class="form-control">
										</div>
										<label class="col-sm-2 control-label form-label">mIoU</label>
										<div class="col-sm-2">
											<input data-bind="value: mlouPt, disable: aiDataSetId()" type="text" class="form-control">
										</div>
									</div>
									<div class="col-sm-12">
										<label class="col-sm-2 control-label form-label">Precision</label>
										<div class="col-sm-2">
											<input data-bind="value: precisionPt, disable: aiDataSetId()" type="text" class="form-control">
										</div>
										<label class="col-sm-2 control-label form-label">Recall</label>
										<div class="col-sm-2">
											<input data-bind="value: recallPt, disable: aiDataSetId()" type="text" class="form-control">
										</div>
										<div class="col-sm-6">
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-gray" data-dismiss="modal" aria-label="Close"><i class="fas fa-times m-r-5"></i>닫기</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>