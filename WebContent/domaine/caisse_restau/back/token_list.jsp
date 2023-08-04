<%@page import="framework.model.common.util.EncryptionUtil"%>
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
         <li>Param&eacute;trage</li>
         <li>Fiche de token</li>
         <li class="active">Recherche</li>
     </ul>
 </div>
<!-- /Page Breadcrumb -->
  <!-- Page Header -->
  <div class="page-header position-relative">
      <div class="header-title" style="padding-top: 4px;">
          <std:linkPopup actionGroup="C" classStyle="btn btn-default" action="admin.token.work_init_create" icon="fa-3x fa-plus" tooltip="Cr&eacute;er" />
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

	
	<div class="row">
        <div class="col-lg-12 col-sm-12 col-xs-12">
           <div class="tabbable">
			 <ul class="nav nav-tabs" id="myTab">
			 	<%request.setAttribute("mnu_param", "token"); %> 
			 	<jsp:include page="/domaine/caisse/back-office/parametrage/parametrage_headers.jsp" />
          	</ul>

	<div class="tab-content">
<std:form name="search-form">
	<!-- Liste des tokens -->
	<cplx:table name="list_token" transitionType="simple" width="100%" titleKey="token.list" initAction="admin.token.work_find">
		<cplx:header>
			<cplx:th type="empty" />
			
			<cplx:th type="string" valueKey="token.reference" field="token.reference" width="200"/>
			<cplx:th type="string" valueKey="token.libelle" field="token.libelle" width="250"/>
			<cplx:th type="string" valueKey="token.opc_user" field="token.opc_user.login"/>
			<cplx:th type="empty" width="150"/>
			<cplx:th type="empty"/>
		</cplx:header>
		<cplx:body>
			<c:forEach items="${list_token }" var="token">
				<cplx:tr workId="${token.id }">
					<cplx:td>
						<work:edit-link-popup/>
					</cplx:td>
					<cplx:td value="${token.reference}"></cplx:td>
					<cplx:td value="${token.libelle}"></cplx:td>
					<cplx:td value="${token.opc_user.login}"></cplx:td>
					<cplx:td>
						<c:if test="${token.is_actif == false}">
							<std:button actionGroup="M" classStyle="btn btn-success btn-sm" action="admin.token.activer_desactiver" workId="${token.id }" value="Activer" />
						</c:if>
						<c:if test="${token.is_actif == true}">
							<std:button actionGroup="M" classStyle="btn btn-warning btn-sm" action="admin.token.activer_desactiver" workId="${token.id }" value="Desactiver" />
						</c:if>
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
      	</div>
	</div>
 </div>
					<!-- end widget content -->

			</div>
				<!-- end widget div -->