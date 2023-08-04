<%@page import="appli.model.domaine.administration.service.IUserService"%>
<%@page import="framework.model.beanContext.EtablissementOuverturePersistant"%>
<%@page import="framework.model.beanContext.EtablissementPersistant"%>
<%@page import="framework.model.util.FileUtil"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="java.util.List"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.model.common.util.ServiceUtil"%>
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
	EtablissementPersistant restauBean = (EtablissementPersistant)request.getAttribute("etablissement");
	if(restauBean != null && restauBean.getId() != null){ 
		IUserService service = (IUserService)ServiceUtil.getBusinessBean(IUserService.class); 
		Map<String, byte[]> dataimg = service.getDataImage(restauBean.getId(), "restau");
		if(dataimg.size() > 0){
			String fileName = dataimg.entrySet().iterator().next().getKey();
			byte[] value = dataimg.entrySet().iterator().next().getValue();
		%>
			$("#photo1_div").css("background", "");
	 		$("#photo1_div").html("<img src='data:image/jpeg;base64,<%=FileUtil.getByte64(value) %>' width='120' height='120'/>");
	 		$("#photo1_name_span").text("<%=fileName%>");
	 		$("#photo1_name").val("<%=fileName%>");
		<%}
	   } %>
	});
</script>
<!-- Page Breadcrumb -->
 <div class="page-breadcrumbs breadcrumbs-fixed">
     <ul class="breadcrumb">
         <li>
             <i class="fa fa-home"></i>
             <a href="#">Accueil</a>
         </li>
         <li>Paramétrage</li>
         <li class="active">Fiche établissement</li>
     </ul>
 </div>
<!-- /Page Breadcrumb -->
  <!-- Page Header -->
  <div class="page-header position-relative">
      <div class="header-title" style="padding-top: 4px;">
          <std:link actionGroup="C" classStyle="btn btn-default" action="admin.etablissement.work_init_update" workId="${etablissement.id }" params="rst=1" icon="fa-3x fa-pencil" tooltip="Modifier" />
<%--           <std:link classStyle="btn btn-default" action="admin.etablissement.work_find" params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la recherche" /> --%>
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
	
	<div class="row">
		<div class="col-lg-12 col-sm-12 col-xs-12">
			<div class="tabbable">
				<ul class="nav nav-tabs" id="myTab">
					<li><a data-toggle="tab" href="#data" wact="<%=EncryptionUtil.encrypt("admin.societe.work_edit")%>" params="tp=sct"> Fiche société </a></li>
					<li class="active"><a data-toggle="tab" href="#data" wact="<%=EncryptionUtil.encrypt("admin.etablissement.work_edit")%>" workId="${societe.id }"> Fiche établissement </a></li>
				</ul>
						<div class="widget-body">
							<div class="row">
		<std:form name="data-form">
			<input type="hidden" name="images_uploaded" id="images_uploaded" value=";">
			<input type="hidden" name="images_names" id="images_names" value=";">
			<input type="hidden" name="MAX_FILE_SIZE" value="2097152" />
			
			<div class="col-md-12">	
				<fieldset style="margin-top: -40px;">
					<hr>
					<h4>Informations société</h4>
					<hr>
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" value="Nom" />
					<div class="col-md-4">
						<std:text name="etablissement.nom" type="string" placeholder="Nom" required="true" maxlength="120" style="width: 80%;" value="${etablissement.nom }" />
					</div>
					<std:label classStyle="control-label col-md-2" value="Raison sociale" />
					<div class="col-md-4">
						<std:text name="etablissement.raison_sociale" type="string" placeholder="Raison sociale" required="true" maxlength="120" style="width: 80%;" value="${etablissement.raison_sociale }" />
					</div>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" value="Adresse" />
					<div class="col-md-4">
						<std:textarea name="etablissement.adresse" placeholder="Adresse établissement" rows="5" cols="40" value="${etablissement.adresse }" />
					</div>
					<std:label classStyle="control-label col-md-2" value="Logo" />
					<div class="col-md-4">
						<div class="col-sm-12">	
							<div id="photo1_div" style="width: 150px;margin-top:10px; border: 1px solid #c6d983; border-radius: 25px; text-align: center; height: 120px; background: url('resources/framework/img/picture_file.png') no-repeat center;cursor: pointer;">
								<span style="font-size: 11px;">Image</span>
							</div>
							<span id="photo1_name_span"></span>
							<input type="hidden" name="photo1_name" id="photo1_name">
						</div>	
						<div class="col-sm-12">
							<!-- Separator -->
							<div id="sep_photo1" style="width:150px;margin-bottom: 5px; height: 20px; text-align: center;">
								<a href="javascript:"><b>X</b></a>
							</div>
							<!-- End -->
							<input type="file" name="photo1" id="photo1_div_file" style="display: none;" accept="image/bmp,image/gif,image/jpg,image/png,image/jpeg">
						</div>
						<i class="fa-fw fa fa-warning" style="color: orange;"></i><span style="color: orange;float: left;width: 100%;">L'image doit &ecirc;tre redimentionnée avant importation.</span>
					</div>
				</div>	
				
			  </fieldset>
			</div>	
			
				<hr style="width: 100%;">
				<div class="row" style="text-align: center;">
					<div class="col-md-12">
						<std:button actionGroup="M" classStyle="btn btn-success" action="admin.etablissement.work_merge" workId="${etablissement.id }" params="tp=rst&rst=1" icon="fa-save" value="Sauvegarder" />
					</div>
				</div>
			</std:form>
		</div>
	</div>
</div>
</div>
</div>
</div>

<script src="resources/framework/js/colorpicker/jquery.minicolors.js"></script>

<script type="text/javascript"> 
/*Handles ToolTips*/
$("[data-toggle=tooltip]")
    .tooltip({
        html: true
    });
</script> 