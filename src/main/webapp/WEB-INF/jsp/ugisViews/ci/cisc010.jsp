<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

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

<link rel="stylesheet" href="${contextPath}/css/knockout.contextmenu.css">
<script type="text/javascript" src="${contextPath}/js/jquery.min.js"></script>
<script type="text/javascript" src="${contextPath}/js/jquery-ui/jquery-ui.min.js"></script>
<script type="text/javascript" src="${contextPath}/js/jquery-ui/jquery.blockUI.js"></script>
<script type="text/javascript" src="${contextPath}/js/bootstrap/bootstrap.min.js"></script>
<script type="text/javascript" src="${contextPath}/js/knockout/knockout-3.5.1.js"></script>
<script type="text/javascript" src="${contextPath}/js/knockout/knockout.mapping-latest.js"></script>
<script type="text/javascript" src="${contextPath}/js/knockout/knockout.validation.min.js"></script>
<script type="text/javascript" src="${contextPath}/js/knockout/knockout.contextmenu.min.js"></script>
<script type="text/javascript" src="${contextPath}/js/knockout/knockout-custom.js"></script>
<script type="text/javascript" src="${contextPath}/js/knockout/jquery.serialize-object.min.js"></script>
<%--<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/proj4js/1.1.0/proj4js-combined.min.js"></script>--%>
	<script type="text/javascript" src="js/map/proj4js-combined.js"></script>
<script type="text/javascript" src="${contextPath}/js/map/v6.7.0/ol.js"></script>
<script type="text/javascript" src="${contextPath}/js/map/map.js"></script>
<script type="text/javascript" src="${contextPath}/js/map/gis.js"></script>
<script type="text/javascript" src="${contextPath}/js/amcharts.js"></script>
<script type="text/javascript" src="${contextPath}/js/serial.js"></script>
<!-- <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-contextmenu/2.9.2/jquery.contextMenu.js" integrity="sha512-2ABKLSEpFs5+UK1Ol+CgAVuqwBCHBA0Im0w4oRCflK/n8PUVbSv5IY7WrKIxMynss9EKLVOn1HZ8U/H2ckimWg==" crossorigin="anonymous" referrerpolicy="no-referrer"></script> -->
<script type="text/javascript">var contextPath = '${contextPath}';</script>
</head>

