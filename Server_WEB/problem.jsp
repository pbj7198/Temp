<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="java.sql.*"%>
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="org.json.simple.JSONObject"%>
<%@ page import="org.json.simple.JSONArray"%>
<!--문제 서버  -->
<%
	request.setCharacterEncoding("utf-8");//utf-8인코딩
	String strUrl = "jdbc:mysql://168.131.152.161/javachip?useUnicode=true&characterEncoding=utf-8";
	String strUser = "root";
	String strPassword = "apmsetup";
	Statement statement;
	Statement statement2;
	ResultSet resultset;
	ResultSet resultset2;
	String tmpcid = request.getParameter("cid");
	int cid = Integer.parseInt(tmpcid);
	JSONObject jsonroot = new JSONObject();

	try {
		Class.forName("com.mysql.jdbc.Driver");
		Connection connection = DriverManager.getConnection(strUrl, strUser, strPassword);
		statement = connection.createStatement();
		statement2 = connection.createStatement();
		String sql = "select * from problem";
		resultset = statement.executeQuery(sql);
		JSONObject jobject;
		JSONArray jArray = new JSONArray();
		boolean bool[] = new boolean[4];
		String ch[] = { "ch1", "ch2", "ch3", "ch4" };
		HashMap<String, String> h = new HashMap<>();

		boolean bool2 = false;

		while (resultset.next()) {

			jobject = new JSONObject();
			if (cid == resultset.getInt("cid")) {
				if (resultset.getString("pinfo") == null) {
					jobject.put("pinfo", "not");
					jobject.put("ch1", 1);
					jobject.put("ch2", 1);
					jobject.put("ch3", 1);
					jobject.put("ch4", 1);
					jArray.add(0, jobject);
				} else { // 문제가있을때 
					jobject.put("pinfo", resultset.getString("pinfo")); // 문제를 삽입
					h.put("ch4", resultset.getString("panswer"));
					int pid = resultset.getInt("pid"); // pid값을 불러와서
					String sql2 = "select *from answerchoice";
					resultset2 = statement2.executeQuery(sql2);

					while (resultset2.next()) {
						if (pid == resultset2.getInt("pid")) {//pid 에 일치하는 선택지 삽입
							h.put("ch1", resultset2.getString("ck1"));
							h.put("ch2", resultset2.getString("ck2"));
							h.put("ch3", resultset2.getString("ck3"));

						}
					}
					int count = 0;
					while (true) {

						bool2 = false;
						for (int i = 0; i < 4; i++) {
							if (bool[i] == false) {
								bool2 = true;
								break;
							}
						}
						if (bool2) {
							int num = (int) (Math.random() * 4);
							if (!bool[num]) {
								//out.println(num);
								if (count == 0) {
									jobject.put("ch1", h.get(ch[num]));
									bool[num] = true;
								} else if (count == 1) {
									jobject.put("ch2", h.get(ch[num]));
									bool[num] = true;
								} else if (count == 2) {
									jobject.put("ch3", h.get(ch[num]));
									bool[num] = true;
								} else if (count == 3) {
									jobject.put("ch4", h.get(ch[num]));
									bool[num] = true;
								}
								count++;

							} else
								continue;
						} else {
							break;
						}
					}
					jArray.add(0, jobject);
				}
			}
		}
		jsonroot.put("result", jArray);
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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>