<%@page import="framework.model.common.util.ServiceUtil"%>
<%@page import="framework.model.util.FileUtil"%>
<%@page import="java.util.Map"%>
<%@page import="appli.model.domaine.stock.service.IArticleService"%>
<%@page import="appli.model.domaine.administration.persistant.GedFichierPersistant"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

 <script src="resources/framework/js/util_file_upload.js?v=1.1"></script>

<script type="text/javascript">
$(document).ready(function() {
	<%if(ControllerUtil.isEditionWritePage(request)){%>
		loadInputFileEvents();
	<% } else{%>
		$("#sep_photoX").remove();
	<%}%>

<%
GedFichierPersistant gedFichierBean = (GedFichierPersistant)request.getAttribute("gedFichier");
if(gedFichierBean != null && gedFichierBean.getId() != null){ 
	IArticleService service = (IArticleService)ServiceUtil.getBusinessBean(IArticleService.class);
	Map<String, byte[]> dataimg = service.getDataImage(gedFichierBean.getId(), "ged");
	if(dataimg.size() > 0){
		String fileName = gedFichierBean.getFile_name();
		byte[] value = dataimg.get(fileName);
	%>
	var img = "";
	var ext = '<%=fileName.substring(fileName.lastIndexOf(".")+1)%>';
    if(ext.startsWith('pdf')){
    	img = "resources/img/filetype_pdf.png";
    } else if(ext.startsWith('xls')){
    	img = "resources/img/xls_file.png";
    } else if(ext.startsWith('txt')){
    	img = "resources/img/txt.png";
    } else if(ext.startsWith('doc')){
    	img = "resources/img/document_microsoft_word_01.png";
    } else{
    	img = "data:image/jpeg;base64,<%=FileUtil.getByte64(value) %>";
    }
		$("#photoX_div").css("background", "");
 		$("#photoX_div").html("<img src='"+img+"' width='120' height='120'/>");
 		$("#photoX_name_span").text("<%=fileName%>");
 		$("#photoX_name").val("<%=fileName%>");
	<%}
} %>

});
</script>

<std:form name="data-form">
	<input type="hidden" name="images_uploaded" id="images_uploaded" value=";">
	<input type="hidden" name="images_names" id="images_names" value=";">
	<input type="hidden" name="MAX_FILE_SIZE" value="2097152" />
				
	<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">Fiche document</span>
			<std:link actionGroup="U" targetDiv="generic_modal_body" classStyle="btn btn-default" action="admin.ged.work_init_update" params="tpline=FC" workId="${gedFichier.id}" icon="fa fa-pencil"
 				tooltip="Modifier" />
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			
			<div class="row">
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Dossier" />
					<div class="col-md-9">
						<std:select name="ged_parent" type="long" key="id" disable="${empty gedFichier.id ? true:false}" labels="libelle" data="${listGedParent}" required="true" isTree="true" width="100%" value="${ged_parent }"/>
					</div>
				</div>
				
				<!-- Formulaire de saisie de type gedFichier -->
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Libell&eacute;" />
					<div class="col-md-9">
						<std:text name="ged.gedFichier.libelle" type="string" placeholder="Libell&eacute;" required="true" value="${gedFichier.libelle }" />
					</div>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Description" />
					<div class="col-md-9">
						<std:textarea name="ged.gedFichier.commentaire" placeholder="Description" rows="3" cols="20" maxlength="255" style="width:100%;" value="${gedFichier.commentaire }" />
					</div>
				</div>
			</div>
			<!-- Photos -->
			<div class="row">
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Document" />
					<div class="col-md-4">
						<div class="col-sm-12">	
							<div id="photoX_div" style="width: 150px;margin-top:10px; border: 1px solid #c6d983; border-radius: 25px; text-align: center; height: 120px; background: url('resources/framework/img/picture_file.png') no-repeat center;cursor: pointer;">
								<span style="font-size: 11px;">Fichier</span>
							</div>
							<span id="photoX_name_span"></span>
							<input type="hidden" name="photoX_name" id="photoX_name">
						</div>	
						<div class="col-sm-12">
							<!-- Separator -->
							<div id="sep_photoX" style="width:150px;margin-bottom: 5px; height: 20px; text-align: center;">
								<a href="javascript:"><b>X</b></a>
							</div>
							<!-- End -->
							<input type="file" name="photoX" id="photoX_div_file" style="display: none;" accept="image/*,.doc,.pdf,.xls,.xlsx,.doc,.docx,.txt">
						</div>
					</div>
			</div>
		</div>
		<hr>
		<div class="modal-footer">
			<std:button actionGroup="M" classStyle="btn btn-success" action="admin.ged.mergeDetailFichier" workId="${gedFichier.id }" icon="fa-save" value="Sauvegarder" />
			<std:button actionGroup="D" classStyle="btn btn-danger" action="admin.ged.deleteDetailFichier" workId="${gedFichier.id }" icon="fa-trash-o" value="Supprimer" />
			<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
				<i class="fa fa-times"></i> Fermer
			</button>
		</div>

	</div>
</div>
</std:form>
  