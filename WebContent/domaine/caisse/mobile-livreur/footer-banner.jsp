<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>
<%@page import="framework.controller.ControllerUtil"%>

<%
boolean isShowHisto = StringUtil.isTrueOrNull(""+ControllerUtil.getUserAttribute("RIGHT_HISTOCAIS", request));
%>

<div id="banner_foot_act" style="padding-left: 4px;">
			<std:link
					action="caisse.livreurMobile.loadCommandes" classStyle=""
					tooltip="Les commandes" style="background-color:orange;"
					params="tp=LOAD_A" targetDiv="right-div">
						<i class="fa fa fa-home"></i> CMD
					</std:link> 
			<%if(isShowHisto){ %>			
			<std:link action="caisse.livreurMobile.loadCommandes" classStyle=""
						params="tp=LOAD_H"
						targetDiv="right-div" 
						tooltip="Historique des commandes">
						<i class="fa fa fa-home"></i> HISTORIQUE
			</std:link>
			<%} %>
			<std:link action="caisse.livreurMobile.loadDashBoard" classStyle=""
						targetDiv="right-div" 
						tooltip="Synthèse des chiffres">
						<i class="fa fa fa-home"></i> SYNTHESE
			</std:link>
			<%if(ContextAppli.getUserBean().getIsMultiMobileEnv()){%>
				<std:link style="color:#3f51b5;text-align: left;" action="caisse.clientMobile.loadApp" targetDiv="right-div" classStyle="" tooltip="APPLICATIONS">
					 <i class="fa fa-building"></i> APP
				</std:link>
			  <%} %>
			<a href="javascript:" class="btn btn-default btn-lg shiny" id="delogLiv_lnk" 
					style="color: red;text-align: left;font-weight: bold;position: absolute;right: 0px;padding-left: 12px;">
					<i class="fa fa-sign-out"></i>
			</a>
</div>

