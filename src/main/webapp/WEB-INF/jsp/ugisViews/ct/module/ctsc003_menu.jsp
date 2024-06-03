<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div role="tabpanel" class="tab-pane" id="tab-03">

	<div class="sidepanel-m-title">
	 국토위성영상 검색
	</div>
	<div class="panel-body">			
		<form class="">
		<div class="form-group m-t-10">
			<label class="col-sm-4 control-label form-label">영상정보 종류</label>
			<div class="col-sm-8">
				<select class="form-basic">
					<option>전체</option>
				</select>            
			</div>
		</div> 		
		<div class="form-group m-t-10">
			<label for="input002" class="col-sm-3 control-label form-label">촬영기간</label>
			<div class="col-sm-4">
				<input type="text" id="datepicker5" class="form-control">
			</div>
			<div class="col-sm-1"><span class="txt-in-form">~<span></div>
			<div class="col-sm-4">
				<input type="text" id="datepicker6" class="form-control">
			</div>
		</div>
		<script>
			let datepicker5 = new DatePicker(document.getElementById('datepicker5'));
			let datepicker6 = new DatePicker(document.getElementById('datepicker6'));
		</script>
		<div class="form-group m-t-10">
			<label class="col-sm-12 control-label form-label">재난지역</label>
			<div class="col-sm-4">
				<select class="form-basic" id="si">
				<option value="00">시도</option>
					<c:forEach var="result" items="${siList}" varStatus="status">
					<option value="${result.bjcd}" minx="${result.stXmin}" miny="${result.stYmin}" maxx="${result.stXmax}" maxy="${result.stYmax}">
						<c:out value="${result.name}"/></option>
					</c:forEach>
				</select>       
			</div>
			<div class="col-sm-4">
				<select class="form-basic" id="sgg">
					<option>시군구</option>
				</select>            
			</div>
			<div class="col-sm-4">
				<select class="form-basic" id="emd">
					<option>읍면동</option>
				</select>            
			</div>
		</div>
		<div class="form-group m-t-10">
			<label for="input002" class="col-sm-3 control-label form-label">좌표</label>
			<div class="col-sm-9">
				<div class="col-sm-2 in-tit">
					ULX
				</div>
				<div class="col-sm-4">
					<input type="text" class="form-control area" id="ulx">
				</div>
				<div class="col-sm-2 in-tit">
					ULY
				</div>
				<div class="col-sm-4">
					<input type="text" class="form-control area" id="uly">
				</div>
				<div class="col-sm-2 in-tit m-t-5">
					LRX
				</div>
				<div class="col-sm-4 m-t-5">
					<input type="text" class="form-control area" id="lrx">
				</div>
				<div class="col-sm-2 in-tit m-t-5">
					LRY
				</div>
				<div class="col-sm-4 m-t-5">
					<input type="text" class="form-control area" id="lry">
				</div>
			</div>
		</div>
		<div class="btn-wrap a-cent">
			<a href="#" class="btn btn-info roi"><i class="fas fa-mouse-pointer m-r-5"></i>사용자 정의 ROI</a>
			<a href="#" class="btn btn-default"><i class="fas fa-search m-r-5"></i>검색</a>
		</div>
		<div class="sidepanel-s-title m-t-30">
		검색 결과
		</div>
		<div class="panel-body">
			<div class="col-lg-12">
				<div class="panel panel-default">
					<ul class="result-list">
						<li>종류 : 국토위성영상</li>
						<li>촬영일 : 2018/01/02</li>
						<li class="btn-list-wrap"><a href="#" class="btn btn-light" data-toggle="modal" data-target="#myModal2"><i class="fas fa-download"></i></a></li>
					</ul>
					<ul class="result-list">
						<li>영상명 : 국토위성영상</li>
						<li>촬영일 : 2019/07/02</li>
						<li class="btn-list-wrap"><a href="#" class="btn btn-light"><i class="fas fa-download"></i></a></li>
					</ul>

				</div>
			</div>
		</div>
		<div class="modal fade" id="myModal2" tabindex="-1" role="dialog" aria-hidden="true">
			<div class="modal-dialog modal-sm">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						<h4 class="modal-title">다운로드 설정</h4>
					</div>
					<div class="modal-body">
						<div class="panel-body">					
							<form class="">
							<div class="form-group m-t-10">
								<label for="input002" class="col-sm-4 control-label form-label">입력파일위치</label>
								<div class="col-sm-6">
									<input type="text" class="form-control" id="input002" value="D:\Users\JHJ\Videos">
								</div>
								<div class="col-sm-2">
									<button type="button" class="btn btn-default btn-icon"><i class="fas fa-ellipsis-h"></i></button>
								</div>
							</div>
							<div class="form-group m-t-10">
								<label for="input002" class="col-sm-4 control-label form-label">영상저장위치</label>
								<div class="col-sm-6">
									<input type="text" class="form-control" id="input002" value="D:\Users\JHJ\Videos">
								</div>
								<div class="col-sm-2">
									<button type="button" class="btn btn-default btn-icon"><i class="fas fa-ellipsis-h"></i></button>
								</div>
							</div>
							</form> 

						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default"><i class="fas fa-plus-square m-r-5"></i>등록</button>
						<button type="button" class="btn btn-gray" data-dismiss="modal" aria-label="Close"><i class="fas fa-times m-r-5"></i>취소</button>
					</div>
				</div>
			</div>
		</div>
		</form>
	</div>


</div>