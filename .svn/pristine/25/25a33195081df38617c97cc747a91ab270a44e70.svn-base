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
	var idxFile = 1;
	$(document).ready(function() {
		
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
			
			  $('#date_peremption_' + cpt).datepicker({
		    	todayBtn: true,
		    	clearBtn: true,
			    language: "fr",
			    autoclose: true,
			    todayHighlight: true
		    });
			 $("select[id='opc_article\\.id_" + cpt + "']").css("width", "100% !important");
			 $("#edit_article_"+cpt).css('display', 'none');
		});
		$(document).off('click', '#delete_cont');
		$(document).on('click', "#delete_cont", function() {
			$(this).closest("tr").remove();
		});


			
		// Peulper les donner cheque fourniseur
		setTimeout(function() {
			manageTypeTransfert();
			$("#opc_article\\.id_0").trigger("change");//Recalcul total all lines
		}, 100);
		
		<% if(request.getAttribute("is_inv_prev") != null){	%>
		$("#ctrl_table").find("input, select").attr("disabled", "disabled").css("background-color", "#eee");
		$("#ctrl_table").find("a").remove();
		<%}%>
	});
</script>

<!-- Page Breadcrumb -->
 <div class="page-breadcrumbs breadcrumbs-fixed">
     <ul class="breadcrumb">
         <li>
             <i class="fa fa-home"></i>
             <a href="#">Accueil</a>
         </li>
         <li>Gestion de stock</li>
         <li>Fiche de demande de transfert</li>
         <li class="active">Edition</li>
     </ul>
 </div>
<!-- /Page Breadcrumb -->
  <!-- Page Header -->
  <div class="page-header position-relative">
      <div class="header-title" style="padding-top: 4px;">
      	 <c:if test="${isEditable }">
          	<std:link actionGroup="U" classStyle="btn btn-default" action="stock.demandeTransfert.work_init_update" workId="${demandeTransfert.id }" icon="fa-3x fa-pencil" tooltip="Modifier" />
		 </c:if>
		<std:link classStyle="btn btn-default" action="stock.demandeTransfert.work_find" params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la recherche" />
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
            <span class="widget-caption">Fiche demande de transfert</span>
         </div>
         <div class="widget-body">
				<div class="row">
			<div class="form-group">
				<std:label classStyle="control-label col-md-2" value="Date souhait&eacute;e" />
				<div class="col-md-2">
					<std:date name="demandeTransfert.date_souhaitee" required="true"  />
				</div>
			</div>
		</div>	
	</div>
</div>

	<div class="widget" id="div_detail">
         <div class="widget-header bordered-bottom bordered-blue">
            <span class="widget-caption">D&eacute;tail des composants</span>
         </div>
         <div class="widget-body">
				<div class="row">
				<table id="ctrl_table" style="width: 97%;margin-left: 20px;">
					<tr>
						<th>Composant</th>
						<th width="60px"></th>
						<th width="100px">Quantit&eacute;</th>
						<th width="50px"></th>
					</tr>
					<tr id="ctrl_gpt" style="display: none;">
						<td style="padding-top: 5px; padding-right: 10px;" valign="top">
							<std:select style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important; width: 100% !important;" classStyle="slct" name="opc_article.id_0" hiddenkey="prix_achat_ht;opc_tva_enum.id" type="long" key="id" labels="code;'-';libelle" data="${listArticle}"  groupKey="opc_famille_stock.id" groupLabels="opc_famille_stock.code;'-';opc_famille_stock.libelle" />
						</td>
						<td style="padding-top: 5px; padding-right: 10px" valign="top">
							<std:linkPopup id="edit_article_0" classStyle="btn btn-sm btn-primary" action="stock.composant.work_edit" noJsValidate="true">
								<span class="fa  fa-eye"></span>
							</std:linkPopup>
						</td>
						<td style="padding-top: 5px; padding-right: 10px;" valign="top">
							<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width: 100px;" name="quantite_0" type="decimal" placeholderKey="mouvement.quantite" maxlength="14"/>
						</td>
						<td valign="top" style="padding-top: 5px; padding-right: 1px;">
							<std:link actionGroup="M" id="delete_cont" icon="fa fa-times" style="color: red;"></std:link>
						</td>
					</tr>

					<c:set var="cpt" value="${1 }" />
					<c:set var="encodeService" value="<%=new EncryptionUtil()%>" />
					<c:set var="articleAction" value="<%=new ArticleAction()%>" />
					<c:set var="bigDecimalUtil" value="<%=new BigDecimalUtil()%>" />
					
					<c:forEach items="${demandeTransfert.list_article }" var="articleMvm">
						<std:hidden name="eaiid_${cpt}" value="${encodeService.encrypt(articleMvm.id) }" />
						<tr>
							<td style="padding-top: 5px; padding-right: 30px;" valign="top">
								<std:select style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;; width: 100% !important;" name="opc_article.id_${cpt}" hiddenkey="prix_achat_ht;opc_tva_enum.id" type="long" key="id" labels="code;'-';libelle" data="${listArticle }" value="${articleMvm.opc_article.id }" groupKey="opc_famille_stock.id" groupLabels="opc_famille_stock.code;'-';opc_famille_stock.libelle" />
							</td>
							<td style="padding-top: 5px; padding-right: 10px;" valign="top">
								<std:linkPopup id="edit_article_${cpt}" classStyle="btn btn-sm btn-primary" action="stock.composant.work_edit" params="art=${articleMvm.opc_article.id }" noJsValidate="true">
									<span class="fa  fa-eye"></span>
								</std:linkPopup>
							</td>
							<td style="padding-top: 5px; padding-right: 30px;" valign="top">
								<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width: 100px;" name="quantite_${cpt}" type="decimal" placeholderKey="mouvement.quantite" value="${articleMvm.quantite }"  maxlength="14"/>
							</td>
							<td valign="top" style="padding-top: 5px; padding-right: 1px;">
								<std:link actionGroup="M" id="delete_cont" icon="fa fa-times" style="color: red;"></std:link>
							</td>
						</tr>

						<c:set var="cpt" value="${cpt+1 }" /> 
					</c:forEach>
				</table>
			</div>
		</div>
	</div>

			<hr>
			<div class="form-actions">
				<div class="row" style="text-align: center;" class="col-md-12" id="action-div">
					<std:link actionGroup="M" id="add_ctrl" tooltipKey="mouvement.article.ajouter" icon="fa-1x fa-plus" classStyle="btn btn-default" />
					<std:button actionGroup="M" classStyle="btn btn-success" action="stock.demandeTransfert.work_merge" workId="${demandeTransfert.id }" icon="fa-save" value="Sauvegarder" />
					<std:button actionGroup="D" classStyle="btn btn-danger" action="stock.demandeTransfert.work_delete" workId="${demandeTransfert.id }" icon="fa-trash-o" value="Supprimer" />
					</div>
				</div>
	</std:form>
</div>			
