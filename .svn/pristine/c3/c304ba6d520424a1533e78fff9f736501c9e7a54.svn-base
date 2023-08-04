	var FIELD_MSG = "Des erreurs ont \u00E9t\u00E9 trouv\u00E9es dans le formulaire \!";
	var ERROR_TITLE = "Erreurs trouv\351es !"; 
	
	var GROWL_OPR_OK = "L'\u00E9l\u00E9ment \u00E0 \u00E9t\u00E9 mise \u00E0 jour avec succ\u00e8s \!";
	var GROWL_OPR_K0 = "Une erreur technique s'est produite !";
	var GROWL_TITLE_OK = "Mise \u00E0 jour effectu\u00E9e";
	var GROWL_TITLE_K0 = "Erreur technique";
	
	var selectedColor = "";
	var txtSelColor = "";
	
$(document).ready(function (){
	selectedColor = $("#mnu_sel_color").val();
	txtSelColor = $("#txt_sel_color").val();
	
	$(document).off('click', '#sidebar-nav').on('click', '#sidebar-nav', function(){
		$('#sidebar').toggle(100);
	});
	
	$(document).off('click', '.closeTabStl').on('click', '.closeTabStl', function(){
		var idTab = $(this).closest("li").find("a[data-toggle]").attr("href").replace('#', '');
		$("#"+idTab).remove();

		$(this).closest("li").remove();		
		$("#tabMnuNav li").removeClass("active");
		$("#tabMnuNavContent .tab-pane").removeClass("active");
		
		$("#tabMnuNav li").first().addClass("active");
		$("#tabMnuNavContent .tab-pane").first().addClass("active");
		// Purger la session
		$("#closeTabLink").attr("params", "wibaj=true&tab="+idTab).trigger("click");
	});
	$(document).off('click', '.tabTtlH').on('click', '.tabTtlH', function(){
		$(".tabTtlH").css("background-color", "#a8a8a8");
		$(this).css("background-color", selectedColor).css('color', txtSelColor);	
	});
	$(document).off('click', '#refresh-ajx').on('click', '#refresh-ajx', function(){
		getTabElement("#refreshTabLink").attr("params", "oldact="+getTabElement("#wfact_tab").val()).trigger("click");
	});
	
	$(document).off('click', 'a[data-toggle="modal"], #close_modal');
	$(document).on('click', 'a[data-toggle="modal"], #close_modal', function(){
		getTabElement("#generic_modal_body").html(""); 
	});
	
	// Prevent multipe firing
	$(document).off('click', 'button[wact], a[wact], div[wact], i[wact]');
	$(document).on('click', 'button[wact], a[wact], div[wact], i[wact]', function(){
		// Erase banner and error labels
		$tarTab = $("#tabMnuNavContent .tab-pane[class*='active']");
		
		$tarTab.find("div[id='top_msg_banner']").css("display", "none").find("p").html("");
		$(".modal-dialog div[id='top_msg_banner']").css("display", "none").find("p").html("");
		 		
		//
		$tarTab.find("label[class='error']").remove();

		if($(this).attr("disabled")){
			return;
		}
		// Prevent double click
		$(this).attr("disabled", "disabled");
		
		var wid = $(this).attr("kwi");
		var url = $(this).attr("wact");
		
		var params = $(this).attr("params");
		var target = $(this).attr("targetBtn");
		var isToValidate = (!$(this).attr("noVal") || $(this).attr("noVal")!='true');
			
		if(wid && wid != null){
			params = params ? (params+"&wid="+wid) : "wid="+wid;
		}
		
		if(target == 'D'){
			showConfirmDeleteBox(url, params, $(this));
		} else{
			var currForm = $(this).closest("form");
			if(currForm && currForm.length > 0){
				var isValid = true;
				if(isToValidate){
					isValid = currForm.valid();// Validate
				}
				if(isValid){ 
					submitAjaxForm(url, params, currForm, $(this));
				} else{
					$(this).removeAttr("disabled");
				}
			} else{
				submitAjaxForm(url, params, null, $(this));
			} 
		}
	});
});

function getTabElement(selector){
	$tarTab = $("#tabMnuNavContent .tab-pane[class*='active']");
	
	$fieldElmnt = $tarTab.find(selector);
	
	return ($fieldElmnt.length == 0 ? $(selector) : $fieldElmnt);
}

