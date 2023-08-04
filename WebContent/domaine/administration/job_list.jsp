<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="framework.model.util.audit.ReplicationGenerationEventListener"%>
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
		<li>Modules avancés</li>
		<li class="active">Traitements</li> 
	</ul>
</div>
<!-- /Page Breadcrumb -->
<!-- Page Header -->
<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
	
	<% if(ContextAppli.getUserBean().getIs_RemoteAdmin() && !ContextAppli.IS_CLOUD_MASTER()){ %>
		<std:link actionGroup="C" classStyle="btn btn-default" value="Tâches SuperAdmin" action="admin.requeteur.initAdminTasks" icon="fa fa-random" tooltip="Tâches SuperAdmin" />
	<%} %>	
	
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
			<!-- Liste des jobs-->
			<cplx:table name="list_job" transitionType="simple" width="100%" title="Liste des traitements" initAction="admin.job.work_find">
				<cplx:header>
					<cplx:th type="empty" />
					<cplx:th type="dateTime" value="Date" field="job.start_date" width="200" />
					<cplx:th type="string" value="Libell&eacute;" field="job.job_label" />
					<cplx:th type="string" value="Etat" field="job.statut" width="150" />
					<cplx:th type="empty"/>
				</cplx:header>
				<cplx:body>
					<c:forEach items="${list_job }" var="job">
						<cplx:tr workId="${job.id }">
							<cplx:td>
								<std:linkPopup action="admin.job.editDetail" workId="${job.id }" classStyle="btn btn-sm btn-primary" icon="fa fa-eye" />
							</cplx:td>
							<cplx:td value="${job.start_date}"></cplx:td>
							<cplx:td value="${job.job_label}"></cplx:td>
							<cplx:td align="center">
								<span style="font-size: 11px !important;font-weight: bold;" class="badge badge-${job.statut=='T'?'green':'red'}">
									${job.statut == 'T' ? 'Termin&eacute;' : 'Erreur'}
								</span>
							</cplx:td>
							<cplx:td align="center">
								<work:delete-link/>
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
