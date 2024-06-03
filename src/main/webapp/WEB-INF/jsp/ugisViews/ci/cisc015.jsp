<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
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
	href="${contextPath}/css/knockout.contextmenu.css">

<script type="text/javascript" src="${contextPath}/js/jquery.min.js"></script>
<script type="text/javascript"
	src="${contextPath}/js/jquery-ui/jquery-ui.min.js"></script>
<script type="text/javascript"
	src="${contextPath}/js/jquery-ui/jquery.blockUI.js"></script>
<script type="text/javascript"
	src="${contextPath}/js/bootstrap/bootstrap.min.js"></script>
<script type="text/javascript"
	src="${contextPath}/js/knockout/knockout-3.5.1.js"></script>
<script type="text/javascript"
	src="${contextPath}/js/knockout/knockout.mapping-latest.js"></script>
<script type="text/javascript"
	src="${contextPath}/js/knockout/knockout.validation.min.js"></script>
<script type="text/javascript"
	src="${contextPath}/js/knockout/knockout.contextmenu.min.js"></script>
<script type="text/javascript"
	src="${contextPath}/js/knockout/knockout-custom.js"></script>
<script type="text/javascript"
	src="${contextPath}/js/knockout/jquery.serialize-object.min.js"></script>
<script type="text/javascript"
	src="${contextPath}/js/moment/moment.min.js"></script>
<script type="text/javascript"
	src="${contextPath}/js/knockout/knockout-date-bindings.js"></script>
<%--<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/proj4js/1.1.0/proj4js-combined.min.js"></script>--%>
<script type="text/javascript" src="js/map/proj4js-combined.js"></script>

<script src="js/cm/cmsc003/lib/proj4js/proj4.js"></script>
<!-- shp2geojson -->
<script src="js/cm/cmsc003/lib/shp2geojson/lib/jszip.js"></script>
<script src="js/cm/cmsc003/lib/shp2geojson/lib/jszip-utils.js"></script>
<script src="js/cm/cmsc003/lib/shp2geojson/preprocess.js"></script>

<script type="text/javascript" src="${contextPath}/js/map/v6.7.0/ol.js"></script>
<script type="text/javascript" src="${contextPath}/js/map/map.js"></script>
<script type="text/javascript" src="${contextPath}/js/map/gis.js"></script>
<script type="text/javascript" src="${contextPath}/js/ci/cisc_map.js"></script>
<script type="text/javascript" src="${contextPath}/js/datepicker.js"></script>
<!-- ================================================
CMSC003 JS Dev start
================================================ -->
<script src="js/cm/cmsc003/src/cmsc003.js"></script>
<script src="js/cm/cmsc003/src/gis/gis.js"></script>
<script src="js/cm/cmsc003/src/storage/storage.js"></script>
<script src="js/cm/cmsc003/src/dom/dom.js"></script>
<script src="js/cm/cmsc003/src/converter/converter.js"></script>
<script src="js/cm/cmsc003/src/util/util.js"></script>
<!-- ================================================
CMSC003 JS Dev end
================================================ -->
<script type="text/javascript">
	var contextPath = '${contextPath}';
</script>

</head>

