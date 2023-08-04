<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp" %>

 <!-- Page Breadcrumb -->
 <div class="page-breadcrumbs breadcrumbs-fixed">
     <ul class="breadcrumb">
         <li>
             <i class="fa fa-home"></i>
             <a href="#">Accueil</a>
         </li>
         <li>Gestion des paiements</li>
         <li class="active">Fiche paiement</li>
     </ul>
 </div>
<!-- /Page Breadcrumb -->
  <!-- Page Header -->
  <div class="page-header position-relative">
	  <div class="header-title" style="padding-top: 4px;">
          <std:linkPopup actionGroup="C" classStyle="btn btn-default" action="admin.etablissementPaiement.work_init_create" icon="fa-3x fa-plus" tooltip="Cr&eacute;er" />
          <std:link actionGroup="C" classStyle="btn btn-success" action="admin.etablissementPaiement.find_dateEcheance" value="Par date échéance" tooltip="Par date" />
      </div>
      <!--Header Buttons-->
      <jsp:include page="/WEB-INF/fragment/shortcut.jsp"></jsp:include>
      <!--Header Buttons End-->
  </div>
  
  <!-- /Page Header -->

<!-- Page Body -->
<div class="page-body">
	<div class="row">
		<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
	</div>

		<!-- widget grid -->
	<div class="widget">
			<div class="widget-body">
			<std:form name="search-form">
				<!-- Liste des employes -->
				<cplx:table name="list_etablissementPaiement" transitionType="simple" width="100%" title="Ech&eacute;ance des paiements" initAction="admin.etablissementPaiement.work_find" checkable="false" autoHeight="true">
					<cplx:header>
						<cplx:th type="empty" />
						<cplx:th type="string" value="Client" field="etablissementPaiement.opc_etablissement.opc_client.raison_sociale" width="200" />
						<cplx:th type="string" value="Etablissement" field="etablissementPaiement.opc_etablissement.nom" width="200" />
						<cplx:th type="string" value="Version" field="etablissementPaiement.opc_etablissement.type_appli" />
						<cplx:th type="decimal" value="Montant abonnement" field="etablissementPaiement.opc_etablissement.mtt_abonnement" />
						<cplx:th type="decimal" value="Montant pay&eacute;" field="etablissementPaiement.mtt_abonnement" />
						<cplx:th type="date" value="Date paiement" field="etablissementPaiement.date_paiement" />
						<cplx:th type="date" value="Date &eacute;ch&eacute;ance" field="etablissementPaiement.date_echeance" />
						<cplx:th type="empty" />
					</cplx:header>
					<cplx:body>
						<c:set var="oldType" value="XX" />
						<c:set var="oldEts" value="XX" />
					
						<c:forEach items="${list_etablissementPaiement }" var="etablissementPaiement">
						
							<c:if test="${oldType=='XX' or etablissementPaiement.opc_etablissement.type_appli!=oldType }">
								<tr style="height: 24px;">
									<td colspan="10" noresize="true" style="font-size: 15px;font-weight: bold;background-color:#eff3c3;color:green;">
										${etablissementPaiement.opc_etablissement.type_appli }
									</td>
								</tr>
								<c:set var="oldEts" value="XX" />
							</c:if>
							<c:if test="${oldEts=='XX' or etablissementPaiement.opc_etablissement.nom!=oldEts }">
								<tr style="height: 24px;">
									<td colspan="10" noresize="true" style="padding-left: 50px;font-size: 15px;font-weight: bold;background-color:#eff3c3;color:#FF5722;">
										${etablissementPaiement.opc_etablissement.nom }
									</td>
								</tr>
							</c:if>
							
							<c:set var="oldType" value="${etablissementPaiement.opc_etablissement.type_appli }" />
							<c:set var="oldEts" value="${etablissementPaiement.opc_etablissement.nom }" />
						
							<cplx:tr workId="${etablissementPaiement.id }">
								<cplx:td><work:edit-link-popup/></cplx:td>
								<cplx:td value="${etablissementPaiement.opc_etablissement.opc_client.nom} (${etablissementPaiement.opc_etablissement.opc_client.raison_sociale})"></cplx:td>
								<cplx:td value="${etablissementPaiement.opc_etablissement.nom}"></cplx:td>
								<cplx:td value="${etablissementPaiement.opc_etablissement.type_appli}"></cplx:td>
								<cplx:td align="right" value="${etablissementPaiement.opc_etablissement.getMontantAbonnementTotal()}"></cplx:td>
								<cplx:td align="right" value="${etablissementPaiement.mtt_abonnement}"></cplx:td>
								<cplx:td align="center" value="${etablissementPaiement.date_paiement}"></cplx:td>
								<cplx:td align="center" value="${etablissementPaiement.date_echeance}"></cplx:td>
								<cplx:td><work:delete-link/></cplx:td>
							</cplx:tr>
						</c:forEach>
					</cplx:body>
				</cplx:table>
			</std:form>
		</div>
	</div>
</div>				