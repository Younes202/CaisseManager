<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="appli.model.domaine.vente.persistant.CaisseMouvementPersistant"%>
<%@page import="appli.controller.domaine.caisse.ContextAppliCaisse"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>
<%@page import="framework.controller.ControllerUtil"%>

	<div id="banner_foot_act" style="float: left;width: 100%;height: 50px;position: fixed;bottom: 0px;background-color: #d1d1d1;border-radius: 23px;border: 1px solid #FF9800;">
			<std:link id="home_lnk" style="color: fuchsia;padding-top: 0px;padding-left: 7px;" classStyle="btn btn-default btn-circle btn-lg btn-menu shiny" action="caisse-web.caisseWeb.init_home" targetDiv="right-div" icon="fa-3x fa fa-home" value="" />
						
			<std:link id="up_btn" style="display:none;" action="caisse-web.caisseWeb.upButtonEvent" targetDiv="right-div" classStyle="btn btn-default btn-circle btn-lg btn-menu shiny" tooltip="Monter d'un niveau">
				<img src="resources/caisse/img/caisse-web/upload.png" />
			</std:link>
			
			<%if(ContextAppli.getUserBean().getIsMultiMobileEnv()){%>
					<std:link style="color:#3f51b5;text-align: left;" action="caisse.clientMobile.loadApp" targetDiv="right-div" classStyle="" tooltip="APPLICATIONS">
						 <i class="fa fa-building"></i> APP
					</std:link>
			  <%} %>
	</div>

