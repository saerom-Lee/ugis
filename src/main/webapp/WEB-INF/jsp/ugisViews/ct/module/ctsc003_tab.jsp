<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- Nav tabs -->
<ul class="nav nav-tabs" role="tablist">
	<li role="presentation" class="active" style="width:33.333%">
		<a href="#tab-01" aria-controls="tab-01" role="tab" data-toggle="tab" class="" aria-expanded="false">
			<i class="fas fa-satellite-dish"></i><br>무료위성영상
		</a>
	</li>
	<li role="presentation" class="" style="width:33.333%">
		<a href="#tab-02" aria-controls="tab-02" role="tab" data-toggle="tab" class="" aria-expanded="false">
			<i class="fas fa-camera"></i><br>긴급촬영영상
		</a>
	</li>
	<li role="presentation" class="" style="width:33.333%">
		<a href="#tab-03" aria-controls="tab-03" role="tab" data-toggle="tab" aria-expanded="true" class="active">
			<i class="fas fa-satellite"></i><br>국토위성영상
		</a>
	</li>
</ul>