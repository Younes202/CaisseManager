<%@page import="framework.model.util.FileUtil"%>
<%@page import="java.util.Map"%>
<%@page import="framework.model.common.util.ServiceUtil"%>
<%@page import="appli.model.domaine.stock.service.IArticleService"%>
<%@page import="appli.controller.domaine.stock.bean.TravauxChantierBean"%>
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
var idxFile = 1;

$(document).ready(function (){
	$("#addFile").click(function(){
		var content = $("#fileLoadDiv").clone().html();
		content = content.replace(/photoX/g,"photo"+idxFile);
		$("#row_file").append(content);
		idxFile++;
	});
	
	<%if(ControllerUtil.isEditionWritePage(request)){%>
		loadInputFileEvents();
	<% } else{%>
		$("div[id^='sep_photo']").remove();
	<%}
	// Initialiser les photos ou documents
	TravauxChantierBean fraisBean = (TravauxChantierBean)request.getAttribute("travauxChantier");
	if(fraisBean != null && fraisBean.getId() != null){
		IArticleService service = (IArticleService)ServiceUtil.getBusinessBean(IArticleService.class);
		Map<String, byte[]> dataimg = service.getDataImage(fraisBean.getId(), "travauxChantier");
		for(String key : dataimg.keySet()){%>
			var content = $("#fileLoadDiv").clone().html();
			content = content.replace(/photoX/g,"photo"+idxFile);
			$("#row_file").append(content);
			var ext = '<%=key.substring(key.lastIndexOf(".")+1)%>';
			var img = "";
	        if(ext.startsWith('pdf')){
	        	img = "resources/img/filetype_pdf.png";
	        } else if(ext.startsWith('xls')){
	        	img = "resources/img/xls_file.png";
	        } else if(ext.startsWith('txt')){
	        	img = "resources/img/txt.png";
	        } else if(ext.startsWith('doc')){
	        	img = "resources/img/document_microsoft_word_01.png";
	        } else{
	        	img = "data:image/jpeg;base64,<%=FileUtil.getByte64(dataimg.get(key)) %>";
	        }
	        $("#photo"+idxFile+"_div").css("background", "");
	        $("#photo"+idxFile+"_div").html("<img src='"+img+"' width='120' height='120'/>");
			$("#photo"+idxFile+"_name_span").html('<a href="front?w_f_act=<%=EncryptionUtil.encrypt("stock.travauxChantier.downloadPieceJointe")%>&pj=<%=EncryptionUtil.encrypt(fraisBean.getId().toString())%>&nm=<%=key%>" target="_blank"><%=key%></a> | <a href="front?w_f_act=<%=EncryptionUtil.encrypt("stock.travauxChantier.downloadPieceJointe")%>&pj=<%=EncryptionUtil.encrypt(fraisBean.getId().toString())%>&nm=<%=key%>&isdown=1" target="downloadframe"><i class="fa fa-cloud-download"></i></a>');
			$("#photo"+idxFile+"_name").val('<%=key%>');
			idxFile++;
	<%}
	} %>
});
</script>

<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Gestion des travaux</li>
		<li>Fiche chantier</li>
		<li class="active">Edition</li>
	</ul>
