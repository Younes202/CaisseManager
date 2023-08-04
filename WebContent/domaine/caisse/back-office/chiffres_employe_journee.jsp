<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="appli.model.domaine.personnel.persistant.EmployePersistant"%>
<%@page import="appli.model.domaine.stock.service.impl.RepartitionBean"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="appli.model.domaine.vente.persistant.JourneePersistant"%>
<%@page import="framework.model.common.util.DateUtil"%>
<%@page import="framework.controller.Context"%>
<%@page import="java.math.BigInteger"%>
<%@page import="java.util.Arrays"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="appli.model.domaine.stock.persistant.FamillePersistant"%>
<%@page import="java.util.List"%>
<%@page import="appli.model.domaine.vente.persistant.CaisseMouvementArticlePersistant"%>
<%@page import="java.util.Map"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>
<%
	boolean isDetailJourneeDroit = Context.isOperationAvailable("DETJRN");
	Long journeeId = (Long)ControllerUtil.getMenuAttribute("journeeId", request);
%>
<style>
	#tab_rep tr:hover{
		background: #DBEDF3;
	}
	#tab_rep td {
		border-bottom: 1px dashed #ff9900;
	}
</style>

<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Gestion de la journ&eacute;</li>
		<li class="active">Chiffres par employ&eacute;</li>
	</ul>
</div>
<!-- /Page Breadcrumb -->
<!-- Page Header -->
<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
	<%if(ControllerUtil.getMenuAttribute("IS_DASH_JRN", request) == null || request.getAttribute("isMois") != null){ %>
		<std:link classStyle="btn btn-default" action="caisse.journee.work_find" params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la recherche" />
	<%} %>
	</div>
	<!--Header Buttons-->
	<jsp:include page="/WEB-INF/fragment/shortcut.jsp"></jsp:include>
	<!--Header Buttons End-->
</div>
<!-- /Page Header -->

<!-- Page Body -->
<div class="page-body">
	<div class="row">
		<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
	</div>

	<div class="widget">
	
	<%if(request.getAttribute("isMois") == null){ %>
		<div class="tabbable">
			<%request.setAttribute("curr_tab", "chiffre_empl"); %>
			<jsp:include page="journee_tab_header.jsp" />
		</div>	
	<%} %>
	
		<std:form name="search-form">
         <div class="widget-body">
			<div class="row">	
							
				<%
				boolean isMargeCaissier = StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("MARGE_CAISSIER"));
				Map<String, Map<String, RepartitionBean>> mapDataEmploye = (Map)request.getAttribute("mapDataEmploye");
				Map<String, RepartitionBean> data_employe = mapDataEmploye.get("data_employe");
				Map<String, RepartitionBean> data_livreur = mapDataEmploye.get("data_livreur");
				Map<String, RepartitionBean> data_serveur = mapDataEmploye.get("data_serveur");
				%>
				<div class="col-lg-4 col-sm-4 col-xs-12">
					<h3>CHIFFRES PAR CAISSIER</h3>
					<%for(String employe : data_employe.keySet()){ 
						RepartitionBean repBean = data_employe.get(employe);
					%>
				 
					<div class="col-lg-12 col-sm-12 col-xs-12">
						<div class="databox">
                             <div class="databox-left bg-white" style="min-height: 81px;">
                                 <div class="databox-sparkline">
                                     <img src="resources/caisse/img/caisse-web/user_male_white_blue_bald.png" style="width:45px; height:45px;">
                                 </div>
                             </div>
                             <div class="databox-right bg-white bordered bordered-platinum" style="min-height: 81px;">
                                 <span class="databox-number sky"><%=employe %></span>
                                 <div class="databox-text darkgray"><%=repBean.getNbrCmd() %> commandes</div>
                                 <div class="databox-stat bg-palegreen radius-bordered">
                                     <div class="stat-text"><%=BigDecimalUtil.formatNumberZero(repBean.getMontant()) %> Dhs</div>
                                 </div>
                               <%if(isMargeCaissier && !BigDecimalUtil.isZero(repBean.getMontantMargeCaissier())){ %>
                                 <div class="databox-text darkgray" style="margin-top: 10px;">
	                                 <%	 
	                                 String param = "user="+repBean.getElementId()+"&jour="+journeeId;
	                                 if(request.getAttribute("isMois") != null){
	                                 	 param = "user="+repBean.getElementId()+"&isMois=1";
	                                 }%> 
	                                 Commission de vente 
	                                 <std:linkPopup onClick="return false" classStyle="databox-stat bg-info radius-bordered" action="caisse.journee.edit_mvm_marge" params="<%=param %>" style="margin-top: -17px;" tooltip="Afficher plus de détails">
	                                 	<%=BigDecimalUtil.formatNumberZero(repBean.getMontantMargeCaissier()) %> Dhs
	                                 	<i class="fa fa-bars"></i>
	                                 </std:linkPopup>
                                 </div>
                                <%}%> 
                             </div>
                         </div>
	                 </div>
				<%} %>
				</div> 
				
				<div class="col-lg-4 col-sm-4 col-xs-12">
					<h3>CHIFFRES PAR LIVREUR</h3>
					<%for(String employe : data_livreur.keySet()){ 
						RepartitionBean repBean = data_livreur.get(employe);
					%>
				 
					<div class="col-lg-12 col-sm-12 col-xs-12">
						<div class="databox">
                             <div class="databox-left bg-white" style="min-height: 81px;">
                                 <div class="databox-sparkline">
                                     <i class="fa fa-motorcycle" style="color: #3F51B5;font-size: 36px;"></i>
                                 </div>
                             </div>
                             <div class="databox-right bg-white bordered bordered-platinum" style="min-height: 81px;">
                                 <span class="databox-number sky"><%=employe %></span>
                                 <div class="databox-text darkgray"><%=repBean.getNbrCmd() %> commandes</div>
                                 <div class="databox-stat bg-palegreen radius-bordered">
                                     <div class="stat-text"><%=BigDecimalUtil.formatNumberZero(repBean.getMontant()) %> Dhs</div>
                                 </div>
                             </div>
                         </div>
	                 </div>
				<%} %>
				</div> 
				
				<div class="col-lg-4 col-sm-4 col-xs-12">
					<h3>CHIFFRES PAR SERVEUR</h3>
					<%for(String employe : data_serveur.keySet()){
						RepartitionBean repBean = data_serveur.get(employe);
					%>
				 
					<div class="col-lg-12 col-sm-12 col-xs-12">
						<div class="databox">
                             <div class="databox-left bg-white" style="min-height: 81px;">
                                 <div class="databox-sparkline">
                                     <img src="resources/caisse/img/caisse-web/user.png" style="width:45px; height:45px;">
                                 </div>
                             </div>
                             <div class="databox-right bg-white bordered bordered-platinum" style="min-height: 81px;">
                                 <span class="databox-number sky"><%=employe %></span>
                                 <div class="databox-text darkgray"><%=repBean.getNbrCmd() %> commandes</div>
                                 <div class="databox-stat bg-palegreen radius-bordered">
                                     <div class="stat-text"><%=BigDecimalUtil.formatNumberZero(repBean.getMontant()) %> Dhs</div>
                                 </div>
                             </div>
                         </div>
	                 </div>
				<%} %>
				</div> 
			</div>
</div>
</std:form>
</div>
</div>