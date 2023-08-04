

<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<script type="text/javascript">
$(document).ready(function (){
	$("#pdf_link").click(function(){
			$(this).attr("href", $(this).attr("href")+"&dateDebut="+$("#dateDebut").val()+"&dateFin="+$("#dateFin").val());
	});
});
</script>
<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Gestion des ventes</li>
		<li>Mouvements livraisons</li>
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

<div class="row">
		<div class="col-lg-12 col-sm-12 col-xs-12">
			<div class="widget">
				<div class="widget-main ">
							<std:form name="search-form">
								<div class="row">
								        <div class="form-group">
								        	<std:label classStyle="control-label col-md-2" value="Date d&eacute;but" />
								            <div class="col-md-2">
								                 <std:date name="dateDebut" required="true" value="${dateDebut }"/>
								            </div>
								            <div class="col-md-2" style="text-align: center;">
								            	<std:link action="caisse.caisse.find_livraison" params="prev=1&is_fltr=1" icon="fa fa-arrow-circle-left" tooltip="Journ&eacute;e pr&eacute;c&eacute;dente" />
								            	<std:link action="caisse.caisse.find_livraison" params="next=1&is_fltr=1" icon="fa fa-arrow-circle-right" tooltip="Journ&eacute;e suivante" />
								            </div>
								            
								            <std:label classStyle="control-label col-md-1" value="Date fin" />
								            <div class="col-md-2">
								                 <std:date name="dateFin" required="true" value="${dateFin }"/>
								            </div>
								            <div class="col-md-2">
								           	 	<std:button action="caisse.caisse.find_livraison" value="Filtrer" params="is_fltr=1" classStyle="btn btn-primary" />
								           	 </div>	
								           	 <div class="col-md-2">
								           	 	<std:link action="caisse.caisse.editPdfLivraison" value="Telecharger Pdf" target="downloadframe" id="pdf_link" classStyle="btn btn-danger" style="left: 80px; top: -32px;" workId="${caisseMouvement.id}" icon="fa fa-download" />
								           	 </div>	
								       </div>
								 </div>
								<!-- Liste des caisses -->
								<cplx:table name="list_mouvement_livraison" transitionType="simple" forceFilter="true" width="100%" title="Livraison" initAction="caisse.caisse.find_livraison" checkable="false" autoHeight="true">
									<cplx:header>
										<cplx:th type="date" value="Date" field="caisseMouvement.date_vente" width="100" filterOnly="true" />	
										<cplx:th type="empty" />
										<cplx:th type="string" valueKey="caisseMouvement.ref_commande" field="caisseMouvement.ref_commande" width="145" />
										<cplx:th type="string[]" value="Statut" field="caisseMouvement.last_statut" width="80" groupValues="${statutArray }" />
										<cplx:th type="string[]" value="Caissier" field="caisseMouvement.opc_user.login" groupValues="${listUser }" groupKey="login" groupLabel="login"/>
										<cplx:th type="long[]" valueKey="caisseMouvement.opc_employe" field="caisseMouvement.opc_employe.id" groupValues="${listEmploye }" groupKey="id" groupLabel="nom" />
										<cplx:th type="long[]" value="Livreur" field="caisseMouvement.opc_livreurU.login" groupValues="${listLivreur }" groupKey="login" groupLabel="login" />
										<cplx:th type="long[]" valueKey="caisseMouvement.opc_client" field="caisseMouvement.opc_client.id" groupValues="${listClient }" groupKey="id" groupLabel="nom" />
										<cplx:th type="decimal" value="Mtt. commande" field="caisseMouvement.mtt_commande" width="100" />
										<cplx:th type="decimal" value="R&eacute;duction Cmd" field="caisseMouvement.mtt_reduction" width="80" filtrable="false" />
										<cplx:th type="decimal" value="R&eacute;duction Art" field="caisseMouvement.mtt_art_reduction" width="80" filtrable="false" />
										<cplx:th type="decimal" value="Mtt. offert" field="caisseMouvement.mtt_art_offert" width="80" filtrable="false" />
										<cplx:th type="decimal" value="Mtt. NET" field="caisseMouvement.mtt_commande_net" width="100" />
										<cplx:th type="string[]" valueKey="caisseMouvement.mode_paiement" field="caisseMouvement.mode_paiement" groupValues="${modePaie }" width="150" />
										<cplx:th type="empty" width="50"/>
									</cplx:header>
									<cplx:body>
										<c:set var="contextRestau" value="<%=new ContextAppli()%>"></c:set>
										<c:set var="oldjour" value="${null }"></c:set>
										
										<c:forEach items="${list_livraisonMouvement }" var="caisseMouvement">
											
											<c:if test="${oldjour == null  or oldjour != caisseMouvement.opc_caisse_journee.opc_journee.id }">
												<tr>
													<td colspan="14" noresize="true" class="separator-group">
													<span class="fa fa-fw fa-folder-open-o separator-icon"></span> <fmt:formatDate value="${caisseMouvement.opc_caisse_journee.opc_journee.date_journee}"/>
												</td>
												</tr>	
											</c:if>
											<c:set var="oldjour" value="${caisseMouvement.opc_caisse_journee.opc_journee.id }"></c:set>
											<cplx:tr workId="${caisseMouvement.id }">
												<cplx:td>
													<std:linkPopup classStyle="btn btn-sm btn-primary" action="caisse.journee.edit_mouvement" workId="${caisseMouvement.id }">
														<span class="fa  fa-eye"></span>
													</std:linkPopup>
												</cplx:td>
												<cplx:td style="${caisseMouvement.is_annule?'text-decoration: line-through;color: #9acde4;':'' }" value="${caisseMouvement.ref_commande}"></cplx:td>
												<cplx:td align="center" style="color:${caisseMouvement.last_statut=='ANNUL'?'red':'blue'};" value="${contextRestau.getLibelleStatut(caisseMouvement.last_statut)}"></cplx:td>
												<cplx:td style="color: blue;" value="${caisseMouvement.opc_user.login}"></cplx:td>
												<cplx:td value="${caisseMouvement.opc_employe.nom} ${caisseMouvement.opc_employe.prenom}"></cplx:td>
												<cplx:td align="center" value="${caisseMouvement.opc_livreurU.login}"></cplx:td>
												<cplx:td align="center" value="${caisseMouvement.opc_client.nom} ${caisseMouvement.opc_client.prenom}"></cplx:td>
												<cplx:td style="font-weight:bold;" align="right" value="${caisseMouvement.mtt_commande}"></cplx:td>
												<cplx:td align="right" value="${caisseMouvement.mtt_reduction}"></cplx:td>
												<cplx:td align="right" value="${caisseMouvement.mtt_art_reduction}"></cplx:td>
												<cplx:td align="right" value="${caisseMouvement.mtt_art_offert}"></cplx:td>
												<cplx:td style="font-weight:bold;" align="right" value="${caisseMouvement.mtt_commande_net}"></cplx:td>
												<cplx:td align="center" style="color:green;" value="${caisseMouvement.mode_paiement}"></cplx:td>
												<cplx:td align="center">
													<std:link classStyle="btn btn-success shiny" style="color:black;" params="mvm=${caisseMouvement.id}" action="caisse-web.caisseWeb.print" icon="fa fa-print" targetDiv="div_gen_printer" tooltip="Imprimer le ticket" />
												</cplx:td>	
											</cplx:tr>
										</c:forEach>
										<c:if test="${!list_livraisonMouvement.isEmpty()}">
												<tr class="sub">
													<td colspan="7"></td>
													<td align="right">
														<span style="font-size: 14px !important;font-weight: bold;height: 28px;" class="badge badge-blue">
															<fmt:formatDecimal value="${mvmDetTotal.mtt_commande }"/>
														</span>
													</td>
													<td align="right">
														<span style="font-size: 14px !important;font-weight: bold;height: 28px;" class="badge badge-blue">
															<fmt:formatDecimal value="${mvmDetTotal.mtt_reduction }"/>
														</span>
													</td>
													<td align="right">
														<span style="font-size: 14px !important;font-weight: bold;height: 28px;" class="badge badge-blue">
															<fmt:formatDecimal value="${mvmDetTotal.mtt_art_reduction }"/>
														</span>
													</td>
													<td align="right">
														<span style="font-size: 14px !important;font-weight: bold;height: 28px;" class="badge badge-blue">
															<fmt:formatDecimal value="${mvmDetTotal.mtt_art_offert }"/>
														</span>
													</td>
													<td align="right">
														<span style="font-size: 14px !important;font-weight: bold;height: 28px;" class="badge badge-blue">
															<fmt:formatDecimal value="${mvmDetTotal.mtt_commande_net }"/>
														</span>
													</td>
													<td colspan="3"></td>
												</tr>
											</c:if>	
									</cplx:body>
								</cplx:table>
								
							</std:form>	
					</div>
				</div>
			</div>
		</div>
	</div>
