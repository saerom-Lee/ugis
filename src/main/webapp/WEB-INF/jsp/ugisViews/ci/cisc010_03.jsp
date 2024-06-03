<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div id="prfomncEvlModel" role="tabpanel" class="tab-pane">

	<div class="sidepanel-m-title">모델 & AI 데이터셋 검색</div>
	<div class="panel-body">
    	<div data-bind="with: search">

	   		<!-- 검색 체크박스 공통 -->
			<%@ include file="cisc010_chkbox.jsp" %>

			<div class="btn-wrap a-cent">
				<a data-bind="click: $parent.searchList" id="searchPrfomncEvlList" href="#" class="btn btn-default">
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
									<input data-bind="value: modelId, attr: {'id': 'modelIdEvl' + modelId}, click: $parent.clickModelId" type="radio" name="modelIdEvl">
									<label data-bind="text: modelNm, attr: {'for': 'modelIdEvl' + modelId}"></label>
								</div>
								<a data-bind="click: $root.searchDetail" href="#" class="btn btn-xs f-right" data-toggle="modal" data-target="#modelDetailEvlModal">
<!-- 								<a href="#" class="btn btn-xs f-right" data-toggle="modal" data-target="#modelDetailEvlModal"> -->
									<i class="fas fa-search"></i>
								</a>
							</dd>
						</dl>
						<div class="in-panel-title">사용자 모델 목록</div>
						<dl data-bind="foreach: userModelList" class="result-list-input">
							<dd>
								<div class="radio radio-info radio-inline">
									<input data-bind="value: modelId, attr: {'id': 'modelIdEvl' + modelId}, click: $parent.clickModelId" type="radio" name="modelIdEvl">
									<label data-bind="text: modelNm, attr: {'for': 'modelIdEvl' + modelId}"></label>
								</div>
								<a data-bind="click: $root.searchDetail" href="#" class="btn btn-xs f-right" data-toggle="modal" data-target="#modelDetailEvlModal">
<!-- 								<a href="#" class="btn btn-xs f-right" data-toggle="modal" data-target="#modelDetailEvlModal"> -->
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
									<input data-bind="value: aiDataSetId, attr: {'id': 'aiDataSetIdEvl' + aiDataSetId}, click: $parent.clickAiDataSetId" type="radio" name="aiDataSetIdEvl">
									<label data-bind="text: aiDataSetNm, attr: {'for': 'aiDataSetIdEvl' + aiDataSetId}"></label>
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
			<a data-bind="click: newData" href="#" class="btn btn-default" data-toggle="modal">
				<i class="fas fa-pen m-r-5"></i>성능평가 수행
			</a>
		</div>

		<div data-bind="with: form" class="modal fade" id="prfomncEvlForm" tabindex="-1" role="dialog" aria-hidden="true" data-backdrop="static" data-keyboard="false">
			<form data-bind="submit: $parent.saveData">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h4 class="modal-title">성능 평가</h4>
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
										<input data-bind="value: aiModelId" name="aiModelId" type="hidden">
										<input data-bind="value: modelNm" name="modelPrfomncEvlNm" type="hidden">
										<input data-bind="value: modelSeCd" type="hidden">
										<input data-bind="value: progrsSttusCd" name="progrsSttusCd" type="hidden">
									</div>
								</div>
								<div class="form-group m-t-10">
									<label class="col-sm-3 control-label form-label">알고리즘</label>
									<div class="col-sm-9">
										<select data-bind="options: algrthNmOptions, optionsValue: 'value', optionsText: 'text', value: algrthCd, valueAllowUnset: true, optionsCaption: '', disable: modelId()" class="form-basic"></select>
									</div>
								</div>
								<div class="form-group m-t-10">
									<label class="col-sm-3 control-label form-label">데이터셋</label>
									<div class="col-sm-9">
										<input data-bind="value: aiDataSetNmModel, disable: aiDataSetId()" type="text" class="form-control">
									</div>
								</div>
<!-- 								<div class="form-group m-t-10"> -->
<!-- 									<label class="col-sm-3 control-label form-label">클래스</label> -->
<!-- 									<div class="col-sm-9"> -->
<!-- 										<input type="text" class="form-control"> -->
<!-- 									</div> -->
<!-- 								</div> -->
								<div class="form-group m-t-10">
									<label class="col-sm-3 control-label form-label">영상</label>
									<div class="col-sm-9">
										<select name="imageNm" data-bind="options: imageNmOptionsModel, optionsValue: 'value', optionsText: 'text', value: imageNmModel, valueAllowUnset: true, optionsCaption: '', disable: aiDataSetId()" class="form-basic"></select>
										<input data-bind="numeric, value: rsoltnValueModel, disable: aiDataSetId()" type="hidden">
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
													<select name="bandNm1" data-bind="options: bandNm1OptionsModel, optionsValue: 'value', optionsText: 'text', value: bandNm1Model, valueAllowUnset: true, optionsCaption: '', disable: aiDataSetId()" class="form-basic"></select>
												</td>
											</tr>
											<tr>
												<td>Band 2</td>
												<td>
													<select name="bandNm2" data-bind="options: bandNm2OptionsModel, optionsValue: 'value', optionsText: 'text', value: bandNm2Model, valueAllowUnset: true, optionsCaption: '', disable: aiDataSetId()" class="form-basic"></select>
												</td>
											</tr>
											<tr>
												<td>Band 3</td>
												<td>
													<select name="bandNm3" data-bind="options: bandNm3OptionsModel, optionsValue: 'value', optionsText: 'text', value: bandNm3Model, valueAllowUnset: true, optionsCaption: '', disable: aiDataSetId()" class="form-basic"></select>
												</td>
											</tr>
											<tr>
												<td>Band 4</td>
												<td>
													<select name="bandNm4" data-bind="options: bandNm4OptionsModel, optionsValue: 'value', optionsText: 'text', value: bandNm4Model, valueAllowUnset: true, optionsCaption: '', disable: aiDataSetId()" class="form-basic"></select>
												</td>
											</tr>
										</tbody>
									</table>
								</div>
								<div class="form-group m-t-10">
									<label class="col-sm-3 control-label form-label">패치 사이즈</label>
									<div class="col-sm-9">
										<select data-bind="options: patchSizeOptions, optionsValue: 'value', optionsText: 'text', valueAllowUnset: true, optionsCaption: '', value: patchSizeCd, disable: aiDataSetId()" name="patchSizeCd" class="form-basic"></select>
									</div>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="sidepanel-m-title">선택된 평가용 데이터셋 정보</div>
								<div class="form-group m-t-10">
									<label class="col-sm-3 control-label form-label">데이터셋</label>
									<div class="col-sm-9">
										<input data-bind="value: aiDataSetNm, disable: aiDataSetId()" type="text" class="form-control">
										<input data-bind="value: aiDataSetId" name="aiDataSetId" type="hidden">
									</div>
								</div>
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
						</div>
					</div>
					<div class="modal-footer">
						<button data-bind="visible: aiDataSetId()" type="submit" type="button" class="btn btn-default">
							<i class="fas fa-pen m-r-5"></i>성능평가 시작
						</button>
						<button type="button" class="btn btn-gray" data-dismiss="modal" aria-label="Close">
							<i class="fas fa-times m-r-5"></i>취소
						</button>
					</div>
				</div>
			</div>
			</form>
		</div>
	</div>
</div>