<body>

	<c:import url="../topMenu.jsp" />

	<!-- START SIDEBAR -->
	<div class="sidebar clearfix">
		<div role="tabpanel" class="sidepanel" style="display: block;">

			<!-- Nav tabs -->
			<ul class="nav nav-tabs" role="tablist">
				<li role="presentation" class="active" style="width: 25%;">
					<a href="#dataSetModel" aria-controls="dataSetModel" role="tab" data-toggle="tab" aria-expanded="true" class="active">
						<i class="fas fa-plus-square"></i><br>AI 데이터셋
					</a>
				</li>
				<li role="presentation" class="" style="width: 25%;">
					<a href="#aiLearningModel" aria-controls="aiLearningModel" role="tab" data-toggle="tab" class="" aria-expanded="false">
						<i class="fas fa-pen"></i><br>AI 학습
					</a>
				</li>
				<li role="presentation" class="" style="width: 25%;">
					<a href="#prfomncEvlModel" aria-controls="prfomncEvlModel" role="tab" data-toggle="tab" class="" aria-expanded="false">
						<i class="fas fa-check-square"></i><br>성능평가
					</a>
				</li>
				<li role="presentation" class="" style="width: 25%;">
					<a href="#prfomncEvlResultModel" aria-controls="prfomncEvlResultModel" role="tab" data-toggle="tab" class="" aria-expanded="false">
						<i class="fas fa-clipboard-list"></i><br>성능평가결과
					</a>
				</li>
			</ul>

			<!-- Tab panes -->
			<div class="tab-content">

				<!-- AI 데이터셋 -->
				<%@ include file="cisc010_01.jsp" %>

				<!-- AI 학습 -->
				<%@ include file="cisc010_02.jsp" %>

				<!-- 성능 평가 -->
				<%@ include file="cisc010_03.jsp" %>

				<!-- 성능 평가 결과 -->
				<%@ include file="cisc010_04.jsp" %>

			</div>

		</div>
	</div>

	<a href="#" class="sidebar-open-button">
		<i class="fa fa-bars"></i>
	</a>
	<a href="#" class="sidebar-open-button-mobile">
		<i class="fa fa-bars"></i>
	</a>

	<!-- START CONTENT -->
    <div class="content">
        <div class="map-wrap-n" id="map" style="background-size:100% auto"></div>
    </div>

	<!-- 완료 팝업 -->
	<%@ include file="cisc010_complete_pop.jsp" %>

	<script type="text/javascript" src="${contextPath}/js/ci/cisc010/cmmn-module.js"></script>
	<script type="text/javascript" src="${contextPath}/js/ci/cisc010/dataset-module.js"></script>
	<script type="text/javascript" src="${contextPath}/js/ci/cisc010/ai-learning-module.js"></script>
	<script type="text/javascript" src="${contextPath}/js/ci/cisc010/prfomnc-evl-module.js"></script>
	<script type="text/javascript" src="${contextPath}/js/ci/cisc010/prfomnc-evl-result-module.js"></script>
	<script type="text/javascript" src="${contextPath}/js/ci/cisc010/ai-learning-complete-module.js"></script>
	<script type="text/javascript">
		//학습 완료 체크 interval
		const cmmnModule = new CmmnModule();
    	setInterval(function() {
			var isModal = $(".modal").hasClass("in");
			var isActive = $('.nav-tabs li:eq(3)').hasClass("active");
			if(!isModal) {
				const params = {
					streYn : "N"
				};
				$.ajax({
	                url: contextPath+'/cisc010/getAiModelList.do',
	                type: 'POST',
	                contentType: 'application/json; charset=utf-8',
	                data: JSON.stringify(params),
	                global: false
	            }).done(function(response){
	            	var baseModelList = response.baseModelList;
	            	var userModelList = response.userModelList;

	        		$.each(baseModelList, function (idx, val) {
	        			if(val.progrsSttusCd == "444" && val.streYn == "N") {
	         				cmmnModule.getAiLearningCompleteModule().openPopup(val.modelId);
	        				return false;
	        			}
	        		});

	        		$.each(userModelList, function (idx, val) {
	        			if(val.progrsSttusCd == "444" && val.streYn == "N") {
	         				cmmnModule.getAiLearningCompleteModule().openPopup(val.modelId);
	        				return false;
	        			}
	        		});
	            });
			}

			if(!isModal && isActive) {
				//목록 선택 초기화
				const params = {
					streYn : "N"
				};
				$.ajax({
	                url: contextPath+'/cisc010/getPrfomncEvlResultList.do',
	                type: 'POST',
	                contentType: 'application/json; charset=utf-8',
	                data: JSON.stringify(params),
	                global: false
	            }).done(function(response){
	        		$.each(response.prfomncEvlResultList, function (idx, val) {
	        			if(val.progrsSttusCd == "444" && val.streYn == "N") {
	         				cmmnModule.getAiLearningCompleteModule().openPopupPrfomncEvlResult(val.modelId, val.aiModelId, val.modelPrfomncEvlId);
	        				return false;
	        			}
	        		});
	            });
			}
    	}, 5000);

// 		//성능평가 결과 interval
//     	setInterval(function() {
// 			var isModal = $(".modal").hasClass("in");
//     	}, 5000);


		//1. 맵생성.
	    let map = createMap('map');
	    let baselyaer = baselayer();
	    map.addLayer(baselyaer);

	    //2. 선택기능 활성화 roi
	    let select = new Select(map);
	    const imageLayer = new ol.layer.Image();
	    map.addLayer(imageLayer);

	    const imageExtent = [957225.1387,1921135.1404,959008.6342,1922918.6358];
	    map.getView().fit(imageExtent);
	</script>

</body>
</html>