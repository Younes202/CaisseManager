<%@page import="framework.model.common.util.StringUtil"%>
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

<div style="margin-top: -1px;
    overflow-x: hidden;
    overflow-y: auto;">
<std:form name="data-form">
	<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">Mes établissements</span>
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row" style="margin-left: 2px;margin-right: 0px;">

<%
Map<String, List<EtablissementPersistant>> mapEts = (Map<String, List<EtablissementPersistant>>)request.getAttribute("mapEts");
if(mapEts != null){
	for(String domaineAppli : mapEts.keySet()){ %>
		<h2 style="margin-top: 26px;float: left;"><%=StringUtil.isEmpty(domaineAppli) ? "AUTRE":domaineAppli.toUpperCase() %></h2>
	<% 
	List<EtablissementPersistant> listEts = mapEts.get(domaineAppli);
	for(EtablissementPersistant etsP : listEts){ %>
		
			<std:link classStyle="" targetDiv="main-ets-div" 
				action="caisse.clientMobile.loadEts" 
				params='<%="etsId="+etsP.getId() %>' 
				icon="fa fa-beer" 
				value="<%=etsP.getRaison_sociale()%>"
				style="font-size: 22px;
    				width: 100%;
    				float: left;
    				padding-left: 20px;" />
  <%}
  }
}%>
</div>

	</div>
</div>
</std:form>
</div>