<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="appli.model.domaine.personnel.persistant.EmployePersistant"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="java.io.File"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="java.util.List"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="framework.model.util.FileUtil"%>
<%@page import="appli.model.domaine.stock.persistant.FamillePersistant"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<% 
List<FamillePersistant> listFamille = (List<FamillePersistant>)request.getAttribute("listFamille");
%>


<% if(listFamille != null && listFamille.size()>0){%>
<div style="float: left;width: 100%;overflow: hidden;">
<%
       	for(FamillePersistant familleP : listFamille){
       		String libelle = familleP.getLibelle().replaceAll("\\#", "");%>
	<std:link params="is_top=1" style='${caisseWeb.GET_STYLE_CONF("BUTTON_FAMILLE", "COULEUR_TEXT_FAMILLE")};' action="caisse-web.caisseWeb.familleEvent" targetDiv="menu-detail-div" workId="<%=familleP.getId().toString() %>" classStyle="caisse-top-btn famille-btn" value="">
		<span class="span-img-stl">
			<img alt="" src="resourcesCtrl?elmnt=<%=EncryptionUtil.encrypt(familleP.getId().toString()) %>&path=famille&rdm=<%=(familleP.getDate_maj()!=null?familleP.getDate_maj().getTime():"")%>" class="img-caisse-stl">
		</span>
		<% File directory = new File(StrimUtil._GET_PATH("famille")+"/"+familleP.getId());
 		if(directory.exists()){ %>
   			<span title="<%=libelle %>" class="span-libelle-oneLine"><%=libelle %>&nbsp;&nbsp;</span>
   		<%} else{ %>
   			<span class="span-libelle-stl"><%=libelle %>&nbsp;&nbsp;</span>
   		<%} %>
	</std:link>
	<%}%>
 </div>
  <%}%>

<%request.setAttribute("TP_P", "FAM"); %> 
 <jsp:include page="/domaine/caisse/normal/pagger_include.jsp" />
<jsp:include page="/commun/print-local.jsp" /> 