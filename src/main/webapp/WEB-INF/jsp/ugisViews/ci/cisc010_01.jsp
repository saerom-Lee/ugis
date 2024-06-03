<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- Start dataSetModel -->
<div id="dataSetModel" role="tabpanel" class="tab-pane active">

	<div class="sidepanel-m-title">데이터셋 검색</div>
	<div class="panel-body">
    	<div data-bind="with: search">

    		<!-- 검색 체크박스 공통 -->
			<%@ include file="cisc010_chkbox.jsp" %>

			<div class="form-group m-t-10">
				<label for="" class="col-sm-2 control-label form-label">이름</label>
				<div class="col-sm-10">
					<input data-bind="value: searchAiDataSetNm" type="text" class="form-control">
				</div>
			</div>
			<div class="btn-wrap a-cent">
				<a id="searchDataSet" data-bind="click: $parent.searchList" href="#" class="btn btn-default">
					<i class="fas fa-search m-r-5"></i>검색
				</a>
			</div>
			<div class="sidepanel-s-title m-t-30">검색 결과</div>
			<div class="panel-body">
				<div class="col-lg-12">
					<div class="panel panel-default" style="height: 300px;">
						<dl data-bind="foreach: dataSetList" class="result-list-input">
							<dd style="min-height:25px;">
								<span data-bind="text: aiDataSetNm"></span>
								<a data-bind="click: $root.searchDetail" href="#" class="btn btn-xs f-right" data-toggle="modal">
									<i class="fas fa-search"></i>
								</a>
							</dd>
						</dl>
					</div>
				</div>
			</div>
		</div>

		<div data-bind="validationOptions: { insertMessages: false }" class="btn-wrap a-cent">
			<a data-bind="click: newData" href="#" class="btn btn-default" data-toggle="modal">
				<i class="fas fa-plus-square m-r-5"></i>AI 데이터셋 신규 등록
			</a>
			<div data-bind="with: form" id="dataSetForm" class="modal fade" tabindex="-1" role="dialog" aria-hidden="true" data-backdrop="static" data-keyboard="false">
		    <form data-bind="submit: $parent.saveData">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal" aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
							<h4 class="modal-title">데이터셋 등록</h4>
						</div>
						<div class="modal-body">
							<div class="panel-body">
								<div class="sidepanel-m-title">데이터셋 상세</div>
								<div class="col-sm-12">
									<div class="form-group m-t-10">
										<label class="col-sm-2 control-label form-label">이름</label>
										<div class="col-sm-10">
											<input name="aiDataSetNm" data-bind="value: aiDataSetNm, disable: aiDataSetId()" type="text" class="form-control" maxlength="25">
										</div>
									</div>
								</div>
								<div class="col-sm-6 p-r-10">
									<div class="form-group m-t-10">
										<label class="col-sm-12 control-label form-label">
											선택 영상 목록 (총 <span data-bind="text: imageDataCount">0</span>개)
											<label data-bind="visible: !aiDataSetId()" class="btn btn-light btn-xs f-right" for="uploadImageData">
                                            	<i class="fas fa-file-import m-r-5"></i>불러오기
                                            </label>
                                            <input name="uploadFile" type="file" id="uploadImageData" data-bind="event:{change: $parent.imageFileChange}" onclick="this.value=null;" style="display:none;"/>
										</label>
										<div class="col-sm-12">
											<div class="panel panel-default" style="height: 100px;">
												<div class="acidjs-css3-treeview">
												    <ul data-bind="foreach: imageDataList">
												        <li><span data-bind="text: fileNm"></span></li>
												    </ul>
												</div>
											</div>
										</div>
									</div>
									<p class="notice-sm" style="text-align: left; marin: 5px 0;">* 영상 삭제 시, 동일한 이름의 라벨링 데이터도 자동 삭제됩니다.</p>
									<div class="form-group m-t-10">
										<label class="col-sm-2 control-label form-label">영상</label>
										<div class="col-sm-6">
											<select name="imageNm" data-bind="options: imageNmOptions, optionsValue: 'value', optionsText: 'text', value: imageNm, valueAllowUnset: true, optionsCaption: '', disable: aiDataSetId(), event:{change: $parent.imageNmChange}" class="form-basic"></select>
										</div>
<!-- 										<div class="col-sm-3"> -->
<!-- 											<input name="rsoltnValue" data-bind="numeric, value: rsoltnValue, disable: aiDataSetId()" type="text" class="form-control" maxlength="5"> -->
<!-- 										</div> -->
									</div>
									<label class="col-sm-12 control-label form-label">밴드 목록</label>
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
									<div class="tbl-content" style="height: 90px;">
										<table cellpadding="0" cellspacing="0" border="0">
											<tbody>
												<tr>
													<td width="50%">Band 1</td>
													<td>
														<select name="bandNm1" data-bind="options: bandNm1Options, optionsValue: 'value', optionsText: 'text', value: bandNm1, valueAllowUnset: true, optionsCaption: ''" class="form-basic" style="appearance:none;"></select>
													</td>
												</tr>
												<tr>
													<td>Band 2</td>
													<td>
														<select name="bandNm2" data-bind="options: bandNm2Options, optionsValue: 'value', optionsText: 'text', value: bandNm2, valueAllowUnset: true, optionsCaption: '', disable: true" class="form-basic" style="appearance:none;"></select>
													</td>
												</tr>
												<tr>
													<td>Band 3</td>
													<td>
														<select name="bandNm3" data-bind="options: bandNm3Options, optionsValue: 'value', optionsText: 'text', value: bandNm3, valueAllowUnset: true, optionsCaption: '', disable: true" class="form-basic" style="appearance:none;"></select>
													</td>
												</tr>
												<tr>
                                                    <td>Band 4</td>
                                                    <td>
                                                        <select name="bandNm4" data-bind="options: bandNm4Options, optionsValue: 'value', optionsText: 'text', value: bandNm4, valueAllowUnset: true, optionsCaption: '', disable: true" class="form-basic" style="appearance:none;"></select>
                                                    </td>
                                                </tr>
											</tbody>
										</table>
									</div>
								</div>
								<div class="col-sm-6">
								    <div class="form-group m-t-10">
                                        <label class="col-sm-12 control-label form-label">
                                                                                                                                      선택 라벨링 데이터 목록(총 <span data-bind="text: lblDataCount">0</span>개)
                                            <label data-bind="visible: !aiDataSetId()" class="btn btn-light btn-xs f-right" for="uploadLblData">
                                                <i class="fas fa-file-import m-r-5"></i>불러오기
                                            </label>
                                            <input name="uploadFile" type="file" id="uploadLblData" data-bind="event:{change: $parent.lblFileChange}" onclick="this.value=null;" style="display:none;"/>
                                        </label>
                                        <div class="col-sm-12">
                                            <div class="panel panel-default" style="height: 100px;">
                                                <div class="acidjs-css3-treeview">
                                                    <ul data-bind="foreach: lblDataList">
                                                        <li><span data-bind="text: fileNm"></span></li>
                                                    </ul>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
								</div>
							</div>
						</div>
						<div class="modal-footer">
							<button type="submit" class="btn btn-default" data-bind="visible: !aiDataSetId()">
								<i class="fas fa-save m-r-5"></i>저장
							</button>
							<button class="btn btn-default" data-bind="click: $parent.deleteData, visible: aiDataSetId()">
                                <i class="fas fa-trash-alt m-r-5"></i>삭제
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