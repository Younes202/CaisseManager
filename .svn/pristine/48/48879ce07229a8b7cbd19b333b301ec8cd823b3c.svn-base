<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>	

<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Gestion de stock</li>
		<li>Fiche fournisseur</li>
		<li class="active">Situation</li>
	</ul>
</div>
<!-- /Page Breadcrumb -->
<!-- Page Header -->
<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<std:link classStyle="btn btn-default" action="stock.fournisseur.init_situation" params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la recherche" />
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
	<std:form name="data-form">
		<!-- widget grid -->
		<div class="widget">
	
	<%if(ControllerUtil.getMenuAttribute("IS_MNU", request) == null){ %>	
	<div class="row">
		<%request.setAttribute("curMnu", "sit");  %>
		<jsp:include page="/domaine/stock/fournisseur_header_tab.jsp" />
     </div>  
     <%} %>   
      
		<div class="widget-body">
   
				<div class="row">
					<cplx:table name="list_mouvement" transitionType="simple" width="100%" title="Etat fournisseur" initAction="stock.fournisseur.situation_detail" autoHeight="true" checkable="false" >
						<cplx:header>
							<cplx:th type="decimal" field="mouvement.data_mouvement" value="Date" width="120" filterOnly="true" />
							<cplx:th type="decimal" field="mouvement.num_bl" value="Facture/BL/Reçu" width="120" />
							<cplx:th type="decimal" value="Paiement" />
							<cplx:th type="decimal" field="mouvement.type_mvmnt" value="Type" width="120" />
							<cplx:th type="decimal" field="mouvement.montant_ht" value="Montant HT" width="120" />
							<cplx:th type="decimal" field="mouvement.montant_tva" value="Montant TVA" width="120" />
							<cplx:th type="decimal" field="mouvement.montant_ttc" value="Montant TTC" width="120" />
							<cplx:th type="decimal" field="mouvement.montant_ttc_rem" value="Remise TTC" width="120" />
							
							<cplx:th type="decimal" value="réglé" width="120" filtrable="false" sortable="false" />
							<cplx:th type="decimal" value="Reste" width="120" filtrable="false" sortable="false"/>
							
						</cplx:header>
						<cplx:body>
							<c:set var="oldDate" value="${null }"></c:set>
							
							<c:forEach items="${list_mouvement }" var="mvm">
								<c:if test="${empty oldDate or mvm.date_mouvement != oldDate}">
								     <tr>
										<td colspan="9" noresize="true" class="separator-group">
											<span class="fa fa-fw fa-folder-open-o separator-icon"></span> 
											<fmt:formatDate value="${mvm.date_mouvement }" />
										</td>
									</tr>
								</c:if>		
								<c:set var="oldDate" value="${mvm.date_mouvement }"></c:set>
								
								<cplx:tr workId="${mvm.id }">
					                <cplx:td align="right" style="color:blue;">
					                	<c:choose>
											<c:when test="${not empty mvm.num_bl}">
												<c:set var="blFac" value="${mvm.num_bl}"/>
											</c:when>
											<c:when test="${not empty mvm.num_recu}">
												<c:set var="blFac" value="${mvm.num_recu}"/>
											</c:when>
											<c:otherwise>
												<c:set var="blFac" value="${mvm.num_facture}" />
											</c:otherwise>	
										</c:choose>									
										${blFac }
					                </cplx:td>
					                <cplx:td align="center" style="font-weight:bold;color:green;"> 
										${mvm.getPaiementsStr() }
									</cplx:td>
									<cplx:td align="center" style="font-weight:bold;color:green;"> 
										${mvm.type_mvmnt=='a' ? 'ACHAT':'AVOIR' }
									</cplx:td>
									<cplx:td value="${mvm.montant_ht }" align="right"/>
									<cplx:td value="${mvm.montant_tva }" align="right"/>
									<cplx:td style="font-weight:bold;" value="${mvm.montant_ttc }" align="right"/>
									<cplx:td align="right">
										<fmt:formatDecimal value="${mvm.montant_ttc_rem }" />
										<c:if test="${mvm.montant_ht_rem!=null and mvm.montant_ht_rem!=0 }">
											<i class="fa fa-fw fa-gift" style="color: red;font-size: 14px;" data-toggle="tooltip" data-placement="buttom" 
												data-original-title="
													HT avant rem :<br><fmt:formatDecimal value="${bigDecimalUtil.add(mvm.montant_ht, mvm.montant_ht_rem)}"/>
													<br>
													Remise HT :<br><fmt:formatDecimal value="${mvm.montant_ht_rem}"/>
													<br>
													Remise TTC :<br><fmt:formatDecimal value="${mvm.montant_ttc_rem}"/>
													<br>
													TTC avant rem :<br><fmt:formatDecimal value="${bigDecimalUtil.add(mvm.montant_ttc, mvm.montant_ttc_rem)}"/>
												"
											></i>
										</c:if>
									</cplx:td>
									<c:set var="paye" value="${mvm.getMttPaye() }" />
									<cplx:td align="right">
										<fmt:formatDecimal value="${paye}" />
									</cplx:td>	
									<cplx:td align="right" style="font-weight:bold;color:blue;">
										<fmt:formatDecimal value="${mvm.montant_ttc-paye}" />
									</cplx:td>
								</cplx:tr>
								
									<c:if test="${mvm.list_paiement.size() > 0 }">
										<tr>
											<td style="text-align: right;">
												PAIEMENTS
											</td>
											<td colspan="8">
												<table style="width: 54%;font-size: 11px;">
													<tr>
														<th style="width: 120px;background: #cbf5ff;">Date échéance</th>
														<th style="width: 120px;background: #cbf5ff;">Date encaissement</th>
														<th style="width: 150px;background: #cbf5ff;">Numéro</th>
														<th style="background: #cbf5ff;">Mode</th>
														<th style="width: 80px;background: #cbf5ff;">Montant</th>
													</tr>
														<c:forEach items="${mvm.list_paiement }" var="paiement">
															<tr>
																<td style="background-color: #e8e8e8;text-align: center;">
																	<fmt:formatDate value="${paiement.date_echeance}"/>
																</td>
																<td style="background-color: #e8e8e8;text-align: center;">
																	<fmt:formatDate value="${paiement.date_encaissement}"/>
																</td>
																<td style="background-color: #e8e8e8;text-align: center;">
																	${num_cheque } ${num_virement }
																</td>
																<td style="color:blue;background-color: #e8e8e8;">${paiement.opc_financement_enum.libelle }</td>
																<td align="right" style="color:blue;background-color: #e8e8e8;"><fmt:formatDecimal value="${paiement.montant }"/></td>
															</tr>
													</c:forEach>
												</table>
											</td>
										</tr>	
									</c:if>
								</c:forEach>		
						</cplx:body>
					</cplx:table>
				</div>
		</div>
		</div>
	</std:form>
</div>
