<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta charset="utf-8">
<!-- imports -->

<!-- js-->
<script src="../views/includes/js/jquery-1.8.3.min.js"></script>
<script src="../views/includes/js/upload.js"></script>


<link rel="stylesheet" href="../views/includes/css/bootstrap.css">
<link rel="stylesheet" href="../views/includes/css/upload.css">
<link rel="stylesheet" href="../views/includes/css/style.css">
<link href='http://fonts.googleapis.com/css?family=Open+Sans'
	rel='stylesheet' type='text/css'>

<title>Upload files</title>
</head>
<body>
<nav id="topNav"><ul class="nav nav-pills">
<li role="presentation"><a id="adminConfiguration" href="../controller/adminMenu"><span class="glyphicon glyphicon-wrench" aria-hidden="true"></span>
 Configuration</a></li>
 <li role="presentation"><a id="adminConfiguration" href="../controller/"><span class="glyphicon glyphicon-search" aria-hidden="true"></span>
 Search</a></li>
</ul></nav>

<section id="uploadWrapper">
<h1 id="mainTitle">Upload Manager</h1>
	<form id="upload" action="upload" method="post"
		enctype="multipart/form-data">
		<article>

			<input id="file" type="file" name="upl" required> <br>
		</article><br>
		<article id="tagNames_holder"></article>
		
		<article>
			<input id="bt_cancel" type="button" value="cancel" onclick='cancel()'>
			<br>
			<input id="bt_upload" type="submit" value="upload"
				onclick='ajaxUpload()'>
		</article>
	</form>
</section>
</body>
</html>
