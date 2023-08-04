<%@page import="appli.model.domaine.stock.service.impl.RepartitionBean"%>
<%@page import="appli.model.domaine.vente.persistant.CaisseJourneePersistant"%>
<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="java.util.Map"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="java.util.Date"%>
<%@page import="framework.controller.Context"%>
<%@page import="framework.model.common.util.DateUtil"%>
<%@page import="java.util.List"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="appli.model.domaine.vente.persistant.JourneePersistant"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%> 
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<style>
#journee_body .databox .databox-text {
	font-size: 12px;
}
</style>

<%
	JourneePersistant journeeVente = (JourneePersistant) request.getAttribute("journeeView");
    BigDecimal fraisLivraison = journeeVente.getTarif_livraison();
    String devise = StrimUtil.getGlobalConfigPropertie("devise.html");
    boolean isJourneeOuverte = "O".equals(journeeVente.getStatut_journee());
    boolean isPortefeuille =  StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("PORTEFEUILLE"));
    boolean isPoints =  StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("POINTS"));
%>

<div class="row">
	<h1 style="text-align: center;
	    background-color: #a7daf1;
	    padding-top: 2px;
	    padding-bottom: 5px;
	    border: 1px solid #6f6f6f;">JOURNÉE <%=DateUtil.dateToString(journeeVente.getDate_journee()) %></h1>
	
	<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
		<div class="databox databox-xxlg databox-vertical databox-shadowed bg-white radius-bordered padding-5">
			<div class="databox-top">
				<div class="databox-row row-12" style="border-bottom: 1px solid gray; height: 122%;">
					<div class="databox-cell cell-8 text-center">
						<div class="databox-number number-xxlg sonic-silver"><%=BigDecimalUtil.formatNumberZero(journeeVente.getMtt_total_net())%>
							DH
						</div>
						<div class="databox-text storm-cloud">Montant net des ventes <i style="color:#427fed;" class="fa fa-info-circle" title="Montant net hors fond de roulement et frais de livraisons <%=!isJourneeOuverte ? "retirées":"inclus" %>"></i></div>
					</div>
					<div class="databox-cell cell-4 text-align-center">
						<div class="databox-number number-xxlg sonic-silver"><%=BigDecimalUtil.formatNumberZero(journeeVente.getNbr_vente())%></div>
						<div class="databox-text storm-cloud">
							Commandes<br>
						</div>
					</div>
				</div>
				<div class="row-12">
					<div class="databox-top" style="text-align: left;margin-top: 23px;">
						<div class="databox-row row-12" style="height: 122%;">
							<div class="col-md-12 databox-number sonic-silver">
								Responsable : <span style="color: #9c27b0;">${journeeView.opc_user.opc_employe.nom } ${journeeView.opc_user.opc_employe.prenom }</span>
							</div>
							<div style="margin-top: 18px;" class="col-md-12 databox-number sonic-silver">
								Statut : <span style="color: #9c27b0;">${journeeView.statut_journee=='C'?'Cloturée':'Ouverte' }</span>
							</div>
							<%
							if(!BigDecimalUtil.isZero(journeeVente.getSolde_coffre())){%>
							<div style="margin-top: 18px;" class="col-md-12 databox-number sonic-silver">
								Solde coffre : <span style="color: #9c27b0;"><%=BigDecimalUtil.formatNumber(journeeVente.getSolde_coffre()) %></span>
							</div>
							<%} %>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	
	<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
		<div class="databox databox-xxlg databox-vertical databox-shadowed bg-white radius-bordered padding-5" style=" height:550px;">
			<div class="databox-bottom">
				<div class="databox-row row-12">
					<div class="databox-cell cell-12 text-center  padding-5">
						<div id="pie-chart" class="chart chart"></div>
					</div>
					<div class="databox-cell cell-12 text-center no-padding-left padding-bottom-30">
						<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10">
							<span class="databox-text sonic-silver pull-left no-margin" style="font-weight: bold;">Type de paiement</span> <span
								class="databox-text sonic-silver pull-right no-margin uppercase">DH</span>
						</div>
						<%if(!BigDecimalUtil.isZero(journeeVente.getMtt_espece())){ %>
						<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10">
							<span class="badge badge-empty pull-left margin-5" style="background-color: #2dc3e8;"></span> <span
								class="databox-text darkgray pull-left no-margin">Especes</span> <span class="databox-text darkgray pull-right no-margin uppercase"> <%=BigDecimalUtil.formatNumberZero(journeeVente.getMtt_espece())%>
							</span>
						</div>
						<%} %>
						<%if(!BigDecimalUtil.isZero(journeeVente.getMtt_cheque())){ %>
						<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10">
							<span class="badge badge-empty pull-left margin-5" style="background-color: #fb6e52;"></span> <span
								class="databox-text darkgray pull-left no-margin">Chèques</span> <span class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumberZero(journeeVente.getMtt_cheque())%>
							</span>
						</div>
						<%} %>
						<%if(!BigDecimalUtil.isZero(journeeVente.getMtt_cb())){ %>
						<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10">
							<span class="badge badge-empty pull-left margin-5" style="background-color: #ffce55;"></span> <span
								class="databox-text darkgray pull-left no-margin">Carte bancaire</span> <span class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumberZero(journeeVente.getMtt_cb())%>
							</span>
						</div>
						<%} %>
						<%if(!BigDecimalUtil.isZero(journeeVente.getMtt_dej())){ %>
						<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10">
							<span class="badge badge-empty pull-left margin-5" style="background-color: #e75b8d;"></span> <span
								class="databox-text darkgray pull-left no-margin">Chèques déj.</span> <span class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumberZero(journeeVente.getMtt_dej())%>
							</span>
						</div>
						<%} %>
					<%if(!BigDecimalUtil.isZero(journeeVente.getMtt_donne_point()) && isPoints){ %>	
						<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10">
							<span class="badge badge-empty pull-left margin-5" style="background-color: green;"></span> <span
								class="databox-text darkgray pull-left no-margin">Points fidélité</span> <span class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumberZero(journeeVente.getMtt_donne_point())%>
							</span>
						</div>
					<%} %>
					<%if(!BigDecimalUtil.isZero(journeeVente.getMtt_portefeuille()) && isPortefeuille){ %>	
						<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10">
							<span class="badge badge-empty pull-left margin-5" style="background-color: #a0d468;"></span> <span
								class="databox-text darkgray pull-left no-margin">Portefeuille virtuel</span> <span class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumberZero(journeeVente.getMtt_portefeuille())%>
							</span>
						</div>
					<%} %>	
						
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
		<div class="databox databox-xxlg databox-vertical databox-shadowed bg-white radius-bordered padding-5" style="height: 350px;">
			<div class="databox-bottom">
				<div class="databox-row row-12">
					<!-- *************************** -->
						<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10">
							<span class="databox-text sonic-silver pull-left no-margin" style="font-weight: bold;">Détail des chiffress</span> <span
								class="databox-text sonic-silver pull-right no-margin uppercase">DH</span>
						</div>
						<%if(!BigDecimalUtil.isZero(journeeVente.getMtt_ouverture())){ %>
						<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10">
							<span class="badge badge-empty pull-left margin-5" style="background-color: #2196f3;"></span> <span
								class="databox-text darkgray pull-left no-margin">Fonds de roulement</span> 
								<span class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumberZero(journeeVente.getMtt_ouverture())%>
							</span>
						</div>
						<%} %>
						<%if(!BigDecimalUtil.isZero(journeeVente.getMtt_annule())){ %>
						<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10">
							<span class="badge badge-empty pull-left margin-5" style="background-color: #2196f3;"></span> <span
								class="databox-text darkgray pull-left no-margin">Annulées CMD</span> 
								<span style="color: red !important;" class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumberZero(journeeVente.getMtt_annule())%>
							</span>
						</div>
						<%} %>
						<%if(!BigDecimalUtil.isZero(journeeVente.getMtt_annule_ligne())){ %>
						<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10">
							<span class="badge badge-empty pull-left margin-5" style="background-color: #2196f3;"></span> <span
								class="databox-text darkgray pull-left no-margin">Annulées Ligne</span> 
								<span style="color: red !important;" class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumberZero(journeeVente.getMtt_annule_ligne())%>
							</span>
						</div>
						<%} %>
						<%if(!BigDecimalUtil.isZero(journeeVente.getMtt_reduction())){ %>
						<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10">
							<span class="badge badge-empty pull-left margin-5" style="background-color: #2196f3;"></span> <span
								class="databox-text darkgray pull-left no-margin">Réductions Cmd</span> <span class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumberZero(journeeVente.getMtt_reduction())%>
							</span>
						</div>
						<%} %>
						<%if(!BigDecimalUtil.isZero(journeeVente.getMtt_art_reduction())){ %>
						<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10">
							<span class="badge badge-empty pull-left margin-5" style="background-color: #2196f3;"></span> <span
								class="databox-text darkgray pull-left no-margin">Réductions Art</span> <span class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumberZero(journeeVente.getMtt_art_reduction())%>
							</span>
						</div>
						<%} %>
						<%if(!BigDecimalUtil.isZero(journeeVente.getMtt_art_offert())){ %>
						<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10">
							<span class="badge badge-empty pull-left margin-5" style="background-color: #2196f3;"></span> <span
								class="databox-text darkgray pull-left no-margin">Art. offerts</span> <span class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumberZero(journeeVente.getMtt_art_offert())%>
							</span>
						</div>
						<%} %>
						
						<%
						if(fraisLivraison != null){
							String val = BigDecimalUtil.formatNumberZero(journeeVente.getNbr_livraison()) 
									+" x "+BigDecimalUtil.formatNumber(fraisLivraison) + 
									devise;
						%>
						<!-- Livraison -->
						<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10">
							<span class="badge badge-empty pull-left margin-5" style="background-color: black;"></span> <span
								class="databox-text darkgray pull-left no-margin">Livraisons <i style="color:blue;" class="fa fa-info-circle" title="<%=val%>"></i></span> 
								<span class="databox-text darkgray pull-right no-margin uppercase">
									<%=BigDecimalUtil.formatNumber(BigDecimalUtil.multiply(fraisLivraison, BigDecimalUtil.get((journeeVente.getNbr_livraison()==null?0:journeeVente.getNbr_livraison())))) %>
								</span>
						</div>
						<%} %>
						
						<%if(!BigDecimalUtil.isZero(journeeVente.getMtt_total_achat())){ %>
						<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10">
							<span class="badge badge-empty pull-left margin-5" style="background-color: #2196f3;"></span> <span class="databox-text darkgray pull-left no-margin">
								<b style="color: green;">Marge brute</b> <i class="fa fa-info-circle" title="Prix de vente - Prix d'achat"></i>
							</span> 
							<%
							// Calcul marge
							BigDecimal mttMargeAll = BigDecimalUtil.substract(journeeVente.getMtt_total_net(), journeeVente.getMttLivraisonPartLivreur(), journeeVente.getMtt_total_achat());
							BigDecimal pourcentMarge = BigDecimalUtil.ZERO;
							if(!BigDecimalUtil.isZero(journeeVente.getMtt_total_achat())){
								pourcentMarge = BigDecimalUtil.divide(BigDecimalUtil.multiply(mttMargeAll, BigDecimalUtil.get(100)), journeeVente.getMtt_total_achat());
							}
							%>	
							<span
								class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumberZero(mttMargeAll) %> 
								 <i class="fa fa-info-circle" title="Total vente : <%=BigDecimalUtil.formatNumberZero(BigDecimalUtil.substract(journeeVente.getMtt_total_net(), journeeVente.getMttLivraisonPartLivreur())) %> | Total valeur achat : <%=BigDecimalUtil.formatNumberZero(journeeVente.getMtt_total_achat()) %> | % Marge brut : <%=BigDecimalUtil.formatNumberZero(pourcentMarge) %>%"></i>
								</span>
						</div>
						<%} %>
				</div>
			</div>
	</div>