function manageTabNavTarget(idTab, html, eventSource){
	var targetElement = null;
	
	if(!eventSource){
		$("#tabMnuNav li").removeClass("active");
		$("#tabMnuNavContent .tab-pane").removeClass("active");
	}
	
	if(idTab != null){
		$(".tabTtlH").css("background-color", "#a8a8a8");
	}
	
	if(idTab != null){
		var url = $.param.fragment();
		var titleTab = getParamsValue(url, 'ttl');
		titleTab = titleTab.replace(/%C3%A9/g,'é');
		titleTab = titleTab.replace(/%20/g, " ");
		titleTab = titleTab.replace(/%C3%A8/g, 'è');
		
		$("#tabMnuNav").append(`
			<li class="active">	
				<a class="tabTtlH" data-toggle="tab" href="#`+idTab+`" style="z-index: 0;text-transform: uppercase;padding-right: 25px;line-height: 34px;padding-bottom: 0px;padding-top: 0px;color:`+txtSelColor+`;background-color: `+selectedColor+`;">
					<i class="fa fa-genderless" style="color: blue;"></i>`+titleTab+`
				</a>
				<i class="fa fa-times closeTabStl" style="color: red;line-height: 7px;font-size: 16px;z-index: 0;"></i>
		  	</li>
		`);
		
		$("#tabMnuNavContent").append(`
			<div id="`+idTab+`" class="tab-pane topNavContent in active">
	            <div id="body-content">
	            	
	            </div>
	         </div> 
		`);
		
		targetElement = $("#"+idTab).find("#body-content");
	} else{
		if(eventSource){
			$tarTab = $("#tabMnuNavContent .tab-pane[class*='active']");
			
			targetElement = $tarTab.find("#body-content");
			idTab = eventSource.closest(".tab-pane").attr("id");
		} else{
			$("#tabMnuNav li").first().addClass("active");
			$("#tabMnuNav li").first().find('a').css('background-color', selectedColor).css('color', txtSelColor);
			
			$("#tabMnuNavContent .tab-pane").first().addClass("active");
			targetElement = $("#tabMnuNavContent .tab-pane").first().find("#body-content");
			idTab = $("#tabMnuNavContent .tab-pane").first().attr("id");
		}
	}
	
	targetElement.empty(); // on vide le div
	targetElement.append(html); // on met dans le div le resultat de la requete ajax
	$(".page-breadcrumbs").hide();
	
	if(idTab == 'tab_nav_0'){
		$("#tab_first_span").html($(".breadcrumb li:eq(2)").text());
	}
	//
	// Add tab id
	if(targetElement.find("#curr_tab_id").length == 0){
		targetElement.find("form").append("<input type='hidden' id='curr_tab_id' name='curr_tab_id' value='"+idTab+"'>");
	}
	$(".flexigrid").show();
	
	// If one tab then hide
	if($(".tabTtlH").length == 1){
		$(".tabTtlH").hide();
	} else{
		$(".tabTtlH").show();
	}
}

/**
* Ajax call and add result to div
*/
function callForwardAjaxUrl(target){
	// Show animation
	showSpinner();
	var idTab = 'tab_'+($("#tabMnuNav li").length+1)+'_'+randString();
	var url = $.param.fragment();
	var isTab = getParamsValue(url, 'ntab');
			
	//
	$.ajax({  // ajax
		url: target+(isTab?"&curr_tab_id="+idTab:""), // url de la page charger
		cache: false, // pas de mise en cache
		success:function(html){ // si la requete est un succes
			
			hideSpinner();
			manageTabNavTarget((isTab ? idTab : null), html);
			refreshSpecialComponents();
			$("html, body").animate({ scrollTop: 0 }, "slow");
		},
		error:function(XMLHttpRequest, textStatus, errorThrows){ // erreur durant la requete
			hideSpinner();
		}
	});
}

