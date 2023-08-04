<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>


<script type="text/javascript">
	$(document).ready(function() {
		$(".selectCustom").each(function(){
			<%if(!ControllerUtil.isEditionWritePage(request)){%>
				$(this).attr("disabled", "disabled");
			<%}%>
			loadSelectAuto(getJQueryName($(this).attr("id")));
		});
	
		$(document).off('change', '#article\\.type_art').on('change', '#article\\.type_art', function(){
			manageGeneric();
		});
		setTimeout(() => {
			manageGeneric();	
		}, 1000);
		
	});
	function manageGeneric(){
		if($('#article\\.type_art').val() == 'G'){
			$("#div-type").css("display", "");
		} else{
			$("#div-type").css("display", "none");
		}
	}
	function loadSelectAuto(idSelect){
		 $("#"+idSelect).select2({
	            language: "fr",
	            minimumInputLength: 3,
	            tags: [],
	            placeholder: "Article",
	            allowClear: true,
	            ajax: {
	                url: 'front?w_f_act=<%=EncryptionUtil.encrypt("stock.mouvement.getListArticles")%>&isAct=1',
	                dataType: 'json',
	                type: "GET",
	                quietMillis: 50,
	                processResults: function (data) {
	                    return {
	                        results: $.map(data.items, function (item) {
	                            return {
	                                text: getArtLib(item),
	                                id: item.id
	                            }
	                        })
	                    };
	                }
	            }
	        }); 
		 	$("#"+idSelect).on('select2:select', function (e) {
			 cleanUpSelected();
			});
		}
		function cleanUpSelected(){
			$("select[id^='opc_article']").each(function(){
				if(!$.isNumeric($(this).val())){
					$(this).empty().trigger("change");
				 }
			});
		}
		function getArtLib(item){
			var val = '';
			val += '['+item.code+']'+'-'+item.libelle;
			
			if(item.code_barre && item.code_barre != ''){
				val += '['+item.code_barre+'] ';	
			}
			
			if(item.composition && item.composition != '' && item.composition != 'undefined'){
				val += '('+item.composition+')';	
			}
			
			return val;
		}
</script>

<div class="form-group">
	<std:label classStyle="control-label col-md-2" value="Taux de remboursement"/>
	<div class="col-md-4">
		<std:text name="article.taux_remboursement" type="decimal" style="width:120px;" />
	</div>
	<std:label classStyle="control-label col-md-2" value="Unité de dosage"/>
	<div class="col-md-4">
		<std:select name="article.opc_unite_dosage_enum.id" type="long" key="id" labels="libelle" data="${listeDosage}" required="true" />
		<i class="fa fa-reorder" style="color:blue;" title="Liste énumérée"></i>
	</div>
</div>
<div class="form-group">
	<std:label classStyle="control-label col-md-2" value="Type"/>
	<div class="col-md-4">
		<std:select name="article.type_art" type="string" data="${typeArticle }" required="true" />
	</div>
	<div id="div-type" style="display: none;">
		<std:label classStyle="control-label col-md-2" value="Médicaments principaux"/>
		<div class="col-md-4">
			<select class="selectCustom" multiple="multiple" name="generic_ids" id="generic_ids" style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;; width: 100% !important;">
				<c:forEach var="gen" items="${list_generic }">
					<option value="${gen.opc_article.id }" selected="selected" id="${gen.opc_article.id }">
						${gen.opc_article.code }-${gen.opc_article.libelle } 
					</option>
				</c:forEach>
			</select>
		</div>
	</div>
</div>
<div class="form-group">	
	<std:label classStyle="control-label col-md-2" value="Composition"/>
	<div class="col-md-4">
		<std:text name="article.composition" type="string" placeholder="Composition" maxlength="30" style="width: 50%;"/>
	</div>
	<std:label classStyle="control-label col-md-2" value="Conditionnement"/>
	<div class="col-md-4">
		<std:text name="article.conditionnement" type="string" />
	</div>
</div>
<div class="form-group">	
	<std:label classStyle="control-label col-md-2" value="Forme" />
	<div class="col-md-4">
		<std:select name="article.opc_forme_enum.id" width="150px;" style="float:left;" type="long" key="id" labels="libelle" data="${listeForme}" required="true" />
		<i class="fa fa-reorder" style="color:blue;" title="Liste énumérée"></i>
	</div>
</div>		