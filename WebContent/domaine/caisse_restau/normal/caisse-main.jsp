<%@page import="framework.model.common.util.BooleanUtil"%>
<%@page import="appli.model.domaine.vente.persistant.CaissePersistant"%>
<%@page import="java.util.List"%>
<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="appli.controller.domaine.caisse.ContextAppliCaisse"%>
<%@page import="appli.controller.domaine.caisse.action.CaisseWebBaseAction"%>
<%@page import="appli.model.domaine.stock.service.IMouvementService"%>
<%@page import="framework.controller.Context"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="java.util.Map"%>
<%@page import="framework.model.util.FileUtil"%>
<%@page import="appli.model.domaine.stock.service.IArticleService"%> 
<%@page import="framework.model.common.util.ServiceUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli.STATUT_JOURNEE"%>
<%@page import="framework.model.common.util.DateUtil"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="appli.model.domaine.personnel.persistant.EmployePersistant"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="appli.model.domaine.vente.persistant.CaisseMouvementPersistant"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%> 
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>
<%@page import="framework.controller.ControllerUtil"%>

<!DOCTYPE html>
<html lang="fr-fr" style="overflow: hidden;">	
<%
		// Purge
	ControllerUtil.cleanAll(request);
	
	boolean isCaisseVerouille = ControllerUtil.getMenuAttribute("IS_CAISSE_VERROUILLE", request) != null;
	boolean isDevise = !BigDecimalUtil.isZero(BigDecimalUtil.get(ContextGloabalAppli.getGlobalConfig("DEVISE_TAUX")));
	boolean isCaisseNotFermee = (ContextAppliCaisse.getJourneeCaisseBean() != null && "O".equals(ContextAppliCaisse.getJourneeCaisseBean().getStatut_caisse()));
	boolean isJourneeCaisseOuverte = !isCaisseVerouille && isCaisseNotFermee;	
	boolean isJourneeOuverte = (ContextAppliCaisse.getJourneeBean() != null && "O".equals(ContextAppliCaisse.getJourneeBean().getStatut_journee()));
	
	boolean isServeur = "SERVEUR".equals(ContextAppli.getUserBean().getOpc_profile().getCode());
	boolean isManager = ContextAppli.getUserBean().isInProfile("MANAGER");
	boolean isAdmin = ContextAppli.getUserBean().isInProfile("ADMIN");
	boolean isCaissier = ContextAppli.getUserBean().isInProfile("CAISSIER");
	
	boolean isConfirmOffre = (!isAdmin && !isManager && StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("SECURE_REDUCTION_CMD")));

	int WIDTH_CMD = isJourneeCaisseOuverte ? 413 : 0;
	int HEIGHT_DETAIL = BigDecimalUtil.get(ContextGloabalAppli.getGlobalConfig("HAUTEUR_BLOC_FAMILLE")).intValue();
	
	boolean isSatCuisine = ContextGloabalAppli.getAbonementBean().isSatCuisine();
	%>	
	
