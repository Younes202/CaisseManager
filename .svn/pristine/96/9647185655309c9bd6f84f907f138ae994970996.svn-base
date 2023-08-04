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
         <li>Gestion des messages</li>
         <li>Fiche message</li>
         <li class="active">Recherche</li>
     </ul>
 </div>
<!-- /Page Breadcrumb -->
  <!-- Page Header -->
  <div class="page-header position-relative">
      <div class="header-title" style="padding-top: 4px;">
          <std:linkPopup actionGroup="C" classStyle="btn btn-default" action="admin.message.work_init_create" icon="fa-3x fa-plus" tooltip="Ajouter" />
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
			<!-- Liste des employes -->
			<cplx:table name="list_message" transitionType="simple" width="100%" title="Applications" initAction="admin.message.work_find" autoHeight="true">
				<cplx:header>
					<cplx:th type="empty" />
					<cplx:th type="string" value="Date" field="message.date_envoi" width="200" />
					<cplx:th type="string" value="Sujet" field="message.sujet" width="200" />
					<cplx:th type="string" value="Destinataires"  width="150" sortable="false" filtrable="false" />
					<cplx:th type="empty" />
				</cplx:header>
				<cplx:body>
				
					<c:forEach items="${list_message }" var="message">
						<cplx:tr workId="${message.id }">
							<cplx:td>
								<work:edit-link-popup />
							</cplx:td>
							<cplx:td value="${message.date_envoi}"></cplx:td>
							<cplx:td value="${message.sujet}"></cplx:td>
							<cplx:td value="${message.getDestinataires()}"></cplx:td>
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
