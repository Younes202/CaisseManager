<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>


<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Finance</li>
		<li>Fiche mouvement de fonds</li>
		<li class="active">Recherche</li>
	</ul>
</div>
<!-- /Page Breadcrumb -->
<!-- Page Header -->
<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<std:linkPopup actionGroup="C" classStyle="btn btn-default" action="admin.compteBancaireFonds.work_init_create" icon="fa-3x fa-plus" tooltip="Cr&eacute;er" />
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
			<!-- Liste des compteBancaireFondss -->
			<cplx:table name="list_compteBancaireFonds" transitionType="simple" width="100%" title="Mouvements de fonds" initAction="admin.compteBancaireFonds.work_find">
				<cplx:header>
					<cplx:th type="empty" />
					<cplx:th type="date" value="Date" field="compteBancaireFonds.date_mouvement" width="100" />
					<cplx:th type="long[]" value="Compte origine" field="compteBancaireFonds.opc_banque_source.id" groupValues="${listeCompteBancaire }" groupKey="id" groupLabel="libelle" />
					<cplx:th type="long[]" value="Compte destination" field="compteBancaireFonds.opc_banque_dest.id" groupValues="${listeCompteBancaire }" groupKey="id" groupLabel="libelle" width="350" />
					<cplx:th type="decimal" value="Montant" field="compteBancaireFonds.montant" width="150" />
					<cplx:th type="empty" />
				</cplx:header>
				<cplx:body>
					<c:forEach items="${list_compteBancaireFonds }" var="compteBancaireFonds">
						<cplx:tr workId="${compteBancaireFonds.id }">
							<cplx:td>
								<work:edit-link-popup />
							</cplx:td>
							<cplx:td value="${compteBancaireFonds.date_mouvement}"></cplx:td>
							<cplx:td value="${compteBancaireFonds.opc_banque_source.libelle}"></cplx:td>
							<cplx:td value="${compteBancaireFonds.opc_banque_dest.libelle}"></cplx:td>
							<cplx:td align="right" style="font-weight:bold;" value="${compteBancaireFonds.montant}"></cplx:td>
							<cplx:td align="center">
								<work:delete-link />
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
