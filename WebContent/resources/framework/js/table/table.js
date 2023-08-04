
	function buildOrder(tableName, action){
		var jqTableName = getJQueryName(tableName);
		var form = (form && form!=null) ? form : $("#"+jqTableName).closest("form");
		
		var order = '';
		$("#"+tableName+"_body tr[drg]").each(function(){
			order += $(this).attr("drg")+"|";
		});
		$("#"+tableName+"_order-h").val(order);
		submitAjaxForm(action, "wibaj=1&tbl="+tableName, form);
	}

	/**
	 * @param tableName
	 * @param action
	 */
	function ajaxCommunTableAction(tableName, form, params){
		var jqTableName = getJQueryName(tableName);
		var form = (form && form!=null) ? form : getTabElement("#"+jqTableName).closest("form");
		  $tarTab = $("#tabMnuNavContent .tab-pane[class*='active']");
		// Show loading animation
		showSpinner();
			
		// Add hidden element
		if(getTabElement("#work_body_table").length == 0){
			$('<input>').attr({
			    type: 'hidden',
			    id: 'work_body_table',
			    name: 'work_body_table',
			    value: tableName
			}).appendTo(form);
		}
        // Body table div id
        
		var divId = "corp_" + jqTableName;
		// Ajax submit
	    //
	    var frontUrl = "front";
		// Form value
		frontUrl = frontUrl + '?nosave=true'+(params?"&"+params:""); 
		if($tarTab){
			frontUrl = frontUrl+"&curr_tab_id="+$tarTab.attr("id");
		}
	    // Put an animated GIF image insight of content
//		var iconRef = "<i class='fa fa-refresh'></i>";
//	    $("#refreshrow_"+jqTableName).css("background", "url('resources/img/table/load.gif') no-repeat;").find("i").remove();
		// Save position
		savePosition();
	    //
	    $.ajax({
	        beforeSend: function(data) {
	        },
	        url: frontUrl,
	        data: form.serialize(),
	        type: "POST",
	        cache: false,
	        dataType: 'text',
	        error: function(data) {
	            getTabElement('#'+divId).html(data);
	            // Restaure scroll positions
	            restorePosition();
	        },
	        success: function(data) {
	            // Hide sinner
	            hideSpinner();
	            
	        	getTabElement("#work_body_table").remove();
//	        	$("#refreshrow_"+jqTableName).attr("src", "resources/img/table/refresh.png");
	        	
	        	// Json error response
				if(data.indexOf("_err_founded") != -1	&& data.indexOf(">") == -1){
					manageErrorMessages(data);
	        		return;
				}
				
				var corpContent = $(data).filter('#'+divId).html();// Mieux que find
				
				if(!corpContent){
					corpContent = $(data).find('#'+divId).html();
				}
				
	            getTabElement('#'+divId).html(corpContent);
	            
	            // Restaure scroll positions
	            restorePosition();
	        }
	    });
	  return false;
	}

	/**
	* Pager with ajax method
	*/
	function pagerAjaxTable(tableName, cp, action) {
		var jqTableName = getJQueryName(tableName);
		//document.getElementById(tableName+'_pager.cp').value = cp;
		getTabElement("#"+jqTableName+'_pager\\.cp').val(cp);
		getTabElement("#"+jqTableName).closest("form").find("#w_f_act").val(action);
		
		// Call ajax action
		
		if(getTabElement("#corp_" + jqTableName).length == 0){
			submitAjaxForm(action, null, $("#"+jqTableName).closest("form"), null);
		} else{
			ajaxCommunTableAction(tableName);			
		}
	}

	/**
	* Build link for header and submit
	*/
	function sort_act(act, tableName, field, order, curent_page, e){
		var jqTableName = getJQueryName(tableName);
		 var form = getTabElement("#"+jqTableName).closest("form");
		 form.find("#w_f_act").val(act);
		
		var ctrlPressed=0;

	   	// update hidden fields
	   	var fieldTab = getTabElement("#"+jqTableName+'_pager\\.fie');

		var cpTab = getTabElement("#"+jqTableName+'_pager\\.cp');
		// Update current page
		cpTab.val(curent_page);
		var newValue = field +":" + order;

	    // Alt key
		if (parseInt(navigator.appVersion)>3){
			  var evt = navigator.appName=="Netscape" ? e:event;
			  //
			  if (navigator.appName=="Netscape" && parseInt(navigator.appVersion)==4) {
				   var mString =(e.modifiers+32).toString(2).substring(3,6);
				   ctrlPressed =(mString.charAt(1)=="1");
			  } else{
				   ctrlPressed = evt.ctrlKey;
			  }
			  
			 if (ctrlPressed){
				var oldValue = fieldTab.val();
				var valIndex = oldValue.indexOf(field +":");
				// If this fiels exist already in hidden field value
				if(valIndex != -1){
					var leftValue = oldValue.substring(0, valIndex);
					var idxEnd = oldValue.indexOf(";", (valIndex+1));
					var rightValue = (idxEnd==-1) ? '' : oldValue.substring(idxEnd, oldValue.length);
					//
					fieldTab.val(leftValue + newValue + rightValue);
				} else{
					fieldTab.val(fieldTab.val() + ";" + newValue);
				}
			}
		}
		// if not alt pressed
		if(!ctrlPressed){
			fieldTab.val(newValue);
		}

		// Call ajax action
		ajaxCommunTableAction(tableName);
	}

	/**
	* test if checkbox is selected
	*/
	function isCheckedSelected(tableName){
		var pager_check = getTabElement("#"+tableName+'_check_save');
		if(pager_check){
			var chaine = pager_check.val();
			var reg = new RegExp("[ ,|]+", "g");
			var table = chaine.split(reg);
			//
			if(table.length <= 1){
				return false;
			}
		}

		return true;
	}

	/**
	 * @param tableName
	 * @param hrefComp
	 */
	function testUpdateRows(tableName, hrefComp){
		if(isCheckedSelected(tableName)){
			var idCheck = tableName + "_check_save";
			var hrefValue = hrefComp.attr('href');
			var idxCheck = hrefValue.indexOf('&work_chckurl_ids');
			var newHrefValue = "";
			var checkedValue = getTabElement("#"+idCheck).val();
			//
			if(idxCheck != -1){
				var leftHrefValue = hrefValue.substring(0, idxCheck);
				var idxAnd = hrefValue.indexOf('&', (idxCheck+1));
				var rightHrefValue = hrefValue.substring(idxAnd, hrefValue.length);
				//
				newHrefValue = leftHrefValue + '&' + 'work_chckurl_ids='+checkedValue + rightHrefValue;
			} else{
				// Replace
				var regCheck = new RegExp("(LIST_CHECK)", "g");
				newHrefValue = hrefValue.replace(regCheck, 'work_chckurl_ids='+checkedValue);
			}
			//
			hrefComp.attr('href', newHrefValue);
			return true;
		} else{
			showCheckRequired();
			return false;
		}
	}

	/**
	*
	*/
	function testCheckedDeleteRows(tableName, action, params){
		if(isCheckedSelected(tableName)){
			if(params && params != ''){
				params = params + "&";
			}
			showConfirmDeleteRowsBox(tableName, action, (params+"work_table="+tableName));
		} else{
			showCheckRequired();
		}
	}

	/**
	*
	*/
	function testCheckedUpdateGroup(tableName, ctrlAct, params){
		if(isCheckedSelected(tableName)){
			if(params && params != ''){
				params = params + "&";
			}
			submitAjaxForm(ctrlAct, (params+"work_table="+tableName));
		} else{
			showCheckRequired();
		}
	}

	/**
	*
	*/
	//var currjqTableName;
	function exportTable(currjqTableName, initAction){
		var tableName = currjqTableName.replace(/\\./g,".");
			// Save order
			var savedOrder = "";
			var table = getTabElement("#"+tableName+"_selected");
			// get all the rows as a wrapped set
			var rows = jQuery("tr", table);
 		rows.each(function() {
				// Iterate through each row, the row is bound to "this"
				var row = jQuery(this);
				savedOrder = savedOrder + "|" + row.attr("id");
			});

			// Show message if no element was selected
			if((savedOrder == "") || (savedOrder == "|") || (savedOrder == "|undefined")){
				showCheckRequired();
			} else{
				// Save order of rows
				getTabElement("#work_export_order").val(savedOrder);
				//
				var formField = document.forms["export_form"];
				
				//
				var actionHidden = formField.w_f_act;
				actionHidden.value = initAction;
				
				// Form value
				formField.submit();
			}
	}
	
	function showExportBox(jqTableName, initAction) {
		getTabElement("#"+jqTableName+"_export_btn").attr("onClick", "exportTable('"+jqTableName+"', '"+initAction+"');");
		getTabElement("#"+jqTableName+"_export_close").trigger('click');
		getTabElement("#"+jqTableName+"_modal_export_link").trigger("click");
		
		/*currjqTableName = jqTableName;
		
		bootbox.dialog({
	        message: $("#"+jqTableName+"_export_form").html(),
	        title: "Export des données",
	        className: "modal-darkorange",
	        buttons: {
	            success: {
	                label: "Lancer l'export",
	                className: "btn-blue",
	                callback: function () {
	                	var tableName = currjqTableName.replace(/\\./g,".");
	 					// Save order
	 					var savedOrder = "";
	 					var table = document.getElementById(tableName+"_selected");
	 					// get all the rows as a wrapped set
	 					var rows = jQuery("tr", table);
	 	        		rows.each(function() {
	 						// Iterate through each row, the row is bound to "this"
	 						var row = jQuery(this);
	 						savedOrder = savedOrder + "|" + row.attr("id");
	 					});

	 					// Show message if no element was selected
	 					if((savedOrder == "") || (savedOrder == "|") || (savedOrder == "|undefined")){
	 						showCheckRequired();
	 					} else{
	 						// Save order of rows
	 						document.getElementById("work_export_order").value = savedOrder;
	 						//
	 						var formField = document.forms["export_form"];
	 						// Form value
	 						formField.submit();
	 					}
	                }
	            },
	            "Fermer": {
	                className: "btn-danger",
	                callback: function () {}
	            }
	        }
		});*/
	}
	
	/**
	 * Filter column
	 * @param jqTableName
	 * @param action
	 */
	function addFilterEvent(jqTableName, action){
		$(document).off('click', "#"+jqTableName+"_img_show");
		$(document).on('click', "#"+jqTableName+"_img_show", function(){		
			if(getTabElement("#"+jqTableName+"_filter_btn").attr("trig") != "1"){
				getTabElement("#"+jqTableName+"_filter_btn").attr("onClick", $("#"+jqTableName+"_filter_btn").attr("onClick")+"filter('"+jqTableName+"', '"+action+"');$('#"+jqTableName+"_filter_close').trigger('click');");
				getTabElement("#"+jqTableName+"_filter_btn").attr("trig", "1");
			}
			getTabElement("#"+jqTableName+"_modal_filter_link").trigger("click");
		});
	}
	

	/**
	* Filter table
	*/
	function filter(tableName, action){
		var currForm = getTabElement("#"+tableName+"_filter_form");

		if(currForm.length == 0){ 
			currForm = getTabElement("#"+getJQueryName(tableName)).closest("form");
		}
		currForm.find("#w_f_act").val(action);
		
		// Clear saved check if filter is called
		clearSavedChecks(tableName);
		// Call ajax action
		ajaxCommunTableAction(tableName, currForm, "is_filter_act=1");
	}
	
	/**
	*
	*/
	/*
	var currjqTableName;
	function showFilterBox(jqTableName, action) {
		currjqTableName = jqTableName;
		
		bootbox.dialog({
	        message: $("#"+jqTableName+"_filter_div").html(),
	        title: "Filtrer les données",
	        className: "modal-darkorange",
	        buttons: {
	            success: {
	                label: "Filtrer",
	                className: "btn-blue",
	                callback: function () {
	                	filter(currjqTableName, action);
	                }
	            },
	            "Fermer": {
	                className: "btn-danger",
	                callback: function () {}
	            }
	        }
		});
	}*/

	/**
	* Clear saved checkbox if  filter is used
	*/
	function clearSavedChecks(tableName){
		var pager_check = document.getElementById(tableName+'_check_save');
		if(pager_check){
			var hiddenValue = pager_check.value;
			if(hiddenValue){
				pager_check.value = '';
			}
		}
	}

	/**
	 * @param tableName
	 */
	function initRowsTable(tableName, isCheckable){
		// Table name
		var bodyTableName = tableName + "_body";
		// Sow tootltip		
		getTabElement("#"+bodyTableName+" td").bind('mouseenter', function(){
		    var $this = $(this);
		    if(!$this.attr("noresize") && this.offsetWidth < this.scrollWidth && !$this.attr('title')){
		        $this.attr('title', $this.text());
		    }
		});
		
		//
		getTabElement("#"+bodyTableName+" tr").dblclick(
			function(event){
				var btnEdit = $(this).find(".btn-primary");
				btnEdit.click();
			}
		);
		// Onmouse over and on mouse out
	    getTabElement("#"+bodyTableName+" tr").mouseover(
			function(){
				// Remove old class (bug context menu)
				$(".over").each(function() {
					$(this).removeClass("over");
					$(this).css("cursor", "");
				});
				//
				$(this).removeClass("sel");
				$(this).addClass("over");
				$(this).css("cursor", "pointer");
			})
		.mouseout(
			function(){
				// Color selected line
				var checked = $($(this).find('input[type=checkbox]')).prop("checked");
				if(checked){
					$(this).addClass("sel");
				}
				$(this).removeClass("over");
				$(this).css("cursor", "");
		});
	    // Alternate colors
		getTabElement("#"+bodyTableName+" tr[class!='sub']:even").addClass("alt");
		getTabElement("#"+bodyTableName+" tr[class!='sub']:odd").addClass("out");

		// Hightlight selected row
		getTabElement('#'+bodyTableName+' input[type=checkbox]').click(function() {
			checkboxTableChecked(tableName, $(this));
		});
		// Check all boxes
		getTabElement("#"+tableName+"_check_all").click(function() {
			// Add check all to saved
			saveChecked(tableName, $(this));
			//
			var checkedStatus = this.checked;
			getTabElement("#"+bodyTableName+" tbody tr td:first-child input:checkbox").each(function() {
				this.checked = checkedStatus;
				var parentTr = $(this).closest("tr");
				if(checkedStatus){
					parentTr.addClass("sel");
				} else{
					parentTr.removeClass("sel");
				}
				// SAve checked
				saveChecked(tableName, $(this));
			});
		});
		// Restaure checked rows
		getTabElement("#"+bodyTableName+" tbody tr td:first-child input:checkbox").each(function() {
			var parentTr = $(this).closest("tr");
			if(this.checked){
				parentTr.addClass("sel");
			} else{
				parentTr.removeClass("sel");
			}
		});
	}

	/**
	 * @param bodyTableName
	 * @param checkboxComp
	 */
	function checkboxTableChecked(tableName, checkboxComp){
		var bodyTableName = tableName + "_body";
		var parentTr = checkboxComp.closest("tr");
		//
		if(checkboxComp.prop("checked") == true){
			parentTr.addClass("sel");
		} else{
			parentTr.removeClass("sel");
			parentTr.removeClass("over");
		}
		// Manage checked and check all
		var cptChecked = 0; var cptCheck = 0;
		getTabElement("#"+bodyTableName+" tbody tr td:first-child input:checkbox").each(function() {
			if(this.checked){
				cptChecked++;
			}
			cptCheck++;
		});

		// Check all or uncheck all
		var checkAll = $("#"+tableName+"_check_all");
		//
		if(cptCheck == cptChecked){
			checkAll.prop("checked", true);
			// SAve checked
			saveChecked(tableName, checkAll);
		} else if(cptChecked == 0){
			checkAll.prop("checked", false);
			// SAve checked
			saveChecked(tableName, checkAll);
		}
		// Save checked
		saveChecked(tableName, checkboxComp);
	}

	/**
	* Save state of ceckbod used in table
	*/
	function saveChecked(tableName, check){
		var pager_check = getTabElement("#" + tableName + "_check_save");
		// Hidden check
		if((pager_check.length > 0) && (check.length > 0)){
			var hiddenValue = pager_check.attr('value');
			var checkId = check.attr("value");
			var checked = check.prop("checked");
			//
			var idxId = hiddenValue.indexOf('|'+checkId+'|');
			// If checked
			if(checked){
				if(idxId == -1){
					var delimit = hiddenValue == '' ? '|' : '';
					pager_check.attr('value', (delimit + hiddenValue + checkId + "|"));
				}
			} else{
				if(idxId != -1){
					var leftStr = hiddenValue.substring(0, (idxId+1));
					var rightStr = hiddenValue.substring((hiddenValue.indexOf('|', (idxId+2))+1), hiddenValue.length);
					// Concat
					var finalValue = leftStr + rightStr;
					// Set value
					pager_check.attr('value', ((finalValue == '|') ? '' : finalValue));
				}
			}
		}
	}

	 /*
	  *
	  */
	 function showCheckRequired() {
		 alertify.error("Vous devez s\u00E9l\u00E9ctionner au moins une ligne.");
	 }

	/**
	*
	*/
	var delAction, delParams;
	function showConfirmDeleteRowsBox(tableName, action, params) {
		delAction = action;
		delParams= params;	
		
		bootbox.dialog({
	        message: '<div class="notice red"><p>Ces &eacute;l&eacute;ments seront supprim&eacute;s d&eacute;finitivement.<br>Voulez-vous confirmer ?</p></div>',
	        title: "Confirmation de suppression",
	        className: "modal-darkorange",
	        buttons: {
	            success: {
	                label: "Confirmer",
	                className: "btn-warning",
	                callback: function () {
	                	submitAjaxForm(delAction, delParams, $("#"+tableName).closest("form"));
	                }
	            },
	            "Annuler": {
	                className: "btn-danger",
	                callback: function () {
	                	
	                }
	            }
	        }
		});
	}
		/*
	* Adjust td widths
	*/
	function adjustWidthTds(tableName){
		var bodyTableName = tableName + "_body";
		var cpt = 1;
		//$.browser.chrome = /chrome/.test(navigator.userAgent.toLowerCase()); 

		var decalRows = 2;
		var cols = "<colgroup>";
		getTabElement("#"+tableName+" thead tr th").each(function() {
			var thWidth = parseInt($(this).attr('width'));

			cols = cols + "<col";
			if(thWidth > 0){
				// For scroll
				if(thWidth == 18){
					thWidth = 3;
				}
				if($(this).attr("noresize") != "true"){
					var width = (thWidth-decalRows);
					cols = cols + " width='"+width+"'";
				}
			}
			
			cols = cols + " />";
		});
		cols = cols + "<col width='1'/></colgroup>";

		getTabElement('#'+bodyTableName).prepend(cols);
		
		//
		/*$("#"+tableName+" thead tr th").each(function() {
			var thWidth = parseInt($(this).attr('width'));

			if(thWidth > 0){
				// For scroll
				if(thWidth == 18){
					thWidth = 3;
				}

				// Update width
				$('#'+bodyTableName+' tr td:nth-child('+cpt+')').each(function() {
					// FireFox
					var decalRows = 2;
					// Decal for chrome naviagtor
//					if($.browser.chrome){
//						decalRows = 0;
//					}
					//
					if($(this).attr("noresize") != "true"){
						$(this).css('width', (thWidth-decalRows));
					}
					// Align center if img
					if($(this).find('img').length > 0 && $(this).attr("align") == null){
						$(this).css('text-align', 'center');
					}
				});
			}
			cpt++;
		});*/

		// Fade current table --> Courrige le bug du redminsionnement visible
		$('#flex_'+tableName).fadeIn(500, function () {});
	}


	/**
	 * @param tableName
	 */
	function alternateHighlightRows(tableName){
		// Onmouse over and on mouse out
	    getTabElement("#"+tableName+" tr").mouseover(
			function(){
				// Remove old class (bug context menu)
				getTabElement(".over").each(function() {
					$(this).removeClass("over");
				});
				//
				$(this).removeClass("sel");
				$(this).addClass("over");
			})
		.mouseout(
			function(){
				// Remove over style if contexte menu is note visible
				$(this).removeClass("over");
		});
	    // Alternate colors
		getTabElement("#"+tableName+" tr:even").addClass("alt");
		getTabElement("#"+tableName+" tr:odd").addClass("out");
	}

	/**
	 * Reinit table and delete all table cookies
	 * @param jqTableName
	 */
	function reinitTable(jqTableName, action){
		updateJsonObj("col_"+jqTableName, null);
		updateJsonObj(jqTableName+"_work_condition", null);
		//
		clearFilterPopup(jqTableName);
	    // Refresh table
	    pagerAjaxTable(jqTableName, '-1', action);
	}

	function clearFilterPopup(tableName){
		getTabElement("#"+tableName+"_img_show").css("border", "none");
		
		getTabElement("#"+tableName+"_filter_div").find(':input').each(function(){
			if($(this).attr("type") != 'hidden'){
				$(this).val('');
			}
		});
		getTabElement("#"+tableName+"_filter_div").find('select').each(function(){
			if($(this).attr("id").indexOf("work_cond") == -1){
				$(this).val("");
			} else{
				$(this).val($(this).prop("selectedIndex", 0).val());
			}
			$(this).trigger("change");
		});
	}
	
	function getJsonValue(currId){
		var data = $.cookie('work_cockies');
		if(!data || data == null || data == ''){
			return null;
		}
		var dataJson = jQuery.parseJSON(data);
		for (var i=0; i<dataJson.items.length; i++) {
		  if (dataJson.items[i].id == currId) {
		    return dataJson.items[i].value;
		  }
		}
		return null;
	}

	function manageFilterState(tableName){
		var isFilter = false;
		getTabElement("select[id^='"+tableName+"_work_filter'], input[id^='"+tableName+"_work_filter']").each(function(){
			if($.trim($(this).val()) != ''){
				isFilter = true;
			}
		});
		if(isFilter){
			getTabElement("#"+tableName+"_img_show").css("border", "2px solid red");
		} else{
			getTabElement("#"+tableName+"_img_show").css("border", "");
		}
	}
	
	/**
	 * Initialiser le tableau 
	 * */
	function manageRefreshTable(tableId, isChecked){
		// Init events
		initRowsTable(tableId, isChecked);
		// Adjust widths
		adjustWidthTds(tableId);
		
		manageFilterState(tableId);
	}
	
	/**
	 * Bug bootstrap dropdown
	 */
	function manageDropMenu(tableId){
		//fix menu overflow under the responsive table 
		// hide menu on click... (This is a must because when we open a menu )
		getTabElement("a[data-toggle]").click(function (event) {
			 $('.dropdown-menu[data-parent]').hide();
		});
		
		$(document).click(function (event) {
		    //hide all our dropdowns
		    getTabElement('.dropdown-menu[data-parent]').hide();

		});
		$(document).on('click', '#'+tableId+'_body [data-toggle="dropdown"]', function () {
		    // if the button is inside a modal
		    if ($('body').hasClass('modal-open')) {
		        throw new Error("This solution is not working inside a responsive table inside a modal, you need to find out a way to calculate the modal Z-index and add it to the element")
		        return true;
		    }

		    $buttonGroup = $(this).parent();
		    if (!$buttonGroup.attr('data-attachedUl')) {
		        var ts = +new Date;
		        $ul = $(this).siblings('ul');
		        $ul.attr('data-parent', ts);
		        $buttonGroup.attr('data-attachedUl', ts);
		        $(window).resize(function () {
		            $ul.css('display', 'none').data('top');
		        });
		    } else {
		        $ul = $('[data-parent=' + $buttonGroup.attr('data-attachedUl') + ']');
		    }
		    if (!$buttonGroup.hasClass('open')) {
		        $ul.css('display', 'none');
		        return;
		    }
		    dropDownFixPosition($(this).parent(), $ul);
		    function dropDownFixPosition(button, dropdown) {
		        var dropDownTop = button.offset().top + button.outerHeight();
		        dropdown.css('top', dropDownTop + "px");
		        dropdown.css('left', (button.offset().left-dropdown.width()+button.width()) + "px");
		        dropdown.css('position', "absolute");

		        dropdown.css('width', dropdown.width());
		        dropdown.css('heigt', dropdown.height());
		        dropdown.css('display', 'block');
		        dropdown.appendTo('body');
		    }
		});
	}