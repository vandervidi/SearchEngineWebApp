<%@ page language="java" contentType="text/html; charset=windows-1255"
    pageEncoding="windows-1255"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=windows-1255">
		<title>Insert title here</title>
	</head>
	<body>
		<article id="numberOfResults">
			<p>
				<%
					String content = (String)request.getAttribute("content");
					out.println(content);
				%>
			</p>
		</article>
	</body>
</html>