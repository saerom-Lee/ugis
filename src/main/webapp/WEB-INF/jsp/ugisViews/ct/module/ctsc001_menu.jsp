<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Start tab-01 -->
<div role="tabpanel" class="tab-pane active" id="tab-01">

	<div class="sidepanel-m-title">
	무료위성영상 자동다운로드 설정
	</div>
	<div class="panel-body">			
		<form class="" id="form1">
		<div class="form-group m-t-10">
			<label class="col-sm-3 control-label form-label">위성 종류</label>
			<div class="col-sm-9">
				<select class="form-basic" id="satlitNo" name="satlitNo">
					<option value="landsat_etm_c1">Landsat 7 ETM+ Collection 1 Level 1</option>
					<option value="landsat_etm_c2_l1">Landsat 7 ETM+ Collection 2 Level 1</option>
					<option value="landsat_etm_c2_l2">Landsat 7 ETM+ Collection 2 Level 2</option>
					<option value="landsat_8_c1">Landsat 8 Collection 1 Level 1</option>
					<option value="landsat_ot_c2_l1">Landsat 8 Collection 2 Level 1</option>
					<option value="landsat_ot_c2_l2">Landsat 8 Collection 2 Level 1</option>
					<option value="sentinel_2a">Sentinel 2A</option>
				</select>
			</div>
		</div> 		
		<div class="form-group m-t-10">
			<label for="input002" class="col-sm-3 control-label form-label">촬영기간</label>
			<div class="col-sm-4">
				<input type="text" id="potogrfBeginDt" name="potogrfBeginDt" class="form-control">
			</div>
			<div class="col-sm-1"><span class="txt-in-form">~<span></div>
			<div class="col-sm-4">
				<input type="text" id="potogrfEndDt" name="potogrfEndDt" class="form-control">
			</div>
		</div>
		<script src="/js/datepicker.js"></script>
		<script>
			let potogrfBeginDt = new DatePicker(document.getElementById('potogrfBeginDt'));
			let potogrfEndDt = new DatePicker(document.getElementById('potogrfEndDt'));
		</script>
		<div class="form-group m-t-10">
			<label for="input002" class="col-sm-3 control-label form-label">대상지역</label>
			<div class="col-sm-9">
				<div class="col-sm-2 in-tit">
					ULX
				</div>
				<div class="col-sm-4">
					<input type="text" class="form-control area" id="ulx" name="ulx">
				</div>
				<div class="col-sm-2 in-tit">
					ULY
				</div>
				<div class="col-sm-4">
					<input type="text" class="form-control area" id="uly" name="uly">
				</div>
				<div class="col-sm-2 in-tit m-t-5">
					LRX
				</div>
				<div class="col-sm-4 m-t-5">
					<input type="text" class="form-control area" id="lrx" name="lrx">
				</div>
				<div class="col-sm-2 in-tit m-t-5">
					LRY
				</div>
				<div class="col-sm-4 m-t-5">
					<input type="text" class="form-control area" id="lry" name="lry">
				</div>
			</div>
		</div>
		<div class="btn-wrap a-cent">
			<a href="#" class="btn btn-info roi"><i class="fas fa-mouse-pointer m-r-5"></i>사용자 정의 ROI</a>
		</div>
		<div class="form-group m-t-10">
			<label for="input002" class="col-sm-4 control-label form-label">외부파일위치</label>
			<div class="col-sm-8">
				<input type="text" class="form-control" id="satlitVidoExtrlPath" name="satlitVidoExtrlPath" value="d:\test\usgs\">
			</div>
		</div>
		<div class="form-group m-t-10">
			<label for="input002" class="col-sm-4 control-label form-label">영상저장위치</label>
			<div class="col-sm-8">
				<input type="text" class="form-control" id="satlitVidoInnerPath" name="satlitVidoInnerPath" value="d:\test\usgs1\">
			</div>
		</div>
		<div class="form-group m-t-10">
			<label for="input002" class="col-sm-4 control-label form-label">시간설정</label>
			<div class="col-sm-8">
				<select class="form-basic" id="dwldDate" name="dwldDate">
					<option></option>
				</select>
			</div>
		</div>
		<div class="btn-wrap a-cent">
			<a href="#" class="btn btn-default" id="downloadBtn"><i class="fas fa-search m-r-5"></i>자동 다운로드</a>
		</div>
		</form>
	</div>
</div>