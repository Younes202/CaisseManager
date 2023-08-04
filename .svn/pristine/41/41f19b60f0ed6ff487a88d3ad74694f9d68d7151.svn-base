<%@page import="framework.model.common.util.BooleanUtil"%>
<%@page import="framework.model.beanContext.EtablissementPersistant"%>
<%@page import="framework.model.common.service.MessageService"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="appli.model.domaine.administration.service.IUserService"%>
<%@page import="framework.model.util.audit.ReplicationGenerationEventListener"%> 
<%@page import="framework.model.service.IGenericJpaService"%>
<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.model.common.util.DateUtil"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.ServiceUtil"%>
<%@page import="framework.model.util.FileUtil"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@page import="framework.controller.Context"%>   
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>

<%
EtablissementPersistant etsBean = ContextGloabalAppli.getEtablissementBean();
if(etsBean == null){
	etsBean = new EtablissementPersistant();
}
%>

<style>
#liLnks a:HOVER{
	text-decoration: underline;
	font-weight: bold;
	color: black !important;
}
@keyframes blink {
  0% {
    opacity: 1;
  }
  50% {
    opacity: 0;
  }
}
a.blink {
  animation: blink 1s infinite steps(1);
}
.navbar .navbar-inner .navbar-header .navbar-account .account-area>li.open>a{
	background-color: <%=etsBean.getThemeDet("fond_top")%> !important;
}
.sidebar-menu>li.open>a{
	background-color: <%=etsBean.getThemeDet("fond_mnu1_select")%> !important;
}
.page-sidebar .sidebar-menu .submenu>li.open>a{
	background-color: <%=etsBean.getThemeDet("fond_mnu2_select")%> !important;
}
.page-sidebar .sidebar-menu .submenu li.active>a{
	background-color: <%=etsBean.getThemeDet("fond_mnu3_select")%> !important;
}


@media only screen and (max-device-width: 800px) {
	#logo_nav img{
		height: 50px !important;
	    margin-top: -3px !important;
	    left: 35px !important;
	}
	.navbar-fixed-top, .navbar-header.pull-right{
		height: 20px !important;
	}
	#client_nav{
	    font-size: 18px !important;
	    left: 73px !important;
    	background-color: black !important;
    	padding-left: 0px !important;
	}
	.tab-content{
		padding-left: 6px !important;
   		padding-right: 0px !important;
	}
	#logo_cli_nav{
		display: none !important;
	}
	#btn-setting{
		display: none !important;
	}
	.databox .databox-number.number-xxlg {
   		font-size: 22px !important;
	}
	.main-container{
		margin-left: -9px !important;
   		margin-right: 8px !important;
	}
	.with-header{
		padding-left: 0px !important;
		margin-left: -5px !important;
   		margin-right: 0px !important;
	}
	.with-header.row{
		margin-left: 0px !important;
	}
	#fav_nav{
		line-height: 8px !important;
	    position: absolute !important;
	    top: -42px !important;
	    width: 70px !important;
	    height: 23px !important;
	    left: -103px !important;
	    padding-top: 4px !important;
	}
	#mnu_nav{
	    position: absolute !important;
    	top: -46px !important;
    	right: -35px !important;
	}
	#li_zoom_nav{
		display: none !important;
	}
	#generic_modal_body{
		width: 100% !important;
    	margin-left: 0px !important;
	}
	.page-container{
		margin-top: -100px;
	}
	#generic_modal_body .modal-body{
		margin-right: 15px !important;
    	margin-left: 6px !important;
	}
	.col-lg-12{
	    padding-left: 2px !important;
	    padding-right: 3px !important;
	}
	.col-md-12{
	    padding-left: 2px !important;
	    padding-right: 3px !important;
	}
	#zoom_div_id{
		z-index: 9999;
		position: fixed;
	    top: 33px;
	    left: 146px;
	    width: 100px;
	    background-color: orange;
	    border-radius: 10px;
	    color: red;
	    opacity: 0.6;
	    display: block !important;
	    text-align: center;" 
	}
	#sidebar-nav{
		position: absolute;
	    left: 5px;
	    color: white;
	    top: 0px;
	    font-size: 26px;
	    display: block !important;
	}
	#tabMnuNav{
		display: none !important;
	}
	.tab-add{
		display: none !important;
	}
	.row {
   		margin-right: -23px !important;
   		margin-left: -6px !important;
	}
	.tabbable .row{
		margin-left: -2px !important;
	}
	.header-title{
		display: block !important;
	}
	.page-header{
	    margin-left: -9px !important;
   		margin-top: 40px !important;
	}
	.flexigrid{
		overflow-x: auto !important;
		margin-left: 7px !important;
	}
	.flexigrid div[id^='corp_']{
		width: fit-content !important;
	}
	.flexigrid div[id^='scroll_']{
		overflow-x: auto !important;
	}
	.flexigrid table[id$='_body']{
	    width: max-content;
	}
	#sidebar{
		position: fixed !important;
		top: 50px !important;
		left: 0px !important;
		background-color: white !important;
	}
	.header-buttons{
		display: none !important;
	}
	.spinner {
   		right: 42px !important;
   		bottom: 120px !important;
	}
}
</style>

