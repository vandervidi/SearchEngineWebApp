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
			<section id="topTitle">Search Engine</section>
				<section id="result_formContainer">
					<form action="../controller/search" method="get">
						<input type="text" name="searchQuery" class="form-control" placeholder="What do you want to search for?" value=" <% out.println(request.getAttribute("searchQuery"));%>">
						<input type="submit" value="search" class="btn btn-default">	
					</form>
				</section>
			</nav>
			
			<article id="resultsContainer">
			<section id="numberOfResults"> <% out.println("Search results for: \"" + request.getAttribute("searchQuery") + "\"\t , Total results: "+request.getAttribute("numberOfSearchResults")) ; %></section>
			
				<%
					Iterator result = (Iterator)request.getAttribute("result");
					while (result.hasNext()){
						FileDescriptor fileItem = (FileDescriptor)result.next();
						
						//make bold text
						//StringBuilder bold = fileItem.getContent();
						
						out.println("<article class='container'>	"
									+"	<section id=\"title\"> <a href=\"#\"> "+ fileItem.getTitle() +"</a></section>" 
									+"	<section id=\"resultDetailsContainer\">"
									+"		<section id=\"author\"> " + fileItem.getAuthor() +"  ,  </section>" 
									+"		<section id=\"date\"> " + fileItem.getCreationDate() +"</section>" 
									+"	</section>"
									+"	<section id=\"preview\"> Preview: "+ fileItem.getPreview() +"</section>"
									//+"															"	
									+"</article><hr>");
					}
				%>
			</article>
		</div>
	</body>
</html>