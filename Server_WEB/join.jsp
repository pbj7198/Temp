<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<%@page import="java.sql.*"%>
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="org.json.simple.JSONObject"%>
<%@ page import="org.json.simple.JSONArray"%>
<!--ȸ�����Լ��� -->
<%
	request.setCharacterEncoding("utf-8");//utf-8���ڵ�
	String strUrl = "jdbc:mysql://168.131.152.161/javachip?useUnicode=true&characterEncoding=utf-8";
	String strUser = "root";
	String strPassword = "apmsetup";
	String id = request.getParameter("id"); // id���۹ޱ�
	String password = request.getParameter("password"); // password���۹ޱ�
	String name = request.getParameter("name"); // �̸� ���۹ޱ�
	String gender = request.getParameter("gender"); //���� ���۹ޱ�
	String email = request.getParameter("email"); // email ���۹ޱ�
	Statement statement;
	ResultSet resultset;
	JSONObject jsonroot = new JSONObject();

	int gen = -1;
	if (gender.equals("men")) //�����϶�
		gen = 0;
	else if (gender.equals("women")) // �����϶�
		gen = 1;
	try {
		Class.forName("com.mysql.jdbc.Driver");
		Connection connection = DriverManager.getConnection(strUrl, strUser, strPassword);
		statement = connection.createStatement();

		String sql2 = "select *from user"; // db user table ���ǹ�
		resultset = statement.executeQuery(sql2);
		int i = 0;
		while (resultset.next()) {
			if (resultset.getString("id").equals(id)) { // �ش� id�� �ߺ��̵Ǹ� i�� 1����
				i = 1;
				break;
			}
		}
		if (i == 1) {
			jsonroot.put("result", 1);//����(�ߺ�)
		} else if (i == 0) { // �ߺ��Ǵ� ���� ������
			String sql = "insert into user(id,pw,name,gender,email) values ('" + id + "', '" + password + "', '"
					+ name + "', '" + gen + "', '" + email + "')";
			//ȸ������ 
			statement.executeUpdate(sql);
			jsonroot.put("result", 0);//����
		}
		PrintWriter pw = response.getWriter();
		pw.print(jsonroot);
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