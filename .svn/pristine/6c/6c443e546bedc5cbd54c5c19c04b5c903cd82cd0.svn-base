<%@page import="appli.model.domaine.administration.service.IClientService"%>
<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="framework.model.common.util.ServiceUtil"%>
<%@page import="appli.model.domaine.fidelite.service.ICarteFideliteClientService"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<%
boolean isPortefeuille =  StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("PORTEFEUILLE"));
boolean isPoints =  StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("POINTS"));
%>

<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Gestion des personnels</li>
		<li>Fiche des clients</li>
		<li class="active">Recherche</li>
	</ul>
</div>
<!-- /Page Breadcrumb -->
<!-- Page Header -->
<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<std:link actionGroup="C" classStyle="btn btn-default" action="pers.client.work_init_create" icon="fa-3x fa-plus" tooltip="Cr&eacute;er" />
		|
		<std:linkPopup noJsValidate="true" actionGroup="C" style='margin-left: 10px;' action="admin.dataForm.work_find" icon="fa fa-cogs" tooltip="Données formulaire" value="Formulaire" params="tp=CLIENT" />
	</div>
	<!--Header Buttons-->
	<jsp:include page="/WEB-INF/fragment/shortcut.jsp"></jsp:include>
	<!--Header Buttons End-->
</div>
<!-- /Page Header -->

<c:set var="carteFideliteService" value="<%=ServiceUtil.getBusinessBean(ICarteFideliteClientService.class) %>" />

<!-- Page Body -->
<div class="page-body">
	<div class="row">
		<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
	</div>

	<!-- row -->
	<div class="row">
		<std:form name="search-form">
			<!-- Liste des clients -->
			<cplx:table name="list_client" transitionType="simple" showDataState="true" checkable="false" autoHeight="true" width="100%" titleKey="client.list" initAction="pers.client.work_find">
				<cplx:header>
					<cplx:th type="empty" />
					<cplx:th type="string" valueKey="client.numero" field="client.numero" width="120" />
					<cplx:th type="string" value="Nom et pr&eacute;nom" field="client.nom" filtrable="false"/>
					<cplx:th type="string" valueKey="client.nom" field="client.nom" filterOnly="true"/>
					<cplx:th type="string" valueKey="client.prenom" field="client.prenom" filterOnly="true" />
					<cplx:th type="string" valueKey="employe.cin" field="client.cin" width="100" />
					<cplx:th type="string" value="T&eacute;l&eacute;phone" field="client.telephone" width="120" />
					<cplx:th type="string" value="T&eacute;l&eacute;phone2" field="client.telephone2" width="120" />
					<cplx:th type="string" value="Mail" field="client.mail" />
					<cplx:th type="string" valueKey="adresse.adresse" field="client.opc_adresse" sortable="false" filtrable="false" />
					<%if(isPoints){ %>
<%-- 					<cplx:th type="long" value="Points" sortable="false" filtrable="false" width="120" /> --%>
					<cplx:th type="decimal" value="Montant fid&eacute;lit&eacute;" sortable="false" filtrable="false" width="100" />
					<%} %>
					<%if(isPortefeuille){ %>
					<cplx:th type="decimal" value="Montant portefeuille" sortable="false" filtrable="false" width="100" />
					<%}%>
					
					<c:forEach items="${listDataValueForm }" var="data">
						<cplx:th type="string" value="${data.opc_data_form.data_label }" filtrable="false" sortable="false" />
					</c:forEach>
					<cplx:th type="empty" width="90"/>
				</cplx:header>
				<cplx:body>
					<c:set var="clientService" value="<%=ServiceUtil.getBusinessBean(IClientService.class) %>" />
					
					<c:forEach items="${list_client }" var="client">
						<c:set var="listDataVal" value="${clientService.loadDataForm(client.id, 'CLIENT') }" />
					
						<cplx:tr workId="${client.id }" style="${client.is_disable?'text-decoration: line-through;':'' }">
							<cplx:td>
								<work:edit-link />
							</cplx:td>
							<cplx:td>
								${client.numero} 
								${client.list_cartes.size() > 0 ? '<i style="color:#d73d32;" class="fa fa-credit-card"></i>':'' }
							</cplx:td>
							<cplx:td style="font-weight:bold;">
								<c:choose>
									<c:when test="${client.civilite == 'H'}">
										<c:set var="civilite" value="Mr." />
									</c:when>
									<c:otherwise>
										<c:set var="civilite" value="Mme." />
									</c:otherwise>
								</c:choose>
								${civilite} ${client.nom} ${client.prenom}
								<c:if test="${client.signature != null}">  
									<i class="fa fa-info-circle" title="Cr&eacute;&eacute;r par ${client.signature}"></i>
								</c:if> 
							</cplx:td>
							<cplx:td value="${client.cin}"></cplx:td>
							<cplx:td value="${client.telephone}"></cplx:td>
							<cplx:td value="${client.telephone2}"></cplx:td>
							<cplx:td value="${client.mail}"></cplx:td>
							<cplx:td value="${client.getAdressFull()}"></cplx:td>
							<%if(isPoints){ %>
							<cplx:td align="right" style="color:blue;font-weight:bold;" value="${carteFideliteService.getCarteClientActive(client.id).mtt_total}"></cplx:td>
							<%} %>
							<%if(isPortefeuille){ %>
							<cplx:td align="right" style="color:blue;font-weight:bold;" value="${client.solde_portefeuille}"></cplx:td>
							<%} %>
							
							<c:forEach items="${listDataValueForm }" var="dataV">		
								<c:forEach items="${listDataVal }" var="data">
									<c:if test="${dataV.opc_data_form.id==data.opc_data_form.id }">
										<c:set var="currDV" value="${data.data_value }" />
										<c:set var="currAlign" value="${(data.opc_data_form.data_type=='LONG' or data.opc_data_form.data_type=='DECIMAL') ? 'right':'center' }" />
									</c:if>
								</c:forEach>
								<cplx:td align="${currAlign }" value="${currDV }" />
							</c:forEach>
					
							<cplx:td align="center">
							 	<std:link action="pers.client.desactiver" workId="${client.id }" actionGroup="C" icon="fa ${client.is_disable?'fa-unlock':'fa-lock' }" classStyle="btn btn-sm btn-${client.is_disable?'success':'warning'}" tooltip="${client.is_disable?'Activer':'D&eacute;sactiver'}" />
							 	<c:if test="${client.origine_auth == null}" >
									<work:delete-link />
								</c:if>
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
