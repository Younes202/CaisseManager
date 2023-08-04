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

<div class="row">
	<c:if test="${empty curr_type_transfo or curr_type_transfo == 'L' }">
		<div class="col-md-6" style="border-right: 1px solid red;">
			<span style="color: blue;">Composants &agrave; transformer</span>
			<hr>
			<table id="ctrl_table" style="width: 97%;margin-left: 20px;">
				<tr style="border-bottom: 1px solid #2196f3;">
					<th>Composant</th>
					<th width="60px"></th>
					<th width="100px">Quantit&eacute;</th>
					<th width="50px"></th>
				</tr>
				<tr id="ctrl_gpt" style="display: none;">
					<td style="padding-top: 5px; padding-right: 10px;" valign="top">
						<std:select style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important; width: 100% !important;" classStyle="slct" name="opc_article.id_0" hiddenkey="prix_achat_ht;opc_tva_enum.id" type="long" key="id" labels="code;'-';libelle" data="${listComposant}"  groupKey="opc_famille_stock.id" groupLabels="opc_famille_stock.code;'-';opc_famille_stock.libelle"/>
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
					<tr>
						<td style="padding-top: 5px; padding-right: 30px;" valign="top">
							<std:select style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;; width: 100% !important;" name="opc_article.id_${cpt}" type="long" key="id" labels="code;'-';libelle" data="${listComposant }" value="${articleMvm.opc_article.id }" groupKey="opc_famille_stock.id" groupLabels="opc_famille_stock.code;'-';opc_famille_stock.libelle" />
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
			<span style="color: blue;">Transformer en</span>
			<hr>
			<table id="ctrl_table_comp" style="width: 97%;margin-left: 20px;">
				<tr style="border-bottom: 1px solid #2196f3;">
					<th>Composant</th>
					<th width="100px">Quantit&eacute;</th>
					<th width="50px"></th>
				</tr>
				<tr id="ctrl_gpt_comp" style="display: none;">
					<td style="padding-top: 5px; padding-right: 10px;" valign="top">
						<std:select style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important; width: 100% !important;" classStyle="slct" name="composant_0" hiddenkey="prix_achat_ht;opc_tva_enum.id" type="long" key="id" labels="code;'-';libelle" data="${listComposantSortie}"  groupKey="opc_famille_stock.id" groupLabels="opc_famille_stock.code;'-';opc_famille_stock.libelle"/>
					</td>
					<td style="padding-top: 5px; padding-right: 10px;" valign="top">
						<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width: 100px;" name="quantite_cmp_0" type="decimal" placeholder="Quantit&eacute;" maxlength="14"/>
					</td>
					<td valign="top" style="padding-top: 5px; padding-right: 1px;">
						<std:link actionGroup="M" id="delete_cont" icon="fa fa-times" style="color: red;"></std:link>
					</td>
				</tr>

				<c:set var="cpt" value="${1 }" />
				<c:set var="encodeService" value="<%=new EncryptionUtil()%>" />
				<c:set var="articleAction" value="<%=new ArticleAction()%>" />
				<c:set var="bigDecimalUtil" value="<%=new BigDecimalUtil()%>" />
				
				<c:forEach items="${mvmDestBean.list_article }" var="articleMvm">
					<tr>
						<td style="padding-top: 5px; padding-right: 30px;" valign="top">
							<std:select style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;; width: 100% !important;" name="composant_${cpt}" type="long" key="id" labels="code;'-';libelle" data="${listComposantSortie}" value="${articleMvm.opc_article.id }" groupKey="opc_famille_stock.id" groupLabels="opc_famille_stock.code;'-';opc_famille_stock.libelle" />
						</td>
						<td style="padding-top: 5px; padding-right: 30px;" valign="top">
							<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width: 100px;" name="quantite_cmp_${cpt}" type="decimal" placeholder="Quantit&eacute;" value="${articleMvm.quantite }"  maxlength="14"/>
						</td>
						<td valign="top" style="padding-top: 5px; padding-right: 1px;">
							<std:link actionGroup="M" id="delete_cont" icon="fa fa-times" style="color: red;"></std:link>
						</td>
					</tr>

					<c:set var="cpt" value="${cpt+1 }" /> 
				</c:forEach>
			</table>
			
