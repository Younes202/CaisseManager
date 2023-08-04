<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="appli.controller.domaine.caisse.ContextAppliCaisse"%>
<%@page import="framework.model.common.util.DateUtil"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="framework.controller.bean.message.GrowlMessageBean"%>
<%@page import="framework.model.common.service.MessageService"%>
<%@page import="java.util.List"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page errorPage="/commun/error.jsp"%>

<!DOCTYPE html>

<html lang="fr-fr" style="overflow:hidden;">	

<%
		// Purge
	ControllerUtil.cleanAll(request);
	
	boolean isInventaireEcran = StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("INVENTAIRE_ECRAN"));
	boolean isStatutConfEcran = StringUtil.isTrue(""+ControllerUtil.getUserAttribute("isValidationEcran", request))
			|| StringUtil.isTrue(""+ControllerUtil.getUserAttribute("isPreparationEcran", request));
	String statutsAutorisse = (String)ControllerUtil.getUserAttribute("ecran_statut", request);
	boolean isModeInventaire= StringUtil.isTrue(""+request.getAttribute("isModeInventaire"));
	%>	
	
<!-- Head -->	
	<head>
		<title>Cuisine [<%=ContextAppliCaisse.getCaisseBean().getReference()%>]</title>
		<meta name="description" content="">
		<jsp:include page="/WEB-INF/fragment/header-resources.jsp"></jsp:include>
		
		<style type="text/css">
			.col-lg-1, .col-lg-10, .col-lg-11, .col-lg-12, .col-lg-2, .col-lg-3, .col-lg-4, .col-lg-5, .col-lg-6, .col-lg-7, .col-lg-8, .col-lg-9, .col-md-1, .col-md-10, .col-md-11, .col-md-12, .col-md-2, .col-md-3, .col-md-4, .col-md-5, .col-md-6, .col-md-7, .col-md-8, .col-md-9, .col-sm-1, .col-sm-10, .col-sm-11, .col-sm-12, .col-sm-2, .col-sm-3, .col-sm-4, .col-sm-5, .col-sm-6, .col-sm-7, .col-sm-8, .col-sm-9, .col-xs-1, .col-xs-10, .col-xs-11, .col-xs-12, .col-xs-2, .col-xs-3, .col-xs-4, .col-xs-5, .col-xs-6, .col-xs-7, .col-xs-8, .col-xs-9{
				padding: 1px;
			}
			.border-paneaux{
			    background-color: #eee;
			    border: 1px solid #53a93f;
			    border-radius: 4px;
			 }
    		 /**Commandes*/
    		 .menu-root-style{
    		 	font-size: 14px;
    		 	background-color: #191919;
    			color: #fbfbfb;
    		 	font-weight: bold;
    		 }
    		 .menu-cat-style{
    		 	font-size: 14px;
    		 	background-color: #a1c2dd;
    			color: #fbfbfb;
    		 	font-weight: bold;
    		 }
    		 .menu-style{
    		 	font-size: 12px;
    		 	color: #d42b11;
    		 	font-weight: bold;
    		 }
    		 .group-style{
    		 	font-size: 10px;
    		 	color: black;
    		 	font-weight: normal; 
    		 }
    		 .group-style td{
    		 	padding-left: 15px;
    		 }
    		 .ligne-style{
    		 	font-size: 13px;
    		 	color: blue;
    		 	font-weight: bold;
    		 	background-color: #FFF9C4;
    		 }
    		 .ligne-style td{
    		 	padding-left: 30px;
    		 }
    		 /*Hors menu*/
    		 .famille-style{
    		 	font-size: 12px;
    		 	color: green;
    		 	font-weight: bold;
    		 }
    		 .ligne-fam-style{
    		 	font-size: 13px;
    		 	color: blue;
    		 	font-weight: bold;
    		 	background-color: #FFF9C4;
    		 }
    		 .ligne-fam-style td{
    		 	padding-left: 15px;
    		 }
    		 #cmd-table tr{
    		 	 border-bottom: 1px dotted #777;
    		 	 height: 12px;
    		 }
    		 #cmd-table td{
    		 	padding-right: 5px;
    		 }
    		 .menu-cat-style{
				background-color: black !important;
			}
		</style>
		
		<script type="text/javascript">
			var interval_id;
			function manageZoom(val){
				$("html").css("zoom", val).css("-moz-transform", "scale("+val+")").css("-moz-transform-origin", "0.0");
			}
			function stopTimer(){
				for (var i = 1; i <= interval_id; i++){
			        window.clearInterval(i);
				}
			}
			function startTimer(){
				interval_id = setInterval(function() {
					submitAjaxForm('<%=EncryptionUtil.encrypt("caisse-web.cuisine.loadCommande")%>', '', $("#data-form"), $("#generic_id"));
				  }, <%=ControllerUtil.getUserAttribute("DELAIS_REFRESH_ECRAN_SECONDE", request)%>);
			}	
			
			$(document).ready(function (){
				$("#disconnect").click(function(){
					stopTimer();
					
					<%if (isModeInventaire){%>
						showConfirmDeleteBox('<%=EncryptionUtil.encrypt("commun.login.disconnect")%>', null, $(this), "Attention: l'inventaire de cl&ocirc;ture n'est pas realis&eacute;!<br> Voulez-vous quitter votre session ?", null, "Confirmation de d&eacute;connexion");
					<%}else{%>
						submitAjaxForm('<%=EncryptionUtil.encrypt("commun.login.disconnect")%>', '', $("#data-form"), $("#generic_id"));
					<%}%>
				});
				
				<%if(isModeInventaire){%>
					setTimeout(function(){ 
						$("#load-inv").trigger("click");
						$("a[targetDiv='corp-div']").hide();
					}, 1000);
				<%}%>
				
				
				<%if(ControllerUtil.getMenuAttribute("IS_INV_MSG", request) != null){%>
						showPostAlert("", "<%=ControllerUtil.getMenuAttribute("IS_INV_MSG", request)%>" , "SUCCESS");
				<%ControllerUtil.removeMenuAttribute("IS_INV_MSG", request);
				}%>
				
				$("#load-inv").click(function(){
					stopTimer();
					submitAjaxForm('<%=EncryptionUtil.encrypt("stock.inventaire.work_init_create")%>', 'tp=CUIS', $("#data-form"), $(this));
				});
				
				$("#zoom_slct").change(function(){
					writeLocalStorage('zoom_cuis_cock', $(this).val());
					manageZoom($(this).val());
				});
				var zoomCook = readLocalStorage('zoom_cuis_cock');
				if(zoomCook && zoomCook!=null && zoomCook!=''){
					manageZoom(zoomCook);
					$("#zoom_slct").val(zoomCook);
				}
				
				setTimeout(function(){ $("#cmd-valide").trigger("click"); }, 100);
				
				<%Integer delaisRefresh = (Integer)ControllerUtil.getUserAttribute("DELAIS_REFRESH_ECRAN_SECONDE", request);
				if(delaisRefresh != null){%>
					startTimer();
				<%}%>
				
				$(".statutBtn").click(function(){
					if($(this).attr("id") != 'valid-cmds'){
						$("#curr-btn").remove();
						$(this).css("font-weight", "bold").prepend("<i id='curr-btn' style='color: red;' class='fa fa-hand-o-right'></i>");;
					}
				});
				$("#pagger_slct").change(function(){
					writeLocalStorage('pagger_cu', $(this).val());
				});
				$("#collapseTrLnk").click(function(){
					var valCollapse = $("#collapse_span").attr("val");
					if(valCollapse == "no"){
						valCollapse = "on";
					} else{
						valCollapse = "no";
					}
					$("#collapse_span").attr("val", valCollapse);
					writeLocalStorage('collapse_cuis', valCollapse);
					$("#collapse_cuis").val(valCollapse);
					alertify.success("Le mode d'affichage est actualis&eacute;.");
				});
				var collapse_cu = readLocalStorage('collapse_cu');
				if(!collapse_cu || collapse_cu==''){
					collapse_cu = "no";
				}
				var pagger_cu = readLocalStorage('pagger_cu');
				
				if(pagger_cu == null || pagger_cu == ''){
					pagger_cu = 4;
				}
				
				$("#pagger_slct").val(readLocalStorage('pagger_cu'));
				$("#collapse_span").attr("val", readLocalStorage('collapse_cuis'));
				$("#collapse_cuis").val(readLocalStorage('collapse_cuis'));
				
				// Commandes area height ----------------------------------
				refreshSize();
				//
				var doit;
				window.onresize = function(){
				  clearTimeout(doit);
				  doit = setTimeout(refreshSize, 100);
				};
				$("#fullscreen-toggler").click(function(){
					setTimeout(function(){
						refreshSize();
					}, 1000);
				});
				
				// Charger les commandes				
				submitAjaxForm('<%=EncryptionUtil.encrypt("caisse-web.cuisine.loadCommande")%>', '', $("#data-form"), $("#generic_id"));
				setTimeout(refreshSize, 1000);
			});
			
			function refreshSize(){
				var widowHeight = $(window).height();
				$("#corp-div").css("height", (widowHeight+20)+"px");
			}
		</script>
	</head>
