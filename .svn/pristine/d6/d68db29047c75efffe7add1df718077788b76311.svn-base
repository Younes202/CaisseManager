<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="appli.controller.domaine.caisse.ContextAppliCaisse"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.controller.Context"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp" %>

<%
boolean isHistoriqueShow = Context.isOperationCaisseAvailable("DETHISTO", "cai-gestion");
boolean isPrintRaz = StringUtil.isTrueOrNull(""+ControllerUtil.getUserAttribute("RIGHT_IMPRAZ", request));
%>

<script type="text/javascript">
$(document).ready(function (){
	$(document).off('click', "a[id^='lnk_det']");
	$(document).on('click', "a[id^='lnk_det']", function(){
		setTrContent($(this).attr("curr"), "<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.editTrHistorique")%>");
	});
});
</script>

<style>
   .form-title{ 
    	margin-left: 12px;
	}
</style>

<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Livraison</li>
		<li>Sociétés de livraison</li>
		<li class="active">Mouvements</li>
	</ul>
</div>

<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<std:link classStyle="btn btn-default" action="pers.societeLivr.work_find" params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la recherche" />
	</div>
	<!--Header Buttons-->
	<jsp:include page="/WEB-INF/fragment/shortcut.jsp"></jsp:include>
	<!--Header Buttons End-->
</div>
<!-- /Page Header -->

<div class="page-body">
	<div class="row">
		<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
	</div>
	<!-- widget grid -->
	<div class="widget">
		<std:form name="data-form">
			<div class="row">
				<div class="col-lg-12 col-sm-6 col-xs-12">
					<div class="tabbable">
						<ul class="nav nav-tabs" id="myTab">
							<li><a data-toggle="tab" noVal="true" href="#descripton" wact="<%=EncryptionUtil.encrypt("pers.societeLivr.work_edit")%>"> Fiche </a></li>
							<li class="active"><a data-toggle="tab" href="#mouvement" noVal="true" wact="<%=EncryptionUtil.encrypt("pers.societeLivr.find_mvm_societeLivr")%>"> Commandes </a></li>
						</ul>
					</div>
				</div>
			</div>

			<div class="widget-body">
				<div class="row">
				
