
<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="appli.model.domaine.administration.persistant.EtatFinancePaiementPersistant"%>
<%@page import="appli.model.domaine.administration.persistant.EtatFinanceDetailPersistant"%>
<%@page import="appli.controller.domaine.administration.bean.EtatFinanceBean"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.model.common.util.BooleanUtil"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.List"%>
<%@page import="framework.model.common.util.DateUtil"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@page errorPage="/commun/error.jsp"%>

<%
EtatFinanceBean etatFinanceBean = (EtatFinanceBean) request.getAttribute("etatFinanceBean");
String devise = "&nbsp;"+StrimUtil.getGlobalConfigPropertie("devise.symbole");
%>

<script type="text/javascript">
	$(document).ready(function (){
		$('.input-group.date, #dateDebut').datepicker({
	    	clearBtn: <%=etatFinanceBean.getId()==null?"true":"false"%>,
		    language: "fr",
		    autoclose: true,
		    format: "mm/yyyy",
		    startView: 1,
		    startDate : '<%=request.getAttribute("minDate")%>m',
		    endDate : '<%=request.getAttribute("maxDate")%>m',
		    minViewMode: 1
	    });
		$('.input-group.date').datepicker().on("changeDate", function(e) {
	        var currDate = $('#dateDebut').datepicker('getFormattedDate');
	        submitAjaxForm('<%=EncryptionUtil.encrypt("caisse.etatFinance.loadFinanceDetail")%>', 'dateDebut='+currDate, $("#search-form"), $(this));
	    });
		
		<%if(!StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("CAISSE_PRINT")) && request.getAttribute("CURR_PRINT_ID") != null){%>
			callExternalUrl("http://localhost:8001/cm-client?act=print&elmnt=<%=request.getAttribute("CURR_PRINT_ID")%>&tp=<%=request.getAttribute("typePrint")%>");
		<%} %>
	});
</script>

<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li class="active">Finance</li>
		<li class="active">> Bilan mensuel</li>
	</ul>
</div>
	<std:form name="search-form">
<!-- /Page Breadcrumb -->
<!-- Page Header -->
<div class="page-header position-relative"> 
		<!--Header Buttons-->
	<div class="header-title" style="padding-top: 4px;float: left;margin-right: 20%;">
		<std:link classStyle="btn btn-default" action="caisse.etatFinance.work_find" params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la recherche" />
		
		<%if(etatFinanceBean.getId() == null){ %>
			<std:link actionGroup="X" classStyle="btn btn-warning shiny" action="caisse.etatFinance.clore_mois" icon="fa-3x fa-lock" value="Clore le mois" tooltip="Clore le mois" />
		<%} else if(!BooleanUtil.isTrue(etatFinanceBean.getIs_purge())){ %>
			<std:link actionGroup="X" classStyle="btn btn-warning shiny" action="caisse.etatFinance.annuler_clore_mois" workId="<%=etatFinanceBean.getId().toString() %>" icon="fa-3x fa-lock" value="Annuler la cl&ocirc;ture" tooltip="Annuler la cl&ocirc;ture" />
		<%} %>
<!-- 		<div class="btn-group"> -->
<%--              <std:link actionGroup="X" classStyle="btn btn-sky shiny" action="caisse.razPrint.imprimer_raz" icon="fa-3x fa-print" value="Imprimer RAZ mensuelle" params="isFromEtat=1&mode=MO" tooltip="Imprimer RAZ mensuelle" /> --%>
<!--              <a class="btn btn-sky dropdown-toggle shiny" data-toggle="dropdown" href="javascript:void(0);" aria-expanded="true"><i class="fa fa-angle-down"></i></a> -->
<!--              <ul class="dropdown-menu dropdown-primary"> -->
<!--                  <li> -->
<%--                      <std:link actionGroup="X" style="text-align: left;" classStyle="btn btn-sky shiny" action="caisse.razPrint.imprimer_raz" params="isFromEtat=1&mode=JRN" icon="fa-3x fa-print" value="Imprimer RAZ jours" tooltip="Imprimer RAZ jours" /> --%>
<!--                  </li> -->
<!--                  <li> -->
<%--                      <std:link actionGroup="X" style="margin-top: 6px;" classStyle="btn btn-sky shiny" action="caisse.razPrint.imprimer_raz" params="isFromEtat=1&mode=BS" icon="fa-3x fa-print" value="Imprimer RAZ boissons" tooltip="Imprimer la RAZ des boissons" /> --%>
<!--                  </li> -->
<!--              </ul> -->
<!--          </div> -->
	</div>
	<span style="font-size: 22px;float: left;">Bilan du mois de </span>
  	<div class="input-group date" style="width: 156px;">
  		<input type="text" class="form-control" name="dateDebut" id="dateDebut" style="font-size: 22px;color:green !important;font-weight: bold;border: 0px;" value="<%=StringUtil.getValueOrEmpty(request.getAttribute("dateDebut"))%>">
  			<span class="input-group-addon" style="border: 1px solid #f3f3f3;padding-top: 9px;">
  				<i class="fa fa-calendar" style="font-size: 22px;color: #9C27B0;"></i>
  			</span>
	</div>

	<jsp:include page="/WEB-INF/fragment/shortcut.jsp"></jsp:include>
	<!--Header Buttons End-->
