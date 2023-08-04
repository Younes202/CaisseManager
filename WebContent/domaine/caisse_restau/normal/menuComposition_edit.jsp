<%@page import="appli.controller.domaine.caisse.bean.MenuCompositionBean"%> 
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="framework.model.common.util.ServiceUtil"%>
<%@page import="appli.model.domaine.stock.service.IArticleService"%>
<%@page import="framework.model.util.FileUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

 <script src="resources/framework/js/util_file_upload.js?v=1.1"></script>

<%
int menuLevel = (request.getAttribute("menuLevel")==null?0 : (Integer)request.getAttribute("menuLevel"));// Un seul menu par arbo
%>

<style>
#generic_modal_body{
	width: 750px;
}
</style>

<script type="text/javascript">
$(document).ready(function() {
	getTabElement("#generer_code").click(function(){
		executePartialAjax($(this), '<%=EncryptionUtil.encrypt("caisse.menuComposition.generer_code")%>', 'menuComposition_code', true, true, null, true);
	});
	
	<%if(ControllerUtil.isEditionWritePage(request)){%>
		loadInputFileEvents();
	<% } else{%>
	getTabElement("#sep_photo1").remove();
	<%}%>
	
	<%
	MenuCompositionBean menuBean = (MenuCompositionBean)request.getAttribute("menuComposition");
	if(menuBean != null && menuBean.getId() != null){ 
		IArticleService service = (IArticleService)ServiceUtil.getBusinessBean(IArticleService.class); 
		Map<String, byte[]> dataimg = service.getDataImage(menuBean.getId(), "menu");
		if(dataimg.size() > 0){
			String fileName = dataimg.entrySet().iterator().next().getKey();
			byte[] value = dataimg.entrySet().iterator().next().getValue();
		%>
			getTabElement("#photo1_div").css("background", "");
			getTabElement("#photo1_div").html("<img src='data:image/jpeg;base64,<%=FileUtil.getByte64(value) %>' width='120' height='120'/>");
	 		getTabElement("#photo1_name_span").text("<%=fileName%>");
	 		getTabElement("#photo1_name").val("<%=fileName%>");
		<%}
		} %>

	$(document).off('click', '#add_ctrl').on('click', '#add_ctrl', function(){
		var contentTr = getTabElement("#ctrl_gpt").html();
		var cpt = getTabElement("input[id^='quantite']").length + 1;
		contentTr = contentTr.replace(new RegExp("_0", 'g'), "_"+ cpt);
		getTabElement("#ctrl_table").append('<tr cpt="'+cpt+'">' + contentTr + '</tr>');
		//
		getTabElement("select[id='opc_article\\.id_" + cpt + "']").select2({allowClear: true});
		getTabElement(".select2-container").css("width", "290px");
	});
	$(document).off('click', '#delete_cont').on('click', "#delete_cont", function() {
		$(this).closest("tr").remove();
	});
	
	// Article include -----------------------------------------------
	$(document).off('click', '#add_ctrl_inc').on('click', '#add_ctrl_inc', function(){
		var contentTr = $("#ctrl_gpt_inc").html();
		var cpt = getTabElement("input[id^='quantite']").length + 1;
		contentTr = contentTr.replace(new RegExp("_0", 'g'), "_"+ cpt);
		getTabElement("#ctrl_table").append('<tr cpt="'+cpt+'">' + contentTr + '</tr>');
		//
		getTabElement("select[id='opc_article_inc\\.id_" + cpt + "']").select2({allowClear: true});
		getTabElement(".select2-container").css("width", "290px");
	});
	$(document).off('click', '#delete_cont_inc').on('click', "#delete_cont_inc", function() {
		$(this).closest("tr").remove();
	});

	// Article destock -----------------------------------------------
	$(document).off('click', '#add_ctrl_destock').on('click', '#add_ctrl_destock', function(){
		var contentTr = getTabElement("#ctrl_gpt_destock").html();
		var cpt = getTabElement("input[id^='quantite']").length + 1;
		contentTr = contentTr.replace(new RegExp("_z", 'g'), "_"+ cpt);
		getTabElement("#ctrl_table").append('<tr cpt="'+cpt+'">' + contentTr + '</tr>');
		//
		getTabElement("select[id='opc_article_destock\\.id_" + cpt + "']").select2({allowClear: true});
		getTabElement(".select2-container").css("width", "290px");
	});
	$(document).off('click', '#delete_cont_destock').on('click', "#delete_cont_destock", function() {
		$(this).closest("tr").remove();
	});
	
	// Liste de choix -----------------------------------------------
	$(document).off('click', '#add_ctrl_choix').on('click', '#add_ctrl_choix', function(){
		var contentTr = $("#ctrl_gpt_choix").html();
		var cpt = getTabElement("input[id^='quantite']").length + 1;
		contentTr = contentTr.replace(new RegExp("_x", 'g'), "_"+ cpt);
		getTabElement("#ctrl_table").append('<tr cpt="'+cpt+'">' + contentTr + '</tr>');
		//
		getTabElement("select[id='opc_list_choix\\.id_" + cpt + "']").select2({allowClear: true});
		getTabElement(".select2-container").css("width", "290px");
	});
	$(document).off('click', '#delete_cont_choix').on('click', "#delete_cont_choix", function() {
		$(this).closest("tr").remove();
	});
	
	// Liste des familles -----------------------------------------------
	$(document).off('click', '#add_ctrl_fam').on('click', '#add_ctrl_fam', function(){
		var contentTr = getTabElement("#ctrl_gpt_fam").html();
		var cpt = getTabElement("input[id^='quantite']").length + 1;
		contentTr = contentTr.replace(new RegExp("_y", 'g'), "_"+ cpt);
		getTabElement("#ctrl_table").append('<tr cpt="'+cpt+'">' + contentTr + '</tr>');
		//
		getTabElement("select[id='opc_famille\\.id_" + cpt + "']").select2({allowClear: true});
		getTabElement(".select2-container").css("width", "290px");
	});
	$(document).off('click', '#delete_cont_fam').on('click', "#delete_cont_fam", function() {
		$(this).closest("tr").remove();
	});
	
	
	$(document).off('change', 'select[id^="opc_article"]').on('change', "select[id^='opc_article']", function() {
		var idx = $(this).attr("id").substring(15);
		if($("option:selected", this).attr("hiddenkey")){
			var dataArray = $("option:selected", this).attr("hiddenkey").split('|');
			if(dataArray[0] != null && dataArray[0] != 'null'){
				getTabElement("#prix_"+idx).val(dataArray[0]);
			}
		}
	});
	
	$(document).off('change', "input[id^='prix_']").on('change', "input[id^='prix_']", function(){
		if($(this).val() != ''){
			getTabElement("#menuComposition\\.mtt_prix").val('');
		}
	});
// 	$("#menuComposition\\.mtt_prix").change(function(){
// 		$("input[id^='prix_']").val('');
// 		if($(this).val() == ''){
// 			$("input[id^='prix_']").removeAttr("readonly", "readonly")
// 		} else{
// 			$("input[id^='prix_']").attr("readonly", "readonly")
// 		}
// 	});
});
</script>

