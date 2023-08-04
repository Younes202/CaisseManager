<%@page import="java.util.List"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@page errorPage="/commun/error.jsp" %>

<c:set var="ContextAppli" value="<%=new ContextAppli()%>" />

			<%int idx = 0; %>
			<c:forEach items="${list_caisse }" var="caisse">
				<div class="col-lg-6 col-sm-6 col-xs-6">
					<div class="widget">
						<div class="widget-header bg-lightred">
						  <span class="widget-caption" style="font-weight: bold !important;margin-left: 10px;">
						  	${caisse.reference }
						  	</span>
						 </div>
						<div class="widget-body" style="overflow: auto;max-height: 500px;height: 500px;">
							<table class="table table-hover table-striped table-bordered" style="font-size: 11px;">
                                    <thead class="bordered-blueberry">
                                        <tr>
                                        	 <th style="width: 20px;"></th>
                                            <th style="width: 50px;"></th>
                                            <th>
                                                REFERENCE
                                            </th>
                                            <th>
                                                TYPE
                                            </th>
                                            <th>
                                                MONTANT
                                            </th>
                                            <th>
                                                STATUT
                                            </th>
                                            <th style="width: 90px;"></th>
                                        </tr>
                                    <c:set var="cptCmd" value="${1 }" />    
									<c:forEach items='<%=request.getAttribute("list_mouvement_"+idx) %>' var="caisseMvmDet">
										<tr>
											<td>
												${cptCmd }
											</td>
											<td>
												<std:linkPopup classStyle="btn btn-sm btn-primary" action="caisse.journee.edit_mouvement" workId="${caisseMvmDet.id }">
													<span class="fa  fa-eye"></span>
												</std:linkPopup>
											</td>
											<td style="font-weight: bold;color: blue;">${caisseMvmDet.ref_commande}</td>
											<td style="color:green;" align="center">
												<c:choose>
													<c:when test="${caisseMvmDet.type_commande=='E'}">A emporter</c:when>
													<c:when test="${caisseMvmDet.type_commande=='P'}">Sur place</c:when>
													<c:when test="${caisseMvmDet.type_commande=='L'}">Livraison</c:when>
												</c:choose>
											</td>
											<td style="font-weight:bold;" align="right"><fmt:formatDecimal value="${caisseMvmDet.mtt_commande_net}"/></td>
											<td align="center" style="color:blue;font-weight:bold;">${ContextAppli.getLibelleStatut(caisseMvmDet.last_statut)}</td>											
											<td nowrap="nowrap">
												<std:link classStyle="btn btn-success shiny btn-xs" workId="${caisseMvmDet.id}" targetDiv="targetRefreshDiv" params="caisseId=${caisse.id }" value="Statut" action="caisse_restau.caisseConfigurationRestau.majStatutCmd"/>
												<c:if test="${caisseMvmDet.last_statut=='VALIDE' }">
													<std:linkPopup classStyle="btn btn-success shiny btn-xs" workId="${caisseMvmDet.id}" tooltip="Tranf&eacute;rer" icon="fa fa-mail-reply-all" params="caisseId=${caisse.id }" action="caisse.caisseConfiguration.initTransfererCmdCuisine"/>
												</c:if>
											</td>
										</tr>
										<c:set var="cptCmd" value="${cptCmd+1 }" />  
									</c:forEach>
								</thead>
							</table>
	         			</div>
	        		</div>
	         	</div>
	         	
	         	<%
	         	idx++;
	         	%>
	         	
         	</c:forEach>