</div>

<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
		<div class="databox databox-xxlg databox-vertical databox-shadowed bg-white radius-bordered padding-5" style="height: 400px;">
			<div class="databox-bottom">
				<div class="databox-row row-12">
					<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10">
							<span class="databox-text sonic-silver pull-left no-margin" style="font-weight: bold;">Clôture et écarts</span> <span
								class="databox-text sonic-silver pull-right no-margin uppercase">DH</span>
						</div>
						
						<%if(journeeVente.getMtt_cloture_caissier() != null){ %>
						<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10"> 
							<span class="badge badge-empty pull-left margin-5" style="background-color: #2196f3;"></span> <span
								class="databox-text darkgray pull-left no-margin">Montant net de clôture</span> <span class="databox-text darkgray pull-right no-margin uppercase">
								<%=BigDecimalUtil.formatNumberZero(BigDecimalUtil.substract(journeeVente.getMtt_cloture_caissier(), journeeVente.getMtt_ouverture()))%>
							</span>
						</div>
						
						<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 114px;color: blue;">
							<span class="badge badge-empty pull-left margin-5" style="background-color: #2196f3;"></span> <span
								class="databox-text darkgray pull-left no-margin">Clôture caissier </span>
								<i class="fa fa-info-circle" style="color: #9E9E9E;font-size: 12px;padding-top: 4px;padding-left: 4px;" title="Avec fonds de roulement"></i>
								<span class="databox-text darkgray pull-right no-margin uppercase">
										<%=BigDecimalUtil.formatNumberZero(journeeVente.getMtt_cloture_caissier())%>
							</span>
							
							<table style="font-size: 12px;width: 98%;">
								<tr style="border-bottom: 1px dashed #9E9E9E;">
									<td align="left" style="padding-left: 20px;"><span class="badge badge-empty pull-left margin-5" style="height: 6px;background-color: #e75b8d;"></span> Espèces : </td><td align="right">
										<%=BigDecimalUtil.formatNumberZero(journeeVente.getMtt_cloture_caissier_espece()) %> 
									</td>
								</tr>
								<tr style="border-bottom: 1px dashed #9E9E9E;">
									<td align="left" style="padding-left: 20px;"><span class="badge badge-empty pull-left margin-5" style="height: 6px;background-color: #e75b8d;"></span> Carte :</td><td align="right">
										<%=BigDecimalUtil.formatNumberZero(journeeVente.getMtt_cloture_caissier_cb()) %> 
									</td>
								</tr>
								<tr style="border-bottom: 1px dashed #9E9E9E;">
									<td align="left" style="padding-left: 20px;"><span class="badge badge-empty pull-left margin-5" style="height: 6px;background-color: #e75b8d;"></span> Chèque :</td><td align="right"> 
										<%=BigDecimalUtil.formatNumberZero(journeeVente.getMtt_cloture_caissier_cheque()) %>
									</td>
								</tr>
								<tr>	
									<td align="left" style="padding-left: 20px;"><span class="badge badge-empty pull-left margin-5" style="height: 6px;background-color: #e75b8d;"></span> Chèque déj. :</td><td align="right"> 
										<%=BigDecimalUtil.formatNumberZero(journeeVente.getMtt_cloture_caissier_dej()) %>
									</td>
								</tr>
								<%if(!BigDecimalUtil.isZero(journeeVente.getMtt_portefeuille())){ %>
								<tr>	
									<td align="left" style="padding-left: 20px;"><span class="badge badge-empty pull-left margin-5" style="height: 6px;background-color: #e75b8d;"></span> Portefeuille :</td><td align="right"> <%=BigDecimalUtil.formatNumberZero(journeeVente.getMtt_portefeuille()) %></td>
								</tr>
								<%} %>
								<%if(!BigDecimalUtil.isZero(journeeVente.getMtt_donne_point())){ %>
								<tr>	
									<td align="left" style="padding-left: 20px;"><span class="badge badge-empty pull-left margin-5" style="height: 6px;background-color: #e75b8d;"></span> Point :</td><td align="right"> <%=BigDecimalUtil.formatNumberZero(journeeVente.getMtt_donne_point()) %></td>
								</tr>
								<%} %>
							</table>
						</div>
						
						<%} %>
						
						
			<%
			if(Context.isOperationAvailable("SHIFT") && journeeVente.getMtt_cloture_caissier() != null){
				if(!BigDecimalUtil.isZero(journeeVente.getMtt_cloture_caissier())){ 
					BigDecimal mttClotureCaisserEsp = journeeVente.getMtt_cloture_caissier_espece();
					BigDecimal mttClotureCaisserCheq = journeeVente.getMtt_cloture_caissier_cheque();
					BigDecimal mttClotureCaisserDej = journeeVente.getMtt_cloture_caissier_dej();
					BigDecimal mttClotureCaisserCb = journeeVente.getMtt_cloture_caissier_cb();
					
					BigDecimal mttClotureCaissierEspNet = (mttClotureCaisserEsp==null?null:BigDecimalUtil.substract(mttClotureCaisserEsp, journeeVente.getMtt_ouverture()));

					BigDecimal mttClotureCaissier = BigDecimalUtil.add(mttClotureCaisserEsp, mttClotureCaisserCheq, mttClotureCaisserDej, mttClotureCaisserCb);
					BigDecimal mttRef = BigDecimalUtil.substract(journeeVente.getMtt_total_net(), journeeVente.getMtt_portefeuille(), journeeVente.getMtt_donne_point());
					BigDecimal mttClotureCaissierNet = (mttClotureCaisserEsp==null?null:BigDecimalUtil.substract(mttClotureCaissier, journeeVente.getMtt_ouverture()));
					
					BigDecimal ecartCaissier = BigDecimalUtil.substract(mttClotureCaissierNet, mttRef);
					BigDecimal ecartEspeses = BigDecimalUtil.substract(mttClotureCaissierEspNet, journeeVente.getMtt_espece());
					BigDecimal ecartCb = BigDecimalUtil.substract(mttClotureCaisserCb, journeeVente.getMtt_cb());
					BigDecimal ecartCheque = BigDecimalUtil.substract(mttClotureCaisserCheq, journeeVente.getMtt_cheque());
					BigDecimal ecartDej = BigDecimalUtil.substract(mttClotureCaisserDej, journeeVente.getMtt_dej());
			%>			
						<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 114px;">
							<span class="badge badge-empty pull-left margin-5" style="background-color: #2196f3;"></span> <span
								class="databox-text darkgray pull-left no-margin" style="color: <%=BigDecimalUtil.getStyle(ecartCaissier) %> !important;">Ecart des montants</span> 
								<span class="databox-text darkgray pull-right no-margin uppercase"><b style="color: <%=BigDecimalUtil.getStyle(ecartCaissier) %>;"><%=BigDecimalUtil.formatNumberZero(ecartCaissier)%></b>
							</span>
							<table style="font-size: 12px;width: 95%;margin-top: 16px;">
								<tr style="border-bottom: 1px dashed #9E9E9E;">
									<td align="left" style=""><span class="badge badge-empty pull-left margin-5" style="height: 6px;background-color: #ffce55;"></span> Espèces : </td>
									<td align="right" style="color: <%=BigDecimalUtil.getStyle(ecartEspeses) %>;"><%=BigDecimalUtil.formatNumberZero(ecartEspeses) %> </td>
								</tr>
								<tr style="border-bottom: 1px dashed #9E9E9E;">
									<td align="left" style=""><span class="badge badge-empty pull-left margin-5" style="height: 6px;background-color: #ffce55;"></span> Carte :</td>
									<td align="right" style="color: <%=BigDecimalUtil.getStyle(ecartCb) %>;"><%=BigDecimalUtil.formatNumberZero(ecartCb) %> </td>
								</tr>
								<tr style="border-bottom: 1px dashed #9E9E9E;">
									<td align="left" style=""><span class="badge badge-empty pull-left margin-5" style="height: 6px;background-color: #ffce55;"></span> Chèque :</td>
									<td align="right" style="color: <%=BigDecimalUtil.getStyle(ecartCheque) %>;"> <%=BigDecimalUtil.formatNumberZero(ecartCheque) %></td>
								</tr>
								<tr style="border-bottom: 1px dashed #9E9E9E;">	
									<td align="left" style=""><span class="badge badge-empty pull-left margin-5" style="height: 6px;background-color: #ffce55;"></span> Chèque déj. :</td>
									<td align="right" style="color: <%=BigDecimalUtil.getStyle(ecartDej) %>;"> <%=BigDecimalUtil.formatNumberZero(ecartDej) %></td>
								</tr>
							</table>
						</div>
			<%} 
			}%>			

					</div>
				</div>
			</div>
	</div>			