<std:form name="data-form">
	<input type="hidden" name="images_uploaded" id="images_uploaded" value=";">
	<input type="hidden" name="images_names" id="images_names" value=";">
	<input type="hidden" name="MAX_FILE_SIZE" value="2097152" />
	
	<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">Fiche menu</span>
			<c:if test="${empty isEditable or isEditable }">
				<std:link actionGroup="U" targetDiv="generic_modal_body" classStyle="btn btn-default" action="caisse.menuComposition.work_init_update" workId="${menuComposition.id}" icon="fa fa-pencil"
					tooltip="Modifier" />
				</c:if>	
			<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
				<i class="fa fa-times"></i> Fermer
			</button>
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<c:if test="${parent_level > 0 }">
				<div class="row">
					<c:if test="${not empty menuComposition.id }">
						<std:label classStyle="control-label col-md-2" value="Parent" />
						<div class="col-md-6" style="margin-left: -13px;">
							<std:select name="menuComposition.parent_id" type="long" key="id" labels="code;'-';libelle" data="${listMenu}" required="true" isTree="true" width="100%" value="${parent_menu }"/>
						</div>
					</c:if>
					
					<%if(menuLevel == 2){ %>
						<std:label classStyle="control-label col-md-2" value="Est un menu" />
						<div class="col-md-2" style="margin-left: -13px;">
							<std:checkbox name="menuComposition.is_menu" />
							<img class="tooltip-lg" data-toggle="tooltip" data-placement="buttom" data-original-title="Si ce niveau crrespond &agrave; la racine du menu propos&eacute;, alors cocher cette case" src="resources/framework/img/info.png" style="vertical-align: bottom;"/>
						</div>
					<%} %>
				</div>
			</c:if>
			<div class="row" style="margin-top: 9px;">
				<!-- Formulaire de saisie de type famille -->
				<input type="hidden" name="fam_worksys" value="${fam }">

				<div class="form-group">
					<std:label classStyle="control-label col-md-2" value="Code" />	
					<div class="col-md-3">
						<std:text name="menuComposition_code" type="string" placeholder="Code" required="true" style="width: 100px;width: 100px;float: left;" maxlength="20" />
						<% if(ControllerUtil.isEditionCreateAction(request)){ %>
							<a class="refresh-num" id="generer_code" href="javascript:" title="G&eacute;n&eacute;rer un nouveau code">
				            	<i class="fa fa-refresh"></i>
				        	</a>
			        	<% }%>
					</div>
					<div class="col-md-7">
						<std:text name="menuComposition.libelle" type="string" placeholderKey="famille.libelle" required="true" maxlength="80" />
					</div>
				</div>
			</div>
	<c:if test="${parent_level > 0 }">		
			<div class="row">
				<std:label classStyle="control-label col-md-2" value="Prix de vente" />
				<div class="col-md-4">
					<std:text name="menuComposition.mtt_prix" type="decimal" placeholder="Prix" maxlength="16" style="width:80px;" />
				</div>
				<std:label classStyle="control-label col-md-2" value="Max articles" />
				<div class="col-md-4">
					<std:text name="menuComposition.nombre_max" type="long" placeholder="Max" required="true" style="width: 80px;float:left;"/>
					<img class="tooltip-lg" data-toggle="tooltip" data-placement="top" data-original-title="Le nombre maximal d'articles &agrave; choisir parmis les composantes ci-dessous. 0=pas de limite" src="resources/framework/img/info.png" style="vertical-align: bottom;"/>
				</div>
			</div>
	</c:if>		
			<div class="row">	
				<std:label classStyle="control-label col-md-2" value="Image" />
				<div class="col-md-4">
					<div class="col-sm-12">	
						<div id="photo1_div" style="width: 100px;margin-top:10px; border: 1px solid #c6d983; border-radius: 25px; text-align: center; height: 90px; background: url('resources/framework/img/picture_file.png') no-repeat center;cursor: pointer;">
							<span style="font-size: 11px;">Image</span>
						</div>
						<span id="photo1_name_span"></span>
						<input type="hidden" name="photo1_name" id="photo1_name">
					</div>	
					<div class="col-sm-12">
						<!-- Separator -->
						<div id="sep_photo1" style="width:100px;margin-bottom: 5px; height: 20px; text-align: center;">
							<a href="javascript:"><b>X</b></a>
						</div>
						<!-- End -->
						<input type="file" name="photo1" id="photo1_div_file" style="display: none;" accept="image/bmp,image/gif,image/jpg,image/png,image/jpeg">
					</div>
				</div>
				<std:label classStyle="control-label col-md-2" value="Caisses destination" />
				<div class="col-md-4" style="margin-top: 15px;">
					<std:select name="menuComposition.caisses_target" multiple="true" width="160" type="string[]" key="id" labels="reference" data="${listeCaisse}" value="${caisseArray }" style="float:left;"/>
					<img class="tooltip-lg" data-toggle="tooltip" data-placement="top" data-original-title="Laissez ce champs vide si l'article doit s'afficher dans toutes les caisses" src="resources/framework/img/info.png" style="vertical-align: bottom;"/>
				</div>
			</div>
			<hr>
			<div class="row">
				<div class="col-sm-12">
					<span style="font-size: 10px;color:red;font-weight: bold;">0</span> <span style="font-size: 10px;color:orange;">dans quantit&eacute; ou nombre maximun = Pas de limite</span>
				</div>	
			</div>
	
		