/*
* Submit form width ajax
*/
function submitAjaxForm(url, params, form, elementObj, targetDiv) {
	$tarTab = $("#tabMnuNavContent .tab-pane[class*='active']");
	 var frontUrl = "front?";

	var isNoSaveAct = false;
	if(elementObj && elementObj.attr("params") && elementObj.attr("params").indexOf('w_nos') != -1){
		isNoSaveAct = true;
	}

	if(form && form.find("#w_f_act").length > 0  ){
		form.find("#w_f_act").val(url);
		if(!isNoSaveAct){
			form.find("#wfact_tab").val(url);
		}
	} else{
		frontUrl = frontUrl + "w_f_act="+url + '&';
	}
	
	// Form value
	if(params){
		frontUrl = frontUrl + params;
	}
	if($tarTab){
		frontUrl = frontUrl+"&curr_tab_id="+$tarTab.attr("id");
	}
	
	// Show loading animation
	showSpinner();
	
	// Scroll position
	savePosition();
	
    $.ajax({
        beforeSend: function(data) {
        },
        url: frontUrl,
        data: form ? form.serialize():null,
        type: "POST",
        cache: false,
        dataType: 'text',
        error: function(data) { 
        	if(elementObj){
        	    elementObj.removeAttr("disabled");
        	}
        },
        success: function(data) {
        	if(elementObj){
        		elementObj.removeAttr("disabled");
        	}
//        	alert(data);  
            // Hide sinner
            hideSpinner();

        	// Json error response
			if(data.indexOf("_err_founded") != -1	&& data.indexOf(">") == -1){
				manageErrorMessages(data);
        		return;
			}
			if(data.indexOf('MSG_CUSTOM') != -1){
				if(elementObj && elementObj.attr("closepop") == 'true'){
					$("#close_modal").trigger("click");// Close modal
				}
				if(elementObj && elementObj.attr("onComplete")){
					eval(elementObj.attr("onComplete"));
				}
				
				var msgCustom = data.split(":");
        		alertify.success(msgCustom[1]);
        		return;
        	}
			if(data.indexOf("REDIRECT") != -1){
				var redirectStr = data.split(':');
				
				if(elementObj && elementObj.attr("onComplete")){
					eval(elementObj.attr("onComplete"));
				}
				
				if(data.indexOf(":") == -1 || redirectStr.length == 1){
					location.reload();
				} else{
					var strLink = "";
					for(var i=1;i<redirectStr.length; i++){
						strLink += redirectStr[i] + ":";
					}
					strLink = strLink.substring(0, strLink.length-1);
					
					window.location=strLink;
				}
				return;
			} else{
				if((targetDiv && targetDiv!='') || (elementObj && elementObj.attr("targetDiv"))){
					if(targetDiv && targetDiv!=''){
						getTabElement("#"+targetDiv).empty().html(data);
					} else{
						getTabElement("#"+elementObj.attr("targetDiv")).empty().html(data);						
					}
					if(elementObj && elementObj.attr("closepop") == 'true'){
						$("#close_modal").trigger("click");// Close modal
					}
				} else{
					manageTabNavTarget(null, data, elementObj);
					
//					$("#body-content").empty().html(data);
					$("#close_modal").trigger("click");// Close modal
				}
				//
				if(elementObj && elementObj.attr("onComplete")){
					eval(elementObj.attr("onComplete"));
				}
				
				refreshSpecialComponents();
				// Restaure
				try {
					restorePosition();
				}catch (e) {
					
				}
			}
        }
    });
  return false;
}

/**
 * @param target
 * @return
 */
function callBackJobAjaxUrl(target, showGrowl, msg, title){
	target += "&wibaj=true";
	//
	$.ajax({  // ajax
		url: target, // url de la page charger
		cache: false, // pas de mise en cache
		success:function(html){ // si la requ�t� est un succ�s
			if(showGrowl){
				showPostAlert(msg, title, 'SUCCES');
			}
		},
		error:function(XMLHttpRequest, textStatus, errorThrows){ // erreur durant la requete
			if(showGrowl){
				showPostAlert(msg, title, 'ERROR');
			}
		}
	});
}
/**
 * Manage all validation errors
 * @param html
 */
