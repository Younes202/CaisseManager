<%@page import="appli.model.domaine.vente.persistant.CaissePersistant"%>
<%@page import="java.util.List"%>
<%@page import="appli.controller.domaine.caisse.ContextAppliCaisse"%>
<%@page import="framework.model.common.util.DateUtil"%>
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
List<CaissePersistant> listCaisse = (List<CaissePersistant>)request.getAttribute("listCaisseCuisine");
%>	
	
<!-- Head -->	
	<head>
		<title>Pilotage cuisine [<%=ContextAppliCaisse.getCaisseBean().getReference()%>]</title>
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
    		 	font-size: 12px;
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
    		 	font-size: 11px;
    		 	color: blue;
    		 	font-weight: normal;
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
    		 	vertical-align: sub;
    		 }
    		 #cmd-table tr{
    		 	height: 23px;
    		 }
		</style>
		
		<script type="text/javascript">
		
			function getWindowPilRatioZoom(){
				var ratioW = 1;
				var ratioH = 1;
				var dataZoom = readLocalStorage('zoom_pil_cock');
				if(dataZoom && dataZoom!=null && dataZoom!='' && dataZoom!='1'){
					if(dataZoom == '0.9'){
						ratioW = 9;
						ratioH = 9;
					} else if(dataZoom == '0.8'){
						ratioW = 4;
						ratioH = 4;
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
		
			function manageZoom(val){
				$("html").css("zoom", val).css("-moz-transform", "scale("+val+")").css("-moz-transform-origin", "0.0");
			}
			//
			$(document).ready(function (){
				$(document).off('click', '.lnk_mnu').on('click', '.lnk_mnu', function(){
					if($(this).find(".icon_exp").attr("class").indexOf("fa fa-plus-square-o") != -1){
						$(this).find(".icon_exp").attr("class", "icon_exp fa fa-minus-square-o");
					} else{
						$(this).find(".icon_exp").attr("class", "icon_exp fa fa-plus-square-o");
					}
					$("tr[id='tr_"+$(this).attr("mnu")+"']").toggle();
				});
				$(document).off('click', '.zoomDiv').on('click', '.zoomDiv', function(){
					$("#zoomLnk").attr("params", $(this).attr("params")).trigger('click'); 
				});
				$(document).off('change', '#zoom_slct').on('change', '#zoom_slct', function(){
					writeLocalStorage('zoom_pil_cock', $(this).val());
					manageZoom($(this).val());
				});
				var zoomCook = readLocalStorage('zoom_pil_cock');
				if(zoomCook && zoomCook!=null && zoomCook!=''){
					manageZoom(zoomCook);
					$("#zoom_slct").val(zoomCook);
				}
				
				$(document).off('change', '#caisse_slct').on('change', '#caisse_slct', function(){
					submitAjaxForm('<%=EncryptionUtil.encrypt("caisse-web.cuisinePilotage.loadCommande")%>', '', $("#data-form"), $("#generic_id"));
				});
				$(document).off('change', '#tp_cmd').on('change', '#tp_cmd', function(){
					submitAjaxForm('<%=EncryptionUtil.encrypt("caisse-web.cuisinePilotage.loadCommande")%>', 'tpCmd='+$(this).val(), $("#data-form"), $("#generic_id"));
				});

				
				//
				setTimeout(function(){ $("#cmd-valide").trigger("click"); }, 100); 
				
				<%Integer delaisRefresh = (Integer)ControllerUtil.getUserAttribute("DELAIS_REFRESH_ECRAN_SECONDE", request);
				if(delaisRefresh != null){%>
					setInterval(function() {
						submitAjaxForm('<%=EncryptionUtil.encrypt("caisse-web.cuisinePilotage.loadCommande")%>', '', $("#data-form"), $("#generic_id"));
					  }, <%=ControllerUtil.getUserAttribute("DELAIS_REFRESH_ECRAN_SECONDE", request)%>);
				<%}%>
				
				$("#pagger_slct").change(function(){
					writeLocalStorage('pagger_cu', $(this).val());
					alertify.success("Le nombre de pas est mise &agrave; jour.");
				});
				var pagger_cu = readLocalStorage('pagger_cu');
				if(!pagger_cu || pagger_cu==''){
					pagger_cu = "3";
				}
				$("#pagger_slct").val(pagger_cu);
				
				// Afficher cacher les lignes
				$("#collapseTrLnk").click(function(){
					var valCollapse = $("#collapse_cu").val();
					if(valCollapse == "no"){
						valCollapse = "on";
					} else{
						valCollapse = "no";
					}
					$("#collapse_cu").val(valCollapse);
					writeLocalStorage('collapse_cu', valCollapse);
					alertify.success("Le mode d'affichage est actualis&eacute;.");
				});
				var collapse_cu = readLocalStorage('collapse_cu');
				if(!collapse_cu || collapse_cu==''){
					collapse_cu = "no";
				}
				$("#collapse_cu").val(collapse_cu);
				
				// Afficher cacher menu
				$("#collapseTrMnu").click(function(){
					var valCollapse = $("#collapse_cuMnu").val();
					if(valCollapse == "no"){
						valCollapse = "on";
					} else{
						valCollapse = "no";
					}
					$("#collapse_cuMnu").val(valCollapse);
					writeLocalStorage('collapse_cuMnu', valCollapse);
					alertify.success("Le mode d'affichage est actualis&eacute;.");
				});
				var collapse_cuMnu = readLocalStorage('collapse_cuMnu');
				if(!collapse_cuMnu || collapse_cuMnu==''){
					collapse_cuMnu = "no";
				}
				$("#collapse_cuMnu").val(collapse_cuMnu);
				
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
				
				submitAjaxForm('<%=EncryptionUtil.encrypt("caisse-web.cuisinePilotage.loadCommande")%>', '', $("#data-form"), $("#generic_id"));
			});
			
			function refreshSize(){
// 				var widowHeight = $(window).height();
// 				$("#corp-div").css("height", (widowHeight-30)+"px");
			}
		</script>
	</head>
<!-- /Head -->
<!-- Body -->
	
<body>
	<a href="javascript:" id="generic_id" targetDiv="corp-div"></a>

<std:form name="data-form">
    <!-- Main Container -->
    <div class="main-container container-fluid">
    	<input type="hidden" name="collapse_cuMnu" id="collapse_cuMnu">
    	<input type="hidden" name="collapse_cu" id="collapse_cu">
    	
        <!-- Page Container -->
        <div class="page-container" style="overflow: hidden;">
			<!-- Page Breadcrumb -->
				<div class="page-header position-relative" style="position: relative;left: 0px;top: 0px;background: #262626;height: 42px;">
					<div class="header-title" style="padding-top: 4px;">
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
                             <li style="float: left;" class="divider"></li>
                             <li style="float: left;">
                                 <a class="refresh" href="javascript:" title="Afficher/Cacher les autres statuts" id="collapseTrMnu">
						      		<i class="fa fa-plus-square-o" style="font-size: 20px;"></i>
						      		Afficher/cacher menus
						      	 </a>
                             </li>
                             <li style="float: left;width: 100%;" class="divider"></li>
				      		 <li style="float: left;width: 100%;">
				      		 	<std:link classStyle="refresh" style="color:red;" action="commun.login.disconnect" targetDiv="corp-div" icon="fa fa-lock" tooltip="Quitter l'&eacute;cran" value="Quitter l'&eacute;cran" />
				      		 </li>
                         </ul>
                     </div>
					
						|
				        <span style="font-size: 19px;font-weight: bold;color: #a1c2dd;margin-left: 10px;margin-right:10px; font-style: italic;">Pilotage cuisine [<%=ContextAppliCaisse.getCaisseBean().getReference()%>]</span>
			      		<select id="pagger_slct" name="pagger_slct" style="background-color: #eeeeee;margin-left: 20px;">
				        	<option>6</option>
				        	<option>4</option>
				        	<option>3</option>
				        	<option>2</option>
				        </select>
				        | <span style="color: white!important;">Zoom </span>
				        <select id="zoom_slct" name="zoom_slct" style="background-color: transparent;color: #03A9F4 !important;">
			      			<option value="1">100%</option>
			      			<option value="0.9">90%</option>
			      			<option value="0.8">80%</option>
			      			<option value="0.7">70%</option>
			      			<option value="0.6">60%</option>
			      			<option value="0.5">50%</option>
			      		</select>
			      		<span style="color: #ffeb3b;padding-left: 10px;"><%=(ContextAppliCaisse.getJourneeBean()!=null?DateUtil.dateToString(ContextAppliCaisse.getJourneeBean().getDate_journee()):"")%></span>
			      		
			      		| <span style="color: white!important;">Type </span>
			      		<select id="tp_cmd" name="tp_cmd" style="background-color: transparent;color: #03A9F4 !important;">
			      			<option></option>
			      			<option value="E">A emporter</option>
			      			<option value="L">Livraison</option>
			      		</select>	
				      		
			      	</div>
			      <!--Header Buttons-->
			      <div class="header-buttons">
				     <a class="refresh" id="refresh-toggler" href="javascript:void(0);" wact="<%=EncryptionUtil.encrypt("caisse-web.cuisinePilotage.loadCommande")%>" targetDiv="corp-div">
				         <i class="glyphicon glyphicon-refresh"></i>
				     </a>
				     <a class="fullscreen" id="fullscreen-toggler" href="#">
				         <i class="glyphicon glyphicon-fullscreen"></i>
				     </a>
				 </div>
			      <!--Header Buttons End-->
			  </div>
			  <!-- /Page Header -->
			
			<div class="row" style="position: absolute;
					    top: 8px;
					    z-index: 999999;
					    right: 100px;
					    font-size: 18px;">
				<std:link action="caisse-web.cuisinePilotage.loadCommande" params="fltr=1&cai=A" style="color: #118df0;
    padding-right: 24px;
    text-transform: uppercase;
    font-weight: bold;" classStyle="" value="Tous" onComplete="$('#refresh-toggler').trigger('click');"/>
				<%for(CaissePersistant caiP : listCaisse){ %>
					<std:link action="caisse-web.cuisinePilotage.loadCommande" params='<%="fltr=1&cai="+caiP.getId() %>' style="color: #118df0;
    padding-right: 24px;
    text-transform: uppercase;
    font-weight: bold;" classStyle="" value="<%=caiP.getReference() %>" onComplete="$('#refresh-toggler').trigger('click');" />
				<%} %>
			</div>

			<div class="page-body" style="margin: 0px;overflow:auto;" id="corp-div">
				
			</div>
        </div>
        <!-- /Page Container -->
    </div>
    <!-- Main Container -->
 </std:form>   
    <jsp:include page="/WEB-INF/fragment/footer-resources.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/fragment/static-panels.jsp"/>
    
  </body>
    
</html>