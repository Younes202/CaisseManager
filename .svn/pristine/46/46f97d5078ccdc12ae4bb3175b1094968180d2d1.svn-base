<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
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
		var cpt = $("input[id^='quantite']").length + 1;
		contentTr = contentTr.replace(new RegExp("_0", 'g'), "_"+ cpt);
		$("#ctrl_table").append('<tr cpt="'+cpt+'">' + contentTr + '</tr>');
		//
		$("select[id='opc_article\\.id_" + cpt + "']").select2({allowClear: true});
		 $(".select2-container").css("width", "200px");
	});
	$(document).off('click', '#delete_cont');
	$(document).on('click', "#delete_cont", function() {
		$(this).closest("tr").remove();
	});
	
	// Liste de choix -----------------------------------------------
	$(document).off('click', '#add_ctrl_choix');
	$(document).on('click', '#add_ctrl_choix', function(){
		var contentTr = $("#ctrl_gpt_choix").html();
		var cpt = $("input[id^='quantite']").length + 1;
		contentTr = contentTr.replace(new RegExp("_x", 'g'), "_"+ cpt);
		$("#ctrl_table").append('<tr cpt="'+cpt+'">' + contentTr + '</tr>');
		//
		$("select[id='opc_list_choix\\.id_" + cpt + "']").select2({allowClear: true});
		 $(".select2-container").css("width", "200px");
	});
	$(document).off('click', '#delete_cont_choix');
	$(document).on('click', "#delete_cont_choix", function() {
		$(this).closest("tr").remove();
	});
	
	// Liste des familles -----------------------------------------------
	$(document).off('click', '#add_ctrl_fam');
	$(document).on('click', '#add_ctrl_fam', function(){
		var contentTr = $("#ctrl_gpt_fam").html();
		var cpt = $("input[id^='quantite']").length + 1;
		contentTr = contentTr.replace(new RegExp("_y", 'g'), "_"+ cpt);
		$("#ctrl_table").append('<tr cpt="'+cpt+'">' + contentTr + '</tr>');
		//
		$("select[id='opc_famille\\.id_" + cpt + "']").select2({allowClear: true});
		 $(".select2-container").css("width", "200px");
	});
	$(document).off('click', '#delete_cont_fam');
	$(document).on('click', "#delete_cont_fam", function() {
		$(this).closest("tr").remove();
	});
	$("#generer_code").click(function(){
		executePartialAjax($(this), '<%=EncryptionUtil.encrypt("caisse.listChoix.genererCode")%>', 'listChoix.code', true, true, null, true);
	});
});
</script>

<std:form name="data-form">
	<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">Fiche liste de choix</span>
			
			<c:if test="${empty isEditable or isEditable }">
				<std:link actionGroup="U" targetDiv="generic_modal_body" classStyle="btn btn-default" action="caisse.listChoix.work_init_update" workId="${listChoix.id}" icon="fa fa-pencil"
					tooltip="Cr&eacute;er" />
			</c:if>		
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row">
				<!-- Formulaire de saisie de type famille -->
				<input type="hidden" name="fam_worksys" value="${fam }">

				<div class="form-group">
					<std:label classStyle="control-label col-md-2" value="Code" />
					<div class="col-md-4">
						<std:text name="listChoix.code" type="string" placeholder="Code" required="true" maxlength="30" style="width:80px;float: left;"/>
						<% if(ControllerUtil.isEditionCreateAction(request)){ %>
							<a class="refresh-num" id="generer_code" href="javascript:" title="G&eacute;n&eacute;rer un code">
					            <i class="fa fa-refresh"></i>
					        </a>
				        <% }%>
					</div>
					<std:label classStyle="control-label col-md-2" valueKey="famille.libelle" />
					<div class="col-md-4">
						<std:text name="listChoix.libelle" type="string" placeholderKey="famille.libelle" required="true" maxlength="120" />
					</div>
				</div>
			</div>
			<div class="form-title">D&eacute;tail articles</div>
			
			
			
