<%@page import="framework.component.ComponentUtil"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="appli.controller.domaine.stock.bean.InventaireBean"%>
<%@page import="java.util.Map"%>
<%@page import="framework.model.common.util.ServiceUtil"%>
<%@page import="appli.model.domaine.stock.service.IArticleService"%>
<%@page import="framework.model.util.FileUtil"%>
<%@page import="appli.model.domaine.stock.persistant.FamillePersistant"%>
<%@page import="java.util.List"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<link href="resources/framework/css/tree/jquery.treeview.css?v=1.0" rel="stylesheet" type="text/css" />

<script src="resources/framework/js/tree/jquery.treeview.js"></script>

<style>
.filetree span.folder {
	background: url(resources/framework/img/folder_close_blue.png) 0 0
		no-repeat !important;
}
</style>

<%
	boolean isCorrection = StringUtil.isTrue("" + ControllerUtil.getMenuAttribute("MODE_CORECT", request));
	InventaireBean invBean = (InventaireBean) request.getAttribute("inventaire");
%>

<script type="text/javascript">
	$(document).ready(function() {
		$(document).off('click', ".filetree a");
		
<%if (ControllerUtil.isEditionWritePage(request) && !ComponentUtil.isReadOnlyFormSetted(request)) {%>	
		$(document).on('click', ".filetree a", function(){
			$(".filetree a").css("background-color", "transparent");
			$(this).css("background-color", "#CDDC39");
			
			if($("#inventaire\\.opc_emplacement\\.id").val() == ''){
				alertify.error("Veuillez s�l�ctionner un emplacement.");
				return;
			}
			$("#saisie_lnk").attr("params", "empl="+$("#inventaire\\.opc_emplacement\\.id").val()+"&fam="+$(this).attr("fam"));
			$("#saisie_lnk").trigger("click");
		});
		
		$("#inventaire\\.opc_emplacement\\.id").change(function(){
			submitAjaxForm('<%=EncryptionUtil.encrypt("stock.inventaire.loadFamilleEmplacement")%>', 'empl='+$(this).val(), $("#data-form"), $('#famille_lnk'));
			manageEmplacement();
		});
<%}%>	
		
	<%if(invBean != null && invBean.getId() != null) {%>
		$("#unite_fltr").change(function(){
			submitAjaxForm('<%=EncryptionUtil.encrypt("stock.inventaire.work_edit")%>', 'workId=<%=EncryptionUtil.encrypt(invBean.getId().toString())%>', $("#data-form"), null);
		});
		<%if(!ControllerUtil.isEditionWritePage(request)){%>
			$("#famille_div").hide();
		<%} %>
		$("#detail_div, #action_div").show();
	<%
	} else{%>
		manageEmplacement();
	<%}%>
	});

	/**
	 *
	 */
	function manageEmplacement() {
		if (($("#inventaire\\.opc_emplacement\\.id").length == 0 || $(
				"#inventaire\\.opc_emplacement\\.id").val() == '')
				&& ($("input[id='inventaire\\.opc_emplacement\\.id']").length == 0 || $(
						"input[id='inventaire\\.opc_emplacement\\.id']").val() == '')) {
			$("#tree_div, #detail_div, #action_div").hide();
		} else {
			$("#tree_div, #detail_div, #action_div").show();
		}
	}
</script>

<img class="imgBarCode" src="resources/framework/img/barcode_scanner.png" style="width: 20px; position: absolute; right: 17px; top: -27px;" title="Lecteur code barre utilisable sur cet écran">

<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Gestion de stock</li>
		<li>Fiche des inventaires</li>
		<li class="active">Edition</li>
	</ul>
</div>
<!-- /Page Breadcrumb -->

<std:link id="famille_lnk" action="stock.inventaire.loadFamilleEmplacement" targetDiv="famille_div" actionGroup="C" style="display:none;" />
<std:linkPopup id="saisie_lnk" action="stock.inventaire.loadSaisieInventaire" actionGroup="C" style="display:none;" />

