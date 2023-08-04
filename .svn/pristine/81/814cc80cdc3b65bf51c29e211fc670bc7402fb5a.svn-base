<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<script type="text/javascript">
	$(document).ready(function() {
		$(document).off('click', '#add_ctrl');
		$(document).on('click', '#add_ctrl', function(){
			var contentTr = $("#ctrl_gpt").html();
			var cpt = $("select[id^='opc_article']").length + 1;
			contentTr = contentTr.replace(new RegExp("_0", 'g'), "_"+ cpt);
			$("#ctrl_table").append('<tr cpt="'+cpt+'">' + contentTr + '</tr>');
			//
			$("select[id='opc_article\\.id_" + cpt + "']").select2({allowClear: true});
// 			$('#quantite_' + cpt).autoNumeric('init', {aSep : ' '});
			$(".select2-container").css("width", "100%");
		});
		$(document).off('click', '#delete_cont');
		$(document).on('click', "#delete_cont", function() {
			$(this).closest("tr").remove(); 
		});
		$("#generer_code").click(function(){
			executePartialAjax($(this), '<%=EncryptionUtil.encrypt("stock.preparation.genererCode")%>', 'preparation.code', true, true, null, true);
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
         <li>Fiche pr&eacute;paration</li>
         <li class="active">Edition</li>
     </ul>
 </div>
<!-- /Page Breadcrumb -->
  <!-- Page Header -->
  <div class="page-header position-relative">
      <div class="header-title" style="padding-top: 4px;">
          <std:link actionGroup="U" classStyle="btn btn-default" action="stock.preparation.work_init_update" workId="${preparation.id }" icon="fa-3x fa-pencil" tooltip="Modifier" />
		<std:link classStyle="btn btn-default" action="stock.preparation.work_find" params="bck=1" icon="fa fa-3x fa-mail-reply-all"
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
            <span class="widget-caption">Fiche pr&eacute;paration</span>
         </div>
         <div class="widget-body">
				<div class="row">
		
			<div class="form-group">
				<std:label classStyle="control-label col-md-2" valueKey="preparation.code"/>
				<div class="col-md-4">
					<std:text name="preparation.code" type="string" placeholderKey="preparation.code" required="true" maxlength="20" style="width:50%;float:left;" />
					<% if(ControllerUtil.isEditionCreateAction(request)){ %>
						<a class="refresh-num" id="generer_code" href="javascript:" title="G&eacute;n&eacute;rer un code">
				            <i class="fa fa-refresh"></i>
				        </a>
			        <% }%>
				</div>
				 <std:label classStyle="control-label col-md-2" valueKey="preparation.libelle"/>
				<div class="col-md-4">
					<std:text name="preparation.libelle" type="string" placeholderKey="preparation.libelle" maxlength="80" required="true" />
				</div>
			</div>
			<div class="form-group">
				 <std:label classStyle="control-label col-md-2" valueKey="preparation.commentaire" />
				<div class="col-md-10">
					<std:textarea name="preparation.commentaire" rows="5" cols="80" maxlength="255" />
				</div>
			</div>
		</div>
	</div>
</div>

	<!-- widget grid -->
	<div class="widget">
         <div class="widget-header bordered-bottom bordered-blue">
            <span class="widget-caption">D&eacute;tail des composants</span>
         </div>
         <div class="widget-body">
         <div class="row">
			<table id="ctrl_table" style="width: 100%;margin-left: 20px;">
				<tr>
					<th width="40%">Composant</th>
					<th width="14%">Quantit&eacute;</th>
					<th></th>
				</tr>
				<tr id="ctrl_gpt" style="display: none;">
					<td style="padding-top: 5px; padding-right: 10px;" valign="top">
						<std:select style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;" classStyle="slct" name="opc_article.id_0" type="long" key="id" labels="libelle" data="${listArticle}" />
					</td>
					<td style="padding-top: 5px; padding-right: 10px;" valign="top">
						<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;" name="quantite_0" type="decimal" placeholderKey="mouvement.quantite" />
					</td>
					<td valign="top" style="padding-top: 5px; padding-right: 1px;">
						<std:link actionGroup="M" id="delete_cont" icon="fa fa-times" style="color: red;"></std:link>
					</td>
				</tr>

				<c:set var="cpt" value="${1 }" />
				<c:set var="encodeService" value="<%=new EncryptionUtil()%>" />

				<c:forEach items="${preparation.list_article }" var="articleMvm">
					<std:hidden name="eaiid_${cpt}" value="${encodeService.encrypt(articleMvm.id) }" />
					<tr>
						<td style="padding-top: 5px; padding-right: 30px;" valign="top">
							<std:select style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;" name="opc_article.id_${cpt}" type="long" key="id" labels="libelle" data="${listArticle }" value="${articleMvm.opc_article.id }" />
						</td>
						<td style="padding-top: 5px; padding-right: 30px;" valign="top">
							<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;" name="quantite_${cpt}" type="decimal" placeholderKey="mouvement.quantite" value="${articleMvm.quantite }" />
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
			<div class="row" style="text-align: center;">
				<div class="col-md-12" id="action-div">
					 <std:link actionGroup="M" id="add_ctrl" tooltipKey="mouvement.article.ajouter" icon="fa-1x fa-plus" classStyle="btn btn-default" />
					<std:button actionGroup="M" classStyle="btn btn-success" action="stock.preparation.work_merge" workId="${preparation.id }" icon="fa-save"
						value="Sauvegarder" />
					<std:button actionGroup="D" classStyle="btn btn-danger" action="stock.preparation.work_delete" workId="${preparation.id }" icon="fa-trash-o"
						value="Supprimer" />
				</div>
			</div>
		</std:form>
	</div>
