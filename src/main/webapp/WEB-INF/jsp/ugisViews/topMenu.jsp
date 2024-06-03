<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />


<!DOCTYPE html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title></title>
</head>

<link href="css/root.css" rel="stylesheet">
<link href="css/all.css" rel="stylesheet">

<!-- <script src="js/jquery.min.js"></script> -->
<!-- <script src="js/bootstrap/bootstrap.min.js"></script>  -->



<body>
	<div id="top" class="clearfix">

		<div class="applogo">
			<a href="" class="logo">
				<img src="img/main_logo.png">
			</a>
		</div>
		<div class="mn-dep01-wrap">
			<button class="w3-bar-item w3-button tablink w3-red" onclick="openCity(event,'monitoring')">재난 모니터링</button>
			<button class="w3-bar-item w3-button tablink" onclick="openCity(event,'add')">긴급공간정보 서비스</button>
			<button class="w3-bar-item w3-button tablink" onclick="openCity(event,'db')">영상DB 관리</button>
			<!-- <button class="w3-bar-item w3-button tablink" onclick="openCity(event,'fusion')">공간정보 융·복합</button> -->
			<button class="w3-bar-item w3-button tablink" onclick="openCity(event,'fusion')">AI 데이터셋 관리</button>
			<button class="w3-bar-item w3-button tablink" onclick="openCity(event,'hadoop')">Hadoop 관리</button>
			<!-- <button class="w3-bar-item w3-button tablink" onclick="location.href='cmsc003.do'">긴급공간정보 생성</button> -->
			
		</div>

		<div id="monitoring" class="w3-container w3-border city" style="display: none">
			<ul class="mn-dep02-wrap">
				<li>
					<a href="mnsc001.do" id="mnsc001" class="dep-02">모니터링</a>
				</li>
				<li>
					<a href="mnsc002.do" id="mnsc002" class="dep-02">수집 설정</a>
				</li>
				<li>
					<a href="mnsc003.do" id="mnsc003" class="dep-02">재난정보 조회</a>
				</li>
			</ul>
		</div>

		<div id="db" class="w3-container w3-border city" style="display: none">
			<ul class="mn-dep02-wrap">
				<li>
					<a href="ctsc001.do" id="ctsc001" class="dep-02">영상조회 및 저장</a>
				</li>
				<li>
					<a href="ctsc004.do" id="ctsc004" class="dep-02">이력관리</a>
				</li>
				<li>
					<a href="ctsc005.do" id="ctsc005" class="dep-02">통계분석</a>
				</li>
				<li>
					<a href="cmsc002.do" id="cmsc002" class="dep-02">국토위성영상 시뮬레이터</a>
				</li>
			</ul>
		</div>

		<div id="fusion" class="w3-container w3-border city" style="display: none">
			<ul class="mn-dep02-wrap">
<!-- 				<li> -->
<!-- 					<a href="cisc001.do" id="cisc001" class="dep-02">프로젝트 관리</a> -->
<!-- 				</li> -->
<!-- 				<li> -->
<!-- 					<a href="cisc004.do" id="cisc004" class="dep-02">대기보정</a> -->
<!-- 				</li> -->
<!-- 				<li> -->
<!-- 					<a href="cisc005.do" id="cisc005" class="dep-02">방사보정</a> -->
<!-- 				</li> -->
<!-- 				<li> -->
<!-- 					<a href="cisc007.do" id="cisc007" class="dep-02">영상처리</a> -->
<!-- 				</li> -->
				<li>
					<a href="cisc010.do" id="cisc010" class="dep-02">AI 학습</a>
				</li>
<!-- 				<li> -->
<!-- 					<a href="cisc015.do" id="cisc015" class="dep-02">객체추출</a> -->
<!-- 				</li> -->
<!-- 				<li> -->
<!-- 					<a href="cisc017.do" id="cisc017" class="dep-02">변화탐지</a> -->
<!-- 				</li> -->
<!-- 				<li> -->
<!-- 					<a href="cmsc002.do" id="cmsc002" class="dep-02">국토위성영상 시뮬레이터</a> -->
<!-- 				</li> -->
			</ul>
		</div>

		<div id="hadoop" class="w3-container w3-border city" style="display: none">
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

		<div id="add" class="w3-container w3-border city" style="display: none">
			<ul class="mn-dep02-wrap">
				<li>
					<a href="cisc001.do" id="cisc001" class="dep-02">프로젝트 관리</a>
				</li>
				<li>
					<a href="cisc004.do" id="cisc004" class="dep-02">대기보정</a>
				</li>
				<li>
					<a href="cisc005.do" id="cisc005" class="dep-02">방사보정</a>
				</li>
				<li>
					<a href="cmsc003.do" id="cmsc003" class="dep-02">긴급공간정보 생성/관리</a>
				</li>
				<li>
					<a href="cisc007.do" id="cisc007" class="dep-02">영상처리</a>
				</li>
				<li>
					<a href="cisc015.do" id="cisc015" class="dep-02">객체추출</a>
				</li>
				<li>
					<a href="cisc017.do" id="cisc017" class="dep-02">변화탐지</a>
				</li>
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
		</script>
		<ul class="top-right">
			<li class="link">
				<a href="#">관리자님 반갑습니다.</a>
			</li>
			<li class="link">
				<a href="#">도움말</a>
			</li>
			<li class="link">
				<a href="logout.do">로그아웃</a>
			</li>
		</ul>
	</div>
</body>




<script type="text/javascript">
    function getURL() {
    	//var urlstr = window.location.pathname;
    	var urlStrTmp = window.location.pathname.replace('/', '');
    	var urlFnameTmp = urlStrTmp.split('.');
    	var urlFname = urlFnameTmp[0].substring(0,4);
    	var urlFname2 =urlFnameTmp[0].substring(4,8);

        switch(urlFname){
        case "mnsc":
        	document.getElementById("monitoring").style.display = "block";
        	//$("#monitoring").css("dsiplay","block");
        	//$("#monitoring").show()
        	break;
        case "ctsc":
        	document.getElementById("db").style.display = "block";
        	break;

        case "cmsc":
			if(urlFname2=="003") {
				document.getElementById("add").style.display = "block";
			}
			else if(urlFname2=="002"){
        		// document.getElementById("fusion").style.display = "block";
        		document.getElementById("db").style.display = "block";
        	}else{
        		document.getElementById("fusion").style.display = "none";
        	}
        	break;
        case "cisc":
        	if(urlFname2=="001" || urlFname2=="004" || urlFname2=="005" || urlFname2=="007" || urlFname2=="015" || urlFname2=="017"){
        		document.getElementById("add").style.display = "block";
        	}
        	else if(urlFname2=="019" || urlFname2=="020" || urlFname2=="021" || urlFname2=="022" || urlFname2=="023"){
        		document.getElementById("hadoop").style.display = "block";
        	}else{
        		document.getElementById("fusion").style.display = "block";
        	}
        	break;
        default:

        }

        //document.getElementById("urlFname2").style.display = "block";

        // active 추가
        var activeId = urlFnameTmp[0];
        var activeElement = document.getElementById(activeId);
        if (activeElement) {
        	activeElement.classList.add('active');
        }

        //if urlfirstname[0]==""
    }
    getURL();
</script>




</html>