<c:set var="encodeService" value="<%=new EncryptionUtil()%>" />

<% if(ControllerUtil.isEditionWritePage(request)){ %>
		<div class="form-title">
			<std:link actionGroup="M" id="add_ctrl" tooltipKey="article.articleDetail.ajouter" icon="fa-1x fa-plus" classStyle="btn btn-default" />
			Article
			
			<std:link actionGroup="M" id="add_ctrl_choix" style="margin-left: 20px;" tooltip="Ajouter une liste de choix" icon="fa-1x fa-plus" classStyle="btn btn-default" />
			Liste de choix
			
			<std:link actionGroup="M" id="add_ctrl_fam" style="margin-left: 20px;" tooltip="Ajouter une famille" icon="fa-1x fa-plus" classStyle="btn btn-default" />
			Famille
			
			<std:link actionGroup="M" id="add_ctrl_inc" style="margin-left: 20px;" tooltipKey="article.articleDetail.ajouter" icon="fa-1x fa-plus" classStyle="btn btn-default" />
			Article Inc
			<img class="tooltip-lg" data-toggle="tooltip" data-placement="top" data-original-title="Articles &agrave; inclure dans la commande sans les séléctionner en caisse." src="resources/framework/img/info.png" style="vertical-align: bottom;"/>
			
			<std:link actionGroup="M" id="add_ctrl_destock" style="margin-left: 20px;" tooltipKey="article.articleDetail.ajouter" icon="fa-1x fa-plus" classStyle="btn btn-default" />
			Destock 
			<img class="tooltip-lg" data-toggle="tooltip" data-placement="top" data-original-title="Articles &agrave; destocker automatiquement sans les afficher dans la caisse." src="resources/framework/img/info.png" style="vertical-align: bottom;"/>
		</div>
<%} %>