function manageErrorMessages(html){
	var jsonResponse = jQuery.parseJSON(html);
	
	//------------------------------ Field---------------------------------------------
	if(jsonResponse.field_err_founded){
		var idx = 0;
		for (var i=0; i<jsonResponse.field_err_founded.length; i++) {
			var msgObg = jsonResponse.field_err_founded[i]; 

			var element = getTabElement("#"+getJQueryName(msgObg.field));
			var label = $('<label id="err_'+msgObg.field+'" class="error">'+msgObg.message+'</label>');
			label.insertAfter(element);
			
			if(idx == 0){
				element.focus();
				
				var closestTab = element.closest(".step-pane");
				if(closestTab.length > 0){
					$('[data-target=#'+closestTab.attr("id")+']').trigger("click");
				}
            }
			idx++;
		}
		if(jsonResponse.field_err_founded.length > 0){ 
			showPostAlert(ERROR_TITLE, FIELD_MSG, "ERROR");
		}
	}
	//---------------------------------------------------------------------------
	
	//------------------------------ Banner---------------------------------------------
	if(jsonResponse.banner_err_founded){
		var bannerHtml = "";
		for (var i=0; i<jsonResponse.banner_err_founded.length; i++) {
			var msgObg = jsonResponse.banner_err_founded[i];
			var msgClass = '<span style="color:#e8823a;"><i class="fa-fw fa fa-warning"></i>';
			
			if(msgObg.type == "ERROR"){
				msgClass = '<span style="color:red;"><i class="fa-fw fa fa-times"></i>';
			} 
			bannerHtml = bannerHtml+ msgClass + msgObg.message+'</span><br />';
		}
		if(jsonResponse.banner_err_founded.length > 0){
			$tarTab = $("#tabMnuNavContent .tab-pane[class*='active']");
			$tarTab.find("div[id='top_msg_banner']").last().css("display", "");
			$(".modal-dialog div[id='top_msg_banner']").last().css("display", "");
			
			$tarTab.find("div[id='top_msg_banner_det']").last().html(bannerHtml);
			$(".modal-dialog div[id='top_msg_banner_det']").last().html(bannerHtml);
			
			$("html, body").animate({ scrollTop: 0 }, "slow");
			showPostAlert(ERROR_TITLE, FIELD_MSG, "ERROR");
		}
	}
	//---------------------------------------------------------------------------
	
	//------------------------------ Growl---------------------------------------------
	if(jsonResponse.growl_err_founded){
		for (var i=0; i<jsonResponse.growl_err_founded.length; i++) {
			var msgObg = jsonResponse.growl_err_founded[i];
			showPostAlert(msgObg.title, msgObg.message, msgObg.type);
		}
	}
	//---------------------------------------------------------------------------
	
	//------------------------------ Notify---------------------------------------------
	/*for (var i=0; i<jsonResponse.notify_err_founded.length; i++) {
		var msgObg = jsonResponse.notify_err_founded[i];
	}*/
	//---------------------------------------------------------------------------
	
	//------------------------------Dialog---------------------------------------------
	if(jsonResponse.dialog_err_founded){
		var dialogMsg = "";
		for (var i=0; i<jsonResponse.dialog_err_founded.length; i++) {
			var msgObg = jsonResponse.dialog_err_founded[i];
			dialogMsg = dialogMsg+ '<span style="font-size: 13px;font-weight: bold;" class="error"><i class="fa fa-minus"></i>&nbsp;'+msgObg.message+'</span><br />';
		}
		if(jsonResponse.dialog_err_founded.length > 0){
			buildErrorDialog(dialogMsg);
			showPostAlert(ERROR_TITLE, FIELD_MSG, "ERROR");
		}
	}
	//------------------------------Dialog technique---------------------------------------------
	if(jsonResponse.technic_err_founded){
//		dialogMsg = "";
//		for (var i=0; i<jsonResponse.technic_err_founded.length; i++) {
//			var msgObg = jsonResponse.technic_err_founded[i];
//			dialogMsg = dialogMsg+ '<span style="font-size: 13px;font-weight: bold;" class="error">&nbsp;'+msgObg.message+'</span><br />';
//		}
//		if(jsonResponse.technic_err_founded.length > 0){
//			buildErrorDialog(dialogMsg);
//			showPostAlert(ERROR_TITLE, FIELD_MSG, "ERROR");
//		}
	
		if(getTabElement("#growl_tech_div").length == 0){
			$("<div id='growl_tech_div' style='position: fixed;right: 0px;bottom: 3px;width: 67px;height: 54px;background-color: rgb(239 239 239);border-radius: 15px;padding-left: 11px;padding-top: 9px;border: 1px solid #ff5722;opacity: 0.6;'><img style='width:40px;' src='resources/framework/img/disaster_warning.png'></div>")
				.appendTo($("body"));
		}
		getTabElement("#growl_tech_div").show(100);
		setTimeout(function(){ getTabElement("#growl_tech_div").hide(1000); }, 4000);
	}
	//---------------------------------------------------------------------------
	
	//------------------------------Dialog question---------------------------------------------
	if(jsonResponse.question_err_founded){
		for (var i=0; i<jsonResponse.question_err_founded.length; i++) {
			var msgObg = jsonResponse.question_err_founded[i];
			//
			buildQuestionDialog(msgObg);
		}
	}
	//---------------------------------------------------------------------------
}

