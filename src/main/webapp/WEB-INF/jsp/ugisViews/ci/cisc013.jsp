<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html lang="ko" data-dark="false">
<head>
<meta charset="utf-8">
<title></title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=1190">
<meta name="apple-mobile-web-app-title" content="NAVER"/>
<meta name="robots" content="index,nofollow"/>
<meta name="description" content=""/>
<meta property="og:title" content="">
<meta property="og:url" content="">
<meta property="og:image" content="">
<meta property="og:description" content=""/>
<meta name="twitter:card" content="summary">
<meta name="twitter:title" content="">
<meta name="twitter:url" content="">
<meta name="twitter:image" content="">
<meta name="twitter:description" content=""/> 
</head>

<!-- ========== Css Files ========== -->
<link href="css/root.css" rel="stylesheet">

<link rel="stylesheet" href="https://pro.fontawesome.com/releases/v5.10.0/css/all.css" integrity="sha384-AYmEC3Yw5cVb3ZcuHtOA93w35dYTsvhLPVnYs9eStHfGJvOvKxVfELGroGkvsg+p" crossorigin="anonymous"/>
<script type="text/javascript">
$(document).ready(function() {
	
});
</script>

<!-- jquery-contextmenu -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jquery-contextmenu/2.9.2/jquery.contextMenu.css" integrity="sha512-EF5k2tHv4ShZB7zESroCVlbLaZq2n8t1i8mr32tgX0cyoHc3GfxuP7IoT8w/pD+vyoq7ye//qkFEqQao7Ofrag==" crossorigin="anonymous" referrerpolicy="no-referrer" />
<!-- jquery -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-contextmenu/2.9.2/jquery.contextMenu.js" integrity="sha512-2ABKLSEpFs5+UK1Ol+CgAVuqwBCHBA0Im0w4oRCflK/n8PUVbSv5IY7WrKIxMynss9EKLVOn1HZ8U/H2ckimWg==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-contextmenu/2.9.2/jquery.ui.position.js" integrity="sha512-vBR2rismjmjzdH54bB2Gx+xSe/17U0iHpJ1gkyucuqlTeq+Q8zwL8aJDIfhQtnWMVbEKMzF00pmFjc9IPjzR7w==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
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
			<li role="presentation" class="" style="width:25%;">
				<a href="#tab-01" aria-controls="tab-01" role="tab" data-toggle="tab" aria-expanded="false" class="">
					<i class="fas fa-plus-square"></i><br>AI 데이터셋
				</a>
			</li>
			<li role="presentation" class="" style="width:25%;">
				<a href="#tab-03" aria-controls="tab-03" role="tab" data-toggle="tab" class="00" aria-expanded="false">
					<i class="fas fa-pencil"></i><br>AI 학습
				</a>
			</li>
			<li role="presentation" class="active" style="width:25%;">
				<a href="#tab-04" aria-controls="tab-04" role="tab" data-toggle="tab" class="active" aria-expanded="true">
					<i class="fas fa-check-square"></i><br>성능평가
				</a>
			</li>
			<li role="presentation" class="" style="width:25%;">
				<a href="#tab-05" aria-controls="tab-05" role="tab" data-toggle="tab" class="" aria-expanded="false">
					<i class="fas fa-clipboard-list"></i><br>성능평가결과
				</a>
			</li>
		</ul>

		<!-- Tab panes -->
		<div class="tab-content">

			<!-- Start tab-01 -->
			<div role="tabpanel" class="tab-pane" id="tab-01">
				<div class="tabs">
					<input id="all" type="radio" name="tab_item" checked>
					<label class="tab_item" for="all">AI 데이터셋 조회</label>
					<input id="programming" type="radio" name="tab_item">
					<label class="tab_item" for="programming">AI 데이터셋 신규등록</label>
					<div class="tab_content" id="all_content">

						<div class="sidepanel-m-title">
							데이터셋 검색
						</div>
						<div class="panel-body">
							<div class="form-group m-t-10">
								<label for="input002" class="col-sm-2 control-label form-label">종류</label>
								<div class="col-sm-10">
									<label class="ckbox-container">항공영상
										<input type="checkbox" checked="checked">
										<span class="checkmark"></span>
									</label>
									<label class="ckbox-container">국토위성
										<input type="checkbox">
										<span class="checkmark"></span>
									</label>
									<label class="ckbox-container">기타위성
										<input type="checkbox">
										<span class="checkmark"></span>
									</label>
								</div>
							</div>	
							<div class="form-group m-t-10">
								<label for="input002" class="col-sm-2 control-label form-label">클래스</label>
								<div class="col-sm-10">
									<label class="ckbox-container">건물
										<input type="checkbox" checked="checked">
										<span class="checkmark"></span>
									</label>
									<label class="ckbox-container">주차장
										<input type="checkbox">
										<span class="checkmark"></span>
									</label>
									<label class="ckbox-container">도로
										<input type="checkbox">
										<span class="checkmark"></span>
									</label>
									<label class="ckbox-container">가로수
										<input type="checkbox">
										<span class="checkmark"></span>
									</label>
									<label class="ckbox-container">논
										<input type="checkbox">
										<span class="checkmark"></span>
									</label>
									<label class="ckbox-container">밭
										<input type="checkbox">
										<span class="checkmark"></span>
									</label>
									<label class="ckbox-container">산림
										<input type="checkbox">
										<span class="checkmark"></span>
									</label>
									<label class="ckbox-container">나지
										<input type="checkbox">
										<span class="checkmark"></span>
									</label>
								</div>
							</div>	
							<div class="form-group m-t-10">
								<label for="" class="col-sm-2 control-label form-label">이름</label>
								<div class="col-sm-10">
									<input type="text" class="form-control">
								</div>
							</div>
							<div class="btn-wrap a-cent">
								<a href="#" class="btn btn-default"><i class="fas fa-search m-r-5"></i>검색</a>
							</div>
							<div class="sidepanel-s-title m-t-30">
							검색 결과
							</div>
							<div class="panel-body">
								<div class="col-lg-12">
									<div class="panel panel-default" style="height:300px;">
										<dl class="result-list-input">
											<dt>항공사진 데이터셋 (3)</dt>
											<dd>
												항공_데이터셋_건물_xxxxxxx001
												<a href="#" class="btn btn-xs f-right" data-toggle="modal" data-target="#myModal3"><i class="fas fa-search"></i></a>
											</dd>
											<dd>
												항공_데이터셋_건물_xxxxxxx002
												<a href="#" class="btn btn-xs f-right" data-toggle="modal" data-target="#myModal3"><i class="fas fa-search"></i></a>
											</dd>
											<dd>
												항공_데이터셋_도로_xxxxxxx001
												<a href="#" class="btn btn-xs f-right" data-toggle="modal" data-target="#myModal3"><i class="fas fa-search"></i></a>
											</dd>
										</dl>
										<dl class="result-list-input">
											<dt>국토위성 데이터셋 (4)</dt>
											<dd>
												K3A_ 데이터셋_건물_xxxxxxx001
												<a href="#" class="btn btn-xs f-right" data-toggle="modal" data-target="#myModal3"><i class="fas fa-search"></i></a>
											</dd>
											<dd>
												K3A _ 데이터셋_건물_xxxxxxx002
												<a href="#" class="btn btn-xs f-right" data-toggle="modal" data-target="#myModal3"><i class="fas fa-search"></i></a>
											</dd>
											<dd>
												K3A _ 데이터셋_건물_xxxxxxx003
												<a href="#" class="btn btn-xs f-right" data-toggle="modal" data-target="#myModal3"><i class="fas fa-search"></i></a>
											</dd>
											<dd>
												XXXXXXX(데이터셋 이름 노출)
												<a href="#" class="btn btn-xs f-right" data-toggle="modal" data-target="#myModal3"><i class="fas fa-search"></i></a>
											</dd>
										</dl>
									</div>
								</div>
							</div>
							<div class="modal fade" id="myModal3" tabindex="-1" role="dialog" aria-hidden="true">
								<div class="modal-dialog">
									<div class="modal-content">
										<div class="modal-header">
											<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
											<h4 class="modal-title">데이터셋 조회</h4>
										</div>
										<div class="modal-body">
											<div class="panel-body">	
												<div class="sidepanel-m-title">
													데이터셋 상세
												</div>	
												<div class="col-sm-12">	
													<div class="form-group m-t-10">
														<label class="col-sm-2 control-label form-label">이름</label>
														<div class="col-sm-10">
															<input type="text" class="form-control"> 
														</div>
													</div>
												</div>
												<div class="col-sm-12">	
													<div class="form-group m-t-10">
														<label class="col-sm-2 control-label form-label">클래스</label>
														<div class="col-sm-10">
															<input type="text" class="form-control"> 
														</div>
													</div>
												</div>
												<div class="col-sm-6 p-r-10">
													<div class="form-group m-t-10">
														<label class="col-sm-12 control-label form-label">
															선택 영상 목록 (총 3개)
														</label>
														<div class="col-sm-12">
															<div class="panel panel-default" style="height:100px;">
																<ul class="pop-result-list">
																	<li>
																		<input type="checkbox" id="map-001" name="vehicle1" value="map-001">
																		<label for="map-001">수치지도_001</label>
																		<a href="" class="btn-list f-right"><i class="far fa-info-circle"></i></a>
																	</li>
																	<li>
																		<input type="checkbox" id="map-001" name="vehicle1" value="map-001">
																		<label for="map-001">수치지도_001</label>
																		<a href="" class="btn-list f-right"><i class="far fa-info-circle"></i></a>
																	</li>
																	<li>
																		<input type="checkbox" id="map-001" name="vehicle1" value="map-001">
																		<label for="map-001">수치지도_001</label>
																		<a href="" class="btn-list f-right"><i class="far fa-info-circle"></i></a>
																	</li>
																</ul>

															</div>
														</div>
													</div>
												</div>
												<div class="col-sm-6">
													<div class="form-group m-t-10">
														<label class="col-sm-12 control-label form-label">선택 라벨링 데이터 목록 (총 3개)</label>
														<div class="col-sm-12">
															<div class="panel panel-default" style="height:100px;">
																<ul class="pop-result-list">
																	<li>벡터 레이어 이름1<a href="" class="btn-list f-right"><i class="far fa-info-circle"></i></a></li>
																	<li>벡터 레이어 이름2<a href="" class="btn-list f-right"><i class="far fa-info-circle"></i></a></li>
																	<li>벡터 레이어 이름3<a href="" class="btn-list f-right"><i class="far fa-info-circle"></i></a></li>
																</ul>

															</div>
														</div>
													</div>
												</div>
												<div class="col-sm-6 p-r-10">					
													<div class="form-group m-t-10">
														<label class="col-sm-4 control-label form-label">영상/해상도</label>
														<div class="col-sm-4">
															<input type="text" class="form-control"> 
														</div>
														<div class="col-sm-3">
															<input type="text" class="form-control"> 
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
															<td>Blue</td>
															</tr>
															<tr>
															<td>Band 2</td>
															<td>Green</td>
															</tr>
															<tr>
															<td>Band 3</td>
															<td>Red</td>
															</tr>
															<tr>
															<td>Band 4</td>
															<td>NIR</td>
															</tr>
															<tr>
															<td>Band 5</td>
															<td></td>
															</tr>
															<tr>
															<td>Band 6</td>
															<td></td>
															</tr>
															<tr>
															<td>Band 7</td>
															<td></td>
															</tr>
															<tr>
															<td>Band 8</td>
															<td></td>
															</tr>
															</tbody>
														</table>
													</div>
												</div>
												<div class="col-sm-6">	
													<div class="form-group m-t-10">
														<label class="col-sm-12 control-label form-label">벡터 추출 조건문 입력</label>
														<div class="col-sm-6">
															<div class="panel panel-default" style="height:130px;">
																<ul class="pop-result-list">
																	<li>OGC_FID (INTEGER)</li>
																	<li>OSM_ID5 (TEXT)</li>
																	<li>ANN_NM (TEXT)</li>
																	<li>ANN_CD (TEXT)</li>
																</ul>
															</div>
														</div>
														<div class="col-sm-6">
															<div class="col-sm-3">
																<button type="button" class="keypad">&#61;</button>
															</div>
															<div class="col-sm-3">
																<button type="button" class="keypad">&#60;&#62;</button>
															</div>
															<div class="col-sm-3">
																<button type="button" class="keypad">&#60;</button>
															</div>
															<div class="col-sm-3">
																<button type="button" class="keypad">&#62;</button>
															</div>
															<div class="col-sm-3">
																<button type="button" class="keypad">&#62;&#61;</button>
															</div>
															<div class="col-sm-3">
																<button type="button" class="keypad">&#60;&#61;</button>
															</div>
															<div class="col-sm-3">
																<button type="button" class="keypad">&#95;</button>
															</div>
															<div class="col-sm-3">
																<button type="button" class="keypad">&#37;</button>
															</div>
															<div class="col-sm-4">
																<button type="button" class="keypad">&#40;&#41;</button>
															</div>
															<div class="col-sm-4">
																<button type="button" class="keypad">is</button>
															</div>
															<div class="col-sm-4">
																<button type="button" class="keypad">Like</button>
															</div>
															<div class="col-sm-4">
																<button type="button" class="keypad">And</button>
															</div>
															<div class="col-sm-4">
																<button type="button" class="keypad">Or</button>
															</div>
															<div class="col-sm-4">
																<button type="button" class="keypad">Not</button>
															</div>
														</div>
													</div>
													<div class="form-group m-t-10">
														<label class="col-sm-12 control-label form-label">SELECT*FROM 벡터 레이어 이름1 WHERE</label>
														<div class="col-sm-12">
															<textarea class="form-control" rows="2" id="textarea1" placeholder="ANN_CD = ‘10’"></textarea>
														</div>
													</div>
												</div>
												</form> 

											</div>
										</div>
										<div class="modal-footer">
											<button type="button" class="btn btn-gray" data-dismiss="modal" aria-label="Close"><i class="fas fa-times m-r-5"></i>삭제</button>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="tab_content" id="programming_content">
						<div class="sidepanel-m-title">
							영상 & 라벨링 데이터 검색
						</div>
						<div class="panel-body">
							<div class="form-group m-t-10">
								<label for="input002" class="col-sm-2 control-label form-label">종류</label>
								<div class="col-sm-10">
									<label class="ckbox-container">항공영상
										<input type="checkbox" checked="checked">
										<span class="checkmark"></span>
									</label>
									<label class="ckbox-container">국토위성
										<input type="checkbox">
										<span class="checkmark"></span>
									</label>
									<label class="ckbox-container">기타위성
										<input type="checkbox">
										<span class="checkmark"></span>
									</label>
								</div>
							</div>			
							<div class="form-group m-t-10">
								<label for="input002" class="col-sm-3 control-label form-label">기간</label>
								<div class="col-sm-4">
									<input type="text" id="datepicker" class="form-control">
								</div>
								<div class="col-sm-1"><span class="txt-in-form">~<span></div>
								<div class="col-sm-4">
									<input type="text" id="datepicker2" class="form-control">
								</div>
							</div>
							<script src="js/datepicker.js"></script>
							<script>
								let datepicker = new DatePicker(document.getElementById('datepicker'));
								let datepicker2 = new DatePicker(document.getElementById('datepicker2'));
							</script>
							<div class="form-group m-t-10">
								<label for="input002" class="col-sm-3 control-label form-label">ID 명</label>
								<div class="col-sm-9">
									<input type="text" class="form-control">
								</div>
							</div>
							<div class="btn-wrap a-cent">
								<a href="#" class="btn btn-default"><i class="fas fa-search m-r-5"></i>검색</a>
							</div>
							<div class="sidepanel-s-title m-t-30">
							검색 결과
							</div>
							<div class="panel-body">
								<div class="col-lg-12">
									<div class="panel panel-default" style="height:150px;">
										<div class="in-panel-title">
										영상 목록
										</div>
										<dl class="result-list-input">
											<dt>항공사진 데이터 (2)</dt>
											<dd>
												<input type="checkbox" id="map-001" name="vehicle1" value="map-001">
												<label for="map-001">LC_AP_36702057_001</label>
											</dd>
											<dd>
												<input type="checkbox" id="map-001" name="vehicle1" value="map-001">
												<label for="map-001">LC_AP_36702057_002</label>
											</dd>
										</dl>
										<dl class="result-list-input">
											<dt>국토위성영상데이터 (2)</dt>
											<dd>
												<input type="checkbox" id="map-001" name="vehicle1" value="map-001">
												<label for="map-001">LC_SN_36702057_001</label>
											</dd>
											<dd>
												<input type="checkbox" id="map-001" name="vehicle1" value="map-001">
												<label for="map-001">LC_SN_36702057_002</label>
											</dd>
										</dl>


									</div>
								</div>
							</div>
							<div class="panel-body m-t-10">
								<div class="col-lg-12">
									<div class="panel panel-default" style="height:150px;">
										<div class="in-panel-title">
										라벨링 데이터 목록<br><span class="txt-sm">* 영상 선택 시, 동일한 이름의 라벨링 데이터가 자동 선택됩니다.</span>

										</div>
										<dl class="result-list-input">
											<dt>항공사진 데이터 (3)</dt>
											<dd>
												<input type="checkbox" id="map-001" name="vehicle1" value="map-001">
												<label for="map-001">LC_AP_36702057_001</label>
											</dd>
											<dd>
												<input type="checkbox" id="map-001" name="vehicle1" value="map-001">
												<label for="map-001">LC_AP_36702057_002</label>
											</dd>
										</dl>
										<dl class="result-list-input">
											<dt>국토위성영상데이터 (2)</dt>
											<dd>
												<input type="checkbox" id="map-001" name="vehicle1" value="map-001">
												<label for="map-001">LC_SN_36702057_001</label>
											</dd>
											<dd>
												<input type="checkbox" id="map-001" name="vehicle1" value="map-001">
												<label for="map-001">LC_SN_36702057_002</label>
											</dd>
										</dl>


									</div>
								</div>

							</div>

							<div class="btn-wrap a-cent">
								<a href="#" class="btn btn-default" data-toggle="modal" data-target="#myModal7"><i class="fas fa-plus-square m-r-5"></i>선택 자료 데이터셋 등록</a>
							</div>

							<div class="modal fade" id="myModal7" tabindex="-1" role="dialog" aria-hidden="true">
								<div class="modal-dialog">
									<div class="modal-content">
										<div class="modal-header">
											<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
											<h4 class="modal-title">데이터셋 등록</h4>
										</div>
										<div class="modal-body">
											<div class="panel-body">	
												<div class="sidepanel-m-title">
													데이터셋 상세
												</div>	
												<div class="col-sm-12">	
													<div class="form-group m-t-10">
														<label class="col-sm-2 control-label form-label">이름</label>
														<div class="col-sm-10">
															<input type="text" class="form-control"> 
														</div>
													</div>
												</div>
												<div class="col-sm-12">	
													<div class="form-group m-t-10">
														<label class="col-sm-2 control-label form-label">클래스</label>
														<div class="col-sm-10">
															<input type="text" class="form-control"> 
														</div>
													</div>
												</div>
												<div class="col-sm-6 p-r-10">
													<div class="form-group m-t-10">
														<label class="col-sm-12 control-label form-label">
															선택 영상 목록 (총 3개)
															<button type="button" class="btn btn-light btn-xs f-right"><i class="fas fa-trash-alt m-r-5"></i>삭제</button>
														</label>
														<div class="col-sm-12">
															<div class="panel panel-default" style="height:100px;">
					 
																<div class="acidjs-css3-treeview">
																	<ul class="dep-01">
																		<li>
																			<input type="checkbox" id="node-0" checked="checked" /><label><input type="checkbox" /><span></span></label><label for="node-0">영상</label>
																			<ul class="dep-02">
																				<li>
																					<input type="checkbox" id="node-0-0"/>
																					<label><input type="checkbox" /><span></span></label>
																					<label for="node-0-0">영상 ID 1</label>
																					<a href="" class="btn-list f-right"><i class="far fa-info-circle"></i></a>
																				</li>
																				<li>
																					<input type="checkbox" id="node-0-1" />
																					<label><input type="checkbox" /><span></span></label>
																					<label for="node-0-1">영상 ID 2</label>
																					<a href="" class="btn-list f-right"><i class="far fa-info-circle"></i></a>
																				</li>
																				<li>
																					<input type="checkbox" id="node-0-2" />
																					<label><input type="checkbox" /><span></span></label>
																					<label for="node-0-2">영상 ID 3</label>
																					<a href="" class="btn-list f-right"><i class="far fa-info-circle"></i></a>
																				</li>
																			</ul>
																		</li>
																	</ul>
																</div>

																<script>
																$(".acidjs-css3-treeview").delegate("label input:checkbox", "change", function() {
																	var
																		checkbox = $(this),
																		nestedList = checkbox.parent().next().next(),
																		selectNestedListCheckbox = nestedList.find("label:not([for]) input:checkbox");
																 
																	if(checkbox.is(":checked")) {
																		return selectNestedListCheckbox.prop("checked", true);
																	}
																	selectNestedListCheckbox.prop("checked", false);
																});
																</script>

															</div>
														</div>
													</div>
												</div>
												<div class="col-sm-6">
													<div class="form-group m-t-10">
														<label class="col-sm-12 control-label form-label">선택 라벨링 데이터 목록 (총 3개)</label>
														<div class="col-sm-12">
															<div class="panel panel-default" style="height:100px;">
																<ul class="pop-result-list">
																	<li>벡터 레이어 이름1<a href="" class="btn-list f-right"><i class="far fa-info-circle"></i></a></li>
																	<li>벡터 레이어 이름2<a href="" class="btn-list f-right"><i class="far fa-info-circle"></i></a></li>
																	<li>벡터 레이어 이름3<a href="" class="btn-list f-right"><i class="far fa-info-circle"></i></a></li>
																</ul>

															</div>
														</div>
													</div>
												</div>
												<div class="col-sm-12">										
													<p class="notice-sm">* 영상 삭제 시, 동일한 이름의 라벨링 데이터도 자동 삭제됩니다.</p>
												</div>
												<div class="col-sm-6 p-r-10">					
													<div class="form-group m-t-10">
														<label class="col-sm-4 control-label form-label">영상/해상도</label>
														<div class="col-sm-4">
															<input type="text" class="form-control"> 
														</div>
														<div class="col-sm-3">
															<input type="text" class="form-control"> 
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
															<td>Blue</td>
															</tr>
															<tr>
															<td>Band 2</td>
															<td>Green</td>
															</tr>
															<tr>
															<td>Band 3</td>
															<td>Red</td>
															</tr>
															<tr>
															<td>Band 4</td>
															<td>NIR</td>
															</tr>
															<tr>
															<td>Band 5</td>
															<td></td>
															</tr>
															<tr>
															<td>Band 6</td>
															<td></td>
															</tr>
															<tr>
															<td>Band 7</td>
															<td></td>
															</tr>
															<tr>
															<td>Band 8</td>
															<td></td>
															</tr>
															</tbody>
														</table>
													</div>
												</div>
												<div class="col-sm-6">	
													<div class="form-group m-t-10">
														<label class="col-sm-12 control-label form-label">벡터 추출 조건문 입력</label>
														<div class="col-sm-6">
															<div class="panel panel-default" style="height:130px;">
															
															</div>
														</div>
														<div class="col-sm-6">
															<div class="col-sm-3">
																<button type="button" class="keypad">&#61;</button>
															</div>
															<div class="col-sm-3">
																<button type="button" class="keypad">&#60;&#62;</button>
															</div>
															<div class="col-sm-3">
																<button type="button" class="keypad">&#60;</button>
															</div>
															<div class="col-sm-3">
																<button type="button" class="keypad">&#62;</button>
															</div>
															<div class="col-sm-3">
																<button type="button" class="keypad">&#62;&#61;</button>
															</div>
															<div class="col-sm-3">
																<button type="button" class="keypad">&#60;&#61;</button>
															</div>
															<div class="col-sm-3">
																<button type="button" class="keypad">&#95;</button>
															</div>
															<div class="col-sm-3">
																<button type="button" class="keypad">&#37;</button>
															</div>
															<div class="col-sm-4">
																<button type="button" class="keypad">&#40;&#41;</button>
															</div>
															<div class="col-sm-4">
																<button type="button" class="keypad">is</button>
															</div>
															<div class="col-sm-4">
																<button type="button" class="keypad">Like</button>
															</div>
															<div class="col-sm-4">
																<button type="button" class="keypad">And</button>
															</div>
															<div class="col-sm-4">
																<button type="button" class="keypad">Or</button>
															</div>
															<div class="col-sm-4">
																<button type="button" class="keypad">Not</button>
															</div>
														</div>
													</div>
													<div class="form-group m-t-10">
														<label class="col-sm-12 control-label form-label">벡터 추출 조건문 입력</label>
														<div class="col-sm-12">
															<textarea class="form-control" rows="2" id="textarea1" placeholder="ANN_CD = ‘10’"></textarea>
														</div>
													</div>
												</div>
												</form> 

											</div>
										</div>
										<div class="modal-footer">
											<button type="button" class="btn btn-default"><i class="fas fa-save m-r-5"></i>저장</button>
											<button type="button" class="btn btn-gray" data-dismiss="modal" aria-label="Close"><i class="fas fa-times m-r-5"></i>취소</button>
										</div>
									</div>
								</div>
							</div>

						</div>
					</div>
				</div>

			</div>
			<div role="tabpanel" class="tab-pane" id="tab-03">

				<div class="sidepanel-m-title">
					모델 & AI 데이터셋 검색
				</div>
				<div class="panel-body">
					<div class="form-group m-t-10">
						<label for="input002" class="col-sm-2 control-label form-label">종류</label>
						<div class="col-sm-10">
							<label class="ckbox-container">항공영상
								<input type="checkbox" checked="checked">
								<span class="checkmark"></span>
							</label>
							<label class="ckbox-container">국토위성
								<input type="checkbox">
								<span class="checkmark"></span>
							</label>
							<label class="ckbox-container">기타위성
								<input type="checkbox">
								<span class="checkmark"></span>
							</label>
						</div>
					</div>	
					<div class="form-group m-t-10">
						<label for="input002" class="col-sm-2 control-label form-label">클래스</label>
						<div class="col-sm-10">
							<label class="ckbox-container">건물
								<input type="checkbox" checked="checked">
								<span class="checkmark"></span>
							</label>
							<label class="ckbox-container">주차장
								<input type="checkbox">
								<span class="checkmark"></span>
							</label>
							<label class="ckbox-container">도로
								<input type="checkbox">
								<span class="checkmark"></span>
							</label>
							<label class="ckbox-container">가로수
								<input type="checkbox">
								<span class="checkmark"></span>
							</label>
							<label class="ckbox-container">논
								<input type="checkbox">
								<span class="checkmark"></span>
							</label>
							<label class="ckbox-container">밭
								<input type="checkbox">
								<span class="checkmark"></span>
							</label>
							<label class="ckbox-container">산림
								<input type="checkbox">
								<span class="checkmark"></span>
							</label>
							<label class="ckbox-container">나지
								<input type="checkbox">
								<span class="checkmark"></span>
							</label>
						</div>
					</div>	
					<div class="btn-wrap a-cent">
						<a href="#" class="btn btn-default"><i class="fas fa-search m-r-5"></i>검색</a>
					</div>
					<div class="sidepanel-s-title m-t-30">
					검색 결과
					</div>
					<div class="panel-body">
						<div class="col-lg-12">
							<div class="panel panel-default" style="height:150px;">
								<div class="in-panel-title">
								모델 목록
								</div>
								<dl class="result-list-input">
									<dd>
										<div class="radio radio-info radio-inline">
											<input type="radio" id="inlineRadio1" value="option1" name="radioInline" checked="">
											<label for="inlineRadio1">모델_항공_건물_xxxxxx001</label>
										</div>
										<a href="#" class="btn btn-xs f-right" data-toggle="modal" data-target="#myModal4"><i class="fas fa-search"></i></a>
									</dd>
									<dd>
										<div class="radio radio-info radio-inline">
											<input type="radio" id="inlineRadio2" value="option2" name="radioInline" checked="">
											<label for="inlineRadio2">모델_항공_도로_xxxxxx001</label>
										</div>
										<a href="#" class="btn btn-xs f-right" data-toggle="modal" data-target="#myModal4"><i class="fas fa-search"></i></a>
									</dd>
								</dl>
							</div>
						</div>
					</div>
					<div class="modal fade" id="myModal4" tabindex="-1" role="dialog" aria-hidden="true">
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
													<input type="text" class="form-control"> 
												</div>
											</div>
										</div>
										<div class="col-sm-12">	
											<div class="form-group m-t-10">
												<label class="col-sm-3 control-label form-label">모델 명</label>
												<div class="col-sm-9">
													<input type="text" class="form-control"> 
												</div>
											</div>
										</div>
										<div class="col-sm-12">	
											<div class="form-group m-t-10">
												<label class="col-sm-3 control-label form-label">알고리즘</label>
												<div class="col-sm-9">
													<input type="text" class="form-control"> 
												</div>
											</div>
										</div>
										<div class="col-sm-12">	
											<div class="form-group m-t-10">
												<label class="col-sm-3 control-label form-label">데이터셋</label>
												<div class="col-sm-9">
													<input type="text" class="form-control"> 
												</div>
											</div>
										</div>
										<div class="col-sm-12">	
											<div class="form-group m-t-10">
												<label class="col-sm-3 control-label form-label">클래스</label>
												<div class="col-sm-9">
													<input type="text" class="form-control"> 
												</div>
											</div>
										</div>
										<div class="col-sm-12">	
											<div class="form-group m-t-10">
												<label class="col-sm-3 control-label form-label">영상/해상도</label>
												<div class="col-sm-6">
													<input type="text" class="form-control"> 
												</div>
												<div class="col-sm-3">
													<input type="text" class="form-control"> 
												</div>
											</div>
										</div>
										<div class="sidepanel-m-title m-t-10 b-none">
											성능 평가 그래프
										</div>	
										<div class="col-sm-12">	
											<div class="graph-wrap">
												<img src="img/test_graph.png" width="100%">
											</div>
										</div>
										<div class="col-sm-12">	
											<div class="form-group m-t-10">
												<label class="col-sm-3 control-label form-label">성능 평가 그래프</label>
												<div class="col-sm-12">	
													<label class="col-sm-2 control-label form-label">Epoch</label>
													<div class="col-sm-2">
														<input type="text" class="form-control"> 
													</div>
													<label class="col-sm-2 control-label form-label">Accuracy</label>
													<div class="col-sm-2">
														<input type="text" class="form-control"> 
													</div>
													<label class="col-sm-2 control-label form-label">mIoU</label>
													<div class="col-sm-2">
														<input type="text" class="form-control"> 
													</div>
												</div>
												<div class="col-sm-12">	
													<label class="col-sm-2 control-label form-label">Precision</label>
													<div class="col-sm-2">
														<input type="text" class="form-control"> 
													</div>
													<label class="col-sm-2 control-label form-label">Recall</label>
													<div class="col-sm-2">
														<input type="text" class="form-control"> 
													</div>
													<div class="col-sm-6">
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
								<div class="modal-footer">
									<button type="button" class="btn btn-gray" data-dismiss="modal" aria-label="Close"><i class="fas fa-times m-r-5"></i>삭제</button>
								</div>
							</div>
						</div>
					</div>
					<div class="panel-body">
						<div class="col-lg-12">
							<div class="panel panel-default" style="height:150px;">
								<div class="in-panel-title">
								AI 데이터셋 목록
								</div>
								<dl class="result-list-input">
									<dd>
										<div class="radio radio-info radio-inline">
											<input type="radio" id="inlineRadio5" value="option5" name="radioInline" checked="">
											<label for="inlineRadio5">AI데이터셋_항공_건물_xxxxxx001</label>
										</div>
										<a href="#" class="btn btn-xs f-right" data-toggle="modal" data-target="#myModal4"><i class="fas fa-search"></i></a>
									</dd>
									<dd>
										<div class="radio radio-info radio-inline">
											<input type="radio" id="inlineRadio6" value="option6" name="radioInline" checked="">
											<label for="inlineRadio6">AI데이터셋 _항공_도로_xxxxxx001</label>
										</div>
										<a href="#" class="btn btn-xs f-right" data-toggle="modal" data-target="#myModal4"><i class="fas fa-search"></i></a>
									</dd>
									<dd>
										<div class="radio radio-info radio-inline">
											<input type="radio" id="inlineRadio7" value="option7" name="radioInline" checked="">
											<label for="inlineRadio7">AI데이터셋 _K3A_건물_ xxxxxx001</label>
										</div>
										<a href="#" class="btn btn-xs f-right" data-toggle="modal" data-target="#myModal4"><i class="fas fa-search"></i></a>
									</dd>
								</dl>
							</div>
						</div>
					</div>
					<div class="btn-wrap a-cent">
						<a href="#" class="btn btn-default" data-toggle="modal" data-target="#myModal5"><i class="fas fa-pencil-alt m-r-5"></i>AI 학습 수행</a>
					</div>
					<div class="modal fade" id="myModal5" tabindex="-1" role="dialog" aria-hidden="true">
						<div class="modal-dialog">
							<div class="modal-content">
								<div class="modal-header">
									<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
									<h4 class="modal-title">모델 학습</h4>
								</div>
								<div class="modal-body">
									<div class="panel-body">	
										<div class="col-sm-6 p-r-15">
											<div class="sidepanel-m-title">
												데이터셋 상세
											</div>	
											<div class="form-group m-t-10">
												<label class="col-sm-3 control-label form-label">모델</label>
												<div class="col-sm-9">
													<input type="text" class="form-control"> 
												</div>
											</div>
											<div class="form-group m-t-10">
												<label class="col-sm-3 control-label form-label">알고리즘</label>
												<div class="col-sm-9">
													<input type="text" class="form-control"> 
												</div>
											</div>
											<div class="sidepanel-m-title m-t-10">
												선택 AI 데이터셋 정보
											</div>	
											<div class="form-group m-t-10">
												<label class="col-sm-3 control-label form-label">데이터셋</label>
												<div class="col-sm-9">
													<input type="text" class="form-control"> 
												</div>
											</div>
											<div class="form-group m-t-10">
												<label class="col-sm-3 control-label form-label">클래스</label>
												<div class="col-sm-9">
													<input type="text" class="form-control"> 
												</div>
											</div>
											<div class="form-group m-t-10">
												<label class="col-sm-3 control-label form-label">영상/해상도</label>
												<div class="col-sm-6">
													<input type="text" class="form-control"> 
												</div>
												<div class="col-sm-3">
													<input type="text" class="form-control"> 
												</div>
											</div>
											<label class="col-sm-12 control-label form-label">밴드 목록</label>
											<div class="tbl-header">
												<table cellpadding="0" cellspacing="0" border="0">
													<thead>
													<tr>
														<th width="15%">
															<input type="checkbox" class="cb-basic">
														</th>
														<th width="45%">이름</th>
														<th>스펙트럼</th>
													</tr>
													</thead>
												</table>
											</div>
											<div class="tbl-content">
												<table cellpadding="0" cellspacing="0" border="0">
													<tbody>
													<tr>
														<td width="15%">
															<input type="checkbox" class="cb-basic">
														</td>
														<td width="45%">Band 1</td>
														<td>Blue</td>
													</tr>
													</tbody>
												</table>
											</div>
										</div>
										<div class="col-sm-6">
											<div class="sidepanel-m-title">
												파라미터 설정
											</div>	
											<div class="form-group m-t-10">
												<label class="col-sm-3 control-label form-label">패치 사이즈</label>
												<div class="col-sm-9">
													<select class="form-basic">
														<option></option>
													</select>
												</div>
											</div>
											<div class="form-group m-t-10">
												<label class="col-sm-3 control-label form-label">중첩 사이즈</label>
												<div class="col-sm-9">
													<select class="form-basic">
														<option></option>
													</select>
												</div>
											</div>
											<div class="form-group m-t-10">
												<label class="col-sm-3 control-label form-label">스트레칭</label>
												<div class="col-sm-9">
													<div class="col-sm-2">
														<button type="button" class="btn btn-light btn-icon"><i class="fas fa-minus"></i></button>
													</div>
													<div class="col-sm-8">
														<input type="text" class="form-control" id="">
													</div>
													<div class="col-sm-2">
														<button type="button" class="btn btn-light btn-icon"><i class="fas fa-plus"></i></button>
													</div>
												</div>
											</div>
											<div class="form-group m-t-10">
												<label class="col-sm-3 control-label form-label">배치 사이즈</label>
												<div class="col-sm-9">
													<select class="form-basic">
														<option></option>
													</select>
												</div>
											</div>
											<div class="form-group m-t-10">
												<label class="col-sm-3 control-label form-label">GPU</label>
												<div class="col-sm-9">
													<select class="form-basic">
														<option></option>
													</select>
												</div>
											</div>
											<div class="form-group m-t-10">
												<label class="col-sm-3 control-label form-label">반복횟수</label>
												<div class="col-sm-9">
													<div class="col-sm-2">
														<button type="button" class="btn btn-light btn-icon"><i class="fas fa-minus"></i></button>
													</div>
													<div class="col-sm-8">
														<input type="text" class="form-control" id="">
													</div>
													<div class="col-sm-2">
														<button type="button" class="btn btn-light btn-icon"><i class="fas fa-plus"></i></button>
													</div>
												</div>
											</div>
											<div class="form-group m-t-10">
												<label class="col-sm-12 control-label form-label">데이터셋 학습/검증 비율 설정</label>
												<div class="col-sm-12">
													<div class="col-sm-3">
														<div class="col-sm-6 in-tit">학습</div>
														<div class="col-sm-6"><input type="text" class="form-control" id=""></div>
													</div>
													<div class="col-sm-6">
														<div class="slidecontainer">
															<input type="range" min="1" max="100" value="50">
														</div>
													</div>
													<div class="col-sm-3">
														<div class="col-sm-6 in-tit">검증</div>
														<div class="col-sm-6"><input type="text" class="form-control" id=""></div>
													</div>
												</div>
											</div>
										</div>
										</form> 

									</div>
								</div>
								<div class="modal-footer">
									<button type="button" class="btn btn-default"><i class="fas fa-pencil-alt m-r-5"></i>학습 시작</button>
									<button type="button" class="btn btn-gray" data-dismiss="modal" aria-label="Close"><i class="fas fa-times m-r-5"></i>취소</button>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div role="tabpanel" class="tab-pane active" id="tab-04">

				<div class="sidepanel-m-title">
					모델 & AI 데이터셋 검색
				</div>
				<div class="panel-body">
					<div class="form-group m-t-10">
						<label for="input002" class="col-sm-2 control-label form-label">종류</label>
						<div class="col-sm-10">
							<label class="ckbox-container">항공영상
								<input type="checkbox" checked="checked">
								<span class="checkmark"></span>
							</label>
							<label class="ckbox-container">국토위성
								<input type="checkbox">
								<span class="checkmark"></span>
							</label>
							<label class="ckbox-container">기타위성
								<input type="checkbox">
								<span class="checkmark"></span>
							</label>
						</div>
					</div>	
					<div class="form-group m-t-10">
						<label for="input002" class="col-sm-2 control-label form-label">클래스</label>
						<div class="col-sm-10">
							<label class="ckbox-container">건물
								<input type="checkbox" checked="checked">
								<span class="checkmark"></span>
							</label>
							<label class="ckbox-container">주차장
								<input type="checkbox">
								<span class="checkmark"></span>
							</label>
							<label class="ckbox-container">도로
								<input type="checkbox">
								<span class="checkmark"></span>
							</label>
							<label class="ckbox-container">가로수
								<input type="checkbox">
								<span class="checkmark"></span>
							</label>
							<label class="ckbox-container">논
								<input type="checkbox">
								<span class="checkmark"></span>
							</label>
							<label class="ckbox-container">밭
								<input type="checkbox">
								<span class="checkmark"></span>
							</label>
							<label class="ckbox-container">산림
								<input type="checkbox">
								<span class="checkmark"></span>
							</label>
							<label class="ckbox-container">나지
								<input type="checkbox">
								<span class="checkmark"></span>
							</label>
						</div>
					</div>	
					<div class="btn-wrap a-cent">
						<a href="#" class="btn btn-default"><i class="fas fa-search m-r-5"></i>검색</a>
					</div>
					<div class="sidepanel-s-title m-t-30">
					검색 결과
					</div>
					<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-hidden="true">
						<div class="modal-dialog">
							<div class="modal-content">
								<div class="modal-header">
									<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
									<h4 class="modal-title">데이터셋 등록</h4>
								</div>
								<div class="modal-body">
									<div class="panel-body">	
										<div class="sidepanel-m-title">
											데이터셋 상세
										</div>	
										<div class="col-sm-12">	
											<div class="form-group m-t-10">
												<label class="col-sm-2 control-label form-label">이름</label>
												<div class="col-sm-10">
													<input type="text" class="form-control"> 
												</div>
											</div>
										</div>
										<div class="col-sm-12">	
											<div class="form-group m-t-10">
												<label class="col-sm-2 control-label form-label">클래스</label>
												<div class="col-sm-10">
													<input type="text" class="form-control"> 
												</div>
											</div>
										</div>
										<div class="col-sm-6 p-r-10">
											<div class="form-group m-t-10">
												<label class="col-sm-12 control-label form-label">
													선택 영상 목록 (총 3개)
													<button type="button" class="btn btn-light btn-xs f-right"><i class="fas fa-trash-alt m-r-5"></i>삭제</button>
												</label>
												<div class="col-sm-12">
													<div class="panel panel-default" style="height:100px;">
             
														<div class="acidjs-css3-treeview">
															<ul class="dep-01">
																<li>
																	<input type="checkbox" id="node-0" checked="checked" /><label><input type="checkbox" /><span></span></label><label for="node-0">영상</label>
																	<ul class="dep-02">
																		<li>
																			<input type="checkbox" id="node-0-0"/>
																			<label><input type="checkbox" /><span></span></label>
																			<label for="node-0-0">영상 ID 1</label>
																			<a href="" class="btn-list f-right"><i class="far fa-info-circle"></i></a>
																		</li>
																		<li>
																			<input type="checkbox" id="node-0-1" />
																			<label><input type="checkbox" /><span></span></label>
																			<label for="node-0-1">영상 ID 2</label>
																			<a href="" class="btn-list f-right"><i class="far fa-info-circle"></i></a>
																		</li>
																		<li>
																			<input type="checkbox" id="node-0-2" />
																			<label><input type="checkbox" /><span></span></label>
																			<label for="node-0-2">영상 ID 3</label>
																			<a href="" class="btn-list f-right"><i class="far fa-info-circle"></i></a>
																		</li>
																	</ul>
																</li>
															</ul>
														</div>

														<script>
														$(".acidjs-css3-treeview").delegate("label input:checkbox", "change", function() {
															var
																checkbox = $(this),
																nestedList = checkbox.parent().next().next(),
																selectNestedListCheckbox = nestedList.find("label:not([for]) input:checkbox");
														 
															if(checkbox.is(":checked")) {
																return selectNestedListCheckbox.prop("checked", true);
															}
															selectNestedListCheckbox.prop("checked", false);
														});
														</script>

													</div>
												</div>
											</div>
										</div>
										<div class="col-sm-6">
											<div class="form-group m-t-10">
												<label class="col-sm-12 control-label form-label">선택 라벨링 데이터 목록 (총 3개)</label>
												<div class="col-sm-12">
													<div class="panel panel-default" style="height:100px;">
														<ul class="pop-result-list">
															<li>벡터 레이어 이름1<a href="" class="btn-list f-right"><i class="far fa-info-circle"></i></a></li>
															<li>벡터 레이어 이름2<a href="" class="btn-list f-right"><i class="far fa-info-circle"></i></a></li>
															<li>벡터 레이어 이름3<a href="" class="btn-list f-right"><i class="far fa-info-circle"></i></a></li>
														</ul>

													</div>
												</div>
											</div>
										</div>
										<div class="col-sm-12">										
											<p class="notice-sm">* 영상 삭제 시, 동일한 이름의 라벨링 데이터도 자동 삭제됩니다.</p>
										</div>
										<div class="col-sm-6 p-r-10">					
											<div class="form-group m-t-10">
												<label class="col-sm-4 control-label form-label">영상/해상도</label>
												<div class="col-sm-4">
													<input type="text" class="form-control"> 
												</div>
												<div class="col-sm-3">
													<input type="text" class="form-control"> 
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
													<td>Blue</td>
													</tr>
													<tr>
													<td>Band 2</td>
													<td>Green</td>
													</tr>
													<tr>
													<td>Band 3</td>
													<td>Red</td>
													</tr>
													<tr>
													<td>Band 4</td>
													<td>NIR</td>
													</tr>
													<tr>
													<td>Band 5</td>
													<td></td>
													</tr>
													<tr>
													<td>Band 6</td>
													<td></td>
													</tr>
													<tr>
													<td>Band 7</td>
													<td></td>
													</tr>
													<tr>
													<td>Band 8</td>
													<td></td>
													</tr>
													</tbody>
												</table>
											</div>
										</div>
										<div class="col-sm-6">	
											<div class="form-group m-t-10">
												<label class="col-sm-12 control-label form-label">벡터 추출 조건문 입력</label>
												<div class="col-sm-6">
													<div class="panel panel-default" style="height:130px;">
													
													</div>
												</div>
												<div class="col-sm-6">
													<div class="col-sm-3">
														<button type="button" class="keypad">&#61;</button>
													</div>
													<div class="col-sm-3">
														<button type="button" class="keypad">&#60;&#62;</button>
													</div>
													<div class="col-sm-3">
														<button type="button" class="keypad">&#60;</button>
													</div>
													<div class="col-sm-3">
														<button type="button" class="keypad">&#62;</button>
													</div>
													<div class="col-sm-3">
														<button type="button" class="keypad">&#62;&#61;</button>
													</div>
													<div class="col-sm-3">
														<button type="button" class="keypad">&#60;&#61;</button>
													</div>
													<div class="col-sm-3">
														<button type="button" class="keypad">&#95;</button>
													</div>
													<div class="col-sm-3">
														<button type="button" class="keypad">&#37;</button>
													</div>
													<div class="col-sm-4">
														<button type="button" class="keypad">&#40;&#41;</button>
													</div>
													<div class="col-sm-4">
														<button type="button" class="keypad">is</button>
													</div>
													<div class="col-sm-4">
														<button type="button" class="keypad">Like</button>
													</div>
													<div class="col-sm-4">
														<button type="button" class="keypad">And</button>
													</div>
													<div class="col-sm-4">
														<button type="button" class="keypad">Or</button>
													</div>
													<div class="col-sm-4">
														<button type="button" class="keypad">Not</button>
													</div>
												</div>
											</div>
											<div class="form-group m-t-10">
												<label class="col-sm-12 control-label form-label">벡터 추출 조건문 입력</label>
												<div class="col-sm-12">
													<input type="text" class="form-control"> 
												</div>
											</div>
										</div>
										</form> 

									</div>
								</div>
								<div class="modal-footer">
									<button type="button" class="btn btn-default"><i class="fas fa-save m-r-5"></i>저장</button>
									<button type="button" class="btn btn-gray" data-dismiss="modal" aria-label="Close"><i class="fas fa-times m-r-5"></i>취소</button>
								</div>
							</div>
						</div>
					</div>
					<div class="panel-body">
						<div class="col-lg-12">
							<div class="panel panel-default" style="height:150px;">
								<div class="in-panel-title">
								모델 목록
								</div>
								<dl class="result-list-input">
									<dd>
										<div class="radio radio-info radio-inline">
											<input type="radio" id="inlineRadio3" value="option3" name="radioInline" checked="">
											<label for="inlineRadio3">모델_항공_건물_xxxxxx001</label>
										</div>
										<a href="#" class="btn btn-xs f-right"><i class="fas fa-search"></i></a>
									</dd>
									<dd>
										<div class="radio radio-info radio-inline">
											<input type="radio" id="inlineRadio4" value="option4" name="radioInline" checked="">
											<label for="inlineRadio4">모델_항공_도로_xxxxxx001</label>
										</div>
										<a href="#" class="btn btn-xs f-right"><i class="fas fa-search"></i></a>
									</dd>
								</dl>
							</div>
						</div>
					</div>
					<div class="panel-body">
						<div class="col-lg-12">
							<div class="panel panel-default" style="height:150px;">
								<div class="in-panel-title">
								AI 데이터셋 목록
								</div>
								<dl class="result-list-input">
									<dd>
										<div class="radio radio-info radio-inline">
											<input type="radio" id="inlineRadio8" value="option8" name="radioInline" checked="">
											<label for="inlineRadio8">AI데이터셋_항공_건물_xxxxxx001</label>
										</div>
										<a href="#" class="btn btn-xs f-right"><i class="fas fa-search"></i></a>
									</dd>
									<dd>
										<div class="radio radio-info radio-inline">
											<input type="radio" id="inlineRadio9" value="option9" name="radioInline" checked="">
											<label for="inlineRadio9">AI데이터셋 _항공_도로_xxxxxx001</label>
										</div>
										<a href="#" class="btn btn-xs f-right"><i class="fas fa-search"></i></a>
									</dd>
									<dd>
										<div class="radio radio-info radio-inline">
											<input type="radio" id="inlineRadio10" value="option10" name="radioInline" checked="">
											<label for="inlineRadio10">AI데이터셋 _K3A_건물_ xxxxxx001</label>
										</div>
										<a href="#" class="btn btn-xs f-right"><i class="fas fa-search"></i></a>
									</dd>
								</dl>
							</div>
						</div>
					</div>
					<div class="btn-wrap a-cent">
						<a href="#" class="btn btn-default" data-toggle="modal" data-target="#myModal6"><i class="fas fa-pen m-r-5"></i>성능평가 수행</a>
					</div>
					<div class="modal fade" id="myModal6" tabindex="-1" role="dialog" aria-hidden="true">
						<div class="modal-dialog">
							<div class="modal-content">
								<div class="modal-header">
									<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
									<h4 class="modal-title">성능 평가</h4>
								</div>
								<div class="modal-body">
									<div class="panel-body">	
										<div class="col-sm-6 p-r-15">
											<div class="sidepanel-m-title">
												데이터셋 상세
											</div>	
											<div class="form-group m-t-10">
												<label class="col-sm-3 control-label form-label">모델</label>
												<div class="col-sm-9">
													<input type="text" class="form-control"> 
												</div>
											</div>
											<div class="form-group m-t-10">
												<label class="col-sm-3 control-label form-label">알고리즘</label>
												<div class="col-sm-9">
													<input type="text" class="form-control"> 
												</div>
											</div>
											<div class="form-group m-t-10">
												<label class="col-sm-3 control-label form-label">데이터셋</label>
												<div class="col-sm-9">
													<input type="text" class="form-control"> 
												</div>
											</div>
											<div class="form-group m-t-10">
												<label class="col-sm-3 control-label form-label">클래스</label>
												<div class="col-sm-9">
													<input type="text" class="form-control"> 
												</div>
											</div>
											<div class="form-group m-t-10">
												<label class="col-sm-3 control-label form-label">영상/해상도</label>
												<div class="col-sm-6">
													<input type="text" class="form-control"> 
												</div>
												<div class="col-sm-3">
													<input type="text" class="form-control"> 
												</div>
											</div>
											<label class="col-sm-12 control-label form-label">밴드 목록</label>
											<div class="tbl-header">
												<table cellpadding="0" cellspacing="0" border="0">
													<thead>
													<tr>
														<th width="45%">이름</th>
														<th>스펙트럼</th>
													</tr>
													</thead>
												</table>
											</div>
											<div class="tbl-content">
												<table cellpadding="0" cellspacing="0" border="0">
													<tbody>
													<tr>
														<td width="45%">Band 1</td>
														<td>Blue</td>
													</tr>
													<tr>
														<td width="45%">Band 2</td>
														<td>Green</td>
													</tr>
													<tr>
														<td width="45%">Band 3</td>
														<td>Red</td>
													</tr>
													</tbody>
												</table>
											</div>
											<div class="form-group m-t-10">
												<label class="col-sm-3 control-label form-label">패치 사이즈</label>
												<div class="col-sm-9">
													<input type="text" class="form-control"> 
												</div>
											</div>
											<div class="form-group m-t-10">
												<label class="col-sm-3 control-label form-label">중첩 사이즈</label>
												<div class="col-sm-9">
													<input type="text" class="form-control"> 
												</div>
											</div>
										</div>
										<div class="col-sm-6">
											<div class="sidepanel-m-title">
												선택된 평가용 데이터셋 정보
											</div>	
											<div class="form-group m-t-10">
												<label class="col-sm-3 control-label form-label">데이터셋</label>
												<div class="col-sm-9">
													<input type="text" class="form-control"> 
												</div>
											</div>
											<div class="form-group m-t-10">
												<label class="col-sm-3 control-label form-label">클래스</label>
												<div class="col-sm-9">
													<input type="text" class="form-control"> 
												</div>
											</div>
											<div class="form-group m-t-10">
												<label class="col-sm-3 control-label form-label">영상/해상도</label>
												<div class="col-sm-6">
													<input type="text" class="form-control"> 
												</div>
												<div class="col-sm-3">
													<input type="text" class="form-control"> 
												</div>
											</div>
											<label class="col-sm-12 control-label form-label">밴드 목록</label>
											<div class="tbl-header">
												<table cellpadding="0" cellspacing="0" border="0">
													<thead>
													<tr>
														<th width="45%">이름</th>
														<th>스펙트럼</th>
													</tr>
													</thead>
												</table>
											</div>
											<div class="tbl-content">
												<table cellpadding="0" cellspacing="0" border="0">
													<tbody>
													<tr>
														<td width="45%">Band 1</td>
														<td>Blue</td>
													</tr>
													<tr>
														<td width="45%">Band 2</td>
														<td>Green</td>
													</tr>
													<tr>
														<td width="45%">Band 3</td>
														<td>Red</td>
													</tr>
													</tbody>
												</table>
											</div>
										</div>
										</form> 

									</div>
								</div>
								<div class="modal-footer">
									<button type="button" class="btn btn-default"><i class="fas fa-pen m-r-5"></i>성능평가 시작</button>
									<button type="button" class="btn btn-gray" data-dismiss="modal" aria-label="Close"><i class="fas fa-times m-r-5"></i>취소</button>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div role="tabpanel" class="tab-pane" id="tab-05">
				<div class="inside-panel panel-default">
					<div class="inside-panel-tit">
						평가 결과 래스터 목록
					</div>

					<div class="inside-panel-body" style="height:300px;">
						<ul class="tree">
							<li class="has">
								<input type="checkbox" name="domain[]" value="평가 결과 – 래스터">
								<label>평가 결과 – 래스터<span class="total">(3)</span></label>
								<ul class="tree-dep-02">
									<li class="">
										<input type="checkbox" name="subdomain[]" value="평가 결과 래스터 ID – xxxxxxxx001">
										<label class="context-menu-one btn-neutral">평가 결과 래스터 ID – xxxxxxxx001</label>
										<p class="sub-label">&#9492;클래스 a</p>
									</li>
									<li class="">
										<input type="checkbox" name="subdomain[]" value="평가 결과 래스터 ID – xxxxxxxx002">
										<label class="context-menu-one btn-neutral">평가 결과 래스터 ID – xxxxxxxx002</label>
									</li>
									<li class="">
										<input type="checkbox" name="subdomain[]" value="평가 결과 래스터 ID – xxxxxxxx003">
										<label class="context-menu-one btn-neutral">평가 결과 래스터 ID – xxxxxxxx003</label>
									</li>
								</ul>
							</li>
						</ul>
					</div>
				</div>
				<div class="inside-panel panel-default m-t-20" style="height:300px;">
					<div class="inside-panel-tit">
						벡터 변환 목록
						<ul class="panel-tools">
							<li><a class="icon search-tool"><i class="fas fa-trash-alt"></i></a></li>
							<li><a class="icon expand-tool"><i class="fas fa-folder"></i></a></li>
							<li><a class="icon closed-tool"><i class="fas fa-folder-open"></i></a></li>
						</ul>
					</div>
					<script>

					$(function(){
						$.contextMenu({
							selector: '.context-menu-one', 
								items: {
									vector: {
										name: "벡터로 변환",
										callback: function(key, opt){
										alert("Clicked on " + key);
									}
								},
									download: {
										name: "다운로드",
										callback: function(key, opt){
										alert("Clicked on " + key);
									}
								},
									result: {
										name: "성능평가 결과 조회",
										callback: function(key, opt){
										alert("Clicked on " + key);
									}
								}
							}, 
						});
					});
					</script>
					<div class="inside-panel-body">
						<ul class="tree">
							<li class="has">
								<input type="checkbox" name="domain[]" value="평가 결과 – XXXXXX 01">
								<label>평가 결과 – XXXXXX 01<span class="total">(3)</span></label>
								<ul class="tree-dep-02">
									<li class="">
										<input type="checkbox" name="subdomain[]" value="평가 결과 ID – xxxxxxxx001">
										<label class="context-menu-one btn-neutral">평가 결과 ID – xxxxxxxx001</label>
										<span class="label-color blue f-right"></span>
									</li>
									<li class="">
										<input type="checkbox" name="subdomain[]" value="평가 결과 ID – xxxxxxxx002">
										<label class="context-menu-one btn-neutral">평가 결과 ID – xxxxxxxx002</label>
										<span class="label-color pink f-right"></span>
									</li>
									<li class="">
										<input type="checkbox" name="subdomain[]" value="평가 결과 ID – xxxxxxxx003">
										<label class="context-menu-one btn-neutral">평가 결과 ID – xxxxxxxx003</label>
										<span class="label-color orange f-right"></span>
									</li>
								</ul>
							</li>
							<li class="has">
								<input type="checkbox" name="domain[]" value="평가 결과 – XXXXXX 02">
								<label>평가 결과 – XXXXXX 02<span class="total">(3)</span></label>
								<ul class="tree-dep-02">
									<li class="">
										<input type="checkbox" name="subdomain[]" value="평가 결과 ID – xxxxxxxx100">
										<label class="context-menu-one btn-neutral">평가 결과 ID – xxxxxxxx100</label>
									</li>
									<li class="">
										<input type="checkbox" name="subdomain[]" value="평가 결과 ID – xxxxxxxx101">
										<label class="context-menu-one btn-neutral">평가 결과 ID – xxxxxxxx101</label>
									</li>
									<li class="has">
										<input type="checkbox" name="subdomain[]" value="평가 결과 ID – xxxxxxxx102">
										<label class="context-menu-one btn-neutral">평가 결과 ID – xxxxxxxx102</label>
								</ul>
							</li>
						</ul>
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
				</script>

			</div>
		</div>

	</div>

