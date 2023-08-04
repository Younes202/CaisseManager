<%@page import="framework.model.util.audit.ReplicationGenerationEventListener"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<style>
.form-title {
	margin-left: 12px;
	font-size: 20px;
}
</style>

<script type="text/javascript">
	$(document).ready(function() {
		$("#generer_bl").click(function(){
			executePartialAjax($(this), '<%=EncryptionUtil.encrypt("pers.client.genererNum")%>', 'client.numero', true, true, null, true);
		});
		$("#generer_codeBarre").click(function(){
			executePartialAjax($(this), '<%=EncryptionUtil.encrypt("stock.composant.generer_codeBarre")%>', 'client.code_barre', true, true, null, true);
		});
		//
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
		$("#client\\.type_client").change(function(){
			manageTypeClient();
		});
		
		setTimeout(function(){
	    	manageTypeClient();
	    }, 1000);
	});
	
	function manageTypeClient(){
		if($("#client\\.type_client").val() == 'PP'){
			$(".pmPers").hide();
			$(".ppPers").show();
		} else{
			$(".pmPers").show();
			$(".ppPers").hide();
		}
	}
</script>
	
<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Gestion des personnels</li>
		<li>Clients</li>
		<li class="active">Edition</li>
	</ul>
</div>

<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<c:if test="${client.origine_auth == null}" >
			<std:link actionGroup="U" classStyle="btn btn-default" action="pers.client.work_init_update" workId="${client.id }" icon="fa-3x fa-pencil" tooltip="Modifier" />
		</c:if>	
		<std:linkPopup actionGroup="U" classStyle="btn btn-info" action="pers.client.init_facture_edit" workId="${client.id }" icon="fa-3x fa-file-pdf-o" value="Facture" tooltip="Editer des factures" />
		<%
		//String act = (ControllerUtil.getMenuAttribute("IS_SUB_ADD", request) != null) 
		  //             ? ""+ControllerUtil.getMenuAttribute("IS_SUB_ADD", request) : "pers.client.work_find";
		%>
		
		<% boolean isSubAdd = "pers.client.work_init_create".equals(ControllerUtil.getMenuAttribute("IS_SUB_ADD", request));
		String act = (!isSubAdd ? ""+ControllerUtil.getMenuAttribute("IS_SUB_ADD", request) : "pers.client.work_find");
		
		//boolean isSubAdd = "pers.client.work_init_create".equals(ControllerUtil.getMenuAttribute("IS_SUB_ADD", request));
		%>
		
		
		<std:link classStyle="btn btn-default" action='<%=act %>' params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la recherche" />
		
		
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
				<div class="col-lg-12 col-sm-12 col-xs-12">
					<% request.setAttribute("curMnu", "fiche"); %>
					<jsp:include page="/domaine/personnel/client_header_tab.jsp" />
				</div>
			</div>

			<div class="widget-body">
				<div class="row">
					<%if(ReplicationGenerationEventListener._IS_CLOUD_SYNCHRO_INSTANCE){ %>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" value="Etablissement" />			
						<div class="col-md-6" >
							<std:select name="client.ets_ids" multiple="true" width="100" labels ="nom" key="id" type="long[]" data="${listEtablissement}"/>
						</div>
					</div>
					<%} %>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" value="Type client" />
						<div class="col-md-6">
							<std:select name="client.type_client" addBlank="false" width="100" type="string" data="${typeClientArray}" required="true" />
						</div>	
					</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" valueKey="client.numero" />
						<div class="col-md-4">
							<std:text name="client.numero" type="string" style="width:120px;float: left;" maxlength="20" required="true" iskey="true" />
							<% if(ControllerUtil.isEditionCreateAction(request)){ %>
								<a class="refresh-num" id="generer_bl" href="javascript:" title="Générer un numéro">
						            <i class="fa fa-refresh" style="padding-top: 10px;"></i>
						        </a>
					        <% }%>
						</div>
						<div class="ppPers">
							<std:label classStyle="control-label col-md-2" valueKey="client.civilite" />
							<div class="col-md-4">
								<std:select name="client.civilite" type="string" data="${civiliteArray}" />
							</div>
						</div>
					</div>
					<div class="form-group pmPers">
						<std:label classStyle="control-label col-md-2" valueKey="fournisseur.marque" />
						<div class="col-md-4">
							<std:text name="client.marque" type="string" placeholderKey="fournisseur.marque" maxlength="80" />
						</div>
					</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" valueKey="client.nom" />
						<div class="col-md-4">
							<std:text name="client.nom" required="true" placeholderKey="client.nom" type="string" maxlength="50" />
						</div>
						<div class="ppPers">
							<std:label classStyle="control-label col-md-2" valueKey="client.prenom" />
							<div class="col-md-4">
								<std:text name="client.prenom" type="string" maxlength="50" />
							</div>
						</div>
					</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" value="Code barre"/>
						<div class="col-md-4">
							<std:text name="client.code_barre" type="string" placeholder="Code barre" style="width: 60%;float: left;" maxlength="50" />
							<% if(ControllerUtil.isEditionWritePage(request)){ %>
							<a class="refresh-num" id="generer_codeBarre" href="javascript:" title="Générer un code">
					            <i class="fa fa-refresh" style="padding-top: 10px;"></i>
					        </a>
					        <%} %>
						</div>
						<div class="ppPers">
							<std:label classStyle="control-label col-md-2" valueKey="employe.cin" />
							<div class="col-md-4">
								<std:text name="client.cin" type="string" style="width:50%;" maxlength="20" />
							</div>
						</div>
					</div>	
					<div class="form-title">Informations de contact</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" valueKey="client.telephone" />
						<div class="col-md-4">
							<std:text name="client.telephone" type="string" style="width:50%;" maxlength="20" />
						</div>
						<std:label classStyle="control-label col-md-2" value="Fix" />
						<div class="col-md-4">
							<std:text name="client.telephone2" type="string" style="width:50%;" maxlength="20" />
						</div>
					</div>	
					<div class="form-group">	
						<std:label classStyle="control-label col-md-2" valueKey="client.mail" />
						<div class="col-md-4">
							<std:text name="client.mail" type="string" validator="email" style="width:50%;" maxlength="50" />
						</div>
						<std:label classStyle="control-label col-md-2" value="Site" />
						<div class="col-md-4">
							<std:text name="client.site" type="string" placeholder="Site" maxlength="50" />
						</div>
					</div>
					
					<div class="form-title">Adresse</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" value="Rue" />
						<div class="col-md-4">
							<std:text name="client.adresse_rue" type="string" placeholder="Rue" style="width:90%;" maxlength="120" />
						</div>
						<std:label classStyle="control-label col-md-2" valueKey="adresse.complement" />
						<div class="col-md-4">
							<std:text name="client.adresse_compl" type="string" placeholderKey="adresse.complement" style="width:90%;" maxlength="120" />
						</div>
					</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" valueKey="adresse.ville" />
						<div class="col-md-4">
							<std:select name="client.opc_ville.id" type="long" data="${listVille }" key="id" labels="libelle" placeholderKey="adresse.ville" style="width:70%;" />
						</div>
					</div>
					
					<!-- Carte de fidilité -->
