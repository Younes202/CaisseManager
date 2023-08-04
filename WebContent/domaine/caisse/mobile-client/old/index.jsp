<%@page import="appli.model.domaine.administration.service.IParametrageService"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.beanContext.EtablissementPersistant"%>
<%@page import="java.util.List"%>
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
		<script type="text/javascript" src="resources/framework/js/autoNumeric.js"></script> 
		
		<link rel="stylesheet" type="text/css" href="domaine/caisse/mobile-client/caisse-mob-client.css?v=<%=StrimUtil.getGlobalConfigPropertie("version.resources")%>11">
		<link rel="stylesheet" type="text/css" href="domaine/caisse/mobile-client/caisse-mob-layout.css?v=<%=StrimUtil.getGlobalConfigPropertie("version.resources")%>11">
		
		 <link href="resources/framework/js/contextmenu/jquery.contextMenu.min.css" rel="stylesheet" type="text/css" />
		 <script src="resources/framework/js/contextmenu/jquery.contextMenu.min.js" type="text/javascript"></script>
	 	<script src="resources/framework/js/contextmenu/jquery.ui.position.min.js" type="text/javascript"></script>
	 	<script src="resources/framework/js/mobile_util.js?v=1.0" type="text/javascript"></script>

	<%
		ControllerUtil.cleanAll(request);
	%>	
		
<script type="text/javascript">

	$(document).ready(function (){
		// Manage floating mnu --------------------
		$(document).mouseup(function(e){
			var container = $("#mnu_r_top");
		    if (!container.is(e.target) && container.has(e.target).length === 0){
		       $("#mnu_r_top").hide(100);
		    }
		});
		$("#mnu_r_top a").click(function(){
			$("#mnu_r_top").hide(100);
		});
		// -----------------END--------------------
	    $(document).on('click', 'a[id="del_cli_lnk"]', function(){
	    	$("#targ_link").attr("targetDiv", "left-div");
			showConfirmDeleteBox($(this).attr("act"), $(this).attr("params"), $("#targ_link"), "Ce client ainsi que ses articles seront supprimés.<br>Voulez-vous confirmer ?", null, "Suppression client");
	    });
		$(document).on('click', '#annul_cmd_main', function(){
			$("#targ_link").attr("targetDiv", "left-div");
			showConfirmDeleteBox($(this).attr("act"), "tp=annul", $("#targ_link"), "Cette commande sera annulée.<br>Voulez-vous confirmer ?", "$('#home_lnk').trigger('click');", "Annulation commande");
		});
		
		$("#delogCli_lnk").click(function(){
			if(typeof Android !== 'undefined' && Android){Android.sendData("logout");}
			showConfirmDeleteBox('<%=EncryptionUtil.encrypt("caisse.clientMobile.logout")%>', null, $("#targ_link"), "Vous &ecirc;tes sur le point de vous d&eacute;connecter.<br>Voulez-vous confirmer ?", null, "Quitter la session");
		});
		
	});
	
	function managerFooterBanner(){
		$("#back_btn, #up_btn").hide();
	}
	function setAndroidToken(token){
		setMobileToken(token);
	}
	
</script>		
	</head>
<!-- /Head -->

<!-- Body -->
<body>

<%
	IParametrageService paramsService = ServiceUtil.getBusinessBean(IParametrageService.class);
	EtablissementPersistant restauP = paramsService.getEtsOneOrCodeAuth();

	  boolean isImg = false;
	  if(restauP.getId() != null){
		  String path = "restau/fond/"+restauP.getId();
		  Map<String, byte[]> imagep = FileUtil.getListFilesByte(path);
	      if(imagep.size() > 0){ %>
			<div style="background-image: url(data:image/jpeg;base64,<%=FileUtil.getByte64(imagep.entrySet().iterator().next().getValue())%>);position: absolute;width: 100%;height: 100%;top: 0px;background-size: 100%;"></div>                        
	    <%
	    	isImg = true;
	     } 
	  }
	  if(!isImg){ %>
		<div style="background-image: url(resources/restau/img/restaurant-691377_1920.jpg);position: absolute;width: 100%;height: 100%;top: 0px;background-size: 100%;"></div>
	<%} %>


<menu id="html5menu" style="display:none" class="showcase">
  	
</menu>

	<a href="javascript:" id="targ_link" targetDiv="left-div"></a>
	<a href="javascript:" id="targ_link_pop" targetdiv="generic_modal_body" data-toggle="modal" data-target="#generic_modal"></a>

	<div class="row">
		<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
	</div>
			
    <!-- Main Container -->
    <div class="main-container container-fluid">
        <!-- Page Container -->
        <div class="page-container">
			<jsp:include page="/domaine/caisse/mobile-client/old/mnu_top_caisse_mobile.jsp" />
			
			<div class="page-body">
				<div class="widget">
					<std:form name="data-form">
				         <div class="widget-body" style="padding: 1px;" id="widget-body">
							<div style="float: left;
								width:100%;
								background-color: white;
								padding-left: 0px;
								padding-right: 0px;<%=CaisseWebBaseAction.GET_STYLE_CONF("PANEL_RIGHT", null)%>" id="main-ets-div">
								
								<input type="hidden" name="qte_calc" id="qte_calc">
							    <!-- Parie gauche -->
							    <div id="left-div">
							    	<jsp:include page="commande-detail.jsp" />
								</div>
								
								<!-- Panneau droit -->
								<div id="right-div">
									<jsp:include page="caisse-right-bloc.jsp"></jsp:include>
								</div>
									
								<jsp:include page="footer-banner.jsp"></jsp:include>
							    
							    <span id="span_pers_up" style="position: absolute;z-index: 999;right: 30px;width: 150px;top: 29px;white-space: nowrap;">
							    	<span id="span_pers"></span>
							    </span>
    
    						</div>
						</div>
					</std:form>
				</div>
			</div>
        </div>
        <!-- /Page Container -->
    </div>
    
    <!-- Main Container -->
    <jsp:include page="/WEB-INF/fragment/footer-resources.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/fragment/static-panels.jsp"/>	
	
  </body>
    
</html>
