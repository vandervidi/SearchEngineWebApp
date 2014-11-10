<!-- <%@ page language="java" contentType="text/html; charset=windows-1255"
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
					String filePath = (String)request.getAttribute("filePath");
					String content = (String)request.getAttribute("content");
					String isPic = (String)request.getAttribute("isPic");
					if (isPic.equals("true")){
						out.println("<img src='"+filePath+"'></img>");
					}else{
						out.println(content);
					}
				%>
			</p>
		</article>
	</body>
</html>
-->