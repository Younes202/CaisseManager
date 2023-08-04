<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
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
         <li>Gestion de caisse</li>
         <li>Fiche de la journ&eacute;e de caisse</li>
         <li class="active">Recherche</li>
     </ul>
 </div>
<!-- /Page Breadcrumb -->
  <!-- Page Header -->
  <div class="page-header position-relative">
      <div class="header-title" style="padding-top: 4px;">
          <std:link classStyle="btn btn-default" action="caisse.caisse.work_find" params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la recherche" />
      </div>
      <!--Header Buttons-->
      <div class="header-buttons">
          <a class="sidebar-toggler" href="javascript:">
              <i class="fa fa-arrows-h"></i>
          </a>
          <a class="refresh" id="refresh-toggler" href="javascript:">
              <i class="glyphicon glyphicon-refresh"></i>
          </a>
          <a class="fullscreen" id="fullscreen-toggler" href="javascript:">
              <i class="glyphicon glyphicon-fullscreen"></i>
          </a>
      </div>
      <!--Header Buttons End-->
  </div>
  <!-- /Page Header -->

<!-- Page Body -->
<div class="page-body">
	<div class="row">
		<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
	</div>
<div class="row">
        <div class="col-lg-12 col-sm-6 col-xs-12">
              <div class="tabbable">
                    <ul class="nav nav-tabs" id="myTab">
                          <li >
                              <a data-toggle="tab" href="#descripton" wact="<%=EncryptionUtil.encrypt("caisse.caisse.work_edit")%>" >
                               Fiche
                              </a>
                           </li>
                           <%if(ControllerUtil.getMenuAttribute("caisseId", request)!=null){ %>
                            <li class="active">
                              <a data-toggle="tab" href="#journeeCaisse" wact="<%=EncryptionUtil.encrypt("caisse.caisseJournee.work_find")%>">
                               Journ&eacute;es caisse
                              </a>
                            </li>
                           <%} %>
                     </ul>

	<!-- row -->
<div class="tab-content">
<std:form name="search-form">
	<!-- Liste des caisses -->
	<cplx:table name="list_caisseJournee" transitionType="simple" width="100%" titleKey="caisseJournee.list" initAction="caisse.caisseJournee.work_find">
		<cplx:header>
			<cplx:th type="empty" />
			<cplx:th type="date" valueKey="caisseJournee.date_journee" field="caisseJournee.opc_journee.date_journee" width="120"/>
			<cplx:th type="string" value="Heures" width="180" sortable="false" filtrable="false"/>
			<cplx:th type="string" valueKey="caisseJournee.opc_statut_journee_enum" field="caisseJournee.statut_caisse" groupValues="${statCaisse }"/>
			<cplx:th type="decimal" valueKey="caisseJournee.mtt_ouverture" field="caisseJournee.mtt_ouverture" width="150"/>
			<cplx:th type="decimal" valueKey="caisseJournee.mtt_calcule" field="caisseJournee.mtt_calcule" width="150"/>
			<cplx:th type="decimal" valueKey="caisseJournee.mtt_espece" field="caisseJournee.mtt_espece" width="150"/>
			<cplx:th type="decimal" valueKey="caisseJournee.mtt_cheque" field="caisseJournee.mtt_cheque" width="150"/>
			<cplx:th type="decimal" valueKey="caisseJournee.mtt_cb" field="caisseJournee.mtt_cb" width="150"/>
			<cplx:th type="decimal" valueKey="caisseJournee.mtt_cloture" field="caisseJournee.mtt_cloture" width="150"/>
			<cplx:th type="empty" />
		</cplx:header>
		<cplx:body>
			<c:forEach items="${list_caisseJournee }" var="caisseJournee">
				<cplx:tr workId="${caisseJournee.id }">
					<cplx:td>
						<c:if test="${caisseJournee.statut_caisse =='C' }">
							<work:edit-link/>
						</c:if>
						<c:if test="${caisseJournee.statut_caisse !='C' }">
							<work:edit-link-popup/>
						</c:if>
					</cplx:td>
					<cplx:td value="${caisseJournee.opc_journee.date_journee}"></cplx:td>
					<cplx:td>
						<fmt:formatDate value="${caisseJournee.date_ouverture}" pattern="HH:mm:ss"/>
						<c:if test="${caisseJournee.statut_caisse =='C' }">
							->
							<fmt:formatDate value="${caisseJournee.date_cloture}" pattern="HH:mm:ss"/>
						</c:if>
					</cplx:td>
                    
                    <cplx:td align="center">
	                    <c:choose>
							<c:when test="${caisseJournee.statut_caisse =='O' }">
								<span class="label" style="font-weight: bold; color: green;">Ouverte</span>
							</c:when>
							<c:when test="${caisseJournee.statut_caisse =='E' }">
								<span class="label" style="font-weight: bold; color:orange;">Cl&ocirc;tur&eacute;e</span>
							</c:when>
							<c:when test="${caisseJournee.statut_caisse =='C' }">
								<span class="label" style="font-weight: bold; color:red;">Cl&ocirc;tur&eacute;e</span>
							</c:when>
						</c:choose>
                    </cplx:td>
                    <cplx:td align="right" value="${caisseJournee.mtt_ouverture}"></cplx:td>
                    <cplx:td align="right" value="${caisseJournee.mtt_calcule}"></cplx:td>
                    <cplx:td align="right" value="${caisseJournee.mtt_espece}"></cplx:td>
                    <cplx:td align="right" value="${caisseJournee.mtt_cheque}"></cplx:td>
                    <cplx:td align="right" value="${caisseJournee.mtt_cb}"></cplx:td>
                    <cplx:td align="right" value="${caisseJournee.mtt_cloture}"></cplx:td>
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
</div>
