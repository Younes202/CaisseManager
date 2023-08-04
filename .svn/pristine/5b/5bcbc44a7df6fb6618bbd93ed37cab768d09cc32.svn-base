<%@page import="framework.controller.ControllerUtil"%>
<%@page import="appli.model.domaine.vente.persistant.JourneePersistant"%>
<%@page import="framework.model.common.util.DateUtil"%>
<%@page import="java.util.Date"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@page errorPage="/commun/error.jsp"%>

<%
JourneePersistant journeeVente = (JourneePersistant) request.getAttribute("journeeView");
boolean isJourneeOuverte = (journeeVente != null && "O".equals(journeeVente.getStatut_journee()));
%>
<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Gestion de stock</li>
		<li>Fiche des retours clients</li>
		<li class="active">Recherche</li>
	</ul>
</div>
<!-- /Page Breadcrumb -->
<!-- Page Header -->
<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">

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
	<!-- widget grid -->
	<div class="widget">
		<std:form name="data-form">
			<div class="row">
				<div class="col-lg-12 col-sm-12 col-xs-12">
					<div class="widget-main ">
						<div class="tabbable">
							<div class="tabbable">
								<div class="row">
							        <div class="form-group">
							        	<std:label classStyle="control-label col-md-2" value="Date début" />
							            <div class="col-md-2">
							                 <std:date name="dateDebut" required="true" value="${dateDebut }"/>
							            </div>
							            <div class="col-md-2" style="text-align: center;">
							            	<std:link action="stock-caisse.mouvementStock.findRetourCmdCaisse" params="prev=1" icon="fa fa-arrow-circle-left" tooltip="Journée précédente" />
							            	<std:link action="stock-caisse.mouvementStock.findRetourCmdCaisse" params="next=1" icon="fa fa-arrow-circle-right" tooltip="Journée suivante" />
							            </div>
							            
							            <std:label classStyle="control-label col-md-1" value="Date fin" />
							            <div class="col-md-2">
							                 <std:date name="dateFin" required="true" value="${dateFin }"/>
							            </div>
							            <div class="col-md-2">
							           	 	<std:button action="stock-caisse.mouvementStock.findRetourCmdCaisse" value="Filtrer" classStyle="btn btn-primary" />
							           	 </div>	
							       </div>
							   </div>
			<div class="tab-content">		
			<!-- Liste des clients -->
			<cplx:table name="list_retourClient" transitionType="simple" checkable="false" width="100%" title="Liste des retours des clients" initAction="stock-caisse.mouvementStock.findRetourCmdCaisse" autoHeight="true">
				<cplx:header>
					<cplx:th type="empty" />
					<cplx:th type="date" value="Date" field="caisseMouvement.date_vente" width="120" filterOnly="true"/>
					<cplx:th type="string" value="Référence" field="caisseMouvement.ref_commande" width="180"/>
					<cplx:th type="string" value="Paiement" field="caisseMouvement.mode_paiement" width="250"/>
					<cplx:th type="decimal" value="Montant" field="caisseMouvement.mtt_commande_net" width="120"/>
					<cplx:th type="long[]" value="Client" field="caisseMouvement.opc_client.id" groupValues="${listClient }" groupKey="id" groupLabel="nom;' ';prenom"/>
					<cplx:th type="long[]" value="Caissier" field="caisseMouvement.opc_user.id" groupValues="${listUser }" groupKey="id" groupLabel="login"/>
					<cplx:th type="empty"/>
				</cplx:header>
				<cplx:body>
					<c:set var="oldDate" value="${null }"></c:set>
					<c:set var="dateUtil" value="<%=new DateUtil() %>" />
					<c:forEach items="${list_retourClient }" var="caisseMouvement">		
						<c:if test="${oldDate == null  or oldDate != dateUtil.dateToString(caisseMouvement.date_vente, 'dd-MM-yyyy') }">
							<tr>
								<td colspan="7" noresize="true" class="separator-group">
									<span class="fa fa-fw fa-folder-open-o separator-icon"></span>  <fmt:formatDate value="${caisseMouvement.date_vente}"/>
								</td>
							</tr>	
						</c:if>
						<c:set var="oldDate" value="${dateUtil.dateToString(caisseMouvement.date_vente, 'dd-MM-yyyy') }"></c:set>				
			
						<cplx:tr workId="${caisseMouvement.id }">
							<cplx:td>
								<std:linkPopup classStyle="btn btn-sm btn-primary" action="caisse.journee.edit_mouvement" workId="${caisseMouvement.id }">
										<span class="fa fa-eye"></span>
								</std:linkPopup>
							</cplx:td>
							<cplx:td value="${caisseMouvement.ref_commande }"></cplx:td>
							<cplx:td value="${caisseMouvement.mode_paiement }"></cplx:td>
							<cplx:td align="right" style="font-weight:bold;" value="${caisseMouvement.mtt_commande_net }" />
							<cplx:td value="${caisseMouvement.opc_client.nom }" />
							<cplx:td value="${caisseMouvement.opc_user.login }" />
							<cplx:td align="center">
								<work:delete-link />
							</cplx:td>
						</cplx:tr>
					</c:forEach>
					<c:if test="${list_retourClient.size() > 0 }">
						<tr class="sub">
							<td colspan="3"></td>
							<td align="right">
								<span style="font-size: 14px !important;font-weight: bold;height: 28px;" class="badge badge-blue">
									<fmt:formatDecimal value="${totalTtc }"/>
								</span>
							</td>
							<td colspan="3"></td>
						</tr>
					</c:if>
				</cplx:body>
			</cplx:table>
			
			</div>
			</div>
			</div>
			</div>
			</div>
			</div>
		</std:form>
</div></div>