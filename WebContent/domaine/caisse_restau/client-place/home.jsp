<%@page import="appli.controller.domaine.caisse.ContextAppliCaisse"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.model.common.util.StringUtil"%>
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

	boolean isJourneeOuverte = (ContextAppliCaisse.getJourneeBean() != null && ContextAppliCaisse.getJourneeBean().getStatut_journee().equals("O"));
	%>	
	
<!-- Head -->	
	<head>
		<title>Gestion de la caisse automatique</title>
		<jsp:include page="/WEB-INF/fragment/header-resources.jsp"></jsp:include>
		
		<link rel="stylesheet" type="text/css" href="resources/caisse/css/caisse-client.css?v=1.13"> 
		
<script type="text/javascript">
$(document).ready(function(){
	// Empecher session out
	setInterval(function() {
		var url = 'front?w_uact=<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.work_init")%>';
		callBackJobAjaxUrl(url, false);
	}, 60000);
	
	$(document).off('click', '.del-lnk');
	$(document).on('click', '.del-lnk', function(){
		showConfirmDeleteBox('<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.deleteArticle")%>', $(this).attr("params"), $(this), "Vous &ecirc;tes sur le point de supprimer cet article.<br>. Voulez-vous confirmer ?", null, "Supprimer l''article");
	});
	$(document).off('click', '.del-mnu');
	$(document).on('click', '.del-mnu', function(){
		showConfirmDeleteBox('<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.supprimerCmd")%>', $(this).attr("params"), $(this), "Vous &ecirc;tes sur le point de supprimer ce menu.<br>. Voulez-vous confirmer ?", null, "Supprimer le menu");
	});
	$(document).off('click', '.popover-content a');
	$(document).on('click', '.popover-content a', function(){
		$(".popover").each(function(){
			$(this).remove();
		});
	});	
	$(document).off('click', '#annul_cmd_main');
	$(document).on('click', '#annul_cmd_main', function(){
		$("#targ_link").attr("targetDiv", "main-div");
		showConfirmDeleteBox($(this).attr("act"), "tp=annul", $("#targ_link"), "Cette commande sera annul&eacute;e.<br>Voulez-vous confirmer ?", null, "Annulation commande");
	});
	$(document).off('click', '.mnu_td');
	$(document).on('click', '.mnu_td', function(e){
		var tp = $(this).attr("tp");
		if(tp == 'CLI'){
			$("#targ_link").attr("targetDiv", "left-div");
			$("#targ_link").attr("wact", '<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.selectGroupe")%>').attr("params", $(this).closest("tr").attr("par")).trigger("click");
		} else if(tp == 'UPD'){
			$("#cmd-table tr[sel]").removeAttr("sel").css("background-color", "");
			$(this).closest("tr").css("background-color", "");
			//
			$("#targ_link").attr("targetDiv", "menu-detail-div");
			$("#targ_link").attr("wact", '<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.selectLigneMenu")%>').attr("params", $(this).closest("tr").attr("par")).trigger("click");
		} else if(tp == 'DEL'){
			$("#targ_link").attr("targetDiv", "left-div");
			var eventBack = $(this).closest("tr").attr("par").indexOf("tp=MENU") == -1 ? null : '$("#home_lnk").trigger("click")';
			showConfirmDeleteBox('<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.deleteRow")%>', $(this).closest("tr").attr("par"), $("#targ_link"), "Cette ligne sera supprim&eacute;e.<br>Voulez-vous confirmer ?", eventBack, "Suppression ligne");
		} else if(tp == 'COM'){
			$("#targ_link").attr("targetDiv", "left-div");
			$("#targ_link_pop").attr("wact", '<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.initCommentaireCommande")%>').attr("params", $(this).closest("tr").attr("par")).trigger("click");
		}
	});
	var barcode="";
    $(document).keydown(function(e) {
        var code = (e.keyCode ? e.keyCode : e.which);
        var sourceEvent = $(e.target).prop('nodeName');
        var isInput = (sourceEvent == 'INPUT') ? true : false;
        
        if(!isInput && code==13 && $.trim(barcode) != ''){
        	<%-- Correction bug caracteres caches --%>
        	if(barcode.length >= 10){
        		e.preventDefault();
        		$("#log-lnk").attr("params", 'tkn='+barcode).trigger("click");
        	}
        	barcode="";
        } else{
  			 barcode = barcode + String.fromCharCode(code);
        }
    });
});
</script>
	</head>
<!-- /Head -->

<!-- Body -->
<body>
	<std:link id="log-lnk" action="caisse-web.caisseWeb.loginCmd" targetDiv="paie-div" style="display:none;" />
	
	 <script src="resources/framework/js/keyboard/my_keyboard.js?v=<%=StrimUtil.getGlobalConfigPropertie("version.resources")%>"></script>
    <jsp:include page="/commun/keyboard-popup.jsp" />
    <jsp:include page="/commun/keyboard-popup-num.jsp" />
		
    <div class="main-container container-fluid" id="main-div" style="overflow: hidden;">
    	<jsp:include page="/domaine/caisse_restau/client-place/bigin-cmd.jsp"></jsp:include>
     </div>
     
    <jsp:include page="/WEB-INF/fragment/footer-resources.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/fragment/static-panels.jsp"/>
    
  </body>
    
</html>