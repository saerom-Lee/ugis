<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- START TOP -->
<div id="top" class="clearfix">

	<div class="applogo">
		<a href="" class="logo"><img src="img/main_logo.png"></a>
	</div>
	<div class="mn-dep01-wrap">
		<button class="w3-bar-item w3-button tablink w3-red" onclick="openCity(event,'monitoring')">재난 모니터링</button>
		<button class="w3-bar-item w3-button tablink" onclick="openCity(event,'db')">영상DB 수집</button>
		<button class="w3-bar-item w3-button tablink" onclick="openCity(event,'fusion')">공간정보 융·복합</button>
		<button class="w3-bar-item w3-button tablink" onclick="openCity(event,'hadoop')">Hadoop 관리</button>
		<button class="w3-bar-item w3-button tablink" onclick="openCity(event,'add');location.href='cmsc003.do'">긴급공간정보 서비스</button>
	</div>
  
	<div id="monitoring" class="w3-container w3-border city" style="display:none">
		<ul class="mn-dep02-wrap">
			<li><a href="mnsc001.do" class="dep-02">모니터링</a></li>
			<li><a href="mnsc002.do" class="dep-02">수집 설정</a></li>
			<li><a href="mnsc003.do" class="dep-02">재난정보 조회</a></li>
		</ul>
	</div>

	<div id="db" class="w3-container w3-border city">
		<ul class="mn-dep02-wrap">
			<li><a href="/ctsc001.do" id="ctsc001" class="dep-02 <c:if test="${tab eq '001' or tab eq '002' or tab eq '003'}">active</c:if>">영상조회 및 저장</a></li>
			<li><a href="/ctsc004.do" id="ctsc004" class="dep-02 <c:if test="${tab eq '004'}">active</c:if>">이력관리</a></li>
			<li><a href="/ctsc005.do" id="ctsc005" class="dep-02 <c:if test="${tab eq '005'}">active</c:if>">통계분석</a></li>
		</ul>
	</div>

	<div id="fusion" class="w3-container w3-border city" style="display:none">
		<ul class="mn-dep02-wrap">
			<li><a href="cisc001.do" class="dep-02">프로젝트 관리</a></li>
			<li><a href="cisc004.do" class="dep-02">대기보정</a></li>
			<li><a href="cisc005.do" class="dep-02">방사보정</a></li>
			<li><a href="cisc007.do" class="dep-02">영상처리</a></li>
			<li><a href="cisc010.do" class="dep-02">AI 학습</a></li>
			<li><a href="cisc015.do" class="dep-02">객체추출</a></li>
			<li><a href="cisc017.do" class="dep-02">변화탐지</a></li>
			<li><a href="cmsc002.do" class="dep-02">국토위성영상 시뮬레이터</a></li>
		</ul>
	</div>

	<div id="hadoop" class="w3-container w3-border city" style="display:none">
		<ul class="mn-dep02-wrap">
				<li>
					<a href="cisc019.do" id="cisc019" class="dep-02">워크플로우 관리</a>
				</li>
				<li>



					<a href="cisc020.do" class="dep-02">스케줄 관리</a>
				</li>
				<li>
					<a href="cisc021.do" class="dep-02">장비 모니터링</a>

				</li>
				<li>
					<a href="cisc022.do" id="cisc022" class="dep-02">SQL 관리 및 검색</a>
				</li>
				<li>
					<a href="cisc023.do" id="cisc023" class="dep-02">HDFS 브라우즈 관리</a>
				</li>
		</ul>
	</div>

	<div id="add" class="w3-container w3-border city" style="display:none">
		<ul class="mn-dep02-wrap">
			<li><a href="cmsc003.do" class="dep-02"></a></li>
		</ul>
	</div>

	<script>
	function openCity(evt, cityName) {
	  var i, x, tablinks;
	  x = document.getElementsByClassName("city");
	  for (i = 0; i < x.length; i++) {
		x[i].style.display = "none";
	  }
	  tablinks = document.getElementsByClassName("tablink");
	  for (i = 0; i < x.length; i++) {
		tablinks[i].className = tablinks[i].className.replace(" w3-red", "");
	  }
	  document.getElementById(cityName).style.display = "block";
	  evt.currentTarget.className += " w3-red";
	}
	
	// 서브메뉴 active
  	var urlStrTmp = window.location.pathname.replace('/', '');
   	var urlFnameTmp = urlStrTmp.split('.');    	

    var activeId = urlFnameTmp[0];
    var activeElement = document.getElementById(activeId);
    if (activeElement) {
    	activeElement.classList.add('active');
    }	
	</script>
	<ul class="top-right">
		<li class="link">
			<a href="#">관리자님 반갑습니다.</a>
		</li>
		<li class="link">
			<a href="#">도움말</a>
		</li>
		<li class="link">
			<a href="#">로그아웃</a>
		</li>
	</ul>
</div>