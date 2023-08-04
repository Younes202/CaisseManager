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


<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Gestion des ventes</li>
		<li>Mouvements avec r&eacute;ductions</li>
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
							            	<std:link action="caisse.caisse.find_reduction" params="prev=1" icon="fa fa-arrow-circle-left" tooltip="Journ&eacute;e pr&eacute;c&eacute;dente" />
							            	<std:link action="caisse.caisse.find_reduction" params="next=1" icon="fa fa-arrow-circle-right" tooltip="Journ&eacute;e suivante" />
							            </div>
							            
							            <std:label classStyle="control-label col-md-1" value="Date fin" />
							            <div class="col-md-2">
							                 <std:date name="dateFin" required="true" value="${dateFin }"/>
							            </div>
							            <div class="col-md-2">
							           	 	<std:button action="caisse.caisse.find_reduction" value="Filtrer" classStyle="btn btn-primary" />
							           	 </div>	
							       </div>
							   </div>
								<!-- Liste des caisses -->
								<cplx:table name="list_mouvement_reduction" transitionType="simple" width="100%" title="Ventes avec r&eacute;ductions" initAction="caisse.caisse.find_reduction" checkable="false" autoHeight="true">
									<cplx:header>
										<cplx:th type="date" value="Date" field="offre.opc_mouvement_caisse.date_creation" width="150" filterOnly="true" />
									
										<cplx:th type="empty" />
										<cplx:th type="string" valueKey="caisseMouvement.ref_commande" field="offre.opc_mouvement_caisse.ref_commande" width="150" />
										<cplx:th type="string[]" value="Statut" field="caisseMouvement.last_statut" width="80" groupValues="${statutArray }" />
										<cplx:th type="string" value="B&eacute;n&eacute;ficiaire" sortable="false" filtrable="false" />
										<cplx:th type="decimal" value="Mtt. commande" field="offre.opc_mouvement_caisse.mtt_commande" width="100" filtrable="false" />
										<cplx:th type="decimal" value="Mtt. r&eacute;duction" field="offre.opc_mouvement_caisse.mtt_reduction" width="100" filtrable="false" />
										<cplx:th type="long[]" value="Type r&eacute;duction" width="100" field="offre.opc_offre.id" groupValues="${listOffre }" groupKey="id" groupLabel="libelle"/>
										<cplx:th type="decimal" value="Mtt. net" field="offre.opc_mouvement_caisse.mtt_commande_net" width="100" filtrable="false" />
										<cplx:th type="string[]" valueKey="caisseMouvement.mode_paiement" field="offre.opc_mouvement_caisse.mode_paiement" width="150" groupValues="${modePaie }" />
										
										<cplx:th type="long[]" value="Client" field="offre.opc_mouvement_caisse.opc_client.id" width="150" groupValues="${listClient }" groupKey="id" groupLabel="nom" filterOnly="true" />
										<cplx:th type="long[]" value="Employ&eacute;" field="offre.opc_mouvement_caisse.opc_employe.id" width="150" groupValues="${listEmploye }" groupKey="id" groupLabel="nom" filterOnly="true" />
										
									</cplx:header>
									<cplx:body>
										<c:set var="contextRestau" value="<%=new ContextAppli() %>"></c:set>
										<c:set var="oldjour" value="${null }"></c:set>
										<c:set var="oldjourCaisse" value="${null }"></c:set>
										
										<c:forEach items="${list_caisseMouvement }" var="caisseMouvement">
											
											<c:if test="${oldjour == null  or oldjour != caisseMouvement.opc_caisse_journee.opc_journee.id }">
												<tr>
													<td colspan="11" noresize="true" style="font-size: 13px;font-weight: bold;color:green;background-color: #fff9e0;"><fmt:formatDate value="${caisseMouvement.date_vente}"/></td>
												</tr>	
											</c:if>
											<c:set var="oldjour" value="${caisseMouvement.opc_caisse_journee.opc_journee.id }"></c:set>
										
										     <c:if test="${oldjourCaisse == null  or oldjourCaisse != caisseMouvement.opc_caisse_journee.id }">
												<tr>
													<td colspan="11" noresize="true" style="font-size: 13px;font-weight: bold;color:#d73d32;background-color: #fff9e0;padding-left: 50px;">Shift de <fmt:formatDate value="${caisseMouvement.opc_caisse_journee.date_ouverture}" pattern="HH:mm:ss"/></td>
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
												<cplx:td style="color:blue;" align="center" value="${caisseMouvement.getOffreStr()}"></cplx:td>
												<cplx:td style="font-weight:bold;" align="right" value="${caisseMouvement.mtt_commande_net}"></cplx:td>
												<cplx:td align="center" style="color:green;" value="${caisseMouvement.mode_paiement}"></cplx:td>
											</cplx:tr>
										</c:forEach>
										<c:if test="${!list_caisseMouvement.isEmpty()}">
											<tr class="sub">
												<td colspan="3"></td>
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
												<td align="right" />
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
