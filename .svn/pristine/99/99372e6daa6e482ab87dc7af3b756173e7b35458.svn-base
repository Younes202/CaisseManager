<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="framework.model.util.FileUtil"%>
<%@page import="framework.model.common.util.ServiceUtil"%>
<%@page import="appli.model.domaine.stock.service.IArticleService"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="appli.controller.domaine.stock.bean.MouvementBean"%>
<%@page import="appli.controller.domaine.stock.action.ArticleAction"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<script src="resources/framework/js/util_file_upload.js?v=1.1"></script>

<style>
.select2-container{
min-width: 140px !important;
}
hr{
margin: 15px;
}

</style>
<script type="text/javascript">
$(document).ready(function (){
		$("#generer_bl").click(function(){
			executePartialAjax($(this), '<%=EncryptionUtil.encrypt("stock.mouvement.genererNumBL")%>', 'mouvement.num_bl', true, true, null, true);
		});
		
		$(document).off('click', '#add_ctrl');
		$(document).on('click', '#add_ctrl', function(){
			var contentTr = $("#ctrl_gpt").html();
			var cpt = $("select[id^='opc_article']").length + 1;
			contentTr = (contentTr ? contentTr.replace(new RegExp("_0", 'g'), "_"+ cpt) : null);
			
			if(contentTr != null){
				$("#ctrl_table").append('<tr cpt="'+cpt+'">' + contentTr + '</tr>');
			}
			//
			$("select[id='opc_article\\.id_" + cpt + "']").select2({allowClear: true});
			 $("select[id='opc_article\\.id_" + cpt + "']").css("width", "100% !important");
			 $("#edit_article_"+cpt).css('display', 'none');
		});
		$(document).off('click', '#delete_cont');
		$(document).on('click', "#delete_cont", function() {
			$(this).closest("tr").remove();
		});
		
		$(document).off('change', 'select[id^="opc_article"]');
		$(document).on('change', "select[id^='opc_article']", function() {
			var idx = $(this).attr("id").substring($(this).attr("id").lastIndexOf("_")+1);
			if($("option:selected", this).attr("hiddenkey")){
				$('#edit_article_'+idx).attr("params", "art="+$(this).val());
				$('#edit_article_'+idx).css('display', 'block');
			} else{
				$('#edit_article_'+idx).css('display', 'none');
			}
		});
		
		<% if(request.getAttribute("is_inv_prev") != null){	%>
		$("#ctrl_table").find("input, select").attr("disabled", "disabled").css("background-color", "#eee");
		$("#ctrl_table").find("a").remove();
		<%}%>
		
// 		$("#article, #composant").change(function(){
// 			manageArtCmp();
// 		});
// 		manageArtCmp();
	});
	
// 	function manageArtCmp(){
// 		if($('#article').val() == '' && $('#composant').val() == ''){
// 			$('#article').removeAttr('disabled', 'disabled');
// 			$('#composant').removeAttr('disabled', 'disabled');
// 			$("#quantite_cmp, #quantite_art").css("background-color", "white");
// 		} else if($('#article').val() == ''){
// 			$('#article').attr('disabled', 'disabled');
// 			$("#quantite_art").val('').css("background-color", "#eee");
// 		} else if ($('#composant').val() == ''){
// 			$('#composant').attr('disabled', 'disabled');
// 			$("#quantite_cmp").val('').css("background-color", "#eee");
// 		}
// 	}
</script>

<!-- Page Breadcrumb -->
 <div class="page-breadcrumbs breadcrumbs-fixed">
     <ul class="breadcrumb">
         <li>
             <i class="fa fa-home"></i>
             <a href="#">Accueil</a>
         </li>
         <li>Gestion de stock</li>
         <li>Fiche des transformations 
		</li>
         <li class="active">Edition</li>
     </ul>
 </div>
