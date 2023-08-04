<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="fn"%>
<%@page errorPage="/commun/error.jsp" %>

 <!-- Page Breadcrumb -->
 <div class="page-breadcrumbs breadcrumbs-fixed">
     <ul class="breadcrumb">
         <li>
             <i class="fa fa-home"></i>
             <a href="#">Accueil</a>
         </li>
         <li>Clients cartes</li>
         <li class="active">Recherche</li>
     </ul>
 </div>
<!-- /Page Breadcrumb -->
  <!-- Page Header -->
  <div class="page-header position-relative">
      <div class="header-title" style="padding-top: 4px;">
      <%if(ControllerUtil.getMenuAttribute("carteId", request) == null){ %>
          <std:linkPopup actionGroup="C" classStyle="btn btn-default" action="fidelite.carteFideliteClient.work_init_create" icon="fa-3x fa-plus" tooltip="Cr&eacute;er" value="Affecter au client" />
       <%} else{ %>
       		<std:link classStyle="btn btn-default" action="fidelite.carteFidelite.work_find" params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la recherche" />
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

<%if(ControllerUtil.getMenuAttribute("carteId", request) != null){ %>
	<div class="row">
        <div class="col-lg-12 col-sm-12 col-xs-12">
              <div class="tabbable">
                    <ul class="nav nav-tabs" id="myTab">
                          <li>
                              <a data-toggle="tab" href="#descripton" wact="<%=EncryptionUtil.encrypt("fidelite.carteFidelite.work_edit")%>" >
                             Description
                              </a>
                           </li>
                            <li class="active">
                              <a data-toggle="tab" href="#client" wact="<%=EncryptionUtil.encrypt("fidelite.carteFideliteClient.work_find")%>">
                               Clients
                              </a>
                            </li>
                     </ul>
                </div>
          </div>
      </div>
<%} %>


	<!-- Liste des articles -->
	<cplx:table name="list_carteFideliteClient" transitionType="simple" width="100%" title="List des cartes" initAction="fidelite.carteFideliteClient.work_find" autoHeight="true" checkable="false">
		<cplx:header>
<%-- 			<cplx:th type="empty" /> --%>
			<cplx:th type="string" value="Client" field="carteFideliteClient.opc_client.nom" />
			<cplx:th type="decimal" value="Montant en cours" field="carteFideliteClient.montant" width="120"/>
			<cplx:th type="boolean" value="Carte active" field="carteFideliteClient.is_active" width="110"/>
			<cplx:th type="empty" width="80"/>
		</cplx:header>
		<cplx:body>
			<c:set var="oldCarte" value="${null }"></c:set>
			<c:forEach items="${list_carteFideliteClient }" var="carteFideliteClient">
				<c:set var="isActive" value="${empty carteFideliteClient.is_active or carteFideliteClient.is_active }"/>
				
				<%if(ControllerUtil.getMenuAttribute("carteId", request) == null){ %>
				<c:if test="${empty(oldCarte) or oldCarte != carteFideliteClient.opc_carte_fidelite.id}">
				     <tr>
						<td colspan="5" noresize="true" class="separator-group">
							<span class="fa fa-fw fa-folder-open-o separator-icon"></span>  ${carteFideliteClient.opc_carte_fidelite.libelle}
						</td>
					</tr>
				</c:if>
				<c:set var="oldCarte" value="${carteFideliteClient.opc_carte_fidelite.id }"></c:set>
				<%} %>
				
				<cplx:tr workId="${carteFideliteClient.id }" style="${isActive?'':'text-decoration: line-through;' }">
<%-- 					<cplx:td> --%>
<%-- 						<work:edit-link/> --%>
<%-- 					</cplx:td> --%>
					<cplx:td>${carteFideliteClient.opc_client.numero}-${carteFideliteClient.opc_client.nom} ${carteFideliteClient.opc_client.prenom}</cplx:td>
					<cplx:td align="right" value="${carteFideliteClient.mtt_total }"></cplx:td>
					<cplx:td align="center" value="${isActive }"></cplx:td>
					 <cplx:td align="center">
					 	<std:link action="fidelite.carteFideliteClient.desactiver" workId="${carteFideliteClient.id }" actionGroup="C" icon="fa ${isActive?'fa-lock':'fa-unlock'}" classStyle="btn btn-sm btn-${isActive?'warning':'success'}" tooltip="${isActive?'D&eacute;sactiver':'Activer'}" />
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
