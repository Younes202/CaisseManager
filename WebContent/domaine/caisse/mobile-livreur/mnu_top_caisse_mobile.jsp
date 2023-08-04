<!-- Page Breadcrumb -->
<%@page import="framework.model.common.util.StrimUtil"%>
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

<img style="width: 94px;
    position: absolute;
    z-index: 99999;
    top: 11px;
    left: 1px;" src="resources/<%=StrimUtil.getGlobalConfigPropertie("context.soft") %>/img/logo_caisse_red.png?v=1.7" alt="Logo">

<div class="page-header position-relative page-header-fixed" style="height: 50px;left: 0px;top: -0px !important;<%=StringUtil.isEmpty(CaisseWebBaseAction.GET_STYLE_CONF("PANEL_ENETETE", null))?"background-image: linear-gradient(#fbfbfb, #ddd)":"background:"+ContextGloabalAppli.getGlobalConfig("PANEL_ENETETE")%>">
	<div class="header-title" style="padding-top: 4px;margin-left: -10px;">
          <div style="margin-left: 105px;margin-top: 12px;">
	        	<i class="fa fa-street-view" style="color: red;font-size: 15px;"></i>
		        <span style="font-size: 14px;"><%=ContextAppli.getUserBean().getLogin() %></span>
           </div>
     	</div>
     <!--Header Buttons-->
     <div class="header-buttons">
	     <a class="refresh" id="refresh-toggler" href="javascript:" onclick="location.reload();">
	         <i class="glyphicon glyphicon-refresh"></i>
	     </a>
	     <a class="fullscreen" id="fullscreen-toggler" href="javascript:">
	         <i class="glyphicon glyphicon-fullscreen"></i>
	     </a>
	     
		<std:link style="color:#3f51b5;text-align: left;" id="app_lnk" action="caisse.clientMobile.loadApp" targetDiv="right-div" classStyle="" tooltip="APPLICATIONS">
		 	<i class="fa fa-cubes"></i>
		</std:link>
	 </div>                          
 
 
     <!--Header Buttons End-->
 </div>
 <!-- /Page Header -->