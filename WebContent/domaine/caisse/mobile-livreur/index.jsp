<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="appli.controller.domaine.caisse.action.CaisseWebBaseAction"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="java.util.Map"%>
<%@page import="framework.model.util.FileUtil"%>
<%@page import="framework.model.common.util.ServiceUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="appli.model.domaine.stock.service.IArticleService"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<!DOCTYPE html>
<html lang="fr-fr" style="overflow: hidden;">	
	<head>
		<title>Gestion de la caisse automatique</title>
		<jsp:include page="/WEB-INF/fragment/header-resources.jsp"></jsp:include>
		
		<link rel="stylesheet" type="text/css" href="domaine/caisse/mobile-livreur/caisse-mob-livreur.css?v=<%=StrimUtil.getGlobalConfigPropertie("version.resources")%>10">
		<link rel="stylesheet" type="text/css" href="domaine/caisse/mobile-livreur/caisse-mob-layout.css?v=<%=StrimUtil.getGlobalConfigPropertie("version.resources")%>10">
		
		 <link href="resources/framework/js/contextmenu/jquery.contextMenu.min.css" rel="stylesheet" type="text/css" />
		 <script src="resources/framework/js/contextmenu/jquery.contextMenu.min.js" type="text/javascript"></script>
	 	<script src="resources/framework/js/contextmenu/jquery.ui.position.min.js" type="text/javascript"></script>
	 	<script src="resources/framework/js/mobile_util.js?v=1.0" type="text/javascript"></script>
	 	
	
		<style type="text/css">
			.bloc-btn {
				height: 78px;
			    border-radius: 17px;
			    width: 98%;
			    line-height: 63px;
			    font-size: 26px;
			    background-color: #fff;
			    margin-bottom: 10px;
			    text-align: left;
    			padding-left: 20%;
			}
			.bloc-btn i{
				font-size: 26px !important;
			}
			
			.context-menu-root .off {
				color: green;
			}
			.context-menu-root .com{
				color: blue;
			}
			.context-menu-active{
				background-color: yellow;
			}
			.btn.btn-circle.btn-lg {
				width: 45px;
				height: 45px;	
			}
			#banner_foot_act img{
				width: 25px;
			}
		</style>
		
	<script type="text/javascript">
		var CMD_LIV_INTERVAL = 0;
	
		$(document).ready(function (){
			$("#delogLiv_lnk").click(function(){
				if(typeof Android !== 'undefined' && Android){Android.sendData("logout");}
				showConfirmDeleteBox('<%=EncryptionUtil.encrypt("caisse.livreurMobile.logout")%>', null, $("#targ_link"), "Vous &ecirc;tes sur le point de vous d&eacute;connecter.<br>La commande en cours sera <b>perdue</b>. Voulez-vous confirmer ?", null, "Quitter la caisse");
			});
			$("#banner_foot_act a").click(function(){
				$("#banner_foot_act a").css("background-color", "black");
				$(this).css("background-color", "orange");
			});
// 			setTimeout(() => {
// 				$("#banner_foot_act a").first().trigger("click");	
// 			}, 1000);
		});
		
		function setAndroidToken(token){
			setMobileToken(token);
		}

		</script>		
	</head>
<!-- /Head -->

<!-- Body -->
<body>

<menu id="html5menu" style="display:none" class="showcase">
  	
</menu>

	<input type="hidden" id="is_confirm_mngr"><%-- Confirm suppression lig,e commande. Alimenté depuis le détail --%>
	<a href="javascript:" id="targ_link" targetDiv="left-div"></a>
	<a href="javascript:" id="targ_link_pop" targetdiv="generic_modal_body" data-toggle="modal" data-target="#generic_modal"></a>
	<std:linkPopup action="caisse-web.caisseWeb.loadConfirmAnnule" id="del-cmd-lnk" style="display:none;" /> 

	<div class="row">
		<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
	</div>
			
    <!-- Main Container -->
    <div class="main-container container-fluid">
        <!-- Page Container -->
        <div class="page-container">
			<jsp:include page="/domaine/caisse/mobile-livreur/mnu_top_caisse_mobile.jsp" />
			
			<div class="page-body" style="position:fixed; margin: 0px;padding: 0px;margin-top: 50px !important;    width: 100%;">
				<div class="widget">
				<std:form name="data-form">
					<input type="hidden" name="qte_calc" id="qte_calc">
					
			         <div class="widget-body" style="padding: 1px;">
			         	<!-- Parie gauche -->
			         	<div style="position:fixed;width:100%;display:none;<%=CaisseWebBaseAction.GET_STYLE_CONF("PANEL_LEFT", null)%>;z-index:99;" id="left-div">
						</div>
						
						<!-- Panneau droit -->
						<div style="float: left;width:100%;overflow:auto;background-color: #e5e5e5;padding-left: 5px;padding-right: 2px;<%=CaisseWebBaseAction.GET_STYLE_CONF("PANEL_RIGHT", null)%>;" id="right-div">
							<jsp:include page="commande-list.jsp"></jsp:include>
						</div>
							
						<jsp:include page="/domaine/caisse/mobile-livreur/footer-banner.jsp"></jsp:include>
						
					</div>
				</std:form>
			</div>
			</div>
        </div>
        <!-- /Page Container -->
    </div>
    
    <span id="span_pers_up" style="position: absolute;z-index: 999;right: 30px;width: 150px;top: 29px;white-space: nowrap;">
    	<span id="span_pers"></span>
    </span>
    
    <!-- Main Container -->
    <jsp:include page="/WEB-INF/fragment/footer-resources.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/fragment/static-panels.jsp"/>	
	
  </body>
    
</html>