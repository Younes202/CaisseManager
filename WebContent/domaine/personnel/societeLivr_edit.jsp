<%@page import="framework.model.util.audit.ReplicationGenerationEventListener"%>
<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<style>
   .form-title{ 
    	margin-left: 12px;
	}
</style>

  <script type="text/javascript">
	$(document).ready(function() {
		$(document).off('click', '#add_ctrl');
		$(document).on('click', '#add_ctrl', function() {
			var contentTr = $("#ctrl_gpt").html();
			var cpt = $("input[id^='contact_']").length + 1;
			contentTr = contentTr.replace(new RegExp("_0", 'g'), "_" + cpt);

			$("#ctrl_table").append('<tr cpt="'+cpt+'">' + contentTr + '</tr>');
			$("#maxCtrl").val(cpt);
		});
		$(document).on('click', "#delete_cont", function() {
			$(this).closest("tr").remove();
		});

 		$("#generer_bl").click(function(){
			executePartialAjax($(this), '<%=EncryptionUtil.encrypt("pers.societeLivr.genererNumBL")%>', 'societeLivr.numero', true, true, null, true);
		});
	});
 </script>
 
<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Livraison</li>
		<li>Sociétés de livraison</li>
		<li class="active">Edition</li>
	</ul>
</div>

<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<std:link actionGroup="U" classStyle="btn btn-default" action="pers.societeLivr.work_init_update" workId="${societeLivr.id }" icon="fa-3x fa-pencil" tooltip="Modifier" />
		<std:link classStyle="btn btn-default" action="pers.societeLivr.work_find" params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la recherche" />
	</div>
	<!--Header Buttons-->
	<jsp:include page="/WEB-INF/fragment/shortcut.jsp"></jsp:include>
	<!--Header Buttons End-->
</div>
<!-- /Page Header -->

