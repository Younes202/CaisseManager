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
		<li>Mouvements annul&eacute;s</li>
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
							            	<std:link action="caisse.caisse.find_annulation" params="prev=1&is_fltr=1" icon="fa fa-arrow-circle-left" tooltip="Journ&eacute;e pr&eacute;c&eacute;dente" />
							            	<std:link action="caisse.caisse.find_annulation" params="next=1&is_fltr=1" icon="fa fa-arrow-circle-right" tooltip="Journ&eacute;e suivante" />
							            </div>
							            
							            <std:label classStyle="control-label col-md-1" value="Date fin" />
							            <div class="col-md-2">
							                 <std:date name="dateFin" required="true" value="${dateFin }"/>
							            </div>
							            <div class="col-md-2">
							           	 	<std:button action="caisse.caisse.find_annulation" value="Filtrer" params="is_fltr=1" classStyle="btn btn-primary" />
							           	 </div>	
							       </div>
							 </div>
							 <div class="row" style="font-size: 20px;text-align: center;margin-bottom: 15px;">
							 		TOTAL DES ANNULATIONS : <span style="color: red;font-weight: bold;">${totalAnnulationAll }</span>
							 </div>
							<!-- Liste des caisses -->
							<cplx:table name="list_mouvement_annulation" transitionType="simple" forceFilter="true" width="100%" title="Commandes annulées" initAction="caisse.caisse.find_annulation" checkable="false" autoHeight="true">
								<cplx:header>
									<cplx:th type="date" value="Date" field="caisseMouvement.date_vente" width="100" filterOnly="true" />	
									<cplx:th type="empty" />
									<cplx:th type="string" valueKey="caisseMouvement.ref_commande" field="caisseMouvement.ref_commande" width="145" />
									<cplx:th type="dateTime" value="Date annulation" field="caisseMouvement.date_annul" width="145" />
									<cplx:th type="string[]" value="Caissier" field="caisseMouvement.opc_user.login" groupValues="${listUser }" groupKey="login" groupLabel="login"/>
									<cplx:th type="string[]" value="Confirmation" field="caisseMouvement.opc_user_annul.login" groupValues="${listUser }" groupKey="login" groupLabel="login"/>
									<cplx:th type="long[]" valueKey="caisseMouvement.opc_employe" field="caisseMouvement.opc_employe.id" groupValues="${listEmploye }" groupKey="id" groupLabel="nom" />
									<cplx:th type="long[]" value="Livreur" field="caisseMouvement.opc_livreurU.login" groupValues="${listUser }" groupKey="login" groupLabel="login"/>
									<cplx:th type="long[]" valueKey="caisseMouvement.opc_client" field="caisseMouvement.opc_client.id" groupValues="${listClient }" groupKey="id" groupLabel="nom" />
									<cplx:th type="decimal" style="color:red;" value="Mtt. commande" field="caisseMouvement.mtt_commande" width="100" />
									<cplx:th type="decimal" style="color:red;" value="Mtt. commande_net" field="caisseMouvement.mtt_commande_net" width="100" />
									<cplx:th type="string[]" valueKey="caisseMouvement.mode_paiement" field="caisseMouvement.mode_paiement" width="150" groupValues="${modePaie }" />
									<cplx:th type="empty" width="50"/>
								</cplx:header>
								<cplx:body>
								
									<c:set var="oldjour" value="${null }"></c:set>
									<c:forEach items="${list_annulationMouvement }" var="caisseMouvement">
										
										<c:if test="${oldjour == null  or oldjour != caisseMouvement.opc_caisse_journee.opc_journee.id }">
											<tr>
												<td colspan="11" noresize="true" class="separator-group">
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
											<cplx:td style="color: red;" value="${caisseMouvement.ref_commande}"></cplx:td>
											<cplx:td style="color: red;" value="${caisseMouvement.date_annul}"></cplx:td>
											<cplx:td style="color: blue;text-transform: uppercase;" value="${caisseMouvement.opc_user.login}"></cplx:td>
											<cplx:td style="color: red;text-transform: uppercase;" value="${caisseMouvement.opc_user_annul.login}"></cplx:td>
											<cplx:td value="${caisseMouvement.opc_employe.nom} ${caisseMouvement.opc_employe.prenom}"></cplx:td>
											<cplx:td align="center" value="${caisseMouvement.opc_livreurU.login}"></cplx:td>
											<cplx:td align="center" value="${caisseMouvement.opc_client.nom} ${caisseMouvement.opc_client.prenom}"></cplx:td>
											<cplx:td style="font-weight:bold;color:red;" align="right" value="${caisseMouvement.mtt_commande}"></cplx:td>
											<cplx:td style="font-weight:bold;color:red;" align="right" value="${caisseMouvement.mtt_commande_net}"></cplx:td>
											<cplx:td align="center" style="color:green;" value="${caisseMouvement.mode_paiement}"></cplx:td>
											<cplx:td align="center">
												<std:link classStyle="btn btn-success shiny" style="color:black;" params="mvm=${caisseMouvement.id}" action="caisse-web.caisseWeb.print" icon="fa fa-print" targetDiv="div_gen_printer" tooltip="Imprimer le ticket" />
											</cplx:td>	
										</cplx:tr>
									</c:forEach>
									<c:if test="${!list_annulationMouvement.isEmpty()}">
											<tr class="sub">
												<td colspan="8"></td>
												<td align="right">
													<span style="font-size: 14px !important;font-weight: bold;height: 28px;color:red;" class="badge badge-blue">
														<fmt:formatDecimal value="${mvmDetTotal.mtt_commande }"/>
													</span>
												</td>
												<td align="right">
													<span style="font-size: 14px !important;font-weight: bold;height: 28px;color:red;" class="badge badge-blue">
														<fmt:formatDecimal value="${mvmDetTotal.mtt_commande_net }"/>
													</span>
												</td>
												<td colspan="3"></td>
											</tr>
										</c:if>	
								</cplx:body>
							</cplx:table>
							
							<br><br>
							<!-- Liste des commandes avec lignes annulées -->
							<cplx:table name="list_mouvementDet_annulation" transitionType="simple" forceFilter="true" width="100%" title="Commandes avec des lignes annulées" initAction="caisse.caisse.find_annulation" checkable="false" autoHeight="true">
								<cplx:header>
									<cplx:th type="date" value="Date" field="caisseMouvement.date_vente" width="100" filterOnly="true" />	
									<cplx:th type="empty" />
									<cplx:th type="string" valueKey="caisseMouvement.ref_commande" field="caisseMouvement.ref_commande" width="145" />
									<cplx:th type="string[]" value="Caissier" field="caisseMouvement.opc_user.login" groupValues="${listUser }" groupKey="login" groupLabel="login"/>
									<cplx:th type="string[]" value="Confirmation" field="caisseMouvement.opc_user_annul.login" groupValues="${listUser }" groupKey="login" groupLabel="login"/>
									<cplx:th type="long[]" valueKey="caisseMouvement.opc_employe" field="caisseMouvement.opc_employe.id" groupValues="${listEmploye }" groupKey="id" groupLabel="nom" />
									<cplx:th type="long[]" value="Livreur" field="caisseMouvement.opc_livreurU.login" groupValues="${listLivreur }" groupKey="login" groupLabel="login"/>
									<cplx:th type="long[]" valueKey="caisseMouvement.opc_client" field="caisseMouvement.opc_client.id" groupValues="${listClient }" groupKey="id" groupLabel="nom" />
									<cplx:th type="decimal" value="Mtt. commande" field="caisseMouvement.mtt_commande" width="100" />
									<cplx:th type="decimal" value="Mtt. commande_net" field="caisseMouvement.mtt_commande_net" width="100" />
									<cplx:th type="decimal" style="color:red;" value="Mtt. annulation" field="caisseMouvement.mtt_annul_ligne" width="100" />
									<cplx:th type="string[]" valueKey="caisseMouvement.mode_paiement" field="caisseMouvement.mode_paiement" width="150" groupValues="${modePaie }" />
									<cplx:th type="empty" width="50"/>
								</cplx:header>
								<cplx:body>
								
									<c:set var="oldjour" value="${null }"></c:set>
									<c:forEach items="${list_annulationMouvementDet }" var="caisseMouvement">
										
										<c:if test="${oldjour == null  or oldjour != caisseMouvement.opc_caisse_journee.opc_journee.id }">
											<tr>
												<td colspan="11" noresize="true" class="separator-group">
													<span class="fa fa-fw fa-folder-open-o separator-icon"></span> <fmt:formatDate value="${caisseMouvement.date_vente}"/>
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
											<cplx:td style="color: red;" value="${caisseMouvement.ref_commande}"></cplx:td>
											<cplx:td style="color: blue;text-transform: uppercase;" value="${caisseMouvement.opc_user.login}"></cplx:td>
											<cplx:td style="color: red;text-transform: uppercase;">
												<c:set var="usrAnn" value="" />
												<c:forEach items="${caisseMouvement.list_article}" var="det">
													<c:if test="${det.opc_user_annul != null and usrAnn.indexOf(det.opc_user_annul.login) == -1 }">
														<c:set var="usrAnn" value="${det.opc_user_annul.login} | ${usrAnn}" />								
													</c:if>
												</c:forEach>
												${usrAnn }
											</cplx:td>
											<cplx:td value="${caisseMouvement.opc_employe.nom} ${caisseMouvement.opc_employe.prenom}"></cplx:td>
											<cplx:td align="center" value="${caisseMouvement.opc_livreurU.login}"></cplx:td>
											<cplx:td align="center" value="${caisseMouvement.opc_client.nom} ${caisseMouvement.opc_client.prenom}"></cplx:td>
											<cplx:td style="font-weight:bold;" align="right" value="${caisseMouvement.mtt_commande}"></cplx:td>
											<cplx:td style="font-weight:bold;" align="right" value="${caisseMouvement.mtt_commande_net}"></cplx:td>
											<cplx:td style="font-weight:bold;color:red;" align="right" value="${caisseMouvement.mtt_annul_ligne}"></cplx:td>
											<cplx:td align="center" style="color:green;" value="${caisseMouvement.mode_paiement}"></cplx:td>
											<cplx:td align="center">
												<std:link classStyle="btn btn-success shiny" style="color:black;" params="mvm=${caisseMouvement.id}" action="caisse-web.caisseWeb.print" icon="fa fa-print" targetDiv="div_gen_printer" tooltip="Imprimer le ticket" />
											</cplx:td>	
										</cplx:tr>
									</c:forEach>
									<c:if test="${!list_annulationMouvementDet.isEmpty()}">
											<tr class="sub">
												<td colspan="7"></td>
												<td align="right">
													<span style="font-size: 14px !important;font-weight: bold;height: 28px;" class="badge badge-blue">
														<fmt:formatDecimal value="${mvmDetTotalDet.mtt_commande }"/>
													</span>
												</td>
												<td align="right">
													<span style="font-size: 14px !important;font-weight: bold;height: 28px;" class="badge badge-blue">
														<fmt:formatDecimal value="${mvmDetTotalDet.mtt_commande_net }"/>
													</span>
												</td>
												<td align="right">
													<span style="font-size: 14px !important;font-weight: bold;height: 28px;color:red;" class="badge badge-blue">
														<fmt:formatDecimal value="${mvmDetTotalDet.mtt_annul_ligne }"/>
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
