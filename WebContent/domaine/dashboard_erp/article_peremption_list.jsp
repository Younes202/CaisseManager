<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>

<cplx:table name="list_article_peremption" filtrable="false" sortable="false" paginate="false" transitionType="simple" width="100%" showTitleBanner="false" initAction="dash.dashBoard.find_article_peremption" checkable="false" exportable="false" autoHeight="true">
		<cplx:header>
			<cplx:th type="string" value="Article" field="lotArticle.opc_article.code"/>
			<cplx:th type="decimal" value="Quantit&eacute; restante" field="lotArticle.quantite"/>
			<cplx:th type="date" value="Date p&eacute;remption" field="lotArticle.date_peremption" width="120"/>
			<cplx:th type="empty"/>
		</cplx:header>
		<cplx:body>
		<c:set var="oldfam" value="${null }"></c:set>
		<c:set var="oldEmplId" value="${null }"></c:set> 
				
		<c:forEach items="${list_article_peremption }" var="lotArt">
			<c:if test="${oldEmplId == null || oldEmplId != lotArt.opc_emplacement.id }">
			     <tr>
					<td colspan="4" noresize="true" class="separator-group">
						<span class="fa fa-fw fa-folder-open-o separator-icon"></span> ${lotArt.opc_emplacement.titre }
					</td>
				</tr>	
			</c:if>
			<c:if test="${oldfam == null  or oldfam != lotArt.opc_article.opc_famille_stock.id }">
			     <tr style="background-color: #eeeeee;">
					<td colspan="4" noresize="true" class="separator-group">
						<span class="fa fa-fw fa-folder-open-o separator-icon"></span> ${lotArt.opc_article.opc_famille_stock.code} - ${lotArt.opc_article.opc_famille_stock.libelle}
					</td>
				</tr>	
			</c:if>
			<c:set var="oldfam" value="${lotArt.opc_article.opc_famille_stock.id }"></c:set>
			<c:set var="oldEmplId" value="${lotArt.opc_emplacement.id }"></c:set>
			
				<cplx:tr workId="${lotArt.opc_article.id }">
					<cplx:td style="padding-left: 25px;">${lotArt.opc_article.code}-${lotArt.opc_article.libelle}</cplx:td>
					<cplx:td align="right" style="color:#e95324;" value="${lotArt.quantite }"></cplx:td>
					<cplx:td align="right" style="color:#e95324;" value="${lotArt.date_peremption }"></cplx:td>
					<cplx:td align="center">
						<std:link action="dash.dashStock.deleteLot" workId="${lotArt.id }" targetDiv="dash_art_2" actionGroup="C" icon="fa fa-close" classStyle="btn btn-sm btn-danger" tooltip="Supprimer l'alerte" />
					</cplx:td>
				</cplx:tr>
			</c:forEach>
		</cplx:body>
	</cplx:table>