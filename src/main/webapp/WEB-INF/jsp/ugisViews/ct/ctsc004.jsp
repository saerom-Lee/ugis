<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<html lang="ko" data-dark="false">
<head>
<meta charset="utf-8">
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
</head>

<jsp:include page="./ctsc_header004.jsp"></jsp:include>

</head>

<body>

<!-- START TOP -->
<%--<jsp:include page="./ctsc_top.jsp"></jsp:include>--%>
<%@ include file="../topMenu.jsp"%>
<!-- //////////////////////////////////////////////////////////////////////////// --> 
<!-- START SIDEBAR -->
<div class="sidebar clearfix">
	<div role="tabpanel" class="sidepanel" style="display: block;">
		<div class="tab-content">
			<div role="tabpanel" class="tab-pane active" id="today">

				<div class="sidepanel-m-title">
				이력 검색
				</div>
				<div class="panel-body">			
					<form class="">
					<div class="form-group m-t-10">
						<label class="col-sm-3 control-label form-label">영상 조회</label>
						<div class="col-sm-9">
							<select class="form-basic" id="tnLogList">
								<option value="free">무료영상</option>
								<option value="emer">긴급영상</option>
								<option value="emer">국토위성</option>
							</select>   
						</div>
					</div>
					</form> 
					<div class="form-group m-t-10">
						<label for="input002" class="col-sm-3 control-label form-label">조회기간</label>
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
					<div class="btn-wrap a-cent">
						<a href="#" class="btn btn-default" id='ctsc004-search'><i class="fas fa-search m-r-5"></i>검색</a>
					</div>
					<div class="sidepanel-s-title m-t-30">
					검색 결과
					</div>
					<div class="panel-body">
						<div class="col-lg-12">
							<div class="panel panel-default" id="histInfoView" style="height:500px">
								<ul class="result-list">
									<li>영상조회 종류 : 무료영상</li>
									<li>조회일 : 2018/01/02</li>
									<li class="btn-list-wrap"><a href="#" class="btn btn-light"><i class="far fa-file-alt"></i></a></li>
								</ul>
								<ul class="result-list">
									<li>영상조회 종류 : 국토위성시스템</li>
									<li>조회일 : 2019/07/02</li>
									<li class="btn-list-wrap"><a href="#" class="btn btn-light"><i class="far fa-file-alt"></i></a></li>
								</ul>

							</div>
						</div>
					</div>
				</div>
			</div>
		</div>

	</div>

</div>

    <a href="#" class="sidebar-open-button"><i class="fa fa-bars"></i></a>
    <a href="#" class="sidebar-open-button-mobile"><i class="fa fa-bars"></i></a>



<!-- START CONTENT -->
<div class="content">

	<div class="page-header">
		<h1 class="title">이력정보 조회</h1>

	</div>
	<div class="container-widget">

		<div class="col-md-12">
			<div class="panel panel-default">
				<div class="panel-title">
					<i class="fas fa-table"></i>로그정보
				</div>
				<div class="panel-body table-responsive">
					<table class="table table-hover table-striped">
						<thead>
							<tr>
								<td class="text-center">사용자 ID</td>
								<td class="text-center">조회 종류</td>
								<td class="text-center">다운로드 여부</td>
								<td class="text-center">다운로드 경로</td>
								<td class="text-center">조회일</td>
								<td class="text-center">메타정보</td>
								<td class="text-center"><input type='checkbox' name='animal' value='selectall' onclick='selectAll(this)'/></td>
							</tr>
						</thead>
						<tbody id="logInfoView">
							<tr>
								<td class="text-center"></td>
								<td class="text-center">무료영상</td>
								<td class="text-center">Y</td>
								<td class="text-center">D:\Users\JHJ\Videos</td>
								<td class="text-center">2020.01.01</td>
								<td class="text-center">
									<a href="#" class="btn btn-rounded btn-light"><i class="far fa-file-alt"></i></a>
								</td>
								<td class="text-center"><input type='checkbox' name='animal' value='dog'/></td>
							</tr>
							<tr>
								<td class="text-center"></td>
								<td class="text-center">무료영상</td>
								<td class="text-center">Y</td>
								<td class="text-center">D:\Users\JHJ\Videos</td>
								<td class="text-center">2020.01.01</td>
								<td class="text-center">
									<a href="#" class="btn btn-rounded btn-light"><i class="far fa-file-alt"></i></a>
								</td>
								<td class="text-center"><input type='checkbox' name='animal' value='cat'/></td>
							</tr>
						</tbody>
					</table>  
					<div class="btn-wrap a-right">
						<a href="#" class="btn btn-default"><i class="fas fa-save m-r-5"></i>저장</a>
						<a href="#" class="btn btn-gray"><i class="fas fa-trash-alt m-r-5"></i>삭제</a>
					</div>  
				</div>

			</div>
		</div>

		<div class="col-md-12">
			<div class="panel panel-default">
				<div class="panel-title">
					<i class="fas fa-table"></i>메타데이터 정보
				</div>
				<div class="panel-body table-responsive">
					<table class="table table-hover table-striped">
							<thead>
								<tr>
									<td class="text-center">영상 ID</td>
									<td class="text-center">시작일시</td>
									<td class="text-center">종료일시</td>
									<td class="text-center">위성번호</td>
									<td class="text-center">지도투영내용</td>
									<td class="text-center">스페로이드내용</td>
									<td class="text-center">취득연도</td>
								</tr>
							</thead>
							<tbody id="metaInfoView1">

							</tbody>
							<thead>
								<tr>
									<td class="text-center">취득월</td>
									<td class="text-center">대역수</td>
									<td class="text-center">투영내용</td>
									<td class="text-center">데이텀내용</td>
									<td class="text-center">영상센서명</td>
									<td class="text-center">센서모델명</td>
									<td class="text-center">촬영시작일시</td>
								</tr>
							</thead>
							<tbody id="metaInfoView2">

							</tbody>
							<thead>
								<tr>
									<td class="text-center">촬영종료일시</td>
									<td class="text-center">궤도번호</td>
									<td class="text-center">촬영모드명</td>
									<td class="text-center">RollAngle수</td>
									<td class="text-center">PitchAngle수</td>
									<td class="text-center">생산번호</td>
									<td class="text-center">원시위성영상파일명</td>
								</tr>
							</thead>
							<tbody id="metaInfoView3">

							</tbody>
							<thead>
								<tr>
									<td class="text-center">위성영상파일명</td>
									<td class="text-center">위성영상헤더파일명</td>
									<td class="text-center">위성영상요약파일명</td>
									<td class="text-center">스테레오영상파일명</td>
									<td class="text-center">등록일시</td>
									<td class="text-center">수정일시</td>
									<td class="text-center">사용제약내용</td>
								</tr>
							</thead>
							<tbody id="metaInfoView4">

							</tbody>
							
						</table>  
				</div>

			</div>
		</div>

	</div>
</div>

<script>
function selectAll(selectAll)  {
  const checkboxes 
       = document.getElementsByName('animal');
  
  checkboxes.forEach((checkbox) => {
    checkbox.checked = selectAll.checked;
  })
}
</script>



<script src="/js/ct/ctsc004.js"></script>
<script>
$(document).ready(function() {
	$('#logInfoView').empty();
	ctsc004.search();
});
</script>
</body>
</html>