<!-- Page Breadcrumb -->
<%@page import="appli.model.domaine.vente.persistant.CaisseMouvementPersistant"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli.STATUT_JOURNEE"%>
<%@page import="framework.model.common.util.DateUtil"%>
<%@page import="appli.model.domaine.personnel.persistant.EmployePersistant"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="appli.controller.domaine.caisse.ContextAppliCaisse"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="appli.controller.domaine.caisse.action.CaisseWebBaseAction"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>

<style>
#lnk-mob-top a {
	line-height: 35px;
}
</style>
<div class="page-header position-relative page-header-fixed" style="height: 50px;left: 0px;top: -0px !important;<%=StringUtil.isEmpty(CaisseWebBaseAction.GET_STYLE_CONF("PANEL_ENETETE", null))?"background-image: linear-gradient(#fbfbfb, #ddd)":"background:"+ContextGloabalAppli.getGlobalConfig("PANEL_ENETETE")%>">
	<div class="header-title" style="padding-top: 4px;margin-left: -10px;">
		<div class="" id="lnk-mob-top" style="float:left;float: left;padding-left: 6px;padding-top: 4px;">
         <a class="" data-toggle="dropdown" aria-expanded="false">
             <i class="fa fa-reorder" style="font-size: 27px;"></i>
         </a>
         <ul class="dropdown-menu dropdown-azure">
            <li>
              <std:linkPopup classStyle="btn btn-default btn-lg shiny" style="color:#007bd5;text-align:left;" 
              		action="caisse-web.caisseWeb.init_opts" tooltip="Ajouter un article">
						<i class="fa fa-gears"></i> OPTIONS 
				</std:linkPopup>
		  	</li> 
	         <li>
	         	<a href="javascript:" id="delogUtil_lnk" class="btn btn-default btn-lg shiny" 
	         			style="color: red;text-align: left;font-weight: bold;" title="Quitter"> 
	         			<i class="fa fa-3x fa-sign-out"></i> DÉCONNEXION
	         		</a>
	         </li>
			 <li>
			 	<std:link style="color:#3f51b5;text-align: left;" id="app_lnk" action="caisse.clientMobile.loadApp" targetDiv="right-div" classStyle="" tooltip="APPLICATIONS">
						<i class="fa fa-building"></i> APP
				</std:link>
			</li>
			</ul>
           </div>
                       
          <div style="font-size: 10px;margin-left: 49px;">
             <span style="margin-right: 20px;">
	        	<i class="fa fa-street-view" style="color: red;font-size: 15px;"></i>
		        <%=ContextAppli.getUserBean().getLogin() %>
	        	</span>
           </div>
	
	
     	</div>
     <!--Header Buttons-->
     <div class="header-buttons">
     		<select id="zoom_slct" style="
     		background-color: transparent;
     		margin-top: 5px;    
     		margin-top: 2px;
	    height: 32px;
	    padding: 0px;
	    font-size: 11px;">
     			<option value="1">100%</option>
     			<option value="0.9">90%</option>
     			<option value="0.85">85%</option>
     			<option value="0.8">80%</option>
     			<option value="0.75">75%</option>
     			<option value="0.7">70%</option>
     			<option value="0.6">60%</option>
     			<option value="0.5">50%</option>
     		</select>
     
     <a class="refresh" id="refresh-toggler" href="javascript:" onclick="location.reload();" style="margin-top: -9px;">
         <i class="glyphicon glyphicon-refresh"></i>
     </a>
     <a class="fullscreen" id="fullscreen-toggler" href="javascript:" style="margin-top: -9px;">
         <i class="glyphicon glyphicon-fullscreen"></i>
     </a>
 </div>                          
 
 
     <!--Header Buttons End-->
 </div>
 <!-- /Page Header -->