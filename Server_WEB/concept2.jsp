<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="java.sql.*"%>
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="org.json.simple.JSONObject"%>
<%@ page import="org.json.simple.JSONArray"%>
<!--개념 서버  -->
<%
	request.setCharacterEncoding("utf-8");//utf-8인코딩
	String strUrl = "jdbc:mysql://168.131.152.161/javachip?useUnicode=true&characterEncoding=utf-8";
	String strUser = "root";
	String strPassword = "apmsetup";
	Statement statement;
	ResultSet resultset;
	JSONObject jsonroot = new JSONObject();
	String cid = request.getParameter("cid"); // id로 파라미터 받아오기
	int tcid = Integer.parseInt(cid);
	try {
		Class.forName("com.mysql.jdbc.Driver");
		Connection connection = DriverManager.getConnection(strUrl, strUser, strPassword);
		statement = connection.createStatement();
		String sql = "select *from lecture";
		resultset = statement.executeQuery(sql);
		String url = "";
		while (resultset.next()) {
			if (tcid == resultset.getInt("cid"))
				url = resultset.getString("url");
		}
		sql = "select * from concept"; // 개념 table값 가져오기
		resultset = statement.executeQuery(sql);
		JSONObject jobject;

		JSONArray jArray = new JSONArray();
		int count = 0;
		while (resultset.next()) {
			if (resultset.getInt("cid") == tcid) {
				jobject = new JSONObject();
				jobject.put("url", url); //cid 값 삽입
				jobject.put("ctitle", resultset.getString("ctitle")); // ctitle 값 삽입
				jobject.put("cinfo", resultset.getString("cinfo")); // 개념 내용 삽입
				jArray.add(0, jobject); // 개념 개수만큼 값 삽입
			}
		}

		jsonroot.put("result", jArray); // json 형식으로 값 전송
		PrintWriter pw = response.getWriter();
		pw.print(jsonroot); // web상에 출력
		pw.flush();
		pw.close();

		connection.close();
	} catch (Exception e) {
		e.printStackTrace();
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>
</head>
<body>

</body>
</html>