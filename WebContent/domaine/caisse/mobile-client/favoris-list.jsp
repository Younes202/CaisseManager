<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="appli.model.domaine.personnel.persistant.ClientPersistant"%>
<%@page import="framework.model.beanContext.EtablissementPersistant"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<%
List<EtablissementPersistant> listEts = (List<EtablissementPersistant>)request.getAttribute("listEts");
String etsFav = "";
if(ContextAppli.getUserBean() != null && ContextAppli.getUserBean().getOpc_client() != null){
	etsFav = ContextAppli.getUserBean().getOpc_client().getEts_fav();
	etsFav = (etsFav == null ? "" : etsFav);
}
boolean isPassed = false;
for(EtablissementPersistant etsP : listEts){
	if(etsFav.indexOf(";"+etsP.getId()+";") == -1){
		continue;
	}
	isPassed = true;
	%>
	<div>
		<std:button classStyle="btn-type btn btn-default" style="margin-top: 17%;" targetDiv="right-div" 
			action="caisse.clientMobile.manageFavoris" params="isF=1" workId='<%=etsP.getId().toString() %>' icon="fa fa-beer" value="<%=etsP.getRaison_sociale()%>" />
			<i style="color: green;" class="fa fa-heart"></i>
	</div>		
<%}

if(!isPassed){ %>
	<h2 style="text-align: center;margin-top: 20%;">Aucun favori ajouté.</h2>
<%} %>

