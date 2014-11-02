<!DOCTYPE html>
<html>

<head>
<meta charset="utf-8" />
</head>
<body>
	<form id="upload-form" class="upload-box" action="upload6"
		method="post" enctype="multipart/form-data">
		<h1>Upload New Style</h1>
		<input type="text" id="tag" name="tag" />
		<input type="file" id="file" name="file" />
		<div id="upload-error" class="error">${uploadError}</div>
		<input type="button" onclick="d()" id="upload-button" value="upload" />
	</form>

</body>
<script>
	function d(){
		alert("");
		$('#upload-form').ajaxForm({
			success : function(msg) {
				alert("Style has been updated successfully");
				window.location.reload();
			},
			error : function(msg) {
				$("#upload-error").text("Couldn't upload file");
			}
		});
	}
</script>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7/jquery.js"></script>
<script src="http://malsup.github.com/jquery.form.js"></script>
</html>