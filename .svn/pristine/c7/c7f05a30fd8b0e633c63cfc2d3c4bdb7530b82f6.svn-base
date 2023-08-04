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
		
		
		$("#type_transformation").change(function(){
			manageTypeTransformation();
			submitAjaxForm('<%=EncryptionUtil.encrypt("stock.transformation.refreshMode")%>', null, $("#data-form"), $("#trigger_type"));
		});
		
		$(document).off('click', '#add_ctrl_comp');
		$(document).on('click', '#add_ctrl_comp', function(){
			var contentTr = $("#ctrl_gpt_comp").html();
			
			var cpt = $("select[id^='composant_']").length + 1;
			contentTr = (contentTr ? contentTr.replace(new RegExp("_0", 'g'), "_"+ cpt) : null); 
			
			if(contentTr != null){
				$("#ctrl_table_comp").append('<tr cpt="'+cpt+'">' + contentTr + '</tr>');
			}
			//
			$("select[id='composant_" + cpt + "']").select2({allowClear: true});
			 $("select[id='composant_" + cpt + "']").css("width", "100% !important");
		});
		
		$(document).off('click', '#add_ctrl');
		$(document).on('click', '#add_ctrl', function(){
			var contentTr = $("#ctrl_gpt").html();
			
			if($("select[id^='opc_article']").length > 0){
				var cpt = $("select[id^='opc_article']").length + 1;
				contentTr = (contentTr ? contentTr.replace(new RegExp("_0", 'g'), "_"+ cpt) : null); 
				
				if(contentTr != null){
					$("#ctrl_table").append('<tr cpt="'+cpt+'">' + contentTr + '</tr>');
				}
				//
				$("select[id='opc_article\\.id_" + cpt + "']").select2({allowClear: true});
				 $("select[id='opc_article\\.id_" + cpt + "']").css("width", "100% !important");
				 $("#edit_article_"+cpt).css('display', 'none');
			} else{
				var cpt = $("select[id^='prep_transfo']").length + 1;
				contentTr = (contentTr ? contentTr.replace(new RegExp("_0", 'g'), "_"+ cpt) : null);
				
				if(contentTr != null){
					$("#ctrl_table").append('<tr cpt="'+cpt+'">' + contentTr + '</tr>');
				}
				//
				$("select[id='prep_transfo\\.id_" + cpt + "']").select2({allowClear: true});
				 $("select[id='prep_transfo\\.id_" + cpt + "']").css("width", "80% !important");
			}
		});
		$(document).off('click', '#delete_cont');
		$(document).on('click', "#delete_cont", function() {
			$(this).closest("tr").remove();
		});
		
		$(document).off('change', 'select[id^="opc_article"]');
		$(document).on('change', "select[id^='opc_article']", function() {
			var idx = $(this).attr("id").substring($(this).attr("id").lastIndexOf("_")+1);
			if($("option:selected", this).attr("hiddenkey")){
				$('#edit_article_'+idx).attr("params", "isCmp=1&art="+$(this).val());
				$('#edit_article_'+idx).css('display', 'block');
			} else{
				$('#edit_article_'+idx).css('display', 'none');
			}
		});
		
		<% if(request.getAttribute("is_inv_prev") != null){	%>
		$("#ctrl_table").find("input, select").attr("disabled", "disabled").css("background-color", "#eee");
		$("#ctrl_table").find("a").remove();
		<%}%>
		
		setTimeout(function() {
			manageTypeTransformation();
		}, 100);
	});
	
	function manageTypeTransformation(){
		$('#add_ctrl_comp').css('display', '');
		$('#add_ctrl_comp').css('display', '');
		if($("#type_transformation").val() == 'L'){
			$("#type_transformation").show();
		} else{
			$("#type_transformation").hide();
			if($("#type_transformation").val() == 'P'){
				$('#add_ctrl_comp').css('display', 'none');
			}
		}
	}
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
	
	<std:link targetDiv="div_detail_prep" id="trigger_type" style="display:none;" />
	
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
						<std:select name="mouvement.opc_emplacement.id" disable="${is_inv_prev?'true':'false' }" type="long" key="id" labels="titre" data="${listeDestination}" required="true" width="80%" />
						<std:link action="stock.emplacement.work_init_create" style="float: right;position: absolute;margin-left: 2%;" icon="fa fa-plus" noJsValidate="true" actionGroup="C" params="w_nos=1" />
					</div>
					<std:label classStyle="control-label col-md-2" value="Destination" />
					<div class="col-md-4">
						<std:select name="mouvement.opc_destination.id" disable="${is_inv_prev?'true':'false' }" type="long" key="id" labels="titre" data="${listeDestination}" required="true" width="80%" value="${mvmDestBean.opc_destination.id }"/>
						<std:link action="stock.emplacement.work_init_create" style="float: right;position: absolute;margin-left: 2%;" icon="fa fa-plus" noJsValidate="true" actionGroup="C" params="w_nos=1" />
					</div>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" value="Commentaire" />
					<div class="col-md-4">
						<std:textarea name="mouvement.commentaire" rows="3" cols="80" />
					</div>
				</div>
			</div>	
			
			<div class="form-group" id="type_div">
				<std:label classStyle="control-label col-md-2" value="Type de transformation" />
				<div class="col-md-4">
						<std:select name="type_transformation" type="String" addBlank="false" data="${type_transformation }" required="true" value="L" width="250px" />
				</div>
			</div>
			
		</div>
	</div>

	<div class="widget">
         <div class="widget-header bordered-bottom bordered-blue">
            <span class="widget-caption">D&eacute;tail de la transformation</span>
         </div>
         <div class="widget-body" id="div_detail_prep">
         	<jsp:include page="/domaine/stock/transformation_detart_include.jsp"></jsp:include>
		</div>
	</div>
			<hr>
			<div class="form-actions">
				<div class="row" style="text-align: center;" class="col-md-12" id="action-div">
				<% if(request.getAttribute("is_inv_prev") == null){	%>
						<std:link actionGroup="M" value="Entr&eacute;e" id="add_ctrl" tooltip="Ajouter" icon="fa-1x fa-plus" classStyle="btn btn-default" />
						<std:link actionGroup="M" value="Sortie" id="add_ctrl_comp" tooltip="Ajouter" icon="fa-1x fa-plus" classStyle="btn btn-default" />
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
