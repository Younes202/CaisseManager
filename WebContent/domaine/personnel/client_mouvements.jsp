<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.controller.Context"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@page errorPage="/commun/error.jsp"%>

<%
	boolean isDetailJourneeDroit = Context.isOperationAvailable("DETJRN");
	boolean isAnnulCmdDroit = Context.isOperationAvailable("ANNULCMD");
%>
<style type="text/css">
	.context-menu-root .del {
		color: red;
	}
	.context-menu-root .off {
		color: green;
	}
	.context-menu-root .com{
		color: blue;
	}
</style>
<script type="text/javascript">
$(document).ready(function (){
	$(document).off('click', 'a[id="lnk_annul"]');
	$(document).on('click', 'a[id="lnk_annul"]', function(){
		showConfirmDeleteBox('<%=EncryptionUtil.encrypt("caisse.journee.annulerCommande")%>', $(this).attr("params"), $(this), "Cette commande sera annul&eacute;e ainsi que son stock. Cette op&eacute;ration n''est pas annulable.<br>Voulez-vous confirmer ?", null, "Annulation commande");
    });
	$(document).off('click', 'a[id="lnk_del_row"]');
	$(document).on('click', 'a[id="lnk_del_row"]', function(){
		showConfirmDeleteBox('<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.deleteRow")%>', $(this).closest("tr").attr("par"), $(this), "Cette &eacute;l&eacute;ment sera annul&eacute;e ainsi que son stock. Cette op&eacute;ration n''est pas annulable.<br>Voulez-vous confirmer ?", null, "Annulation ligne");
    });
});
</script>
		
<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Gestion des personnels</li>
		<li>Liste des commandes</li>
		<li class="active">Recherche</li>
	</ul>