<!-- Head -->	
	<head>
		<title>Gestion de la caisse automatique</title>
		<jsp:include page="/WEB-INF/fragment/header-resources.jsp"></jsp:include>
		
		<link rel="stylesheet" type="text/css" href="resources/caisse/css/caisse.css?v=<%=StrimUtil.getGlobalConfigPropertie("version.resources")%>15">
		 <link href="resources/framework/js/contextmenu/jquery.contextMenu.min.css" rel="stylesheet" type="text/css" />
		 <script src="resources/framework/js/contextmenu/jquery.contextMenu.min.js" type="text/javascript"></script>
	 	<script src="resources/framework/js/contextmenu/jquery.ui.position.min.js" type="text/javascript"></script>
	
	<jsp:include page="/domaine/caisse/normal/caisse-js-include.jsp" />

	<script type="text/javascript">
		window.addEventListener('popstate', function(event) {
			history.pushState(null, null, window.location.pathname);
		    alert("Merci de ne pas utiliser le bouton retour du navigateur");
		}, false);
	
	
		var intervalTime = null;
		var interval_tracker_id = 0;
	
		function manageItemTr(trParam, key){
			var tp = trParam.substring(trParam.indexOf("tp=")+3);
			var isdbS = trParam.indexOf("isDb=") != -1;
			//
        	if(key == 'selCli'){
				$("#targ_link").attr("targetDiv", "left-div");
				$("#targ_link").attr("wact", '<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.selectGroupe")%>').attr("params", trParam).trigger("click");
			} else if(key == 'selTab'){
				$("#targ_link").attr("targetDiv", "left-div");
				$("#targ_link").attr("wact", '<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.selectTable")%>').attr("params", trParam).trigger("click");
			} else if(key == 'annTab'){
				$("#targ_link").attr("targetDiv", "left-div");
				$("#targ_link").attr("wact", '<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.resetElement")%>').attr("params", trParam).trigger("click");
			} else if(key == 'couvTab'){
				$("#targ_link").attr("targetDiv", "left-div");
				$("#targ_link_pop").attr("wact", '<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.manageCouverts")%>').attr("params", trParam).trigger("click");
			} else if(key == 'selMenu'){
				$("#targ_link").attr("targetDiv", "menu-detail-div");
				$("#targ_link").attr("wact", '<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.selectLigneMenu")%>').attr("params", trParam).trigger("click");
			} else if(key == 'dupMenu'){
				$("#targ_link").attr("targetDiv", "left-div");
        		$("#targ_link_pop").attr("wact", '<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.initDupMnu")%>').attr("params", trParam).trigger("click");
			} else if(tp == 'TAB' && key == 'del'){
				if($("#is_confirm_mngr").val() == '1'){
					$("#del-cmd-lnk").attr("params", "tpact=delrow&trParam="+trParam.replace(/\&/g,"**")).trigger("click");
				} else{
					$("#targ_link").attr("targetDiv", "left-div");
        			var eventBack = trParam.indexOf("tp=MENU") == -1 ? null : '$("#home_lnk").trigger("click")';
					showConfirmDeleteBox('<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.supprimerTable")%>', trParam, $("#targ_link"), "Cette ligne sera supprim&eacute;e.<br>Voulez-vous confirmer ?", eventBack, "Suppression ligne");
				}
        	} else if(tp == 'CLI' && key == 'del'){
        		if($("#is_confirm_mngr").val() == '1'){
        			$("#del-cmd-lnk").attr("params", "tpact=delrow&trParam="+trParam.replace(/\&/g,"**")).trigger("click");
        		} else{
        			$("#targ_link").attr("targetDiv", "left-div");
        			var eventBack = trParam.indexOf("tp=MENU") == -1 ? null : '$("#home_lnk").trigger("click")';
					showConfirmDeleteBox('<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.supprimerClient")%>', trParam, $("#targ_link"), "Cette ligne sera supprim&eacute;e.<br>Voulez-vous confirmer ?", eventBack, "Suppression ligne");
        		}
        	} else if(key == 'suite'){
        		$("#targ_link").attr("targetDiv", "left-div");
        		$("#targ_link").attr("wact", '<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.suiteCommande")%>').attr("params", trParam).trigger("click");
        	} else if(key == 'devise'){
        		$("#targ_link").attr("targetDiv", "left-div");
        		$("#targ_link").attr("wact", '<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.manageDevise")%>').attr("params", trParam).trigger("click");
        	} else if(key == 'annSuite'){
        		$("#targ_link").attr("targetDiv", "left-div");
        		$("#targ_link").attr("wact", '<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.suiteCommandeAnnule")%>').attr("params", trParam).trigger("click");
        	} else if(key == 'del'){
        		if($("#is_confirm_mngr").val() == '1' && isdbS){
        			$("#del-cmd-lnk").attr("params", "tpact=delrow&trParam="+trParam.replace(/\&/g,"**")).trigger("click");// Remplacer les & pour les traiter dans la popup confirmation
        		} else{
        			$("#targ_link").attr("targetDiv", "left-div");
    				var eventBack = trParam.indexOf("tp=MENU") == -1 ? null : '$("#home_lnk").trigger("click")';
    				showConfirmDeleteBox('<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.deleteRow")%>', trParam, $("#targ_link"), "Cette ligne sera supprim&eacute;e.<br>Voulez-vous confirmer ?", eventBack, "Suppression ligne");	
        		}
        	} else if(key == 'enc'){
        		$("#targ_link").attr("targetDiv", "left-div");
        		$("#targ_link_pop").attr("wact", '<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.initPaiement")%>').attr("params", trParam).trigger("click");
        	} else if(key == 'annEnc'){ 
        		$("#targ_link").attr("targetDiv", "left-div");
        		$("#targ_link").attr("wact", '<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.annulerEncaissement")%>').attr("params", trParam).trigger("click");
        	} else if(key == 'com'){
        		$("#targ_link").attr("targetDiv", "left-div");
        		$("#targ_link_pop").attr("wact", '<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.initCommentaireCommande")%>').attr("params", trParam).trigger("click");
        	} else if(key == 'qte'){
        		$("#targ_link").attr("targetDiv", "left-div");
        		$("#targ_link_pop").attr("wact", '<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.changeQuantite")%>').attr("params", trParam).trigger("click");
        	} else if(key == 'off'){
        		<%if(isConfirmOffre){%>
	    			$("#del-cmd-lnk").attr("params", "tpact=offrir&trParam="+trParam.replace(/\&/g,"**")).trigger("click");
	    		<%} else{%>
	    			$("#targ_link").attr("targetDiv", "left-div");
					$("#targ_link").attr("wact", '<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.offrirLigneCommande")%>').attr("params", trParam).trigger("click");
	    		<%}%>
        	} else if(key == 'annOff'){
        		$("#targ_link").attr("targetDiv", "left-div");
        		$("#targ_link").attr("wact", '<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.offrirLigneCommande")%>').attr("params", trParam).trigger("click");
        	} else if(key == 'offRed'){
         			$("#targ_link_pop").attr("wact", '<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.initOffrirLigneCommande")%>').attr("params", trParam).trigger("click");
         	} else if(key == 'annOffRed'){
         		$("#targ_link").attr("targetDiv", "left-div");
         		$("#targ_link").attr("wact", '<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.offrirLigneCommandeRed")%>').attr("params", trParam).trigger("click");
        	} else if(key == 'trans'){
        		if(tp == 'CLI'){
        			$("#targ_link_pop").attr("wact", '<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.initPlan")%>').attr("params", trParam).trigger("click");
        		} else {
        			$("#targ_link_pop").attr("wact", '<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.initTransfertArtMnu")%>').attr("params", trParam).trigger("click");
        		}
        	} else if(key == 'chngTab'){
        		$("#targ_link_pop").attr("wact", '<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.initPlan")%>').attr("params", trParam).trigger("click");
        	}
		}
		
		$(document).ready(function (){
		<%
		if(ControllerUtil.getUserAttribute("LOADER_TRIGGERED", request) == null){
			if(!StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("CAISSE_PRINT"))){%>
				callExternalUrl("http://localhost:8001/cm-client?act=printers&cais=<%=ContextAppliCaisse.getCaisseBean().getId()%>");
			<%}
			
			List<CaissePersistant> listAfficheurs = (List<CaissePersistant>) ControllerUtil.getUserAttribute("LIST_AFFICHEUR", request);
			if(listAfficheurs != null && listAfficheurs.size() > 0 && BooleanUtil.isTrue(listAfficheurs.get(0).getIs_aff_integre())){%>
				callExternalUrl("http://localhost:8001/cm-client?act=2screen&aff=<%=ContextAppliCaisse.getCaisseBean().getId() %>");
			<%}
			ControllerUtil.setUserAttribute("LOADER_TRIGGERED", true, request);
		}%>
			
			$.contextMenu({
		        selector: '#cmd-table tr[par]', 
		        trigger: 'left',
		        build: function($trigger, e) {
		        	
		        	if($trigger.find("a").length > 0){
			        	var position = $trigger.find("a").offset();
			        	
			        	if(e.pageX <= 45){
			        		return;
			        	}
		        	}
		        	
		        	var isMttInLine = $trigger.attr("par").indexOf("isart=1") != -1;
		        	var isRoot = $trigger.attr("par").indexOf("isroot=1") != -1;
		        	$("#cmd-table tr[sel]").each(function(){
		        		if(!$(this).attr("par") || ($(this).attr("par").indexOf("istab=1")== -1 && $(this).attr("par").indexOf("iscli=1")== -1)){
		        			$(this).removeAttr("sel").css("background-color", "");	
		        		}
		        	});
		        	
		        	var tp = $trigger.attr("par").substring($trigger.attr("par").indexOf("tp=")+3);
		        	if(tp.indexOf("&") != -1){
		        		tp = tp.substring(0, tp.indexOf("&"));
		        	}
		        	
		        	if(tp == 'LIVRAISON' || tp == 'GARANTIE'){
		        		setTimeout(() => {
		        			$(".context-menu-root").remove();	
						}, 1000);
		        		
		        		return;
		        	}
		        	
		        	$("#cmd-table tr[sel]").each(function(){
		        		if(!$trigger.attr("par") || ($trigger.attr("par").indexOf("istab=1")== -1 && $trigger.attr("par").indexOf("iscli=1")== -1)){
		        			$trigger.attr("sel", "1").css("background-color", "#94e0fd");	
		        		}
		        	});
		        	
		        	var mnuItems = "";
		        	
		        	if(tp == 'CLI'){
		        		mnuItems = mnuItems + '<hr> <command label="S&eacute;l&eacute;ctionner ce client" icon="fa-male" onclick="manageItemTr(\''+$trigger.attr("par")+'\', \'selCli\');">';
		        	} else if(tp == 'TAB' ){
		        		mnuItems = mnuItems + '<hr> <command label="S&eacute;l&eacute;ctionner cette table" icon="fa-cutlery" onclick="manageItemTr(\''+$trigger.attr("par")+'\', \'selTab\');">';
		        	} else if(tp == 'MENU' && $trigger.attr("class").indexOf("mnu_")!=-1){
		        		mnuItems = mnuItems + '<hr> <command label="S&eacute;l&eacute;ctionner ce menu" icon="fa-cutlery" onclick="manageItemTr(\''+$trigger.attr("par")+'\', \'selMenu\');">';
		        		mnuItems = mnuItems + '<hr> <command label="Dupliquer ce menu" icon="fa-copy" onclick="manageItemTr(\''+$trigger.attr("par")+'\', \'dupMenu\');">';
		        	}
		        <%if(!isServeur || isCaissier || isManager || isAdmin){%>
		        	if(!isRoot && ((isMttInLine && $trigger.attr("par").indexOf("mnu=null")!=-1) || tp == 'MENU' || tp == 'CLI' || tp == 'TAB')){
			        	if($trigger.find("i[isenc]").length == 0){
			        		mnuItems = mnuItems + '<hr> <command label="Encaisser" icon="fa-money enc" onclick="manageItemTr(\''+$trigger.attr("par")+'\', \'enc\');">';
			        	} else{
			        		mnuItems = mnuItems + '<hr> <command label="Annuler encaissement" icon="fa-money annul-enc" onclick="manageItemTr(\''+$trigger.attr("par")+'\', \'annEnc\');">';	
			        	}
		        	}
		        <%}%>
		        <%if(StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("CAISSE_OFFRE")) && (!isServeur || isCaissier || isManager || isAdmin)){%>
		        	if(tp != 'TAB' && tp != 'CLI'){
		        		if($trigger.find("i[isoffR]").length == 0){
        					if($trigger.find("i[isoff]").length == 0 && isMttInLine){
        						mnuItems = mnuItems + '<hr> <command label="R&eacute;duction" icon="fa-gift off" onclick="manageItemTr(\''+$trigger.attr("par")+'\', \'offRed\');">';
        					}
        				} else{
        					mnuItems = mnuItems + '<hr> <command label="Annuler r&eacute;duction" icon="fa-gift annul-off" onclick="manageItemTr(\''+$trigger.attr("par")+'\', \'annOffRed\');">';
        				}
		        		
		        		if($trigger.find("i[isoff]").length == 0){
		        			if($trigger.find("i[isoffR]").length == 0 && isMttInLine){
			        			mnuItems = mnuItems + '<hr> <command label="Offrir cet &eacute;l&eacute;ment" icon="fa-gift off" onclick="manageItemTr(\''+$trigger.attr("par")+'\', \'off\');">';
			        		}
			        	} else{
			        		mnuItems = mnuItems + '<hr> <command label="Annuler offre" icon="fa-gift annul-off" onclick="manageItemTr(\''+$trigger.attr("par")+'\', \'annOff\');">';
			        	}
		        	}
		        <%}%>
		        	if(!isRoot && (tp == 'MENU' || (isMttInLine && $trigger.attr("par").indexOf("mnu=null")!=-1) || tp == 'CLI')){
		        		mnuItems = mnuItems + '<hr> <command label="Transf&eacute;rer" icon="fa-mail-forward trans" onclick="manageItemTr(\''+$trigger.attr("par")+'\', \'trans\');">';
		        	} 
		        	if(tp == 'TAB'){
		        		mnuItems = mnuItems + '<hr> <command label="Annuler table" icon="fa-power-off annul-tab" onclick="manageItemTr(\''+$trigger.attr("par")+'\', \'annTab\');">';
		        		mnuItems = mnuItems + '<hr> <command label="Nombre de couverts" icon="fa-paw couvert-tab" onclick="manageItemTr(\''+$trigger.attr("par")+'\', \'couvTab\');">';
		        		mnuItems = mnuItems + '<hr> <command label="Changer la table" icon="fa-mail-forward chng-tab" onclick="manageItemTr(\''+$trigger.attr("par")+'\', \'chngTab\');">';
		        	}
		        	
		        	if(isMttInLine || tp == 'ART_MENU'){ 
		        		
		        		mnuItems = mnuItems + '<hr> <command label="Commenter" icon="fa-comment-o com" onclick="manageItemTr(\''+$trigger.attr("par")+'\', \'com\');">';
		        		
		        		if(isMttInLine 
		        				&& !isRoot 
		        				&& tp != 'MENU' 
		        				&& $trigger.attr("par").indexOf("mnu=null")!=-1){
		        			
		        			mnuItems = mnuItems + '<hr> <command label="Quantit&eacute;" icon="fa-sort-numeric-asc qte" onclick="manageItemTr(\''+$trigger.attr("par")+'\', \'qte\');">';
		        		}
		        	}
		        	
		        	<%if(isSatCuisine){%>
			        	if(tp == 'ART' || tp == 'ART_MENU'){
			        		if($trigger.find("i[issuiteend]").length == 0){
				        		if($trigger.find("i[issuite]").length == 0){
				        			mnuItems = mnuItems + '<hr>  <command label="Suite commande" icon="fa-clock-o suite" onclick="manageItemTr(\''+$trigger.attr("par")+'\', \'suite\');">';
				        		} else{
				        			mnuItems = mnuItems + '<hr>  <command label="Lancer la suite" icon="fa-clock-o ann-suite" onclick="manageItemTr(\''+$trigger.attr("par")+'\', \'annSuite\');">';
				        		}
			        		}
			        	}
		        	<%}%>
		        	
		        	<%if(isDevise){%>
		        		if(isMttInLine){
		        			mnuItems = mnuItems + '<hr>  <command label="En devise" icon="fa-euro eur" onclick="manageItemTr(\''+$trigger.attr("par")+'\', \'devise\');">';
		        		}
		        	<%}%>
		        	
		        	if(!isRoot){
	        			mnuItems = mnuItems + '<hr>  <command label="Supprimer" icon="fa-times del" onclick="manageItemTr(\''+$trigger.attr("par")+'\', \'del\');">';
		        	}
		        	
		        	$('#html5menu').html(mnuItems);
			
		        	return {
				        items: $.contextMenu.fromMenu($('#html5menu'))
		        	};
		           }
			    });
		});

		function refreshSize(){
			var widowHeight = $(window).height();
			var widowWidth = $(window).width();
			
			var dataZoom = readLocalStorage('zoom_cai_cock');
			if(dataZoom && dataZoom!=null && dataZoom!='' && dataZoom!='1'){
				
				var ratioArray = getWindowRatioZoom();
				
				widowHeight = parseInt(widowHeight+(widowHeight/ratioArray[1]));
				widowWidth = parseInt(widowWidth+(widowWidth/ratioArray[0]));
				//
				$("#zoom_slct").val(dataZoom);
			}
			// Css
			manageZoom(dataZoom);
			
			// Commande
			$("#left-div").css("height", (widowHeight-103)+"px");
			$("#div_detail_cmds").css("height", (widowHeight-<%=isJourneeCaisseOuverte?"233":"103"%>)+"px");
			
			// Menu
			$("#menu-detail-div, #menu-left-div").css("height", (widowHeight-(<%=HEIGHT_DETAIL%>+48))+"px");
			
			$("#right-div").css("width", (widowWidth-(<%=WIDTH_CMD%>+5))+"px");
			
			$("#menu-detail-div").css("width", ($("#right-div").width()-170)+"px");
			$("#famille-div").css("width", ($("#right-div").width()-3)+"px"); 
		}
		</script>	
	</head>