<%
Integer nbrDayOut = (Integer)ControllerUtil.getUserAttribute("CHECK_DIFF_DAYS", request);
%>

<script type="text/javascript">
	function callUrlMaj(){
		callBackJobAjaxUrl('front?w_f_act=<%=EncryptionUtil.encrypt("commun.login.triggerMajApplication") %>');
		
		$("#upd-div-1").hide();
		$("#upd-div-2").show(1000);
		
		callUrlReload(false);
	}
	
	var idxTry = 0;
	function callUrlReload(isRecall){
		if(idxTry > 5){
			return;
		}
		setTimeout(function() {
			$.ajax({  // ajax
				url: "front?", // url de la page charger
				cache: false, // pas de mise en cache
				success:function(html){ // si la requete est un succes
					location.reload();
				},
				error:function(XMLHttpRequest, textStatus, errorThrows){ // erreur durant la requete
					callUrlReload(true);
				}
			});
			idxTry++;
			
		}, (isRecall ? 7000 : 15000));	
	}

	$(document).ready(function (){
		$("#zoom_slct").change(function(){
			writeLocalStorage('zoom_back_cock', $(this).val());
			manageZoom($(this).val());
		});
		var zoomCook = readLocalStorage('zoom_back_cock');
		if(zoomCook && zoomCook!=null && zoomCook!=''){
			manageZoom(zoomCook);
			$("#zoom_slct").val(zoomCook);
		}
		
		<%if(nbrDayOut != null){%>
			setTimeout(function(){
				$("a.blink").attr("class", "dropdown-toggle");
			}, 10000); 
		
			setTimeout(function(){
				$("#abn-lnk-href").trigger("click");
				$(".modal-backdrop").css("z-index", "0");
				
				var count;
				  var sec = <%=nbrDayOut<15 ? 30 : 55%>;
				  count = setInterval(function() {
				    document.getElementById("abn_span_count").innerHTML = sec;
				    sec--;
				    if (sec <= 0) {
				    	clearInterval(count);
				    	$("#abn_close_pop").trigger("click");
				    }
				  }, 1000);
				  
				  
			}, 1000); 
		<%}%>
		
		<%
		if(!ReplicationGenerationEventListener._IS_CLOUD_SYNCHRO_INSTANCE){
			if(ControllerUtil.getUserAttribute("IS_NEW_VERSION", request) != null){%>
				setTimeout(function(){
					$("#upd-lnk-href").trigger("click");
					$(".modal-backdrop").css("z-index", "0");
				}, 1000); 
			<%
				ControllerUtil.removeUserAttribute("IS_NEW_VERSION", request);
			}
		}
		%>
	});
	function manageZoom(val){
		$("html").css("zoom", val).css("-moz-transform", "scale("+val+")").css("-moz-transform-origin", "0.0");
	}
</script>

<input type="hidden" id="mnu_sel_color" value="<%=etsBean.getThemeDet("fond_mnu1_select") %>">
<input type="hidden" id="txt_sel_color" value="<%=etsBean.getThemeDet("txt_mnu1") %>">

