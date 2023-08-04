<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp" %>

<std:form name="search-form">
	<div class="widget-header bordered-bottom bordered-blue">
		<span class="widget-caption">Offres et réductions</span>
		<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal" style="margin-top: 2px;margin-right: 5px;">
			<i class="fa fa-times"></i> Fermer
		</button>
	</div>
	<div class="widget-body">
		<cplx:table name="list_offre" transitionType="simple" width="100%" title="Liste des offres" autoHeight="true" initAction="caisse-web.caisseWeb.initOffre" exportable="false" checkable="false">
			<cplx:header>
				<cplx:th type="empty" width="50" />
				<cplx:th type="string" valueKey="offre.libelle" field="offre.libelle" />
				<cplx:th type="decimal" valueKey="offre.mtt_seuil" field="offre.mtt_seuil" width="120" />
				<cplx:th type="decimal" valueKey="offre.mtt_plafond" field="offre.mtt_plafond" width="120" />
				<cplx:th type="string" value="Type d'offre" field="offre.type_offre" groupValues="${typeOffreArray }" width="120" />
				<cplx:th type="decimal" valueKey="offre.taux_reduction" field="offre.taux_reduction" width="120" />
			</cplx:header>
			<cplx:body>
				<c:set var="oldDest" value="${null }" />
				<c:forEach items="${list_offre }" var="offre">
					<c:set var="currDest" value="${offre.destination }" />
					
					<c:if test="${currDest != oldDest }">
							<tr>
								<td colspan="6" noresize="true" class="separator-group" style="padding-left:10px;">
									<c:set var="lib" value="GLOBAL" />
									<c:choose>
										<c:when test="${currDest=='E' }">
											<c:set var="lib" value="EMPLOYÉ" />
										</c:when>
										<c:when test="${currDest=='C' }">
											<c:set var="lib" value="CLIENT" />
										</c:when>
										<c:when test="${currDest=='S' }">
											<c:set var="lib" value="SOCIÉTÉ LIVRAISON" />
										</c:when>
									</c:choose>
									<span class="fa fa-fw fa-folder-open-o separator-icon"></span> ${lib }	
								</td>
							</tr>
						</c:if>
						<c:set var="oldDest" value="${currDest }" /> 
					<cplx:tr workId="${offre.id }">
						<cplx:td>
							<std:link classStyle="btn btn-default" style="color:blue;" workId="${offre.id}" action="caisse-web.caisseWeb.selectOffre" targetDiv="left-div" icon="fa fa-eye" tooltip="Sélectionner" />
						</cplx:td>
						<cplx:td value="${offre.libelle}"></cplx:td>
						<cplx:td align="right" value="${offre.mtt_seuil}"></cplx:td>
						<cplx:td align="right" value="${offre.mtt_plafond}"></cplx:td>
						<cplx:td style="color:green" value="${offre.type_offre=='P'?'Prix d\\'achat':'Réduction' }"></cplx:td>
						<cplx:td align="right" value="${offre.taux_reduction}"></cplx:td>
					</cplx:tr>
				</c:forEach>
			</cplx:body>
		</cplx:table>
	</div>	
 </std:form>	

