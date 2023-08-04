<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.controller.ControllerUtil"%>
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
		<li>Mouvements avec réductions</li>
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
							        	<std:label classStyle="control-label col-md-2" value="Date début" />
							            <div class="col-md-2">
							                 <std:date name="dateDebut" required="true" value="${dateDebut }"/>
							            </div>
							            <div class="col-md-2" style="text-align: center;">
							            	<std:link action="caisse.caisse.find_reduction" params="prev=1&is_fltr=1" icon="fa fa-arrow-circle-left" tooltip="Journée précédente" />
							            	<std:link action="caisse.caisse.find_reduction" params="next=1&is_fltr=1" icon="fa fa-arrow-circle-right" tooltip="Journée suivante" />
							            </div>
							            
							            <std:label classStyle="control-label col-md-1" value="Date fin" />
							            <div class="col-md-2">
							                 <std:date name="dateFin" required="true" value="${dateFin }"/>
							            </div>
							            <div class="col-md-2">
							           	 	<std:button action="caisse.caisse.find_reduction" value="Filtrer" params="is_fltr=1" classStyle="btn btn-primary" />
							           	 </div>	
							           	 <div class="col-md-2">
								           	 	<std:link id="pdf_link" action="caisse.caisse.editPdfReduction" target="downloadframe" value="Telecharger Pdf" classStyle="btn btn-danger" style="left: 80px; top: -32px;" workId="${caisseMouvement.id}" icon="fa fa-download" />
								         </div>	
							       </div>
							   </div>
								<!-- Liste des caisses -->
								<cplx:table name="list_mouvement_reduction" transitionType="simple" forceFilter="true" width="100%" title="Ventes avec réductions" initAction="caisse.caisse.find_reduction" checkable="false" autoHeight="true">
									<cplx:header>
										<cplx:th type="date" value="Date" field="offre.opc_mouvement_caisse.date_creation" width="150" filterOnly="true" />
									
										<cplx:th type="empty" />
										<cplx:th type="string" valueKey="caisseMouvement.ref_commande" field="offre.opc_mouvement_caisse.ref_commande" width="150" />
										<cplx:th type="string[]" value="Statut" field="offre.opc_mouvement_caisse.last_statut" width="80" groupValues="${statutArray }" />
										<cplx:th type="string[]" value="Caissier" field="offre.opc_mouvement_caisse.opc_user.login" groupValues="${listUser }" groupKey="login" groupLabel="login"/>
										<cplx:th type="string" value="Bénéficiaire" sortable="false" filtrable="false" />
										<cplx:th type="decimal" value="Mtt. commande" field="offre.opc_mouvement_caisse.mtt_commande" width="100" filtrable="false" />
										<cplx:th type="decimal" value="Cmd Réduc " field="offre.opc_mouvement_caisse.mtt_reduction" width="80" filtrable="false" />
										<cplx:th type="decimal" value="Art Réduc" field="offre.opc_mouvement_caisse.mtt_art_reduction" width="80" filtrable="false" />
										<cplx:th type="long[]" value="Type Réduc" width="100" field="offre.opc_offre.id" groupValues="${listOffre }" groupKey="id" groupLabel="libelle" />
										<cplx:th type="decimal" value="Mtt. offert" field="offre.opc_mouvement_caisse.mtt_art_offert" width="80" filtrable="false" />
										<cplx:th type="decimal" value="Mtt. net" field="offre.opc_mouvement_caisse.mtt_commande_net" width="100" filtrable="false" />
										<cplx:th type="string[]" valueKey="caisseMouvement.type_commande" field="offre.opc_mouvement_caisse.type_commande" width="100" groupValues="${typeCmd }" />
										<cplx:th type="string[]" valueKey="caisseMouvement.mode_paiement" field="offre.opc_mouvement_caisse.mode_paiement" width="150" groupValues="${modePaie }" />
										
										<cplx:th type="long[]" value="Client" field="offre.opc_mouvement_caisse.opc_client.id" width="150" groupValues="${listClient }" groupKey="id" groupLabel="nom" filterOnly="true" />
										<cplx:th type="long[]" value="Employé" field="offre.opc_mouvement_caisse.opc_employe.id" width="150" groupValues="${listEmploye }" groupKey="id" groupLabel="nom" filterOnly="true" />
										
									</cplx:header>
									<cplx:body>
									
										<c:set var="oldjour" value="${null }"></c:set>
										<c:set var="oldCaisse" value="${null }"></c:set>
										<c:set var="oldjourCaisse" value="${null }"></c:set>
										<c:set var="contextRestau" value="<%=new ContextAppli()%>"></c:set>
										
										<c:forEach items="${list_caisseMouvement }" var="caisseMouvement">
											<!-- Journee -->
											<c:if test="${oldjour == null  or oldjour != caisseMouvement.opc_caisse_journee.opc_journee.id }">
												<tr>
													<td colspan="13" noresize="true" class="separator-group">
														<span class="fa fa-fw fa-folder-open-o separator-icon"></span> <fmt:formatDate value="${caisseMouvement.opc_caisse_journee.opc_journee.date_journee}"/>
													</td>
												</tr>	
											</c:if>
											<c:set var="oldjour" value="${caisseMouvement.opc_caisse_journee.opc_journee.id }"></c:set>
											
											<!-- Caisse -->
											<c:if test="${oldCaisse == null  or oldCaisse != caisseMouvement.opc_caisse_journee.opc_caisse.id }">
												<tr>
													<td colspan="13" noresize="true" class="separator-group" style="padding-left: 30px;">
														<span class="fa fa-fw fa-folder-open-o separator-icon"></span> ${caisseMouvement.opc_caisse_journee.opc_caisse.reference }
													</td>
												</tr>	
											</c:if>
											<c:set var="oldCaisse" value="${caisseMouvement.opc_caisse_journee.opc_caisse.id }"></c:set>
											
											<!-- Shift -->
										     <c:if test="${oldjourCaisse == null  or oldjourCaisse != caisseMouvement.opc_caisse_journee.id }">
												<tr>
													<td colspan="13" noresize="true" class="separator-group" style="padding-left: 50px;color: fuchsia;">
													<span class="fa fa-fw fa-folder-open-o separator-icon"></span> Shift de <fmt:formatDate value="${caisseMouvement.opc_caisse_journee.date_ouverture}" pattern="HH:mm:ss"/>
												</td>
												</tr>	
											</c:if>
											<c:set var="oldjourCaisse" value="${caisseMouvement.opc_caisse_journee.id }"></c:set>
										
											<cplx:tr workId="${caisseMouvement.id }">
												<cplx:td>
													<std:linkPopup classStyle="btn btn-sm btn-primary" action="caisse.journee.edit_mouvement" workId="${caisseMouvement.id }">
														<span class="fa  fa-eye"></span>
													</std:linkPopup>
												</cplx:td>
												<cplx:td style="${caisseMouvement.is_annule?'text-decoration: line-through;color: #9acde4;':'' }" value="${caisseMouvement.ref_commande}"></cplx:td>
												<cplx:td align="center" style="color:${caisseMouvement.last_statut=='ANNUL'?'red':'blue'};" value="${contextRestau.getLibelleStatut(caisseMouvement.last_statut)}"></cplx:td>
												<cplx:td style="color: blue;" value="${caisseMouvement.opc_user.login}"></cplx:td>
												<cplx:td>
													<c:if test="${caisseMouvement.opc_employe.nom != null}">
														${caisseMouvement.opc_employe.nom} ${caisseMouvement.opc_employe.prenom}
													</c:if>
													<c:if test="${caisseMouvement.opc_client.nom != null}">
														${caisseMouvement.opc_client.nom} ${caisseMouvement.opc_client.prenom}
													</c:if>
												</cplx:td>
												<cplx:td style="font-weight:bold;" align="right" value="${caisseMouvement.mtt_commande}"></cplx:td>
												<cplx:td style="color:orange;" align="right" value="${caisseMouvement.mtt_reduction}"></cplx:td>
												<cplx:td style="color:orange;" align="right" value="${caisseMouvement.mtt_art_reduction}"></cplx:td>
												<cplx:td style="color:blue;" align="center" value="${caisseMouvement.getOffreStr()}"></cplx:td>
												<cplx:td style="color:orange;" align="right" value="${caisseMouvement.mtt_art_offert}"></cplx:td>
												<cplx:td style="font-weight:bold;" align="right" value="${caisseMouvement.mtt_commande_net}"></cplx:td>
												<cplx:td align="center" value="${caisseMouvement.type_commande}"></cplx:td>
												<cplx:td align="center" style="color:green;" value="${caisseMouvement.mode_paiement}"></cplx:td>
											</cplx:tr>
										</c:forEach>
										<c:if test="${!list_caisseMouvement.isEmpty()}">
											<tr class="sub">
												<td colspan="5"></td>
												<td align="right">
													<span style="font-size: 14px !important;font-weight: bold;height: 28px;" class="badge badge-blue">
														<fmt:formatDecimal value="${mvm_total.mtt_commande }"/>
													</span>
												</td>
												<td align="right">
													<span style="font-size: 14px !important;font-weight: bold;height: 28px;" class="badge badge-blue">
														<fmt:formatDecimal value="${mvm_total.mtt_reduction }"/>
													</span>
												</td>
												<td align="right">
													<span style="font-size: 14px !important;font-weight: bold;height: 28px;" class="badge badge-blue">
														<fmt:formatDecimal value="${mvm_total.mtt_art_reduction }"/>
													</span>
												</td>
												<td align="right" />
												<td align="right">
													<span style="font-size: 14px !important;font-weight: bold;height: 28px;" class="badge badge-blue">
														<fmt:formatDecimal value="${mvm_total.mtt_art_offert }"/>
													</span>
												</td>
												<td align="right">
													<span style="font-size: 14px !important;font-weight: bold;height: 28px;" class="badge badge-blue">
														<fmt:formatDecimal value="${mvm_total.mtt_commande_net }"/>
													</span>
												</td>
												<td align="right" />
												<td align="right" />
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