<!-- /Head -->
<!-- Body -->
	
<body>
	<a href="javascript:" id="generic_id" targetDiv="corp-div"></a>

<std:form name="data-form">
	<input type="hidden" name="collapse_cuis" id="collapse_cuis">
    <!-- Main Container -->
    <div class="main-container container-fluid">
    	<span id="collapse_span" style="display: none;"></span>
        <!-- Page Container -->
        <div class="page-container" style="overflow: hidden;">
			<!-- Page Breadcrumb -->
				<div class="page-header position-relative" style="position: relative;left: 0px;top: 0px;background: #262626;height: 42px;">
					<div class="header-title" style="padding-top: 2px;">
						<div class="btn-group">
                         <a class="btn btn-default dropdown-toggle shiny" data-toggle="dropdown" href="javascript:void(0);"><i class="fa fa-angle-down"></i></a>
                         <ul class="dropdown-menu">
                             <li style="float: left;">
                                <a class="refresh" id="refresh-toggler" href="javascript:" onclick="location.reload();">
							         <i class="glyphicon glyphicon-repeat"></i>
							         Actualiser l'&eacute;cran
							     </a>
                             </li>
                             <li style="float: left;" class="divider"></li>
                             <li style="float: left;">
                                 <a class="refresh" href="javascript:" title="Afficher/Cacher les autres statuts" id="collapseTrLnk">
						      		<i class="fa fa-plus-square-o" style="font-size: 20px;"></i>
						      		Afficher/cacher articles
						      	 </a>
                             </li>
                             <li style="float: left;width: 100%;" class="divider"></li>
                             <%
                             	if(isInventaireEcran && ContextAppliCaisse.getCaisseBean().getOpc_stock_cible() != null){
                             %>
                             <li style="float: left;">
				      			 <std:link classStyle="refresh" style="color:green;" id="load-inv" targetDiv="corp-div" icon="fa fa-tags" value="R&eacute;aliser inventaire" tooltip="R&eacute;aliser inventaire" />
				      		</li>	
				      		 <%
					      		 	}
					      		 %>
				      		 <li style="float: left;width: 100%;">
				      		 	<std:link classStyle="refresh" style="color:red;" id="disconnect" icon="fa fa-lock" tooltip="Quitter l'&eacute;cran" value="Quitter l'&eacute;cran" />
				      		 </li>
                         </ul>
                     </div>
						|
				        <span style="font-size: 19px;font-weight: bold;color: #a1c2dd;margin-left: 10px;margin-right:10px; font-style: italic;">Cuisine [<%=ContextAppliCaisse.getCaisseBean().getReference()%>]</span>
				        
				        <%
				        				        	if(StringUtil.isEmpty(statutsAutorisse) || statutsAutorisse.indexOf(";V;") != -1){
				        				        %> 
				        <std:link classStyle="btn btn-yellow shiny statutBtn" style="color:white;" targetDiv="corp-div" action="caisse-web.cuisine.loadCommande" params="tp=val" id="cmd-valide" icon="fa-3x fa-cutlery" value="Valid&eacute;es" tooltip="Valid&eacute;es">
				        	 (<span id="nbr_validee_span" style="font-weight: bold;color: red;font-size: 14px;"></span>)
				        </std:link>
				        <%
				        	}
				        %>
				        <%
				        	if(StringUtil.isEmpty(statutsAutorisse) || statutsAutorisse.indexOf(";EP;") != -1){
				        %>
				        | <std:link classStyle="btn btn-magenta shiny statutBtn" style="color:white;" targetDiv="corp-div" action="caisse-web.cuisine.loadCommande" params="tp=enc" icon="fa-3x fa-history" value="En cours" tooltip="En cours" />
				        <%
				        	}
				        %>
				        <%
				        	if(StringUtil.isEmpty(statutsAutorisse) || statutsAutorisse.indexOf(";P;") != -1){
				        %>
				        | <std:link classStyle="btn btn-success shiny statutBtn" style="color:white;" targetDiv="corp-div" action="caisse-web.cuisine.loadCommande" params="tp=pre" icon="fa-3x fa-cutlery" value="Pr&ecirc;tes" tooltip="Pr&ecirc;tes" />
				        <%
				        	}
				        %>
				        
				     <%
				        				     	if(isStatutConfEcran){
				        				     %>   
				        
				        | <std:link classStyle="btn btn-default" style="color:green;margin-left:30px;background-color:#2dc3e8;" id="valid-cmds" targetDiv="corp-div" action="caisse-web.cuisine.changerStatut" icon="fa-3x fa-check" value="Statut" tooltip="Changer le statut des commandes" />
				      <%
				      	}
				      %>  
			      		 |
			      		 <select name="pagger_slct" id="pagger_slct" style="background-color: #eeeeee;margin-left: 20px;">
				        	<option>6</option>
				        	<option>4</option>
				        	<option>3</option>
				        	<option>2</option>
				        </select>
				        | 
				        <span style="color: white!important;">Zoom </span>
				        <select name="zoom_slct" id="zoom_slct" style="background-color: transparent;color: #90caf9 !important;">
			      			<option value="1">100%</option>
			      			<option value="0.9">90%</option>
			      			<option value="0.8">80%</option>
			      			<option value="0.7">70%</option>
			      			<option value="0.6">60%</option>
			      			<option value="0.5">50%</option>
			      		</select>
			      		<span style="color: #ffeb3b;padding-left: 10px;"><%=(ContextAppliCaisse.getJourneeBean()!=null?DateUtil.dateToString(ContextAppliCaisse.getJourneeBean().getDate_journee()):"")%></span>
			      	</div>
			      <!--Header Buttons-->
			      <div class="header-buttons">
				     <a class="refresh" id="refresh-toggler" href="javascript:void(0);" wact="<%=EncryptionUtil.encrypt("caisse-web.cuisine.loadCommande")%>" targetDiv="corp-div">
				         <i class="glyphicon glyphicon-refresh"></i>
				     </a>
				     <a class="fullscreen" id="fullscreen-toggler" href="javascript:">
				         <i class="glyphicon glyphicon-fullscreen"></i>
				     </a>
				 </div>
			      <!--Header Buttons End-->
			  </div>
			  <!-- /Page Header -->
			
			<div class="page-body" style="margin: 0px;margin-top:-20px; overflow:auto;overflow: hidden;" id="corp-div"> 
				
			</div>
        </div>
        <!-- /Page Container -->
    </div>
    <!-- Main Container -->
 </std:form>   
    <jsp:include page="/WEB-INF/fragment/footer-resources.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/fragment/static-panels.jsp"/>
    
    <script src="resources/framework/js/keyboard/my_keyboard.js?v=<%=StrimUtil.getGlobalConfigPropertie("version.resources")%>"></script>
    <jsp:include page="/commun/keyboard-popup.jsp" />
    <jsp:include page="/commun/keyboard-popup-num.jsp" />
    
  </body>
    
</html>