<%-- 					<c:if test="${client.list_cartes.size() == 0}"> --%>
						<div class="form-title">Carte de fidélité</div>
						<div class="form-group">
							<std:label classStyle="control-label col-md-2" value="Carte" />
							<div class="col-md-4">
								<std:select name="carte_id" type="long" data="${liste_carte }" key="id" labels="libelle" style="width:100%;" value="${carteClient.opc_carte_fidelite.id }"/>
								<%if(isSubAdd){ %>
								<std:link action="fidelite.carteFidelite.work_init_create" style="float: right;position: absolute;margin-left: 2%;" icon="fa fa-plus" noJsValidate="true" actionGroup="C" params="w_nos=1" />
								<%} %>
							</div>
						</div>
						<div class="form-group">	
							<std:label classStyle="control-label col-md-2" value="Date début validité" />
							<div class="col-md-4">
								<std:date name="carte_date_debut" value="${carteClient.date_debut }" />
							</div>
								<std:label classStyle="control-label col-md-2" value="Date fin validité" />
							<div class="col-md-4">
								<std:date name="carte_date_fin" value="${carteClient.date_fin }" />
							</div>
						</div>
						<div class="form-group">
							<std:label classStyle="control-label col-md-2" value="Code/code barre" />
							<div class="col-md-2">
								<std:text name="carte_code_bar" value="${carteClient.code_barre }" type="string" maxlength="80"  />
							</div>
						</div>
