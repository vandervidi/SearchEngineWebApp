<%@ page language="java" contentType="text/html; charset=windows-1255"
	pageEncoding="windows-1255"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=windows-1255">
	  
	
	<link rel="stylesheet" href="../views/includes/css/bootstrap.css">
	<link rel="stylesheet" href="../views/includes/css/style.css">
	<title>Search Engine Web App</title>
</head>
<body>
<a id="adminConfiguration" href="../controller/adminLogin">Configuration</a>




<br><br>
	<div id="wrapper">
		<h1>Search Engine</h1>
		<section id="search_formContainer">
			<form action="../controller/search" method="get">
				<input type="text" name="searchQuery" class="form-control" placeholder="What do you want to search for?" required>
				<input type="submit" value="search" class="btn btn-default">	
			</form>
		</section>
	</div>
</body>
</html>