</div>




<h2 style="text-align: center;background-color: #ffc107;">CHIFFRES CAISSES</h2>					


<div class="row">	
							
				<%
				List<CaisseJourneePersistant> listDataShift = (List) request.getAttribute("listDataShift");
				//
				if(listDataShift != null){
					String oldCaisse = null;
					int idxShift = 0;
					for(CaisseJourneePersistant data : listDataShift){
						if((!Context.isOperationAvailable("SHIFT") && !"C".equals(data.getStatut_caisse())) || (!Context.isOperationAvailable("SHIFTCL") && !"O".equals(data.getStatut_caisse()))){
							continue;
						}
						boolean isDblCloture = (data.getMtt_cloture_old_espece() != null);
						// Caissier
						BigDecimal mttClotureCaisserEsp = data.getMtt_cloture_caissier_espece();
						BigDecimal mttClotureCaisserCheq = data.getMtt_cloture_caissier_cheque();
						BigDecimal mttClotureCaisserDej = data.getMtt_cloture_caissier_dej();
						BigDecimal mttClotureCaisserCb = data.getMtt_cloture_caissier_cb();
						// Manager
						BigDecimal mttClotureManagerEsp = null;
						BigDecimal mttClotureManagerCheq = null;
						BigDecimal mttClotureManagerDej = null;
						BigDecimal mttClotureManagerCb = null;
						//
						boolean isEspeceEcart = false;
						boolean isCarteEcart = false;
						boolean isChequeEcart = false; 
						boolean isDejEcart = false;
						//
						if(isDblCloture){
							// Caissier
							mttClotureCaisserEsp = data.getMtt_cloture_old_espece();
							mttClotureCaisserCheq = data.getMtt_cloture_old_cheque();
							mttClotureCaisserDej = data.getMtt_cloture_old_dej();
							mttClotureCaisserCb = data.getMtt_cloture_old_cb();
							// Manager
							mttClotureManagerEsp = data.getMtt_cloture_caissier_espece();
							mttClotureManagerCheq = data.getMtt_cloture_caissier_cheque();
							mttClotureManagerDej = data.getMtt_cloture_caissier_dej();
							mttClotureManagerCb = data.getMtt_cloture_caissier_cb();
							//
							isEspeceEcart = (mttClotureCaisserEsp!=null && mttClotureCaisserEsp.compareTo(mttClotureManagerEsp) != 0);
							isCarteEcart = (mttClotureCaisserCb!=null && mttClotureCaisserCb.compareTo(mttClotureManagerCb) != 0);
							isChequeEcart = (mttClotureCaisserCheq!=null && mttClotureCaisserCheq.compareTo(mttClotureManagerCheq) != 0); 
							isDejEcart = (mttClotureCaisserDej!=null && mttClotureCaisserDej.compareTo(mttClotureManagerDej) != 0);
						}
						
						BigDecimal mttClotureCaissier = BigDecimalUtil.add(mttClotureCaisserEsp, mttClotureCaisserCheq, mttClotureCaisserDej, mttClotureCaisserCb);
						BigDecimal mttClotureManager = BigDecimalUtil.add(mttClotureManagerEsp, mttClotureManagerCheq, mttClotureManagerDej, mttClotureManagerCb);
						
						BigDecimal mttClotureCaissierNet = (mttClotureCaisserEsp==null?null:BigDecimalUtil.substract(mttClotureCaissier, data.getMtt_ouverture()));
						BigDecimal mttClotureManagerNet = isDblCloture ? BigDecimalUtil.substract(mttClotureManager, data.getMtt_ouverture()) : null;
						
						BigDecimal mttClotureCaissierEspNet = (mttClotureCaisserEsp==null?null:BigDecimalUtil.substract(mttClotureCaisserEsp, data.getMtt_ouverture()));
						BigDecimal mttClotureManagerEspNet = isDblCloture ? BigDecimalUtil.substract(mttClotureManagerEsp, data.getMtt_ouverture()) : null;
						
						// On retire les montants non calculable 
						BigDecimal mttRef = BigDecimalUtil.substract(data.getMtt_total_net(), data.getMtt_portefeuille(), data.getMtt_donne_point());
						BigDecimal ecartCaissier = BigDecimalUtil.substract(mttClotureCaissierNet, mttRef);
						BigDecimal ecartManager = isDblCloture ? BigDecimalUtil.substract(mttClotureManagerNet, mttRef) : null;
						
						idxShift++;
						
						if(oldCaisse == null || !oldCaisse.equals(data.getOpc_caisse().getReference())){
							idxShift = 1;
						%>
							<h2 style="color: #8bc34a;text-align: center;width:100%;float:left;"><b><%=data.getOpc_caisse().getReference() %></b></h2>
							<hr>
						<%}
						oldCaisse = data.getOpc_caisse().getReference();
						%>			
					<!-- Detail shift -->
					<div class="col-lg-6 col-md-12 col-sm-12 col-xs-12">
						<div class="well with-header">
							<div class="header bordered-darkorange" style="font-weight: bold;color: #009688;height: 51px;text-align: center;">
								<h3>
									Shift <b><%=idxShift %></b>
									(
									<%if("O".equals(data.getStatut_caisse())){%>
										<span style='color:green;'>Ouvert</span>
									<%} else if("E".equals(data.getStatut_caisse())){%>
										<span style='color:orange;'>En Cloture</span>
									<%} else{%>
										<span style='color:red;'>Clos</span>
									<%} %>
									)
									  <b style="font-size: 14px;">de <%=DateUtil.dateToString(data.getDate_ouverture(), "HH:mm:ss") %> <%=data.getDate_cloture()!=null?"&agrave; "+StringUtil.getValueOrEmpty(DateUtil.dateToString(data.getDate_cloture(), "HH:mm:ss")): ""%></b>
									  
									  <%if(ContextAppli.getUserBean().getIs_RemoteAdmin()){ %>
										(<std:link actionGroup="C" style="font-size: 13px;" classStyle="" value="Recalculer" action="caisse.journee.recalculShifts" params='<%="cjId="+data.getId() %>' tooltip="Recalculer les chiffres des shifts" />)
									  <%} %>
								</h3>
							</div>
							<div class="databox databox-xxlg databox-vertical databox-shadowed bg-white radius-bordered padding-5" style="height: 580px;">
								<div class="databox-top">
									<div class="databox-row row-12" style="border-bottom: 1px solid gray;height: 50px;;">
										<div class="databox-cell cell-8 text-center">
											<div class="databox-number number-xxlg sonic-silver" style="font-size: 22px;"><%=BigDecimalUtil.formatNumberZero(data.getMtt_total_net())%> DH</div>
											<div class="databox-text storm-cloud">Montant net des ventes <i style="color:#427fed;" class="fa fa-info-circle" title="Montant net hors fond de roulement et frais de livraisons incluses"></i></div>
										</div>
										<div class="databox-cell cell-4 text-align-center">
											<div class="databox-number number-xxlg sonic-silver" style="font-size: 22px;"><%=BigDecimalUtil.formatNumberZero(data.getNbr_vente())%></div>
											<div class="databox-text storm-cloud">Commandes<br></div>
										</div>
									</div>
								</div>
								<div class="databox-bottom">
									<div class="databox-row row-12 bordered-bottom bordered-ivory padding-10" style="height: 43px;">
										<div class="databox-cell cell-6 text-center no-padding-left padding-bottom-30">	
											<span class="badge badge-empty pull-left margin-5" style="background-color: transparent;"></span> 
											<span class="databox-text darkgray pull-left no-margin">
											<span style="color: black;font-size: 13px;color: blue;">Ouverture :</span></span> 
											<span class="databox-text darkgray pull-left no-margin uppercase"> 
												<b style="color: black;font-size: 13px;">
												<%if(data.getOpc_user() != null){%>
												 	<%if(data.getOpc_user().getOpc_employe()==null){%>
													 	<%=data.getOpc_user().getLogin() %>
												 <% } else{%>
												 		<%=data.getOpc_user().getOpc_employe().getNom() %> <%=StringUtil.getValueOrEmpty(data.getOpc_user().getOpc_employe().getPrenom()) %>
												 <% }
												 } %>
												</b>
											</span>
										</div>
										<%if(data.getOpc_user_cloture() != null){%>	
										<div class="databox-cell cell-6 text-center no-padding-left padding-bottom-30">	
											<span class="badge badge-empty pull-left margin-5" style="background-color: transparent;"></span> 
											<span class="databox-text darkgray pull-left no-margin">
											<span style="color: black;font-size: 13px;color: blue;">Cl&ocirc;ture :</span></span> 
											<span class="databox-text darkgray pull-left no-margin uppercase"> 
												<b style="color: black;font-size: 13px;">
												
												 	<%if(data.getOpc_user_cloture().getOpc_employe()==null){%>
													 	<%=data.getOpc_user_cloture().getLogin() %>
												 <% } else{%>
												 		<%=data.getOpc_user_cloture().getOpc_employe().getNom() %> <%=StringUtil.getValueOrEmpty(data.getOpc_user_cloture().getOpc_employe().getPrenom()) %>
												 <% } %>
												</b>
											</span>
										</div>
										<% } %>
									</div>
									
									
									<%
										int ecartDash = data.getList_caisse_mouvement().size()-(data.getList_caisse_mouvement().size()+(data.getNbr_dash_open()!=null?data.getNbr_dash_open():0));
										if(ecartDash < 0 && data.getNbr_dash_open() > 0){ %>
										<div class="databox-row row-12" style="margin-top: 3px;height: 30px;">
											<div class="databox-cell cell-6 text-center no-padding-left padding-bottom-30">	
												<span style="color: blue;font-size: 12px;">Ecart ouverture tiroir </span> 
												<span style="color: orange;font-size: 11px;">
													<b><%=ecartDash*-1 %></b>
													<i style="color:blue;" class="fa fa-info-circle" title="Commandes : <%=data.getList_caisse_mouvement().size()%> - ouvertures : <%=(data.getList_caisse_mouvement().size()+data.getNbr_dash_open())%>"></i>
												</span>
											</div>
										</div>
										<%} %>
									
									
									<!-- ****** Detail ****** -->
									<div class="databox-row row-12" style="margin-top: 3px;height: 130px;">
										<div class="databox-cell cell-6 text-center no-padding-left padding-bottom-30">
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="badge badge-empty pull-left margin-5" style="background-color: #2196f3;"></span> 
												<span class="databox-text darkgray pull-left no-margin">Fonds de roulement</span> <span
													class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumberZero(data.getMtt_ouverture())%> </span>
											</div>
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="badge badge-empty pull-left margin-5" style="background-color: #2196f3;"></span> <span class="databox-text darkgray pull-left no-margin">Annulations Cmd</span> <span
													class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumberZero(data.getMtt_annule())%> </span>
											</div>
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="badge badge-empty pull-left margin-5" style="background-color: #2196f3;"></span> <span class="databox-text darkgray pull-left no-margin">Annulations Lignes</span> <span
													class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumberZero(data.getMtt_annule_ligne())%> </span>
											</div>
											<%
											if(fraisLivraison != null){
												String val = BigDecimalUtil.formatNumberZero(data.getNbr_livraison()) 
														+" x "+BigDecimalUtil.formatNumber(fraisLivraison) + 
														devise;
											%>
											<!-- Livraison -->
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="badge badge-empty pull-left margin-5" style="background-color: black;"></span> <span
													class="databox-text darkgray pull-left no-margin">Livraisons <i style="color:blue;" class="fa fa-info-circle" title="<%=val%>"></i></span> 
													<span class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumber(BigDecimalUtil.multiply(fraisLivraison, BigDecimalUtil.get((data.getNbr_livraison()==null?0:data.getNbr_livraison())))) %></span>
											</div>
											<%} %>
										</div>
										<div class="databox-cell cell-6 text-center no-padding-left padding-bottom-30">
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="badge badge-empty pull-left margin-5" style="background-color: #2196f3;"></span> <span class="databox-text darkgray pull-left no-margin">R&eacute;ductions Cmd</span> <span
													class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumberZero(data.getMtt_reduction())%> </span>
											</div>
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="badge badge-empty pull-left margin-5" style="background-color: #2196f3;"></span> <span class="databox-text darkgray pull-left no-margin">R&eacute;ductions Art</span> <span
													class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumberZero(data.getMtt_art_reduction())%> </span>
											</div>
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="badge badge-empty pull-left margin-5" style="background-color: #2196f3;"></span> <span class="databox-text darkgray pull-left no-margin">Articles offerts</span> <span
													class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumberZero(data.getMtt_art_offert())%> </span>
											</div>
										</div>	
									</div>
									<%if(mttClotureCaisserEsp != null){ %>
									<div class="databox-row row-12" style="margin-top: 20px;height: 50px;border-top: 2px solid red;">
										<div class="databox-cell cell-6 text-center no-padding-left padding-bottom-30">
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="color:#9c27b0;height: 34px !important;">
												<span class="badge badge-empty pull-left margin-5" style="background-color: #2196f3;"></span> <span class="databox-text darkgray pull-left no-margin">Cl&ocirc;ture caissier</span> <span
													class="databox-text darkgray pull-right no-margin uppercase" style="font-weight: bold;"><%=BigDecimalUtil.formatNumberZero(mttClotureCaissier)%> </span>
											</div>
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="badge badge-empty pull-left margin-5" style="background-color: #2196f3;"></span> 
												<span class="databox-text darkgray pull-left no-margin" style="color: <%=ecartCaissier.compareTo(BigDecimalUtil.ZERO) < 0 ? "red":"green" %> !important;">Ecart caissier</span> 
												<span class="databox-text darkgray pull-right no-margin uppercase"><b style="color: <%=ecartCaissier.compareTo(BigDecimalUtil.ZERO) < 0 ? "red":"green" %>;"><%=BigDecimalUtil.formatNumberZero(ecartCaissier)%> </b></span>
											</div>
										</div>
										<div class="databox-cell cell-6 text-center no-padding-left padding-bottom-30">
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="badge badge-empty pull-left margin-5" style="background-color: #2196f3;"></span> <span class="databox-text darkgray pull-left no-margin">Cl&ocirc;ture caissier net</span> <span
													class="databox-text darkgray pull-right no-margin uppercase" style="font-weight: bold;"><%=BigDecimalUtil.formatNumberZero(mttClotureCaissierNet)%> </span>
											</div>
											<%if(ecartManager != null){ %>
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="badge badge-empty pull-left margin-5" style="background-color: #2196f3;"></span> <span class="databox-text darkgray pull-left no-margin" style="color: <%=ecartManager.compareTo(BigDecimalUtil.ZERO) < 0 ? "red":"green" %> !important;">Ecart manager</span> 
												<span class="databox-text darkgray pull-right no-margin uppercase"><b style="color: <%=ecartManager.compareTo(BigDecimalUtil.ZERO) < 0 ? "red":"green" %>;"><%=BigDecimalUtil.formatNumberZero(ecartManager)%> </b></span>
											</div>
											<%} %>
										</div>	
									</div>
									<%} %>
									<!-- ****** Detail ****** -->
									<div class="databox-row row-12" style="margin-top: 30px;height: 100px;border-top: 2px solid red;">
										<div class="databox-cell cell-4 text-center no-padding-left padding-bottom-30">
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="databox-text sonic-silver pull-left no-margin" style="font-weight: bold;"></span> <span class="databox-text sonic-silver pull-right no-margin uppercase">SYSTEM</span>
											</div>
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="badge badge-empty pull-left margin-5" style="background-color: #e75b8d;"></span> <span class="databox-text darkgray pull-left no-margin">Especes</span> <span
													class="databox-text darkgray pull-right no-margin uppercase"> <%=BigDecimalUtil.formatNumberZero(data.getMtt_espece())%> </span>
											</div>
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="badge badge-empty pull-left margin-5" style="background-color: #a0d468;"></span> <span class="databox-text darkgray pull-left no-margin">Ch&egrave;ques</span> <span
													class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumberZero(data.getMtt_cheque())%> </span>
											</div>
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="badge badge-empty pull-left margin-5" style="background-color: #a0d468;"></span> <span class="databox-text darkgray pull-left no-margin">Ch&egrave;ques d&eacute;j.</span> <span
													class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumberZero(data.getMtt_dej())%> </span>
											</div>
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="badge badge-empty pull-left margin-5" style="background-color: #ffce55;"></span> <span class="databox-text darkgray pull-left no-margin">Carte bancaire</span> <span
													class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumberZero(data.getMtt_cb())%> </span>
											</div>
											<!-- ************* -->
											<%if(!BigDecimalUtil.isZero(data.getMtt_portefeuille())){ %>
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="badge badge-empty pull-left margin-5" style="background-color: #dbdbdb;"></span> <span class="databox-text darkgray pull-left no-margin">Portefeuille</span> <span
													class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumberZero(data.getMtt_portefeuille())%> </span>
											</div>
											<%} %>
											<%if(!BigDecimalUtil.isZero(data.getMtt_donne_point())){ %>
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="badge badge-empty pull-left margin-5" style="background-color: #dbdbdb;"></span> <span class="databox-text darkgray pull-left no-margin">Points</span> <span
													class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumberZero(data.getMtt_donne_point())%> </span>
											</div>
											<%} %>
										</div>
										<div class="databox-cell cell-3 text-center no-padding-left padding-bottom-30">	
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="databox-text sonic-silver pull-left no-margin" style="font-weight: bold;"></span> <span class="databox-text sonic-silver pull-right no-margin uppercase">Caissier</span>
											</div>
											
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="databox-text darkgray pull-left no-margin"></span> <span
													class="databox-text darkgray pull-right no-margin uppercase" style="<%=isEspeceEcart?"text-decoration: line-through;":""%>"> <%=BigDecimalUtil.formatNumber(mttClotureCaissierEspNet) %> </span>
											</div>
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="databox-text darkgray pull-left no-margin"></span> <span
													class="databox-text darkgray pull-right no-margin uppercase" style="<%=isChequeEcart?"text-decoration: line-through;":""%>"><%=BigDecimalUtil.formatNumber(mttClotureCaisserCheq)%> </span>
											</div>
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="databox-text darkgray pull-left no-margin"></span> <span
													class="databox-text darkgray pull-right no-margin uppercase" style="<%=isDejEcart?"text-decoration: line-through;":""%>"><%=BigDecimalUtil.formatNumber(mttClotureCaisserDej)%> </span>
											</div>
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="databox-text darkgray pull-left no-margin"></span> <span
													class="databox-text darkgray pull-right no-margin uppercase" style="<%=isCarteEcart?"text-decoration: line-through;":""%>"><%=BigDecimalUtil.formatNumber(mttClotureCaisserCb)%> </span>
											</div>
										</div>
										<div class="databox-cell cell-3 text-center no-padding-left padding-bottom-30">
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="databox-text sonic-silver pull-left no-margin" style="font-weight: bold;"></span> <span class="databox-text sonic-silver pull-right no-margin uppercase">Manager</span>
											</div>
											
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="databox-text darkgray pull-left no-margin"></span> <span
													class="databox-text darkgray pull-right no-margin uppercase"> <%=BigDecimalUtil.formatNumber(mttClotureManagerEspNet) %> </span>
											</div>
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="databox-text darkgray pull-left no-margin"></span> <span
													class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumber(mttClotureManagerCheq) %> </span>
											</div>
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="databox-text darkgray pull-left no-margin"></span> <span
													class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumber(mttClotureManagerDej) %> </span>
											</div>
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="databox-text darkgray pull-left no-margin"></span> <span
													class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumber(mttClotureManagerCb) %> </span>
											</div>
										</div>
										<div class="databox-cell cell-2 text-center no-padding-left padding-bottom-30">
											
										<%if("C".equals(data.getStatut_caisse())){
											BigDecimal ecartEspeses = BigDecimalUtil.substract((mttClotureManagerEspNet!=null?mttClotureManagerEspNet:mttClotureCaissierEspNet), data.getMtt_espece());
											BigDecimal ecartCb = BigDecimalUtil.substract((mttClotureManagerCb!=null?mttClotureManagerCb:mttClotureCaisserCb), data.getMtt_cb());
											BigDecimal ecartCheque = BigDecimalUtil.substract((mttClotureManagerCheq!=null?mttClotureManagerCheq:mttClotureCaisserCheq), data.getMtt_cheque());
											BigDecimal ecartDej = BigDecimalUtil.substract((mttClotureManagerDej!=null?mttClotureManagerDej:mttClotureCaisserDej), data.getMtt_dej());
											%>	
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="databox-text sonic-silver pull-left no-margin" style="font-weight: bold;"></span> <span class="databox-text sonic-silver pull-right no-margin uppercase" style="font-weight: bold;">Ecart</span>
											</div>
											
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="databox-text darkgray pull-left no-margin"></span> <span
													class="databox-text darkgray pull-right no-margin uppercase" style='color:<%=(ecartEspeses!=null && ecartEspeses.compareTo(BigDecimalUtil.get(0))<0)?"red":"green" %> !important;'> <%=BigDecimalUtil.formatNumber(ecartEspeses) %> </span>
											</div>
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="databox-text darkgray pull-left no-margin"></span> <span
													class="databox-text darkgray pull-right no-margin uppercase" style='color:<%=(ecartCheque!=null && ecartCheque.compareTo(BigDecimalUtil.get(0))<0)?"red":"green" %> !important;'><%=BigDecimalUtil.formatNumber(ecartCheque) %> </span>
											</div>
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="databox-text darkgray pull-left no-margin"></span> <span
													class="databox-text darkgray pull-right no-margin uppercase" style='color:<%=(ecartDej!=null && ecartDej.compareTo(BigDecimalUtil.get(0))<0)?"red":"green" %> !important;'><%=BigDecimalUtil.formatNumber(ecartDej) %> </span>
											</div>
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="databox-text darkgray pull-left no-margin"></span> <span
													class="databox-text darkgray pull-right no-margin uppercase" style='color:<%=(ecartCb!=null && ecartCb.compareTo(BigDecimalUtil.get(0))<0)?"red":"green" %> !important;'><%=BigDecimalUtil.formatNumber(ecartCb) %> </span>
											</div>
										<%} %>
										</div>
									</div>
								</div>
							</div>
						</div>	
					</div>
					<%}
				} %>			
							
			</div>