</div>
<!-- /Page Breadcrumb -->
<!-- Page Header -->
<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<std:link classStyle="btn btn-default" action="pers.client.work_find" params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la recherche" />
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
	<div class="widget">
		<div class="row">
			<div class="col-lg-12 col-sm-12 col-xs-12">
				<% request.setAttribute("curMnu", "mvm"); %>
				<jsp:include page="/domaine/personnel/client_header_tab.jsp" />
			</div>
		</div>
		<div class="widget-body">
		<std:form name="search-form">
			<div class="row">
					<!-- Liste des employes -->
					<cplx:table name="list_caisseMouvement" transitionType="simple" width="100%" autoHeight="true" titleKey="caisseMouvement.list" initAction="pers.client.find_mvm_client">
						<cplx:header>
							<cplx:th type="empty" />
							<cplx:th type="date" value="Date" field="caisseMouvement.date_vente" width="100" filterOnly="true" />
							<cplx:th type="string" valueKey="caisseMouvement.ref_commande" field="caisseMouvement.ref_commande" width="145" />
							<cplx:th type="string[]" value="Statut" field="caisseMouvement.last_statut" width="80" groupValues="${statutArray }" />
							<cplx:th type="long[]" valueKey="caisseMouvement.opc_employe" field="caisseMouvement.opc_employe.id"  groupValues="${listEmploye }" groupKey="id" groupLabel="nom"/>
							<%
						if(isDetailJourneeDroit){
						%>
							<cplx:th type="decimal" value="Mtt. commande" field="caisseMouvement.mtt_commande" width="100" />
							<cplx:th type="decimal" value="Mtt. commande net" field="caisseMouvement.mtt_commande_net" width="100" />
							<cplx:th type="decimal" value="Mtt. annulation" field="caisseMouvement.mtt_annul_ligne" width="100" />
							<cplx:th type="decimal" value="Mtt. r&eacute;duction" field="caisseMouvement.mtt_reduction" width="100" filtrable="false" />
							<cplx:th type="decimal" value="Mtt. offert" field="caisseMouvement.mtt_art_offert" width="100" filtrable="false" />
						<%
							}
						%>
							<cplx:th type="string[]" valueKey="caisseMouvement.type_commande" field="caisseMouvement.type_commande" width="130" groupValues="${typeCmd }" />
							<cplx:th type="string[]" valueKey="caisseMouvement.mode_paiement" field="caisseMouvement.mode_paiement" width="150" groupValues="${modePaie }"/>
							<cplx:th type="empty" width="120" />
						</cplx:header>
						<cplx:body>
								<c:set var="oldjour" value="${null }"></c:set>
								<c:set var="oldCaisse" value="${null }"></c:set>
								<c:set var="oldjourCaisse" value="${null }"></c:set>
								<c:set var="contextRestau" value="<%=new ContextAppli()%>"></c:set>
								
								<c:forEach items="${list_caisseMouvement }" var="caisseMouvement">
									
								<%if(ControllerUtil.getMenuAttribute("journeeId", request) == null){ %>	
									<c:if test="${oldjour == null  or oldjour != caisseMouvement.opc_caisse_journee.opc_journee.id }">
										<tr>
											<td colspan="${isDetailJourneeDroit? '8':'13'}" noresize="true" class="separator-group">
												<span class="fa fa-fw fa-folder-open-o separator-icon"></span>  <fmt:formatDate value="${caisseMouvement.date_vente}"/>
											</td>
										</tr>	
									</c:if>
									<c:set var="oldjour" value="${caisseMouvement.opc_caisse_journee.opc_journee.id }"></c:set>
								<%} else{ %>
									<!-- Caisse -->
									<c:if test="${oldCaisse == null  or oldCaisse != caisseMouvement.opc_caisse_journee.opc_caisse.id }">
										<tr>
											<td colspan="${isDetailJourneeDroit? '8':'13'}" noresize="true" class="separator-group">
												<span class="fa fa-fw fa-folder-open-o separator-icon"></span>  ${caisseMouvement.opc_caisse_journee.opc_caisse.reference.toUpperCase() }
											</td>
										</tr>	
									</c:if>
									<c:set var="oldCaisse" value="${caisseMouvement.opc_caisse_journee.opc_caisse.id }"></c:set>
								<%} %>
									<!-- Caisse journ&eacute;e -->
								     <c:if test="${oldjourCaisse == null  or oldjourCaisse != caisseMouvement.opc_caisse_journee.id }">
										<tr>
											<td colspan="${isDetailJourneeDroit? '8':'12'}" noresize="true" style="font-size: 13px;font-weight: bold;color:blue;background-color: #fff9e0;padding-left:30px;">
												<span class="fa fa-fw fa-folder-open-o separator-icon"></span> Shift de <fmt:formatDate value="${caisseMouvement.opc_caisse_journee.date_ouverture}" pattern="HH:mm:ss"/>
											</td>
										</tr>	
									</c:if>
									<c:set var="oldjourCaisse" value="${caisseMouvement.opc_caisse_journee.id }"></c:set>
								
									<cplx:tr workId="${caisseMouvement.id }">
										<cplx:td>
											<std:linkPopup classStyle="btn btn-sm btn-primary" action="caisse.journee.edit_mouvement" workId="${caisseMouvement.id }">
												<span class="fa fa-eye"></span>
											</std:linkPopup>
										</cplx:td>
										<cplx:td style="${caisseMouvement.is_annule?'text-decoration: line-through;color: #9acde4;':'' }" value="${caisseMouvement.ref_commande}"></cplx:td>
										<cplx:td align="center" style="color:${caisseMouvement.last_statut=='VALIDE'?'green':'orange'};" value="${contextRestau.getLibelleStatut(caisseMouvement.last_statut)}"></cplx:td>
										<cplx:td value="${caisseMouvement.opc_employe.nom} ${caisseMouvement.opc_employe.prenom}"></cplx:td>
										<%if(isDetailJourneeDroit){%>
											<cplx:td style="font-weight:bold;" align="right" value="${caisseMouvement.mtt_commande}"></cplx:td>
											<cplx:td style="font-weight:bold;" align="right" value="${caisseMouvement.mtt_commande_net}"></cplx:td>
											<cplx:td style="font-weight:bold;" align="right" value="${caisseMouvement.mtt_annul_ligne}"></cplx:td>
											<cplx:td align="right" value="${caisseMouvement.mtt_reduction}"></cplx:td>
											<cplx:td align="right" value="${caisseMouvement.mtt_art_offert}"></cplx:td>
										<%}%>
										<cplx:td align="center" value="${caisseMouvement.type_commande}"></cplx:td>
										<cplx:td align="center" style="color:green;" value="${caisseMouvement.mode_paiement}"></cplx:td>
										<cplx:td align="right">
											<%if(isAnnulCmdDroit && ControllerUtil.getMenuAttribute("isCaisse", request) == null){ %>
												<c:if test="${not caisseMouvement.is_annule && caisseMouvement.opc_caisse_journee.opc_journee.statut_journee=='O' }">
													<std:link id="lnk_annul" classStyle="btn btn-sm btn-info shiny" style="color:red;" params="mvm=${caisseMouvement.id}" action="" icon="fa fa-power-off" tooltip="Annuler cette commande" />
												</c:if>
											<%} %>
											<std:link classStyle="btn btn-sm btn-success shiny" style="color:black;" params="mvm=${caisseMouvement.id}" action="caisse-web.caisseWeb.print" icon="fa fa-print" targetDiv="div_gen_printer" tooltip="Imprimer le ticket" />
										</cplx:td>	
									</cplx:tr>
								</c:forEach>
								<c:if test="${!list_caisseMouvement.isEmpty()}">
										<%if(isDetailJourneeDroit){%>
										<tr class="sub">
											<td colspan="5"></td>
												<td align="right">
													<span style="font-size: 14px !important;font-weight: bold;height: 28px;" class="badge badge-blue">
														<fmt:formatDecimal value="${mvmDetTotal.mtt_commande }"/>
													</span>
												</td>
												<td align="right">
													<span style="font-size: 14px !important;font-weight: bold;height: 28px;" class="badge badge-blue">
														<fmt:formatDecimal value="${mvmDetTotal.mtt_commande_net }"/>
													</span>
												</td>
												<td align="right">
													<span style="font-size: 14px !important;font-weight: bold;height: 28px;" class="badge badge-blue">
														<fmt:formatDecimal value="${mvmDetTotal.mtt_annul_ligne}"/>
													</span>
												</td>
												<td align="right">
													<span style="font-size: 14px !important;font-weight: bold;height: 28px;" class="badge badge-blue">
														<fmt:formatDecimal value="${mvmDetTotal.mtt_reduction }"/>
													</span>
												</td>
												<td align="right">
													<span style="font-size: 14px !important;font-weight: bold;height: 28px;" class="badge badge-blue">
														<fmt:formatDecimal value="${mvmDetTotal.mtt_art_offert }"/>
													</span>
												</td>
											<td colspan="3"></td>
										</tr>
										<%}%>
									</c:if>	
						</cplx:body>
					</cplx:table>

			</div>
			
			<div class="row" style="text-align: center;">
				<std:link actionGroup="S" classStyle="btn btn-sm btn-default shiny" style="color:black;" params="tpp=T" action="pers.clientCaisse.print_ticket_cmd" targetDiv="div_gen_printer" icon="fa fa-print" value="Tout imprimer" tooltip="Imprimer" />
				<std:link actionGroup="S" classStyle="btn btn-sm btn-default shiny" style="color:black;" params="tpp=T&isRes=1" action="pers.clientCaisse.print_ticket_cmd" targetDiv="div_gen_printer" icon="fa fa-print" value="Imprimer commandes potefeuille" tooltip="Imprimer" />
			</div>
			</std:form>
			<!-- end widget content -->
		</div>
	</div>
</div>

<!-- end widget div -->
