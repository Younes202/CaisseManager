<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@page errorPage="/commun/error.jsp"%>

<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Gestion des type de frais</li>
		<li>Fiche des types de frais</li>
		<li class="active">Recherche</li>
	</ul>
</div>
<!-- /Page Breadcrumb -->
<!-- Page Header -->
<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<std:linkPopup actionGroup="C" classStyle="btn btn-default" action="pers.typeFrais.work_init_create" icon="fa-3x fa-plus" tooltip="Cr&eacute;er" />
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
			<cplx:table name="list_typeFrais" transitionType="simple" checkable="false" width="100%" title="Liste des types de frais" initAction="pers.typeFrais.work_find" autoHeight="true">
				<cplx:header>
					<cplx:th type="empty" />
					<cplx:th type="string" value="Libell&eacute;" field="typeFrais.libelle" />
					<cplx:th type="decimal" value="Plafond" field="typeFrais.montant_max" width="120"/>
					<cplx:th type="empty"/>
				</cplx:header>
				<cplx:body>
					<c:forEach items="${list_typeFrais }" var="typeFrais">						
						<cplx:tr workId="${typeFrais.id }">
							<cplx:td>
								<work:edit-link-popup />
							</cplx:td>
							<cplx:td value="${typeFrais.libelle}"></cplx:td>
							<cplx:td align="right" value="${typeFrais.montant_max}"></cplx:td>
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