/**
 * @param msgObg
 */
function buildQuestionDialog(msgObg){
	var isConfirm = (msgObg.listButtons && msgObg.listButtons.length > 0) ? false : true;
	$("#global-dialog-quastion-content").html(msgObg.message);
	 //
	 if(isConfirm){
		if(!msgObg.action || msgObg.action == ''){
			alert("Action is required for confirm dialog message !");
			return;
		}
	 }
	 
	 bootbox.dialog({
	        message: $("#global-dialog-quastion").html(),
	        title: (msgObg.title ? msgObg.title : "Confirmation"),
	        className: "modal-darkorange",
	        buttons: {
	        	/*   success: {
	                label: "Confirmer",
	                className: "btn-blue",
	                callback: function () {
	                	// $('#genericDialogBox_question').dialog('close');
	            		 $("#w_qstid").val(msgObg.id);
	            		 //submitAjaxBodyForm(msgObg.action, null);
	            		 submitAjaxForm(msgObg.action, null, form, null);
	                }
	            },*/
	            "Fermer": {
	                className: "btn-danger",
	                callback: function () { 
	                	callBackJobAjaxUrl("front?methode=cleanQmsg");
	                }
	            }
	        }
		});
	 
	    if(isConfirm){
			 var btn = '<button type="button" onClick="triggerConfirmDialog(\''+msgObg.action+'\', \''+msgObg.id+'\');" class="btn btn-success">Confirmer</button>';
			 $(".bootbox .modal-footer").prepend(btn); 
		 } else{	 
			 for(var i=0; i<msgObg.listButtons.length; i++) {
				 var dialogButton = msgObg.listButtons[i];
				 var btn = '<button type="button" onClick="triggerConfirmDialog(\''+dialogButton.action+'\', null, \''+dialogButton.params+'\');" class="btn btn-success">'+dialogButton.label+'</button>';
				 $(".bootbox .modal-footer").prepend(btn); 
			 }
		 }
}

function triggerConfirmDialog(action, dialogId, params){
	var elmnt = getTabElement("input[id='w_qstid']");
	if(dialogId != null){
	    elmnt.val(dialogId);
	}
	// Devra etre dynamique ...
	var currForm = elmnt.closest("form");
	submitAjaxForm(action, params, currForm, null);
}

/**
 * @param divId
 */
function buildErrorDialog(htmlMessage, title){
	getTabElement("#global-msg-title").html(title?title:"Erreur");
	getTabElement("#global-msg-body").html(htmlMessage);
	getTabElement("#global-msg-href").trigger("click");
}

/**
* Show loading animation
*/
function showSpinner(){
	$('#spinner').show();
}

/**
 * Hide loading img
 */
function hideSpinner(){
	$("#spinner").hide();
}

/**
* Get jquery name by replacing . by \\.
*/
function getJQueryName(name){
	return name.replace(/\./g,"\\\.");
}

/**
 * Get value of parameter of url params
 * @param params
 * @param key
 * @returns
 */
function getParamsValue(params, key){ 
    var hash, value = null;
    var hashes = params.split('&');
    //
    for(var i = 0; i < hashes.length; i++){
        hash = hashes[i].split('=');

        if(hash[0] == key){
        	value = hash[1];
        	break;
        }
    }
    return value;
}

/**
 * Function generates a random string for use in unique IDs, etc
 *
 * @param <int> n - The length of the string
 */
function randString(n) {
	if(!n) {
	    n = 5;
	}
	
	var text = '';
	var possible = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
	
	for(var i=0; i < n; i++) {
	    text += possible.charAt(Math.floor(Math.random() * possible.length));
	}
	
	return text;
}

