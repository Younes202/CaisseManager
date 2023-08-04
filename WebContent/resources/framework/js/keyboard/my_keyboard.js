var global_input_focus = null;
//

function init_keyboard_events(){
	var isCookActivate = $.cookie('work_keyboard');
	
	if(isCookActivate && isCookActivate == "true"){
		$("#keyboard-activator").attr("checked", "checked");
		iniKeyBoard("input[type='text']:not([readonly]), input[type='password']:not([readonly]), textarea:not([readonly])", true);
	}
	
	$(document).off('click', '#keyboard-activator');
	$(document).on('click', '#keyboard-activator', function(){
		var isActivate = $(this).prop("checked");
		if(isActivate){
			iniKeyBoard("input[type='text']:not([readonly]), input[type='password']:not([readonly]), textarea:not([readonly])", true);
		} else{
			iniKeyBoard("input[type='text']:not([readonly]), input[type='password']:not([readonly]), textarea:not([readonly])", false);
		}
		$.cookie('work_keyboard', isActivate);
	});
}

function iniKeyBoard(selector, addEvent){
	if(!addEvent){
		$(selector).unbind("focusin");
		$("#close-keyboard").trigger("click");
		return;
	}
	
	$(selector).focusin(function(){
		var isActivate = $.cookie('work_keyboard');
		if(!isActivate){
			return;
		}
		var isNum = ($(this).css("text-align") == 'right');
		if((isNum && $("#keyboard-div-num").css("display") == "none") || (!isNum && $("#keyboard-div").css("display") == "none")){
			var position = $(this).position();
			var widowHeight = $(window).height();
			
			// Numeriques
			if(isNum){
				$("#keyboard-div").hide();
				$("#keyboard-div-num").show();
			} else{
				$("#keyboard-div").show();
				$("#keyboard-div-num").hide();
			}
			
			
//			$("#keyboard-div").css("top", ($(document).height()-$("#keyboard-div").outerHeight(true))+"px"); 
//			$("#keyboard-div").css("left", "0");
			//
			if(global_input_focus == null){
				ini_keyboard_keys_events();
			}
		}
		global_input_focus = $(this);
	});
}

function ini_keyboard_keys_events(){
	$(document).off('click', '#close-keyboard');
	$(document).on('click', '#close-keyboard', function(){
		$("#keyboard-div").hide(1000);
		$("#keyboard-div-num").hide(1000);
	});
	
	$(document).off('click', '#upper-keyboard');
	$(document).on('click', '#upper-keyboard', function(){
		var isUpper = $(this).prop("checked");
		$("#keys-keyboard a").each(function(){
			if($(this).attr("id") != 'reset' && $(this).attr("id") != 'back'){
				if(isUpper){
					$(this).text($(this).text().toUpperCase());
				} else{
					$(this).text($(this).text().toLowerCase());
				}
			}
		});
	});
	
	$(document).off('click', '#keys-keyboard a');
	$(document).on('click', '#keys-keyboard a', function(){
		if(global_input_focus != null){
			if($(this).attr("id") == 'reset'){
				global_input_focus.val('');
			} else if ($(this).attr("id") == 'back'){
				global_input_focus.val(global_input_focus.val().substring(0, global_input_focus.val().length-1));
			} else{
				global_input_focus.val(global_input_focus.val()+$(this).text());
			}
			global_input_focus.focus();
		}
	});
}