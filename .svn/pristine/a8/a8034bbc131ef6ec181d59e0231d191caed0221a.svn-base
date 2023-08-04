<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp" %>

<style>
.sortable tr {
	height: 15px;
}
.tab_det_histo{
	margin-left: -100px;
}
.tab_det_histo td{
	font-size: 11px !important;
	border: 0px !important;
}
.tab_det_histo label{
	font-weight: bold;
}
</style>

<div class="row">
	<table style="margin-left: -100px;" class="tab_det_histo">
		<tr>
			<td><std:label classStyle="control-label col-md-3" value="Type" /></td>
			<td>
				<c:choose>
					<c:when test="${caisseMouvement.type_commande=='P' }">
						Sur place
					</c:when>
					<c:when test="${caisseMouvement.type_commande=='E' }">
						A emporter
					</c:when>
					<c:when test="${caisseMouvement.type_commande=='L' }">
						Livraison
					</c:when>
				</c:choose></b>
			</td>
			<td><std:label classStyle="control-label col-md-3" valueKey="caisseMouvement.mode_paiement" /></td>
			<td><b>${caisseMouvement.mode_paiement }</b></td>
		</tr>	
		<tr><td colspan="4"><hr style="margin-top: 5px;margin-bottom: 0px;"></td></tr>
		<tr>
			<td><std:label classStyle="control-label col-md-3" value="Esp&egrave;ces" /></td>
			<td><fmt:formatDecimal value="${caisseMouvement.mtt_donne }"/></td>
			<td><std:label classStyle="control-label col-md-3" value="Ch&egrave;que" /></td>
			<td><fmt:formatDecimal value="${caisseMouvement.mtt_donne_cheque }"/></td>
		</tr>
		<tr>
			<td><std:label classStyle="control-label col-md-3" value="Carte" /></td>
			<td><fmt:formatDecimal value="${caisseMouvement.mtt_donne_cb }"/></td>
			<td><std:label classStyle="control-label col-md-3" value="Ch&egrave;que d&eacute;j." /></td>
			<td><fmt:formatDecimal value="${caisseMouvement.mtt_donne_dej }"/></td>
		</tr>
		<tr>
			<td><std:label classStyle="control-label col-md-3" value="Offert" /> </td>
			<td><fmt:formatDecimal value="${caisseMouvement.mtt_art_offert }" /> </td>
			<td><std:label classStyle="control-label col-md-3" value="R&eacute;duction" /></td>
			<td><fmt:formatDecimal value="${caisseMouvement.mtt_reduction }"/></td>
		</tr>
	<c:if test="${caisseMouvement.opc_client != null }">	
		<tr>
			<td><std:label classStyle="control-label col-md-3" valueKey="caisseMouvement.opc_client" /></td>
			<td colspan="3">${caisseMouvement.opc_client.nom } ${caisseMouvement.opc_client.prenom }</td>
		</tr>
	</c:if>	
	<c:if test="${caisseMouvement.opc_employe != null }">	
		<tr>
			<td><std:label classStyle="control-label col-md-3" valueKey="caisseMouvement.opc_employe" /></td>
			<td colspan="3">${caisseMouvement.opc_employe.nom } ${caisseMouvement.opc_employe.prenom }</td>
		</tr>
	</c:if>	
	<tr><td colspan="4"><hr style="margin-top: 5px;margin-bottom: 0px;"></td></tr>
	<tr>
		<td><std:label classStyle="control-label col-md-3" value="Montant cmd" />
		<td style="color: blue;font-weight: bold;">
			<fmt:formatDecimal value="${caisseMouvement.mtt_commande }" /> 
		</td>
		<td><std:label classStyle="control-label col-md-3" value="Montant net" /></td>
		<td style="font-weight: bold;font-size:16px;color: #630767;">
			<fmt:formatDecimal value="${caisseMouvement.mtt_commande_net }" /> 
		</td>
	</tr>	
	</table>
</div>