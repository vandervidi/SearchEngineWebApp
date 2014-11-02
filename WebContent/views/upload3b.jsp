<!DOCTYPE html>
<html>

	<head>
		<meta charset="utf-8"/>
		<title>Mini Ajax File Upload Form</title>

		<!-- Google web fonts -->
		<link href="http://fonts.googleapis.com/css?family=PT+Sans+Narrow:400,700" rel='stylesheet' />

		<!-- The main CSS file -->
		<link href="../views/assets/css/style.css" rel="stylesheet" />
	</head>

	<body>

		<form id="upload" method="post" action="upload?loadFile=true" enctype="multipart/form-data"> <!-- "../upload.php" -->
			<div id="drop">
				Drop Here
				<br>
				text or image files

				<a>Browse</a>
				<input type="file" name="upl" required>
				
			</div>
			
			<ul>
				<!-- The file uploads will be shown here -->
			</ul>

		</form>
        
		<!-- JavaScript Includes -->
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
		<script src="../views/assets/js/jquery.knob.js"></script>

		<!-- jQuery File Upload Dependencies -->
		<script src="../views/assets/js/jquery.ui.widget.js"></script> 
		<script src="../views/assets/js/jquery.fileupload.js"></script>
		
		<!-- Our main JS file -->
		<script src="../views/assets/js/script.js"></script>

		<!--<script src="../views/assets/js/jquery.iframe-transport.js"></script>-->
	</body>
</html>