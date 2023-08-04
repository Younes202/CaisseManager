<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>
	
	<link href="resources/framework/css/flexselect.css" rel="stylesheet" />
	<script src="resources/framework/js/selectedit/liquidmetal.js"></script>
	<script src="resources/framework/js/selectedit/jquery.flexselect.js"></script>
	
	<script type="text/javascript">
	$(document).ready(function() {
		$("#select_portefeuille").click(function(){
			$("label[class='error']").remove();
			
			if($.trim($("#portefeuille\\.mtt_recharge").val()) == ''){
				var label = $('<label id="err_portefeuille.mtt_recharge" class="error">Le champ est obligatoire</label>');
				label.insertAfter($("#portefeuille\\.mtt_recharge"));
				return;
			}
			if($.trim($("#portefeuille\\.mode_paie").val()) == ''){
				var label = $('<label id="err_portefeuille.mode_paie" class="error">Le champ est obligatoire</label>');
				label.insertAfter($("#portefeuille\\.mode_paie"));
				return;
			}
			
			if($("#div_portefeuille").find(".error").length == 0){
				$("#div_portefeuille").hide('slide');
			}
		});
		$("#reset_portefeuille, #close_portefeuille").click(function(){
			$("#div_portefeuille").hide('slide');
		});
	});
	</script>

<std:form name="portefeuille_form">	
	<div class="row" style="margin-top: 10px;">
		<a id="close_portefeuille" class="btn btn-default btn-sm shiny icon-only danger" href="javascript:void(0);" style="position: absolute;right: 12px;top: 4px;"><i class="fa fa-times "></i></a>
	</div>
	
	<div class="row" style="margin-top: 25px;">
		<std:label classStyle="control-label col-md-4" value="Montant recharge" />
		<div class="col-md-6">
			<std:text name="portefeuille.mtt_recharge" forceWriten="true" style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;text-align:right;" type="string" placeholder="Montant" maxlength="14"/>
		</div>
	</div>
	<div class="row" style="margin-top: 25px;">
		<std:label classStyle="control-label col-md-4" value="Mode paiement" />
		<div class="col-md-6">
			<std:select name="portefeuille.mode_paie" style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;" data="${list_mode_paiement }" forceWriten="true" type="string" width="100%"/>
		</div>
	</div>
	<div class="row" style="text-align: center;margin-top: 7px;margin-bottom: 10px;">
		<std:button id="select_portefeuille" actionGroup="X" action="caisse-web.caisseWeb.chargerPortefeuille" targetDiv="generic_modal_body" classStyle="btn btn-success" icon="fa-save" value="Recharger" forceWriten="true" />
	</div>
</std:form>	