</div>
<!-- /Page Header -->
<!-- Page Body -->
<div class="page-body">
	<%if(BooleanUtil.isTrue(etatFinanceBean.getIs_purge())){ %>
	<div class="row">
		<div class="alert alert-warning fade in">
	        <button class="close" data-dismiss="alert">
	           x
	        </button>
	        <i class="fa-fw fa fa-warning"></i>
	        Une purge a &eacute;t&eacute; r&eacute;alis&eacute;e sur ce mois.
    	</div>
    </div>	
   <%} %> 
	<div class="row">
		<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
	</div>
			
	<%
	BigDecimal soldeStock = null, valeurStockActuel = null, valeurStockPrecedent = null,
			soldeBanque = null, soldeBanqueActuel = null, soldeBanquePrecedent= null;
	for(EtatFinanceDetailPersistant det : etatFinanceBean.getList_detail()){
		if(det.getType().equals("EMPL")){
			soldeStock = BigDecimalUtil.add(soldeStock, BigDecimalUtil.substract(det.getMtt_etat_actuel(), det.getMtt_etat_prev()));
			valeurStockActuel = BigDecimalUtil.add(valeurStockActuel, det.getMtt_etat_actuel());
			valeurStockPrecedent = BigDecimalUtil.add(valeurStockPrecedent, det.getMtt_etat_prev());
		}
		if(det.getType().equals("BANQ")){
			soldeBanque = BigDecimalUtil.add(soldeBanque, BigDecimalUtil.substract(det.getMtt_etat_actuel(), det.getMtt_etat_prev()));
			soldeBanqueActuel = BigDecimalUtil.add(soldeBanqueActuel, det.getMtt_etat_actuel());
			soldeBanquePrecedent = BigDecimalUtil.add(soldeBanquePrecedent, det.getMtt_etat_prev());
		}
	}
	
	BigDecimal soldeChequeRecette = BigDecimalUtil.add(
				BigDecimalUtil.substract(etatFinanceBean.getMtt_recette_cheque_encais(), etatFinanceBean.getMtt_recette_cheque_non_encais()),
				BigDecimalUtil.substract(etatFinanceBean.getMtt_vente_cheque_encais(), etatFinanceBean.getMtt_vente_cheque_non_encais()),
				BigDecimalUtil.substract(etatFinanceBean.getMtt_avoir_cheque_encais(), etatFinanceBean.getMtt_avoir_cheque_non_encais())
			);
	BigDecimal soldeChequeDepense = BigDecimalUtil.add(
				BigDecimalUtil.substract(etatFinanceBean.getMtt_depense_cheque_encais(), etatFinanceBean.getMtt_depense_cheque_non_encais()),
				BigDecimalUtil.substract(etatFinanceBean.getMtt_achat_cheque_encais(), etatFinanceBean.getMtt_achat_cheque_non_encais()),
				BigDecimalUtil.substract(etatFinanceBean.getMtt_salaire_cheque_encais(), etatFinanceBean.getMtt_salaire_cheque_non_encais())
			);
	
	BigDecimal soldeCheque = BigDecimalUtil.substract(soldeChequeRecette, soldeChequeDepense);
	
	BigDecimal totalRecette = BigDecimalUtil.add(
					etatFinanceBean.getMtt_vente_caisse_cloture(), 
					etatFinanceBean.getMtt_vente_hors_caisse(), 
					etatFinanceBean.getMtt_recette_divers(), 
					etatFinanceBean.getMtt_avoir()
				);
	
	BigDecimal totalDepense = BigDecimalUtil.add(
					etatFinanceBean.getMtt_achat(), 
					etatFinanceBean.getMtt_salaire(), 
					etatFinanceBean.getMtt_depense_divers(),
					etatFinanceBean.getMttLivraisonPartLivreur()
				); //, soldeChequeDepense
	%>
	
		<!-- ******************************* the Flash Info chart row ******************************* -->
		<div class="row">
			<div class="alert alert-warning fade in">
	          <button class="close" data-dismiss="alert">
	              �
	          </button>
	          <i class="fa-fw fa fa-warning"></i>
	          Module<strong> exp&eacute;rimental</strong>.
	      </div>
      </div>                                  
	<div class="row">
		<div class="col-lg-12 col-sm-12 col-xs-12">
			<div class="col-lg-3 col-sm-3 col-xs-3">
				<div class="well bordered-left bordered-themeprimary" style="text-align: center;">
					<span style="font-size: 20px;">SOLDE RECETTE/DEPENSE</span> 
					<span style="font-size: 37px;font-weight: bold;">
						<%=BigDecimalUtil.formatNumberZero(BigDecimalUtil.substract(totalRecette, totalDepense)) %><%=devise%>
					</span>
				</div>
			</div>
			<div class="col-lg-3 col-sm-3 col-xs-3">
				<div class="well bordered-left bordered-themeprimary" style="text-align: center;">
					<span style="font-size: 20px;">ETAT CHEQUES</span> 
					<span style="font-size: 37px;font-weight: bold;text-align: right;color:<%=(soldeCheque!=null && soldeCheque.intValue()<0)? "red":"green"%>;">
						<%=BigDecimalUtil.formatNumberZero(soldeCheque) %><%=devise%>
					</span>
				</div>
			</div>
			<div class="col-lg-3 col-sm-3 col-xs-3">
				<div class="well bordered-left bordered-themeprimary" style="text-align: center;">
					<span style="font-size: 20px;">SOLDE BANQUE</span> 
					<span style="font-size: 37px;font-weight: bold;text-align: right;color:<%=(soldeBanque!=null && soldeBanque.intValue()<0)? "red":"green"%>;">
						<%=BigDecimalUtil.formatNumberZero(soldeBanqueActuel) %><%=devise%>
					</span>
					<br>
					<span>
					Ancien solde : <%=BigDecimalUtil.formatNumberZero(soldeBanquePrecedent) %>
					</span>
					<br>
					<span style="color:<%=(soldeBanque!=null && soldeBanque.intValue()<0)? "red":"green"%>;">
					Diff&eacute;rence : <%=BigDecimalUtil.formatNumberZero(soldeBanque) %><%=devise%>
					</span>
				</div>
			</div>
			<div class="col-lg-3 col-sm-3 col-xs-3">
				<div class="well bordered-left bordered-themeprimary" style="text-align: center;">
					<span style="font-size: 20px;">VALEUR STOCK</span> 
					<span style="font-size: 37px;font-weight: bold;">
						<%=BigDecimalUtil.formatNumberZero(valeurStockActuel) %>
					</span>
					<br>
					<span>
					Ancienne valeur : <%=BigDecimalUtil.formatNumberZero(valeurStockPrecedent) %>
					</span>
					<br>
					<span style="color:<%=(soldeStock!=null && soldeStock.intValue()<0)? "red":"green"%>;">
					Diff&eacute;rence : <%=BigDecimalUtil.formatNumberZero(soldeStock) %><%=devise%>
					</span>
					</div>
				</div>
			</div>	
		</div>
	</div>
			
	<%
		BigDecimal espece_R = BigDecimal.ZERO, cheque_R = BigDecimal.ZERO, chequeF_R = BigDecimal.ZERO, 
				virement_R = BigDecimal.ZERO, carte_R = BigDecimal.ZERO, dej_R = BigDecimal.ZERO;
		for(EtatFinancePaiementPersistant det : etatFinanceBean.getList_paiement()){ 
			if(det.getType().equals("a") || det.getType().equals("d") || det.getType().equals("s")){
				continue;
			}
			espece_R = BigDecimalUtil.add(espece_R, det.getMtt_espece());
			cheque_R = BigDecimalUtil.add(cheque_R, det.getMtt_cheque());
			chequeF_R = BigDecimalUtil.add(chequeF_R, det.getMtt_cheque_f());
			carte_R = BigDecimalUtil.add(carte_R, det.getMtt_carte());
			virement_R = BigDecimalUtil.add(virement_R, det.getMtt_virement());
			dej_R = BigDecimalUtil.add(dej_R, det.getMtt_dej());
		}

		BigDecimal espece_D = BigDecimal.ZERO, cheque_D = BigDecimal.ZERO, chequeF_D = BigDecimal.ZERO, 
				virement_D = BigDecimal.ZERO, carte_D = BigDecimal.ZERO, dej_D = BigDecimal.ZERO;
		for(EtatFinancePaiementPersistant det : etatFinanceBean.getList_paiement()){ 
			if(det.getType().equals("v") || det.getType().equals("vc") || det.getType().equals("r") || det.getType().equals("av")){
				continue;
			}
			espece_D = BigDecimalUtil.add(espece_D, det.getMtt_espece());
			cheque_D = BigDecimalUtil.add(cheque_D, det.getMtt_cheque());
			chequeF_D = BigDecimalUtil.add(chequeF_D, det.getMtt_cheque_f());
			carte_D = BigDecimalUtil.add(carte_D, det.getMtt_carte());
			virement_D = BigDecimalUtil.add(virement_D, det.getMtt_virement());
			dej_D = BigDecimalUtil.add(dej_D, det.getMtt_dej());
		}
		
		BigDecimal etatDepences = BigDecimalUtil.add(espece_D, cheque_D, chequeF_D, carte_D, virement_D, dej_D);
		BigDecimal etatRecette = BigDecimalUtil.add(espece_R, cheque_R, chequeF_R, carte_R, virement_R, dej_R);
	%>
	
