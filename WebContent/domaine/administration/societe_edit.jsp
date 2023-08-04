<%@page import="appli.model.domaine.administration.service.IUserService"%>
<%@page import="framework.model.beanContext.SocietePersistant"%>
<%@page import="java.util.List"%>
<%@page import="framework.model.common.util.ServiceUtil"%>
<%@page import="framework.model.util.FileUtil"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<style>
#copie-cal {
	vertical-align: sub;
	margin-left: 2px;
}
</style>


<script type="text/javascript">
	$(document).ready(function() {
		$("#cal-table input[type='text']").mask('99:99');
		<%if(ControllerUtil.isEditionWritePage(request)){%>
			loadInputFileEvents();
		<% } else{%>
			$("#sep_photo1").remove();
		<%}%>
	
	<%
	SocietePersistant societeBean = (SocietePersistant)request.getAttribute("societe");
	if(societeBean != null && societeBean.getId() != null){ 
		IUserService service = (IUserService)ServiceUtil.getBusinessBean(IUserService.class);
		Map<String, byte[]> dataimg = service.getDataImage(societeBean.getId(), "societe");
		if(dataimg.size() > 0){
			String fileName = dataimg.entrySet().iterator().next().getKey();
			byte[] value = dataimg.entrySet().iterator().next().getValue();
		%>
			$("#photo1_div").css("background", "");
	 		$("#photo1_div").html("<img src='data:image/jpeg;base64,<%=FileUtil.getByte64(value) %>' width='120' height='120'/>");
	 		$("#photo1_name_span").text("<%=fileName%>");
	 		$("#photo1_name").val("<%=fileName%>");
<%}
			}%>
});
</script>
<%
	Map<String, String> mapJours = new HashMap();
	mapJours.put("1", "Lundi");
	mapJours.put("2", "Mardi");
	mapJours.put("3", "Mercredi");
	mapJours.put("4", "Jeudi");
	mapJours.put("5", "Vendredi");
	mapJours.put("6", "Samedi");
	mapJours.put("7", "Dimanche");
%>
<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Paramétrage</li>
		<li class="active">Fiche Societe</li>
	</ul>
</div>
<!-- /Page Breadcrumb -->
<!-- Page Header -->
<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<std:link actionGroup="U" classStyle="btn btn-default" action="admin.societe.work_init_update" workId="${societe.id }" icon="fa-3x fa-pencil" tooltip="Modifier" />
<%-- 		<std:link classStyle="btn btn-default" action="admin.societe.work_find" params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la recherche" /> --%>
	</div>
	<!--Header Buttons-->
	<jsp:include page="/WEB-INF/fragment/shortcut.jsp"></jsp:include>
	<!--Header Buttons End-->
</div>
<!-- /Page Header -->

