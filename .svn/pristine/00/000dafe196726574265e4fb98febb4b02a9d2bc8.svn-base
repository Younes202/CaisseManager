<%@page import="framework.model.common.util.ReflectUtil"%>
<%@page import="java.util.Map"%>
<%@page import="framework.model.common.util.ServiceUtil"%>
<%@page import="appli.model.domaine.stock.service.IArticleService"%>
<%@page import="appli.controller.domaine.stock.bean.ChargeDiversBean"%>
<%@page import="framework.model.util.FileUtil"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="appli.model.domaine.stock.persistant.MouvementPersistant"%>
<%@page import="java.util.List"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<style>
#generic_modal_body{
	width: 750px;
}
</style>

<script src="resources/framework/js/util_file_upload.js?v=1.2"></script>
<script type="text/javascript">
	var idxFile = 1;
	$(document).ready(function() {
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
		boolean isAchat = !"CH".equals(ControllerUtil.getMenuAttribute("tpR", request));
		if(isAchat){
			String mvmId= ReflectUtil.getStringPropertieValue(request.getAttribute("currMvm"), "id");
			if(StringUtil.isNotEmpty(mvmId)){
				IArticleService service = (IArticleService)ServiceUtil.getBusinessBean(IArticleService.class);
				Map<String, byte[]> dataimg = service.getDataImage(Long.valueOf(mvmId), "rgrpment_achat");
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
					$("#photo"+idxFile+"_name_span").html('<a href="front?w_f_act=<%=EncryptionUtil.encrypt("stock.regroupementMvm.downloadPieceJointe")%>&isa=1&pj=<%=EncryptionUtil.encrypt(mvmId)%>&nm=<%=key%>" target="_blank"><%=key%></a> | <a href="front?w_f_act=<%=EncryptionUtil.encrypt("stock.regroupementMvm.downloadPieceJointe")%>&isa=1&pj=<%=EncryptionUtil.encrypt(mvmId)%>&nm=<%=key%>&isdown=1" target="downloadframe"><i class="fa fa-cloud-download"></i></a>');
					$("#photo"+idxFile+"_name").val('<%=key%>');
					idxFile++;
			<%}
			}
	} else{
		ChargeDiversBean chrgDvBean = (ChargeDiversBean)request.getAttribute("chargeDivers");
		if(chrgDvBean != null && chrgDvBean.getId() != null){
			IArticleService service = (IArticleService)ServiceUtil.getBusinessBean(IArticleService.class);
			Map<String, byte[]> dataimg = service.getDataImage(chrgDvBean.getId(), "rgrpment_depense");
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
				$("#photo"+idxFile+"_name_span").html('<a href="front?w_f_act=<%=EncryptionUtil.encrypt("stock.regroupementMvm.downloadPieceJointe")%>&pj=<%=EncryptionUtil.encrypt(chrgDvBean.getId().toString())%>&nm=<%=key%>" target="_blank"><%=key%></a> | <a href="front?w_f_act=<%=EncryptionUtil.encrypt("stock.regroupementMvm.downloadPieceJointe")%>&pj=<%=EncryptionUtil.encrypt(chrgDvBean.getId().toString())%>&nm=<%=key%>&isdown=1" target="downloadframe"><i class="fa fa-cloud-download"></i></a>');
				$("#photo"+idxFile+"_name").val('<%=key%>');
				idxFile++;
		<%}
		}
	} %>
	});
</script>

<%
String tp = (String)ControllerUtil.getMenuAttribute("tpR", request);
String tpCH = (String)ControllerUtil.getMenuAttribute("tpCH", request);
%>

<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li class="active">Edition</li>
		<li>Regroupement des 
		<% if(tp.equals("MVM")){%>
			<%=tp.equals("a") ? "achats" : "avoirs" %>
		<%} else{ %>
			d&eacute;penses
		<%} %>
		</li>
		
	</ul>