<h5 class="row-title"><i class="typcn typcn-th-small"></i>RECETTES ET DEPENSES</h5>	
	<div class="row" style="margin-left: 0px;margin-right: -7px;">	
		<!-- ************************************************************************************************* -->
		<div class="col-xs-12 col-md-6">
              <div class="well with-header  with-footer">
                  <div class="header bg-blue">
                      RECETTES
                  </div>
                  
                  <h4 style="color: blue;">Synth&egrave;se</h4>
                  <table class="table table-hover">
                      <thead class="bordered-darkorange">
                          <tr>
                              <th>
                                  Source
                              </th>
                              <th style="text-align: right;">
                                  Montant
                              </th>
                          </tr>
                      </thead>
                      <tbody>
                          <tr>
                              <td>
                                  VENTES CAISSES
                              </td>
                              <td style="text-align: right;">
                                  <%=BigDecimalUtil.formatNumberZero(etatFinanceBean.getMtt_vente_caisse_cloture()) %><%=devise%>
                                  <br>
                                  <span style="font-size: 11px;"> 
									<%
									BigDecimal ecart = BigDecimalUtil.substract(etatFinanceBean.getMtt_vente_caisse(), etatFinanceBean.getMtt_vente_caisse_cloture());
									%>
									[ Calcul&eacute; caisse : <b style="color: blue;"><%=BigDecimalUtil.formatNumberZero(etatFinanceBean.getMtt_vente_caisse()) %><%=devise%></b>
									 | Ecart : <b style='color: <%=ecart.intValue()<0? "red":"orange"%>;'><%=BigDecimalUtil.formatNumberZero(ecart) %></b><%=devise%> 
									 | Livraison : <b><%=BigDecimalUtil.formatNumberZero(etatFinanceBean.getMtt_livraison_part()) %></b><%=devise%>
									]
								</span>
                              </td>
                          </tr>
                           <%
                           for(EtatFinancePaiementPersistant det : etatFinanceBean.getList_paiement()){ 
									if(!det.getType().equals("vc")){
										continue;
									}
								%>
									<%if(!BigDecimalUtil.isZero(det.getMtt_espece())){ %>
									<tr>
										<td style="padding-left: 50px;color:blue;">Esp&egrave;ce</td>
										<td style="text-align: right;color:blue;"><%=BigDecimalUtil.formatNumberZero(det.getMtt_espece()) %></td>
									</tr>
									<%} %>
									<%if(!BigDecimalUtil.isZero(det.getMtt_cheque())){ %>
									<tr>
										<td style="padding-left: 50px;color:blue;">Ch&egrave;que</td>
										<td style="text-align: right;color:blue;"><%=BigDecimalUtil.formatNumberZero(det.getMtt_cheque()) %></td>
									</tr>
									<%} %>
									<%if(!BigDecimalUtil.isZero(det.getMtt_dej())){ %>
									<tr>
										<td style="padding-left: 50px;color:blue;">Ch&egrave;que d&eacute;j.</td>
										<td style="text-align: right;color:blue;"><%=BigDecimalUtil.formatNumberZero(det.getMtt_dej()) %></td>
									</tr>
									<%} %>
									<%if(!BigDecimalUtil.isZero(det.getMtt_carte())){ %>
									<tr>
										<td style="padding-left: 50px;color:blue;">Carte</td>
										<td style="text-align: right;color:blue;"><%=BigDecimalUtil.formatNumberZero(det.getMtt_carte()) %></td>
									</tr>
									<%} %>
								<%} %>
								
									<tr>	
										<td>VENTES HORS CAISSES</td>
										<td style="text-align: right;"><%=BigDecimalUtil.formatNumberZero(etatFinanceBean.getMtt_vente_hors_caisse()) %><%=devise%></td>
									</tr>
								<%for(EtatFinancePaiementPersistant det : etatFinanceBean.getList_paiement()){ 
									if(!det.getType().equals("v")){
										continue;
									}
								%>
									<%if(!BigDecimalUtil.isZero(det.getMtt_espece())){ %>
									<tr>
										<td style="padding-left: 50px;color:blue;">Esp&egrave;ce</td>
										<td style="text-align: right;color:blue;"><%=BigDecimalUtil.formatNumberZero(det.getMtt_espece()) %></td>
									</tr>
									<%} %>
									<%if(!BigDecimalUtil.isZero(det.getMtt_cheque())){ %>
									<tr>
										<td style="padding-left: 50px;color:blue;">Ch&egrave;que</td>
										<td style="text-align: right;color:blue;"><%=BigDecimalUtil.formatNumberZero(det.getMtt_cheque()) %></td>
									</tr>
									<%} %>
									<%if(!BigDecimalUtil.isZero(det.getMtt_cheque_f())){ %>
									<tr>
										<td style="padding-left: 50px;color:blue;">Ch&egrave;que fournisseur</td>
										<td style="text-align: right;color:blue;"><%=BigDecimalUtil.formatNumberZero(det.getMtt_cheque_f()) %></td>
									</tr>
									<%} %>
									<%if(!BigDecimalUtil.isZero(det.getMtt_carte())){ %>
									<tr>
										<td style="padding-left: 50px;color:blue;">Carte</td>
										<td style="text-align: right;color:blue;"><%=BigDecimalUtil.formatNumberZero(det.getMtt_carte()) %></td>
									</tr>
									<%} %>
									<%if(!BigDecimalUtil.isZero(det.getMtt_virement())){ %>
									<tr>
										<td style="padding-left: 50px;color:blue;">Virement</td>
										<td style="text-align: right;color:blue;"><%=BigDecimalUtil.formatNumberZero(det.getMtt_virement()) %></td>
									</tr>
									<%} %>
								<%} %>
									<tr>
										<td>RECETTES DIVERS</td>
										<td style="text-align: right;"><%=BigDecimalUtil.formatNumberZero(etatFinanceBean.getMtt_recette_divers()) %><%=devise%></td>
									</tr>	
							<%for(EtatFinancePaiementPersistant det : etatFinanceBean.getList_paiement()){ 
									if(!det.getType().equals("r")){
										continue;
									}
								%>
									<%if(!BigDecimalUtil.isZero(det.getMtt_espece())){ %>
									<tr>
										<td style="padding-left: 50px;color:blue;">Esp&egrave;ce</td>
										<td style="text-align: right;color:blue;"><%=BigDecimalUtil.formatNumberZero(det.getMtt_espece()) %></td>
									</tr>
									<%} %>
									<%if(!BigDecimalUtil.isZero(det.getMtt_cheque())){ %>
									<tr>
										<td style="padding-left: 50px;color:blue;">Ch&egrave;que</td>
										<td style="text-align: right;color:blue;"><%=BigDecimalUtil.formatNumberZero(det.getMtt_cheque()) %></td>
									</tr>
									<%} %>
									<%if(!BigDecimalUtil.isZero(det.getMtt_cheque_f())){ %>
									<tr>
										<td style="padding-left: 50px;color:blue;">Ch&egrave;que fournisseur</td>
										<td style="text-align: right;color:blue;"><%=BigDecimalUtil.formatNumberZero(det.getMtt_cheque_f()) %></td>
									</tr>
									<%} %>
									<%if(!BigDecimalUtil.isZero(det.getMtt_carte())){ %>
									<tr>
										<td style="padding-left: 50px;color:blue;">Carte</td>
										<td style="text-align: right;color:blue;"><%=BigDecimalUtil.formatNumberZero(det.getMtt_carte()) %></td>
									</tr>
									<%} %>
									<%if(!BigDecimalUtil.isZero(det.getMtt_virement())){ %>
									<tr>
										<td style="padding-left: 50px;color:blue;">Virement</td>
										<td style="text-align: right;color:blue;"><%=BigDecimalUtil.formatNumberZero(det.getMtt_virement()) %></td>
									</tr>
									<%} %>	
								<%} %>
								<tr>
									<td>AVOIRS</td>
									<td style="text-align: right;"><%=BigDecimalUtil.formatNumberZero(etatFinanceBean.getMtt_avoir()) %><%=devise%></td>
								</tr>	
							<%for(EtatFinancePaiementPersistant det : etatFinanceBean.getList_paiement()){ 
									if(!det.getType().equals("av")){
										continue;
									}
								%>
									<%if(!BigDecimalUtil.isZero(det.getMtt_espece())){ %>
									<tr>
										<td style="padding-left: 50px;color:blue;">Esp&egrave;ce</td>
										<td style="text-align: right;color:blue;"><%=BigDecimalUtil.formatNumberZero(det.getMtt_espece()) %></td>
									</tr>
									<%} %>
									<%if(!BigDecimalUtil.isZero(det.getMtt_cheque())){ %>
									<tr>
										<td style="padding-left: 50px;color:blue;">Ch&egrave;que</td>
										<td style="text-align: right;color:blue;"><%=BigDecimalUtil.formatNumberZero(det.getMtt_cheque()) %></td>
									</tr>
									<%} %>
									<%if(!BigDecimalUtil.isZero(det.getMtt_cheque_f())){ %>
									<tr>
										<td style="padding-left: 50px;color:blue;">Ch&egrave;que fournisseur</td>
										<td style="text-align: right;color:blue;"><%=BigDecimalUtil.formatNumberZero(det.getMtt_cheque_f()) %></td>
									</tr>
									<%} %>
									<%if(!BigDecimalUtil.isZero(det.getMtt_carte())){ %>
									<tr>
										<td style="padding-left: 50px;color:blue;">Carte</td>
										<td style="text-align: right;color:blue;"><%=BigDecimalUtil.formatNumberZero(det.getMtt_carte()) %></td>
									</tr>
									<%} %>
									<%if(!BigDecimalUtil.isZero(det.getMtt_virement())){ %>
									<tr>
										<td style="padding-left: 50px;color:blue;">Virement</td>
										<td style="text-align: right;color:blue;"><%=BigDecimalUtil.formatNumberZero(det.getMtt_virement()) %></td>
									</tr>
									<%} %>	
								<%} %>
								<tr>
									<td style="text-align: right;font-weight: bold;">TOTAL RECETTES</td>
									<td style="text-align: right;font-weight: bold;"><%=BigDecimalUtil.formatNumberZero(totalRecette) %><%=devise%></td>
								</tr>
                                    </tbody>
                                </table>
                                
                  <br>
                  <h4 style="color: blue;">REPARTITION PAR MODE DE PAIEMENT</h4>
                  <table class="table table-hover">
                      <thead class="bordered-darkorange">
                          <tr>
                              <th>
                                  Mod de paiement
                              </th>
                              <th style="text-align: right;">
                                  Montant
                              </th>
                          </tr>
                      </thead>
                      <tbody>
                      		<%if(!BigDecimalUtil.isZero(espece_R)){ %>
                          <tr>
                              <td>
                                  Esp&egrave;ce
                              </td>
                              <td style="text-align: right;">
                                  <%=BigDecimalUtil.formatNumberZero(espece_R) %><%=devise%>
                              </td>
                          </tr>
                          <%} %>
                          <%if(!BigDecimalUtil.isZero(cheque_R)){ %>
						<tr>
                              <td>
                                  Ch&egrave;que
                              </td>
                              <td style="text-align: right;">
                                  <%=BigDecimalUtil.formatNumberZero(cheque_R) %><%=devise%>
                              </td>
                          </tr>
                          <%} %>
                          <%if(!BigDecimalUtil.isZero(chequeF_R)){ %>
                          <tr>
                              <td>
                                 Ch&egrave;que fournisseur
                              </td>
                              <td style="text-align: right;">
                                  <%=BigDecimalUtil.formatNumberZero(chequeF_R) %><%=devise%>
                              </td>
                          </tr>
                          <%} %>
                          <%if(!BigDecimalUtil.isZero(carte_R)){ %>
		 				  <tr>
                              <td>
                                Carte
                              </td>
                              <td style="text-align: right;">
                                  <%=BigDecimalUtil.formatNumberZero(carte_R) %><%=devise%>
                              </td>
                          </tr>
                          <%} %>
                          <%if(!BigDecimalUtil.isZero(dej_R)){ %>
						  <tr>
                              <td>
                                Ch&egrave;que d&eacute;j.
                              </td>
                              <td style="text-align: right;">
                                  <%=BigDecimalUtil.formatNumberZero(dej_R) %><%=devise%>
                              </td>
                          </tr>
                          <%} %>
                         </tbody>
                       </table>   
                     </div>
				</div>
				<div class="col-xs-12 col-md-6">
              <div class="well with-header  with-footer">
                  <div class="header bg-blue">
                      DEPENSES
                  </div>
                  
                  <h4 style="color: blue;">Synth&egrave;se</h4>
                  <table class="table table-hover">
                      <thead class="bordered-darkorange">
                          <tr>
                              <th>
                                  Source
                              </th>
                              <th style="text-align: right;">
                                  Montant
                              </th>
                          </tr>
                      </thead>
                      <tbody>
                          <tr>
                              <td>
                                  ACHAT MARCHANDISES
                              </td>
                              <td style="text-align: right;">
                                  <%=BigDecimalUtil.formatNumberZero(etatFinanceBean.getMtt_achat()) %><%=devise%>
                              </td>
                          </tr>
				
							<%for(EtatFinancePaiementPersistant det : etatFinanceBean.getList_paiement()){ 
									if(!det.getType().equals("a")){
										continue;
									}
								%>
									<%if(!BigDecimalUtil.isZero(det.getMtt_espece())){ %>
									<tr>
										<td style="padding-left: 50px;color:blue;">Esp&egrave;ce</td>
										<td style="text-align: right;color:blue;"><%=BigDecimalUtil.formatNumberZero(det.getMtt_espece()) %></td>
									</tr>
									<%} %>
									<%if(!BigDecimalUtil.isZero(det.getMtt_cheque())){ %>
									<tr>
										<td style="padding-left: 50px;color:blue;">Ch&egrave;que</td>
										<td style="text-align: right;color:blue;"><%=BigDecimalUtil.formatNumberZero(det.getMtt_cheque()) %></td>
									</tr>
									<%} %>
									<%if(!BigDecimalUtil.isZero(det.getMtt_cheque_f())){ %>
									<tr>
										<td style="padding-left: 50px;color:blue;">Ch&egrave;que fournisseur</td>
										<td style="text-align: right;color:blue;"><%=BigDecimalUtil.formatNumberZero(det.getMtt_cheque_f()) %></td>
									</tr>
									<%} %>
									<%if(!BigDecimalUtil.isZero(det.getMtt_carte())){ %>
									<tr>
										<td style="padding-left: 50px;color:blue;">Carte</td>
										<td style="text-align: right;color:blue;"><%=BigDecimalUtil.formatNumberZero(det.getMtt_carte()) %></td>
									</tr>
									<%} %>
									<%if(!BigDecimalUtil.isZero(det.getMtt_virement())){ %>
									<tr>
										<td style="padding-left: 50px;color:blue;">Virement</td>
										<td style="text-align: right;color:blue;"><%=BigDecimalUtil.formatNumberZero(det.getMtt_virement()) %></td>
									</tr>
									<%} %>	
								<%} %>
						<tr>
							<td>SALAIRES</td>
							<td style="text-align: right;"><%=BigDecimalUtil.formatNumberZero(etatFinanceBean.getMtt_salaire()) %><%=devise%></td>
						</tr>
							<%for(EtatFinancePaiementPersistant det : etatFinanceBean.getList_paiement()){ 
									if(!det.getType().equals("s")){
										continue;
									}
								%>
									<%if(!BigDecimalUtil.isZero(det.getMtt_espece())){ %>
									<tr>
										<td style="padding-left: 50px;color:blue;">Esp&egrave;ce</td>
										<td style="text-align: right;color:blue;"><%=BigDecimalUtil.formatNumberZero(det.getMtt_espece()) %></td>
									</tr>
									<%} %>
									<%if(!BigDecimalUtil.isZero(det.getMtt_cheque())){ %>
									<tr>
										<td style="padding-left: 50px;color:blue;">Ch&egrave;que</td>
										<td style="text-align: right;color:blue;"><%=BigDecimalUtil.formatNumberZero(det.getMtt_cheque()) %></td>
									</tr>
									<%} %>
									<%if(!BigDecimalUtil.isZero(det.getMtt_cheque_f())){ %>
									<tr>
										<td style="padding-left: 50px;color:blue;">Ch&egrave;que fournisseur</td>
										<td style="text-align: right;color:blue;"><%=BigDecimalUtil.formatNumberZero(det.getMtt_cheque_f()) %></td>
									</tr>
									<%} %>
									<%if(!BigDecimalUtil.isZero(det.getMtt_carte())){ %>
									<tr>
										<td style="padding-left: 50px;color:blue;">Carte</td>
										<td style="text-align: right;color:blue;"><%=BigDecimalUtil.formatNumberZero(det.getMtt_carte()) %></td>
									</tr>
									<%} %>
									<%if(!BigDecimalUtil.isZero(det.getMtt_virement())){ %>
									<tr>
										<td style="padding-left: 50px;color:blue;">Virement</td>
										<td style="text-align: right;color:blue;"><%=BigDecimalUtil.formatNumberZero(det.getMtt_virement()) %></td>
									</tr>
									<%} %>	
								<%} %>
						<tr>
							<td>FRAIS LIVREURS (<%=etatFinanceBean.getNbr_livraison() %> commandes)</td>
							<td style="text-align: right;"><%=BigDecimalUtil.formatNumberZero(etatFinanceBean.getMttLivraisonPartLivreur()) %><%=devise%></td>
						</tr>
						<tr>
							<td>DEPENSES DIVERS</td>
							<td style="text-align: right;"><%=BigDecimalUtil.formatNumberZero(etatFinanceBean.getMtt_depense_divers()) %><%=devise%></td>
						</tr>
							<%for(EtatFinancePaiementPersistant det : etatFinanceBean.getList_paiement()){ 
									if(!det.getType().equals("d")){
										continue;
									}
								%>
									<%if(!BigDecimalUtil.isZero(det.getMtt_espece())){ %>
									<tr>
										<td style="padding-left: 50px;color:blue;">Esp&egrave;ce</td>
										<td style="text-align: right;color:blue;"><%=BigDecimalUtil.formatNumberZero(det.getMtt_espece()) %></td>
									</tr>
									<%} %>
									<%if(!BigDecimalUtil.isZero(det.getMtt_cheque())){ %>
									<tr>
										<td style="padding-left: 50px;color:blue;">Ch&egrave;que</td>
										<td style="text-align: right;color:blue;"><%=BigDecimalUtil.formatNumberZero(det.getMtt_cheque()) %></td>
									</tr>
									<%} %>
									<%if(!BigDecimalUtil.isZero(det.getMtt_cheque_f())){ %>
									<tr>
										<td style="padding-left: 50px;color:blue;">Ch&egrave;que fournisseur</td>
										<td style="text-align: right;color:blue;"><%=BigDecimalUtil.formatNumberZero(det.getMtt_cheque_f()) %></td>
									</tr>
									<%} %>
									<%if(!BigDecimalUtil.isZero(det.getMtt_carte())){ %>
									<tr>
										<td style="padding-left: 50px;color:blue;">Carte</td>
										<td style="text-align: right;color:blue;"><%=BigDecimalUtil.formatNumberZero(det.getMtt_carte()) %></td>
									</tr>
									<%} %>
									<%if(!BigDecimalUtil.isZero(det.getMtt_virement())){ %>
									<tr>
										<td style="padding-left: 50px;color:blue;">Virement</td>
										<td style="text-align: right;color:blue;"><%=BigDecimalUtil.formatNumberZero(det.getMtt_virement()) %></td>
									</tr>
									<%} %>	
								<%} %>
								
								<tr>
									<td style="text-align: right;font-weight: bold;">TOTAL DEPENSES</td>
									<td style="text-align: right;font-weight: bold;"><%=BigDecimalUtil.formatNumberZero(totalDepense) %><%=devise%></td>
								</tr>
							</tbody>
							</table>
							
				<br>		
				  <h4 style="color: blue;">REPARTITION PAR MODE DE PAIEMENT</h4>
                  <table class="table table-hover">
                      <thead class="bordered-darkorange">
                          <tr>
                              <th>
                                  Mode de paiement
                              </th>
                              <th style="text-align: right;">
                                  Montant
                              </th>
                          </tr>
                      </thead>
                      <tbody>
                      		<%if(!BigDecimalUtil.isZero(espece_D)){ %>
                          <tr>
                              <td>
                                  Esp&egrave;ce
                              </td>
                              <td style="text-align: right;">
                                 <%=BigDecimalUtil.formatNumberZero(espece_D) %><%=devise%>
                              </td>
                          </tr>
                          <%} %>
                          <%if(!BigDecimalUtil.isZero(cheque_D)){ %>
                          <tr>
                              <td>
                                  Ch&egrave;que
                              </td>
                              <td style="text-align: right;">
                                <%=BigDecimalUtil.formatNumberZero(cheque_D) %><%=devise%>
                              </td>
                          </tr>
                          <%} %>
                          <%if(!BigDecimalUtil.isZero(chequeF_D)){ %>
                          <tr>
                              <td>
                                  Ch&egrave;que fournisseur
                              </td>
                              <td style="text-align: right;">
                                 <%=BigDecimalUtil.formatNumberZero(chequeF_D) %><%=devise%>
                              </td>
                          </tr>
                          <%} %>
                          <%if(!BigDecimalUtil.isZero(carte_D)){ %>
                          <tr>
                              <td>
                                  Carte
                              </td>
                              <td style="text-align: right;">
                                 <%=BigDecimalUtil.formatNumberZero(carte_D) %><%=devise%>
                              </td>
                          </tr>
                          <%} %>
                          <%if(!BigDecimalUtil.isZero(virement_D)){ %>
                          <tr>
                              <td>
                                  Virement
                              </td>
                              <td style="text-align: right;">
                                 <%=BigDecimalUtil.formatNumberZero(virement_D) %><%=devise%>
                              </td>
                          </tr>
                          <%} %>
						</tbody>
					</table>	
				</div>
			</div>
		</div>
	
	
	<h5 class="row-title"><i class="typcn typcn-th-small"></i>ETATS</h5>
	<!-- ************************************************************************************************* -->
	<div class="row" style="margin-left: 0px;margin-right: -7px;">	
		<div class="col-xs-12 col-md-6">
              <div class="well with-header  with-footer">
                  <div class="header bg-blue">
                     ETAT CHEQUE
                  </div>
                  <table class="table table-hover">
                      <thead class="bordered-darkorange">
                          <tr>
                          	  <th>
                          	  Type
                          	  </th>
                              <th style="text-align: right;">
                                 Encaiss&eacute;
                              </th>
                              <th style="text-align: right;">
                                  Non encaiss&eacute;
                              </th>
                              <th style="text-align: right;">
                                  Solde
                              </th>
                          </tr>
                      </thead>
                      <tbody>
                      		<tr><td colspan="4" style="text-align: center;font-weight: bold;background-color: #e6ee9c;">RECETTES</td></tr>
							<tr>
								<td>Recettes divers</td>
								<td style="text-align: right;"><%=BigDecimalUtil.formatNumberZero(etatFinanceBean.getMtt_recette_cheque_encais()) %></td>
								<td style="text-align: right;"><%=BigDecimalUtil.formatNumberZero(etatFinanceBean.getMtt_recette_cheque_non_encais()) %></td>
								<td style="text-align: right;font-weight: bold;"><%=BigDecimalUtil.formatNumberZero(BigDecimalUtil.add(etatFinanceBean.getMtt_recette_cheque_encais(), etatFinanceBean.getMtt_recette_cheque_non_encais()))%></td>
							</tr>
							<tr>
								<td>Avoirs</td>
								<td style="text-align: right;"><%=BigDecimalUtil.formatNumberZero(etatFinanceBean.getMtt_avoir_cheque_encais()) %></td>
								<td style="text-align: right;"><%=BigDecimalUtil.formatNumberZero(etatFinanceBean.getMtt_avoir_cheque_non_encais()) %></td>
								<td style="text-align: right;font-weight: bold;"><%=BigDecimalUtil.formatNumberZero(BigDecimalUtil.add(etatFinanceBean.getMtt_avoir_cheque_encais(), etatFinanceBean.getMtt_avoir_cheque_non_encais()))%></td>
							</tr>
							<tr>
								<td>Ventes</td>
								<td style="text-align: right;"><%=BigDecimalUtil.formatNumberZero(etatFinanceBean.getMtt_vente_cheque_encais()) %></td>
								<td style="text-align: right;"><%=BigDecimalUtil.formatNumberZero(etatFinanceBean.getMtt_vente_cheque_non_encais()) %></td>
								<td style="text-align: right;font-weight: bold;"><%=BigDecimalUtil.formatNumberZero(BigDecimalUtil.add(etatFinanceBean.getMtt_vente_cheque_encais(), etatFinanceBean.getMtt_vente_cheque_non_encais()))%></td>
							</tr>
							<%
							BigDecimal ttEncaisseREC = BigDecimalUtil.add(
									etatFinanceBean.getMtt_recette_cheque_encais(), 
									etatFinanceBean.getMtt_vente_cheque_encais(), 
									etatFinanceBean.getMtt_avoir_cheque_encais());
							BigDecimal ttNonEncaisseREC = BigDecimalUtil.add(
									etatFinanceBean.getMtt_recette_cheque_non_encais(), 
									etatFinanceBean.getMtt_vente_cheque_non_encais(),
									etatFinanceBean.getMtt_avoir_cheque_non_encais());
							BigDecimal ttSoldeREC = BigDecimalUtil.add(ttEncaisseREC, ttNonEncaisseREC);
										
							%>
							<tr>
								<td style="text-align: right;font-weight: bold;">TOTAL</td>
								<td style="text-align: right;font-weight: bold;"><%=BigDecimalUtil.formatNumberZero(ttEncaisseREC) %></td>
								<td style="text-align: right;font-weight: bold;"><%=BigDecimalUtil.formatNumberZero(ttNonEncaisseREC) %></td>
								<td style="text-align: right;font-weight: bold;"><%=BigDecimalUtil.formatNumberZero(ttSoldeREC) %></td>
							</tr>
							
							
                      		<tr><td colspan="4" style="text-align: center;font-weight: bold;background-color: #e6ee9c;">DEPENSES</td></tr>
							<tr>
								<td>Achats</td>
								<td style="text-align: right;"><%=BigDecimalUtil.formatNumberZero(etatFinanceBean.getMtt_achat_cheque_encais()) %></td>
								<td style="text-align: right;"><%=BigDecimalUtil.formatNumberZero(etatFinanceBean.getMtt_achat_cheque_non_encais()) %></td>
								<td style="text-align: right;font-weight: bold;"><%=BigDecimalUtil.formatNumberZero(BigDecimalUtil.add(etatFinanceBean.getMtt_achat_cheque_encais(), etatFinanceBean.getMtt_achat_cheque_non_encais()))%></td>
							</tr>
							<tr>
								<td>D&eacute;penses</td>
								<td style="text-align: right;"><%=BigDecimalUtil.formatNumberZero(etatFinanceBean.getMtt_depense_cheque_encais()) %></td>
								<td style="text-align: right;"><%=BigDecimalUtil.formatNumberZero(etatFinanceBean.getMtt_depense_cheque_non_encais()) %></td>
								<td style="text-align: right;font-weight: bold;"><%=BigDecimalUtil.formatNumberZero(BigDecimalUtil.add(etatFinanceBean.getMtt_depense_cheque_encais(), etatFinanceBean.getMtt_depense_cheque_non_encais()))%></td>
							</tr>
							<tr>
								<td>Salaires</td>
								<td style="text-align: right;"><%=BigDecimalUtil.formatNumberZero(etatFinanceBean.getMtt_salaire_cheque_encais()) %></td>
								<td style="text-align: right;"><%=BigDecimalUtil.formatNumberZero(etatFinanceBean.getMtt_salaire_cheque_non_encais()) %></td>
								<td style="text-align: right;font-weight: bold;"><%=BigDecimalUtil.formatNumberZero(BigDecimalUtil.add(etatFinanceBean.getMtt_salaire_cheque_encais(), etatFinanceBean.getMtt_salaire_cheque_non_encais()))%></td>
							</tr>
							<%
							BigDecimal ttEncaisseDEP = BigDecimalUtil.add(
									etatFinanceBean.getMtt_achat_cheque_encais(), 
									etatFinanceBean.getMtt_depense_cheque_encais(), 
									etatFinanceBean.getMtt_salaire_cheque_encais());
									
							BigDecimal ttNonEncaisseDEP = BigDecimalUtil.add(
									etatFinanceBean.getMtt_achat_cheque_non_encais(), 
									etatFinanceBean.getMtt_depense_cheque_non_encais(), 
									etatFinanceBean.getMtt_salaire_cheque_non_encais());
									
							BigDecimal ttSoldeDEP = BigDecimalUtil.add(ttEncaisseDEP, ttNonEncaisseDEP);
							%>
							<tr>
								<td style="text-align: right;font-weight: bold;">TOTAL</td>
								<td style="text-align: right;font-weight: bold;"><%=BigDecimalUtil.formatNumberZero(ttEncaisseDEP) %></td>
								<td style="text-align: right;font-weight: bold;"><%=BigDecimalUtil.formatNumberZero(ttNonEncaisseDEP) %></td>
								<td style="text-align: right;font-weight: bold;"><%=BigDecimalUtil.formatNumberZero(ttSoldeDEP)%></td>
							</tr>
						</tbody>
					</table>		
				</div>
			</div>
			<div class="col-xs-12 col-md-6">
              <div class="well with-header  with-footer">
                  <div class="header bg-blue">
                     ETAT BANQUE
                  </div>
                  <table class="table table-hover">
                      <thead class="bordered-darkorange">
                          <tr>
                              <th>
                                 Banque
                              </th>
                              <th style="text-align: right;">
                                Pr&eacute;c&eacute;dent
                            </th>
                            <th style="text-align: right;">
                                Actuel
                            </th>
                            <th style="text-align: right;font-weight: bold;">
                                Solde
                            </th>
                        </tr>
                        <%for(EtatFinanceDetailPersistant det : etatFinanceBean.getList_detail()){ 
						if(!det.getType().equals("BANQ")){
							continue;
						}
					%>
						<tr>
							<td><%=det.getLibelle() %></td>
							<td align="right"><%=BigDecimalUtil.formatNumberZero(det.getMtt_etat_prev()) %></td>
							<td align="right"><%=BigDecimalUtil.formatNumberZero(det.getMtt_etat_actuel()) %></td>
							<td align="right"><%= BigDecimalUtil.formatNumberZero(BigDecimalUtil.substract(det.getMtt_etat_actuel(), det.getMtt_etat_prev())) %></td>
						</tr>	
					<%} %>
                        <tr>
					<td style="text-align: right;font-weight: bold;" colspan="3">SOLDE</td>
					<td style="text-align: right;font-weight: bold;"><%=BigDecimalUtil.formatNumberZero(soldeBanque) %><%=devise%></td>
				  </tr>
                    </thead>
                    <tbody> 
					</table>
				</div>
			</div>
		</div>	
		<div class="row" style="margin-left: 0px;margin-right: -7px;">	
			<div class="col-xs-12 col-md-6">
              <div class="well with-header  with-footer">
                  <div class="header bg-blue">
                     ETAT STOCK
                  </div>
                  <table class="table table-hover">
                      <thead class="bordered-darkorange">
                          <tr>
                              <th>
                                 Stock
                              </th>
                              <th style="text-align: right;">
                                  Pr&eacute;c&eacute;dent
                              </th>
                              <th style="text-align: right;">
                                  Actuel
                              </th>
                              <th style="text-align: right;font-weight: bold;">
                                  Solde
                              </th>
                          </tr>
                          <%for(EtatFinanceDetailPersistant empl : etatFinanceBean.getList_detail()){ 
								if(!empl.getType().equals("EMPL")){
									continue;
								}
							%>
								<tr>
									<td><%=empl.getLibelle() %></td>
									<td align="right"><%=BigDecimalUtil.formatNumberZero(empl.getMtt_etat_prev()) %></td>
									<td align="right"><%=BigDecimalUtil.formatNumberZero(empl.getMtt_etat_actuel()) %></td>
									<td align="right"><%=BigDecimalUtil.formatNumberZero(soldeStock) %></td>
								</tr>	
							<%} %>
                          <tr>
                          	<td  colspan="3" style="text-align: right;font-weight: bold;">SOLDE</td>
                          	<td style="text-align: right;font-weight: bold;"><%=BigDecimalUtil.formatNumberZero(soldeStock) %><%=devise%></td>
                          </tr>
                         </thead>
                      </table>    
				</div>
		</div>		
	</div>

	</std:form>