<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp" %>

<std:form name="search-form">
	<%if(ControllerUtil.getUserAttribute("IS_INITIALIZE_MODE", request) != null){ %>
<div class="widget">
			<div class="row">
				<div class="col-lg-12 col-sm-12 col-xs-12">
					<%
				    request.setAttribute("tabName", "emplacement");
					request.setAttribute("pAction", EncryptionUtil.encrypt("caisse.wizardInstall.initMaintenance"));
					request.setAttribute("sAction", EncryptionUtil.encrypt("stock.famille.work_find"));
					%>
					<jsp:include page="/domaine/administration/wizard/wizard.jsp" />
					<div class="step-content" id="simplewizardinwidget-steps">
					</div>	
				</div>
			</div>
		</div>		
	<%} %>
 <!-- Page Breadcrumb -->
 <div class="page-breadcrumbs breadcrumbs-fixed">
     <ul class="breadcrumb">
         <li>
             <i class="fa fa-home"></i>
             <a href="#">Accueil</a>
         </li>
         <li>Gestion de stock</li>
         <li>Fiche des emplacements</li>
         <li class="active">Recherche</li>
     </ul>
 </div>
<!-- /Page Breadcrumb -->
  <!-- Page Header -->
  <div class="page-header position-relative">
      <div class="header-title" style="padding-top: 4px;">
          <std:linkPopup actionGroup="C" classStyle="btn btn-default" action="stock.emplacement.work_init_create" icon="fa-3x fa-plus" tooltip="Cr&eacute;er" />
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
	<!-- Liste des emplacements -->
	<cplx:table name="list_emplacement" transitionType="simple" width="100%" checkable="false" titleKey="emplacement.list" initAction="stock.emplacement.work_find">
		<cplx:header>
			<cplx:th type="empty" />
			<cplx:th type="string" valueKey="emplacement.titre" field="emplacement.titre"/>
			<cplx:th type="empty" />
			<cplx:th type="empty" />
		</cplx:header>
		<cplx:body>
			<c:forEach items="${list_emplacement }" var="emplacement">
				<cplx:tr workId="${emplacement.id }" style="${emplacement.is_desactive?'text-decoration: line-through;':'' }">
					<cplx:td>
						<work:edit-link-popup/>
					</cplx:td>
					<cplx:td>
						${emplacement.titre}
						<c:if test="${emplacement.is_externe }">
							<i class="fa fa-wifi" style="color: red;" title="Emplacement externe"></i>
						</c:if>
					</cplx:td>
					<cplx:td align="center">
						<c:if test="${emplacement.origine_id == null}" >
					 		<std:link action="stock.emplacement.desactiver" workId="${emplacement.id }" actionGroup="C" icon="fa ${emplacement.is_desactive?'fa-unlock':'fa-lock' }" classStyle="btn btn-sm btn-${emplacement.is_desactive?'success':'warning'}" tooltip="${emplacement.is_desactive?'Activer':'D&eacute;sactiver'}" />
					 	</c:if>
					 </cplx:td>
					<cplx:td align="center">
						<c:if test="${emplacement.origine_id == null}" >
							<work:delete-link />
						</c:if>
					</cplx:td>
				</cplx:tr>
			</c:forEach>
		</cplx:body>
	</cplx:table>

 </div>
					<!-- end widget content -->

				</div>
				 </std:form>			
				
				<!-- end widget div -->