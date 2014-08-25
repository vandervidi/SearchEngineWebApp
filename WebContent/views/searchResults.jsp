<%@ page language="java" contentType="text/html; charset=windows-1255"
    pageEncoding="windows-1255"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="il.ac.shenkar.SearchEngine.FileDescriptor" %>
<!DOCTYPE html>
<html>
	<head>
		<link rel="stylesheet" href="../views/includes/css/bootstrap.css">
		<link rel="stylesheet" href="../views/includes/css/style.css">
		<meta http-equiv="Content-Type" content="text/html; charset=windows-1255">
		<title>Insert title here</title>
	</head>
	<body>
		<div class="wrapper">
			
			<nav id="topSearch">
				<section id="result_formContainer">
					<form action="../controller/search" method="get">
						<input type="text" name="searchQuery" class="form-control" placeholder="What do you want to search for?">
						<input type="submit" value="search" class="btn btn-default">	
					</form>
				</section>
			</nav>
			
			<article id="resultsContainer">
				<%
					Iterator result = (Iterator)request.getAttribute("result"); 
					while (result.hasNext()){
						FileDescriptor fileItem = (FileDescriptor)result.next();
						
						//make bold text
						//StringBuilder bold = fileItem.getContent();
						
						out.println("<article class='container'>	"
									+"	<section> Title: "+ fileItem.getTitle() +"</section>"  
									+"	<section> Author: "+ fileItem.getAuthor() +"</section>" 
									+"	<section> Date: "+ fileItem.getCreationDate() +"</section>" 
									+"	<section> Preview: "+ fileItem.getPreview() +"</section>"
									//+"															"	
									+"</article><hr>");
					}
				%>
			</article>
		</div>
	</body>
</html>