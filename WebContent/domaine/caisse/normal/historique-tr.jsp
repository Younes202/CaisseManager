<%@page import="appli.model.domaine.vente.persistant.CaisseMouvementOffrePersistant"%>
<%@page import="appli.model.domaine.vente.persistant.CaisseMouvementPersistant"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp" %>

<style>
.sortable tr {
	height: 15px;
}
</style>

<div class="row" style="border-radius: 15px;width: 80%;background-color: white;">
	<div class="form-group" style="margin-bottom: 0px;">
		<std:label classStyle="control-label col-md-3" value="Type" />
		<div class="col-md-3"><b>
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
		</div>
		<std:label classStyle="control-label col-md-3" valueKey="caisseMouvement.mode_paiement" />
		<div class="col-md-3">
			<b>${caisseMouvement.mode_paiement }</b>
		</div>
	</div>	
	<hr style="margin-top: 5px;margin-bottom: 0px;">
	<div class="form-group" style="margin-bottom: 0px;">
		<std:label classStyle="control-label col-md-3" value="Esp&egrave;ces" />
		<div class="col-md-3">
			<fmt:formatDecimal value="${caisseMouvement.mtt_donne }"/>
		</div>
		<std:label classStyle="control-label col-md-3" value="Ch&egrave;que" />
		<div class="col-md-3">
			<fmt:formatDecimal value="${caisseMouvement.mtt_donne_cheque }"/>
		</div>
	</div>
	<div class="form-group" style="margin-bottom: 0px;">
		<std:label classStyle="control-label col-md-3" value="Carte" />
		<div class="col-md-3">
			<fmt:formatDecimal value="${caisseMouvement.mtt_donne_cb }"/>
		</div>
		<std:label classStyle="control-label col-md-3" value="Ch&egrave;que d&eacute;j." />
		<div class="col-md-3">
			<fmt:formatDecimal value="${caisseMouvement.mtt_donne_dej }"/>
		</div>
	</div>
	<div class="form-group" style="margin-bottom: 0px;">
		<std:label classStyle="control-label col-md-3" value="Offert" /> 
		<div class="col-md-3">
			<fmt:formatDecimal value="${caisseMouvement.mtt_art_offert }" /> 
		</div>	
	</div>
	<div class="form-group" style="margin-bottom: 0px;">
		<std:label classStyle="control-label col-md-3" value="R&eacute;duction Cmd" /> 
		<div class="col-md-3">
			<fmt:formatDecimal value="${caisseMouvement.mtt_reduction }" /> 
		</div>	
		<std:label classStyle="control-label col-md-3" value="R&eacute;duction Art" />
		<div class="col-md-3">
			<fmt:formatDecimal value="${caisseMouvement.mtt_art_reduction }"/>
		</div>
	</div>
	<div class="form-group" style="margin-bottom:0px;">	
	<c:if test="${caisseMouvement.opc_client != null }">	
		<std:label classStyle="control-label col-md-3" valueKey="caisseMouvement.opc_client" />
		<div class="col-md-6">
			${caisseMouvement.opc_client.nom } ${caisseMouvement.opc_client.prenom }
		</div>
	</c:if>	
	<c:if test="${caisseMouvement.opc_employe != null }">	
		<std:label classStyle="control-label col-md-3" valueKey="caisseMouvement.opc_employe" />
		<div class="col-md-6">
			${caisseMouvement.opc_employe.nom } ${caisseMouvement.opc_employe.prenom }
		</div>
	</c:if>	
	</div>
	<hr style="margin-top: 5px;margin-bottom: 0px;">
	<div class="form-group" style="margin-bottom: 0px;">
		<std:label classStyle="control-label col-md-3" value="Montant cmd" />
		<div class="col-md-3" style="color: blue;font-weight: bold;">
			<fmt:formatDecimal value="${caisseMouvement.mtt_commande }" /> 
		</div>
		<std:label classStyle="control-label col-md-3" value="Montant net" />
		<div class="col-md-3" style="font-weight: bold;font-size:16px;color: #630767;">
			<fmt:formatDecimal value="${caisseMouvement.mtt_commande_net }" /> 
		</div>	
	</div>
</div>