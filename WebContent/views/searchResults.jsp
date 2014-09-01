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
						<input type="text" name="searchQuery" class="form-control" placeholder="What do you want to search for?" value="<% out.println(request.getAttribute("searchQuery"));%>">
						<input type="submit" value="search" class="btn btn-default">	
					</form>
				</section>
			</nav>
			
			<article id="resultsContainer">
			<section id="numberOfResults"> <% out.println("Search results for: '" + request.getAttribute("searchQuery") + "'\t , Total results: "+request.getAttribute("numberOfSearchResults")) ; %></section>
			
				<%
					Iterator result = (Iterator)request.getAttribute("result");
					while (result.hasNext()){
						FileDescriptor fileItem = (FileDescriptor)result.next();
						
						/* Build preview with bold text */
						//make 2 arrays of words
						String previewWords[] = fileItem.getPreview().split(" ");
						String queryWords[] = ((String)request.getAttribute("searchQuery")).split(" ");
						StringBuilder preview_with_bold_text = new StringBuilder();
						for (String previewWord : previewWords) {
							for (int i=0; i<queryWords.length; i++) {
								if (previewWord.equals(queryWords[i])) { 
									preview_with_bold_text.append("<b>"+previewWord+"</b> ");	
									break;			
								}else{
									if (i == queryWords.length-1){
										preview_with_bold_text.append(previewWord+" ");
									}
								}
							}
						}
						
						out.println("<article class='container'>	"
								//<a href="file:///C:\Users\joe\Desktop\tasks.txt">click me</a>
									+"	<section id='title'> <a href='../controller/show_a_result?filePath="+fileItem.getPath()+"' target='_blank'>"+ fileItem.getTitle() +"</a></section>"
									+"	<section id='preview'>"+ preview_with_bold_text +"</section>"
									+"	<section id='resultDetailsContainer'>"
									+"		<section id='author'>" + fileItem.getAuthor() +",&nbsp </section>" 
									+"		<section id='date'>" + fileItem.getCreationDate() +"</section>" 
									+"	</section>"
									+"</article><hr>");
					}
				%>
			</article>
		</div>
	</body>
</html>