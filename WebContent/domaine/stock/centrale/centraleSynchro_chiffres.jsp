<%@page import="appli.model.domaine.stock.persistant.centrale.CentraleEtsPersistant"%>
<%@page import="java.util.List"%>
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

<style>
.tooltip-inner{
	text-align: right !important;
}
</style>

<%
List<CentraleEtsPersistant> listEts = (List<CentraleEtsPersistant>)request.getAttribute("listEts");
%>	
 <!-- Page Breadcrumb -->
 <div class="page-breadcrumbs breadcrumbs-fixed">
     <ul class="breadcrumb">
         <li>
             <i class="fa fa-home"></i>
             <a href="#">Accueil</a>
         </li>
         <li>Synchronisation des données</li>
         <li class="active">Synthèses</li>
     </ul>
 </div>
<!-- /Page Breadcrumb -->
  <!-- Page Header -->
  <div class="page-header position-relative">
      <div class="header-title" style="padding-top: 4px;">
			<std:link classStyle="btn btn-default" action="stock.centraleSynchro.work_find" params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour" />      
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
              <div class="tabbable">

<c:set var="bigDecimalUtil" value="<%=new BigDecimalUtil() %>" />
<c:set var="zero" value="<%=BigDecimalUtil.ZERO %>" />                    

<!-- row -->
<std:form name="search-form">
	<div class="row">
        <div class="form-group">
        	<std:label classStyle="control-label col-md-2" value="Date début" />
            <div class="col-md-2">
                 <std:date name="dateDebut" value="${dateDebut }"/>
            </div>
            <div class="col-md-2" style="text-align: center;">
            	<std:link action="stock.centraleSynchro.loadChiffresEts" params="prev=1" icon="fa fa-arrow-circle-left" tooltip="Mois précédent" />
            	<std:link action="stock.centraleSynchro.loadChiffresEts" params="next=1" icon="fa fa-arrow-circle-right" tooltip="Mois suivant" />
            </div>
            
            <std:label classStyle="control-label col-md-1" value="Date fin" />
            <div class="col-md-2">
                 <std:date name="dateFin" value="${dateFin }"/>
            </div>
            <div class="col-md-2">
           	 	<std:button action="stock.centraleSynchro.loadChiffresEts" value="Filtrer" classStyle="btn btn-primary" />
           	 </div>	
       </div>
   </div>
	<h2>JOURNEES</h2>
	
	<cplx:table name="list_journee" transitionType="simple" paginate="false" showTitleBanner="false" 
					width="100%" filtrable="false" sortable="false" titleKey="journee.list" initAction="" checkable="false" autoHeight="true">
		<cplx:header>
			<cplx:th type="date" valueKey="journee.date_journee" field="journee.date_journee" width="70"/>
			<cplx:th type="string" valueKey="journee.statut_journee" field="journee.statut_journee" groupValues="${statutArray}" /> 
			<cplx:th type="integer" value="Nbr.vente" field="journee.nbr_vente" width="40" />
			<cplx:th type="decimal" valueKey="caisseJournee.mtt_ouverture" field="journee.mtt_ouverture" width="70" />
			<cplx:th type="decimal" value="Calculé" field="journee.mtt_total_net" width="90" />
			<cplx:th type="decimal" value="C&ocirc;ture" field="journee.mtt_cloture_caissier" width="90" />
			<cplx:th type="decimal" value="Ecart" width="70" filtrable="false" sortable="false" />
			<cplx:th type="decimal" value="Réduction" field="journee.mtt_reduction" width="60" />
			<cplx:th type="decimal" value="Offert" field="journee.mtt_art_offert" width="60" />
			<cplx:th type="decimal" value="Annulation" style="color:red;" field="journee.mtt_annule" width="70" />
			<cplx:th type="decimal" value="Annu. ligne" style="color:red;" field="journee.mtt_annule_ligne" width="70" />
			<cplx:th type="decimal" value="Livraison" width="70" filtrable="false" sortable="false" />
			<cplx:th type="decimal" value="Portefeuille" width="70" filtrable="false" sortable="false" />
			<cplx:th type="decimal" value="Fidélité" width="70" filtrable="false" sortable="false" />
			<cplx:th type="decimal" value="Marge brut" width="80" filtrable="false" sortable="false" />
		</cplx:header>
		<cplx:body>
		
		
		<%for(CentraleEtsPersistant centraleP : listEts){ %>
			<tr>
				<td colspan="15">
					<span style="font-size: 14px;font-weight: bold;"><%=centraleP.getNom() %></span>
				<td>
			</tr>
			
			<c:set var="list_journee" value='<%=request.getAttribute(centraleP.getNom()+"_listJ") %>' />
			<c:set var="journee_total" value='<%=request.getAttribute(centraleP.getNom()+"_totalJ") %>' />
		
			<c:forEach items="${list_journee }" var="journee">
				<cplx:tr workId="${journee.id }" style="font-size: 12px;">
					<cplx:td value="${journee.date_journee}"></cplx:td>
					<cplx:td align="center">
						<c:choose>
							<c:when test="${journee.statut_journee =='O' }">
								<span class="label" style="font-weight: bold; color: green;">Ouverte</span>
							</c:when>
							<c:when test="${journee.statut_journee =='C' }">
								<span class="label" style="font-weight: bold; color:orange;">Cl&ocirc;turée</span>
							</c:when>
						</c:choose>
					</cplx:td>
					<cplx:td align="right" value="${journee.nbr_vente}"></cplx:td>
					<cplx:td align="right" value="${journee.mtt_ouverture}"></cplx:td>
					
					<c:if test="${journee.mtt_cloture_caissier>0 }">
						<c:set var="ecart" value="${journee.getEcartNet() }" />
					</c:if>
							 				
					<cplx:td style="font-weight:bold;" align="right" value="${journee.mtt_total_net}">
						<img class="tooltip-blue" data-toggle="tooltip" data-placement="buttom" 
								data-original-title="Esp&egrave;ces : ${bigDecimalUtil.formatNumberZeroBd(journee.mtt_espece)}
										<br>Ch&egrave;que : ${bigDecimalUtil.formatNumberZeroBd(journee.mtt_cheque)}
										<br>Carte : ${bigDecimalUtil.formatNumberZeroBd(journee.mtt_cb)}
										<br>Déj. : ${bigDecimalUtil.formatNumberZeroBd(journee.mtt_dej)}
										<br>Fidélité : ${bigDecimalUtil.formatNumberZeroBd(journee.mtt_donne_point)}
										<br>Portefeuille : ${bigDecimalUtil.formatNumberZeroBd(journee.mtt_portefeuille)}
										<br>Réduction : ${bigDecimalUtil.formatNumberZeroBd(journee.mtt_reduction)}
										<br>Art. offerts : ${bigDecimalUtil.formatNumberZeroBd(journee.mtt_art_offert)}
										<br>Annulé CMD: ${bigDecimalUtil.formatNumberZeroBd(journee.mtt_annule)}
										<br>Annulé ligne: ${bigDecimalUtil.formatNumberZeroBd(journee.mtt_annule_ligne)}
										<br>Livraisons : ${bigDecimalUtil.formatNumberZeroBd(journee.mtt_livraison)}
										<br>Part livreur : ${bigDecimalUtil.formatNumberZeroBd(journee.getMttLivraisonPartLivreur())}
										" src="resources/framework/img/exclamation.png" style="vertical-align: bottom;"/>
					</cplx:td>
					<cplx:td align="right" style="font-weight:bold;">
						<c:if test="${journee.statut_journee =='C' }">
							${bigDecimalUtil.formatNumberZeroBd(bigDecimalUtil.substract(journee.mtt_cloture_caissier, journee.mtt_ouverture))}
							<img class="tooltip-blue" data-toggle="tooltip" data-placement="buttom" data-original-title="Esp&egrave;ces : ${bigDecimalUtil.formatNumberZeroBd(bigDecimalUtil.substract(journee.mtt_cloture_caissier_espece,journee.mtt_ouverture))}<br>Ch&egrave;que : ${bigDecimalUtil.formatNumberZeroBd(journee.mtt_cloture_caissier_cheque)}<br>Ch&egrave;que déj. : ${bigDecimalUtil.formatNumberZeroBd(journee.mtt_cloture_caissier_dej)}<br>Carte : ${bigDecimalUtil.formatNumberZeroBd(journee.mtt_cloture_caissier_cb)}<br>Fidélité : ${bigDecimalUtil.formatNumberZeroBd(journee.mtt_donne_point)}<br>Portefeuille : ${bigDecimalUtil.formatNumberZeroBd(journee.mtt_portefeuille)}" src="resources/framework/img/exclamation.png" style="vertical-align: bottom;"/>
						</c:if>	
					</cplx:td>
					<cplx:td align="right" style="color:${ecart.compareTo(zero)<0?'red':'green' };font-weight:bold;" value="${ecart }"></cplx:td>
					
					<cplx:td align="right" style="color:green;" value="${bigDecimalUtil.isZero(journee.mtt_reduction)?null:journee.mtt_reduction }"></cplx:td>
					<cplx:td align="right" style="color:green;" value="${bigDecimalUtil.isZero(journee.mtt_art_offert)?null:journee.mtt_art_offert }"></cplx:td>
					
					<cplx:td align="right" style="color:red;">
						${bigDecimalUtil.isZero(journee.mtt_annule)?'': bigDecimalUtil.formatNumberZeroBd(journee.mtt_annule) }
					</cplx:td>
					<cplx:td align="right" style="color:red;">
						${bigDecimalUtil.isZero(journee.mtt_annule_ligne)?'': bigDecimalUtil.formatNumberZeroBd(journee.mtt_annule_ligne) }
					 </cplx:td>
					
					<cplx:td align="right" value="${bigDecimalUtil.isZero(journee.getMttLivraisonGlobal())?null:journee.getMttLivraisonGlobal()}">
						<c:if test="${!bigDecimalUtil.isZero(journee.getMttLivraisonGlobal())}">
							<img class="tooltip-blue" data-toggle="tooltip" data-placement="buttom" data-original-title="Nombre livraison : ${journee.nbr_livraison }<br>Tarif livraison : ${bigDecimalUtil.formatNumberZeroBd(journee.tarif_livraison)}" src="resources/framework/img/exclamation.png" style="vertical-align: bottom;"/>
						</c:if>
					</cplx:td>
					
					<cplx:td align="right" style="color:${journee.mtt_portefeuille.compareTo(zero)<0?'red':'green' };">
						${bigDecimalUtil.isZero(journee.mtt_portefeuille)?'': bigDecimalUtil.formatNumberZeroBd(journee.mtt_portefeuille) }
					 </cplx:td>
					<cplx:td align="right" style="color:${journee.mtt_donne_point.compareTo(zero)<0?'red':'green' };">
						${bigDecimalUtil.isZero(journee.mtt_donne_point)?'': bigDecimalUtil.formatNumberZeroBd(journee.mtt_donne_point) }
					 </cplx:td>
					
					<c:set var="margeJrnNet" value="${journee.mtt_total_net>0?(journee.mtt_total_net-journee.getMttLivraisonPartLivreur()-journee.mtt_total_achat):0}" />
					
					<cplx:td align="right" style="color:#9c27b0;" value="${margeJrnNet!=journee.mtt_total_net ? margeJrnNet : '' }">
						<c:if test="${margeJrnNet!=journee.mtt_total_net }">
							<img class="tooltip-blue" data-toggle="tooltip" data-placement="buttom" data-original-title="Ventes : ${bigDecimalUtil.formatNumberZeroBd(journee.mtt_total_net-journee.getMttLivraisonPartLivreur())}<br>Achats : ${bigDecimalUtil.formatNumberZeroBd(journee.mtt_total_achat)}" src="resources/framework/img/exclamation.png" style="vertical-align: bottom;"/>
						</c:if>
					</cplx:td>
				</cplx:tr>
			</c:forEach>
			
			
		    <c:if test="${!list_journee.isEmpty()}">
				<tr class="sub" style="font-size: 12px !important;height: 37px;">
					<td colspan="2"></td>
					<td align="right">
						<span style="font-weight: bold;height: 20px;" class="badge badge-blue">
							${journee_total.nbr_vente }
						</span>
					</td>
					<td align="right">
						<span style="font-weight: bold;height: 20px;" class="badge badge-blue">
							<fmt:formatDecimal value="${journee_total.mtt_ouverture }"/>
						</span>
					</td>
					<!-- NET -->				
					<td align="right">
						<span style="font-weight: bold;height: 20px;" class="badge badge-blue">
							<fmt:formatDecimal value="${journee_total.mtt_total_net }"/>
						</span>
					</td>
					<!-- CLOTURE -->
					<td align="right">
						<c:if test="${journee_total.mtt_cloture_caissier>0 }">
							<span style="font-weight: bold;height: 20px;" class="badge badge-blue">
								<fmt:formatDecimal value="${journee_total.mtt_cloture_caissier }"/>
							</span>
						</c:if>
					</td>
					<!-- ECART -->
					<td align="right">
						<span style="font-weight: bold;height: 20px;" class="badge badge-blue">
							<fmt:formatDecimal value="${journee_total.mtt_total }"/>
						</span>
					</td>					
					
					<td align="right">
						<span style="font-weight: bold;height: 20px;" class="badge badge-blue">
							<fmt:formatDecimal value="${journee_total.mtt_reduction }"/>
						</span>
					</td>
					<td align="right">
						<span style="font-weight: bold;height: 20px;" class="badge badge-blue">
							<fmt:formatDecimal value="${journee_total.mtt_art_offert }"/>
						</span>
					</td>
					
					<td align="right">
						<span style="font-weight: bold;height: 20px;color:red;" class="badge badge-blue">
							<fmt:formatDecimal value="${journee_total.mtt_annule }"/>
						</span>
					</td>
					<td align="right">
						<span style="font-weight: bold;height: 20px;color:red;" class="badge badge-blue">
							<fmt:formatDecimal value="${journee_total.mtt_annule_ligne }"/>
						</span>
					</td>

					<td align="right">
						<span style="font-weight: bold;height: 20px;" class="badge badge-blue">
							<fmt:formatDecimal value="${journee_total.mtt_livraison }"/>
						</span>
					</td>
					<td align="right">
						<span style="font-weight: bold;height: 20px;" class="badge badge-blue">
							<fmt:formatDecimal value="${journee_total.mtt_portefeuille }"/>
						</span>
					</td>
					<td align="right">
						<span style="font-weight: bold;height: 20px;" class="badge badge-blue">
							<fmt:formatDecimal value="${journee_total.mtt_donne_point }"/>
						</span>
					</td>
					<td align="right">
						<span style="font-weight: bold;height: 20px;" class="badge badge-blue">
							<fmt:formatDecimal value="${journee_total.mtt_cloture_caissier_cb }"/>
						</span>
					</td>
				</tr>
			</c:if>	
			
	<%} %>		
			
		</cplx:body>
	</cplx:table>
	
	<h2>ETAT FINANCIER</h2>
	<cplx:table name="list_etat" transitionType="simple" paginate="false" showTitleBanner="false" width="100%" 
						filtrable="false" sortable="false" title="Etat financier" initAction="" checkable="false" autoHeight="true">
		<cplx:header>
			<cplx:th value="Ventes" type="decimal" />
			<cplx:th value="Recettes" type="decimal" />
			<cplx:th value="Avoirs" type="decimal" />
			
			<cplx:th value="Salaires" type="decimal" />
			<cplx:th value="Achats" type="decimal" />
			<cplx:th value="Dépenses" type="decimal" />
		</cplx:header>
		<cplx:body>	
			<%for(CentraleEtsPersistant centraleP : listEts){ %>
				<c:set var="etat_financier" value='<%=request.getAttribute(centraleP.getNom()+"_etat") %>' />
				<tr>
					<td colspan="6">
						<span style="font-size: 14px;font-weight: bold;"><%=centraleP.getNom() %></span>
					<td>
				</tr>
				<cplx:tr>
					<cplx:td align="right" value="${etat_financier.mtt_vente_caisse + etat_financier.mtt_vente_hors_caisse }" />
					<cplx:td align="right" value="${etat_financier.mtt_recette_divers }" />
					<cplx:td align="right" value="${etat_financier.mtt_avoir }" />
					
					<cplx:td align="right" value="${etat_financier.mtt_salaire }" />
					<cplx:td align="right" value="${etat_financier.mtt_achat }" />
					<cplx:td align="right" value="${etat_financier.mtt_depense_divers }" />
				</cplx:tr>
			<%} %>
		</cplx:body>
	</cplx:table>
	
 </std:form>			
</div>
 			</div>
		</div>
	</div>
	
<script type="text/javascript">
/*Handles ToolTips*/
$("[data-toggle=tooltip]")
    .tooltip({
        html: true
    });
</script> 