</div>

<a href="#" class="sidebar-open-button"><i class="fa fa-bars"></i></a>
<a href="#" class="sidebar-open-button-mobile"><i class="fa fa-bars"></i></a>



<!-- START CONTENT -->
<div class="content">

</div>


<!-- ================================================
jQuery Library
================================================ -->

<!-- ================================================
Bootstrap Core JavaScript File
================================================ -->
<script src="js/bootstrap/bootstrap.min.js"></script>

<!-- ================================================
Plugin.js - Some Specific JS codes for Plugin Settings
================================================ -->
<script src="js/plugins.js"></script>

<!-- ================================================
Bootstrap Select
================================================ -->
<script src="js/bootstrap-select/bootstrap-select.js"></script>

<!-- ================================================
Bootstrap Toggle
================================================ -->
<script src="js/bootstrap-toggle/bootstrap-toggle.min.js"></script>

<!-- ================================================
Bootstrap WYSIHTML5
================================================ -->
<!-- main file -->
<script src="js/bootstrap-wysihtml5/wysihtml5-0.3.0.min.js"></script>
<!-- bootstrap file -->
<script src="js/bootstrap-wysihtml5/bootstrap-wysihtml5.js"></script>

<!-- ================================================
Summernote
================================================ -->
<script src="js/summernote/summernote.min.js"></script>

