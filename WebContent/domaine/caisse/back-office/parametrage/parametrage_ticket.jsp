<%@page import="framework.model.beanContext.AbonnementBean"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="framework.model.util.FileUtil"%>
<%@page import="appli.model.domaine.administration.persistant.ParametragePersistant"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.model.common.util.ServiceUtil"%>
<%@page import="appli.model.domaine.stock.service.IArticleService"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="fn"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<style>
   .form-title{ 
    	margin-left: 12px;
	}
</style>
<script type="text/javascript">
	$(document).ready(function() {
		<%if(ControllerUtil.isEditionWritePage(request)){%>
			loadInputFileEvents();
	<%} else{%>
		resetInputFileEvents();
		$("div[id^='sep_photo']").remove();
	<%}
		// Initialiser les photos ou documents
		IArticleService service = (IArticleService)ServiceUtil.getBusinessBean(IArticleService.class);
		Map<String, byte[]> dataimg = service.getDataImage(ContextAppli.getEtablissementBean().getId(), "paramTICK");
		for(String key : dataimg.keySet()){%>
			var ext = '<%=key.substring(key.lastIndexOf(".")+1)%>';
			var img = "";
	        if(ext.startsWith('pdf')){
	        	img = "resources/framework/img/filetype_pdf.png";
	        } else if(ext.startsWith('xls')){
	        	img = "resources/framework/img/xls_file.png";
	        } else if(ext.startsWith('txt')){
	        	img = "resources/framework/img/txt.png";
	        } else if(ext.startsWith('doc')){
	        	img = "resources/framework/img/document_microsoft_word_01.png";
	        } else{
	        	img = "data:image/jpeg;base64,<%=FileUtil.getByte64(dataimg.get(key))%>";
	        }
	        $("#photoX_div").css("background", "");
	        $("#photoX_div").html("<img src='"+img+"' width='120' height='120'/>");
			$("#photoX_name_span").html('<a href="front?w_f_act=<%=EncryptionUtil.encrypt("admin.parametrage.downloadPieceJointe")%>&pj=<%=EncryptionUtil.encrypt(ContextAppli.getEtablissementBean().getId().toString())%>&nm=<%=key%>" target="_blank"><%=key%></a> | <a href="front?w_f_act=<%=EncryptionUtil.encrypt("admin.parametrage.downloadPieceJointe")%>&pj=<%=EncryptionUtil.encrypt(ContextAppli.getEtablissementBean().getId().toString())%>&nm=<%=key%>&isdown=1" target="downloadframe"><i class="fa fa-cloud-download"></i></a>');
			$("#photoX_name").val('<%=key%>');
		<%}
		
		dataimg = service.getDataImage(ContextAppli.getEtablissementBean().getId(), "paramFE");
		for(String key : dataimg.keySet()){%>
			var ext = '<%=key.substring(key.lastIndexOf(".")+1)%>';
			var img = "";
	        if(ext.startsWith('pdf')){
	        	img = "resources/framework/img/filetype_pdf.png";
	        } else if(ext.startsWith('xls')){
	        	img = "resources/framework/img/xls_file.png";
	        } else if(ext.startsWith('txt')){
	        	img = "resources/framework/img/txt.png";
	        } else if(ext.startsWith('doc')){
	        	img = "resources/framework/img/document_microsoft_word_01.png";
	        } else{
	        	img = "data:image/jpeg;base64,<%=FileUtil.getByte64(dataimg.get(key)) %>";
	        }
	        $("#photoZ_div").css("background", "");
	        $("#photoZ_div").html("<img src='"+img+"' width='120' height='120'/>");
			$("#photoZ_name_span").html('');
			$("#photoZ_name").val('<%=key%>');
		<%} %>
	});
</script>

<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Paramétrage</li>
		<li class="active">Caisse</li>
	</ul>
</div>