</div>
<!-- /Page Breadcrumb -->
<!-- Page Header -->
<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<std:link actionGroup="U" classStyle="btn btn-default" action="stock.travauxChantier.work_init_update" workId="${travauxChantier.id }" icon="fa-3x fa-pencil" tooltip="Modifier" />
		<%
		String act = (ControllerUtil.getMenuAttribute("IS_SUB_ADD", request) != null) 
		               ? ""+ControllerUtil.getMenuAttribute("IS_SUB_ADD", request) : "stock.travauxChantier.work_find";
		%>
		<std:link classStyle="btn btn-default" action='<%=act %>' params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la recherche" />
        <% boolean isSubDiv = "stock.travauxChantier.work_init_create".equals(ControllerUtil.getMenuAttribute("IS_SUB_ADD", request)); %>
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
		
			<div class="widget-body">
				<div class="row">
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" value="Libellé" />
						<div class="col-md-4">
							<std:text name="travauxChantier.libelle" type="string" placeholder="Libellé" required="true" maxlength="80" />
						</div>
					</div>	
					<div class="form-group">	
						<std:label classStyle="control-label col-md-2" value="Lieu" />
						<div class="col-md-4">
							<std:text name="travauxChantier.lieu" type="string" placeholder="Lieu" maxlength="80" />
						</div>
					</div>
					<div class="form-group">					
						<std:label classStyle="control-label col-md-2" value="Date début" />
						<div class="col-md-4">
							<std:date name="travauxChantier.date_debut" />
						</div>
						<std:label classStyle="control-label col-md-2" value="Date fin" />
						<div class="col-md-4">
							<std:date name="travauxChantier.date_fin" />
						</div>
					</div>	
					<c:if test="${travauxChantier.id != null }">
						<div class="form-group">
							<std:label classStyle="control-label col-md-2" value="Budget prévu" />
							<div class="col-md-4">
								<std:text name="travauxChantier.budget_prevu" readOnly="true" type="decimal" style="width:150px;" placeholder="Budget prévu" maxlength="10" />
							</div>
							<std:label classStyle="control-label col-md-2" value="Budget consommé" />
							<div class="col-md-4">
								<std:text name="travauxChantier.budget_consomme" readOnly="true" type="decimal" style="width:150px;" placeholder="Budget consommé" maxlength="10" />
							</div>
						</div>
					</c:if>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" value="Responsable" />
						<div class="col-md-4">
							<std:select width="100%" name="travauxChantier.opc_responsable.id" type="long" key="id" labels="nom;' ';prenom" data="${listeEmploye }" />
							<% if(isSubDiv){ %>
							<std:link action="pers.employe.work_init_create" style="float: right;position: absolute;margin-left: 2%;" icon="fa fa-plus" noJsValidate="true" actionGroup="C" params="w_nos=1" />
					        <%} %>
						</div>
					</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" value="Description" />
						<div class="col-md-10">
							<std:textarea-rich name="travauxChantier.description"/>
						</div>
					</div>
				</div>
			</div>
		</div>

<!-- Pieces -->
		<div class="widget">
			<div class="widget-header bordered-bottom bordered-blue">
				<span class="widget-caption">
				<%if(ControllerUtil.isEditionWritePage(request)){ %>
					<a href="javascript:void(0);" id="addFile" targetBtn="C" class="btn btn-default" title="Ajouter pi&egrave;ce jointe" style="margin-top: -2px;">
						<i class="fa fa-3x fa-plus"> </i> Ajouter pi&egrave;ces jointes
					</a>
					<%} %>
				</span>
			</div>
			 <div class="widget-body">
					<!-- Photos -->
					<div class="row" id="row_file">
						<div id="fileLoadDiv" style="display: none;">
							<div class="col-md-2">
								<div class="col-sm-12">	
									<div id="photoX_div" style="border: 1px solid #c6d983; border-radius: 25px; text-align: center; height: 120px; background: url('resources/framework/img/picture_file.png') no-repeat center;cursor: pointer;">
										<span style="font-size: 11px;">Fichier</span>
									</div>
								</div>
								<div class="col-sm-12" style="text-align: center;color: olive;">
									<span id="photoX_name_span"></span>
									<input type="hidden" name="photoX_name" id="photoX_name">
								</div>
								<div class="col-sm-12">
									<!-- Separator -->
									<div id="sep_photoX" style="margin-bottom: 5px; height: 20px; text-align: center;">
										<a href="javascript:"><b>X</b></a>
									</div>
									<!-- End -->
									<input type="file" name="photoX" id="photoX_div_file" style="display: none;" accept="image/*,.doc,.pdf,.xls,.xlsx,.doc,.docx,.txt">
								</div>
							</div>
						</div>
				</div>
			</div>		
		</div>
		
		<hr>
		<div class="row" style="text-align: center;">
			<div class="col-md-12">
				<std:button actionGroup="M" classStyle="btn btn-success" action="stock.travauxChantier.work_merge" workId="${travauxChantier.id }" icon="fa-save" value="Sauvegarder" />
				<std:button actionGroup="D" classStyle="btn btn-danger" action="stock.travauxChantier.work_delete" workId="${travauxChantier.id }" icon="fa-trash-o" value="Supprimer" />
			</div>
		</div>
	</std:form>
</div>

