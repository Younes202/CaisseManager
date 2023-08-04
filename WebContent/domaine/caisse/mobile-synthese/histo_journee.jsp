<%@page import="framework.model.common.util.BooleanUtil"%>
<%@page import="appli.model.domaine.vente.persistant.JourneePersistant"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.controller.Context"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp" %>

<c:set var="bigDecimalUtil" value="<%=new BigDecimalUtil() %>" />
<c:set var="zero" value="<%=BigDecimalUtil.ZERO %>" />                    

<style>
#list_journee_body td{
	vertical-align: top;
    border: 0px;
    text-align: right;
    background-color: #faf9f2;
 }
</style>

<std:form name="search-form">
	<cplx:table name="list_journee" transitionType="simple" width="100%" forceFilter="true" titleKey="journee.list" initAction="caisse.journee.work_find" checkable="false" autoHeight="true">
		<cplx:header>
			<cplx:th type="date" value="Journée" field="journee.date_journee" width="150"/>
			<cplx:th type="string" valueKey="journee.statut_journee" field="journee.statut_journee" groupValues="${statutArray}" filterOnly="true" /> 
			<cplx:th type="integer" value="Nbr.vente" field="journee.nbr_vente" width="40" filterOnly="true" />
			<cplx:th type="decimal" valueKey="caisseJournee.mtt_ouverture" field="journee.mtt_ouverture" width="70" filterOnly="true" />
			
			<cplx:th type="decimal" value="Montans" field="journee.mtt_total_net"/>
			<cplx:th type="decimal" value="C&ocirc;ture" field="journee.mtt_cloture_caissier" width="90" filterOnly="true"/>
			<cplx:th type="decimal" value="Ecart" width="70" filtrable="false" sortable="false" filterOnly="true"/>
			
			<cplx:th type="decimal" value="Réduction Cmd" field="journee.mtt_reduction" width="60" filterOnly="true"/>
			<cplx:th type="decimal" value="Réduction Art" field="journee.mtt_art_reduction" width="60" filterOnly="true"/>
			<cplx:th type="decimal" value="Offert" field="journee.mtt_art_offert" width="60" filterOnly="true"/>
			<cplx:th type="decimal" value="Annulation" style="color:red;" field="journee.mtt_annule" width="70" filterOnly="true"/>
			<cplx:th type="decimal" value="Annu. ligne" style="color:red;" field="journee.mtt_annule_ligne" width="70" filterOnly="true"/>
			<cplx:th type="decimal" value="Livraison" width="70" filtrable="false" sortable="false" filterOnly="true"/>
			<cplx:th type="decimal" value="Portefeuille" width="70" filtrable="false" sortable="false" filterOnly="true"/>
			<cplx:th type="decimal" value="Fidélité" width="70" filtrable="false" sortable="false" filterOnly="true"/>
			
			<cplx:th type="decimal" value="Marge brut" width="80" filtrable="false" sortable="false" filterOnly="true"/>
		</cplx:header>
		<cplx:body>
			<c:forEach items="${list_journee }" var="journee">
				<tr>
					<td colspan="2" style="font-size: 17px;
					    font-weight: bold;
					    text-align: center;
					    background-color: black;
					    color: white;"><fmt:formatDate value="${journee.date_journee}" /></td>
				</tr>
				<cplx:tr workId="${journee.id }" style="font-size: 12px;border-bottom: 5px solid black;">
					<cplx:td>
						<table>
							<tr>
								<td>Statut</td>
								<td>
									<c:choose>
										<c:when test="${journee.statut_journee =='O' }">
											<span class="label" style="font-weight: bold; color: green;">Ouverte</span>
										</c:when>
										<c:when test="${journee.statut_journee =='C' }">
											<span class="label" style="font-weight: bold; color:orange;">Cl&ocirc;turée</span>
										</c:when>
									</c:choose>
								</td>
							</tr>
							<tr>
								<td>Nbr vente :</td>
								<td>${journee.nbr_vente}</td>
							</tr>
							<tr>
								<td>Ouverture</td>
								<td><fmt:formatDecimal value="${journee.mtt_ouverture}" /></td>
							</tr>		
						</table>
					</cplx:td>
					
					<cplx:td>
						<c:if test="${journee.mtt_cloture_caissier>0 }">
							<c:set var="ecart" value="${journee.getEcartNet() }" />
						</c:if>
						<table>
							<tr>
								<td>Montant calculé : </td>
								<td>
									<span style="font-weight:bold;">${bigDecimalUtil.formatNumberZeroBd(journee.mtt_total_net)}</span>
									<img class="tooltip-blue" data-toggle="tooltip" data-placement="buttom" 
										data-original-title="Esp&egrave;ces : ${bigDecimalUtil.formatNumberZeroBd(journee.mtt_espece)}
												<br>Ch&egrave;que : ${bigDecimalUtil.formatNumberZeroBd(journee.mtt_cheque)}
												<br>Carte : ${bigDecimalUtil.formatNumberZeroBd(journee.mtt_cb)}
												<br>Déj. : ${bigDecimalUtil.formatNumberZeroBd(journee.mtt_dej)}
												<br>Fidélité : ${bigDecimalUtil.formatNumberZeroBd(journee.mtt_donne_point)}
												<br>Portefeuille : ${bigDecimalUtil.formatNumberZeroBd(journee.mtt_portefeuille)}
												<br>Réduction Cmd : ${bigDecimalUtil.formatNumberZeroBd(journee.mtt_reduction)}
												<br>Réduction Art : ${bigDecimalUtil.formatNumberZeroBd(journee.mtt_art_reduction)}
												<br>Art. offerts : ${bigDecimalUtil.formatNumberZeroBd(journee.mtt_art_offert)}
												<br>Annulé CMD: ${bigDecimalUtil.formatNumberZeroBd(journee.mtt_annule)}
												<br>Annulé ligne: ${bigDecimalUtil.formatNumberZeroBd(journee.mtt_annule_ligne)}
												<br>Livraisons : ${bigDecimalUtil.formatNumberZeroBd(journee.mtt_livraison)}
												<br>Part livreur : ${bigDecimalUtil.formatNumberZeroBd(journee.getMttLivraisonPartLivreur())}
												" src="resources/framework/img/exclamation.png" style="vertical-align: bottom;"/>
									</span>
								</td>
							</tr>
							<c:if test="${journee.statut_journee =='C' }">
								<tr>
									<td>
										Montant clôture :
									</td>
									<td style="font-weight:bold;">
										${bigDecimalUtil.formatNumberZeroBd(bigDecimalUtil.substract(journee.mtt_cloture_caissier, journee.mtt_ouverture))}
										<img class="tooltip-blue" data-toggle="tooltip" data-placement="buttom" data-original-title="Esp&egrave;ces : ${bigDecimalUtil.formatNumberZeroBd(bigDecimalUtil.substract(journee.mtt_cloture_caissier_espece,journee.mtt_ouverture))}<br>Ch&egrave;que : ${bigDecimalUtil.formatNumberZeroBd(journee.mtt_cloture_caissier_cheque)}<br>Ch&egrave;que déj. : ${bigDecimalUtil.formatNumberZeroBd(journee.mtt_cloture_caissier_dej)}<br>Carte : ${bigDecimalUtil.formatNumberZeroBd(journee.mtt_cloture_caissier_cb)}<br>Fidélité : ${bigDecimalUtil.formatNumberZeroBd(journee.mtt_donne_point)}<br>Portefeuille : ${bigDecimalUtil.formatNumberZeroBd(journee.mtt_portefeuille)}" src="resources/framework/img/exclamation.png" style="vertical-align: bottom;"/>
									</td>
								</tr>	
							</c:if>	
							<tr>
								<td>Ecart</td>
								<td style="color:${ecart.compareTo(zero)<0?'red':'green' };font-weight:bold;">
									${bigDecimalUtil.formatNumberZeroBd(ecart) }
								</td>	
							</tr>
							<tr>
								<td>Réduction Cmd</td>
								<td style="color:green;">
									${bigDecimalUtil.isZero(journee.mtt_reduction)?null:bigDecimalUtil.formatNumberZeroBd(journee.mtt_reduction) }
								</td>
							</tr>
							<tr>
								<td>Réduction Art</td>
								<td style="color:green;">
									${bigDecimalUtil.isZero(journee.mtt_art_reduction)?null:bigDecimalUtil.formatNumberZeroBd(journee.mtt_art_reduction) }
								</td>
							</tr>
							<tr>
								<td>Offre</td>
								<td style="color:green;">
									${bigDecimalUtil.isZero(journee.mtt_art_offert)?null:bigDecimalUtil.formatNumberZeroBd(journee.mtt_art_offert) }
								</td>
							</tr>		
							<tr>
								<td>Annulation CMD</td>
								<td style="color:red;">
									${bigDecimalUtil.isZero(journee.mtt_annule)?'': bigDecimalUtil.formatNumberZeroBd(journee.mtt_annule) }
								</td>
							</tr>
							<tr>
								<td>Annulation ligne</td>
								<td style="color:red;">	
									${bigDecimalUtil.isZero(journee.mtt_annule_ligne)?'': bigDecimalUtil.formatNumberZeroBd(journee.mtt_annule_ligne) }
					 			</td>
							</tr>
							<tr>
								<td>Réduction</td>
								<td>
									${bigDecimalUtil.isZero(journee.getMttLivraisonGlobal())?null:journee.getMttLivraisonGlobal()}
									<c:if test="${!bigDecimalUtil.isZero(journee.getMttLivraisonGlobal())}">
										<img class="tooltip-blue" data-toggle="tooltip" data-placement="buttom" data-original-title="Nombre livraison : ${journee.nbr_livraison }<br>Tarif livraison : ${bigDecimalUtil.formatNumberZeroBd(journee.tarif_livraison)}" src="resources/framework/img/exclamation.png" style="vertical-align: bottom;"/>
									</c:if>
								</td>
							</tr>		
							<tr>
								<td>Portefeuille</td>
								<td style="color:${journee.mtt_portefeuille.compareTo(zero)<0?'red':'green' };">
									${bigDecimalUtil.isZero(journee.mtt_portefeuille)?'': bigDecimalUtil.formatNumberZeroBd(journee.mtt_portefeuille) }
								</td>
							</tr>
							<tr>
								<td>Point</td>
								<td style="color:${journee.mtt_donne_point.compareTo(zero)<0?'red':'green' };">
									${bigDecimalUtil.isZero(journee.mtt_donne_point)?'': bigDecimalUtil.formatNumberZeroBd(journee.mtt_donne_point) }
								</td>
							</tr>		
							<c:set var="margeJrnNet" value="${journee.mtt_total_net>0?(journee.mtt_total_net-journee.getMttLivraisonPartLivreur()-journee.mtt_total_achat):0}" />
							<tr>
								<td>Marge</td>
								<td style="color:#9c27b0;">
									${margeJrnNet!=journee.mtt_total_net ? bigDecimalUtil.formatNumberZeroBd(margeJrnNet) : '' }
									<c:if test="${margeJrnNet!=journee.mtt_total_net }">
										<img class="tooltip-blue" data-toggle="tooltip" data-placement="buttom" data-original-title="Ventes : ${bigDecimalUtil.formatNumberZeroBd(journee.mtt_total_net-journee.getMttLivraisonPartLivreur())}<br>Achats : ${bigDecimalUtil.formatNumberZeroBd(journee.mtt_total_achat)}" src="resources/framework/img/exclamation.png" style="vertical-align: bottom;"/>
									</c:if>
								</td>
							</tr>
						</table>			
					</cplx:td>
				</cplx:tr>
			</c:forEach>
			
			
		    <c:if test="${!list_journee.isEmpty()}">
				<tr class="sub" style="font-size: 12px !important;height: 37px;">
					<td colspan="2">
						<table style="width: 100%;">
							<tr>
								<td>Nbr vente : </td>
								<td>
									<span style="font-weight: bold;height: 20px;" class="badge badge-blue">
										${journee_total.nbr_vente }
									</span>
								</td>
							</tr>
							<tr>
								<td>Ouverture : </td>
								<td>
									<span style="font-weight: bold;height: 20px;" class="badge badge-blue">
										<fmt:formatDecimal value="${journee_total.mtt_ouverture }"/>
									</span>
								</td>
							</tr>
							<tr>
								<td>Total calculé</td>
								<td>
									<span style="font-weight: bold;height: 20px;" class="badge badge-blue">
										<fmt:formatDecimal value="${journee_total.mtt_total_net }"/>
									</span>
								</td>
							</tr>
							<c:if test="${journee_total.mtt_cloture_caissier>0 }">	
								<tr>
									<td>Clôture :</td>
									<td>
										<span style="font-weight: bold;height: 20px;" class="badge badge-blue">
											<fmt:formatDecimal value="${journee_total.mtt_cloture_caissier }"/>
										</span>
									</td>							
								</tr>
							</c:if>
							<tr>
								<td>Ecart :</td>
								<td>
									<span style="font-weight: bold;height: 20px;" class="badge badge-blue">
										<fmt:formatDecimal value="${journee_total.mtt_total }"/>
									</span>
								</td>
							</tr>
							<tr>
								<td>Réduction Cmd :</td>
								<td>
									<span style="font-weight: bold;height: 20px;" class="badge badge-blue">
										<fmt:formatDecimal value="${journee_total.mtt_reduction }"/>
									</span>
								</td>
							</tr>
							<tr>
								<td>Réduction Art :</td>
								<td>
									<span style="font-weight: bold;height: 20px;" class="badge badge-blue">
										<fmt:formatDecimal value="${journee_total.mtt_art_reduction }"/>
									</span>
								</td>
							</tr>
							<tr>
								<td>Offert :</td>
								<td>	
									<span style="font-weight: bold;height: 20px;" class="badge badge-blue">
										<fmt:formatDecimal value="${journee_total.mtt_art_offert }"/>
									</span>
								</td>
							</tr>
							<tr>
								<td>Annulé CMD :</td>					
								<td>
									<span style="font-weight: bold;height: 20px;color:red;" class="badge badge-blue">
										<fmt:formatDecimal value="${journee_total.mtt_annule }"/>
									</span>
								</td>
							</tr>
							<tr>
								<td>Annulé ligne :</td>	
								<td>
									<span style="font-weight: bold;height: 20px;color:red;" class="badge badge-blue">
										<fmt:formatDecimal value="${journee_total.mtt_annule_ligne }"/>
									</span>
								</td>
							</tr>
							<tr>
								<td>Livraison :</td>
								<td>
									<span style="font-weight: bold;height: 20px;" class="badge badge-blue">
										<fmt:formatDecimal value="${journee_total.mtt_livraison }"/>
									</span>
								</td>
							</tr>
							<tr>
								<td>Portefeuille :</td>	
								<td>
									<span style="font-weight: bold;height: 20px;" class="badge badge-blue">
										<fmt:formatDecimal value="${journee_total.mtt_portefeuille }"/>
									</span>
								</td>
							</tr>
							<tr>
								<td>Point :</td>	
								<td>
									<span style="font-weight: bold;height: 20px;" class="badge badge-blue">
										<fmt:formatDecimal value="${journee_total.mtt_donne_point }"/>
									</span>
								</td>
							</tr>
							<tr>
								<td>Marge :</td>	
								<td>
									<span style="font-weight: bold;height: 20px;" class="badge badge-blue">
										<fmt:formatDecimal value="${journee_total.mtt_cloture_caissier_cb }"/>
									</span>
								</td>
							</tr>	
						</table>
					</td>
				</tr>
			</c:if>	
		</cplx:body>
	</cplx:table>
 </std:form>			
</div>