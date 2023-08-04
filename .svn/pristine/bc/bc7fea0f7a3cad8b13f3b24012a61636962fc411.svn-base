<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp" %>

 <!-- Page Breadcrumb -->
 <div class="page-breadcrumbs breadcrumbs-fixed">
     <ul class="breadcrumb">
         <li>
             <i class="fa fa-home"></i>
             <a href="#">Accueil</a>
         </li>
         <li>Gestion de stock</li>
         <li>Fiche des demandes de transfert</li>
         <li class="active">Recherche</li>
     </ul>
 </div>
<!-- /Page Breadcrumb -->
  <!-- Page Header -->
  <div class="page-header position-relative">
      <div class="header-title" style="padding-top: 4px;">
      <%
      if(ContextAppli.getAbonementBean().isOptPlusSyncCentrale()){
      %>
      	  <std:link actionGroup="C" classStyle="btn btn-default" action="stock.demandeTransfert.work_init_create" icon="fa-3x fa-plus" tooltip="Créer" />
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
		<std:form name="search-form">		   
			<cplx:table name="list_demandeTransfert" transitionType="simple" width="100%" forceFilter="true" title="Liste des demandes de transfert" initAction="stock.demandeTransfert.work_find" autoHeight="true" checkable="false">
				<cplx:header>
					<cplx:th type="empty" />
					<cplx:th type="string" value="Date souhaitée" field="demandeTransfert.date_souhaitee" width="150" />
					<cplx:th type="string" value="Date émission" field="demandeTransfert.date_emission" width="150" />
					<cplx:th type="string" value="Login" field="demandeTransfert.login"/>
					<cplx:th type="string" value="Statut" field="demandeTransfert.statut"/>
					<cplx:th type="date" value="Date transfert" field="demandeTransfert.date_transfert" width="150"/>
					<cplx:th type="empty" width="90" />
				</cplx:header>
				<cplx:body>
					<c:set var="oldDate" value="${null }"></c:set>
					<c:forEach items="${list_demandeTransfert }" var="demande">
						<c:if test="${oldDate == null  or oldDate != demande.date_souhaitee }">
							<tr>
								<td colspan="7" class="separator-group">
									<span class="fa fa-fw fa-folder-open-o separator-icon"></span> <fmt:formatDate value="${demande.date_souhaitee }"/>
								</td>
							</tr>	
						</c:if>
						<c:set var="oldDate" value="${demande.date_souhaitee }"></c:set>
					
						<cplx:tr workId="${demande.id }">
							<cplx:td>
								<work:edit-link/>
							</cplx:td>
							<cplx:td>
								<fmt:formatDate value="${demande.date_souhaitee }"></fmt:formatDate>
							</cplx:td>
							<cplx:td >
								<fmt:formatDate value="${demande.date_emission }" pattern="dd/MM/yyyy HH:mm:ss" ></fmt:formatDate>
							</cplx:td>
							<cplx:td value="${demande.login }" />
							<cplx:td>
								<c:if test="${demande.statut == 'TRAITEE' }" >
								 	<span style="color:green;font-weight:bold;">Traitée</span>
								</c:if>
								<c:if test="${demande.statut == 'ENREGISTREE' }" >
								 	<span style="color:blue;font-weight:bold;">Enregistrée</span>
								</c:if>
								<c:if test="${demande.statut == 'ANNULEE' }" >
								 	<span style="color:red;font-weight:bold;">Annulée</span>
								</c:if>
								<c:if test="${demande.statut == 'NON ENREGISTREE' }" >
								 	<span style="color:red;font-weight:bold;">Non enregistrée</span>
								</c:if>
							</cplx:td>
							<cplx:td >
								<fmt:formatDate value="${demande.date_transfert }" pattern="dd/MM/yyyy HH:mm:ss" ></fmt:formatDate>
							</cplx:td>
							<cplx:td align="center">
								<%if(ContextAppli.getAbonementBean().isOptPlusSyncCentrale()){%>
								<c:if test="${demande.statut != 'TRAITEE' and demande.statut != 'NON ENREGISTREE' }" >
									<c:if test="${demande.statut != 'ANNULEE' }" >
										<std:link action="stock.demandeTransfert.annulerDemande" workId="${demande.id }" actionGroup="C" icon="fa fa-close" classStyle="btn btn-sm btn-warning" tooltip="Annuler" />
									</c:if>
								</c:if>
								<c:if test="${empty demande.statut or demande.statut == 'NON ENREGISTREE' }" >
									<std:link action="stock.demandeTransfert.synchroniserDemande" workId="${demande.id }" actionGroup="C" icon="fa fa-share" classStyle="btn btn-sm btn-success" tooltip="Synchroniser" />
									<work:delete-link />
								</c:if>
								<%} else if(ContextAppli.getAbonementBean().isOptPlusEtsCentrale()){ %>
								<c:if test="${empty demande.statut 
											or demande.statut == 'NON ENREGISTREE'
											or demande.statut == 'ENREGISTREE' }" >
									<std:link action="stock.demandeTransfert.init_transfert" workId="${demande.id }" actionGroup="C" icon="fa fa-share" classStyle="btn btn-sm btn-success" tooltip="Transférer" />
								</c:if>
								<%} %>
							</cplx:td>
						</cplx:tr>
					</c:forEach>
				</cplx:body>
			</cplx:table>
		 </std:form>	
	 </div>
</div>
