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

<%
int menuLevel = (request.getAttribute("menuLevel")==null?0 : (Integer)request.getAttribute("menuLevel"));// Un seul menu par arbo
%>

<script type="text/javascript">
$(document).ready(function() {
	getTabElement("#sep_photoV").remove();
	
	<%
	MenuCompositionBean menuBean = (MenuCompositionBean)request.getAttribute("menuComposition");
	if(menuBean != null && menuBean.getId() != null){ 
		IArticleService service = (IArticleService)ServiceUtil.getBusinessBean(IArticleService.class); 
		Map<String, byte[]> dataimg = service.getDataImage(menuBean.getId(), "menu");
		if(dataimg.size() > 0){
			String fileName = dataimg.entrySet().iterator().next().getKey();
			byte[] value = dataimg.entrySet().iterator().next().getValue();
		%>
			getTabElement("#photoV_div").css("background", "");
			getTabElement("#photoV_div").html("<img src='data:image/jpeg;base64,<%=FileUtil.getByte64(value) %>' width='120' height='120'/>");
	 		getTabElement("#photoV_name_span").text("<%=fileName%>");
	 		getTabElement("#photoV_name").val("<%=fileName%>");
		<%}
		} %>
});
</script>

<std:form name="data-formV">	
	<!-- widget grid -->
	<div class="widget">
		<div class="widget-body">
			<c:if test="${parent_level > 0 }">
				<div class="row">
					<c:if test="${not empty menuComposition.id }">
						<std:label classStyle="control-label col-md-2" value="Parent" />
						<div class="col-md-6" style="margin-left: -13px;">
							<std:select disable="true" name="menuComposition.parent_id" type="long" key="id" labels="code;'-';libelle" data="${listMenu}" required="true" isTree="true" width="100%" value="${parent_menu }"/>
						</div>
					</c:if>
					<%if(menuLevel == 2){ %>
						<std:label classStyle="control-label col-md-2" value="Est un menu" />
						<div class="col-md-2" style="margin-left: -13px;">
							<std:checkbox disable="true" name="menuComposition.is_menu" />
						</div>
					<%} %>	
				</div>
			</c:if>
			<div class="row" style="margin-top: 9px;">
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" value="Code" />	
					<div class="col-md-3">
						<std:text name="menuComposition.code" readOnly="true" type="string" placeholder="Code" required="true" style="width: 100px;width: 100px;float: left;" maxlength="20" />
					</div>
					<div class="col-md-7">
						<std:text name="menuComposition.libelle" readOnly="true" type="string" placeholderKey="famille.libelle" required="true" maxlength="80" />
					</div>
				</div>
			</div>
	<c:if test="${parent_level > 0 }">		
			<div class="row">
				<std:label classStyle="control-label col-md-2" value="Prix de vente" />
				<div class="col-md-4">
					<std:text name="menuComposition.mtt_prix" readOnly="true" type="decimal" placeholder="Prix" maxlength="16" style="width:80px;" />
				</div>
				<std:label classStyle="control-label col-md-2" value="Max articles" />
				<div class="col-md-4">
					<std:text name="menuComposition.nombre_max" readOnly="true" type="long" placeholder="Max" required="true" style="width: 80px;float:left;"/>
				</div>
			</div>
	</c:if>		
			<div class="row">	
				<std:label classStyle="control-label col-md-2" value="Image" />
				<div class="col-md-4">
					<div class="col-sm-12">	
						<div id="photoV_div" style="width: 100px;margin-top:10px; border: 1px solid #c6d983; border-radius: 25px; text-align: center; height: 90px; background: url('resources/framework/img/picture_file.png') no-repeat center;cursor: pointer;">
							<span style="font-size: 11px;">Image</span>
						</div>
						<span id="photoV_name_span"></span>
						<input type="hidden" name="photoV_name" id="photoV_name">
					</div>	
					<div class="col-sm-12">
						<!-- Separator -->
						<div id="sep_photoV" style="width:100px;margin-bottom: 5px; height: 20px; text-align: center;">
							<a href="javascript:"><b>X</b></a>
						</div>
						<!-- End -->
						<input type="file" name="photoV" id="photoV_div_file" style="display: none;" accept="image/bmp,image/gif,image/jpg,image/png,image/jpeg">
					</div>
				</div>
				<std:label classStyle="control-label col-md-2" value="Caisses destination" />
				<div class="col-md-4" style="margin-top: 15px;">
					<std:select disable="true" name="menuComposition.caisses_target" multiple="true" width="160" type="string[]" key="id" labels="reference" data="${listeCaisse}" value="${caisseArray }" style="float:left;"/>
				</div>
			</div>
			<hr>
	