function refreshSpecialComponents(){
	if(getTabElement("select[class='select2']").length > 0){
	    getTabElement("select[class='select2']").select2({
	//        placeholder: "-------------------",
	        allowClear: true
	    });

		getTabElement(".select2[multiple]").select2({
	//        placeholder: "-------------------",
	        allowClear: true,
			closeOnSelect: false
	    });
	}
	
    //--Bootstrap Date Picker--
	if(getTabElement('.date-picker:not([readonly])').length > 0){
	    getTabElement('.date-picker:not([readonly])').datepicker({
	    	todayBtn: true,
	    	clearBtn: true,
		    language: "fr",
		    autoclose: true,
		    todayHighlight: true
	    });
	}
    // Add required style
    getTabElement("select[req]").each(function(){
    	$(this).next("span").css("border-bottom", "1px solid #FF9800").css("border-bottom-style", "dashed");
    });
    getTabElement('[data-toggle="popover"]').popover();
}


/**
 * Sow delete confirm dialog
 * @param action
 * @param params
 * @param isTable
 */
//var delUrl; var delParams;
function showConfirmDeleteBox(url, params, source, msg, backFunction, title) {
	var formClos = (source ? source.closest("form") : null);
	bootbox.dialog({
        message: '<div class="notice red"><p>'+(msg?msg:'Cet &eacute;l&eacute;ment sera supprim&eacute; d&eacute;finitivement.<br>Voulez-vous confirmer ?')+'</p></div>',
        title: (title ? title : "Confirmation de suppression"),
        className: "modal-darkorange",
        buttons: {
            success: {
                label: "Confirmer",
                className: "btn-warning",
                callback: function () {
                	if(backFunction && backFunction != null && backFunction != ''){
                		eval(backFunction);
                	}
                	submitAjaxForm(url, params, formClos, source);
                }
            },
            "Annuler": {
                className: "btn-danger",
                callback: function () {
                	source.removeAttr("disabled");
                }
            }
        }
	});
	
	/*
	 alertify.set({
         labels: {
           ok: "Confirmer",
           cancel: "Fermer"
         },
         delay: 5000,
         buttonReverse: false,
         buttonFocus: "ok"
       });
       
   alertify.confirm(
   		'<div class="notice red"><p>Cet &eacute;l&eacute;ment sera supprim&eacute; d&eacute;finitivement.<br>Voulez-vous confirmer ?</p></div>', function (e) {
     if (e) {
    	 submitAjaxForm(url, params);
     } else{
    	 source.removeAttr("disabled");
     }
   });*/
}


function updateJsonObj(currId, newValue){
	var data = $.cookie('work_cockies');
	if(!data || data == null || data == ''){
		data = {items: [
		          {id: currId, value: newValue}
		         ]
				};
		$.cookie('work_cockies', JSON.stringify(data));
		return;
	}
	
	var dataJson = jQuery.parseJSON(data);
	var isExist = false;
	for (var i=0; i<dataJson.items.length; i++) {
	  if (dataJson.items[i].id == currId) {
		  dataJson.items[i].value = newValue;
		  isExist = true;
		  break;
	  }
	}
	// Add
	if(!isExist){
		dataJson.items.push(
		    {id: currId, value: newValue}
		);
	}
	$.cookie('work_cockies', JSON.stringify(dataJson));
}

/**
* Save scroll tabble position
*/
function savePosition(){
	updateJsonObj('pos', $(".main-container").scrollTop());
}

/**
* restaure scroll position for divs and wondow
*/
function restorePosition(){
    var positions = getJsonValue('pos');
	
	if(positions != null){
		getTabElement(".main-container").scrollTop(positions);
	}
	// Clean cookie
    updateJsonObj('pos', null);
}

/**
 * 
 */
function showPostAlert(title, message, type){
	if(type == 'ERROR'){
		alertify.error(message);//"Des erreurs de saisie ont &eacute;t&eacute; trouv&eacute;es.<br> Veuillez corriger le formulaire.");
	} else if(type == 'WARNING'){
		alertify.error(message);
	} else{
		alertify.success(message);
	}
}

/**
 * @param action
 * @param targetIds
 * @param skipInit
 * @param skipPost
 * @return
 */
