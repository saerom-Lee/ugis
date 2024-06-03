<%@ page contentType="application/json; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="org.json.simple.JSONObject"%>
<%@ page isErrorPage="true" %>
<%
	Exception e;
    // json 형태로 리턴하기 위한 json객체 생성
    JSONObject jobj = new JSONObject();
	jobj.put("code", "9999");
	if(exception.getCause() != null) {
		jobj.put("message", exception.getMessage() + " [exception = " + exception.getCause().getMessage() + "]");
	} else {
		jobj.put("message", exception.getMessage());
	}

    // 응답시 json 타입이라는 걸 명시 ( 안해주면 json 타입으로 인식하지 못함 )
	out.print(jobj.toJSONString()); // json 형식으로 출력
%>