<%@page import="framework.controller.ControllerUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<script type="text/javascript">
$(document).ready(function() {
	$("#type_offre").change(function(){
		$("#lnk_offre").attr("params", "type_offre="+$("#type_offre").val()).trigger("click");
	});
});
</script>

<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Gestion des personnels</li>
		<li>Fiche des offres</li>
		<li class="active">Recherche</li>
	</ul>
</div>
<!-- /Page Breadcrumb -->
<!-- Page Header -->
<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<std:link actionGroup="C" classStyle="btn btn-default" action="pers.offre.work_init_create" icon="fa-3x fa-plus" tooltip="Créer" />
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
<std:link id="lnk_offre" action="pers.offre.work_find" style="display:none;" />
	<!-- row -->
	<div class="row">
		<std:form name="search-form">
			<!-- Liste des offres -->
			<cplx:table name="list_offre" dragable="true" checkable="false" showDataState="true" transitionType="simple" width="100%" autoHeight="true" title="Liste des offres " initAction="pers.offre.work_find">
				<cplx:header>
					<cplx:th type="empty" />
					<cplx:th type="string" valueKey="offre.libelle" field="offre.libelle" />
					<cplx:th type="date" valueKey="offre.date_debut" field="offre.date_debut" width="120" />
					<cplx:th type="date" valueKey="offre.date_fin" field="offre.date_fin" width="120" />
					<cplx:th type="decimal" valueKey="offre.mtt_seuil" field="offre.mtt_seuil" width="120" />
					<cplx:th type="decimal" valueKey="offre.mtt_plafond" field="offre.mtt_plafond" width="120" />
					<cplx:th type="string" value="Type d'offre" field="offre.type_offre" groupValues="${typeOffreArray }" width="120" />
					<cplx:th type="decimal" valueKey="offre.taux_reduction" field="offre.taux_reduction" width="120" />
					<cplx:th type="empty" width="80" />
				</cplx:header>
				<cplx:body>
					<c:set var="oldDest" value="${null }" />
					
					<c:forEach items="${list_offre }" var="offre">
						<c:set var="currDest" value="${offre.destination!='E' and offre.destination!='C' ? 'A' : offre.destination }" />
						
						<c:if test="${currDest != oldDest }">
							<tr>
								<td colspan="9" noresize="true" class="separator-group" style="padding-left:10px;">
									<c:set var="lib" value="GLOBAL" />
									<c:choose>
										<c:when test="${currDest=='E' }">
											<c:set var="lib" value="EMPLOYÉ" />
										</c:when>
										<c:when test="${currDest=='C' }">
											<c:set var="lib" value="CLIENT" />
										</c:when>
									</c:choose>
									<span class="fa fa-fw fa-folder-open-o separator-icon"></span> ${lib }	
								</td>
							</tr>
						</c:if>
						<c:set var="oldDest" value="${currDest }" /> 
						
						<cplx:tr workId="${offre.id }" style="${offre.is_disable?'text-decoration: line-through;':'' }">
							<cplx:td>
								<work:edit-link />
							</cplx:td>
							<cplx:td value="${offre.libelle}"></cplx:td>
							<cplx:td align="center" value="${offre.date_debut}"></cplx:td>
							<cplx:td align="center" value="${offre.date_fin}"></cplx:td>
							<cplx:td align="right" value="${offre.mtt_seuil}"></cplx:td>
							<cplx:td align="right" value="${offre.mtt_plafond}"></cplx:td>
							<cplx:td style="color:green" value="${offre.type_offre=='P'?'Prix d\\'achat':'Réduction' }"></cplx:td>
							<cplx:td style="color:blue;" align="right" value="${offre.taux_reduction}"></cplx:td>
							<cplx:td align="center">
								<std:link action="pers.offre.desactiver" workId="${offre.id }" actionGroup="C" style="color:${offre.is_disable?'green':'orange'};" icon="fa ${offre.is_disable?'fa-unlock':'fa-lock' }" classStyle="btn btn-sm btn-primary" tooltip="${offre.is_disable?'Activer':'Désactiver'}" />
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
