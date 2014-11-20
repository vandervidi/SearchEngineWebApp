<%@page import="java.awt.print.Printable"%>
<%@ page language="java" contentType="text/html; charset=windows-1255"
    pageEncoding="windows-1255"%>
<%@page import="il.ac.shenkar.SearchEngine.FileSchema" %>
<%@page import="java.util.ArrayList" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1255">
<title>files</title>
</head>
<body>
List of files
<p><% ArrayList<FileSchema> fs = (ArrayList)request.getAttribute("files");
	for(int i=0; i<fs.size(); i++)	
	{
	out.print("<a href=\""+fs.get(i).getPath() + "\">"+fs.get(i).getPath()+"</a><br>");	
	
	}
out.print("blabla");	%></p>
</body>
</html>