<!-- Liste des mouvements -->
<cplx:table name="list_societeLivrMvm" transitionType="simple" width="100%" autoHeight="true" title="Historique des commandes" initAction="pers.societeLivr.find_mvm_societeLivr" exportable="false" checkable="false" paginate="false">
	<cplx:header>
	<%if(isHistoriqueShow || isPrintRaz){%>
		<cplx:th type="empty" width="50"/>
	<%} %>
		<cplx:th type="string" value="Commande" field="mouvement.ref_commande" width="210" />
		<%if(isHistoriqueShow || isPrintRaz){%>
		<cplx:th type="decimal" value="Montant" field="mouvement.mtt_commande" width="90"/>
		<cplx:th type="decimal" value="Montant NET" field="mouvement.mtt_commande_net" width="90"/>
		<cplx:th type="decimal" value="Part société" field="mouvement.mtt_livraison_livr" width="90" filtrable="false" sortable="false"/>
		<cplx:th type="decimal" value="Frais livraison" field="mouvement.mtt_livraison_ttl" width="90"/>
		<cplx:th type="decimal" value="Mode paiement" field="mouvement.mode_paiement" width="90" />
		<%} %>
		<cplx:th type="string" value="Statut" align="center" field="mouvement.last_statut"/>
		<cplx:th type="string" value="Call" field="mouvement.num_token_cmd" width="50"/>
		<cplx:th type="string" value="Table" field="mouvement.ref_table" filtrable="false" sortable="false" width="90"/>
		<cplx:th type="string" value="Type" field="mouvement.type_commande"/>
		<%if(isHistoriqueShow || isPrintRaz){%>
			<cplx:th type="empty" width="50"/>
		<%} %>
	</cplx:header>
	<cplx:body>
		<c:forEach items="${list_societeLivrMvm }" var="mouvement">
			<cplx:tr workId="${mouvement.id }">
			<%if(isHistoriqueShow || isPrintRaz){%>
				<cplx:td>
					<std:linkPopup classStyle="btn btn-primary" workId="${mouvement.id}" action="caisse-web.caisseWeb.selectHistorique" icon="fa fa-eye" tooltip="Consulter" />
				</cplx:td>
			<%} %>
				<cplx:td>
					<%if(isHistoriqueShow || isPrintRaz){%>
					<a style="font-weight:bold;${mouvement.is_annule?'text-decoration: line-through;color: gray;':'' }" href="javascript:" id="lnk_det" curr="${mouvement.id}">
						<span class="fa fa-plus" style="color:green;"></span> ${mouvement.ref_commande}
					</a>
					<%} else{ %>
						<span style="font-weight:bold;${mouvement.is_annule?'text-decoration: line-through;color: gray;':'' }">${mouvement.ref_commande}</span>
					<%} %>
					<c:if test="${not empty mouvement.mtt_reduction and mouvement.mtt_reduction > 0}">
						<i class="fa fa-gift" style="color: green;" title="Avec r&eacute;duction"></i>
					</c:if>
					<c:if test="${empty mouvement.mode_paiement || mouvement.last_statut=='TEMP'}">
						<i class="fa fa-spinner" style="color: #673ab7;font-size: 14px;font-weight: bold;" title="Commande en attente d'encaissement"></i>
					</c:if>
					<c:if test="${mouvement.isMvmRecharge()}">
						[<i class="fa fa fa-briefcase" style="color: #673ab7;font-size: 14px;font-weight: bold;" title="Recharge portefeuille"></i> Recharge]
					</c:if>
					
				</cplx:td>
				<%if(isHistoriqueShow || isPrintRaz){%>
				<cplx:td align="right" style="font-weight:bold;">
					<fmt:formatDecimal value="${mouvement.mtt_commande}"/>
				</cplx:td>
				<cplx:td align="right" style="font-weight:bold;" value="${mouvement.mtt_commande_net}"></cplx:td>
				<cplx:td align="right" style="font-weight:bold;" value="${mouvement.mtt_commande_net-mouvement.mtt_livraison_ttl}"></cplx:td>
				<cplx:td align="right" style="font-weight:bold;" value="${mouvement.mtt_livraison_ttl}"></cplx:td>
				<cplx:td align="center" style="color:blue;" value="${mouvement.mode_paiement}"></cplx:td>
				<%} %>
				<cplx:td style="color:blue;font-weight:bold;padding-left:7px;text-align:center;">
					<c:set var="colEtat" value="black" />
					<c:choose>
						<c:when test="${mouvement.last_statut=='ANNUL' }">
							<i class="fa fa-user-times" style="color: red;font-size: 22px;font-weight: bold;"></i>
							<c:set var="colEtat" value="red" />
						</c:when>
						<c:when test="${mouvement.last_statut=='TEMP'}">
							<i class="fa fa-spinner" style="color: #673ab7;font-size: 22px;font-weight: bold;" title="Commande en attente d'encaissement"></i>
							<c:set var="colEtat" value="#673ab7" />
						</c:when>
						<c:when test="${mouvement.last_statut=='PREP' }">
							<img src="resources/restau/img/cuisine-image-animee-0008.gif" style="width: 25px;" />
							<c:set var="colEtat" value="#795548" />	
						</c:when>
						<c:when test="${mouvement.last_statut=='PRETE' }">
							<i class="fa fa-check-square-o" style="color: green;font-size: 22px;font-weight: bold;"></i>
							<c:set var="colEtat" value="green" />
						</c:when>
						<c:when test="${mouvement.last_statut=='VALIDE' }">
							<i class="fa fa-save" style="color: blue;font-size: 22px;font-weight: bold;"></i>
							<c:set var="colEtat" value="blue" />
						</c:when>
						<c:when test="${mouvement.last_statut=='LIVRE' }">
							<i class="fa fa-motorcycle" style="color: black;font-size: 22px;font-weight: bold;"></i>
							<c:set var="colEtat" value="black" />
						</c:when>
					</c:choose>
					<span style="padding-left: 5px;color:${colEtat}">${contextRestau.getLibelleStatut(mouvement.last_statut) }</span>
				</cplx:td>
				<cplx:td value="${mouvement.num_token_cmd}" align="right"></cplx:td>
				<cplx:td value="${mouvement.getRefTablesDetail()}" align="right"></cplx:td>
				<cplx:td style="color:blue;">
					<c:choose>
						<c:when test="${mouvement.type_commande == 'P' }">
							Sur place
						</c:when>
						<c:when test="${mouvement.type_commande == 'E' }">
							A emporter
						</c:when>
						<c:when test="${mouvement.type_commande == 'L' }">
							Livraison
						</c:when>
					</c:choose>
					<c:if test="${not empty mouvement.opc_livreurU }"> 
						&nbsp;[<span style="color:blue;font-size: 9px;">${mouvement.opc_livreurU.login }</span>]
					</c:if>
				</cplx:td> 
				<cplx:td align="center">
					<std:link action="caisse-web.caisseWeb.print" targetDiv="div_gen_printer" params='mvm=${mouvement.id }' classStyle="btn btn-xs btn-primary shiny" icon="fa-print" />
				</cplx:td>
			</cplx:tr>
			<tr style="display: none;" id="tr_det_${mouvement.id}" class="sub">
				<td colspan="10" noresize="true" style="background-color: #fff4d3;cursor: default;" align="center" id="tr_consult_${mouvement.id}">
					
				</td>
			</tr>
		</c:forEach>
	</cplx:body>
</cplx:table>

</div>
</div>
</std:form>
</div>
</div>