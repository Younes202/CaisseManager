<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<script type="text/javascript">
$(document).ready(function (){
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
	$("#generer_code").click(function(){
		executePartialAjax($(this), '<%=EncryptionUtil.encrypt("stock.fournisseur.genererCode")%>', 'fournisseur.code', true, true, null, true);
	});
});
</script>

<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Gestion de stock</li>
		<li>Fiche fournisseur</li>
		<li class="active">Edition</li>
	</ul>
</div>
<!-- /Page Breadcrumb -->
<!-- Page Header -->
<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<std:link actionGroup="U" classStyle="btn btn-default" action="stock.fournisseur.work_init_update" workId="${fournisseur.id }" icon="fa-3x fa-pencil" tooltip="Modifier" />
		<std:link actionGroup="U" classStyle="btn btn-default" action="pers.fournisseur.init_facture_edit" workId="${client.id }" icon="fa-3x fa-pencil" tooltip="Editer des factures" />
<%-- 		<std:link classStyle="btn btn-default" action="stock.fournisseur.work_find" params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la recherche" /> --%>

		<% boolean isSubAdd = "stock.fournisseur.work_init_create".equals(ControllerUtil.getMenuAttribute("IS_SUB_ADD", request));
		String act = (!isSubAdd ? ""+ControllerUtil.getMenuAttribute("IS_SUB_ADD", request) : "stock.fournisseur.work_find");
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
	<std:form name="data-form">
		<!-- widget grid -->
		<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
				<span class="widget-caption">Informations</span>
			</div>
		
