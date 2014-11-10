//var loadFile = false;
var file;
// var filePath = "";
var fileData = "";
var fileName = "";
var fileType = "";
var fileExtension = "";
var tagNames = "";

//var width = $(window).width() * 0.70;
//var height = $(window).height() * 0.80;
jQuery(document).ready(function($) {
	//dragAndDrop_init();
	
//	$('.holder a').click(function(){
//        // Simulate a click on the file input button
//        // to show the file browser dialog
//        $(this).parent().find('input').click();
//    });
//	
//	$("#pic_tagname").keyup(function() {
//		tagNames = $.trim($(this).val());
//		$('#tagNames').val(tagNames);
//	});
//	$("#pic_tagname").click(function() {
//		tagNames = $.trim($(this).val());
//		$('#tagNames').val(tagNames);
//	});
//	$("#pic_tagname").change(function() {
//		tagNames = $.trim($(this).val());
//		$('#tagNames').val(tagNames);
//	});
	
	 $("#file").change(function(e) {
		 debugger;
		 if (e.target.files.length >0){
			 fileName = e.target.files[0].name;
	         fileType = e.target.files[0].type;
	         if (fileType.indexOf('image') > -1){
	        	 $("#tagNames_holder").append("<label for='pic_tagNames' id='lable_pic_tagNames' >tag names:\
												<input id='pic_tagNames' name='pic_tagNames' type='text' placeholder='write here picture tag names' required>\
										   </label>");
	        	 
	         }else if (fileType.indexOf('text') > -1){
	        	 $("#tagNames_holder").empty();
	         }else{
	        	 console.log("file unknown");
	         }
	         
	         var fileExtension = fileName.split('.')[fileName.split('.').length - 1].toLowerCase();
		 }else{
			 $("#tagNames_holder").empty();
		 }
	});
	
});

function dragAndDrop_init() {
	// $(".holder").css("height", (height-30)+"px");
	var holder = $(".holder"), state = $(".status");
	// length of 'holder' & 'state' are the same
	for (i = 0; i < holder.length; i++) {
		if (!holder[i])
			i = 0;

		if (typeof window.FileReader === 'undefined') {
			state[i].id = 'fail';
		} else {
			state[i].id = 'success';
			// state[i].innerHTML = 'File API & FileReader available';
		}

		holder[i].ondragover = function() {
			this.id = 'hover';
			return false;
		};
		holder[i].ondragend = function() {
			this.id = '';
			return false;
		};
		holder[i].ondrop = function(e) {
			this.id = '';
			this.innerHTML = "";
			e.preventDefault();

			file = e.dataTransfer.files[0], reader = new FileReader();

			reader.onload = function(event) {
				// Get file extension
				fileName = file.name;
				fileExtension = fileName.split('.')[fileName.split('.').length - 1]
						.toLowerCase();

				// console.log(file.type);
				// debugger;
				// Turn on Cancel button
				$("#bt_cancel").css("visibility", "visible");

				// Show file to user by manipulate file extension
				if (fileExtension == "txt") {
					/* Txt file */
					holder.html('<p>Uploaded ' + file.name + ' '
							+ (file.size ? (file.size / 1024 | 0) + 'K' : ''));
					
					$("#tagNames_holder").empty();
					//$("#lable_pic_tagname").css("visibility", "hidden");
					//$("#lable_pic_tagname").prop('required',false);
					e.target.style.background = "";
				} else {
					/* picture file */
					e.target.style.background = 'url(' + event.target.result
							+ ') no-repeat center';
					
					$("#tagNames_holder").add("<label for='pic_tagNames' id='lable_pic_tagNames' >tag names:\
												<input id='pic_tagNames' name='pic_tagNames' type='text' placeholder='write here picture tag names' required>\
											</label>");
				}

				// 'e.target' is equal to 'holder[i]'
				console.log(reader.result);

				// File Data
				// (event.target.result) <= equal to => (reader.result)
				holder.innerText = event.target.result;
				fileData = event.target.result;
				fileType = file.type;

				$('#fileDataBase64').val(fileData);
				$('#fileExtension').val(fileExtension);
				$('#fileName').val(fileName);
				$('#fileType').val(fileType);
				$('#tagNames').val(tagNames);

				$("#unload_err").css("visibility", "hidden");
			};

			console.log(file);
			reader.readAsDataURL(file);

			return false;
		};
	}
}

function cancel() {
	$("#bt_cancel").css("visibility", "hidden");
	$("#lable_pic_tagNames").css("visibility", "hidden");
	$(".holder").html("Drag&Drop image or text file here");
	$(".holder").attr("style", "");
	$("#unload_err").css("visibility", "hidden");
	$("#pic_tagNames_missing_err").css("visibility", "hidden");
	// $(".status").html("");
	// loadFile = false;
	file = null;
	fileData = "";
	fileName = "";
	fileType = "";
	fileExtension = "";
	tagNames = "";
	$("#pic_tagNames").val("");

	$('#fileDataBase64').val("");
	$('#fileExtension').val("");
	$('#fileName').val("");
	$('#fileType').val("");
	$('#tagNames').val("");
	
	
	
	$("#tagNames_holder").empty();
}

