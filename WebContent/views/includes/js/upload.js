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
	        	 //$("#lable_pic_tagname").css("visibility", "visible");
	        	 //$("#pic_tagname").prop('required',true);
	        	 $("#tagNames_holder").append("<label for='pic_tagNames' id='lable_pic_tagNames' >tag names:\
												<input id='pic_tagNames' name='pic_tagNames' type='text' placeholder='write here picture tag names' required>\
										   </label>");
	        	 
	         }else if (fileType.indexOf('text') > -1){
	        	 $("#tagNames_holder").empty();
	        	 //$("#lable_pic_tagname").css("visibility", "hidden");
	        	 //$("#pic_tagname").prop('required',false);
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
					//$("#lable_pic_tagname").css("visibility", "visible");
					//$("#lable_pic_tagname").prop('required',true);
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

//function upload() {
//	if (file != null) {
//		var data;
//		var canUpload = false;
//		if (fileExtension == "txt") { // txt file
//			// txt file
//			console.log("loaded txt file");
//
//			data = { // create object
//				fileExtension : fileExtension,
//				fileData : fileData,
//				fileName : fileName,
//				fileType : fileType
//			};
//			canUpload=true;
//		} else { // pic file
//			// check if user write tag names
//			if (tagNames != "" && tagNames.length > 0) {
//				// Can upload file
//				console.log("input is full");
//				$("#pic_tagname_missing_err").css("visibility", "hidden");
//
//				data = { // create object
//					fileExtension : fileExtension,
//					fileData : fileData,
//					fileName : fileName,
//					fileType : fileType,
//					tagNames : tagNames
//				};
//				canUpload=true;
//			} else {
//				// Input is empty
//				console.log("Input is empty");
//				$("#pic_tagname_missing_err").css("visibility", "visible");
//			}
//		}
//		
//		if (canUpload){
//			$.ajax({
//				type : "POST",
//				data : data, // pass as data
//				url : "upload?loadFile=true"
//			}).done(function(databack) {
//				alert(databack);
//			});
//			
////			$.ajax({
////			    url : "../controller/upload?loadFile=true",  //your servlet or jsp name/path
////			    type: "POST",
////			    data : data, // pass as data
////			    success: function(databack, textStatus, jqXHR){
////			        //databack - response from server
////			    	alert(databack);
////			    	console.log("response..:"+ databack);
////			    },
////			    error: function (jqXHR, textStatus, errorThrown){
////			    	console.log("error...");
////			    }
////			});
//			
//			//now reset the page
//			cancel();
//		}
//		
//		
//	} else {
//		// File is missing
//		$("#unload_err").css("visibility", "visible");
//	}
//}



//function upload2() {
//	if (file != null) {
//		var data;
//		var canUpload = false;
//		if (fileExtension == "txt") { // txt file
//			// txt file
//			console.log("loaded txt file");
//			data = { // create object
//				fileExtension : fileExtension,
//				fileData : fileData,
//				fileName : fileName,
//				fileType : fuu2
//			};
//			canUpload=true;
//		} else { // pic file
//			// check if user write tag names
//			if (tagNames != "" && tagNames.length > 0) {
//				// Can upload file
//				console.log("input is full");
//				$("#pic_tagname_missing_err").css("visibility", "hidden");
//				data = { // create object
//					fileExtension : fileExtension,
//					fileData : fileData,
//					fileName : fileName,
//					fileType : foo,
//					tagNames : tagNames
//				};
//				canUpload=true;
//			} else {
//				// Input is empty
//				console.log("Input is empty");
//				$("#pic_tagname_missing_err").css("visibility", "visible");
//			}
//		}
//		if (canUpload){
//			$.ajax({
//				type : "POST",
//				data : data, // pass as data
//				url : "../controller/upload?loadFile=true"
//			}).done(function(databack) {
//				alert(databack);
//			});
//			
//			//now reset the page
//			cancel();
//		}
//	} else {
//		// File is missing
//		$("#unload_err").css("visibility", "visible");
//	}
//}

