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

		<link rel="stylesheet" href="../views/includes/css/upload.css">
		<title>Upload files</title>
	</head>
	<body>
		<a id="search" href="index.html">Search</a>
		
		<h1>Upload Manager</h1>
		<br><br>
		
		<form id="upload" action="upload" method="post" enctype="multipart/form-data">
			
			<article>
				<!-- <section class="holder" required>
					Drop Here
					<br>
					text or image files
					<br><br>
					<a>Browse</a>	
				</section>
				<p class="status"></p> -->
				
				<input id="file" type="file" name="upl" required>
				<br>
			</article>
			
			<article id="tagNames_holder">
				
			</article>
			
			
			<br>
			
			<article>
				<input id="bt_cancel" type="button" value="cancel" onclick='cancel()'>
				<input id="bt_upload" type="submit" value="upload" onclick='ajaxUpload()'>
			</article>
		</form>
		
	</body>
</html>
