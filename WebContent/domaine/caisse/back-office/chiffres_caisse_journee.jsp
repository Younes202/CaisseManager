<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="appli.model.domaine.vente.persistant.CaisseJourneePersistant"%>
<%@page import="appli.model.domaine.vente.persistant.JourneePersistant"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="framework.model.common.util.DateUtil"%>
<%@page import="framework.controller.Context"%>
<%@page import="java.math.BigInteger"%>
<%@page import="java.util.Arrays"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="java.util.List"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>
<%
	boolean isDetailJourneeDroit = Context.isOperationAvailable("DETJRN");
%>
<style>
	#tab_rep tr:hover{
		background: #DBEDF3;
	}
	#tab_rep td {
		border-bottom: 1px dashed #ff9900;
	}
</style>

<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Gestion de la journ&eacute;</li>
		<li class="active">Chiffres par caisse</li>
	</ul>
</div>
<!-- /Page Breadcrumb -->
<!-- Page Header -->
<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
	<%if(ControllerUtil.getMenuAttribute("IS_DASH_JRN", request) == null){ %>
		<std:link classStyle="btn btn-default" action="caisse.journee.work_find" params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la recherche" />
	<%}%>
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

	<div class="widget">
		<div class="tabbable">
			<%request.setAttribute("curr_tab", "chiffre_cai"); %>
			<jsp:include page="journee_tab_header.jsp" />
		</div>
	
		<std:form name="search-form">
         <div class="widget-body">
				<div class="row">	
							
				<%
				String devise = StrimUtil.getGlobalConfigPropertie("devise.html");
				JourneePersistant journeeVente = (JourneePersistant) request.getAttribute("journeeView");
				List<CaisseJourneePersistant> listDataShift = (List) request.getAttribute("listDataShift");
				BigDecimal fraisLivraison = journeeVente.getTarif_livraison();
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
											<div class="databox-number number-xxlg sonic-silver"><%=BigDecimalUtil.formatNumberZero(data.getMtt_total_net())%> DH</div>
											<div class="databox-text storm-cloud">Montant net des ventes <i style="color:#427fed;" class="fa fa-info-circle" title="Montant net hors fond de roulement et frais de livraisons incluses"></i></div>
										</div>
										<div class="databox-cell cell-4 text-align-center">
											<div class="databox-number number-xxlg sonic-silver"><%=BigDecimalUtil.formatNumberZero(data.getNbr_vente())%></div>
											<div class="databox-text storm-cloud">Commandes<br></div>
										</div>
									</div>
								</div>
								<div class="databox-bottom">
									<div class="databox-row row-12 bordered-bottom bordered-ivory padding-10" style="height: 43px;">
										<div class="databox-cell cell-6 text-center no-padding-left padding-bottom-30">	
											<span class="badge badge-empty pull-left margin-5" style="background-color: transparent;"></span> 
											<span class="databox-text darkgray pull-left no-margin hidden-xs">
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
											<span class="databox-text darkgray pull-left no-margin hidden-xs">
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
												<span class="databox-text darkgray pull-left no-margin hidden-xs">Fonds de roulement</span> <span
													class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumberZero(data.getMtt_ouverture())%> </span>
											</div>
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="badge badge-empty pull-left margin-5" style="background-color: #2196f3;"></span> <span class="databox-text darkgray pull-left no-margin hidden-xs">Annulations Cmd</span> <span
													class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumberZero(data.getMtt_annule())%> </span>
											</div>
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="badge badge-empty pull-left margin-5" style="background-color: #2196f3;"></span> <span class="databox-text darkgray pull-left no-margin hidden-xs">Annulations Lignes</span> <span
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
													class="databox-text darkgray pull-left no-margin hidden-xs">Livraisons <i style="color:blue;" class="fa fa-info-circle" title="<%=val%>"></i></span> 
													<span class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumber(BigDecimalUtil.multiply(fraisLivraison, BigDecimalUtil.get((data.getNbr_livraison()==null?0:data.getNbr_livraison())))) %></span>
											</div>
											<%} %>
										</div>
										<div class="databox-cell cell-6 text-center no-padding-left padding-bottom-30">
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="badge badge-empty pull-left margin-5" style="background-color: #2196f3;"></span> <span class="databox-text darkgray pull-left no-margin hidden-xs">R&eacute;ductions Cmd</span> <span
													class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumberZero(data.getMtt_reduction())%> </span>
											</div>
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="badge badge-empty pull-left margin-5" style="background-color: #2196f3;"></span> <span class="databox-text darkgray pull-left no-margin hidden-xs">R&eacute;ductions Art</span> <span
													class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumberZero(data.getMtt_art_reduction())%> </span>
											</div>
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="badge badge-empty pull-left margin-5" style="background-color: #2196f3;"></span> <span class="databox-text darkgray pull-left no-margin hidden-xs">Articles offerts</span> <span
													class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumberZero(data.getMtt_art_offert())%> </span>
											</div>
										</div>	
									</div>
									<%if(mttClotureCaisserEsp != null){ %>
									<div class="databox-row row-12" style="margin-top: 20px;height: 50px;border-top: 2px solid red;">
										<div class="databox-cell cell-6 text-center no-padding-left padding-bottom-30">
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="color:#9c27b0;height: 34px !important;">
												<span class="badge badge-empty pull-left margin-5" style="background-color: #2196f3;"></span> <span class="databox-text darkgray pull-left no-margin hidden-xs">Cl&ocirc;ture caissier</span> <span
													class="databox-text darkgray pull-right no-margin uppercase" style="font-weight: bold;"><%=BigDecimalUtil.formatNumberZero(mttClotureCaissier)%> </span>
											</div>
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="badge badge-empty pull-left margin-5" style="background-color: #2196f3;"></span> 
												<span class="databox-text darkgray pull-left no-margin hidden-xs" style="color: <%=ecartCaissier.compareTo(BigDecimalUtil.ZERO) < 0 ? "red":"green" %> !important;">Ecart caissier</span> 
												<span class="databox-text darkgray pull-right no-margin uppercase"><b style="color: <%=ecartCaissier.compareTo(BigDecimalUtil.ZERO) < 0 ? "red":"green" %>;"><%=BigDecimalUtil.formatNumberZero(ecartCaissier)%> </b></span>
											</div>
										</div>
										<div class="databox-cell cell-6 text-center no-padding-left padding-bottom-30">
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="badge badge-empty pull-left margin-5" style="background-color: #2196f3;"></span> <span class="databox-text darkgray pull-left no-margin hidden-xs">Cl&ocirc;ture caissier net</span> <span
													class="databox-text darkgray pull-right no-margin uppercase" style="font-weight: bold;"><%=BigDecimalUtil.formatNumberZero(mttClotureCaissierNet)%> </span>
											</div>
											<%if(ecartManager != null){ %>
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="badge badge-empty pull-left margin-5" style="background-color: #2196f3;"></span> <span class="databox-text darkgray pull-left no-margin hidden-xs" style="color: <%=ecartManager.compareTo(BigDecimalUtil.ZERO) < 0 ? "red":"green" %> !important;">Ecart manager</span> 
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
												<span class="badge badge-empty pull-left margin-5" style="background-color: #e75b8d;"></span> <span class="databox-text darkgray pull-left no-margin hidden-xs">Especes</span> <span
													class="databox-text darkgray pull-right no-margin uppercase"> <%=BigDecimalUtil.formatNumberZero(data.getMtt_espece())%> </span>
											</div>
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="badge badge-empty pull-left margin-5" style="background-color: #a0d468;"></span> <span class="databox-text darkgray pull-left no-margin hidden-xs">Ch&egrave;ques</span> <span
													class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumberZero(data.getMtt_cheque())%> </span>
											</div>
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="badge badge-empty pull-left margin-5" style="background-color: #a0d468;"></span> <span class="databox-text darkgray pull-left no-margin hidden-xs">Ch&egrave;ques d&eacute;j.</span> <span
													class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumberZero(data.getMtt_dej())%> </span>
											</div>
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="badge badge-empty pull-left margin-5" style="background-color: #ffce55;"></span> <span class="databox-text darkgray pull-left no-margin hidden-xs">Carte bancaire</span> <span
													class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumberZero(data.getMtt_cb())%> </span>
											</div>
											<!-- ************* -->
											<%if(!BigDecimalUtil.isZero(data.getMtt_portefeuille())){ %>
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="badge badge-empty pull-left margin-5" style="background-color: #dbdbdb;"></span> <span class="databox-text darkgray pull-left no-margin hidden-xs">Portefeuille</span> <span
													class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumberZero(data.getMtt_portefeuille())%> </span>
											</div>
											<%} %>
											<%if(!BigDecimalUtil.isZero(data.getMtt_donne_point())){ %>
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="badge badge-empty pull-left margin-5" style="background-color: #dbdbdb;"></span> <span class="databox-text darkgray pull-left no-margin hidden-xs">Points</span> <span
													class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumberZero(data.getMtt_donne_point())%> </span>
											</div>
											<%} %>
										</div>
										<div class="databox-cell cell-3 text-center no-padding-left padding-bottom-30">	
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="databox-text sonic-silver pull-left no-margin" style="font-weight: bold;"></span> <span class="databox-text sonic-silver pull-right no-margin uppercase">Caissier</span>
											</div>
											
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="databox-text darkgray pull-left no-margin hidden-xs"></span> <span
													class="databox-text darkgray pull-right no-margin uppercase" style="<%=isEspeceEcart?"text-decoration: line-through;":""%>"> <%=BigDecimalUtil.formatNumber(mttClotureCaissierEspNet) %> </span>
											</div>
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="databox-text darkgray pull-left no-margin hidden-xs"></span> <span
													class="databox-text darkgray pull-right no-margin uppercase" style="<%=isChequeEcart?"text-decoration: line-through;":""%>"><%=BigDecimalUtil.formatNumber(mttClotureCaisserCheq)%> </span>
											</div>
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="databox-text darkgray pull-left no-margin hidden-xs"></span> <span
													class="databox-text darkgray pull-right no-margin uppercase" style="<%=isDejEcart?"text-decoration: line-through;":""%>"><%=BigDecimalUtil.formatNumber(mttClotureCaisserDej)%> </span>
											</div>
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="databox-text darkgray pull-left no-margin hidden-xs"></span> <span
													class="databox-text darkgray pull-right no-margin uppercase" style="<%=isCarteEcart?"text-decoration: line-through;":""%>"><%=BigDecimalUtil.formatNumber(mttClotureCaisserCb)%> </span>
											</div>
										</div>
										<div class="databox-cell cell-3 text-center no-padding-left padding-bottom-30">
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="databox-text sonic-silver pull-left no-margin" style="font-weight: bold;"></span> <span class="databox-text sonic-silver pull-right no-margin uppercase">Manager</span>
											</div>
											
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="databox-text darkgray pull-left no-margin hidden-xs"></span> <span
													class="databox-text darkgray pull-right no-margin uppercase"> <%=BigDecimalUtil.formatNumber(mttClotureManagerEspNet) %> </span>
											</div>
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="databox-text darkgray pull-left no-margin hidden-xs"></span> <span
													class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumber(mttClotureManagerCheq) %> </span>
											</div>
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="databox-text darkgray pull-left no-margin hidden-xs"></span> <span
													class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumber(mttClotureManagerDej) %> </span>
											</div>
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="databox-text darkgray pull-left no-margin hidden-xs"></span> <span
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
												<span class="databox-text darkgray pull-left no-margin hidden-xs"></span> <span
													class="databox-text darkgray pull-right no-margin uppercase" style='color:<%=(ecartEspeses!=null && ecartEspeses.compareTo(BigDecimalUtil.get(0))<0)?"red":"green" %> !important;'> <%=BigDecimalUtil.formatNumber(ecartEspeses) %> </span>
											</div>
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="databox-text darkgray pull-left no-margin hidden-xs"></span> <span
													class="databox-text darkgray pull-right no-margin uppercase" style='color:<%=(ecartCheque!=null && ecartCheque.compareTo(BigDecimalUtil.get(0))<0)?"red":"green" %> !important;'><%=BigDecimalUtil.formatNumber(ecartCheque) %> </span>
											</div>
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="databox-text darkgray pull-left no-margin hidden-xs"></span> <span
													class="databox-text darkgray pull-right no-margin uppercase" style='color:<%=(ecartDej!=null && ecartDej.compareTo(BigDecimalUtil.get(0))<0)?"red":"green" %> !important;'><%=BigDecimalUtil.formatNumber(ecartDej) %> </span>
											</div>
											<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="height: 34px !important;">
												<span class="databox-text darkgray pull-left no-margin hidden-xs"></span> <span
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
</div>
</std:form>
</div>
</div>