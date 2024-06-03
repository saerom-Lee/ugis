<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>위성 기반의 긴급 공간정보(G119) 제공 서비스</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=1190">
<meta name="apple-mobile-web-app-title" content=""/>
<meta name="robots" content="index,nofollow"/>
<meta name="description" content=""/>
<meta property="og:title" content="">
<meta property="og:url" content="">
<meta property="og:image" content="">
<meta property="og:description" content=""/>


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
			<li role="presentation" class="active" style="width:50%;">
				<a href="#tab-01" aria-controls="tab-01" role="tab" data-toggle="tab" aria-expanded="true" class="active">
					<i class="fas fa-eye-dropper"></i><br>객체추출 요청
				</a>
			</li>
			<li role="presentation" class="" style="width:50%;">
				<a href="#tab-02" aria-controls="tab-02" role="tab" data-toggle="tab" class="" aria-expanded="false">
					<i class="fas fa-search"></i><br>처리 결과
				</a>
			</li>
		</ul>

		<!-- Tab panes -->
		<div class="tab-content">

			<!-- Start tab-01 -->
			<div role="tabpanel" class="tab-pane active" id="tab-01">

				<div class="sidepanel-m-title">
					대상영상 검색
				</div>
				<div class="panel-body">
					<div class="form-group m-t-10">
						<label for="input002" class="col-sm-3 control-label form-label">종류</label>
						<div class="col-sm-9">
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
						<label for="input002" class="col-sm-3 control-label form-label">촬영기간</label>
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
						<label for="input002" class="col-sm-3 control-label form-label">해상도</label>
						<div class="col-sm-4">
							<select class="form-basic">
								<option></option>
							</select>
						</div>
						<div class="col-sm-1"><span class="txt-in-form">~<span></span></span></div>
						<div class="col-sm-4">
							<select class="form-basic">
								<option></option>
							</select>
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
								<div class="in-panel-title">
								영상 목록
								</div>
								<dl class="result-list-input">
									<dt>국토위성영상 (3)</dt>
									<dd>
										<input type="checkbox" id="" name="vehicle1" value="">
										<label for="">국토위성-1호 xxxxxx001</label>
									</dd>
									<dd>
										<input type="checkbox" id="" name="vehicle1" value="">
										<label for="">국토위성-1호 xxxxxx002</label>
									</dd>
									<dd>
										<input type="checkbox" id="" name="vehicle1" value="">
										<label for="">국토위성-1호 xxxxxx003</label>
									</dd>
								</dl>
								<dl class="result-list-input">
									<dt>기타위성 (3)</dt>
									<dd>
										<input type="checkbox" id="" name="vehicle1" value="">
										<label for="">KOMPSAT 3A xxxxxxxxx001</label>
									</dd>
									<dd>
										<input type="checkbox" id="" name="vehicle1" value="">
										<label for="">KOMPSAT 3A xxxxxxxxx002</label>
									</dd>
									<dd>
										<input type="checkbox" id="" name="vehicle1" value="">
										<label for="">KOMPSAT 3A xxxxxxxxx003</label>
									</dd>
								</dl>
								<dl class="result-list-input">
									<dt>정사영상 (3)</dt>
									<dd>
										<input type="checkbox" id="" name="vehicle1" value="">
										<label for="">경기강원권역 25cm(1블럭)</label>
									</dd>
									<dd>
										<input type="checkbox" id="" name="vehicle1" value="">
										<label for="">경기강원권역 25cm(2블럭)</label>
									</dd>
									<dd>
										<input type="checkbox" id="" name="vehicle1" value="">
										<label for="">경기강원권역 25cm(3블럭)</label>
									</dd>
								</dl>
							</div>
						</div>

					</div>

					<div class="btn-wrap a-cent">
						<a href="#" class="btn btn-default" data-toggle="modal" data-target="#myModal"><i class="fas fa-video m-r-5"></i>선택 영상 수행</a>
					</div>
					<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-hidden="true">
						<div class="modal-dialog modal-sm">
							<div class="modal-content">
								<div class="modal-header">
									<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
									<h4 class="modal-title">객체 추출 요청</h4>
								</div>
								<div class="modal-body">
									<div class="panel-body">	
										<div class="sidepanel-m-title">
											모델
										</div>	
										<div class="col-sm-12">	
											<div class="form-group m-t-10">
												<label class="col-sm-2 control-label form-label">모델</label>
												<div class="col-sm-9">
													<input type="text" class="form-control"> 
												</div>
												<div class="col-sm-1">
													<button type="button" class="btn btn-default btn-icon"><i class="fas fa-project-diagram"></i></button>
												</div>
											</div>
										</div>
										<div class="col-sm-12">	
											<div class="form-group m-t-10">
												<label class="col-sm-2 control-label form-label">알고리즘</label>
												<div class="col-sm-10">
													<input type="text" class="form-control"> 
												</div>
											</div>
										</div>
										<div class="col-sm-12">	
											<div class="form-group m-t-10">
												<label class="col-sm-2 control-label form-label">데이터셋</label>
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
										<div class="col-sm-12">	

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
													</tbody>
												</table>
											</div>
										</div>
										<div class="col-sm-12">	
											<div class="form-group m-t-10">
												<label class="col-sm-3 control-label form-label">패치 사이즈</label>
												<div class="col-sm-9">
													<input type="text" class="form-control"> 
												</div>
											</div>
										</div>
										<div class="col-sm-12">	
											<div class="form-group m-t-10">
												<label class="col-sm-3 control-label form-label">중첩 사이즈</label>
												<div class="col-sm-9">
													<input type="text" class="form-control"> 
												</div>
											</div>
										</div>
										<div class="form-group m-t-10">
											<label class="col-sm-12 control-label form-label">선택 라벨링 데이터 목록 (총 3개)</label>
											<div class="col-sm-12">
												<div class="panel panel-default" style="height:100px;">

													<ul class="tree">
														<li class="has">
															<label>영상<span class="total">(3)</span></label>
															<ul class="tree-dep-02">
																<li class="">
																	<label class="context-menu-one btn-neutral">KOMPSAT 3A xxxxxxxxx001</label>
																</li>
																<li class="">
																	<label class="context-menu-one btn-neutral">KOMPSAT 3A xxxxxxxxx002</label>
																</li>
																<li class="">
																	<label class="context-menu-one btn-neutral">KOMPSAT 3A xxxxxxxxx003</label>
																</li>
															</ul>
														</li>
													</ul>

												</div>
											</div>
										</div>
									</div>
								</div>
								<div class="modal-footer">
									<button type="button" class="btn btn-default"><i class="fas fa-eye-dropper m-r-5"></i>추출 시작</button>
									<button type="button" class="btn btn-gray" data-dismiss="modal" aria-label="Close"><i class="fas fa-times m-r-5"></i>취소</button>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div role="tabpanel" class="tab-pane" id="tab-02">
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
Summernote
================================================ -->
<script src="js/summernote/summernote.min.js"></script>

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


</body>
</html>