<!-- Page Header -->
<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">

		<c:if test="${!inventaire.is_valid }">
			<std:link actionGroup="U" classStyle="btn btn-default" action="stock.inventaire.work_init_update" workId="${inventaire.id }" icon="fa-3x fa-pencil" tooltip="Modifier" />
		</c:if>
		<std:link classStyle="btn btn-default" action="stock.inventaire.work_find" params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la recherche" />

		<%
			if (isCorrection) {
		%>
		<span style="font-weight: bold; color: orange;">MODE CORRECTION</span>
		<%
			}
		%>
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

		<div class="row">
			<div class="col-md-6">
				<!-- widget grid -->
				<div class="widget">

					<div class="widget-body">
						<div class="row">
							<div class="form-group">
								<std:label classStyle="control-label col-md-3" valueKey="inventaire.opc_emplacement" />
								<div class="col-md-9">
									<std:select name="inventaire.opc_emplacement.id" key="id" labels="titre" type="long" data="${listEmplacement }" required="true" width="80%" disable="${empty inventaire.id ? 'false':'true' }" />
									<std:linkPopup action="stock.emplacement.work_init_create" style="float: right;position: absolute;margin-left: 2%;" icon="fa fa-plus" noJsValidate="true" actionGroup="C" params="w_nos=1" />
									
								</div>
							</div>
							<div class="form-group">
								<std:label classStyle="control-label col-md-3" value="Date r&eacute;alisation" />
								<div class="col-md-9">
									<std:date name="inventaire.date_realisation" required="true" />
								</div>
							</div>
							<div class="form-group">
								<std:label classStyle="control-label col-md-3" valueKey="inventaire.type" />
								<div class="col-md-9">
									<div style="float: left;">
										<std:select type="long" required="true" style="float:left;" width="95%" name="employe.type_contrat_enum.id" data="${listeType }" labels="libelle" key="id" />
									</div>
									<div style="padding-right: 4px; float: left;">
										<std:linkPopup actionGroup="C" params="w_nos=1&valEnum.opc_typenum.id=${typeEnumId }" classStyle="" noJsValidate="true" action="admin.valTypeEnum.work_init_create">
											<i class="fa fa-reorder" style="color: blue;" title="Liste &eacute;num&eacute;r&eacute;e"></i>
										</std:linkPopup>
									</div>
								</div>
							</div>
							<div class="form-group">
								<std:label classStyle="control-label col-md-3" valueKey="inventaire.opc_responsable" />
								<div class="col-md-9">
									<std:select name="inventaire.opc_responsable.id" data="${listEmploye }" key="id" labels="nom;' ';prenom" type="long" width="80%" />
									<std:link action="pers.employe.work_init_create" style="float: right;position: absolute;margin-left: 2%;" icon="fa fa-plus" noJsValidate="true" actionGroup="C" params="w_nos=1" />
									
								</div>
							</div>
							<div class="form-group">
								<std:label classStyle="control-label col-md-3" valueKey="inventaire.opc_saisisseur" />
								<div class="col-md-9">
									<std:select name="inventaire.opc_saisisseur.id" data="${listEmploye }" key="id" labels="nom;' ';prenom" type="long" width="80%" />
									<std:link action="pers.employe.work_init_create" style="float: right;position: absolute;margin-left: 2%;" icon="fa fa-plus" noJsValidate="true" actionGroup="C" params="w_nos=1" />
								
								</div>
							</div>
							<div class="form-group">
								<std:label classStyle="control-label col-md-3" valueKey="inventaire.commentaire" />
								<div class="col-md-9">
									<std:textarea name="inventaire.commentaire" rows="5" cols="40" />
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>

			<div class="col-md-6" id="famille_div">
				<jsp:include page="inventaire_famille_emplacement.jsp"></jsp:include>
			</div>

		</div>
		<div class="row" id="detail_div" style="display: none;">
			<div class="widget">
				<div class="widget-header bordered-bottom bordered-blue">
					<span class="widget-caption">D&eacute;tail de l'inventaire</span>
					<div style="width: 50%; float: left; margin-top: 2px; background-color: #c9e1e4;">
						Filtrer par unit&eacute; :
						<std:select name="unite_fltr" type="long" data="${listeUnite }" key="id" labels="libelle" width="120px;" forceWriten="true" value="${uniteIdFilter }" />
					</div>
				</div>
				<div class="widget-body">
					<div class="row" id="inventaireDetail_div">
						<jsp:include page="inventaire_recap.jsp"></jsp:include>
					</div>
				</div>
			</div>
		</div>

		<div class="row" id="action_div" style="display: none;">
			<div class="col-md-12" id="action-div" style="text-align: center;">
				<std:button actionGroup="M" classStyle="btn btn-success" action="stock.inventaire.work_merge" workId="${inventaire.id }" icon="fa-save" value="Sauvegarder" />
				<std:button actionGroup="D" classStyle="btn btn-danger" action="stock.inventaire.work_delete" workId="${inventaire.id }" icon="fa-trash-o" value="Supprimer" />
			</div>
		</div>
	</std:form>
</div>