<% if(ControllerUtil.isEditionWritePage(request)){ %>
		<div class="form-title">
			<std:link actionGroup="M" id="add_ctrl" tooltipKey="article.articleDetail.ajouter" icon="fa-1x fa-plus" classStyle="btn btn-default" />
			Article
			<std:link actionGroup="M" id="add_ctrl_fam" style="margin-left: 10%;" tooltip="Ajouter une famille" icon="fa-1x fa-plus" classStyle="btn btn-default" />
			Famille
			<std:link actionGroup="M" id="add_ctrl_choix" style="margin-left: 10%;" tooltip="Ajouter une liste de choix" icon="fa-1x fa-plus" classStyle="btn btn-default" />
			Liste de choix
		</div>
<%} %>
			
			<div class="row" style="overflow-y: auto;overflow-x:hidden;max-height: 350px;">
				<table id="ctrl_table" style="width: 97%;margin-left: 10px;">
					<tr>
						<th width="60%"></th>
						<th width="20%">Quantit&eacute;</th>
						<th>Max</th>
						<th></th>
					</tr>
					<tr id="ctrl_gpt" style="display: none;">
						<td style="padding-top: 5px; padding-right: 10px;" valign="top">
							<std:select style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width:200px;" classStyle="slct" name="opc_article.id_0" type="long" key="id" labels="libelle" data="${listArticle}" groupKey="famille_cuisine_id" groupLabels="opc_famille_cuisine.code;'-';opc_famille_cuisine.libelle" width="200"/> (A)
						</td>
						<td style="padding-top: 5px; padding-right: 10px;" valign="top">
							<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width:80px;" name="quantite_0" type="decimal" placeholder="Quantit&eacute;" maxlength="14" value="1"/>
						</td>
						<td>
						
						</td>
						<td valign="top" style="padding-top: 3px; padding-right: 1px;">
							<std:link actionGroup="M" id="delete_cont" icon="fa fa-times" style="color: red;margin-left: 5px;"></std:link>
						</td>
					</tr>
					<tr id="ctrl_gpt_fam" style="display: none;">
						<td style="padding-top: 5px; padding-right: 10px;" valign="top">
							<std:select style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width:200px;" classStyle="slct" name="opc_famille.id_y" type="long" key="id" labels="libelle" data="${listFamille}" isTree="true" width="200"/> (F)
						</td>
						<td style="padding-top: 5px; padding-right: 10px;" valign="top">
							<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width:80px;" name="quantite_y" type="decimal" placeholder="Quantit&eacute;" maxlength="14" value="1"/>
						</td>
						<td style="padding-top: 5px; padding-right: 10px;" valign="top">
							<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width:80px;" name="nombre_y" type="long" placeholder="Max" />
						</td>
						<td valign="top" style="padding-top: 3px; padding-right: 1px;">
							<std:link actionGroup="M" id="delete_cont" icon="fa fa-times" style="color: red;margin-left: 5px;"></std:link>
						</td>
					</tr>
					<tr id="ctrl_gpt_choix" style="display: none;">
						<td style="padding-top: 5px; padding-right: 10px;" valign="top">
							<std:select style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width:200px;" classStyle="slct" name="opc_list_choix.id_x" type="long" key="id" labels="libelle" data="${listChoixAll}" isTree="true" width="200"/> (L)
						</td>
						<td style="padding-top: 5px; padding-right: 10px;" valign="top">
							<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width: 80px;" name="quantite_x" type="decimal" placeholder="Quantit&eacute;" maxlength="14" value="1"/>
						</td>
						<td style="padding-top: 5px; padding-right: 10px;" valign="top">
							<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width:80px;" name="nombre_x" type="long" placeholder="Max" />
						</td>
						<td valign="top" style="padding-top: 3px; padding-right: 1px;">
							<std:link actionGroup="M" id="delete_cont" icon="fa fa-times" style="color: red;margin-left: 5px;"></std:link>
						</td>
					</tr>
					
					<c:set var="cpt" value="${1 }" />
					<c:set var="encodeService" value="<%=new EncryptionUtil()%>" />
					<c:forEach items="${listChoix.list_choix_detail }" var="listChoixArt">
						<std:hidden name="eaiid_${cpt}" value="${encodeService.encrypt(listChoixArt.id) }" />
						<tr>
							<td style="padding-top: 5px; padding-right: 30px;" valign="top">
								<c:choose>
									<c:when test="${listChoixArt.opc_article.id != null }">
										<std:select style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width:200px;" name="opc_article.id_${cpt}" type="long" key="id" labels="libelle" data="${listArticle }" value="${listChoixArt.opc_article.id }" groupKey="famille_cuisine_id" groupLabels="opc_famille_cuisine.code;'-';opc_famille_cuisine.libelle" width="200"/> (A)
									</c:when>
									<c:when test="${listChoixArt.opc_famille.id != null }">
										<std:select style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width:200px;" name="opc_famille.id_${cpt}" type="long" key="id" labels="libelle" data="${listFamille }" value="${listChoixArt.opc_famille.id }" isTree="true" width="200"/> (F)
									</c:when>
									<c:when test="${listChoixArt.opc_list_choix.id != null }">
										<std:select style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width:200px;" name="opc_list_choix.id_${cpt}" type="long" key="id" labels="libelle" data="${listChoixAll }" value="${listChoixArt.opc_list_choix.id }" isTree="true" width="200"/> (L)
									</c:when>	
								</c:choose>
							</td>
							<td style="padding-top: 5px; padding-right: 30px;" valign="top">
								<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width: 80px;" name="quantite_${cpt}" type="decimal" placeholderKey="article.quantite" value="${listChoixArt.quantite }"  maxlength="14"/>
							</td>
							<td>
								<c:if test="${listChoixArt.opc_article.id == null }">
									<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width:80px;" name="nombre_${cpt}" type="long" placeholder="Max"  value="${listChoixArt.nombre}"  maxlength="6" />
								</c:if>
							</td>
							<td valign="top" style="padding-top: 3px; padding-right: 1px;">
								<std:link actionGroup="M" id="delete_cont" icon="fa fa-times" style="color: red;margin-left: 5px;"></std:link>
							</td>
						</tr>
						<c:set var="cpt" value="${cpt+1 }" /> 
					</c:forEach>
				</table>
			</div>
		<hr>
		<div class="modal-footer">
			<std:button actionGroup="M" classStyle="btn btn-success" action="caisse.listChoix.work_merge" workId="${listChoix.id }" icon="fa-save" value="Sauvegarder" />
			<std:button actionGroup="D" classStyle="btn btn-danger" action="caisse.listChoix.work_delete" workId="${listChoix.id }" icon="fa-trash-o" value="Supprimer" />
			<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
				<i class="fa fa-times"></i> Fermer
			</button>
		</div>

	</div>
</div>
</std:form>