<h2 style="text-align: center;background-color: #ffc107;">CHIFFRES PAR EMPLOYE</h2>

<div class="row">	
							
				<%
				Long journeeId = (long)ControllerUtil.getMenuAttribute("journeeId", request);
				boolean isMargeCaissier = StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("MARGE_CAISSIER"));
				Map<String, Map<String, RepartitionBean>> mapDataEmploye = (Map)request.getAttribute("mapDataEmploye");
				Map<String, RepartitionBean> data_employe = mapDataEmploye.get("data_employe");
				Map<String, RepartitionBean> data_livreur = mapDataEmploye.get("data_livreur");
				Map<String, RepartitionBean> data_serveur = mapDataEmploye.get("data_serveur");
				%>
				<div class="col-lg-4 col-sm-4 col-xs-12" style="    padding-right: 16px;
    padding-left: 12px;">
					<b style="padding-left: 12px;">PAR CAISSIER</b>
					
					<%for(String employe : data_employe.keySet()){ 
						RepartitionBean repBean = data_employe.get(employe);
					%>
				 
					<div class="col-lg-12 col-sm-12 col-xs-12">
						<div class="databox">
                             <div class="databox-left bg-white" style="min-height: 81px;">
                                 <div class="databox-sparkline">
                                     <img src="resources/caisse/img/caisse-web/user_male_white_blue_bald.png" style="width:45px; height:45px;">
                                 </div>
                             </div>
                             <div class="databox-right bg-white bordered bordered-platinum" style="min-height: 81px;">
                                 <span class="databox-number sky"><%=employe %></span>
                                 <div class="databox-text darkgray"><%=repBean.getNbrCmd() %> commandes</div>
                                 <div class="databox-stat bg-palegreen radius-bordered">
                                     <div class="stat-text"><%=BigDecimalUtil.formatNumberZero(repBean.getMontant()) %> Dhs</div>
                                 </div>
                               <%if(isMargeCaissier){ %>
                                 <div class="databox-text darkgray" style="margin-top: 10px;">
                                 <%String param = "user="+repBean.getElementId()+"&jour="+journeeId; %>
	                                 Commission de vente 
	                                 <std:linkPopup onClick="return false" classStyle="databox-stat bg-info radius-bordered" action="caisse.journee.edit_mvm_marge" params="<%=param %>" style="margin-top: -17px;" tooltip="Afficher plus de détails">
	                                 	<%=BigDecimalUtil.formatNumberZero(repBean.getMontantMargeCaissier()) %> Dhs
	                                 	<i class="fa fa-bars"></i>
	                                 </std:linkPopup>
s                                 </div>
                                 <%}%>
                             </div>
                         </div>
	                 </div>
				<%} %>
				</div> 
				
				<div class="col-lg-4 col-sm-4 col-xs-12" style="    padding-right: 16px;
    padding-left: 12px;">
					<b style="padding-left: 12px;">PAR LIVREUR</b>
					
					<%for(String employe : data_livreur.keySet()){ 
						RepartitionBean repBean = data_livreur.get(employe);
					%>
				 
					<div class="col-lg-12 col-sm-12 col-xs-12">
						<div class="databox">
                             <div class="databox-left bg-white" style="min-height: 81px;">
                                 <div class="databox-sparkline">
                                     <i class="fa fa-motorcycle" style="color: #3F51B5;font-size: 36px;"></i>
                                 </div>
                             </div>
                             <div class="databox-right bg-white bordered bordered-platinum" style="min-height: 81px;">
                                 <span class="databox-number sky"><%=employe %></span>
                                 <div class="databox-text darkgray"><%=repBean.getNbrCmd() %> commandes</div>
                                 <div class="databox-stat bg-palegreen radius-bordered">
                                     <div class="stat-text"><%=BigDecimalUtil.formatNumberZero(repBean.getMontant()) %> Dhs</div>
                                 </div>
                             </div>
                         </div>
	                 </div>
				<%} %>
				</div> 
				
				<div class="col-lg-4 col-sm-4 col-xs-12" style="    padding-right: 16px;
    padding-left: 12px;">
					<b style="padding-left: 12px;">PAR SERVEUR</b>
					
					<%for(String employe : data_serveur.keySet()){ 
						RepartitionBean repBean = data_serveur.get(employe);
					%>
				 
					<div class="col-lg-12 col-sm-12 col-xs-12">
						<div class="databox">
                             <div class="databox-left bg-white" style="min-height: 81px;">
                                 <div class="databox-sparkline">
                                     <img src="resources/caisse/img/caisse-web/user.png" style="width:45px; height:45px;">
                                 </div>
                             </div>
                             <div class="databox-right bg-white bordered bordered-platinum" style="min-height: 81px;">
                                 <span class="databox-number sky"><%=employe %></span>
                                 <div class="databox-text darkgray"><%=repBean.getNbrCmd() %> commandes</div>
                                 <div class="databox-stat bg-palegreen radius-bordered">
                                     <div class="stat-text"><%=BigDecimalUtil.formatNumberZero(repBean.getMontant()) %> Dhs</div>
                                 </div>
                             </div>
                         </div>
	                 </div>
				<%} %>
				</div> 
			</div>	
