<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/fn" prefix="fn"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>


<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Modules avanc&eacute;s</li>
		<li>Agencement</li>
		<li class="active">Recherche</li>
	</ul>
</div>
<!-- /Page Breadcrumb -->
<!-- Page Header -->
<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<std:link actionGroup="C" classStyle="btn btn-default" action="admin.agencement.work_init_create" icon="fa-3x fa-plus" tooltip="Cr&eacute;er" />
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
			<!-- Liste des agencements -->
			<cplx:table name="list_agencement" transitionType="simple" width="100%" title="Liste des agencements" initAction="admin.agencement.work_find" checkable="false">
				<cplx:header>
					<cplx:th type="empty" />
					<cplx:th type="string" value="Emplacament" field="agencement.emplacement" />
					<cplx:th type="integer" value="Nbr. éléments" filtrable="false" sortable="false" width="120" />
					<cplx:th type="empty" width="90"/>
				</cplx:header>
				<cplx:body>
					<c:forEach items="${list_agencement }" var="agencement">
						<cplx:tr workId="${agencement.id }">
							<cplx:td>
								<work:edit-link />
							</cplx:td>
							<cplx:td value="${agencement.emplacement}"></cplx:td>
							<cplx:td align="right" style="font-weight:bold;">${fn:length(fn:split(agencement.table_coords, ';')) }</cplx:td>
							<cplx:td align="center">
								<std:link icon="fa fa fa-calendar" classStyle="btn btn-sm btn-default" style="color:${agencement.is_calendrier ? 'green':'#b4adad' }" workId="${agencement.id }" tooltip="Gérer dans le calendrier des réservations" action="admin.agencement.manageCalendrier" />
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