<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<std:link actionGroup="C" classStyle="btn btn-default" action="admin.parametrage.work_init_update" icon="fa-3x fa-pencil" tooltip="Modifier" />
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
		              <div class="tabbable">
		                    <ul class="nav nav-tabs" id="myTab">
		                          <%request.setAttribute("mnu_param", "ticket"); %> 
			 					  <jsp:include page="parametrage_headers.jsp" />
		                     </ul>
		                </div>
		          </div>
		      </div>
			
			<div class="widget-body">
				<div class="row" style="margin-top: 25px;">
					<c:set var="oldSubGroupe" value="" />
					<c:forEach items="${listParams }" var="parametre">
						<c:if test="${parametre.groupe == 'CAISSE_TICKET'}">
							<c:if test="${oldSubGroupe != parametre.groupe_sub or empty oldSubGroupe}">
								<h3 style="color: #2dc3e8;padding-left: 20px;border-bottom: 1px dashed rgba(0, 0, 0, .2);">${parametre.groupe_sub }</h3>
							</c:if>
							<c:set var="oldSubGroupe" value="${parametre.groupe_sub }" />
							<div class="form-group">
								<std:label classStyle="control-label col-md-5" value="${parametre.libelle}" />
								<div class="col-md-7">
									<c:choose>
										<c:when test="${parametre.code=='FORMAT_TICKET'}">
											<std:select name="param_${parametre.code}" type="string" style="width:50%;float:left;" data="${formatArray }" value="${parametre.valeur}" />
										</c:when>
										<c:when test="${parametre.type=='STRING'}">
											<std:text name="param_${parametre.code}" type="string" style="width:50%;float:left;" maxlength="120" value="${parametre.valeur}" />
										</c:when>
										<c:when test="${parametre.type=='NUMERIC'}">
											<std:text name="param_${parametre.code}" type="long" style="width:120px;float:left;" maxlength="120" value="${parametre.valeur}" />
										</c:when>
										<c:when test="${parametre.type=='BOOLEAN'}">	
											<std:checkbox name="param_${parametre.code}" checked='${parametre.valeur }' />
										</c:when>
									</c:choose>
									<c:if test="${parametre.help != null && parametre.help != ''}">
										<img class="tooltip-lg" data-toggle="tooltip" data-placement="top" data-original-title="${parametre.help}" src="resources/framework/img/info.png" style="vertical-align: bottom;"/>
									</c:if>
								</div>
							</div>
						</c:if>
					</c:forEach>	
				</div>
				<!-- Pieces -->
				<div class="row" style="margin-top: 25px;">
					<h3 style="color: #2dc3e8;padding-left: 20px;border-bottom: 1px dashed rgba(0, 0, 0, .2);">PUBLICITÉ DANS LE TICKET DE CAISSE</h3>
					<div class="col-md-8">
						<div class="col-md-12">
							<div class="form-group">
								<std:label classStyle="control-label col-md-5" value="Titre publicité" />
								<std:text name="restaurant.titre_publicite" type="string" style="width:50%;float:left;" value="${restaurant.titre_publicite}" />
							</div>	
						</div>
						<div class="col-md-12">	
						 	<div class="form-group">
								<std:label classStyle="control-label col-md-5" value="Message publicité" />
								<std:textarea name="restaurant.msg_publicite" style="width:50%;float:left;" rows="3" value="${restaurant.msg_publicite}" />
							</div>
						</div>		
					</div>
					<div class="col-md-2" id="row_file">
						<!-- Photos -->
						<div id="fileLoadDiv">
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
								<input type="file" name="photoX" id="photoX_div_file" path="paramTICK" style="display: none;" accept="image/*">
							</div>
							<i class="fa-fw fa fa-warning" style="color: orange;"></i><span style="color: orange;float: left;width: 100%;">L'image doit &ecirc;tre redimentionnée avant importation.</span>
						</div>
					</div>
				</div>			
			</div>
		<hr>
		<div class="form-actions">
			<div class="row" style="text-align: center;" class="col-md-12" id="action-div">
				<std:button actionGroup="M" classStyle="btn btn-success" action="admin.parametrage.work_update" params="tp=tick" icon="fa-save" value="Sauvegarder" />
			</div>
		</div>
		</std:form>
	</div>
</div>

<script type="text/javascript">
/*Handles ToolTips*/
$("[data-toggle=tooltip]")
    .tooltip({
        html: true
    });
</script>    