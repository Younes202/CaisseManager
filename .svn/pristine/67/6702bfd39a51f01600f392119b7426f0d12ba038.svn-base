<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.model.common.util.BooleanUtil"%>
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

<%
	String typeMouvement = (String) ControllerUtil.getMenuAttribute("type_mvmnt", request);	
	ControllerUtil.setMenuAttribute("tp", "A", request);
%>

<script type="text/javascript">
	$(document).ready(function() {		
		$(".selectCustom").each(function(){
			<%if(!ControllerUtil.isEditionWritePage(request)){%>
				$(this).attr("disabled", "disabled");
			<%}%>
			loadSelectAuto(getJQueryName($(this).attr("id")));
		});
	});
</script>	

<style>
#det-bloc input{
	font-size: 12px;
}

</style>

<%-- <c:if test="${type_mouvment == 't' || not empty mouvement.id || type_mouvment != 't' }"> --%>
        <div class="widget-header bordered-bottom bordered-blue">
           <span class="widget-caption">D&eacute;tail des composants &nbsp;
           </span> 
           <%
           	if(typeMouvement.equals("a")) {
           %>
           	<std:link noJsValidate="true" params="is_compo_inc=1" action="stock.composant.work_init_create" classStyle="btn btn-primary btn-sm" icon="fa fa-plus-circle" style="float: left;margin-left: 20px;margin-top: 5px;" value="Ajouter un composant" />
           <%} %>
           
           <% if(request.getAttribute("is_inv_prev") == null){	%>
				<std:link actionGroup="M" id="add_ctrl" tooltipKey="mouvement.article.ajouter" style="float:left;margin-top: 5px;" icon="fa-1x fa-plus" classStyle="btn btn-primary btn-sm" />
			<%} %>	
           
      <%if(typeMouvement.equals("a") || typeMouvement.equals("v") || typeMouvement.equals("av")){ %>     
           Total TTC <span style="height: 26px;font-weight: bold;font-size: 20px !important;margin-top: 7px;margin-right: 11px;" class="badge badge-green" id="total_all">-</span>
      <%}%>   
        </div>
        <div class="widget-body" id="det-bloc">
			<div class="row">
			<table id="ctrl_table" style="width: 97%;margin-left: 20px;">
				<tr>
					<th>Composant</th>
					<th width="60px"></th>
					<th width="100px">Quantit&eacute;</th>
				<%if(typeMouvement.equals("a") || typeMouvement.equals("v") || typeMouvement.equals("av")){ %>
					<th width="110px">Prix unitaire HT</th>
				<%}%>
				<%if(typeMouvement.equals("a") || typeMouvement.equals("av")){ %>
					<th width="90px">TVA</th>
					<th width="110px">Prix unitaire TTC</th>
					
					 <%if(!typeMouvement.equals("av")){%>
						<th width="110px">Remise <i class="fa fa-info-circle" title="Montant global de la remise sur le HT. Si la remise est en % alors cocher la case"></i></th>
						<th width="20px">% <i class="fa fa-info-circle" title="Cocher si remise en %"></i></th>
						<th width="180px">Date de &eacute;remption</th>
					<%} %>
				<%}%>
				<%if(typeMouvement.equals("v")){ %>
					<th width="180px">Prix unitaire TTC</th>
				<% }%>
				<%if(typeMouvement.equals("a") || typeMouvement.equals("v") || typeMouvement.equals("av")){ %>
					<th width="50px">Total HT</th>
					<th width="50px">Total TTC</th>
				<%} %>	
					<th width="50px"></th>
				</tr>
				<tr id="ctrl_gpt" style="display: none;">
					<td style="padding-top: 5px; padding-right: 10px;" valign="top">
						<std:select style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important; width: 100% !important;" classStyle="slct" name="opc_article.id_0" hiddenkey="prix_achat_ht;opc_tva_enum.id" type="long" key="id" labels="'[';code_barre;']';code;'-';libelle;[';composition;']'" groupKey="opc_famille_stock.id" groupLabels="opc_famille_stock.code;'-';opc_famille_stock.libelle" mode="std" />
					</td>
					<td style="padding-top: 5px; padding-right: 10px" valign="top">
						<std:linkPopup id="edit_article_0" classStyle="btn btn-sm btn-primary" action="stock.composant.work_edit" noJsValidate="true">
							<span class="fa  fa-eye"></span>
						</std:linkPopup>
					</td>
					<td style="padding-top: 5px; padding-right: 10px;" valign="top">
						<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width: 80px;" name="quantite_0" type="decimal" placeholderKey="mouvement.quantite" maxlength="14"/>
					</td>
				
				<%if(typeMouvement.equals("a") || typeMouvement.equals("av")){ %>	
					<td style="padding-top: 5px; padding-right: 10px;" valign="top">
						<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width:50px;" name="prix_ht_0" type="decimal" placeholderKey="mouvement.prix_ht" maxlength="14"/>
					</td>
					<td style="padding-top: 5px; padding-right: 10px;" valign="top">
						<std:select style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important; width:65px;" classStyle="slct" name="opc_tva_enum.id_0" type="long" key="id" labels="libelle" data="${valTVA}" addBlank="false" mode="standard"/>
					</td>
					<td style="padding-top: 5px; padding-right: 10px;" valign="top">
						<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width:80px;" name="prix_ttc_0" type="decimal" placeholderKey="mouvement.prix_ttc" maxlength="14" />
					</td>
					
					<% if(!typeMouvement.equals("av")){ %>
					<td style="padding-top: 5px; padding-right: 5px;" valign="top">
						<std:text name="remise_0" type="decimal" style="width:60px;" placeholderKey="mouvement.remise" maxlength="14"/>
					</td>
					<td style="padding-top: 5px; padding-right: 10px;" valign="top">
						<std:checkbox name="is_remise_ratio_0" style="height: 20px !important;" />
					</td>
					<td style="padding-top: 5px; padding-right: 10px;" valign="top">
						<std:date name="date_peremption_0" placeholderKey="mouvement.date_peremption" style="width:120px !important;" classStyle="form-control"/>
					</td>
					<%} %>
				<%} %>
				<%if(typeMouvement.equals("v")){ %>	
					<td style="padding-top: 5px; padding-right: 10px;" valign="top">
						<std:text style="border-color: #eee;width:80px;" name="prix_ht_0" type="decimal" placeholderKey="mouvement.prix_ht" maxlength="14"/>
					</td>
					<td style="padding-top: 5px; padding-right: 10px;" valign="top">
						<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width:80px;" name="prix_vente_0" type="decimal" placeholderKey="mouvement.prix_vente" maxlength="14" />
					</td>
				<%} %>
				<%if(typeMouvement.equals("a") || typeMouvement.equals("v") || typeMouvement.equals("av")){ %>
					<td valign="top" style="padding-top: 5px; padding-right: 1px;">
						<span style="height: 20px;font-weight: bold;margin-top: 5px;margin-right: 1px;font-size: 15px !important;" class="badge badge-blue" id="total_lnHt_0">-</span>
					</td>
					<td valign="top" style="padding-top: 5px; padding-right: 1px;">
						<span style="height: 20px;font-weight: bold;margin-top: 5px;margin-right: 1px;font-size: 15px !important;" class="badge badge-blue" id="total_ln_0">-</span>
					</td>
				<%} %>	
					<td valign="top" style="padding-top: 5px; padding-right: 1px;">
						<std:link actionGroup="M" id="delete_cont" icon="fa fa-times" name="del_0" style="color: red;"></std:link>
					</td>
				</tr>
				
				<c:set var="cpt" value="${1 }" />
				<c:set var="encodeService" value="<%=new EncryptionUtil()%>" />
				<c:set var="articleAction" value="<%=new ArticleAction()%>" />
				<c:set var="bigDecimalUtil" value="<%=new BigDecimalUtil()%>" />
				
				<c:forEach items="${mouvement.list_article }" var="articleMvm">
					<std:hidden name="eaiid_${cpt}" value="${encodeService.encrypt(articleMvm.id) }" />
					<tr>
						<td style="padding-top: 5px; padding-right: 15px;" valign="top">
							<select class="selectCustom" name="opc_article.id_${cpt}" id="opc_article.id_${cpt}" style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;; width: 100% !important;">
								<c:if test="${not empty articleMvm.opc_article }">
									<option value="${articleMvm.opc_article.id }" selected="selected" id="${articleMvm.opc_article.id }">
										<c:if test="${articleMvm.opc_article.code_barre != null }">
											[${articleMvm.opc_article.code_barre }] 
										</c:if>
										${articleMvm.opc_article.code }-${articleMvm.opc_article.libelle } 
										<c:if test="${not empty(articleMvm.opc_article.composition) }">
											(${articleMvm.opc_article.composition })
										</c:if>
									</option>
								</c:if>
							</select>
						</td>
						<td style="padding-top: 5px; padding-right: 10px;" valign="top">
							<std:linkPopup id="edit_article_${cpt}" classStyle="btn btn-sm btn-primary" action="stock.composant.work_edit" params="art=${articleMvm.opc_article.id }" noJsValidate="true">
								<span class="fa  fa-eye"></span>
							</std:linkPopup>
						</td>
						<td style="padding-top: 5px; padding-right: 15px;" valign="top">
							<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width: 80px;" name="quantite_${cpt}" type="decimal" placeholderKey="mouvement.quantite" value="${articleMvm.quantite }"  maxlength="14"/>
						</td>
				
				<%if(typeMouvement.equals("a") || typeMouvement.equals("av")){ %>		
						<td style="padding-top: 5px; padding-right: 15px;" valign="top">
							<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width:80px;" name="prix_ht_${cpt}" type="decimal" placeholderKey="mouvement.prix_ht" value="${articleMvm.prix_ht }"  maxlength="14">
							</std:text>
						</td>
						<td style="padding-top: 5px; padding-right: 10px;" valign="top">
							<std:select style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width: 65px !important;" name="opc_tva_enum.id_${cpt}" type="long" key="id" labels="libelle" data="${valTVA}" value="${articleMvm.opc_tva_enum.id }" mode="standard"/>
						</td>
						<td style="padding-top: 5px; padding-right: 10px;" valign="top">
							<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width:80px;" name="prix_ttc_${cpt}" type="decimal" value="${articleMvm.prix_ttc }" maxlength="14"/>
						</td>
						
						<% if(!typeMouvement.equals("av")){ %>
						<td style="padding-top: 5px; padding-right: 5px;" valign="top">
							<std:text name="remise_${cpt}" style="width:50px;" type="decimal" placeholderKey="mouvement.remise" value="${articleMvm.remise }"  maxlength="14"/>
						</td>
						<td style="padding-top: 5px; padding-right: 15px;" valign="top">
							<std:checkbox name="is_remise_ratio_${cpt}" value="${articleMvm.is_remise_ratio }" style="height: 20px !important;" />
						</td>
						<td style="padding-top: 5px; padding-right: 15px;" valign="top">
							<std:date name="date_peremption_${cpt}" placeholderKey="mouvement.date_peremption" style="width:120px !important;" value="${articleMvm.date_peremption }" />
						</td>
						<%} %>
					<%} %>
					<%if(typeMouvement.equals("v")){ %>	
						<td style="padding-top: 5px; padding-right: 15px;" valign="top">
							<std:text style="border-color: #eee;width:80px;" name="prix_ht_${cpt}" type="decimal" placeholderKey="mouvement.prix_ht" value="${articleMvm.prix_ht }" maxlength="14"/>
						</td>
						<td style="padding-top: 5px; padding-right: 10px;" valign="top">
							<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width:80px;" name="prix_vente_${cpt}" type="decimal" placeholderKey="mouvement.prix_vente" value="${articleMvm.prix_vente }"  maxlength="14"/>
						</td>
					<%} %>
					<%if(typeMouvement.equals("a") || typeMouvement.equals("v") || typeMouvement.equals("av")){ %>
						<td valign="top" style="padding-top: 5px; padding-right: 1px;">
							<span style="height: 20px;font-weight: bold;margin-top: 5px;margin-right: 1px;font-size: 15px !important;" class="badge badge-blue" id="total_lnHt_${cpt}">
								<fmt:formatDecimal value="${articleMvm.prix_ht_total}"/>
							</span>
						</td>
						<td valign="top" style="padding-top: 5px; padding-right: 1px;">
							<span style="height: 20px;font-weight: bold;margin-top: 5px;margin-right: 1px;font-size: 15px !important;" class="badge badge-blue" id="total_ln_${cpt}">
								<fmt:formatDecimal value="${articleMvm.prix_ttc_total }" />
							</span>
						</td>
					<%} %>	
						<td valign="top" style="padding-top: 5px; padding-right: 1px;">
							<std:link actionGroup="${isM ? 'X':'M' }" id="delete_cont" name="del_${cpt}" icon="fa fa-times" style="color: red;"></std:link>
						</td>
					</tr>

					<c:set var="cpt" value="${cpt+1 }" /> 
				</c:forEach>
			</table>
		</div>
	</div>
<%-- </c:if> --%>
