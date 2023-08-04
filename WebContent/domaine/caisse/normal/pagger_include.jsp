<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.controller.bean.PagerBean"%>
<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="appli.model.domaine.personnel.persistant.EmployePersistant"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>

<%
String src = (String)request.getAttribute("TP_P");
PagerBean pagerBean = (PagerBean) ControllerUtil.getUserAttribute("PAGER_BEAN_"+src, request);
String targetDiv = null;

if("FAM".equals(src)){
	targetDiv = "menu1";
} else if("MNU".equals(src)){
	targetDiv = "menu2";
} else if("FAV".equals(src)){
	targetDiv = "menu3";	
} else{
	targetDiv = "menu1"; 
}
%>

<div style="float: left;width: 100%;height: 55px;position: absolute;bottom: 0px;text-align: center;">
 	 <div style="width: 25%;float: left;font-size: 17px;font-weight: bold;text-align: left;">
 		<span style="line-height: 60px;text-transform: uppercase;">
        	<i class="fa fa-street-view" style="color: red;"></i>
        	<span id="userConSpan">
	        <%
	        EmployePersistant emplP = ContextGloabalAppli.getUserBean().getOpc_employe();
	        if(emplP != null){
	        %>
	        	<%=StringUtil.getValueOrEmpty(emplP.getNom()) %> <%=StringUtil.getValueOrEmpty(emplP.getPrenom()) %>
	        <%} %> 
	        </span>
        </span>
        |
        <i style="margin-left: 10px;color: red;" class="fa fa-clock-o"></i>
        <span style="color: #64b5f6;" id="time-part"></span>
 	</div>
 	<div style="width: 30%;float: left;">&nbsp;
<%if(pagerBean.getNbrPage() > 0){ %> 
	 <%if(pagerBean.getCurrPage() == 0){ %>
	 	<a href="javascript:" class="btn-next-stl" style="background-color: #cccccc;">
			<img src="resources/framework/img/back.png" style="width: 40px;">
		</a>
	 <%} else{ %>
	    <std:link params='<%="sens=PP&src="+src %>' targetDiv="<%=targetDiv %>" classStyle="btn-prev-stl" id="up_btn" action="caisse-web.caisseWeb.init_home">
			<img src="resources/framework/img/back.png" style="width: 40px;">
		</std:link>
	<%} %>	
		<span style="margin-left: 5px;margin-right: 5px;"><%=pagerBean.getPagerText() %></span>
		
	<%if(pagerBean.getCurrPage() == (pagerBean.getNbrPage()-1)){ %>	
		<a href="javascript:" class="btn-next-stl" style="background-color: #cccccc;">
			<img src="resources/framework/img/forward.png" style="width: 40px;">
		</a>
	<%} else{ %>
		<std:link params='<%="sens=NP&src="+src %>' targetDiv="<%=targetDiv %>" classStyle="btn-next-stl" action="caisse-web.caisseWeb.init_home">
			<img src="resources/framework/img/forward.png" style="width: 40px;">
		</std:link>
	<%} %>
<%}%>
	</div>
	<div style="width: 45%;float: left;padding-top: 30px;text-align: right;padding-right: 18px;">
		<%if(request.getAttribute("IS_P_PASSED") == null){ %>
			<div style="font-size: 12px;color: #9c27b0;text-transform: uppercase;float: left;" id="div_serveur"></div>
	        <div style="font-size: 12px;color: #9c27b0;text-transform: uppercase;float: left;" id="div_livreur"></div>
	        <div style="font-size: 12px;color: #9c27b0;text-transform: uppercase;float: left;" id="div_client_cmd"></div>
        <%} %>
        <%request.setAttribute("IS_P_PASSED", true); %>
        
        <img class="imgBarCode" src="resources/framework/img/barcode_scanner.png" style="width: 20px" title="Lecteur code barre utilisable sur cet écran">
        <img src="resources/framework/img/badge_scanner.png" style="width: 20px" title="Lecteur badge utilisable sur cet écran">
	</div>	
</div>  