<%if(!ControllerUtil.isEditionCreateAction(request)){ %>		
	<div class="row">
        <%request.setAttribute("curMnu", "edit");  %>
		<jsp:include page="/domaine/stock/fournisseur_header_tab.jsp" />
      </div>
    <%} %>  
			<div class="widget-body">
				<div class="row">

					<input type="hidden" id="maxCtrl" name="maxCtrl">
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" valueKey="fournisseur.code" />
						<div class="col-md-4">
							<std:text name="fournisseur.code" type="string" iskey="true" placeholderKey="fournisseur.code" required="true" style="width:50%;float: left;" maxlength="20" />
							<% if(ControllerUtil.isEditionCreateAction(request)){ %>
								<a class="refresh-num" id="generer_code" href="javascript:" title="Générer un code">
						            <i class="fa fa-refresh"></i>
						        </a>
					        <% }%>
						</div>
						
						<std:label classStyle="control-label col-md-2" valueKey="fournisseur.libelle" />
						<div class="col-md-4">
							<std:text name="fournisseur.libelle" type="string" placeholderKey="fournisseur.libelle" required="true" maxlength="80" />
						</div>
					</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" valueKey="fournisseur.marque" />
						<div class="col-md-4">
							<std:text name="fournisseur.marque" type="string" placeholderKey="fournisseur.marque" maxlength="80" />
						</div>
						<std:label classStyle="control-label col-md-2" valueKey="fournisseur.opc_famille" />
						<div class="col-md-4">
							<std:select name="fournisseur.opc_famille.id" type="long" key="id" labels="code;'-';libelle" data="${listeFaimlle }" isTree="true" required="true"/>
							<%if(isSubAdd){ %>
							<std:linkPopup action="stock.famille.work_init_create" style="float: right;position: absolute;margin-left: 2%;" icon="fa fa-plus" noJsValidate="true" actionGroup="C" params="w_nos=1&tp=FO" />
							<%} %>
						</div>
					</div>
					
					<div class="form-group">

						<std:label classStyle="control-label col-md-2" valueKey="fournisseur.siret" />
						<div class="col-md-4">
							<std:text name="fournisseur.siret" type="string" placeholderKey="fournisseur.siret" style="width: 60%" maxlength="30" />
						</div>

						<std:label classStyle="control-label col-md-2" valueKey="fournisseur.rcs" />
						<div class="col-md-4">
							<std:text name="fournisseur.rcs" type="string" placeholderKey="fournisseur.rcs" style="width: 60%" maxlength="30" />
						</div>
					</div>
					<div class="form-group">

						<std:label classStyle="control-label col-md-2" valueKey="fournisseur.ape" />
						<div class="col-md-4">
							<std:text name="fournisseur.ape" type="string" placeholderKey="fournisseur.ape" style="width: 60%" maxlength="30" />
						</div>

					</div>
				</div>
			</div>
		</div>

		<div class="widget">
			<div class="widget-header bordered-bottom bordered-blue">
				<span class="widget-caption">Coordonnées</span>
			</div>
			<div class="widget-body">
				<div class="row">

					<div class="form-group">

						<std:label classStyle="control-label col-md-2" valueKey="fournisseur.adresse" />
						<div class="col-md-4">
							<std:text name="fournisseur.adresse" type="string" placeholderKey="fournisseur.adresse" maxlength="120" />
						</div>

						<std:label classStyle="control-label col-md-2" value="Complément" />
						<div class="col-md-4">
							<std:text name="fournisseur.adresse_compl" type="string" placeholder="Complément" style="width: 30%" maxlength="10" />
						</div>
					</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" valueKey="fournisseur.ville" />
						<div class="col-md-4">
							<std:select name="fournisseur.opc_ville.id" type="long" data="${listVille }" key="id" labels="libelle" placeholderKey="fournisseur.ville" />
						</div>
					</div>
					<div class="form-group">

						<std:label classStyle="control-label col-md-2" valueKey="fournisseur.telephone" />
						<div class="col-md-4">
							<std:text name="fournisseur.telephone" type="string" placeholderKey="fournisseur.telephone" style="width: 60%" maxlength="20" />
						</div>

						<std:label classStyle="control-label col-md-2" valueKey="fournisseur.portable" />
						<div class="col-md-4">
							<std:text name="fournisseur.portable" type="string" placeholderKey="fournisseur.portable" style="width: 60%" maxlength="20" />
						</div>
					</div>
					<div class="form-group">

						<std:label classStyle="control-label col-md-2" valueKey="fournisseur.mail" />
						<div class="col-md-4">
							<std:text name="fournisseur.mail" type="string" placeholderKey="fournisseur.mail" maxlength="50" />
						</div>

						<std:label classStyle="control-label col-md-2" valueKey="fournisseur.site" />
						<div class="col-md-4">
							<std:text name="fournisseur.site" type="string" placeholderKey="fournisseur.site" maxlength="50" />
						</div>
					</div>
					
				</div>
				
				<!-- Generic Form -->
				<jsp:include page="/domaine/administration/dataValue_form.jsp" />
				
			</div>
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
									style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;" name="contact_0" type="string" placeholderKey="fournisseur.contact"
									maxlength="80" /></td>
							<td style="padding-top: 5px; padding-right: 30px;" valign="top"><std:text name="fonction_0" type="string" placeholderKey="fournisseur.fonction" maxlength="80" /></td>
							<td style="padding-top: 5px; padding-right: 30px;" valign="top"><std:text name="coord_0" type="string" placeholderKey="fournisseur.coord" maxlength="80" /></td>
							<td valign="middle" style="padding-top: 5px; padding-right: 1px;"><std:link id="delete_cont" icon="fa fa-times" style="color: red;"></std:link></td>
						</tr>

						<c:set var="cpt" value="${1 }" />
						<c:set var="encodeService" value="<%=new EncryptionUtil()%>" />

						<c:forEach items="${fournisseur.list_contact }" var="contactFourn">
							<std:hidden name="eaiid_${cpt}" value="${encodeService.encrypt(contactFourn.id) }" />
							<tr>
								<td style="padding-top: 5px; padding-right: 30px;" valign="top"><std:text
										style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;" name="contact_${cpt}" type="string"
										placeholderKey="fournisseur.contact" value="${contactFourn.contact }" maxlength="80" /></td>
								<td style="padding-top: 5px; padding-right: 30px;" valign="top"><std:text name="fonction_${cpt}" type="string" placeholderKey="fournisseur.fonction"
										value="${contactFourn.fonction }" maxlength="80" /></td>
								<td style="padding-top: 5px; padding-right: 30px;" valign="top"><std:text name="coord_${cpt}" type="string" placeholderKey="fournisseur.coord"
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
		<div class="row" style="text-align: center;">
			<div class="col-md-12">
				<std:link actionGroup="M" id="add_ctrl" tooltipKey="fournisseur.contact.ajouter" icon="fa-1x fa-plus" classStyle="btn btn-default"></std:link>
				<std:button actionGroup="M" classStyle="btn btn-success" action="stock.fournisseur.work_merge" workId="${fournisseur.id }" icon="fa-save" value="Sauvegarder" />
				<std:button actionGroup="D" classStyle="btn btn-danger" action="stock.fournisseur.work_delete" workId="${fournisseur.id }" icon="fa-trash-o" value="Supprimer" />
			</div>
		</div>
	</std:form>
</div>