</div>
<!-- /Page Breadcrumb -->
<!-- Page Header -->
<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<std:link actionGroup="U" classStyle="btn btn-default" action="stock.regroupementMvm.work_init_update" workId="${currMvm.id }" icon="fa-3x fa-pencil" tooltip="Modifier" />
		<% String act = null;
		
		if(tp.equals("MVM")){
			act = StringUtil.isTrue(""+ControllerUtil.getMenuAttribute("IS_GRP_VIEW", request)) ? "stock.mouvement.find_mvm_groupe" : "stock.mouvement.work_find";
		} else{
			act = StringUtil.isTrue(""+ControllerUtil.getMenuAttribute("IS_GRP_VIEW", request)) ? "stock.chargeDivers.find_charge_groupe" : "stock.chargeDivers.work_find";
		}%>
		
		<std:link classStyle="btn btn-default" action="<%=act %>" params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la recherche" />
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
		<div class="widget" >
			<div class="widget-header bordered-bottom bordered-blue">
				<span class="widget-caption">Fiche regroupement</span>
			</div>
			<div class="widget-body">
			<div class="row">
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Date facture" />
					<div class="col-md-6">
						<std:date name="date_mouvement" required="true" value="${currMvm.date_mouvement }" />
					</div>
				</div>	
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" valueKey="mouvement.num_facture" />
					<div class="col-md-6">
						<c:set var="tpR" value="<%=tp %>" />
						<std:text name="num_facture" type="string" placeholderKey="mouvement.num_facture" value="${tpR=='MVM' ? currMvm.num_facture : currMvm.num_bl }" maxlength="50" style="width:200px;"/>
					</div>
				</div>
			<hr>
			<!-- **************************** FINANCEMENT BLOC ********************** --> 
			<c:if test="${not empty mttTotal }">
				<h3 style="margin-left: 15%;color: green;">Total mouvements : <fmt:formatDecimal value="${mttTotal }" /> </h3>
			</c:if>
			<c:if test="${not empty mttAvoirTotal }">
				<h3 style="margin-left: 15%;color: green;">Total avoir : <fmt:formatDecimal value="${mttAvoirTotal }" /> </h3>
				<h3 style="margin-left: 15%;color: green;font-weight:bold !important;">Total : <fmt:formatDecimal value="${mttTotal-mttAvoirTotal }" /> </h3>
			</c:if>
			<c:if test="${not empty mttPaie }">
				<h3 style="margin-left: 15%;color: fuchsia;">Payé : <fmt:formatDecimal value="${mttPaie }" /> </h3>
			</c:if>
			<c:if test="${not empty mttRestePaie }">
				<h3 style="margin-left: 15%;color: blue;">Reste à payer : <fmt:formatDecimal value="${mttRestePaie }" /> </h3>
			</c:if>
			<hr>
			<c:set var="menu_scope.PAIEMENT_DATA" value="${currMvm.list_paiement }" scope="session" />
			<div class="form-group" id="finance_bloc">
				<jsp:include page="/domaine/compta/paiement_consult.jsp"></jsp:include>
			</div>
			<!-- **************************** FIN FINANCEMENT BLOC ********************** -->
			<hr>
<!-- 			<div class="form-group">	 -->
<%-- 				<std:label classStyle="control-label col-md-3" value="Groupement par fournisseur" /> --%>
<!-- 				<div class="col-md-9"> -->
<%-- 					<std:checkbox name="is_fournisseur" onClick="$('#fourn-div').toggle();" /> --%>
<!-- 				</div> -->
<!-- 			</div> -->
			
			<div class="form-group">
				<std:label classStyle="control-label col-md-3" value="Fournisseur" />
				<div class="col-md-9">
					<std:select name="fournisseur" type="long" data="${list_fournisseur }" value="${fourn_id }" required="true" key="id" labels="code;' - ';libelle" width="50%"/>
				</div>
			</div>
			<div class="form-group">
				<std:label classStyle="control-label col-md-3" value="Date d&eacute;but" />
				<div class="col-md-2">
					<std:date name="date_debut" />
				</div>
				<std:label classStyle="control-label col-md-1" value="Fin" />
				<div class="col-md-2">
					<std:date name="date_fin" />
				</div>
				<div class="col-md-1">
					<std:link noJsValidate="true" classStyle="btn btn-primary"  tooltip="Charger les mouvements" action="stock.regroupementMvm.work_init_create" params="is_refresh=1" icon="fa fa-refresh" />
				</div>
			</div>	
			<div class="form-group">
				<std:label classStyle="control-label col-md-3" value="BL/Facture/Re&ccedil;u &agrave; regrouper" />
				<div class="col-md-9">
					<std:select name="mouvementIds" type="long[]" data="${list_bl }" value="${currMvm.mouvementIds }" key="id" labels="num_bl;' ';num_facture;' ';num_recu" multiple="true" required="true" width="100%"/>
				</div>
			</div>
			<div class="form-group">
				<std:label classStyle="control-label col-md-3" value="Avoirs à inclure" />
				<div class="col-md-9">
					<std:select name="avoirIds" type="long[]" data="${list_avoir }" value="${currMvm.avoirIds }" key="id" labels="num_bl;' ';num_facture;' ';num_recu" multiple="true" width="100%"/>
				</div>
			</div>
				<hr>
                 <span style="padding:20px;">
                 	<%if(ControllerUtil.isEditionWritePage(request)){ %>
						<a href="javascript:void(0);" id="addFile" targetBtn="C" class="btn btn-info" title="Ajouter pi&egrave;ce jointe">
							<i class="fa fa-3x fa-plus"> </i> Ajouter pi&egrave;ces jointes
						</a>
					<%} %>
                 </span>
				<!-- Photos -->
				<div class="row" id="row_file" style="margin-top: 20px;">
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
				<hr>
				<div class="row" style="text-align: center;">
					<div class="col-md-12">
						<std:button actionGroup="M" classStyle="btn btn-success" action="stock.regroupementMvm.work_merge" workId="${currMvm.id }" icon="fa-save" value="Sauvegarder" />
					</div>
				</div>
			</div>
		</div>
	</div>
  </std:form>
</div>