function executePartialAjax(element, action, targetIds, skipInit, skipPost, params, isInput){
	var form = element.closest("form");
	if(form){
		form.find("#w_f_act").val(action);
	}
    var frontUrl = "front?";
	// Form value
	if(params){
		frontUrl = frontUrl + params;
	}
	showSpinner();
    //
    $.ajax({
        beforeSend: function(data) {
        },
        url: frontUrl+"&wpaj=true&wfaj=true&skipI="+skipInit+"&skipP="+skipPost,
        data: form.serialize(),
        type: "POST",
        cache: false,
        dataType: 'text',
        error: function(data) {
        	hideSpinner();
        	console.debug(data);
//            alert("Error-->"+data);
        },
        success: function(data) {
        	 hideSpinner();
        	 //
        	 if(isInput){
        		 component = getTabElement("#"+getJQueryName(targetIds));
        		 if(component){
					 var elmntType = component.prop("nodeName");
					 if(elmntType == 'SPAN' || elmntType == 'DIV'){
						 component.html(data);
					 } else{
						 component.val(data);
					 }
				 }
        		 return;
        	 }
        	 
	   		 var reg = new RegExp("[,;]+", "g");
			 var targetArray = targetIds.split(reg);
			 //
			 for(var i=0; i<targetArray.length; i++){
				 var compId = getJQueryName(targetArray[i]);
				 var targetComp = $(data).find("#"+compId);
				 // Replace
				 getTabElement("#"+compId).replaceWith(targetComp);
			 }
			 // Replace js
			 targetComp = $(data).find("#"+form.attr("name")+"_js_bloc");
			 getTabElement("#"+form.attr("name")+"_js_bloc").replaceWith(targetComp);
			 
			 targetComp = $(data).find("#"+form.attr("name")+"_js_bloc2");
			 getTabElement("#"+form.attr("name")+"_js_bloc2").replaceWith(targetComp);
			 //
			 refreshSpecialComponents();
        }
    });
    return false;
}

/**
 * @param action
 * @param targetIds
 * @param skipInit
 * @param skipPost
 * @return
 */
function executePartialInputAjax(formId, action, skipInit, skipPost, params){
	getTabElement("#"+formId).find("#w_f_act").val(action);
    var frontUrl = "front?";
	// Form value
	if(params){
		frontUrl = frontUrl + params;
	}
	showSpinner();
    //
    $.ajax({
        beforeSend: function(data) {
        },
        url: frontUrl+"&wpaj=true&wfaj=true&skipI="+skipInit+"&skipP="+skipPost,
        data: $("#"+formId).serialize(),
        type: "POST",
        cache: false,
        dataType: 'text',
        error: function(data) {
        	hideSpinner();
        	console.debug(data);
            //alert("Error-->"+data);
        },
        success: function(data) {
        	 hideSpinner();
        	 //
			 var dataArray = data.split(";");
			 //
			 for(var i=0; i<dataArray.length; i++){
				 var comp = dataArray[i];
				 var keyValueArray = comp.split(":");
				 
				 var name = keyValueArray[0];
			
				 if($.trim(name) != ''){
					 var component = getTabElement("#"+getJQueryName(name));
					 var value = keyValueArray[1];
					 //
					 if(component){
						 var elmntType = component.prop("nodeName");
						 if(elmntType == 'SPAN' || elmntType == 'DIV'){
							 component.html(value);
						 }/* else if(elmntType == 'SELECT'){
							 component
							    .find('option')
							    .remove()
							    .end()
							    .append(value);
							 component.trigger("change");
						 } */else{
							 component.val(value);
						 }
					 }
				}
			 }
        }
    });
    return false;
}

function clearAllCookies(){
	$.cookie('work_cockies', null);
	document.cookie = 'work_cockies=; expires=Thu, 01-Jan-70 00:00:01 GMT;';
}

function setTrContent(idx, action){
	// Remttre les plus/minus
	getTabElement("a[id='lnk_det']").each(function(){
		$(this).find("span").attr("class", "fa fa-plus");
	});
	//
	if(getTabElement("#tr_det_"+idx).css("display") == "none"){
		$("a[curr='"+idx+"']").find("span").attr("class", "fa fa-minus");
	} else{
		getTabElement("a[curr='"+idx+"']").find("span").attr("class", "fa fa-plus");
	}
	
	getTabElement("tr[id^='tr_det_']").each(function(){
		if($(this).attr("id") != "tr_det_"+idx){
			$(this).hide();
		}
	});
	if(getTabElement("#tr_consult_"+idx).html().indexOf("<") != -1){
		getTabElement("#tr_det_"+idx).toggle(100);
		return;
	}
	var target = "front?w_f_act="+action+"&art="+idx;
	showSpinner();
	
	$.ajax({
		url: target,
		cache: false,
		success:function(data){
			getTabElement("#tr_consult_"+idx).html(data);
			getTabElement("#tr_det_"+idx).toggle(100);
			hideSpinner();
		},
		error:function(XMLHttpRequest, textStatus, errorThrows){ // erreur durant la requete
			hideSpinner();
		}
	});	
}