<body>

	<c:import url="../topMenu.jsp" />

	<!-- START SIDEBAR -->
	<div class="sidebar clearfix">
		<div role="tabpanel" class="sidepanel" style="display: block;">

			<!-- Nav tabs -->
			<ul id="tab" class="nav nav-tabs" role="tablist">
				<li role="presentation" class="active" style="width: 50%;"><a
					id="objChange" href="#objChangeModel"
					aria-controls="objChangeModel" role="tab" data-toggle="tab"
					aria-expanded="true" class="active"> <i
						class="fas fa-plus-square"></i><br>객체추출 요청
				</a></li>
				<li role="presentation" class="" style="width: 50%;"><a
					id=objResult href="#objResultModel" aria-controls="objResultModel"
					role="tab" data-toggle="tab" class="" aria-expanded="false"> <i
						class="fas fa-list"></i><br>처리 결과
				</a></li>
			</ul>

			<!-- Tab panes -->
			<div class="tab-content">

				<div id="objChangeModel"
					data-bind="validationOptions: { insertMessages: false }"
					role="tabpanel" class="tab-pane active">
					<div class="sidepanel-m-title">대상영상 검색</div>
					<div class="panel-body">
						<div data-bind="with: search">
							<div class="form-group m-t-10">
								<label class="col-sm-2 control-label form-label">종류</label>
								<div class="col-sm-10">
									<label class="ckbox-container">항공영상 <input
										data-bind="checked: searchPotogrfVidoCd, value: '7,8'"
										type="checkbox" /> <span class="checkmark"></span>
									</label> <label class="ckbox-container">국토위성 <input
										data-bind="checked: searchPotogrfVidoCd, value: '00'"
										type="checkbox" /> <span class="checkmark"></span>
									</label> <label class="ckbox-container">기타위성 <input
										data-bind="checked: searchPotogrfVidoCd, value: '1,2,6'"
										type="checkbox" /> <span class="checkmark"></span>
									</label> </label> <label class="ckbox-container">드론영상 <input
										data-bind="checked: searchPotogrfVidoCd, value: '3,4,5,20'"
										type="checkbox" /> <span class="checkmark"></span>
									</label>
								</div>
							</div>
							<div class="form-group m-t-10">
								<label class="col-sm-3 control-label form-label">촬영기간</label>
								<div class="col-sm-4">
									<input
										data-bind="date: searchPotogrfBeginDt, dateFormat: 'YYYY-MM-DD'"
										id="searchPotogrfBeginDt" type="text" class="form-control">
								</div>
								<div class="col-sm-1">
									<span class="txt-in-form">~<span>
								</div>
								<div class="col-sm-4">
									<input
										data-bind="date: searchPotogrfEndDt, dateFormat: 'YYYY-MM-DD'"
										id="searchPotogrfEndDt" type="text" class="form-control">
								</div>
							</div>
							<div class="btn-wrap a-cent">
								<a data-bind="click: $parent.searchList" href="#"
									class="btn btn-default"> <i class="fas fa-search m-r-5"></i>검색
								</a>
							</div>
							<div class="sidepanel-s-title m-t-30">검색 결과</div>
							<div class="panel-body">
								<div class="col-lg-12">
									<div class="panel panel-default" style="height: 300px;">
										<dl
											data-bind="foreach: aeroList, css: { 'result-list-input' : aeroList().length > 0 }"
											style="margin-bottom: 0px;">
											<!-- ko if: message === null -->
											<dt data-bind="visible: $index() === 0">
												항공영상 (<span data-bind="text: $parent.aeroList().length">0</span>)
											</dt>
											<dd style="display: flex;">
												<input
													data-bind="checked: $parent.aeroChecked, checkedValue: $data"
													type="checkbox" style="margin-top: 3px;" /> <a
													data-bind="click: $root.viewImg" href="#"
													style="margin-left: 5px;"><span
													data-bind="text: fileNm" /></a>
											</dd>
											<!-- /ko -->
											<!-- ko if: message !== null -->
											<dt>항공영상 (0)</dt>
											<dd>
												<span data-bind="text: message" />
											</dd>
											<!-- /ko -->
										</dl>
										<dl
											data-bind="foreach: soilList, css: { 'result-list-input' : soilList().length > 0 }"
											style="margin-bottom: 0px;">
											<!-- ko if: message === null -->
											<dt data-bind="visible: $index() === 0">
												국토위성 (<span data-bind="text: $parent.soilList().length">0</span>)
											</dt>
											<dd style="display: flex;">
												<input
													data-bind="checked: $parent.soilChecked, checkedValue: $data"
													type="checkbox" style="margin-top: 3px;" /> <a
													data-bind="click: $root.viewImg" href="#"
													style="margin-left: 5px;"><span
													data-bind="text: fileNm" /></a>
											</dd>
											<!-- /ko -->
											<!-- ko if: message !== null -->
											<dt>국토위성 (0)</dt>
											<dd>
												<span data-bind="text: message" />
											</dd>
											<!-- /ko -->
										</dl>
										<dl
											data-bind="foreach: etcList, css: { 'result-list-input' : etcList().length > 0 }"
											style="margin-bottom: 0px;">
											<!-- ko if: message === null -->
											<dt data-bind="visible: $index() === 0">
												기타위성 (<span data-bind="text: $parent.etcList().length">0</span>)
											</dt>
											<dd style="display: flex;">
												<input
													data-bind="checked: $parent.etcChecked, checkedValue: $data"
													type="checkbox" style="margin-top: 3px;" /> <a
													data-bind="click: $root.viewImg" href="#"
													style="margin-left: 5px;"><span
													data-bind="text: fileNm" /></a>
											</dd>
											<!-- /ko -->
											<!-- ko if: message !== null -->
											<dt>기타위성 (0)</dt>
											<dd>
												<span data-bind="text: message" />
											</dd>
											<!-- /ko -->
										</dl>
										<dl
											data-bind="foreach: droneList, css: { 'result-list-input' : droneList().length > 0 }"
											style="margin-bottom: 0px;">
											<!-- ko if: message === null -->
											<dt data-bind="visible: $index() === 0">
												드론영상 (<span data-bind="text: $parent.droneList().length">0</span>)
											</dt>
											<dd style="display: flex;">
												<input
													data-bind="checked: $parent.etcChecked, checkedValue: $data"
													type="checkbox" style="margin-top: 3px;" /> <a
													data-bind="click: $root.viewImg" href="#"
													style="margin-left: 5px;"><span
													data-bind="text: fileNm" /></a>
											</dd>
											<!-- /ko -->
											<!-- ko if: message !== null -->
											<dt>드론영상 (0)</dt>
											<dd>
												<span data-bind="text: message" />
											</dd>
											<!-- /ko -->
										</dl>
									</div>
								</div>
							</div>
							<div class="btn-wrap a-cent">
								<a data-bind="click: $parent.newData" href="#"
									class="btn btn-default" data-toggle="modal"> <i
									class="fas fa-video m-r-5"></i>선택 영상 수행
								</a>
							</div>
						</div>

						<div data-bind="with: form" id="objChangeForm" class="modal fade"
							tabindex="-1" role="dialog" aria-hidden="true"
							data-backdrop="static" data-keyboard="false">
							<div class="modal-dialog modal-sm">
								<div class="modal-content">
									<div class="modal-header">
										<button type="button" class="close" data-dismiss="modal"
											aria-label="Close">
											<span aria-hidden="true">&times;</span>
										</button>
										<h4 class="modal-title">객체 추출 요청</h4>
									</div>
									<div class="modal-body">
										<div class="panel-body">
											<div class="sidepanel-m-title">모델</div>
											<div class="col-sm-12">
												<div class="form-group m-t-10">
													<label class="col-sm-2 control-label form-label">모델</label>
													<div class="col-sm-9">
														<input data-bind="value: modelNm, disable: true"
															type="text" class="form-control">
													</div>
													<div class="col-sm-1">
														<button data-bind="click: $parent.searchModel"
															type="button" class="btn btn-default btn-icon">
															<i class="fas fa-project-diagram"></i>
														</button>
													</div>
												</div>
											</div>
											<div class="col-sm-12">
												<div class="form-group m-t-10">
													<label class="col-sm-2 control-label form-label">알고리즘</label>
													<div class="col-sm-10">
														<select
															data-bind="options: algrthCdOptions, optionsValue: 'value', optionsText: 'text', value: algrthCd, valueAllowUnset: true, optionsCaption: '', disable: true"
															class="form-basic" style="appearance: none;"></select>
													</div>
												</div>
											</div>
											<div class="col-sm-12">
												<div class="form-group m-t-10">
													<label class="col-sm-2 control-label form-label">데이터셋</label>
													<div class="col-sm-10">
														<input data-bind="value: aiDataSetNm, disable: true"
															type="text" class="form-control">
													</div>
												</div>
											</div>
											<div class="col-sm-12">
												<div class="form-group m-t-10">
													<label class="col-sm-2 control-label form-label">클래스</label>
													<div class="col-sm-10">
														<input data-bind="value: classNm, disable: true"
															type="text" class="form-control">
													</div>
												</div>
											</div>
											<div class="col-sm-12">
												<div class="form-group m-t-10">
													<label class="col-sm-2 control-label form-label">영상</label>
													<div class="col-sm-10">
														<select
															data-bind="options: imageNmOptions, optionsValue: 'value', optionsText: 'text', value: imageNm, valueAllowUnset: true, optionsCaption: '', disable: true"
															class="form-basic" style="appearance: none;"></select>
													</div>
												</div>
											</div>
											<div class="col-sm-12">
												<label class="col-sm-12 control-label form-label">밴드
													목록</label>
												<div class="tbl-header">
													<table cellpadding="0" cellspacing="0" border="0">
														<thead>
															<tr>
																<th width="50%">밴드</th>
																<th>밴드명</th>
															</tr>
														</thead>
													</table>
												</div>
												<div class="tbl-content">
													<table cellpadding="0" cellspacing="0" border="0">
														<tbody>
															<tr>
																<td width="50%">Band 1</td>
																<td><select
																	data-bind="options: bandNm1Options, optionsValue: 'value', optionsText: 'text', value: bandNm1, valueAllowUnset: true, optionsCaption: '', disable: true"
																	class="form-basic" style="appearance: none;"></select></td>
															</tr>
															<tr>
																<td>Band 2</td>
																<td><select
																	data-bind="options: bandNm2Options, optionsValue: 'value', optionsText: 'text', value: bandNm2, valueAllowUnset: true, optionsCaption: '', disable: true"
																	class="form-basic" style="appearance: none;"></select></td>
															</tr>
															<tr>
																<td>Band 3</td>
																<td><select
																	data-bind="options: bandNm3Options, optionsValue: 'value', optionsText: 'text', value: bandNm3, valueAllowUnset: true, optionsCaption: '', disable: true"
																	class="form-basic" style="appearance: none;"></select></td>
															</tr>
															<tr>
																<td>Band 4</td>
																<td><select
																	data-bind="options: bandNm4Options, optionsValue: 'value', optionsText: 'text', value: bandNm4, valueAllowUnset: true, optionsCaption: '', disable: true"
																	class="form-basic" style="appearance: none;"></select></td>
															</tr>
														</tbody>
													</table>
												</div>
											</div>
											<div class="col-sm-12">
												<div class="form-group m-t-10">
													<label class="col-sm-3 control-label form-label">패치
														사이즈</label>
													<div class="col-sm-9">
														<select
															data-bind="options: patchSizeCdOptions, optionsValue: 'value', optionsText: 'text', value: patchSizeCd, valueAllowUnset: true, optionsCaption: '', disable: true"
															class="form-basic" style="appearance: none;"></select>
													</div>
												</div>
											</div>
											<div class="form-group m-t-10">
												<label class="col-sm-12 control-label form-label">선택
													영상 목록 (총 <span data-bind="text: vidoList().length"></span>개)
												</label>
												<div class="col-sm-12">
													<div class="panel panel-default" style="height: 100px;">
														<dl data-bind="foreach: vidoList" class="tree-dep-02">
															<dd>
																<label data-bind="text: innerFileCoursNm"
																	class="context-menu-one btn-neutral"></label>
															</dd>
														</dl>
													</div>
												</div>
											</div>
										</div>
									</div>
									<div class="modal-footer">
										<button data-bind="click: $parent.saveData" type="submit"
											class="btn btn-default">
											<i class="fas fa-eye-dropper m-r-5"></i>추출 시작
										</button>
										<button type="button" class="btn btn-gray"
											data-dismiss="modal" aria-label="Close">
											<i class="fas fa-times m-r-5"></i>취소
										</button>
									</div>
								</div>
							</div>
						</div>

						<div data-bind="with: modelForm" id="searchModel"
							class="modal fade" tabindex="-1" role="dialog" aria-hidden="true"
							data-backdrop="static" data-keyboard="false">
							<div class="modal-dialog modal-sm">
								<div class="modal-content">
									<div class="modal-header">
										<button type="button" class="close" data-dismiss="modal"
											aria-label="Close">
											<span aria-hidden="true">&times;</span>
										</button>
										<h4 class="modal-title">모델 목록</h4>
									</div>
									<div class="modal-body">
										<div class="panel panel-default" style="height: 130px;">
											<dl data-bind="foreach: modelList">
												<dd>
													<div class="radio radio-info radio-inline">
														<input
															data-bind="attr: {id: modelId}, checked: $parent.modelChecked, checkedValue: $data"
															type="radio" name="modelNm" /> <label
															data-bind="attr: {for: modelId}, text: modelNm"></label>
													</div>
												</dd>
											</dl>
										</div>
									</div>
									<div class="modal-footer">
										<button data-bind="click: $parent.modelSelect" type="button"
											class="btn btn-default">
											<i class="fas fa-check-square m-r-5"></i>확인
										</button>
										<button type="button" class="btn btn-gray"
											data-dismiss="modal" aria-label="Close">
											<i class="fas fa-times m-r-5"></i>취소
										</button>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>

				<div id="objResultModel" role="tabpanel" class="tab-pane">
					<div data-bind="with: rslt" id="confirmModel" class="modal fade"
						tabindex="-1" role="dialog" aria-hidden="true"
						data-backdrop="static" data-keyboard="false">
						<div class="modal-dialog modal-sm">
							<div class="modal-content" style="width: 320px;">
								<div class="modal-header">
									<button type="button" class="close" data-dismiss="modal"
										aria-label="Close">
										<span aria-hidden="true">&times;</span>
									</button>
									<h4 class="modal-title">추출완료</h4>
								</div>
								<div class="modal-body">
									객체 추출이 완료 되었습니다.<br />처리 결과는 처리결과(Tab) 에 추가 됩니다.
								</div>
								<div class="modal-footer">
									<button data-bind="click: $parent.resultView" type="button"
										class="btn btn-default">
										<i class="fas fa-check-square m-r-5"></i>결과보기
									</button>
									<button type="button" class="btn btn-gray" data-dismiss="modal"
										aria-label="Close">
										<i class="fas fa-times m-r-5"></i>닫기
									</button>
								</div>
							</div>
						</div>
					</div>

					<div data-bind="with: rslt" class="inside-panel panel-default">
						<div class="inside-panel-tit">
							추출 결과 래스터 목록
							<ul class="panel-tools">
								<li><a id="resultSearch"
									data-bind="click: $parent.searchRsltList"
									class="icon search-tool"><i class="fas fa-sync-alt"></i></a></li>
							</ul>
						</div>
						<div class="panel inside-panel-body" style="height: 300px;">
							<dl data-bind="foreach: resultList" class="result-list-input">
								<dd>
									<a data-bind="click: $root.viewImg" href="#"
										style="margin-left: 5px;"> <span
										data-bind="text: fileCoursNm +'/'+ allFileNm, contextMenu: $root.vectorMenu"></span>
									</a>
								</dd>
							</dl>
						</div>
					</div>

					<div data-bind="with: vec"
						class="inside-panel panel-default m-t-20" style="height: 350px;">
						<div class="inside-panel-tit">
							벡터 변환 목록
							<ul class="panel-tools">
								<li><a data-bind="click: $parent.deleteVector"
									class="icon search-tool"><i class="fas fa-trash-alt"></i></a></li>
								<li><a id="vectorSearch"
									data-bind="click: $parent.searchVectorList"
									class="icon search-tool"><i class="fas fa-sync-alt"></i></a></li>
							</ul>
						</div>
						<div class="panel inside-panel-body" style="height: 300px;">
							<dl data-bind="foreach: vectorList" class="result-list-input">
								<dd style="display: flex;">
									<input
										data-bind="checked: $parent.vectorDeleteList, checkedValue: $data"
										type="checkbox" style="margin-top: 3px;" /> <a
										data-bind="click: $root.viewVectorImg" href="#"
										style="margin-left: 5px;"><span
										data-bind="text: vectorFileNm" /></a>
								</dd>
							</dl>
						</div>
					</div>

					<!-- 벡터 변환 팝업 -->
					<div data-bind="validationOptions: { insertMessages: false }">
						<div data-bind="with: vec" class="modal fade" id="convertVector" tabindex="-1" role="dialog" aria-hidden="true" data-backdrop="static" data-keyboard="false">
							<form data-bind="submit: $parent.convertVector">
								<div class="modal-dialog">
									<div class="modal-content">
										<div class="modal-header">
											<button type="button" class="close" data-dismiss="modal" aria-label="Close">
												<span aria-hidden="true">&times;</span>
											</button>
											<h4 class="modal-title">벡터 변환</h4>
										</div>
										<div class="modal-body">
											<div class="panel-body">
												<div class="col-sm-12">
													<div class="sidepanel-m-title">벡터 변환 상세</div>
													<div class="form-group m-t-10">
														<label class="col-sm-3 control-label form-label">업샘플링 비율 (%)</label>
														<div class="col-sm-9">
															<input data-bind="value: upsamplingRate" name="upsamplingRate" type="text" class="form-control">
															<input data-bind="value: modelId" name="modelId" type="hidden">
															<input data-bind="value: aiModelId" name="aiModelId" type="hidden">
															<input data-bind="value: modelAnalsId" name="modelAnalsId" type="hidden">
															<input data-bind="value: analsVidoId" name="analsVidoId" type="hidden">
														</div>
													</div>
												</div>
											</div>
										</div>
										<div class="modal-footer">
											<button type="submit" type="button" class="btn btn-default">
												<i class="fas fa-pen m-r-5"></i>벡터변환 시작
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
			</div>
		</div>
	</div>

	<a href="#" class="sidebar-open-button"> <i class="fa fa-bars"></i>
	</a>
	<a href="#" class="sidebar-open-button-mobile"> <i
		class="fa fa-bars"></i>
	</a>

	<!-- START CONTENT -->
	<div class="content">
		<div class="map-wrap-n" id="map1" style="background-size: 100% auto"></div>
	</div>

	<script type="text/javascript"
		src="${contextPath}/js/ci/cisc015/cmmn-module.js"></script>
	<script type="text/javascript"
		src="${contextPath}/js/ci/cisc015/obj-change-module.js"></script>
	<script type="text/javascript"
		src="${contextPath}/js/ci/cisc015/obj-result-module.js"></script>

</body>
</html>