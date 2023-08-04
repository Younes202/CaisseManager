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
		$(document).off('click', '#add_ctrl');
		$(document).on('click', '#add_ctrl', function(){
			var contentTr = $("#ctrl_gpt").html();
			var cpt = $("select[id^='opc_composant']").length + 1;
			contentTr = (contentTr ? contentTr.replace(new RegExp("_0", 'g'), "_"+ cpt) : null);
			
			if(contentTr != null){
				$("#ctrl_table").append('<tr cpt="'+cpt+'">' + contentTr + '</tr>');
			}
			//
			$("select[id='opc_composant\\.id_" + cpt + "']").select2({allowClear: true});
			 $("select[id='opc_composant\\.id_" + cpt + "']").css("width", "100% !important");
			 $("#edit_article_"+cpt).css('display', 'none');
		});
		$(document).off('click', '#delete_cont');
		$(document).on('click', "#delete_cont", function() {
			$(this).closest("tr").remove();
		});
		
		$(document).off('change', 'select[id^="opc_composant"]');
		$(document).on('change', "select[id^='opc_composant']", function() {
			var idx = $(this).attr("id").substring($(this).attr("id").lastIndexOf("_")+1);
			if($("option:selected", this).attr("hiddenkey")){
				$('#edit_article_'+idx).attr("params", "isCmp=1&art="+$(this).val());
				$('#edit_article_'+idx).css('display', 'block');
			} else{
				$('#edit_article_'+idx).css('display', 'none');
			}
		});
		$("#generer_code").click(function(){
			executePartialAjax($(this), '<%=EncryptionUtil.encrypt("stock.preparationTransfo.genererCode")%>', 'preparationTransfo.code', true, true, null, true);
		});
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
         <li>Fiche pr&eacute;paration transformation
		</li>
         <li class="active">Edition</li>
     </ul>
 </div>
