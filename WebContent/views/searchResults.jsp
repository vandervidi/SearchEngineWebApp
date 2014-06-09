<%@ page language="java" contentType="text/html; charset=windows-1255"
    pageEncoding="windows-1255"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
		<section id="results">
		result
		</section>
	</div>
</body>
</html>