<%@page import="framework.controller.Context"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@page errorPage="/commun/error.jsp"%>

<%
boolean isRemboursementDroit = Context.isOperationAvailable("REMBFRAIS");
%>

<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Gestion des frais RH</li>
		<li>Fiche des frais RH</li>
		<li class="active">Recherche</li>
	</ul>
</div>
<!-- /Page Breadcrumb -->
<!-- Page Header -->
<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<std:link actionGroup="C" classStyle="btn btn-default" action="pers.frais.work_init_create" icon="fa-3x fa-plus" tooltip="Cr&eacute;er" />
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

	<!-- row -->
	<div class="row">
		<std:form name="search-form">
			<!-- Liste des clients -->
			<cplx:table name="list_frais" transitionType="simple" checkable="false" width="100%" title="Liste des frais" initAction="pers.frais.work_find" autoHeight="true">
				<cplx:header>
					<cplx:th type="empty" />
					<cplx:th type="long[]" value="Employ&eacute;" field="frais.opc_employe.id" groupValues="${listeEmploye }" groupKey="id" groupLabel="nom;' ';prenom" filterOnly="true"/>
					<cplx:th type="string" value="Libell&eacute;" field="frais.libelle" />
					<cplx:th type="decimal" value="Montant total" field="frais.mtt_total" width="150"/>
					<cplx:th type="decimal" value="Montant &agrave; rembourser" field="frais.mtt_rembours" width="150"/>
					<cplx:th type="empty" width="120"/>
				</cplx:header>
				<cplx:body>
					<c:set var="oldClient" value="${null }"></c:set>
					<c:set var="isRemboursementDroit" value="<%=isRemboursementDroit %>" />
					
					<c:forEach items="${list_frais }" var="frais">						
						<c:if test="${empty oldClient or oldClient != frais.opc_employe.id }">
						     <tr>
								<td colspan="6" noresize="true" class="separator-group">
									<c:choose>
										<c:when test="${frais.opc_employe.civilite == 'H'}">
											<c:set var="civilite" value="Mr." />
										</c:when>
										<c:otherwise>
											<c:set var="civilite" value="Mme." />
										</c:otherwise>
									</c:choose>
									${civilite} ${frais.opc_employe.nom} ${frais.opc_employe.prenom}
								</td>
							</tr>
						</c:if>
						<c:set var="oldClient" value="${frais.opc_employe.id }"></c:set>
				
						<cplx:tr workId="${frais.id }">
							<cplx:td>
								<work:edit-link />
							</cplx:td>
							<cplx:td value="${frais.libelle}"></cplx:td>
							<cplx:td align="right" value="${frais.mtt_total}"></cplx:td>
							<cplx:td align="right" value="${frais.mtt_rembours}"></cplx:td>
							<cplx:td align="center">
								<c:if test="${frais.statut == null and isRemboursementDroit}">
									<std:linkPopup actionGroup="C" classStyle="btn btn-sm btn-success" action="pers.frais.init_remboursDemande" workId="${frais.id }" icon="fa fa-check" tooltip="Valider la demande" />
									<std:link actionGroup="C" classStyle="btn btn-sm btn-warning" action="pers.frais.rejeterDemande" workId="${frais.id }" icon="fa fa-times" tooltip="Rejeter la demande" />
								</c:if>
								<c:if test="${frais.statut == 'VALIDATED' and isRemboursementDroit}">
									<std:link actionGroup="C" classStyle="btn btn-sm btn-danger" action="pers.frais.annuler_reboursDemande" workId="${frais.id }" icon="fa fa-money" tooltip="Annuler le remboursement" />
								</c:if>
								<c:if test="${frais.statut == null or frais.statut == 'REJECTED'}">
									<work:delete-link />
								</c:if>
							</cplx:td>
						</cplx:tr>
					</c:forEach>
				</cplx:body>
			</cplx:table>
		</std:form>

	</div>
	<!-- end widget content -->

</div>
<!-- end widget div -->