<div style="overflow-y: auto;overflow-x: hidden;max-height: 400px;" class="row">
		<div class="form-group" style="margin-left: 0px;">
			<table id="ctrl_table" style="width: 97%;margin-left: 10px;">
				<tr>
					<td></td>
					<td style="font-size: 12px;color: #009688;">Quantit&eacute;</td>
					<td style="font-size: 12px;color: #009688;">Prix</td>
					<td style="font-size: 12px;color: #009688;">Max</td>
				</tr>
				<tr id="ctrl_gpt" style="display: none;">
					<td style="padding-top: 5px; padding-right: 10px;" valign="top">
						<std:select style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width:290px;" hiddenkey="prix_vente" classStyle="slct" name="opc_article.id_0" type="long" key="id" labels="libelle" data="${listArticle}" groupKey="famille_cuisine_id" groupLabels="opc_famille_cuisine.code;'-';opc_famille_cuisine.libelle" />(A)
					</td>
					<td style="padding-top: 5px; padding-right: 10px;" valign="top">
						<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width:80px;" name="quantite_0" type="decimal" placeholderKey="article.quantite"  maxlength="14"/>
					</td>
					<td style="padding-top: 5px; padding-right: 10px;" valign="top">
						<std:text style="width:80px;" name="prix_0" type="decimal" placeholder="Prix"  maxlength="14"/>
					</td>
					<td style="padding-top: 5px; padding-right: 10px;" valign="top">
						<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width:80px;" name="nombre_0" type="long" placeholder="Max" value="0"/>
					</td>
					<td valign="top" style="padding-top: 5px; padding-right: 1px;">
						<std:link actionGroup="M" id="delete_cont" icon="fa fa-times" style="color: red;"></std:link>
					</td>
				</tr>
				<tr id="ctrl_gpt_inc" style="display: none;">
					<td style="padding-top: 5px; padding-right: 10px;" valign="top">
						<std:select style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width:290px;" hiddenkey="prix_vente" classStyle="slct" name="opc_article_inc.id_0" type="long" key="id" labels="libelle" data="${listArticle}" groupKey="famille_cuisine_id" groupLabels="opc_famille_cuisine.code;'-';opc_famille_cuisine.libelle" />(A)
					</td>
					<td style="padding-top: 5px; padding-right: 10px;" valign="top">
						<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width:80px;" name="quantite_0" type="decimal" placeholderKey="article.quantite"  maxlength="14"/>
					</td>
					<td style="padding-top: 5px; padding-right: 10px;" valign="top">
						<std:text style="width:80px;" name="prix_0" type="decimal" placeholder="Prix"  maxlength="14"/>
					</td>
					<td style="padding-top: 5px; padding-right: 10px;" valign="top">
						
					</td>
					<td valign="top" style="padding-top: 5px; padding-right: 1px;">
						<std:link actionGroup="M" id="delete_cont" icon="fa fa-times" style="color: red;"></std:link>
					</td>
				</tr>
				<tr id="ctrl_gpt_destock" style="display: none;">
					<td style="padding-top: 5px; padding-right: 10px;" valign="top">
						<std:select style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width:290px;" hiddenkey="prix_vente" classStyle="slct" name="opc_article_destock.id_z" type="long" key="id" labels="libelle" data="${listArticleStock}" groupKey="opc_famille_stock.id" groupLabels="opc_famille_stock.code;'-';opc_famille_stock.libelle" />(D)
					</td>
					<td style="padding-top: 5px; padding-right: 10px;" valign="top">
						<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width:80px;" name="quantite_z" type="decimal" placeholderKey="article.quantite"  maxlength="14"/>
					</td>
					<td style="padding-top: 5px; padding-right: 10px;" valign="top">
					</td>
					<td style="padding-top: 5px; padding-right: 10px;" valign="top">
					</td>
					<td valign="top" style="padding-top: 5px; padding-right: 1px;">
						<std:link actionGroup="M" id="delete_cont" icon="fa fa-times" style="color: red;"></std:link>
					</td>
				</tr>				
				<tr id="ctrl_gpt_choix" style="display: none;">
					<td style="padding-top: 5px; padding-right: 10px;" valign="top">
						<std:select style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width:290px;" classStyle="slct" name="opc_list_choix.id_x" type="long" key="id" labels="libelle" data="${listChoix}" />(C)
					</td>
					<td style="padding-top: 5px; padding-right: 10px;" valign="top">
						<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width:80px;" name="quantite_x" type="decimal" placeholderKey="article.quantite"  maxlength="14"/>
					</td>
					<td style="padding-top: 5px; padding-right: 10px;" valign="top">
						<std:text style="width:80px;" name="prix_x" type="decimal" placeholder="Prix"  maxlength="14"/>
					</td>
					<td style="padding-top: 5px; padding-right: 10px;" valign="top">
						<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width:80px;" name="nombre_x" type="long"  placeholder="Max" value="0"/>
					</td>
					<td valign="top" style="padding-top: 5px; padding-right: 1px;">
						<std:link actionGroup="M" id="delete_cont_choix" icon="fa fa-times" style="color: red;"></std:link>
					</td>
				</tr>
				<tr id="ctrl_gpt_fam" style="display: none;">
					<td style="padding-top: 5px; padding-right: 10px;" valign="top">
						<std:select style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width:290px;" classStyle="slct" name="opc_famille.id_y" type="long" key="id" labels="libelle" data="${listFamille}" isTree="true" />(F)
					</td>
					<td style="padding-top: 5px; padding-right: 10px;" valign="top">
						<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width:80px;" name="quantite_y" type="decimal" placeholderKey="article.quantite"  maxlength="14"/>
					</td>
					<td style="padding-top: 5px; padding-right: 10px;" valign="top">
						<std:text style="width:80px;" name="prix_y" type="decimal" placeholder="Prix"  maxlength="14"/>
					</td>
					<td style="padding-top: 5px; padding-right: 10px;" valign="top">
						<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width:80px;" name="nombre_y" type="long" placeholder="Max" value="0"/>
					</td>
					<td valign="top" style="padding-top: 5px; padding-right: 1px;">
						<std:link actionGroup="M" id="delete_cont_fam" icon="fa fa-times" style="color: red;"></std:link>
					</td>
				</tr>

				<c:set var="cpt" value="${1 }" />
				<c:forEach items="${menuComposition.list_composition }" var="articleDet">
					<std:hidden name="eaiid_${cpt}" value="${encodeService.encrypt(articleDet.id) }" />
						<tr>
						<td style="padding-top: 5px; padding-right: 30px;" valign="top">
						<c:choose>
							<c:when test="${articleDet.opc_article.id != null }">								
								<std:select style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width:290px;" hiddenkey="prix_vente" name="opc_article.id_${cpt}" type="long" key="id" labels="libelle" data="${listArticle }" value="${articleDet.opc_article.id }" groupKey="famille_cuisine_id" groupLabels="opc_famille_cuisine.code;'-';opc_famille_cuisine.libelle" />(ARTICLE)
							</c:when>
							<c:when test="${articleDet.opc_article_inc.id != null }">								
								<std:select style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width:290px;" hiddenkey="prix_vente" name="opc_article_inc.id_${cpt}" type="long" key="id" labels="libelle" data="${listArticle }" value="${articleDet.opc_article_inc.id }" groupKey="famille_cuisine_id" groupLabels="opc_famille_cuisine.code;'-';opc_famille_cuisine.libelle" />(INCLUS)
							</c:when>
							<c:when test="${articleDet.opc_article_destock.id != null }">								
								<std:select style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width:290px;" hiddenkey="prix_vente" name="opc_article_destock.id_${cpt}" type="long" key="id" labels="libelle" data="${listArticleStock }" value="${articleDet.opc_article_destock.id }" groupKey="opc_famille_stock.id" groupLabels="opc_famille_stock.code;'-';opc_famille_stock.libelle" />(STOCK)
							</c:when>
							<c:when test="${articleDet.opc_list_choix.id != null }">
								<std:select style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width:290px;" name="opc_list_choix.id_${cpt}" type="long" key="id" labels="libelle" data="${listChoix }" value="${articleDet.opc_list_choix.id }" />(CHOIX)
							</c:when>
							<c:when test="${articleDet.opc_famille.id != null }">
								<std:select style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width:290px;" name="opc_famille.id_${cpt}" type="long" key="id" labels="libelle" data="${listFamille }" value="${articleDet.opc_famille.id }" isTree="true" />(FAMILLE)
							</c:when>	
						</c:choose> 
						</td>
							<td style="padding-top: 5px; padding-right: 30px;" valign="top">
								<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width:80px;" name="quantite_${cpt}" type="decimal" placeholderKey="article.quantite" value="${articleDet.quantite }"  maxlength="14"/>
							</td>
							<td style="padding-top: 5px; padding-right: 30px;" valign="top">
							<c:if test="${articleDet.opc_article_destock.id == null }">
								<std:text style="width:80px;" name="prix_${cpt}" type="decimal" placeholder="Prix" value="${articleDet.prix }"  maxlength="14"/>
							</c:if>	
							</td>
							<td style="padding-top: 5px; padding-right: 30px;" valign="top">
							<c:if test="${articleDet.opc_article_destock.id == null and articleDet.opc_article_inc.id == null }">
								<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width:80px;" name="nombre_${cpt}" type="long"  value="${articleDet.nombre }" placeholder="Nombre" />
							</c:if>	
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
		<div class="modal-footer text-center">
			<std:button actionGroup="M" classStyle="btn btn-success" action="caisse.menuComposition.work_merge" workId="${menuComposition.id }" params="mode=${is_dup==1?'dup':'' }" icon="fa-save" value="Sauvegarder" />
			<std:button actionGroup="D" classStyle="btn btn-danger" action="caisse.menuComposition.work_delete" workId="${menuComposition.id }" icon="fa-trash-o" value="Supprimer" />
		</div>

	</div>

</std:form>

<script type="text/javascript">
/*Handles ToolTips*/
$("[data-toggle=tooltip]")
    .tooltip({
        html: true
    });
</script>    