<%-- 					</c:if>	 --%>
					<div class="form-title">Portefeuille vituel</div>	
					<div class="form-group">	
						<std:label classStyle="control-label col-md-2" value="Activer portefeuille" />
						<div class="col-md-2">
							<std:checkbox name="client.is_portefeuille" style="vertical-align: bottom;" />
						</div>
						<std:label classStyle="control-label col-md-2" value="Autoriser solde négatif" />
						<div class="col-md-2">
							<std:checkbox name="client.is_solde_neg" style="vertical-align: bottom;" />
						</div>
						<std:label classStyle="control-label col-md-2" value="Taux offert" />
						<div class="col-md-2">
							<std:text name="client.taux_portefeuille" type="decimal"  style="width:120px;float: left;"/>
							<i class="fa fa-info-circle tooltip-lg" data-toggle="tooltip" data-placement="top" data-original-title="Taux à ajouter à chaque recharge du portefeuille. Exemple : Si 10% alors 450Dhs de recharge donnera 495Dhs de solde."></i>
							<span style="font-size: 10px;">%</span>
						</div>
					</div>	
					
					<div class="form-title">Autres options</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" value="Plafond endettement" />
						<div class="col-md-4">
							<std:text name="client.plafond_dette" type="decimal" maxlength="15" />
						</div>
						<std:label classStyle="control-label col-md-2" value="Réduction article par article" />
						<div class="col-md-4">
							<std:checkbox name="client.is_reduc_art" />
						</div>
					</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" value="Commentaire" />
						<div class="col-md-4">
							<std:textarea name="client.commentaire" maxlength="255" cols="60" rows="4" />
						</div>
					</div>	
				</div>
				<div class="widget pmPers">
					<div class="widget-header bordered-bottom bordered-blue">
						<span class="widget-caption">Information professionnelles</span>
					</div>
					<div class="widget-body">
						<div class="row">
							<div class="form-group">
								<std:label classStyle="control-label col-md-2" value="SIRET" />
								<div class="col-md-4">
									<std:text name="client.siret" type="string" placeholder="SIRET" style="width: 60%" maxlength="30" />
								</div>
		
								<std:label classStyle="control-label col-md-2" value="RCS" />
								<div class="col-md-4">
									<std:text name="client.rcs" type="string" placeholder="RCS" style="width: 60%" maxlength="30" />
								</div>
							</div>
							<div class="form-group">
								<std:label classStyle="control-label col-md-2" value="APE" />
								<div class="col-md-4">
									<std:text name="client.ape" type="string" placeholder="APE" style="width: 60%" maxlength="30" />
								</div>
							</div>
							<div class="form-group">
								<std:label classStyle="control-label col-md-2" value="ICE" />
								<div class="col-md-4">
									<std:text name="client.ice" type="string" placeholder="ICE" style="width: 60%" maxlength="30" />
								</div>
							</div>
						</div>
					</div>
				</div>
				
				<!-- Generic Form -->
				<jsp:include page="/domaine/administration/dataValue_form.jsp" />
				
		<div class="widget">
			<div class="form-title">Contacts supplémentaires</div>
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
							<td style="padding-top: 5px; padding-right: 30px;" valign="top"><std:text name="coord_0" type="string" placeholder="Coordonnées" maxlength="80" /></td>
							<td valign="middle" style="padding-top: 5px; padding-right: 1px;"><std:link id="delete_cont" icon="fa fa-times" style="color: red;"></std:link></td>
						</tr>

						<c:set var="cpt" value="${1 }" />
						<c:set var="encodeService" value="<%=new EncryptionUtil()%>" />

						<c:forEach items="${client.list_contact }" var="contactCli">
							<std:hidden name="eaiid_${cpt}" value="${encodeService.encrypt(contactCli.id) }" />
							<tr>
								<td style="padding-top: 5px; padding-right: 30px;" valign="top"><std:text
										style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;" name="contact_${cpt}" type="string"
										placeholder="Contact" value="${contactCli.contact }" maxlength="80" /></td>
								<td style="padding-top: 5px; padding-right: 30px;" valign="top"><std:text name="fonction_${cpt}" type="string" placeholder="Fonction"
										value="${contactCli.fonction }" maxlength="80" /></td>
								<td style="padding-top: 5px; padding-right: 30px;" valign="top"><std:text name="coord_${cpt}" type="string" placeholder="Coordonnéés"
										value="${contactCli.coord }" maxlength="80" /></td>
								<td valign="top" style="padding-top: 5px; padding-right: 1px;"><std:link actionGroup="M" id="delete_cont" icon="fa fa-times" style="color: red;"></std:link></td>
							</tr>

							<c:set var="cpt" value="${cpt+1 }" />
						</c:forEach>
					</table>
				</div>
			</div>
			</div>
			</div>
			<hr>
			<div class="form-actions">
				<div class="row" style="text-align: center;" class="col-md-12" id="action-div">
					<std:link actionGroup="M" id="add_ctrl" tooltipKey="fournisseur.contact.ajouter" icon="fa-1x fa-plus" classStyle="btn btn-default"></std:link>
					<std:button actionGroup="M" classStyle="btn btn-success" action="pers.client.work_merge" workId="${client.id }" icon="fa-save" value="Sauvegarder" />
					<std:button actionGroup="D" classStyle="btn btn-danger" action="pers.client.work_delete" workId="${client.id }" icon="fa-trash-o" value="Supprimer" />
				</div>

			</div>
		</std:form>
	</div>
</div>