<!-- /Page Breadcrumb -->
  <!-- Page Header -->
  <div class="page-header position-relative">
      <div class="header-title" style="padding-top: 4px;">
  	 <c:if test="${!is_mois_clos}">
          <std:link actionGroup="U" classStyle="btn btn-default" action="stock.transformation.work_init_update" workId="${mouvement.id }" icon="fa-3x fa-pencil" tooltip="Modifier" />
  	 </c:if>       
	<std:link classStyle="btn btn-default" action="stock.transformation.work_find" params="bck=1" icon="fa fa-3x fa-mail-reply-all"
			tooltip="Retour &agrave; la recherche" />
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
	<input type="hidden" name="is_inv_prev" value="${is_inv_prev?'true':'false' }">
	<input type="hidden" name="mouvement.type_mvmnt" value="tr">
	
	<!-- widget grid -->
	<div class="widget">
         <div class="widget-header bordered-bottom bordered-blue">
            <span class="widget-caption">Fiche transformation</span>
         </div>
         <div class="widget-body">
			<div class="row">
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" value="Date mouvement" />
					<div class="col-md-2">
						<std:date name="mouvement.date_mouvement" required="true" readOnly="${is_inv_prev?'true':'false' }" />
					</div>
				</div>
				<div class="form-group">	
					<std:label classStyle="control-label col-md-2" value="Num&eacute;ro Bon" />
					<div class="col-md-2">
						<std:text name="mouvement.num_bl" type="string" placeholder="Bon transformation" maxlength="50" style="width: 90%;float: left;"/>
						<% if(ControllerUtil.isEditionCreateAction(request)){ %>
							<a class="refresh-num" id="generer_bl" href="javascript:" title="G&eacute;n&eacute;rer un num&eacute;ro de bon de transfert">
					            <i class="fa fa-refresh"></i>
					        </a>
				        <% }%>
					</div>
				</div>
				<hr>
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" value="Emplacement" />
					<div class="col-md-4">
						<std:select name="mouvement.opc_emplacement.id" disable="${is_inv_prev?'true':'false' }" type="long" key="id" labels="titre" data="${listeDestination}" required="true" width="100%" />
					</div>
					<std:label classStyle="control-label col-md-2" value="Destination" />
					<div class="col-md-4">
						<std:select name="mouvement.opc_destination.id" disable="${is_inv_prev?'true':'false' }" type="long" key="id" labels="titre" data="${listeDestination}" required="true" width="100%" value="${mvmDestBean.opc_destination.id }" />
					</div>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" value="Commentaire" />
					<div class="col-md-4">
						<std:textarea name="mouvement.commentaire" rows="3" cols="80" />
					</div>
				</div>
			</div>	
		</div>
	</div>

	<div class="widget" id="div_detail">
         <div class="widget-header bordered-bottom bordered-blue">
            <span class="widget-caption">Composants &agrave; transformer</span>
            <span style="margin-left: 50%;" class="widget-caption">Transformer en :</span>
         </div>
         <div class="widget-body">
				<div class="row">
					<div class="col-md-6" style="border-right: 1px solid red;">
					<table id="ctrl_table" style="width: 97%;margin-left: 20px;">
						<tr style="border-bottom: 1px solid #2196f3;">
							<th>Composant</th>
							<th width="60px"></th>
							<th width="100px">Quantit&eacute;</th>
							<th width="50px"></th>
						</tr>
						<tr id="ctrl_gpt" style="display: none;">
							<td style="padding-top: 5px; padding-right: 10px;" valign="top">
								<std:select style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important; width: 100% !important;" classStyle="slct" name="opc_article.id_0" hiddenkey="prix_achat_ht;opc_tva_enum.id" type="long" key="id" labels="libelle" data="${listComposant}"  groupKey="opc_famille_stock.id" groupLabels="opc_famille_stock.code;'-';opc_famille_stock.libelle"/>
							</td>
							<td style="padding-top: 5px; padding-right: 10px" valign="top">
								<std:linkPopup id="edit_article_0" classStyle="btn btn-sm btn-primary" action="stock.composant.work_edit" noJsValidate="true">
									<span class="fa  fa-eye"></span>
								</std:linkPopup>
							</td>
							<td style="padding-top: 5px; padding-right: 10px;" valign="top">
								<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width: 100px;" name="quantite_0" type="decimal" placeholder="Quantit&eacute;" maxlength="14"/>
							</td>
							<td valign="top" style="padding-top: 5px; padding-right: 1px;">
								<std:link actionGroup="M" id="delete_cont" icon="fa fa-times" style="color: red;"></std:link>
							</td>
						</tr>
	
						<c:set var="cpt" value="${1 }" />
						<c:set var="encodeService" value="<%=new EncryptionUtil()%>" />
						<c:set var="articleAction" value="<%=new ArticleAction()%>" />
						<c:set var="bigDecimalUtil" value="<%=new BigDecimalUtil()%>" />
						
						<c:forEach items="${mouvement.list_article }" var="articleMvm">
							<std:hidden name="eaiid_${cpt}" value="${encodeService.encrypt(articleMvm.id) }" />
							<tr>
								<td style="padding-top: 5px; padding-right: 30px;" valign="top">
									<std:select style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;; width: 100% !important;" name="opc_article.id_${cpt}" type="long" key="id" labels="libelle" data="${listComposant }" value="${articleMvm.opc_article.id }" groupKey="opc_famille_stock.id" groupLabels="opc_famille_stock.code;'-';opc_famille_stock.libelle" />
								</td>
								<td style="padding-top: 5px; padding-right: 10px;" valign="top">
									<std:linkPopup id="edit_article_${cpt}" classStyle="btn btn-sm btn-primary" action="stock.composant.work_edit" params="art=${articleMvm.opc_article.id }&isCmp=1" noJsValidate="true"> 
										<span class="fa fa-eye"></span>
									</std:linkPopup>
								</td>
								<td style="padding-top: 5px; padding-right: 30px;" valign="top">
									<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width: 100px;" name="quantite_${cpt}" type="decimal" placeholder="Quantit&eacute;" value="${articleMvm.quantite }"  maxlength="14"/>
								</td>
								<td valign="top" style="padding-top: 5px; padding-right: 1px;">
									<std:link actionGroup="M" id="delete_cont" icon="fa fa-times" style="color: red;"></std:link>
								</td>
							</tr>
	
							<c:set var="cpt" value="${cpt+1 }" /> 
						</c:forEach>
					</table>
				</div>
				<div class="col-md-6">
				
					<c:if test="${not empty mvmDestBean}">
						<c:choose>
							<c:when test="${mvmDestBean.list_article.get(0).opc_article.is_stock }">
								<c:set var="valCompId" value="${mvmDestBean.list_article.get(0).opc_article.id }" />
								<c:set var="valCompQte" value="${mvmDestBean.list_article.get(0).quantite }" />
							</c:when>
							<c:otherwise>
								<c:set var="valArtId" value="${mvmDestBean.list_article.get(0).opc_article.id }" />
								<c:set var="valArtQte" value="${mvmDestBean.list_article.get(0).quantite }" />
							</c:otherwise>
						</c:choose>
					</c:if>
					<div class="row" style="margin-top: 10px;">
						<std:label classStyle="control-label col-md-2" value="Composant" />
						<div class="col-md-7">
							<std:select name="composant" disable="${is_inv_prev?'true':'false' }" type="long" key="id" labels="code;'-';libelle" data="${listComposant}" width="100%" groupKey="opc_famille_stock.id" groupLabels="opc_famille_stock.code;'-';opc_famille_stock.libelle" value="${valCompId }"/>
						</div>
						<div class="col-md-3">
							<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width: 100px;" name="quantite_cmp" type="decimal" placeholder="Quantit&eacute;" maxlength="14" value="${valCompQte }"/>
						</div>	
					</div>	
				</div>
		</div>
	</div>
</div>
			<hr>
			<div class="form-actions">
				<div class="row" style="text-align: center;" class="col-md-12" id="action-div">
				<% if(request.getAttribute("is_inv_prev") == null){	%>
						<std:link actionGroup="M" id="add_ctrl" tooltip="Ajouter un article" icon="fa-1x fa-plus" classStyle="btn btn-default" />
					<%} %>	
						<std:button actionGroup="M" classStyle="btn btn-success" action="stock.transformation.work_merge" workId="${mouvement.id }" icon="fa-save"
							value="Sauvegarder" />
					<% if(request.getAttribute("is_inv_prev") == null){	%>		
						<std:button actionGroup="D" classStyle="btn btn-danger" action="stock.transformation.work_delete" workId="${mouvement.id }" icon="fa-trash-o"
							value="Supprimer" />
					<%} %>		
					</div>
				</div>
	</std:form>
</div>			
