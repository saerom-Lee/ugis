<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div role="tabpanel" class="tab-pane" id="tab-02">

	<div class="sidepanel-m-title">
		긴급 촬영영상 검색
	</div>
	<div class="panel-body">			
		<form class="" id="form2">
		<div class="form-group m-t-10">
			<label class="col-sm-3 control-label form-label">영상명</label>
			<div class="col-sm-9">
				<input type="text" id="vidoNmForSearch" name="vidoNmForSearch" class="form-control">
			</div>
		</div> 		
		<div class="form-group m-t-10">
			<label for="input002" class="col-sm-3 control-label form-label">촬영기간</label>
			<div class="col-sm-4">
				<input type="text" id="potogrfBeginDt3" name="potogrfBeginDt3" class="form-control">
			</div>
			<div class="col-sm-1"><span class="txt-in-form">~<span></div>
			<div class="col-sm-4">
				<input type="text" id="potogrfEndDt3" name="potogrfEndDt3" class="form-control">
			</div>
		</div>
		<script>
			let potogrfBeginDt3 = new DatePicker(document.getElementById('potogrfBeginDt3'));
			let potogrfEndDt3 = new DatePicker(document.getElementById('potogrfEndDt3'));
		</script>
		<div class="btn-wrap a-cent">
			<a href="#" class="btn btn-default" id="search"><i class="fas fa-search m-r-5"></i>검색</a>
			<a href="" class="btn btn-light" data-toggle="modal" data-target="#myModal"><i class="fas fa-plus-square m-r-5"></i>등록</a>
		</div>
		<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-hidden="true">
			<div class="modal-dialog modal-sm">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						<h4 class="modal-title">촬영영상 등록</h4>
					</div>
					<div class="modal-body">
						<div class="panel-body">					
							<form class="" id="Emergency">
							<div class="form-group m-t-10">
								<label class="col-sm-4 control-label form-label">영상명</label>
								<div class="col-sm-8">
									<input type="text" class="form-control" id="vidoNm" name="vidoNm">
								</div>
							</div>
							<div class="form-group m-t-10">
								<label class="col-sm-4 control-label form-label">취득일</label>
								<div class="col-sm-6">
									<input type="text" class="form-control" id="regDt" name="regDt">
								</div>
								<div class="col-sm-2">
									<button type="button" class="btn btn-default btn-icon"><i class="fas fa-calendar-alt"></i></button>
								</div>
							</div>
							<script>
								let accDatePicker = new DatePicker(document.getElementById('regDt'));
							</script>
							<div class="form-group m-t-10">
								<label class="col-sm-4 control-label form-label">영상구분</label>
								<div class="col-sm-8">
									<select class="form-basic" id="potogrfVidoCd" name="potogrfVidoCd">
										<option value="IMG001">항공영상</option>
										<option value="IMG002">국토위성</option>
										<option value="IMG003">기타위성</option>
										<option value="IMG004">드론영상</option>
									</select>            
								</div>
							</div>
							<div class="form-group m-t-10">
								<label for="satlitVidoExtrlPath" class="col-sm-4 control-label form-label">입력영상위치</label>
								<div class="col-sm-8">
									<input type="text" class="form-control" id="satlitVidoExtrlPath" name="satlitVidoExtrlPath" value="D:\Users\JHJ\Videos\">
								</div>
							</div>
							<div class="form-group m-t-10">
								<label for="satlitVidoInnerPath" class="col-sm-4 control-label form-label">영상저장위치</label>
								<div class="col-sm-8">
									<input type="text" class="form-control" id="satlitVidoInnerPath" name="satlitVidoInnerPath" value="D:\Users\JHJ\Videos1\">
								</div>
							</div>
								<div class="form-group m-t-10">
									<label for="input002" class="col-sm-3 control-label form-label">촬영기간</label>
									<div class="col-sm-4">
										<input type="text" id="potogrfBeginDt2" name="potogrfBeginDt2" class="form-control">
									</div>
									<div class="col-sm-1"><span class="txt-in-form">~<span></div>
									<div class="col-sm-4">
										<input type="text" id="potogrfEndDt2" name="potogrfEndDt2" class="form-control">
									</div>
								</div>
								<script>
									let potogrfBeginDt2 = new DatePicker(document.getElementById('potogrfBeginDt2'));
									let potogrfEndDt2 = new DatePicker(document.getElementById('potogrfEndDt2'));
								</script>
							</form> 

						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" id="downloadBtn"><i class="fas fa-plus-square m-r-5"></i>등록</button>
						<button type="button" class="btn btn-gray" data-dismiss="modal" id="modalclose" aria-label="Close"><i class="fas fa-times m-r-5"></i>취소</button>
					</div>
					<div class="load" id="progres2" style="display: none;"></div>
				</div>
			</div>
		</div>
		<div class="sidepanel-s-title m-t-30">
		검색 결과
		</div>
		<div class="panel-body">
			<div class="col-lg-12">
				<div class="panel panel-default" id="ct002_result">
					<%--<ul class="result-list">
						<li>영상명 : 긴급 재난 지역 촬영 2018</li>
						<li>취득일 : 2018/01/02</li>
						<li>영상구분 : 드론영상</li>
					</ul>
					<ul class="result-list">
						<li>영상명 : 태풍 피해 재난 지역 2019-07</li>
						<li>취득일 : 2019/07/02</li>
						<li>영상구분 : 국토위성</li>
					</ul>--%>

				</div>
			</div>
		</div>
		</form>
	</div>


</div>