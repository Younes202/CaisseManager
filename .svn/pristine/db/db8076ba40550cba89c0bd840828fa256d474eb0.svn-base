<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp" %>

<!-- Liste des mouvements -->
<cplx:table name="list_cheque_nonpointe" transitionType="simple" filtrable="false" width="100%" title="Ch&egrave;ques non point&eacute;s" initAction="admin.compteBancaire.gestion_cheque" checkable="false" autoHeight="true">
	<cplx:header>
		<cplx:th type="string" value="Num&eacute;ro ch&egrave;que" field="paiement.num_cheque" width="80" />
		<cplx:th type="string" value="Libell&eacute;" field="paiement.libelle" />
		<cplx:th type="date" value="Date mouvement" field="paiement.date_mouvement" width="100" />
		<cplx:th type="date" value="Date &eacute;ch&eacute;ance" field="paiement.date_echeance" width="80" />
		<cplx:th type="decimal" value="Montant" field="paiement.montant" width="100" />
		<cplx:th type="empty" width="70" />
	</cplx:header>
	<cplx:body>
		<c:set var="oldFourn" value="${null }"></c:set>
		
		<!-- **************************** mouvements ************************ -->
		<c:forEach items="${listDataNonPointe }" var="detail">
			<c:if test="${oldFourn == null  or oldFourn != detail.opc_fournisseur.id }">
			     <tr>
					<td colspan="6" noresize="true" class="separator-group">
						<span class="fa fa-fw fa-folder-open-o separator-icon"></span> ${detail.opc_fournisseur.libelle}
					</td>
				</tr>	
			</c:if>
			<c:set var="oldFourn" value="${detail.opc_fournisseur.id }"></c:set>
			
			<cplx:tr workId="${detail.id }">
				<cplx:td value="${detail.num_cheque }" style="font-weight:bold;"></cplx:td>
				<cplx:td align="center" value="${detail.libelle}"></cplx:td>
				<cplx:td align="center" value="${detail.date_mouvement}"></cplx:td>
				<cplx:td align="center" value="${detail.date_echeance}"></cplx:td>
				<cplx:td align="right" style="font-weight:bold;color:#c90000;" value="${detail.montant }"></cplx:td>
				<cplx:td align="center">
					<std:linkPopup classStyle="" action="dash.dashCompta.pointer_cheque" workId="${detail.id }" value="Pointer" />
				</cplx:td>
			</cplx:tr>
			<tr style="display: none;" id="tr_det_${detail.id}" class="sub">
				<td colspan="6" noresize="true" style="background-color: #fff4d3;" id="tr_consult_${detail.id}">
					
				</td>
			</tr>
		</c:forEach>
		<c:if test="${not empty listDataNonPointe }">
			<tr>
				<td colspan="6" noresize="true" style="background-color: #fff4d3;">
					<std:label classStyle="control-label col-md-6" value="Total montant non point&eacute" />
					<span style="font-size: 14px !important;font-weight: bold;margin-top: 6px;" class="badge badge-orange"><fmt:formatDecimal value="${ttl_non_pointe }"/></span>
				</td>
			</tr>
		</c:if>
	</cplx:body>
</cplx:table>