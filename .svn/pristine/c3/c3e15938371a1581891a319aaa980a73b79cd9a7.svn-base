<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli.TYPE_CAISSE_ENUM"%>
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
		<li>Profile</li>
		<li class="active">Recherche</li>
	</ul>
</div>
<!-- /Page Breadcrumb -->
<!-- Page Header -->
<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<std:link actionGroup="C" classStyle="btn btn-default" action="hab.profile.work_init_create" icon="fa-3x fa-plus" tooltip="Cr&eacute;er" />
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
			<!-- Liste profiles -->
			<cplx:table name="list_profile" checkable="false" transitionType="simple" autoHeight="true" width="100%" titleKey="profile.list" initAction="hab.profile.work_find">
				<cplx:header>
					<cplx:th type="empty" />
					<cplx:th valueKey="user.profile" field="profile.libelle" type="string" width="150" />
					<cplx:th value="Acc&egrave;s" field="profile.envs" type="string" />
					<cplx:th valueKey="user.is_desactive" field="profile.is_desactive" type="boolean" width="150" />
					<cplx:th type="empty" />
					<cplx:th type="empty" />
				</cplx:header>
				<cplx:body>
					<c:forEach items="${list_profile }" var="profile">
						<cplx:tr workId="${profile.id }" style="${profile.is_desactive?'text-decoration: line-through;':'' }">
							<cplx:td>
								<work:edit-link />
							</cplx:td>
							<cplx:td value="${profile.libelle }"></cplx:td>
							<cplx:td>
								<c:if test="${profile.code != 'ADMIN'}">
									${mapPrfl.get(profile.id) }
								</c:if>
							</cplx:td>
							<cplx:td align="center" value="${profile.is_desactive }"></cplx:td>
							<cplx:td align="center">
								<c:if test="${profile.code != 'ADMIN' }">
								 	<std:link action="hab.profile.desactiver" workId="${profile.id }" actionGroup="C" icon="fa ${profile.is_desactive?'fa-unlock':'fa-lock' }" classStyle="btn btn-sm btn-${profile.is_desactive?'success':'warning'}" tooltip="${profile.is_desactive?'Activer':'D&eacute;sactiver'}" />
							 	</c:if>
							 </cplx:td>
							<cplx:td align="center">
								<c:if test="${profile.code != 'ADMIN' }">
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