function handle_mousedown(e){
    window.my_dragging = {};
    my_dragging.pageX0 = e.pageX;
    my_dragging.pageY0 = e.pageY;
    my_dragging.elem = this;
    my_dragging.offset0 = $(this).offset();
    function handle_dragging(e){
        var left = my_dragging.offset0.left + (e.pageX - my_dragging.pageX0);
        var top = my_dragging.offset0.top + (e.pageY - my_dragging.pageY0);
        $(my_dragging.elem)
        .offset({top: top, left: left});
    }
    function handle_mouseup(e){
        $('body')
        .off('mousemove', handle_dragging)
        .off('mouseup', handle_mouseup);
    }
    $('body')
    .on('mouseup', handle_mouseup)
    .on('mousemove', handle_dragging);
}

function readLocalStorage(name){
	// Check browser support
	if (typeof(Storage) !== "undefined") {
		return localStorage.getItem(name);
	} else {
		$.each(document.cookie.split(/; */), function()  {
			if(splitCookie[0].indexOf(name) != -1){
	    		return(splitCookie[1]);
			}
		});
	}
}
function writeLocalStorage(name, value){
	// Check browser support
	if (typeof(Storage) !== "undefined") {
	  		localStorage.setItem(name, value);
	} else{
		$.cookie(name, value, { expires: 1000 });
	}
}

function callExternalUrl(url){
	try{
		$.getJSON(url, function(data) {
					    
		});
	}catch (e) {
					
	} 
}

function manageCodeBarre(action, onSuccess){
	var chars = []; 
	var pressed = false; 
	$(document).off("keypress").on("keypress", function(e) {
		if(getTabElement("#body-content").find(".imgBarCode").length == 0){
			return;
		}
		 if (e.which >= 48 && e.which <= 57) {
	        chars.push(String.fromCharCode(e.which));
	     }
		 if (pressed == false) {
            setTimeout(function(){
                if (chars.length >= 10) {
                    var barcode = chars.join("");
                    
                    if($.trim(barcode).length > 5){
        	    		e.preventDefault();
        	    		showSpinner();
        	    		$.ajax({  // ajax
        	    			url: 'front?w_f_act='+action+'&cb='+barcode,
        	    			cache: false, // pas de mise en cache
        	    			success:function(html){ // si la requete est un succes
        	    				hideSpinner();
        	    				if(onSuccess && onSuccess != ''){
        	    					eval(onSuccess);
        	    				}
        	    			},
        	    			error:function(XMLHttpRequest, textStatus, errorThrows){ // erreur durant la requete
        	    				hideSpinner();
        	    			}
        	    		});
                    }
                }
                chars = [];
                pressed = false;
            },500);
        }
        pressed = true;
	});
}

function addTokenParams(){
	setTimeout(function(){
		// Ajouter token
		var idxTkn = window.location.toString().indexOf("jtn=");
		if(idxTkn != -1){
			var idxNext = window.location.toString().indexOf("&", idxTkn);
			idxNext = (idxNext == -1 ? window.location.toString().length : idxNext);
			fragSync = window.location.toString().substring(idxTkn, idxNext);
			$("#login_lnk").attr("params", $("#login_lnk").attr("params")+"&"+fragSync);
		}
	}, 1000);
}

/**--------------ANDROID-------------------------- */
function androidPrint(html){
   app.printHtml(html);
}

function replaceErrImg(imgComp){
	imgComp.onerror = null; 
	imgComp.src = 'resources/framework/img/pictures_stack.png';
}

function manageResaZoom(sens){
	var curr = parseFloat($('#resa_zoom').attr('val'));
	
	var val = ('-' == sens ? (curr-0.1) : (curr+0.1));
	$('#resa_zoom').attr('val', val);
	//
	$('.flexigrid').css("zoom", val);
}

function getFloat(val){
	if(val && $.trim(val) != '' && val != 'null'){
		val = $.trim(val).replace(/\s/g, '');
		val = val.replace(/,/g, '.');
		return  parseFloat(val);
	}
	return null;
}
function isEmpty(value) {
  return typeof value == 'string' && !value.trim() || typeof value == 'undefined' || value === null;
}