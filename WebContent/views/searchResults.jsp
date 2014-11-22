<%@ page language="java" contentType="text/html; charset=windows-1255"
	pageEncoding="windows-1255"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="il.ac.shenkar.SearchEngine.FileDescriptor"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="../views/includes/css/bootstrap.css">
<link rel="stylesheet" href="../views/includes/css/style.css">
<link href='http://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css'>

<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1255">
<title>Results</title>
</head>
<body>
	<div class="wrapper">
		<nav id="topSearch">
			<nav id="topNav">
				<ul class="nav nav-pills">
					<li role="presentation"><a id="adminConfiguration"
						href="../controller/adminLogin"><span
							class="glyphicon glyphicon-wrench" aria-hidden="true"></span>
							Configuration</a></li>
					<li role="presentation"><a id="adminConfiguration"
						href="../controller/"><span class="glyphicon glyphicon-search"
							aria-hidden="true"></span> Search</a></li>
				</ul>
			</nav>
			<a id="topTitle_link" href="../controller/">
				<section id="topTitle">Search Engine</section>
			</a>
			<section id="result_formContainer">
				<form action="../controller/search" method="get">
					<input type="text" name="searchQuery" class="form-control"
						placeholder="What do you want to search for?"
						value="<%out.println(request.getAttribute("searchQuery"));%>"
						required> <input type="submit" value="search"
						class="btn btn-default">
				</form>
			</section>
		</nav>

		<article id="resultsContainer">
			<section id="numberOfResults">
				<%
					out.println("Search results for: '"
							+ request.getAttribute("searchQuery")
							+ "'\t , Total results: "
							+ request.getAttribute("numberOfSearchResults"));
				%>
			</section>

			<%
				Iterator result = (Iterator) request.getAttribute("result");
				while (result.hasNext()) {
					FileDescriptor fileItem = (FileDescriptor) result.next();

					/* Build preview with bold text */
					//make 2 arrays of words
					String previewWords[] = fileItem.getPreview().split(" ");
					String queryWords[] = ((String) request
							.getAttribute("searchQuery")).split(" ");
					StringBuilder preview_with_bold_text = new StringBuilder();
					for (String previewWord : previewWords) {
						for (int i = 0; i < queryWords.length; i++) {
							if (previewWord.toLowerCase().equals(queryWords[i].toLowerCase())) {
								preview_with_bold_text.append("<b>" + previewWord
										+ "</b> ");
								break;
							} else {
								if (i == queryWords.length - 1) {
									preview_with_bold_text
											.append(previewWord + " ");
								}
							}
						}
					}

					int startIn = fileItem.getPath().indexOf("\\db");
					int endIn = fileItem.getPath().length();
					String pathWithoutStart = fileItem.getPath().substring(startIn,
							endIn);

					out.println("<article class='container'>	"
							//http://localhost:8080/SearchEngineWebApp/db/images/1.1414669848596.jpg
							//<a href="../db/file.txt" target="_blank">Who is Superman?</a>
							+ "	<section id='title'> <a href='.."
							+ pathWithoutStart + "' target='_blank' class='resultTitleLink'>"
							+ fileItem.getTitle() + "</a></section>"
							+ "	<section id='preview'>" + preview_with_bold_text
							+ "</section>"
							+ "	<section id='resultDetailsContainer'>"
							+ "		<section id='author'>" + fileItem.getAuthor()
							+ ",&nbsp </section>" + "		<section id='date'>"
							+ fileItem.getCreationDate() + "</section>"
							+ "	</section>" + "</article><hr>");
				}
			%>
		</article>
	</div>
</body>
</html>