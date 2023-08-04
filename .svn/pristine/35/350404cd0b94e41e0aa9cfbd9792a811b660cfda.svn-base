<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp" %>

<c:choose>
	<c:when test="${listArticleBarre.size() == 0 }">
		Aucun article trouv&eacute;.
	</c:when>
	<c:otherwise>
		<table style="width: 99%;">
			<tr style="background-color: #57b5e3;">
				<th></th>
				<th>Article</th>
				<th>Code barre</th>
				<th>Prix</th>
			</tr>
		
			<c:forEach var="article" items="${listArticleBarre }">
				<tr style="border: 1px solid #57b5e3;height: 30px;">
					<td>
						<std:linkPopup action="caisse-web.balance.addArticleMenuCmd" workId="${article.id }" actionGroup="X" icon="fa fa-eye" classStyle="btn btn-sm btn-success" tooltip="S&eacute;l&eacute;ctionner" onClick="$('#close_modal').trigger('click');" />
					</td>
					<td>${article.code }-${article.libelle }</td>
					<td style="padding-right: 18px;padding-left: 11px;">${article.code_barre=='null' ? '':article.code_barre}</td>
					<td style="font-weight: bold;color:blue;text-align: right;padding-right: 5px;"><fmt:formatDecimal value="${article.prix_vente }" /></td>
				</tr>
			</c:forEach>
		</table>
	</c:otherwise>
</c:choose>		