<!-- /Page Breadcrumb -->
  <!-- Page Header -->
  <div class="page-header position-relative">
      <div class="header-title" style="padding-top: 4px;">
          <std:link actionGroup="U" classStyle="btn btn-default" action="stock.preparationTransfo.work_init_update" workId="${preparationTransfo.id }" icon="fa-3x fa-pencil" tooltip="Modifier" />
		  <std:link classStyle="btn btn-default" action="stock.preparationTransfo.work_find" params="bck=1" icon="fa fa-3x fa-mail-reply-all"
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
	<!-- widget grid -->
	<div class="widget">
         <div class="widget-header bordered-bottom bordered-blue">
            <span class="widget-caption">Fiche pr&eacute;paration transformation</span>
         </div>
         <div class="widget-body">
			<div class="row">
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" value="Code"/>
					<div class="col-md-4">
						<std:text name="preparationTransfo.code" type="string" placeholder="Code" required="true" maxlength="20" style="width: 150px;float: left;" />
						<% if(ControllerUtil.isEditionCreateAction(request)){ %>
							<a class="refresh-num" id="generer_code" href="javascript:" title="G&eacute;n&eacute;rer un code">
					            <i class="fa fa-refresh"></i>
					        </a>
				        <% }%>
					</div>
					 <std:label classStyle="control-label col-md-2" value="Libell&eacute;"/>
					<div class="col-md-4">
						<std:text name="preparationTransfo.libelle" type="string" placeholder="Libell&eacute;" maxlength="80" required="true" />
					</div>
				</div>
				<div class="form-group">
					 <std:label classStyle="control-label col-md-2" value="Commentaire" />
					<div class="col-md-10">
						<std:textarea name="preparationTransfo.commentaire" rows="5" cols="80" maxlength="255" />
					</div>
				</div>
			</div>	
		</div>
	</div>

	<div class="widget" id="div_detail">
         <div class="widget-header bordered-bottom bordered-blue">
            <span class="widget-caption">Composants &agrave; transformer</span>
            <span style="margin-left: 50%;" class="widget-caption">En une unité de </span>
         </div>
         <div class="widget-body">
				<div class="row">
					<div class="col-md-6" style="border-right: 1px solid red;">
					<table id="ctrl_table" style="width: 97%;margin-left: 20px;">
						<tr style="border-bottom: 1px solid #2196f3;">
							<th>Composant</th>
							<th width="60px"></th>
							<th width="100px">Quantit&eacute; <i class="fa fa-info-circle" title="Quantité nécessaire pour produire une seule unité du composant en sortie"></i></th>
							<th width="50px"></th>
						</tr>
						<tr id="ctrl_gpt" style="display: none;">
							<td style="padding-top: 5px; padding-right: 10px;" valign="top">
								<std:select style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important; width: 100% !important;" classStyle="slct" name="opc_composant.id_0" hiddenkey="prix_achat_ht;opc_tva_enum.id" type="long" key="id" labels="code;'-';libelle;' [';prix_achat_moyen_ttc;']'" data="${listComposant}"  groupKey="opc_famille_stock.id" groupLabels="opc_famille_stock.code;'-';opc_famille_stock.libelle"/>
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
						
						<c:forEach items="${preparationTransfo.list_composant }" var="articleMvm">
							<std:hidden name="eaiid_${cpt}" value="${encodeService.encrypt(articleMvm.id) }" />
							<tr>
								<td style="padding-top: 5px; padding-right: 30px;" valign="top">
									<std:select style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;; width: 100% !important;" name="opc_composant.id_${cpt}" type="long" key="id" labels="code;'-';libelle;' [';prix_achat_moyen_ttc;']'" data="${listComposant }" value="${articleMvm.opc_composant.id }" groupKey="opc_famille_stock.id" groupLabels="opc_famille_stock.code;'-';opc_famille_stock.libelle" />
								</td>
								<td style="padding-top: 5px; padding-right: 10px;" valign="top">
									<std:linkPopup id="edit_article_${cpt}" classStyle="btn btn-sm btn-primary" action="stock.composant.work_edit" params="art=${articleMvm.opc_composant.id }&isCmp=1" noJsValidate="true"> 
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
					<div class="row" style="margin-top: 10px;">
						<std:label classStyle="control-label col-md-3" value="Fiche composant" addSeparator="false" />
						<std:hidden name="preparationTransfo.opc_composant_target.id"/>
						<div class="col-md-9">
							<div id="div_sel">
								<std:select name="preparationTransfo.opc_composant_target.opc_composant.id" type="long" key="id" labels="code;'-';libelle" data="${listComposantF}" width="100%" groupKey="opc_famille_stock.id" groupLabels="opc_famille_stock.code;'-';opc_famille_stock.libelle" />
							</div>
							<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width: 100%;display:none;" name="new_compo" type="string" placeholder="Libell&eacute;" maxlength="120"/>
							<a style="font-size: 10px;color: blue;" href="javascript:" onclick="$('#new_compo').val('').toggle(100);$('#div_sel').toggle();">Ajouter nouveau composant</a>
						</div>
<!-- 						<div class="col-md-3"> -->
<%-- 							<std:text required="true" name="preparationTransfo.opc_composant_target.quantite" type="decimal" placeholder="Quantit&eacute;" maxlength="14" /> --%>
<!-- 						</div>	 -->
					</div>	
				</div>
		</div>
	</div>
</div>
			<hr>
			<div class="form-actions">
				<div class="row" style="text-align: center;" class="col-md-12" id="action-div">
						<std:link actionGroup="M" id="add_ctrl" tooltip="Ajouter un article" icon="fa-1x fa-plus" classStyle="btn btn-default" />
						<std:button actionGroup="M" classStyle="btn btn-success" action="stock.preparationTransfo.work_merge" workId="${preparationTransfo.id }" icon="fa-save"
							value="Sauvegarder" />
						<std:button actionGroup="D" classStyle="btn btn-danger" action="stock.preparationTransfo.work_delete" workId="${preparationTransfo.id }" icon="fa-trash-o"
							value="Supprimer" />
					</div>
				</div>
	</std:form>
</div>			