<!-- ================================================
Flot Chart
================================================ -->
<!-- main file -->
<script src="js/flot-chart/flot-chart.js"></script>
<!-- time.js -->
<script src="js/flot-chart/flot-chart-time.js"></script>
<!-- stack.js -->
<script src="js/flot-chart/flot-chart-stack.js"></script>
<!-- pie.js -->
<script src="js/flot-chart/flot-chart-pie.js"></script>
<!-- demo codes -->
<script src="js/flot-chart/flot-chart-plugin.js"></script>

<!-- ================================================
Chartist
================================================ -->
<!-- main file -->
<script src="js/chartist/chartist.js"></script>
<!-- demo codes -->
<script src="js/chartist/chartist-plugin.js"></script>

<!-- ================================================
Easy Pie Chart
================================================ -->
<!-- main file -->
<script src="js/easypiechart/easypiechart.js"></script>
<!-- demo codes -->
<script src="js/easypiechart/easypiechart-plugin.js"></script>

<!-- ================================================
Rickshaw
================================================ -->
<!-- d3 -->
<script src="js/rickshaw/d3.v3.js"></script>
<!-- main file -->
<script src="js/rickshaw/rickshaw.js"></script>
<!-- demo codes -->
<script src="js/rickshaw/rickshaw-plugin.js"></script>

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
Below codes are only for index widgets
================================================ -->
<!-- Today Sales -->
<script>

// set up our data series with 50 random data points

var seriesData = [ [], [], [] ];
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

</script>

<!-- Today Activity -->
<script>
// set up our data series with 50 random data points

var seriesData = [ [], [], [] ];
var random = new Rickshaw.Fixtures.RandomData(20);

for (var i = 0; i < 50; i++) {
  random.addData(seriesData);
}

// instantiate our graph!

var graph = new Rickshaw.Graph( {
  element: document.getElementById("todayactivity"),
  renderer: 'area',
  series: [
    {
      color: "#9A80B9",
      data: seriesData[0],
      name: 'London'
    }, {
      color: "#CDC0DC",
      data: seriesData[1],
      name: 'Tokyo'
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
</script>

</body>
</html>