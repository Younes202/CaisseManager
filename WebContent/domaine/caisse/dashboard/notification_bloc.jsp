<%@page import="framework.model.common.util.DateUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.controller.Context"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Map"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>

<%
Map<Date, String> mapMessage = (Map<Date, String>) request.getAttribute("mapMessage");
Map<String, Map<String, Integer>> mapAlertes = (Map<String, Map<String, Integer>>) request.getAttribute("mapAlertes");
%>
					
<ul class="account-area">
                   	<li>
        <%
 		int nbr_total_msg = 0;
 		if(mapMessage != null && mapMessage.size() > 0){
	  		for (Date dt : mapMessage.keySet()) {
	  			nbr_total_msg++;
	  		}
 		}
 		%>
         <a class="dropdown-toggle wave in" id="chat-link" data-toggle="dropdown" title="Notifications" href="#" aria-expanded="false">
             <i class="icon glyphicon glyphicon-comment" style="font-size: 25px;"></i>
             <span class="badge" style="background-color: #ffeb3b66 !important;"><span style="color: green;font-size: 13px;font-weight: bold;"><%=nbr_total_msg %></span></span>
         </a>
         <!--Notification Dropdown-->
         <ul class="pull-right dropdown-menu dropdown-arrow dropdown-notifications">
         	<%
         	if(mapMessage != null && mapMessage.size() > 0){
         		for (Date dt : mapMessage.keySet()) {%>
<li style="padding-top:10px;padding-bottom:10px;">
                              <div class="clearfix">
                              		<div class="notification-icon">
                                       <i class="fa fa-comment bg-themeprimary white"></i>
                                   </div>
                                  <div class="notification-body">
<span class="title" style="font-weight: bold;"><%=DateUtil.dateToString(dt, "dd/MM/yyyy HH:mm") %></span>
                          <span class="description" style="font-size: 12px;color: black;"><%=mapMessage.get(dt) %></span>
                        </div>
                   </div>
           </li>
<%}
} else{%>
<li>
	<div class="clearfix">
		<div class="notification-body" style="padding:10px;">
			<span>Aucun message.</span>
		</div>
	</div>
</li>
<%} %>
			<li class="dropdown-footer ">
                 <span>
                     <%=DateUtil.getDefaultFormattedDate(new Date()) %>
                 </span>
                 <span class="pull-right">
                     
                 </span>
             </li>
         </ul>
         <!--/Notification Dropdown-->
     </li>
 	<li>
 		<%
 		
 		int nbr_total_alertes = 0;
 		if(mapAlertes != null && mapAlertes.size() > 0){
  		for (String partie_alert : mapAlertes.keySet()) {
  			Map<String, Integer> mapDetail = mapAlertes.get(partie_alert);
  			for (String alert_str : mapDetail.keySet()) {
  				nbr_total_alertes = nbr_total_alertes + mapDetail.get(alert_str);
  			}
  		}
 		}
 		%>
         <a class="dropdown-toggle" data-toggle="dropdown" title="Notifications" href="#" aria-expanded="false">
             <i class="icon fa fa-warning" style="color: #F44336;font-size: 25px;"></i>
             <span class="badge" style="background-color: #ffeb3b66 !important;"><span style="color: red;font-size: 13px;font-weight: bold;"><%=nbr_total_alertes %></span></span>
         </a>
         <!--Notification Dropdown-->
         <ul class="pull-right dropdown-menu dropdown-arrow dropdown-notifications">
         	<%
            if(mapAlertes != null && mapAlertes.size() > 0){
            	int nbr_all_alertes = 0;
				for (String partie_alert : mapAlertes.keySet()) {
					Map<String, Integer> mapDetail = mapAlertes.get(partie_alert);
					int nbr_alerte_detail = 0;
					String alerte_detail = "";
					for (String alert_str : mapDetail.keySet()) {
						nbr_alerte_detail = nbr_alerte_detail + mapDetail.get(alert_str); 
						if(StringUtil.isNotEmpty(alerte_detail)){
							alerte_detail = alerte_detail + "<br>";
						}
						
						alerte_detail = alerte_detail + mapDetail.get(alert_str) + " " + alert_str;
					}
					if(nbr_alerte_detail == 0){
						continue;
					}
					nbr_all_alertes = nbr_all_alertes + nbr_alerte_detail;
					%>
				<li>
					<a href="javascript:">
	                  <div class="clearfix">
	                      <div class="notification-icon">
	                          <i class="fa fa-warning bg-warning white"></i>
	                      </div>
	                      <div class="notification-body">
	                          <span class="title" style="font-weight: bold;"><%=partie_alert %></span>
	                          <span class="description">
								<span style="font-size: 11px;color: #dd940e;"><%=alerte_detail %></span>
	                          </span>
	                      </div>
	                   <div class="notification-extra">
	                          <span style="color:red;font-size: 16px;font-weight: bold;"><%=nbr_alerte_detail %></span>
	                      </div>
	                   </div>
                   </a>
                </li>
				<%}
				if(nbr_all_alertes == 0){%>
				<li>
					<div class="clearfix">
						<div class="notification-body" style="padding:10px;">
							<span>Aucune notification.</span>
						</div>
					</div>
				</li>
				<% }
				}%>
             <li class="dropdown-footer ">
                 <span>
                     <%=DateUtil.getDefaultFormattedDate(new Date()) %>
                 </span>
                 <span class="pull-right">
                     
                 </span>
             </li>
         </ul>
         <!--/Notification Dropdown-->
     </li>
 
 	 <%if(ControllerUtil.getUserAttribute("CHECK_DIFF_DAYS", request) != null){ %>
 	<li>
 		<a class=" dropdown-toggle" data-toggle="dropdown" title="Expiration abonnement" href="#" aria-expanded="false">
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
                             <span class="title">Abonnement expir&eacute; depuis <%=ControllerUtil.getUserAttribute("CHECK_DIFF_DAYS", request) %> jours.</span>
                             <span class="description" style="font-size: 12px;">Dur&eacute;e avant arr&ecirc;t : <b style="color: red;"><%=15-((Integer)ControllerUtil.getUserAttribute("CHECK_DIFF_DAYS", request)) %></b> jours</span>
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
<%} %> 
 <%
