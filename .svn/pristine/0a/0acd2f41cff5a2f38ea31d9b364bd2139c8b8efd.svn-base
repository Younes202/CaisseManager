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
         <li>Gestion d'agenda</li>
         <li>Fiche des lieux de consultation</li>
         <li class="active">Recherche</li>
     </ul>
 </div>
<!-- /Page Breadcrumb -->
  <!-- Page Header -->
  <div class="page-header position-relative">
      <div class="header-title" style="padding-top: 4px;">
          <std:linkPopup actionGroup="C" classStyle="btn btn-default" action="admin.lieux.work_init_create" icon="fa-3x fa-plus" tooltip="Cr&eacute;er" />
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
	<!-- Liste des emplacements -->
	<cplx:table name="list_lieux" transitionType="simple" width="100%" checkable="false" title="Lieux de travail" initAction="admin.lieux.work_find">
		<cplx:header>
			<cplx:th type="empty" />
			<cplx:th type="string" value="Libellé" field="lieux.libelle"/>
			<cplx:th type="string" value="Adresse" field="lieux.adresse_rue" />
			<cplx:th type="string" value="Téléphone" field="lieux.telephone" width="130" />
			<cplx:th type="string" value="Portable" field="lieux.portable" width="130" />
			<cplx:th type="string" value="Mail" field="lieux.mail" />
			<cplx:th type="string" value="Site" field="lieux.site" />
			<cplx:th type="empty" />
		</cplx:header>
		<cplx:body>
			<c:forEach items="${list_lieux }" var="lieux">
				<cplx:tr workId="${lieux.id }">
					<cplx:td>
						<work:edit-link-popup/>
					</cplx:td>
					<cplx:td value="${lieux.libelle}" />
					<cplx:td value="${lieux.getAdressFull()}" />
					<cplx:td value="${lieux.telephone}" style="color:blue;text-align:center;" />
					<cplx:td value="${lieux.portable}" style="color:blue;text-align:center;" />
					<cplx:td value="${lieux.mail}" />
					<cplx:td value="${lieux.site}" />
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