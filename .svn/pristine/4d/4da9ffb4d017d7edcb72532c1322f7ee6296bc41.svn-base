<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="framework.model.common.util.ServiceUtil"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
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
		<li>Gestion des vacances</li>
		<li>Fiche des vacances</li>
		<li class="active">Recherche</li>
	</ul>
</div>
<!-- /Page Breadcrumb -->
<!-- Page Header -->
<div class="page-header position-relative">
 <div class="header-title" style="padding-top: 4px;">
          <std:linkPopup actionGroup="C" classStyle="btn btn-default" action="admin.vacance.work_init_create" icon="fa-3x fa-plus" tooltip="Cr&eacute;er" />
          
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
			<!-- Liste des patients -->
			<cplx:table name="list_vacance" transitionType="simple" showDataState="true" checkable="false" autoHeight="true" width="100%" title="Liste des vacance" initAction="admin.vacance.work_find">
				<cplx:header>
					<cplx:th type="empty" />
					<cplx:th type="date" value="Date début" field="vacance.date_début" width="120"/>
					<cplx:th type="date" value="Date fin" field="vacance.date_fin" width="120"/>
					<cplx:th type="boolean" value="Permanant" field="vacance.is_permanant" width="120" />
					<cplx:th type="long" value="Remplacant" field="vacance.opc_remplacant" />
					<cplx:th type="empty" width="90"/>
				</cplx:header>
				<cplx:body>
					<c:forEach items="${list_vacance }" var="vacance">
						<cplx:tr workId="${vacance.id }" >
							<cplx:td>
								<work:edit-link />
							</cplx:td>
							<cplx:td value="${vacance.date_debut}"></cplx:td>
							<cplx:td value="${vacance.date_fin}"></cplx:td>
							<cplx:td align="center" value="${vacance.is_permanant}"></cplx:td>
							<cplx:td value="${vacance.opc_remplacant.nom} ${vacance.opc_remplacant.prenom}"></cplx:td>
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
