
//----------------------------------------- File Upload --------------------------------------
function resetInputFileEvents(){
	$(document).off('click', "div[id^='sep_photo']");
	$(document).off('click', "div[id^='photo']");
	$(document).off('change', "input[type='file']");
}

/**
 * 
 */
function loadInputFileEvents(){
	$(document).off('click', "div[id^='sep_photo']");
	$(document).off('click', "div[id^='photo']");
	$(document).off('change', "input[type='file']");
	//
	$(document).on('click', "div[id^='photo']", function(){
		$("#"+$(this).attr("id")+"_file").trigger("click");
	});
	$(document).on('change', "input[type='file']", function(e){
		uploadFile(e, this, $(this));
	});
	$(document).on('click', "div[id^='sep_photo']", function(){
		unloadData($(this))
	});
}

function uploadFile(event, input, field){
	    event.stopPropagation(); 
	    event.preventDefault(); 
	    var files = event.target.files; 
	    var data = new FormData();
	    $.each(files, function(key, value){
	        data.append(key, value);
	    });
	    postFilesData(data, event.target.id, input, field); 
}

/**
 * @param data
 * @param fieldName
 * @param input
 * @returns {Boolean}
 */
function postFilesData(data, fieldName, input, field) {
    var frontUrl = "front?upload=1&fn="+fieldName+"&path="+(field.attr("path")?field.attr("path"):'');
	showSpinner();
	
	var fieldDel = "sep_"+fieldName.substring(0, fieldName.indexOf("_"));
	
	// Suprimer ancienne photos
	unloadData($("#"+fieldDel)),
	
    $.ajax({
        beforeSend: function(data) {
        },
        url: frontUrl,
        data: data,
        type: "POST",
        cache: false,
        dataType: 'json',
        processData: false, 
        contentType: false, 
        error: function(data) {
        	field.replaceWith(field = field.clone(true));
        	hideSpinner();
        },
        success: function(data) {
        	data = JSON.stringify(data);
        	hideSpinner();
        	if(data.indexOf("_err_founded") != -1	&& data.indexOf(">") == -1){
        		// Clean input file
        		field.replaceWith(field = field.clone(true));
				manageErrorMessages(data);
			} else{
				readUrlFile(input, field);
			}
        }
    });
  return false;
}

/**
 * @param fieldName
 */
function unloadData(delElement) {
	var currId = delElement.attr("id").substring(4, delElement.attr("id").length);
	var frontUrl = "front?upload=0&fn="+delElement.attr("id")+"&fnm="+$("#"+currId+"_name").val();
	$.ajax({
        beforeSend: function(data) {
        },
        url: frontUrl,
        data: null,
        type: "POST",
        cache: false,
        contentType: false,
        processData: false,
        error: function(data) {
            hideSpinner();
        },
        success: function(data) {
        	data = JSON.stringify(data);
        	hideSpinner();
        	if(data.indexOf("_err_founded") != -1	&& data.indexOf(">") == -1){
				manageErrorMessages(data);
			} else{
				unreadUrlFile(delElement);
			}
		}
	 });	
}

/**
 * 
 */
function readUrlFile(input, field) {
	var realName = null;
	if(field.val().indexOf("/") == -1){
		realName = field.val().substring(field.val().lastIndexOf("\\")+1, field.val().length);
	} else{
		realName = field.val().substring(field.val().lastIndexOf("/")+1, field.val().length);							
	}	
	var idImage = field.attr("name").substring(5);
	
	$divClosest = $("#"+field.attr("id").substring(0, 10));
	
	// Apercu
	    if (input.files && input.files[0]) {
	        var reader = new FileReader();
	        reader.onload = function (e) {
	        	var ext = realName.substring(realName.lastIndexOf(".")+1);
	        	var img = e.target.result;//"kdeprint_testprinter.png";
	        	//
		        $divClosest.css("background", "");
		        if(ext.startsWith('pdf')){
		        	img = "resources/framework/img/filetype_pdf.png";
		        } else if(ext.startsWith('xls')){
		        	img = "resources/framework/img/xls_file.png";
		        } else if(ext.startsWith('txt')){
		        	img = "resources/framework/img/txt.png";
		        } else if(ext.startsWith('doc')){
		        	img = "resources/framework/img/document_microsoft_word_01.png";
		        }
		        $divClosest.html("<img style='border-radius: 10px;' src='"+img+"' width='120' height='120'/>");
	        }
	        reader.readAsDataURL(input.files[0]);
	    }
//	}
	$("#photo"+idImage+"_name").val(realName);
	if($("#photo"+idImage+"_name_span")){
		$("#photo"+idImage+"_name_span").html(realName);
	}
}

/**
 * @param delElement
 */
function unreadUrlFile(delElement){
	var photo = delElement.attr("id").substring(4);
	var divClosest = $("#"+photo+"_div");
	var inpuFile = $("#"+photo+"_div_file");

	//-----------------------------------------------------------------------------------
	var idImage = delElement.attr("id").substring(9);

	var imgPath = divClosest.find("img").attr("src");		
	var realName = null;
	if(imgPath){
		if(imgPath.indexOf("/") == -1){
			realName = imgPath.substring(imgPath.lastIndexOf("\\")+1, imgPath.length);
		} else{
			realName = imgPath.substring(imgPath.lastIndexOf("/")+1, imgPath.length);							
		}	
	}
	$("#photo"+idImage+"_name").val("");
	if($("#photo"+idImage+"_name_span")){
		$("#photo"+idImage+"_name_span").html("");
	}
	//-----------------------------------------------------------------------------------	
    
    if(!delElement.attr("noview") || delElement.attr("noview") != "true"){
		divClosest.html('<span style="font-size: 11px;">Ajouter un fichier</span>');
	    divClosest.css("background", "url('resources/framework/img/picture_file.png') no-repeat center");
    } else{
    	divClosest.find("span").html("Document");
    }
	inpuFile.replaceWith(inpuFile = inpuFile.clone(true));
}