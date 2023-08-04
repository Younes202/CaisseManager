<%@page import="appli.model.domaine.stock.persistant.MouvementPersistant"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="appli.controller.domaine.stock.bean.MouvementBean"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<%
	MouvementBean mvmBean = (MouvementBean)request.getAttribute("mouvementBean");
	MouvementPersistant mvmDestBean = (MouvementPersistant)request.getAttribute("mouvementDestBean");
%>
<style>
	.control-label{
	margin-top: -4px;
	}
</style>

	<!-- widget grid -->
	<div class="widget" style="width: 95%;margin: 5px;">
         <div class="widget-body" style="border-radius: 15px;">
				<div class="row">
			<div class="form-group">

				<std:label classStyle="control-label col-md-2" valueKey="mouvement.date_mouvement" />
				<div class="col-md-2">
					<fmt:formatDate value="${mouvementBean.date_mouvement}" />
				</div>
				<std:label classStyle="control-label col-md-2" valueKey="mouvement.num_bl" />
				<div class="col-md-2">
					${mouvementBean.num_bl}
				</div>
			</div>
			<hr>
			<div class="form-group">
				<std:label classStyle="control-label col-md-2" valueKey="mouvement.opc_emplacement" />
				<div class="col-md-4">
					${mouvementBean.opc_emplacement.titre }
				</div>
				<std:label classStyle="control-label col-md-2" valueKey="mouvement.opc_destination" />
				<div class="col-md-4">
					${mouvementDestBean.opc_destination.titre}
				</div>
			</div>
			<div class="form-group">
				<std:label classStyle="control-label col-md-2" valueKey="mouvement.commentaire" />
				<div class="col-md-4">
					${mouvementBean.commentaire}
				</div>
			</div>
		</div>	
		<b style="color: blue;">D&eacute;tail des composants</b>
		<hr>
			<div class="row">
				<div class="col-md-6">
					<table style="width: 97%;margin-left: 20px;">
						<tr>
							<th>Composant</th>
							<th width="100px;" style="text-align: center;">Quantit&eacute;</th>
						</tr>
						<c:set var="bigDecimalUtil" value="<%=new BigDecimalUtil() %>" />
						<c:forEach items="${mouvementBean.list_article }" var="articleMvm">
							<tr style="height: 10px;">
								<td style="padding-top: 5px; padding-right: 10px;" valign="top">
									${articleMvm.opc_article.getLibelleDataVal()}
								</td>
								<td style="padding-top: 5px; padding-right: 10px;" valign="top" align="right">
									<fmt:formatDecimal value="${articleMvm.quantite}" />
								</td>
							</tr>
						</c:forEach>
					</table>
				</div>
				<div class="col-md-6">
					<table style="width: 97%;margin-left: 20px;">
						<tr>
							<th>Article/composant</th>
							<th width="100px;" style="text-align: center;">Quantit&eacute;</th>
						</tr>
						<c:set var="bigDecimalUtil" value="<%=new BigDecimalUtil() %>" />
						<c:forEach items="${mouvementDestBean.list_article }" var="articleMvm">
							<tr style="height: 10px;">
								<td style="padding-top: 5px; padding-right: 10px;" valign="top">
									${articleMvm.opc_article.getLibelleDataVal()}
								</td>
								<td style="padding-top: 5px; padding-right: 10px;" valign="top" align="right">
									<fmt:formatDecimal value="${articleMvm.quantite}" />
								</td>
							</tr>
						</c:forEach>
					</table>
				</div>	
			</div>
		</div>
	</div>
<br>