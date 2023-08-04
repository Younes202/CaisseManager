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
         <li>Comptabilité</li>
         <li>Exercices</li>
         <li class="active">Recherche</li>
     </ul>
 </div>
<!-- /Page Breadcrumb -->
  <!-- Page Header -->
  <div class="page-header position-relative">
      <div class="header-title" style="padding-top: 4px;">
          <std:linkPopup actionGroup="C" classStyle="btn btn-default" action="compta.exercice.work_init_create" icon="fa-3x fa-plus" tooltip="Cr&eacute;er" />
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
	<!-- Liste des exercices -->
	<cplx:table name="list_exercice" checkable="false" transitionType="simple" width="100%" title="Liste des exercices" initAction="compta.exercice.work_find">
		<cplx:header>
			<cplx:th type="empty" />
			<cplx:th type="date" value="Date d&eacute;but" field="exercice.date_debut" width="120"/>
			<cplx:th type="date" value="Date fin" field="exercice.date_fin" width="120"/>
			<cplx:th type="string" value="Libell&eacute;" field="exercice.libelle"/>
			<cplx:th type="string" value="Etat" field="exercice.etat" width="100"/>
			<cplx:th type="empty" width="120" />
		</cplx:header>
		<cplx:body>
			<c:forEach items="${listExercice }" var="exercice">
				<cplx:tr workId="${exercice.id }">
					<cplx:td>
						<work:edit-link-popup/>
					</cplx:td>
					<cplx:td value="${exercice.date_debut}"></cplx:td>
					<cplx:td value="${exercice.date_fin}"></cplx:td>
					<cplx:td value="${exercice.libelle}"></cplx:td>
					<cplx:td align="center" style="font-weight:bold;" value="${exercice.etat=='O' ? 'Ouvert':'Clos'}"></cplx:td>
					<cplx:td align="center">
						<c:choose>
							<c:when test="${exercice.etat=='C' }">
								<std:link action="compta.exercice.annuler_cloture" workId="${exercice.id }" actionGroup="C" icon="fa fa-unlock" classStyle="btn btn-sm btn-success" tooltip="Annuler cl&ocirc;ture" />
							</c:when>
							<c:when test="${exercice.etat=='I' }">
								<std:link action="compta.exercice.ouvrir" workId="${exercice.id }" actionGroup="C" icon="fa fa-unlock" classStyle="btn btn-sm btn-success" tooltip="Ouvrir" />
							</c:when>
							<c:otherwise>
								<std:link action="compta.exercice.cloreExercice" workId="${exercice.id }" actionGroup="C" icon="fa fa-lock" classStyle="btn btn-sm btn-warning" tooltip="Cl&ocirc;turer" />
							</c:otherwise>
						</c:choose>
						<std:linkPopup action="compta.exercice.getChiffresExercice" workId="${exercice.id }" actionGroup="C" icon="fa fa-navicon" classStyle="btn btn-sm btn-primary" tooltip="D&eacute;tails de l'exercice" />
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