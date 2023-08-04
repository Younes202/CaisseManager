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
    boolean isMttJrnEnNet =  StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("CHIFFRE_JRN_NET"));
%>

<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Gestion des journées</li>
		<li class="active">Fiche journée</li>
	</ul>
</div>

<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<%if(ControllerUtil.getMenuAttribute("IS_DASH_JRN", request) == null){ %>
			<std:link classStyle="btn btn-default" action="caisse.journee.work_find" params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la recherche" />
		<%} %>
      	<%if(StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("DOUBLE_CLOTURE")) && StringUtil.isTrue(""+request.getAttribute("isDoubleCloture"))/* && isJourneeOuverte*/){%> 
			<std:link actionGroup="C" classStyle="btn btn-danger" action="caisse.caisse.init_cloturer_shifts" tooltip="Double clôture des shifts activée" value="Double clôture shifts" />
      	<%} %>
	</div>
	<!--Header Buttons-->
	<jsp:include page="/WEB-INF/fragment/shortcut.jsp"></jsp:include>
	<!--Header Buttons End-->
</div>
<!-- /Page Header -->

<div class="page-body" id="journee_body">
	<div class="row">
		<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
	</div>

	<!-- widget grid -->
	<div class="widget">
		<std:form name="data-form">

			<div class="row">
				<div class="col-lg-12 col-sm-12 col-xs-12">
					<div class="widget-main ">
						<div class="tabbable">
							<%request.setAttribute("curr_tab", "jour"); %>
							<jsp:include page="journee_tab_header.jsp" />

							<div class="tab-content">
								<h3 style="text-align: center;">Journée du <fmt:formatDate value="${journee.date_journee}" /></h3>
								<hr>
								<div class="row">
									<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
										<div class="databox databox-xxlg databox-vertical databox-shadowed bg-white radius-bordered padding-5" style="height: 300px;">
											<div class="databox-top">
												<div class="databox-row row-12" style="border-bottom: 1px solid gray; height: 122%;">
													<div class="databox-cell cell-8 text-center">
														<div class="databox-number number-xxlg sonic-silver">
															<%if(isMttJrnEnNet){ %>
																<%=BigDecimalUtil.formatNumberZero(BigDecimalUtil.substract(journeeVente.getMtt_total_net(), journeeVente.getMtt_portefeuille(), journeeVente.getMtt_donne_point()))%> DH
															<%} else{ %>
																<%=BigDecimalUtil.formatNumberZero(journeeVente.getMtt_total_net())%> DH
															<%} %>
														</div>
														<div class="databox-text storm-cloud">Montant net des ventes <i style="color:#427fed;" class="fa fa-info-circle" title="Montant net hors fond de roulement et frais de livraisons <%=!isJourneeOuverte ? "retirées":"inclus" %>"></i></div>
														
														<%if(!BigDecimalUtil.isZero(journeeVente.getMtt_portefeuille()) || !BigDecimalUtil.isZero(journeeVente.getMtt_donne_point())){ %>
															<div class="databox-number number-xxlg sonic-silver" style="font-size: 16px;margin-top: -4px;color: #ff5722 !important;font-weight: bold;">
																<%if(isMttJrnEnNet){ %>
																	BRUT <i style="color:#427fed;" class="fa fa-info-circle" title="Avec paiements en portefeuille et points de fidélité"></i> : 
																	<%=BigDecimalUtil.formatNumberZero(BigDecimalUtil.add(journeeVente.getMtt_total_net(), journeeVente.getMtt_portefeuille(), journeeVente.getMtt_donne_point()))%> DH
																<%} else{ %>
																	NET <i style="color:#427fed;" class="fa fa-info-circle" title="Hors paiements en portefeuille et points de fidélité"></i> : 
																	<%=BigDecimalUtil.formatNumberZero(BigDecimalUtil.substract(journeeVente.getMtt_total_net(), journeeVente.getMtt_portefeuille(), journeeVente.getMtt_donne_point()))%> DH
																<%} %>
															</div>
														<%} %>
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
														<div class="databox-row row-12" style="height: 122%;padding-top: 47px;">
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
											<div class="databox databox-xxlg databox-vertical databox-shadowed bg-white radius-bordered padding-5" style="height: 300px;">
												<div class="databox-bottom">
													<div class="databox-row row-12">
														<div class="databox-cell cell-6 text-center  padding-5">
															<div id="pie-chart" class="chart chart"></div>
														</div>
														<div class="databox-cell cell-6 text-center no-padding-left padding-bottom-30">
															<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10">
																<span class="databox-text sonic-silver pull-left no-margin" style="font-weight: bold;">Type de paiement</span> <span
																	class="databox-text sonic-silver pull-right no-margin uppercase">DH</span>
															</div>
															<%if(!BigDecimalUtil.isZero(journeeVente.getMtt_espece())){ %>
															<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10">
																<span class="badge badge-empty pull-left margin-5" style="background-color: #2dc3e8;"></span> <span
																	class="databox-text darkgray pull-left no-margin hidden-xs">Especes</span> <span class="databox-text darkgray pull-right no-margin uppercase"> <%=BigDecimalUtil.formatNumberZero(journeeVente.getMtt_espece())%>
																</span>
															</div>
															<%} %>
															<%if(!BigDecimalUtil.isZero(journeeVente.getMtt_cheque())){ %>
															<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10">
																<span class="badge badge-empty pull-left margin-5" style="background-color: #fb6e52;"></span> <span
																	class="databox-text darkgray pull-left no-margin hidden-xs">Chèques</span> <span class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumberZero(journeeVente.getMtt_cheque())%>
																</span>
															</div>
															<%} %>
															<%if(!BigDecimalUtil.isZero(journeeVente.getMtt_cb())){ %>
															<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10">
																<span class="badge badge-empty pull-left margin-5" style="background-color: #ffce55;"></span> <span
																	class="databox-text darkgray pull-left no-margin hidden-xs">Carte bancaire</span> <span class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumberZero(journeeVente.getMtt_cb())%>
																</span>
															</div>
															<%} %>
															<%if(!BigDecimalUtil.isZero(journeeVente.getMtt_dej())){ %>
															<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10">
																<span class="badge badge-empty pull-left margin-5" style="background-color: #e75b8d;"></span> <span
																	class="databox-text darkgray pull-left no-margin hidden-xs">Chèques déj.</span> <span class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumberZero(journeeVente.getMtt_dej())%>
																</span>
															</div>
															<%} %>
														<%if(!BigDecimalUtil.isZero(journeeVente.getMtt_donne_point()) && isPoints){ %>	
															<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10">
																<span class="badge badge-empty pull-left margin-5" style="background-color: green;"></span> <span
																	class="databox-text darkgray pull-left no-margin hidden-xs">Points fidélité</span> <span class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumberZero(journeeVente.getMtt_donne_point())%>
																</span>
															</div>
														<%} %>
														<%if(!BigDecimalUtil.isZero(journeeVente.getMtt_portefeuille()) && isPortefeuille){ %>	
															<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10">
																<span class="badge badge-empty pull-left margin-5" style="background-color: #a0d468;"></span> <span
																	class="databox-text darkgray pull-left no-margin hidden-xs">Portefeuille virtuel</span> <span class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumberZero(journeeVente.getMtt_portefeuille())%>
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
																	class="databox-text darkgray pull-left no-margin hidden-xs">Fonds de roulement</span> 
																	<span class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumberZero(journeeVente.getMtt_ouverture())%>
																</span>
															</div>
															<%} %>
															<%if(!BigDecimalUtil.isZero(journeeVente.getMtt_annule())){ %>
															<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10">
																<span class="badge badge-empty pull-left margin-5" style="background-color: #2196f3;"></span> <span
																	class="databox-text darkgray pull-left no-margin hidden-xs">Annulées CMD</span> 
																	<span style="color: red !important;" class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumberZero(journeeVente.getMtt_annule())%>
																</span>
															</div>
															<%} %>
															<%if(!BigDecimalUtil.isZero(journeeVente.getMtt_annule_ligne())){ %>
															<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10">
																<span class="badge badge-empty pull-left margin-5" style="background-color: #2196f3;"></span> <span
																	class="databox-text darkgray pull-left no-margin hidden-xs">Annulées Ligne</span> 
																	<span style="color: red !important;" class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumberZero(journeeVente.getMtt_annule_ligne())%>
																</span>
															</div>
															<%} %>
															<%if(!BigDecimalUtil.isZero(journeeVente.getMtt_reduction())){ %>
															<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10">
																<span class="badge badge-empty pull-left margin-5" style="background-color: #2196f3;"></span> <span
																	class="databox-text darkgray pull-left no-margin hidden-xs">Réductions Cmd</span> <span class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumberZero(journeeVente.getMtt_reduction())%>
																</span>
															</div>
															<%} %>
															<%if(!BigDecimalUtil.isZero(journeeVente.getMtt_art_reduction())){ %>
															<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10">
																<span class="badge badge-empty pull-left margin-5" style="background-color: #2196f3;"></span> <span
																	class="databox-text darkgray pull-left no-margin hidden-xs">RéductionsArt</span> <span class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumberZero(journeeVente.getMtt_art_reduction())%>
																</span>
															</div>
															<%} %>
															<%if(!BigDecimalUtil.isZero(journeeVente.getMtt_art_offert())){ %>
															<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10">
																<span class="badge badge-empty pull-left margin-5" style="background-color: #2196f3;"></span> <span
																	class="databox-text darkgray pull-left no-margin hidden-xs">Art. offerts</span> <span class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumberZero(journeeVente.getMtt_art_offert())%>
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
																	class="databox-text darkgray pull-left no-margin hidden-xs">Livraisons <i style="color:blue;" class="fa fa-info-circle" title="<%=val%>"></i></span> 
																	<span class="databox-text darkgray pull-right no-margin uppercase">
																		<%=BigDecimalUtil.formatNumber(BigDecimalUtil.multiply(fraisLivraison, BigDecimalUtil.get((journeeVente.getNbr_livraison()==null?0:journeeVente.getNbr_livraison())))) %>
																	</span>
															</div>
															<%} %>
															
															<!-- Recharge -->
															<c:if test="${mttRecharge > 0 }">
																<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10">
																	<span class="badge badge-empty pull-left margin-5" style="background-color: black;"></span> <span
																		class="databox-text darkgray pull-left no-margin hidden-xs">Recharges portefeuille </span> 
																		<span class="databox-text darkgray pull-right no-margin uppercase">
																			 <fmt:formatDecimal value="${mttRecharge }" />
																		</span>
																</div>
															</c:if>
															
															<%if(!BigDecimalUtil.isZero(journeeVente.getMtt_total_achat())){ %>
															<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10">
																<span class="badge badge-empty pull-left margin-5" style="background-color: #2196f3;"></span> <span class="databox-text darkgray pull-left no-margin hidden-xs">
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
											<div class="databox databox-xxlg databox-vertical databox-shadowed bg-white radius-bordered padding-5" style="height: 350px;">
												<div class="databox-bottom">
													<div class="databox-row row-12">
														<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10">
																<span class="databox-text sonic-silver pull-left no-margin" style="font-weight: bold;">Clôture et écarts</span> <span
																	class="databox-text sonic-silver pull-right no-margin uppercase">DH</span>
															</div>
															
															<%if(journeeVente.getMtt_cloture_caissier() != null){ %>
															<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10"> 
																<span class="badge badge-empty pull-left margin-5" style="background-color: #2196f3;"></span> <span
																	class="databox-text darkgray pull-left no-margin hidden-xs">Montant net de clôture</span> <span class="databox-text darkgray pull-right no-margin uppercase">
																	<%=BigDecimalUtil.formatNumberZero(BigDecimalUtil.substract(journeeVente.getMtt_cloture_caissier(), journeeVente.getMtt_ouverture()))%>
																</span>
															</div>
															
															<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 114px;color: blue;">
																<span class="badge badge-empty pull-left margin-5" style="background-color: #2196f3;"></span> <span
																	class="databox-text darkgray pull-left no-margin hidden-xs">Clôture caissier </span>
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
																	class="databox-text darkgray pull-left no-margin hidden-xs" style="color: <%=BigDecimalUtil.getStyle(ecartCaissier) %> !important;">Ecart des montants</span> 
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
								</div>
						</div>
					</div>
				</div>
			</div>
		
		</std:form>
		
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
	var vTotal = vEsp+vChq+vCb+vPF+vDej+vPoint;

	if(vTotal){
		var chartDom = document.getElementById('pie-chart');
		var myChart = echarts.init(chartDom, null,  {width: 400, height: 400});
		var option;	
	option = {
	  title: {
		  text: 'Modes de paiements',
	    subtext: '',
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
	      top: '-30%',
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
		          shadowColor: 'rgba(0, 0, 0, 0.5)'
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
	}
</script>
