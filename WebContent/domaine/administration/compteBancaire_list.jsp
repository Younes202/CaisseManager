<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>


<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Finance</li>
		<li>Fiche compte bancaire</li>
		<li class="active">Recherche</li>
	</ul>
</div>
<!-- /Page Breadcrumb -->
<!-- Page Header -->
<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<std:link actionGroup="C" classStyle="btn btn-default" action="admin.compteBancaire.work_init_create" icon="fa-3x fa-plus" tooltip="Cr&eacute;er" />
		|
		<std:linkPopup noJsValidate="true" actionGroup="C" style='margin-left: 10px;' action="admin.dataForm.work_find" icon="fa fa-cogs" tooltip="Données formulaire" value="Formulaire" params="tp=BANQUE" />
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
			<!-- Liste des compteBancaires -->
			<cplx:table name="list_compteBancaire" checkable="false" transitionType="simple" width="100%" titleKey="compteBancaire.list" initAction="admin.compteBancaire.work_find">
				<cplx:header>
					<cplx:th type="empty" />
					<cplx:th type="string" valueKey="compteBancaire.libelle" field="compteBancaire.libelle" />
					<cplx:th type="string" valueKey="compteBancaire.banque" field="compteBancaire.banque" />
					<cplx:th type="string" value="Type" field="compteBancaire.type_compte" groupValues="${typeCompteArray }" />
					<cplx:th type="decimal" valueKey="compteBancaire.solde" field="compteBancaire.mtt_solde" sortable="false" filtrable="false" width="150" />
					<cplx:th type="empty" />
					<cplx:th type="empty" />
				</cplx:header>
				<cplx:body>
					<c:forEach items="${list_compteBancaire }" var="compteBancaire">
						<cplx:tr workId="${compteBancaire.id }" style="${compteBancaire.is_disable?'text-decoration: line-through;':'' }">
							<cplx:td>
								<work:edit-link />
							</cplx:td>
							<cplx:td><b>${compteBancaire.libelle}</b></cplx:td>
							<cplx:td value="${compteBancaire.banque}"></cplx:td>
							<cplx:td value="${compteBancaire.type_compte}"></cplx:td>
							<cplx:td align="right" style="font-weight:bold;color:${compteBancaire.mtt_solde>=0?'green':'red'};">
								${compteBancaire.mtt_solde>=0?'+':''}<fmt:formatDecimal value="${compteBancaire.mtt_solde}"/>
							 </cplx:td>
							 <cplx:td align="center">
							 	<std:link action="admin.compteBancaire.desactiver" workId="${compteBancaire.id }" actionGroup="C" icon="fa ${compteBancaire.is_disable?'fa-unlock':'fa-lock' }" classStyle="btn btn-sm btn-${compteBancaire.is_disable?'success':'warning'}" tooltip="${compteBancaire.is_disable?'Activer':'D&eacute;sactiver'}" />
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