<script>
	
<%BigDecimal mttReel = journeeVente.getMtt_total_net();
			mttReel = (mttReel == null || mttReel.compareTo(BigDecimalUtil.ZERO) == 0)
					? BigDecimalUtil.get(1)
					: mttReel;%>
	var vEsp = <%=BigDecimalUtil.formatNumber(BigDecimalUtil.divide(BigDecimalUtil.multiply(journeeVente.getMtt_espece(), BigDecimalUtil.get(100)), mttReel)).replace(',', '.')%>;
	var vChq = <%=BigDecimalUtil.formatNumber(BigDecimalUtil.divide(BigDecimalUtil.multiply(journeeVente.getMtt_cheque(), BigDecimalUtil.get(100)), mttReel)).replace(',', '.')%>;
	var vCb = <%=BigDecimalUtil.formatNumber(BigDecimalUtil.divide(BigDecimalUtil.multiply(journeeVente.getMtt_cb(), BigDecimalUtil.get(100)), mttReel)).replace(',', '.')%>;
	var vPF = <%=BigDecimalUtil.formatNumber(BigDecimalUtil.divide(BigDecimalUtil.multiply(journeeVente.getMtt_portefeuille(), BigDecimalUtil.get(100)), mttReel)).replace(',', '.')%>;
	var vDej = <%=BigDecimalUtil.formatNumber(BigDecimalUtil.divide(BigDecimalUtil.multiply(journeeVente.getMtt_dej(), BigDecimalUtil.get(100)), mttReel)).replace(',', '.')%>;
	var vPoint = <%=BigDecimalUtil.formatNumber(BigDecimalUtil.divide(BigDecimalUtil.multiply(journeeVente.getMtt_donne_point(), BigDecimalUtil.get(100)), mttReel)).replace(',', '.')%>;
	
	$(window).ready(function() {
		var chartDom = document.getElementById('pie-chart');
		var myChart = echarts.init(chartDom, null,  {width: 350, height: 350});
		var option;	
			option = {
			  title: {
			    text: 'Répartition modes de paiements',
			    //subtext: '',
			    left: 'center'
			  },
			  tooltip: {
			    trigger: 'item'
			  },
			  legend: {
			    orient: 'vertical',
			    left: 'left',
			    show: false
			  },
			  series: [
			    {
			      name: 'Modes de paiement',
			      type: 'pie',
			      radius: '50%',
			      top: '-25%',
			      data: [
			    	  <%if(!BigDecimalUtil.isZero(journeeVente.getMtt_espece())){%>
			        { value: vEsp, name: 'Especes' },
			        <%}%>
			        <%if(!BigDecimalUtil.isZero(journeeVente.getMtt_cheque())){%>
			        { value: vChq, name: 'Chèque' },
			        <%}%>
			        <%if(!BigDecimalUtil.isZero(journeeVente.getMtt_cb())){%>
			        { value: vCb, name: 'Carte' },
			        <%}%>
			        <%if(!BigDecimalUtil.isZero(journeeVente.getMtt_portefeuille())){%>
			        { value: vPF, name: 'Portefeuille' },
			        <%}%>
			        <%if(!BigDecimalUtil.isZero(journeeVente.getMtt_dej())){%>
			        { value: vDej, name: 'Chèq. dej' },
			        <%}%>
			        <%if(!BigDecimalUtil.isZero(journeeVente.getMtt_donne_point())){%>
			        { value: vPoint, name: 'Points' }
			        <%}%>
			      ],
			      emphasis: {
				        itemStyle: {
				          shadowBlur: 10,
				          shadowOffsetX: 0,
				          shadowColor: 'rgba(0, 0, 0, 0.5)',
				          
				        }
				      },
		
				      itemStyle : {
			                normal : {
			                     label : {
			                        show: true, 
			                        position: 'inner',
			                        formatter : function (params){
			                              return params.name + '\n('+ params.percent + '%'+')'
			                        },
			                    },
			                    labelLine : {
			                        show : true
			                    }
			                }},
			    }
			  ]
			};
		
			option && myChart.setOption(option);
	});
</script>
