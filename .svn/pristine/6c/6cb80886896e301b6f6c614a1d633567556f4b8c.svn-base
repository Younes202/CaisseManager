<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp" %>

<cplx:table name="list_paiement_echeance" transitionType="simple" filtrable="false" width="100%" title="Paiement � �ch�ance" initAction="dash.dashCompta.gestionPaiementEcheance" checkable="false" autoHeight="true">
	<cplx:header>
		<cplx:th type="string" value="Libell&eacute;" field="paiement.libelle" />
		<cplx:th type="date" value="Date &eacute;ch&eacute;ance" field="paiement.date_echeance" width="100" />
		<cplx:th type="decimal" value="Montant" field="paiement.montant" width="100" />
		<cplx:th type="decimal" field="paiement.opc_financement_enum.libelle" value="Paiement" />
		<cplx:th type="empty" width="70" />
	</cplx:header>
	<cplx:body>
		<c:set var="oldFourn" value="${null }"></c:set>
		
		<!-- **************************** mouvements ************************ -->
		<c:forEach items="${listEcheance }" var="paiement">
			<c:if test="${oldFourn == null  or oldFourn != paiement.opc_fournisseur.id }">
			     <tr>
					<td colspan="5" noresize="true" class="separator-group">
						<span class="fa fa-fw fa-folder-open-o separator-icon"></span> ${paiement.opc_fournisseur.libelle}
					</td>
				</tr>	
			</c:if>
			<c:set var="oldFourn" value="${paiement.opc_fournisseur.id }"></c:set>
			
			<cplx:tr workId="${paiement.id }">
				<cplx:td value="${paiement.libelle }"></cplx:td>
				<cplx:td align="center" value="${paiement.date_echeance}"></cplx:td>
				<cplx:td align="right" style="font-weight:bold;color:#c90000;" value="${paiement.montant }"></cplx:td>
				<cplx:td value="${paiement.opc_financement_enum.libelle}"></cplx:td>
				<cplx:td align="center">
					<std:linkPopup classStyle="" action="dash.dashCompta.pointer_paiement_echeance" workId="${paiement.id }" value="Marquer" />
				</cplx:td>
			</cplx:tr>
			<tr style="display: none;" id="tr_det_${paiement.id}" class="sub">
				<td colspan="5" noresize="true" style="background-color: #fff4d3;" id="tr_consult_${paiement.id}">
					
				</td>
			</tr>
		</c:forEach>
		<c:if test="${not empty listEcheance }">
			<tr>
				<td colspan="5" noresize="true" style="background-color: #fff4d3;">
					<std:label classStyle="control-label col-md-6" value="Total montant non point&eacute" />
					<span style="font-size: 14px !important;font-weight: bold;margin-top: 6px;" class="badge badge-orange"><fmt:formatDecimal value="${ttl_echeance }"/></span>
				</td>
			</tr>
		</c:if>
	</cplx:body>
</cplx:table>