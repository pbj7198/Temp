<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
    <%@page import ="org.json.simple.JSONObject" %>
    <%@page import ="org.json.simple.JSONArray" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
JSONArray jsonArray =new JSONArray();
JSONObject object1 = new JSONObject();
object1.put("name","ȫ�浿");
object1.put("age", "19");
object1.put("juso" , "�뱸");
JSONObject object2 =new JSONObject();
object2.put("name", "�ں���");
object2.put("age","25");
object2.put("juso","����");
JSONObject object3 = new JSONObject();
object3.put("name", "���ֹ�");
object3.put("age","35");
object3.put("juso","����");
jsonArray.add(object1);
jsonArray.add(object2);
jsonArray.add(object3);
out.clear();
out.println(jsonArray);
out.flush();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>
</head>
<body>

</body>
</html>