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

<div id="banner_foot_act" style="
			float: left;width: 100%;
			height: 50px;
			position: fixed;
			text-align: center;
			bottom: 0px;
			background-color: #d1d1d1;
			border-radius: 23px;
			border: 1px solid #FF9800;">
	 
	<std:link style="color: #000000;padding-top: 0px;padding-left: 7px;" 
			classStyle="btn btn-default btn-circle btn-lg btn-menu shiny" 
			action="caisse.syntheseMobile.load_synthese" 
			targetDiv="right-div" 
			tooltip="Synthèse journée"
			icon="fa-3x fa fa-bar-chart" />

	<std:link style="color: #76008a;padding-top: 0px;padding-left: 7px;" 
			classStyle="btn btn-default btn-circle btn-lg btn-menu shiny" 
			action="caisse.syntheseMobile.load_histo_journee" 
			targetDiv="right-div" 
			tooltip="Historique des journées"
			icon="fa-3x fa fa-server" />

	<std:link style="color: #e84511;padding-top: 0px;padding-left: 7px;" 
			classStyle="btn btn-default btn-circle btn-lg btn-menu shiny" 
			action="caisse.syntheseMobile.load_situation" 
			targetDiv="right-div"
			tooltip="Situation financière"
			icon="fa-3x fa fa-area-chart" />
				
	<std:link style="color:#06700e;padding-top: 0px;padding-left: 7px;" 
			action="caisse.syntheseMobile.load_alerte" 
			classStyle="btn btn-default btn-circle btn-lg btn-menu shiny"
			targetDiv="right-div" 
			tooltip="Notifications"
			icon="fa-3x fa fa fa-bell" />
			
	<std:link style="color:blue;padding-top: 0px;padding-left: 4px;" 
		id="app_lnk" 
		action="caisse.clientMobile.loadApp" 
		targetDiv="right-div" 
		classStyle="btn btn-default btn-circle btn-lg btn-menu shiny" 
		tooltip="APPLICATIONS"
		icon="fa-3x fa fa fa-cubes" />
</div>