<div class="navbar-inner" style="box-shadow: 0px 5px 22px #888;height: 45px;background-color: <%=etsBean.getThemeDet("fond_top") %>;color:<%=etsBean.getThemeDet("txt_top") %>;">
            <div class="navbar-container"> 
                <!-- Navbar Barnd -->
                <div class="navbar-header pull-left">
                    <a href="#" class="navbar-brand" id="logo_nav">
                    	<img alt="Caisse Manager" src="resources/<%=StrimUtil.getGlobalConfigPropertie("context.soft") %>/img/logo_caisse_red.png?v=1.10<%=StrimUtil.getGlobalConfigPropertie("version.resources")%>" onerror="this.onerror=null;this.remove();" style="height: 40px;margin-top: 0px;position: absolute;left: 16px;" />
                    </a>
                </div>
                <!-- /Navbar Barnd -->
                <!-- Sidebar Collapse -->
                <div id="sidebar-nav" style="display: none;">
                    <i class="collapse-icon fa fa-bars"></i>
                </div>
                <%
                IUserService mouvementService = (IUserService)ServiceUtil.getBusinessBean(IUserService.class);
                if(etsBean.getId() != null){
	                 Map<String, byte[]> imagep = mouvementService.getDataImage(etsBean.getId(), "restau");
	                 if(imagep.size() > 0){
	                %>
							<img id="logo_cli_nav" src="data:image/jpeg;base64,<%=FileUtil.getByte64(imagep.entrySet().iterator().next().getValue())%>" alt="Caisse manager" style="height: 43px;margin-left: 15%;" />                        
	                 <%
	                  } 
                }
                 %>
                 <span id="client_nav" style="bottom: 1px;font-size:16px; font-style: italic;color: <%=etsBean.getThemeDet("txt_top") %>;padding-left: 130px;position: absolute;text-transform: uppercase;line-height: 47px;">
                 	<%=StringUtil.getValueOrEmpty(etsBean.getRaison_sociale()) %>
                 </span>
                 
                <!-- /Sidebar Collapse -->
                <!-- Account Area and Settings --->
                <div class="navbar-header pull-right">
                    <div class="navbar-account">
                    	<div id="notif_content">
                        	
                        </div>
                        <ul class="account-area">
                        	<%
								String favoris_nav = (ContextAppli.getUserBean() != null ? ContextAppli.getUserBean().getFavoris_nav() : null); 
								favoris_nav = (favoris_nav==null ? "" : favoris_nav);
								String[] favArray = StringUtil.getArrayFromStringDelim(favoris_nav, ";");
							%>
                        	<%if(favArray != null && favArray.length > 0){ %>
	                        	 <li id="fav_nav" style="line-height: 44px;
									    padding-right: 2px;
									    border: 1px solid white;
									    border-radius: 10px;
									    margin-right: 10px;">
									<a href="javascript:" style="color: <%=etsBean.getThemeDet("txt_top") %>;padding-right: 4px;" onclick="$('#cn-button').trigger('click');">
										 <i style="color: #fb6e52;" class="fa fa-fw fa-star"></i> Favoris
									</a>
								</li>	
                        	<%} %>
                        	<%if(ReplicationGenerationEventListener._IS_CLOUD_SYNCHRO_INSTANCE
                        			&& ContextAppli.getUserBean().isInProfile("ADMIN")) {%> 
                        	<li style="line-height: 44px;
								    padding-right: 2px;
								    border: 1px solid white;
								    border-radius: 10px;
								    margin-right: 10px;">
								    
								<std:linkPopup action="admin.societe.changer_ets" style='<%="color:"+etsBean.getThemeDet("txt_top") %>' icon="fa-filter" classStyle="" value="Ets" />
                        	</li> 
                        <%} %>        
                        
                        <%
                        	if(nbrDayOut != null){
                        %>
                        	<li>
                        		<a class=" dropdown-toggle blink" data-toggle="dropdown" title="Expiration abonnement" href="#" aria-expanded="false">
                                    <i class="icon fa fa-warning" style="color: orange;font-size: 34px;"></i>
                                </a>
                        	<ul class="pull-right dropdown-menu dropdown-arrow dropdown-notifications">
                                    <li>
                                        <a href="#">
                                            <div class="clearfix">
                                                <div class="notification-icon">
                                                    <i class="fa fa-warning bg-themeprimary white"></i>
                                                </div>
                                                <div class="notification-body">
                                                    <span class="title">Abonnement expir&eacute; depuis <%=nbrDayOut%> jours.</span>
                                                    <span class="description" style="font-size: 12px;">Votre logiciel va être <b style="color: red;">désactivé</b> prochainement</b></span>
                                                </div>
                                            </div>
                                        </a>
                                    </li>
                                    <li class="dropdown-footer ">
                                        <span>
                                            Merci de r&eacute;gler votre abonnement.
                                        </span>
                                    </li>
                                </ul>
                        	</li>
                       <%
                       	}
                       %> 
                       
                       		<li id="li_manager" style='display:<%=MessageService.getGlobalMap().get("IS_ADMIN_MODE")==null?"none":"" %>;padding-right: 4px;padding-left: 5px;border: 1px solid #fcfcfc;border-radius: 4px;margin-right: 6px;background-color: #ffc107;text-align: center;'>
                       			<span style="color: red;font-size: 20px;">
                       				MODE ADMIN
                       			</span>
                       			<br>
                       			<std:link icon="fa-unlock-alt" action="admin.user.releaseActCmd" onComplete="$('#li_manager').hide(1000);" classStyle="" style="left: -18px;color: #1c1b1a;padding-left: 32px;position: absolute;top: 24px;text-decoration: underline;" value="Quitter ce mode" />
                       		</li>
                       
                       		<li id="li_zoom_nav">
                       			<i class="fa fa-search-plus fa-1" style="margin-right: -17px;color:<%=etsBean.getThemeDet("txt_top") %>;"></i> 
	                       		<select id="zoom_slct" style="background-color: transparent;margin-top: 5px;border: 0px;color:<%=etsBean.getThemeDet("txt_top") %> !important;">
					      			<option value="1" style="color: black;">100%</option>
					      			<option value="0.9" style="color: black;">90%</option>
					      			<option value="0.8" style="color: black;">80%</option>
					      			<option value="0.7" style="color: black;">70%</option>
					      			<option value="0.6" style="color: black;">60%</option>
					      			<option value="0.5" style="color: black;">50%</option>
				      			</select>
                       		</li>
                            <li id="mnu_nav">
                                <a class="login-area dropdown-toggle" data-toggle="dropdown">
                                    <div class="avatar" title="View your public profile">
                                        <img src="resources/framework/img/avatars/adam-jansen.jpg">
                                    </div>
                                    <section>
                                        <h2>
                                        	<span class="profile">
                                        		<span style="color:<%=etsBean.getThemeDet("txt_top") %>;"><%=Context.getUserLogin()%></span>
                                        	</span>
                                        </h2>
                                    </section>
                                    <section>
                                    	&nbsp;&nbsp;[<span style="font-size: 11px;color:<%=etsBean.getThemeDet("txt_top") %>;">V <%=StringUtil.getValueOrEmpty(etsBean.getVersion_soft()) %></span>] 
                                    </section>
                                    <section>	
                                    	<%
                                    	String stl = "font-size: 16px;margin-left: 6px;color: "+etsBean.getThemeDet("txt_top")+";position: absolute;top: 9px;right: 0px;";
                                    	%>
                                    	<std:linkPopup icon="fa fa-info-circle" classStyle="" action="commun.login.load_histo_maj" style="<%=stl %>" />
                                    </section>
                                </a>
                                <!--Login Area Dropdown-->
                                <ul class="pull-right dropdown-menu dropdown-arrow dropdown-login-area">
                                    <li class="username"><a><%=Context.getUserLogin()%></a></li>
