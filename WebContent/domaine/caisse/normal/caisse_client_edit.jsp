<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
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
		$("#select_client").click(function(){
			$("label[class='error']").remove();
			
			if($.trim($("#client\\.nom").val()) == ''){
				var label = $('<label id="err_client.nom" class="error">Le champ est obligatoire</label>');
				label.insertAfter($("#client\\.nom"));
				return;
			}
			if($("#div_client").find(".error").length == 0){
				$("#valider_client").trigger("click");
				$("#div_client").hide('slide');
			}
		});
		
		$("#reset_client, #close_client").click(function(){
			$("label[class='error']").remove();
			$("#div_client").hide('slide');
		});
		$("#client\\.opc_ville\\.id").select2();
	});
	</script>
<%	
boolean isPortefeuille =  StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("PORTEFEUILLE"));
boolean isPoints =  StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("POINTS"));
boolean isServeur = ContextAppli.getUserBean().isInProfile("SERVEUR");
%>

<std:form name="client-form">
	<div class="row" style="margin-top: 10px;">
		<a id="close_client" class="btn btn-default btn-sm shiny icon-only danger" href="javascript:void(0);" style="position: absolute;right: 12px;top: 4px;"><i class="fa fa-times "></i></a>
	</div>
	
	<!-- Generic Form -->
	<%
		request.setAttribute("colLab", 0); 
		request.setAttribute("colInput", 10);
		request.setAttribute("forceW", true);
	%>
	<div class="row" style="margin-top: 10px;">
		<jsp:include page="/domaine/administration/dataValue_form.jsp" />
	</div>
	<div class="row" style="margin-top: 10px;">
		<div class="col-md-10">
			<std:text name="client.cin" type="string" placeholder="CIN" forceWriten="true" style="float: left;width: 130px;"/>
		</div>
	</div>
	<div class="row">	
		<div class="col-md-12">
			<std:text name="client.telephone" type="string" placeholder="T&eacute;l&eacute;phone" style="width:130px;float: left;" maxlength="20" forceWriten="true" />
		</div>
	</div>
	<div class="row">	
		<div class="col-md-10">
			<std:text name="client.nom" type="string" placeholder="Nom *" forceWriten="true" style="width:80%;float: left;border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;"/>
		</div>
	</div>
	<div class="row">	
		<div class="col-md-10">
			<std:text name="client.prenom" type="string" placeholder="Pr&eacute;nom" forceWriten="true"/>
		</div>
	</div>	
	<div class="row">	
		<div class="col-md-12">
			<std:text name="client.mail" type="string" validator="email" placeholder="Mail" maxlength="50" forceWriten="true" />
		</div>
	</div>	
	<div class="row">	
		<div class="form-title">Adresse</div>
		<div class="col-md-12">
			<std:text name="client.adresse_rue" type="string" placeholder="Rue" maxlength="120" forceWriten="true" />
		</div>
	</div>
	<div class="row">	
		<div class="col-md-12">
			<std:text name="client.adresse_compl" type="string" placeholder="Compl&eacute;ment" maxlength="120" forceWriten="true" />
		</div>
	</div>
	<div class="row">	
		<div class="col-md-12">
			<std:select name="client.opc_ville.id" type="long" data="${listVille }" key="id" labels="libelle" classStyle="form-control" placeholder="Ville" groupKey="opc_region.id" groupLabels="opc_region.libelle" style="width:80%;" forceWriten="true" />
		</div>
	</div>
	
	<%
	if(isPortefeuille && isPoints && !isServeur){
	%>
	<div class="row">	
		<div class="form-title">Affecter carte</div>
		<div class="col-md-12">
			<std:select forceWriten="true" name="carte_id" type="long" data="${liste_carte }" key="id" labels="libelle" style="width:100%;" value="${carte_id }"/>
		</div>
	</div>
	<div class="row">	
		<div class="col-md-6">
			Activer portefeuille <std:checkbox forceWriten="true" name="client.is_portefeuille" checked="${is_portefeuille }" />
		</div>
		<div class="col-md-6">
			Autoriser solde négatif <std:checkbox forceWriten="true" name="client.is_solde_neg" value="${is_solde_neg }" />
		</div>
	</div>	
	<%
	}
	%>
	
	<div class="row" style="text-align: center;margin-top: 7px;margin-bottom: 10px;">
		<button id="select_client" type="button" class="btn btn-success" style="margin-top: 2px;margin-right: 5px;">
			<i class="fa fa-save"></i> ${empty client.id ? 'Ajouter':'Mette &agrave; jour' }
		</button>
		<std:button id="valider_client" style="display:none;" actionGroup="X" action="caisse-web.caisseWeb.addClientFromCaisse" targetDiv="left-div" params="isCais=1" workId="${client.id }" value="" forceWriten="true" />
	</div>
</std:form>	