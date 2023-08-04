"http://www.customtaglib.com/c"<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%> 
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>

<cplx:table name="list_article_alert" forceFilter="true" transitionType="simple" width="100%" showTitleBanner="false" initAction="dash.dashBoard.find_article_alert" checkable="false" exportable="false" autoHeight="true">
	<cplx:header>
		<cplx:th type="string" value="Article" field="articleStockInfo.opc_article.code"/>
		<cplx:th type="string" value="Libellé" field="articleStockInfo.opc_article.libelle" filterOnly="true"/>
		<cplx:th type="string" value="Emplacement" field="articleStockInfo.opc_emplacement.titre" filterOnly="true"/>
		<cplx:th type="long" value="Quantit&eacute;" field="articleStockInfo.qte_restante" sortable="false" filtrable="false"  width="120"/>
		<cplx:th type="integer" value="Seuil" field="articleStockInfo.opc_emplacement_seuil.qte_seuil" width="120"/>
	</cplx:header>
	<cplx:body>
	<c:set var="oldfam" value="${null }"></c:set>
	<c:set var="oldEmplId" value="${null }"></c:set> 
			
	<c:forEach items="${list_article }" var="articleStockInfo">
		<c:if test="${oldEmplId == null || oldEmplId != articleStockInfo.opc_emplacement.id }">
		     <tr> 
				<td colspan="3" noresize="true" class="separator-group">
					${articleStockInfo.opc_emplacement.titre }
				</td>
			</tr>	
		</c:if>
		<c:if test="${oldfam == null  or oldfam != articleStockInfo.opc_article.opc_famille_stock.id }">
		     <tr style="background-color: #eeeeee;">
				<td colspan="3" noresize="true" class="separator-group">
					<span class="fa fa-fw fa-folder-open-o separator-icon"></span> ${articleStockInfo.opc_article.opc_famille_stock.code} - ${articleStockInfo.opc_article.opc_famille_stock.libelle}
				</td>
			</tr>	
		</c:if> 
		<c:set var="oldfam" value="${articleStockInfo.opc_article.opc_famille_stock.id }"></c:set> 
		<c:set var="oldEmplId" value="${articleStockInfo.opc_emplacement.id }"></c:set>
		
			<cplx:tr workId="${articleStockInfo.opc_article.id }">
				<cplx:td style="padding-left: 25px;">${articleStockInfo.opc_article.code}-${articleStockInfo.opc_article.libelle}</cplx:td>
				<cplx:td align="right" style="color:#e95324;" value="${articleStockInfo.qte_restante }"></cplx:td>
				<cplx:td align="right" value="${articleStockInfo.opc_emplacement_seuil.qte_seuil==null?0:articleStockInfo.opc_emplacement_seuil.qte_seuil }"></cplx:td>
			</cplx:tr>
		</c:forEach>
	</cplx:body>
</cplx:table>
