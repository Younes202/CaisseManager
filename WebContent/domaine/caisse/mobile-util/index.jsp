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
		
		<link rel="stylesheet" type="text/css" href="resources/caisse/css/caisse.css?v=<%=StrimUtil.getGlobalConfigPropertie("version.resources")%>15">
		 <link href="resources/framework/js/contextmenu/jquery.contextMenu.min.css" rel="stylesheet" type="text/css" />
		 <script src="resources/framework/js/contextmenu/jquery.contextMenu.min.js" type="text/javascript"></script>
	 	<script src="resources/framework/js/contextmenu/jquery.ui.position.min.js" type="text/javascript"></script>
	
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
		$(document).ready(function (){
			refreshSize();
			
			$("#fullscreen-toggler").click(function(){
				setTimeout(function(){
					refreshSize();
				}, 1000);
			});

			var doit;
			window.onresize = function(){
			  clearTimeout(doit);
			  doit = setTimeout(refreshSize, 100);
			};
			
			$("#delogUtil_lnk").click(function(){
				if(typeof Android !== 'undefined' && Android){Android.sendData("logout");}
				showConfirmDeleteBox('<%=EncryptionUtil.encrypt("caisse.boUtilMobile.logout")%>', null, $("#targ_link"), "Vous &ecirc;tes sur le point de vous d&eacute;connecter.<br>La commande en cours sera <b>perdue</b>. Voulez-vous confirmer ?", null, "Quitter la caisse");
			});
		});
		
		function getWindowRatioZoom(){
			var ratioW = 1;
			var ratioH = 1;
			var dataZoom = readLocalStorage('zoom_cai_cock');
			if(dataZoom && dataZoom!=null && dataZoom!='' && dataZoom!='1'){
				if(dataZoom == '0.9'){
					ratioW = 9;
					ratioH = 9;
				} else if(dataZoom == '0.85'){
					ratioW = 5.6;
					ratioH = 5.6;
				} else if(dataZoom == '0.8'){
					ratioW = 4;
					ratioH = 4;
				} else if(dataZoom == '0.75'){
					ratioW = 3;
					ratioH = 3;
				} else if(dataZoom == '0.7'){
					ratioW = 2.35;
					ratioH = 2.35;
				} else if(dataZoom == '0.6'){
					ratioW = 1.5;
					ratioH = 1.5;
				} else if(dataZoom == '0.5'){
					ratioW = 1;
					ratioH = 1;
				}
			}
			var ratioArray = [ratioW, ratioH];
			
			return ratioArray;
		}
		
		function refreshSize(){
			var dataZoom = readLocalStorage('zoom_cai_cock');
			if(dataZoom && dataZoom!=null && dataZoom!='' && dataZoom!='1'){
				var ratioArray = getWindowRatioZoom();
				$("#zoom_slct").val(dataZoom);
			} else{
				dataZoom = 0.9;
			}
			// Css
			manageZoom(dataZoom);
			
			var widowHeight = $(window).height();
			var widowWidth = $(window).width();
			// Right bloc
			$("#tab_det_fam").css("height", (widowHeight-130)+"px");
			$("#tab_det_fam").css("width", (widowWidth+40)+"px");
			// Commande bloc
			$("#left-div").css("height", (widowHeight-80)+"px");
			$("#right-div").css("height", (widowHeight-90)+"px");
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
			<jsp:include page="/domaine/caisse/mobile-util/mnu_top_caisse_mobile.jsp" />
			
			<div class="page-body" style="position:fixed; margin: 0px;padding: 0px;margin-top: 50px !important;    width: 100%;">
				<div class="widget">
				<std:form name="data-form">
					<input type="hidden" name="qte_calc" id="qte_calc">
					
			         <div class="widget-body" style="padding: 1px;">
			         	<!-- Parie gauche -->
			         	<div style="position:fixed;width:100%;display:none;z-index:99;" id="left-div">
						</div>
						
						<!-- Panneau droit -->
						<div style="float: left;width:100%;overflow:auto;background-color: #e5e5e5;padding-left: 5px;padding-right: 2px;" id="right-div">
							<div class="col-lg-6 col-sm-6 col-xs-12" style="margin-top: 20%;">
								<a class="btn btn-default bloc-btn" href="javascript:void(0);"><i class="fa fa-plus"></i> INVENTAIRE</a>
							</div>
							<div class="col-lg-6 col-sm-6 col-xs-12">
								<a class="btn btn-default bloc-btn" href="javascript:void(0);"><i class="fa fa-plus"></i> ARTICLE</a>
							</div>
							<div class="col-lg-6 col-sm-6 col-xs-12">
								<a class="btn btn-default bloc-btn" href="javascript:void(0);"><i class="fa fa-plus"></i> BALANCE</a>
							</div>
							<div class="col-lg-6 col-sm-6 col-xs-12">
								<a class="btn btn-default bloc-btn" href="javascript:void(0);"><i class="fa fa-plus"></i> DECLARER PERTE</a>
							</div>	
						</div>
							
						<jsp:include page="/domaine/caisse/mobile-util/footer-banner.jsp"></jsp:include>
						
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