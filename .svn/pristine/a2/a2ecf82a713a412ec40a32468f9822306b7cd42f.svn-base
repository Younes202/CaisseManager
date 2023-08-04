<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="framework.model.common.constante.ProjectConstante"%>
<%@page import="framework.controller.bean.ColumnsExportBean"%>
<%@page import="java.util.List"%>
<%
List<ColumnsExportBean> listFilterCondition = (List<ColumnsExportBean>)request.getAttribute("filtrable_beans");
String tableName = (String)request.getAttribute("filtrable_table");
%>

<script type="text/javascript">
function loadTableSelectAuto(idSelect, act, id, lib){
	 $("#"+getJQueryName(idSelect)).select2({
           language: "fr",
           minimumInputLength: 3,
           tags: [],
           placeholder: "Filtre",
           allowClear: true,
           ajax: {
               url: 'front?w_f_act='+act,
               dataType: 'json',
               type: "GET",
               quietMillis: 50,
               processResults: function (data) {
                   return {
                       results: $.map(data.items, function (item) {
                           return {
                        	   text: (lib ? getTableLib(item, lib) : getArtTabLib(item)),
                               id: item.id
                           }
                       })
                   };
               }
           }
       }); 
	}
	function getArtTabLib(item){
		var val = '';
		if(item.code_barre && item.code_barre != ''){
			val += item.code_barre+' ';	
		}
		val += item.libelle;
		if(item.composition && item.composition != ''){
			val += '('+item.composition+')';	
		}
		
		return val;
	}
	function getTableLib(item, lib){
		if(lib.indexOf(";") != -1){
			var dataArray = lib.split(";");
			var dataV = "";
			for(var i=0; i<dataArray.length; i++){
				var lib = dataArray[i];
				if(lib.startsWith("'")){
					lib = lib.substring(1, (lib.length-1));
				} else{
					lib = eval("item."+lib);
				}
				dataV += lib;
			}
			return  dataV;
		} else{
			return  eval(lib);
		}
	}
	function cleanFilterUpSelected(){
		$(".autoSlct").each(function(){
			$(this).find("option").each(function() {
				if(!$.isNumeric($(this).val())){
					$(this).remove().trigger("change");
				 }
			});
		});
	}
	
$(document).ready(function (){
	$(".filtrSlct").select2({
      allowClear: true
  	});
	
	$("select[id*='work_cond_']").each(function(){
		var type  = $("#"+getJQueryName($(this).attr("id").replace(/\work_cond_/g,"work_type_"))).val();
		var field = $("#"+getJQueryName($(this).attr("id").replace(/\work_cond_/g,"work_filter_")));
		//		
		if(field.is("select")){
			field.css("width", "80% !important");
		} else{
			manageInputFilter(field, type);
		}
	});
	
	$("select[id*='work_cond_']").change(function(){
		var type = $("#"+getJQueryName(($(this).attr("id").replace(/\work_cond_/g,"work_type_")))).val();
		var field = $("#"+getJQueryName($(this).attr("id").replace(/\work_cond_/g,"work_filter_")));
		var field2 = $("#"+getJQueryName(field.attr("id")+"_to")); 
		
		//
		if($(this).val() == '> | <' || $(this).val() == '>= | <='){
			if(field2.length == 0){
				field2 = field.clone().attr("id", field.attr("id")+"_to").attr("name", field.attr("id")+"_to");
				manageInputFilter(field2, type);
			} else{
				field2.show();
			}
			if(type == 'date' || type == 'dateTime'){
				field2.datepicker({
			    	todayBtn: true,
			    	clearBtn: true,
				    language: "fr",
				    autoclose: true,
				    todayHighlight: true
			    });
			}
			field.closest("div").append(field2);
		} else{
			if(field2.length == 1){
				field2.val("");
				field2.hide();
			}
		}
	});
	
	if($("#dateDebut").length > 0){
		$("#dateDebut_fltr").val($("#dateDebut").val());
		$("#dateFin_fltr").val($("#dateFin").val());
	}
});

function manageInputFilter(field, type){
	if(type == 'integer' || type == 'long'){
		field.css("width", "150px");
		field.autoNumeric('init', {vMin: '-9999999999',vMax: '9999999999', aSep: ' '});
	} else if(type == 'decimal'){
		field.css("width", "150px");
		field.autoNumeric('init', {vMin: '-9999999999.99',vMax: '9999999999.99', aSep: ' '});
	} else if(type == 'date' || type == 'dateTime'){
		field.mask("99/99/9999",{placeholder:" "});
		field.css("width", "100px");
	} else{
		field.css("width", "80%");
	}
}
</script>


<style>
	.ui-dialog .ui-dialog-titlebar{
	    background-color: #c2b99b !important;
	}
	.ui-dialog .ui-dialog-buttonpane{
	    background: #e6e6e6 !important;
   }
</style>

<a href="javascript:" id="<%=tableName %>_modal_filter_link" data-toggle="modal" data-backdrop="static" data-keyboard="false" data-target="#<%=tableName %>_filter_div" style="display: none;"></a>

 <div id="<%=tableName %>_filter_div" class="modal modal-message modal-warning fade" style="display: none;" aria-hidden="true">
 <div class="modal-dialog" style="min-width: 700px;">  
	<div class="modal-content" style="min-width: 700px;">
 		<!-- widget grid -->
		<div class="widget">
         <div class="widget-header bordered-bottom bordered-blue">
            <span class="widget-caption">Filtrer les donn&eacute;es</span>
         </div>
         <div class="widget-body">
			<form id="<%=tableName %>_filter_form" name="<%=tableName %>_filter_form">
				<%--Corection bu filtre date debut et fin qui sont dans le formulaire --%>
				<input type="hidden" name="dateDebut" id="dateDebut_fltr">
				<input type="hidden" name="dateFin" id="dateFin_fltr">
				<input type="hidden" name="<%=ProjectConstante.WORK_FORM_ACTION%>" id="<%=ProjectConstante.WORK_FORM_ACTION%>"/>
				<%
				if((listFilterCondition != null) && (listFilterCondition.size() > 0)) {
					for(ColumnsExportBean column : listFilterCondition){%>
						<div class="row" style="margin-top: 5px;">
							<div class="col-md-3">
								<%=column.getLabel() %>
							</div>
							<div class="col-md-3">
								<%=column.isSortable() %> 
							</div>
							<div class="col-md-6">
								<%=column.isFiltrable() %>
							</div>
						</div>
				<%	  }
					}%>
			</form>
	</div>
	<div class="modal-footer">
         <button type="button" class="btn btn-info" id="<%=tableName %>_filter_btn" onclick="cleanFilterUpSelected();manageFilterState('<%=tableName%>');"> Filtrer les donn&eacute;es</button>
         <button type="button" id="<%=tableName %>_filter_close" class="btn btn-danger" data-dismiss="modal"><i class="fa fa-times"></i> Fermer</button>
     </div>
	</div>
	</div>
	</div>
	</div>
