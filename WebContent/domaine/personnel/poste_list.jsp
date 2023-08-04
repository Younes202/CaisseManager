<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp" %>


 <!-- Page Breadcrumb -->
 <div class="page-breadcrumbs breadcrumbs-fixed"> 
     <ul class="breadcrumb">
         <li>
             <i class="fa fa-home"></i>
             <a href="#">Accueil</a>
         </li>
         <li>Gestion du personnel</li>
         <li>Fiche des postes</li>
         <li class="active">Recherche</li>
     </ul>
 </div>
<!-- /Page Breadcrumb -->
  <!-- Page Header -->
  <div class="page-header position-relative">
      <div class="header-title" style="padding-top: 4px;">
          <std:linkPopup actionGroup="C" classStyle="btn btn-default" action="pers.poste.work_init_create" icon="fa-3x fa-plus" tooltip="Cr&eacute;er" />
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
	<!-- Liste des postes -->
	<cplx:table name="list_poste" checkable="false" transitionType="simple" width="100%" titleKey="poste.list" initAction="pers.poste.work_find">
		<cplx:header>
			<cplx:th type="empty" />
			<cplx:th type="string" valueKey="poste.intitule" field="poste.intitule"/>
			<cplx:th type="decimal" width="120" value="Traif référence" field="poste.tarif"/>
			<cplx:th type="string" width="120" value="Mode travail" field="poste.mode_paie"/>
			<cplx:th type="empty" />
		</cplx:header>
		<cplx:body>
			<c:forEach items="${list_poste }" var="poste">
				<cplx:tr workId="${poste.id }">
					<cplx:td>
						<work:edit-link-popup/>
					</cplx:td>
					<cplx:td value="${poste.intitule}"></cplx:td>
					<cplx:td value="${poste.tarif}"></cplx:td>
					<cplx:td>
						<c:choose>
							<c:when test="${poste.mode_paie == 'J'}">
								Jour
							</c:when>
							<c:when test="${poste.mode_paie == 'H'}">
								Heure (=${poste.heureParJour})  
							</c:when>
						</c:choose>
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
				