<c:if test="${parent_level > 0 }">
		
<c:set var="encodeService" value="<%=new EncryptionUtil()%>" />

<div style="overflow-y: auto;overflow-x: hidden;max-height: 400px;" class="row">
		<div class="form-group" style="margin-left: 0px;">
			<table style="width: 97%;margin-left: 10px;">
				<tr>
					<td></td>
					<td style="font-size: 12px;color: #009688;">Quantit&eacute;</td>
					<td style="font-size: 12px;color: #009688;">Prix</td>
					<td style="font-size: 12px;color: #009688;">Max</td>
				</tr>

				<c:set var="cpt" value="${1 }" />
				<c:forEach items="${menuComposition.list_composition }" var="articleDet">
						<tr>
						<td style="padding-top: 5px; padding-right: 30px;" valign="top">
						<c:choose>
							<c:when test="${articleDet.opc_article.id != null }">								
								<std:select disable="true" style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width:230px !important;" hiddenkey="prix_vente" name="opc_article.id_${cpt}" type="long" key="id" labels="libelle" data="${listArticle }" value="${articleDet.opc_article.id }" groupKey="famille_cuisine_id" groupLabels="opc_famille_cuisine.code;'-';opc_famille_cuisine.libelle" />
								<span style="font-size: 9px;color:blue;">(ARTICLE)</span>
							</c:when>
							<c:when test="${articleDet.opc_article_inc.id != null }">								
								<std:select disable="true" style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width:230px !important;" hiddenkey="prix_vente" name="opc_article_inc.id_${cpt}" type="long" key="id" labels="libelle" data="${listArticle }" value="${articleDet.opc_article_inc.id }" groupKey="famille_cuisine_id" groupLabels="opc_famille_cuisine.code;'-';opc_famille_cuisine.libelle" />
								<span style="font-size: 9px;color:blue;">(INCLUS)</span>
							</c:when>
							<c:when test="${articleDet.opc_article_destock.id != null }">								
								<std:select disable="true" style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width:230px !important;" hiddenkey="prix_vente" name="opc_article_destock.id_${cpt}" type="long" key="id" labels="libelle" data="${listArticleStock }" value="${articleDet.opc_article_destock.id }" groupKey="opc_famille_stock.id" groupLabels="opc_famille_stock.code;'-';opc_famille_stock.libelle" />
								<span style="font-size: 9px;color:blue;">(STOCK)</span>
							</c:when>
							<c:when test="${articleDet.opc_list_choix.id != null }">
								<std:select disable="true" style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width:230px !important;" name="opc_list_choix.id_${cpt}" type="long" key="id" labels="libelle" data="${listChoix }" value="${articleDet.opc_list_choix.id }" />
								<span style="font-size: 9px;color:blue;">(CHOIX)</span>
							</c:when>
							<c:when test="${articleDet.opc_famille.id != null }">
								<std:select disable="true" style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width:230px !important;" name="opc_famille.id_${cpt}" type="long" key="id" labels="libelle" data="${listFamille }" value="${articleDet.opc_famille.id }" isTree="true" />
								<span style="font-size: 9px;color:blue;">(FAMILLE)</span>
							</c:when>	
						</c:choose> 
						</td>
							<td style="padding-top: 5px; padding-right: 30px;" valign="top">
								<std:text readOnly="true" style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width:60px;" name="quantite_${cpt}" type="decimal" placeholderKey="article.quantite" value="${articleDet.quantite }"  maxlength="14"/>
							</td>
							<td style="padding-top: 5px; padding-right: 30px;" valign="top">
							<c:if test="${articleDet.opc_article_destock.id == null }">
								<std:text readOnly="true" style="width:80px;" name="prix_${cpt}" type="decimal" placeholder="Prix" value="${articleDet.prix }"  maxlength="14"/>
							</c:if>	
							</td>
							<td style="padding-top: 5px; padding-right: 30px;" valign="top">
							<c:if test="${articleDet.opc_article_destock.id == null and articleDet.opc_article_inc.id == null }">
								<std:text readOnly="true" style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width:60px;" name="nombre_${cpt}" type="long"  value="${articleDet.nombre }" placeholder="Nombre" />
							</c:if>	
							</td>
						</tr>
						<c:set var="cpt" value="${cpt+1 }" />
				</c:forEach>
			</table>
		</div>
	</div>
</c:if>	
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