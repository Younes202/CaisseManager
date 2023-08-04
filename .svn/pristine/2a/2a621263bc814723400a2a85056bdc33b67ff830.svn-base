<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="framework.model.common.util.DateUtil"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>

<%@page errorPage="/error.jsp"%>

<%
List<Map<String,String>> listInfos = (List<Map<String,String>>)request.getAttribute("appliInfos");
%>
		
<style>
.morris-hover {
	position: absolute;
	z-index: 1000
}

.morris-hover.morris-default-style {
	border-radius: 10px;
	padding: 6px;
	color: #666;
	background: rgba(255, 255, 255, 0.8);
	border: solid 2px rgba(230, 230, 230, 0.8);
	font-family: sans-serif;
	font-size: 12px;
	text-align: center
}

.morris-hover.morris-default-style .morris-hover-row-label {
	font-weight: bold;
	margin: 0.25em 0
}

.morris-hover.morris-default-style .morris-hover-point {
	white-space: nowrap;
	margin: 0.1em 0
}
</style>


<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li class="active">Tableau de bord</li>
	</ul>
</div>
<!-- /Page Breadcrumb -->
<!-- Page Header -->
<div class="page-header position-relative">
	<div class="header-title">
		<h1 style="color: gray;font-weight: bold !important;">
		Tableau de bord
		</h1>
	</div>
	<!--Header Buttons-->
	<jsp:include page="/WEB-INF/fragment/shortcut.jsp"></jsp:include>
	<!--Header Buttons End-->
</div>
<!-- /Page Header -->
<!-- Page Body -->
<div class="page-body">
<std:form name="search-form">
	<!-- ******************************* the Flash Info chart row ******************************* -->
	<div class="row">
		<div class="col-lg-6 col-md-12 col-sm-12 col-xs-12">
			<div class="well with-header">
				<div class="header bordered-darkorange">Abonnements</div>
					<table class="table table-hover table-striped table-bordered table-condensed">
						<tr>
							<td>Type</td>
							<td>Version</td>
							<td>Abonn&eacute;s</td>
							<td>Abonn&eacute;s actifs</td>
							<td>Etablissements</td>
							<td>Etablissements actifs</td>
						</tr>
					<%for(Map<String, ?> mapInfos : listInfos){ %>
						<tr>
							<td style="font-weight: bold;color: blue;"><%=StringUtil.getValueOrEmpty(mapInfos.get("type_appli")) %></td>
							<td style="text-align: right;"><%=StringUtil.getValueOrEmpty(mapInfos.get("caisse_version")) %></td>
							<td style="text-align: right;"><%=StringUtil.getValueOrEmpty(mapInfos.get("nbr_client")) %></td>
							<td style="text-align: right;"><%=StringUtil.getValueOrEmpty(mapInfos.get("nbr_client_actif")) %></td>
							<td style="text-align: right;"><%=StringUtil.getValueOrEmpty(mapInfos.get("nbr_ets")) %></td>
							<td style="text-align: right;"><%=StringUtil.getValueOrEmpty(mapInfos.get("nbr_ets_actif")) %></td>
						</tr>
					<%} %>	
					</table>
			</div>
		</div>
		
		<div class="col-lg-6 col-md-12 col-sm-12 col-xs-12">
			<div class="well with-header">
				<div class="header bordered-darkorange">Ech&eacute;ances en approche</div>
					<table class="table table-hover table-striped table-bordered table-condensed">
						<tr>
							<td>Client</td>
							<td>Etablissement</td>
							<td>Abonnement</td>
							<td>Date &eacute;ch&eacute;ance</td>
						</tr>
						<c:forEach var="echeance" items="${paiementEnApproche }">
							<tr>
								<td style="font-weight: bold;color: blue;">
									${echeance.opc_etablissement.opc_client.nom } - 
									${echeance.opc_etablissement.opc_client.raison_sociale }
								</td>
								<td style="font-weight: bold;color: olive;">${echeance.opc_etablissement.nom }</td>
								<td style="text-align: center;">${echeance.opc_etablissement.frequence_paie } mois</td>
								<td style="text-align: center;"><fmt:formatDate value="${echeance.date_echeance }"/></td>
							</tr>
						</c:forEach>	
					</table>
			</div>
		</div>
	</div>
	</std:form>
</div>