<!-- Page Body -->
<div class="page-body">
	<div class="row">
		<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
	</div>
	<std:form name="data-form">
		<!-- widget grid -->
		<div class="widget">
			<%
				if (!ControllerUtil.isEditionCreateAction(request)) {
			%>

			<div class="row">
				<div class="col-lg-12 col-sm-12 col-xs-12">
					<div class="tabbable">
						<ul class="nav nav-tabs" id="myTab">
							<li class="active"><a data-toggle="tab" href="#data" wact="<%=EncryptionUtil.encrypt("admin.societe.work_edit")%>" params="tp=sct"> Fiche société </a></li>
							<li><a data-toggle="tab" href="#data" wact="<%=EncryptionUtil.encrypt("admin.etablissement.work_edit")%>"> Fiche établissement </a></li>
						</ul>
						<%
							}
						%>
						<div class="widget-body">
							<div class="row">
								<input type="hidden" name="images_uploaded" id="images_uploaded" value=";"> <input type="hidden" name="images_names" id="images_names" value=";"> <input type="hidden" name="MAX_FILE_SIZE" value="2097152" />
								<std:hidden name="societe.code_authentification" />

								<div class="col-lg-12 col-sm-12 col-xs-12">
									<fieldset style="margin-top: -40px;">
										<hr>
										<h4>Informations société</h4>
										<div class="form-group">
											<std:label classStyle="control-label col-md-2" value="Raison sociale" />
											<div class="col-md-4">
												<std:text name="societe.raison_sociale" type="string" placeholder="Raison sociale" required="true" maxlength="120" style="width: 80%;" />
											</div>
										</div>
										<div class="form-group">
											<std:label classStyle="control-label col-md-2" value="Num. ICE" />
											<div class="col-md-4">
												<std:text name="societe.numero_ice" type="string" placeholder="ICE" maxlength="20" style="width:220px;" />
											</div>
											<std:label classStyle="control-label col-md-2" value="Num. TVA" />
											<div class="col-md-4">
												<std:text name="societe.numero_tva" type="string" placeholder="TVA" maxlength="20" style="width:120px;" />
											</div>
										</div>
										<div class="form-group">
											<std:label classStyle="control-label col-md-2" value="Num. RCS" />
											<div class="col-md-4">
												<std:text name="societe.numero_rcs" type="string" placeholder="RCS" maxlength="20" style="width:120px;" />
											</div>
										</div>
										<div class="form-group">
											<std:label classStyle="control-label col-md-2" value="Identifiant Fiscal" />
											<div class="col-md-4">
												<std:text name="societe.identifiant_fiscal" type="string" placeholder="Identifiant Fiscal" maxlength="20" style="width:120px;" />
											</div>
										</div>
										<div class="form-group">
											<std:label classStyle="control-label col-md-2" value="Téléphone" />
											<div class="col-md-4">
												<std:text name="societe.telephone1" type="string" style="width:50%;" maxlength="20" />
											</div>
											<std:label classStyle="control-label col-md-2" value="Email" />
											<div class="col-md-4">
												<std:text name="societe.mail" type="string" validator="email" style="width:50%;" maxlength="50" />
											</div>
										</div>
										<div class="form-title">Adresse</div>
										<div class="form-group">
											<std:label classStyle="control-label col-md-2" value="Rue" />
											<div class="col-md-4">
												<std:text name="societe.adresse_rue" type="string" placeholderKey="adresse.rue" style="width:90%;" maxlength="120" />
											</div>
											<std:label classStyle="control-label col-md-2" value="Adresse complet" />
											<div class="col-md-4">
												<std:textarea name="societe.adresse_compl" placeholder="Complément" rows="5" cols="40" />
											</div>

											<std:label classStyle="control-label col-md-2" value="Logo" />
											<div class="col-md-4">
												<div class="col-sm-12">
													<div id="photo1_div" style="width: 150px; margin-top: 10px; border: 1px solid #c6d983; border-radius: 25px; text-align: center; height: 120px; background: url('resources/framework/img/picture_file.png') no-repeat center; cursor: pointer;">
														<span style="font-size: 11px;">Image</span>
													</div>
													<span id="photo1_name_span"></span> <input type="hidden" name="photo1_name" id="photo1_name">
												</div>
												<div class="col-sm-12">
													<!-- Separator -->
													<div id="sep_photo1" style="width: 150px; margin-bottom: 5px; height: 20px; text-align: center;">
														<a href="javascript:"><b>X</b></a>
													</div>
													<!-- End -->
													<input type="file" name="photo1" id="photo1_div_file" style="display: none;" accept="image/bmp,image/gif,image/jpg,image/png,image/jpeg">
												</div>
												<i class="fa-fw fa fa-warning" style="color: orange;"></i><span style="color: orange; float: left; width: 100%;">L'image doit &ecirc;tre redimentionnée avant importation.</span>
											</div>
										</div>
									</fieldset>
								</div>
							</div>
							<hr>
							<div class="row" style="text-align: center;">
								<div class="col-md-12">
									<std:button actionGroup="M" classStyle="btn btn-success" action="admin.societe.work_merge" workId="${societe.id }" icon="fa-save" value="Sauvegarder" />
									<std:button actionGroup="D" classStyle="btn btn-danger" action="admin.societe.work_delete" workId="${societe.id }" icon="fa-trash-o" value="Supprimer" />
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</std:form>
</div>

<script type="text/javascript">
	/*Handles ToolTips*/
	$("[data-toggle=tooltip]").tooltip({
		html : true
	});
</script>
