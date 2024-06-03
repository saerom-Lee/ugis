<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>


<!DOCTYPE html>
<html lang="ko" data-dark="false">
<head>
<meta charset="utf-8">
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
</head>

<!-- ========== Css Files ========== -->
<link href="css/root.css" rel="stylesheet">
<link href="css/all.css" rel="stylesheet">

<script type="text/javascript">
	// 로그인 처리 
	function login() {
		if ($("#id").val() == "") {
			alert("ID를 입력하세요.");
			return;
		}

		if ($("#pwd").val() == "") {
			alert("비밀번호를 입력하세요.");
			return;
		}

		var formData = $("#loginForm").serialize();

		$.ajax({
			url : "signinProcess.do",
			type : "post",
			data : formData,
			success : function(returnData) {
				document.loginForm.action = "mnsc001.do";
				document.loginForm.submit();
			},
			error : function(request, status, error) {
				alert("code:" + request.status + "\n" + "message:"
						+ request.respnseText + "\n" + "error:" + error);
			}
		});
	}

	// 비밀번호 변경
	function openChangePwdPopUp() {
		// 비밀번호 변경 팝업창 실행
		window.open("cmsc001_pop_01.do", "비밀번효 변경", "width=543px,height=928px");
	}
</script>

</head>
<body style="background-color: #f5f5f5;">

	<div class="login-form">
		<!-- <form action="index.html"> -->
		<form action="/signinProcess.do" method="post" id="loginForm"
			name="loginForm">
			<sec:csrfInput />
			<div class="top">
				<img src="img/login_logo.png" alt="icon" class="icon">
				<h1>지능형 영상분석 플랫폼</h1>
			</div>
			<div class="form-area">
				<div class="group">
					<input type="text" class="form-control" placeholder="아이디"
						id="login-username" name="username"> <i class="fa fa-user"></i>
				</div>

				<div class="group">
					<input type="password" class="form-control" placeholder="패스워드"
						id="password" name="password"> <i class="fa fa-key"></i>
				</div>

				<div class="notice">
					<i class="fas fa-bullhorn m-r-5"></i>사용하는 이름과 비밀번호를 입력해 주세요.
				</div>

				<!-- <a href="01_UGIS_MN_SC_001.html" class="btn btn-default btn-block">로그인</a> -->
<!-- 				<a href="javascript:login()" class="btn btn-default btn-block">로그인</a> -->
				<a href="javascript:{}" onclick="document.getElementById('loginForm').submit();" class="btn btn-default btn-block">로그인</a>
<!-- 				<input type="submit" value="login"/> -->
				<a href="javascript:openChangePwdPopUp()"
					class="btn btn-default btn-block">비밀번호 변경</a>

				<!-- <button type="submit" class="btn btn-default btn-block">로그인</button> -->

				<!-- Modal -->
				<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
					aria-hidden="true">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal"
									aria-label="Close">
									<span aria-hidden="true">&times;</span>
								</button>
								<h4 class="modal-title">로그인 실패</h4>
							</div>
							<div class="modal-body">
								<div class="normal-txt">
									로그인에 실패하였습니다.<br>이름과 비밀번호를 확인해 주세요.
								</div>
							</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-default">확인</button>
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
		<div class="login-footer">
			(우)16517 경기도 수원시 영통구 월드컵로 92(원천동)<br>전화 : 031) 210-2700 팩스 :
			031) 210-2644
		</div>
	</div>

	<!-- ================================================
jQuery Library
================================================ -->
	<script src="js/jquery.min.js"></script>

	<!-- ================================================
Bootstrap Core JavaScript File
================================================ -->
	<script src="js/bootstrap/bootstrap.min.js"></script>

	<!-- ================================================
Plugin.js - Some Specific JS codes for Plugin Settings
================================================ -->
	<script src="js/plugins.js"></script>

</body>
</html>