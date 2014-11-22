<%@page import="java.awt.print.Printable"%>
<%@ page language="java" contentType="text/html; charset=windows-1255"
    pageEncoding="windows-1255"%>
<%@page import="il.ac.shenkar.SearchEngine.FileSchema" %>
<%@page import="java.util.ArrayList" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1255">
<link href='http://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css'>
<link rel="stylesheet" href="../views/includes/css/bootstrap.css">
<link rel="stylesheet" href="../views/includes/css/style.css">

<title>files</title>
</head>
<body>
<nav id="topNav"><ul class="nav nav-pills">
<li role="presentation"><a id="adminConfiguration" href="../controller/adminMenu"><span class="glyphicon glyphicon-wrench" aria-hidden="true"></span>
 Configuration</a></li>
 <li role="presentation"><a id="adminConfiguration" href="../controller/"><span class="glyphicon glyphicon-search" aria-hidden="true"></span>
 Search</a></li>
</ul></nav>

<h1 id="titleFiles">List of indexed files</h1>
<table class="table table-bordered table-hover">

<% ArrayList<FileSchema> fs = (ArrayList)request.getAttribute("files");
out.print("<tr class=\"active\"><td><strong>File name</strong></td> <td><strong>Enable / Disable</strong></td></tr>");
	for(int i=0; i<fs.size(); i++)	
	{
		if(fs.get(i).getDeleted()==0){
			out.print("<tr class=\"success\"><td><a class='filepathlink' target=\"_blank\" href=\""+fs.get(i).getPath() + "\">"+fs.get(i).getPath()+"</a></td><td>		<a href=\"../controller/enableOrDisable?action=disable&docNum="+fs.get(i).getDocNum()+"\"><button type=\"button\" class=\"btn btn-danger\">Disable</button></a>		</td></tr> ");	
			}else{
			out.print("<tr class=\"danger\"><td><a class='filepathlink' target=\"_blank\"  href=\""+fs.get(i).getPath() + "\">"+fs.get(i).getPath()+"</a></td><td>		<a href=\"../controller/enableOrDisable?action=enable&docNum="+fs.get(i).getDocNum()+"\"> <button type=\"button\" class=\"btn btn-success\">Enable</button></a>		</td></tr> ");	
			}
	}
	%>
</table>
</body>
</html>