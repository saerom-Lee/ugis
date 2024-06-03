<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko" data-dark="false">
<head>
<meta charset="utf-8">
<title>비밀번호 변경</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=500">
<meta name="apple-mobile-web-app-title" content=""/>
<meta name="robots" content="index,nofollow"/>
<meta name="description" content=""/>
</head>

<!-- ========== Css Files ========== -->
<link href="css/root.css" rel="stylesheet">
<link href="css/all.css" rel="stylesheet">

<script type="text/javascript">
	function changePwd() {
		var id = $('#id');
		var before = $('#before_pwd');
		var after = $('#after_pwd');
		var after_check = $('#after_pwd_check');
		
		if (id.val() == '') {
			alert("아이디를 입력하세요.");
			return;
		}
		
		if (before.val() == '') {
			alert("현재 비밀번호를 입력하세요.");
			return;
		} else if (before.val() == after.val()) {
			alert("현재 비밀번호와 동일합니다.");
			return;
		}
		
		if (after.val() != after_check.val()) {
			alert("새 비밀번호와 비밀번호 확인이 일치하지 않습니다.");
			return;
		}
		
		var formData = $('#changePwdForm').serialize();
		
		$.ajax({
			url: "updateUserPwd.do",
			type: "post",
			data : formData,
			success : function(returnData) {
				alert('비밀번호가 변경되었습니다.');
				window.close();
			},
			error : function(request, status, error) {
				alert("code:" + request.status + "\n" + "message:"
						+ request.respnseText + "\n" + "error:" + error);
			}
		})
	}
</script>

</head>
<body style="background-color: #f5f5f5; overflow: auto;">
	<div class="login-form">
		<form action="updateUserPwd.do" method="post" id="changePwdForm" name="changePwdForm">
			<div class="top">
				<img src="img/login_logo.png" alt="icon" class="icon">
				<h1>비밀번호 변경</h1>
			</div>
			<div class="form-area">
				<div class="group">
					<input type="text" class="form-control" placeholder="아이디" id="id"
						name="id"> <i class="fa fa-user"></i>
				</div>
			
				<div class="group">
					<input type="password" class="form-control" placeholder="현재 비밀번호" id="before_pwd" name="before_pwd">
					<i class="fa fa-key"></i>
				</div>
				
				<div class="group">
					<input type="password" class="form-control" placeholder="새 비밀번호" id="after_pwd" name="after_pwd">
					<i class="fa fa-key"></i>
				</div>
				
				<div class="group">
					<input type="password" class="form-control" placeholder="새 비밀번호 확인" id="after_pwd_check" name="after_pwd_check">
					<i class="fa fa-key"></i>
				</div>
				
				<div class="notice">
					<i class="fas fa-bullhorn m-r-5"></i>현재 비밀번호와 새 비밀번호는 다르게 입력해 주세요.
				</div>
				
				<a href="javascript:changePwd()" class="btn btn-default btn-block">확인</a>
				<a href="javascript:window.close()" class="btn btn-light btn-block">취소</a>
				<!-- <button type="submit" class="btn btn-default btn-block">로그인</button> -->
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