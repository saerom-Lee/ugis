<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="form-group m-t-10">
	<label for="input002" class="col-sm-2 control-label form-label">종류</label>
	<div class="col-sm-10">
		<label class="ckbox-container">
			항공영상
			<input data-bind="checked: searchImageNm" value="301" type="checkbox">
			<span class="checkmark"></span>
		</label>
		<label class="ckbox-container">
			국토위성
			<input data-bind="checked: searchImageNm" value="302" type="checkbox">
			<span class="checkmark"></span>
		</label>
		<label class="ckbox-container">
			기타위성
			<input data-bind="checked: searchImageNm" value="303" type="checkbox">
			<span class="checkmark"></span>
		</label>
	</div>
</div>