<!-- /Head -->

<!-- Body -->
<body>
	<std:linkPopup action="caisse-web.caisseWeb.loadConfirmAnnule" id="del-cmd-lnk" style="display:none;" />  
	<menu id="html5menu" style="display:none" class="showcase">
	  	
	</menu>
			
    <!-- Main Container -->
    <div class="main-container container-fluid">
        <!-- Page Container -->
        <div class="page-container">
        
			<jsp:include page="/domaine/caisse/normal/fragment/mnu_top_caisse_web.jsp" />
			
			<div class="page-body" style="position:fixed; margin: 0px;padding: 0px;z-index: -1;top: 48px;">
				<div class="widget">
				<std:form name="data-form">
					<input type="hidden" name="qte_calc" id="qte_calc">
					
			         <div class="widget-body" style="padding: 0px;" id="corp-div">
			         	<!-- Parie gauche -->
			         	<!-- D&eacute;tail articles -->
			         	<% if(isJourneeCaisseOuverte){ %>
			         	<div style="min-height: 534px;float: left;width: <%=WIDTH_CMD%>px;<%=CaisseWebBaseAction.GET_STYLE_CONF("PANEL_LEFT", null)%>" id="left-div">
		         				<jsp:include page="/domaine/caisse_restau/normal/commande-detail.jsp" />
						</div>
						<%} %>
						
						<!-- Panneau droit -->
						<div style="float: left;<%=CaisseWebBaseAction.GET_STYLE_CONF("PANEL_RIGHT", null)%>" id="right-div">
							<jsp:include page="/domaine/caisse_restau/normal/caisse-right-bloc.jsp"></jsp:include>
						</div>
					</div>
				</std:form>
			</div>
			</div>
        </div>
        <!-- /Page Container -->
    </div>
    <!-- Main Container -->
    <script src="resources/framework/js/keyboard/my_keyboard.js?v=<%=StrimUtil.getGlobalConfigPropertie("version.resources")%>"></script>
    <jsp:include page="/commun/keyboard-popup.jsp" />
    <jsp:include page="/commun/keyboard-popup-num.jsp" />
    
    <jsp:include page="/WEB-INF/fragment/footer-resources.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/fragment/static-panels.jsp"/>	
  </body>
    
</html>