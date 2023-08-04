<%@page import="framework.controller.ControllerUtil"%>
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
		<li>Modules avanc&eacute:;s</li>
		<li>Utilisateurs</li>
		<li class="active">Recherche</li>
	</ul>
</div>
<std:form name="search-form"> 
<!-- /Page Breadcrumb -->
<!-- Page Header -->
<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		
		<%if(ControllerUtil.getMenuAttribute("tpEmpl", request) == null){ %>
			<std:linkPopup actionGroup="C" classStyle="btn btn-default" style="float:left;" action="admin.user.work_init_create" icon="fa-3x fa-plus" tooltip="Cr&eacute;er" />
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
		
			<!-- Liste des users -->
			<cplx:table name="list_user" showDataState="true" autoHeight="true" checkable="false" transitionType="simple" width="100%" titleKey="user.list" initAction="admin.user.work_find">
				<cplx:header>
					<cplx:th type="empty" />
					<cplx:th type="string" valueKey="user.login" field="user.login" />
					<cplx:th type="string" value="Employé" field="user.opc_employe.nom" />
					<cplx:th type="long[]" valueKey="user.profile" field="user.opc_profile.id" groupValues="${list_profile }" groupKey="id" groupLabel="libelle" filterOnly="true" />
					<cplx:th type="string" valueKey="user.badge" field="user.badge" width="100" />
					<cplx:th type="dateTime" valueKey="user.date_connexion" field="user.date_connexion" width="140" />
					<cplx:th type="boolean" valueKey="user.is_desactive" field="user.is_desactive" width="100" />
					<cplx:th type="empty" />
					<cplx:th type="empty" />
				</cplx:header>
				<cplx:body>
					<c:set var="currProfil" value="${null }" />
					
					<c:forEach items="${list_user }" var="user">
						<c:if test="${empty currProfil or currProfil!=user.opc_profile.id }">
							<tr>
								<td colspan="8" noresize="true" class="separator-group">
									<span class="fa fa-fw fa-folder-open-o separator-icon"></span>  ${user.opc_profile.libelle}
								</td>
							</tr>
						</c:if>
						<c:set var="currProfil" value="${user.opc_profile.id }" />
					
						<cplx:tr workId="${user.id }" style="${user.is_desactive?'text-decoration: line-through;':'' }">
							<cplx:td>
								<work:edit-link-popup />
							</cplx:td>
							<cplx:td value="${user.login}">
								<span style="color: blue;">
									<c:if test="${not empty user.opc_profile2.code }">
										[${user.opc_profile2.libelle }]
									</c:if>
									<c:if test="${not empty user.opc_profile3.code }">
										[${user.opc_profile3.libelle }]
									</c:if>
								</span>
							</cplx:td>
							<cplx:td value="${user.opc_employe.nom}"></cplx:td>
							<cplx:td align="center" value="${user.badge}"></cplx:td>
							<cplx:td style="color:#d73d32;" value="${user.date_connexion}"></cplx:td>
							<cplx:td align="center" value="${user.is_desactive}"></cplx:td>
							<cplx:td align="center">
							 	<std:link action="admin.user.desactiver" workId="${user.id }" actionGroup="C" icon="fa ${user.is_desactive?'fa-unlock':'fa-lock' }" classStyle="btn btn-sm btn-${user.is_desactive?'success':'warning'}" tooltip="${user.is_desactive?'Activer':'D&eacute;sactiver'}" />
							 </cplx:td>
							<cplx:td align="center">
								<work:delete-link />
							</cplx:td>
						</cplx:tr>
					</c:forEach>
				</cplx:body>
			</cplx:table>
	</div>
	<!-- end widget content -->

</div>
<!-- end widget div -->
</std:form>