<%@page import="java.util.Map"%>
<%@page import="framework.model.common.util.ServiceUtil"%>
<%@page import="appli.model.domaine.stock.service.IMarqueService"%>
<%@page import="appli.controller.domaine.stock.bean.MarqueBean"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.util.FileUtil"%>
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
	$("#generer_code").click(function(){
		executePartialAjax($(this), '<%=EncryptionUtil.encrypt("stock.marque.genererCode")%>', 'marque.code', true, true, null, true);
	});
	
	<%if(ControllerUtil.isEditionWritePage(request)){%>
		loadInputFileEvents();
	<%} else{%>
		$("#sep_photo1").remove();
	<%}%>
	
	<%MarqueBean marqueBean = (MarqueBean)request.getAttribute("marque");
	if(marqueBean != null && marqueBean.getId() != null){ 
		IMarqueService service = (IMarqueService)ServiceUtil.getBusinessBean(IMarqueService.class);
		Map<String, byte[]> dataimg = service.getDataImage(marqueBean.getId(), "marque");
		if(dataimg.size() > 0){
			String fileName = dataimg.entrySet().iterator().next().getKey();
			byte[] value = dataimg.entrySet().iterator().next().getValue();%>
			$("#photo1_div").css("background", "");
	 		$("#photo1_div").html("<img src='data:image/jpeg;base64,<%=FileUtil.getByte64(value) %>' width='120' height='120'/>");
	 		$("#photo1_name_span").text("<%=fileName%>");
	 		$("#photo1_name").val("<%=fileName%>");
		<%}
	}%>
});
</script>

<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Gestion de stock</li>
		<li>Fiche marque</li>
		<li class="active">Edition</li>
	</ul>
</div>
<!-- /Page Breadcrumb -->
<!-- Page Header -->
<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<std:link actionGroup="U" classStyle="btn btn-default" action="stock.marque.work_init_update" workId="${marque.id }" icon="fa-3x fa-pencil" tooltip="Modifier" />
		<%
		String act = (ControllerUtil.getMenuAttribute("IS_SUB_ADD", request) != null) 
		               ? ""+ControllerUtil.getMenuAttribute("IS_SUB_ADD", request) : "stock.marque.work_find";
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
		
	<div class="row">
	<div class="widget">
         <div class="widget-header bordered-bottom bordered-blue">
            <span class="widget-caption">Fiche marque</span>
         </div>
         <div class="widget-body">
				<div class="row">
					<input type="hidden" id="maxCtrl" name="maxCtrl">
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" value="Code" />
						<div class="col-md-4">
							<std:text name="marque.code" type="string" iskey="true" placeholder="Code" required="true" style="width:50%;float: left;" maxlength="20" />
							<% if(ControllerUtil.isEditionCreateAction(request)){ %>
								<a class="refresh-num" id="generer_code" href="javascript:" title="G&eacute;n&eacute;rer un code">
						            <i class="fa fa-refresh"></i>
						        </a>
					        <% }%>
						</div>
						
						<std:label classStyle="control-label col-md-2" value="Libellé" />
						<div class="col-md-4">
							<std:text name="marque.libelle" type="string" placeholder="Libellé" required="true" maxlength="80" />
						</div>
					</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" value="Image" />
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
						</div>
					</div>
					<div class="form-group">	
						<std:label classStyle="control-label col-md-2" value="Description"/>
						<div class="col-md-4">
							<std:textarea name="marque.description" rows="5" cols="50" maxlength="255"/>
						</div>
					</div>
				</div>i
				<!-- Generic Form -->
				<jsp:include page="/domaine/administration/dataValue_form.jsp" />
			</div>
		</div>

		<hr>
		<div class="form-actions">
			<div class="row" style="text-align: center;">
				<std:button actionGroup="M" classStyle="btn btn-success" action="stock.marque.work_merge" workId="${marque.id }" icon="fa-save" value="Sauvegarder" />
				<std:button actionGroup="D" classStyle="btn btn-danger" action="stock.marque.work_delete" workId="${marque.id }" icon="fa-trash-o" value="Supprimer" />
			</div>
		</div>	
	</div>
</div>
	</std:form>
</div>