if(ControllerUtil.getUserAttribute("MAJ_DISPONIBLE", request) != null){%>
	 <li>
         <a class="dropdown-toggle" data-toggle="dropdown" title="Mise &agrave; jour disponible" href="#" aria-expanded="true">
             <i class="icon fa fa-download" style="color: red;font-size: 34px;"></i>
             <span class="badge">1</span>
         </a>
         <!--Tasks Dropdown-->
         <ul class="pull-right dropdown-menu dropdown-tasks dropdown-arrow ">
             <li class="dropdown-header bordered-darkorange">
  <a id="update-lnk" targetDiv="X" style="color: #ff4c00;" href="javascript:void(0)" act="<%=EncryptionUtil.encrypt("commun.login.updateApplication") %>">
  	<i class="fa fa-download" style="color: red;"></i> 
  	Mettre &agrave; jour l'application
  </a>
             </li>
         </ul>
         <!--/Tasks Dropdown-->
     </li>
<%} %>
     <li>
         <a class="login-area dropdown-toggle" data-toggle="dropdown">
             <div class="avatar" title="View your public profile">
                 <img src="resources/framework/img/avatars/adam-jansen.jpg">
             </div>
             <section>
                 <h2><span class="profile"><span><%=Context.getUserLogin() %></span></span></h2>
             </section>
             <section>
             	&nbsp;&nbsp;[<span style="font-size: 11px;color: #fff1a8;">Version <%=StringUtil.getValueOrEmpty(ContextAppli.getEtablissementBean().getVersion_soft())%></span>]
             </section>
         </a>
         <!--Login Area Dropdown-->
         <ul class="pull-right dropdown-menu dropdown-arrow dropdown-login-area">
             <li class="username"><a><%=Context.getUserLogin() %></a></li>
             <li class="theme-area">
                 <ul class="colorpicker" id="skin-changer">
                     <li><a class="colorpick-btn" href="#" style="background-color:#5DB2FF;" rel="resources/framework/css/skins/blue.min.css"></a></li>
                     <li><a class="colorpick-btn" href="#" style="background-color:#2dc3e8;" rel="resources/framework/css/skins/azure.min.css"></a></li>
                     <li><a class="colorpick-btn" href="#" style="background-color:#03B3B2;" rel="resources/framework/css/skins/teal.min.css"></a></li>
                     <li><a class="colorpick-btn" href="#" style="background-color:#53a93f;" rel="resources/framework/css/skins/green.min.css"></a></li>
                     <li><a class="colorpick-btn" href="#" style="background-color:#FF8F32;" rel="resources/framework/css/skins/orange.min.css"></a></li>
                     <li><a class="colorpick-btn" href="#" style="background-color:#cc324b;" rel="resources/framework/css/skins/pink.min.css"></a></li>
                     <li><a class="colorpick-btn" href="#" style="background-color:#AC193D;" rel="resources/framework/css/skins/darkred.min.css"></a></li>
                     <li><a class="colorpick-btn" href="#" style="background-color:#8C0095;" rel="resources/framework/css/skins/purple.min.css"></a></li>
                     <li><a class="colorpick-btn" href="#" style="background-color:#0072C6;" rel="resources/framework/css/skins/darkblue.min.css"></a></li>
                     <li><a class="colorpick-btn" href="#" style="background-color:#585858;" rel="resources/framework/css/skins/gray.min.css"></a></li>
                     <li><a class="colorpick-btn" href="#" style="background-color:#474544;" rel="resources/framework/css/skins/black.min.css"></a></li>
                     <li><a class="colorpick-btn" href="#" style="background-color:#001940;" rel="resources/framework/css/skins/deepblue.min.css"></a></li>
                 </ul>
             </li>
             <!--/Theme Selector Area-->
             <li class="dropdown-footer" style="text-align: left;" id="liLnks">
             	<span style="font-size: 11px;">Date &eacute;ch&eacute;ance : <b style="color: #0072c6;padding-left:10px;"><%=StringUtil.getValueOrEmpty(DateUtil.dateToString(ContextAppli.getEtablissementBean().getDate_ouverture())) %></b></span><br>
             	<span style="font-size: 11px;">Date version : <b style="color: #0072c6;padding-left:10px;"><%=StringUtil.getValueOrEmpty(DateUtil.dateToString(ContextAppli.getEtablissementBean().getDate_soft())) %></b></span>
              <hr style="margin: 6px;">
              <std:linkPopup icon="fa fa-info-circle" classStyle="" value="Historique des maj." action="commun.login.load_histo_maj" style="" />
              <hr style="margin: 6px;">
                 <std:linkPopup action="admin.user.init_changerPw" icon="fa fa-unlock-alt" actionGroup="C" value="Changer pw"/> 
             </li>
         </ul>
         <!--/Login Area Dropdown-->
     </li>
     <!-- /Account Area -->
     <!--Note: notice that setting div must start right after account area list.
     no space must be between these elements-->
     <!-- Settings -->
 </ul>