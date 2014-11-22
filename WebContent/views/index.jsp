<%@ page language="java" contentType="text/html; charset=windows-1255"
	pageEncoding="windows-1255"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1255">
<link href='http://fonts.googleapis.com/css?family=Open+Sans'
	rel='stylesheet' type='text/css'>
<link rel="stylesheet" href="../views/includes/css/bootstrap.css">
<link rel="stylesheet" href="../views/includes/css/style.css">
<title>Search Engine Web Application</title>
</head>
<body>
	<nav id="topNav">
		<ul class="nav nav-pills">
			<li role="presentation"><a id="adminConfiguration"
				href="../controller/adminLogin"><span
					class="glyphicon glyphicon-wrench" aria-hidden="true"></span>
					Configuration</a></li>
		</ul>
	</nav>

	<br>
	<br>
	<div id="wrapper">

		<section id="search_formContainer">
			<h1 id="mainTitle">Search Engine</h1>
			<form action="../controller/search" method="get">
				<input type="text" name="searchQuery" class="form-control"
					placeholder="What do you want to search for?" required> <input
					type="submit" value="search" class="btn btn-default">
			</form>
		</section>
	</div>
</body>
</html>