<!--                                     <li class="theme-area"> -->
<!--                                         <ul class="colorpicker" id="skin-changer"> -->
<!--                                             <li><a class="colorpick-btn" href="#" style="background-color:#5DB2FF;" rel="resources/framework/css/skins/blue.min.css"></a></li> -->
<!--                                             <li><a class="colorpick-btn" href="#" style="background-color:#2dc3e8;" rel="resources/framework/css/skins/azure.min.css"></a></li> -->
<!--                                             <li><a class="colorpick-btn" href="#" style="background-color:#03B3B2;" rel="resources/framework/css/skins/teal.min.css"></a></li> -->
<!--                                             <li><a class="colorpick-btn" href="#" style="background-color:#53a93f;" rel="resources/framework/css/skins/green.min.css"></a></li> -->
<!--                                             <li><a class="colorpick-btn" href="#" style="background-color:#FF8F32;" rel="resources/framework/css/skins/orange.min.css"></a></li> -->
<!--                                             <li><a class="colorpick-btn" href="#" style="background-color:#cc324b;" rel="resources/framework/css/skins/pink.min.css"></a></li> -->
<!--                                             <li><a class="colorpick-btn" href="#" style="background-color:#AC193D;" rel="resources/framework/css/skins/darkred.min.css"></a></li> -->
<!--                                             <li><a class="colorpick-btn" href="#" style="background-color:#8C0095;" rel="resources/framework/css/skins/purple.min.css"></a></li> -->
<!--                                             <li><a class="colorpick-btn" href="#" style="background-color:#0072C6;" rel="resources/framework/css/skins/darkblue.min.css"></a></li> -->
<!--                                             <li><a class="colorpick-btn" href="#" style="background-color:#585858;" rel="resources/framework/css/skins/gray.min.css"></a></li> -->
<!--                                             <li><a class="colorpick-btn" href="#" style="background-color:#474544;" rel="resources/framework/css/skins/black.min.css"></a></li> -->
<!--                                             <li><a class="colorpick-btn" href="#" style="background-color:#001940;" rel="resources/framework/css/skins/deepblue.min.css"></a></li> -->
<!--                                         </ul> -->
<!--                                     </li> -->
<!--                                     /Theme Selector Area -->
                                    <li class="dropdown-footer" style="text-align: left;" id="liLnks">
                                    <%
                                    String codeAuth = etsBean.getCode_authentification();
                                    String maxCode = "";
                                    if(codeAuth != null){
                                    	maxCode = (codeAuth.length() > 6) ? codeAuth.substring(0, 6) : codeAuth.substring(0, codeAuth.length());
                                    }
                                    
                                    if(codeAuth != null){ %>
                                    	<span style="font-size: 11px;">Date &eacute;ch&eacute;ance : <b style="color: #0072c6;padding-left:10px;"><%=StringUtil.getValueOrEmpty(DateUtil.dateToString(etsBean.getTarget_endDecrypt()))%></b></span>
                                    	<br><span style="font-size: 11px;">Date version : <b style="color: #0072c6;padding-left:10px;"><%=StringUtil.getValueOrEmpty(DateUtil.dateToString(etsBean.getDate_soft()))%></b></span>
	                                    <br>
	                                    <span style="font-size: 11px;">
	                                    	Clé : <b title=" <%=etsBean.getNom() %>/<%=codeAuth %>|<%=StrimUtil.getGlobalConfigPropertie("db.name") %>" style="color: #0072c6;padding-left:10px;">
	                                    			<%=maxCode %>...
	                                    		</b>
	                                    </span>
	                                    <%if(ReplicationGenerationEventListener._IS_CLOUD_SYNCHRO_INSTANCE){ %>
	                                    	<br><span style="font-size: 11px;">Mode serveur : <b style="color:fuchsia;">CLOUD MASTER</b></span>
	                                    <%} else if(ReplicationGenerationEventListener._IS_LOCAL_SYNCHRO_INSTANCE){ %>
	                                    	<br><span style="font-size: 11px;">Mode serveur : <b style="color: blue;">LOCAL SLAVE</b></span>
	                                    <%} %>
	                                  <%} %>
	                                    
	                                    <hr style="margin: 6px;">
                                        <std:linkPopup action="admin.user.init_changerPw" icon="fa fa-unlock-alt" actionGroup="C" value="Changer pw"/> 
                                    
                                        <a href="#lmnu=lgo" style="text-decoration: underline;color: red;position: absolute;right: 5px;">
                                           <i class="fa fa-power-off"></i> D&eacute;connexion
                                        </a>
                                    </li>
                                </ul>
                                <!--/Login Area Dropdown-->
                            </li>
                            <!-- /Account Area -->
                            <!--Note: notice that setting div must start right after account area list.
                            no space must be between these elements-->
                            <!-- Settings -->
                        </ul>
                        <div class="setting">
                            <a id="btn-setting" title="Options d'affichage" href="#">
                                <i class="icon glyphicon glyphicon-cog" style="color:<%=etsBean.getThemeDet("txt_top") %>;"></i>
                            </a>
                        </div>
                        <div class="setting-container">
                            <label>
                                <input type="checkbox" id="checkbox_fixednavbar">
                                <span>Fixer Navbar</span>
                            </label>
                            <label>
                                <input type="checkbox" id="checkbox_fixedsidebar">
                                <span>Fixer SideBar</span>
                            </label>
                            <label>
                                <input type="checkbox" id="checkbox_fixedbreadcrumbs">
                                <span>Fixer historique</span>
                            </label>
                            <label>
                                <input type="checkbox" id="checkbox_fixedheader">
                                <span>Fixer ent&ecirc;te</span>
                            </label>
                        </div>
                        <!-- Settings -->
                    </div>
                </div>
                <!-- /Account Area and Settings -->
            </div>
        </div>
	        
  <a href="javascript:" id="upd-lnk-href" data-toggle="modal" data-backdrop="static" data-keyboard="false" data-target="#update_div" style="display: none;"></a>
  <div id="update_div" class="modal modal-message modal-warning fade" style="display: none;z-index: 99999" aria-hidden="true">
     <div class="modal-dialog">
         <div class="modal-content">
             <div class="modal-header">
                 <i class="fa fa-warning"></i>
             </div>
             <div class="modal-title">Nouvelle version disponible</div>
             <div class="modal-body">
	            <div class="row">
	                <div class="col-md-12">
	                	Une nouvelle version du logiciel est disponible. Cliquer sur ce lien pour la lancer.
	                	<br><br>
	                	<a class="btn btn-info" href="javascript:callUrlMaj();">Installer la version <i class="fa fa-download right"></i></a>
	                	<br>
	                </div>
	                <div class="col-md-12" id="upd-div-2" style="display: none;">
	                	Merci de patienter environ <b style="color: blue;">5 &agrave; 10 min</b>.<br>
	                	 La page va se recharger automatiquement &agrave; la fin de la mise &agrave; jour.<br><br>
	                	
	                	Si au bout de 10 minutes, la page ne s'est pas recharg&eacute;e, alors recharger manuellement cette page.<br>
	                	<br><br>
	                	<span style="margin-top: 10px;color: orange;margin-left: 20%;font-style: italic;font-weight: bold;">
	                      	<i style="color: green;" class="fa fa-download"></i> Veuillez patienter ... <img src='resources/framework/img/ajax-loader.gif' />
	                    </span>
	                </div>
	            </div>
	        </div>
	        <div class="modal-footer">
                 <button type="button" class="btn btn-warning" data-dismiss="modal">Fermer</button>
             </div>
	     </div>
	  </div>
	</div>	     
	
	<div id="zoom_div_id" style="display: none;">
       	<a href="javascript:" onclick="manageResaZoom('-');" style="font-size: 20px;">
       		<i class="fa fa-minus"></i>
       	</a>
       	<span id="resa_zoom" val="1" style="font-size: 10px;">ZOOM</span>
       	<a href="javascript:" onclick="manageResaZoom('+');" style="font-size: 20px;">
       		<i class="fa fa-plus"></i>
       	</a>
    </div>
         
    <script src="resources/framework/js/toastr/toastr.js?v=1.0"></script>
        