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
		<li>Gestion des type de planning</li>
		<li>Fiche des types de planning</li>
		<li class="active">Recherche</li>
	</ul>
</div>
<!-- /Page Breadcrumb -->
<!-- Page Header -->
<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<std:linkPopup actionGroup="C" classStyle="btn btn-default" action="pers.typePlanning.work_init_create" icon="fa-3x fa-plus" tooltip="Créer" />
		<std:link classStyle="btn btn-default" action="pers.planning.work_find" params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; l'agenda" />
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
			<cplx:table name="list_typePlanning" transitionType="simple" checkable="false" width="100%" title="Liste des types de planning" initAction="pers.typePlanning.work_find" autoHeight="true">
				<cplx:header>
					<cplx:th type="empty" />
					<cplx:th type="string" value="Libellé" field="typePlanning.libelle" />
					<cplx:th type="boolean" value="Situation client" field="typePlanning.is_situation_cli" width="50" />
					<cplx:th type="integer" value="Durée" field="typePlanning.duree" width="80" />
					<cplx:th type="decimal" value="Couleur" field="typePlanning.color" width="120"/>
					<cplx:th type="empty"/>
				</cplx:header>
				<cplx:body>
					<c:forEach items="${list_typePlanning }" var="typePlanning">						
						<cplx:tr workId="${typePlanning.id }">
							<cplx:td>
								<work:edit-link-popup />
							</cplx:td>
							<cplx:td value="${typePlanning.libelle}"></cplx:td>
							<cplx:td align="center" value="${typePlanning.is_situation_cli}"></cplx:td>
							<cplx:td align="center" value="${typePlanning.duree}"></cplx:td>
							<cplx:td align="center" style="background-color:${typePlanning.color};">
							</cplx:td>
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