<div class="page-body">
	<div class="row">
		<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
	</div>
	<!-- widget grid -->
	<div class="widget">
		<std:form name="data-form">
			<div class="row">
				<div class="col-lg-12 col-sm-6 col-xs-12">
					<div class="tabbable">
						<ul class="nav nav-tabs" id="myTab">
							<li class="active"><a data-toggle="tab" noVal="true" href="#descripton" wact="<%=EncryptionUtil.encrypt("pers.societeLivr.work_edit")%>"> Fiche </a></li>
							<%if (ControllerUtil.getMenuAttribute("societeLivrId", request) != null){ %>
								<li><a data-toggle="tab" href="#mouvement" noVal="true" wact="<%=EncryptionUtil.encrypt("pers.societeLivr.find_mvm_societeLivr")%>"> Commandes </a></li>
							<%} %>	
						</ul>
					</div>
				</div>
			</div>

			<div class="widget-body">
				<div class="row">
					<%if(ReplicationGenerationEventListener._IS_CLOUD_SYNCHRO_INSTANCE){ %>
						<div class="form-group">
							<std:label classStyle="control-label col-md-3" value="Etablissement" />			
							<div class="col-md-4" >
								<std:select name="societeLivr.ets_ids" multiple="true" labels ="nom" key="id" type="long[]" data="${listEtablissement}"/>
							</div>
						</div>
					<%} %>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" value="Numéro" />
						<div class="col-md-4">
							<std:text name="societeLivr.numero" type="string" iskey="true" style="width:120px;float: left;" maxlength="20" required="true"/>
							<% if(ControllerUtil.isEditionCreateAction(request)){ %>
								<a class="refresh-num" id="generer_bl" href="javascript:" title="Générer un numéro">
						            <i class="fa fa-refresh"></i>
						        </a>
					        <% }%>
						</div>
					</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" value="Nom" />
						<div class="col-md-4">
							<std:text name="societeLivr.nom" required="true" placeholder="Nom" type="string" maxlength="50" />
						</div>
					</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" value="Téléphone" />
						<div class="col-md-4">
							<std:text name="societeLivr.telephone" type="string" style="width:120px;" maxlength="20" />
						</div>
						<std:label classStyle="control-label col-md-2" value="Mail" />
						<div class="col-md-4">
							<std:text name="societeLivr.mail" type="string" validator="email" maxlength="50" />
						</div>
					</div>
					
					<div class="form-title">Informations du partenariat</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" value="Taux de marge(%)" />
						<div class="col-md-4">
							<std:text name="societeLivr.taux_marge" type="decimal" style="width:30%;" maxlength="15"/>
						</div>
					</div>
					<div class="form-title">Adresse</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" value="Adresse" />
						<div class="col-md-4">
							<std:text name="societeLivr.adresse_rue" type="string" placeholder="Adresse" style="width:90%;" maxlength="50" />
						</div>
						<std:label classStyle="control-label col-md-2" value="Complément" />
					<div class="col-md-4">
						<std:text name="societeLivr.adresse_compl" type="string" placeholder="Complément" style="width:90%;" maxlength="120" />
					</div>
					</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" value="Ville" />
						<div class="col-md-4">
							<std:select name="societeLivr.opc_ville.id" type="long" data="${listVille }" key="id" labels="libelle" placeholder="Ville" style="width:70%;" />
						</div>
					</div>
					
					<div class="form-title">Autres options</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" value="Plafond endettement" />
						<div class="col-md-4">
							<std:text name="societeLivr.plafond_dette" type="decimal" maxlength="15" />
						</div>
						<std:label classStyle="control-label col-md-2" value="Ventiler la réduction" />
						<div class="col-md-4">
							<std:checkbox name="societeLivr.is_ventille" />
						</div>
					</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" value="Activer portefeuille" />
						<div class="col-md-2">
							<std:checkbox name="societeLivr.is_portefeuille" />
						</div>
						<std:label classStyle="control-label col-md-2" value="Autoriser solde négatif" />
						<div class="col-md-2">
							<std:checkbox name="societeLivr.is_solde_neg" />
						</div>
						<std:label classStyle="control-label col-md-2" value="Taux offert" />
						<div class="col-md-2">
							<std:text name="societeLivr.taux_portefeuille" type="decimal"  style="width:120px;float: left;"/>
							<i class="fa fa-info-circle tooltip-lg" data-toggle="tooltip" data-placement="top" data-original-title="Taux &agrave; ajouter &agrave; chaque recharge du portefeuille. Exemple : Si 10% alors 450Dhs de recharge donnera 495Dhs de solde."></i>
							<span style="font-size: 10px;">%</span>
						</div>
					</div>	
				</div>
				
				<!-- Generic Form -->
				<jsp:include page="/domaine/administration/dataValue_form.jsp" />
				
			</div>
			<div class="widget">
				<div class="widget-header bordered-bottom bordered-blue">
					<span class="widget-caption">Contacts supplémentaires</span>
				</div>
				<div class="widget-body">
					<div class="row">
						<table id="ctrl_table" style="width: 97%; margin-left: 20px;">
							<tr>
								<th>Contact</th>
								<th>Fonction</th>
								<th>Infos</th>
							</tr>
							<tr id="ctrl_gpt" style="display: none;">
								<td style="padding-top: 5px; padding-right: 30px;" valign="top"><std:text
										style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;" name="contact_0" type="string" placeholder="Contact"
										maxlength="80" /></td>
								<td style="padding-top: 5px; padding-right: 30px;" valign="top"><std:text name="fonction_0" type="string" placeholder="Fonction" maxlength="80" /></td>
								<td style="padding-top: 5px; padding-right: 30px;" valign="top"><std:text name="coord_0" type="string" placeholder="Coorddonné" maxlength="80" /></td>
								<td valign="middle" style="padding-top: 5px; padding-right: 1px;"><std:link id="delete_cont" icon="fa fa-times" style="color: red;"></std:link></td>
							</tr>
	
							<c:set var="cpt" value="${1 }" />
							<c:set var="encodeService" value="<%=new EncryptionUtil()%>" />
	
							<c:forEach items="${societeLivr.list_contact }" var="contactFourn">
								<std:hidden name="eaiid_${cpt}" value="${encodeService.encrypt(contactFourn.id) }" />
								<tr>
									<td style="padding-top: 5px; padding-right: 30px;" valign="top"><std:text
											style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;" name="contact_${cpt}" type="string"
											placeholder="Contact" value="${contactFourn.contact }" maxlength="80" /></td>
									<td style="padding-top: 5px; padding-right: 30px;" valign="top"><std:text name="fonction_${cpt}" type="string" placeholder="Fonction"
											value="${contactFourn.fonction }" maxlength="80" /></td>
									<td style="padding-top: 5px; padding-right: 30px;" valign="top"><std:text name="coord_${cpt}" type="string" placeholder="Coordonné"
											value="${contactFourn.coord }" maxlength="80" /></td>
									<td valign="top" style="padding-top: 5px; padding-right: 1px;"><std:link actionGroup="M" id="delete_cont" icon="fa fa-times" style="color: red;"></std:link></td>
								</tr>
	
								<c:set var="cpt" value="${cpt+1 }" />
							</c:forEach>
						</table>
					</div>
				</div>
			</div>
			<hr>
			<div class="form-actions">
				<div class="row" style="text-align: center;" class="col-md-12">
					<std:link actionGroup="M" id="add_ctrl" tooltipKey="fournisseur.contact.ajouter" icon="fa-1x fa-plus" classStyle="btn btn-default"></std:link>
					<std:button actionGroup="M" classStyle="btn btn-success" action="pers.societeLivr.work_merge" workId="${societeLivr.id }" icon="fa-save" value="Sauvegarder" />
					<std:button actionGroup="D" classStyle="btn btn-danger" action="pers.societeLivr.work_delete" workId="${societeLivr.id }" icon="fa-trash-o" value="Supprimer" />
				</div>

			</div>
		</std:form>
	</div>
</div>