<%-- 			<c:if test="${not empty mvmDestBean}"> --%>
<%-- 				<c:set var="valCompId" value="${mvmDestBean.list_article.get(0).opc_article.id }" /> --%>
<%-- 				<c:set var="valCompQte" value="${mvmDestBean.list_article.get(0).quantite }" /> --%>
<%-- 			</c:if> --%>
<!-- 			<div class="row" style="margin-top: 10px;"> -->
<%-- 				<std:label classStyle="control-label col-md-2" value="Composant" /> --%>
<!-- 				<div class="col-md-7"> -->
<%-- 					<std:select name="composant" disable="${is_inv_prev?'true':'false' }" type="long" key="id" labels="code;'-';libelle" data="${listComposant}" width="100%" groupKey="opc_famille_stock.id" groupLabels="opc_famille_stock.code;'-';opc_famille_stock.libelle" value="${valCompId }"/> --%>
<!-- 				</div> -->
<!-- 				<div class="col-md-3"> -->
<%-- 					<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width: 100px;" name="quantite_cmp" type="decimal" placeholder="Quantit&eacute;" maxlength="14" value="${valCompQte }"/> --%>
<!-- 				</div>	 -->
<!-- 			</div>	 -->
		</div>
	</c:if>
	<c:if test="${not empty curr_type_transfo and curr_type_transfo == 'D' }">
				<div class="col-md-6" style="border-right: 1px solid red;">
			<span style="color: blue;">Composants &agrave; transformer</span>
			<hr>
			<table id="ctrl_table" style="width: 97%;margin-left: 20px;">
				<tr style="border-bottom: 1px solid #2196f3;">
					<th>Composant</th>
					<th width="60px"></th>
					<th width="100px">Quantit&eacute;</th>
					<th width="50px"></th>
				</tr>
				<tr id="ctrl_gpt" style="display: none;">
					<td style="padding-top: 5px; padding-right: 10px;" valign="top">
						<std:select style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important; width: 100% !important;" classStyle="slct" name="opc_article.id_0" hiddenkey="prix_achat_ht;opc_tva_enum.id" type="long" key="id" labels="code;'-';libelle" data="${listComposant}"  groupKey="opc_famille_stock.id" groupLabels="opc_famille_stock.code;'-';opc_famille_stock.libelle"/>
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
			</table>
		</div>
		<div class="col-md-6">
			<span style="color: blue;">Transformer en</span>
			<hr>
			<table id="ctrl_table_comp" style="width: 97%;margin-left: 20px;">
				<tr style="border-bottom: 1px solid #2196f3;">
					<th>Composant</th>
					<th width="100px">Quantit&eacute;</th>
					<th width="50px"></th>
				</tr>
				<tr id="ctrl_gpt_comp" style="display: none;">
					<td style="padding-top: 5px; padding-right: 10px;" valign="top">
						<std:select style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important; width: 100% !important;" classStyle="slct" name="composant_0" hiddenkey="prix_achat_ht;opc_tva_enum.id" type="long" key="id" labels="code;'-';libelle" data="${listComposantSortie}"  groupKey="opc_famille_stock.id" groupLabels="opc_famille_stock.code;'-';opc_famille_stock.libelle"/>
					</td>
					<td style="padding-top: 5px; padding-right: 10px;" valign="top">
						<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width: 100px;" name="quantite_cmp_0" type="decimal" placeholder="Quantit&eacute;" maxlength="14"/>
					</td>
					<td valign="top" style="padding-top: 5px; padding-right: 1px;">
						<std:link actionGroup="M" id="delete_cont" icon="fa fa-times" style="color: red;"></std:link>
					</td>
				</tr>
			</table>
		</div>
	</c:if>
	<c:if test="${not empty curr_type_transfo and curr_type_transfo == 'P' }">
	
		<table id="ctrl_table" style="width: 97%;margin-left: 20px;">
			<tr style="border-bottom: 1px solid #2196f3;">
				<th>Recette</th>
				<th width="100px">Quantit&eacute;</th>
				<th width="50px"></th>
			</tr>
			<tr id="ctrl_gpt" style="display: none;">
				<td style="padding-top: 5px; padding-right: 10px;" valign="top"> 
					<std:select name="prep_transfo.id_0" type="long" classStyle="slct" style="width: 80%;" key="id" labels="libelle" data="${listPrepTransfo }"  />
				</td>
				<td style="padding-top: 5px; padding-right: 10px;" valign="top">
					<std:text name="prep_quantite_0" type="decimal" placeholder="Quantit&eacute;"  maxlength="14"/>
				</td>
				<td valign="top" style="padding-top: 5px; padding-right: 1px;">
					<std:link actionGroup="M" id="delete_cont" icon="fa fa-times" style="color: red;"></std:link>
				</td>
			</tr>
<!-- 			<tr> -->
<!-- 				<td>	 -->
<%-- 					<std:select name="prep_transfo.id" required="true" type="long" key="id" labels="libelle" data="${listPrepTransfo }"  /> --%>
<!-- 				</td> -->
<!-- 				<td>	 -->
<%-- 					<std:text name="prep_quantite" type="decimal" required="true" placeholder="Quantit&eacute;"  maxlength="14"/> --%>
<!-- 				</td>	 -->
<!-- 				<td valign="top" style="padding-top: 5px; padding-right: 1px;"> -->
<%-- 					<std:link actionGroup="M" id="delete_cont" icon="fa fa-times" style="color: red;"></std:link> --%>
<!-- 				</td> -->
<!-- 			</tr> -->
		</table>		
	</c:if>
  </div>       
				